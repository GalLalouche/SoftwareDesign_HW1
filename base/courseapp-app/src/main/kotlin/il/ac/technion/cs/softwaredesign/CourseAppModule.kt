package il.ac.technion.cs.softwaredesign

import com.authzee.kotlinguice4.KotlinModule
import il.ac.technion.cs.softwaredesign.storage.SecureStorage
import il.ac.technion.cs.softwaredesign.storage.impl.SecureStorageImpl

class CourseAppModule : KotlinModule() {
    override fun configure() {
        bind<SecureStorage>().to<SecureStorageImpl>()
        bind<IStorageLayer>().to<StorageLayer>()
        bind<CourseAppInitializer>().to<CourseAppInitializerImpl>()
        bind<CourseApp>().to<CourseAppImpl>()

    }
}