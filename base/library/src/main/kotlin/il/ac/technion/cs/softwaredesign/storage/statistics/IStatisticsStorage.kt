package il.ac.technion.cs.softwaredesign.storage.api

interface IStatisticsStorage {
    fun getLongValue(key : String) : Long?
    fun setLongValue(key : String, value : Long)
}