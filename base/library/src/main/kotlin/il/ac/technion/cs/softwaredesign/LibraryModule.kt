package il.ac.technion.cs.softwaredesign

import com.authzee.kotlinguice4.KotlinModule
import com.google.inject.Inject
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.matcher.Matcher
import com.google.inject.matcher.Matchers
import il.ac.technion.cs.softwaredesign.managers.*
import il.ac.technion.cs.softwaredesign.internals.ISequenceGenerator
import il.ac.technion.cs.softwaredesign.storage.SecureStorage
import il.ac.technion.cs.softwaredesign.storage.SecureStorageFactory
import il.ac.technion.cs.softwaredesign.storage.api.*
import il.ac.technion.cs.softwaredesign.storage.channels.IChannelStorage
import il.ac.technion.cs.softwaredesign.storage.users.IUserStorage
import il.ac.technion.cs.softwaredesign.storage.channels.SecureChannelStorage
import il.ac.technion.cs.softwaredesign.storage.proxies.SecureStorageCache
import il.ac.technion.cs.softwaredesign.storage.statistics.IStatisticsStorage
import il.ac.technion.cs.softwaredesign.storage.statistics.SecureStatisticsStorage
import il.ac.technion.cs.softwaredesign.storage.users.SecureUserStorage
import il.ac.technion.cs.softwaredesign.storage.utils.DB_NAMES


class LibraryModule : KotlinModule() {
    override fun configure() {
        bind<IStatisticsStorage>().to<SecureStatisticsStorage>()
        bind<IStatisticsManager>().to<StatisticsManager>()
        bind<IChannelStorage>().to<SecureChannelStorage>()

        //bindInterceptor(createSecureStorageFactoryMatcher(),Matchers.any(), SecureStorageFactoryCacheConcern)
        bind<ISequenceGenerator>().annotatedWith<UserIdSeqGenerator>().to<UserIdGenerator>()
        bind<ISequenceGenerator>().annotatedWith<ChannelIdSeqGenerator>().to<ChannelIdGenerator>()
        bind<IUserStorage>().to<SecureUserStorage>()
        bind<ITokenManager>().to<TokenManager>()
        bind<IUserManager>().to<UserManager>()
        bind<IChannelManager>().to<ChannelManager>()
    }

    private fun createSecureStorageFactoryMatcher(): Matcher<in Class<*>>? {
        return Matchers.inPackage(Package.getPackage("il.ac.technion.cs.softwaredesign.storage"))
                .and(Matchers.subclassesOf(SecureStorageFactory::class.java))
    }

    @Provides @Singleton @Inject
    @AuthenticationStorage
    fun provideTokenStorage(factory: SecureStorageFactory): SecureStorage {
        return factory.open(DB_NAMES.TOKEN.toByteArray())
    }

    @Provides @Singleton @Inject
    @MemberDetailsStorage
    fun provideUserDetailsStorage(factory: SecureStorageFactory): SecureStorage {
        return factory.open(DB_NAMES.USER_DETAILS.toByteArray())
    }

    @Provides @Singleton @Inject
    @MemberIdStorage
    fun provideUserIdStorage(factory: SecureStorageFactory): SecureStorage {
        return factory.open(DB_NAMES.USER_ID.toByteArray())
    }

    @Provides @Singleton @Inject
    @StatisticsStorage
    fun provideStatisticsStorage(factory: SecureStorageFactory): SecureStorage {
        return factory.open(DB_NAMES.STATISTICS.toByteArray())
    }

    @Provides @Singleton @Inject
    @ChannelIdStorage
    fun provideChannelIdStorage(factory: SecureStorageFactory): SecureStorage {
        return factory.open(DB_NAMES.CHANNEL_ID.toByteArray())
    }

    @Provides @Singleton @Inject
    @ChannelDetailsStorage
    fun provideChannelDetailsStorage(factory: SecureStorageFactory): SecureStorage {
        return factory.open(DB_NAMES.CHANNEL_DETAILS.toByteArray())
    }

    @Provides @Singleton @Inject
    @ChannelByUserCountStorage
    fun provideChannelByUserCountStorage(factory: SecureStorageFactory): SecureStorage {
        return factory.open(DB_NAMES.TREE_CHANNELS_BY_USERS_COUNT.toByteArray())
    }

    @Provides @Singleton @Inject
    @ChannelByActiveUserCountStorage
    fun provideChannelByActiveUserCountStorage(factory: SecureStorageFactory): SecureStorage {
        return factory.open(DB_NAMES.TREE_CHANNELS_BY_ACTIVE_USERS_COUNT.toByteArray())
    }

    @Provides @Singleton @Inject
    @UsersByChannelCountStorage
    fun provideUsersByChannelCountStorage(factory: SecureStorageFactory): SecureStorage {
        return factory.open(DB_NAMES.TREE_USERS_BY_CHANNELS_COUNT.toByteArray())
    }
}