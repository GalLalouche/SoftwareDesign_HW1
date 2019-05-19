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

    private val channelsByUsersCountStorage = factory.open(DB_NAMES.TREE_CHANNELS_BY_USERS_COUNT.toByteArray())
    private val channelsByActiveUsersCountStorage = factory.open(DB_NAMES.TREE_CHANNELS_BY_ACTIVE_USERS_COUNT.toByteArray())
    private val usersByChannelsCountStorage = factory.open(DB_NAMES.TREE_USERS_BY_CHANNELS_COUNT.toByteArray())

    private val defaultKey: () -> CountIdKey = { CountIdKey() }

    private val channelsByUsersCountTree = SecureAVLTree<CountIdKey>(channelsByUsersCountStorage, defaultKey)
    private val channelsByActiveUsersCountTree = SecureAVLTree<CountIdKey>(channelsByActiveUsersCountStorage, defaultKey)
    private val usersByChannelsCountTree = SecureAVLTree<CountIdKey>(usersByChannelsCountStorage, defaultKey)

    private inner class CountIdKey : ISecureStorageKey<CountIdKey> {
        var count : Long = 0 // primary key
        var id : Long = MANAGERS_CONSTS.CHANNEL_INVALID_ID // secondary key

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

//    private val channelsByUserCountTree : SecureAVLTree<>

    /** COMPLEX STATISTICS **/
    /** This functions used to update the data structures related to statistics **/

    /**
     * Update the new status of the user
     * @param userId Long
     * @param status IUserManager.LoginStatus to be updated
     * @throws
     */
    fun updateUserStatusInSystem(userId: Long, status: IUserManager.LoginStatus) {
        // should update channelsByActiveUsersCountTree
        // should remove operators? no

    }

    /**
     * Update channel and user after user joined to channel
     * @param userId Long
     * @param channelId Long
     */
    fun joinUserToChannel(userId: Long, channelId: Long) {}

    /**
     * Update channel and user after user leaved the channel
     * @param userId Long
     * @param channelId Long
     */
    fun removeUserFromChannel(userId: Long, channelId: Long) {}
}