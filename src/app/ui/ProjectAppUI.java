/**
 * Main responsible: Simon Amtoft Pedersen
 */

package app.ui;

import app.OperationNotAllowedException;
import app.model.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

public class ProjectAppUI implements PropertyChangeListener {
    private static ProjectAppUI instance;

    private ProjectApp app;
    private menuHelper display;
    private inputHelper in;

    private ProjectAppUI() {
        app = ProjectApp.getInstance();
        display = menuHelper.getInstance();
        in = inputHelper.getInstance();
        app.addObserver(this);
    }

    public static ProjectAppUI getInstance() {
        if (instance == null) {
            instance = new ProjectAppUI();
        }
        return instance;
    }

    public void mainLoop() throws OperationNotAllowedException {
        // app.derpHelper(); // Initialize some data to ease testing

        // Run program
        while(true) {
            if (app.getUser() == null) {
                userLogin();
            }
            processChoiceMain(in.pickItem(display.mainMenu()));
        }
    }

    private void processChoiceMain(int pick) throws OperationNotAllowedException {
        int[] ceo_options = {7, 8, 9, 10, 13, 14};
        if (!app.getUser().equals(app.getCEO()) && Arrays.stream(ceo_options).anyMatch(i -> i == pick)) {
            System.out.println("Insufficient Permissions: User is not CEO.\n");
            return;
        }

        switch(pick) {
            case 0: app.logout();                   break;

            // Employee
            case 1:  registerWorktime();            break;
            case 2:  registerAbsence();             break;
            case 3:  display.activityListUser();    break;
            case 4:  getAssistance();               break;

            // PM
            case 5:  projectMenu();                 break;
            case 6:  checkAvailability();           break;

            // CEO
            case 7:  setPM();                       break;
            case 8:  addEmployee();                 break;
            case 9:  addProject();                  break;
            case 10: display.summary();             break;

            // for all employees
            case 11: display.projectList();         break;
            case 12: display.employeeList();        break;

            // CEO
            case 13: removeEmployee();              break;
            case 14: exitApp();                     break;
        }
    }

    private void registerWorktime() throws OperationNotAllowedException {
        int id;
        try {
            id = userPickProjectWithActivity();
        } catch (OperationNotAllowedException e){
            System.out.println(e.getMessage());
            return;
        }

        display.activityList(app.getProject(id));
        String acName = in.pickActivity(id);

        System.out.print("Date of work. ");
        int date = in.getDate();

        System.out.print("Enter worktime of activity: ");
        float wt = in.getWorkHours();

        app.setWorktime(date, wt, id, acName);
    }

    private void getAssistance() throws OperationNotAllowedException {
        int id;
        try {
            id = userPickProjectWithActivity();
        } catch (OperationNotAllowedException e){
            System.out.println(e.getMessage());
            return;
        }
        if(app.getProject(id).hasAssistant(app.getUser())) {
            System.out.println("Insufficient Permissions: User is not assigned to that project.");
            return;
        }

        display.activityList(app.getProject(id));
        String acName = in.pickActivity(id);

        String un = pickEmployee();
        app.addAssistance(app.getEmployee(un), acName, id);
        System.out.printf("%s successfully added as an assistant to activity %s on project %d\n\n", un, acName, id);
    }

    private int userPickProjectWithActivity() throws OperationNotAllowedException {
        if (app.getProjects().size() == 0) {
            throw new OperationNotAllowedException("No projects added to project-planner yet.");
        }
        int id = pickProject();
        if (!app.isUserOnProject(id) && !app.getProject(id).hasAssistant(app.getUser())) {
            throw new OperationNotAllowedException("Insufficient Permissions: " +
                    "User is not assigned to that project " +
                    "or is an assistant on the project.");
        }
        if (app.getProject(id).getActivities().size() == 0) {
            throw new OperationNotAllowedException("Project doesn't have any activities.");
        }
        return id;
    }

    private void checkAvailability() {
        int[] dates = in.getDates();
        display.availability(dates);
    }

    private void registerAbsence() throws OperationNotAllowedException {
        int[] dates = in.getDates();
        app.registerAbsence(dates[0], dates[1]);
        System.out.println(dates[0]);
        if (dates[0] / 1000000 > 0) {
            System.out.printf("Successfully set absence for %s in period: %d to %d\n\n",
                    app.getUser().getUsername(), dates[0], dates[1]);
        } else {
            System.out.printf("Successfully set absence for %s in period: %06d to %06d\n\n",
                    app.getUser().getUsername(), dates[0], dates[1]);
        }
    }

    private void projectMenu() throws OperationNotAllowedException {
        if (app.getProjects().size() == 0) {
            System.out.println("No projects added to project-planner yet.");
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

    /* CEO options */
    private void removeEmployee() throws OperationNotAllowedException {
        System.out.print("Enter initials of employee: ");
        String username = in.getInitials();
        if (!app.hasEmployee(username)) {
            System.out.printf("Error: App does not contain an employee with initials %s.\n\n", username);
            return;
        } else if (app.getUser().getUsername().equals(username)) {
            System.out.println("Error: Can't remove yourself from project-planner.");
            return;
        }
        app.removeEmployee(username);
        System.out.printf("Employee %s was successfully removed from the project-planner.\n\n", username);
    }

    private void exitApp() {
        System.out.println("Exiting project-planner. All data will be lost.");
        if (in.getConfirmation()) {
            System.out.println("project-planner exiting...");
            System.exit(0);
        } else {
            System.out.println("project-planner will continue running.\n");
        }
    }

    private void setPM() throws OperationNotAllowedException {
        if (app.getProjects().size() == 0) {
            System.out.println("No projects added to the project-planner yet.");
            return;
        }
        int id = pickProject();
        String username = pickEmployee();
        app.setPM(username, id);
        System.out.print("PM of " + id + " was successfully set to " + username + "\n\n");
    }

    private void addProject() throws OperationNotAllowedException {
        System.out.print("Enter year of project start: ");
        int year = in.getInt(1900, 2999);
        System.out.print("Enter name of project (type 'N' to skip naming): ");
        String name = in.getString();
        app.addNewProject(year, name);
        System.out.printf("Project %d was successfully added to the project-planner.\n\n", app.calculateID(year));
    }

    private void addEmployee() throws OperationNotAllowedException {
        System.out.print("Initials of new Employee: ");
        String username = in.getInitials();
        if (app.hasEmployee(username)) {
            System.out.printf("App already has an Employee with initials %s.\n\n", username);
            return;
        }
        app.addNewEmployee(new Employee(username));
        System.out.print("Employee " + username + " was successfully added to the project-planner.\n\n");
    }

    /* */

    private void processChoiceProject(int pick, int id) throws OperationNotAllowedException {
        switch (pick) {
            case 0: return;
            case 1: addProjectEmployee(id); break;
            case 2: addProjectActivity(id); break;
            case 3: editActivityDates(id);  break;
            case 4: editActivityWt(id);     break;
            case 5: display.timeTable(id);  break;
            case 6: editProjectName(id);    break;
            case 7: editActivityName(id);   break;
            case 8: removeProjectEmployee(id); break;
        }
    }

    private void removeProjectEmployee(int id) throws OperationNotAllowedException {
        String username = pickEmployee();
        Project p = app.getProject(id);
        if (!p.hasEmployee(username)) {
            System.out.printf("Error: %s is not on the project %d.\n\n", username, id);
            return;
        } else if (app.getCEO().getUsername().equals(username)) {
            System.out.println("Error: Can't remove ceo from project.");
            return;
        } else if (p.getPm().getUsername().equals(username)) {
            System.out.println("Error: Can't remove yourself (pm) from project.");
            return;
        }

        app.removeEmployee(username, id);
        System.out.printf("Employee %s successfully removed from %d.\n\n", username, id);
    }


    private void editProjectName(int id) throws OperationNotAllowedException {
        System.out.print("Enter new name of project");
        String name = in.getString();
        app.setProjectName(id, name);
    }

    private void editActivityName(int id) throws OperationNotAllowedException {
        if (app.getProject(id).getActivities().size() == 0) {
            System.out.println("Project doesn't have any activities.");
            return;
        }
        display.activityList(app.getProject(id));
        String acName = in.pickActivity(id);
        System.out.print("Enter new name of activity: ");
        String newname = in.getString();
        app.setActivityName(acName, id, newname);
    }

    private void addProjectEmployee(int id) throws OperationNotAllowedException {
        String username = pickEmployee();
        if (app.getProject(id).hasEmployee(username)) {
            System.out.printf("Error: %s is already on project %d\n\n", username, id);
            return;
        }
        app.addEmployee(username, id);
        System.out.printf("Employee %s successfully added to %d.\n\n", username, id);
    }

    private void addProjectActivity(int id) throws OperationNotAllowedException {
        System.out.print("Enter name of new activity: ");
        String acName = in.getString();
        if (app.getProject(id).hasActivity(acName)) {
            System.out.printf("Error: Project already has activity with name %s.\n\n", acName);
            return;
        }
        app.addNewActivity(acName, id);
        System.out.printf("Activity %s successfully added to %d.\n\n", acName, id);
    }

    private void editActivityDates(int id) throws OperationNotAllowedException {
        if (app.getProject(id).getActivities().size() == 0) {
            System.out.println("Project doesn't have any activities.");
            return;
        }
        display.activityList(app.getProject(id));
        String acName = in.pickActivity(id);
        int[] dates = in.getDates();
        app.setDates(id, acName, dates[0], dates[1]);
        System.out.printf("Dates of %s successfully changed to %06d %06d\n\n", acName, dates[0], dates[1]);
    }

    private void editActivityWt(int id) throws OperationNotAllowedException {
        if (app.getProject(id).getActivities().size() == 0) {
            System.out.println("Project doesn't have any activities.");
            return;
        }
        display.activityList(app.getProject(id));
        String acName = in.pickActivity(id);
        System.out.print("Enter expected worktime of activity: ");
        float wt = in.getPosFloat();
        app.setExpectedWt(id, acName, wt);
        System.out.printf("Worktime of %s has successfully been set to %.1f.\n\n", acName, wt);
    }

    private String pickEmployee() {
        display.employeeList();
        System.out.print("Please input username of employee: ");
        String username = in.getInitials();

        while (!app.hasEmployee(username)) {
            System.out.print("Input is not an employee, please enter new: ");
            username = in.getInitials();
        }
        System.out.println("");
        return username;
    }

    private int pickProject() {
        display.projectList();
        System.out.print("Enter ID of project: ");
        int pick = in.getPosInt();
        while(!app.hasProject(pick)) {
            System.out.print("ID is not associated with any project. Please enter new: ");
            pick = in.getPosInt();
        }
        System.out.println("");
        return pick;
    }

    private void userLogin() {
        String username;
        System.out.print("Please input username to login: ");
        username = in.getInitials();

        while (!app.hasEmployee(username)) {
            System.out.print("Input is not an employee, please enter new: ");
            username = in.getInitials();
        }
        app.login(app.getEmployee(username));
        System.out.print("");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String type = evt.getPropertyName();
        switch (type) {
            case NotificationType.LOGOUT:
                System.out.println("User has been logged out.\n");
                break;
            case NotificationType.LOGIN:
                System.out.printf("Successfully logged in as %s.\n\n", app.getUser().getUsername());
                break;
        }
    }
}
