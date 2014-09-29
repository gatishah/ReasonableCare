package reasonablecare;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StudentShellTest extends BaseShellTest {

  private StudentShell shell;

  @Before
  public void setUpShell() throws Exception {
    // Student "Mary"
    shell = new StudentShell(getConnection(), 1004);
  }

  @After
  public void tearDownShell() throws Exception {
    shell = null;
  }

  @Test
  public void testGetSpecializations() throws SQLException {
    Table expected = new Table("Specialization", "Doctor ID", "Doctor Name");
    expected.add("Heart Specialist", 2001, "Dr. Singh");
    expected.add("Pathology", 2002, "Dr. Healey");
    expected.add("Physician", 2000, "Dr. Miller");
    expected.add("Physician", 2003, "Dr. William");
    expected.add("Psychiatrist", 2004, "Dr. Rob");

    Table result = shell.getDoctors();

    assertThat(result, equalTo(expected));
  }

}
