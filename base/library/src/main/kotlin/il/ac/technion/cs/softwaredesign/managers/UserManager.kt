package il.ac.technion.cs.softwaredesign.managers

import com.google.inject.BindingAnnotation
import il.ac.technion.cs.softwaredesign.managers.IUserManager.*
import il.ac.technion.cs.softwaredesign.storage.ISequenceGenerator
import il.ac.technion.cs.softwaredesign.storage.users.IUserStorage
import il.ac.technion.cs.softwaredesign.storage.utils.MANAGERS_CONSTS
import il.ac.technion.cs.softwaredesign.storage.utils.MANAGERS_CONSTS.INVALID_USER_ID
import il.ac.technion.cs.softwaredesign.storage.utils.MANAGERS_CONSTS.LIST_PROPERTY
import il.ac.technion.cs.softwaredesign.storage.utils.MANAGERS_CONSTS.PASSWORD_PROPERTY
import java.lang.IllegalArgumentException
import javax.inject.Inject

class UserManager @Inject constructor(private val userStorage: IUserStorage,
                                      @UserIdSeqGenerator private val userIdGenerator: ISequenceGenerator) : IUserManager {

    override fun getUserId(username: String): Long? {
        return userStorage.getUserIdByUsername(username)
    }

    override fun add(username: String, password: String, status: LoginStatus, privilege: PrivilegeLevel):Long {
        var userId = getUserId(username)
        if (userId != null) throw IllegalArgumentException("user already exist")
        userId = userIdGenerator.next()
        userStorage.setUserIdToUsername(username, userId)
        userStorage.setPropertyStringToUserId(userId, MANAGERS_CONSTS.USERNAME_PROPERTY, username)
        userStorage.setPropertyStringToUserId(userId, MANAGERS_CONSTS.PASSWORD_PROPERTY, password)
        updateStatus(userId, status)
        updatePrivilege(userId, privilege)
        updateChannelListSize(userId, 0L)
        initChannelList(userId)
        return userId
    }

    private fun initChannelList(userId: Long) {
        userStorage.setPropertyListToUserId(userId, LIST_PROPERTY, mutableListOf())
    }

    override fun updatePrivilege(userId: Long, privilege: PrivilegeLevel) {
        userStorage.setPropertyStringToUserId(userId, MANAGERS_CONSTS.PRIVILAGE_PROPERTY, privilege.ordinal.toString())
    }

    override fun getPrivilege(userId: Long): PrivilegeLevel {
        val userPrivilege = userStorage.getPropertyStringByUserId(userId, MANAGERS_CONSTS.PRIVILAGE_PROPERTY)
                ?: throw IllegalArgumentException("user id does not exist")
        return PrivilegeLevel.values()[userPrivilege.toInt()]
    }

    override fun updateStatus(userId: Long, status: LoginStatus) {
        userStorage.setPropertyStringToUserId(userId, MANAGERS_CONSTS.STATUS_PROPERTY, status.ordinal.toString())
    }

    override fun getStatus(userId: Long): LoginStatus {
        val userPrivilege = userStorage.getPropertyStringByUserId(userId, MANAGERS_CONSTS.STATUS_PROPERTY)
                ?: throw IllegalArgumentException("user id does not exist")
        return LoginStatus.values()[userPrivilege.toInt()]
    }

    override fun isUsernameExists(username: String): Boolean {
        val userId = userStorage.getUserIdByUsername(username)
        return userId != null && userId != INVALID_USER_ID
    }

    override fun isUserIdExists(userId: Long): Boolean {
        val password = userStorage.getPropertyStringByUserId(userId, PASSWORD_PROPERTY)
        return password != null
    }

    override fun getUserPassword(userId: Long): String {
        val password = userStorage.getPropertyStringByUserId(userId, PASSWORD_PROPERTY)
        return password ?: throw IllegalArgumentException("user id does not exist")
    }

    override fun getChannelListOfUser(userId: Long): List<Long> {
        return userStorage.getPropertyListByUserId(userId, LIST_PROPERTY)
                ?: throw IllegalArgumentException("user id does not exist")
    }

    override fun addChannelToUser(userId: Long, channelId: Long) {
        val currentList = ArrayList<Long>(getChannelListOfUser(userId))
        currentList.add(channelId)
        userStorage.setPropertyListToUserId(userId, LIST_PROPERTY, currentList)
    }

    override fun removeChannelFromUser(userId: Long, channelId: Long) {
        val currentList = ArrayList<Long>(getChannelListOfUser(userId))
        currentList.remove(channelId)
        userStorage.setPropertyListToUserId(userId, LIST_PROPERTY, currentList)
    }

    override fun updateChannelListSize(userId: Long, size: Long) {
        userStorage.setPropertyLongToUserId(userId, MANAGERS_CONSTS.SIZE_PROPERTY, size)
    }

    override fun getChannelListSize(userId: Long): Long {
        return userStorage.getPropertyLongByUserId(userId, MANAGERS_CONSTS.SIZE_PROPERTY)
                ?: throw IllegalArgumentException("user id does not exist")
    }
}