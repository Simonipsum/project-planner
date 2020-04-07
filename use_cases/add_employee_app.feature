Feature: Add Employee to ProjectApp
  Background:
    Given the user is an Employee
    And the ProjectApp does not contain an Employee "jan"

  Scenario: CEO adds new Employee
    Given the user is CEO
    When the user adds a new Employee "jan"
    Then the ProjectApp now contains an Employee "jan"
    And the error message "" is given

  Scenario: Employee tries to add Employee
    Given the user is not CEO
    When the user adds a new Employee "jan"
    Then the error message "Insufficient Permissions. User is not CEO." is given
    Then the ProjectApp does not contain an Employee "jan"

  Scenario: CEO adds two new Employees with same username
    Given the user is CEO
    When the user adds a new Employee "jan"
    Then the ProjectApp now contains an Employee "jan"
    And the error message "" is given
    When the user adds a new Employee "jan"
    Then the error message "Employee with that username already registered" is given
