package il.ac.technion.cs.softwaredesign.storage.users

import com.google.inject.Inject
import il.ac.technion.cs.softwaredesign.storage.SecureStorageFactory
import il.ac.technion.cs.softwaredesign.storage.utils.ConversionUtils
import il.ac.technion.cs.softwaredesign.storage.utils.DB_NAMES
import il.ac.technion.cs.softwaredesign.storage.utils.MANAGERS_CONSTS.DELIMITER

class SecureUserStorage @Inject constructor(secureStorageFactory: SecureStorageFactory) : IUserStorage {
    private val userIdStorage= secureStorageFactory.open(DB_NAMES.USER_ID.toByteArray())
    private val userDetailsStorage= secureStorageFactory.open(DB_NAMES.USER_DETAILS.toByteArray())
    private val userChannelsStorage= secureStorageFactory.open(DB_NAMES.USER_CHANNELS.toByteArray())
    private val tokenStorage= secureStorageFactory.open(DB_NAMES.TOKEN.toByteArray())

    override fun getUserIdByUsername(username: String): Long? {
        val userIdByteArray=userIdStorage.read(username.toByteArray()) ?: return null
        return ConversionUtils.bytesToLong(userIdByteArray)
    }

    override fun setUserIdToUsername(userId: Long, username: String) {
        userIdStorage.write(username.toByteArray(),ConversionUtils.longToBytes(userId))
    }

    override fun getUserIdByToken(token: String): Long? {
        val userIdByteArray=tokenStorage.read(token.toByteArray())?: return null
        return ConversionUtils.bytesToLong(userIdByteArray)
    }

    override fun setUserIdToToken(userId: Long, token: String) {
       tokenStorage.write(token.toByteArray(),ConversionUtils.longToBytes(userId))
    }

    override fun getPropertyStringByUserId(userId: Long, property: String): String? {
        val userIdByteArray= ConversionUtils.longToBytes(userId)
        val keySuffixByteArray="$DELIMITER$property".toByteArray()
        val key= userIdByteArray+keySuffixByteArray
        val value= userDetailsStorage.read(key) ?: return null
        return String(value)
    }

    override fun setPropertyStringToUserId(userId: Long, property: String, value: String) {

    }

    override fun getPropertyLongByUserId(userId: Long, property: String): Long? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPropertyLongToUserId(userId: Long, property: String, value: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPropertyListByUserId(userId: Long, property: String): MutableList<Long>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPropertyListToUserId(userId: Long, property: String, listValue: MutableList<Long>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}