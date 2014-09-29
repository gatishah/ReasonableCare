DROP TABLE Student CASCADE CONSTRAINTS;
DROP TABLE Doctor CASCADE CONSTRAINTS;
DROP TABLE Appointment CASCADE CONSTRAINTS;
DROP TABLE Nurse CASCADE CONSTRAINTS;
DROP TABLE Staff CASCADE CONSTRAINTS;
DROP TABLE Consultation CASCADE CONSTRAINTS;
DROP TABLE makesAppointment CASCADE CONSTRAINTS;
DROP TABLE makesConsultation CASCADE CONSTRAINTS;

DROP SEQUENCE studentID_sequence;
DROP SEQUENCE doctorID_sequence;
DROP SEQUENCE AppointmentID_sequence;
DROP SEQUENCE NurseID_sequence;
DROP SEQUENCE staffID_sequence;
DROP SEQUENCE ConsultationID_sequence;

create table Student (
StudentID int primary key, 
studentName varchar2(32) not null, 
password varchar2(32) not null, 
healthInsuranceProviderName varchar2(32),
healthInsurancePolicynumber varchar2(32), 
startingDate date
);

create table Doctor (
doctorID int primary key, 
doctorName varchar(32) not null, 
password varchar(32) not null, 
phoneNumber varchar(32) not null, 
specialization varchar(32) not null
);

create table Appointment (
AppointmentID int primary key, 
reasonForVisit varchar(512) not null, 
type varchar(32) not null, 
appointmentTime timestamp not null,  
doctorNotes varchar(512), 
cost int
);

create table Nurse (
NurseID int primary key, 
nurseName varchar(32) not null, 
password varchar(32) not null
);

create table Staff (
staffID int primary key, 
staffName varchar(32) not null, 
password varchar(32) not null
);

create table makesAppointment(
StudentID int,
DoctorID int,
AppointmentID int,
primary key(StudentID, AppointmentID, DoctorID),
foreign key (StudentID) references Student(StudentID) on delete cascade,
foreign key (AppointmentID) references Appointment (AppointmentID) on delete cascade,
foreign key (DoctorID) references Doctor(doctorID) on delete cascade
);

create table Consultation(
ConsultationID int primary key, 
timeOfConsultation timestamp not null, 
nurseNotes varchar (512) not null
);

create table makesConsultation(
StudentID int,
NurseID int,
ConsultationID int,
primary key(StudentID, ConsultationID, NurseID),
foreign key (StudentID) references Student(StudentID) on delete cascade,
foreign key (ConsultationID) references Consultation (ConsultationID) on delete cascade,
foreign key (NurseID) references Nurse(NurseID) on delete cascade
);

CREATE SEQUENCE StudentID_sequence
START WITH 1000
INCREMENT BY 1;

CREATE SEQUENCE doctorID_sequence
START WITH 2000
INCREMENT BY 1;

CREATE SEQUENCE AppointmentID_sequence 
START WITH 3000
INCREMENT BY 1;

CREATE SEQUENCE NurseID_sequence 
START WITH 4000
INCREMENT BY 1;

CREATE SEQUENCE staffID_sequence 
START WITH 5000
INCREMENT BY 1;

CREATE SEQUENCE ConsultationID_sequence 
START WITH 6000
INCREMENT BY 1;

CREATE OR REPLACE TRIGGER studentID_trigger
BEFORE INSERT
ON Student
REFERENCING NEW AS N
FOR EACH ROW
WHEN (N.StudentID IS NULL)
BEGIN
SELECT StudentID_sequence.nextval INTO :N.StudentID FROM dual;
END;
/

CREATE OR REPLACE TRIGGER doctorID_trigger
BEFORE INSERT
ON Doctor
REFERENCING NEW AS N
FOR EACH ROW
WHEN (N.DoctorID IS NULL)
BEGIN
SELECT doctorID_sequence.nextval INTO :N.doctorID FROM dual;
END;
/

CREATE OR REPLACE TRIGGER AppointmentID_trigger 
BEFORE INSERT
ON Appointment
REFERENCING NEW AS N
FOR EACH ROW
WHEN (N.AppointmentID IS NULL)
BEGIN
SELECT AppointmentID_sequence.nextval INTO :N.AppointmentID FROM dual;
END;
/

CREATE OR REPLACE TRIGGER NurseID_trigger 
BEFORE INSERT
ON Nurse
REFERENCING NEW AS N
FOR EACH ROW
WHEN (N.NurseID IS NULL)
BEGIN
SELECT NurseID_sequence.nextval INTO :N.NurseID FROM dual;
END;
/

CREATE OR REPLACE TRIGGER StaffID_trigger 
BEFORE INSERT
ON Staff
REFERENCING NEW AS N
FOR EACH ROW
WHEN (N.StaffID IS NULL)
BEGIN
SELECT staffID_sequence.nextval INTO :N.staffID FROM dual;
END;
/

CREATE OR REPLACE TRIGGER ConsultationID_trigger 
BEFORE INSERT
ON Consultation
REFERENCING NEW AS N
FOR EACH ROW
WHEN (N.ConsultationID IS NULL)
BEGIN
SELECT ConsultationID_sequence.nextval INTO :N.ConsultationID FROM dual;
END;
/
