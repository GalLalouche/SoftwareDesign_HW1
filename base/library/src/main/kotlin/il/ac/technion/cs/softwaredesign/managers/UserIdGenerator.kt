package il.ac.technion.cs.softwaredesign.managers

import il.ac.technion.cs.softwaredesign.internals.ISequenceGenerator
import il.ac.technion.cs.softwaredesign.storage.statistics.IStatisticsStorage
import il.ac.technion.cs.softwaredesign.storage.utils.STATISTICS_KEYS.NUMBER_OF_USERS
import java.lang.NullPointerException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserIdGenerator @Inject constructor(private val statisticsStorage: IStatisticsStorage) : ISequenceGenerator {
    override fun next(): Long {
        val currentValue = statisticsStorage.getLongValue(NUMBER_OF_USERS) ?: throw NullPointerException("Number of users must be valid key")
        val newValue = currentValue+1L
        statisticsStorage.setLongValue(NUMBER_OF_USERS, newValue)
        return newValue
    }
}