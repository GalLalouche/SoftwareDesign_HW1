@file:JvmName("CourseAppConstants")
package il.ac.technion.cs.softwaredesign

object DB_NAMES {
    internal const val USER_ID = "USER_ID"
    internal const val TOKEN = "TOKEN"
    internal const val USER_DETAILS = "USER_DETAILS"
    internal const val CHANNEL_ID = "CHANNEL_ID"
    internal const val CHANNEL_DETAILS = "CHANNEL_DETAILS"
    internal const val STATISTICS = "STATISTICS"
    //DATA BASES FOR TREES

    internal const val TREE_USERS_BY_CHANNELS_COUNT="USERS_BY_CHANNELS_COUNT"
    internal const val TREE_CHANNELS_BY_USERS_COUNT="CHANNELS_BY_USERS_COUNT"
    internal const val TREE_CHANNELS_BY_ACTIVE_USERS_COUNT="CHANNELS_BY_ACTIVE_USERS_COUNT"
}


object STATISTICS_KEYS{
    internal const val NUMBER_OF_USERS="numberOfUsers"
    internal const val NUMBER_OF_LOGGED_IN_USERS="numberOfLoggenInUsers"
    internal const val NUMBER_OF_CHANNELS="numberOfChannels"
    internal const val MAX_CHANNEL_INDEX="maxChannelIndex"

    internal const val INIT_INDEX_VAL=0L
}

object TREE_KEYS{
    internal const val ROOT_KEY="root"
    internal const val ROOT_INIT_INDEX=0L //DONT FORGET TO UPDATE IN SecureStorageConstants too

}
object ALGORITHEMS{
    internal const val HASH_ALGORITHM = "MD5"
}



