package il.ac.technion.cs.softwaredesign.tests

import com.authzee.kotlinguice4.getInstance
import com.google.common.primitives.Longs
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
         channelManager.add("ron")
    }

    @Test
    fun `cannot add invalid channel name`() {
        assertThrows<IllegalArgumentException> { channelManager.add(MANAGERS_CONSTS.CHANNEL_INVALID_NAME) }
    }

    @Test
    fun `add new channel return valid id`() {
        val id = channelManager.add("ron")
        assertThat(id, !equalTo(MANAGERS_CONSTS.CHANNEL_INVALID_ID), {"add channel returned invalid id"})
    }

    @Test
    fun `add new channel throws if channel name exists`() {
        channelManager.add("ron")
        assertThrows<IllegalArgumentException> { channelManager.add("ron") }
    }

    @Test
    fun `remove channel by name and add it again not throws`() {
        channelManager.add("ron")
        channelManager.remove("ron")
        channelManager.add("ron")
    }

    @Test
    fun `remove channel by id and add it again not throws`() {
        val id = channelManager.add("ron")
        channelManager.remove(id)
        channelManager.add("ron")
    }

    @Test
    fun `isChannelNameExists returned true`() {
        channelManager.add("ron")
        assertThat(channelManager.isChannelNameExists("ron"), isTrue, {"channel name does not exist"})
    }

    @Test
    fun `isChannelNameExists returned false if no channel added`() {
        assertThat(channelManager.isChannelNameExists("ron"), isFalse, {"channel name does not exist"})
    }

    @Test
    fun `isChannelNameExists returned false if channel removed by name`() {
        channelManager.add("ron")
        channelManager.remove("ron")
        assertThat(channelManager.isChannelNameExists("ron"), isFalse, {"channel name does not exist"})
    }

    @Test
    fun `isChannelNameExists returned false if channel removed by id`() {
        val id = channelManager.add("ron")
        channelManager.remove(id)
        assertThat(channelManager.isChannelNameExists("ron"), isFalse, {"channel name does not exist"})
    }

    @Test
    fun `isChannelNameExists returned false for invalid name`() {
        assertThat(channelManager.isChannelNameExists(MANAGERS_CONSTS.CHANNEL_INVALID_NAME), isFalse,
                    {"invalid channel name cannot be exists"})
    }

    @Test
    fun `isChannelIdExists returned true`() {
        val id = channelManager.add("ron")
        assertThat(channelManager.isChannelIdExists(id), isTrue, {"channel id does not exist"})
    }

    @Test
    fun `isChannelIdExists returned false if no channel added`() {
        assertThat(channelManager.isChannelIdExists(8L), isFalse, {"channel id does not exist"})
    }

    @Test
    fun `isChannelIdExists returned false if channel removed by name`() {
        val id = channelManager.add("ron")
        channelManager.remove("ron")
        assertThat(channelManager.isChannelIdExists(id), isFalse, {"channel id does not exist"})
    }

    @Test
    fun `isChannelIdExists returned false if channel removed by id`() {
        val id = channelManager.add("ron")
        channelManager.remove(id)
        assertThat(channelManager.isChannelIdExists(id), isFalse, {"channel id does not exist"})
    }

    @Test
    fun `isChannelIdExists returned false for invalid id`() {
        assertThat(channelManager.isChannelIdExists(MANAGERS_CONSTS.CHANNEL_INVALID_ID), isFalse,
                {"invalid channel id cannot be exists"})
    }

    @Test
    fun `get id throws for invalid channel name`() {
        assertThrows<IllegalArgumentException> {channelManager.getId(MANAGERS_CONSTS.CHANNEL_INVALID_NAME)}
    }

    @Test
    fun `get id throws for channel name that does not exist`() {
        assertThrows<IllegalArgumentException> {channelManager.getId("ron")}
    }

    @Test
    fun `get id returned the given id that returned in add`() {
        val id1 = channelManager.add("ron")
        val id2 = channelManager.add("ben")
        val id3 = channelManager.add("aviad")
        assertThat(channelManager.getId("ron"), equalTo(id1), {"ids are not identical"})
        assertThat(channelManager.getId("ben"), equalTo(id2), {"ids are not identical"})
        assertThat(channelManager.getId("aviad"), equalTo(id3), {"ids are not identical"})
    }

    @Test
    fun `get name throws for invalid channel id`() {
        channelManager.add("ron")
        assertThrows<IllegalArgumentException> {channelManager.getName(MANAGERS_CONSTS.CHANNEL_INVALID_ID)}
    }

    @Test
    fun `get name throws for channel id that does not exist`() {
        channelManager.add("ron")
        assertThrows<IllegalArgumentException> {channelManager.getName(2000L)}
    }

    @Test
    fun `get name returned the right name`() {
        val id1 = channelManager.add("ron")
        val id2 = channelManager.add("ben")
        val id3 = channelManager.add("aviad")
        assertThat(channelManager.getName(id1), equalTo("ron"), {"names are not identical"})
        assertThat(channelManager.getName(id2), equalTo("ben"), {"names are not identical"})
        assertThat(channelManager.getName(id3), equalTo("aviad"), {"names are not identical"})
    }

    @Test
    fun `getNumberOfActiveMembers returned 0 after init`() {
        val id1 = channelManager.add("ron")
        val id2 = channelManager.add("ben")
        val id3 = channelManager.add("aviad")
        assertThat(channelManager.getNumberOfActiveMembers(id1), equalTo(0L))
        assertThat(channelManager.getNumberOfActiveMembers(id2), equalTo(0L))
        assertThat(channelManager.getNumberOfActiveMembers(id3), equalTo(0L))
    }

    @Test
    fun `getNumberOfActiveMembers throws for CHANNEL_INVALID_ID`() {
        channelManager.add("ron")
        assertThrows<IllegalArgumentException> {channelManager.getNumberOfActiveMembers(MANAGERS_CONSTS.CHANNEL_INVALID_ID)}
    }

    @Test
    fun `getNumberOfActiveMembers throws for invalid channel id`() {
        val id = channelManager.add("ron")
        assertThrows<IllegalArgumentException> {channelManager.getNumberOfActiveMembers(id + 1L)}
    }

    @Test
    fun `getNumberOfActiveMembers throws for removed channel id`() {
        val id = channelManager.add("ron")
        channelManager.remove(id)
        assertThrows<IllegalArgumentException> {channelManager.getNumberOfActiveMembers(id)}
    }

    @Test
    fun `getNumberOfMembers returned 0 after init`() {
        val id1 = channelManager.add("ron")
        val id2 = channelManager.add("ben")
        val id3 = channelManager.add("aviad")
        assertThat(channelManager.getNumberOfMembers(id1), equalTo(0L))
        assertThat(channelManager.getNumberOfMembers(id2), equalTo(0L))
        assertThat(channelManager.getNumberOfMembers(id3), equalTo(0L))
    }

    @Test
    fun `getNumberOfMembers throws for CHANNEL_INVALID_ID`() {
        channelManager.add("ron")
        assertThrows<IllegalArgumentException> {channelManager.getNumberOfMembers(MANAGERS_CONSTS.CHANNEL_INVALID_ID)}
    }

    @Test
    fun `getNumberOfMembers throws for invalid channel id`() {
        val id = channelManager.add("ron")
        assertThrows<IllegalArgumentException> {channelManager.getNumberOfMembers(id + 1L)}
    }

    @Test
    fun `getNumberOfMembers throws for removed channel id`() {
        val id = channelManager.add("ron")
        channelManager.remove(id)
        assertThrows<IllegalArgumentException> {channelManager.getNumberOfMembers(id)}
    }

    @Test
    fun `updateNumberOfActiveMembers throws for CHANNEL_INVALID_ID`() {
        channelManager.add("ron")
        assertThrows<IllegalArgumentException> {channelManager.updateNumberOfActiveMembers(MANAGERS_CONSTS.CHANNEL_INVALID_ID, 6L)}
    }

    @Test
    fun `updateNumberOfActiveMembers throws for invalid channel id`() {
        val id = channelManager.add("ron")
        assertThrows<IllegalArgumentException> {channelManager.updateNumberOfActiveMembers(id + 1L, 8L)}
    }

    @Test
    fun `updateNumberOfActiveMembers update value`() {
        val id1 = channelManager.add("ron")
        val id2 = channelManager.add("ben")
        val id3 = channelManager.add("aviad")
        channelManager.updateNumberOfActiveMembers(id1, 8L)
        assertThat(channelManager.getNumberOfActiveMembers(id1), equalTo(8L))
        assertThat(channelManager.getNumberOfActiveMembers(id2), equalTo(0L))
        assertThat(channelManager.getNumberOfActiveMembers(id3), equalTo(0L))
        channelManager.updateNumberOfActiveMembers(id1, 12L)
        assertThat(channelManager.getNumberOfActiveMembers(id1), equalTo(12L))
        assertThat(channelManager.getNumberOfActiveMembers(id2), equalTo(0L))
        assertThat(channelManager.getNumberOfActiveMembers(id3), equalTo(0L))
        channelManager.updateNumberOfActiveMembers(id2, 18L)
        assertThat(channelManager.getNumberOfActiveMembers(id1), equalTo(12L))
        assertThat(channelManager.getNumberOfActiveMembers(id2), equalTo(18L))
        assertThat(channelManager.getNumberOfActiveMembers(id3), equalTo(0L))
    }

    @Test
    fun `updateNumberOfMembers update value`() {
        val id1 = channelManager.add("ron")
        val id2 = channelManager.add("ben")
        val id3 = channelManager.add("aviad")
        channelManager.updateNumberOfMembers(id1, 8L)
        assertThat(channelManager.getNumberOfMembers(id1), equalTo(8L))
        assertThat(channelManager.getNumberOfMembers(id2), equalTo(0L))
        assertThat(channelManager.getNumberOfMembers(id3), equalTo(0L))
        channelManager.updateNumberOfMembers(id1, 12L)
        assertThat(channelManager.getNumberOfMembers(id1), equalTo(12L))
        assertThat(channelManager.getNumberOfMembers(id2), equalTo(0L))
        assertThat(channelManager.getNumberOfMembers(id3), equalTo(0L))
        channelManager.updateNumberOfMembers(id2, 18L)
        assertThat(channelManager.getNumberOfMembers(id1), equalTo(12L))
        assertThat(channelManager.getNumberOfMembers(id2), equalTo(18L))
        assertThat(channelManager.getNumberOfMembers(id3), equalTo(0L))
    }

    @Test
    fun `getMembersList throws for CHANNEL_INVALID_ID`() {
        channelManager.add("ron")
        assertThrows<IllegalArgumentException> {channelManager.getMembersList(MANAGERS_CONSTS.CHANNEL_INVALID_ID)}
    }

    @Test
    fun `getMembersList throws for invalid channel id`() {
        val id = channelManager.add("ron")
        assertThrows<IllegalArgumentException> {channelManager.getMembersList(id + 1L)}
    }

    @Test
    fun `getMembersList throws for removed channel id`() {
        val id = channelManager.add("ron")
        channelManager.remove(id)
        assertThrows<IllegalArgumentException> {channelManager.getMembersList(id)}
    }

    @Test
    fun `getOperatorsList throws for CHANNEL_INVALID_ID`() {
        channelManager.add("ron")
        assertThrows<IllegalArgumentException> {channelManager.getOperatorsList(MANAGERS_CONSTS.CHANNEL_INVALID_ID)}
    }

    @Test
    fun `getOperatorsList throws for invalid channel id`() {
        val id = channelManager.add("ron")
        assertThrows<IllegalArgumentException> {channelManager.getOperatorsList(id + 1L)}
    }

    @Test
    fun `getOperatorsList throws for removed channel id`() {
        val id = channelManager.add("ron")
        channelManager.remove(id)
        assertThrows<IllegalArgumentException> {channelManager.getOperatorsList(id)}
    }

    @Test
    fun `add&remove MemberToChannel throws for CHANNEL_INVALID_ID`() {
        channelManager.add("ron")
        assertThrows<IllegalArgumentException> {channelManager.addMemberToChannel(MANAGERS_CONSTS.CHANNEL_INVALID_ID, 2L)}
        assertThrows<IllegalArgumentException> {channelManager.removeMemberFromChannel(MANAGERS_CONSTS.CHANNEL_INVALID_ID, 2L)}
    }

    @Test
    fun `add&remove MemberToChannel throws for invalid channel id`() {
        val id = channelManager.add("ron")
        assertThrows<IllegalArgumentException> {channelManager.addMemberToChannel(id + 1L, 2L)}
        assertThrows<IllegalArgumentException> {channelManager.removeMemberFromChannel(id + 1L, 8L)}
    }

    @Test
    fun `add&remove MemberToChannel throws for removed channel id`() {
        val id = channelManager.add("ron")
        channelManager.remove(id)
        assertThrows<IllegalArgumentException> {channelManager.addMemberToChannel(id, 20L)}
        assertThrows<IllegalArgumentException> {channelManager.removeMemberFromChannel(id, 7000L)}
    }

    @Test
    fun `add&remove MemberToChannel throws for channel id that does not exist`() {
        val id = channelManager.add("ron")
        channelManager.remove(id)
        assertThrows<IllegalArgumentException> {channelManager.addMemberToChannel(id, 20L)}
        assertThrows<IllegalArgumentException> {channelManager.removeMemberFromChannel(id, 7000L)}
    }

    @Test
    fun `addMemberToChannel & addOperatorToChannel throws if element exists`() {
        val id = channelManager.add("ron")
        channelManager.addMemberToChannel(id, 20L)
        channelManager.addOperatorToChannel(id, 7000L)
        assertThrows<IllegalAccessException> {channelManager.addMemberToChannel(id, 20L)}
        assertThrows<IllegalAccessException> {channelManager.addOperatorToChannel(id, 7000L)}
    }

    @Test
    fun `removeMemberFromChannel & removeOperatorFromChannel throws if element does exists`() {
        val id = channelManager.add("ron")
        assertThrows<IllegalAccessException> {channelManager.removeMemberFromChannel(id, 20L)}
        assertThrows<IllegalAccessException> {channelManager.removeOperatorFromChannel(id, 7000L)}
    }

    @Test
    fun `getMembers&operators return empty list after init`() {
        val id = channelManager.add("ron")
        assertThat(channelManager.getMembersList(id), equalTo(emptyList()))
        assertThat(channelManager.getOperatorsList(id), equalTo(emptyList()))
    }

    @Test
    fun `getMembers&operators return full list`() {
        val id = channelManager.add("ron")

        val members = listOf<Long>(20L, 8000L, 500L, 4747L)
        val operators = listOf<Long>(2066L, 8040L, 5011L, 47337L)

        members.forEach({channelManager.addMemberToChannel(id, it)})
        operators.forEach({channelManager.addOperatorToChannel(id, it)})

        assertThat(channelManager.getMembersList(id), equalTo(members))
        assertThat(channelManager.getOperatorsList(id), equalTo(operators))
    }

    @Test
    fun `removeMembers&operators removes element`() {
        val id = channelManager.add("ron")

        val members = mutableListOf<Long>(20L, 8000L, 500L, 4747L)
        val operators = mutableListOf<Long>(2066L, 8040L, 5011L, 47337L)

        members.forEach({channelManager.addMemberToChannel(id, it)})
        operators.forEach({channelManager.addOperatorToChannel(id, it)})

        channelManager.removeMemberFromChannel(id, members[2])
        channelManager.removeOperatorFromChannel(id, operators[2])

        members.removeAt(2)
        operators.removeAt(2)
        assertThat(channelManager.getMembersList(id), equalTo(members.toList()))
        assertThat(channelManager.getOperatorsList(id), equalTo(operators.toList()))
    }

    @Test
    fun `removeMembers&operators makes list empty`() {
        val id = channelManager.add("ron")

        val members = mutableListOf<Long>(20L, 8000L, 500L, 4747L)
        val operators = mutableListOf<Long>(2066L, 8040L, 5011L, 47337L)

        members.forEach({channelManager.addMemberToChannel(id, it)})
        operators.forEach({channelManager.addOperatorToChannel(id, it)})

        members.forEach({channelManager.removeMemberFromChannel(id, it)})
        operators.forEach({channelManager.removeOperatorFromChannel(id, it)})

        assertThat(channelManager.getMembersList(id), equalTo(emptyList()))
        assertThat(channelManager.getOperatorsList(id), equalTo(emptyList()))
    }



}