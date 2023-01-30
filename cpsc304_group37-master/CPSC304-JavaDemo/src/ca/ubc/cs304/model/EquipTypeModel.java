package ca.ubc.cs304.model;

public class EquipTypeModel extends FacilityModel {
    private Integer equipmentID;
    private final String facilityName;
    private String type;

    public EquipTypeModel(Integer equipmentID, String type, String faciltyName) {
        super(faciltyName);
        this.equipmentID = equipmentID;
        this.facilityName = faciltyName;
        this.type = type;
    }

    public Integer getEquipmentID () {
        return equipmentID;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public String getType() {
        return type;
    }

}
