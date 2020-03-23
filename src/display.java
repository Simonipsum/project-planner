import java.sql.SQLOutput;
import java.util.*;

public class display {
    private static String[] mainMenuOptions = {
            // Normal employee options
            "1. Add/edit work time of activity\n" +
            "2. Register absence\n" +
            "3. List activities worked on\n" +
            "4. Get assistance on activity\n" +
            "5. Logout\n",

            // PM options
            "6. Edit project\n",
            // CEO options
            "6. Edit project\n" +
            "7. Register new employee\n" +
            "8. Add new project\n" +
            "9. List all projects\n",
    };
    private static String[] projectMenuOptions = {
            // PM options
            "1. Add employee\n" +
            "2. Edit activities\n" +
            "3. Check availability\n" +
            "4. See timetable of project\n",
            // CEO options
            "1. Set PM\n",
    };
    private static String[] activityMenuOptions = {
            // Employee
            "1. Add time to an activity\n",
            // PM
            "1. Add new activity\n" +
            "2. Edit start/end date\n" +
            "3. Change estimated work time\n",
    };

    public static int activityEmployee() {
        String outStr = menuHeader("Activity Menu");
        outStr += activityMenuOptions[0];
        outStr += "----------------------------------\n";
        System.out.print(outStr);
        return 1;
    }

    public static int activityPM() {
        String outStr = menuHeader("Activity Menu");
        outStr += activityMenuOptions[0];
        outStr += activityMenuOptions[1];
        outStr += "----------------------------------\n";
        System.out.print(outStr);
        return 1;
    }

    public static int mainEmployee(String user) {
        String outStr = menuHeader("Main Menu");
        outStr += mainMenuOptions[0];
        outStr += "----------------------------------\n";
        System.out.print(outStr);
        return 5;
    }

    public static int mainPM(String user) {
        String outStr = menuHeader("Main Menu");
        outStr += mainMenuOptions[0];
        outStr += mainMenuOptions[1];
        outStr += "----------------------------------\n";
        System.out.print(outStr);
        return 6;
    }

    public static int mainCEO(String user) {
        String outStr = menuHeader("Main Menu");
        outStr += mainMenuOptions[0];
        outStr += mainMenuOptions[2];
        outStr += "----------------------------------\n";
        System.out.print(outStr);
        return 9;
    }

    public static void listProjects(List<Project> projects) {
        if (projects.size() == 0) {
            System.out.println("No projects added to ProjectApp yet.");
        } else {
            System.out.println("List of projects in app");
            System.out.println("-----------------------");
            System.out.println("ID \t\t Name");
            for (Project p : projects) {
                projectInfo(p);
            }
            System.out.println("-----------------------\n");
        }
    }

    private static void projectInfo(Project p) {
        System.out.printf("%d \t\t %s\n", p.getId(), p.getName());
    }

    private static String menuHeader(String name) {
        String header = "----------------------------------\n";

        for (int i = 0; i < (16 - name.length()/2); i++) {
            header += " ";
        }
        header += name + "\n";
        header += "----------------------------------\n";
        return header;
    }

}
