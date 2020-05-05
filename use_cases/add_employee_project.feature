Feature: Add Employee to a Project
  Background:
    Given the ProjectApp contains project 200002
    And the user is an Employee
    And the ProjectApp contains a new Employee "jan"

  Scenario: PM adds Employee to a Project
    Given the user is PM of the project with ID 200001
    When the user adds "jan" to the project with ID 200001
    Then the employee list of project 200001 contains the employee "jan"
    And the error message "" is given

  Scenario: Employee adds Employee to a Project
    Given the user is not PM of the project with ID 200001
    When the user adds "jan" to the project with ID 200001
    Then the error message "Insufficient Permissions: User is not PM." is given
    And the employee list of project 200001 does not contain the employee "jan"
