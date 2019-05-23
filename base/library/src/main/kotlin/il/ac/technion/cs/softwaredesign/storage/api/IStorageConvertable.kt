package il.ac.technion.cs.softwaredesign.storage.api

interface IStorageConvertable<T> {
    fun toByteArray():ByteArray
    fun fromByteArray(value:ByteArray)
}