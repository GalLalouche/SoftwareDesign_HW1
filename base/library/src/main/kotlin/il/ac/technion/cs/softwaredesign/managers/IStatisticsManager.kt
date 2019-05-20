package il.ac.technion.cs.softwaredesign.managers

interface IStatisticsManager {
    /** PRIMITIVE STATISTICS **/
    /**
     * get number of total users in the system
     * @return Long
     */
    fun getTotalUsers() : Long
    // fun setTotalUsers(value : Long) : Long

    /**
     * get number of total logged in users in the system
     * @return Long
     */
    fun getLoggedInUsers() : Long
    // fun setLoggedInUsers(value : Long)

    /**
     * get number of channels in the system
     * @return Long
     */
    fun getNumberOfChannels() : Long

    //done by user id generator
//    /**
//     * Increase number of users in the system by [count]
//     * @param count Int, increase value by count
//     * @return Long, the updated value
//     */
//    fun increaseTotalUsersBy(count : Int = 1) : Long
//
//    /**
//     * Decrease number of users in the system by [count]
//     * @param count Int, decrease value by count
//     * @return Long, the updated value
//     */
//    fun decreaseTotalUsersBy(count : Int = 1) : Long

    /**
     * Increase number of logged in users in the system by [count]
     * @param count Int, increase value by count
     * @return Long, the updated value
     */
    fun increaseLoggedInUsersBy(count : Int = 1) : Long

    /**
     * Decrease number of logged in users in the system by [count]
     * @param count Int, decrease value by count
     * @return Long, the updated value
     */
    fun decreaseLoggedInUsersBy(count : Int = 1) : Long

    /**
     * Increase number of channels in the system by [count]
     * @param count Int, increase value by count
     * @return Long, the updated value
     */
    fun increaseNumberOfChannelsBy(count : Int = 1) : Long

    /**
     * Decrease number of channels in the system by [count]
     * @param count Int, decrease value by count
     * @return Long, the updated value
     */
    fun decreaseNumberOfChannelsBy(count : Int = 1) : Long
}