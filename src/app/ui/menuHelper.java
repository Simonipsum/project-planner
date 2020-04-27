package app.ui;

import app.model.Activity;
import app.model.Employee;
import app.model.Project;
import app.model.ProjectApp;

import java.util.List;
import java.util.Map;

public class menuHelper {
    private ProjectApp model;

    public menuHelper(ProjectApp model) {
        this.model = model;
    }

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

    public void projectList() {
        if (model.getProjects().size() == 0) {
            System.out.println("No projects added to ProjectApp yet.");
        } else {
            System.out.println("List of projects in app");
            System.out.println(sep);
            System.out.println("ID \t\t\t Name \t\t PM");
            for (Project p : model.getProjects()) {
                System.out.printf(
                        "%d \t\t %s\t %s\n",
                        p.getId(),
                        p.getName() == null ? ""    : p.getName(),
                        p.getPm().getUsername().equals("NONE") ? ""      : p.getPm().getUsername()
                );
            }
            System.out.println(sep + "\n");
        }
    }

    public void employeeList() {
        List<Employee> es = model.getEmployees();
        if (es.size() == 0) {
            System.out.println("No employees in ProjectApp yet.");
        } else {
            System.out.println("List of employees in ProjectApp");
            System.out.println(sep);
            for (Employee e : es) {
                if (e.equals(model.getCEO())) {
                    System.out.printf("\t%s\t(CEO)\n", e.getUsername());
                } else {
                    System.out.printf("\t%s\n", e.getUsername());
                }
            }
            System.out.println(sep + "\n");
        }
    }

    public void activityList(Project p) {
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

    public void listActivities(Employee currentUser, List<Project> projects) {
        for (Project p : projects) {
            if (p.hasEmployee(currentUser.getUsername())) {
                for (Activity a : p.getActivities()) {
                    System.out.printf(
                            "%s \t %06d \t %06d \t %.1f\n",
                            a.getName(),
                            a.getStart() == -1 ? 0 : a.getStart(),
                            a.getEnd() == -1 ? 0 : a.getEnd(),
                            a.getExpectedWorkTime());
                }
            }
        }
    }
    public void timeTable(int id) {
        Project p = model.getProject(id);
        if(!p.isPm(model.getUser().getUsername())) {
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


    public void summary() {
        if (!model.getUser().equals(model.getCEO())){
            System.out.println("Insufficient Permissions. User is not CEO.");
            return;
        }
        String out = "ID \t\t worktime \t remaining wt\n";
        for (Project p : model.getProjects()) {
            //if (p.getRemainingWT() > 0) {
            out += p.getId() + "\t" + p.getWorkedTime() + "\t\t" + p.getRemainingWT() + "\n";
            //}
        }
        System.out.println(out);
    }
}
