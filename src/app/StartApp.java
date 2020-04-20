package app;

public class StartApp {
    public static void main(String[] args) throws OperationNotAllowedException {
        Model m = new Model();
        // View v = new View(m);
        // Controller c = new Controller(m, v);
        m.mainLoop(); // should main loop be in Model?
    }
}
