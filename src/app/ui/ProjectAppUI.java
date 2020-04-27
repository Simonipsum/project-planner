package app.ui;

import app.OperationNotAllowedException;
import app.model.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProjectAppUI implements PropertyChangeListener {
    private ProjectApp app;
    private menuHelper display;
    private inputHelper in;

    public ProjectAppUI(ProjectApp app) {
        this.app = app;
        display = new menuHelper(app);
        in = new inputHelper(app);
        app.addObserver(this);
    }

    public ProjectAppUI() {
        app = new ProjectApp();
        display = new menuHelper(app);
        in = new inputHelper(app);
        app.addObserver(this);
    }

    public void mainLoop() throws OperationNotAllowedException {
        int pick;

        app.derpHelper();

        while(true) {
            if (app.getUser() == null) {
                userLogin();
            }
            display.mainMenu();
            pick = in.pickItem(12);
            processChoiceMain(pick);
        }
    }

    private void processChoiceMain(int pick) throws OperationNotAllowedException {
        switch(pick) {
            case 0: app.logout();             break;

            // Employee
            case 1:  registerWorktime();                break;
            case 2:  registerAbsence();          break;
            case 3:  display.listActivities(app.getUser(), app.getProjects());    break;
//            case 4:  getAssistance();                           break;

            // PM
            case 5:  projectMenu();             break;
            case 6:  checkAvailability();                       break;

            // CEO
            case 7:  setPM();                   break;
            case 8:  addEmployee();             break;
            case 9:  addProject();              break;
            case 10: display.summary();         break;

            // undecided
            case 11: display.projectList();    break;
            case 12: display.employeeList();   break;
        }
    }


    public void checkAvailability() {
        int[] dates = in.getDates();
        display.availability(dates);
    }

    private void registerAbsence() {
        int[] dates = in.getDates();
        app.registerAbsence(dates[0], dates[1]);
        System.out.printf("Successfully set absence for %s in period: %06d to %06d\n\n",
                app.getUser().getUsername(), dates[0], dates[1]);
    }

    private void addProject() throws OperationNotAllowedException {
        if (!app.currentUserIsCEO()) {
            System.out.println("Insufficient Permissions. User is not CEO.");
            return;
        }
        System.out.print("Enter year of project start: ");
        int year = in.getInt(1900, 2999);
        System.out.print("Enter name of project (type 'N' to skip naming): ");
        String name = in.getString();
        app.addNewProject(year, name);
        System.out.printf("Project %d was successfully added to the ProjectApp.\n\n", app.calculateID(year));
    }

    // Add employee to Project App
    private void addEmployee() throws OperationNotAllowedException {
        System.out.print("Initials of new Employee: ");
        String username = in.getInitials(4);
        app.addNewEmployee(username);
        System.out.print("Employee " + username + " was successfully added to the ProjectApp.\n\n");
    }

    private void setPM() throws OperationNotAllowedException {
        if (!app.currentUserIsCEO()) {
            System.out.println("Insufficient Permissions. User is not CEO.");
            return;
        }
        if (app.getProjects().size() == 0) {
            System.out.println("No projects added to ProjectApp yet.");
            return;
        }
        int id = pickProject();
        String username = pickEmployee();
        app.setPM(username, id);
        System.out.print("PM of " + id + " was successfully set to " + username + "\n\n");
    }

    public void projectMenu() throws OperationNotAllowedException {
        if (app.getProjects().size() == 0) {
            System.out.println("No projects added to ProjectApp yet.");
            return;
        }
        int id = pickProject();
        if (!app.isUserPm(id)) {
            System.out.println("Insufficient Permissions. User is not PM.");
            return;
        }

        int maxPick = display.projectMenu(id);
        processChoiceProject(in.pickItem(maxPick), id);
    }

    private void processChoiceProject(int pick, int id) throws OperationNotAllowedException {
        switch (pick) {
            case 0: return;
                case 1: addProjectEmployee(id); break;
                case 2: addProjectActivity(id); break;
                case 3: editActivityDates(id);  break;
                case 4: editActivityWt(id);     break;
                case 5: display.timeTable(id);  break;
        }
    }

    private void addProjectEmployee(int id) throws OperationNotAllowedException {
        String username = pickEmployee();
        if (app.getProject(id).hasEmployee(username)) {
            return;
        }
        app.addEmployee(username, id);
        System.out.printf("Employee %s successfully added to %d.\n\n", username, id);
    }

    private void addProjectActivity(int id) throws OperationNotAllowedException {
        System.out.print("Enter name of new activity: ");
        String name = in.getString();
        app.addNewActivity(name, id);
        System.out.printf("Activity %s successfully added to %d.\n\n", name, id);
    }

    private void editActivityDates(int id) throws OperationNotAllowedException {
        if (app.getProject(id).getActivities().size() == 0) {
            System.out.println("Project doesn't have any activities.");
            return;
        }
        display.activityList(app.getProject(id));
        String name = in.pickActivity(id);
        int[] dates = in.getDates();
        app.setDates(id, name, dates[0], dates[1]);
        System.out.printf("Dates of %s successfully changed to %06d %06d\n\n", name, dates[0], dates[1]);
    }

    private void editActivityWt(int id) throws OperationNotAllowedException {
        if (app.getProject(id).getActivities().size() == 0) {
            System.out.println("Project doesn't have any activities.");
            return;
        }
        display.activityList(app.getProject(id));
        String name = in.pickActivity(id);
        System.out.print("Enter expected worktime of activity: ");
        float wt = in.getPosFloat();
        app.setExpectedWt(id, name, wt);
        System.out.printf("Worktime of %s has successfully been set to %.1f.\n\n", name, wt);
    }

    private void registerWorktime() {
        if (app.getProjects().size() == 0) {
            System.out.println("No projects added to ProjectApp yet.");
            return;
        }
        int id = pickProject();
        if (!app.isUserOnProject(id)) {
            System.out.println("Insufficient Permissions. User is not assigned to the project.");
            return;
        }
        if (app.getProject(id).getActivities().size() == 0) {
            System.out.println("Project doesn't have any activities.");
            return;
        }

        display.activityList(app.getProject(id));
        String name = in.pickActivity(id);

        System.out.print("Date of work. ");
        int date = in.getDate();

        System.out.print("Enter worktime of activity: ");
        float wt = in.getPosFloat();

        app.setWorktime(date, wt, id, name);
    }

    private String pickEmployee() {
        display.employeeList();
        System.out.print("Please input username of employee: ");
        String username = in.getInitials(4);

        while (!app.hasEmployee(username)) {
            System.out.print("Input is not an employee, please enter new: ");
            username = in.getInitials(4);
        }
        System.out.println("");
        return username;
    }

    private int pickProject() {
        display.projectList();
        System.out.print("Enter ID of project you want to edit: ");
        int pick = in.getInt();
        while(!app.hasProject(pick)) {
            System.out.print("ID is not associated with any project. Please enter new: ");
            pick = in.getInt();
        }
        System.out.println("");
        return pick;
    }

    public void userLogin() {
        String username;
        System.out.print("Please input username to login: ");
        username = in.getInitials(4);

        while (!app.hasEmployee(username)) {
            System.out.print("Input is not an employee, please enter new: ");
            username = in.getInitials(4);
        }

        app.login(username);
        System.out.print("");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String type = evt.getPropertyName();
        switch (type) {
            case NotificationType.LOGOUT:
                System.out.println("User has been logged out.\n");
                break;
        }
    }
}
