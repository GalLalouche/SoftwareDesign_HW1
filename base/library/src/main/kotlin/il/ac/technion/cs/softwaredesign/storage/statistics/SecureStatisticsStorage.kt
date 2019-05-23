package il.ac.technion.cs.softwaredesign.storage.statistics

import il.ac.technion.cs.softwaredesign.managers.StatisticsStorage
import il.ac.technion.cs.softwaredesign.storage.SecureStorage
import il.ac.technion.cs.softwaredesign.storage.utils.ConversionUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecureStatisticsStorage @Inject constructor(@StatisticsStorage private val statisticsStorage: SecureStorage) : IStatisticsStorage {

    override fun getLongValue(key: String): Long? {
        val byteArrayValue = statisticsStorage.read(key.toByteArray()) ?: return null
        return ConversionUtils.bytesToLong(byteArrayValue)
    }

    override fun setLongValue(key: String, value: Long) {
        statisticsStorage.write(key.toByteArray(), ConversionUtils.longToBytes(value))
    }
}