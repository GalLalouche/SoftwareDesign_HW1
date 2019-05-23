package il.ac.technion.cs.softwaredesign.storage.channels

import il.ac.technion.cs.softwaredesign.managers.ChannelDetailsStorage
import il.ac.technion.cs.softwaredesign.managers.ChannelIdStorage
import il.ac.technion.cs.softwaredesign.storage.SecureStorage
import il.ac.technion.cs.softwaredesign.storage.utils.ConversionUtils
import il.ac.technion.cs.softwaredesign.storage.utils.MANAGERS_CONSTS
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecureChannelStorage
@Inject constructor(@ChannelIdStorage private val channelIdsStorage: SecureStorage,
                    @ChannelDetailsStorage private val channelDetailsStorage: SecureStorage) : IChannelStorage {

    override fun getChannelIdByChannelName(channelName: String): Long? {
        val channelIdByteArray = channelIdsStorage.read(channelName.toByteArray()) ?: return null
        return ConversionUtils.bytesToLong(channelIdByteArray)
    }

    override fun setChannelIdToChannelName(channelNameKey: String, channelId: Long) {
        channelIdsStorage.write(channelNameKey.toByteArray(), ConversionUtils.longToBytes(channelId))
    }

    override fun getPropertyStringByChannelId(channelIdKey: Long, property: String): String? {
        val key = createPropertyKey(channelIdKey, property)
        val value = channelDetailsStorage.read(key) ?: return null
        return String(value)
    }

    override fun setPropertyStringToChannelId(channelIdKey: Long, property: String, value: String) {
        val key = createPropertyKey(channelIdKey, property)
        channelDetailsStorage.write(key, value.toByteArray())
    }

    override fun getPropertyLongByChannelId(channelIdKey: Long, property: String): Long? {
        val key = createPropertyKey(channelIdKey, property)
        val value = channelDetailsStorage.read(key) ?: return null
        return ConversionUtils.bytesToLong(value)
    }

    override fun setPropertyLongToChannelId(channelIdKey: Long, property: String, value: Long) {
        val key = createPropertyKey(channelIdKey, property)
        channelDetailsStorage.write(key, ConversionUtils.longToBytes(value))
    }

    override fun getPropertyListByChannelId(channelIdKey: Long, property: String): List<Long>? {
        val key = createPropertyKey(channelIdKey, property)
        val value = channelDetailsStorage.read(key) ?: return null
        val stringValue = String(value)
        if (stringValue == "") return emptyList()
        return stringValue.split(MANAGERS_CONSTS.DELIMITER).map { it.toLong() }.toMutableList()
    }

    override fun setPropertyListToChannelId(channelIdKey: Long, property: String, listValue: List<Long>) {
        val key = createPropertyKey(channelIdKey, property)
        val value = listValue.joinToString(MANAGERS_CONSTS.DELIMITER)
        channelDetailsStorage.write(key, value.toByteArray())
    }

    private fun createPropertyKey(channelId: Long, property: String): ByteArray {
        val channelIdByteArray = ConversionUtils.longToBytes(channelId)
        val keySuffixByteArray = "${MANAGERS_CONSTS.DELIMITER}$property".toByteArray()
        return channelIdByteArray + keySuffixByteArray
    }
}