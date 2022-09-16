package mc.carlton.freerpg.utils;

import mc.carlton.freerpg.FreeRPG;
import org.apache.logging.log4j.Level;

public class FreeRPGPrint {

  // TODO move this method in the main class or replace the usage of this method with the LOGGER is self instead of using this one!
  public static void print(String message) {
    FreeRPG.log(Level.INFO, message);
  }
}
