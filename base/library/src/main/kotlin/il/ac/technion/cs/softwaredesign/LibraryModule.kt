package il.ac.technion.cs.softwaredesign

import com.authzee.kotlinguice4.KotlinModule
import il.ac.technion.cs.softwaredesign.storage.SecureStorage
import il.ac.technion.cs.softwaredesign.storage.datastructures.GeneratorStorage
import il.ac.technion.cs.softwaredesign.storage.impl.SecureStorageImpl
import il.ac.technion.cs.softwaredesign.storage.users.IUserStorage
import il.ac.technion.cs.softwaredesign.storage.users.SecureUserStorage


class LibraryModule : KotlinModule() {
    override fun configure() {
        bind<SecureStorage>().annotatedWith<GeneratorStorage>().to<SecureStorageImpl>()
        bind<IUserStorage>().to<SecureUserStorage>()
    }
}