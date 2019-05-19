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
    fun `add new channel return valid id`() {
        val id = channelManager.add("ron")
        assertThat(id, !equalTo(MANAGERS_CONSTS.CHANNEL_INVALID_ID), {"add channel returned invalid id"})
    }

    @Test
    fun `add new channel throws if channel name exists`() {
        channelManager.add("ron")
        assertThrows<IllegalArgumentException> { channelManager.add("ron") }
    }

}