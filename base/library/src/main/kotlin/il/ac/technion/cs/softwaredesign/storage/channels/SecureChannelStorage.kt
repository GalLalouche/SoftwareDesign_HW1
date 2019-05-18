package il.ac.technion.cs.softwaredesign.storage.channels

import il.ac.technion.cs.softwaredesign.storage.IStorageConvertable

class SecureChannelStorage : IChannelStorage {
    override fun getChannelIdByChannelName(channelName: String): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setChannelIdToChannelName(channelId: Long, channelName: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <T : IStorageConvertable<T>> getPropertyValueByChannelId(channelId: Long, property: String): T {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <T : IStorageConvertable<T>> setPropertyValueToChannelId(channelId: Long, property: String, value: T) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPropertyListByChannelId(userId: Long, property: String): List<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPropertyListToChannelId(userId: Long, property: String, listValue: List<Long>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}