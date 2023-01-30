package ca.ubc.cs304.controller;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.LoginWindowDelegate;
import ca.ubc.cs304.delegates.RecreationCenterDelegate;
import ca.ubc.cs304.model.*;
import ca.ubc.cs304.ui.LoginWindow;
import ca.ubc.cs304.ui.RecreationCenterWindow;

/**
 * This is the main controller class that will orchestrate everything.
 */
public class RecreationCenter implements LoginWindowDelegate, RecreationCenterDelegate {
	private DatabaseConnectionHandler dbHandler = null;
	private LoginWindow loginWindow = null;
	private RecreationCenterWindow recreationCenterWindow = null;

	public RecreationCenter() {
		dbHandler = new DatabaseConnectionHandler();
	}
	
	private void start() {
		loginWindow = new LoginWindow();
		loginWindow.showFrame(this);
	}
	
	/**
	 * LoginWindowDelegate Implementation
	 * 
     * connects to Oracle database with supplied username and password
     */ 
	public void login(String username, String password) {
		boolean didConnect = dbHandler.login(username, password);

		if (didConnect) {
			// Once connected, remove login window and start text transaction flow
			loginWindow.dispose();
			recreationCenterWindow = new RecreationCenterWindow();
			recreationCenterWindow.showFrame(this);
		} else {
			loginWindow.handleLoginFailed();

			if (loginWindow.hasReachedMaxLoginAttempts()) {
				loginWindow.dispose();
				System.out.println("You have exceeded your number of allowed attempts");
				System.exit(-1);
			}
		}
	}

	public List<String> findAllTables() throws SQLException {
		return dbHandler.getAllTableNames();
	}

	public List<String[]> joinTables(String tableOne, String tableTwo) throws SQLException {
		List<String> attributesFromTables = getColumnNamesFromTables(tableOne, tableTwo);
		List<String> columnNames = removeDuplicates(attributesFromTables);
		return dbHandler.joinTables(tableOne, tableTwo, columnNames);
	}

	public List<String> removeDuplicates(List<String> list) {
		List<String> newList = new ArrayList<>();

		for (String element : list) {
			if (!newList.contains(element)) {
				newList.add(element);
			}
		}

		return newList;
	}

	public List<String> getColumnNamesFromTables(String tableOne, String tableTwo) {
		List<String> result = new ArrayList<>();
		if (tableOne.toLowerCase().equals("customer") || tableTwo.toLowerCase().equals("customer")) {
			result.add("customerID");
			result.add("cName");
			result.add("cAddress");
			result.add("postalCode");
			result.add("phoneNum");
			result.add("mType");
		}
		if (tableOne.toLowerCase().equals("membership") || tableTwo.toLowerCase().equals("membership")) {
			result.add("mType");
			result.add("price");
		}
		if (tableOne.toLowerCase().equals("events") || tableTwo.toLowerCase().equals("events")) {
			result.add("eventName");
			result.add("eventDate");
			result.add("theme");
		}
		if (tableOne.toLowerCase().equals("participatesin") || tableTwo.toLowerCase().equals("participatesin")) {
			result.add("customerID");
			result.add("eventName");
			result.add("eventDate");
		}
		if (tableOne.toLowerCase().equals("employee") || tableTwo.toLowerCase().equals("employee")) {
			result.add("employeeID");
		}
		if (tableOne.toLowerCase().equals("manager") || tableTwo.toLowerCase().equals("manager")) {
			result.add("officeNum");
			result.add("employeeID");
		}
		if (tableOne.toLowerCase().equals("manages") || tableTwo.toLowerCase().equals("manages")) {
			result.add("mType");
			result.add("managerID");
		}
		if (tableOne.toLowerCase().equals("volunteer") || tableTwo.toLowerCase().equals("volunteer")) {
			result.add("employeeID");
			result.add("endingDate");
		}
		if (tableOne.toLowerCase().equals("instructor") || tableTwo.toLowerCase().equals("instructor")) {
			result.add("employeeID");
			result.add("expertise");
		}
		if (tableOne.toLowerCase().equals("floorinfo") || tableTwo.toLowerCase().equals("floorinfo")) {
			result.add("floorNum");
			result.add("fsize");
			result.add("fhours");
		}
		if (tableOne.toLowerCase().equals("facility") || tableTwo.toLowerCase().equals("facility")) {
			result.add("facilityName");
			result.add("floorNum");
			result.add("fsize");
			result.add("fhours");
		}
		if (tableOne.toLowerCase().equals("worksin") || tableTwo.toLowerCase().equals("worksin")) {
			result.add("employeeID");
			result.add("facilityName");
		}
		if (tableOne.toLowerCase().equals("fitnessclass") || tableTwo.toLowerCase().equals("fitnessclass")) {
			result.add("fcName");
			result.add("timeslot");
			result.add("dayOfTheWeek");
			result.add("instructorID");
		}
		if (tableOne.toLowerCase().equals("eventlocation") || tableTwo.toLowerCase().equals("eventlocation")) {
			result.add("eventName");
			result.add("facilityName");
		}
		if (tableOne.toLowerCase().equals("gym") || tableTwo.toLowerCase().equals("gym")) {
			result.add("facilityName");
			result.add("numOfMachines");
			result.add("numOfWeights");
		}
		if (tableOne.toLowerCase().equals("pools") || tableTwo.toLowerCase().equals("pools")) {
			result.add("facilityName");
			result.add("numOfPools");
			result.add("isSaunaAvailable");
		}
		if (tableOne.toLowerCase().equals("changingroom") || tableTwo.toLowerCase().equals("changingroom")) {
			result.add("facilityName");
			result.add("numOfLockers");
			result.add("numOfShowers");
		}
		if (tableOne.toLowerCase().equals("equipment") || tableTwo.toLowerCase().equals("equipment")) {
			result.add("mEquipmentID");
			result.add("isMaintanceRequired");
		}
		if (tableOne.toLowerCase().equals("equiptype") || tableTwo.toLowerCase().equals("equiptype")) {
			result.add("facilityName");
			result.add("etype");
		}
		if (tableOne.toLowerCase().equals("facilityequip") || tableTwo.toLowerCase().equals("facilityequip")) {
			result.add("equipmentID");
			result.add("facilityName");
		}
		if (tableOne.toLowerCase().equals("registersin") || tableTwo.toLowerCase().equals("registersin")) {
			result.add("customerID");
			result.add("fitnessClassName");
			result.add("fitnessClassDay");
		}
		if (tableOne.toLowerCase().equals("locker") || tableTwo.toLowerCase().equals("locker")) {
			result.add("lockNumber");
			result.add("isTaken");
			result.add("locationName");
		}
		if (tableOne.toLowerCase().equals("borrows") || tableTwo.toLowerCase().equals("borrows")) {
			result.add("customerID");
			result.add("lockNumber");
			result.add("endingTime");
		}
		return result;
	}

	public List<String[]> viewAllCustomers() throws SQLException {
		return dbHandler.getCustomerInfo();
	}

	public List<String[]> findCustomers(String participation) throws SQLException {
		if (participation.equals("All of the Events")) {
			return dbHandler.findCustomersParticipatingInAllEvents();
		} else {
			return dbHandler.findCustomersNotParticipatingAnywhere();
		}
	}


	public List<String> findAllCustomerIDs() throws SQLException {
		return dbHandler.getCustomerIDs();
	}

	public void deleteCustomer(String customerID) throws SQLException {
		int integerCustomerID = Integer.parseInt(customerID);
		dbHandler.deleteCustomer(integerCustomerID);
	}

	public void addNewCustomer(String customerID, String customerName, String customerAddress, String customerPostalCode, String customerPhoneNumber, String mType) throws SQLException {
		CustomerModel model = new CustomerModel(Integer.parseInt(customerID), customerName, customerAddress, customerPostalCode, customerPhoneNumber, mType);
		dbHandler.insertCustomer(model);
	}

	public void updateCustomer(String customerID, String customerName, String customerAddress, String customerPostalCode, String customerPhoneNumber, String mType) throws SQLException {
		CustomerModel model = new CustomerModel(Integer.parseInt(customerID), customerName, customerAddress, customerPostalCode, customerPhoneNumber, mType);
		dbHandler.updateCustomer(model);
	}

	public List<String[]> viewAllMemberships() throws SQLException {
		return dbHandler.getMembershipInfo();
	}

	public List<String[]> findMembership(String maxOrMin) throws SQLException {
		if (maxOrMin.equals("max")) {
			return dbHandler.findMaxMembershipPrice();
		} else {
			return dbHandler.findMinMembershipPrice();
		}
	}


	public List<String> findAllMembershipTypes() throws SQLException {
		return dbHandler.getMembershipTypes();
	}

	public void deleteMembership(String membershipType) throws  SQLException {
		dbHandler.deleteMembership(membershipType);
	}

	public void addNewMembership(String membershipType, String membershipPrice) throws SQLException {
		MembershipModel model = new MembershipModel(membershipType, Float.parseFloat(membershipPrice));
		dbHandler.insertMembership(model);
	}

	public void updateMembership(String membershipType, String membershipPrice) throws SQLException {
		MembershipModel model = new MembershipModel(membershipType, Float.parseFloat(membershipPrice));
		dbHandler.updateMembership(model);
	}

	public List<String[]> viewAllEvents() throws SQLException {
		return dbHandler.getEventInfo();
	}

	public List<String[]> viewParticipations() throws SQLException {
		return dbHandler.getParticipationsInfo();
	}

	public List<String> findAllEventNames() throws SQLException {
		return dbHandler.getEventNames();
	}

	public List<String> findAllEventDates() throws SQLException {
		return dbHandler.getEventDates();
	}

	public List<String> findAllLocations() throws SQLException {
		return dbHandler.getAllLocations();
	}

	public void deleteEvent(String eventName, String eventDate) throws  SQLException {
		dbHandler.deleteEvent(eventName, eventDate);
	}

	public void addNewEvent(String eventName, String eventDate, String eventTheme, String eventLocation) throws SQLException {
		EventModel eventsTable = new EventModel(eventName, Date.valueOf(eventDate), eventTheme);
		EventModel eventLocationsTable = new EventModel(eventName, eventLocation);
		dbHandler.insertEvent(eventsTable);
		dbHandler.insertEventLocation(eventLocationsTable);
	}

	public void updateEvent(String eventName, String eventDate, String eventTheme, String eventLocation) throws SQLException {
		dbHandler.updateEvent(eventName, eventDate, eventTheme);
		dbHandler.updateEventLocation(eventName, eventLocation);
	}

	public List<String[]> viewAllParticipantsForTheEvent(String eventName, String eventDate) throws SQLException {
		return dbHandler.getParticipantsInfo(eventName, eventDate);
	}

	public void addNewEventParticipant(String eventName, String eventDate, String customerID) throws SQLException {
		dbHandler.insertParticipatesIn(eventName, eventDate, customerID);
	}

	public void deleteEventParticipant(String eventName, String eventDate, String customerID) throws SQLException {
		dbHandler.deleteParticipatesIn(eventName, eventDate, customerID);
	}

	// Facility

	public List<String[]> viewAllFacility() throws SQLException {
		return dbHandler.getFacilityInfo();
	}

	public List<String[]> selectFacility(String attributeChoice, String signInput, String numberInput) throws SQLException {
		if (attributeChoice.equals("floor")) {
			return dbHandler.selectFacility("floorNum", signInput, numberInput);
		} else {
			return dbHandler.selectFacility("fsize", signInput, numberInput);
		}
	}

	public List<String[]> projectFacility(String selectedAttributues) throws SQLException {
		return dbHandler.projectFacilityAttributes(selectedAttributues);
	}

	public void deleteFacility(String facilityName) throws SQLException {
		dbHandler.deleteFacility(facilityName);
	}

	public void addNewFacility(String facilityName, String floor, String size, String hours) throws SQLException {
		FacilityModel facilityTable = new FacilityModel(facilityName, Integer.parseInt(floor), Integer.parseInt(size), hours);
		dbHandler.insertFacility(facilityTable);
	}

	public void updateFacility(String facilityName, String floor, String size, String hours) throws SQLException {
	    FacilityModel model = new FacilityModel(facilityName, Integer.parseInt(floor), Integer.parseInt(size), hours);
		dbHandler.updateFacility(model);
	}


	//----------------------------------
	public void insertFitnessClass (String fcName, String location) throws SQLException {
		FitnessClassModel model = new FitnessClassModel(fcName, location);
		dbHandler.insertFitnessClass(model);}
	public void insertFitnessClassInfo(String fcName, String timeslot, String dayOfTheWeek, String instructorID) throws SQLException {
		FitnessClassModel model = new FitnessClassModel(fcName, timeslot, dayOfTheWeek, Integer.parseInt(instructorID));
		dbHandler.insertFitnessClassInfo(model);}
	public void insertRegistersIn(String customerID, String fitnessClassName, String fitnessClassDay) throws SQLException {
		RegistrationModel model = new RegistrationModel(Integer.parseInt(customerID), fitnessClassName, fitnessClassDay);
		dbHandler.insertRegistersIn(model);
	}
	public void deleteFitnessClass(String fcName) throws SQLException {dbHandler.deleteFitnessClass(fcName);}
	public void deleteFitnessClassInfo(String fcName, String dayOfTheWeek) throws SQLException {dbHandler.deleteFitnessClassInfo(fcName, dayOfTheWeek);}
	public void deleteRegistersIn(String customerID, String fitnessClassName, String fitnessClassDay) throws SQLException {
		dbHandler.deleteRegistersIn(Integer.parseInt(customerID), fitnessClassName, fitnessClassDay);
	}
	public void updateFitnessClass(String newLocation, String fcName) throws SQLException {dbHandler.updateFitnessClass(newLocation, fcName);}
	public void updateFitnessClassInfo(String fcName, String timeslot, String dayOfTheWeek, String instructorID) throws SQLException {
	    FitnessClassModel model = new FitnessClassModel(fcName, timeslot, dayOfTheWeek, Integer.parseInt(instructorID));
	    dbHandler.updateFitnessClassInfo(model);}

    public List<String[]> viewRegistrations() throws SQLException {
	    return dbHandler.getRegistrationInfo();
    }
	public List<String[]> viewMostPopularClasses() throws SQLException {
		return dbHandler.getMostPopularClasses();
	}
	public List<String[]> viewAvgNumCustomersInClasses() throws SQLException {
		return dbHandler.getAvgNumCustomers();
	}
	public List<String[]> viewDetailedClassSchedule() throws SQLException {
		return dbHandler.getClassSchedule();
	}
    public List<String[]> viewFitnessClasses() throws SQLException {
        return dbHandler.getFitnessClassInfo();
    }
	public List<String> findAllFitnessClassNames() throws SQLException {
		return dbHandler.getFitnessClassNames();
	}
	public List<String> findAllFitnessClassesWithDays() throws SQLException {
		return dbHandler.getFitnessClassesWithDays();
	}




	public void insertEmployee(EmployeeModel model) {dbHandler.insertEmployee(model);}
	public void insertManager(ManagerModel model) {dbHandler.insertManager(model);}
	public void insertInstructor(InstructorModel model) {dbHandler.insertInstructor(model);}
	public void insertVolunteer(VolunteerModel model) {dbHandler.insertVolunteer(model);}

	public void insertManages(ManagesModel model) {dbHandler.insertManages(model);}
	public void insertWorksIn(WorksInModel model) {dbHandler.insertWorksIn(model);}
	public void insertGym(GymModel model) {dbHandler.insertGym(model);}
	public void insertPool(PoolModel model) {dbHandler.insertPool(model);}
	public void insertChangingRoom(ChangingRoomModel model) {dbHandler.insertChangingRoom(model);}
	public void insertFloorInfo(FacilityModel model) {dbHandler.insertFloorInfo(model);}
	public void insertEquipment(EquipmentModel model) {dbHandler.insertEquipment(model);}
	public void insertFacilityEquip(FacilityEquipModel model) {dbHandler.insertFacilityEquip(model);}
	public void insertEquipType(EquipTypeModel model) {dbHandler.insertEquipType(model);}
	public void insertLockers(LockerModel model) {dbHandler.insertLockers(model);}
	public void insertBorrowers(BorrowsModel model) {dbHandler.insertBorrowers(model);}

	public void deleteEmployee(int employeeID) {dbHandler.deleteEmployee(employeeID);}
	public void deleteManages(String mType, int managerID) {dbHandler.deleteManages(mType, managerID);}
	public void deleteWorksIn(int employeeID, String fcName) {dbHandler.deleteWorksIn(employeeID, fcName);}
	public void deleteGym(String facilityName) {dbHandler.deleteGym(facilityName);}
	public void deletePool(String facilityName) {dbHandler.deletePool(facilityName);}
	public void deleteChangingRoom(String facilityName) {dbHandler.deleteChangingRoom(facilityName);}
	public void deleteFloorInfo (Integer floor, String facilityName) {dbHandler.deleteFloorInfo(floor,facilityName);}
	public void deleteEquipment (Integer equipmentID) {dbHandler.deleteEquipment(equipmentID);}
	public void deleteFacilityEquipment (Integer equipmentID, String facilityName) {dbHandler.deleteFacilityEquipment(equipmentID, facilityName);}
    public void deleteEquipType (String facilityName) {dbHandler.deleteEquipType(facilityName);}
	public void deleteLocker(Integer lockNumber, String locationName) {dbHandler.deleteLocker(lockNumber, locationName);}
	public void deleteBorrows(Integer lockNumber, Integer customerID) {dbHandler.deleteBorrows(lockNumber, customerID);}

	public void updateEmployee(int newID, int currentID) {dbHandler.updateEmployee(newID, currentID);}
	public void updateManager(int newOfficeNum, int currentID) {dbHandler.updateManager(newOfficeNum, currentID);}
	public void updateInstructor(String newExpertise, int currentID) {dbHandler.updateInstructor(newExpertise, currentID);}
	public void updateVolunteer(Date newEndingDate, int currentID) {dbHandler.updateVolunteer(newEndingDate, currentID);}
	public void updateManages(String newType, int managerID, String currentType) {dbHandler.updateManages(newType, managerID, currentType);}
	public void updateWorksIn(String newLocation, int employeeID, String currentLocation) {dbHandler.updateWorksIn(newLocation, employeeID, currentLocation);}
	public void updateGym(Integer numOfMachines, Integer numOfWeights, String facilityName) {dbHandler.updateGym(numOfMachines, numOfWeights, facilityName); }
	public void updatePool(Integer numOfPools, Integer isSaunaAvailable, String facilityName) {dbHandler.updatePool(numOfPools, isSaunaAvailable, facilityName);}
	public void updateChangingRoom(Integer numOfLockers, Integer numOfShowers, String facilityName) {dbHandler.updateChangingRoom(numOfLockers, numOfShowers, facilityName);}
	public void updateFloor(String facilityName, Integer floor, Integer size, String hours) {dbHandler.updateFloor(facilityName, floor, size, hours);}
	public void updateEquipment(Integer equipmentID, String type, Integer isMaintenanceRequired, String facilityName) {dbHandler.updateEquipment(equipmentID, type, isMaintenanceRequired, facilityName);}
	public void updateFacilityEquip(Integer equipmentID, Integer isMaintenanceRequired, String facilityName) {dbHandler.updateFacilityEquip (equipmentID, isMaintenanceRequired, facilityName);}
	public void updateEquipType(Integer equipmentID, String type, String facilityName) {dbHandler.updateEquipType(equipmentID, type, facilityName);}
	public void updateLocker (Integer isTaken, Integer lockNumber, String locationName) {dbHandler.updateLocker(isTaken, lockNumber, locationName);}
	public void updateBorrower (Integer customerID, Integer lockNumber, Time endingTime) {dbHandler.updateBorrower(customerID, lockNumber, endingTime);}

	public void showEmployees() {
		EmployeeModel[] models = dbHandler.getEmployeeInfo();

		for (int i = 0; i < models.length; i++) {
			EmployeeModel model = models[i];

			// simplified output formatting; truncation may occur
			System.out.printf("%-25.25s", model.getEmployeeID());
			System.out.println();
		}
	}
	public void showManagers() {
		ManagerModel[] models = dbHandler.getManagerInfo();

		for (int i = 0; i < models.length; i++) {
			ManagerModel model = models[i];

			// simplified output formatting; truncation may occur
			System.out.printf("%-25.25s", model.getEmployeeID());
			System.out.printf("%-15.15s", model.getOfficeNum());
			System.out.println();
		}
	}
	public void showInstructors() {
		InstructorModel[] models = dbHandler.getInstructorInfo();

		for (int i = 0; i < models.length; i++) {
			InstructorModel model = models[i];

			// simplified output formatting; truncation may occur
			System.out.printf("%-25.25s", model.getEmployeeID());
			System.out.printf("%-15.15s", model.getExpertise());
			System.out.println();
		}
	}
	public void showVolunteers() {
		VolunteerModel[] models = dbHandler.getVolunteerInfo();

		for (int i = 0; i < models.length; i++) {
			VolunteerModel model = models[i];

			// simplified output formatting; truncation may occur
			System.out.printf("%-25.25s", model.getEmployeeID());
			System.out.printf("%-15.15s", model.getEndingDate());
			System.out.println();
		}
	}

	public void showManages() {
		ManagesModel[] models = dbHandler.getManagesInfo();

		for (int i = 0; i < models.length; i++) {
			ManagesModel model = models[i];

			// simplified output formatting; truncation may occur
			System.out.printf("%-25.25s", model.getmType());
			System.out.printf("%-15.15s", model.getManagerID());
			System.out.println();
		}
	}
	public void showWorksIns() {
		WorksInModel[] models = dbHandler.getWorksInInfo();

		for (int i = 0; i < models.length; i++) {
			WorksInModel model = models[i];

			// simplified output formatting; truncation may occur
			System.out.printf("%-25.25s", model.getEmployeeID());
			System.out.printf("%-15.15s", model.getFacilityName());
			System.out.println();
		}
	}

	public void showGym() {
		GymModel[] models = dbHandler.getGymInfo();

		for (int i = 0; i < models.length; i++) {
			GymModel model = models[i];

			// simplified output formatting; truncation may occur
			System.out.printf("%-25.25s", model.getNumOfMachines());
			System.out.printf("%-15.15s", model.getNumOfWeights());
			System.out.printf("%-10.10s", model.getFacilityName());
			System.out.println();
		}
	}

	public void showPool() {
		PoolModel[] models = dbHandler.getPoolInfo();

		for (int i = 0; i < models.length; i++) {
			PoolModel model = models[i];

			// simplified output formatting; truncation may occur
			System.out.printf("%-25.25s", model.getNumOfPools());
			System.out.printf("%-15.15s", model.getIsSaunaAvailable());
			System.out.printf("%-10.10s", model.getFacilityName());
			System.out.println();
		}
	}

	public void showChangingRoom() {
		ChangingRoomModel[] models = dbHandler.getChangingRoomInfo();

		for (int i = 0; i < models.length; i++) {
			ChangingRoomModel model = models[i];

			// simplified output formatting; truncation may occur
			System.out.printf("%-25.25s", model.getNumOfLockers());
			System.out.printf("%-15.15s", model.getNumOfShowers());
			System.out.printf("%-10.10s", model.getFacilityName());
			System.out.println();
		}
	}

	public void showFloorInfo() {
		FacilityModel[] models = dbHandler.getFloorInfo();

		for (int i = 0; i < models.length; i++) {
			FacilityModel model = models[i];

			// simplified output formatting; truncation may occur
			System.out.printf("%-25.25s", model.getFacilityName());
			System.out.printf("%-15.15s", model.getFloor());
			System.out.println();
		}
	}

	public void showEquipment() {
		EquipmentModel[] models = dbHandler.getEquipmentInfo();

		for (int i = 0; i < models.length; i++) {
			EquipmentModel model = models[i];

			// simplified output formatting; truncation may occur
			System.out.printf("%-25.25s", model.getEquipmentID());
			System.out.printf("%-15.15s", model.getType());
			System.out.printf("%-10.10s", model.getIsMaintanenceRequired());
			System.out.printf("%-5.5s", model.getFacilityName());
			System.out.println();
		}
	}

	public void showFacilityEquip() {
		FacilityEquipModel[] models = dbHandler.getFacilityEquipInfo();

		for (int i = 0; i < models.length; i++) {
			FacilityEquipModel model = models[i];

			// simplified output formatting; truncation may occur
			System.out.printf("%-25.25s", model.getEquipmentID());
			System.out.printf("%-15.15s", model.getIsMaintanenceRequired());
			System.out.printf("%-10.10s", model.getFacilityName());
			System.out.println();
		}
	}

	public void showEquipType() {
		EquipTypeModel[] models = dbHandler.getEquipTypeInfo();

		for (int i = 0; i < models.length; i++) {
			EquipTypeModel model = models[i];

			// simplified output formatting; truncation may occur
			System.out.printf("%-25.25s", model.getEquipmentID());
			System.out.printf("%-15.15s", model.getType());
			System.out.printf("%-10.10s", model.getFacilityName());
			System.out.println();
		}
	}

	public void showLocker() {
		LockerModel[] models = dbHandler.getLockerInfo();

		for (int i = 0; i < models.length; i++) {
			LockerModel model = models[i];

			// simplified output formatting; truncation may occur
			System.out.printf("%-25.25s", model.getIsTaken());
			System.out.printf("%-15.15s", model.getLockNumber());
			System.out.printf("%-10.10s", model.getLocationName());
			System.out.println();
		}
	}

	public void showLockerInfo() {
		BorrowsModel[] models = dbHandler.getBorrowerInfo();

		for (int i = 0; i < models.length; i++) {
			BorrowsModel model = models[i];

			// simplified output formatting; truncation may occur
			System.out.printf("%-25.25s", model.getCustomerID());
			System.out.printf("%-15.15s", model.getLockNumber());
			System.out.printf("%-10.10s", model.getEndingTime());
			System.out.println();
		}
	}
	
    /**
	 * TerminalTransactionsDelegate Implementation
	 * 
     * The TerminalTransaction instance tells us that it is done with what it's 
     * doing so we are cleaning up the connection since it's no longer needed.
     */ 
    public void terminalTransactionsFinished() {
    	dbHandler.close();
    	dbHandler = null;
    	
    	System.exit(0);
    }
    
	/**
	 * Main method called at launch time
	 */
	public static void main(String args[]) {
		RecreationCenter recreationCenter = new RecreationCenter();
		recreationCenter.start();
	}
}
