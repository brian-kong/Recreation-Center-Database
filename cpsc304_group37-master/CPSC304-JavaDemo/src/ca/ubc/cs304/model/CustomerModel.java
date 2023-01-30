package ca.ubc.cs304.model;

/**
 * The intent for this class is to update/store information about a single
 * customer
 */
public class CustomerModel {
	private String name;
	private int customerID;
	private String address;
	private String postalCode;
	private String phoneNum;
	private String mType; // should this be of type membership or something?

	public CustomerModel(int customerID, String name, String address, String postalCode, String phoneNum, String mType) {
		this.name = name;
		this.customerID = customerID;
		this.address = address;
		this.postalCode = postalCode;
		this.phoneNum = phoneNum;
		if (mType.equals("")) {
			this.mType = "Adult";
		} else {
			this.mType = mType;
		}
	}

	public String getName() {
		return name;
	}
	public int getCustomerID() {
		return customerID;
	}
	public String getAddress() {
		return address;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public String getMType() {
		return mType;
	}
}
