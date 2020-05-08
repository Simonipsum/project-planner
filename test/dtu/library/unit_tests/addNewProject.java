/**
 * Main responsible: Emil Skov Andersen
 */

package dtu.library.unit_tests;

import app.OperationNotAllowedException;
import app.model.Employee;
import app.model.ProjectApp;
import dtu.library.acceptance_tests.ErrorMessageHolder;
import org.junit.Test;
import static org.junit.Assert.*;

public class addNewProject {
    ProjectApp app;
    ErrorMessageHolder errorMessage;

    // Use this for all tests
    String ceo_name = "huba";
    String employee_name = "derp";
    String project_name = "someName";
    int project_id = 200001;

    @Test
    public void userNotCeo() {
        app = new ProjectApp();
        errorMessage = new ErrorMessageHolder();

        try {
            // Login as employee
            app.login(app.getEmployee(ceo_name));
            app.addNewEmployee(new Employee(employee_name));
            app.logout();
            app.login(app.getEmployee(employee_name));

            // Try to add project
            app.addNewProject(2020, project_name);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
        assertEquals("Insufficient Permissions: User is not CEO.", errorMessage.getErrorMessage());
    }

    @Test
    public void invalidYear() {
        app = new ProjectApp();
        errorMessage = new ErrorMessageHolder();
        app.login(app.getEmployee(ceo_name));

        try {
            app.addNewProject(-2020, project_name);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
        assertEquals("Error: Year is below 0.", errorMessage.getErrorMessage());
    }

    @Test
    public void ceoAddsUnnamedProject() {
        app = new ProjectApp();
        errorMessage = new ErrorMessageHolder();
        app.login(app.getEmployee(ceo_name));

        try {
            app.addNewProject(2020, "N");
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
        assertEquals("", errorMessage.getErrorMessage());
        assertTrue(app.hasProject(project_id));
        assertNull(app.getProject(project_id).getName());
    }

    @Test
    public void ceoAddsNamedProject() {
        app = new ProjectApp();
        errorMessage = new ErrorMessageHolder();
        app.login(app.getEmployee(ceo_name));

        try {
            app.addNewProject(2020, project_name);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
        assertEquals("", errorMessage.getErrorMessage());
        assertTrue(app.hasProject(project_id));
        assertEquals(project_name, app.getProject(project_id).getName());
    }

    @Test
    public void ceoAddsMultipleProjectsWithSameYear() {
        ceoAddsNamedProject();

        try {
            app.addNewProject(2020, project_name);
            project_id += 1;
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
        assertEquals("", errorMessage.getErrorMessage());
        assertTrue(app.hasProject(project_id));
        assertEquals(project_name, app.getProject(project_id).getName());
    }

    @Test
    public void ceoAddsMultipleProjectsWithDifferentYear() {
        ceoAddsNamedProject();

        try {
            app.addNewProject(2021, project_name);
            project_id = 210001;
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
        assertEquals("", errorMessage.getErrorMessage());
        assertTrue(app.hasProject(project_id));
    }
}
