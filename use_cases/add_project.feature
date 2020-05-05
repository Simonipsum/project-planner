Feature: Add Project
  Description: A project is added to the planner
  Actors: user - An Employee or CEO

  Background:
    Given the user is an Employee

  Scenario: CEO adds new Project to ProjectApp
    Given the user is CEO
    And the ProjectApp does not contain any projects
    When the user adds a project named "Project1" in the year 2020
    Then a project with name "Project1" and project ID 200001 exists in the ProjectApp.
    And the error message "" is given

  Scenario: Failed creation of new project
    Given the user is not CEO
    And the ProjectApp does not contain any projects
    When the user adds a project named "Project1" in the year 2020
    Then the error message "Insufficient Permissions: User is not CEO." is given
    And the ProjectApp does not contain any projects

  Scenario: CEO sets PM of existing Project
    Given the ProjectApp contains project 200001
    And the user is CEO
    And the ProjectApp contains a new Employee "jan"
    When the user sets PM of project 200001 to "jan"
    Then the PM of the project with ID 200001 is "jan"
    And the error message "" is given

  Scenario: CEO sets PM of existing Project unsuccessfully
    Given the ProjectApp contains project 200001
    And the user is CEO
    And the ProjectApp does not contain an Employee "jan"
    When the user sets PM of project 200001 to "jan"
    Then the error message "Insufficient Permissions: User is not an Employee." is given

  Scenario: Employee unsuccessfully sets PM of existing Project
    Given the ProjectApp contains project 200001
    And the user is not CEO
    And the ProjectApp contains a new Employee "jan"
    When the user sets PM of project 200001 to "jan"
    Then the error message "Insufficient Permissions: User is not CEO." is given

  Scenario: CEO adds new Project with no name to ProjectApp
    Given the user is CEO
    And the ProjectApp does not contain any projects
    When the user adds a project named "N" in the year 2020
    Then a project with no name and project ID 200001 exists in the ProjectApp.
    And the error message "" is given