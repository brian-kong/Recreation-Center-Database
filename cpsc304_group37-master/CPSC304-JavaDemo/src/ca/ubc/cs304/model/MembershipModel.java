package ca.ubc.cs304.model;

/**
 * The intent for this class is to update/store information about a single membership
 */
public class MembershipModel {
	private String type;
	private Float price;
	
	public MembershipModel(String type, Float price) {
		this.type = type;
		this.price = price;
	}

	public Float getPrice() {
		return price;
	}

	public String getType() {
		return type;
	}
}
