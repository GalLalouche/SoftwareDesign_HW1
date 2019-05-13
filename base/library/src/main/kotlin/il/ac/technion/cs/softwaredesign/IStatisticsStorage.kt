package il.ac.technion.cs.softwaredesign

interface IStatisticsStorage {
    fun getPropertyLongValue(property : String) : Long
    fun setPropertyLongValue(property : String, value : Long) : Unit

    fun getPropertyIndexListValue(property : String, index : Long) : List<Long>
    fun setPropertyIndexListValue(property : String, index : Long, listValue : List<Long>) : Unit
}