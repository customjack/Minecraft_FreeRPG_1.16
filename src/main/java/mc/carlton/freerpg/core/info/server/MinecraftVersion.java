package mc.carlton.freerpg.core.info.server;

import mc.carlton.freerpg.FreeRPG;
import org.apache.logging.log4j.Level;
import org.bukkit.Bukkit;

/**
 * MinecraftVersion is the class which provides the current Minecraft version of the server the
 * plugin is running on. This helps to turn off certain features of the plugin if they are not
 * supported in this version.
 */
public class MinecraftVersion {

  public static String minecraftVersion;
  public static double minecraftVersion_Double;

  /**
   * Initializes the Minecraft version of the server the plugin is running, so features can be
   * turned off for certain versions.
   */
  public void initializeVersion() {
    minecraftVersion = Bukkit.getVersion();
    if (minecraftVersion.contains("1.8")) {
      minecraftVersion_Double = 1.8;
    } else if (minecraftVersion.contains("1.9")) {
      minecraftVersion_Double = 1.9;
    } else if (minecraftVersion.contains("1.10")) {
      minecraftVersion_Double = 1.10;
    } else if (minecraftVersion.contains("1.11")) {
      minecraftVersion_Double = 1.11;
    } else if (minecraftVersion.contains("1.12")) {
      minecraftVersion_Double = 1.12;
    } else if (minecraftVersion.contains("1.13")) {
      minecraftVersion_Double = 1.13;
    } else if (minecraftVersion.contains("1.14")) {
      minecraftVersion_Double = 1.14;
    } else if (minecraftVersion.contains("1.15")) {
      minecraftVersion_Double = 1.15;
    } else if (minecraftVersion.contains("1.16")) {
      minecraftVersion_Double = 1.16;
    } else if (minecraftVersion.contains("1.17")) {
      minecraftVersion_Double = 1.17;
    } else if (minecraftVersion.contains("1.18")) {
      minecraftVersion_Double = 1.18;
    } else {
      minecraftVersion_Double = 1.18;
      // TODO replace with logger call
      FreeRPG.log(Level.WARN,
          "Could not determine minecraft version, the plugin assumes 1.18. "
              + "This might be caused due to the usage of a newer minecraft version the plugin does not support. "
              + "If you notice unexpected behaviour please visit the plugins Github page and open a new issue!");
    }
  }

  /**
   * Provides the current Minecraft version of the server as a double. A server running on Minecraft
   * 1.18 returns the double 1.18
   *
   * @return
   */
  public double getMinecraftVersionAsDouble() {
    return minecraftVersion_Double;
  }

  /**
   * Provides the current Minecraft version of the server as a String.
   *
   * @return
   */
  public String getMinecraftVersionAsString() {
    return minecraftVersion;
  }
}
