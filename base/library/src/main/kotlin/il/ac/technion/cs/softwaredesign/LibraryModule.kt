package il.ac.technion.cs.softwaredesign

import com.authzee.kotlinguice4.KotlinModule
import il.ac.technion.cs.softwaredesign.managers.UserIdGenerator
import il.ac.technion.cs.softwaredesign.managers.UserIdSeqGenerator
import il.ac.technion.cs.softwaredesign.storage.ISequenceGenerator
import il.ac.technion.cs.softwaredesign.storage.SecureStorage
import il.ac.technion.cs.softwaredesign.storage.datastructures.GeneratorStorage
import il.ac.technion.cs.softwaredesign.storage.impl.SecureStorageImpl
import il.ac.technion.cs.softwaredesign.storage.statistics.IStatisticsStorage
import il.ac.technion.cs.softwaredesign.storage.statistics.SecureStatisticsStorage
import il.ac.technion.cs.softwaredesign.storage.users.IUserStorage
import il.ac.technion.cs.softwaredesign.storage.users.SecureUserStorage


class LibraryModule : KotlinModule() {
    override fun configure() {
        bind<IStatisticsStorage>().to<SecureStatisticsStorage>()
        bind<ISequenceGenerator>().annotatedWith<UserIdSeqGenerator>().to<UserIdGenerator>()
        bind<IUserStorage>().to<SecureUserStorage>()
    }
}