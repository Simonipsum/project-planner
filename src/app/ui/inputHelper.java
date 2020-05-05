package app.ui;

import app.model.ProjectApp;

import java.util.Scanner;

public class inputHelper {
    private static inputHelper instance;
    private Scanner consoleIn = new Scanner(System.in);
    private ProjectApp app;

    private inputHelper() {
        this.app = ProjectApp.getInstance();
    }

    public static inputHelper getInstance() {
        if (instance == null) {
            instance = new inputHelper();
        }
        return instance;
    }

    int pickItem(int maxPick) {
        System.out.print("Enter item number: ");
        int pick = getInt();
        while (pick < 0 || pick > maxPick) {
            System.out.printf("Error: Item %d is not on the list.\n" +
                    "Please try again: ", pick);
            pick = getInt();
        }
        System.out.println("");
        return pick;
    }

    int[] getDates() {
        int[] dates = {0, 0};
        System.out.println("Start date");
        dates[0] = getDate();
        System.out.println("End date");
        dates[1] = getDate();
        while(dates[1] < dates[0]) {
            System.out.print("Error: End date can't be before start date.\n" +
                    "Please enter new end date: ");
            dates[1] = getDate();
        }
        return dates;
    }

    int getDate() {
        int year, month, day;
        System.out.print("Enter year: ");
        year = getPosInt();

        // Get month
        System.out.print("Enter month (1-12): ");
        month = getInt(1, 12);

        // Get day of month
        if (month == 2) {
            System.out.print("Enter day of month (1-28): ");
            day = getInt(1, 28);
        } else if (month % 2 == 0){
            System.out.print("Enter day of month (1-30): ");
            day = getInt(1, 30);
        } else {
            System.out.print("Enter day of month (1-31): ");
            day = getInt(1, 31);

        }
        return (year%100)*10000 + month*100 + day;
    }

    int getInt(int min, int max) {
        int input = getInt();
        while (input > max || input < min) {
            System.out.printf("Error: Input %d in range %d-%d.\n" +
                    "Please input integer in range: ", input, min, max);
            input = getInt();
        }
        return input;
    }

    float getPosFloat() {
        float input = getFloat();
        while (input < 0) {
            System.out.print("Error: Input float %f has to be positive.\n" +
                    "Please enter a positive float: ");
            input = getFloat();
        }
        return input;
    }

    private int getPosInt() {
        int input = getInt();
        while (input < 0) {
            System.out.print("Error: Input integer has to be positive.\n" +
                    "Please enter a positive integer: ");
            input = getInt();
        }
        return input;
    }

    private float getFloat() {
        while (!consoleIn.hasNextFloat()) {
            consoleIn.next();
            System.out.print("Error: Input must be a float.\n" +
                    "Please a float: ");
        }
        return consoleIn.nextFloat();
    }

    int getInt() {
        while (!consoleIn.hasNextInt()) {
            consoleIn.next();
            System.out.print("Error: Input must be an integer.\n" +
                    "Please enter an integer: ");
        }
        return consoleIn.nextInt();
    }

    boolean getConfirmation() {
        System.out.print("Are you sure (y/n)? ");
        String ch = getString();
        while (!ch.equals("y") && !ch.equals("n")) {
            System.out.print("Error: Didn't type 'y' or 'n'.\n" +
                    "Please enter either 'y' or 'n': ");
            ch = getString();
        }
        return ch.equals("y");
    }

    String getInitials(int maxLength) {
        String input = "";

        while (true) {
            input = consoleIn.next().toLowerCase();

            if (input.length() <= maxLength) {
                return input;
            }
            System.out.printf("Error: String '%s' too long.\n" +
                    "Please enter a string of maximum %d length:", input, maxLength);
        }
    }

    String getString() {
        while (!consoleIn.hasNext()) {
            consoleIn.next();
        }
        return consoleIn.next();
    }

    String pickActivity(int id) {
        System.out.print("Enter name of activity: ");
        String name = getString();

        while(!app.getProject(id).hasActivity(name)) {
            System.out.printf("Error: Activity %s is not on project %d.\n" +
                    "Please pick one of the listed activities: ", name, id);
            name = getString();
        }
        System.out.println("");
        return name;
    }

    float getWorkHours() {
        float hours = getPosFloat();
        while (hours > 24) {
            System.out.printf("Error: Can't work %.1f hours on a single day.\n" +
                    "Please enter a number below 24: ", hours);
            hours = getPosFloat();
        }
        return hours;
    }

}
