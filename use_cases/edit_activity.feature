Feature: Edit existing Project Activity
  Background:
    Given the ProjectApp contains a project with ID 200001
    And the user is an Employee
    And project 200001 contains activity "Activity1"

  Scenario: PM sets start/end date of Activity
    Given the user is PM of the project with ID 200001
    When the user sets the start date as 20200101 and end date as 20200501 of "Activity1" in project 200001
    Then the activity with name "Activity1" in project 200001 has start date 20200101 and end date 20200501

  Scenario: Employee sets start/end date of Activity
    Given the user is not PM of the project with ID 200001
    When the user sets the start date as 20200101 and end date as 20200501 of "Activity1" in project 200001
    Then the activity with name "Activity1" in project 200001 has start date -1 and end date -1
    And the error message "Insufficient Permissions. User is not PM." is given

  Scenario: PM edits name of Activity
    Given the user is PM of the project with ID 200001
    When the user sets the name of "Activity1" from project 200001 to "Activity2"
    Then project 200001 will contain activity "Activity2"
    And the project with ID 200001 does not contain an activity with name "Activity1"

#  Scenario: PM sets expected worktime of Activity
#    Given the user is PM of the project with ID 200001
#    And the project with ID 200001 contains an activity with name "Activity1"
#    When the user sets the expected work time of "Activity1" from project 200001 to 10
#    Then "Activity1" of project 200001 has expected work time 10