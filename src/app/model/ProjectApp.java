/**
 * Main responsible: Simon Amtoft Pedersen
 */

package app.model;

import app.OperationNotAllowedException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

public class ProjectApp {
    private static ProjectApp instance;

    private PropertyChangeSupport sup = new PropertyChangeSupport(this);
    private List<Employee> employees = new ArrayList<>();
    private List<Project> projects = new ArrayList<>();
    private Employee ceo;
    private Employee user = null;
    private Activity absence = new Activity("Absence");

    public ProjectApp() {
        ceo = new Employee("huba");
        employees.add(ceo);
    }

    public static ProjectApp getInstance() {
        if (instance == null) {
            instance = new ProjectApp();
        }
        return instance;
    }

    /* Alter ProjectApp */

    public void registerAbsence(int start, int end) throws OperationNotAllowedException {
        if (!isDateValid(start) || !isDateValid(end)) {
            throw new OperationNotAllowedException("Error: Date not valid!");
        } else if (start > end) {
            throw new OperationNotAllowedException("Start date must be before end date.");
        }

        assert hasEmployee(getUser().getUsername()) && start <= end && (!(!isDateValid(start) || !isDateValid(end))) : "Preconditions for registerAbsence";

        // Start is first day of absence, end is last day of absence
        Calendar current = new GregorianCalendar(start/10000, (start%10000)/100, (start%100));
        Calendar last    = new GregorianCalendar(end/10000, (end%10000)/100, (end%100));
        int date;

        while(!current.after(last)) {
            date = current.get(Calendar.YEAR) * 10000 +
                    current.get(Calendar.MONTH)  * 100 + current.get(Calendar.DATE) ;
            current.set(Calendar.DATE, current.get(Calendar.DATE) + 1);
            absence.setTime(user, 8, date);
            assert getAbsence().getWorkTime().get(getUser()).get(date) == 8 : "Postcondition for registerAbsence";
        }
    }

    // Add new Employee to ProjectApp
    public void addNewEmployee(Employee e) throws OperationNotAllowedException {
        if (!isCurrentUserCeo()) {
            throw new OperationNotAllowedException("Insufficient Permissions: User is not CEO.");
        } else if (e.getUsername().length() > 4) {
            throw new OperationNotAllowedException("Error: Username of Employee can't be longer than four initials.");
        } else if(hasEmployee(e.getUsername())) {
            throw new OperationNotAllowedException("Error: Employee with that username already registered.");
        }
        employees.add(e);
    }

    // Removes an Employee from ProjectApp
    public void removeEmployee(String username) throws OperationNotAllowedException {
        if (!isCurrentUserCeo()) {
            throw new OperationNotAllowedException("Insufficient Permissions: User is not CEO.");
        } else if (user.getUsername().equals(username)) {
            throw new OperationNotAllowedException("Error: User can't be removed from app.");
        }
        Employee e = getEmployee(username);

        // Remove employee from all assigned projects
        for (Project p : projects) {
            if (p.hasEmployee(e)) {
                p.removeEmployee(e);
                if (p.getPm().equals(e)) {
                    p.setPm(new Employee(""));
                }
            }
        }

        // Remove employee from app
        employees.remove(e);
    }

    public void addNewProject(int year, String name) throws OperationNotAllowedException {
        if (!isCurrentUserCeo()){
            throw new OperationNotAllowedException("Insufficient Permissions: User is not CEO.");
        } else if (year < 0) {
            throw new OperationNotAllowedException("Error: Year is below 0.");
        }

        assert isCurrentUserCeo() && year >= 0 : "Preconditions for addNewProject";

        int id = calculateID(year);
        if (name.equals("N")) {
            projects.add(new Project(id, year));
        } else {
            projects.add(new Project(id, year, name));
        }
        getProject(id).addEmployee(ceo); // Lets always have CEO on the project
        assert hasProject(id) : "Postcondition for addNewProject";
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
    public void addAssistance(Employee assistant, String name, int id) throws OperationNotAllowedException {
        Project p = getProject(id);
        if (!p.hasEmployee(user)) {
            throw new OperationNotAllowedException("Insufficient Permissions: User is not assigned to that project.");
        }
        p.addAssistant(assistant, name);
    }

    // Add Activity to Project
    public void addNewActivity(String name, int id) throws  OperationNotAllowedException {
        accessProject(id).addActivity(new Activity(name));
    }

    // Set dates of project activity
    public void setDates(int id, String name, int start, int end) throws OperationNotAllowedException {
        if (start > end) {
            throw new OperationNotAllowedException("Error: Start date must be before end date");
        }
        accessProject(id).setActivityDates(name, start, end);
    }

    public Project accessProject(int id) throws OperationNotAllowedException {
        Project project = getProject(id);
        if (!project.isPm(user)) {
            throw new OperationNotAllowedException("Insufficient Permissions: User is not PM.");
        }
        return project;
    }

    // Set PM of project with id
    public void setPM(String username, int id) throws OperationNotAllowedException {
        if (!isCurrentUserCeo()) {
            throw new OperationNotAllowedException("Insufficient Permissions: User is not CEO.");
        } else if (!hasEmployee(username)) {
            throw new OperationNotAllowedException("Insufficient Permissions: User is not an Employee.");
        }
        Project p = getProject(id);
        p.setPm(getEmployee(username));
    }

    public void setActivityName(String acName, int id, String newname) throws OperationNotAllowedException {
        accessProject(id).setActivityName(acName, newname);
    }

    public void setProjectName(int id, String newname) throws OperationNotAllowedException {
        accessProject(id).setName(newname);
    }

    public void setExpectedWt(int id, String acName, float time) throws OperationNotAllowedException {
        accessProject(id).getActivity(acName).setExpectedWT(time);
    }

    // Set worktime of one date
    public void setWorktime(int date, float time, int id, String acName) throws OperationNotAllowedException {
        Project p = getProject(id);
        if (!isDateValid(date)) {
            throw new OperationNotAllowedException("Error: Date not valid!");
        } else if (!p.hasEmployee(user) && !p.hasAssistant(user, acName)) {
            throw new OperationNotAllowedException("Insufficient Permissions: " +
                    "User is not assigned to that project or is an assistant on that activity");
        }

        assert isDateValid(date) &&  p.hasActivity(acName) && time <= 24.0 && time >= 0.0 && (p.hasEmployee(user)
                || p.hasAssistant(user, acName)) : "Preconditions for setWorktime";

        p.setActivityWorktime(acName, user, time, date);

        assert getProject(id).getActivity(acName).getWorkTime().get(getUser()).get(date).equals(time) : "Postcondition for setWorktime";
    }

    /* Login and logout*/

    public void logout() {
        this.user = null;
        sup.firePropertyChange(NotificationType.LOGOUT,null, null);
    }

    public void login(Employee e) {
        if (this.user == null) {
            this.user = e;
        }
        sup.firePropertyChange(NotificationType.LOGIN, null, null);
    }

    /* Has/is checks */

    public boolean isUserOnProject(int id) {
        return getProject(id).hasEmployee(user);
    }

    public boolean hasEmployee(String username) {
        return employees.stream().anyMatch(e -> e.getUsername().equals(username));
    }

    public boolean hasProject(int id) {
        return projects.stream().anyMatch(p -> p.getId() == id);
    }

    public boolean isUserPm(int id) {
        return getProject(id).isPm(user);
    }

    public boolean isCurrentUserCeo() {
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

    public Map<Employee, Integer> getOverlapOfEmployees(int[] dates) {
        int overlap;
        Map<Employee, Integer> acOverlap = new HashMap<>();

        for (Employee e : employees) {
            overlap = 0;
            for (Project p : projects) {
                overlap += p.computeOverlap(e, dates);
            }
            acOverlap.put(e, overlap);
        }
        return acOverlap;
    }

    public int calculateID(int year) {
        int count = 1;
        for (Project p : projects) {
            count += p.getStartYear() == year ? 1 : 0;
        }
        return (year % 100) * 10000 + count;
    }

    private boolean isDateValid(int d) {
        int month = (d%10000)/100;
        int date = d%100;
        return (month % 2 + 30) >= date&& date > 0 && month > 0 && month <= 12 && !(month == 2 && date > 28);
    }

    public void addObserver(PropertyChangeListener listener) {
        sup.addPropertyChangeListener(listener); }

    // Initialize app with some data
//    public void derpHelper() throws OperationNotAllowedException {
//        user = ceo;
//        employees.add(new Employee("jan"));
//        employees.add(new Employee("sim"));
//        employees.add(new Employee("joe"));
//        employees.add(new Employee("kim"));
//
//        addNewProject(2020, "Project1");
//        addNewProject(2020, "Project2");
//        addNewProject(2020, "Project3");
//        addNewProject(2020, "Project4");
//        addNewProject(2020, "Project5");
//
//        getProject(200001).addActivity(new Activity("ac1"));
//        getProject(200001).addActivity(new Activity("ac2"));
//        getProject(200002).addActivity(new Activity("ac1"));
//        getProject(200002).addActivity(new Activity("ac2"));
//        getProject(200003).addActivity(new Activity("ac1"));
//        getProject(200003).addActivity(new Activity("ac2"));
//        getProject(200004).addActivity(new Activity("ac1"));
//        getProject(200004).addActivity(new Activity("ac2"));
//        getProject(200005).addActivity(new Activity("ac1"));
//        getProject(200005).addActivity(new Activity("ac2"));
//
//        setPM("jan", 200001);
//        setPM("sim", 200002);
//        setPM("kim", 200003);
//        setPM("joe", 200005);
//
//        addAssistance(getEmployee("sim"),"ac1", 200001);
//
//        // Set some start and end dates
//        getProject(200001).getActivity("ac1").setStart(200101);
//        getProject(200001).getActivity("ac1").setEnd(200201);
//
//        getProject(200002).getActivity("ac1").setStart(200301);
//        getProject(200002).getActivity("ac1").setEnd(200401);
//
//        getProject(200003).getActivity("ac1").setStart(200115);
//        getProject(200003).getActivity("ac1").setEnd(200220);
//
//        getProject(200004).getActivity("ac1").setStart(200120);
//        getProject(200004).getActivity("ac1").setEnd(200205);
//
//        getProject(200005).getActivity("ac1").setStart(200220);
//        getProject(200005).getActivity("ac1").setEnd(200330);
//
//        user = null;
//    }
}
