package reasonablecare;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

public class DatabaseExport {

  static final List<String> tables = Arrays.asList("Student", "Appointment",
      "Consultation", "Doctor", "Nurse", "Staff", "MakesAppointment",
      "MakesConsultation");

  public static void main(String[] args) throws Exception {

    // database connection
    try (Connection jdbcConnection = Constants.makeConnection()) {
      IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);

      // partial database export
      QueryDataSet partialDataSet = new QueryDataSet(connection);
      for (String table : tables) {
        partialDataSet.addTable(table);
      }

      // write DTD file
      FlatDtdDataSet.write(partialDataSet, new FileOutputStream("test.dtd"));

      // write data file
      FlatXmlDataSet.write(partialDataSet, new FileOutputStream("partial.xml"));

    }
  }

}
