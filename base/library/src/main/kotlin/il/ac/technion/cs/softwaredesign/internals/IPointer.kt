package il.ac.technion.cs.softwaredesign.internals

import il.ac.technion.cs.softwaredesign.storage.api.IStorageConvertable

internal interface IPointer : IStorageConvertable<IPointer> {
    fun getAddress() : Long
}