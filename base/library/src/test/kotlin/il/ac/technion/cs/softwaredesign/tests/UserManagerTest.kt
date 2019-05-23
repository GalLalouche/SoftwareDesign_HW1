package il.ac.technion.cs.softwaredesign.tests

import com.authzee.kotlinguice4.getInstance
import com.google.common.primitives.Longs
import com.google.inject.Guice
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasElement
import il.ac.technion.cs.softwaredesign.storage.api.IUserManager
import il.ac.technion.cs.softwaredesign.storage.SecureStorageFactory
import il.ac.technion.cs.softwaredesign.storage.statistics.IStatisticsStorage
import il.ac.technion.cs.softwaredesign.storage.utils.DB_NAMES
import il.ac.technion.cs.softwaredesign.storage.utils.STATISTICS_KEYS
import il.ac.technion.cs.softwaredesign.storage.utils.TREE_CONST
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class UserManagerTest {

    private val injector = Guice.createInjector(LibraryTestModule())

    private val userManager = injector.getInstance<IUserManager>()

    private fun initTrees() {
        val factory = injector.getInstance<SecureStorageFactory>()
        val s1 = factory.open(DB_NAMES.TREE_USERS_BY_CHANNELS_COUNT.toByteArray())
        val s2 = factory.open(DB_NAMES.TREE_CHANNELS_BY_ACTIVE_USERS_COUNT.toByteArray())
        val s3 = factory.open(DB_NAMES.TREE_CHANNELS_BY_USERS_COUNT.toByteArray())
        s1.write(TREE_CONST.ROOT_KEY.toByteArray(), Longs.toByteArray(TREE_CONST.ROOT_INIT_INDEX))
        s2.write(TREE_CONST.ROOT_KEY.toByteArray(), Longs.toByteArray(TREE_CONST.ROOT_INIT_INDEX))
        s3.write(TREE_CONST.ROOT_KEY.toByteArray(), Longs.toByteArray(TREE_CONST.ROOT_INIT_INDEX))
    }

    private fun initStatistics() {
        val statisticsStorage = injector.getInstance<IStatisticsStorage>()
        statisticsStorage.setLongValue(STATISTICS_KEYS.NUMBER_OF_USERS, STATISTICS_KEYS.INIT_INDEX_VAL)
        statisticsStorage.setLongValue(STATISTICS_KEYS.NUMBER_OF_LOGGED_IN_USERS, STATISTICS_KEYS.INIT_INDEX_VAL)
        statisticsStorage.setLongValue(STATISTICS_KEYS.NUMBER_OF_CHANNELS, STATISTICS_KEYS.INIT_INDEX_VAL)
        statisticsStorage.setLongValue(STATISTICS_KEYS.MAX_CHANNEL_INDEX, STATISTICS_KEYS.INIT_INDEX_VAL)
    }

    @BeforeEach
    private fun init() {
        initStatistics()
        initTrees()
    }

    @Test
    fun `gets the user id from the system`() {
        val aviadID = userManager.addUser("aviad", "aviad_password")
        userManager.addUser("ron", "ron_password")
        assertThat(userManager.getUserId("aviad"), equalTo(aviadID))
    }

    @Test
    fun `returns null if user does not exist in the system`() {
        userManager.addUser("aviad", "aviad_password")
        userManager.addUser("ron", "ron_password")
        Assertions.assertNull(userManager.getUserId("yossi"))
    }

    @Test
    fun `add user with as logged out`() {
        val aviadID = userManager.addUser("aviad", "aviad_password", IUserManager.LoginStatus.OUT)
        userManager.addUser("ron", "ron_password")
        assertThat(userManager.getUserStatus(aviadID), equalTo(IUserManager.LoginStatus.OUT))
    }

    @Test
    fun `add user with with admin privilege`() {
        val aviadID = userManager.addUser("aviad", "aviad_password", privilege = IUserManager.PrivilegeLevel.ADMIN)
        userManager.addUser("ron", "ron_password")
        assertThat(userManager.getUserPrivilege(aviadID), equalTo(IUserManager.PrivilegeLevel.ADMIN))
    }

    @Test
    fun `update privilege from user to admin`() {
        val aviadID = userManager.addUser("aviad", "aviad_password")
        assertThat(userManager.getUserPrivilege(aviadID), equalTo(IUserManager.PrivilegeLevel.USER))
        userManager.addUser("ron", "ron_password")
        userManager.updateUserPrivilege(aviadID, IUserManager.PrivilegeLevel.ADMIN)
        assertThat(userManager.getUserPrivilege(aviadID), equalTo(IUserManager.PrivilegeLevel.ADMIN))
    }

    @Test
    fun `update user status to logout`() {
        val aviadID = userManager.addUser("aviad", "aviad_password")
        assertThat(userManager.getUserStatus(aviadID), equalTo(IUserManager.LoginStatus.IN))
        userManager.addUser("ron", "ron_password")
        userManager.updateUserStatus(aviadID, IUserManager.LoginStatus.OUT)
        assertThat(userManager.getUserStatus(aviadID), equalTo(IUserManager.LoginStatus.OUT))
    }

    @Test
    fun `all methods throws IllegalArgumentException if user id does not exist in the system`() {
        userManager.addUser("aviad", "password")
        assertThrowsWithTimeout<Long, IllegalArgumentException>({ userManager.addUser("aviad", "password") })
        assertThrowsWithTimeout<IUserManager.PrivilegeLevel, IllegalArgumentException>({ userManager.getUserPrivilege(1000L) })
        assertThrowsWithTimeout<IUserManager.LoginStatus, IllegalArgumentException>({ userManager.getUserStatus(1000L) })
        assertThrowsWithTimeout<String, IllegalArgumentException>({ userManager.getUserPassword(1000L) })
        assertThrowsWithTimeout<Unit, IllegalArgumentException>({ userManager.removeChannelFromUser(1000L, 0) })
        assertThrowsWithTimeout<Unit, IllegalArgumentException>({ userManager.addChannelToUser(1000L, 0) })
        assertThrowsWithTimeout<Unit, IllegalArgumentException>({ userManager.removeChannelFromUser(1000L, 0) })
        assertThrowsWithTimeout<Unit, IllegalArgumentException>({ userManager.getUserChannelListSize(1000L) })
    }

    @Test
    fun `is username exist after he was added to the system`() {
        userManager.addUser("aviad", "aviad_password")
        assertThat(userManager.isUsernameExists("aviad"), isTrue)
        assertThat(userManager.isUsernameExists("yossi"), isFalse)
    }

    @Test
    fun `is user id exist after he was added to the system`() {
        val aviadID = userManager.addUser("aviad", "aviad_password")
        assertThat(userManager.isUserIdExists(aviadID), isTrue)
        assertThat(userManager.isUserIdExists(1000L), isFalse)
    }

    @Test
    fun `getUserPassword after adding new user`() {
        val aviadID = userManager.addUser("aviad", "aviad_password")
        assertThat(userManager.getUserPassword(aviadID), equalTo("aviad_password"))
    }

    @Test
    fun `adding new channels to user and validating that all channel exists`() {
        val aviadID = userManager.addUser("aviad", "aviad_password")
        (1L..20L).forEach { userManager.addChannelToUser(aviadID, it) }
        val channelList = userManager.getChannelListOfUser(aviadID)
        (1L..20L).forEach { assertThat(channelList, hasElement(it)) }
    }

    @Test
    fun `adding new channels to user and removing part of them validating that all channel exists`() {
        val aviadID = userManager.addUser("aviad", "aviad_password")
        (1L..20L).forEach { userManager.addChannelToUser(aviadID, it) }
        (1L..9L).forEach { userManager.removeChannelFromUser(aviadID, it) }
        val channelList = userManager.getChannelListOfUser(aviadID)
        assertThat(channelList.size, equalTo(11))
    }

    @Test
    fun `after inserting users, total users is valid`() {
        assertThat(userManager.getTotalUsers(), equalTo(0L))
        userManager.addUser("ron", "ron_password")
        userManager.addUser("aviad", "aviad_password")
        userManager.addUser("gal", "gal_password")
        assertThat(userManager.getTotalUsers(), equalTo(3L))
    }

    @Test
    fun `after inserting users, loggedInUsers is valid`() {
        assertThat(userManager.getLoggedInUsers(), equalTo(0L))
        userManager.addUser("ron", "ron_password")
        userManager.addUser("aviad", "aviad_password")
        userManager.addUser("gal", "gal_password")
        assertThat(userManager.getLoggedInUsers(), equalTo(3L))
    }

    @Test
    fun `after status update, loggedInUsers is valid`() {
        assertThat(userManager.getLoggedInUsers(), equalTo(0L))
        val id1 = userManager.addUser("ron", "ron_password")
        val id2 = userManager.addUser("aviad", "aviad_password")
        val id3 = userManager.addUser("gal", "gal_password")
        assertThat(userManager.getLoggedInUsers(), equalTo(3L))
        userManager.updateUserStatus(id1, IUserManager.LoginStatus.IN)
        userManager.updateUserStatus(id1, IUserManager.LoginStatus.OUT)
        assertThat(userManager.getLoggedInUsers(), equalTo(2L))
        userManager.updateUserStatus(id1, IUserManager.LoginStatus.IN)
        assertThat(userManager.getLoggedInUsers(), equalTo(3L))
        userManager.updateUserStatus(id2, IUserManager.LoginStatus.OUT)
        userManager.updateUserStatus(id2, IUserManager.LoginStatus.OUT)
        assertThat(userManager.getLoggedInUsers(), equalTo(2L))
        userManager.updateUserStatus(id3, IUserManager.LoginStatus.OUT)
        assertThat(userManager.getLoggedInUsers(), equalTo(1L))
    }

    @Test
    fun `after adding channel, list size has changed`(){
        val id1 = userManager.addUser("ron", "ron_password")
        assertThat(userManager.getUserChannelListSize(id1), equalTo(0L))
        userManager.addChannelToUser(id1, 123L)
        userManager.addChannelToUser(id1, 128L)
        userManager.addChannelToUser(id1, 129L)
        assertThat(userManager.getUserChannelListSize(id1), equalTo(3L))
        assertThat(userManager.getChannelListOfUser(id1).size.toLong(), equalTo(userManager.getUserChannelListSize(id1)))
    }

    @Test
    fun `after removing channel, list size has changed`(){
        val id1 = userManager.addUser("ron", "ron_password")
        assertThat(userManager.getUserChannelListSize(id1), equalTo(0L))
        userManager.addChannelToUser(id1, 123L)
        userManager.addChannelToUser(id1, 128L)
        userManager.addChannelToUser(id1, 129L)
        userManager.removeChannelFromUser(id1, 123L)
        assertThat(userManager.getUserChannelListSize(id1), equalTo(2L))
        assertThat(userManager.getChannelListOfUser(id1).size.toLong(), equalTo(userManager.getUserChannelListSize(id1)))
    }

    @Test
    fun `add the same element twice throws and list size is valid`(){
        val id1 = userManager.addUser("ron", "ron_password")
        assertThat(userManager.getUserChannelListSize(id1), equalTo(0L))
        userManager.addChannelToUser(id1, 123L)
        assertThrows<IllegalAccessException> { userManager.addChannelToUser(id1, 123L) }
        assertThat(userManager.getUserChannelListSize(id1), equalTo(1L))
        assertThat(userManager.getChannelListOfUser(id1).size.toLong(), equalTo(userManager.getUserChannelListSize(id1)))
    }

    @Test
    fun `remove the same element twice throws and list size is valid`(){
        val id1 = userManager.addUser("ron", "ron_password")
        assertThat(userManager.getUserChannelListSize(id1), equalTo(0L))
        assertThat(userManager.getChannelListOfUser(id1).size.toLong(), equalTo(userManager.getUserChannelListSize(id1)))
        userManager.addChannelToUser(id1, 123L)
        userManager.removeChannelFromUser(id1, 123L)
        assertThrows<IllegalAccessException> { userManager.removeChannelFromUser(id1, 123L) }
        assertThat(userManager.getUserChannelListSize(id1), equalTo(0L))
        assertThat(userManager.getChannelListOfUser(id1).size.toLong(), equalTo(userManager.getUserChannelListSize(id1)))
    }

    @Test
    fun `test get top 10`() {
        val ids = (0..40).map { userManager.addUser(it.toString(), it.toString()) }
        ids.forEach { userManager.addChannelToUser(it,it * 100) }

        val best = mutableListOf<Long>(ids[14], ids[37], ids[5], ids[7], ids[20], ids[12], ids[18], ids[33], ids[8], ids[0])
        (5000..5028).forEach { userManager.addChannelToUser(best[0],it.toLong()) }
        (5000..5022).forEach { userManager.addChannelToUser(best[1],it.toLong()) }
        (5000..5020).forEach { userManager.addChannelToUser(best[2],it.toLong()) }
        (5000..5019).forEach { userManager.addChannelToUser(best[3],it.toLong()) }
        (5000..5017).forEach { userManager.addChannelToUser(best[4],it.toLong()) }
        (5000..5012).forEach { userManager.addChannelToUser(best[5],it.toLong()) }
        (5000..5009).forEach { userManager.addChannelToUser(best[6],it.toLong()) }
        (5000..5006).forEach { userManager.addChannelToUser(best[7],it.toLong()) }
        (5000..5004).forEach { userManager.addChannelToUser(best[8],it.toLong()) }
        (5000..5001).forEach { userManager.addChannelToUser(best[9],it.toLong()) }

        val output = userManager.getTop10UsersByChannelsCount()
        for ((k, username) in output.withIndex()) {
            assertThat(userManager.getUserId(username), equalTo(best[k]))
        }
    }

    @Test
    fun `test get top 7`() {
        val ids = (0..6).map { userManager.addUser(it.toString(), it.toString()) }
        ids.forEach { userManager.addChannelToUser(it,it * 100) }

        val best = mutableListOf<Long>(ids[2], ids[5], ids[0], ids[4], ids[3], ids[1], ids[6])
        (5000..5028).forEach { userManager.addChannelToUser(best[0],it.toLong()) }
        (5000..5022).forEach { userManager.addChannelToUser(best[1],it.toLong()) }
        (5000..5020).forEach { userManager.addChannelToUser(best[2],it.toLong()) }
        (5000..5019).forEach { userManager.addChannelToUser(best[3],it.toLong()) }
        (5000..5017).forEach { userManager.addChannelToUser(best[4],it.toLong()) }

        (5000..5012).forEach { userManager.addChannelToUser(best[5],it.toLong()) }
        (5000..5012).forEach { userManager.addChannelToUser(best[6],it.toLong()) }

        val output = userManager.getTop10UsersByChannelsCount()
        val outputIds = output.map { userManager.getUserId(it) }
        for ((k, userId) in outputIds.withIndex()) {
            assertThat(userId, equalTo(best[k]))
        }
    }

    @Test
    fun `check secondary order`() {
        val ids = (0..6).map { userManager.addUser(it.toString(), it.toString()) }
        ids.forEach { userManager.addChannelToUser(it,it * 100) }

        val best = mutableListOf<Long>(ids[2], ids[5], ids[0], ids[3], ids[4], ids[1], ids[6])
        (5000..5028).forEach { userManager.addChannelToUser(best[0],it.toLong()) }
        (5000..5022).forEach { userManager.addChannelToUser(best[1],it.toLong()) }
        (5000..5020).forEach { userManager.addChannelToUser(best[2],it.toLong()) }

        (5000..5017).forEach { userManager.addChannelToUser(best[3],it.toLong()) }
        (5000..5017).forEach { userManager.addChannelToUser(best[4],it.toLong()) }

        (5000..5012).forEach { userManager.addChannelToUser(best[5],it.toLong()) }
        (5000..5012).forEach { userManager.addChannelToUser(best[6],it.toLong()) }

        val output = userManager.getTop10UsersByChannelsCount()
        val outputIds = output.map { userManager.getUserId(it) }
        for ((k, userId) in outputIds.withIndex()) {
            assertThat(userId, equalTo(best[k]))
        }
    }

    @Test
    fun `check secondary order only`() {
        val ids = (0..30).map { userManager.addUser(it.toString(), it.toString()) }
        ids.forEach { userManager.addChannelToUser(it,it * 100) }

        val output = userManager.getTop10UsersByChannelsCount()
        val outputIds = output.map { userManager.getUserId(it) }
        for ((k, userId) in outputIds.withIndex()) {
            assertThat(userId, equalTo(ids[k]))
        }
    }
}