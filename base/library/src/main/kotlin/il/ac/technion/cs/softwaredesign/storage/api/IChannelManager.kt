package il.ac.technion.cs.softwaredesign.storage.api

import java.lang.IllegalArgumentException

interface IChannelManager {
    /**
     * Add new channel to the system
     * @param channelName String
     * @throws IllegalArgumentException if channelName is already exist
     * @return Long, channel id
     */
    fun addChannel(channelName: String): Long

    /**
     * Remove channel from the system
     * @param channelId Long
     */
    fun removeChannel(channelId : Long)

    /**
     * Check if channelName already exist in the system
     * @param channelName String
     * @return Boolean - true if exist, false if not
     */
    fun isChannelNameExists(channelName : String) : Boolean

    /**
     * Check if channel id already exist in the system
     * @param channelId Long
     * @return Boolean - true if exist, false if not
     */
    fun isChannelIdExists(channelId : Long) : Boolean

    /**
     * Get channelId that match channelName
     * @param channelName String
     * @throws IllegalArgumentException if channelName does not exist
     * @return Long, channel id
     */
    fun getChannelIdByName(channelName : String) : Long

    /**
     * Get channel name
     * @param channelId Long
     * @throws IllegalArgumentException throws if channel id does not exist in the system
     * @return String, channel name
     */
    fun getChannelNameById(channelId : Long) : String

    /**
     * get number of channels in the system
     * @return Long
     */
    fun getNumberOfChannels() : Long


    /** NUMBER OF ACTIVE MEMBERS **/
    /** this property should be updated regardless members list updates **/
    /**
     * Get the number of active members in a specific channel
     * @param channelId Long
     * @throws IllegalArgumentException throws if channel id does not exist in the system
     * @return Long, number of active members
     */
    fun getNumberOfActiveMembersInChannel(channelId : Long) : Long

    /**
     * increase the number of active members in a specific channel by [count]
     * @param channelId Long
     * @param count Long
     * @throws IllegalArgumentException throws if channel id does not exist in the system
     */
    fun increaseNumberOfActiveMembersInChannelBy(channelId: Long, count: Long = 1L)

    /**
     * decrease the number of active members in a specific channel by [count]
     * @param channelId Long
     * @param count Long
     * @throws IllegalArgumentException throws if channel id does not exist in the system
     */
    fun decreaseNumberOfActiveMembersInChannelBy(channelId: Long, count: Long = 1L)


    /** MEMBERS LIST **/
    /**
     * Get the number of total members in a specific channel
     * @param channelId Long
     * @throws IllegalArgumentException throws if channel id does not exist in the system
     * @return Long, number of total members
     */
    fun getNumberOfMembersInChannel(channelId : Long) : Long

    /**
     * gets members list of a specific channel
     * @param channelId channel Id
     * @throws IllegalArgumentException throws if channelId does not exist in the system
     * @return ids of the members of current channel
     */
    fun getChannelMembersList(channelId: Long) : List<Long>

    /**
     * add a member to a specific channel
     * Important: this function assumes that member id is a valid user
     * @param channelId channel Id
     * @param memberId member Id
     * @throws IllegalArgumentException throws if channel Id does not exist in the system
     * @throws IllegalAccessException throws if memberId already exists in channel
     */
    fun addMemberToChannel(channelId:Long, memberId:Long)

    /**
     * removes a member from a specific channel
     * Important: this function assumes that member id is a valid user
     * @param channelId channel Id
     * @param memberId member Id
     * @throws IllegalArgumentException throws if channel Id or member id does not exist in the system
     * @throws IllegalAccessException throws if memberId does not exist in channel
     */
    fun removeMemberFromChannel(channelId: Long,memberId: Long)


    /** OPERATORS LIST **/
    /**
     * gets operators list of a specific channel
     * @param channelId channel Id
     * @throws IllegalArgumentException throws if channelId does not exist in the system
     * @return ids of the operators of current channel
     */
    fun getChannelOperatorsList(channelId: Long) : List<Long>

    /**
     * add an operators to a specific channel
     * Important: this function assumes that member id is a valid user
     * @param channelId channel Id
     * @param operatorId operators Id
     * @throws IllegalArgumentException throws if channel Id does not exist in the system
     */
    fun addOperatorToChannel(channelId:Long, operatorId:Long)

    /**
     * removes an operators from a specific channel
     * Important: this function assumes that member id is a valid user
     * @param channelId channel Id
     * @param operatorId operators Id
     * @throws IllegalArgumentException throws if channel Id does not exist in the system
     */
    fun removeOperatorFromChannel(channelId: Long, operatorId: Long)


    /** CHANNEL COMPLEX STATISTICS **/
    /**
     * Get a list contains 10 best channels by users count (or less than 10 if nr of total channels < 10)
     * @return List<String> of channel names
     */
    fun getTop10ChannelsByUsersCount() : List<String>

    /**
     * Get a list contains 10 best channels by active users count (or less than 10 if nr of total channels < 10)
     * @return List<String> of channel names
     */
    fun getTop10ChannelsByActiveUsersCount() : List<String>
}