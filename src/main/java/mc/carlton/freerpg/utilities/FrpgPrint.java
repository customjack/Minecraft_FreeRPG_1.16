package mc.carlton.freerpg.utilities;

import mc.carlton.freerpg.FreeRPG;
import org.apache.logging.log4j.Level;

public class FrpgPrint {

  public static void print(String message) {
    FreeRPG.log(Level.INFO, message);
  }
}
