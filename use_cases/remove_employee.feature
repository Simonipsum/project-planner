Feature: Remove Employee from ProjectApp
  Scenario: CEO fires Employee
    Given the user is an Employee
    And the user is CEO
    And the ProjectApp contains an Employee with username "jan"
    When the user removes the Employee with username "jan"
    Then the ProjectApp does not contain an Employee with username "jan"

  Scenario: Employee unsuccessfully fires Employee
    Given the user is an Employee
    And the user is not CEO
    And the ProjectApp contains an Employee with username "jan"
    When the user removes the Employee with username "jan"
    Then the ProjectApp contains an Employee with username "jan"
    And the error message "Insufficient Permissions. User is not CEO." is given