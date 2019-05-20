package il.ac.technion.cs.softwaredesign.tests
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Assertions.assertTimeoutPreemptively
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.function.ThrowingSupplier
import java.time.Duration
import java.time.Duration.ofSeconds

// This should be standard.
val isTrue = equalTo(true)
val isFalse = equalTo(false)

// This is a tiny wrapper over assertTimeoutPreemptively which makes the syntax slightly nicer.
fun <T> runWithTimeout(timeout: Duration, executable: () -> T): T =
        assertTimeoutPreemptively(timeout, ThrowingSupplier(executable))

fun <T> assertWithTimeout(executable: () -> T, criteria: Matcher<T>, timeout: Duration = ofSeconds(10)) =
        assertThat(runWithTimeout(timeout, executable), criteria)

inline fun <T, reified E : Throwable> assertThrowsWithTimeout(noinline executable: () -> T, timeout: Duration = ofSeconds(10)) =
        assertThrows<E> { runWithTimeout(timeout, executable) }