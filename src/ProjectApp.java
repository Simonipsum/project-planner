import java.lang.reflect.Array;
import java.util.*;

public class ProjectApp {
    static List<Employee> employees = new ArrayList<Employee>();
    static List<Project> projects = new ArrayList<Project>();
    static String ceo = "marc";
    static String user = "NONE";

    public static void main(String[] args) {
        // Set CEO as an employee
        employees.add(new Employee(ceo));
        //employees.add(new Employee("Simo"));

        // Run program
        while(true) {
            userLogin();
            mainMenu(); // This menu goes to other menus.
        }
    }

    private static void mainMenu() {
        int maxPick = display.mainMenu();
        int pick = controller.pickItem(maxPick);

        // Go to next menu / display
        switch(pick) {
            case 1:  workTime();                                break;
            case 2:  registerAbsence();                         break;
            case 3:  display.listActivities(getEmployee(user)); break;
            case 4:  getAssistance();                           break;
            case 5:  userLogout();                              break;

            // PM
            case 6:  projectMenu();                             break;
            case 7:  checkAvailability();                       break;

            // CEO
            case 8:  setPM();                                   break;
            case 9:  addEmployee();                             break;
            case 10: addProject();                              break;
            case 11: display.listProjects(projects);            break;
            case 12: display.listEmployees(employees);          break;
        }
    }

    private static void checkAvailability() {
        // check what projects user are PM of.
        // if none, return

        // pick date / dates
        // check availability of employees
    }

    private static void getAssistance() {
        // get assistance on an activity.
    }

    private static void registerAbsence() {
        // Register absence of user at given start to end date interval
    }

    private static void workTime() {
        // list projects.
        // pick one
        // list all activities of that project
        // pick one
        // add/edit work time of that one
    }

    private static void setPM() {
        if (!isCEO()) {
            System.out.println("User is not CEO!");
            return;
        }

        display.listProjects(projects);

        // list all projects
        // pick one
        // set pm of that one
    }

    private static void projectMenu() {
        int pick, maxPick;

        maxPick = display.projectMenu();
        pick = controller.pickItem(maxPick);
    }

    private static void addEmployee() {
        System.out.print("Initials of new Employee: ");
        String initials = controller.getInitials(4);
        employees.add(new Employee(initials));
    }

    private static int calculateID(int year) {
        int count = 1;

        for (Project p : projects) {
            if (p.getStartYear() == year) {
                count += 1;
            }
        }
        return (year%100)*10000 + count;
    }

    private static void addProject() {
        int id, year;
        String name;

        System.out.print("Enter year of project start: ");
        year = controller.getInputInt();

        System.out.print("Enter name of project (type no if not named yet): ");
        name = controller.getString();

        id = calculateID(year);

        if (name.equals("no")) {
            projects.add(new Project(id, year));
        } else {
            projects.add(new Project(id, year, name));
        }
    }

    private static void userLogout() {
        user = "NONE";
        System.out.println("User has been logged out.\n");
    }

    private static void userLogin() {
        if (user.equals("NONE")) {
            System.out.print("Please input initials to login: ");
            user = controller.getInitials(4);
            while (!isEmployee(user)) {
                System.out.print("Input is not an employee, please enter new: ");
                user = controller.getInitials(4);
            }
            System.out.println("");
        }
    }

    private static Employee getEmployee(String initials) {
        for (Employee e : employees) {
            if (e.getInitials().equals(initials)) { return e; }
        }
        return new Employee("NONE");
    }

    // Check if there is an employee with "initials"
    private static boolean isEmployee(String initials) {
        for (Employee e : employees) {
            if (e.getInitials().equals(initials)) { return true; }
        }
        return false;
    }

    // Check if user is CEO
    private static boolean isCEO() { return user.equals(ceo); }
}
