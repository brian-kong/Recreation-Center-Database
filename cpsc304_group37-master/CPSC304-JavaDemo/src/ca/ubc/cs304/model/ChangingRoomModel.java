package ca.ubc.cs304.model;

public class ChangingRoomModel extends FacilityModel {
    final private String facilityName;
    private Integer numOfLockers;
    private Integer numOfShowers;

    public ChangingRoomModel(Integer numOfLockers, Integer numOfShowers, String facilityName) {
        super(facilityName);
        this.numOfLockers = numOfLockers;
        this.numOfShowers = numOfShowers;
        this.facilityName = facilityName;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public Integer getNumOfLockers() {
        return numOfLockers;
    }

    public Integer getNumOfShowers() {
        return numOfShowers;
    }
}
