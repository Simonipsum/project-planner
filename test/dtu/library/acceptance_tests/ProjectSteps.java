package dtu.library.acceptance_tests;

import app.OperationNotAllowedException;
import app.Project;
import app.ProjectApp;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

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
        List<Project> projects = projectApp.getProjects();
        assertTrue(projects.stream().anyMatch(p -> p.getId() == id && p.getName().equals(name)));
    }


    @When("the user sets the PM of the project with ID {int} to {string}")
    public void setPmOfProject(int id, String username) {
        Project project = projectApp.getProjects().stream().filter(p -> p.getId() == id).findFirst().get();
        // call some method that sets pm of project.
    }

    @Then("the PM of the project with ID {int} is {string}")
    public void usernameIsPmOfProject(int id, String username) {
        Project project = projectApp.getProjects().stream().filter(p -> p.getId() == id).findFirst().get();
        assertTrue(project.getPm().equals(username));
    }


}
