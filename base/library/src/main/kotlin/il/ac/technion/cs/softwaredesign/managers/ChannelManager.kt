package il.ac.technion.cs.softwaredesign.managers

import com.google.inject.Inject
import il.ac.technion.cs.softwaredesign.storage.ISequenceGenerator
import il.ac.technion.cs.softwaredesign.storage.channels.IChannelStorage
import il.ac.technion.cs.softwaredesign.storage.utils.MANAGERS_CONSTS
import il.ac.technion.cs.softwaredesign.storage.utils.STATISTICS_KEYS.INIT_INDEX_VAL
import java.lang.IllegalArgumentException

class ChannelManager @Inject constructor(private val channelStorage: IChannelStorage,
                                         @ChannelIdSeqGenerator private val channelIdGenerator: ISequenceGenerator) : IChannelManager {

    // channel name exists if and only if it is mapped to a VALID channel id, i.e. channel id != INIT_INDEX_VAL
    override fun getId(channelName: String): Long? {
        val id = channelStorage.getChannelIdByChannelName(channelName)
        if (id != null && id != INIT_INDEX_VAL) return id
        return null
    }

    override fun add(channelName: String): Long {
        if (isChannelNameExists(channelName)) throw IllegalArgumentException("channel name already exist")
        val channelId = channelIdGenerator.next()
        channelStorage.setChannelIdToChannelName(channelName, channelId)
        channelStorage.setPropertyStringToChannelId(channelId, MANAGERS_CONSTS.CHANNEL_NAME_PROPERTY, channelName)
        channelStorage.setPropertyLongToChannelId(channelId, MANAGERS_CONSTS.CHANNEL_NR_MEMBERS, 0L)
        channelStorage.setPropertyLongToChannelId(channelId, MANAGERS_CONSTS.CHANNEL_NR_ACTIVE_MEMBERS, 0L)
        channelStorage.setPropertyListToChannelId(channelId, MANAGERS_CONSTS.CHANNEL_MEMBERS_LIST, emptyList())
        channelStorage.setPropertyListToChannelId(channelId, MANAGERS_CONSTS.CHANNEL_OPERATORS_LIST, emptyList())
        return channelId
    }

    // remove channel name by mapping it to INIT_INDEX_VAL to indicate it is not valid anymore
    override fun remove(channelId: Long) {
        val channelName = getName(channelId)
        channelStorage.setChannelIdToChannelName(channelName, INIT_INDEX_VAL)
    }

    override fun remove(channelName : String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // channel name exists if and only if it is mapped to a VALID channel id, i.e. channel id != INIT_INDEX_VAL
    override fun isChannelNameExists(channelName: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isChannelIdExists(channelId: Long): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getName(channelId: Long): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getNumberOfActiveMembers(channelId: Long): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateNumberOfActiveMembers(channelId: Long, value: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getNumberOfMembers(channelId: Long): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateNumberOfMembers(channelId: Long, value: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMembersList(channelId: Long): List<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addMemberToChannel(channelId: Long, memberId: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeMemberFromChannel(channelId: Long, memberId: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getOperatorsList(channelId: Long): List<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addOperatorToChannel(channelId: Long, operatorsId: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeOperatorFromChannel(channelId: Long, operatorsId: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}