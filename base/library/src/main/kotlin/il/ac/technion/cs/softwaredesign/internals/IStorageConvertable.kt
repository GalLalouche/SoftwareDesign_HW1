package il.ac.technion.cs.softwaredesign.storage

interface IStorageConvertable<T> {
    fun toByteArray():ByteArray
    fun fromByteArray(value:ByteArray)
}