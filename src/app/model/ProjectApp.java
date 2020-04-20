package app.model;

import app.Controller;
import app.OperationNotAllowedException;
import app.View;

import java.util.*;

public class ProjectApp {
    private View v = new View();;
    private Controller c = new Controller(v);

    private List<Employee> employees = new ArrayList<>();
    private List<Project> projects = new ArrayList<>();;
    private Employee ceo = new Employee("marc");;
    private Employee user = null;
    private Activity absence = new Activity("Absence");

    public void mainLoop() throws OperationNotAllowedException {
        employees.add(ceo);
        while(true) {
            if (user == null) {
                userLogin();
            }
            mainMenu();
        }
    }

    private void mainMenu() throws OperationNotAllowedException {
        int maxPick = v.mainMenu();
        int pick = c.pickItem(maxPick);

        // Go to next menu / display
        switch (pick) {
            case 0: userLogout();                               break;

            // Employee
            case 1:  worktime();                                break;
            case 2:  registerAbsence();                         break;
            //case 3:  v.listActivities(user, projects);    break;
            //case 4:  getAssistance();                           break;

            // PM
            case 5:  projectMenu();                       break;
            //case 6:  checkAvailability();                       break;

            // CEO
            case 7:  setPM();                             break;
            case 8:  addEmployee();                       break;
            case 9:  addProject();                        break;
            case 10: v.summary(projects, user, ceo);      break;

            // undecided
            case 11: v.listProjects(projects);            break;
            case 12: v.listEmployees(employees, ceo);     break;
        }
    }

    private void worktime() {
        if (projects.size() == 0) {
            v.println("No projects added to ProjectApp yet.");
            return;
        }
        int id = pickProject();
        if (!getProject(id).hasEmployee(user.getUsername())) {
            v.println("Insufficient Permissions. User is not assigned to the project.");
            return;
        }
        if (getProject(id).getActivities().size() == 0) {
            v.println("Project doesn't have any activities.");
            return;
        }
        String name = pickActivity(id);

        v.print("Date of work. ");
        int date = c.getDate();

        v.print("Enter worktime of activity: ");
        float wt = c.getPosFloat();

        getProject(id).getActivity(name).setTime(user, wt, date);
        v.printf("Worktime of %s on date %06d has successfully been set to %.1f.\n\n", name, date, wt);
    }

    private void projectMenu() throws OperationNotAllowedException {
        if (projects.size() == 0) {
            v.println("No projects added to ProjectApp yet.");
            return;
        }

        int id = pickProject();
        if (!getProject(id).isPm(user.getUsername())) {
            v.println("Insufficient Permissions. User is not PM.");
            return;
        }

        int maxPick = v.projectMenu(id);
        int pick = c.pickItem(maxPick);
        switch (pick) {
            case 0: return;
            case 1: addProjectEmployee(id);               break;
            case 2: addProjectActivity(id);               break;
            case 3: editActivityDates(id);                break;
            case 4: editActivityWT(id);                   break;
            case 5: v.timeTable(getProject(id), user);    break;
        }
    }

    private void editActivityWT(int id) {
        if (getProject(id).getActivities().size() == 0) {
            v.println("Project doesn't have any activities.");
            return;
        }
        String name = pickActivity(id);
        v.print("Enter expected worktime of activity: ");
        float wt = c.getPosFloat();
        getProject(id).getActivity(name).setExpectedWT(wt);
        v.printf("Worktime of %s has successfully been set to %.1f.\n\n", name, wt);
    }

    private void editActivityDates(int id) throws OperationNotAllowedException {
        if (getProject(id).getActivities().size() == 0) {
            v.println("Project doesn't have any activities.");
            return;
        }
        String name = pickActivity(id);
        int[] dates = c.getDates();
        setDates(id, name, dates[0], dates[1]);
        v.printDates("Dates of %s successfully changed to %06d %06d\n\n", dates);
    }

    private String pickActivity(int id) {
        v.listActivities(getProject(id));
        v.print("Enter name of activity: ");
        String name = c.getString();
        while(!getProject(id).hasActivity(name)) {
            v.print("Activity " + name + " does not exist. Pick another: ");
            name = c.getString();
        }
        v.newLine();
        return name;
    }

    private void addProjectActivity(int id) throws OperationNotAllowedException {
        v.print("Enter name of new activity: ");
        String name = c.getString();
        addNewActivity(name, id);
        v.print("Activity " + name + "successfully added to " + id + ".\n\n");
    }

    private void addProjectEmployee(int id) throws OperationNotAllowedException {
        String username = pickEmployee();
        if (getProject(id).hasEmployee(username)) return;
        addEmployee(username, id);
        v.print("Employee " + username + " successfully added to "+ id + ".\n\n");
    }

    private void setPM() {
        if (!isCEO()) {
            v.println("Insufficient Permissions. User is not CEO.");
            return;
        }
        if (projects.size() == 0) {
            v.println("No projects added to ProjectApp yet.");
            return;
        }
        int id = pickProject();
        String username = pickEmployee();
        getProject(id).setPm(getEmployee(username));
        v.print("PM of " + id + " was successfully set to " + username + "\n\n");
    }

    public void registerAbsence() {
        int[] dates = c.getDates();
        registerAbsence(dates[0], dates[1]);
        v.printDates("Successfully set absence for "+user.getUsername()+" in period: %06d to %06d\n\n", dates);
    }

    // Start is first day of absence, end is last day of absence
    public void registerAbsence(int start, int end) {
        Calendar current = new GregorianCalendar(start/10000, (start%10000)/100, (start%100));
        Calendar last    = new GregorianCalendar(end/10000, (end%10000)/100, (end%100));
        int date;

        while(!current.after(last)) {
            date = current.get(Calendar.YEAR) * 10000 + current.get(Calendar.MONTH)  * 100 + current.get(Calendar.DATE) ;
            current.set(Calendar.DATE,current.get(Calendar.DATE) + 1);
            absence.setTime(user, 8, date);
        }
    }

    // Add employee to Project App
    private void addEmployee() throws OperationNotAllowedException {
        v.print("Initials of new Employee: ");
        String username = c.getInitials(4);
        addNewEmployee(new Employee(username));
        v.print("Employee " + username + " was successfully added to the ProjectApp.\n\n");
    }

    private void addProject() throws OperationNotAllowedException {
        if (!isCEO()) {
            v.println("Insufficient Permissions. User is not CEO.");
            return;
        }
        v.print("Enter year of project start: ");
        int year = c.getInt(1900, 2999);
        v.print("Enter name of project (type 'N' to skip naming): ");
        String name = c.getString();
        addNewProject(year, name);
        v.print("Project " + calculateID(year) + " was successfully added to the ProjectApp.\n\n");
    }

    private int pickProject() {
        v.listProjects(projects);
        v.print("Enter ID of project you want to edit: ");
        int pick = c.getInt();
        while(!hasProject(pick)) {
            v.print("ID is not associated with any project. Please enter new: ");
            pick = c.getInt();
        }
        v.newLine();
        return pick;
    }

    private String pickEmployee() {
        v.listEmployees(employees, ceo);
        v.print("Please input username of employee: ");
        String username = c.getInitials(4);

        while (!isEmployee(username)) {
            v.print("Input is not an employee, please enter new: ");
            username = c.getInitials(4);
        }
        v.newLine();
        return username;
    }

    // Add new Employee to ProjectApp
    public void addNewEmployee(Employee e) throws OperationNotAllowedException {
        if (!isCEO()) {
            throw new OperationNotAllowedException("Insufficient Permissions. User is not CEO.");
        }
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
        if (!isCEO()) {
            throw new OperationNotAllowedException("Insufficient Permissions. User is not CEO.");
        }
        if (!isEmployee(username)) {
            throw new OperationNotAllowedException("Username is not an Employee.");
        }
        getProject(id).setPm(getEmployee(username));
    }

    public void removeEmployee(String username) throws OperationNotAllowedException {
        if (!isCEO()) {
            throw new OperationNotAllowedException("Insufficient Permissions. User is not CEO.");
        }
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

    // Set new name of project activity
    public void setName(String name, int id, String newname) throws OperationNotAllowedException {
        checkPM(id).setName(name, newname);
    }

    public void setExpectedWT(int id, String name, float time) throws OperationNotAllowedException {
        checkPM(id).getActivity(name).setExpectedWT(time);
    }

    public Project checkPM(int id) throws OperationNotAllowedException {
        Project project = getProject(id);
        if (!project.isPm(user.getUsername())) {
            throw new OperationNotAllowedException("Insufficient Permissions. User is not PM.");
        }
        return project;
    }

    public void addNewProject(int year, String name) throws OperationNotAllowedException {
        if (!isCEO()){
            throw new OperationNotAllowedException("Insufficient Permissions. User is not CEO.");
        }

        if (name.equals("N")) {
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
        user = null;
        v.println("User has been logged out.\n");
    }

    public void userLogin() {
        String username;
        v.print("Please input username to login: ");
        username = c.getInitials(4);

        while (!isEmployee(username)) {
            v.print("Input is not an employee, please enter new: ");
            username = c.getInitials(4);
        }

        user = getEmployee(username);
        v.newLine();
    }

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

        user = null;
    }
}
