package il.ac.technion.cs.softwaredesign.tests

import com.authzee.kotlinguice4.getInstance
import com.google.inject.Guice
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasElement
import com.natpryce.hamkrest.hasSize
import il.ac.technion.cs.softwaredesign.IStorageLayer
import il.ac.technion.cs.softwaredesign.User
import il.ac.technion.cs.softwaredesign.managers.IChannelManager
import il.ac.technion.cs.softwaredesign.managers.IUserManager
import il.ac.technion.cs.softwaredesign.managers.UserManager
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class UserManagerTest {


    private val injector = Guice.createInjector(LibraryTestModule())

    private val userManager = injector.getInstance<IUserManager>()


    @Test
    fun `gets the user id from the system`() {
        val aviadID=userManager.add("aviad","aviad_password")
        userManager.add("ron","ron_password")
        assertThat(userManager.getUserId("aviad"), equalTo(aviadID))
    }

    @Test
    fun `returns null if user does not exist in the system`() {
        userManager.add("aviad","aviad_password")
        userManager.add("ron","ron_password")
        Assertions.assertNull(userManager.getUserId("yossi"))
    }

    @Test
    fun `add user with as logged out`() {
        val aviadID=userManager.add("aviad","aviad_password",IUserManager.LoginStatus.OUT)
        userManager.add("ron","ron_password")
        assertThat(userManager.getStatus(aviadID), equalTo(IUserManager.LoginStatus.OUT))
    }

    @Test
    fun `add user with with admin privilege`() {
        val aviadID=userManager.add("aviad","aviad_password",privilege = IUserManager.PrivilegeLevel.ADMIN)
        userManager.add("ron","ron_password")
        assertThat(userManager.getPrivilege(aviadID), equalTo(IUserManager.PrivilegeLevel.ADMIN))
    }

    @Test
    fun `update privilege from user to admin`() {
        val aviadID=userManager.add("aviad","aviad_password")
        assertThat(userManager.getPrivilege(aviadID), equalTo(IUserManager.PrivilegeLevel.USER))
        userManager.add("ron","ron_password")
        userManager.updatePrivilege(aviadID,IUserManager.PrivilegeLevel.ADMIN)
        assertThat(userManager.getPrivilege(aviadID), equalTo(IUserManager.PrivilegeLevel.ADMIN))
    }

    @Test
    fun `update user status to logout`() {
        val aviadID=userManager.add("aviad","aviad_password")
        assertThat(userManager.getStatus(aviadID), equalTo(IUserManager.LoginStatus.IN))
        userManager.add("ron","ron_password")
        userManager.updateStatus(aviadID,IUserManager.LoginStatus.OUT)
        assertThat(userManager.getStatus(aviadID), equalTo(IUserManager.LoginStatus.OUT))
    }

    @Test
    fun `all methods throws IllegalArgumentException if user id does not exist in the system`() {
        val userID=userManager.add("aviad","password")
        assertThrowsWithTimeout<Long, IllegalArgumentException>({ userManager.add("aviad","password") })
        assertThrowsWithTimeout<IUserManager.PrivilegeLevel, IllegalArgumentException>({userManager.getPrivilege(1000L)})
        assertThrowsWithTimeout<IUserManager.LoginStatus, IllegalArgumentException>({userManager.getStatus(1000L)})
        assertThrowsWithTimeout<String, IllegalArgumentException>({ userManager.getUserPassword(1000L) })
        assertThrowsWithTimeout<Unit, IllegalArgumentException>({ userManager.removeChannelFromUser(1000L,0) })
        assertThrowsWithTimeout<Unit, IllegalArgumentException>({ userManager.addChannelToUser(1000L,0) })
        assertThrowsWithTimeout<Unit, IllegalArgumentException>({ userManager.removeChannelFromUser(1000L,0) })
        assertThrowsWithTimeout<Unit, IllegalArgumentException>({ userManager.getChannelListSize(1000L) })
    }

    @Test
    fun `is username exist after he was added to the system`() {
        val aviadID=userManager.add("aviad","aviad_password")
        assertThat(userManager.isUsernameExists("aviad"), isTrue)
        assertThat(userManager.isUsernameExists("yossi"), isFalse)
    }

    @Test
    fun `is user id exist after he was added to the system`() {
        val aviadID=userManager.add("aviad","aviad_password")
        assertThat(userManager.isUserIdExists(aviadID), isTrue)
        assertThat(userManager.isUserIdExists(1000L), isFalse)
    }

    @Test
    fun `getUserPassword after adding new user`() {
        val aviadID=userManager.add("aviad","aviad_password")
        assertThat(userManager.getUserPassword(aviadID), equalTo("aviad_password"))
    }

    @Test
    fun `adding new channels to user and validating that all channel exists`() {
        val aviadID=userManager.add("aviad","aviad_password")
        (1L..20L).forEach{userManager.addChannelToUser(aviadID, it)}
        val channelList=userManager.getChannelListOfUser(aviadID)
        (1L..20L).forEach{ assertThat(channelList, hasElement(it))}
    }

    @Test
    fun `adding new channels to user and removing part of them validating that all channel exists`() {
        val aviadID=userManager.add("aviad","aviad_password")
        (1L..20L).forEach{userManager.addChannelToUser(aviadID, it)}
        (1L..9L).forEach{userManager.removeChannelFromUser(aviadID,it) }
        val channelList=userManager.getChannelListOfUser(aviadID)
        assertThat(channelList.size, equalTo(10))
    }

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