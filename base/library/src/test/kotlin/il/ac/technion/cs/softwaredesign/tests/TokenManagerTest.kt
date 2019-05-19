package il.ac.technion.cs.softwaredesign.tests

import com.authzee.kotlinguice4.getInstance
import com.google.inject.Guice
import com.google.inject.Inject
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import il.ac.technion.cs.softwaredesign.*
import il.ac.technion.cs.softwaredesign.managers.ITokenManager
import il.ac.technion.cs.softwaredesign.managers.TokenManager
import il.ac.technion.cs.softwaredesign.storage.users.IUserStorage
import il.ac.technion.cs.softwaredesign.storage.utils.MANAGERS_CONSTS.INVALID_USER_ID
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class TokenManagerTest{

    private val userStorage = mockk<IUserStorage>()
    private val tokenManager=TokenManager(userStorage)

    /**
     * isTokenValid
     */
    @Test
    fun `token that has'nt been written to the system is not exist`() {
        every{userStorage.getUserIdByToken(any()) } returns null
        assertWithTimeout({ tokenManager.isTokenValid("InvalidToken")}, isFalse)
    }

    @Test
    fun `token that has been written to the system is exist`() {
        every { userStorage.getUserIdByToken("validToken") } returns 5L
        assertWithTimeout({ tokenManager.isTokenValid("validToken") }, isTrue)
    }


    /**
     * getUserIdByToken
     */
    @Test
    fun `returned user compatible to written token-user mapping`() {
        every{ userStorage.getUserIdByToken("aviad")} returns 10L
        assertWithTimeout({ tokenManager.getUserIdByToken("aviad") }, equalTo(10L))
    }

    /**
     * getUserIdByToken
     */
    @Test
    fun `getUserIdByToken returns null if token is not valid`() {
        Assertions.assertNull( tokenManager.getUserIdByToken("aviad"))
    }

    @Test
    fun `token been assigned to user`() {
        every { userStorage.setUserIdToToken(any(),1L) }
        val token=tokenManager.assignTokenToUserId(1L)
        every{ userStorage.getUserIdByToken(token)} returns 1L
        assertWithTimeout({tokenManager.isTokenValid(token)}, isTrue)
    }

    @Test
    fun `token been invalidated`() {
       every { userStorage.setUserIdToToken(any(),any()) } answers {  }
        every{ userStorage.getUserIdByToken(any())} returns 1L
        val token=tokenManager.assignTokenToUserId(1L)
        assertWithTimeout({tokenManager.isTokenValid(token)}, isTrue)
        tokenManager.invalidateUserToken(token)
        every{ userStorage.getUserIdByToken(token)} returns INVALID_USER_ID
        assertWithTimeout({tokenManager.isTokenValid(token)}, isFalse)
    }
/*
    @Test
    fun `invalid username user throws`() {
        every { userStorage.readUsernameOfToken(any()) } returns null
        every { userStorage.readUsernameOfToken(users[0].token) } returns INVALID_USERNAME
        val tokenManager = TokenManager(userStorage)
        assertThrowsWithTimeout<String, IllegalArgumentException>({ tokenManager.getUserIdByToken(users[0].userName) })
    }

    *//**
     * assignTokenToUserId
     *//*
    @Test
    fun `invalid username throws`() {
        val tokenManager = TokenManager(userStorage)
        assertThrowsWithTimeout<String, IllegalArgumentException>({ tokenManager.assignTokenToUserId(INVALID_USERNAME) })
    }

    *//**
     * invalidateUserToken
     *//*
    @Test
    fun `invalid mapped username throws`() {
        every { userStorage.readUsernameOfToken(any()) } returns INVALID_USERNAME
        val tokenManager = TokenManager(userStorage)
        assertThrowsWithTimeout<Unit, IllegalArgumentException>({ tokenManager.invalidateUserToken(users[0].token) })
    }*/
}