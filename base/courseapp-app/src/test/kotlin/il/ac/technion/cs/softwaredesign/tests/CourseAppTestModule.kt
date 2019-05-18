package il.ac.technion.cs.softwaredesign

import com.authzee.kotlinguice4.KotlinModule
import il.ac.technion.cs.softwaredesign.managers.ITokenManager
import il.ac.technion.cs.softwaredesign.managers.IUserManager
import il.ac.technion.cs.softwaredesign.managers.TokenManager
import il.ac.technion.cs.softwaredesign.managers.UserManager
import il.ac.technion.cs.softwaredesign.storage.SecureStorage
import il.ac.technion.cs.softwaredesign.storage.SecureStorageFactory
import il.ac.technion.cs.softwaredesign.storage.impl.SecureStorageFactoryImpl
import il.ac.technion.cs.softwaredesign.storage.impl.SecureStorageImpl
import il.ac.technion.cs.softwaredesign.tests.SecureHashMapStorageFactoryImpl

class CourseAppTestModule : KotlinModule() {
    override fun configure() {
        bind<SecureStorageFactory>().to<SecureHashMapStorageFactoryImpl>()
        install(LibraryModule())
        bind<CourseAppInitializer>().to<CourseAppInitializerImpl>()
        bind<ITokenManager>().to<TokenManager>()
        bind<IUserManager>().to<UserManager>()
        bind<CourseApp>().to<CourseAppImpl>()
    }

}