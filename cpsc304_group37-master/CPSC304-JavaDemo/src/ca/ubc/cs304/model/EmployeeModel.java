package ca.ubc.cs304.model;

/**
 * The intent for this class is to update/store information about a single employee
 */
public class EmployeeModel {
	protected int employeeID;
	
	public EmployeeModel(int employeeID) {
		this.employeeID = employeeID;
	}

	public int getEmployeeID() {
		return employeeID;
	}
}
