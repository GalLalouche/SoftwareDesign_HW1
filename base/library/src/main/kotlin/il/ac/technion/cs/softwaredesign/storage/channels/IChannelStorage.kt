package il.ac.technion.cs.softwaredesign.storage.channels

interface IChannelStorage {
    fun getChannelIdByChannelName(channelName : String) : Long?
    fun setChannelIdToChannelName(channelNameKey: String, channelId: Long)

    fun getPropertyStringByChannelId(channelIdKey: Long, property: String) : String?
    fun setPropertyStringToChannelId(channelIdKey: Long, property: String, value: String)

    fun getPropertyLongByUserId(channelIdKey: Long, property: String) :Long?
    fun setPropertyLongToUserId(channelIdKey: Long, property: String, value: Long)

    fun getPropertyListByChannelId(channelIdKey: Long, property: String) : List<Long>?
    fun setPropertyListToChannelId(channelIdKey: Long, property: String, listValue: List<Long>)
}
