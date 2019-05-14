package il.ac.technion.cs.softwaredesign.storage.statistics

interface IStatisticsStorage {
    fun getPropertyLongValue(property : String) : Long
    fun setPropertyLongValue(property : String, value : Long)

    fun getPropertyIndexListValue(property : String, index : Long) : List<Long>
    fun setPropertyIndexListValue(property : String, index : Long, listValue : List<Long>)
}