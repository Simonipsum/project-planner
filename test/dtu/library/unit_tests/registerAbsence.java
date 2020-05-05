package dtu.library.unit_tests;

import app.OperationNotAllowedException;
import app.model.ProjectApp;
import dtu.library.acceptance_tests.ErrorMessageHolder;
import org.junit.Test;
import static org.junit.Assert.*;

public class registerAbsence {
    ProjectApp app;
    ErrorMessageHolder errorMessage;

    // Use this for all tests
    String ceo_name = "huba";

    @Test
    public void invalidDate() {
        app = new ProjectApp();
        errorMessage = new ErrorMessageHolder();

        app.login(app.getEmployee(ceo_name));

        int start = 200000;
        int end = 200101;

        try {
            app.registerAbsence(start, end);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
        assertEquals("Error: Date not valid!", errorMessage.getErrorMessage());
    }

    @Test
    public void endBeforeStart() {
        app = new ProjectApp();
        errorMessage = new ErrorMessageHolder();

        app.login(app.getEmployee(ceo_name));

        int start = 200102;
        int end = 200101;

        try {
            app.registerAbsence(start, end);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
        assertEquals("Start date must be before end date.", errorMessage.getErrorMessage());
    }

    @Test
    public void registerAbsenceProperly() {
        app = new ProjectApp();
        errorMessage = new ErrorMessageHolder();

        app.login(app.getEmployee(ceo_name));

        int start = 200101;
        int end = 200102;

        try {
            app.registerAbsence(start, end);
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
        assertEquals("", errorMessage.getErrorMessage());
    }
}
