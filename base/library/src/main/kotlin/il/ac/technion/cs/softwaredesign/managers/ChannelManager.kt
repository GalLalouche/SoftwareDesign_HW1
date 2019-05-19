package il.ac.technion.cs.softwaredesign.managers

import com.google.inject.BindingAnnotation
import com.google.inject.Inject
import il.ac.technion.cs.softwaredesign.storage.ISequenceGenerator
import il.ac.technion.cs.softwaredesign.storage.channels.IChannelStorage

@Target(AnnotationTarget.CONSTRUCTOR, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
@BindingAnnotation
annotation class ChannelIdSeqGenerator

class ChannelManager @Inject constructor(private val channelStorage: IChannelStorage,
                                        @ChannelIdSeqGenerator private val channelIdGenerator: ISequenceGenerator) : IChannelManager {

    override fun getChannelId(channelName: String): Long? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun add(channelName: String): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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