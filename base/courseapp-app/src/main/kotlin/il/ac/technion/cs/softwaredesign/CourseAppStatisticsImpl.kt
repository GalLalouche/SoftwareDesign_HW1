package il.ac.technion.cs.softwaredesign

import il.ac.technion.cs.softwaredesign.storage.api.IChannelManager
import il.ac.technion.cs.softwaredesign.storage.api.IUserManager
import javax.inject.Inject

class CourseAppStatisticsImpl @Inject constructor(private val userManager: IUserManager,
                                                  private val channelManager: IChannelManager) : CourseAppStatistics {
    override fun totalUsers(): Long {
        return userManager.getTotalUsers()
    }

    override fun loggedInUsers(): Long {
        return userManager.getLoggedInUsers()
    }

    override fun top10ChannelsByUsers(): List<String> {
        return channelManager.getTop10ChannelsByUsersCount()
    }

    override fun top10ActiveChannelsByUsers(): List<String> {
        return channelManager.getTop10ChannelsByActiveUsersCount()
    }

    override fun top10UsersByChannels(): List<String> {
        return userManager.getTop10UsersByChannelsCount()
    }
}