package app;

public class StartApp {
    public static void main(String[] args) throws OperationNotAllowedException {
        ProjectApp m = new ProjectApp();
        // View v = new View(m);
        // Controller c = new Controller(m, v);
        m.mainLoop(); // should main loop be in Model?
    }
}
