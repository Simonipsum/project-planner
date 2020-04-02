package app;

import java.util.ArrayList;
import java.util.List;

public class Project {
    private int id;
    private String name;
    private int startYear;
    private Employee pm = new Employee("NONE");
    private List<Employee> employees = new ArrayList<>();
    private List<Activity> activities = new ArrayList<>();

    public Project(int id, int startYear) {
        this.id = id;
        this.startYear = startYear;
    }

    public Project(int id, int startYear, String name) {
        this.id = id;
        this.name = name;
        this.startYear = startYear;
    }


    public void setDates(String name, int start, int end) {
        if (start < end) {
            Activity ac = getActivity(name);
            ac.setStart(start);
            ac.setEnd(end);
        } else {
            // throw some kind of exception
        }
    }

    public int[] getDates(String name) {
        Activity ac = getActivity(name);
        return new int[]{ac.getStart(), ac.getEnd()};
    }

    public void setName(String name, String newname) {
        if (hasActivity(name)) {
            getActivity(name).setName(newname);
        } else {
            // throw some shit
        }
    }

    public boolean hasEmployee(String name) {
        return this.employees.stream().anyMatch(e -> e.getUsername().equals(name));
    }

    public boolean hasActivity(String name) {
        return this.activities.stream().anyMatch(a -> a.getName().equals(name));
    }

    public void addEmployee(Employee e) {
        if (!hasEmployee(e.getUsername())) {
            this.employees.add(e);
        }
    }

    public void addActivity(Activity a) {
        if (!hasActivity(a.getName())) {
            this.activities.add(a);
        }
    }

    public void setPm(Employee e) {
        addEmployee(e);
        this.pm = e;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public int getStartYear() {
        return this.startYear;
    }

    public List<Employee> getEmployees() {
        return this.employees;
    }

    public List<Activity> getActivities() {
        return this.activities;
    }

    public Activity getActivity(String n) {
        return activities.stream().filter(a -> a.getName().equals(n)).findFirst().get();
    }

    public Employee getPm() {
        return this.pm;
    }

    public String getName() {
        return this.name;
    }
}
