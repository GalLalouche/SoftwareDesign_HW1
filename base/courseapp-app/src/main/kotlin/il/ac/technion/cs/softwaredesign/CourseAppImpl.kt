package il.ac.technion.cs.softwaredesign

import com.google.inject.Inject
import java.lang.IllegalArgumentException
import java.math.BigInteger
import java.security.MessageDigest


/**
 * This is the class implementing CourseApp, a course discussion group system.
 *
 * You may assume that [CourseAppInitializer.setup] was called before this class was instantiated.
 *
 * Currently specified:
 * + User authentication.
 */
class CourseAppImpl @Inject constructor(private val storage: IStorageLayer) : CourseApp {
    companion object {
        private const val HASH_ALGORITHM = "MD5"
    }
//    private val storage=StorageLayer()
    private var tokenManager = TokenManager(storage)
    private var userManager = UserManager(storage)

    override fun login(username: String, password: String): String {
        val hashedPassword = password.hashString(HASH_ALGORITHM)
        if(userManager.isUsernameExists(username)){
            if (userManager.getUserStatus(username) == IStorageLayer.LoginStatus.IN)
                throw IllegalArgumentException("User is already logged in")
            else if (userManager.getUserPassword(username) != hashedPassword)
                throw IllegalArgumentException("Wrong password")
        }else{
            userManager.saveUser(username, hashedPassword,IStorageLayer.LoginStatus.IN)
        }
        //At this point user is surly exist we just need to create a token
        return tokenManager.assignTokenToUsername(username)
    }

    override fun logout(token: String): Unit {
        val username = tokenManager.getUsernameByToken(token)
        val password = userManager.getUserPassword(username)
        tokenManager.invalidateKnownUserToken(token)
        userManager.saveUser(username, password, IStorageLayer.LoginStatus.OUT)
    }

    override fun isUserLoggedIn(token: String, username: String): Boolean? {
        validateToken(token)
        if(!userManager.isUsernameExists(username)) return null
        return userManager.getUserStatus(username) == IStorageLayer.LoginStatus.IN

    }

    override fun makeAdministrator(token: String, username: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun channelJoin(token: String, channel: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun channelPart(token: String, channel: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun channelMakeOperator(token: String, channel: String, username: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun channelKick(token: String, channel: String, username: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isUserInChannel(token: String, channel: String, username: String): Boolean? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun numberOfActiveUsersInChannel(token: String, channel: String): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun numberOfTotalUsersInChannel(token: String, channel: String): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun String.hashString(hashAlgorithm: String): String {
        val positiveNumberSign = 1
        val numberBase = 16
        val hashFunc = MessageDigest.getInstance(hashAlgorithm)
        return BigInteger(positiveNumberSign, hashFunc.digest(this.toByteArray())).toString(numberBase).padStart(32, '0')
    }

    //the validation of the token takes place in the getUsernameByToken method for 'free'
    private fun validateToken(token: String) = tokenManager.getUsernameByToken(token)
}