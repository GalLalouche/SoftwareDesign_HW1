package il.ac.technion.cs.softwaredesign.tests

import com.authzee.kotlinguice4.getInstance
import com.google.common.primitives.Longs
import com.google.inject.Guice
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import il.ac.technion.cs.softwaredesign.storage.api.IChannelManager
import il.ac.technion.cs.softwaredesign.storage.SecureStorageFactory
import il.ac.technion.cs.softwaredesign.storage.statistics.IStatisticsStorage
import il.ac.technion.cs.softwaredesign.storage.utils.DB_NAMES
import il.ac.technion.cs.softwaredesign.storage.utils.MANAGERS_CONSTS
import il.ac.technion.cs.softwaredesign.storage.utils.STATISTICS_KEYS
import il.ac.technion.cs.softwaredesign.storage.utils.TREE_CONST.ROOT_INIT_INDEX
import il.ac.technion.cs.softwaredesign.storage.utils.TREE_CONST.ROOT_KEY
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class ChannelManagerTest {
    private val injector = Guice.createInjector(LibraryTestModule())

    private val channelManager = injector.getInstance<IChannelManager>()

    private fun initTrees() {
        val factory = injector.getInstance<SecureStorageFactory>()
        val s1 = factory.open(DB_NAMES.TREE_USERS_BY_CHANNELS_COUNT.toByteArray())
        val s2 = factory.open(DB_NAMES.TREE_CHANNELS_BY_ACTIVE_USERS_COUNT.toByteArray())
        val s3 = factory.open(DB_NAMES.TREE_CHANNELS_BY_USERS_COUNT.toByteArray())
        s1.write(ROOT_KEY.toByteArray(), Longs.toByteArray(ROOT_INIT_INDEX))
        s2.write(ROOT_KEY.toByteArray(), Longs.toByteArray(ROOT_INIT_INDEX))
        s3.write(ROOT_KEY.toByteArray(), Longs.toByteArray(ROOT_INIT_INDEX))
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
    fun `add new channel does not throws`() {
        channelManager.addChannel("ron")
    }

    @Test
    fun `cannot add invalid channel name`() {
        assertThrows<IllegalArgumentException> { channelManager.addChannel(MANAGERS_CONSTS.CHANNEL_INVALID_NAME) }
    }

    @Test
    fun `add new channel return valid id`() {
        val id = channelManager.addChannel("ron")
        assertThat(id, !equalTo(MANAGERS_CONSTS.CHANNEL_INVALID_ID), { "addChannel channel returned invalid id" })
    }

    @Test
    fun `add new channel throws if channel name exists`() {
        channelManager.addChannel("ron")
        assertThrows<IllegalArgumentException> { channelManager.addChannel("ron") }
    }

    @Test
    fun `remove channel by name and add it again not throws`() {
        val id = channelManager.addChannel("ron")
        channelManager.removeChannel(id)
        channelManager.addChannel("ron")
    }

    @Test
    fun `remove channel by id and add it again not throws`() {
        val id = channelManager.addChannel("ron")
        channelManager.removeChannel(id)
        channelManager.addChannel("ron")
    }

    @Test
    fun `isChannelNameExists returned true`() {
        channelManager.addChannel("ron")
        assertThat(channelManager.isChannelNameExists("ron"), isTrue, { "channel name does not exist" })
    }

    @Test
    fun `isChannelNameExists returned false if no channel added`() {
        assertThat(channelManager.isChannelNameExists("ron"), isFalse, { "channel name does not exist" })
    }

    @Test
    fun `isChannelNameExists returned false if channel removed by name`() {
        val id = channelManager.addChannel("ron")
        channelManager.removeChannel(id)
        assertThat(channelManager.isChannelNameExists("ron"), isFalse, { "channel name does not exist" })
    }

    @Test
    fun `isChannelNameExists returned false if channel removed by id`() {
        val id = channelManager.addChannel("ron")
        channelManager.removeChannel(id)
        assertThat(channelManager.isChannelNameExists("ron"), isFalse, { "channel name does not exist" })
    }

    @Test
    fun `isChannelNameExists returned false for invalid name`() {
        assertThat(channelManager.isChannelNameExists(MANAGERS_CONSTS.CHANNEL_INVALID_NAME), isFalse,
                { "invalid channel name cannot be exists" })
    }

    @Test
    fun `isChannelIdExists returned true`() {
        val id = channelManager.addChannel("ron")
        assertThat(channelManager.isChannelIdExists(id), isTrue, { "channel id does not exist" })
    }

    @Test
    fun `isChannelIdExists returned false if no channel added`() {
        assertThat(channelManager.isChannelIdExists(8L), isFalse, { "channel id does not exist" })
    }

    @Test
    fun `isChannelIdExists returned false if channel removed by id`() {
        val id = channelManager.addChannel("ron")
        channelManager.removeChannel(id)
        assertThat(channelManager.isChannelIdExists(id), isFalse, { "channel id does not exist" })
    }

    @Test
    fun `isChannelIdExists returned false for invalid id`() {
        assertThat(channelManager.isChannelIdExists(MANAGERS_CONSTS.CHANNEL_INVALID_ID), isFalse,
                { "invalid channel id cannot be exists" })
    }

    @Test
    fun `get id throws for invalid channel name`() {
        assertThrows<IllegalArgumentException> { channelManager.getChannelIdByName(MANAGERS_CONSTS.CHANNEL_INVALID_NAME) }
    }

    @Test
    fun `get id throws for channel name that does not exist`() {
        assertThrows<IllegalArgumentException> { channelManager.getChannelIdByName("ron") }
    }

    @Test
    fun `get id returned the given id that returned in add`() {
        val id1 = channelManager.addChannel("ron")
        val id2 = channelManager.addChannel("ben")
        val id3 = channelManager.addChannel("aviad")
        assertThat(channelManager.getChannelIdByName("ron"), equalTo(id1), { "ids are not identical" })
        assertThat(channelManager.getChannelIdByName("ben"), equalTo(id2), { "ids are not identical" })
        assertThat(channelManager.getChannelIdByName("aviad"), equalTo(id3), { "ids are not identical" })
    }

    @Test
    fun `get name throws for invalid channel id`() {
        channelManager.addChannel("ron")
        assertThrows<IllegalArgumentException> { channelManager.getChannelNameById(MANAGERS_CONSTS.CHANNEL_INVALID_ID) }
    }

    @Test
    fun `get name throws for channel id that does not exist`() {
        channelManager.addChannel("ron")
        assertThrows<IllegalArgumentException> { channelManager.getChannelNameById(2000L) }
    }

    @Test
    fun `get name returned the right name`() {
        val id1 = channelManager.addChannel("ron")
        val id2 = channelManager.addChannel("ben")
        val id3 = channelManager.addChannel("aviad")
        assertThat(channelManager.getChannelNameById(id1), equalTo("ron"), { "names are not identical" })
        assertThat(channelManager.getChannelNameById(id2), equalTo("ben"), { "names are not identical" })
        assertThat(channelManager.getChannelNameById(id3), equalTo("aviad"), { "names are not identical" })
    }

    @Test
    fun `getNumberOfActiveMembers returned 0 after init`() {
        val id1 = channelManager.addChannel("ron")
        val id2 = channelManager.addChannel("ben")
        val id3 = channelManager.addChannel("aviad")
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id1), equalTo(0L))
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id2), equalTo(0L))
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id3), equalTo(0L))
    }

    @Test
    fun `getNumberOfActiveMembers throws for CHANNEL_INVALID_ID`() {
        channelManager.addChannel("ron")
        assertThrows<IllegalArgumentException> { channelManager.getNumberOfActiveMembersInChannel(MANAGERS_CONSTS.CHANNEL_INVALID_ID) }
    }

    @Test
    fun `getNumberOfActiveMembers throws for invalid channel id`() {
        val id = channelManager.addChannel("ron")
        assertThrows<IllegalArgumentException> { channelManager.getNumberOfActiveMembersInChannel(id + 1L) }
    }

    @Test
    fun `getNumberOfActiveMembers throws for removed channel id`() {
        val id = channelManager.addChannel("ron")
        channelManager.removeChannel(id)
        assertThrows<IllegalArgumentException> { channelManager.getNumberOfActiveMembersInChannel(id) }
    }

    @Test
    fun `getNumberOfMembers returned 0 after init`() {
        val id1 = channelManager.addChannel("ron")
        val id2 = channelManager.addChannel("ben")
        val id3 = channelManager.addChannel("aviad")
        assertThat(channelManager.getNumberOfMembersInChannel(id1), equalTo(0L))
        assertThat(channelManager.getNumberOfMembersInChannel(id2), equalTo(0L))
        assertThat(channelManager.getNumberOfMembersInChannel(id3), equalTo(0L))
    }

    @Test
    fun `getNumberOfMembers throws for CHANNEL_INVALID_ID`() {
        channelManager.addChannel("ron")
        assertThrows<IllegalArgumentException> { channelManager.getNumberOfMembersInChannel(MANAGERS_CONSTS.CHANNEL_INVALID_ID) }
    }

    @Test
    fun `getNumberOfMembers throws for invalid channel id`() {
        val id = channelManager.addChannel("ron")
        assertThrows<IllegalArgumentException> { channelManager.getNumberOfMembersInChannel(id + 1L) }
    }

    @Test
    fun `getNumberOfMembers throws for removed channel id`() {
        val id = channelManager.addChannel("ron")
        channelManager.removeChannel(id)
        assertThrows<IllegalArgumentException> { channelManager.getNumberOfMembersInChannel(id) }
    }

    @Test
    fun `inc_dec NumberOfActiveMembers throws for CHANNEL_INVALID_ID`() {
        channelManager.addChannel("ron")
        assertThrows<IllegalArgumentException> { channelManager.increaseNumberOfActiveMembersInChannelBy(MANAGERS_CONSTS.CHANNEL_INVALID_ID, 6L) }
        assertThrows<IllegalArgumentException> { channelManager.decreaseNumberOfActiveMembersInChannelBy(MANAGERS_CONSTS.CHANNEL_INVALID_ID, 6L) }
    }

    @Test
    fun `inc_dec NumberOfActiveMembers throws for invalid channel id`() {
        val id = channelManager.addChannel("ron")
        assertThrows<IllegalArgumentException> { channelManager.increaseNumberOfActiveMembersInChannelBy(id + 1L, 8L) }
        assertThrows<IllegalArgumentException> { channelManager.decreaseNumberOfActiveMembersInChannelBy(MANAGERS_CONSTS.CHANNEL_INVALID_ID, 6L) }
    }

    @Test
    fun `updateNumberOfActiveMembers update value`() {
        val id1 = channelManager.addChannel("ron")
        val id2 = channelManager.addChannel("ben")
        val id3 = channelManager.addChannel("aviad")
        channelManager.increaseNumberOfActiveMembersInChannelBy(id1, 8L)
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id1), equalTo(8L))
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id2), equalTo(0L))
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id3), equalTo(0L))
        channelManager.increaseNumberOfActiveMembersInChannelBy(id1, 12L)
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id1), equalTo(20L))
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id2), equalTo(0L))
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id3), equalTo(0L))
        channelManager.increaseNumberOfActiveMembersInChannelBy(id2, 18L)
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id1), equalTo(20L))
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id2), equalTo(18L))
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id3), equalTo(0L))
    }

    @Test
    fun `updateNumberOfMembers update value`() {
        val id1 = channelManager.addChannel("ron")
        val id2 = channelManager.addChannel("ben")
        val id3 = channelManager.addChannel("aviad")
        channelManager.increaseNumberOfActiveMembersInChannelBy(id1, 8L)
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id1), equalTo(8L))
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id2), equalTo(0L))
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id3), equalTo(0L))
        channelManager.increaseNumberOfActiveMembersInChannelBy(id1, 12L)
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id1), equalTo(20L))
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id2), equalTo(0L))
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id3), equalTo(0L))
        channelManager.increaseNumberOfActiveMembersInChannelBy(id2, 18L)
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id1), equalTo(20L))
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id2), equalTo(18L))
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id3), equalTo(0L))

        channelManager.decreaseNumberOfActiveMembersInChannelBy(id1, 3L)
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id1), equalTo(17L))
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id2), equalTo(18L))
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id3), equalTo(0L))
        channelManager.decreaseNumberOfActiveMembersInChannelBy(id1, 5L)
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id1), equalTo(12L))
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id2), equalTo(18L))
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id3), equalTo(0L))
        channelManager.decreaseNumberOfActiveMembersInChannelBy(id2, 18L)
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id1), equalTo(12L))
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id2), equalTo(0L))
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id3), equalTo(0L))

    }

    @Test
    fun `getMembersList throws for CHANNEL_INVALID_ID`() {
        channelManager.addChannel("ron")
        assertThrows<IllegalArgumentException> { channelManager.getChannelMembersList(MANAGERS_CONSTS.CHANNEL_INVALID_ID) }
    }

    @Test
    fun `getMembersList throws for invalid channel id`() {
        val id = channelManager.addChannel("ron")
        assertThrows<IllegalArgumentException> { channelManager.getChannelMembersList(id + 1L) }
    }

    @Test
    fun `getMembersList throws for removed channel id`() {
        val id = channelManager.addChannel("ron")
        channelManager.removeChannel(id)
        assertThrows<IllegalArgumentException> { channelManager.getChannelMembersList(id) }
    }

    @Test
    fun `getOperatorsList throws for CHANNEL_INVALID_ID`() {
        channelManager.addChannel("ron")
        assertThrows<IllegalArgumentException> { channelManager.getChannelOperatorsList(MANAGERS_CONSTS.CHANNEL_INVALID_ID) }
    }

    @Test
    fun `getOperatorsList throws for invalid channel id`() {
        val id = channelManager.addChannel("ron")
        assertThrows<IllegalArgumentException> { channelManager.getChannelOperatorsList(id + 1L) }
    }

    @Test
    fun `getOperatorsList throws for removed channel id`() {
        val id = channelManager.addChannel("ron")
        channelManager.removeChannel(id)
        assertThrows<IllegalArgumentException> { channelManager.getChannelOperatorsList(id) }
    }

    @Test
    fun `add&remove MemberToChannel throws for CHANNEL_INVALID_ID`() {
        channelManager.addChannel("ron")
        assertThrows<IllegalArgumentException> { channelManager.addMemberToChannel(MANAGERS_CONSTS.CHANNEL_INVALID_ID, 2L) }
        assertThrows<IllegalArgumentException> { channelManager.removeMemberFromChannel(MANAGERS_CONSTS.CHANNEL_INVALID_ID, 2L) }
    }

    @Test
    fun `add&remove MemberToChannel throws for invalid channel id`() {
        val id = channelManager.addChannel("ron")
        assertThrows<IllegalArgumentException> { channelManager.addMemberToChannel(id + 1L, 2L) }
        assertThrows<IllegalArgumentException> { channelManager.removeMemberFromChannel(id + 1L, 8L) }
    }

    @Test
    fun `add&remove MemberToChannel throws for removed channel id`() {
        val id = channelManager.addChannel("ron")
        channelManager.removeChannel(id)
        assertThrows<IllegalArgumentException> { channelManager.addMemberToChannel(id, 20L) }
        assertThrows<IllegalArgumentException> { channelManager.removeMemberFromChannel(id, 7000L) }
    }

    @Test
    fun `add&remove MemberToChannel throws for channel id that does not exist`() {
        val id = channelManager.addChannel("ron")
        channelManager.removeChannel(id)
        assertThrows<IllegalArgumentException> { channelManager.addMemberToChannel(id, 20L) }
        assertThrows<IllegalArgumentException> { channelManager.removeMemberFromChannel(id, 7000L) }
    }

    @Test
    fun `addMemberToChannel throws if element exists`() {
        val id = channelManager.addChannel("ron")
        channelManager.addMemberToChannel(id, 20L)
        channelManager.addOperatorToChannel(id, 7000L)
        assertThrows<IllegalAccessException> { channelManager.addMemberToChannel(id, 20L) }
    }

    @Test
    fun `removeMemberFromChannel throws if element does exists`() {
        val id = channelManager.addChannel("ron")
        assertThrows<IllegalAccessException> { channelManager.removeMemberFromChannel(id, 20L) }
    }

    @Test
    fun `getMembers&operators return empty list after init`() {
        val id = channelManager.addChannel("ron")
        assertThat(channelManager.getChannelMembersList(id), equalTo(emptyList()))
        assertThat(channelManager.getChannelOperatorsList(id), equalTo(emptyList()))
    }

    @Test
    fun `getMembers&operators return full list`() {
        val id = channelManager.addChannel("ron")

        val members = listOf<Long>(20L, 8000L, 500L, 4747L)
        val operators = listOf<Long>(2066L, 8040L, 5011L, 47337L)

        members.forEach({ channelManager.addMemberToChannel(id, it) })
        operators.forEach({ channelManager.addOperatorToChannel(id, it) })

        assertThat(channelManager.getChannelMembersList(id), equalTo(members))
        assertThat(channelManager.getChannelOperatorsList(id), equalTo(operators))
    }

    @Test
    fun `removeMembers&operators removes element`() {
        val id = channelManager.addChannel("ron")

        val members = mutableListOf<Long>(20L, 8000L, 500L, 4747L)
        val operators = mutableListOf<Long>(2066L, 8040L, 5011L, 47337L)

        members.forEach({ channelManager.addMemberToChannel(id, it) })
        operators.forEach({ channelManager.addOperatorToChannel(id, it) })

        channelManager.removeMemberFromChannel(id, members[2])
        channelManager.removeOperatorFromChannel(id, operators[2])

        members.removeAt(2)
        operators.removeAt(2)
        assertThat(channelManager.getChannelMembersList(id), equalTo(members.toList()))
        assertThat(channelManager.getChannelOperatorsList(id), equalTo(operators.toList()))
    }

    @Test
    fun `removeMembers&operators makes list empty`() {
        val id = channelManager.addChannel("ron")

        val members = mutableListOf<Long>(20L, 8000L, 500L, 4747L)
        val operators = mutableListOf<Long>(2066L, 8040L, 5011L, 47337L)

        members.forEach({ channelManager.addMemberToChannel(id, it) })
        operators.forEach({ channelManager.addOperatorToChannel(id, it) })

        members.forEach({ channelManager.removeMemberFromChannel(id, it) })
        operators.forEach({ channelManager.removeOperatorFromChannel(id, it) })

        assertThat(channelManager.getChannelMembersList(id), equalTo(emptyList()))
        assertThat(channelManager.getChannelOperatorsList(id), equalTo(emptyList()))
    }

    @Test
    fun `getNumberOfChannels update after add`() {
        assertThat(channelManager.getNumberOfChannels(), equalTo(0L))
        channelManager.addChannel("ron")
        assertThat(channelManager.getNumberOfChannels(), equalTo(1L))
        channelManager.addChannel("ron1")
        channelManager.addChannel("ron2")
        channelManager.addChannel("ron3")
        assertThat(channelManager.getNumberOfChannels(), equalTo(4L))
    }

    @Test
    fun `getNumberOfChannels update after remove`() {
        val id0 = channelManager.addChannel("ron")
        val id1 = channelManager.addChannel("ron1")
        val id2 = channelManager.addChannel("ron2")
        var id3 = channelManager.addChannel("ron3")
        channelManager.removeChannel(id3)
        assertThat(channelManager.getNumberOfChannels(), equalTo(3L))
        channelManager.removeChannel(id0)
        assertThat(channelManager.getNumberOfChannels(), equalTo(2L))
        channelManager.removeChannel(id1)
        channelManager.removeChannel(id2)
        assertThat(channelManager.getNumberOfChannels(), equalTo(0L))
        id3 = channelManager.addChannel("ron3")
        assertThat(channelManager.getNumberOfChannels(), equalTo(1L))
        channelManager.removeChannel(id3)
        assertThat(channelManager.getNumberOfChannels(), equalTo(0L))
    }

    @Test
    fun `increase decrease number of active members`() {
        val id0 = channelManager.addChannel("ron")
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id0), equalTo(0L))
        channelManager.increaseNumberOfActiveMembersInChannelBy(id0)
        channelManager.increaseNumberOfActiveMembersInChannelBy(id0)
        channelManager.increaseNumberOfActiveMembersInChannelBy(id0)
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id0), equalTo(3L))
        channelManager.decreaseNumberOfActiveMembersInChannelBy(id0)
        channelManager.increaseNumberOfActiveMembersInChannelBy(id0)
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id0), equalTo(3L))
        channelManager.decreaseNumberOfActiveMembersInChannelBy(id0)
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id0), equalTo(2L))
        channelManager.decreaseNumberOfActiveMembersInChannelBy(id0)
        channelManager.decreaseNumberOfActiveMembersInChannelBy(id0)
        assertThat(channelManager.getNumberOfActiveMembersInChannel(id0), equalTo(0L))
    }

    @Test
    fun `after removing channel, list size has changed`() {
        val id1 = channelManager.addChannel("ron")
        assertThat(channelManager.getNumberOfMembersInChannel(id1), equalTo(0L))
        channelManager.addMemberToChannel(id1, 123L)
        channelManager.addMemberToChannel(id1, 128L)
        channelManager.addMemberToChannel(id1, 129L)
        channelManager.removeMemberFromChannel(id1, 123L)
        assertThat(channelManager.getNumberOfMembersInChannel(id1), equalTo(2L))
        assertThat(channelManager.getChannelMembersList(id1).size.toLong(), equalTo(channelManager.getNumberOfMembersInChannel(id1)))
    }

    @Test
    fun `add the same element twice throws and list size is valid`() {
        val id1 = channelManager.addChannel("ron")
        assertThat(channelManager.getNumberOfMembersInChannel(id1), equalTo(0L))
        channelManager.addMemberToChannel(id1, 123L)
        assertThrows<IllegalAccessException> { channelManager.addMemberToChannel(id1, 123L) }
        assertThat(channelManager.getNumberOfMembersInChannel(id1), equalTo(1L))
        assertThat(channelManager.getChannelMembersList(id1).size.toLong(), equalTo(channelManager.getNumberOfMembersInChannel(id1)))
    }

    @Test
    fun `remove the same element twice throws and list size is valid`() {
        val id1 = channelManager.addChannel("ron")
        assertThat(channelManager.getNumberOfMembersInChannel(id1), equalTo(0L))
        assertThat(channelManager.getChannelMembersList(id1).size.toLong(), equalTo(channelManager.getNumberOfMembersInChannel(id1)))
        channelManager.addMemberToChannel(id1, 123L)
        channelManager.removeMemberFromChannel(id1, 123L)
        assertThrows<IllegalAccessException> { channelManager.removeMemberFromChannel(id1, 123L) }
        assertThat(channelManager.getNumberOfMembersInChannel(id1), equalTo(0L))
        assertThat(channelManager.getChannelMembersList(id1).size.toLong(), equalTo(channelManager.getNumberOfMembersInChannel(id1)))
    }

    @Test
    fun `test get top 10`() {
        val ids = (0..40).map { channelManager.addChannel(it.toString()) }
        ids.forEach { channelManager.addMemberToChannel(it, it * 100) }

        val best = mutableListOf<Long>(ids[14], ids[37], ids[5], ids[7], ids[20], ids[12], ids[18], ids[33], ids[8], ids[0])
        (5000..5028).forEach { channelManager.addMemberToChannel(best[0], it.toLong()) }
        assertThat(channelManager.getNumberOfMembersInChannel(best[0]), equalTo(30L))
        (5000..5022).forEach { channelManager.addMemberToChannel(best[1], it.toLong()) }
        assertThat(channelManager.getNumberOfMembersInChannel(best[1]), equalTo(24L))
        (5000..5020).forEach { channelManager.addMemberToChannel(best[2], it.toLong()) }
        assertThat(channelManager.getNumberOfMembersInChannel(best[2]), equalTo(22L))
        (5000..5019).forEach { channelManager.addMemberToChannel(best[3], it.toLong()) }
        assertThat(channelManager.getNumberOfMembersInChannel(best[3]), equalTo(21L))
        (5000..5017).forEach { channelManager.addMemberToChannel(best[4], it.toLong()) }
        assertThat(channelManager.getNumberOfMembersInChannel(best[4]), equalTo(19L))
        (5000..5012).forEach { channelManager.addMemberToChannel(best[5], it.toLong()) }
        assertThat(channelManager.getNumberOfMembersInChannel(best[5]), equalTo(14L))
        (5000..5009).forEach { channelManager.addMemberToChannel(best[6], it.toLong()) }
        assertThat(channelManager.getNumberOfMembersInChannel(best[6]), equalTo(11L))
        (5000..5006).forEach { channelManager.addMemberToChannel(best[7], it.toLong()) }
        assertThat(channelManager.getNumberOfMembersInChannel(best[7]), equalTo(8L))
        (5000..5004).forEach { channelManager.addMemberToChannel(best[8], it.toLong()) }
        assertThat(channelManager.getNumberOfMembersInChannel(best[8]), equalTo(6L))
        (5000..5001).forEach { channelManager.addMemberToChannel(best[9], it.toLong()) }
        assertThat(channelManager.getNumberOfMembersInChannel(best[9]), equalTo(3L))

        val output = channelManager.getTop10ChannelsByUsersCount()

        for ((k, username) in output.withIndex()) {
            assertThat(channelManager.getChannelIdByName(username), equalTo(best[k]))
        }
    }

    @Test
    fun `test get top 7`() {
        val ids = (0..6).map { channelManager.addChannel(it.toString()) }
        ids.forEach { channelManager.addMemberToChannel(it, it * 100) }

        val best = mutableListOf<Long>(ids[2], ids[5], ids[0], ids[4], ids[3], ids[1], ids[6])
        (5000..5028).forEach { channelManager.addMemberToChannel(best[0], it.toLong()) }
        (5000..5022).forEach { channelManager.addMemberToChannel(best[1], it.toLong()) }
        (5000..5020).forEach { channelManager.addMemberToChannel(best[2], it.toLong()) }
        (5000..5019).forEach { channelManager.addMemberToChannel(best[3], it.toLong()) }
        (5000..5017).forEach { channelManager.addMemberToChannel(best[4], it.toLong()) }

        (5000..5012).forEach { channelManager.addMemberToChannel(best[5], it.toLong()) }
        (5000..5012).forEach { channelManager.addMemberToChannel(best[6], it.toLong()) }

        val output = channelManager.getTop10ChannelsByUsersCount()
        val outputIds = output.map { channelManager.getChannelIdByName(it) }
        for ((k, userId) in outputIds.withIndex()) {
            assertThat(userId, equalTo(best[k]))
        }
    }

    @Test
    fun `check secondary order`() {
        val ids = (0..6).map { channelManager.addChannel(it.toString()) }
        ids.forEach { channelManager.addMemberToChannel(it, it * 100) }

        val best = mutableListOf<Long>(ids[2], ids[5], ids[0], ids[3], ids[4], ids[1], ids[6])
        (5000..5028).forEach { channelManager.addMemberToChannel(best[0], it.toLong()) }
        (5000..5022).forEach { channelManager.addMemberToChannel(best[1], it.toLong()) }
        (5000..5020).forEach { channelManager.addMemberToChannel(best[2], it.toLong()) }

        (5000..5017).forEach { channelManager.addMemberToChannel(best[3], it.toLong()) }
        (5000..5017).forEach { channelManager.addMemberToChannel(best[4], it.toLong()) }

        (5000..5012).forEach { channelManager.addMemberToChannel(best[5], it.toLong()) }
        (5000..5012).forEach { channelManager.addMemberToChannel(best[6], it.toLong()) }

        val output = channelManager.getTop10ChannelsByUsersCount()
        val outputIds = output.map { channelManager.getChannelIdByName(it) }
        for ((k, userId) in outputIds.withIndex()) {
            assertThat(userId, equalTo(best[k]))
        }
    }

    @Test
    fun `check secondary order only`() {
        val ids = (0..30).map { channelManager.addChannel(it.toString()) }
        ids.forEach { channelManager.addMemberToChannel(it, it * 100) }

        val output = channelManager.getTop10ChannelsByUsersCount()
        val outputIds = output.map { channelManager.getChannelIdByName(it) }
        for ((k, userId) in outputIds.withIndex()) {
            assertThat(userId, equalTo(ids[k]))
        }
    }

    @Test
    fun `check active users top 10`() {
        // TODO("Implement this test")
    }
}