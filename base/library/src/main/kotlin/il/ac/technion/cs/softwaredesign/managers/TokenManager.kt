package il.ac.technion.cs.softwaredesign.managers

import il.ac.technion.cs.softwaredesign.storage.api.ITokenManager
import il.ac.technion.cs.softwaredesign.storage.users.IUserStorage
import il.ac.technion.cs.softwaredesign.storage.utils.MANAGERS_CONSTS.INVALID_USER_ID
import java.security.SecureRandom
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(private val userStorage: IUserStorage) : ITokenManager {

    override fun isTokenValid(token: String): Boolean {
        val userId = userStorage.getUserIdByToken(token) ?: return false
        if (userId == INVALID_USER_ID) return false
        return true
    }

    override fun getUserIdByToken(token: String): Long? {
        val userId = userStorage.getUserIdByToken(token)
        if (userId == null || userId == INVALID_USER_ID) return null
        return userId
    }

    override fun assignTokenToUserId(userId: Long): String {
        if (userId == INVALID_USER_ID) throw IllegalArgumentException("User id is not valid")
        val token = generateValidUserToken()
        userStorage.setUserIdToToken(token, userId)
        return token
    }


    override fun invalidateUserToken(token: String) {
        getUserIdByToken(token) ?: throw java.lang.IllegalArgumentException("token does not exist")
        userStorage.setUserIdToToken(token, INVALID_USER_ID)
    }

    private fun isTokenUnique(token: String): Boolean {
        return getUserIdByToken(token) == null
    }

    /**
     * Generate 8 byte token for user and return it
     * @return String
     */
    private fun generateUserToken(): String {
        val random = SecureRandom()
        val bytes = ByteArray(8)
        random.nextBytes(bytes)
        return bytes.toString()
    }

    /**
     * Generate token that does not exist in the persistent memory
     * @return String token
     */
    private fun generateValidUserToken(): String {
        var token: String
        var it = 0
        do {
            token = generateUserToken()
            if (it > 30) {
                break
            }
            it += 1
        } while (!isTokenUnique(token))
        return token
    }
}