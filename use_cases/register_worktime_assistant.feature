Feature: Assistant registers worktime
  Background:
    Given the ProjectApp contains project 200001
    And the user is an Employee
    And the user is on project 200001
    And project 200001 contains activity "Activity1"
    And the ProjectApp contains a new Employee "jan"

  Scenario: Successful
    Given the user ask for assistance on activity "Activity1" in project 200001 from employee "jan"
    When the employee "jan" sets worktime of 7.5 hours to "Activity1" on date 20200420 on project 200001
    Then "Activity1" on project 200001 on date 20200420 has 7.5 hours from "jan"
    And project 200001 has total worktime of 7.5 hours
    And the error message "" is given

  Scenario: Unsuccessful
    When the employee "jan" sets worktime of 7.5 hours to "Activity1" on date 20200420 on project 200001
    Then the error message "Insufficient Permissions: User is not assigned to that project or is an assistant on that activity" is given
