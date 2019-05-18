package il.ac.technion.cs.softwaredesign.storage.users

import com.google.inject.Inject
import il.ac.technion.cs.softwaredesign.storage.SecureStorageFactory
import il.ac.technion.cs.softwaredesign.storage.utils.ConversionUtils
import il.ac.technion.cs.softwaredesign.storage.utils.DB_NAMES
import il.ac.technion.cs.softwaredesign.storage.utils.MANAGERS_CONSTS.DELIMITER

class SecureUserStorage @Inject constructor(secureStorageFactory: SecureStorageFactory) : IUserStorage {
    private val userIdStorage= secureStorageFactory.open(DB_NAMES.USER_ID.toByteArray())
    private val userDetailsStorage= secureStorageFactory.open(DB_NAMES.USER_DETAILS.toByteArray())
    private val tokenStorage= secureStorageFactory.open(DB_NAMES.TOKEN.toByteArray())

    override fun getUserIdByUsername(usernameKey: String): Long? {
        val userIdByteArray=userIdStorage.read(usernameKey.toByteArray()) ?: return null
        return ConversionUtils.bytesToLong(userIdByteArray)
    }

    override fun setUserIdToUsername(usernameKey: String, userIdValue: Long) {
        userIdStorage.write(usernameKey.toByteArray(),ConversionUtils.longToBytes(userIdValue))
    }

    override fun getUserIdByToken(tokenKey: String): Long? {
        val userIdByteArray=tokenStorage.read(tokenKey.toByteArray())?: return null
        return ConversionUtils.bytesToLong(userIdByteArray)
    }

    override fun setUserIdToToken(tokenKey: String, userIdValue: Long) {
       tokenStorage.write(tokenKey.toByteArray(),ConversionUtils.longToBytes(userIdValue))
    }

    override fun getPropertyStringByUserId(userIdKey: Long, property: String): String? {
        val key = createPropertyKey(userIdKey, property)
        val value= userDetailsStorage.read(key) ?: return null
        return String(value)
    }

    override fun setPropertyStringToUserId(userIdKey: Long, property: String, value: String) {
        val key = createPropertyKey(userIdKey, property)
        userDetailsStorage.write(key, value.toByteArray())
    }

    override fun getPropertyLongByUserId(userIdKey: Long, property: String): Long? {
        val key = createPropertyKey(userIdKey, property)
        val value= userDetailsStorage.read(key) ?: return null
        return ConversionUtils.bytesToLong(value)
    }

    override fun setPropertyLongToUserId(userIdKey: Long, property: String, value: Long) {
        val key = createPropertyKey(userIdKey, property)
        userDetailsStorage.write(key, ConversionUtils.longToBytes(value))
    }

    override fun getPropertyListByUserId(userIdKey: Long, property: String): MutableList<Long>? {
        val key = createPropertyKey(userIdKey, property)
        val value= userDetailsStorage.read(key) ?: return null
        val stringValue = String(value)
        return stringValue.split(DELIMITER).map { it.toLong() }.toMutableList()
    }

    override fun setPropertyListToUserId(userIdKey: Long, property: String, listValue: MutableList<Long>) {
        val key = createPropertyKey(userIdKey, property)
        val value = listValue.joinToString(DELIMITER)
        userDetailsStorage.write(key, value.toByteArray())
    }

    private fun createPropertyKey(userId: Long, property: String) : ByteArray{
        val userIdByteArray = ConversionUtils.longToBytes(userId)
        val keySuffixByteArray = "$DELIMITER$property".toByteArray()
        return userIdByteArray + keySuffixByteArray
    }
}