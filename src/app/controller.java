package app;

import java.util.Scanner;

public class controller {
    static Scanner consoleIn = new Scanner(System.in);

    public static int pickItem(int maxPick) {
        System.out.print("Enter item number: ");
        int pick = getInt();
        while (pick < 0 || pick > maxPick) {
            System.out.print("Please pick one of the listed options.");
            pick = getInt();
        }
        System.out.println("");
        return pick;
    }

    public static int[] getDates() {
        int[] dates = {0, 0};
        System.out.print("Enter start date: ");
        dates[0] = getDate();
        System.out.print("Enter end date: ");
        dates[1] = getDate();
        while(dates[1] < dates[0]) {
            System.out.print("End date can't be before start date: ");
            dates[1] = getDate();
        }
        return dates;
    }

    public static int getDate() {
        int year, month, day, date;
        System.out.print("Enter year: ");
        year = getInt(1900, 2999);

        // Get month
        System.out.print("Enter month (1-12): ");
        month = getInt(1, 12);

        // Get day of month
        if (month == 2) {
            System.out.print("Enter day of month (1-29): ");
            day = getInt(1, 29);
        } else if (month % 2 == 0){
            System.out.print("Enter day of month (1-30): ");
            day = getInt(1, 30);
        } else {
            System.out.print("Enter day of month (1-31): ");
            day = getInt(1, 31);

        }

        return (year%100)*10000 + month*100 + day;
    }

    public static int getInt(int min, int max) {
        int input = getInt();
        while (input > max || input < min) {
            System.out.printf("Input not in range %d-%d. Enter new: ", min, max);
            input = getInt();
        }
        return input;
    }

    public static float getPosFloat() {
        float input = getFloat();
        while (input < 0) {
            System.out.print("Input float has to be positive. Enter new: ");
            input = getFloat();
        }
        return input;
    }

    public static int getPosInt() {
        int input = getInt();
        while (input < 0) {
            System.out.print("Input integer has to be positive. Enter new: ");
            input = getInt();
        }
        return input;
    }

    public static float getFloat() {
        while (!consoleIn.hasNextFloat()) {
            consoleIn.next();
            System.out.print("Input must be a float. Enter new float:");
        }
        return consoleIn.nextFloat();
    }

    public static int getInt() {
        while (!consoleIn.hasNextInt()) {
            consoleIn.next();
            System.out.print("Input must be an integer. Enter new integer:");
        }
        return consoleIn.nextInt();
    }

    public static String getInitials(int maxLength) {
        String input = "";

        while (true) {
            input = consoleIn.next().toLowerCase();

            if (input.length() <= maxLength) {
                return input;
            }
            System.out.printf("Please enter a maximum of %d initials.\n", maxLength);
        }
    }

    public static String getString() {
        while (!consoleIn.hasNext()) {
            consoleIn.next();
        }
        return consoleIn.next();
    }
}
