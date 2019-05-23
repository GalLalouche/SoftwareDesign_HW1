package il.ac.technion.cs.softwaredesign.storage.concerns


/*
@Singleton
class SecureStorageFactoryCache
(private val secureStorageFactory: SecureStorageFactory) : SecureStorageFactory {
    private var cache: LRUCache<ByteArrayKey,SecureStorage> = LRUCache(capacity = 60)
    override fun open(name: ByteArray): SecureStorage {
        val key=ByteArrayKey(name)
        if(!cache.contains(key))
            cache[key]=secureStorageFactory.open(name)
        return cache[key]!!
    }


    class ByteArrayKey(private val bytes: ByteArray) {
        override fun equals(other: Any?): Boolean =
                this === other || other is ByteArrayKey && this.bytes contentEquals other.bytes
        override fun hashCode(): Int = bytes.contentHashCode()
        override fun toString(): String = bytes.contentToString()
    }


}*/
