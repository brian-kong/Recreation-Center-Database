package ca.ubc.cs304.model;

import java.sql.Date;

/**
 * The intent for this class is to update/store information about a single event
 */
public class EventModel {
	private String eventName;
	private Date eventDate;
	private String theme;
	private String location;
	
	// events table
	public EventModel(String eventName, Date eventDate, String theme) {
		this.eventName = eventName;
		this.eventDate = eventDate;
		this.theme = theme;
	}

	// eventLocation table
	public EventModel(String eventName, String location) {
		this.eventName = eventName;
		this.location = location;
	}

	public String getEventName() {
		return eventName;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public String getTheme() {
		return theme;
	}

	public String getLocation() {
		return location;
	}
}
