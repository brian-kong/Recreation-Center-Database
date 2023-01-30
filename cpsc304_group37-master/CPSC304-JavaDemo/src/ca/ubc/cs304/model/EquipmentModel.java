package ca.ubc.cs304.model;

public class EquipmentModel extends FacilityModel {
    private Integer equipmentID;
    private final String facilityName;
    private Integer isMaintanenceRequired;
    private String type;

    public EquipmentModel(Integer equipmentID, String type, Integer isMaintanenceRequired, String faciltyName) {
        super(faciltyName);
        this.equipmentID = equipmentID;
        this.isMaintanenceRequired = isMaintanenceRequired;
        this.facilityName = faciltyName;
        this.type = type;
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

    public String getType() {
        return type;
    }

}
