package ca.ubc.cs304.model;

/**
 * The intent for this class is to update/store information about a single fitness class
 */
public class FitnessClassModel {
    private String name;
    private String dayOfTheWeek;
    private String location;
    private String timeslot;
    private int instructorID;

    // Table: fitnessclass
    public FitnessClassModel(String name, String location) {
        this.name = name;
        this.location = location;
    }

    // Table: fitnessclassinfo
    public FitnessClassModel(String name, String timeslot, String dayOfTheWeek, int instructorID) {
        this.name = name;
        this.timeslot = timeslot;
        this.dayOfTheWeek = dayOfTheWeek;
        this.instructorID = instructorID;
    }

    public String getDay() {
        return dayOfTheWeek;
    }

    public String getLocation() {
        return location;
    }

    public int getInstructor() {
        return instructorID;
    }

    public String getName() {
        return name;
    }

    public String getTimeslot() {
        return timeslot;
    }
}