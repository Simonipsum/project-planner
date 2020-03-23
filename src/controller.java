import java.util.Scanner;

public class controller {
    static Scanner consoleIn = new Scanner(System.in);

    public static int pickItem(int maxPick) {
        System.out.print("Enter item number: ");
        int pick = getInputInt();
        while (pick < 1 || pick > maxPick) {
            System.out.println("Please pick one of the listed options.");
            pick = getInputInt();
        }
        System.out.println("");
        return pick;
    }

    public static int getInputInt() {
        while (!consoleIn.hasNextInt()) {
            consoleIn.next();
            System.out.print("Input must be an integer. Enter new integer:");
        }
        return consoleIn.nextInt();
    }

    public static String getInitials(int maxLength) {
        String input = "";
        boolean getInput = true;

        while (getInput) {
            System.out.print("Please input initials to login: ");
            input = consoleIn.next().toLowerCase();

            if (input.length() > maxLength) {
                System.out.printf("Please enter a maximum of %d initials.\n", maxLength);
            } else {
                getInput = false;
            }
        }
        return input;
    }

    public static String getString() {
        while (!consoleIn.hasNext()) {
            consoleIn.next();
        }
        return consoleIn.next();
    }

}
