package app;

import app.model.ProjectApp;

public class StartApp {
    public static void main(String[] args) throws OperationNotAllowedException {
        ProjectApp app = new ProjectApp();
        View v = new View(app);
        Controller c = new Controller(v, app);
        app.mainLoop(v, c); // should main loop be in Model?
    }
}
