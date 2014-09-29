package reasonablecare;
import java.sql.Connection;
import java.sql.DriverManager;


import asg.cliche.ShellFactory;

/**
 * Entrance program for the Reasonable Care system.
 * 
 * @see <a href="https://code.google.com/p/cliche/wiki/Manual>Manual for Cliche
 *      shell system</a>
 */
public class ReasonableCare {

  private static final String jdbcURL = "jdbc:oracle:thin:@ora.csc.ncsu.edu:1521:orcl";

  // Put your oracle ID and password here
  private static final String user = "gshah";
  private static final String password = "001080029";
  
  public static final String APP_NAME = "ReasonableCare";

  /**
   * Main program to launch the connection with the DB and launch the login Shell
   * 
   * @param args
   *          Not used.
   */
  public static void main(String[] args) {

    try {
      // Loading the driver. This creates an instance of the driver
      // and calls the registerDriver method to make Oracle Thin
      // driver, at ora.csc.ncsu.edu, available to clients.
      Class.forName("oracle.jdbc.driver.OracleDriver");
      
	  Connection connection = null;

      // Get a connection instance from the first driver in the
      // DriverManager list that recognizes the URL jdbcURL
      try {
    	  
    	connection = DriverManager.getConnection(jdbcURL, user,password);

        connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        Object shell = new LoginShell(connection);
        ShellFactory.createConsoleShell("ReasonableCare", APP_NAME, shell).commandLoop();

      }
       finally {
        close(connection);
       }	

    } catch (Exception oops) {
      System.out.println("Exception running program.");
      oops.printStackTrace();
    }

 }

  static void close(Connection connection) 
  {
	  if(connection != null) {
		  try { 
			  connection.close(); 
		  } catch(Throwable whatever) {}
	  }
  }

}
