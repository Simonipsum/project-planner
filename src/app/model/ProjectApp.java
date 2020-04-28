package app.model;

import app.OperationNotAllowedException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.util.*;

public class ProjectApp {
    private PropertyChangeSupport sup = new PropertyChangeSupport(this);

    private List<Employee> employees = new ArrayList<>();
    private List<Project> projects = new ArrayList<>();
    private Employee ceo;
    private Employee user = null;
    private Activity absence = new Activity("Absence");

    public ProjectApp() {
        ceo = new Employee("marc");
        employees.add(ceo);
    }

    /* Alter ProjectApp */

    public void registerAbsence(int start, int end) {
        // Start is first day of absence, end is last day of absence
        Calendar current = new GregorianCalendar(start/10000, (start%10000)/100, (start%100));
        Calendar last    = new GregorianCalendar(end/10000, (end%10000)/100, (end%100));
        int date;

        while(!current.after(last)) {
            date = current.get(Calendar.YEAR) * 10000 + current.get(Calendar.MONTH)  * 100 + current.get(Calendar.DATE) ;
            current.set(Calendar.DATE,current.get(Calendar.DATE) + 1);
            absence.setTime(user, 8, date);
        }
    }

    // Add new Employee to ProjectApp
    public void addNewEmployee(Employee e) throws OperationNotAllowedException {
        if (!currentUserIsCEO()) {
            throw new OperationNotAllowedException("Insufficient Permissions. User is not CEO.");
        }
        if (!hasEmployee(e.getUsername())) {
            employees.add(e);
        } else {
            throw new OperationNotAllowedException("Employee with that username already registered");
        }
    }

    public void addNewEmployee(String username) throws OperationNotAllowedException {
        addNewEmployee(new Employee(username));
    }

    // Removes an Employee from ProjectApp
    public void removeEmployee(String username) throws OperationNotAllowedException {
        if (!currentUserIsCEO()) {
            throw new OperationNotAllowedException("Insufficient Permissions. User is not CEO.");
        }
        employees.remove(getEmployee(username));
    }

    public void addNewProject(int year, String name) throws OperationNotAllowedException {
        if (!currentUserIsCEO()){
            throw new OperationNotAllowedException("Insufficient Permissions. User is not CEO.");
        }
        int id = calculateID(year);
        if (name.equals("N")) {
            projects.add(new Project(id, year));
        } else {
            projects.add(new Project(id, year, name));
        }
        getProject(id).addEmployee(ceo); // Lets always have CEO on the project
    }

    /* Alter Projects & Activities */

    // Removes an Employee from a project
    public void removeEmployee(String username, int id) throws OperationNotAllowedException {
        accessProject(id).removeEmployee(getEmployee(username));
    }

    // Add Employee to Project
    public void addEmployee(String username, int id) throws OperationNotAllowedException {
        accessProject(id).addEmployee(getEmployee(username));
    }

    // Add assistance to Project
    public void addAssistance(String username, String name, int id) {
        Project p = getProject(id);
        if (p.hasEmployee(user.getUsername())) {
            p.addAssistant(getEmployee(username), name);
        }
    }

    // Add Activity to Project
    public void addNewActivity(String name, int id) throws  OperationNotAllowedException {
        accessProject(id).addActivity(new Activity(name));
    }

    // Set dates of project activity
    public void setDates(int id, String name, int start, int end) throws OperationNotAllowedException {
        accessProject(id).setActivityDates(name, start, end);
    }

    public Project accessProject(int id) throws OperationNotAllowedException {
        Project project = getProject(id);
        if (!project.isPm(user.getUsername())) {
            throw new OperationNotAllowedException("Insufficient Permissions. User is not PM.");
        }
        return project;
    }

    // Set PM of project with id
    public void setPM(String username, int id) throws OperationNotAllowedException {
        if (!currentUserIsCEO()) {
            throw new OperationNotAllowedException("Insufficient Permissions. User is not CEO.");
        }
        if (!hasEmployee(username)) {
            throw new OperationNotAllowedException("Username is not an Employee.");
        }
        Project p = getProject(id);
        if (!p.hasEmployee(username)) {
            p.addEmployee(getEmployee(username));       // Add pm to the project if he was not on it
        }
        p.setPm(getEmployee(username));
    }

    public void setActivityName(String name, int id, String newname) throws OperationNotAllowedException {
        accessProject(id).setActivityName(name, newname);
    }

    public void setExpectedWt(int id, String name, float time) throws OperationNotAllowedException {
        accessProject(id).getActivity(name).setExpectedWT(time);
    }

    // Set worktime of one date
    public void setWorktime(int date, float time, int id, String name) throws OperationNotAllowedException {
        Project p = getProject(id);

        if(p.hasEmployee(user) || p.hasAssistant(user)) {
            p.getActivity(name).setTime(user, time, date);
        } else {
            throw new OperationNotAllowedException("Insufficient Permissions: User is not assigned to that project or is a helper on that activity");
        }
    }

    /* Login and logout*/

    public void logout() {
        this.user = null;
        sup.firePropertyChange(NotificationType.LOGOUT,null, null);
    }

    public void login(Employee e) throws OperationNotAllowedException {
        if (this.user == null) {
            this.user = e;
        } else {
            throw new OperationNotAllowedException("Insufficient Permissions: A user is already logged in.");
        }
    }

    /* Has/is checks */

    public boolean isUserOnProject(int id) {
        return getProject(id).hasEmployee(user);
    }

    public boolean isUserAssistantOnProject(int id) {
        return getProject(id).hasAssistant(user);
    }

    public boolean hasEmployee(String username) {
        return employees.stream().anyMatch(e -> e.getUsername().equals(username));
    }

    public boolean hasProject(int id) {
        return projects.stream().anyMatch(p -> p.getId() == id);
    }

    public boolean isUserPm(int id) {
        return getProject(id).isPm(user.getUsername());
    }

    public boolean currentUserIsCEO() {
        return user.equals(ceo);
    }

    /* Getters and Setters */

    public Employee getCEO() {
        return this.ceo;
    }

    public void setCEO(Employee e) {
        this.ceo = e;
    }

    public Employee getUser() {
        return this.user;
    }

    public Project getProject(int id) {
        return projects.stream().filter(p -> p.getId() == id).findFirst().get();
    }

    public List<Project> getProjects() {
        return projects;
    }

    public Activity getAbsence() {
        return this.absence;
    }

    public List<Employee> getEmployees() {
        return this.employees;
    }

    public Employee getEmployee(String username) {
        return employees.stream().filter(e -> e.getUsername().equals(username)).findFirst().get();
    }

    /* MISC */

    public Map<Employee, Integer> getAvailability(int[] dates) {
        int overlap, start, end;
        Map<Employee, Integer> availability = new HashMap<>();

        for (Employee e : employees) {
            overlap = 0;

            for (Project p : projects) {
                if (p.hasEmployee(e)) {
                    for (Activity a : p.getActivities()) {
                        start = a.getStart();
                        end = a.getEnd();

                        if (end >= dates[0] && start <= dates[1]) {
                            overlap += Math.min(end, dates[1]) - Math.max(start, dates[0]) + 1;
                        }
                    }
                }
            }
            availability.put(e, overlap);
        }
        return availability;
    }

    public int calculateID(int year) {
        int count = 1;
        for (Project p : projects) {
            count += p.getStartYear() == year ? 1 : 0;
        }
        return (year % 100) * 10000 + count;
    }

    public void addObserver(PropertyChangeListener listener) {
        sup.addPropertyChangeListener(listener);
    }

    public void derpHelper() throws OperationNotAllowedException {
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
        setPM("sim", 200002);
        setPM("kim", 200003);
        setPM("joe", 200005);

        addAssistance("sim","ac1", 200001);
        // Set some start and end dates
        getProject(200001).getActivity("ac1").setStart(200101);
        getProject(200001).getActivity("ac1").setEnd(200201);

        getProject(200002).getActivity("ac1").setStart(200301);
        getProject(200002).getActivity("ac1").setEnd(200401);

        getProject(200003).getActivity("ac1").setStart(200115);
        getProject(200003).getActivity("ac1").setEnd(200220);

        getProject(200004).getActivity("ac1").setStart(200120);
        getProject(200004).getActivity("ac1").setEnd(200205);

        getProject(200005).getActivity("ac1").setStart(200220);
        getProject(200005).getActivity("ac1").setEnd(200330);

        user = null;
    }
}
