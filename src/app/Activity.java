package app;

import java.util.Map;

public class Activity {
    private static String name;
    private static String start;
    private static String end;
    private static float expectedWorkTime;
    private static Map<Employee, Map<String, Float>> workTime;

    public Activity(String name) {
        Activity.name = name;
    }


    public void setTime(Employee e, float time, String date) {
        // check first if instance exists. use put if doesn't, use replace if it does.
        Map<String, Float> dateTime = null;
        dateTime.put(date, time);
        workTime.put(e, dateTime);
    }

    // Add time to already time set activity
    public void addTime(Employee e, float time, String date) {
        Map<String, Float> dateTime = workTime.get(e);
        dateTime.put(date, dateTime.get(date) + time);
        workTime.put(e, dateTime);
    }

    public float getRemainingWT() {
        float wt = 0;

        return wt;
    }

    public static void setEnd(String end) {
        Activity.end = end;
    }

    public static void setStart(String start) {
        Activity.start = start;
    }

    public static String getName() {
        return name;
    }

    public static String getEnd() {
        return end;
    }

    public static String getStart() {
        return start;
    }

    public void setName(String name) {
        Activity.name = name;
    }

    public void setExpectedWT(float time) {
        expectedWorkTime = time;
    }
}
