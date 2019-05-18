package il.ac.technion.cs.softwaredesign.storage.utils

import il.ac.technion.cs.softwaredesign.storage.IStorageConvertable
import java.lang.IllegalArgumentException

class LongStorageable(var value:Long) : IStorageConvertable<LongStorageable> {

    override fun toByteArray(): ByteArray {
        return ConversionUtils.longToBytes(value)
    }

    override fun fromByteArray(value: ByteArray?) {
        if(value==null) throw IllegalArgumentException("Long value cannot be null")
        this.value= ConversionUtils.bytesToLong(value)
    }
}