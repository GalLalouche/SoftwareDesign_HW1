package il.ac.technion.cs.softwaredesign.managers

import il.ac.technion.cs.softwaredesign.internals.ISequenceGenerator
import il.ac.technion.cs.softwaredesign.storage.statistics.IStatisticsStorage
import il.ac.technion.cs.softwaredesign.storage.utils.STATISTICS_KEYS.MAX_CHANNEL_INDEX
import java.lang.NullPointerException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChannelIdGenerator @Inject constructor(private val statisticsStorage: IStatisticsStorage) : ISequenceGenerator {
    override fun next(): Long {
        val currentValue = statisticsStorage.getLongValue(MAX_CHANNEL_INDEX) ?: throw NullPointerException("Number of channels must be valid key")
        val newValue = currentValue+1L
        statisticsStorage.setLongValue(MAX_CHANNEL_INDEX, newValue)
        return newValue
    }
}