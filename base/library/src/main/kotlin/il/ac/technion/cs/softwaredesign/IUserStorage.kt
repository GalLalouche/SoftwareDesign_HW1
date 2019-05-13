package il.ac.technion.cs.softwaredesign

interface IUserStorage {
    fun getUserIdByUsername(username : String) : Long
    fun setUserIdToUsername(userId : Long, username : String) : Unit

    fun getUserIdByToken(token : String) : Long
    fun setUserIdToToken(userId : Long, token : String) : Unit

    fun getPropertyValueByUserId(userId : Long, property : String) : String
    fun setPropertyValueToUserId(userId : Long, property : String, value : String) : Unit

    fun getPropertyListByUserId(userId : Long, property : String) : List<Long>
    fun setPropertyListToUserId(userId : Long, property : String, listValue : List<Long>) : Unit
}