Feature: Add Employee to ProjectApp

  Scenario: CEO adds new Employee
    Given the user is an Employee
    And the user is CEO
    And the ProjectApp does not contain an Employee with username "jan"
    When the user adds a new Employee with username "jan"
    Then the ProjectApp contains an Employee with username "jan"

  Scenario: Employee tries to add Employee
    Given the user is an Employee
    And the user is not CEO
    And the ProjectApp does not contain an Employee with username "jan"
    When the user adds a new Employee with username "jan"
    Then the error message "Insufficient Permissions. User is not CEO." is given
    And the ProjectApp does not contain an Employee with username "jan"

#  Scenario: CEO tries to add Employee with existing username
