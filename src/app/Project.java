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

    public boolean hasEmployee(Employee e)  { return this.employees.contains(e); }
    public void addEmployee(Employee e)     { this.employees.add(e); }
    public void setPm(Employee e)           { this.pm = e; }
    public void setName(String name)        { this.name = name; }
    public int getId()                      { return this.id; }
    public int getStartYear()               { return this.startYear; }
    public List<Employee> getEmployees()    { return this.employees; }
    public Employee getPm()                 { return this.pm; }
    public String getName()                 { return this.name; }
}
