Feature: Remove Employee from ProjectApp
  Background:
    Given the user is an Employee
    And the ProjectApp contains a new Employee "jan"

  Scenario: CEO fires Employee
    Given the user is CEO
    When the user removes the Employee with username "jan"
    Then the ProjectApp does not contain an Employee "jan"
    And the error message "" is given

  Scenario: Employee unsuccessfully fires Employee
    Given the user is not CEO
    When the user removes the Employee with username "jan"
    Then the ProjectApp now contains an Employee "jan"
    And the error message "Insufficient Permissions: User is not CEO." is given

  Scenario: Fire employee that is also pm of a project
    Given the user is CEO
    And the ProjectApp contains project 200001
    And the user sets PM of project 200001 to "jan"
    When the user removes the Employee with username "jan"
    Then the ProjectApp does not contain an Employee "jan"
    And the project with ID 200001 doesn't have a PM

  Scenario: CEO tries to remove themselves
    Given the user is CEO
    When the user removes themselves
    Then the ProjectApp now contains an Employee "huba"
    And the error message "Error: User can't be removed from app." is given