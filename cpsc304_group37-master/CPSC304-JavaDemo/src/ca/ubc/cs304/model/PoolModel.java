package ca.ubc.cs304.model;

public class PoolModel extends FacilityModel {
    final private String facilityName;
    private Integer numOfPools;
    private Integer isSaunaAvailable;

    public PoolModel(Integer numOfPools, Integer isSaunaAvailable, String facilityName) {
        super(facilityName);
        this.numOfPools = numOfPools;
        this.isSaunaAvailable = isSaunaAvailable;
        this.facilityName = facilityName;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public Integer getNumOfPools() {
        return numOfPools;
    }

    public Integer getIsSaunaAvailable() {
        return isSaunaAvailable;
    }
}
