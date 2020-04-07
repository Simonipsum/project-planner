package app;

import java.util.*;

public class Activity {
    private String name;
    private int start;
    private int end;
    private float expectedWorkTime;
    private Map<Employee, Map<Integer, Float>> workTime = new HashMap<>();

    public Activity(String name) {
        this.name = name;
        this.start = -1;
        this.end = -1;
    }

    public void setTime(Employee e, float time, int date) {
        Map<Integer, Float> dateTime;

        if (workTime.containsKey(e)) {
            dateTime = workTime.get(e);
        } else {
            dateTime = new HashMap<>();
        }

        dateTime.put(date, time);
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
    public Map<Employee, Map<Integer, Float>> getWorkTime() {
        return this.workTime;
    }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setEnd(int end) { this.end = end; }
    public void setStart(int start) { this.start = start; }
    public void setExpectedWT(float time) { expectedWorkTime = time; }
}
