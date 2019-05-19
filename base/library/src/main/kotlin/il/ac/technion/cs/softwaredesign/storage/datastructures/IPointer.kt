package il.ac.technion.cs.softwaredesign.storage.datastructures

import il.ac.technion.cs.softwaredesign.storage.IStorageConvertable

interface IPointer : IStorageConvertable<IPointer> {
    fun getAddress() : Long
}