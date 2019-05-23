package il.ac.technion.cs.softwaredesign.storage.concerns


import il.ac.technion.cs.softwaredesign.internals.LRUCache
import il.ac.technion.cs.softwaredesign.storage.SecureStorage
import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import java.lang.IllegalArgumentException



object SecureStorageFactoryCacheConcern : MethodInterceptor ,ResetableInterceptor {

    private var cache: LRUCache<ByteArrayKey, SecureStorage> = LRUCache(capacity = 50)

    override fun invoke(invocation: MethodInvocation?): Any? {
        if (invocation == null) throw IllegalArgumentException("SecureStorageFactoryCacheConcern got null as argument")
        //if we detected method name with the name open, it is SecureStorageFactory and we want to cache it
        if (invocation.method.name == "open") {
            val name = invocation.arguments[0] as ByteArray
            val key = ByteArrayKey(name)
            if (!cache.contains(key))
                cache[key] = (invocation.proceed() as SecureStorage)
            return cache[key]
        }
        //in any other case proceed as usual
        return invocation.proceed()
    }
    override fun reset() {
        cache.clear()
    }

    class ByteArrayKey(private val bytes: ByteArray) {
        override fun equals(other: Any?): Boolean =
                this === other || other is ByteArrayKey && this.bytes contentEquals other.bytes

        override fun hashCode(): Int = bytes.contentHashCode()
        override fun toString(): String = bytes.contentToString()
    }

}