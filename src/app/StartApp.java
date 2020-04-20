package app;

import app.model.ProjectApp;
import app.ui.ProjectAppUI;

public class StartApp {
    public static void main(String[] args) throws OperationNotAllowedException {
        ProjectApp model = new ProjectApp();
        ProjectAppUI ui = new ProjectAppUI(model);
        ui.mainLoop();
    }
}
