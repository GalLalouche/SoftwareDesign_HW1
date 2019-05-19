package il.ac.technion.cs.softwaredesign.storage.channels

import il.ac.technion.cs.softwaredesign.storage.SecureStorageFactory
import il.ac.technion.cs.softwaredesign.storage.utils.ConversionUtils
import il.ac.technion.cs.softwaredesign.storage.utils.DB_NAMES.CHANNEL_DETAILS
import il.ac.technion.cs.softwaredesign.storage.utils.DB_NAMES.CHANNEL_ID
import il.ac.technion.cs.softwaredesign.storage.utils.MANAGERS_CONSTS
import javax.inject.Inject

class SecureChannelStorage @Inject constructor (factory: SecureStorageFactory) : IChannelStorage {
    private val channelIdsStorage = factory.open(CHANNEL_ID.toByteArray())
    private val channelDetailsStorage = factory.open(CHANNEL_DETAILS.toByteArray())

    override fun getChannelIdByChannelName(channelName: String): Long? {
        val channelIdByteArray=channelIdsStorage.read(channelName.toByteArray()) ?: return null
        return ConversionUtils.bytesToLong(channelIdByteArray)
    }

    override fun setChannelIdToChannelName(channelNameKey: String, channelId: Long) {
        channelIdsStorage.write(channelNameKey.toByteArray(),ConversionUtils.longToBytes(channelId))
    }

    override fun getPropertyStringByChannelId(channelIdKey: Long, property: String): String? {
        val key = createPropertyKey(channelIdKey, property)
        val value= channelDetailsStorage.read(key) ?: return null
        return String(value)
    }

    override fun setPropertyStringToChannelId(channelIdKey: Long, property: String, value: String) {
        val key = createPropertyKey(channelIdKey, property)
        channelDetailsStorage.write(key, value.toByteArray())
    }

    override fun getPropertyLongByUserId(channelIdKey: Long, property: String): Long? {
        val key = createPropertyKey(channelIdKey, property)
        val value= channelDetailsStorage.read(key) ?: return null
        return ConversionUtils.bytesToLong(value)
    }

    override fun setPropertyLongToUserId(channelIdKey: Long, property: String, value: Long) {
        val key = createPropertyKey(channelIdKey, property)
        channelDetailsStorage.write(key, ConversionUtils.longToBytes(value))
    }

    override fun getPropertyListByChannelId(channelIdKey: Long, property: String): List<Long>? {
        val key = createPropertyKey(channelIdKey, property)
        val value= channelDetailsStorage.read(key) ?: return null
        val stringValue = String(value)
        return stringValue.split(MANAGERS_CONSTS.DELIMITER).map { it.toLong() }.toMutableList()
    }

    override fun setPropertyListToChannelId(channelIdKey: Long, property: String, listValue: List<Long>) {
        val key = createPropertyKey(channelIdKey, property)
        val value = listValue.joinToString(MANAGERS_CONSTS.DELIMITER)
        channelDetailsStorage.write(key, value.toByteArray())
    }

    private fun createPropertyKey(channelId: Long, property: String) : ByteArray{
        val channelIdByteArray = ConversionUtils.longToBytes(channelId)
        val keySuffixByteArray = "${MANAGERS_CONSTS.DELIMITER}$property".toByteArray()
        return channelIdByteArray + keySuffixByteArray
    }
}