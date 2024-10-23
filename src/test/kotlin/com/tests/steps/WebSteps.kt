package com.tests.steps

import io.cucumber.java.en.And
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.jupiter.api.Assertions
import java.util.Random

class WebSteps {
    @When("I open labels page")
    fun openLabelsPage() {
        maybeThrowElementNotFoundException()
    }

    @When("I open milestones page")
    fun openMilestonesPage() {
    }

    @And("I create label with title {string}")
    fun createLabelWithTitle(title: String?) {
        maybeThrowElementNotFoundException()
    }

    @And("I create milestone with title {string}")
    fun createMilestoneWithTitle(title: String?) {
    }

    @And("I delete label with title {string}")
    fun deleteLabelWithTitle(title: String) {
        maybeThrowAssertionException(title)
    }

    @Then("I should see issue with label title {string}")
    fun labelsShouldContainsNoteWithText(title: String) {
        maybeThrowAssertionException(title)
    }

    @Then("I should not see note with content {string}")
    fun notesShouldNotContainsNoteWithText(text: String) {
        maybeThrowAssertionException(text)
    }

    @And("I delete milestone with title {string}")
    fun deleteMilestoneWithTitle(title: String?) {
    }

    @Then("I should see milestone with title {string}")
    fun shouldSeeMilestoneWithTitle(title: String?) {
    }

    @Then("I should not see milestone with content {string}")
    fun shouldNotSeeMilestoneWithContent(title: String?) {
    }

    @Then("I should see label with title {string}")
    fun iShouldSeeLabelWithTitle(text: String?) {
    }

    @When("I open issue with id {int}")
    fun openIssuePage(id: Int) {
        maybeThrowElementNotFoundException()
    }

    @And("I add label with title {string} to issue")
    fun addLabelToIssue(text: String?) {
        maybeThrowElementNotFoundException()
    }

    @And("I filter issue by label title {string}")
    fun filterIssueByLabel(title: String?) {
        maybeThrowElementNotFoundException()
    }

    private fun maybeThrowElementNotFoundException() {
        if (isTimeToThrowException()) {
            throw RuntimeException("Element not found for xpath [//div[@class='something']]")
        }
    }

    private fun maybeThrowAssertionException(text: String) {
        try {
            Thread.sleep(1000)
            if (isTimeToThrowException()) {
                Assertions.assertEquals("another text", text)
            }
        } catch (e: InterruptedException) {
            // do nothing test is dummy
        }
    }

    private fun isTimeToThrowException(): Boolean {
        return Random().nextBoolean() && Random().nextBoolean()
    }
}
