package il.ac.technion.cs.softwaredesign

interface IChannelStorage {
    fun getChannelIdByChannelName(channelName : String) : Long
    fun setChannelIdToChannelName(channelId : Long, channelName : String) : Unit

    fun getPropertyStringValueByChannelId(channelId : Long, property : String) : String
    fun setPropertyStringValueToChannelId(channelId : Long, property : String, value : String) : Unit

    fun getPropertyLongValueByChannelId(channelId : Long, property : String) : Long
    fun setPropertyLongValueToChannelId(channelId : Long, property : String, value : Long) : Unit

    fun getPropertyListByChannelId(userId : Long, property : String) : List<Long>
    fun setPropertyListToChannelId(userId : Long, property : String, listValue : List<Long>) : Unit
}