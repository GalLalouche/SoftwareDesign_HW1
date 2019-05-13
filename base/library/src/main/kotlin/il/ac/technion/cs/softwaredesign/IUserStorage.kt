package il.ac.technion.cs.softwaredesign

interface IUserStorage {
    fun getUserIdByUsername(username : String) : Long

//    fun getUserIdByToken
}