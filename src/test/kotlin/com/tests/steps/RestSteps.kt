package com.tests.steps

import com.tests.configurations.CucumberTest
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.qameta.allure.Allure.step

class RestSteps: CucumberTest() {
    @When("I create label with title {string} via api")
    fun createLabelWithTitle(title: String?) {
        step("POST /repos/:owner/:repo/labels")
    }

    @When("I delete label with title {string} via api")
    fun deleteLabelWithTitle(title: String) {
        step("GET /repos/:owner/:repo/labels?text=$title")
        step("DELETE /repos/:owner/:repo/labels/237")
    }

    @Then("I should see label with title {string} via api")
    fun labelsShouldContainsNoteWithText(text: String) {
        step("GET /repos/:owner/:repo/labels?text=$text")
        step("GET /repos/:owner/:repo/labels/834")
    }

    @Then("I should not see label with content {string} via api")
    fun labelsShouldNotContainsNoteWithText(text: String) {
        step("GET /repos/:owner/:repo/labels?text=$text")
        step("GET /repos/:owner/:repo/labels/834")
    }
}
