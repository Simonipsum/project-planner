Feature: Change name of existing project
  Background:
    Given the ProjectApp contains project 200001
    And project with ID 200001 is named "project1"
    And the user is an Employee

  Scenario: PM changes name of project
    Given the user is PM of the project with ID 200001
    When the user changes the name of the project with ID 200001 to "newName"
    Then a project with name "newName" and project ID 200001 exists in the ProjectApp.
    And the error message "" is given

  Scenario: Employee changes name of project
    Given the user is not PM of the project with ID 200001
    When the user changes the name of the project with ID 200001 to "newName"
    Then the error message "Insufficient Permissions. User is not PM." is given