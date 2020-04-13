package app;

import io.cucumber.java.en_old.Ac;

import java.util.*;

public class display {
    private static String[] mainMenuOptions = {
        "Logout",
        // Normal employee options
        "Add/edit work time of activity",
        "Register absence",
        "List activities worked on",
        "Get assistance on activity",
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
        "Exit menu",
        "Add employee",
        "Add activity",
        "Edit activity dates",
        "Edit activity worktime",
        "See timetable of project",
    };

    private static void listMenu(String[] menu) {
        String list = "";

        for (int i = 0; i < menu.length; i++) {
            list += "\n" + (i) + ". " + menu[i];
        }

        System.out.println(list);
    }

    public static int projectMenu(int id) {
        menuHeader("Project Menu for " + id);
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

    public static void listActivities(Project p) {
        System.out.println("Activities for project " + p.getId());
        System.out.println("-------------------------------------");
        System.out.println("Name \t Start \t\t End \t EWT");
        for (Activity a : p.getActivities()) {
            System.out.printf("%s \t %06d \t %06d \t %.1f\n", a.getName(), a.getStart(), a.getEnd(), a.getExpectedWorkTime());
        }
        System.out.println("-------------------------------------\n");
    }

    public static void listActivities(Employee user, List<Project> projects) {
        System.out.println("List of activities user has worked on");
        System.out.println("-------------------------------------");
        System.out.println("Name \t\t Work Time");
        for (Project p : projects) {
            if (p.hasEmployee(user.getUsername())) {
                for (Activity a : p.getActivities()) {
                    System.out.printf("%s \t\t %d\n", a.getName(), 1);
                    a.getWorkTime(user);
                }
            }
        }
        System.out.println("-------------------------------------\n");

        // also list the end date of activities that hasn't ended yet
        // and start date of activities not started yet.
    }

    public static void listProjects(List<Project> projects) {
        if (projects.size() == 0) {
            System.out.println("No projects added to ProjectApp yet.");
        } else {
            System.out.println("List of projects in app");
            System.out.println("----------------------------------");
            System.out.println("ID \t\t\t Name \t\t PM");
            for (Project p : projects) {
                System.out.printf(
                        "%d \t\t %s\t %s\n",
                        p.getId(),
                        p.getName() == null ? "UNNAMED" : p.getName(),
                        p.getPm().getUsername()
                );
            }
            System.out.println("----------------------------------\n");
        }
    }

    public static void listEmployees(List<Employee> employees, Employee ceo) {
        if (employees.size() == 0) {
            System.out.println("No employees in ProjectApp yet.");
        } else {
            System.out.println("List of employees in ProjectApp");
            System.out.println("----------------------------------");
            for (Employee e : employees) {
                if (e.equals(ceo)) {
                    System.out.printf("\t%s\t(CEO)\n", e.getUsername());
                } else {
                    System.out.printf("\t%s\n", e.getUsername());
                }
            }
            System.out.println("----------------------------------\n");
        }
    }
}
