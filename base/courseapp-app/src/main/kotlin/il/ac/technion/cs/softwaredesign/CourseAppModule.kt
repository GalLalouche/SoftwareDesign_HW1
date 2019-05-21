package il.ac.technion.cs.softwaredesign

import com.authzee.kotlinguice4.KotlinModule
import il.ac.technion.cs.softwaredesign.storage.SecureStorageFactory
import il.ac.technion.cs.softwaredesign.storage.impl.SecureStorageFactoryImpl
import il.ac.technion.cs.softwaredesign.storage.impl.SecureStorageImpl

class CourseAppModule : KotlinModule() {
    override fun configure() {
        bind<CourseAppInitializer>().to<CourseAppInitializerImpl>()
        install(LibraryModule())
        bind<CourseAppStatistics>().to<CourseAppStatisticsImpl>()
        bind<CourseApp>().to<CourseAppImpl>()
    }

}