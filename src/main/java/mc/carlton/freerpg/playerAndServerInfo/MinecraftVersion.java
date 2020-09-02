package mc.carlton.freerpg.playerAndServerInfo;

import org.bukkit.Bukkit;

public class MinecraftVersion {
    public static String minecraftVersion;
    public static double minecraftVersion_Double;

    public void initializeVersion() {
        minecraftVersion = Bukkit.getVersion();
        if (minecraftVersion.contains("1.8")){
            minecraftVersion_Double = 1.8;
        }
        else if (minecraftVersion.contains("1.9")){
            minecraftVersion_Double = 1.9;
        }
        else if (minecraftVersion.contains("1.10")){
            minecraftVersion_Double = 1.10;
        }
        else if (minecraftVersion.contains("1.11")){
            minecraftVersion_Double = 1.11;
        }
        else if (minecraftVersion.contains("1.12")){
            minecraftVersion_Double = 1.12;
        }
        else if (minecraftVersion.contains("1.13")){
            minecraftVersion_Double = 1.13;
        }
        else if (minecraftVersion.contains("1.14")){
            minecraftVersion_Double = 1.14;
        }
        else if (minecraftVersion.contains("1.15")){
            minecraftVersion_Double = 1.15;
        }
        else if (minecraftVersion.contains("1.16")){
            minecraftVersion_Double = 1.16;
        }
        else {
            minecraftVersion_Double = 1.16;
            System.out.println("[FreeRPG] Could not determine minecraft verison, Assuming 1.16...");
        }
        System.out.println(minecraftVersion_Double);
    }

    public double getMinecraftVersion_Double(){
        return minecraftVersion_Double;
    }
    public String getMinecraftVersion(){
        return minecraftVersion;
    }

}
