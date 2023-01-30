package ca.ubc.cs304.model;

/**
 * The intent for this class is to update/store information about a single instructor
 */
public class InstructorModel extends EmployeeModel {
	private String expertise;

	public InstructorModel(int employeeID, String expertise) {
		super(employeeID);
		this.expertise = expertise;
	}

	public String getExpertise() {
		return expertise;
	}

}
