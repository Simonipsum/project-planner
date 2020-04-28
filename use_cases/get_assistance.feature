Feature: Get assistance for an existing activity
  Background:
    Given the ProjectApp contains project 200001
    And the user is an Employee
    And the ProjectApp contains a new Employee "jan"
    And project 200001 contains activity "Activity1"

  Scenario: user on project asks for assistance
    And the user is on project 200001
    When the user ask for assistance on activity "Activity1" in project 200001 from employee "jan"
    Then the activity with name "Activity1" in project 200001 has an assistant "jan"

   Scenario: user not on project asks for assistance

