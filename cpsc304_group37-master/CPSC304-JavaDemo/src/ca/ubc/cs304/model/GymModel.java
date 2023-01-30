package ca.ubc.cs304.model;

public class GymModel extends FacilityModel {
    final private String facilityName;
    private Integer numOfMachines;
    private Integer numOfWeights;

    public GymModel(Integer numOfMachines, Integer numOfWeights, String facilityName) {
        super(facilityName);
        this.numOfMachines = numOfMachines;
        this.numOfWeights = numOfWeights;
        this.facilityName = facilityName;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public Integer getNumOfMachines() {
        return numOfMachines;
    }

    public Integer getNumOfWeights() {
        return numOfWeights;
    }
}
