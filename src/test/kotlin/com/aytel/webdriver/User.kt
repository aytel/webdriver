package com.aytel.webdriver

import org.openqa.selenium.By
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.util.stream.Collectors

class User(private val driver: WebDriver) {
    private val loginPage: String = "http://localhost:8080/login"
    private val rootPassword: String = "pass"

    private val usersPage: String = "http://localhost:8080/users"
    private val createUserId: String = "id_l.U.createNewUser"
    private val createUserOkId: String = "id_l.U.cr.createUserOk"

    private fun waitForClickable(by: By) {
        val wait = WebDriverWait(driver, 10)
        wait.until<WebElement>(ExpectedConditions.visibilityOfElementLocated(by))
    }

    fun loginAsRoot() {
        driver.get(loginPage)
        waitForClickable(By.id("id_l.L.loginButton"))
        driver.findElement(By.id("id_l.L.login")).sendKeys("root");
        driver.findElement(By.id("id_l.L.password")).sendKeys(rootPassword);
        driver.findElement(By.id("id_l.L.loginButton")).click();
    }

    fun createUser(login: String, password: String): Boolean {
        driver.get(usersPage)
        waitForClickable(By.id(createUserId))
        driver.findElement(By.id(createUserId)).click()
        waitForClickable(By.id("id_l.U.cr.login"))

        driver.findElement(By.id("id_l.U.cr.login")).sendKeys(login)
        driver.findElement(By.id("id_l.U.cr.password")).sendKeys(password)
        driver.findElement(By.id("id_l.U.cr.confirmPassword")).sendKeys(password)
        driver.findElement(By.id(createUserOkId)).click()

        return try {
            WebDriverWait(driver, 2).until(ExpectedConditions.urlContains("editUser/$login"))
            true
        } catch (e: TimeoutException) {
            false
        }
    }

    fun deleteUser(login: String): Boolean {
        driver.get(usersPage)
        waitForClickable(By.id(createUserId))
        val elements: List<WebElement> = driver.findElements(By.linkText("Delete")).stream()
            .filter { elem: WebElement -> elem.getAttribute("id").endsWith(login.replace("-", "_"))}
            .collect(Collectors.toList())
        if (elements.size != 1) {
            return false
        }
        elements[0].click()
        driver.switchTo().alert().accept()
        return true
    }
}