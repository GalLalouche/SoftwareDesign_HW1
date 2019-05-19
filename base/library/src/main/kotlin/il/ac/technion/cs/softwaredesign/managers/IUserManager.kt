package il.ac.technion.cs.softwaredesign.managers

import il.ac.technion.cs.softwaredesign.storage.utils.MANAGERS_CONSTS
import java.lang.IllegalArgumentException


interface IUserManager {
    enum class LoginStatus{
        OUT,
        IN
    }
    enum class PrivilegeLevel{
        USER,
        ADMIN
    }

    /**
     * gets the user id from the system
     * @param username user name
     * @return user id or null if user does not exist in the system
     */
    fun getUserId(username:String):Long?

    /**
     * Add a new user to the system.
     * @param username String the user name of the user
     * @param password String the password of the user
     * @param status IStorageLayer.LoginStatus is logged in as default
     * @throws IllegalArgumentException if username is already exist
     * @return user id that was added to the system
     */
    fun add(username: String, password: String, status:LoginStatus = LoginStatus.IN, privilege:PrivilegeLevel=PrivilegeLevel.USER):Long

    /**
     * updates a user privilege
     * @param userId String the user name of the user
     * @param privilege privilege of the user
     */
    fun updatePrivilege(userId: Long, privilege: PrivilegeLevel)

    /**
     * gets the user privilege
     * @param userId user id
     * @throws IllegalArgumentException throws if user id does not exist in the system
     * @return the user privilege
     */
    fun getPrivilege(userId:Long):PrivilegeLevel

    /**
     * updates a user privilege
     * @param username String the user name of the user
     * @param status status of the user
     */
    fun updateStatus(userId:Long, status:LoginStatus)

    /**
     * gets the user status
     * @param userId user id
     * @throws IllegalArgumentException throws if user id does not exist in the system
     * @return login status of user
     */
    fun getStatus(userId:Long):LoginStatus


    /**
     * Check if username already exist in the system
     * @param username String
     * @return Boolean - true if exist, false if not
     */
    fun isUsernameExists(username : String) : Boolean

    /**
     * Check if user id already exist in the system
     * @param username String
     * @return Boolean - true if exist, false if not
     */
    fun isUserIdExists(userId : Long) : Boolean

    /**
     * Get the password of the given user id
     * @param userId String
     * @throws IllegalArgumentException if user id does not exist in the system
     * @return String - the password if username exists
     */
    fun getUserPassword(userId: Long) : String

    /**
     * gets the channel list of a specific user
     * @param userId user id
     * @throws IllegalArgumentException throws if user id does not exist in the system
     * @return ids of the channels
     */
    fun getChannelListOfUser(userId: Long): List<Long>

    /**
     * add a channel to a specific user
     * @param userId user id
     * @param channelId channel id
     * @throws IllegalArgumentException throws if user id does not exist in the system
     */
    fun addChannelToUser(userId:Long, channelId:Long)

    /**
     * removes a channel to a specific user
     * @param userId user id
     * @param channelId channel id
     * @throws IllegalArgumentException throws if user id does not exist in the system
     */
    fun removeChannelFromUser(userId: Long,channelId: Long)

    /**
     * updates the channel size of user id
     * @param userId user id
     * @param size new size
     */
    fun updateChannelListSize(userId: Long, size: Long)

    /**
     * gets the channel list size of the user
     * @param userId user id
     * @throws IllegalArgumentException throws if user id does not exist in the system
     * @return size of the channel list
     */
    fun getChannelListSize(userId: Long):Long
}