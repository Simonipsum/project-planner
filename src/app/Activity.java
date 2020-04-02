package app;

import java.util.Map;

public class Activity {
    private String name;
    private int start;
    private int end;
    private float expectedWorkTime;
    private Map<Employee, Map<Integer, Float>> workTime;

    public Activity(String name) {
        this.name = name;
        this.start = -1;
        this.end = -1;
    }

    public void setTime(Employee e, float time, int date) {
        // check first if instance exists. use put if doesn't, use replace if it does.
        Map<Integer, Float> dateTime = null;
        dateTime.put(date, time);
        workTime.put(e, dateTime);
    }

    // Add time to already time set activity
    public void addTime(Employee e, float time, int date) {
        Map<Integer, Float> dateTime = workTime.get(e);
        dateTime.put(date, dateTime.get(date) + time);
        workTime.put(e, dateTime);
    }

    public float getRemainingWT() {
        float wt = 0;

        return wt;
    }

    // Getters
    public String getName() {
        return name;
    }
    public int getEnd() { return end; }
    public int getStart() { return start; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setEnd(int end) { this.end = end; }
    public void setStart(int start) { this.start = start; }
    public void setExpectedWT(float time) { expectedWorkTime = time; }
}
