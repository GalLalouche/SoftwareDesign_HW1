package il.ac.technion.cs.softwaredesign.storage.users

interface IUserStorage {
    fun getUserIdByUsername(username : String) : Long
    fun setUserIdToUsername(userId : Long, username : String)

    fun getUserIdByToken(token : String) : Long
    fun setUserIdToToken(userId : Long, token : String)

    fun getPropertyValueByUserId(userId : Long, property : String) : String
    fun setPropertyValueToUserId(userId : Long, property : String, value : String)

    fun getPropertyListByUserId(userId : Long, property : String) : List<Long>
    fun setPropertyListToUserId(userId : Long, property : String, listValue : List<Long>)
}