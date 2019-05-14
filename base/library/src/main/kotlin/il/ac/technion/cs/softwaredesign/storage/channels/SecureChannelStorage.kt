package il.ac.technion.cs.softwaredesign.storage.channels

class SecureChannelStorage : IChannelStorage {
    override fun getChannelIdByChannelName(channelName: String): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setChannelIdToChannelName(channelId: Long, channelName: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPropertyStringValueByChannelId(channelId: Long, property: String): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPropertyStringValueToChannelId(channelId: Long, property: String, value: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPropertyLongValueByChannelId(channelId: Long, property: String): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPropertyLongValueToChannelId(channelId: Long, property: String, value: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPropertyListByChannelId(userId: Long, property: String): List<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPropertyListToChannelId(userId: Long, property: String, listValue: List<Long>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}