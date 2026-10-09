package mc.carlton.freerpg.serverInfo;

import mc.carlton.freerpg.utilities.FrpgPrint;

import org.bukkit.Bukkit;

public class MinecraftVersion {
    public static String minecraftVersion;
    public static double minecraftVersion_Double;

    public void initializeVersion() {
        minecraftVersion = Bukkit.getVersion();
        //Bukkit.getMinecraftVersion() returns e.g. "26.2" or "26.1.2"; keep only "major.minor"
        String[] parts = Bukkit.getMinecraftVersion().split("\\.");
        try {
            minecraftVersion_Double = Double.parseDouble(parts[0] + "." + (parts.length > 1 ? parts[1] : "0"));
        } catch (NumberFormatException e) {
            minecraftVersion_Double = 26.2;
            FrpgPrint.print("Could not determine minecraft version, assuming 26.2...");
        }
    }

    public double getMinecraftVersion_Double(){
        return minecraftVersion_Double;
    }
    public String getMinecraftVersion(){
        return minecraftVersion;
    }

}
