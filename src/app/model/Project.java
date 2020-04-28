package app.model;

import app.OperationNotAllowedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Project {
    private int id;
    private String name;
    private int startYear;
    private Employee pm = new Employee("");
    private List<Employee> employees = new ArrayList<>();
    private List<Activity> activities = new ArrayList<>();
    private Map<Employee, List<Activity>> assistants = new HashMap<>();

    public Project(int id, int startYear) {
        this.id = id;
        this.startYear = startYear;
    }

    public Project(int id, int startYear, String name) {
        this.id = id;
        this.name = name;
        this.startYear = startYear;
    }

    public float getWorkedTime() {
        float wt = 0;
        for (Activity a : activities) wt += a.getWorkedTime();
        return wt;
    }

    public float getRemainingWT() {
        float rwt = 0;
        for (Activity a : activities) rwt += a.getRemainingWT();
        return rwt;
    }

    public void setActivityDates(String name, int start, int end) throws OperationNotAllowedException {
        if (start < end) {
            Activity ac = getActivity(name);
            ac.setStart(start);
            ac.setEnd(end);
        } else {
            throw new OperationNotAllowedException("Start date must be before end date");
        }
    }

    public void setActivityName(String name, String newname) throws OperationNotAllowedException {
        if (hasActivity(name)) {
            getActivity(name).setName(newname);
        } else {
            throw new OperationNotAllowedException("Project does not contain activity");
        }
    }

    public void removeEmployee(Employee e) throws OperationNotAllowedException {
        if (hasEmployee(e)) {
            employees.remove(e);
        } else {
            throw new OperationNotAllowedException("Project does not contain employee");
        }
    }

    public boolean hasEmployee(String un) {
        return this.employees.stream().anyMatch(e -> e.getUsername().equals(un));
    }

    public boolean hasEmployee(Employee e) {
        return this.employees.contains(e);
    }

    public boolean hasAssistant(Employee e){
        return assistants.containsKey(e);
    }

    public boolean hasActivity(String name) {
        return this.activities.stream().anyMatch(a -> a.getName().equals(name));
    }

    public void addEmployee(Employee e) {
        if (!hasEmployee(e)) {
            this.employees.add(e);
        }
    }

    public void addAssistant(Employee e, String name) {
        List<Activity> temp = new ArrayList<>();

        if (hasAssistant(e)) {
            if (assistants.get(e).stream().anyMatch(a -> a.getName().equals(name))) {
                temp = assistants.get(e);
                temp.add(getActivity(name));
                assistants.replace(e, temp);
            }
        } else {
            temp.add(getActivity(name));
            assistants.put(e, temp);
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

    public List<Activity> getActivities() {
        return this.activities;
    }

    public List<Activity> getAssistantActivities(Employee e) {
        return this.assistants.get(e);
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

    public boolean isPm(String username) {
        return pm.getUsername().equals(username);
    }

}
