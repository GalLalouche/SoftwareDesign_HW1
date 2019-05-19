package il.ac.technion.cs.softwaredesign.tests

import com.authzee.kotlinguice4.getInstance
import com.google.inject.Guice
import il.ac.technion.cs.softwaredesign.managers.IChannelManager
import org.junit.jupiter.api.Test

class ChannelManagerTest {
    private val injector = Guice.createInjector(LibraryTestModule())

    private val channelManager = injector.getInstance<IChannelManager>()

    @Test
    fun `add new channel to the system does not throws`() {
         channelManager.add("ron")
    }

}