package il.ac.technion.cs.softwaredesign

import il.ac.technion.cs.softwaredesign.storage.SecureStorageModule

interface IStorageLayer {
    enum class LoginStatus{
        OUT,
        IN
    }
    /**
     * writes the username,password,token to storage layer
     * @param username String the user name
     * @param password String the password
     * @param token String the token
     */
    fun writeUsernameToPasswordStatus(username: String, password: String, status: LoginStatus)
    /**
     * writes the token and username to the storageLayer
     * if user already exist the data is overridden
     * @param token String the token
     * @param username String the username
     */
    fun writeTokenToUsername(token: String, username: String)
    /**
     * read the username from the storage layer based on the token.
     * @param token String token of the user
     * @return String? the username , null if no such token exist in the storage layer
     */
    fun readUsernameOfToken(token: String) : String?

    /**
     * read pair (password,LoginStatus) from the storage layer using the key username
     *
     * @param username String the key
     * @return Pair<String, String>? the password,token pair , null if no such username exist in the storage layer
     */
    fun readPasswordStatusOfUsername(username: String) : Pair<String, LoginStatus>?
}

/**
 * The StorageLayer singleton is implementing a Storage Layer
 */
class StorageLayer : IStorageLayer{
    companion object {
        //const that separates between password and status in the storage
        private const val delimiter=','
        private const val capacity=10000
    }
    //usernameToPasswordStatusPair and tokenToUsername are write through caches
    private val usernameToPasswordStatusPair= LRUCache<String,Pair<String, IStorageLayer.LoginStatus>>(capacity)
    private val tokenToUserName= LRUCache<String,String>(capacity)

    override fun writeUsernameToPasswordStatus(username: String, password: String, status: IStorageLayer.LoginStatus) {
        val pwdStatus = password + delimiter + status.ordinal //we separate the password and token as a standard with '_'

        write(username.toByteArray(Charsets.UTF_8), pwdStatus.toByteArray(Charsets.UTF_8))
        usernameToPasswordStatusPair[username] = Pair(password,status)
    }

    override fun writeTokenToUsername(token: String, username: String) {
        write(token.toByteArray(Charsets.UTF_8), username.toByteArray(Charsets.UTF_8))
        tokenToUserName[token]=username
    }

    override fun readUsernameOfToken(token: String) : String? {
        val cachedValue=tokenToUserName[token]
        if(cachedValue!=null) return cachedValue
        return convertByteArrayToString(read(token.toByteArray(Charsets.UTF_8)))
    }

    override fun readPasswordStatusOfUsername(username: String) : Pair<String, IStorageLayer.LoginStatus>? {
        val cachedValue=usernameToPasswordStatusPair[username]
        if(cachedValue!=null) return cachedValue
        val output = convertByteArrayToString(read(username.toByteArray(Charsets.UTF_8)))
        val pwdToken = output?.split(delimiter, limit = 2) //we splits according to the first '_' in the string
        return extractPasswordAndStatusToPair(pwdToken)
    }

    private fun extractPasswordAndStatusToPair(pwdToken: List<String>?): Pair<String, IStorageLayer.LoginStatus>? {
        return if (pwdToken != null)
            Pair(
                    pwdToken[0],
                    IStorageLayer.LoginStatus.values()[pwdToken[1].toInt()]
            )
        else null
    }

    private fun convertByteArrayToString(byteArray:ByteArray?):String?{
        return  if(byteArray!=null)  String(byteArray,Charsets.UTF_8) else null
    }
}