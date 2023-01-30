package ca.ubc.cs304.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import ca.ubc.cs304.model.*;

/**
 * This class handles all database related transactions
 */
public class DatabaseConnectionHandler {
    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";

    private Connection connection = null;

    public DatabaseConnectionHandler() {
        try {
            // Load the Oracle JDBC driver
            // Note that the path could change for new drivers
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public List<String> getAllTableNames() throws SQLException{
        List<String> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT table_name FROM user_tables");
            while (rs.next()) {
                result.add(rs.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            rollbackConnection();
            throw e;
        }
        return result;
    }

    public List<String[]> joinTables(String tableOne, String tableTwo, List<String> attributesFromTables) throws SQLException {
        List<String[]> result = new ArrayList<>();
        String[] colName = attributesFromTables.toArray(new String[0]);;
        result.add(colName);

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableOne + " NATURAL JOIN " + tableTwo);
            while(rs.next()) {
                String[] row = new String[colName.length];
                for (int i = 0; i < colName.length; i ++) {
                    String columnName = colName[i].trim();
                    row[i] = rs.getString(columnName);
                }

                result.add(row);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    public List<String[]> getCustomerInfo() throws SQLException {
        List<String[]> result = new ArrayList<>();
        String[] colName = {"customerID","name", "address", "postalCode", "phoneNumber", "membershipType"};
        result.add(colName);

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM customer");
            while(rs.next()) {
                String[] row = new String[colName.length];
                row[0] = rs.getString("customerID");
                row[1] = rs.getString("cName");
                row[2] = rs.getString("cAddress");
                row[3] = rs.getString("postalCode");
                row[4] = rs.getString("phoneNum");
                row[5] = rs.getString("mType");

                result.add(row);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    public List<String[]> findCustomersParticipatingInAllEvents() throws SQLException {
        List<String[]> result = new ArrayList<>();
        String[] colName = {"customerID","name", "address", "postalCode", "phoneNumber", "membershipType"};
        result.add(colName);

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT * FROM customer c WHERE NOT EXISTS ((SELECT e.eventName, e.eventDate FROM events e)MINUS(SELECT p.eventName, p.eventDate FROM participatesin p WHERE p.customerID=c.customerID))");
            while(rs.next()) {
                String[] row = new String[colName.length];
                row[0] = rs.getString("customerID");
                row[1] = rs.getString("cName");
                row[2] = rs.getString("cAddress");
                row[3] = rs.getString("postalCode");
                row[4] = rs.getString("phoneNum");
                row[5] = rs.getString("mType");

                result.add(row);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    public List<String[]> findCustomersNotParticipatingAnywhere() throws SQLException {
        List<String[]> result = new ArrayList<>();
        String[] colName = {"customerID","name", "address", "postalCode", "phoneNumber", "membershipType"};
        result.add(colName);

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM customer WHERE customerID NOT IN (SELECT customerID FROM participatesin)");
            while(rs.next()) {
                String[] row = new String[colName.length];
                row[0] = rs.getString("customerID");
                row[1] = rs.getString("cName");
                row[2] = rs.getString("cAddress");
                row[3] = rs.getString("postalCode");
                row[4] = rs.getString("phoneNum");
                row[5] = rs.getString("mType");

                result.add(row);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    public List<String> getCustomerIDs() {
        List<String> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT customerID FROM customer");
            while(rs.next()) {
                result.add(rs.getString("customerID"));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    public void deleteCustomer(int customerID) throws SQLException {

        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM customer WHERE customerID = ?");
            ps.setInt(1, customerID);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Customer " + customerID + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertCustomer(CustomerModel model) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO customer VALUES (?,?,?,?,?,?)");
            ps.setInt(1, model.getCustomerID());
            ps.setString(2, model.getName());
            ps.setString(3, model.getAddress());
            ps.setString(4, model.getPostalCode());
            ps.setString(5, model.getPhoneNum());
            ps.setString(6, model.getMType());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateCustomer(CustomerModel model) {
        try {
            // Figure out how to do specific updates!!!
            PreparedStatement ps = connection.prepareStatement("UPDATE customer SET cName = ?, cAddress = ?, postalCode = ?, phoneNum = ?, mType = ? WHERE customerID = ?");
            ps.setString(1, model.getName());
            ps.setString(2, model.getAddress());
            ps.setString(3, model.getPostalCode());
            ps.setString(4, model.getPhoneNum());
            ps.setString(5, model.getMType());
            ps.setInt(6, model.getCustomerID());

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Customer " + model.getCustomerID() + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public List<String[]> getMembershipInfo() {
        List<String[]> result = new ArrayList<>();
        String[] colName = {"membershipType","price"};
        result.add(colName);

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM membership");
            while(rs.next()) {
                String[] row = new String[colName.length];
                row[0] = rs.getString("mType");
                row[1] = rs.getString("price");

                result.add(row);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    public List<String[]> findMaxMembershipPrice() {
        List<String[]> result = new ArrayList<>();
        String[] colName = {"membershipType","price"};
        result.add(colName);

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM membership WHERE price >= (SELECT MAX(price) FROM membership)");
            while(rs.next()) {
                String[] row = new String[colName.length];
                row[0] = rs.getString("mType");
                row[1] = rs.getString("price");

                result.add(row);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    public List<String[]> findMinMembershipPrice() {
        List<String[]> result = new ArrayList<>();
        String[] colName = {"membershipType","price"};
        result.add(colName);

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM membership WHERE price <= (SELECT MIN(price) FROM membership)");
            while(rs.next()) {
                String[] row = new String[colName.length];
                row[0] = rs.getString("mType");
                row[1] = rs.getString("price");

                result.add(row);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    public List<String> getMembershipTypes() {
        List<String> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT mType FROM membership");
            while(rs.next()) {
                result.add(rs.getString("mType"));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    public void deleteMembership(String type) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM membership WHERE mType = '" + type + "'");

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Membership Type " + type + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertMembership(MembershipModel model) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO membership VALUES (?,?)");
            ps.setString(1, model.getType());
            ps.setFloat(2, model.getPrice());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateMembership(MembershipModel model) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE membership SET price = ? WHERE mType = '" + model.getType() + "'");
            ps.setFloat(1, model.getPrice());

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Membership: " + model.getType() + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public List<String[]>  getEventInfo() {
        List<String[]> result = new ArrayList<>();
        String[] colName = {"Name","Date", "Theme", "Location"};
        result.add(colName);

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM events NATURAL JOIN eventlocation");
            while(rs.next()) {
                String[] row = new String[colName.length];
                row[0] = rs.getString("eventName");
                row[1] = rs.getDate("eventDate").toString();
                row[2] = rs.getString("theme");
                row[3] = rs.getString("facilityName");

                result.add(row);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    public List<String[]> getParticipationsInfo() {
        List<String[]> result = new ArrayList<>();
        String[] colName = {"Customer ID","Event Name", "Event Date"};
        result.add(colName);

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from participatesin");
            while(rs.next()) {
                String[] row = new String[colName.length];
                row[0] = Integer.toString(rs.getInt("customerID"));
                row[1] = rs.getString("eventName");
                row[2] = rs.getDate("eventDate").toString();
                result.add(row);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    public List<String> getEventNames() {
        List<String> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT eventName FROM events");
            while(rs.next()) {
                result.add(rs.getString("eventName"));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    public List<String> getEventDates() {
        List<String> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT eventDate FROM events");
            while(rs.next()) {
                result.add(rs.getDate("eventDate").toString());
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    public List<String[]> getFacilityInfo () {
        List<String[]> result = new ArrayList<>();
        String[] colName = {"name","floor", "size", "hours"};
        result.add(colName);

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM facility");

            while (rs.next()) {
                String[] row = new String[colName.length];
                row[0] = rs.getString("facilityName");
                row[1] = rs.getString("floorNum");
                row[2] = rs.getString("fsize");
                row[3] = rs.getString("fhours");

                result.add(row);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return result;
    }

    public List<String> getAllLocations() {
        List<String> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT facilityName FROM facility");
            while(rs.next()) {
                result.add(rs.getString("facilityName"));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    public List<String[]> selectFacility (String attr,String signInput, String numberInput) {
        List<String[]> result = new ArrayList<>();
        String[] colName = {"name","floor", "size", "hours"};
        result.add(colName);

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM facility WHERE " + attr + signInput + numberInput);

            while (rs.next()) {
                String[] row = new String[colName.length];
                row[0] = rs.getString("facilityName");
                row[1] = rs.getString("floorNum");
                row[2] = rs.getString("fsize");
                row[3] = rs.getString("fhours");

                result.add(row);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return result;
    }

    public List<String[]> projectFacilityAttributes (String selectedAttribites) {
        List<String[]> result = new ArrayList<>();

        String[] colName = selectedAttribites.split(",");
        result.add(colName);

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT" + selectedAttribites +  " FROM facility");

            while (rs.next()) {
                String[] row = new String[colName.length];
                for (int i = 0; i < colName.length; i ++) {
                    String columnName = colName[i].trim();
                    row[i] = rs.getString(columnName);
                }

                result.add(row);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage() + " WHERE selected attributes: " + selectedAttribites);
        }
        return result;
    }


    public void deleteEvent(String eventName, String eventDate) {
        try {
            String query = "DELETE FROM events WHERE eventName = '" + eventName +
                    "' AND eventDate = TO_DATE ('"+ eventDate +"', 'YYYY-MM-DD')";
            PreparedStatement ps = connection.prepareStatement(query);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Event " + eventName + " on " + eventDate + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertEvent(EventModel model) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO events VALUES (?,?,?)");
            ps.setString(1, model.getEventName());
            ps.setDate(2, model.getEventDate());
            ps.setString(3, model.getTheme());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateEvent(String eventName, String eventDate, String theme) {
        try {
            String query = "UPDATE events SET theme = '" + theme + "' WHERE eventName = '" + eventName +
                    "' AND eventDate = TO_DATE ('"+ eventDate +"', 'YYYY-MM-DD')";
            PreparedStatement ps = connection.prepareStatement(query);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Event " + eventName + "on" + eventDate + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertEventLocation(EventModel model) {
        try {
            String query = "SELECT * FROM eventLocation WHERE eventName = '" + model.getEventName() + "'";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next() == false) {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO eventLocation VALUES (?,?)");
                ps.setString(1, model.getEventName());
                ps.setString(2, model.getLocation());

                ps.executeUpdate();
                connection.commit();

                ps.close();
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateEventLocation(String eventName, String location) {
        try {
            String query = "UPDATE eventlocation SET facilityName = '" + location + "' WHERE eventName = '" + eventName + "'";
            PreparedStatement ps = connection.prepareStatement(query);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Event " + eventName + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }



    public List<String[]> getRegistrationInfo() {
        List<String[]> result = new ArrayList<>();
        String[] colName = {"Customer ID","Fitness Class", "Day"};
        result.add(colName);

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from REGISTERSIN");
            while(rs.next()) {
                String[] row = new String[colName.length];
                row[0] = Integer.toString(rs.getInt("customerID"));
                row[1] = rs.getString("fitnessClassName");
                row[2] = rs.getString("fitnessClassDay");
                result.add(row);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    public List<String> getFitnessClassNames() {
        List<String> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT fcName FROM FITNESSCLASS");
            while(rs.next()) {
                result.add(rs.getString("fcName"));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    public List<String> getFitnessClassesWithDays() {
        List<String> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT fcName, dayOfTheWeek FROM FITNESSCLASSINFO");
            while(rs.next()) {
                result.add(rs.getString("fcName") + " // " + rs.getString("dayOfTheWeek"));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    public void deleteRegistersIn(int customerID, String fitnessClassName, String fitnessClassDay) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM registersin WHERE customerID = ? AND fitnessClassName = ? AND fitnessClassDay = ?");
            ps.setInt(1, customerID);
            ps.setString(2, fitnessClassName);
            ps.setString(3, fitnessClassDay);


            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " registration of " + customerID + "in" + fitnessClassName + "on" + fitnessClassDay + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertRegistersIn(RegistrationModel model) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO registersin VALUES (?,?,?)");
            ps.setInt(1, model.getCustomerID());
            ps.setString(2, model.getName());
            ps.setString(3, model.getDay());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public List<String[]> getParticipantsInfo(String eventName, String eventDate) {
        List<String[]> result = new ArrayList<>();
        String[] eventIdentifier = {eventName + " on " + eventDate, "", ""};
        result.add(eventIdentifier);
        String[] colName = {"CustomerID","Name","Phone"};
        result.add(colName);

        try {
            String query = "SELECT customerID, cName, phoneNum FROM customer NATURAL JOIN participatesin WHERE eventName = '" + eventName +
                    "' AND eventDate = TO_DATE ('"+ eventDate +"', 'YYYY-MM-DD')";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                String[] row = new String[colName.length];
                row[0] = rs.getString("customerID");
                row[1] = rs.getString("cName");
                row[2] = rs.getString("phoneNum");

                result.add(row);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    public void deleteParticipatesIn(String eventName, String eventDate, String participantID) {
        try {
            String query = "DELETE FROM participatesin WHERE customerID = '" + participantID + "' AND eventName = '" + eventName +
                    "' AND eventDate = TO_DATE ('"+ eventDate +"', 'YYYY-MM-DD')";
            PreparedStatement ps = connection.prepareStatement(query);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Participation of " + participantID + " in " + eventName + " on " + eventDate + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertParticipatesIn(String eventName, String eventDate, String participantID) {
        try {
            String query = "INSERT INTO participatesin VALUES ('" + participantID + "', '" + eventName +
                    "', TO_DATE ('"+ eventDate +"', 'YYYY-MM-DD'))";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public List<String[]> getMostPopularClasses() {
        List<String[]> result = new ArrayList<>();
        String[] colName = {"Class Name","Number of Students"};
        result.add(colName);

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT FITNESSCLASSNAME, count(*) as NumStudents FROM REGISTERSIN GROUP BY FITNESSCLASSNAME ORDER BY count(*) DESC");
            while(rs.next()) {
                String[] row = new String[colName.length];
                row[0] = rs.getString("fitnessClassName");
                row[1] = Integer.toString(rs.getInt("NumStudents"));
                result.add(row);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    public List<String[]> getAvgNumCustomers() {
        List<String[]> result = new ArrayList<>();
        String[] colName = {"Avg Num of Customers in a Class"};
        result.add(colName);

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT avg(NumStudents) as AvgStudents FROM (SELECT FITNESSCLASSNAME, count(*) AS NumStudents FROM REGISTERSIN GROUP BY FITNESSCLASSNAME)");
            while(rs.next()) {
                String[] row = new String[colName.length];
                row[0] = Integer.toString(rs.getInt("AvgStudents"));
                result.add(row);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    public List<String[]> getClassSchedule() {
        List<String[]> result = new ArrayList<>();
        String[] colName = {"Class Name","Facility Name", "Timeslot", "Day", "Instructor"};
        result.add(colName);

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from FITNESSCLASS left outer join FITNESSCLASSINFO ON FITNESSCLASS.FCNAME = FITNESSCLASSINFO.FCNAME ORDER BY DAYOFTHEWEEK, FITNESSCLASS.FCNAME, FITNESSCLASSINFO.TIMESLOT");
            while(rs.next()) {
                String[] row = new String[colName.length];
                row[0] = rs.getString("fcName");
                row[1] = rs.getString("facilityName");
                row[2] = rs.getString("timeslot");
                row[3] = rs.getString("dayOfTheWeek");
                row[4] = Integer.toString(rs.getInt("instructorID"));
                result.add(row);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    public void deleteEmployee(int employeeID) {
        try {
            // do we need to explicitly delete from instructor/manager/volunteer tables? it should cascade in database right?
            // i think it was total particip. so an employee has to be one of instructor, manager, volunteer so there won't be a case of only deleting instructor, not employee
            PreparedStatement ps = connection.prepareStatement("DELETE FROM employee WHERE employeeID = ?");
            ps.setInt(1, employeeID);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Employee " + employeeID + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }
    public void deleteFitnessClass(String fcName) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM fitnessclass WHERE fcName = ?");
            ps.setString(1, fcName);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Fitness class " + fcName + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void deleteFitnessClassInfo(String fcName, String dayOfTheWeek) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM fitnessclassinfo WHERE fcName = ? AND dayOfTheWeek = ?");
            ps.setString(1, fcName);
            ps.setString(2, dayOfTheWeek);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Fitness class info for " + fcName + " on " + dayOfTheWeek + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void deleteManages(String mType, int managerID) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM manages WHERE mType = ? AND managerID = ?");
            ps.setString(1, mType);
            ps.setInt(2, managerID);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " The manager " + managerID + " does not manage " + mType + " !");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void deleteWorksIn(int employeeID, String facilityName) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM worksin WHERE employeeID = ? AND facilityName = ?");
            ps.setInt(1, employeeID);
            ps.setString(2, facilityName);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " The employee " + employeeID + " does not work in " + facilityName + " !");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void deleteFacility (String facilityName) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM facility WHERE facilityName = ?");
            ps.setString(1, facilityName);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " facility of " + facilityName + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + "  " + e.getMessage());
            rollbackConnection();
        }
    }

    public void deleteGym (String facilityName) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM gym WHERE facilityName = ?");
            ps.setString(1, facilityName);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " facility of " + facilityName + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + "  " + e.getMessage());
            rollbackConnection();
        }
    }

    public void deletePool (String facilityName) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM pool WHERE facilityName = ?");
            ps.setString(1, facilityName);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " facility of " + facilityName + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + "  " + e.getMessage());
            rollbackConnection();
        }
    }

    public void deleteChangingRoom (String facilityName) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM changingRoom WHERE facilityName = ?");
            ps.setString(1, facilityName);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " facility of " + facilityName + " does not exist!");
            }
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + "  " + e.getMessage());
            rollbackConnection();
        }
    }

    public void deleteFloorInfo (Integer floor, String facilityName) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM floorInfo WHERE floor = ? AND facilityName = ?");
            ps.setInt(1, floor);
            ps.setString (2, facilityName);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println (WARNING_TAG + " FloorInfo" + facilityName + " on" + floor + " does not exist!");
            }
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + "  " + e.getMessage());
            rollbackConnection();
        }
    }

    public void deleteEquipment (Integer equipmentID) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Equipment WHERE equipmentID = ?");
            ps.setInt(1, equipmentID);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " equipment of " + equipmentID + " does not exist!");
            }
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + "  " + e.getMessage());
            rollbackConnection();
        }
    }

    public void deleteFacilityEquipment (Integer equipmentID, String facilityName) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM facilityEquipment WHERE equipmentID = ? AND facilityName = ?");
            ps.setInt(1, equipmentID);
            ps.setString(2, facilityName);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " facilityEquipment" + equipmentID + " in" + facilityName + " does not exist!");
            }
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + "  " + e.getMessage());
            rollbackConnection();
        }
    }

    public void deleteEquipType (String facilityName) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM EquipType WHERE facilityName = ?");
            ps.setString(1, facilityName);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " equipType" + facilityName + " does not exist!");
            }
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + "  " + e.getMessage());
            rollbackConnection();
        }
    }

    public void deleteLocker(Integer lockNumber, String locationName){
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM locker WHERE lockNumber = ? AND locationName = ?");
            ps.setInt(1, lockNumber);
            ps.setString(2, locationName);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " locker" + lockNumber + " in" + locationName + " does not exist!");
            }
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + "  " + e.getMessage());
            rollbackConnection();
        }
    }

    public void deleteBorrows(Integer lockNumber, Integer customerID) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM borrows WHERE lockNumber = ? AND customerID = ?");
            ps.setInt(1, lockNumber);
            ps.setInt(2, customerID);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " borrows" + lockNumber + " on" + customerID +" does not exist!");
            }
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + "  " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertEmployee(EmployeeModel model) {
        try {
            // i assume we call this, then we call insert manager/etc in order to insert into both employee and manager/etc tables?
            PreparedStatement ps = connection.prepareStatement("INSERT INTO employee VALUES (?)");
            ps.setInt(1, model.getEmployeeID());
            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertManager(ManagerModel model) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO manager VALUES (?,?)");
            ps.setInt(1, model.getEmployeeID());
            ps.setInt(2, model.getOfficeNum());
            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertInstructor(InstructorModel model) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO instructor VALUES (?,?)");
            ps.setInt(1, model.getEmployeeID());
            ps.setString(2, model.getExpertise());
            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertVolunteer(VolunteerModel model) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO volunteer VALUES (?,?)");
            ps.setInt(1, model.getEmployeeID());
            ps.setDate(2, model.getEndingDate());
            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertFitnessClass(FitnessClassModel model) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO fitnessclass VALUES (?,?)");
            ps.setString(1, model.getName());
            ps.setString(2, model.getLocation());
            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertFitnessClassInfo(FitnessClassModel model) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO fitnessclassinfo VALUES (?,?,?,?)");
            ps.setString(1, model.getName());
            ps.setString(2, model.getTimeslot());
            ps.setString(3, model.getDay());
            ps.setInt(4, model.getInstructor());
            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertManages(ManagesModel model) {
        try {
            // todo: ensure that it's a manager and not a random employee
            PreparedStatement ps = connection.prepareStatement("INSERT INTO manages VALUES (?,?)");
            ps.setString(1, model.getmType());
            ps.setInt(2, model.getManagerID());
            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertWorksIn(WorksInModel model) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO worksin VALUES (?,?)");
            ps.setInt(1, model.getEmployeeID());
            ps.setString(2, model.getFacilityName());
            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertFacility (FacilityModel model) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO facility VALUES (?,?,?,?)");
            ps.setString(1, model.getFacilityName());
            ps.setInt(2, model.getFloor());
            ps.setInt(3, model.getSize());
            ps.setString(4, model.getHours());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }

    }

    public void insertGym (GymModel model) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO gym VALUES (?,?,?)");
            ps.setInt(1, model.getNumOfMachines());
            ps.setInt(2, model.getNumOfWeights());
            ps.setString(3, model.getFacilityName());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }

    }

    public void insertPool (PoolModel model) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO pool VALUES (?,?,?)");
            ps.setInt(1, model.getNumOfPools());
            ps.setInt(2, model.getIsSaunaAvailable());
            ps.setString(3, model.getFacilityName());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }

    }

    public void insertChangingRoom (ChangingRoomModel model) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO changingRoom VALUES (?,?,?)");
            ps.setInt(1, model.getNumOfLockers());
            ps.setInt(2, model.getNumOfShowers());
            ps.setString(3, model.getFacilityName());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }

    }

    public void insertFloorInfo (FacilityModel model) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO floorInfo VALUES (?,?)");
            ps.setString(1, model.getFacilityName());
            ps.setInt(2, model.getFloor());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertEquipment (EquipmentModel model) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO equipment VALUES (?,?,?,?)");
            ps.setInt(1, model.getEquipmentID());
            ps.setString(2, model.getType());
            ps.setInt(3, model.getIsMaintanenceRequired());
            ps.setString(4, model.getFacilityName());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertFacilityEquip(FacilityEquipModel model) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO facilityEquip VALUES (?,?,?)");
            ps.setInt(1, model.getEquipmentID());
            ps.setInt(2, model.getIsMaintanenceRequired());
            ps.setString(3, model.getFacilityName());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertEquipType (EquipTypeModel model) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO equipType VALUES (?,?,?)");
            ps.setInt(1, model.getEquipmentID());
            ps.setString(2, model.getType());
            ps.setString(3, model.getFacilityName());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertLockers (LockerModel model) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO locker VALUES (?,?,?)");
            ps.setInt(1, model.getIsTaken());
            ps.setInt(2, model.getLockNumber());
            ps.setString(3, model.getLocationName());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertBorrowers (BorrowsModel model) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO borrowers VALUES (?,?,?)");
            ps.setInt(1, model.getCustomerID());
            ps.setInt(2, model.getLockNumber());
            ps.setTime(3, model.getEndingTime());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public CustomerModel getSpecificCustomerInfo(int customerID) {
        CustomerModel result = null;

        try {
            String query = "SELECT * FROM customer WHERE customerID = " + customerID;
                   
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            result = new CustomerModel(
                    rs.getInt("customerID"),
                    rs.getString("cName"),
                    rs.getString("cAddress"),
                    rs.getString("postalCode"),
                    rs.getString("phoneNum"),
                    rs.getString("mType"));

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    public EmployeeModel[] getEmployeeInfo() {
        ArrayList<EmployeeModel> result = new ArrayList<EmployeeModel>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM employee");
            while(rs.next()) {
                EmployeeModel model = new EmployeeModel(rs.getInt("employeeID"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new EmployeeModel[result.size()]);
    }

    public ManagerModel[] getManagerInfo() {
        ArrayList<ManagerModel> result = new ArrayList<ManagerModel>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM manager");
            while(rs.next()) {
                ManagerModel model = new ManagerModel(rs.getInt("employeeID"),
                        rs.getInt("officeNum"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new ManagerModel[result.size()]);
    }

    public InstructorModel[] getInstructorInfo() {
        ArrayList<InstructorModel> result = new ArrayList<InstructorModel>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM instructor");
            while(rs.next()) {
                InstructorModel model = new InstructorModel(rs.getInt("employeeID"),
                        rs.getString("expertise"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new InstructorModel[result.size()]);
    }

    public VolunteerModel[] getVolunteerInfo() {
        ArrayList<VolunteerModel> result = new ArrayList<VolunteerModel>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM volunteer");
            while(rs.next()) {
                VolunteerModel model = new VolunteerModel(rs.getInt("employeeID"),
                        rs.getDate("endingDate"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new VolunteerModel[result.size()]);
    }

    public List<String[]> getFitnessClassInfo() {
        List<String[]> result = new ArrayList<>();
        String[] colName = {"Class Name","Facility Name"};
        result.add(colName);

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from FITNESSCLASS");
            while(rs.next()) {
                String[] row = new String[colName.length];
                row[0] = rs.getString("fcName");
                row[1] = rs.getString("facilityName");
                result.add(row);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }


    public ManagesModel[] getManagesInfo() {
        ArrayList<ManagesModel> result = new ArrayList<ManagesModel>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM manages");
            while(rs.next()) {
                ManagesModel model = new ManagesModel(rs.getString("mType"),
                        rs.getInt("managerID"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new ManagesModel[result.size()]);
    }

    public WorksInModel[] getWorksInInfo() {
        ArrayList<WorksInModel> result = new ArrayList<WorksInModel>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM worksin");
            while(rs.next()) {
                WorksInModel model = new WorksInModel(rs.getInt("employeeID"),
                        rs.getString("facilityName"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new WorksInModel[result.size()]);
    }

    public GymModel[] getGymInfo () {
        ArrayList<GymModel> result = new ArrayList<GymModel>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM gym");

            while (rs.next()) {
                GymModel model = new GymModel(rs.getInt("numOfMachines"), rs.getInt("numOfWeights"), rs.getString("facilityName"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return result.toArray(new GymModel[result.size()]);
    }

    public PoolModel[] getPoolInfo () {
        ArrayList<PoolModel> result = new ArrayList<PoolModel>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM gym");

            while (rs.next()) {
                PoolModel model = new PoolModel(rs.getInt("numOfPools"), rs.getInt("isSaunaAvailable"), rs.getString("facilityName"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return result.toArray(new PoolModel[result.size()]);
    }

    public ChangingRoomModel[] getChangingRoomInfo () {
        ArrayList<ChangingRoomModel> result = new ArrayList<ChangingRoomModel>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM gym");

            while (rs.next()) {
                ChangingRoomModel model = new ChangingRoomModel(rs.getInt("numOfLockers"), rs.getInt("numOfShowers"), rs.getString("facilityName"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return result.toArray(new ChangingRoomModel[result.size()]);
    }

    public FacilityModel[] getFloorInfo () {
        ArrayList<FacilityModel> result = new ArrayList<FacilityModel>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM facility");

            while (rs.next()) {
                FacilityModel model = new FacilityModel(rs.getString("facilityName"), rs.getInt("floor"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return result.toArray(new FacilityModel[result.size()]);
    }

    public EquipmentModel[] getEquipmentInfo () {
        ArrayList<EquipmentModel> result = new ArrayList<EquipmentModel>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM facility");

            while (rs.next()) {
                EquipmentModel model = new EquipmentModel(rs.getInt("equipmentID"), rs.getString("type"), rs.getInt("isMaintanenceRequired"), rs.getString("facilityName"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return result.toArray(new EquipmentModel[result.size()]);
    }

    public FacilityEquipModel[] getFacilityEquipInfo () {
        ArrayList<FacilityEquipModel> result = new ArrayList<FacilityEquipModel>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM facility");

            while (rs.next()) {
                FacilityEquipModel model = new FacilityEquipModel(rs.getInt("equipmentID"), rs.getInt("isMaintanenceRequired"), rs.getString("facilityName"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return result.toArray(new FacilityEquipModel[result.size()]);
    }

    public EquipTypeModel[] getEquipTypeInfo () {
        ArrayList<EquipTypeModel> result = new ArrayList<EquipTypeModel>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM facility");

            while (rs.next()) {
                EquipTypeModel model = new EquipTypeModel(rs.getInt("equipmentID"), rs.getString("type"), rs.getString("facilityName"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return result.toArray(new EquipTypeModel[result.size()]);
    }

    public LockerModel[] getLockerInfo () {
        ArrayList<LockerModel> result = new ArrayList<LockerModel>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM facility");

            while (rs.next()) {
                LockerModel model = new LockerModel(rs.getInt("isTaken"), rs.getInt("lockNumber"), rs.getString("locationName"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return result.toArray(new LockerModel[result.size()]);
    }

    public BorrowsModel[] getBorrowerInfo () {
        ArrayList<BorrowsModel> result = new ArrayList<BorrowsModel>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM facility");

            while (rs.next()) {
                BorrowsModel model = new BorrowsModel(rs.getInt("customerID"), rs.getInt("lockNumber"), rs.getTime("endingTime"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return result.toArray(new BorrowsModel[result.size()]);
    }

    public void updateEmployee(int newID, int currentID) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE employee SET employeeID = ? WHERE employeeID = ?");
            ps.setInt(1, newID);
            ps.setInt(2, currentID);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Employee " + currentID + " does not exist!");
            }
            // will an error be thrown by sql if we try to update the employeeID to an ID that already exists?

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateManager(int newOfficeNum, int managerID) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE manager SET officeNum = ? WHERE employeeID = ?");
            ps.setInt(1, newOfficeNum);
            ps.setInt(2, managerID);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Manager " + managerID + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateInstructor(String newExpertise, int instructorID) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE instructor SET expertise = ? WHERE employeeID = ?");
            ps.setString(1, newExpertise);
            ps.setInt(2, instructorID);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Instructor " + instructorID + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateVolunteer(Date newEndingDate, int volunteerID) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE volunteer SET endingDate = ? WHERE employeeID = ?");
            ps.setDate(1, newEndingDate);
            ps.setInt(2, volunteerID);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Volunteer " + volunteerID + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateFitnessClass(String newLocation, String fcName) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE fitnessclass SET facilityName = ? WHERE fcName = ?");
            ps.setString(1, newLocation);
            ps.setString(2, fcName);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Fitness class " + fcName + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateFitnessClassInfo(FitnessClassModel model) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE fitnessclassinfo SET timeslot = ?, INSTRUCTORID = ? WHERE fcName = ? AND dayOfTheWeek = ?");
            ps.setString(1, model.getTimeslot());
            ps.setInt(2, model.getInstructor());
            ps.setString(3, model.getName());
            ps.setString(4, model.getDay());

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Fitness class " + model.getName() + " on " + model.getDay() + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateFitnessClassInfo(int newInstructor, String fcName, String dayOfTheWeek) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE fitnessclassinfo SET instructorID = ? WHERE fcName = ? AND dayOfTheWeek = ?");
            ps.setInt(1, newInstructor);
            ps.setString(2, fcName);
            ps.setString(3, dayOfTheWeek);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Fitness class " + fcName + " on " + dayOfTheWeek + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateManages(String newType, int managerID, String currentType) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE manages SET mType = ? WHERE managerID = ? AND mType = ?");
            ps.setString(1, newType);
            ps.setInt(2, managerID);
            ps.setString(3, currentType);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Manager " + managerID + " does not manage " + currentType + "!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateWorksIn(String newLocation, int employeeID, String currentLocation) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE worksin SET facilityName = ? WHERE employeeID = ? AND facilityName = ?");
            ps.setString(1, newLocation);
            ps.setInt(2, employeeID);
            ps.setString(3, currentLocation);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Employee " + employeeID + " does not work in " + currentLocation + "!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }


    public void updateFacility(FacilityModel model) {
        try {

            PreparedStatement ps = connection.prepareStatement("UPDATE facility SET floorNum = ?, fsize = ?, fhours = ? WHERE facilityName = ?");
            ps.setInt(1, model.getFloor());
            ps.setInt(2, model.getSize());
            ps.setString(3, model.getHours());
            ps.setString(4, model.getFacilityName());


            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " facilityName " + model.getFacilityName() + " on " + model.getFloor() + " and " + model.getHours() + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateGym(Integer numOfMachines, Integer numOfWeights, String facilityName) {
        try {

            PreparedStatement ps = connection.prepareStatement("UPDATE gym SET facilityName = ? WHERE numOfMachines = ? AND numOfWeights = ?");
            ps.setInt(1, numOfMachines);
            ps.setInt(2, numOfWeights);
            ps.setString(3, facilityName);


            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " facilityName " + facilityName + " have" + numOfMachines + " and" + numOfWeights +" does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updatePool(Integer numOfPools, Integer isSaunaAvailable, String facilityName) {
        try {

            PreparedStatement ps = connection.prepareStatement("UPDATE gym SET facilityName = ? WHERE numOfPools = ? AND isSaunaAvailable = ?");
            ps.setInt(1, numOfPools);
            ps.setInt(2, isSaunaAvailable);
            ps.setString(3, facilityName);


            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " facilityName " + facilityName + " have" + numOfPools + " and" + isSaunaAvailable +" does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateChangingRoom(Integer numOfLockers, Integer numOfShowers, String facilityName) {
        try {

            PreparedStatement ps = connection.prepareStatement("UPDATE gym SET facilityName = ? WHERE numOfLockers = ? AND numOfShowers = ?");
            ps.setInt(1, numOfLockers);
            ps.setInt(2, numOfShowers);
            ps.setString(3, facilityName);


            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " facilityName " + facilityName + " have" + numOfLockers + " and" + numOfShowers +" does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateFloor(String facilityName, Integer floor, Integer size, String hours) {
        try {

            PreparedStatement ps = connection.prepareStatement("UPDATE floor SET facilityName = ? WHERE floor = ?");
            ps.setString(1, facilityName);
            ps.setInt(2, floor);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " facilityName " + facilityName + " on" + floor + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateEquipment (Integer equipmentID, String type, Integer isMaintenanceRequired, String facilityName) {
        try {

            PreparedStatement ps = connection.prepareStatement("UPDATE equipment SET facilityName = ? WHERE equipmentID = ? AND type = ? AND isMaintenanceRequired = ?");
            ps.setInt(1, equipmentID);
            ps.setString(2, type);
            ps.setInt(3, isMaintenanceRequired);
            ps.setString(4, facilityName);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " equipment " + equipmentID + " in" + facilityName + " has"+ isMaintenanceRequired +"and" + type +" does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateFacilityEquip (Integer equipmentID, Integer isMaintenanceRequired, String facilityName) {
        try {

            PreparedStatement ps = connection.prepareStatement("UPDATE facilityEquip SET facilityName = ? WHERE equipmentID = ? AND isMaintenanceRequired = ?");
            ps.setInt(1, equipmentID);
            ps.setInt(2, isMaintenanceRequired);
            ps.setString(3, facilityName);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " equipment " + equipmentID + " in" + facilityName + " has"+ isMaintenanceRequired + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateEquipType (Integer equipmentID, String type, String facilityName) {
        try {

            PreparedStatement ps = connection.prepareStatement("UPDATE equipment SET facilityName = ? WHERE equipmentID = ? AND type = ?");
            ps.setInt(1, equipmentID);
            ps.setString(2, type);
            ps.setString(3, facilityName);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " equipment " + equipmentID + " in" + facilityName + " has" + type +" does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateLocker (Integer isTaken, Integer lockNumber, String locationName) {
        try {

            PreparedStatement ps = connection.prepareStatement("UPDATE locker SET locationName = ? WHERE isTaken = ? AND lockNumber = ?");
            ps.setInt(1, isTaken);
            ps.setInt(2, lockNumber);
            ps.setString(3, locationName);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " locker " + lockNumber + " and" + isTaken + " in" + locationName +" does not exist!");
            }

            connection.commit();

            ps.close();

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateBorrower (Integer customerID, Integer lockNumber, Time endingTime) {
        try {

            PreparedStatement ps = connection.prepareStatement("UPDATE borrower SET customerID = ? WHERE lockNumber = ? AND endingTime = ?");
            ps.setInt(1, customerID);
            ps.setInt(2, lockNumber);
            ps.setTime(3, endingTime);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " customer " + customerID + " in" + lockNumber + " and" + endingTime +" does not exist!");
            }

            connection.commit();

            ps.close();

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public boolean login(String username, String password) {
        try {
            if (connection != null) {
                connection.close();
            }

            connection = DriverManager.getConnection(ORACLE_URL, username, password);
            connection.setAutoCommit(false);

            System.out.println("\nConnected to Oracle!");
            return true;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return false;
        }
    }

    private void rollbackConnection() {
        try  {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }
}
