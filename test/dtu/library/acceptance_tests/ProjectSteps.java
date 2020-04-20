package dtu.library.acceptance_tests;

import app.Employee;
import app.OperationNotAllowedException;
import app.Model;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.*;

public class ProjectSteps {

    private Model model;
    private ErrorMessageHolder errorMessage;

    public ProjectSteps(Model model, ErrorMessageHolder errorMessage) {
        this.model = model;
        this.errorMessage = errorMessage;
    }

    @Given("the ProjectApp does not contain any projects")
    public void hasNoProject() {
        assertEquals(0, model.getProjects().size());
    }

    @Given("the ProjectApp contains project {int}")
    public void hasAProject(int id) {
        if (!model.hasProject(id)) {
            int year = id / 10000;
            int numb = id % 10000;

            Employee user = model.getUser();
            model.setUser(model.getCEO());
            for (int i = 0; i < numb; i++) {
                userCreatesProject("Project1", year);
            }
            model.setUser(user);
        }
    }

    @When("the user adds a project named {string} in the year {int}")
    public void userCreatesProject(String name, int year) {
        try {
            model.addNewProject(year, name);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
    }

    @Then("a project with name {string} and project ID {int} exists in the ProjectApp.")
    public void newProjectCreated(String name, int id) {
        assertTrue( model.getProject(id).getName().equals(name) );
    }

    @When("the user sets PM of project {int} to {string}")
    public void setPmOfProject(int id, String username) {
        try {
            model.setPM(username, id);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
    }

    @Then("the PM of the project with ID {int} is {string}")
    public void usernameIsPmOfProject(int id, String username) {
        assertEquals(model.getProject(id).getPm(), model.getEmployee(username));
    }

    @Given("the user is PM of the project with ID {int}")
    public void setUserPmOfProject(int id) {
        Employee temp = model.getUser();
        model.setUser(model.getCEO());
        setPmOfProject(id, temp.getUsername());
        model.setUser(temp);
        assertEquals(model.getProject(id).getPm(), temp);
    }

    @Given("the user is not PM of the project with ID {int}")
    public void setNotUserPmOfProject(int id) {
        assertNotEquals(model.getProject(id).getPm().getUsername(), model.getUser());
    }

    @When("the user adds {string} to the project with ID {int}")
    public void pmAddsEmployeeToProject(String username, int id) {
        try {
            model.addEmployee(username, id);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
    }

    @Given("the user is on project {int}")
    public void userIsOnProject(int id) {
        Employee temp = model.getUser();
        model.setUser(model.getProject(id).getPm());
        try {
            model.addEmployee(temp.getUsername(), id);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
        model.setUser(temp);
    }

    @When("the user removes the employee {string} from project {int}")
    public void userRemovesEmployeeFromProject(String name, int id) {
        try {
            model.removeEmployee(name, id);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
    }

    @Then("the employee list of project {int} contains the employee {string}")
    public void projectContainsEmployee(int id, String username) {
        assertTrue(model.getProject(id).hasEmployee(username));
    }

    @Then("the employee list of project {int} does not contain the employee {string}")
    public void projectDoesNotContainEmployee(int id, String username) {
        assertFalse(model.getProject(id).hasEmployee(username));
    }
}
