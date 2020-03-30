package app;

public class Employee {
    private static String username;

    public Employee(String username) {
        this.username = username;
    }

    public static String getUsername() {
        return username;
    }

}
