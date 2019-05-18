package il.ac.technion.cs.softwaredesign.tests

import com.authzee.kotlinguice4.getInstance
import com.google.inject.Guice
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.present
import il.ac.technion.cs.softwaredesign.*
import il.ac.technion.cs.softwaredesign.storage.SecureStorageModule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException
import java.time.Duration
import javax.inject.Inject

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class CourseAppTest{
    private val injector = Guice.createInjector(CourseAppTestModule())
    private val courseApp = injector.getInstance<CourseApp>()

   // private val courseAppStatistics = injector.getInstance<CourseAppStatistics>()

    private val courseAppInitializer = injector.getInstance<CourseAppInitializer>()

    init {
        courseAppInitializer.setup()
    }
    @Test
    fun `after login, a user is logged in`() {
        courseApp.login("gal", "hunter2")
        courseApp.login("imaman", "31337")

        val token = courseApp.login("matan", "s3kr1t")

        assertThat(runWithTimeout(Duration.ofSeconds(10)) {
            courseApp.isUserLoggedIn(token, "matan") },
                present(isTrue))
    }

    @Test
    fun `throws IllegalArgumentException after login with wrong password`(){
        val username="gal"
        val password="gal_password"
        val galToken1=courseApp.login(username, password)
        courseApp.login("aviad","shiber!$75")
        courseApp.logout(galToken1)

        assertThrows<IllegalArgumentException> {
            runWithTimeout(Duration.ofSeconds(10)) {courseApp.login(username, "wrong_password") }
        }
    }
    @Test
    fun `throws IllegalArgumentException after re-login`(){
        val username="gal"
        val password="gal_password"
        courseApp.login(username, password)
        courseApp.login("aviad","shiber!$75")
        assertThrows<IllegalArgumentException> {
            runWithTimeout(Duration.ofSeconds(10)) {courseApp.login(username, password) }
        }
    }

    @Test
    fun `throws IllegalArgumentException after logout with invalid token`(){
        val username="aviad"
        val password="aviad_password"
        courseApp.login(username, password)
        val ronToken=courseApp.login("ron", password)
        courseApp.logout(ronToken)
        assertThrows<IllegalArgumentException> { courseApp.logout("") }
        assertThrows<IllegalArgumentException> { courseApp.logout("bad_token") }
        assertThrows<IllegalArgumentException> {
            runWithTimeout(Duration.ofSeconds(10)) {courseApp.logout(ronToken)}
        }
    }

    /**
     * the test checks that after registering to the system we can login again after logout
     * also the test CHECKS with exhaustive search that no assumptions are made
     * regarding the password & username charSet.
     */
    @Test
    fun `login after register`(){
        val printableAsciiRange = ' '..'~'
        for(char in printableAsciiRange){
            val username= "Aviad$char"
            val password=username+"Password"
            val ronToken=courseApp.login(username,password)
            courseApp.logout(ronToken)
            courseApp.login(username,password)
        }
    }

    @Test
    fun `throws IllegalArgumentException after checking user login status with invalid token`(){
        assertThrows<IllegalArgumentException> {
            runWithTimeout(Duration.ofSeconds(10)) {courseApp.isUserLoggedIn("","notExistingUser")}
        }
        courseApp.login("aviad","aviad_password")
        val username="ron"
        val password="ron_password"
        val ronToken=courseApp.login(username,password)
        assertThrows<IllegalArgumentException> {
            runWithTimeout(Duration.ofSeconds(10)) {courseApp.isUserLoggedIn("bad_token","notExistingUser")}
        }
        courseApp.logout(ronToken)
        courseApp.login(username,password)
        assertThrows<IllegalArgumentException> {
            runWithTimeout(Duration.ofSeconds(10)) {courseApp.isUserLoggedIn(ronToken,username)}
        }
    }

    @Test
    fun `user login and then logout`(){
        val username="aviad"
        val password="aviad_password"
        val aviadToken= courseApp.login(username, password)
        val adminToken=courseApp.login("admin","123456")
        assertThat(runWithTimeout(Duration.ofSeconds(10)) {
            courseApp.isUserLoggedIn(aviadToken, username) },
                present(equalTo(true)))
        courseApp.logout(aviadToken)

        assertThat(runWithTimeout(Duration.ofSeconds(10)) {
            courseApp.isUserLoggedIn(adminToken, username) },
                present(equalTo(false)))

    }
    @Test
    fun `returns null if user does not exist`(){
        val token=courseApp.login("aviad","aviad_password")
        val actual =courseApp.isUserLoggedIn(token,"notExsitingUser")
        assertEquals(null, actual,"when user does not exist null expected to be returned")
    }
}