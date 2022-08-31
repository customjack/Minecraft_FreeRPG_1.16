package mc.carlton.freerpg.core.info.server;

import org.bukkit.Bukkit;

public class MinecraftVersion {

  public static String minecraftVersion;
  public static double minecraftVersion_Double;

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
      System.out.println("[FreeRPG] Could not determine minecraft version, Assuming 1.18...");
    }
  }

  public double getMinecraftVersion_Double() {
    return minecraftVersion_Double;
  }

  public String getMinecraftVersion() {
    return minecraftVersion;
  }

}
