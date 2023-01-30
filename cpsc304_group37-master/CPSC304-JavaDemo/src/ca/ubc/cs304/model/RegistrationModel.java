package ca.ubc.cs304.model;

/**
 * The intent for this class is to update/store information about a single fitness class registration instance
 */
public class RegistrationModel {
    private String name;
    private String dayOfTheWeek;
    private int customerID;

    public RegistrationModel(int customerID, String name, String dayOfTheWeek) {
        this.customerID = customerID;
        this.name = name;
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getName() {
        return name;
    }

    public String getDay() {
        return dayOfTheWeek;
    }
}
