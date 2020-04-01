package app;

import java.util.*;

public class ProjectApp {
    private List<Employee> employees = new ArrayList<Employee>();
    private List<Project> projects = new ArrayList<Project>();
    private String ceo = "marc";
    private String user = "NONE";

    public void main(String[] args) throws OperationNotAllowedException {
        // Set CEO as an employee
        this.employees.add(new Employee(ceo));

        // Run program
        while(true) {
            userLogin();
            mainMenu(); // This menu goes to other menus.
        }
    }

    public void mainMenu() throws OperationNotAllowedException {
        int maxPick = display.mainMenu();
        int pick = controller.pickItem(maxPick);

        // Go to next menu / app.display
        switch(pick) {
            // app.Employee
            case 1:  workTime();                                break;
            case 2:  registerAbsence();                         break;
            case 3:  display.listActivities(getEmployee(user)); break;
            case 4:  getAssistance();                           break;
            case 5:  userLogout();                              break;

            // PM
            case 6:  projectMenu();                             break;
            case 7:  checkAvailability();                       break;

            // CEO
            case 8:  setPM();                                   break;
            //case 9:  addEmployee();                             break;
            case 10: addProject();                              break;

            // undecided
            case 11: display.listProjects(projects);            break;
            case 12: display.listEmployees(employees);          break;
        }
    }

    public void checkAvailability() {
        // check what projects user are PM of.
        // if none, return

        // pick date / dates
        // check availability of employees
    }

    public void getAssistance() {
        // get assistance on an activity.
    }

    public void registerAbsence() {
        // Register absence of user at given start to end date interval
    }

    public void workTime() {
        // list projects.
        // pick one
        // list all activities of that project
        // pick one
        // add/edit work time of that one
    }

    public void setPM() {
        if (!isCEO()) {
            System.out.println("User is not CEO!");
            return;
        }

        display.listProjects(projects);

        // list all projects
        // pick one
        // set pm of that one
    }

    public void projectMenu() {
        int pick, maxPick;

        maxPick = display.projectMenu();
        pick = controller.pickItem(maxPick);
    }

    // Set PM of project with id 'id'.
    public void setPM(String username, int id) throws OperationNotAllowedException {
        if(!isCEO()) {
            throw new OperationNotAllowedException("Insufficient Permissions. User is not CEO.");
        }
        getProject(id).setPm(getEmployee(username));
    }

    private void addEmployee() throws OperationNotAllowedException {
        System.out.print("Initials of new Employee: ");
        String username = controller.getInitials(4);
        addNewEmployee(username);
    }

    // Add new Employee to ProjectApp
    public void addNewEmployee(String username) throws OperationNotAllowedException {
        if(!isCEO()) {
            throw new OperationNotAllowedException("Insufficient Permissions. User is not CEO.");
        }
        employees.add(new Employee(username));
    }

    // Add Employee to project
    public void addEmployee(String username, int id) throws OperationNotAllowedException {
        Project project = projects.stream().filter(p -> p.getId() == id).findFirst().get();
        if(!project.getPm().getUsername().equals(user)) {
            throw new OperationNotAllowedException("Insufficient Permissions. User is not PM.");
        }
        project.addEmployee(getEmployee(username));
    }

    public int calculateID(int year) {
        int count = 1;

        for (Project p : projects) {
            if (p.getStartYear() == year) {
                count += 1;
            }
        }
        return (year%100)*10000 + count;
    }

    public void addProject() throws OperationNotAllowedException {
        int year;
        String name;

        System.out.print("Enter year of project start: ");
        year = controller.getInputInt();

        System.out.print("Enter name of project (type no if not named yet): ");
        name = controller.getString();

        addNewProject(year, name);
    }

    public void addNewProject(int year, String name) throws OperationNotAllowedException {
        if(!isCEO()) {
            throw new OperationNotAllowedException("Insufficient Permissions. User is not CEO.");
        }

        if (name.equals("no")) {
            projects.add(new Project(calculateID(year), year));
        } else {
            projects.add(new Project(calculateID(year), year, name));
        }
    }

    public void userLogout() {
        user = "NONE";
        System.out.println("User has been logged out.\n");
    }

    public void userLogin() {
        if (user.equals("NONE")) {
            System.out.print("Please input username to login: ");
            user = controller.getInitials(4);
            while (!isEmployee(user)) {
                System.out.print("Input is not an employee, please enter new: ");
                user = controller.getInitials(4);
            }
            System.out.println("");
        }
    }

    public Employee getEmployee(String username) {
        return employees.stream().filter(e -> e.getUsername().equals(username)).findFirst().get();
    }

    public boolean isEmployee(String username) {
        return employees.stream().anyMatch(e -> e.getUsername().equals(username));
    }

    public Project getProject(int id) {
        return projects.stream().filter(p -> p.getId() == id).findFirst().get();
    }

    public void setCEO(String username) { this.ceo = username; }
    public void setUser(String username) { this.user = username; }
    public boolean isCEO() { return user.equals(ceo); }
    public String getCEO() { return this.ceo; }
    public String getUser() { return this.user; }
    public List<Project> getProjects() { return projects; }

}
