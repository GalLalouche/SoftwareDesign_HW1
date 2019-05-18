//package il.ac.technion.cs.softwaredesign.tests
//
//import com.natpryce.hamkrest.equalTo
//import il.ac.technion.cs.softwaredesign.IStorageLayer
//import il.ac.technion.cs.softwaredesign.User
//import il.ac.technion.cs.softwaredesign.managers.UserManager
//import io.mockk.every
//import io.mockk.mockk
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.TestInstance
//
//@TestInstance(TestInstance.Lifecycle.PER_METHOD)
//class UserManagerTest {
//    companion object {
//        var users : ArrayList<User> = ArrayList()
//        init {
//            val username = "user"
//            val pwd = "pwd"
//            val token = "token"
//            for (i in 0..9) {
//                val num = i.toString()
//                val usernameToAdd = username + num
//                val pwdToAdd = pwd + num
//                val tokenToAdd = token + num + num + num
//                val user = User(usernameToAdd, pwdToAdd, tokenToAdd)
//                users.add(user)
//            }
//        }
//    }
//
//    private val storageLayer = mockk<IStorageLayer>()
//
//    /**
//     * isUsernameExists
//     */
//    @Test
//    fun `username does not exist before add it to the system`() {
//        every { storageLayer.readPasswordStatusOfUsername(any()) } returns null
//        every { storageLayer.readPasswordStatusOfUsername(users[1].userName) } returns Pair(users[1].password, IStorageLayer.LoginStatus.IN)
//        val userManager = UserManager(storageLayer)
//        assertWithTimeout({ userManager.isUsernameExists(users[0].userName) }, isFalse)
//    }
//
//    @Test
//    fun `username exist after add it to the system as logged in`() {
//        every { storageLayer.readPasswordStatusOfUsername(any()) } returns null
//        every { storageLayer.readPasswordStatusOfUsername(users[0].userName) } returns Pair(users[0].password, IStorageLayer.LoginStatus.IN)
//        val userManager = UserManager(storageLayer)
//        assertWithTimeout({ userManager.isUsernameExists(users[0].userName) }, isTrue)
//    }
//
//    @Test
//    fun `username exist after add it to the system as logged out`() {
//        every { storageLayer.readPasswordStatusOfUsername(any()) } returns null
//        every { storageLayer.readPasswordStatusOfUsername(users[0].userName) } returns Pair(users[0].password, IStorageLayer.LoginStatus.OUT)
//        val userManager = UserManager(storageLayer)
//        assertWithTimeout({ userManager.isUsernameExists(users[0].userName) }, isTrue)
//    }
//
//    /**
//     * getUserPassword
//     */
//    @Test
//    fun `get user password return right password`() {
//        every { storageLayer.readPasswordStatusOfUsername(any()) } returns null
//        every { storageLayer.readPasswordStatusOfUsername(users[0].userName) } returns Pair(users[0].password, IStorageLayer.LoginStatus.OUT)
//        val userManager = UserManager(storageLayer)
//        assertWithTimeout({ userManager.getUserPassword(users[0].userName) }, equalTo(users[0].password))
//    }
//
//    @Test
//    fun `get user password throws when read return null`() {
//        every { storageLayer.readPasswordStatusOfUsername(any()) } returns null
//        every { storageLayer.readPasswordStatusOfUsername(users[1].userName) } returns Pair(users[1].password, IStorageLayer.LoginStatus.OUT)
//        val userManager = UserManager(storageLayer)
//        assertThrowsWithTimeout<String, IllegalArgumentException>({ userManager.getUserPassword(users[0].userName) })
//    }
//
//    /**
//     * getUserStatus
//     */
//    @Test
//    fun `get user status return logged out status`() {
//        every { storageLayer.readPasswordStatusOfUsername(any()) } returns null
//        every { storageLayer.readPasswordStatusOfUsername(users[0].userName) } returns Pair(users[0].password, IStorageLayer.LoginStatus.OUT)
//        val userManager = UserManager(storageLayer)
//        assertWithTimeout({ userManager.getUserStatus(users[0].userName) }, equalTo(IStorageLayer.LoginStatus.OUT))
//    }
//
//    @Test
//    fun `get user status return logged in status`() {
//        every { storageLayer.readPasswordStatusOfUsername(any()) } returns null
//        every { storageLayer.readPasswordStatusOfUsername(users[0].userName) } returns Pair(users[0].password, IStorageLayer.LoginStatus.IN)
//        val userManager = UserManager(storageLayer)
//        assertWithTimeout({ userManager.getUserStatus(users[0].userName) }, equalTo(IStorageLayer.LoginStatus.IN))
//    }
//
//    @Test
//    fun `get user status throws when read return null`() {
//        every { storageLayer.readPasswordStatusOfUsername(any()) } returns null
//        every { storageLayer.readPasswordStatusOfUsername(users[1].userName) } returns Pair(users[1].password, IStorageLayer.LoginStatus.OUT)
//        val userManager = UserManager(storageLayer)
//        assertThrowsWithTimeout<IStorageLayer.LoginStatus, IllegalArgumentException>({ userManager.getUserStatus(users[0].userName) })
//    }
//}