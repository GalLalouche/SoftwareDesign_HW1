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
    fun provideTokenStorage(factory: SecureStorageFactory): @AuthenticationStorage SecureStorage {
        return factory.open(DB_NAMES.TOKEN.toByteArray())
    }

    @Provides @Singleton @Inject
    fun provideUserDetailsStorage(factory: SecureStorageFactory): @MemberDetailsStorage SecureStorage {
        return factory.open(DB_NAMES.USER_DETAILS.toByteArray())
    }

    @Provides @Singleton @Inject
    fun provideUserIdStorage(factory: SecureStorageFactory): @MemberIdStorage SecureStorage {
        return factory.open(DB_NAMES.USER_ID.toByteArray())
    }

    @Provides @Singleton @Inject
    fun provideStatisticsStorage(factory: SecureStorageFactory): @StatisticsStorage SecureStorage {
        return factory.open(DB_NAMES.STATISTICS.toByteArray())
    }

    @Provides @Singleton @Inject
    fun provideChannelIdStorage(factory: SecureStorageFactory): @ChannelIdStorage SecureStorage {
        return factory.open(DB_NAMES.CHANNEL_ID.toByteArray())
    }

    @Provides @Singleton @Inject
    fun provideChannelDetailsStorage(factory: SecureStorageFactory): @ChannelDetailsStorage SecureStorage {
        return factory.open(DB_NAMES.CHANNEL_DETAILS.toByteArray())
    }

    @Provides @Singleton @Inject
    fun provideChannelByUserCountStorage(factory: SecureStorageFactory): @ChannelByUserCountStorage SecureStorage {
        return factory.open(DB_NAMES.TREE_CHANNELS_BY_USERS_COUNT.toByteArray())
    }

    @Provides @Singleton @Inject
    fun provideChannelByActiveUserCountStorage(factory: SecureStorageFactory): @ChannelByActiveUserCountStorage SecureStorage {
        return factory.open(DB_NAMES.TREE_CHANNELS_BY_ACTIVE_USERS_COUNT.toByteArray())
    }

    @Provides @Singleton @Inject
    fun provideUsersByChannelCountStorage(factory: SecureStorageFactory): @ChannelByActiveUserCountStorage SecureStorage {
        return factory.open(DB_NAMES.TREE_USERS_BY_CHANNELS_COUNT.toByteArray())
    }
}