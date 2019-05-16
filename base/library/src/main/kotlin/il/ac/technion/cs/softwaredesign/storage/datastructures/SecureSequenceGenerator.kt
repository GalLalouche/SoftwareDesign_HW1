package il.ac.technion.cs.softwaredesign.storage.datastructures

import com.google.inject.BindingAnnotation
import il.ac.technion.cs.softwaredesign.storage.SecureStorage
import il.ac.technion.cs.softwaredesign.storage.utils.ByteUtils
import il.ac.technion.cs.softwaredesign.storage.utils.TREE_CONST
import javax.inject.Inject
import javax.inject.Singleton

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@BindingAnnotation
annotation class GeneratorStorage

@Singleton
class SecureSequenceGenerator :ISequenceGenerator {
    private val lastGeneratedKey="LAST_GENERATED_ID".toByteArray() //maybe should be hashed or something
    @Inject @GeneratorStorage
    private lateinit var secureStorage:SecureStorage
    override fun next(): Long {
        val currentValueInByteArray :ByteArray= secureStorage.read(lastGeneratedKey) ?:  ByteUtils.longToBytes(TREE_CONST.ROOT_INIT_INDEX)
        val nextValue:Long= ByteUtils.bytesToLong(currentValueInByteArray)+1L
        secureStorage.write(lastGeneratedKey,ByteUtils.longToBytes(nextValue))
        return nextValue
    }

}