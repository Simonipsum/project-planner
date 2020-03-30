package app;

import java.util.Map;

public class Activity {
    private String name;
    private String start;
    private String end;
    private float expectedWorkTime;
    private Map<Employee, Map<String, Float>> workTime;

    public Activity(String name) {
        this.name = name;
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

    public void setEnd(String end) {
        this.end = end;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getName() {
        return name;
    }

    public String getEnd() {
        return end;
    }

    public String getStart() {
        return start;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExpectedWT(float time) {
        expectedWorkTime = time;
    }
}
