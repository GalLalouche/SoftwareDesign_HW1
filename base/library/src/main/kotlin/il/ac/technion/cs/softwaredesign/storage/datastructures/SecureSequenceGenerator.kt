package il.ac.technion.cs.softwaredesign.storage.datastructures

import com.google.inject.BindingAnnotation
import il.ac.technion.cs.softwaredesign.storage.SecureStorage
import il.ac.technion.cs.softwaredesign.storage.utils.ByteUtils
import il.ac.technion.cs.softwaredesign.storage.utils.TREE_CONST
import javax.inject.Inject
import javax.inject.Singleton

@Target(AnnotationTarget.CONSTRUCTOR)
@Retention(AnnotationRetention.RUNTIME)
@BindingAnnotation
annotation class GeneratorStorage

@Singleton
class SecureSequenceGenerator
@Inject @GeneratorStorage constructor(private val secureStorage: SecureStorage) :ISequenceGenerator {
    private val lastGeneratedKey="LAST_GENERATED_ID".toByteArray()
    override fun next(): Long {
        val currentValueInByteArray :ByteArray= secureStorage.read(lastGeneratedKey) ?:  ByteUtils.longToBytes(TREE_CONST.ROOT_INIT_INDEX)
        val nextValue:Long= ByteUtils.bytesToLong(currentValueInByteArray)+1L
        secureStorage.write(lastGeneratedKey,ByteUtils.longToBytes(nextValue))
        return nextValue
    }

}