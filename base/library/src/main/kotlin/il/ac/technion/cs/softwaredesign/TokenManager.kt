package il.ac.technion.cs.softwaredesign

import java.security.SecureRandom

class TokenManager(private val storageLayer: IStorageLayer){
    companion object{
        const val INVALID_USERNAME=""
        const val TOKEN_NOT_EXIST="Token does not exist"
        const val TOKEN_NOT_VALID="Token is not valid"
    }
    /**
     * Check if token is valid
     * Token is valid if and only if token exist in the system & was not invalidated
     * @param token String
     * @return Boolean
     */
    fun isTokenValid(token : String) : Boolean {
        val username =storageLayer.readUsernameOfToken(token)
        return username!=null && username!=INVALID_USERNAME
    }

    /**
     * Get the username that been assigned to the token
     * @param token String the token to search for in the system
     * @throws IllegalArgumentException if token does not exist or token is not valid
     * @return String username
     */
    fun getUsernameByToken(token: String) : String {
        val username = storageLayer.readUsernameOfToken(token) ?: throw IllegalArgumentException(TOKEN_NOT_EXIST)
        if(username==INVALID_USERNAME) throw IllegalArgumentException(TOKEN_NOT_VALID)
        return username
    }

    /**
     * Generate a new valid token and assign it to user
     * @param username String user name of the user
     * @throws IllegalArgumentException if username is not valid
     * @return token has been assigned to user
     */
    fun assignTokenToUsername(username : String) : String {
        if (username == INVALID_USERNAME) throw IllegalArgumentException("Empty username is not valid")
        val token = generateValidUserToken()
        // write to token -> username mapping:
        storageLayer.writeTokenToUsername(token, username)
        return token
    }

    /**
     * Invalidate user token
     * @param token String
     * @throws IllegalArgumentException if token does not belong to any user
     * @return the username that token has been assigned to it
     */
    fun invalidateUserToken(token : String) {
        getUsernameByToken(token)
        // invalidate token on token -> username mapping
        storageLayer.writeTokenToUsername(token, INVALID_USERNAME)
    }

    /**
     * Invalidate user token. user this function when getUsernameByToken called before this function
     * @param token String
     * @param username String
     * @throws IllegalArgumentException if token does not belong to any user
     * @return the username that token has been assigned to it
     */
    fun invalidateKnownUserToken(token : String) {
        // invalidate token on token -> username mapping
        storageLayer.writeTokenToUsername(token, INVALID_USERNAME)
    }

    /**
     * This function used by the library to generate unique tokens.
     * Check if token is valid for a new session
     * Token is unique if and only if there is no mapping from the token to any value
     * @param token String
     * @return Boolean
     */
    private fun isTokenUnique(token : String) : Boolean {
        return storageLayer.readUsernameOfToken(token) == null
    }

    /**
     * Generate 8 byte token for user and return it
     * @return String
     */
    private fun generateUserToken() : String {
        val random = SecureRandom()
        val bytes = ByteArray(8)
        random.nextBytes(bytes)
        return bytes.toString()
    }

    /**
     * Generate token that does not exist in the persistent memory
     * @return String token
     */
    private fun generateValidUserToken() : String {
        var token : String
        var it = 0;
        do {
            token = generateUserToken()
            if (it > 30) {
                System.out.println(it) // TODO: remove print
                break;
            }
            it += 1
        } while (!isTokenUnique(token))
        return token
    }
}