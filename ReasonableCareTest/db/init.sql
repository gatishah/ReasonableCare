VAR john_id NUMBER
INSERT INTO Doctor(doctorName, password, phoneNumber, specialization)
VALUES('John Terry', 'password', '919-100-2101', 'Oncology Surgeon')
RETURNING doctorId INTO :john_id;

INSERT INTO Doctor(doctorName, password, phoneNumber, specialization)
VALUES('Mary Jobs', 'password', '919-500-1212', 'Oncology Surgeon');

INSERT INTO Nurse(nurseName, password) 
VALUES('Rebecca Johnston', 'password');

INSERT INTO Nurse(nurseName, password) 
VALUES('Davy Jacobs', 'password');

INSERT INTO Staff(staffName, password)
VALUES('Michael Smith', 'password');

VAR jason_id NUMBER
INSERT INTO Student(studentName, password, healthInsuranceProviderName,
healthInsurancePolicyNumber, startingDate)
VALUES('Jason Hunter', 'password', 'Acme', 
123456, '15-MAR-2011')
RETURNING studentId INTO :jason_id;

VAR dale_id NUMBER
INSERT INTO Student(studentName, password, startingDate)
VALUES('Dale Steyn', 'password', '1-JAN-2011')
RETURNING studentId INTO :dale_id;

VAR appt_id NUMBER
INSERT INTO Appointment(reasonForVisit, type, appointmentTime,
doctorNotes, cost) VALUES('Orthopedics', 'Office Visit', 
TO_DATE('15.03.2011 09:00:00', 'dd.mm.yyyy hh24:mi:ss'), 
'Boken bone: Prescribed pain killer', 30) RETURNING appointmentId INTO :appt_id;

INSERT INTO MakesAppointment(studentId, doctorId, appointmentId)
VALUES(:jason_id, :john_id, :appt_id);

INSERT INTO Appointment(reasonForVisit, type, appointmentTime,
doctorNotes, cost) VALUES('Vaccination', 'Vaccination',
TO_DATE('21.04.2014 16:00:00', 'dd.mm.yyyy hh24:mi:ss'),
'', 100) RETURNING appointmentId INTO :appt_id;

INSERT INTO MakesAppointment(studentId, doctorId, appointmentId)
VALUES(:jason_id, :john_id, :appt_id);



INSERT INTO Appointment(reasonForVisit, type, appointmentTime,
doctorNotes, cost) VALUES('Vaccination', 'Vaccination',
TO_DATE('16.03.2011 09:00:00', 'dd.mm.yyyy hh24:mi:ss'),
'', 30) RETURNING appointmentId INTO :appt_id;

INSERT INTO MakesAppointment(studentId, doctorId, appointmentId)
VALUES(:dale_id, :john_id, :appt_id);

INSERT INTO Appointment(reasonForVisit, type, appointmentTime,
doctorNotes, cost) VALUES('Vaccination', 'Vaccination',
TO_DATE('16.03.2011 09:30:00', 'dd.mm.yyyy hh24:mi:ss'),
'', 30) RETURNING appointmentId INTO :appt_id;

INSERT INTO MakesAppointment(studentId, doctorId, appointmentId)
VALUES(:dale_id, :john_id, :appt_id);

INSERT INTO Appointment(reasonForVisit, type, appointmentTime,
doctorNotes, cost) VALUES('Vaccination', 'Vaccination',
TO_DATE('16.03.2011 10:00:00', 'dd.mm.yyyy hh24:mi:ss'),
'', 30) RETURNING appointmentId INTO :appt_id;

INSERT INTO MakesAppointment(studentId, doctorId, appointmentId)
VALUES(:dale_id, :john_id, :appt_id);

COMMIT;

