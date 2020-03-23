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

        // Run program
        while(true) {
            userLogin();
            mainMenu(); // This menu goes to other menus...
        }
    }

    private static void mainMenu() {
        int pick, maxPick;

        if (isCEO()) {
            maxPick = display.mainCEO(user);
        } else {
            maxPick = display.mainEmployee(user);
        }
        // maxPick = displayMenu.mainPM(user);

        pick = controller.pickItem(maxPick);

        // Go to next menu
        switch(pick) {
            case 1: activityMenu();                 break;
            case 5: userLogout();                   break;
            case 8: addProject();                   break;
            case 9: display.listProjects(projects); break;
            default:
                break;
        }
    }

    private static void activityMenu() {
        int maxPick, pick;

        if (isCEO()) {
            maxPick = display.activityEmployee();
        } else {
            maxPick = display.activityEmployee();
        }
        //maxPick = display.activityPM();

        pick = controller.pickItem(maxPick);

        switch(pick) {
            case 1:
                break;
        }


    }

    private static int calculateID(int year) {
        int count = 1;

        for (Project p : projects) {
            if (p.getStartYear() == year) {
                count += 1;
            }
        }
        year %= 100;

        return year*10000 + count;
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
            user = controller.getInitials(4);
            while (!isEmployee(user)) {
                System.out.println("Input is not an employee, please enter new:");
                user = controller.getInitials(4);
            }
            System.out.println("");
        }
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
