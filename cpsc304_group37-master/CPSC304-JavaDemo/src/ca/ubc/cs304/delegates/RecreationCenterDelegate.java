package ca.ubc.cs304.delegates;
import java.sql.SQLException;
import java.util.List;

/**
 * This interface uses the delegation design pattern where instead of having
 * the LoginWindow class try to do everything, it will only focus on
 * handling the UI. The actual logic/operation will be delegated to the controller
 * class (in this case Bank).
 * 
 * LoginWindow calls the methods that we have listed below but
 * Bank is the actual class that will implement the methods.
 */
public interface RecreationCenterDelegate {
    public List<String> findAllTables() throws SQLException;
    public List<String[]> joinTables(String tableOne, String tableTwo) throws SQLException;
    public List<String[]> viewAllCustomers() throws SQLException;
    public List<String[]> findCustomers(String participation) throws SQLException;
    public List<String> findAllCustomerIDs() throws SQLException;
    public void deleteCustomer(String customerID) throws  SQLException;
    public void addNewCustomer(String customerID, String customerName, String customerAddress, String customerPostalCode, String customerPhoneNumber, String mType) throws SQLException;
    public void updateCustomer(String customerID, String customerName, String customerAddress, String customerPostalCode, String customerPhoneNumber, String mType) throws SQLException;

    public List<String[]> viewAllMemberships() throws SQLException;
    public List<String[]> findMembership(String maxOrMin) throws SQLException;
    public List<String> findAllMembershipTypes() throws SQLException;
    public void deleteMembership(String membershipType) throws  SQLException;
    public void addNewMembership(String membershipType, String membershipPrice) throws SQLException;
    public void updateMembership(String membershipType, String membershipPrice) throws SQLException;

    public List<String[]> viewAllEvents() throws SQLException;
    public List<String[]> viewParticipations() throws SQLException;
    public List<String> findAllEventNames() throws SQLException;
    public List<String> findAllEventDates() throws SQLException;
    public List<String> findAllLocations() throws SQLException;
    public void deleteEvent(String eventName, String eventDate) throws  SQLException;
    public void addNewEvent(String eventName, String eventDate, String eventTheme, String eventLocation) throws SQLException;
    public void updateEvent(String eventName, String eventDate, String eventTheme, String eventLocation) throws SQLException;

    public List<String[]> viewAllParticipantsForTheEvent(String eventName, String eventDate) throws SQLException;
    public void addNewEventParticipant(String eventName, String eventDate, String customerID) throws SQLException;
    public void deleteEventParticipant(String eventName, String eventDate, String customerID) throws SQLException;

    public List<String[]> viewFitnessClasses() throws SQLException;
    public List<String> findAllFitnessClassNames() throws SQLException;
    public void deleteFitnessClass(String fcName) throws  SQLException;
    public void insertFitnessClass(String fcName, String location) throws SQLException;
    public void updateFitnessClass(String newLocation, String fcName) throws SQLException;

    public List<String[]> viewDetailedClassSchedule() throws SQLException;
    public List<String> findAllFitnessClassesWithDays() throws SQLException;
    public void deleteFitnessClassInfo(String fcName, String dayOfTheWeek) throws  SQLException;
    public void insertFitnessClassInfo(String fcName, String timeslot, String dayOfTheWeek, String instructorID) throws SQLException;
    public void updateFitnessClassInfo(String fcName, String timeslot, String dayOfTheWeek, String instructorID) throws SQLException;

    public List<String[]> viewRegistrations() throws SQLException;
    public List<String[]> viewMostPopularClasses() throws SQLException;
    public List<String[]> viewAvgNumCustomersInClasses() throws SQLException;
    public void deleteRegistersIn(String customerID, String fitnessClassName, String fitnessClassDay) throws  SQLException;
    public void insertRegistersIn(String customerID, String fitnessClassName, String fitnessClassDay) throws SQLException;

    public List<String[]> viewAllFacility() throws SQLException;
    public List<String[]> selectFacility(String attributeChoice, String signInput, String numberInput) throws SQLException;
    public List<String[]> projectFacility(String selectedAttributues) throws SQLException;
    public void deleteFacility(String facilityName) throws SQLException;
    public void addNewFacility(String facilityName, String floor, String size, String hours) throws SQLException;
    public void updateFacility(String facilityName, String floor, String size, String hours) throws SQLException;

}
