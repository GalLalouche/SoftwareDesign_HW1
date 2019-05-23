package il.ac.technion.cs.softwaredesign.storage.datastructures

import il.ac.technion.cs.softwaredesign.storage.api.ISecureStorageKey
import il.ac.technion.cs.softwaredesign.storage.utils.ConversionUtils
import il.ac.technion.cs.softwaredesign.storage.utils.MANAGERS_CONSTS

class CountIdKey(private var count: Long = 0, // primary key
                 private var id: Long = MANAGERS_CONSTS.CHANNEL_INVALID_ID) // secondary key
    : ISecureStorageKey<CountIdKey> {

    override fun compareTo(other: CountIdKey): Int {
        val primaryRes = count.compareTo(other.count)
        if (primaryRes != 0) return primaryRes
        return -(id.compareTo(other.id))
    }

    // format: <count><id>
    override fun toByteArray(): ByteArray {
        return ConversionUtils.longToBytes(count) + ConversionUtils.longToBytes(id)
    }

    override fun fromByteArray(value: ByteArray) {
        var start = 0
        var end = Long.SIZE_BYTES - 1
        count = ConversionUtils.bytesToLong(value.sliceArray(IntRange(start,end)))
        start += Long.SIZE_BYTES
        end += Long.SIZE_BYTES
        id = ConversionUtils.bytesToLong(value.sliceArray(IntRange(start,end)))
    }

    fun getId() : Long = id
}
