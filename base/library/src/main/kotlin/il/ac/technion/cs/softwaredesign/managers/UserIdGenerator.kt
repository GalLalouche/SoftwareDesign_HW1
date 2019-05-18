package il.ac.technion.cs.softwaredesign.managers

import il.ac.technion.cs.softwaredesign.storage.ISequenceGenerator
import il.ac.technion.cs.softwaredesign.storage.statistics.IStatisticsStorage
import il.ac.technion.cs.softwaredesign.storage.utils.LongStorageable
import il.ac.technion.cs.softwaredesign.storage.utils.STATISTICS_KEYS.NUMBER_OF_USERS
import java.lang.NullPointerException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserIdGenerator @Inject constructor(private val statisticsStorage: IStatisticsStorage) : ISequenceGenerator {

    override fun next(): Long {
        val currentValue = statisticsStorage.getValue<LongStorageable>(NUMBER_OF_USERS) ?: throw NullPointerException("Number of uses must be valid key")
        return currentValue.value+1L
    }
}