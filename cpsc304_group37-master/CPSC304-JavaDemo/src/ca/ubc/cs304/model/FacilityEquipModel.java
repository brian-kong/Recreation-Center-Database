package ca.ubc.cs304.model;

public class FacilityEquipModel extends FacilityModel {
    private Integer equipmentID;
    private final String facilityName;
    private Integer isMaintanenceRequired;


    public FacilityEquipModel(Integer equipmentID, Integer isMaintanenceRequired, String faciltyName) {
        super(faciltyName);
        this.equipmentID = equipmentID;
        this.isMaintanenceRequired = isMaintanenceRequired;
        this.facilityName = faciltyName;

    }

    public Integer getEquipmentID () {
        return equipmentID;
    }

    public Integer getIsMaintanenceRequired() {
        return isMaintanenceRequired;
    }

    public String getFacilityName() {
        return facilityName;
    }


}
