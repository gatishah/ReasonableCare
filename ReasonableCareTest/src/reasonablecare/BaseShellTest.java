package reasonablecare;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class BaseShellTest {

  private static IDatabaseConnection connection;

  @BeforeClass
  public static void connect() throws Exception {
    connection = new DatabaseConnection(Constants.makeConnection());
  }

  @AfterClass
  public static void disconnect() throws Exception {
    connection.close();
    connection = null;
  }

  @Before
  public void setUpDataset() throws Exception {
    IDataSet dataSet = getDataSet();
    DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
  }
  
  private IDataSet getDataSet() throws Exception {
    return new FlatXmlDataSetBuilder().build(new FileInputStream(
        "db/shell1.xml"));
  }
  
  protected static Connection getConnection() throws SQLException {
    return connection.getConnection();
  }

}