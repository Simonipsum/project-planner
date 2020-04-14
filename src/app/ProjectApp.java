package app;

import java.util.*;

public class ProjectApp {
    private List<Employee> employees = new ArrayList<>();
    private List<Project> projects = new ArrayList<>();
    private Employee ceo = new Employee("marc");
    private Employee user = new Employee("NONE");
    private Activity absence = new Activity("Absence");

    public static void main(String[] args) throws OperationNotAllowedException {
        // Set CEO as an employee
        ProjectApp app = new ProjectApp();
        app.setCEOEmployee();

        // Run program
        app.derpHelper(); // initialises ProjectApp with some employees, projects and activities
        while (true) {
            app.userLogin();
            app.mainMenu(); // This menu goes to other menus.
        }
    }

    private void mainMenu() throws OperationNotAllowedException {
        int maxPick = display.mainMenu();
        int pick = controller.pickItem(maxPick);

        // Go to next menu / display
        switch (pick) {
            case 0: userLogout();                               break;

            // Employee
            case 1:  worktime();                                break;
            case 2:  registerAbsence();                         break;
            //case 3:  display.listActivities(user, projects);    break;
            //case 4:  getAssistance();                           break;

            // PM
            case 5:  projectMenu();                             break;
            //case 6:  checkAvailability();                       break;

            // CEO
            case 7:  setPM();                                   break;
            case 8:  addEmployee();                             break;
            case 9:  addProject();                              break;
            case 10: display.summary(projects, user, ceo);      break;

            // undecided
            case 11: display.listProjects(projects);            break;
            case 12: display.listEmployees(employees, ceo);     break;
        }
    }

    private void worktime() {
        if (projects.size() == 0) {
            System.out.println("No projects added to ProjectApp yet.");
            return;
        }
        int id = pickProject();
        if (!getProject(id).hasEmployee(user.getUsername())) {
            System.out.println("Insufficient Permissions. User is assigned to the project.");
            return;
        }
        if (getProject(id).getActivities().size() == 0) {
            System.out.println("Project doesn't have any activities.");
            return;
        }
        String name = pickActivity(id);

        System.out.print("Date of work. ");
        int date = controller.getDate();

        System.out.print("Enter worktime of activity: ");
        float wt = controller.getPosFloat();

        getProject(id).getActivity(name).setTime(user, wt, date);
        System.out.printf("Worktime of %s on date %06d has successfully been set to %.1f\n\n", name, date, wt);
    }

    private void projectMenu() throws OperationNotAllowedException {
        int pick, maxPick;

        if (projects.size() == 0) {
            System.out.println("No projects added to ProjectApp yet.");
            return;
        }

        int id = pickProject();
        if (!getProject(id).getPm().equals(user)) {
            System.out.println("Insufficient Permissions. User is not PM.");
            return;
        }
        maxPick = display.projectMenu(id);
        pick = controller.pickItem(maxPick);

        // Missing error checking.
        // Move cases to own methods?
        switch (pick) {
            case 0: return;
            case 1: addProjectEmployee(id);                     break;
            case 2: addProjectActivity(id);                     break;
            case 3: editActivityDates(id);                      break;
            case 4: editActivityWT(id);                         break;
            case 5: display.timeTable(getProject(id), user);    break;
        }
    }

    private void editActivityWT(int id) {
        if (getProject(id).getActivities().size() == 0) {
            System.out.println("Project doesn't have any activities.");
            return;
        }
        String name = pickActivity(id);
        System.out.print("Enter expected worktime of activity: ");
        float wt = controller.getPosFloat();
        getProject(id).getActivity(name).setExpectedWT(wt);
        System.out.printf("Worktime of %s has successfully been set to %.1f\n\n", name, wt);
    }

    private void editActivityDates(int id) throws OperationNotAllowedException {
        if (getProject(id).getActivities().size() == 0) {
            System.out.println("Project doesn't have any activities.");
            return;
        }
        String name = pickActivity(id);
        int[] dates = controller.getDates();
        setDates(id, name, dates[0], dates[1]);
        System.out.printf("Dates of %s successfully changed to %06d %06d\n\n", name, dates[0], dates[1]);
    }

    private String pickActivity(int id) {
        display.listActivities(getProject(id));
        System.out.print("Enter name of activity: ");
        String name = controller.getString();
        while(!getProject(id).hasActivity(name)) {
            System.out.printf("Activity %s does not exist. Pick another: ", name);
            name = controller.getString();
        }
        System.out.println("");
        return name;
    }

    private void addProjectActivity(int id) throws OperationNotAllowedException {
        System.out.print("Enter name of new activity: ");
        String name = controller.getString();
        addNewActivity(name, id);
        System.out.printf("Activity %s successfully added to %d\n\n", name, id);
    }

    private void addProjectEmployee(int id) throws OperationNotAllowedException {
        String username = pickEmployee();
        if (getProject(id).hasEmployee(username)) return;
        addEmployee(username, id);
        System.out.printf("Employee %s successfully added to %d\n\n", username, id);
    }

    private void setPM() {
        if (!isCEO()) {
            System.out.println("Insufficient Permissions. User is not CEO.");
            return;
        }
        if (projects.size() == 0) {
            System.out.println("No projects added to ProjectApp yet.");
            return;
        }
        int id = pickProject();
        String username = pickEmployee();
        getProject(id).setPm(getEmployee(username));
        System.out.printf("PM of %d was successfully set to %s\n\n", id, username);
    }

    public void registerAbsence() {
        int[] dates = controller.getDates();
        registerAbsence(dates[0], dates[1]);
        System.out.printf("Successfully set absence for %s in period: %06d to %06d\n\n",
                user.getUsername(), dates[0], dates[1]);
    }

    // Start is first day of absence, end is last day of absence
    public void registerAbsence(int start, int end) {
        Date current = new Date(start/10000, (start%10000)/100, (start%100));
        Date last    = new Date(end/10000, (end%10000)/100, (end%100));
        int date;

        while(!current.after(last)) {
            date = current.getYear() * 10000 + current.getMonth() * 100 + current.getDate();
            current.setDate(current.getDate() + 1);
            absence.setTime(user, 8, date);
        }
    }

    // Add employee to Project App
    private void addEmployee() throws OperationNotAllowedException {
        System.out.print("Initials of new Employee: ");
        String username = controller.getInitials(4);
        addNewEmployee(new Employee(username));
        System.out.printf("Employee %s was successfully added to the ProjectApp.\n\n", username);
    }

    private void addProject() throws OperationNotAllowedException {
        if (!isCEO()) {
            System.out.println("Insufficient Permissions. User is not CEO.");
            return;
        }
        System.out.print("Enter year of project start: ");
        int year = controller.getInt(1900, 2999);
        System.out.print("Enter name of project (type 'no' to skip naming): ");
        String name = controller.getString();
        addNewProject(year, name);
        System.out.printf("Project %d was successfully added to the ProjectApp.\n\n", calculateID(year));
    }

    private int pickProject() {
        display.listProjects(projects);
        System.out.print("Enter ID of project you want to edit: ");
        int pick = controller.getInt();
        while(!hasProject(pick)) {
            System.out.print("ID is not associated with any project. Please enter new: ");
            pick = controller.getInt();
        }
        System.out.println("");
        return pick;
    }

    private String pickEmployee() {
        display.listEmployees(employees, ceo);
        System.out.print("Please input username of employee: ");
        String username = controller.getInitials(4);

        while (!isEmployee(username)) {
            System.out.print("Input is not an employee, please enter new: ");
            username = controller.getInitials(4);
        }
        System.out.println("");
        return username;
    }

    // Add new Employee to ProjectApp
    public void addNewEmployee(Employee e) throws OperationNotAllowedException {
        if (!isCEO()) throw new OperationNotAllowedException("Insufficient Permissions. User is not CEO.");

        if (!isEmployee(e.getUsername())) {
            employees.add(e);
        } else {
            throw new OperationNotAllowedException("Employee with that username already registered");
        }
    }

    // Set worktime of one date
    public void setWorkTime(int date, float time, int id, String name) {
        getProject(id).getActivity(name).setTime(user, time, date);
    }

    // Set PM of project with id
    public void setPM(String username, int id) throws OperationNotAllowedException {
        if (!isCEO()) throw new OperationNotAllowedException("Insufficient Permissions. User is not CEO.");
        if (!isEmployee(username)) throw new OperationNotAllowedException("Username is not an Employee.");
        getProject(id).setPm(getEmployee(username));
    }


    public void removeEmployee(String username) throws OperationNotAllowedException {
        if (!isCEO()) throw new OperationNotAllowedException("Insufficient Permissions. User is not CEO.");
        employees.remove(getEmployee(username));
    }

    public void removeEmployee(String username, int id) throws OperationNotAllowedException {
        checkPM(id).removeEmployee(getEmployee(username));
    }

    // Add Employee to project
    public void addEmployee(String username, int id) throws OperationNotAllowedException {
        checkPM(id).addEmployee(getEmployee(username));
    }

    // Add Activity to Project
    public void addNewActivity(String name, int id) throws  OperationNotAllowedException {
        checkPM(id).addActivity(new Activity(name));
    }

    // Set dates of project activity
    public void setDates(int id, String name, int start, int end) throws OperationNotAllowedException {
        checkPM(id).setDates(name, start, end);
    }

    // Set newname of project activity
    public void setName(String name, int id, String newname) throws OperationNotAllowedException {
        checkPM(id).setName(name, newname);
    }

    public void setExpectedWT(int id, String name, float time) throws OperationNotAllowedException {
        checkPM(id).getActivity(name).setExpectedWT(time);
    }

    public Project checkPM(int id) throws OperationNotAllowedException {
        Project project = getProject(id);
        if (!project.getPm().equals(user)) throw new OperationNotAllowedException("Insufficient Permissions. User is not PM.");
        return project;
    }

    public void addNewProject(int year, String name) throws OperationNotAllowedException {
        if (!isCEO()) throw new OperationNotAllowedException("Insufficient Permissions. User is not CEO.");

        if (name.equals("no")) {
            projects.add(new Project(calculateID(year), year));
        } else {
            projects.add(new Project(calculateID(year), year, name));
        }
    }

    public int calculateID(int year) {
        int count = 1;
        for (Project p : projects) {
            count += p.getStartYear() == year ? 1 : 0;
        }
        return (year % 100) * 10000 + count;
    }

    public void userLogout() {
        user = new Employee("NONE");
        System.out.println("User has been logged out.\n");
    }

    public void userLogin() {
        String username;
        if (user.getUsername().equals("NONE")) {
            System.out.print("Please input username to login: ");
            username = controller.getInitials(4);

            while (!isEmployee(username)) {
                System.out.print("Input is not an employee, please enter new: ");
                username = controller.getInitials(4);
            }

            user = getEmployee(username);
            System.out.println("");
        }
    }

    // Used by use cases
    public void setCEO(Employee e) {
        this.ceo = e;
    }

    public void setUser(Employee e) {
        this.user = e;
    }

    public boolean isCEO() {
        return user.equals(ceo);
    }

    public boolean isEmployee(String username) {
        return employees.stream().anyMatch(e -> e.getUsername().equals(username));
    }

    public Employee getCEO() {
        return this.ceo;
    }

    public Employee getUser() {
        return this.user;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public Activity getAbsence() {
        return this.absence;
    }

    public Project getProject(int id) {
        return projects.stream().filter(p -> p.getId() == id).findFirst().get();
    }

    public Employee getEmployee(String username) {
        return employees.stream().filter(e -> e.getUsername().equals(username)).findFirst().get();
    }

    public boolean hasProject(int id) {
        return projects.stream().anyMatch(p -> p.getId() == id);
    }

    public void setCEOEmployee() {
        employees.add(ceo);
    }

    private void derpHelper() throws OperationNotAllowedException {
        user = getEmployee("marc");
        employees.add(new Employee("jan"));
        employees.add(new Employee("sim"));
        employees.add(new Employee("joe"));
        employees.add(new Employee("kim"));

        addNewProject(2020, "Project1");
        addNewProject(2020, "Project2");
        addNewProject(2020, "Project3");
        addNewProject(2020, "Project4");
        addNewProject(2020, "Project5");

        getProject(200001).addActivity(new Activity("ac1"));
        getProject(200001).addActivity(new Activity("ac2"));
        getProject(200002).addActivity(new Activity("ac1"));
        getProject(200002).addActivity(new Activity("ac2"));
        getProject(200003).addActivity(new Activity("ac1"));
        getProject(200003).addActivity(new Activity("ac2"));
        getProject(200004).addActivity(new Activity("ac1"));
        getProject(200004).addActivity(new Activity("ac2"));
        getProject(200005).addActivity(new Activity("ac1"));
        getProject(200005).addActivity(new Activity("ac2"));

        setPM("jan", 200001);
        setPM("jan", 200003);
        setPM("joe", 200005);


        user = new Employee("NONE");
    }
}
