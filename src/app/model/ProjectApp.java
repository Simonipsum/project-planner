package app.model;

import app.OperationNotAllowedException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class ProjectApp {
    private PropertyChangeSupport sup = new PropertyChangeSupport(this);

    private List<Employee> employees = new ArrayList<>();
    private List<Project> projects = new ArrayList<>();;
    private Employee ceo;
    private Employee user = null;
    private Activity absence = new Activity("Absence");

    public ProjectApp() {
        ceo = new Employee("marc");
        employees.add(ceo);
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

    public void addNewEmployee(String username) throws OperationNotAllowedException {
        addNewEmployee(new Employee(username));
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

    public List<Employee> getEmployees() {
        return this.employees;
    }

    public boolean hasProject(int id) {
        return projects.stream().anyMatch(p -> p.getId() == id);
    }

    public void addObserver(PropertyChangeListener listener) {
        sup.addPropertyChangeListener(listener);
    }

    public void logout() {
        this.user = null;
        sup.firePropertyChange(NotificationType.LOGOUT,null, null);
    }

    public void login(String username) {
        this.user = getEmployee(username);
    }

    public boolean userOnProject(int id) {
        return getProject(id).hasEmployee(user.getUsername());
    }

    public boolean userIsPm(int id) {
        return getProject(id).isPm(user.getUsername());
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
        setPM("jan", 200003);
        setPM("joe", 200005);

        user = null;
    }
}
