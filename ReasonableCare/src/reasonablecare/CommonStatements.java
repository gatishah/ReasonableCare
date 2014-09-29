package reasonablecare;

import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Utility class containing methods common to multiple user types
 *
 */

public class CommonStatements implements AutoCloseable {

  private static BufferedReader br = new BufferedReader(new InputStreamReader(
      System.in));

  private static Statement stm;
	static ResultSet result = null;

  Connection connection;

  /**
   * Constructor for CommonStatements - allows shells to use these methods
   * @param connection
   * @throws SQLException
   */
  public CommonStatements(Connection connection) throws SQLException {
    this.connection=connection;
  	if(stm != null) {
  	  stm.close();
  	}
    stm = connection.createStatement();
  }

  @Override
  public void close() throws SQLException {
    if (stm != null) {
      stm.close();
    }
  }

  /**
   * Allows a user to update a consultation.
   * 
   * @throws IOException
   * @throws SQLException
   */
  public void updateConsultations() throws IOException, SQLException {
		int atype1=0;
		System.out.println("Enter the consultation id you want to update");
		atype1 = Integer.parseInt(br.readLine());
		
		//validate consultation id and return attributes if valid
		result = stm.executeQuery("SELECT consultationid from consultation");
		int ID3=0;
		int flag=0;
		while (result.next()) 
		{
			ID3=result.getInt("consultationid");
			if(atype1==ID3)
				{
				flag=1;
				break;
				}
		}// end of while
		if(flag==0)
			{
			System.out.println("Not a valid consultation id");
			updateConsultations();
			}
		else
		{
			String b="";
				int choice=0;
				//allow user to update or append the nurse's notes
				System.out.println("1.Do you want to add to your note\n"
						+ "2.Change the contents of the note");
				choice=Integer.parseInt(br.readLine());
				if(choice==1)
				{
					String ID="";
					result = stm.executeQuery("SELECT * FROM Consultation WHERE Consultationid="+atype1+"");
					while (result.next()) 
					{
						ID=result.getString("NURSENOTES");
						
					}// end of while
					System.out.println ("Enter the additional part of the note");
					b=(br.readLine());
					b=ID+" "+b;
				}
				if(choice==2)
				{	
					System.out.println ("Enter the changed value");
					b=(br.readLine());
				}
			stm.executeUpdate("update Consultation set NURSENOTES =' " + b + " 'where Consultationid=" + atype1);	
			System.out.println("Record Updated");
			System.out.println("Your updated details are as shown:");
			result = stm.executeQuery("SELECT * FROM Consultation WHERE Consultationid="+atype1+"");	
			if(result.next())
			{
				do
				{
					System.out.println("ID: "+result.getString("Consultationid"));
					System.out.println("Time: "+result.getString("TIMEOFCONSULTATION"));
					System.out.println("Nurse's Notes: "+result.getString("NURSENOTES"));
					
				}while (result.next());
			}
		}}
		
	
  /**
   * Class allowing the updating of the attributes of a nurse
   * 
   * @param z NurseID
   * @throws IOException
   * @throws SQLException
   */
  public void updatenurseinformation(int z) throws IOException, SQLException {
    int userid2 = z;
    int y;
    String b;
    out.println("Enter the attribute to be updated: \n1.Name \n2. Password");
    y = Integer.parseInt(br.readLine());
    out.println("Enter the changed value");
    b = (br.readLine());
    if (b.isEmpty()) {
      out.println("invalid input");
      updatenurseinformation(userid2);
    }

    switch (y) {
    case 1:
      stm.executeUpdate("update Nurse set nursename =' " + b
          + " 'where nurseid=" + z);
      out.println("Record Updated");
      printNurseInfo(z);
      break;
    case 2:

      stm.executeUpdate("update nurse set password =' " + b
          + " 'where nurseid=" + z);
      out.println("Record Updated");
      printNurseInfo(z);

      break;
    }// end of switch
  }

  private void printNurseInfo(int nurseId) throws SQLException {
    out.println("Your updated details are as shown:");

    // get the details of the student whose record is updated
    try (ResultSet result = stm
        .executeQuery("SELECT * FROM nurse WHERE nurseid=" + nurseId + "")) {

      while (result.next()) {
        out.println("ID: " + result.getString("nurseid"));
        out.println("Name: " + result.getString("nursename"));
        out.println("Password: " + result.getString("password"));
      }
    }
  }
  
  /**
   * Shows selected attributes of all doctors in a neat table when called
   * 
   * @throws IOException
   * @throws SQLException
   */
  public void viewDoctors() throws IOException, SQLException {
	
		result = stm.executeQuery("SELECT * FROM doctor");	//get the details of the all the doctors
		if(result.next())
		{
			do
			{
				System.out.println("ID: "+result.getString("doctorid"));
				System.out.println("Name: "+result.getString("doctorname"));
				System.out.println("Specialization: "+result.getString("specialization"));
				System.out.println("Phone Number: "+result.getString("Phonenumber"));
				System.out.println("-----------------------------------------------");

			}while (result.next());
		}
  }
  
/**
 * Allows the updating of the attributes of a doctor
 * 
 * @param z Doctor ID
 * @throws IOException
 * @throws SQLException
 */
  public void updatedoctorinformation(int z) throws IOException, SQLException {
    int userid2 = z;
    int y;
    String b;
    out.println("Enter the attribute to be updated: \n1.Name \n2.Password\n3.Specialization\n4.Phone Number");
    y = Integer.parseInt(br.readLine());
    out.println("Enter the changed value");
    b = (br.readLine());
    if (b.isEmpty()) {
      out.println("invalid input");
      updatedoctorinformation(userid2);
    }

    switch (y) {
    case 1:
      stm.executeUpdate("update doctor set doctorname ='" + b
          + "' where doctorid=" + z);
      out.println("Record Updated");
      printDoctorInfo(z);
      break;

    case 2:
      stm.executeUpdate("update doctor set PASSWORD ='" + b
          + " ' where doctorid=" + z);
      out.println("Record Updated");
      printDoctorInfo(z);
      break;
    case 3:
    
    	while (!verifySpecialty(b))
    	{
    		System.out.println("Invalid Specialization.  Enter a valid one.\n"
    				+"Valid Choices are :\n "
    				+ "General Physician\n"
    				+ "Endocrinologist\n"
    				+ "Psychiatrist\n"
    				+ "Orthopedic Surgeon\n"
    				+ "Gynaceologist\n"
    				+ "Nephrologist\n"
    				+ "ENT Specialist\n"
    				+ "Cardiologist\n"
    				+ "Oncology Surgeon \n");
    		System.out.println("Enter Specialization: ");
    		
    		b = br.readLine().trim();	
    	}
      stm.executeUpdate("update doctor set Specialization ='" + b
          + "' where doctorid=" + z);
      out.println("Record Updated");
      printDoctorInfo(z);
      break;
    case 4:
      stm.executeUpdate("update doctor set phonenumber ='" + b
          + "' where doctorid=" + z);
      out.println("Record Updated");
      printDoctorInfo(z);
      break;
    }// end of switch
  }

  /**
   * Prints the information for a doctor - used after editing
   * 
   * @param doctorId
   * @throws SQLException
   */
  private void printDoctorInfo(int doctorId) throws SQLException {
    out.println("Your updated details are as shown:");

    // get the details of the student whose record is updated
    try (ResultSet result = stm
        .executeQuery("SELECT * FROM doctor WHERE doctorid=" + doctorId + "")) {

      while (result.next()) {
        out.println("ID: " + result.getString("doctorid"));
        out.println("Name: " + result.getString("doctorname"));
        out.println("Password: " + result.getString("password"));
        out.println("Specialization: " + result.getString("specialization"));
        out.println("Phone Number: " + result.getString("Phonenumber"));
      }
    }
  }

  /**
   * Allows the updating of attributes for a staff member
   * 
   * @param z StaffID
   * @throws IOException
   * @throws SQLException
   */
  public void updatestaffinformation(int z) throws IOException, SQLException {
    int userid2 = z;
    int y;
    String b;
    out.println("Enter the attribute to be updated: \n1.Name \n2. Password");
    y = Integer.parseInt(br.readLine());
    out.println("Enter the changed value");
    b = (br.readLine());
    if (b.isEmpty()) {
      out.println("invalid input");
      updatestaffinformation(userid2);
    }

    switch (y) {
    case 1:
      stm.executeUpdate("update staff set staffname =' " + b
          + " 'where staffid=" + z);
      out.println("Record Updated");
      printStaffInfo(z);
      break;
    case 2:

      stm.executeUpdate("update staff set password =' " + b
          + " 'where staffid=" + z);
      out.println("Record Updated");
      printStaffInfo(z);

      break;
    }// end of switch
  }

  private void printStaffInfo(int staffId) throws SQLException {
    out.println("Your updated details are as shown:");

    // get the details of the student whose record is updated
    try (ResultSet result = stm
        .executeQuery("SELECT * FROM staff WHERE staffid=" + staffId + "")) {

      while (result.next()) {
        out.println("ID: " + result.getString("staffid"));
        out.println("Name: " + result.getString("staffname"));
        out.println("Password: " + result.getString("password"));
      }
    }
  }

  /**
   * Allows the updating of student attributes for a selected student
   * 
   * @param z StudentID
   * @throws IOException
   * @throws SQLException
   */
  public void updatestudentinformation(int z) throws IOException, SQLException {
    int userid2 = z;
    int y;
    String b;
    out.println("Enter the attribute to be updated: \n1.Name \n2.HealthInsurance Provider Name\n3.HealthInsurance Provider Number \n4.Password\n5.Starting semester");
    y = Integer.parseInt(br.readLine());
    out.println("Enter the changed value");
    b = (br.readLine());
    if (b.isEmpty()) {
      out.println("invalid input");
      updatestudentinformation(userid2);
    }

    switch (y) {
    case 1:
      stm.executeUpdate("update Student set studentname =' " + b
          + " 'where studentid=" + z);
      out.println("Record Updated");
      printStudentInfo(z);
      break;
    case 2:
    	/*
    	 * If health insurance provider changes, policy number must change too
    	 */
      stm.executeUpdate("update Student set HEALTHINSURANCEPROVIDERNAME =' "
          + b + " 'where studentid=" + z);
      do {
        out.println("Enter the HEALTHINSURANCE POLICY NUMBER");
        b = (br.readLine());
      } while (b.isEmpty());
      stm.executeUpdate("update Student set HEALTHINSURANCEPOLICYNUMBER =' "
          + b + " 'where studentid=" + z);
      out.println("Record Updated");
      printStudentInfo(z);

      break;
    case 3:
      stm.executeUpdate("update Student set HEALTHINSURANCEPOLICYNUMBER =' "
          + b + " 'where studentid=" + z);
      out.println("Record Updated");
      printStudentInfo(z);
      break;
    case 4:
      stm.executeUpdate("update Student set PASSWORD =' " + b
          + " 'where studentid=" + z);
      out.println("Record Updated");
      printStudentInfo(z);
      break;
    case 5:
      stm.executeUpdate("update Student set startingdate =' " + b
          + " 'where studentid=" + z);
      out.println("Record Updated");
      printStudentInfo(z);
      break;
    }// end of switch
  }// end of updatestudentinformation

  /**
   * Prints all attributes for a selected student - used after updating
   * 
   * @param studentId
   * @throws SQLException
   */
  private void printStudentInfo(int studentId) throws SQLException {
    out.println("Your updated details are as shown:");

    // get the details of the student whose record is updated
    try (ResultSet result = stm
        .executeQuery("SELECT * FROM student WHERE studentid=" + studentId + "")) {

      while (result.next()) {
        out.println("ID: " + result.getString("studentID"));
        out.println("Name: " + result.getString("studentname"));
        out.println("Password: " + result.getString("password"));
        out.println("HealthInsurance Provider Name: "
            + result.getString("HEALTHINSURANCEPROVIDERNAME"));
        out.println("HealthInsurance Provider Number: "
            + result.getString("HEALTHINSURANCEPOLICYNUMBER"));
        out.println("Starting semester: " + result.getString("startingdate"));
      }
    }
  }
  
  /**
   * Print Table of doctors
   * @return Table of doctors with their specializations, names, and doctorIDs
   * @throws SQLException
   */
  
  public Table getDoctors() throws SQLException {
		try (Statement statement = connection.createStatement()) {

		// Get records from the Student table
		try (ResultSet rs = statement
				.executeQuery("SELECT specialization, doctorID, doctorName "							+ "FROM Doctor ORDER BY specialization, doctorID")) {

			Table table = new Table("Specialization", "Doctor ID",
					"Doctor Name");

			while (rs.next()) {
				table.add(rs.getString(1), rs.getInt(2), rs.getString(3));
			}
			
			return table;
		}
	}
}
  
  /**
   * Creates an appointment in the db given all of the information
   * 
   * @throws SQLException 
   * 
   */
  public Object makeAppointment(int studentID, int doctorID, String type, String reason,
		  java.sql.Timestamp time, int cost) throws SQLException
  {
	  String makeAppt = "insert into appointment(reasonForVisit,type,appointmentTime,"
				+ "doctorNotes, cost) values(?,?,?,?,?)";
	  
	  PreparedStatement apptStmt = null;
	  PreparedStatement makesApptStmt = null;
	  ResultSet rs = null;
	  
	  int apptID = 0;
	  String associateAppt = "insert into makesAppointment(studentID,doctorID,appointmentID)"
			 	+ "values (?,?,?)";
	  
	  try {
		  connection.setAutoCommit(false);
		  // Create the appointment prepared statement and inject parameters
		  apptStmt = connection.prepareStatement(makeAppt,
			        new String[] { "AppointmentID" });

	      apptStmt.setString(1, reason);
	      apptStmt.setString(2, type);
	      apptStmt.setTimestamp(3, time); 
	      apptStmt.setString(4, "");
	      apptStmt.setInt(5, 0);
	      apptStmt.executeUpdate();
	      
	      rs = apptStmt.getGeneratedKeys();
	      if (rs != null && rs.next()) {
	        apptID = rs.getInt(1);
	      }
	      // Create the makesAppointment prepared statement and inject parameters
	      makesApptStmt = connection.prepareStatement(associateAppt);
			 
	      makesApptStmt.setInt(1, studentID);
	      makesApptStmt.setInt(2, doctorID);
	      makesApptStmt.setInt(3, apptID);
	      makesApptStmt.executeUpdate();
	      // Commit the transaction
	      connection.commit();
		      
	      return "Created new Appointment with id = "+apptID+"\n";
	}
  	catch(SQLException e){
  		// If there was an exception for ther insert statements, rollback the transaction
  		connection.rollback();
  		return "Failed to create appointment: " + e;
  	}
	finally{
		close(apptStmt);
		close(makesApptStmt);
		close(rs);
	}		
  }
  
  /**
   * Show the available appointment times for a given doctor on a given date
   * @param doctorID
   * @param date YYYY-MM-DD
   * @return appointment Table
   * @throws Exception
   */
  public Object showAvailableTimes(String doctorID, String date) throws Exception {
		
		java.sql.Date apptDate;
		int docID;
		
		//validate input and ensure valid doctor and date
		if (!validateDoctorID(doctorID))
			return "Invalid Doctor ID";
		else 
			docID=Integer.parseInt(doctorID);
		
		try{
			apptDate = java.sql.Date.valueOf(date);
		}
		catch (IllegalArgumentException e){
			return "Invalid Date Format: Must be YYYY-MM-DD";
		}
		
		//set a string equal to the day of week for the date
	      String dayName = String.format("%tA", apptDate);
	      
	    //system is closed on Sundays.
	    if (dayName.equals("Sunday"))
	    	return "The Health Center is Closed on Sundays";
		
		//Build Table of available Times 
		Table appointmentTable = new Table ("Available Appointment Times on "+ apptDate);
		
		//return appointments for a specific doctor on a specific date
		String sql="select a.appointmenttime from (appointment a natural join makesappointment ma) "
				+ "where ma.doctorID= ? and to_char(a.appointmenttime, 'YYYY-MM-DD') = ?";
		
		try (PreparedStatement stm = connection.prepareStatement(sql,
		        new String[] { "AppointmentTime" })) {

		      stm.setInt(1, docID);
		      stm.setString(2, date);
		      ResultSet rs = stm.executeQuery();
		      
		      //import result set into an ArrayList
		      ArrayList<java.sql.Timestamp> scheduledAppointments = 
		    		  new ArrayList<java.sql.Timestamp>();
		      while (rs.next()) 
		      {
		    	  scheduledAppointments.add(rs.getTimestamp(1));
		      }
		      
		    //Timestamp representing actual current time
		      java.util.Calendar calendar = Calendar.getInstance();
		      java.sql.Timestamp now = new java.sql.Timestamp(calendar.getTime().getTime());
		           
		      //Build AppointmentTable
		      
		      java.sql.Timestamp currentTime;
		      if (dayName.equals("Saturday")) //open 10-2
		      {
		    	  for (int hour=10; hour<14; hour++)
					{
						for (int minute=0; minute<31; minute+=30)
						{
							currentTime = java.sql.Timestamp.valueOf(apptDate+" "+hour+":"+minute+":00");
							if (!scheduledAppointments.contains(currentTime) 
									&& now.before(currentTime))
								appointmentTable.add(currentTime);
							else scheduledAppointments.remove(currentTime);
						}
					}
		      }
		      else //weekday - open 8-5
		      {
		    	  for (int hour=8; hour<17; hour++)
					{
						for (int minute=0; minute<31; minute+=30)
						{
							currentTime = java.sql.Timestamp.valueOf(apptDate+" "+hour+":"+minute+":00");
							if (!scheduledAppointments.contains(currentTime)
									&& now.before(currentTime))
								appointmentTable.add(currentTime);
							else scheduledAppointments.remove(currentTime);
						}
					}
		      }
		}
		
		return appointmentTable;
}
  
  
  private void close(ResultSet rs) {
	  if(rs != null) {
          try { 
          rs.close(); 
          } catch(Throwable whatever) {}
      }
	
}

private void close(PreparedStatement prepStmt) {
	  if(prepStmt != null) {
          try { 
          prepStmt.close(); 
          } catch(Throwable whatever) {}
      }
	
}


/**
 * Determines if student is eligible for a free physical
 * Students get one free physical year year.
 * 
 * @param studentID
 * @param date proposed date of appointment
 */
public boolean freePhysicalEligibility(int studentID, String date)
{
	return true;
}

public boolean validateStudentID(String stID) throws SQLException
{
	int studentID=0;
	
	//ensure the given studentID
	try {
		studentID=Integer.parseInt(stID);
	} catch (NumberFormatException e) {
		return false;
	}
	//check valid range
	if (studentID<1000 || studentID>1999)
		return false;
	
	//check if exists in DB
	String sql = "select 1 from student where studentID=?";
	
	try (PreparedStatement stm = connection.prepareStatement(sql)) {

		stm.setInt(1,studentID);

		ResultSet rs = stm.executeQuery();
		if (!rs.next()) {
			return false;
		}
		else return true;

	}
}

/**
	 * Utility method to validate that a given int is a valid doctorID in the DB
	 * 
	 * @param docID
	 * @throws Exception
	 */
	public boolean validateDoctorID(String docID) throws Exception {

		int doctorID=0;
		
		//ensure the given doctor ID is an int
		try {
			doctorID=Integer.parseInt(docID);
		} catch (NumberFormatException e) {
			return false;
		}
		//check valid range
		if (doctorID<2000 || doctorID>2999)
			return false;
		
		//check if exists in DB
		String sql = "select 1 from doctor where doctorID=?";
		
		try (PreparedStatement stm = connection.prepareStatement(sql)) {

			stm.setInt(1,doctorID);

			ResultSet rs = stm.executeQuery();
			if (!rs.next()) {
				return false;
			}
			else return true;

		}
	}
	
	  /**
	   * Delete an appointment from the db
	   * @param appointmentId
	   * @return
	   * @throws SQLException
	   */
	  public String deleteAppointment(String appointmentId) throws SQLException {
	    int id;
	    try {
	      id = Integer.parseInt(appointmentId);
	    } catch (NumberFormatException e) {
	      return "Error: AppointmentId must be a number. Appointment not deleted.";
	    }

	    final String studentName, doctorName;
	    try {
	      connection.setAutoCommit(false);

	      String sql = "select studentname, doctorname from makesAppointment join "
	      		+ "student using(studentid) join doctor using(doctorid) "
	          + " where appointmentid=?";
	      try (PreparedStatement statement = connection.prepareStatement(sql)) {

	        statement.setInt(1, id);

	        // Get records from the Student table
	        try (ResultSet rs = statement.executeQuery()) {

	          if (!rs.next()) {
	            return "Error: Could not find appointment. Appointment not deleted.";
	          }

	          studentName = rs.getString(1);
	          doctorName = rs.getString(2);
	        }
	      }

	      sql = "DELETE FROM Appointment WHERE appointmentId=?";
	      try (PreparedStatement statement = connection.prepareStatement(sql)) {
	        statement.setInt(1, id);
	        statement.executeUpdate();
	      }

	      connection.commit();
	    } catch (SQLException e) {
	      connection.rollback();
	      throw e;
	    } finally {
	      connection.setAutoCommit(true);
	    }

	    return "Deleted appointment between " + doctorName + " and " + studentName;
}

	/**  
	 * Shows a student's past appointments and consultations
	 * 
	 * @param studId
	 * @return
	 * @throws Exception
	 */
	  public String checkStudentRecord(String studId) throws Exception{
		  int studentId;
			try {
				studentId = Integer.parseInt(studId);
			} catch (NumberFormatException e) {
				return "Student ID must be an integer.";
			}

			String sql = "select appointmenttime, type, reasonforvisit, doctorname, doctorNotes from appointment "
					+ "join makesappointment using(appointmentid) join doctor using(doctorid) "
					+ "where studentid=? and (appointmenttime < CURRENT_TIMESTAMP)";
			try (PreparedStatement stm = connection.prepareStatement(sql)) {
				stm.setInt(1, studentId);

				Table table = new Table("Date and Time", "Type", "Reason",
						"Doctor", "Notes");
				ResultSet rs = stm.executeQuery();
				while (rs.next()) {
					table.add(rs.getTimestamp(1), rs.getString(2), rs.getString(3),
							rs.getString(4), rs.getString(5));
				}

				out.println("Student's Past Appointments:");
				out.println(table);
			}

			sql = "select timeOfConsultation, nurseName, nurseNotes from Consultation "
					+ "join makesConsultation using(consultationId) join nurse using(nurseid) "
					+ "where studentid=? and (timeofconsultation < CURRENT_TIMESTAMP)";
			try (PreparedStatement stm = connection.prepareStatement(sql)) {
				stm.setInt(1, studentId);

				Table table = new Table("Date and Time", "Nurse", "Notes");
				ResultSet rs = stm.executeQuery();
				while (rs.next()) {
					table.add(rs.getTimestamp(1), rs.getString(2), rs.getString(3));
				}

				out.println("Student's Consultations:");
				out.println(table);
			}

			return "";
	  }
	
	/**
	 * Allows one to find a specialist given a medical condition
	 * 
	 * @throws IOException
	 * @throws SQLException
	 */
	public void FindSpecialist() throws IOException, SQLException {
		String specialization="";
			int aname1=0;
			do{
					System.out.println("Enter the reason of your visit \n1.Diabetes \n2.FluShots "
							+ "\n3.General Medical Problems \n4.Mental Health \n5.Orthopedics "
							+ "\n6.Physical Therapy \n7.Women's Health\n8.Urinary, Genital Problems "
							+ "\n9.HIV Testing \10.Ear, Nose, Throat Problems "
							+ "\n11.Heart related Problems \n12.Vaccination"
							+ "\n13. Cancer Surgery");
					aname1 = Integer.parseInt(br.readLine());
				}while(aname1>=13);
			
			
			switch(aname1)
			{
			case 1:
				specialization="Endocrinologist";
				break;
			case 2:
				specialization="General Physician";
				break;
			case 3:
				specialization="General Physician";
				break;
			case 4:
				specialization="Psychiatrist";
				break;
			case 5:
				specialization="Orthopedic Surgeon";
				break;
			case 6:
				specialization="Physical Therapist";
				break;
			case 7:
				specialization="Gynaceologist";
				break;
			case 8:
				specialization="Nephrologist";
				break;
			case 9:
				specialization="General Physician";
				break;
			case 10:
				specialization="ENT specialist";
				break;
			case 11:
				specialization="Cardiologist";
				break;
			case 12:
				specialization="General Physician";
				break;
			case 13: 
				specialization="Oncology Surgeon";
				}
			System.out.println(specialization+":");
			
			String sql = "SELECT doctorname,doctorid FROM doctor WHERE SPECIALIZATION=?";
			try(PreparedStatement stm = connection.prepareStatement(sql)) {
			  stm.setString(1, specialization);
  			ResultSet result = stm.executeQuery();
  			
  			Table table = new Table("Doctor ID", "Doctor Name");
  			while(result.next())
  			{
  			  table.add(result.getInt("doctorid"), result.getString("doctorname"));
  			}		
			}
		}

	@SuppressWarnings("unused")
	private static void makeAppointment1(int z,String job)throws IOException, SQLException 
	{ ResultSet result = null;
		int studentid=0;
		//System.out.println(job);
		if(job.equalsIgnoreCase("Student"))
		{
			studentid=z;
		}
		else
		{
			System.out.println("Enter the student id");
			studentid=Integer.parseInt(br.readLine());
		}
		
		int atype1=0;
	do
	{
		System.out.println("Choose the type of your visit \n1.Physical \n2.Vaccination");
		atype1 = Integer.parseInt(br.readLine());
	}while(atype1>2);
	String atype="";
	if (atype1==2)
		atype="Vaccination";
	else
		atype="Physical";
	int aname1=0;
String aname="";
String specialization="";
	if (atype1==2)
		{
		aname="Vaccination";
		specialization="General Physician";
		}
	else
	{	
	do
		{
			System.out.println("Enter the reason of your visit \n1.Diabetes "
					+ "\n2.FluShots \n3.General Medical Problems \n4.Mental Health "
					+ "\n5.Orthopedics \n6.Physical Therapy \n7.Women's Health"
					+ "\n8.Urinary, Genital Problems \n9.HIV Testing \10.Ear, Nose, Throat Problems "
					+ "\n11.Heart related Problems "
					+ "\n12. Cancer Surgery");
			aname1 = Integer.parseInt(br.readLine());
		}while(aname1>=12);}
	switch(aname1)
		{
	case 1:
		aname="Diabetes";
		specialization="Endocrinologist";
		break;
	case 2:
		aname="FluShots";
		specialization="General Physician";
		break;
	case 3:
		aname="General Medical Problems";
		specialization="General Physician";
		break;
	case 4:
		aname="Mental Health";
		specialization="Psychiatrist";
		break;
	case 5:
		aname="Orthopedics";
		specialization="Orthopedic Surgeon";
		break;
	case 6:
		aname="Physical Therapy";
		specialization="Physical Therapist";
		break;
	case 7:
		aname="Women's Health";
		specialization="Gynaceologist";
		break;
	case 8:
		aname="Urinary, Genital Problems";
		specialization="Nephrologist";
		break;
	case 9:
		aname="HIV Testing";
		specialization="General Physician";
		break;
	case 10:
		aname="Ear, Nose, Throat Problems";
		specialization="ENT specialist";
		break;
	case 11:
		aname="Heart related Problems";
		specialization="Cardiologist";
		break;
	case 12:
		aname="Cancer Surgery";
		specialization="Oncology Surgeon";
		break;
		}
	
	result = stm.executeQuery("SELECT doctorname,doctorid FROM doctor WHERE SPECIALIZATION='"+specialization+"'");	
	if(result.next())
	{
		do
		{
			System.out.println(result.getInt("doctorid")+"          "+result.getString("doctorname"));
		}while (result.next());
	}
	int ID=0;
	int flag=0;
	int doc=0;
	do{
	System.out.println("Select the id of the doctor you want to book appointment with"); 
	doc=Integer.parseInt(br.readLine());
	result = stm.executeQuery("SELECT doctorname,doctorid FROM doctor WHERE SPECIALIZATION='"+specialization+"'");	
	while (result.next()) 
	{
		ID=result.getInt("doctorid");
		if(doc==ID)
			{
			flag=1;
			break;
			}
	}
	if(flag==0)
	System.out.println("Please choose a valid doctor id");
	}while(flag!=1);
	}
	
	/**
	 * Returns whether a student is eligible for an annual free physical
	 * 
	 * @param studentId
	 * @return
	 * @throws Exception
	 */
	public boolean verifyFreePhysical(int studentId)throws Exception{
		
		String strPhysicalAppointmentNum = "SELECT COUNT(*) FROM Appointment NATURAL JOIN MakesAppointment " +
		"WHERE StudentId = ? AND Type = 'Physical' AND AppointmentTime > TRUNC(SYSDATE, 'YEAR') AND " +
		"AppointmentTime < ADD_MONTHS(TRUNC(SYSDATE, 'YEAR'), 12) - 1";
		
		PreparedStatement physicalAppointmentNum = null;
		try{
			physicalAppointmentNum = connection.prepareStatement(strPhysicalAppointmentNum);
			
			physicalAppointmentNum.setInt(1,  studentId);
			ResultSet rs = physicalAppointmentNum.executeQuery();
			
			if(rs != null && rs.next()){
				int physicalCount = rs.getInt(1);
				if(physicalCount >= 1){
					return false;
				}
				else{
					return true;
				}
			}
		}
		catch(Exception e){
			throw e;
		}
		finally{
			physicalAppointmentNum.close();
		}
		return false;
	}
	
	
	  /**
	   * Verifies that the specialty of a doctor is one of the allowed specialties
	   * 
	   * @param proposedSpecialty
	   * @return
	   */
	  
	  /*
	   * Creating appointments and searching by health problem requires that
	   * only certain types of doctors exist in the system.  A doctor must conform to
	   * one of the below types of doctors.
	   */
	  public boolean verifySpecialty(String proposedSpecialty)
	  {
		  switch(proposedSpecialty)
			{
			case "General Physician":
				return true;
			case "Endocrinologist":
				return true;
			case "Psychiatrist":
				return true;
			case "Orthopedic Surgeon":
				return true;
			case "Physical Therapist":
				return true;
			case "Gynaceologist":
				return true;
			case "Nephrologist":
				return true;
			case "ENT specialist":
				return true;
			case "Cardiologist":
				return true;
			case "Oncology Surgeon":
				return true;
			default:
				System.out.println(proposedSpecialty);
				return false;
			}
	  }
}
// end of class HealthCentre
