package app;

import java.util.Scanner;

public class Controller {
    private Scanner consoleIn;
    private View v;

    public Controller(View v) {
        consoleIn = new Scanner(System.in);
        this.v = v;
    }

    public int pickItem(int maxPick) {
        v.print("Enter item number: ");
        int pick = getInt();
        while (pick < 0 || pick > maxPick) {
            v.print("Please pick one of the listed options.");
            pick = getInt();
        }
        v.newLine();
        return pick;
    }

    public int[] getDates() {
        int[] dates = {0, 0};
        v.println("Start date");
        dates[0] = getDate();
        v.println("End date");
        dates[1] = getDate();
        while(dates[1] < dates[0]) {
            v.print("End date can't be before start date: ");
            dates[1] = getDate();
        }
        return dates;
    }

    public int getDate() {
        int year, month, day;
        v.print("Enter year: ");
        year = getInt(1900, 2999);

        // Get month
        v.print("Enter month (1-12): ");
        month = getInt(1, 12);

        // Get day of month
        if (month == 2) {
            v.print("Enter day of month (1-29): ");
            day = getInt(1, 29);
        } else if (month % 2 == 0){
            v.print("Enter day of month (1-30): ");
            day = getInt(1, 30);
        } else {
            v.print("Enter day of month (1-31): ");
            day = getInt(1, 31);

        }

        return (year%100)*10000 + month*100 + day;
    }

    public int getInt(int min, int max) {
        int input = getInt();
        while (input > max || input < min) {
            v.print("Input not in range " + min + "-" + max + ". Enter new: ");
            input = getInt();
        }
        return input;
    }

    public float getPosFloat() {
        float input = getFloat();
        while (input < 0) {
            v.print("Input float has to be positive. Enter new: ");
            input = getFloat();
        }
        return input;
    }

    public int getPosInt() {
        int input = getInt();
        while (input < 0) {
            v.print("Input integer has to be positive. Enter new: ");
            input = getInt();
        }
        return input;
    }

    public float getFloat() {
        while (!consoleIn.hasNextFloat()) {
            consoleIn.next();
            v.print("Input must be a float. Enter new float:");
        }
        return consoleIn.nextFloat();
    }

    public int getInt() {
        while (!consoleIn.hasNextInt()) {
            consoleIn.next();
            v.print("Input must be an integer. Enter new integer: ");
        }
        return consoleIn.nextInt();
    }

    public String getInitials(int maxLength) {
        String input = "";

        while (true) {
            input = consoleIn.next().toLowerCase();

            if (input.length() <= maxLength) {
                return input;
            }
            v.print("Please enter a maximum of " + maxLength + " initials.\n");
        }
    }

    public String getString() {
        while (!consoleIn.hasNext()) {
            consoleIn.next();
        }
        return consoleIn.next();
    }
}
