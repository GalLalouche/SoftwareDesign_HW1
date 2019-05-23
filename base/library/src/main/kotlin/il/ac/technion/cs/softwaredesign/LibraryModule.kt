package il.ac.technion.cs.softwaredesign

import com.authzee.kotlinguice4.KotlinModule
import com.google.inject.Provides
import com.google.inject.matcher.Matcher
import com.google.inject.matcher.Matchers
import il.ac.technion.cs.softwaredesign.managers.*
import il.ac.technion.cs.softwaredesign.storage.ISequenceGenerator
import il.ac.technion.cs.softwaredesign.storage.SecureStorage
import il.ac.technion.cs.softwaredesign.storage.SecureStorageFactory
import il.ac.technion.cs.softwaredesign.storage.channels.IChannelStorage
import il.ac.technion.cs.softwaredesign.storage.channels.SecureChannelStorage
import il.ac.technion.cs.softwaredesign.storage.proxies.SecureStorageCache
import il.ac.technion.cs.softwaredesign.storage.statistics.IStatisticsStorage
import il.ac.technion.cs.softwaredesign.storage.statistics.SecureStatisticsStorage
import il.ac.technion.cs.softwaredesign.storage.users.IUserStorage
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

    @Provides
    fun provideTokenStorage(factory:SecureStorageFactory): @AuthenticationStored SecureStorage{
       return SecureStorageCache(factory.open(DB_NAMES.TOKEN.toByteArray()))
    }

    @Provides
    fun provideUserDetailsStorage(factory:SecureStorageFactory): @MemberDetailsStored SecureStorage{
        return SecureStorageCache(factory.open(DB_NAMES.USER_DETAILS.toByteArray()))
    }

    @Provides
    fun provideUserIdStorage(factory:SecureStorageFactory): @MemberIdStored SecureStorage{
        return SecureStorageCache(factory.open(DB_NAMES.USER_DETAILS.toByteArray()))
    }

    @Provides
    fun provideStatisticsStorage(factory:SecureStorageFactory): @StatisticsStored SecureStorage{
        return SecureStorageCache(factory.open(DB_NAMES.STATISTICS.toByteArray()))
    }
    @Provides
    fun provideChannelIdStorage(factory:SecureStorageFactory): @ChannelIdStored SecureStorage{
        return SecureStorageCache(factory.open(DB_NAMES.CHANNEL_ID.toByteArray()))
    }
    @Provides
    fun provideChannelDetailsStorage(factory:SecureStorageFactory): @ChannelDetailsStored SecureStorage{
        return SecureStorageCache(factory.open(DB_NAMES.CHANNEL_DETAILS.toByteArray()))
    }
}