import java.util.*;

public class ProjectApp {
    static Scanner consoleIn = new Scanner(System.in);

    static List<Employee> employees = new ArrayList<Employee>();
    static String CEO = "marc";
    static String user = "NONE";

    static String[] mainMenuOptions = {
        // Normal employee options
       "1. Add/edit work time of activity\n" +
       "2. Register absence\n" +
       "3. List activities worked on\n" +
       "4. Get assistance on activity\n" +
       "5. Logout\n",

        // PM options
        "6. Edit project\n",
        // CEO options
        "6. Edit project\n" +
        "7. Register new employee\n" +
        "8. Add new project\n",
    };
    static String[] projectMenuOptions = {
        // PM options
        "1. Add employee\n" +
        "2. Edit activities\n" +
        "3. Check availability\n" +
        "4. See timetable of project\n",
        // CEO options
        "1. Set PM\n",
    };
    static String[] activityMenuOptions = {
        // PM
        "1. Add new activity\n" +
        "2. Edit start/end date\n" +
        "3. Change estimated work time\n",
    };


    public static void main(String[] args) {
        int option;
        employees.add(new Employee(CEO)); // Set CEO as an employee

        while(true) {
            if (user.equals("NONE")) {
                userLogin();
            } else {
                System.out.printf("Logged in as '%s'\n", user);
            }
            mainMenu();
        }
    }

    private static void mainMenu() {
        int pick, maxPick = 5;
        String outStr = "";
        outStr += "Available options for '" + user + "' are:\n";
        outStr += mainMenuOptions[0];

        // if is pm outStr += mainMenu[1], maxPick += 1
        if (isCEO()) {
            outStr += mainMenuOptions[2];
            maxPick += 3;
        }
        System.out.print(outStr);

        pick = getInputInt();
        while (pick < 1 || pick > maxPick) {
            System.out.println("Please pick one of the listed options.");
            pick = getInputInt();
        }

        // Go to next menu
        switch(pick) {
            case 1:
                break;
            case 5:
                userLogout();
                break;
            default:
                break;
        }
    }

    private static void userLogout() {
        user = "NONE";
        System.out.println("User has been logged out.");
    }

    private static int getInputInt() {
        System.out.println("Input integer: ");
        while (!consoleIn.hasNextInt()) {
            consoleIn.next();
            System.out.print("Input must be an integer. Enter new integer:");
        }
        return consoleIn.nextInt();
    }

    private static void userLogin() {
        String input = "";
        boolean getInput = true;

        while (getInput) {
            System.out.print("Please input initials to login: ");
            input = consoleIn.next().toLowerCase();

            if (input.length() > 4) {
                System.out.println("Please enter a maximum of four initials.");
            } else {
                if (isEmployee(input)) {
                    getInput = false;
                } else {
                    System.out.printf("'%s' is not a registered employee.\n", input);
                }
            }
        }
        user = input;
    }

    // Check if there is an employee with "initials"
    private static boolean isEmployee(String initials) {
        for (Employee e : employees) {
            if (e.getInitials().equals(initials)) {return true; }
        }
        return false;
    }

    // Check if user is CEO
    private static boolean isCEO() {
        return user.equals(CEO); // also add stuff after employee class is implemented
    }
}
