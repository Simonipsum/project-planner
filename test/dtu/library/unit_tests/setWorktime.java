package dtu.library.unit_tests;

import app.OperationNotAllowedException;
import app.model.Employee;
import app.model.ProjectApp;
import dtu.library.acceptance_tests.ErrorMessageHolder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class setWorktime {
    ProjectApp app;
    ErrorMessageHolder errorMessage;

    // Use this for all tests
    String ceo_name = "huba";
    String employee_name = "derp";
    String activity_name = "ac1";
    String project_name = "Project";
    int project_id = 200001;


    @Test
    public void invalidDate() {
        errorMessage = new ErrorMessageHolder();
        app = new ProjectApp();
        app.login(app.getEmployee(ceo_name));

        int date = 0;
        float time = 0;

        try {
            app.addNewProject(2020, project_name);
            app.setPM(ceo_name, project_id);
            app.addNewActivity(activity_name, project_id);
            app.setWorktime(date, time, project_id, activity_name);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
        assertEquals("Error: Date not valid!", errorMessage.getErrorMessage());
    }

    @Test
    public void employeeNotAssigned() {
        errorMessage = new ErrorMessageHolder();
        app = new ProjectApp();
        app.login(app.getEmployee(ceo_name));

        int date = 20200101;
        float time = 8;

        try {
            app.addNewEmployee(new Employee(employee_name));
            app.addNewProject(2020, project_name);
            app.setPM(ceo_name, project_id);
            app.addNewActivity(activity_name, project_id);

            // Login with other user, which is not assigned, and try to add worktime
            app.logout();
            app.login(app.getEmployee(employee_name));
            app.setWorktime(date, time, project_id, activity_name);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
        assertEquals("Insufficient Permissions: " +
                "User is not assigned to that project or is an assistant on that activity",
                errorMessage.getErrorMessage());
    }

    @Test
    public void employeeIsAssignedToProject() {
        errorMessage = new ErrorMessageHolder();
        app = new ProjectApp();
        app.login(app.getEmployee(ceo_name));

        int date = 20200101;
        float time = 8;

        try {
            app.addNewProject(2020, project_name);
            app.setPM(ceo_name, project_id);
            app.addNewActivity(activity_name, project_id);
            app.setWorktime(date, time, project_id, activity_name);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
        assertEquals("", errorMessage.getErrorMessage());
        assertEquals(time, app.getProject(project_id).getActivity(activity_name)
                .getWorkTime().get(app.getEmployee(ceo_name)).get(date), 0.0);
    }

    @Test
    public void employeeIsAssistantOnProject() {
        errorMessage = new ErrorMessageHolder();
        app = new ProjectApp();
        app.login(app.getEmployee(ceo_name));

        int date = 20200101;
        float time = 8;

        try {
            app.addNewEmployee(new Employee(employee_name));
            app.addNewProject(2020, project_name);
            app.setPM(ceo_name, project_id);
            app.addNewActivity(activity_name, project_id);

            // Add other employee as assistant and try to do it with him
            app.addAssistance(app.getEmployee(employee_name), activity_name, project_id);
            app.logout();
            app.login(app.getEmployee(employee_name));
            app.setWorktime(date, time, project_id, activity_name);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
        assertEquals("", errorMessage.getErrorMessage());
        assertEquals(time, app.getProject(project_id).getActivity(activity_name)
                .getWorkTime().get(app.getEmployee(employee_name)).get(date), 0.0);
    }
}
