Feature: Add Employee to ProjectApp

  Scenario: CEO adds new Employee
    Given the user is an Employee
    And the user is CEO
    When there is no employees with the username "jan"
    And the user adds a new Employee with username "jan"
    Then the ProjectApp contains an Employee with username "jan"

  Scenario: Employee tries to add Employee
    Given the user is an Employee
    And the user is not CEO
    When the user adds a new Employee with username "jan"
    Then the error message "Insufficient Permissions. User is not CEO." is given