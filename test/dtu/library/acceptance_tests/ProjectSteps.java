package dtu.library.acceptance_tests;

import app.Employee;
import app.OperationNotAllowedException;
import app.ProjectApp;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.*;

public class ProjectSteps {

    private ProjectApp projectApp;
    private ErrorMessageHolder errorMessage;

    public ProjectSteps(ProjectApp projectApp, ErrorMessageHolder errorMessage) {
        this.projectApp = projectApp;
        this.errorMessage = errorMessage;
    }

    @Given("the ProjectApp does not contain any projects")
    public void hasNoProject() {
        assertEquals(0, projectApp.getProjects().size());
    }

    @Given("the ProjectApp contains project {int}")
    public void hasAProject(int id) {
        if (!projectApp.hasProject(id)) {
            int year = id / 10000;
            int numb = id % 10000;

            Employee user = projectApp.getUser();
            projectApp.setUser(projectApp.getCEO());
            for (int i = 0; i < numb; i++) {
                userCreatesProject("Project1", year);
            }
            projectApp.setUser(user);
        }
    }

    @When("the user adds a project named {string} in the year {int}")
    public void userCreatesProject(String name, int year) {
        try {
            projectApp.addNewProject(year, name);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
    }

    @Then("a project with name {string} and project ID {int} exists in the ProjectApp.")
    public void newProjectCreated(String name, int id) {
        assertTrue( projectApp.getProject(id).getName().equals(name) );
    }

    @When("the user sets PM of project {int} to {string}")
    public void setPmOfProject(int id, String username) {
        try {
            projectApp.setPM(username, id);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
    }

    @Then("the PM of the project with ID {int} is {string}")
    public void usernameIsPmOfProject(int id, String username) {
        assertEquals(projectApp.getProject(id).getPm(), projectApp.getEmployee(username));
    }

    @Given("the user is PM of the project with ID {int}")
    public void setUserPmOfProject(int id) {
        Employee temp = projectApp.getUser();
        projectApp.setUser(projectApp.getCEO());
        setPmOfProject(id, temp.getUsername());
        projectApp.setUser(temp);
        assertEquals(projectApp.getProject(id).getPm(), temp);
    }

    @Given("the user is not PM of the project with ID {int}")
    public void setNotUserPmOfProject(int id) {
        assertNotEquals(projectApp.getProject(id).getPm().getUsername(), projectApp.getUser());
    }

    @When("the user adds {string} to the project with ID {int}")
    public void pmAddsEmployeeToProject(String username, int id) {
        try {
            projectApp.addEmployee(username, id);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
    }

    @Given("the user is on project {int}")
    public void userIsOnProject(int id) {
        Employee temp = projectApp.getUser();
        projectApp.setUser(projectApp.getProject(id).getPm());
        try {
            projectApp.addEmployee(temp.getUsername(), id);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
        projectApp.setUser(temp);
    }

    @When("the user removes the employee {string} from project {int}")
    public void userRemovesEmployeeFromProject(String name, int id) {
        try {
            projectApp.removeEmployee(name, id);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
    }

    @Then("the employee list of project {int} contains the employee {string}")
    public void projectContainsEmployee(int id, String username) {
        assertTrue(projectApp.getProject(id).hasEmployee(username));
    }

    @Then("the employee list of project {int} does not contain the employee {string}")
    public void projectDoesNotContainEmployee(int id, String username) {
        assertFalse(projectApp.getProject(id).hasEmployee(username));
    }
}
