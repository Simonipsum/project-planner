import java.util.*;

public class display {
    private static String[] mainMenuOptions = {
        // Normal employee options
        "Add/edit work time of activity",
        "Register absence",
        "List activities worked on",
        "Get assistance on activity",
        "Logout",
        // PM options
        "Edit projects",
        "Check availability",
        // CEO options
        "Set PM of a project",
        "Register new employee",
        "Add new project",
        "List all projects",
        "List all employees",
    };
    private static String[] projectMenuOptions = {
        // PM options
        "Add employee",
        "Add activity",
        "Edit start/end date of an activity",
        "Change estimated work time of an activity",
        "See timetable of project",
    };

    private static void listMenu(String[] menu) {
        String list = "";

        for (int i = 0; i < menu.length; i++) {
            list += "\n" + (i+1) + ". " + menu[i];
        }

        System.out.println(list);
    }

    public static int projectMenu() {
        menuHeader("Project Menu");
        listMenu(projectMenuOptions);
        System.out.print("----------------------------------\n");
        return projectMenuOptions.length;
    }

    public static int mainMenu() {
        menuHeader("Main Menu");
        listMenu(mainMenuOptions);
        System.out.print("----------------------------------\n");
        return mainMenuOptions.length;
    }

    private static void menuHeader(String name) {
        String header = "----------------------------------\n";

        for (int i = 0; i < (16 - name.length()/2); i++) {
            header += " ";
        }
        header += name + "\n";
        header += "----------------------------------";
        System.out.println(header);
    }

    public static void listActivities(Employee user) {
        // also list the end date of activities that hasn't ended yet
        // and start date of activities not started yet.


        // List<Activity> activities = user.getActivities();
        // System.out.println("List of activities user has worked on");
        // System.out.println("-------------------------------------");
        // System.out.println("Name \t\t Work Time");
        // for (Activity a : activities) {
        //      System.out.printf("%s \t\t %d\n", a.getName(), a.getWorkTime(user));
        // }
        // System.out.println("-------------------------------------\n");
    }

    public static void listProjects(List<Project> projects) {
        if (projects.size() == 0) {
            System.out.println("No projects added to ProjectApp yet.");
        } else {
            System.out.println("List of projects in app");
            System.out.println("-----------------------");
            System.out.println("ID \t\t Name");
            for (Project p : projects) {
                System.out.printf("%d \t\t %s\n", p.getId(), p.getName());
            }
            System.out.println("-----------------------\n");
        }
    }

    public static void listEmployees(List<Employee> employees) {
        if (employees.size() == 0) {
            System.out.println("No employees in ProjectApp yet.");
        } else {
            System.out.println("List of employees in ProjectApp");
            System.out.println("-----------------------");
            for (Employee e : employees) {
                System.out.printf("\t%s\n", e.getInitials());
            }
            System.out.println("-----------------------\n");
        }
    }
}
