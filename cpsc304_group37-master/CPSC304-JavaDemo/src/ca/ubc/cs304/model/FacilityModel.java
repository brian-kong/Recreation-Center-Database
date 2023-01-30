package ca.ubc.cs304.model;

public class FacilityModel {
    protected String facilityName;
    private Integer floor;
    private Integer size;
    private String hours;

    public FacilityModel(String facilityName, Integer floor, Integer size, String hours) {
        this.facilityName = facilityName;
        this.floor = floor;
        this.size = size;
        this.hours = hours;
    }

    public FacilityModel(String facilityName, Integer floor) {
        this.facilityName = facilityName;
        this.floor = floor;
    }

    public FacilityModel(String facilityName) {
    }


    public String getFacilityName() {
        return facilityName;
    }

    public Integer getFloor() {
        return floor;
    }

    public Integer getSize() {
        return size;
    }

    public String getHours() {
        return hours;
    }
}
