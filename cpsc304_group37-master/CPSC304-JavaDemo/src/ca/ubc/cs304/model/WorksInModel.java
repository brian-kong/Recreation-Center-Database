package ca.ubc.cs304.model;

/**
 * The intent for this class is to update/store information about a single worksIn instance
 */
public class WorksInModel {
	private int employeeID;
	private String facilityName;

	public WorksInModel(int employeeID, String facilityName) {
		this.employeeID = employeeID;
		this.facilityName = facilityName;
	}

	public int getEmployeeID() {
		return employeeID;
	}

	public String getFacilityName() { return facilityName;}
}
