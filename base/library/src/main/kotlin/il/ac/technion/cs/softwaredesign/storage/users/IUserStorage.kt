package il.ac.technion.cs.softwaredesign.storage.users

interface IUserStorage {
    fun getUserIdByUsername(username : String) : Long?
    fun setUserIdToUsername(userId : Long, username : String)

    fun getUserIdByToken(token : String) : Long?
    fun setUserIdToToken(userId : Long, token : String)

    fun getPropertyStringByUserId(userId : Long, property : String) : String?
    fun setPropertyStringToUserId(userId : Long, property : String, value : String)

    fun getPropertyLongByUserId(userId : Long, property : String) :Long?
    fun setPropertyLongToUserId(userId : Long, property : String, value : Long)

    fun getPropertyListByUserId(userId : Long, property : String) : MutableList<Long>?
    fun setPropertyListToUserId(userId : Long, property : String, listValue : MutableList<Long>)
}