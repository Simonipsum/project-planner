Feature: Edit existing Project Activity
  Background:
    Given the ProjectApp contains project 200001
    And the user is an Employee
    And project 200001 contains activity "Activity1"

  Scenario: PM sets start/end date of Activity
    Given the user is PM of the project with ID 200001
    When the user sets the start date as 20200101 and end date as 20200110 of "Activity1" in project 200001
    Then the activity with name "Activity1" in project 200001 has start date 20200101 and end date 20200110
    And the user will have overlap of 10 activity days for the period 20200101 to 20200110
    And the error message "" is given

  Scenario: PM sets start/end date of Activity with end date before start date
    Given the user is PM of the project with ID 200001
    When the user sets the start date as 20200501 and end date as 20200101 of "Activity1" in project 200001
    Then the error message "Start date must be before end date" is given

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
    And the error message "" is given

  Scenario: PM edits name of non-existing Activity
    Given the user is PM of the project with ID 200001
    When the user sets the name of "Activity2" from project 200001 to "Activity1"
    Then the error message "Project does not contain activity" is given
    And project 200001 will contain activity "Activity1"
    And the project with ID 200001 does not contain an activity with name "Activity2"

  Scenario: Employee edits name of Activity
    Given the user is not PM of the project with ID 200001
    When the user sets the name of "Activity1" from project 200001 to "Activity2"
    Then the error message "Insufficient Permissions. User is not PM." is given

  Scenario: PM sets expected worktime of Activity
    Given the user is PM of the project with ID 200001
    When the user sets the expected worktime of "Activity1" from project 200001 to 10
    Then "Activity1" of project 200001 has expected worktime 10
    And project 200001 has expected remaining worktime of 10
    And the error message "" is given

  Scenario: Employee sets expected worktime of Activity
    Given the user is not PM of the project with ID 200001
    When the user sets the expected worktime of "Activity1" from project 200001 to 10
    Then the error message "Insufficient Permissions. User is not PM." is given