package dtu.library.acceptance_tests;

import app.model.Employee;
import app.OperationNotAllowedException;

import app.model.ProjectApp;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.*;

public class ProjectSteps {

    private ProjectApp app;
    private ErrorMessageHolder errorMessage;

    public ProjectSteps(ProjectApp app, ErrorMessageHolder errorMessage) {
        this.app = app;
        this.errorMessage = errorMessage;
    }

    public void setUser(Employee emp) {
        app.logout();
        try {
            app.login(emp);
        } catch (OperationNotAllowedException e) {
            System.out.println(e.getMessage());
        }
    }


    @Given("the ProjectApp does not contain any projects")
    public void hasNoProject() {
        assertEquals(0, app.getProjects().size());
    }

    @Given("the ProjectApp contains project {int}")
    public void hasAProject(int id) {
        if (!app.hasProject(id)) {
            int year = id / 10000;
            int numb = id % 10000;

            Employee user = app.getUser();
            setUser(app.getCEO());
            for (int i = 0; i < numb; i++) {
                userCreatesProject("Project1", year);
            }
            setUser(user);
        }
    }

    @Given("project with ID {int} is named {string}")
    public void projectHasName(int id, String name){
        app.getProject(id).setName(name);
    }

    @When("the user adds a project named {string} in the year {int}")
    public void userCreatesProject(String name, int year) {
        try {
            app.addNewProject(year, name);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
    }

    @When("the user changes the name of the project with ID {int} to {string}")
    public void userChangeProjectName(int id, String newname) {
        try {
            app.setProjectName(id, newname);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
    }

    @Then("a project with name {string} and project ID {int} exists in the ProjectApp.")
    public void newProjectCreated(String name, int id) {
        assertTrue(app.getProject(id).getName().equals(name));
    }

    @When("the user sets PM of project {int} to {string}")
    public void setPmOfProject(int id, String username) {
        try {
            app.setPM(username, id);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
    }

    @Then("the PM of the project with ID {int} is {string}")
    public void usernameIsPmOfProject(int id, String username) {
        assertEquals(app.getProject(id).getPm(), app.getEmployee(username));
    }

    @Given("the user is PM of the project with ID {int}")
    public void setUserPmOfProject(int id) {
        Employee temp = app.getUser();
        setUser(app.getCEO());
        setPmOfProject(id, temp.getUsername());
        setUser(temp);
        assertEquals(app.getProject(id).getPm(), temp);
    }

    @Given("the user is not PM of the project with ID {int}")
    public void setNotUserPmOfProject(int id) {
        assertNotEquals(app.getProject(id).getPm().getUsername(), app.getUser());
    }

    @When("the user adds {string} to the project with ID {int}")
    public void pmAddsEmployeeToProject(String username, int id) {
        try {
            app.addEmployee(username, id);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
    }

    @Given("the user is on project {int}")
    public void userIsOnProject(int id) {
        Employee temp = app.getUser();
        setUser(app.getProject(id).getPm());
        try {
            app.addEmployee(temp.getUsername(), id);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
        setUser(temp);
    }

    @When("the user removes the employee {string} from project {int}")
    public void userRemovesEmployeeFromProject(String name, int id) {
        try {
            app.removeEmployee(name, id);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
    }

    @Then("the employee list of project {int} contains the employee {string}")
    public void projectContainsEmployee(int id, String username) {
        assertTrue(app.getProject(id).hasEmployee(username));
    }

    @Then("the employee list of project {int} does not contain the employee {string}")
    public void projectDoesNotContainEmployee(int id, String username) {
        assertFalse(app.getProject(id).hasEmployee(username));
    }
}
