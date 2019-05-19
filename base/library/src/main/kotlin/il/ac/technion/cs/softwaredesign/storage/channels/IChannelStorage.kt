package il.ac.technion.cs.softwaredesign.storage.channels



interface IChannelStorage {
    fun getChannelIdByChannelName(channelName : String) : Long?
    fun setChannelIdToChannelName(channelId : Long, channelName : String)

    fun getPropertyStringByChannelId(userIdKey : Long, property : String) : String?
    fun setPropertyStringToChannelId(userIdKey : Long, property : String, value : String)

    fun getPropertyLongByUserId(userIdKey : Long, property : String) :Long?
    fun setPropertyLongToUserId(userIdKey : Long, property : String, value : Long)

    fun getPropertyListByChannelId(userId : Long, property : String) : List<Long>?
    fun setPropertyListToChannelId(userId : Long, property : String, listValue : List<Long>)
}
