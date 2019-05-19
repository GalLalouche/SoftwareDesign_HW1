package il.ac.technion.cs.softwaredesign.managers

class LibraryManager {
    /** COMPLEX STATISTICS **/
    /** This functions used to update the data structures related to statistics **/

    /**
     * Update the new status of the user
     * @param userId Long
     * @param status IUserManager.LoginStatus to be updated
     * @throws
     */
    fun updateUserStatus(userId: Long, status: IUserManager.LoginStatus) {}

    /**
     * Update channel and user after user joined to channel
     * @param userId Long
     * @param channelId Long
     */
    fun joinUserToChannel(userId: Long, channelId: Long) {}

    /**
     * Update channel and user after user leaved the channel
     * @param userId Long
     * @param channelId Long
     */
    fun removeUserFromChannel(userId: Long, channelId: Long) {}
}