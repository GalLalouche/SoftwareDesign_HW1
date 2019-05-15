package il.ac.technion.cs.softwaredesign.storage

interface IStorageConverter<T> {
    fun toByteArray():ByteArray
    fun fromByteArray(value:ByteArray?)
}