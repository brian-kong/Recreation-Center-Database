package ca.ubc.cs304.model;

/**
 * The intent for this class is to update/store information about a single manager
 */
public class ManagerModel extends EmployeeModel {
	private int officeNum;
	
	public ManagerModel(int employeeID, int officeNum) {
		super(employeeID);
		this.officeNum = officeNum;
	}

	public int getOfficeNum() {
		return officeNum;
	}

}
