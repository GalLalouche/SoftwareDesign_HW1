package il.ac.technion.cs.softwaredesign

import java.lang.IllegalArgumentException

class UserManager(private val storageLayer: IStorageLayer) {
    companion object{
         const val USER_NOT_EXIST="User does not exist"
    }
    /**
     * Add a new user to the system.
     * @param username String the user name of the user
     * @param password String the password of the user
     * @param status IStorageLayer.LoginStatus is logged in as default
     */
    fun saveUser(username : String, password : String, status:IStorageLayer.LoginStatus=IStorageLayer.LoginStatus.IN) {
        storageLayer.writeUsernameToPasswordStatus(username, password, status)
    }

    /**
     * Check if username already exist in the system
     * @param username String
     * @return Boolean - true if exist, false if not
     */
    fun isUsernameExists(username : String) : Boolean {
        return storageLayer.readPasswordStatusOfUsername(username) != null
    }

    /**
     * Get the password of the given username from the storage layer
     * @param username String
     * @throws IllegalArgumentException if username does not exist in the system
     * @return String - the password if username exists
     */
    fun getUserPassword(username: String) : String {
        val userPwdStatus = storageLayer.readPasswordStatusOfUsername(username)
        return userPwdStatus?.first ?: throw IllegalArgumentException(USER_NOT_EXIST)
    }

    /**
     * Gets the user status from the storage layer
     * @param username String user name of the user
     * @throws IllegalArgumentException if username does not exist in the system
     * @return IStorageLayer.LoginStatus
     */
    fun getUserStatus(username:String) : IStorageLayer.LoginStatus {
        val status = storageLayer.readPasswordStatusOfUsername(username)?.second
        return status ?: throw IllegalArgumentException(USER_NOT_EXIST)
    }

    /**
     * Gets the user status from the storage layer
     * @param username String user name of the user
     * @return Pair of password, IStorageLayer.LoginStatus if username exist, null otherwise
     */
    fun getUserPasswordStatus(username:String) : Pair<String, IStorageLayer.LoginStatus>? {
        return storageLayer.readPasswordStatusOfUsername(username)
    }
}