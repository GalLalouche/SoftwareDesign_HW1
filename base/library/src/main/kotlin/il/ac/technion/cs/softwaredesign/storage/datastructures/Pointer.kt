package il.ac.technion.cs.softwaredesign.storage.datastructures

import il.ac.technion.cs.softwaredesign.storage.IStorageConverter
import il.ac.technion.cs.softwaredesign.storage.SecureStorage
import javax.inject.Inject


class Pointer(private var pointer : String) : IStorageConverter<Pointer> {
    override fun toByteArray():ByteArray = pointer.toByteArray()

    override fun fromByteArray(value:ByteArray?) {
        if (value == null) throw IllegalArgumentException("Pointer can not be null")
        pointer = String(value)
    }

//    fun getValue() : String? {
//        secureStorage.read(pointer.toByteArray())
//    }
}