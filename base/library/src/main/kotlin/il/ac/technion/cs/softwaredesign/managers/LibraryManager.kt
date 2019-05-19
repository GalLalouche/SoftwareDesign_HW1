package il.ac.technion.cs.softwaredesign.managers

import com.google.inject.Inject
import il.ac.technion.cs.softwaredesign.storage.ISecureStorageKey
import il.ac.technion.cs.softwaredesign.storage.SecureStorageFactory
import il.ac.technion.cs.softwaredesign.storage.datastructures.SecureAVLTree
import il.ac.technion.cs.softwaredesign.storage.utils.ConversionUtils
import il.ac.technion.cs.softwaredesign.storage.utils.DB_NAMES
import il.ac.technion.cs.softwaredesign.storage.utils.MANAGERS_CONSTS

class LibraryManager @Inject constructor(
        factory: SecureStorageFactory,
        private val userManager: IUserManager,
        private val channelManager: IChannelManager,
        private val statisticsManager: IStatisticsManager) :
        IUserManager by userManager,
        IChannelManager by channelManager,
        IStatisticsManager by statisticsManager {

    private val channeldByUsersCountStorage = factory.open(DB_NAMES.TREE_CHANNELS_BY_USERS_COUNT.toByteArray())
    private val channeldByActiveUsersCountStorage = factory.open(DB_NAMES.TREE_CHANNELS_BY_ACTIVE_USERS_COUNT.toByteArray())
    private val usersByChannelsCountStorage = factory.open(DB_NAMES.TREE_USERS_BY_CHANNELS_COUNT.toByteArray())

    private val defaultKey: () -> CountIdKey = { CountIdKey() }

    private val channeldByUsersCountTree = SecureAVLTree<CountIdKey>(channeldByUsersCountStorage, defaultKey)
    private val channeldByActiveUsersCountTree = SecureAVLTree<CountIdKey>(channeldByActiveUsersCountStorage, defaultKey)
    private val usersByChannelsCountTree = SecureAVLTree<CountIdKey>(usersByChannelsCountStorage, defaultKey)

    private inner class CountIdKey(private var count: Long = 0, // primary key
                                   private var id: Long = MANAGERS_CONSTS.CHANNEL_INVALID_ID) // secondary key
                                    : ISecureStorageKey<CountIdKey> {

        override fun compareTo(other: CountIdKey): Int {
            val primaryRes = count.compareTo(other.count)
            if (primaryRes != 0) return primaryRes
            return id.compareTo(other.id)
        }

        // format: <count><id>
        override fun toByteArray(): ByteArray {
            return ConversionUtils.longToBytes(count) + ConversionUtils.longToBytes(id)
        }

        override fun fromByteArray(value: ByteArray) {
            var start = 0
            var end = Long.SIZE_BYTES - 1
            count = ConversionUtils.bytesToLong(value.sliceArray(IntRange(start,end)))
            start += Long.SIZE_BYTES
            end += Long.SIZE_BYTES
            id = ConversionUtils.bytesToLong(value.sliceArray(IntRange(start,end)))
        }
    }

    /** COMPLEX STATISTICS **/
    /** This functions used to update the data structures related to statistics **/

    /**
     * Update the new status of the user
     * @param userId Long
     * @param status IUserManager.LoginStatus to be updated
     * @throws
     */
    fun updateUserStatusInSystem(userId: Long, status: IUserManager.LoginStatus) {
        // should update channeldByActiveUsersCountTree, (assume list and size has been updated in course app before)
        // consider new user?
        // increase nr users
        // incease / decrease nr active users

        updateChanneldByActiveUsersCountTreeOnStatusUpdate(status, userId)
        TODO("implement")
    }

    private fun updateChanneldByActiveUsersCountTreeOnStatusUpdate(status: IUserManager.LoginStatus, userId: Long) {
        val diff: Int = if (status == IUserManager.LoginStatus.IN) -1 else 1
        val channelsList = userManager.getChannelListOfUser(userId)
        for (channelId in channelsList) {
            val newCount = channelManager.getNumberOfActiveMembersInChannel(channelId)
            // user logged out => old one is bigger(+1), user logged in => old one is smaller(-1)
            val oldCount = newCount + diff
            val oldKey = CountIdKey(oldCount, channelId)
            val newKey = CountIdKey(newCount, channelId)
            channeldByActiveUsersCountTree.delete(oldKey)
            channeldByActiveUsersCountTree.put(newKey)
        }
    }

    private fun updateusersByChannelsCountTreeOnStatusUpdate(userId: Long) {TODO("implement")}

    /**
     * Update channel and user after user joined to channel
     * @param userId Long
     * @param channelId Long
     */
    fun joinUserToChannel(userId: Long, channelId: Long) {
        // should update channeldByUsersCountTree, channeldByActiveUsersCountTree, usersByChannelsCountTree
        // consider new user? new channel?
        TODO("implement")
    }

    /**
     * Update channel and user after user leaved the channel
     * @param userId Long
     * @param channelId Long
     */
    fun removeUserFromChannel(userId: Long, channelId: Long) {TODO("implement")}
}