package il.ac.technion.cs.softwaredesign.tests

import com.natpryce.hamkrest.equalTo
import il.ac.technion.cs.softwaredesign.*
import il.ac.technion.cs.softwaredesign.TokenManager.Companion.INVALID_USERNAME
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class TokenManagerTest {
    companion object {
        var users : ArrayList<User> = ArrayList()
        init {
            val username = "user"
            val pwd = "pwd"
            val token = "token"
            for (i in 0..9) {
                val num = i.toString()
                val usernameToAdd = username + num
                val pwdToAdd = pwd + num
                val tokenToAdd = token + num + num + num
                val user = User(usernameToAdd, pwdToAdd, tokenToAdd)
                users.add(user)
            }
        }
    }

    private val storageLayer = mockk<IStorageLayer>()

    /**
     * isTokenValid
     */
    @Test
    fun `token that has'nt been written to the system is not exist`() {
        every { storageLayer.readUsernameOfToken(any()) } returns null
        every { storageLayer.readUsernameOfToken(users[1].token) } returns users[1].userName
        val tokenManager = TokenManager(storageLayer)
        assertWithTimeout({ tokenManager.isTokenValid(users[0].token) }, isFalse)
    }

    @Test
    fun `token that has been written to the system is exist`() {
        every { storageLayer.readUsernameOfToken(any()) } returns null
        every { storageLayer.readUsernameOfToken(users[0].token) } returns users[0].userName
        val tokenManager = TokenManager(storageLayer)
        assertWithTimeout({ tokenManager.isTokenValid(users[0].token) }, isTrue)
    }

    @Test
    fun `invalid token that has been written to the system is not exist`() {
        every { storageLayer.readUsernameOfToken(any()) } returns null
        every { storageLayer.readUsernameOfToken(users[0].token) } returns INVALID_USERNAME
        val tokenManager = TokenManager(storageLayer)
        assertWithTimeout({ tokenManager.isTokenValid(users[0].token) }, isFalse)
    }

    /**
     * getUsernameByToken
     */
    @Test
    fun `returned user compatible to written token-user mapping`() {
        every { storageLayer.readUsernameOfToken(any()) } returns null
        every { storageLayer.readUsernameOfToken(users[0].token) } returns users[0].userName
        val tokenManager = TokenManager(storageLayer)
        assertWithTimeout({ tokenManager.getUsernameByToken(users[0].token) }, equalTo(users[0].userName))
    }

    @Test
    fun `null user throws`() {
        every { storageLayer.readUsernameOfToken(any()) } returns null
        every { storageLayer.readUsernameOfToken(users[1].token) } returns users[0].userName
        val tokenManager = TokenManager(storageLayer)
        assertThrowsWithTimeout<String, IllegalArgumentException>({ tokenManager.getUsernameByToken(users[0].userName) })
    }

    @Test
    fun `invalid username user throws`() {
        every { storageLayer.readUsernameOfToken(any()) } returns null
        every { storageLayer.readUsernameOfToken(users[0].token) } returns INVALID_USERNAME
        val tokenManager = TokenManager(storageLayer)
        assertThrowsWithTimeout<String, IllegalArgumentException>({ tokenManager.getUsernameByToken(users[0].userName) })
    }

    /**
     * assignTokenToUsername
     */
    @Test
    fun `invalid username throws`() {
        val tokenManager = TokenManager(storageLayer)
        assertThrowsWithTimeout<String, IllegalArgumentException>({ tokenManager.assignTokenToUsername(INVALID_USERNAME) })
    }

    /**
     * invalidateUserToken
     */
    @Test
    fun `invalid mapped username throws`() {
        every { storageLayer.readUsernameOfToken(any()) } returns INVALID_USERNAME
        val tokenManager = TokenManager(storageLayer)
        assertThrowsWithTimeout<Unit, IllegalArgumentException>({ tokenManager.invalidateUserToken(users[0].token) })
    }
}