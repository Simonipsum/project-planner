Feature: Register Absence
  Scenario: Employee registers absence
    Given the user is an Employee
    When the user registers absence for date 20200101
    Then the user is absent for date 20200101

  Scenario: Employee unsuccessfully registers absence