drop table customer cascade constraints;
drop table membership cascade constraints;
drop table registersin cascade constraints;
drop table locker cascade constraints;
drop table borrows cascade constraints;
drop table events cascade constraints;
drop table eventlocation cascade constraints;
drop table participatesin cascade constraints;
drop table manages cascade constraints;
drop table employee cascade constraints;
drop table worksin cascade constraints;
drop table manager cascade constraints;
drop table volunteer cascade constraints;
drop table instructor cascade constraints;
drop table fitnessclass cascade constraints;
drop table fitnessclassinfo cascade constraints;
drop table facility cascade constraints;
drop table floorinfo cascade constraints;
drop table gym cascade constraints;
drop table pools cascade constraints;
drop table changingroom cascade constraints;
drop table equipment cascade constraints;
drop table equiptype cascade constraints;
drop table facilityequip cascade constraints;

PURGE RECYCLEBIN;

CREATE TABLE membership (
	mType CHAR(10) PRIMARY KEY,
	price FLOAT);
grant select on membership to public;

CREATE TABLE customer (
	customerID INTEGER PRIMARY KEY,
	cName CHAR(20),
	cAddress CHAR(20),
	postalCode CHAR(7),
	phoneNum CHAR(10),
	mType CHAR(10),
	FOREIGN KEY (mType) REFERENCES membership ON DELETE SET NULL);
grant select on customer to public;
 
CREATE TABLE events (
	eventName CHAR(30),
	eventDate DATE,
	theme CHAR(20),
	PRIMARY KEY (eventName, eventDate));
grant select on events to public;

CREATE TABLE participatesin (
	customerID INTEGER,
	eventName CHAR(30),
	eventDate DATE,
	PRIMARY KEY (customerID, eventName, eventDate),
	FOREIGN KEY (customerID) REFERENCES customer(customerID) ON DELETE CASCADE,
	FOREIGN KEY (eventName, eventDate) REFERENCES events ON DELETE CASCADE);
grant select on participatesin to public;

CREATE TABLE employee(
	employeeID INTEGER PRIMARY KEY);
grant select on employee to public;

CREATE TABLE manager (
	officeNum INTEGER,
	employeeID INTEGER PRIMARY KEY,
	FOREIGN KEY (employeeID) REFERENCES employee ON DELETE CASCADE);
grant select on manager to public;

CREATE TABLE manages (
	mType CHAR(10),
	managerID INTEGER,
	PRIMARY KEY(mType, managerID),
	FOREIGN KEY (mType) REFERENCES membership ON DELETE CASCADE,
	FOREIGN KEY (managerID) REFERENCES manager(employeeID) ON DELETE CASCADE);
grant select on manages to public;

CREATE TABLE volunteer (
	employeeID INTEGER PRIMARY KEY,
	endingDate DATE,
	FOREIGN KEY (employeeID) REFERENCES employee ON DELETE CASCADE);
grant select on volunteer to public;

CREATE TABLE instructor (
	employeeID INTEGER PRIMARY KEY,
	expertise CHAR(20),
	FOREIGN KEY (employeeID) REFERENCES employee ON DELETE CASCADE);
grant select on instructor to public;

CREATE TABLE floorinfo (
	floorNum INTEGER PRIMARY KEY,
	fsize INTEGER,
	fhours CHAR(13));
grant select on floorinfo to public;

CREATE TABLE facility (
	facilityName CHAR(25) PRIMARY KEY,
	floorNum INTEGER,
	fsize INTEGER,
	fhours CHAR(13),
	FOREIGN KEY (floorNum) REFERENCES floorinfo ON DELETE SET NULL);
grant select on facility to public;

CREATE TABLE worksin (
	employeeID INTEGER,
	facilityName CHAR(25),
	PRIMARY KEY(employeeID, facilityName),
	FOREIGN KEY (employeeID) REFERENCES employee ON DELETE CASCADE,
	FOREIGN KEY (facilityName) REFERENCES facility ON DELETE CASCADE);
grant select on worksin to public;

CREATE TABLE fitnessclass(
	fcName CHAR(20) PRIMARY KEY,
	facilityName CHAR(25) NOT NULL,
	FOREIGN KEY (facilityName) REFERENCES facility ON DELETE CASCADE);
grant select on fitnessclass to public;

CREATE TABLE fitnessclassinfo(
	fcName CHAR(20),
	timeslot CHAR(13),
	dayOfTheWeek CHAR(10),
	instructorID INTEGER NOT NULL,
	PRIMARY KEY(fcName, dayOfTheWeek),
	FOREIGN KEY (instructorID) REFERENCES instructor(employeeID) ON DELETE CASCADE);
grant select on fitnessclassinfo to public;

CREATE TABLE eventlocation (
	eventName CHAR(30) PRIMARY KEY,
	facilityName CHAR(25),
	FOREIGN KEY (facilityName) REFERENCES facility ON DELETE SET NULL);
grant select on eventlocation to public;

CREATE TABLE gym (
	facilityName CHAR(25),
	numOfMachines INTEGER,
	numOfWeights INTEGER,
	PRIMARY KEY (facilityName),
	FOREIGN KEY (facilityName) REFERENCES facility ON DELETE SET NULL);
grant select on gym to public;

CREATE TABLE pools (
	facilityName CHAR(25),
	numOfPools INTEGER,
	isSaunaAvailable INTEGER,
	PRIMARY KEY (facilityName),
	FOREIGN KEY (facilityName) REFERENCES facility ON DELETE SET NULL);
grant select on pools to public;

CREATE TABLE changingroom (
	facilityName CHAR(25),
	numOfLockers INTEGER NOT NULL,
	numOfShowers INTEGER,
	PRIMARY KEY (facilityName),
	FOREIGN KEY (facilityName) REFERENCES facility ON DELETE SET NULL);
grant select on changingroom to public;

CREATE TABLE equipment (
	mEquipmentID INTEGER PRIMARY KEY,
	isMaintanceRequired INTEGER);
grant select on equipment to public;

CREATE TABLE equiptype (
	facilityName CHAR(25) PRIMARY KEY,
	etype CHAR(20),
	FOREIGN KEY (facilityName) REFERENCES facility ON  DELETE SET NULL);
grant select on equiptype to public;

CREATE TABLE facilityequip (
	equipmentID INTEGER,
	facilityName CHAR(25),
	PRIMARY KEY (equipmentID, facilityName),
	FOREIGN KEY (equipmentID) REFERENCES equipment ON DELETE CASCADE,
	FOREIGN KEY (facilityName) REFERENCES facility ON  DELETE SET NULL);
grant select on facilityequip to public;

CREATE TABLE registersin (
	customerID INTEGER,
	fitnessClassName CHAR(20),
	fitnessClassDay CHAR(10),
	PRIMARY KEY (customerID, fitnessClassName, fitnessClassDay),
	FOREIGN KEY (customerID) REFERENCES customer ON DELETE CASCADE,
	FOREIGN KEY (fitnessClassName, fitnessClassDay) REFERENCES fitnessclassinfo(fcName, dayOfTheWeek) ON DELETE CASCADE);
grant select on registersin to public;

CREATE TABLE locker (
	lockNumber INTEGER PRIMARY KEY,
	isTaken INT NOT NULL,
	locationName CHAR (25),
	FOREIGN KEY (locationName) REFERENCES changingroom(facilityName) ON DELETE SET NULL);
grant select on locker to public;

CREATE TABLE borrows (
	customerID INTEGER,
	lockNumber INTEGER,
	endingTime TIMESTAMP NOT NULL,
	PRIMARY KEY (customerID, lockNumber),
	FOREIGN KEY (customerID) REFERENCES customer ON DELETE CASCADE,
	FOREIGN KEY (lockNumber) REFERENCES locker ON DELETE CASCADE);
grant select on borrows to public;

insert into membership values ('Adult',33.99);
insert into membership values ('Youth',22.99);
insert into membership values ('Senior',20.99);
insert into membership values ('Half-day',20.99);
insert into membership values ('Student',22.99);
insert into membership values ('Drop In',10);

insert into customer values (101,'John Doe','123 Lake Str','V3N 1T7','6041234567','Adult');
insert into customer values (102,'Jane Doe','101 Bute Str','V8B 1B8','7785671234','Student');
insert into customer values (103,'Ben Smith','11 West Mall','V3A 3H5','2360987655','Senior');
insert into customer values (104,'Rose White','156 Haro Str','V7B 3A4','6729086744','Youth');
insert into customer values (105,'Violet Vu','304 Comp Str','V6C 1K3','6045551111','Half-day');
insert into customer values (106,'Steve Nash','123 Sports Str','V68 1K1','6045552222','Adult');
insert into customer values (107,'Daniel Smith','444 Rose Str','V5Y 8U7','6045551212','Senior');

insert into events values ('Open Pool Day', TO_DATE ('2020-03-05', 'YYYY-MM-DD'), 'Aquatic');
insert into events values ('Badminton Tournament', TO_DATE ('2020-04-12', 'YYYY-MM-DD'), 'Competitive');
insert into events values ('Table Tennis Tournament', TO_DATE ('2020-03-22', 'YYYY-MM-DD'), 'Competitive');
insert into events values ('Children Day', TO_DATE ('2020-05-01', 'YYYY-MM-DD'), 'Family');
insert into events values ('Open Yoga Day', TO_DATE ('2020-06-13', 'YYYY-MM-DD'), 'Wellness');

insert into participatesin values (101, 'Open Pool Day', TO_DATE ('2020-03-05', 'YYYY-MM-DD'));
insert into participatesin values (102, 'Open Pool Day', TO_DATE ('2020-03-05', 'YYYY-MM-DD'));
insert into participatesin values (103, 'Open Pool Day', TO_DATE ('2020-03-05', 'YYYY-MM-DD'));
insert into participatesin values (102, 'Badminton Tournament', TO_DATE ('2020-04-12', 'YYYY-MM-DD'));
insert into participatesin values (106, 'Badminton Tournament', TO_DATE ('2020-04-12', 'YYYY-MM-DD'));
insert into participatesin values (103, 'Table Tennis Tournament', TO_DATE ('2020-03-22', 'YYYY-MM-DD'));
insert into participatesin values (102, 'Table Tennis Tournament', TO_DATE ('2020-03-22', 'YYYY-MM-DD'));
insert into participatesin values (101, 'Table Tennis Tournament', TO_DATE ('2020-03-22', 'YYYY-MM-DD'));
insert into participatesin values (104, 'Children Day', TO_DATE ('2020-05-01', 'YYYY-MM-DD'));
insert into participatesin values (102, 'Children Day', TO_DATE ('2020-05-01', 'YYYY-MM-DD'));
insert into participatesin values (101, 'Children Day', TO_DATE ('2020-05-01', 'YYYY-MM-DD'));
insert into participatesin values (105, 'Open Yoga Day', TO_DATE ('2020-06-13', 'YYYY-MM-DD'));
insert into participatesin values (102, 'Open Yoga Day', TO_DATE ('2020-06-13', 'YYYY-MM-DD'));

insert into employee values (1000);
insert into employee values (1001);
insert into employee values (1002);
insert into employee values (1003);
insert into employee values (1004);
insert into employee values (1009);
insert into employee values (1010);
insert into employee values (1011);
insert into employee values (1020);
insert into employee values (1021);
insert into employee values (1022);
insert into employee values (1030);
insert into employee values (1031);
insert into employee values (1040);
insert into employee values (1041);
insert into employee values (1042);
insert into employee values (1043);
insert into employee values (1044);

insert into manager values (190, 1000);
insert into manager values (290, 1001);
insert into manager values (389, 1002);
insert into manager values (340, 1003);
insert into manager values (315, 1004);

insert into manages values ('Adult', 1000);
insert into manages values ('Youth', 1001);
insert into manages values ('Senior', 1002);
insert into manages values ('Half-day', 1003);
insert into manages values ('Drop In', 1004);

insert into volunteer values (1040, TO_DATE ('2020-03-29', 'YYYY-MM-DD'));
insert into volunteer values (1041, TO_DATE ('2020-03-29', 'YYYY-MM-DD'));
insert into volunteer values (1042, TO_DATE ('2020-04-30', 'YYYY-MM-DD'));
insert into volunteer values (1043, TO_DATE ('2020-04-15', 'YYYY-MM-DD'));
insert into volunteer values (1044, TO_DATE ('2020-05-10', 'YYYY-MM-DD'));

insert into instructor values (1011, 'Swimming');
insert into instructor values (1031, 'Hatha Yoga');
insert into instructor values (1009, 'Nutrition');
insert into instructor values (1021, 'Cardio');
insert into instructor values (1022, 'Weightlifting');

insert into floorinfo values (1, 12000, '6:00 -23:00');
insert into floorinfo values (2, 8000, '6:00 -23:00');
insert into floorinfo values (3, 8000, '6:00 -23:00');

insert into facility values ('Gymnasium 1', 1, 500,'6:00 - 21:00');
insert into facility values ('Gymnasium 2', 2, 500,'6:00 - 21:00');
insert into facility values ('Swimming Pool 1', 1, 2000,'6:00 - 21:00');
insert into facility values ('Swimming Pool 2', 1, 1000,'6:00 - 21:00');
insert into facility values ('Changing Room Female', 1, 1000,'6:00 - 23:00');
insert into facility values ('Changing Room Male', 1, 1000,'6:00 - 23:00');
insert into facility values ('Changing Room Unisex', 1, 1000,'6:00 - 23:00');
insert into facility values ('Active Studio 1', 2, 500,'6:00 - 23:00');
insert into facility values ('Active Studio 2', 1, 500,'6:00 - 20:00');
insert into facility values ('Reception Desk', 1, 600,'6:00 - 22:00');
insert into facility values ('Office', 3, 3000,'9:00 - 18:00');
insert into facility values ('Cycling Studio', 1, 800,'6:00 - 21:00');

insert into worksin values (1001, 'Reception Desk');
insert into worksin values (1002, 'Office');
insert into worksin values (1010, 'Swimming Pool 1');
insert into worksin values (1020, 'Gymnasium 1');
insert into worksin values (1030, 'Active Studio 2');

insert into fitnessclass values ('Flow Yoga', 'Active Studio 1');
insert into fitnessclass values ('Pilates', 'Active Studio 2');
insert into fitnessclass values ('Bootcamp', 'Gymnasium 1');
insert into fitnessclass values ('Cycling', 'Gymnasium 2');
insert into fitnessclass values ('Beginner Swimming', 'Swimming Pool 1');
insert into fitnessclass values ('Water Aerobics', 'Swimming Pool 1');

insert into fitnessclassinfo values ('Flow Yoga', '14:00 - 15:00', 'Monday', 1031);
insert into fitnessclassinfo values ('Pilates', '15:00 - 16:00', 'Monday', 1031);
insert into fitnessclassinfo values ('Pilates', '15:00 - 16:00', 'Friday', 1031);
insert into fitnessclassinfo values ('Bootcamp', '9:00 - 10:00', 'Wednesday', 1021);
insert into fitnessclassinfo values ('Cycling', '12:00 - 1:00', 'Thursday', 1022);
insert into fitnessclassinfo values ('Beginner Swimming', '9:00 - 10:00', 'Monday', 1011);
insert into fitnessclassinfo values ('Beginner Swimming', '9:00 - 10:00', 'Wednesday', 1011);
insert into fitnessclassinfo values ('Beginner Swimming', '9:00 - 10:00', 'Friday', 1011);
insert into fitnessclassinfo values ('Water Aerobics', '10:00 - 11:00', 'Wednesday', 1011);

insert into eventlocation values ('Open Pool Day', 'Swimming Pool 1');
insert into eventlocation values ('Badminton Tournament', 'Gymnasium 1');
insert into eventlocation values ('Table Tennis Tournament', 'Gymnasium 2');
insert into eventlocation values ('Children Day', 'Gymnasium 1');
insert into eventlocation values ('Open Yoga Day', 'Active Studio 2');

insert into gym values ('Gymnasium 1', 100, 50);
insert into gym values ('Gymnasium 2', 75, 60);

insert into pools values ('Swimming Pool 1', 2, 1);
insert into pools values ('Swimming Pool 2', 1, 0);

insert into changingroom values ('Changing Room Female', 100, 10);
insert into changingroom values ('Changing Room Male', 100, 10);
insert into changingroom values ('Changing Room Unisex', 150, 15);

insert into equipment values (1000, 0);
insert into equipment values (1001, 1);
insert into equipment values (1002, 1);
insert into equipment values (1003, 0);
insert into equipment values (1004, 0);

insert into equiptype values ('Gymnasium 1', 'Treadmill');
insert into equiptype values ('Gymnasium 2', 'Weights');
insert into equiptype values ('Active Studio 1', 'Yoga mat');
insert into equiptype values ('Swimming Pool 1', 'Swimming board');
insert into equiptype values ('Cycling Studio', 'Indoor bike');

insert into facilityequip values (1000, 'Gymnasium 1');
insert into facilityequip values (1001, 'Gymnasium 2');
insert into facilityequip values (1002, 'Active Studio 1');
insert into facilityequip values (1003, 'Swimming Pool 1');
insert into facilityequip values (1004, 'Cycling Studio');

insert into registersin values (101, 'Flow Yoga', 'Monday');
insert into registersin values (101, 'Beginner Swimming', 'Wednesday');
insert into registersin values (101, 'Water Aerobics', 'Wednesday');
insert into registersin values (101, 'Beginner Swimming', 'Friday');
insert into registersin values (101, 'Pilates', 'Friday');
insert into registersin values (101, 'Bootcamp', 'Wednesday');
insert into registersin values (101, 'Cycling', 'Thursday');
insert into registersin values (102, 'Pilates', 'Monday');
insert into registersin values (102, 'Pilates', 'Friday');
insert into registersin values (103, 'Pilates', 'Friday');
insert into registersin values (104, 'Pilates', 'Friday');
insert into registersin values (103, 'Bootcamp', 'Wednesday');
insert into registersin values (104, 'Cycling', 'Thursday');

insert into locker values (100, 1, 'Changing Room Female');
insert into locker values (200, 1, 'Changing Room Female');
insert into locker values (300, 1, 'Changing Room Unisex');

insert into borrows values (102, 100, TO_TIMESTAMP('2020-04-20 15:30:10.00', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into borrows values (104, 200, TO_TIMESTAMP('2020-04-20 16:30:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into borrows values (105, 300, TO_TIMESTAMP('2020-04-20 17:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'));
