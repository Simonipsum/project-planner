package app.model;

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

    // Getters
    public float getRemainingWT() {
        return expectedWorkTime - getWorkedTime();
    }
    public String getName() {
        return name;
    }
    public int getEnd() { return end; }
    public int getStart() { return start; }
    public Map<Employee, Map<Integer, Float>> getWorkTime() {
        return this.workTime;
    }
    public Map<Integer, Float> getDateTime() {
        float time;
        Map<Integer, Float> dateTime = new HashMap<>();

        for (Map<Integer, Float> dt : workTime.values()) {
            for (Integer d : dt.keySet()) {
                time = dt.get(d);
                if (dateTime.containsKey(d)) {
                    time += dateTime.get(d);
                }
                dateTime.put(d, time);
            }
        }
        return dateTime;
    }
    public float getWorkedTime() {
        float wt = 0;
        Map<Integer, Float> dt = getDateTime();
        for (Float t : dt.values()) wt += t;
        return wt;
    }
    public float getExpectedWorkTime() {
        return this.expectedWorkTime;
    }

    // Setters
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
    public void setName(String name) { this.name = name; }
    public void setEnd(int end) { this.end = end; }
    public void setStart(int start) { this.start = start; }
    public void setExpectedWT(float time) { this.expectedWorkTime = time; }
}
