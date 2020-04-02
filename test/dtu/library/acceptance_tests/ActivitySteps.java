package dtu.library.acceptance_tests;

import app.Employee;
import app.OperationNotAllowedException;
import app.ProjectApp;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Date;

import static org.junit.Assert.*;

public class ActivitySteps {
    private ProjectApp projectApp;
    private ErrorMessageHolder errorMessage;

    public ActivitySteps(ProjectApp projectApp, ErrorMessageHolder errorMessage) {
        this.projectApp = projectApp;
        this.errorMessage = errorMessage;
    }

    @When("the user adds an activity with name {string} to the project with ID {int}")
    public void userAddsActivityToProject(String name, int id) {
        try {
            projectApp.addNewActivity(name, id);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
    }

    @Then("project {int} will contain activity {string}")
    public void projectContainsActivity(int id, String name) {
        assertTrue(projectApp.getProject(id).hasActivity(name));
    }

    @Given("project {int} contains activity {string}")
    public void projectAlreadyContainsActivity(int id, String name) {
        Employee temp = projectApp.getUser();
        projectApp.setUser(projectApp.getProject(id).getPm());
        userAddsActivityToProject(name, id);
        projectApp.setUser(temp);
    }

    @When("the user sets the start date as {int} and end date as {int} of {string} in project {int}")
    public void userSetsDatesOfProjectActivity(int start, int end, String name, int id) {
        try {
            projectApp.setDates(id, name, start, end);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
    }

    @Then("the activity with name {string} in project {int} has start date {int} and end date {int}")
    public void projectActivityHasDates(String name, int id, int start, int end) {
        assertEquals(projectApp.getProject(id).getDates(name)[0], start);
        assertEquals(projectApp.getProject(id).getDates(name)[1], end);
    }

    @When("the user sets the name of {string} from project {int} to {string}")
    public void userSetsNameOfProjectActivity(String name, int id, String newname) {
        try {
            projectApp.setName(name, id, newname);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
    }

    @Then("the project with ID {int} does not contain an activity with name {string}")
    public void projectDoesNotContainActivity(int id, String name) {
        assertFalse(projectApp.getProject(id).hasActivity(name));
    }

    @When("the user registers absence from date {int} to date {int}")
    public void userRegistersAbsence(int start, int end) {
        projectApp.registerAbsence(start, end);
    }

    @Then("the user is absent for date {int}")
    public void userIsAbsent(int date) {
        assertTrue(projectApp.getAbsence().getWorkTime().get(projectApp.getUser()).get(date) == 8);
    }

    @Then("the user is absent for date {int} till date {int}")
    public void userIsAbsentForMultipleDates(int start, int end) {
        Date current = new Date(start/10000, (start%10000)/100, (start%100));
        Date last    = new Date(end/10000, (end%10000)/100, (end%100));
        int date;

        while(!current.after(last)) {
            date = current.getYear() * 10000 + current.getMonth() * 100 + current.getDate();
            current.setDate(current.getDate() + 1);
            userIsAbsent(date);
        }
    }
}
