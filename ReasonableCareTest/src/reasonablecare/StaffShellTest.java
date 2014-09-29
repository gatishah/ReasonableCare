package reasonablecare;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StaffShellTest extends BaseShellTest {

  private StaffShell shell;

  @Before
  public void setUpShell() throws Exception {
    shell = new StaffShell(getConnection(), 5021);
  }

  @After
  public void tearDownShell() throws Exception {
    shell = null;
  }

  @Test
  public void testGetDoctors() throws SQLException {
    Object result = shell.getDoctors();
    Object expected = "+-----------+-------------+\n| Doctor ID | Doctor Name |\n+-----------+-------------+\n| 2000      | Dr. Miller  |\n| 2001      | Dr. Singh   |\n| 2002      | Dr. Healey  |\n| 2003      | Dr. William |\n| 2004      | Dr. Rob     |\n+-----------+-------------+\n"
;

    String res = result.toString();
    assertThat(res, equalTo(expected));
  }

  @Test
  public void testGetStudents() throws SQLException {
    Table expected = new Table("Student ID", "Student Name");
    expected.add(1000, "Gati");
    expected.add(1001, "GDavid");
    expected.add(1002, "James");
    expected.add(1003, "Maria");
    expected.add(1004, "Mary");
    expected.add(1005, "MDaniel");
    expected.add(1020, "Randy");
    expected.add(1040, "donald");
    expected.add(1041, "PeeDee");
    expected.add(1043, "Bob");
    expected.add(1044, "Bob");
    expected.add(1045, "Bob");
    expected.add(1046, "walter");
    expected.add(1047, "Jane");
    expected.add(1048, "Parker");
    expected.add(1049, "evan");
    expected.add(1050, "Jim");
    expected.add(1051, "Jim");
    expected.add(1052, "Jim");
    expected.add(1053, "Jim");
    expected.add(1054, "Jim");
    expected.add(1055, "jim");
    expected.add(1056, "jim");
    expected.add(1057, "jim");

    Table result = (Table) shell.getStudents();

    assertThat(result, equalTo(expected));
  }

}
