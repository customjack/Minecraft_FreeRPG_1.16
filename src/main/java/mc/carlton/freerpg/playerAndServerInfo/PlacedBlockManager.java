package mc.carlton.freerpg.playerAndServerInfo;

import mc.carlton.freerpg.FreeRPG;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.*;

import org.bukkit.plugin.Plugin;

public class PlacedBlockManager {
    public static void startConditions() throws IOException {
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        File f = new File(plugin.getDataFolder(),"blockLocations.dat");
        String path = f.getPath();
        World world = Bukkit.getServer().getWorlds().get(0);
        String worldName = world.getName();
        //When player file is created for the first time...
        if(!f.exists()){
            f.createNewFile();
            try(FileWriter fileWriter = new FileWriter(path)){
                fileWriter.write(worldName+",0,0,0");
                fileWriter.write("\n");
                fileWriter.write(worldName+",1,1,1");
            } catch (IOException exception){
                exception.printStackTrace();
            }
        }

    }
}
