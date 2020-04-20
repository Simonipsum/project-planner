package dtu.library.acceptance_tests;

import app.Employee;
import app.OperationNotAllowedException;
import app.Model;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class EmployeeSteps {

    private Model model;
    private ErrorMessageHolder errorMessage;
    private Employee user;

    public EmployeeSteps(Model model, ErrorMessageHolder errorMessage) {
        this.model = model;
        this.errorMessage = errorMessage;
    }

    @Given("the user is an Employee")
    public void userIsEmployee() throws OperationNotAllowedException {
        user = new Employee("emil");
        model.setUser(model.getCEO());
        model.addNewEmployee(user);
        model.setUser(user);
    }

    @Given("the user is CEO")
    public void userIsCEO() {
        model.setCEO(user);
        assertTrue(model.isCEO());
    }

    @Given("the user is not CEO")
    public void userIsNotCEO() {
        model.setCEO(new Employee("derp"));
        assertFalse(model.isCEO());
    }

    @When("the ProjectApp does not contain an Employee {string}")
    public void hasNoEmployee(String username) {
        assertFalse(model.isEmployee(username));
    }

    @When("the user adds a new Employee {string}")
    public void addNewEmployee(String username) {
        try {
            model.addNewEmployee(new Employee(username));
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
    }

    @Given("the ProjectApp contains a new Employee {string}")
    public void hasEmployee(String username) {
        Employee temp = model.getUser();
        model.setUser(model.getCEO());
        addNewEmployee(username);
        model.setUser(temp);
        hasNewEmployee(username);
    }

    @Then("the ProjectApp now contains an Employee {string}")
    public void hasNewEmployee(String username) {
        assertTrue(model.isEmployee(username));
    }

    @Then("the error message {string} is given")
    public void theErrorMessageIsGiven(String errorMessage) {
        assertEquals(errorMessage, this.errorMessage.getErrorMessage());
    }

    @When("the user removes the Employee with username {string}")
    public void userRemovesEmployeeFromApp(String username) {
        try {
            model.removeEmployee(username);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
    }
}
