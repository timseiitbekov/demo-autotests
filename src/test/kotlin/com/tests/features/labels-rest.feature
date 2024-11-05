Feature: Labels API

  @smoke @automated
  Scenario: Create new label via api
    When I create label with title "hello" via api
    Then I should see label with title "hello" via api

  @regress @automated
  Scenario: Delete existing label via api
    When I create label with title "hello" via api
    And I delete label with title "hello" via api
    Then I should not see label with content "hello" via api

  @gorest
  Scenario: Create a new user validate presence
    When I create user with following params:
      | name   | "Automated name"        |
      | email  | someemaildemo@gmail.com |
      | status | inactive                |
      | gender | male                    |
    Then I validate user is present with email "someemaildemo@gmail.com"