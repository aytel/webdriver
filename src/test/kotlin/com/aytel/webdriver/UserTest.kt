package com.aytel.webdriver

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.openqa.selenium.WebDriver
import org.openqa.selenium.safari.SafariDriver
import java.util.*

class UserTest {
    companion object {
        private val driver: WebDriver = SafariDriver()
        private val user: User = User(driver)

        @BeforeAll
        @JvmStatic
        fun login() {
            user.loginAsRoot()
        }

        @AfterAll
        @JvmStatic
        fun finish() {
            driver.close()
        }
    }

    @Test
    fun testSuccessful() {
        val login: String = UUID.randomUUID().toString()
        assertDoesNotThrow {user.createUser(login, "pass") }
        assertTrue(user.deleteUser(login))
    }

    @Test
    fun testEmpty() {
        assertThrows<Exception>("empty login") {user.createUser("", "pass")}
    }

    @Test
    fun testWithSpace() {
        assertThrows<Exception>("Restricted character ' ' in the name") {
            user.createUser("lo gin", "pass")
        }
    }

    @Test
    fun testRoot() {
        assertThrows<Exception>("Removing null is prohibited") {
            user.createUser("root", "pass")
        }
    }

    @Test
    fun testGuest() {
        assertThrows<Exception>("Removing null is prohibited") {
            user.createUser("guest", "pass")
        }
    }

    @Test
    fun testNonUnique() {
        val login: String = UUID.randomUUID().toString()
        user.createUser(login, "pass")
        assertThrows<Exception>("Value should be unique: login") {
            user.createUser(login, "pass")
        }
        assertTrue(user.deleteUser(login))
    }
}