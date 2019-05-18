package il.ac.technion.cs.softwaredesign.tests

import il.ac.technion.cs.softwaredesign.storage.SecureStorage
import il.ac.technion.cs.softwaredesign.storage.SecureStorageFactory

class SecureHashMapStorageFactoryImpl : SecureStorageFactory {
    class ByteArrayKey(private val bytes: ByteArray) {
        override fun equals(other: Any?): Boolean =
                this === other || other is ByteArrayKey && this.bytes contentEquals other.bytes
        override fun hashCode(): Int = bytes.contentHashCode()
        override fun toString(): String = bytes.contentToString()
    }
    private val storages = mutableMapOf<ByteArrayKey, SecureStorage>()

    override fun open(name: ByteArray): SecureStorage {
        var storage = storages[ByteArrayKey(name)]
        if (storage == null) {
            storage = SecureStorageHashMap()
            storages[ByteArrayKey(name)] = storage
            Thread.sleep(100)
        }
        return storage
    }
}