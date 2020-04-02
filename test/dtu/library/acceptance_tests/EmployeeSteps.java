package dtu.library.acceptance_tests;

import app.Employee;
import app.OperationNotAllowedException;
import app.ProjectApp;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class EmployeeSteps {

    private ProjectApp projectApp;
    private ErrorMessageHolder errorMessage;
    private Employee user;

    public EmployeeSteps(ProjectApp projectApp, ErrorMessageHolder errorMessage) {
        this.projectApp = projectApp;
        this.errorMessage = errorMessage;
    }

    @Given("the user is an Employee")
    public void userIsEmployee() throws OperationNotAllowedException {
        user = new Employee("emil");
        projectApp.setUser(projectApp.getCEO());
        projectApp.addNewEmployee(user);
        projectApp.setUser(user);
    }

    @Given("the user is CEO")
    public void userIsCEO() {
        projectApp.setCEO(user);
        assertTrue(projectApp.isCEO());
    }

    @Given("the user is not CEO")
    public void userIsNotCEO() {
        projectApp.setCEO(new Employee("derp"));
        assertFalse(projectApp.isCEO());
    }

    @When("the ProjectApp does not contain an Employee {string}")
    public void hasNoEmployee(String username) {
        assertFalse(projectApp.isEmployee(username));
    }

    @When("the user adds a new Employee {string}")
    public void addNewEmployee(String username) {
        try {
            projectApp.addNewEmployee(new Employee(username));
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
    }

    @Given("the ProjectApp contains a new Employee {string}")
    public void hasEmployee(String username) {
        Employee temp = projectApp.getUser();
        projectApp.setUser(projectApp.getCEO());
        addNewEmployee(username);
        projectApp.setUser(temp);
        hasNewEmployee(username);
    }

    @Then("the ProjectApp now contains an Employee {string}")
    public void hasNewEmployee(String username) {
        assertTrue(projectApp.isEmployee(username));
    }

    @Then("the error message {string} is given")
    public void theErrorMessageIsGiven(String errorMessage) {
        assertEquals(errorMessage, this.errorMessage.getErrorMessage());
    }

    @When("the user removes the Employee with username {string}")
    public void userRemovesEmployeeFromApp(String username) {
        try {
            projectApp.removeEmployee(username);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
    }
}
