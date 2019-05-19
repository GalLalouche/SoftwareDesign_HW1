package il.ac.technion.cs.softwaredesign.tests

import com.authzee.kotlinguice4.getInstance
import com.google.inject.Guice
import com.natpryce.hamkrest.equalTo
import il.ac.technion.cs.softwaredesign.IStorageLayer
import il.ac.technion.cs.softwaredesign.User
import il.ac.technion.cs.softwaredesign.managers.IChannelManager
import il.ac.technion.cs.softwaredesign.managers.IUserManager
import il.ac.technion.cs.softwaredesign.managers.UserManager
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class UserManagerTest {


    private val injector = Guice.createInjector(LibraryTestModule())

    private val userManager = injector.getInstance<IUserManager>()

    /**
     * isUsernameExists
     *//*
    @Test
    fun `username does not exist before add it to the system`() {
        every { storageLayer.readPasswordStatusOfUsername(any()) } returns null
        every { storageLayer.readPasswordStatusOfUsername(users[1].userName) } returns Pair(users[1].password, IStorageLayer.LoginStatus.IN)
        val userManager = UserManager(storageLayer)
        assertWithTimeout({ userManager.isUsernameExists(users[0].userName) }, isFalse)
    }

    @Test
    fun `username exist after add it to the system as logged in`() {
        every { storageLayer.readPasswordStatusOfUsername(any()) } returns null
        every { storageLayer.readPasswordStatusOfUsername(users[0].userName) } returns Pair(users[0].password, IStorageLayer.LoginStatus.IN)
        val userManager = UserManager(storageLayer)
        assertWithTimeout({ userManager.isUsernameExists(users[0].userName) }, isTrue)
    }

    @Test
    fun `username exist after add it to the system as logged out`() {
        every { storageLayer.readPasswordStatusOfUsername(any()) } returns null
        every { storageLayer.readPasswordStatusOfUsername(users[0].userName) } returns Pair(users[0].password, IStorageLayer.LoginStatus.OUT)
        val userManager = UserManager(storageLayer)
        assertWithTimeout({ userManager.isUsernameExists(users[0].userName) }, isTrue)
    }

    *//**
     * getUserPassword
     *//*
    @Test
    fun `getAddress user password return right password`() {
        every { storageLayer.readPasswordStatusOfUsername(any()) } returns null
        every { storageLayer.readPasswordStatusOfUsername(users[0].userName) } returns Pair(users[0].password, IStorageLayer.LoginStatus.OUT)
        val userManager = UserManager(storageLayer)
        assertWithTimeout({ userManager.getUserPassword(users[0].userName) }, equalTo(users[0].password))
    }

    @Test
    fun `getAddress user password throws when read return null`() {
        every { storageLayer.readPasswordStatusOfUsername(any()) } returns null
        every { storageLayer.readPasswordStatusOfUsername(users[1].userName) } returns Pair(users[1].password, IStorageLayer.LoginStatus.OUT)
        val userManager = UserManager(storageLayer)
        assertThrowsWithTimeout<String, IllegalArgumentException>({ userManager.getUserPassword(users[0].userName) })
    }

    *//**
     * getUserStatus
     *//*
    @Test
    fun `getAddress user status return logged out status`() {
        every { storageLayer.readPasswordStatusOfUsername(any()) } returns null
        every { storageLayer.readPasswordStatusOfUsername(users[0].userName) } returns Pair(users[0].password, IStorageLayer.LoginStatus.OUT)
        val userManager = UserManager(storageLayer)
        assertWithTimeout({ userManager.getUserStatus(users[0].userName) }, equalTo(IStorageLayer.LoginStatus.OUT))
    }

    @Test
    fun `getAddress user status return logged in status`() {
        every { storageLayer.readPasswordStatusOfUsername(any()) } returns null
        every { storageLayer.readPasswordStatusOfUsername(users[0].userName) } returns Pair(users[0].password, IStorageLayer.LoginStatus.IN)
        val userManager = UserManager(storageLayer)
        assertWithTimeout({ userManager.getUserStatus(users[0].userName) }, equalTo(IStorageLayer.LoginStatus.IN))
    }

    @Test
    fun `getAddress user status throws when read return null`() {
        every { storageLayer.readPasswordStatusOfUsername(any()) } returns null
        every { storageLayer.readPasswordStatusOfUsername(users[1].userName) } returns Pair(users[1].password, IStorageLayer.LoginStatus.OUT)
        val userManager = UserManager(storageLayer)
        assertThrowsWithTimeout<IStorageLayer.LoginStatus, IllegalArgumentException>({ userManager.getUserStatus(users[0].userName) })
    }*/
}