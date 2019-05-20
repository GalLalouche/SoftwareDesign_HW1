package il.ac.technion.cs.softwaredesign.tests

import com.authzee.kotlinguice4.getInstance
import com.google.inject.Guice
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import il.ac.technion.cs.softwaredesign.managers.IChannelManager
import il.ac.technion.cs.softwaredesign.storage.statistics.IStatisticsStorage
import il.ac.technion.cs.softwaredesign.storage.utils.MANAGERS_CONSTS
import il.ac.technion.cs.softwaredesign.storage.utils.STATISTICS_KEYS
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class ChannelManagerTest {
    private val injector = Guice.createInjector(LibraryTestModule())

    private val channelManager = injector.getInstance<IChannelManager>()

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
        assertThat(id, !equalTo(MANAGERS_CONSTS.CHANNEL_INVALID_ID), {"addChannel channel returned invalid id"})
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
        assertThat(channelManager.isChannelNameExists("ron"), isTrue, {"channel name does not exist"})
    }

    @Test
    fun `isChannelNameExists returned false if no channel added`() {
        assertThat(channelManager.isChannelNameExists("ron"), isFalse, {"channel name does not exist"})
    }

    @Test
    fun `isChannelNameExists returned false if channel removed by name`() {
        val id = channelManager.addChannel("ron")
        channelManager.removeChannel(id)
        assertThat(channelManager.isChannelNameExists("ron"), isFalse, {"channel name does not exist"})
    }

    @Test
    fun `isChannelNameExists returned false if channel removed by id`() {
        val id = channelManager.addChannel("ron")
        channelManager.removeChannel(id)
        assertThat(channelManager.isChannelNameExists("ron"), isFalse, {"channel name does not exist"})
    }

    @Test
    fun `isChannelNameExists returned false for invalid name`() {
        assertThat(channelManager.isChannelNameExists(MANAGERS_CONSTS.CHANNEL_INVALID_NAME), isFalse,
                    {"invalid channel name cannot be exists"})
    }

    @Test
    fun `isChannelIdExists returned true`() {
        val id = channelManager.addChannel("ron")
        assertThat(channelManager.isChannelIdExists(id), isTrue, {"channel id does not exist"})
    }

    @Test
    fun `isChannelIdExists returned false if no channel added`() {
        assertThat(channelManager.isChannelIdExists(8L), isFalse, {"channel id does not exist"})
    }

    @Test
    fun `isChannelIdExists returned false if channel removed by id`() {
        val id = channelManager.addChannel("ron")
        channelManager.removeChannel(id)
        assertThat(channelManager.isChannelIdExists(id), isFalse, {"channel id does not exist"})
    }

    @Test
    fun `isChannelIdExists returned false for invalid id`() {
        assertThat(channelManager.isChannelIdExists(MANAGERS_CONSTS.CHANNEL_INVALID_ID), isFalse,
                {"invalid channel id cannot be exists"})
    }

    @Test
    fun `get id throws for invalid channel name`() {
        assertThrows<IllegalArgumentException> {channelManager.getChannelIdByName(MANAGERS_CONSTS.CHANNEL_INVALID_NAME)}
    }

    @Test
    fun `get id throws for channel name that does not exist`() {
        assertThrows<IllegalArgumentException> {channelManager.getChannelIdByName("ron")}
    }

    @Test
    fun `get id returned the given id that returned in add`() {
        val id1 = channelManager.addChannel("ron")
        val id2 = channelManager.addChannel("ben")
        val id3 = channelManager.addChannel("aviad")
        assertThat(channelManager.getChannelIdByName("ron"), equalTo(id1), {"ids are not identical"})
        assertThat(channelManager.getChannelIdByName("ben"), equalTo(id2), {"ids are not identical"})
        assertThat(channelManager.getChannelIdByName("aviad"), equalTo(id3), {"ids are not identical"})
    }

    @Test
    fun `get name throws for invalid channel id`() {
        channelManager.addChannel("ron")
        assertThrows<IllegalArgumentException> {channelManager.getChannelNameById(MANAGERS_CONSTS.CHANNEL_INVALID_ID)}
    }

    @Test
    fun `get name throws for channel id that does not exist`() {
        channelManager.addChannel("ron")
        assertThrows<IllegalArgumentException> {channelManager.getChannelNameById(2000L)}
    }

    @Test
    fun `get name returned the right name`() {
        val id1 = channelManager.addChannel("ron")
        val id2 = channelManager.addChannel("ben")
        val id3 = channelManager.addChannel("aviad")
        assertThat(channelManager.getChannelNameById(id1), equalTo("ron"), {"names are not identical"})
        assertThat(channelManager.getChannelNameById(id2), equalTo("ben"), {"names are not identical"})
        assertThat(channelManager.getChannelNameById(id3), equalTo("aviad"), {"names are not identical"})
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
        assertThrows<IllegalArgumentException> {channelManager.getNumberOfActiveMembersInChannel(MANAGERS_CONSTS.CHANNEL_INVALID_ID)}
    }

    @Test
    fun `getNumberOfActiveMembers throws for invalid channel id`() {
        val id = channelManager.addChannel("ron")
        assertThrows<IllegalArgumentException> {channelManager.getNumberOfActiveMembersInChannel(id + 1L)}
    }

    @Test
    fun `getNumberOfActiveMembers throws for removed channel id`() {
        val id = channelManager.addChannel("ron")
        channelManager.removeChannel(id)
        assertThrows<IllegalArgumentException> {channelManager.getNumberOfActiveMembersInChannel(id)}
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
        assertThrows<IllegalArgumentException> {channelManager.getNumberOfMembersInChannel(MANAGERS_CONSTS.CHANNEL_INVALID_ID)}
    }

    @Test
    fun `getNumberOfMembers throws for invalid channel id`() {
        val id = channelManager.addChannel("ron")
        assertThrows<IllegalArgumentException> {channelManager.getNumberOfMembersInChannel(id + 1L)}
    }

    @Test
    fun `getNumberOfMembers throws for removed channel id`() {
        val id = channelManager.addChannel("ron")
        channelManager.removeChannel(id)
        assertThrows<IllegalArgumentException> {channelManager.getNumberOfMembersInChannel(id)}
    }

    @Test
    fun `inc_dec NumberOfActiveMembers throws for CHANNEL_INVALID_ID`() {
        channelManager.addChannel("ron")
        assertThrows<IllegalArgumentException> {channelManager.increaseNumberOfActiveMembersInChannelBy(MANAGERS_CONSTS.CHANNEL_INVALID_ID, 6L)}
        assertThrows<IllegalArgumentException> {channelManager.decreaseNumberOfActiveMembersInChannelBy(MANAGERS_CONSTS.CHANNEL_INVALID_ID, 6L)}
    }

    @Test
    fun `inc_dec NumberOfActiveMembers throws for invalid channel id`() {
        val id = channelManager.addChannel("ron")
        assertThrows<IllegalArgumentException> {channelManager.increaseNumberOfActiveMembersInChannelBy(id + 1L, 8L)}
        assertThrows<IllegalArgumentException> {channelManager.decreaseNumberOfActiveMembersInChannelBy(MANAGERS_CONSTS.CHANNEL_INVALID_ID, 6L)}
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
        assertThrows<IllegalArgumentException> {channelManager.getChannelMembersList(MANAGERS_CONSTS.CHANNEL_INVALID_ID)}
    }

    @Test
    fun `getMembersList throws for invalid channel id`() {
        val id = channelManager.addChannel("ron")
        assertThrows<IllegalArgumentException> {channelManager.getChannelMembersList(id + 1L)}
    }

    @Test
    fun `getMembersList throws for removed channel id`() {
        val id = channelManager.addChannel("ron")
        channelManager.removeChannel(id)
        assertThrows<IllegalArgumentException> {channelManager.getChannelMembersList(id)}
    }

    @Test
    fun `getOperatorsList throws for CHANNEL_INVALID_ID`() {
        channelManager.addChannel("ron")
        assertThrows<IllegalArgumentException> {channelManager.getChannelOperatorsList(MANAGERS_CONSTS.CHANNEL_INVALID_ID)}
    }

    @Test
    fun `getOperatorsList throws for invalid channel id`() {
        val id = channelManager.addChannel("ron")
        assertThrows<IllegalArgumentException> {channelManager.getChannelOperatorsList(id + 1L)}
    }

    @Test
    fun `getOperatorsList throws for removed channel id`() {
        val id = channelManager.addChannel("ron")
        channelManager.removeChannel(id)
        assertThrows<IllegalArgumentException> {channelManager.getChannelOperatorsList(id)}
    }

    @Test
    fun `add&remove MemberToChannel throws for CHANNEL_INVALID_ID`() {
        channelManager.addChannel("ron")
        assertThrows<IllegalArgumentException> {channelManager.addMemberToChannel(MANAGERS_CONSTS.CHANNEL_INVALID_ID, 2L)}
        assertThrows<IllegalArgumentException> {channelManager.removeMemberFromChannel(MANAGERS_CONSTS.CHANNEL_INVALID_ID, 2L)}
    }

    @Test
    fun `add&remove MemberToChannel throws for invalid channel id`() {
        val id = channelManager.addChannel("ron")
        assertThrows<IllegalArgumentException> {channelManager.addMemberToChannel(id + 1L, 2L)}
        assertThrows<IllegalArgumentException> {channelManager.removeMemberFromChannel(id + 1L, 8L)}
    }

    @Test
    fun `add&remove MemberToChannel throws for removed channel id`() {
        val id = channelManager.addChannel("ron")
        channelManager.removeChannel(id)
        assertThrows<IllegalArgumentException> {channelManager.addMemberToChannel(id, 20L)}
        assertThrows<IllegalArgumentException> {channelManager.removeMemberFromChannel(id, 7000L)}
    }

    @Test
    fun `add&remove MemberToChannel throws for channel id that does not exist`() {
        val id = channelManager.addChannel("ron")
        channelManager.removeChannel(id)
        assertThrows<IllegalArgumentException> {channelManager.addMemberToChannel(id, 20L)}
        assertThrows<IllegalArgumentException> {channelManager.removeMemberFromChannel(id, 7000L)}
    }

    @Test
    fun `addMemberToChannel & addOperatorToChannel throws if element exists`() {
        val id = channelManager.addChannel("ron")
        channelManager.addMemberToChannel(id, 20L)
        channelManager.addOperatorToChannel(id, 7000L)
        assertThrows<IllegalAccessException> {channelManager.addMemberToChannel(id, 20L)}
        assertThrows<IllegalAccessException> {channelManager.addOperatorToChannel(id, 7000L)}
    }

    @Test
    fun `removeMemberFromChannel & removeOperatorFromChannel throws if element does exists`() {
        val id = channelManager.addChannel("ron")
        assertThrows<IllegalAccessException> {channelManager.removeMemberFromChannel(id, 20L)}
        assertThrows<IllegalAccessException> {channelManager.removeOperatorFromChannel(id, 7000L)}
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

        members.forEach({channelManager.addMemberToChannel(id, it)})
        operators.forEach({channelManager.addOperatorToChannel(id, it)})

        assertThat(channelManager.getChannelMembersList(id), equalTo(members))
        assertThat(channelManager.getChannelOperatorsList(id), equalTo(operators))
    }

    @Test
    fun `removeMembers&operators removes element`() {
        val id = channelManager.addChannel("ron")

        val members = mutableListOf<Long>(20L, 8000L, 500L, 4747L)
        val operators = mutableListOf<Long>(2066L, 8040L, 5011L, 47337L)

        members.forEach({channelManager.addMemberToChannel(id, it)})
        operators.forEach({channelManager.addOperatorToChannel(id, it)})

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

        members.forEach({channelManager.addMemberToChannel(id, it)})
        operators.forEach({channelManager.addOperatorToChannel(id, it)})

        members.forEach({channelManager.removeMemberFromChannel(id, it)})
        operators.forEach({channelManager.removeOperatorFromChannel(id, it)})

        assertThat(channelManager.getChannelMembersList(id), equalTo(emptyList()))
        assertThat(channelManager.getChannelOperatorsList(id), equalTo(emptyList()))
    }
}