package il.ac.technion.cs.softwaredesign.storage.channels

import il.ac.technion.cs.softwaredesign.storage.IStorageConvertable

interface IChannelStorage {
    fun getChannelIdByChannelName(channelName : String) : Long?
    fun setChannelIdToChannelName(channelId : Long, channelName : String)

    fun<T:IStorageConvertable<T>> getPropertyValueByChannelId(channelId : Long, property : String) : T?
    fun<T:IStorageConvertable<T>> setPropertyValueToChannelId(channelId : Long, property : String, value : T)

    fun getPropertyListByChannelId(userId : Long, property : String) : List<Long>?
    fun setPropertyListToChannelId(userId : Long, property : String, listValue : List<Long>)
}
