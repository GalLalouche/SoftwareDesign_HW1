package il.ac.technion.cs.softwaredesign.storage.channels

import il.ac.technion.cs.softwaredesign.storage.IStorageConvertable

class SecureChannelStorage : IChannelStorage {
    override fun getChannelIdByChannelName(channelName: String): Long? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setChannelIdToChannelName(channelId: Long, channelName: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPropertyStringByChannelId(userIdKey: Long, property: String): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPropertyStringToChannelId(userIdKey: Long, property: String, value: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPropertyLongByUserId(userIdKey: Long, property: String): Long? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPropertyLongToUserId(userIdKey: Long, property: String, value: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPropertyListByChannelId(userId: Long, property: String): List<Long>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPropertyListToChannelId(userId: Long, property: String, listValue: List<Long>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}