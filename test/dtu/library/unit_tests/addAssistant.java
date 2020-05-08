/**
 * Main responsible: Simon Amtoft Pedersen
 */

package dtu.library.unit_tests;

import app.OperationNotAllowedException;
import app.model.Employee;
import app.model.ProjectApp;
import dtu.library.acceptance_tests.ErrorMessageHolder;
import org.junit.Test;

import static org.junit.Assert.*;

public class addAssistant {
    ProjectApp app;
    ErrorMessageHolder errorMessage;

    // Use this for all tests
    String ceo_name = "huba";
    String employee_name = "derp";
    String activity_name1 = "ac1";
    String activity_name2 = "ac2";
    String project_name = "Project";
    int project_id = 200001;

    @Test
    public void employeeAlreadyAssigned() {
        app = new ProjectApp();
        errorMessage = new ErrorMessageHolder();
        app.login(app.getEmployee(ceo_name));

        try {
            app.addNewProject(2020, project_name);
            app.addNewEmployee(new Employee(employee_name));
            app.setPM(ceo_name, project_id);
            app.addEmployee(employee_name, project_id);
            app.addNewActivity(activity_name1, project_id);
            app.addAssistance(app.getEmployee(employee_name), activity_name1, project_id);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
        assertEquals("Error: Project already has Employee.", errorMessage.getErrorMessage());
        assertFalse(app.getProject(project_id).hasAssistant(app.getEmployee(employee_name), activity_name1));
    }

    @Test
    public void employeeAlreadyAssistant() {
        app = new ProjectApp();
        errorMessage = new ErrorMessageHolder();
        app.login(app.getEmployee(ceo_name));

        try {
            app.addNewProject(2020, project_name);
            app.addNewEmployee(new Employee(employee_name));
            app.setPM(ceo_name, project_id);
            app.addNewActivity(activity_name1, project_id);

            // Add assistance twice
            app.addAssistance(app.getEmployee(employee_name), activity_name1, project_id);
            app.addAssistance(app.getEmployee(employee_name), activity_name1, project_id);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }

        // Post conditions
        assertTrue(app.getProject(project_id).hasAssistant(app.getEmployee(employee_name), activity_name1));
        assertEquals("", errorMessage.getErrorMessage());
    }

    @Test
    public void employeeIsAssistantOnOtherActivity() {
        employeeAddedAsAssistant();

        try {
            // Add on another activity
            app.addNewActivity(activity_name2, project_id);
            app.addAssistance(app.getEmployee(employee_name), activity_name2, project_id);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }

        // Post conditions
        assertTrue(app.getProject(project_id).hasAssistant(app.getEmployee(employee_name), activity_name2));
        assertEquals("", errorMessage.getErrorMessage());
    }

    @Test
    public void employeeAddedAsAssistant() {
        app = new ProjectApp();
        errorMessage = new ErrorMessageHolder();
        app.login(app.getEmployee(ceo_name));

        try {
            app.addNewProject(2020, project_name);
            app.addNewEmployee(new Employee(employee_name));
            app.setPM(ceo_name, project_id);
            app.addNewActivity(activity_name1, project_id);
            app.addAssistance(app.getEmployee(employee_name), activity_name1, project_id);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }

        // Post conditions
        assertTrue(app.getProject(project_id).hasAssistant(app.getEmployee(employee_name), activity_name1));
        assertEquals("", errorMessage.getErrorMessage());
    }
}
