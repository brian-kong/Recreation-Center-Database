package ca.ubc.cs304.model;

import java.sql.Date;

/**
 * The intent for this class is to update/store information about a single participation instance
 */
public class ParticipationModel {
	private String eventName;
	private Date eventDate;
	private int customerID;
	
	public ParticipationModel(String eventName, Date eventDate, int customerID) {
		this.eventName = eventName;
		this.eventDate = eventDate;
		this.customerID = customerID;
	}

	public String getEventName() {
		return eventName;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public int getCustomerID() {
		return customerID;
	}
}
