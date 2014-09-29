package reasonablecare;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import asg.cliche.Command;
import asg.cliche.Param;

/**
 * Shell containing the operations corresponding to the views for the Staff user class
 *
 */
@SuppressWarnings("unused")
public class NurseShell {

  Connection connection;
  int id;
  private final CommonStatements commonStatements;
  
  public NurseShell(Connection connection, int id) throws SQLException {
    this.connection = connection;
    this.id = id;
    this.commonStatements = new CommonStatements(connection);
  }
  
  @Command(description="Add a consultation, including a student ID and consultation notes.")
  public String addConsultation(
		  @Param(name="nurseId", description = "Nurse ID") String strNurseId,
		  @Param(name="studentId", description = "Student ID") String strStudentId,
		  @Param(name="nurseNotes", description = "Consultation notes, surrounded in single quotes")String nurseNotes)
  throws SQLException{
	  int studentId;
	  int nurseId;
	  try{
		  studentId = Integer.parseInt(strStudentId);
		  nurseId = Integer.parseInt(strNurseId);
	  }
	  catch(Exception e){
		  return "Error parsing student ID.";
	  }
	  PreparedStatement addConsultation = null;
	  PreparedStatement addMakesConsultation = null;
	  
	  String addConsultationString = "INSERT INTO Consultation(timeofConsultation, nurseNotes) VALUES(?, ?)";
	  String addMakesConsultationString = "INSERT INTO MakesConsultation(studentId, nurseId, consultationID) " +
			  "VALUES(?, ?, ?)";
	  
	  try{
		  connection.setAutoCommit(false);
		  Date now = new Date();		  
		  
		  Timestamp timestamp = new Timestamp(now.getTime());
		  
		  int consultationId = -1;
		  // Create the consultation creation statement and inject the parameters
		  addConsultation = connection.prepareStatement(addConsultationString, new String[] {"ConsultationId"});
		  addConsultation.setTimestamp(1, timestamp);
		  addConsultation.setString(2,  nurseNotes);
		  
		  addConsultation.executeUpdate();
		  
		  ResultSet generatedKeys = addConsultation.getGeneratedKeys();
		  if(generatedKeys != null && generatedKeys.next()){
			  consultationId = generatedKeys.getInt(1);
		  }
		  
		  // Create the makesConsultation creation statement and inject parameters
		  addMakesConsultation = connection.prepareStatement(addMakesConsultationString);
		  
		  addMakesConsultation.setInt(1, studentId);
		  addMakesConsultation.setInt(2, nurseId);
		  addMakesConsultation.setInt(3, consultationId);
		  
		  int rowsUpdated = addMakesConsultation.executeUpdate();
		  // Commit the transaction
		  connection.commit();
		  return "Created new consultation with ID " + consultationId;
	  }
	  catch(Exception e){
		  // If there was an exception, roll back the transaction
		  connection.rollback();
		  return "Error adding consultation: " + e;
	  }
	  finally{
		  if(addConsultation != null){
			  addConsultation.close();
		  }
		  if(addMakesConsultation != null){
			  addMakesConsultation.close();
		  }
		  connection.setAutoCommit(true);
	  }
	  
  }
 
  

@Command(description = "Update consultation information")
public void updateConsultation() throws IOException, SQLException{
	   commonStatements.updateConsultations();
}  
  
  @Command(description="Update nurse information, including name and password.")
  public String updateNurse(
		  @Param(name="password", description = "New password surrounded in single quotes") String password,
		  @Param(name="nurseName", description = "New name surrounded in single quotes") String nurseName) 
				  throws SQLException{
	  
	  int nurseId = id;
	  
	  PreparedStatement updateNurse = null;
	  
	  String updateString = "UPDATE Nurse SET password = ?, nurseName = ? WHERE NurseId = ?";
	  
	  try {
		  updateNurse = connection.prepareStatement(updateString);
		  updateNurse.setString(1, password);
		  updateNurse.setString(2,  nurseName);
		  updateNurse.setInt(3,  nurseId);
		  
		  int numRowsUpdated = updateNurse.executeUpdate();
		  return "Updated nurse information for nurse with ID: " + id;
	  }
	  catch(SQLException e){
		  return "Error updating information for nurse with ID: " + nurseId + ". " + e;
	  }
	  finally {
		  if(updateNurse != null){
			  updateNurse.close();
		  }
	  }
  }
 
  
  @Command(description="Check all doctors records, including number and specialization")
  public void viewDoctors () throws IOException, SQLException{
	  commonStatements.viewDoctors();	  
  }
  
  /**
   * View a student's past appointments and consultations
   * @param strStudentId
   * @return
   * @throws Exception
   */
  @Command(description="Check a student's medical records, including past appointments and consultations")
  public String checkStudentRecords(
		  @Param(name="studentId", description = "Student's ID to find appointments for") String strStudentId)
		  throws Exception {	 
	  return commonStatements.checkStudentRecord(strStudentId);
  }
  
  @Command(description = "Show the list of doctors a student has seen in the past.")
  public Object showStudentsDoctors(
		  @Param(name="studentId", description = "Student's ID")String strStudentId) 
		  throws SQLException{
	  
	  int studentId;
	  try{
		  studentId = Integer.parseInt(strStudentId);
	  }
	  catch(Exception e){
		  return "Error parsing student ID.";
	  }
	  
	  PreparedStatement getDoctors = null;
	  
	  String getDoctorsString = "SELECT * FROM Doctor WHERE DoctorId IN " + 
	  "(SELECT DoctorId FROM Appointment a JOIN MakesAppointment ma ON " +
	  "a.AppointmentID = ma.AppointmentId WHERE appointmentTime < SYSDATE AND " +
	  "ma.StudentId = ?)";
	  
	  Table table = new Table("doctorName", "password", "phoneNumber", "specialization");
	  
	  try{
		  getDoctors = connection.prepareStatement(getDoctorsString);
		  getDoctors.setInt(1, studentId);
		  
		  ResultSet doctors = getDoctors.executeQuery();
		  
		  String x = "something";
		  while (doctors.next()) {
			table.add(doctors.getString(1), doctors.getString(2), doctors.getString(3),
					doctors.getString(4));
		  }
	  
		  return table;
	  }
	  catch(Exception e){
		  return "Error retrieving student's doctors: " + e;
	  }
	  finally{
		  if(getDoctors != null){
			  getDoctors.close();
		  }
	  }
  }

}
