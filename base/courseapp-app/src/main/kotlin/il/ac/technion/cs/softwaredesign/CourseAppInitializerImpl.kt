package il.ac.technion.cs.softwaredesign

import il.ac.technion.cs.softwaredesign.DB_NAMES.CHANNEL_DETAILS
import il.ac.technion.cs.softwaredesign.DB_NAMES.CHANNEL_ID
import il.ac.technion.cs.softwaredesign.DB_NAMES.STATISTICS
import il.ac.technion.cs.softwaredesign.DB_NAMES.TOKEN
import il.ac.technion.cs.softwaredesign.DB_NAMES.TREE_ACTIVE_CHANNEL
import il.ac.technion.cs.softwaredesign.DB_NAMES.TREE_CHANNEL
import il.ac.technion.cs.softwaredesign.DB_NAMES.TREE_USER
import il.ac.technion.cs.softwaredesign.DB_NAMES.USER_CHANNELS
import il.ac.technion.cs.softwaredesign.DB_NAMES.USER_DETAILS
import il.ac.technion.cs.softwaredesign.DB_NAMES.USER_ID
import il.ac.technion.cs.softwaredesign.TREE_KEYS.ROOT_INIT_INDEX
import il.ac.technion.cs.softwaredesign.TREE_KEYS.ROOT_KEY
import il.ac.technion.cs.softwaredesign.storage.SecureStorageFactory
import java.nio.ByteBuffer
import javax.inject.Inject

class CourseAppInitializerImpl @Inject constructor(private val storageFactory: SecureStorageFactory) : CourseAppInitializer {

    companion object ByteUtils { //DONT FORGET TO UPDATE IN il.ac.technion.cs.softwaredesign.storage.utils TOO
        private val buffer = ByteBuffer.allocate(java.lang.Long.BYTES)

        fun longToBytes(x: Long): ByteArray {
            buffer.putLong(0, x)
            return buffer.array()
        }

        fun bytesToLong(bytes: ByteArray): Long {
            buffer.put(bytes, 0, bytes.size)
            buffer.flip()//need flip
            return buffer.long
        }
    }

    override fun setup() {
        storageFactory.open(USER_ID.toByteArray())
        storageFactory.open(TOKEN.toByteArray())
        storageFactory.open(USER_DETAILS.toByteArray())
        storageFactory.open(USER_CHANNELS.toByteArray())
        storageFactory.open(CHANNEL_ID.toByteArray())
        storageFactory.open(CHANNEL_DETAILS.toByteArray())

        initTree(TREE_USER)
        initTree(TREE_CHANNEL)
        initTree(TREE_ACTIVE_CHANNEL)
        initStatistics()


    }

    private fun initStatistics() {
        val db= storageFactory.open(STATISTICS.toByteArray())
        db.write(STATISTICS_KEYS.NUMBER_OF_USERS.toByteArray(), longToBytes(0L))
        db.write(STATISTICS_KEYS.NUMBER_OF_LOGGED_IN_USERS.toByteArray(), longToBytes(0L))
        db.write(STATISTICS_KEYS.NUMBER_OF_CHANNELS.toByteArray(), longToBytes(0L))
        db.write(STATISTICS_KEYS.MAX_CHANNEL_INDEX.toByteArray(), longToBytes(0L))

    }

    private fun initTree(dbName:String) {
        val db= storageFactory.open(dbName.toByteArray())
        db.write(ROOT_KEY.toByteArray(), longToBytes(ROOT_INIT_INDEX))
    }
}