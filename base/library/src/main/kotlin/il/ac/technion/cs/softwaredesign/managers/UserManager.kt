package il.ac.technion.cs.softwaredesign.managers

import il.ac.technion.cs.softwaredesign.storage.api.IUserManager.LoginStatus
import il.ac.technion.cs.softwaredesign.storage.api.IUserManager.PrivilegeLevel
import il.ac.technion.cs.softwaredesign.internals.ISequenceGenerator
import il.ac.technion.cs.softwaredesign.storage.SecureStorage
import il.ac.technion.cs.softwaredesign.storage.api.IStatisticsManager
import il.ac.technion.cs.softwaredesign.storage.api.IUserManager
import il.ac.technion.cs.softwaredesign.storage.datastructures.CountIdKey
import il.ac.technion.cs.softwaredesign.storage.datastructures.SecureAVLTree
import il.ac.technion.cs.softwaredesign.storage.users.IUserStorage
import il.ac.technion.cs.softwaredesign.storage.utils.MANAGERS_CONSTS
import il.ac.technion.cs.softwaredesign.storage.utils.MANAGERS_CONSTS.INVALID_USER_ID
import il.ac.technion.cs.softwaredesign.storage.utils.MANAGERS_CONSTS.LIST_PROPERTY
import il.ac.technion.cs.softwaredesign.storage.utils.MANAGERS_CONSTS.PASSWORD_PROPERTY
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager
@Inject constructor(private val userStorage: IUserStorage,
                    private val statisticsManager: IStatisticsManager,
                    @UserIdSeqGenerator private val userIdGenerator: ISequenceGenerator,
                    @UsersByChannelCountStorage private val usersByChannelsCountStorage: SecureStorage
) : IUserManager {

    private val defaultKey: () -> CountIdKey = { CountIdKey() }
    private val usersByChannelsCountTree = SecureAVLTree(usersByChannelsCountStorage, defaultKey)

    override fun addUser(username: String, password: String, status: LoginStatus, privilege: PrivilegeLevel): Long {
        var userId = getUserId(username)
        if (userId == INVALID_USER_ID) throw IllegalArgumentException("user id is not valid")
        if (userId != null) throw IllegalArgumentException("user already exist")
        userId = userIdGenerator.next()

        // id db
        userStorage.setUserIdToUsername(username, userId)

        // details db
        userStorage.setPropertyStringToUserId(userId, MANAGERS_CONSTS.USERNAME_PROPERTY, username)
        userStorage.setPropertyStringToUserId(userId, MANAGERS_CONSTS.PASSWORD_PROPERTY, password)
        userStorage.setPropertyStringToUserId(userId, MANAGERS_CONSTS.STATUS_PROPERTY, status.ordinal.toString())
        userStorage.setPropertyStringToUserId(userId, MANAGERS_CONSTS.PRIVILAGE_PROPERTY, privilege.ordinal.toString())
        initChannelList(userId)

        // tree db
        addNewUserToUserTree(userId = userId, count = 0L)

        // increase logged in users only, cause number of users was increased by id generator
        if (status == LoginStatus.IN) statisticsManager.increaseLoggedInUsersBy()

        return userId
    }


    /** GETTERS & SETTERS **/
    override fun getUserId(username: String): Long? {
        return userStorage.getUserIdByUsername(username)
    }

    override fun getUsernameById(userId: Long): String {
        return userStorage.getPropertyStringByUserId(userId, MANAGERS_CONSTS.USERNAME_PROPERTY)
                ?: throw IllegalArgumentException("user id does not exist")
    }

    override fun getUserPrivilege(userId: Long): PrivilegeLevel {
        val userPrivilege = userStorage.getPropertyStringByUserId(userId, MANAGERS_CONSTS.PRIVILAGE_PROPERTY)
                ?: throw IllegalArgumentException("user id does not exist")
        return PrivilegeLevel.values()[userPrivilege.toInt()]
    }

    override fun getUserStatus(userId: Long): LoginStatus {
        val userPrivilege = userStorage.getPropertyStringByUserId(userId, MANAGERS_CONSTS.STATUS_PROPERTY)
                ?: throw IllegalArgumentException("user id does not exist")
        return LoginStatus.values()[userPrivilege.toInt()]
    }

    override fun getUserPassword(userId: Long): String {
        val password = userStorage.getPropertyStringByUserId(userId, PASSWORD_PROPERTY)
        return password ?: throw IllegalArgumentException("user id does not exist")
    }

    override fun updateUserPrivilege(userId: Long, privilege: PrivilegeLevel) {
        userStorage.setPropertyStringToUserId(userId, MANAGERS_CONSTS.PRIVILAGE_PROPERTY, privilege.ordinal.toString())
    }

    override fun updateUserStatus(userId: Long, status: LoginStatus) {
        try {
            val oldStatus = getUserStatus(userId)
            if (oldStatus == status) return
            userStorage.setPropertyStringToUserId(userId, MANAGERS_CONSTS.STATUS_PROPERTY, status.ordinal.toString())
            if (status == LoginStatus.IN) {
                statisticsManager.increaseLoggedInUsersBy()
            } else {
                statisticsManager.decreaseLoggedInUsersBy()
            }
        } catch (e: IllegalArgumentException) { /* user id does not exist, do nothing */
        }
    }


    override fun isUsernameExists(username: String): Boolean {
        val userId = userStorage.getUserIdByUsername(username)
        return userId != null && userId != INVALID_USER_ID
    }

    override fun isUserIdExists(userId: Long): Boolean {
        val password = userStorage.getPropertyStringByUserId(userId, PASSWORD_PROPERTY)
        return password != null
    }


    /** CHANNELS OF USER **/
    override fun getChannelListOfUser(userId: Long): List<Long> {
        return userStorage.getPropertyListByUserId(userId, LIST_PROPERTY)
                ?: throw IllegalArgumentException("user id does not exist")
    }

    override fun getUserChannelListSize(userId: Long): Long {
        return userStorage.getPropertyLongByUserId(userId, MANAGERS_CONSTS.SIZE_PROPERTY)
                ?: throw IllegalArgumentException("user id does not exist")
    }

    override fun addChannelToUser(userId: Long, channelId: Long) {
        val currentList = ArrayList<Long>(getChannelListOfUser(userId))
        if (currentList.contains(channelId)) throw IllegalAccessException("channel id already exists in users list")
        currentList.add(channelId)
        userStorage.setPropertyListToUserId(userId, LIST_PROPERTY, currentList)
        userStorage.setPropertyLongToUserId(userId, MANAGERS_CONSTS.SIZE_PROPERTY, currentList.size.toLong())

        // update tree:
        val currentSize = currentList.size.toLong()
        updateUserNode(userId, oldCount = currentSize - 1L, newCount = currentSize)
    }

    override fun removeChannelFromUser(userId: Long, channelId: Long) {
        val currentList = ArrayList<Long>(getChannelListOfUser(userId))
        if (!currentList.contains(channelId)) throw IllegalAccessException("channel id does not exists in users list")
        currentList.remove(channelId)
        userStorage.setPropertyListToUserId(userId, LIST_PROPERTY, currentList)
        userStorage.setPropertyLongToUserId(userId, MANAGERS_CONSTS.SIZE_PROPERTY, currentList.size.toLong())

        // update tree:
        val currentSize = currentList.size.toLong()
        updateUserNode(userId, oldCount = currentSize + 1L, newCount = currentSize)
    }


    /** USER STATISTICS **/
    override fun getTotalUsers(): Long {
        return statisticsManager.getTotalUsers()
    }

    override fun getLoggedInUsers(): Long {
        return statisticsManager.getLoggedInUsers()
    }


    /** USER COMPLEX STATISTICS **/
    override fun getTop10UsersByChannelsCount(): List<String> {
        val values = mutableListOf<String>()
        val nrUsers = getTotalUsers()
        val nrOutputUsers = if (nrUsers > 10) 10 else nrUsers
        for (k in 1..nrOutputUsers) {
            val kthLarger = nrUsers - k
            val userId = usersByChannelsCountTree.select(kthLarger).getId()
            val userName = getUsernameById(userId)
            values.add(userName)
        }
        return values
    }

    /** PRIVATES **/
    private fun initChannelList(userId: Long) {
        userStorage.setPropertyListToUserId(userId, LIST_PROPERTY, emptyList())
        userStorage.setPropertyLongToUserId(userId, MANAGERS_CONSTS.SIZE_PROPERTY, 0L)
    }

    private fun addNewUserToUserTree(userId: Long, count: Long) {
        val key = CountIdKey(count = count, id = userId)
        usersByChannelsCountTree.put(key)
    }

    private fun updateUserNode(userId: Long, oldCount: Long, newCount: Long) {
        val oldKey = CountIdKey(count = oldCount, id = userId)
        usersByChannelsCountTree.delete(oldKey)
        val newKey = CountIdKey(count = newCount, id = userId)
        usersByChannelsCountTree.put(newKey)
    }
}