package il.ac.technion.cs.softwaredesign.storage.datastructures

import il.ac.technion.cs.softwaredesign.storage.SecureStorage
import il.ac.technion.cs.softwaredesign.storage.utils.ByteUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecureSequenceGenerator :ISequenceGenerator {
    private val lastGeneratedKey="LAST_GENERATED_ID".toByteArray() //maybe should be hashed or something
    @Inject
    private lateinit var secureStorage:SecureStorage
    override fun next(): Long {
        val currentValueInByteArray :ByteArray= secureStorage.read(lastGeneratedKey) ?:  ByteUtils.longToBytes(0L)
        val nextValue:Long= ByteUtils.bytesToLong(currentValueInByteArray)+1L
        secureStorage.write(lastGeneratedKey,ByteUtils.longToBytes(nextValue))
        return nextValue
    }

}