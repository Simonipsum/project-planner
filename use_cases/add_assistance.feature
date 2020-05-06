Feature: Get assistance for an existing activity
  Background:
    Given the ProjectApp contains project 200001
    And the user is an Employee
    And the ProjectApp contains a new Employee "jan"
    And project 200001 contains activity "Activity1"
    And project 200001 contains activity "Activity2"

  Scenario: user on project asks for assistance
    Given the user is on project 200001
    When the user ask for assistance on activity "Activity1" in project 200001 from employee "jan"
    Then the activity with name "Activity1" in project 200001 has an assistant "jan"
    And the error message "" is given

  Scenario: user asks for assistance on two different activities
    Given the user is on project 200001
    When the user ask for assistance on activity "Activity1" in project 200001 from employee "jan"
    And the user ask for assistance on activity "Activity2" in project 200001 from employee "jan"
    Then the activity with name "Activity1" in project 200001 has an assistant "jan"
    And the activity with name "Activity2" in project 200001 has an assistant "jan"
    And the error message "" is given

  Scenario: user on project asks for assistance on invalid activity
    Given the user is on project 200001
    When the user ask for assistance on activity "Activity3" in project 200001 from employee "jan"
    Then the error message "Error: Project does not have an Activity with that name." is given
    And the activity with name "Activity3" in project 200001 does not have an assistant "jan"
    And the project with ID 200001 does not contain an activity with name "Activity3"

  Scenario: user not on project asks for assistance
    When the user ask for assistance on activity "Activity1" in project 200001 from employee "jan"
    Then the activity with name "Activity1" in project 200001 does not have an assistant "jan"
    And the error message "Insufficient Permissions: User is not assigned to that project." is given

  Scenario: user on project asks for assistance form a user that already on the project
    Given "jan" is on project 200001
    And the user is on project 200001
    When the user ask for assistance on activity "Activity1" in project 200001 from employee "jan"
    Then the activity with name "Activity1" in project 200001 does not have an assistant "jan"
    And the error message "Error: Project already has Employee." is given