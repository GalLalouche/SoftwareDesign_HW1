package il.ac.technion.cs.softwaredesign.storage

interface IStorageConverter<T> {
    fun toByteArray(value:T):ByteArray
    fun fromByteArray(data:ByteArray):T
}