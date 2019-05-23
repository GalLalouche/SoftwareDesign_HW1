package il.ac.technion.cs.softwaredesign.storage.proxies

import il.ac.technion.cs.softwaredesign.internals.LRUCache
import il.ac.technion.cs.softwaredesign.storage.SecureStorage


class SecureStorageCache(private val secureStorage: SecureStorage) : SecureStorage {
    private val cache: LRUCache<ByteArrayKey, ByteArray?> = LRUCache(capacity = 250_000)

    override fun read(key: ByteArray): ByteArray? {
        val keyWrapper = ByteArrayKey(key)
        if (cache[keyWrapper] == null) {
            cache[keyWrapper] = secureStorage.read(key)
        }
        return cache[keyWrapper]
    }

    override fun write(key: ByteArray, value: ByteArray) {
        secureStorage.write(key, value)
        val keyWrapper = ByteArrayKey(key)
        cache[keyWrapper] = value
    }

    class ByteArrayKey(private val bytes: ByteArray) {
        override fun equals(other: Any?): Boolean =
                this === other || other is ByteArrayKey && this.bytes contentEquals other.bytes

        override fun hashCode(): Int = bytes.contentHashCode()
        override fun toString(): String = bytes.contentToString()
    }

}