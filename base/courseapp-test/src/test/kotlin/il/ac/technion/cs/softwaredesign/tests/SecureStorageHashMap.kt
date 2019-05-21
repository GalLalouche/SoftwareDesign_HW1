package il.ac.technion.cs.softwaredesign.tests

import il.ac.technion.cs.softwaredesign.CourseApp
import il.ac.technion.cs.softwaredesign.CourseAppInitializer
import il.ac.technion.cs.softwaredesign.CourseAppInitializerImpl
import il.ac.technion.cs.softwaredesign.storage.SecureStorage

class SecureStorageHashMap : SecureStorage{
    class ByteArrayKey(private val bytes: ByteArray) {
        override fun equals(other: Any?): Boolean =
                this === other || other is ByteArrayKey && this.bytes contentEquals other.bytes
        override fun hashCode(): Int = bytes.contentHashCode()
        override fun toString(): String = bytes.contentToString()
    }
    private val storage = mutableMapOf<ByteArrayKey,ByteArray>()

    override fun read(key: ByteArray): ByteArray? {
        return storage[ByteArrayKey(key)]
    }

    override fun write(key: ByteArray, value: ByteArray) {
        storage[ByteArrayKey(key)] = value
    }
}