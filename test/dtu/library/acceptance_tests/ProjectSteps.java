package dtu.library.acceptance_tests;

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

    @Given("the ProjectApp contains a project with ID 200001")
    public void hasAProject() {
        String user = projectApp.getUser();
        projectApp.setUser(projectApp.getCEO());
        userCreatesProject("Project1", 2020);
        projectApp.setUser(user);
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


    @When("the user sets the PM of the project with ID {int} to {string}")
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
        String temp = projectApp.getUser();
        projectApp.setUser(projectApp.getCEO());
        setPmOfProject(id, temp);
        projectApp.setUser(temp);
        assertEquals(projectApp.getProject(id).getPm().getUsername(), temp);
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

    @Then("the Employee list of project {int} contains the Employee {string}")
    public void projectContainsEmployee(int id, String username) {
        assertTrue(projectApp.getProject(id).hasEmployee(projectApp.getEmployee(username)));
    }

}
