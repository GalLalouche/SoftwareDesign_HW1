package il.ac.technion.cs.softwaredesign.storage.statistics

import il.ac.technion.cs.softwaredesign.storage.IStorageConvertable

interface IStatisticsStorage {
    fun getLongValue(key : String) : Long?
    fun setLongValue(key : String, value : Long)
}