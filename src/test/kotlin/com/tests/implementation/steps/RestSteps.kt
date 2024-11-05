package com.tests.implementation.steps

import com.google.gson.Gson
import com.tests.implementation.services.DemoService
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.qameta.allure.Allure.step

class RestSteps(
    private val demoService: DemoService
) {
    @When("I create label with title {string} via api")
    fun createLabelWithTitle(title: String?) {
        demoService.`some method service`()
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

    @When("I create user with following params:")
    fun createUserWithParams(dataTable: DataTable) {
        val user = demoService.`return model of user from datatable`(dataTable)
        demoService.`send POST request to endpoint with JSON body`("https://gorest.co.in/public/v2/users", Gson().toJson(user))
    }

    @Then("I validate user is present with email {string}")
    fun validateUserIsPresentWithEmail(string: String?) {
        val userGet = demoService.`send GET request to endpoint`("https://gorest.co.in/public/v2/users?email=$string")
        println(userGet.second.body().asString("application/json"))
    }
}
