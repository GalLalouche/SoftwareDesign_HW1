package il.ac.technion.cs.softwaredesign.storage.statistics

import com.google.inject.Inject
import il.ac.technion.cs.softwaredesign.storage.SecureStorageFactory
import il.ac.technion.cs.softwaredesign.storage.utils.ConversionUtils
import il.ac.technion.cs.softwaredesign.storage.utils.DB_NAMES

class SecureStatisticsStorage @Inject constructor(secureStorageFactory: SecureStorageFactory) : IStatisticsStorage{
    private val statisticsStorage= secureStorageFactory.open(DB_NAMES.STATISTICS.toByteArray())

    override fun getLongValue(key: String): Long? {
        val byteArrayValue=statisticsStorage.read(key.toByteArray()) ?: return null
        return ConversionUtils.bytesToLong(byteArrayValue)
    }

    override fun setLongValue(key: String, value: Long) {
        statisticsStorage.write(key.toByteArray(),ConversionUtils.longToBytes(value))
    }
}