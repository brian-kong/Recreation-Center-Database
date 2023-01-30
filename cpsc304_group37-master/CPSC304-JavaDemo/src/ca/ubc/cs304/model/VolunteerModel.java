package ca.ubc.cs304.model;

import java.sql.Date;

/**
 * The intent for this class is to update/store information about a single volunteer
 */
public class VolunteerModel extends EmployeeModel {
	private Date endingDate;

	public VolunteerModel(int employeeID, Date endingDate) {
		super(employeeID);
		this.endingDate = endingDate;
	}

	public Date getEndingDate() {
		return endingDate;
	}

}
