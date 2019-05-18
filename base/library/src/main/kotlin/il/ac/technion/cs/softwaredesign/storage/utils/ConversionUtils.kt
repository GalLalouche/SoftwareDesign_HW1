package il.ac.technion.cs.softwaredesign.storage.utils

import com.google.common.primitives.Longs

object ConversionUtils {

    fun longToBytes(x: Long): ByteArray = Longs.toByteArray(x)

    fun bytesToLong(bytes: ByteArray): Long = Longs.fromByteArray(bytes)
}