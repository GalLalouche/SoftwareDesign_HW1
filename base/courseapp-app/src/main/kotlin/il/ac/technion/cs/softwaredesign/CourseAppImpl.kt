package il.ac.technion.cs.softwaredesign

import il.ac.technion.cs.softwaredesign.ALGORITHEMS.HASH_ALGORITHM
import il.ac.technion.cs.softwaredesign.exceptions.*
import il.ac.technion.cs.softwaredesign.managers.IChannelManager
import il.ac.technion.cs.softwaredesign.managers.ITokenManager
import il.ac.technion.cs.softwaredesign.managers.IUserManager
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.math.BigInteger
import java.security.MessageDigest
import javax.inject.Inject


class CourseAppImpl @Inject constructor(private val tokenManager: ITokenManager,
                                        private val userManager: IUserManager,
                                        private val channelManager: IChannelManager) : CourseApp {

    companion object {
        val regex : Regex = Regex("#[#_A-Za-z0-9]*")
    }

    override fun login(username: String, password: String): String {
        var userId = userManager.getUserId(username)
        val hashedPassword = password.hashString(HASH_ALGORITHM)
        if (userId == null) {
            userId = userManager.addUser(username, hashedPassword)
            if (userId == 1L) userManager.updateUserPrivilege(userId, IUserManager.PrivilegeLevel.ADMIN)
        } else {
            if (userManager.getUserPassword(userId) != hashedPassword)
                throw NoSuchEntityException()
            else if (userManager.getUserStatus(userId) == IUserManager.LoginStatus.IN)
                throw UserAlreadyLoggedInException()

            userManager.updateUserStatus(userId, IUserManager.LoginStatus.IN)
            updateUserStatusInChannels(userId, IUserManager.LoginStatus.IN)
        }
        //At this point user is surly exist we just need to create a token
        return tokenManager.assignTokenToUserId(userId)
    }

    override fun logout(token: String) {
        val userId = tokenManager.getUserIdByToken(token) ?: throw InvalidTokenException()
        try {
            tokenManager.invalidateUserToken(token)
        } catch (e:IllegalArgumentException){
            throw InvalidTokenException()
        }
        userManager.updateUserStatus(userId, IUserManager.LoginStatus.OUT)
        updateUserStatusInChannels(userId, IUserManager.LoginStatus.OUT)
    }

    override fun isUserLoggedIn(token: String, username: String): Boolean? {
        if(!tokenManager.isTokenValid(token)) throw InvalidTokenException()
        val userId= userManager.getUserId(username) ?: return null
        return userManager.getUserStatus(userId) == IUserManager.LoginStatus.IN
    }

    override fun makeAdministrator(token: String, username: String) {
        if(!tokenManager.isTokenValid(token)) throw InvalidTokenException()
        val adminId = tokenManager.getUserIdByToken(token)
                ?: throw ImpossibleSituation("getUserIdByToken returned null but token is valid")
        if (userManager.getUserPrivilege(adminId) != IUserManager.PrivilegeLevel.ADMIN) throw UserNotAuthorizedException()
        val userId = userManager.getUserId(username) ?: throw NoSuchEntityException()
        userManager.updateUserPrivilege(userId, IUserManager.PrivilegeLevel.ADMIN)
    }

    override fun channelJoin(token: String, channel: String) {
        if (!tokenManager.isTokenValid(token)) throw InvalidTokenException()
        if (!(regex matches channel)) throw NameFormatException()
        val userId = tokenManager.getUserIdByToken(token)
                ?: throw ImpossibleSituation("getUserIdByToken returned null but token is valid")
        val channelId : Long
        if (!channelManager.isChannelNameExists(channel)) { // channel does not exist
            if (userManager.getUserPrivilege(userId) != IUserManager.PrivilegeLevel.ADMIN)
                throw UserNotAuthorizedException()
            channelId = channelManager.addChannel(channel)
            channelManager.addOperatorToChannel(channelId, userId)
        } else {
            channelId = channelManager.getChannelIdByName(channel) // should not throw at this point
        }

        try {
            userManager.addChannelToUser(userId, channelId)
        } catch (e: Exception) { /* if user try to join again, its ok */ }

        try {
            channelManager.addMemberToChannel(channelId, userId)
            if (userManager.getUserStatus(userId) == IUserManager.LoginStatus.IN) {
                channelManager.increaseNumberOfActiveMembersInChannelBy(channelId)
            }
        } catch (e: Exception) { /* if user try to join again, its ok */ }
    }

    override fun channelPart(token: String, channel: String) {
        if (!tokenManager.isTokenValid(token)) throw InvalidTokenException()
        if (!channelManager.isChannelNameExists(channel)) throw NoSuchEntityException()
        val userId = tokenManager.getUserIdByToken(token)
                ?: throw ImpossibleSituation("getUserIdByToken returned null but token is valid")
        val channelId = channelManager.getChannelIdByName(channel)
        if (!isUserMember(userId, channelId)) throw NoSuchEntityException()
        channelManager.removeMemberFromChannel(channelId, userId)
        channelManager.removeOperatorFromChannel(channelId, userId)
        userManager.removeChannelFromUser(userId, channelId) // should not throw!!
        if (userManager.getUserStatus(userId) == IUserManager.LoginStatus.IN) {
            channelManager.decreaseNumberOfActiveMembersInChannelBy(channelId)
        }
        if (channelManager.getNumberOfMembersInChannel(channelId) == 0L) {
            channelManager.removeChannel(channelId)
        }
    }

    override fun channelMakeOperator(token: String, channel: String, username: String) {
        val (initiatorUserId, channelId) = preValidations(token, channel)

        if (!isUserMember(initiatorUserId, channelId)) throw UserNotAuthorizedException()

        val operatorPriv = userManager.getUserPrivilege(initiatorUserId)
        if (!isUserOperator(initiatorUserId, channelId) && operatorPriv != IUserManager.PrivilegeLevel.ADMIN)
            throw UserNotAuthorizedException()

        val userId = userManager.getUserId(username)
        if (operatorPriv == IUserManager.PrivilegeLevel.ADMIN && (userId == null || userId != initiatorUserId)) {
            throw UserNotAuthorizedException()
        }

        if (userId == null || !isUserMember(userId, channelId)) throw NoSuchEntityException()

        channelManager.addOperatorToChannel(channelId, userId)
    }

    override fun channelKick(token: String, channel: String, username: String) {
        val (initiatorUserId, channelId) = preValidations(token, channel)
        if (!isUserOperator(initiatorUserId, channelId)) throw UserNotAuthorizedException()
        val userId = userManager.getUserId(username)
        if (userId == null || !isUserMember(userId, channelId)) throw NoSuchEntityException()

        channelManager.removeMemberFromChannel(channelId, userId)
        channelManager.removeOperatorFromChannel(channelId, userId)
        userManager.removeChannelFromUser(userId, channelId) // should not throw!!
        if (userManager.getUserStatus(userId) == IUserManager.LoginStatus.IN) {
            channelManager.decreaseNumberOfActiveMembersInChannelBy(channelId)
        }
        if (channelManager.getNumberOfMembersInChannel(channelId) == 0L) {
            channelManager.removeChannel(channelId)
        }
    }

    override fun isUserInChannel(token: String, channel: String, username: String): Boolean? {
        val (initiatorUserId, channelId) = preValidations(token, channel)
        val isUserAdmin = userManager.getUserPrivilege(initiatorUserId) == IUserManager.PrivilegeLevel.ADMIN
        if (!isUserAdmin && !isUserMember(initiatorUserId, channelId)) throw UserNotAuthorizedException()
        val userId = userManager.getUserId(username) ?: return null
        return isUserMember(userId, channelId)
    }

    override fun numberOfActiveUsersInChannel(token: String, channel: String): Long {
        val (initiatorUserId, channelId) = preValidations(token, channel)
        val isUserAdmin = userManager.getUserPrivilege(initiatorUserId) == IUserManager.PrivilegeLevel.ADMIN
        if (!isUserAdmin && !isUserMember(initiatorUserId, channelId)) throw UserNotAuthorizedException()
        return channelManager.getNumberOfActiveMembersInChannel(channelId)
    }

    override fun numberOfTotalUsersInChannel(token: String, channel: String): Long {
        val (initiatorUserId, channelId) = preValidations(token, channel)
        val isUserAdmin = userManager.getUserPrivilege(initiatorUserId) == IUserManager.PrivilegeLevel.ADMIN
        if (!isUserAdmin && !isUserMember(initiatorUserId, channelId)) throw UserNotAuthorizedException()
        return channelManager.getNumberOfMembersInChannel(channelId)
    }

    class ImpossibleSituation(message: String? = null, cause: Throwable? = null) : Exception(message, cause)

    /** PRIVATES **/
    private fun updateUserStatusInChannels(userId: Long, newStatus: IUserManager.LoginStatus) {
        val channelsList = userManager.getChannelListOfUser(userId)
        for (channelId in channelsList) {
            if (newStatus == IUserManager.LoginStatus.IN)
                channelManager.increaseNumberOfActiveMembersInChannelBy(channelId)
            else {
                channelManager.decreaseNumberOfActiveMembersInChannelBy(channelId)
                channelManager.removeOperatorFromChannel(channelId, userId)
            }
        }
    }
    private fun preValidations(token: String, channel: String) : Pair<Long, Long> {
        if (!tokenManager.isTokenValid(token)) throw InvalidTokenException()
        if (!channelManager.isChannelNameExists(channel)) throw NoSuchEntityException()
        val initiatorUserId = tokenManager.getUserIdByToken(token)
                ?: throw ImpossibleSituation("getUserIdByToken returned null but token is valid")
        val channelId = channelManager.getChannelIdByName(channel)
        return Pair(initiatorUserId, channelId)
    }
    private fun String.hashString(hashAlgorithm: String): String {
        val positiveNumberSign = 1
        val numberBase = 16
        val hashFunc = MessageDigest.getInstance(hashAlgorithm)
        return BigInteger(positiveNumberSign, hashFunc.digest(this.toByteArray())).toString(numberBase).padStart(32, '0')
    }
    private fun isUserOperator(userId: Long, channelId: Long) : Boolean{
        return channelManager.getChannelOperatorsList(channelId).contains(userId)
    }
    private fun isUserMember(userId: Long, channelId: Long) : Boolean{
        return channelManager.getChannelMembersList(channelId).contains(userId)
    }
}