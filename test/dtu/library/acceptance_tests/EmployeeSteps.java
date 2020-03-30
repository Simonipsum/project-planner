package dtu.library.acceptance_tests;

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
    private String user;
    private String ceo;

    public EmployeeSteps(ProjectApp projectApp, ErrorMessageHolder errorMessage) {
        this.projectApp = projectApp;
        this.errorMessage = errorMessage;
    }

    @Given("the user is an Employee")
    public void userIsEmployee() throws OperationNotAllowedException {
        user = "emil";
        projectApp.setUser(projectApp.getCEO());
        projectApp.addNewEmployee(user);
        projectApp.setUser(user);
    }

    @Given("the user is CEO")
    public void userIsCEO() {
        ceo = user;
        projectApp.setCEO(ceo);
        assertTrue(projectApp.isCEO());
    }

    @Given("the user is not CEO")
    public void userIsNotCEO() {
        ceo = user + "derp";
        projectApp.setCEO(ceo);
        assertFalse(projectApp.isCEO());
    }

    @When("there is no employees with the username {string}")
    public void hasNoEmployee(String username) {
        assertFalse(projectApp.isEmployee(username));
    }

    @When("the user adds a new Employee with username {string}")
    public void addNewEmployee(String username) {
        try {
            projectApp.addNewEmployee(username);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
    }

    @Given("the ProjectApp contains an Employee with username {string}")
    public void hasEmployee(String username) {
        projectApp.setUser(projectApp.getCEO());
        addNewEmployee(username);
        projectApp.setUser(user);
        hasNewEmployee(username);
    }

    @Then("the ProjectApp now contains an Employee with username {string}")
    public void hasNewEmployee(String username) {
        assertTrue(projectApp.isEmployee(username));
    }

    @Then("the error message {string} is given")
    public void theErrorMessageIsGiven(String errorMessage) {
        assertEquals(errorMessage, this.errorMessage.getErrorMessage());
    }




}
