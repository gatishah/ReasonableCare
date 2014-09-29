package reasonablecare;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import asg.cliche.Shell;

public class LoginShellTest extends BaseShellTest {

  private LoginShell shell;

  private ShellFactory mockedFactory;
  private Shell mockedSubshell;

  @Before
  public void setUpShell() throws Exception {
    shell = new LoginShell(getConnection());

    // Create a mock subshell for the loginshell to create
    mockedSubshell = mock(Shell.class);
    doNothing().when(mockedSubshell).commandLoop();

    // Create a mock ShellFactory that doesn't create subshells.
    mockedFactory = mock(ShellFactory.class);
    doReturn(mockedSubshell).when(mockedFactory).createSubshell(
        any(Shell.class), anyString(), any());

    shell.setFactory(mockedFactory);
  }

  @After
  public void tearDownShell() throws Exception {
    mockedFactory = null;
    mockedSubshell = null;
    shell = null;
  }

  @Test
  public void testLoginStudent() throws Exception {
    shell.login(1004, "Triangle");
    
    verify(mockedFactory).createSubshell(any(Shell.class), eq("student"), any(StudentShell.class));
    verify(mockedSubshell).commandLoop();
  }
  
  @Test
  public void testLoginDoctor() throws Exception {
    shell.login(2000, "India");
    
    verify(mockedFactory).createSubshell(any(Shell.class), eq("doctor"), any(DoctorShell.class));
    verify(mockedSubshell).commandLoop();
  }

  
  @Test
  public void testLoginNurse() throws Exception {
    shell.login(4003, "Green");
    
    verify(mockedFactory).createSubshell(any(Shell.class), eq("nurse"), any(NurseShell.class));
    verify(mockedSubshell).commandLoop();
  }
  
  @Test
  public void testLoginStaff() throws Exception {
    shell.login(5020, "Eleven");
    
    verify(mockedFactory).createSubshell(any(Shell.class), eq("staff"), any(StaffShell.class));
    verify(mockedSubshell).commandLoop();
  }
  
  @Test
  public void testLoginWrongId() throws Exception {
    // ID range 3000-3999 is invalid for Appointments. Can't login with those.
    String result = shell.login(3040, "anypassword");
    
    // No subshell created
    verify(mockedFactory, never()).createSubshell(any(Shell.class), anyString(), any());
    
    // Non-empty error message returned
    assertThat(result, not(isEmptyOrNullString()));
  }

  @Test
  public void testLoginStudentWrongPassword() throws Exception {
    String result = shell.login(1004, "wrongpassword");
    
    // No subshell created
    verify(mockedFactory, never()).createSubshell(any(Shell.class), anyString(), any());
    
    // Non-empty error message returned
    assertThat(result, not(isEmptyOrNullString()));
  }
  
  @Test
  public void testLoginDoctorWrongPassword() throws Exception {
    String result = shell.login(2000, "wrongpassword");
    
    // No subshell created
    verify(mockedFactory, never()).createSubshell(any(Shell.class), anyString(), any());
    
    // Non-empty error message returned
    assertThat(result, not(isEmptyOrNullString()));
  }
  
  @Test
  public void testLoginNurseWrongPassword() throws Exception {
    String result = shell.login(4003, "wrongpassword");
    
    // No subshell created
    verify(mockedFactory, never()).createSubshell(any(Shell.class), anyString(), any());
    
    // Non-empty error message returned
    assertThat(result, not(isEmptyOrNullString()));
  }
  
  @Test
  public void testLoginStaffWrongPassword() throws Exception {
    String result = shell.login(5020, "wrongpassword");
    
    // No subshell created
    verify(mockedFactory, never()).createSubshell(any(Shell.class), anyString(), any());
    
    // Non-empty error message returned
    assertThat(result, not(isEmptyOrNullString()));
  }
  
}
