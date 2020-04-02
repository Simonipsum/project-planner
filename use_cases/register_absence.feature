Feature: Register Absence
  Background:
    Given the user is an Employee

  Scenario: Employee registers absence for one day
    When the user registers absence from date 20200101 to date 20200101
    Then the user is absent for date 20200101

  Scenario: Employee registers absence for one month
    When the user registers absence from date 20200101 to date 20200131
    Then the user is absent for date 20200101 till date 20200131

  #Scenario: Employee unsuccessfully registers absence
  # can't register absence if already assigned time to other activities that day