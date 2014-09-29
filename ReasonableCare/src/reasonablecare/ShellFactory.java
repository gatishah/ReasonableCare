package reasonablecare;

import asg.cliche.Shell;

/**
 * Shell factory class needed to load shells with the Cliche library
 *
 */
public class ShellFactory {

  static final String APP_NAME = ReasonableCare.APP_NAME;

  public ShellFactory() {
  }

  public Shell createSubshell(Shell parent, String name, Object shell) {
    return asg.cliche.ShellFactory
        .createSubshell(name, parent, APP_NAME, shell);
  }

}
