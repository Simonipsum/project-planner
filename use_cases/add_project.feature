Feature: Add Project
  Description: A project is added to the planner
  Actors: user

  Scenario: CEO adds new Project to ProjectApp
    Given the user is an Employee
    And the user is CEO
    And the ProjectApp does not contain any projects
    When the user adds a project named "Project1" in the year 2020
    Then a project with name "Project1" and project ID 200001 exists in the ProjectApp.

  Scenario: Failed creation of new project
    Given the user is an Employee
    And the user is not CEO
    And the ProjectApp does not contain any projects
    When the user adds a project named "Project1" in the year 2020
    Then the error message "Insufficient Permissions. User is not CEO." is given
    And the ProjectApp does not contain any projects


  Scenario: CEO sets PM of existing Project
    Given the ProjectApp contains a project with ID 200001
    And the user is an Employee
    And the user is CEO
    And the ProjectApp contains an Employee with username "jan"
    When the user sets the PM of the project with ID 200001 to "jan"
    Then the PM of the project with ID 200001 is "jan"
