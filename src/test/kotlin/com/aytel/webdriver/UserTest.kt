package com.aytel.webdriver

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
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
        assertTrue(user.createUser(login, "pass"))
        assertTrue(user.deleteUser(login))
    }

    @Test
    fun testEmpty() {
        assertFalse(user.createUser("", "pass"))
    }

    @Test
    fun testWithSpace() {
        assertFalse(user.createUser("lo gin", "pass"))
    }
}