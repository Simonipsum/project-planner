Feature: Add Activity to Project
  Background:
    Given the ProjectApp contains project 200001
    And the user is an Employee

  Scenario: PM adds an activity to the Project
    Given the user is PM of the project with ID 200001
    When the user adds an activity with name "Activity1" to the project with ID 200001
    Then project 200001 will contain activity "Activity1"
    Then the error message "" is given

  Scenario: Employee adds and activity to the Project
    Given the user is not PM of the project with ID 200001
    When the user adds an activity with name "Activity1" to the project with ID 200001
    Then the error message "Insufficient Permissions. User is not PM." is given