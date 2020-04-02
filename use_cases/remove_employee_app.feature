Feature: Remove Employee from ProjectApp
  Background:
    Given the user is an Employee
    And the ProjectApp contains a new Employee "jan"

  Scenario: CEO fires Employee
    Given the user is CEO
    When the user removes the Employee with username "jan"
    Then the ProjectApp does not contain an Employee "jan"

  Scenario: Employee unsuccessfully fires Employee
    Given the user is not CEO
    When the user removes the Employee with username "jan"
    Then the ProjectApp now contains an Employee "jan"
    And the error message "Insufficient Permissions. User is not CEO." is given