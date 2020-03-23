Scenario: Successfuld creation of a new project
  Given that the ProjectApp doesn't include any projects
  And the year is 2020
  And the user wants to create a project named "Project1"
  When a user adds the project
  And the user is CEO
  Then a project with name "Project1" and project ID "200001" exists in the ProjectApp.

Scenario: Failed creation of new project
  Given that the ProjectApp doesn't include any projects
  And the year is 2020
  And the user wants to create a project named "Project1"
  When a user adds the project
  And the user is not CEO
  Then the errormessage "Insufficient Permissions" is thrown
  And the system does not contain any projects.