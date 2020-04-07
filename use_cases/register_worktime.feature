Feature: Register Worktime
  Background:
    Given the ProjectApp contains project 200001
    And the user is an Employee
    And the user is on project 200001
    And project 200001 contains activity "Activity1"

  Scenario: An Employee registers time on an Activity
    When the user sets worktime of 7.5 hours to "Activity1" on date 20200420 on project 200001
    Then "Activity1" on project 200001 on date 20200420 has 7.5 hours from user

  Scenario: An Employee registers time on an Activity twice
    When the user sets worktime of 7.5 hours to "Activity1" on date 20200420 on project 200001
    And the user sets worktime of 5 hours to "Activity1" on date 20200420 on project 200001
    Then "Activity1" on project 200001 on date 20200420 has 5 hours from user