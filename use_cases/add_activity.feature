Feature: Add Activity to Project
  Scenario: PM adds an activity to the Project
    Given the ProjectApp contains a project with ID 200001
    And the user is an Employee
    And the user is PM of the project with ID 200001
    When the user adds an activity with name "Activity1" to the project with ID 200001
    Then the project 200001 contains the activity "Activity1"

#  Scenario: Employee adds and activity to the Project
#    Given the ProjectApp contains a project with ID 200001
#    And the user is an Employee
#    And the user is not PM of the project with ID 200001
#    When the user adds an activity with name "Activity1" to the project with ID 200001
#    Then ERROR