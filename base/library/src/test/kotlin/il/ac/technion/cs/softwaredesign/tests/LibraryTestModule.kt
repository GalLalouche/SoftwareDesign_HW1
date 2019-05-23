package il.ac.technion.cs.softwaredesign.tests

import com.authzee.kotlinguice4.KotlinModule
import il.ac.technion.cs.softwaredesign.LibraryModule
import il.ac.technion.cs.softwaredesign.storage.SecureStorageFactory


class LibraryTestModule : KotlinModule() {
    override fun configure() {
        bind<SecureStorageFactory>().toInstance(SecureHashMapStorageFactoryImpl())
        install(LibraryModule())
    }
}