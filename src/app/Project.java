package app;

public class Project {
    private static int id;
    private static String name;
    private static String pm;
    private static int startYear;

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

    public static int getId() { return id; }
    public static int getStartYear() { return startYear; }
    public static String getPm() { return pm; }
    public static String getName() { return name; }
}
