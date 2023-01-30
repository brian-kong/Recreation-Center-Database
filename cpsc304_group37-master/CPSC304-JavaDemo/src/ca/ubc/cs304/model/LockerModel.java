package ca.ubc.cs304.model;

public class LockerModel {
    private Integer isTaken;
    protected Integer lockNumber;
    private String locationName;

    public LockerModel(Integer isTaken, Integer lockNumber, String locationName) {
        this.isTaken = isTaken;
        this.lockNumber = lockNumber;
        this.locationName = locationName;
    }

    public LockerModel(Integer customerID, Integer lockNumber) {
    }

    public Integer getIsTaken() {
        return isTaken;
    }

    public Integer getLockNumber() {
        return lockNumber;
    }

    public String getLocationName(){
        return locationName;
    }
}
