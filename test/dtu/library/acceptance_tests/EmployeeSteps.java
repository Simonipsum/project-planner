package dtu.library.acceptance_tests;

import app.model.Employee;
import app.OperationNotAllowedException;

import app.model.ProjectApp;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class EmployeeSteps {

    private ProjectApp app;
    private ErrorMessageHolder errorMessage;
    private Employee user;

    public EmployeeSteps(ProjectApp app, ErrorMessageHolder errorMessage) {
        this.app = app;
        this.errorMessage = errorMessage;
    }

    @Given("the user is an Employee")
    public void userIsEmployee() throws OperationNotAllowedException {
        user = new Employee("emil");
        app.setUser(app.getCEO());
        app.addNewEmployee(user);
        app.setUser(user);
    }

    @Given("the user is CEO")
    public void userIsCEO() {
        app.setCEO(user);
        assertTrue(app.isUserCeo());
    }

    @Given("the user is not CEO")
    public void userIsNotCEO() {
        app.setCEO(new Employee("derp"));
        assertFalse(app.isUserCeo());
    }

    @When("the ProjectApp does not contain an Employee {string}")
    public void hasNoEmployee(String username) {
        assertFalse(app.hasEmployee(username));
    }

    @When("the user adds a new Employee {string}")
    public void addNewEmployee(String username) {
        try {
            app.addNewEmployee(new Employee(username));
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
    }

    @Given("the ProjectApp contains a new Employee {string}")
    public void hasEmployee(String username) {
        Employee temp = app.getUser();
        app.setUser(app.getCEO());
        addNewEmployee(username);
        app.setUser(temp);
        hasNewEmployee(username);
    }

    @Then("the ProjectApp now contains an Employee {string}")
    public void hasNewEmployee(String username) {
        assertTrue(app.hasEmployee(username));
    }

    @Then("the error message {string} is given")
    public void theErrorMessageIsGiven(String errorMessage) {
        assertEquals(errorMessage, this.errorMessage.getErrorMessage());
    }

    @When("the user removes the Employee with username {string}")
    public void userRemovesEmployeeFromApp(String username) {
        try {
            app.removeEmployee(username);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
    }
}
