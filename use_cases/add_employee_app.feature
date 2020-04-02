Feature: Add Employee to ProjectApp
  Background:
    Given the user is an Employee
    And the ProjectApp does not contain an Employee "jan"

  Scenario: CEO adds new Employee
    Given the user is CEO
    When the user adds a new Employee "jan"
    Then the ProjectApp now contains an Employee "jan"

  Scenario: Employee tries to add Employee
    Given the user is not CEO
    When the user adds a new Employee "jan"
    Then the error message "Insufficient Permissions. User is not CEO." is given
    And the ProjectApp does not contain an Employee "jan"

#  Scenario: CEO tries to add Employee with existing username
