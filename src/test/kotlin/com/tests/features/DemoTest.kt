package com.tests.features

import io.qameta.allure.Description
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.Owner
import io.qameta.allure.Severity
import io.qameta.allure.SeverityLevel.NORMAL
import io.qameta.allure.Step
import io.qameta.allure.Story
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Tags
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class DemoTest {

    @Test
    @Tags(
        Tag("smoke"),
        Tag("automated"),
        Tag("critical")
    )
    @Epic("Web interface")
    @Feature("Essential features")
    @Story("Authentication")
    @DisplayName("Test Authentication")
    @Description("This test attempts to log into the website using a login and a password. Fails if any error happens.\n\nNote that this test does not test 2-Factor Authentication.")
    @Severity(
        NORMAL
    )
    @Owner("John Doe")
    fun testAuthentication() {
        println("test start")
        step1()
        step2()
    }

    @Step("Step 1")
    fun step1() {
        println("step1 start")
        subStep1()
        subStep2()
    }

    @Step("Sub-step 1")
    fun subStep1() {
        println("substep1 start")
    }

    @Step("Sub-step 2")
    fun subStep2() {
        println("substep2 start")
    }

    @Step("Step 2")
    fun step2() {
        println("step2 start")
    }

    @Test
    @Tag("manual")
    @Epic("Web interface2")
    @Feature("Essential features2")
    @Story("Authentication2")
    @DisplayName("Test Authentication2")
    @Description("This test a2 ttempts to log into the website using a login and a password. Fails if any error happens.\n\nNote that this test does not test 2-Factor Authentication.")
    @Severity(
        NORMAL
    )
    @Owner("John Doe2")
    fun testAuthentication2() {
        println("test start2")
        step1()
        step2()
    }
}
