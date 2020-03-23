public class Project {
    private static int id;
    private static String name;
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

    public void setName(String name) {
        this.name = name;
    }

    public static int getId() {
        return id;
    }

    public static String getName() {
        return name;
    }

    public static int getStartYear() {
        return startYear;
    }


}
