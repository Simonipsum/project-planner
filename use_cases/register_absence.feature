Feature: Register Absence
  Background:
    Given the user is an Employee

  Scenario: Employee registers absence for one day
    When the user registers absence from date 20200101 to date 20200101
    Then the user is absent for date 20200101
    And the error message "" is given

  Scenario: Employee registers absence for one month
    When the user registers absence from date 20200101 to date 20200131
    Then the user is absent for date 20200101 till date 20200131
    And the error message "" is given

  Scenario: Employee registers absence wrong
    When the user registers absence from date 20200131 to date 20200101
    Then the error message "Start date must be before end date." is given

  Scenario: Input invalid date
    When the user registers absence from date 0 to date 0
    Then the error message "Error: Date not valid!" is given