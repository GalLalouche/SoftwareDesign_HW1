package il.ac.technion.cs.softwaredesign.managers

import com.google.inject.Inject
import il.ac.technion.cs.softwaredesign.storage.ISequenceGenerator
import il.ac.technion.cs.softwaredesign.storage.channels.IChannelStorage
import il.ac.technion.cs.softwaredesign.storage.utils.MANAGERS_CONSTS
import il.ac.technion.cs.softwaredesign.storage.utils.MANAGERS_CONSTS.CHANNEL_INVALID_ID
import il.ac.technion.cs.softwaredesign.storage.utils.MANAGERS_CONSTS.CHANNEL_INVALID_NAME
import il.ac.technion.cs.softwaredesign.storage.utils.MANAGERS_CONSTS.CHANNEL_NAME_PROPERTY
import java.lang.IllegalArgumentException

class ChannelManager @Inject constructor(private val channelStorage: IChannelStorage,
                                         @ChannelIdSeqGenerator private val channelIdGenerator: ISequenceGenerator) : IChannelManager {

    override fun add(channelName: String): Long {
        if (channelName == CHANNEL_INVALID_NAME) throw IllegalArgumentException("channel name cannot be empty")
        if (isChannelNameExists(channelName)) throw IllegalArgumentException("channel name already exist")
        val channelId = channelIdGenerator.next()
        channelStorage.setChannelIdToChannelName(channelName, channelId)
        channelStorage.setPropertyStringToChannelId(channelId, CHANNEL_NAME_PROPERTY, channelName)
        channelStorage.setPropertyLongToChannelId(channelId, MANAGERS_CONSTS.CHANNEL_NR_MEMBERS, 0L)
        channelStorage.setPropertyLongToChannelId(channelId, MANAGERS_CONSTS.CHANNEL_NR_ACTIVE_MEMBERS, 0L)
        channelStorage.setPropertyListToChannelId(channelId, MANAGERS_CONSTS.CHANNEL_MEMBERS_LIST, emptyList())
        channelStorage.setPropertyListToChannelId(channelId, MANAGERS_CONSTS.CHANNEL_OPERATORS_LIST, emptyList())
        return channelId
    }

    override fun remove(channelId: Long) {
        invalidateChannel(channelId)
    }

    override fun remove(channelName : String) {
        invalidateChannel(channelName)
    }

    override fun isChannelNameExists(channelName: String): Boolean {
        return isChannelValid(channelName)
    }

    override fun isChannelIdExists(channelId: Long): Boolean {
        return isChannelValid(channelId)
    }

    override fun getId(channelName: String): Long {
        if (!isChannelValid(channelName = channelName)) throw IllegalArgumentException("channel name is not valid")
        val id = channelStorage.getChannelIdByChannelName(channelName)
                if (isChannelValid(channelId = id)) return id!! // TODO: redundant, consider removing it
                throw IllegalArgumentException("returned channel id is not valid")
    }

    override fun getName(channelId: Long): String {
        if (!isChannelValid(channelId = channelId)) throw IllegalArgumentException("channel id is not valid")
        val name = channelStorage.getPropertyStringByChannelId(channelId, CHANNEL_NAME_PROPERTY)
                if (isChannelValid(channelName = name)) return name!! // TODO: redundant, consider removing it
                throw IllegalArgumentException("returned channel name is not valid")
    }

    override fun getNumberOfActiveMembers(channelId: Long): Long {
        if (!isChannelValid(channelId = channelId)) throw IllegalArgumentException("channel id is not valid")
        return channelStorage.getPropertyLongByChannelId(channelId, MANAGERS_CONSTS.CHANNEL_NR_ACTIVE_MEMBERS)
                ?: throw IllegalArgumentException("channel id is valid but returned null")
    }

    override fun updateNumberOfActiveMembers(channelId: Long, value: Long) {
        if (!isChannelValid(channelId = channelId)) throw IllegalArgumentException("channel id is not valid")
        channelStorage.setPropertyLongToChannelId(channelId, MANAGERS_CONSTS.CHANNEL_NR_ACTIVE_MEMBERS, value)
    }

    override fun getNumberOfMembers(channelId: Long): Long {
        if (!isChannelValid(channelId = channelId)) throw IllegalArgumentException("channel id is not valid")
        return channelStorage.getPropertyLongByChannelId(channelId, MANAGERS_CONSTS.CHANNEL_NR_MEMBERS)
                ?: throw IllegalArgumentException("channel id is valid but returned null")
    }

    override fun updateNumberOfMembers(channelId: Long, value: Long) {
        if (!isChannelValid(channelId = channelId)) throw IllegalArgumentException("channel id is not valid")
        channelStorage.setPropertyLongToChannelId(channelId, MANAGERS_CONSTS.CHANNEL_NR_MEMBERS, value)
    }

    override fun getMembersList(channelId: Long): List<Long> {
        if (!isChannelValid(channelId = channelId)) throw IllegalArgumentException("channel id is not valid")
        return channelStorage.getPropertyListByChannelId(channelId, MANAGERS_CONSTS.CHANNEL_MEMBERS_LIST)
                ?: throw IllegalArgumentException("channel id does not exist")
    }

    override fun addMemberToChannel(channelId: Long, memberId: Long) {
        if (!isChannelValid(channelId = channelId)) throw IllegalArgumentException("channel id is not valid")
        val currentList = ArrayList<Long>(getMembersList(channelId))
        if (currentList.contains(memberId)) throw IllegalAccessException("member id already exists in channel")
        currentList.add(memberId)
        channelStorage.setPropertyListToChannelId(channelId, MANAGERS_CONSTS.CHANNEL_MEMBERS_LIST, currentList)
    }

    override fun removeMemberFromChannel(channelId: Long, memberId: Long) {
        if (!isChannelValid(channelId = channelId)) throw IllegalArgumentException("channel id is not valid")
        val currentList = ArrayList<Long>(getMembersList(channelId))
        if (!currentList.contains(memberId)) throw IllegalAccessException("member id does not exists in channel")
        currentList.remove(memberId)
        channelStorage.setPropertyListToChannelId(channelId, MANAGERS_CONSTS.CHANNEL_MEMBERS_LIST, currentList)
    }

    override fun getOperatorsList(channelId: Long): List<Long> {
        if (!isChannelValid(channelId = channelId)) throw IllegalArgumentException("channel id is not valid")
        return channelStorage.getPropertyListByChannelId(channelId, MANAGERS_CONSTS.CHANNEL_OPERATORS_LIST)
                ?: throw IllegalArgumentException("channel id does not exist")
    }

    override fun addOperatorToChannel(channelId: Long, operatorId: Long) {
        if (!isChannelValid(channelId = channelId)) throw IllegalArgumentException("channel id is not valid")
        val currentList = ArrayList<Long>(getMembersList(channelId))
        if (currentList.contains(operatorId)) throw IllegalAccessException("operator id already exists in channel")
        currentList.add(operatorId)
        channelStorage.setPropertyListToChannelId(channelId, MANAGERS_CONSTS.CHANNEL_OPERATORS_LIST, currentList)
    }

    override fun removeOperatorFromChannel(channelId: Long, operatorId: Long) {
        if (!isChannelValid(channelId = channelId)) throw IllegalArgumentException("channel id is not valid")
        val currentList = ArrayList<Long>(getMembersList(channelId))
        if (!currentList.contains(operatorId)) throw IllegalAccessException("operator id does not exists in channel")
        currentList.remove(operatorId)
        channelStorage.setPropertyListToChannelId(channelId, MANAGERS_CONSTS.CHANNEL_OPERATORS_LIST, currentList)
    }

    // channel name exists if and only if it is mapped to a VALID channel id, i.e. channel id != CHANNEL_INVALID_ID
    // and its id_name is not mapped to CHANNEL_INVALID_NAME
    private fun isChannelValid(channelId: Long?) : Boolean {
        if (channelId != null && channelId != CHANNEL_INVALID_ID) {
            val name = channelStorage.getPropertyStringByChannelId(channelId, CHANNEL_NAME_PROPERTY)
            return name != null && name != CHANNEL_INVALID_NAME
        }
        return false
    }
    private fun isChannelValid(channelName: String?) : Boolean {
        if (channelName != null && channelName != CHANNEL_INVALID_NAME) {
            val id = channelStorage.getChannelIdByChannelName(channelName)
            return id != null && id != CHANNEL_INVALID_ID
        }
        return false
    }

    private fun invalidateChannel(channelId: Long) {
        try {
            val channelName = getName(channelId)
            invalidateChannel(channelId, channelName)
        } catch (e : IllegalArgumentException) {}
    }
    private fun invalidateChannel(channelName: String) {
        try {
            val channelId = getId(channelName)
            invalidateChannel(channelId, channelName)
        } catch (e : IllegalArgumentException) {}
    }
    // if you can, use this overload
    private fun invalidateChannel(channelId: Long, channelName: String) {
        channelStorage.setChannelIdToChannelName(channelName, CHANNEL_INVALID_ID)
        channelStorage.setPropertyStringToChannelId(channelId, CHANNEL_NAME_PROPERTY, CHANNEL_INVALID_NAME)
    }
}