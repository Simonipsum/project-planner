/**
 * Main responsible: Emil Skov Andersen
 */

package app;
import app.ui.ProjectAppUI;

public class StartApp {
    public static void main(String[] args) throws OperationNotAllowedException {
        ProjectAppUI ui = ProjectAppUI.getInstance();
        ui.mainLoop();
    }
}
