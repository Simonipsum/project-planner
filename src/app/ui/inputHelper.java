package app.ui;

import app.model.ProjectApp;

import java.util.Scanner;

public class inputHelper {
    private Scanner consoleIn = new Scanner(System.in);
    private ProjectApp model;

    public inputHelper(ProjectApp model) {
        this.model = model;
    }

    int pickItem(int maxPick) {
        System.out.print("Enter item number: ");
        int pick = getInt();
        while (pick < 0 || pick > maxPick) {
            System.out.print("Please pick one of the listed options.");
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
            System.out.print("End date can't be before start date: ");
            dates[1] = getDate();
        }
        return dates;
    }

    int getDate() {
        int year, month, day;
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

    int getInt(int min, int max) {
        int input = getInt();
        while (input > max || input < min) {
            System.out.println("Input not in range " + min + "-" + max + ". Enter new: ");
            input = getInt();
        }
        return input;
    }

    float getPosFloat() {
        float input = getFloat();
        while (input < 0) {
            System.out.print("Input float has to be positive. Enter new: ");
            input = getFloat();
        }
        return input;
    }

    private int getPosInt() {
        int input = getInt();
        while (input < 0) {
            System.out.print("Input integer has to be positive. Enter new: ");
            input = getInt();
        }
        return input;
    }

    private float getFloat() {
        while (!consoleIn.hasNextFloat()) {
            consoleIn.next();
            System.out.print("Input must be a float. Enter new float:");
        }
        return consoleIn.nextFloat();
    }

    int getInt() {
        while (!consoleIn.hasNextInt()) {
            consoleIn.next();
            System.out.print("Input must be an integer. Enter new integer: ");
        }
        return consoleIn.nextInt();
    }

    String getInitials(int maxLength) {
        String input = "";

        while (true) {
            input = consoleIn.next().toLowerCase();

            if (input.length() <= maxLength) {
                return input;
            }
            System.out.print("Please enter a maximum of " + maxLength + " initials.\n");
        }
    }

    String getString() {
//        while (!consoleIn.hasNext()) {
//            consoleIn.next();
//        }
        return consoleIn.next();
    }

    String pickActivity(int id) {
        System.out.print("Enter name of activity: ");
        String name = getString();
        while(!model.getProject(id).hasActivity(name)) {
            System.out.print("Activity " + name + " does not exist. Pick another: ");
            name = getString();
        }
        System.out.println("");
        return name;
    }
}
