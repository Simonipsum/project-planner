package app;

public class Project {
    private int id;
    private String name;
    private String pm;
    private int startYear;

    public Project(int id, int startYear) {
        this.id = id;
        this.startYear = startYear;
    }

    public Project(int id, int startYear, String name) {
        this.id = id;
        this.name = name;
        this.startYear = startYear;
    }

    public void setName(String name) { this.name = name; }
    public void setPm(String username) {this.pm = username; }

    public int getId() { return id; }
    public int getStartYear() { return startYear; }
    public String getPm() { return pm; }
    public String getName() { return name; }
}
