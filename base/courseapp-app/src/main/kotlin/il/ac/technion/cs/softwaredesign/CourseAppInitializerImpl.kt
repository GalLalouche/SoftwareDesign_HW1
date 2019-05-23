package il.ac.technion.cs.softwaredesign

import com.google.common.primitives.Longs
import il.ac.technion.cs.softwaredesign.DB_NAMES.CHANNEL_DETAILS
import il.ac.technion.cs.softwaredesign.DB_NAMES.CHANNEL_ID
import il.ac.technion.cs.softwaredesign.DB_NAMES.STATISTICS
import il.ac.technion.cs.softwaredesign.DB_NAMES.TOKEN
import il.ac.technion.cs.softwaredesign.DB_NAMES.TREE_CHANNELS_BY_ACTIVE_USERS_COUNT
import il.ac.technion.cs.softwaredesign.DB_NAMES.TREE_CHANNELS_BY_USERS_COUNT
import il.ac.technion.cs.softwaredesign.DB_NAMES.TREE_USERS_BY_CHANNELS_COUNT
import il.ac.technion.cs.softwaredesign.DB_NAMES.USER_DETAILS
import il.ac.technion.cs.softwaredesign.DB_NAMES.USER_ID
import il.ac.technion.cs.softwaredesign.TREE_KEYS.ROOT_INIT_INDEX
import il.ac.technion.cs.softwaredesign.TREE_KEYS.ROOT_KEY
import il.ac.technion.cs.softwaredesign.storage.SecureStorageFactory
import javax.inject.Inject

class CourseAppInitializerImpl
@Inject constructor(private val storageFactory: SecureStorageFactory) : CourseAppInitializer {

    override fun setup() {
        /*storageFactory.open(USER_ID.toByteArray())
        storageFactory.open(TOKEN.toByteArray())
        storageFactory.open(USER_DETAILS.toByteArray())
        storageFactory.open(CHANNEL_ID.toByteArray())
        storageFactory.open(CHANNEL_DETAILS.toByteArray())*/

        initTree(TREE_USERS_BY_CHANNELS_COUNT)
        initTree(TREE_CHANNELS_BY_USERS_COUNT)
        initTree(TREE_CHANNELS_BY_ACTIVE_USERS_COUNT)
        initStatistics()
    }

    private fun initStatistics() {
        val db = storageFactory.open(STATISTICS.toByteArray())
        db.write(STATISTICS_KEYS.NUMBER_OF_USERS.toByteArray(), Longs.toByteArray(STATISTICS_KEYS.INIT_INDEX_VAL))
        db.write(STATISTICS_KEYS.NUMBER_OF_LOGGED_IN_USERS.toByteArray(), Longs.toByteArray(STATISTICS_KEYS.INIT_INDEX_VAL))
        db.write(STATISTICS_KEYS.NUMBER_OF_CHANNELS.toByteArray(), Longs.toByteArray(STATISTICS_KEYS.INIT_INDEX_VAL))
        db.write(STATISTICS_KEYS.MAX_CHANNEL_INDEX.toByteArray(), Longs.toByteArray(STATISTICS_KEYS.INIT_INDEX_VAL))
    }

    private fun initTree(dbName: String) {
        val db = storageFactory.open(dbName.toByteArray())
        db.write(ROOT_KEY.toByteArray(), Longs.toByteArray(ROOT_INIT_INDEX))
    }
}