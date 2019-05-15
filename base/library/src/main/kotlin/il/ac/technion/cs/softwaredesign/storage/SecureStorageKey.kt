package il.ac.technion.cs.softwaredesign.storage

interface SecureStorageKey<Key> :  Comparable<Key>{

    fun toByteArray(value:Key)
    fun fromByteArray(data:kotlin.ByteArray):Key
}