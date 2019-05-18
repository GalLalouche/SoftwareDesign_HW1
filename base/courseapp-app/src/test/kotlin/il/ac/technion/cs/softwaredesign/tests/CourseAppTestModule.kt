package il.ac.technion.cs.softwaredesign.tests

import com.authzee.kotlinguice4.KotlinModule
import il.ac.technion.cs.softwaredesign.*
import il.ac.technion.cs.softwaredesign.storage.SecureStorageFactory

class CourseAppTestModule : KotlinModule() {
    override fun configure() {
        bind<SecureStorageFactory>().toInstance(SecureHashMapStorageFactoryImpl())
        install(LibraryModule())
        bind<CourseAppInitializer>().to<CourseAppInitializerImpl>()
//        bind<ITokenManager>().to<TokenManager>()
//        bind<IUserManager>().to<UserManager>()
        bind<CourseApp>().to<CourseAppImpl>()
    }

}