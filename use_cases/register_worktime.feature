Feature: An Employee registers worktime
  Background:
    Given the ProjectApp contains a project with ID 200001
    And the user is an Employee
    And the user is on project 200001
    And project 200001 contains activity "Activity1"

#  Scenario: An Employee registers time on an Activity
#    When the user adds worktime of 7.5 hours to "Activity1" on date 20200420 on project 200001
#    Then "Activity1" on project 200001 on date 20200420 has 7.5 hours from user