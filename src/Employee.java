public class Employee {
    private static String initials;

    public Employee(String initials) {
        this.initials = initials;
    }

    public static String getInitials() {
        return initials;
    }

}
