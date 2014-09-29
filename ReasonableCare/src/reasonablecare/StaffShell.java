package reasonablecare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import asg.cliche.Command;
import asg.cliche.Param;

/**
 * Shell to provide the required tasks, operations, and views for a staff member.
 *
 */
public class StaffShell {

  /**
   * Connection object used to create Statements. This shell doesn't own the
   * connection; no need to close.
   */
  final Connection connection;
  final CommonStatements commonStatements;

  final int id;

  public StaffShell(Connection connection, int id) throws SQLException {
    this.connection = connection;
    this.id = id;
    //constructor to use shared statements
  	commonStatements = new CommonStatements(connection);
  }
  
  /**
   * Allow a staff member to add a new student to the system
   * 
   * @param name
   * @param password
   * @param startingDate
   * @return studentID of created student if successful
   * @throws SQLException
   */
  @Command(description="Add a new student to the system")
  public Object createStudent(
		  @Param(name = "name")
		  String name, 
		  @Param(name="password")
		  String password, 
		  @Param(name="startingDate", description="starting semester of student in the format"
		  		+ " YYYY-MM-DD")
		  String startingDate)
      throws SQLException {

    String sql = "insert into student(studentName,password,startingDate) values(?,?,?)";

    // Create a statement instance that will be sending
    // your SQL statements to the DBMS
    // second argument is the generated key that is to be returned
    try (PreparedStatement stm = connection.prepareStatement(sql,
        new String[] { "StudentID" })) {

      stm.setString(1, name);
      stm.setString(2, password);
      stm.setDate(3, java.sql.Date.valueOf(startingDate)); //date must be in YYYY-MM-DD format
      stm.executeUpdate();

      //get the auto-generated key from the row that was added
      int id = 0;

      ResultSet rs = stm.getGeneratedKeys();
      if (rs != null && rs.next()) {
        id = rs.getInt(1);
      }

      return "Created new student with id " + id;
    } catch(IllegalArgumentException ex) {
    	return "Error parsing date argument. Enter in yyyy-mm-dd format. Student not created.";
    }

  }
  			
/**
 * Allow staff to add a new doctor to the system.
 * 
 * @param name
 * @param password
 * @param phoneNumber
 * @param specialization
 * @return doctorID of created doctor
 * @throws SQLException
 */
  @Command(description = "Add a new doctor to the system")
  public Object createDoctor(
      @Param(name = "name") 
      String name,
      @Param(name = "password") 
      String password,
      @Param(name = "phoneNumber", description = "Should be in form ###-###-####.") 
      String phoneNumber,
      @Param(name = "specialization", description = "If doctor is not a specialist, use "
      		+ "'General Physician'.") 
      String specialization)
      throws SQLException {
	  
	  if (!commonStatements.verifySpecialty(specialization))
	  {
		  return "Invalid Specialization Provided.  Doctor not created.";
	  }

    String sql = "insert into doctor(doctorName,password,phoneNumber,specialization) values(?,?,?,?)";

    // Create a statement instance that will be sending
    // your SQL statements to the DBMS
    // second argument is the generated key that is to be returned
    try (PreparedStatement stm = connection.prepareStatement(sql,
        new String[] { "DoctorID" })) {

      stm.setString(1, name);
      stm.setString(2, password);
      stm.setString(3, phoneNumber);
      stm.setString(4, specialization);
      stm.executeUpdate();

      //get the auto-generated key from the row that was added
      int id = 0;

      ResultSet rs = stm.getGeneratedKeys();
      if (rs != null && rs.next()) {
        id = rs.getInt(1);
      }

      return "Created new doctor with id " + id;
    }
  }

  /**
   * Return a table of all doctors, grouped by specialization
   * @return table of all doctors
   * @throws SQLException
   */

  @Command(description = "Show list of all doctors and their specializations")
	public Table getDoctors() throws SQLException {
		
		Table doctorTable = commonStatements.getDoctors();

		return doctorTable;
	}
/**
 * Show list of all students
 * @return table of all students
 * @throws SQLException
 */
  @Command(description = "Return a table of all students in the system")
  public Object getStudents() throws SQLException {

    try (Statement statement = connection.createStatement()) {

      // Get records from the Student table
      try (ResultSet result = statement
          .executeQuery("SELECT studentID, studentName FROM Student ORDER BY studentID")) {

        Table res = new Table("Student ID", "Student Name");

        while (result.next()) {
          int id = result.getInt("studentID");
          String name = result.getString("studentName");
          res.add(id, name);
        }

        return res;
      }
    }
  }
  
/**
 * Allow a staff member to add a new staff member to the system
 * @param name
 * @param password
 * @return staffID of the created Staff member
 * @throws SQLException
 */
  @Command(description = "Add a new staff member to the system")
	  public Object createStaff(
	      @Param(name = "name") 
	      String name,
	      @Param(name = "password") 
	      String password)
	      throws SQLException {

	    String sql = "insert into staff(staffName,password) values(?,?)";

	    // Create a statement to enter staff into DB and return ID
	    try (PreparedStatement stm = connection.prepareStatement(sql,
	        new String[] { "StaffID" })) {

	      stm.setString(1, name);
	      stm.setString(2, password);
	      stm.executeUpdate();

	      //get the auto-generated key from the row that was added
	      int id = 0;

	      ResultSet rs = stm.getGeneratedKeys();
	      if (rs != null && rs.next()) {
	        id = rs.getInt(1);
	      }

	      return "Created new staff member with id " + id;
	    }
	  }

  /**
   * Allow staff member to add a new nurse to the system
   * @param name
   * @param password
   * @return nurseID of the created nurse
   * @throws SQLException
   */
  @Command(description = "Add a new nurse to the system.")
		  public Object createNurse(
		      @Param(name = "name") 
		      String name,
		      @Param(name = "password") 
		      String password)
		      throws SQLException {

		    String sql = "insert into nurse(nurseName,password) values(?,?)";

		    // Create a statement to enter staff into DB and return ID
		    try (PreparedStatement stm = connection.prepareStatement(sql,
		        new String[] { "NurseID" })) {

		      stm.setString(1, name);
		      stm.setString(2, password);
		      stm.executeUpdate();

		      //get the auto-generated key from the row that was added
		      int id = 0;

		      ResultSet rs = stm.getGeneratedKeys();
		      if (rs != null && rs.next()) {
		        id = rs.getInt(1);
		      }

		      return "Created new nurse with id " + id;
		    }
		   }
	
  /**
   * Update information for a Nurse
   * @param nurseId
   * @throws IOException
   * @throws SQLException
   */
  @Command(description="update information for a nurse")
  public void updateNurse(@Param(name="nurseID")int nurseId) throws IOException, SQLException {
    try (CommonStatements stm = new CommonStatements(connection)) {
      stm.updatenurseinformation(nurseId);
    }
  }

  /**
   * Update information for a Doctor
   * 
   * @param id Doctor ID
   * @throws IOException
   * @throws SQLException
   */
  @Command(description="update information for a doctor")
  public void updateDoctor(@Param(name="doctorID") int id) throws IOException, SQLException {
    try (CommonStatements stm = new CommonStatements(connection)) {
      stm.updatedoctorinformation(id);
    }
  }
  
  /**
   * Update information for a Student
   * 
   * @param id Student ID
   * @throws IOException
   * @throws SQLException
   */
  @Command(description="update information for a student")
  public void updateStudent(@Param(name="studentID") int id) throws IOException, SQLException {
    try (CommonStatements stm = new CommonStatements(connection)) {
      stm.updatestudentinformation(id);
    }
  }

  /**
   * Update information for a staff member
   * @param id Staff ID
   * @throws IOException
   * @throws SQLException
   */
  @Command(description="update information for a staff member")
  public void updateStaff(@Param(name="staffID") int id) throws IOException, SQLException {
    try (CommonStatements stm = new CommonStatements(connection)) {
      stm.updatestaffinformation(id);
    }
  }

  /**
   * Interactive method to allow a staff member to make appointment by being prompted for 
   * the values needed.  Invalid input is mostly handled
   * 
   * @throws Exception
   */
  	@Command(description="Interactive way to make an appointment.  Prompts for all information"
  			+ "needed.")
  public Object makeAppointment() throws Exception{
	  int ID3=0;
		int flag=0;
		int ccMonth, ccYear;
	  BufferedReader br = new BufferedReader(new InputStreamReader( System.in));
	  System.out.println("Enter the student id for which appointment has to be made");
	  int id = Integer.parseInt(br.readLine());
	  String sql = "SELECT STUDENTID from STUDENT";
	  
		try (PreparedStatement stm = connection.prepareStatement(sql)) {
				     //stm.setString(1, specialization);
				    
		
		ResultSet rs = stm.executeQuery();						

		while (rs.next()){
			ID3=rs.getInt("STUDENTID");
			if(id==ID3)
				{
				flag=1;
				break;
				}
		}
		
		}		
		if(flag==0)
			{
			System.out.println("Not a valid student id");
			makeAppointment();
			}
		//runs method from studentShell
	final StudentShell  StudentShell = new StudentShell(connection,id);
		java.sql.Timestamp apptTime;
		int apptDoc=0,menuSelection=0, cost=0;
		String apptType="", apptReason="", insuranceProvider, insuranceNumber, ccNumber;
		boolean apptTypeSelected=false, hasInsurance=false, 
				creditCardAccepted=false;

		//Check that student has insurance information
		while (!hasInsurance)
		{
			
			if (!StudentShell.checkHasInsurance(id))
			{
				System.out.println("The student does not have insurance.  \nEnter an option from the menu below:"
						+"\n1. Provide Insurance Information"
						+"\n2. Exit System and Log out\n");
				try {
					menuSelection=Integer.parseInt(br.readLine().trim());
					if (menuSelection<1 || menuSelection>2)
				   	  System.out.println("Invalid Selection\n");
					else apptTypeSelected=true;
				      
				} catch (NumberFormatException e) {
			      System.out.println("Invalid Selection\n");
				}		
				if (apptTypeSelected){
				switch (menuSelection) {
			    case 1:
			    	//take insurance info
			    	System.out.println("Enter the name of your insurance provider:");
			    	insuranceProvider = br.readLine().trim();
			    	System.out.println("Enter your insurance policy number:");
			    	insuranceNumber = br.readLine().trim();
			    	
			    	String updateIns = "UPDATE STUDENT SET HEALTHINSURANCEPROVIDERNAME=?, "
			    			+ "HEALTHINSURANCEPOLICYNUMBER= ? "
			    			+ "WHERE STUDENTID=?";
			    	
			    	try (PreparedStatement stm = connection.prepareStatement(updateIns)) {
					      stm.setString(1, insuranceProvider);
					      stm.setString(2, insuranceNumber);
					      stm.setInt(3, id); 
					      stm.executeUpdate();
			    	}
			    	System.out.println("Insurance Information Accepted");
			    	hasInsurance=true;
			    	break;
			    	
				default:
					System.exit(0);
				}}// end switch+if
			}
			else hasInsurance=true;
		}
		String specialization="";
		int apptReason1=0;
		//prompt for appointment type and reason (if not physical/vaccination)
		do
		{
					
			System.out.println("Select Appointment Type"
					+"\n1. Vaccination"
					+"\n2. Physical"
					+"\n3. Office Visit");
			try {
				menuSelection=Integer.parseInt(br.readLine().trim());
				if (menuSelection<1 || menuSelection>3)
			   	  System.out.println("Invalid Selection\n");
				else apptTypeSelected=true;
			      
			} catch (NumberFormatException e) {
		      System.out.println("Invalid Selection\n");
			}		
			if (apptTypeSelected){
			switch (menuSelection) {
		    case 1:
		    	apptType=apptReason="Vaccination"; 
		    	specialization="General Physician";
		    	break;
		    case 2:
		    	apptType=apptReason="Physical";
		    	specialization="General Physician";
		    	break;
			default:
				apptType="Office Visit";
				System.out.println("Enter the reason of your visit \n1.Diabetes "
						+ "\n2.FluShots \n3.Mental Health \n4.Orthopedics \n5.Physical Therapy "
						+ "\n6.Women's Health\n7.Urinary, Genital Problems \n8.HIV Testing "
						+ "\n9.Ear, Nose, Throat Problems "
						+ "\n10.Heart related Problems "
						+ "\n11. Cancer Surgery");
				apptReason1=Integer.parseInt(br.readLine());
				switch(apptReason1)
				{
				case 1:
					apptReason="Diabetes";
					specialization="Endocrinologist";
					break;
				case 2:
					apptReason="FluShots";
					specialization="General Physician";
					break;
				
				case 3:
					apptReason="Mental Health";
					specialization="Psychiatrist";
					break;
				case 4:
					apptReason="Orthopedics";
					specialization="Orthopedic Surgeon";
					break;
				case 5:
					apptReason="Physical Therapy";
					specialization="Physical Therapist";
					break;
				case 6:
					apptReason="Women's Health";
					specialization="Gynaceologist";
					break;
				case 7:
					apptReason="Urinary, Genital Problems";
					specialization="Nephrologist";
					break;
				case 8:
					apptReason="HIV Testing";
					specialization="General Physician";
					break;
				case 9:
					apptReason="Ear, Nose, Throat Problems";
					specialization="ENT specialist";
					break;
				case 10:
					apptReason="Heart related Problems";
					specialization="Cardiologist";
					break;
				case 11:
					apptReason="Cancer Surgery";
					specialization="Oncology Surgeon";
					break;
				}
			}}// end switch+if
		} while (!apptTypeSelected);//end while
		
		String sql2 = "SELECT doctorname,doctorid FROM doctor where specialization=?";
		  
		try (PreparedStatement stm = connection.prepareStatement(sql2)) {
				     stm.setString(1, specialization);
				    
		
		ResultSet rs = stm.executeQuery();						

		while (rs.next()){
			System.out.println(rs.getInt("doctorid")+"          "+rs.getString("doctorname"));
		}
		
		}
		int ID=0;
		flag=0;
		do{		
			System.out.println("Select any valid id of the doctor you want to book appointment"); 
		
			apptDoc=Integer.parseInt(br.readLine());

			String sql1 = "SELECT doctorname,doctorid FROM doctor";
			  
			try (PreparedStatement stm = connection.prepareStatement(sql1)) {
			
			ResultSet rs = stm.executeQuery();						

		
		while (rs.next()) 
		{
			ID=rs.getInt("doctorid");
			if(apptDoc==ID)
				{
				flag=1;
				break;
				}
		}
		}//end try
		if(flag==0)
		System.out.println("Please choose a valid doctor id");
		}while(flag!=1);
		
		apptTime = StudentShell.selectDateTime(apptDoc);
		
		//fetch insurance information
		insuranceProvider = StudentShell.getInsuranceProvider(id);
		insuranceNumber = StudentShell.getInsuranceNumber(id);
		
		InsuranceCompanySystem insurance= new InsuranceCompanySystem();
		CreditCardSystem creditCard = new CreditCardSystem();
		//get cost of appointment
		cost = insurance.getCopay(apptType, apptDoc, insuranceProvider, insuranceNumber);
		if(apptType.equals("Physical") && commonStatements.verifyFreePhysical(id)){
			cost = 0;
		}
		
		System.out.println("The copayment for your appointment will be: "+cost);
		if(cost == 0){
			//do nothing
		}
		//determine if deductible has been paid for the year
		else if (insurance.getDeductiblePaid(insuranceProvider, insuranceNumber))
		{
			System.out.println("Your deductible has been paid for the year.  You will not be billed.");
		}
		else
		{
			System.out.println("Your deductible has not been paid for the year.");
			do{
				//get credit card information and validate it
				System.out.println("Enter your credit card number:");
				ccNumber = br.readLine().trim();
				System.out.println("Enter your the expiration month:");
				ccMonth = Integer.parseInt(br.readLine());
				System.out.println("Enter your the expiration year:");
				ccYear = Integer.parseInt(br.readLine());
				if (creditCard.validateCreditCard(ccNumber,ccMonth,ccYear))
				{
				
					if (creditCard.getPreapproval(ccNumber))
					{
						creditCardAccepted=true;
						System.out.println("Your credit card was pre-approved.");
					}
					else
					{
						System.out.println("Your credit card was not pre-approved. Use a different card (Y/N)?");
						String response = br.readLine().trim();
						if("Y".equals(response)) {
						  continue;
						}
						
						break;
					}
				}
				else {
					System.out.println("Invalid credit card number or expiration date. Try again (Y/N)?");
          String response = br.readLine().trim();
          if("Y".equals(response)) {
            continue;
          }
          
          break;
				}
				
			} while (!creditCardAccepted);
		}
		
		//Make the appointment
		
		return commonStatements.makeAppointment(id, apptDoc, apptType, apptReason, apptTime, cost);  }
  
  @Command(description = "Delete an appointment, given the appointment's ID.")
  public String deleteAppointment(@Param(name="appointmentID")String appointmentId) throws SQLException {
	  
	  return commonStatements.deleteAppointment(appointmentId);
  }

  
  @Command(description = "List students that have been attending for 6 months and have not scheduled 3 vaccinations.")
  public Table checkVaccinations() throws SQLException {

    try (Statement statement = connection.createStatement()) {

      String sql = "SELECT StudentID, studentName, startingDate, nvl(V.Vacc, 0) "
          +"FROM Student LEFT JOIN ( "
          +" SELECT StudentID, count(*) AS Vacc "
          +" FROM makesAppointment NATURAL JOIN Appointment "
          +" WHERE Appointment.type='Vaccination' "
          +" AND appointmentTime < CURRENT_TIMESTAMP "
          +" GROUP BY StudentID "
          +") V USING (StudentID) "
          +"WHERE (MONTHS_BETWEEN(CURRENT_TIMESTAMP, startingDate) >= 6) "
          +" AND (nvl(V.Vacc,0) < 3)";


      // Get records from the Student table
      try (ResultSet rs = statement.executeQuery(sql)) {

        Table table = new Table("Student ID", "Student Name",
            "Starting Semester", "Number of Vaccinations");

        while (rs.next()) {
          table.add(rs.getInt(1), rs.getString(2), rs.getString(3),
              rs.getInt(4));
        }

        return table;
      }
    }

  }
}