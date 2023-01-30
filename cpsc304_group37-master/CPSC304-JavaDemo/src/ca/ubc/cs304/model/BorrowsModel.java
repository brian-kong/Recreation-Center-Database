package ca.ubc.cs304.model;

import java.sql.Time;

public class BorrowsModel extends LockerModel {
    private Integer customerID;
    private Integer lockNumber;
    private Time endingTime;

    public BorrowsModel(Integer customerID, Integer lockNumber, Time endingTime) {
        super(customerID, lockNumber);
        this.customerID = customerID;
        this.lockNumber = lockNumber;
        this.endingTime = endingTime;
    }

    public Integer getCustomerID () {
        return customerID;
    }

    public Integer getLockNumber() {
        return lockNumber;
    }

    public Time getEndingTime() {
        return endingTime;
    }
}
