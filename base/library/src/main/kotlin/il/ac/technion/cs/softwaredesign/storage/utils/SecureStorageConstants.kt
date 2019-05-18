@file:JvmName("SecureStorageConstants")
package il.ac.technion.cs.softwaredesign.storage.utils
object MANAGERS_CONSTS{
    internal const val DELIMITER = "_"
    const val INVALID_USER_ID=-1L
    const val USERNAME_PROPERTY="username"
    const val PASSWORD_PROPERTY="password"
    const val STATUS_PROPERTY="status"
    const val PRIVILAGE_PROPERTY="privilage"

    const val LIST_PROPERTY="list"
    const val SIZE_PROPERTY="size"

}
object TREE_CONST {
    internal const val DELIMITER = "_"
    internal const val SECURE_AVL_STORAGE_NUM_PROPERTIES = 5
    internal const val ROOT_KEY = "root"
    internal const val ROOT_INIT_INDEX=0L
    internal const val LAST_GENERATED_ID="LAST_GENERATED_ID"
}

object DB_NAMES {
    internal const val USER_ID = "USER_ID"
    internal const val TOKEN = "TOKEN"
    internal const val USER_DETAILS = "USER_DETAILS"
    internal const val USER_CHANNELS = "USER_CHANNELS"
    internal const val CHANNEL_ID = "CHANNEL_ID"
    internal const val CHANNEL_DETAILS = "CHANNEL_DETAILS"
    internal const val STATISTICS = "STATISTICS"
    //DATA BASES FOR TREES

    internal const val TREE_USER="AVL_TREE_USER"
    internal const val TREE_CHANNEL="AVL_TREE_CHANNEL"
    internal const val TREE_ACTIVE_CHANNEL="AVL_TREE_ACTIVE_CHANNEL"
}


object STATISTICS_KEYS{
    internal const val NUMBER_OF_USERS="numberOfUsers"
    internal const val NUMBER_OF_LOGGED_IN_USERS="numberOfLoggenInUsers"
    internal const val NUMBER_OF_CHANNELS="numberOfChannels"
    internal const val MAX_CHANNEL_INDEX="maxChannelIndex"
}

