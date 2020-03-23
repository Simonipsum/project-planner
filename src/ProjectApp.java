import java.util.*;

public class ProjectApp {
    static Scanner consoleIn = new Scanner(System.in);

    static String CEO = "marc";
    static List<String> employees = new ArrayList<String>();
    static String user;


    public static void main(String[] args) {

        employees.add(CEO);
        System.out.println(employees);

        while(true) {
            userLogin();
            System.out.println(user);
        }
    }

    private static void userLogin() {
        String input = "";
        boolean getInput = true;

        while (getInput) {
            System.out.print("Please input initials to login: ");
            input = consoleIn.next().toLowerCase();

            if (input.length() > 4) {
                System.out.println("Please enter a maximum of four initials.");
            } else {
                if (isEmployee(input)) {
                    getInput = false;
                } else {
                    System.out.println("Is not a registered employee.");
                }
            }
        }
        user = input;
    }

    private static boolean isEmployee(String initials) {
        return employees.contains(initials);
    }
}
