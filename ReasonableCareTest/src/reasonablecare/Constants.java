package reasonablecare;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Constants {

  public static final String driverClass = "oracle.jdbc.driver.OracleDriver",
      jdbcURL = "jdbc:oracle:thin:@ora.csc.ncsu.edu:1521:orcl";

  // Put your oracle ID and password here
  public static final String user = "mpsenn", password = "000772408";

  private Constants() {
  }

  public static Connection makeConnection() throws ClassNotFoundException,
      SQLException {
    Class.forName("oracle.jdbc.driver.OracleDriver");
    return DriverManager.getConnection(jdbcURL, user, password);
  }

}
