Feature: Remove Employee from Project
  Background:
    Given the user is an Employee
    And the ProjectApp contains a new Employee "jan"
    And the ProjectApp contains project 200001

  Scenario: Remove employee from Project
    Given the user is PM of the project with ID 200001
    And the user adds "jan" to the project with ID 200001
    When the user removes the employee "jan" from project 200001
    Then the employee list of project 200001 does not contain the employee "jan"
    And the error message "" is given

  Scenario: Remove employee that is not on project
    Given the user is PM of the project with ID 200001
    When the user removes the employee "jan" from project 200001
    Then the error message "Error: Project does not contain employee" is given
    And the employee list of project 200001 does not contain the employee "jan"

