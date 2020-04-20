package app;

import io.cucumber.java.en_old.Ac;

import java.util.*;

public class View {
    private static String sep = "-------------------------------------";

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
        "Summary report",

        // All
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

    private void listMenu(String[] menu) {
        String list = 0 + ". " + menu[0];
        for (int i = 1; i < menu.length; i++) {
            list += "\n" + (i) + ". " + menu[i];
        }
        System.out.println(list);
    }

    public int projectMenu(int id) {
        menuHeader("Project Menu for " + id);
        listMenu(projectMenuOptions);
        System.out.println(sep);
        return projectMenuOptions.length;
    }

    public int mainMenu() {
        menuHeader("Main Menu");
        listMenu(mainMenuOptions);
        System.out.println(sep);
        return mainMenuOptions.length;
    }

    private void menuHeader(String name) {
        String header = sep + "\n";
        for (int i = 0; i < (16 - name.length()/2); i++) {
            header += " ";
        }
        header += name + "\n" + sep;
        System.out.println(header);
    }

    public void listActivities(Project p) {
        System.out.println("Activities for project " + p.getId());
        System.out.println(sep);
        System.out.println("Name \t Start \t\t End \t\t EWT");
        for (Activity a : p.getActivities()) {
            System.out.printf(
                    "%s \t %06d \t %06d \t %.1f\n",
                    a.getName(),
                    a.getStart() == -1 ? 0 : a.getStart(),
                    a.getEnd() == -1 ? 0 : a.getEnd(),
                    a.getExpectedWorkTime());
        }
        System.out.println(sep + "\n");
    }

    public void listActivities(Employee user, List<Project> projects) {
        System.out.println("List of activities user has worked on");
        System.out.println(sep);
        System.out.println("Name \t\t Work Time");
        for (Project p : projects) {
            if (p.hasEmployee(user.getUsername())) {
                for (Activity a : p.getActivities()) {
                    System.out.printf("%s \t\t %d\n", a.getName(), 1);
                    //a.getWorkedTime(user);
                }
            }
        }
        System.out.println(sep + "\n");

        // also list the end date of activities that hasn't ended yet
        // and start date of activities not started yet.
    }

    public void listProjects(List<Project> projects) {
        if (projects.size() == 0) {
            System.out.println("No projects added to ProjectApp yet.");
        } else {
            System.out.println("List of projects in app");
            System.out.println(sep);
            System.out.println("ID \t\t\t Name \t\t PM");
            for (Project p : projects) {
                System.out.printf(
                        "%d \t\t %s\t %s\n",
                        p.getId(),
                        p.getName() == null ? "UNNAMED" : p.getName(),
                        p.getPm().getUsername()
                );
            }
            System.out.println(sep + "\n");
        }
    }

    public void listEmployees(List<Employee> employees, Employee ceo) {
        if (employees.size() == 0) {
            System.out.println("No employees in ProjectApp yet.");
        } else {
            System.out.println("List of employees in ProjectApp");
            System.out.println(sep);
            for (Employee e : employees) {
                if (e.equals(ceo)) {
                    System.out.printf("\t%s\t(CEO)\n", e.getUsername());
                } else {
                    System.out.printf("\t%s\n", e.getUsername());
                }
            }
            System.out.println(sep + "\n");
        }
    }

    public void timeTable(Project p, Employee user) {
        if(!user.equals(p.getPm())){
            System.out.println("Insufficient Permissions. User is not PM.");
            return;
        }
        String out = "Time Table for " + p.getId() + "\n";
        out += sep + "\n" + "Activity \tdate \t\twt\n";
        Map<Integer, Float> dt;
        for (Activity a : p.getActivities()) {
            dt = a.getDateTime();
            out += a.getName();
            for (Integer d : dt.keySet()) {
                out += "\t\t\t" + d + "\t\t" + dt.get(d) + "\n";
            }
            out += "\n";
        }
        float rwt = p.getRemainingWT();
        if (rwt < 0) {
            out += "Project is overworked with " + (-1*rwt) + "\nhours compared to expected worktime.";
        } else {
            out += "Project is expected to require " + rwt + "\nmore hours of work to be completed.";
        }
        System.out.println(out);
    }

    public void summary(List<Project> ps, Employee user, Employee ceo) {
        if (!user.equals(ceo)){
            System.out.println("Insufficient Permissions. User is not CEO.");
            return;
        }
        String out = "ID \t\t worktime \t remaining wt\n";
        for (Project p : ps) {
            //if (p.getRemainingWT() > 0) {
            out += p.getId() + "\t" + p.getWorkedTime() + "\t\t" + p.getRemainingWT() + "\n";
            //}
        }
        System.out.println(out);
    }

    public void print(String out) {
        System.out.print(out);
    }

    public void println(String out) {
        System.out.println(out);
    }

    public void newLine() {
        System.out.println("");
    }

    public void printDates(String out, int[] dates) {
        System.out.printf(out, dates[0], dates[1]);
    }

    public void printf(String out, String arg1, float arg2) {
        System.out.printf(out, arg1, arg2);
    }

    public void printf(String out, String arg1, int arg2, float arg3) {
        System.out.printf(out, arg1, arg2, arg3);
    }
}
