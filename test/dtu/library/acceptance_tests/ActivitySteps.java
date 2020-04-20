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
    private ProjectApp app;
    private ErrorMessageHolder errorMessage;

    public ActivitySteps(ProjectApp app, ErrorMessageHolder errorMessage) {
        this.app = app;
        this.errorMessage = errorMessage;
    }

    @When("the user adds an activity with name {string} to the project with ID {int}")
    public void userAddsActivityToProject(String name, int id) {
        try {
            app.addNewActivity(name, id);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
    }

    @Then("project {int} will contain activity {string}")
    public void projectContainsActivity(int id, String name) {
        assertTrue(app.getProject(id).hasActivity(name));
    }

    @Given("project {int} contains activity {string}")
    public void projectAlreadyContainsActivity(int id, String name) {
        Employee temp = app.getUser();
        app.setUser(app.getProject(id).getPm());
        userAddsActivityToProject(name, id);
        app.setUser(temp);
    }

    @When("the user sets the start date as {int} and end date as {int} of {string} in project {int}")
    public void userSetsDatesOfProjectActivity(int start, int end, String name, int id) {
        try {
            app.setDates(id, name, start, end);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
    }

    @Then("the activity with name {string} in project {int} has start date {int} and end date {int}")
    public void projectActivityHasDates(String name, int id, int start, int end) {
        assertEquals(app.getProject(id).getDates(name)[0], start);
        assertEquals(app.getProject(id).getDates(name)[1], end);
    }

    @When("the user sets the name of {string} from project {int} to {string}")
    public void userSetsNameOfProjectActivity(String name, int id, String newname) {
        try {
            app.setName(name, id, newname);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
    }

    @Then("the project with ID {int} does not contain an activity with name {string}")
    public void projectDoesNotContainActivity(int id, String name) {
        assertFalse(app.getProject(id).hasActivity(name));
    }

    @When("the user registers absence from date {int} to date {int}")
    public void userRegistersAbsence(int start, int end) {
        app.registerAbsence(start, end);
    }

    @Then("the user is absent for date {int}")
    public void userIsAbsent(int date) {
        assertTrue(app.getAbsence().getWorkTime().get(app.getUser()).get(date) == 8);
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

    @When("the user sets worktime of {float} hours to {string} on date {int} on project {int}")
    public void userSetsWorktimeOfActivity(float time, String name, int date, int id) {
        app.setWorkTime(date, time, id, name);
    }

    @When("the user sets the expected worktime of {string} from project {int} to {float}")
    public void userSetsExpectedWorktimeOfActivity(String name, int id, float time) {
        try {
            app.setExpectedWT(id, name, time);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
    }

    @Then("{string} on project {int} on date {int} has {float} hours from user")
    public void activityHasWorktimeFromUser(String name, int id, int date, float time) {
        assertTrue(app.getProject(id).getActivity(name).getWorkTime().get(app.getUser()).get(date) == time);
    }

    @Then("{string} of project {int} has expected worktime {float}")
    public void activityHasExpectedWorktime(String name, int id, float time) {
        assertTrue(app.getProject(id).getActivity(name).getExpectedWorkTime() == time);
    }

}
