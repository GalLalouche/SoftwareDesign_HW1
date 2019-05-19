package il.ac.technion.cs.softwaredesign

import com.google.inject.Inject
import il.ac.technion.cs.softwaredesign.ALGORITHEMS.HASH_ALGORITHM
import il.ac.technion.cs.softwaredesign.exceptions.InvalidTokenException
import il.ac.technion.cs.softwaredesign.exceptions.NoSuchEntityException
import il.ac.technion.cs.softwaredesign.exceptions.UserAlreadyLoggedInException
import il.ac.technion.cs.softwaredesign.managers.ITokenManager
import il.ac.technion.cs.softwaredesign.managers.IUserManager
import java.lang.IllegalArgumentException
import java.math.BigInteger
import java.security.MessageDigest


class CourseAppImpl @Inject constructor(private val tokenManager: ITokenManager, private val userManager: IUserManager) : CourseApp {

    override fun login(username: String, password: String): String {
        var userId = userManager.getUserId(username)
        val hashedPassword = password.hashString(HASH_ALGORITHM)
        if (userId == null) {
            userId = userManager.addUser(username, hashedPassword)
            if (userId == 1L)
                userManager.updateUserPrivilege(userId, IUserManager.PrivilegeLevel.ADMIN)
        } else {
            if (userManager.getUserPassword(userId) != hashedPassword)
                throw NoSuchEntityException()
            else if (userManager.getUserStatus(userId) == IUserManager.LoginStatus.IN)
                throw UserAlreadyLoggedInException()
        }
        //At this point user is surly exist we just need to create a token
        return tokenManager.assignTokenToUserId(userId)
    }

    override fun logout(token: String) {
        val userId = tokenManager.getUserIdByToken(token)
        try {
            tokenManager.invalidateUserToken(token)
        }catch(e:IllegalArgumentException){
            throw InvalidTokenException()
        }
        userManager.updateUserStatus(userId!!, IUserManager.LoginStatus.OUT)
    }

    override fun isUserLoggedIn(token: String, username: String): Boolean? {
        if(!tokenManager.isTokenValid(token)) throw InvalidTokenException()
        val userId= userManager.getUserId(username) ?: return null
        return userManager.getUserStatus(userId) == IUserManager.LoginStatus.IN
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

}