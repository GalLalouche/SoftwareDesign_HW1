package il.ac.technion.cs.softwaredesign.managers

import com.google.inject.Inject
import il.ac.technion.cs.softwaredesign.storage.ISequenceGenerator
import il.ac.technion.cs.softwaredesign.storage.channels.IChannelStorage
import il.ac.technion.cs.softwaredesign.storage.utils.MANAGERS_CONSTS
import java.lang.IllegalArgumentException

class ChannelManager @Inject constructor(private val channelStorage: IChannelStorage,
                                         @ChannelIdSeqGenerator private val channelIdGenerator: ISequenceGenerator) : IChannelManager {

    override fun getChannelId(channelName: String): Long? {
        return channelStorage.getChannelIdByChannelName(channelName)
    }

    override fun add(channelName: String): Long {
        if (isChannelNameExists(channelName)) throw IllegalArgumentException("channel name already exist")
        val channelId = channelIdGenerator.next()
        channelStorage.setChannelIdToChannelName(channelName, channelId)
        channelStorage.setPropertyStringToChannelId(channelId, MANAGERS_CONSTS.CHANNEL_NAME_PROPERTY, channelName)
        channelStorage.setPropertyLongToChannelId(channelId, MANAGERS_CONSTS.CHANNEL_NAME_PROPERTY, channelName)

        return channelId
    }

    override fun remove(channelId: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

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