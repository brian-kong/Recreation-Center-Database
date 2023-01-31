### Recreation-Center-Database

CRUD android app Recreational Center Database project using MySql, Java Spring GUI connected with JDBC. 
This project is a prototype database that could be used for modern recreation centers and gyms 

  - View/add/delete/modify customers;
  - View/add/delete/modify memberships; 
  - View cheapest and most expensive memberships;
  - View/add//delete/modify events;
  - View/add/delete/modify customers’ participation in events; 
  - View customers participating in specific events;
  - View customers who participate in all or none of the events; 
  - View/add/delete/modify facilities; 
  - Select facilities by their size or floor that they are located on;
  - View facility table with only specific information requested by user;
  - View fitness classes, in both simple and more detailed views;
  - View most popular fitness classes, i.e. sorted fitness classes by amount of customers registered in them;
  - Perform natural join on tables of user’s choice;

<p>
<img src = "https://github.com/brian-kong/Recreation-Center-Database/blob/main/ER_diagram.jpg" width = "350px" height = "auto">
</p>

## Queries: 
# Insert: 
- "INSERT INTO customer VALUES (?,?,?,?,?,?)" 
- "INSERT INTO membership VALUES (?,?)"
- "INSERT INTO events VALUES (?,?,?)"
- "INSERT INTO eventLocation VALUES (?,?)"
- "INSERT INTO registersin VALUES (?,?,?)"
- "INSERT INTO participatesin VALUES ('" + participantID + "', '" + eventName +  "', TO_DATE ('"+ eventDate +"', 'YYYY-MM-DD'))"
- "INSERT INTO fitnessclass VALUES (?,?)"
- "INSERT INTO fitnessclassinfo VALUES (?,?,?,?)"
- "INSERT INTO facility VALUES (?,?,?,?)"

# Delete: 
- "DELETE FROM customer WHERE customerID = ?"
- "DELETE FROM membership WHERE mType = '" + type + "'"
- "DELETE FROM events WHERE eventName = '" + eventName + "' AND eventDate = TO_DATE ('"+ eventDate +"', 'YYYY-MM-DD')"
- "DELETE FROM registersin WHERE customerID = ? AND fitnessClassName = ? AND fitnessClassDay = ?"
- "DELETE FROM participatesin WHERE customerID = '" + participantID + "' AND eventName = '" + eventName +  "' AND eventDate = TO_DATE ('"+ eventDate +"', 'YYYY-MM-DD')"
- "DELETE FROM fitnessclass WHERE fcName = ?"
- "DELETE FROM fitnessclassinfo WHERE fcName = ? AND dayOfTheWeek = ?"
- "DELETE FROM facility WHERE facilityName = ?"
# Update: 
- "UPDATE customer SET cName = ?, cAddress = ?, postalCode = ?, phoneNum = ?, mType = ? WHERE customerID = ?"
- "UPDATE membership SET price = ? WHERE mType = '" + model.getType() + "'"
- "UPDATE events SET theme = '" + theme + "' WHERE eventName = '" + eventName + "' AND eventDate = TO_DATE ('"+ eventDate +"', 'YYYY-MM-DD')"
- "UPDATE eventlocation SET facilityName = '" + location + "' WHERE eventName = '" + eventName + "'
- "UPDATE facility SET floorNum = ?, fsize = ?, fhours = ? WHERE facilityName = ?"

# Selection: 
- "SELECT * FROM facility WHERE " + attr + signInput + numberInput 
# Projection: 
- "SELECT" + selectedAttribites +  " FROM facility"

# Join: 
"SELECT * FROM " + tableOne + " NATURAL JOIN " + tableTwo

# Aggregation: 
- "SELECT * FROM membership WHERE price >= (SELECT MAX(price) FROM membership)"
- "SELECT * FROM membership WHERE price <= (SELECT MIN(price) FROM membership)"

# Nested aggregation with group by: 
- "SELECT avg(NumStudents) as AvgStudents FROM (SELECT FITNESSCLASSNAME, count(*) AS NumStudents FROM REGISTERSIN GROUP BY FITNESSCLASSNAME)"

# Division: 
- "SELECT * FROM customer c WHERE NOT EXISTS ((SELECT e.eventName, e.eventDate FROM events e)MINUS(SELECT p.eventName, p.eventDate FROM participatesin p WHERE p.customerID=c.customerID))"
  

## Insert(Customer)

<p>
<img src = "https://github.com/brian-kong/Recreation-Center-Database/blob/main/Insert.jpg" width = "350px" height = "auto">
</p>

## Delete(Customer)

<p>
<img src = "https://github.com/brian-kong/Recreation-Center-Database/blob/main/Insert.jpg" width = "350px" height = "auto">
</p>

## Update(Membership)

<p>
<img src = "https://github.com/brian-kong/Recreation-Center-Database/blob/main/Insert.jpg" width = "350px" height = "auto">
</p>

## Select(Facility)

<p>
<img src = "https://github.com/brian-kong/Recreation-Center-Database/blob/main/select.jpg" width = "350px" height = "auto">
</p>

## Project(Facility)

<p>
<img src = "https://github.com/brian-kong/Recreation-Center-Database/blob/main/Delete.jpg" width = "350px" height = "auto">
</p>

## Join(Tables)

<p>
<img src = "https://github.com/brian-kong/Recreation-Center-Database/blob/main/join.jpg" width = "350px" height = "auto">
</p>

## Aggregate(Membership Prices)

<p>
<img src = "https://github.com/brian-kong/Recreation-Center-Database/blob/main/aggregate.jpg" width = "350px" height = "auto">
</p>

## Division(Customers who participate in all events)

<p>
<img src = "https://github.com/brian-kong/Recreation-Center-Database/blob/main/division.jpg" width = "350px" height = "auto">
</p>








