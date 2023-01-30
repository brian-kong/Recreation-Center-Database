package ca.ubc.cs304.model;

/**
 * The intent for this class is to update/store information about a single manages instance
 */
public class ManagesModel {
	private String mType;
	private int managerID;

	public ManagesModel(String mType, int managerID) {
		this.mType = mType;
		this.managerID = managerID;
	}

	public String getmType() { return mType;}
	public int getManagerID() {
		return managerID;
	}

}
