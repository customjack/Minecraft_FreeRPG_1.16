package mc.carlton.freerpg.serverFileManagement;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.serverInfo.PlacedBlocksManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public class PlacedBlockFileManager {
    static File placedBlocksDat;


    public void initializePlacedBlocks(){
        PlacedBlocksManager placedBlocksManager = new PlacedBlocksManager();
        HashSet<Location> blocks = placedBlocksManager.getBlocksMap();
        File f = placedBlocksDat;
        String path = f.getPath();
        if (f.exists()) {
            try (BufferedReader fileReader = new BufferedReader(new FileReader(path))) {
                String line = fileReader.readLine();
                while (line != null) {
                    String[] coords_string = line.split(",");
                    String worldName = coords_string[0];
                    double x = Integer.parseInt(coords_string[1]);
                    double y = Integer.parseInt(coords_string[2]);
                    double z = Integer.parseInt(coords_string[3]);
                    World world = Bukkit.getWorld(worldName);
                    Location location = new Location(world,x,y,z);
                    blocks.add(location);
                    line = fileReader.readLine();
                }
                placedBlocksManager.setBlocksMap(blocks);
            } catch (IOException error) {
                error.printStackTrace();
            }
        }
    }

    public void writePlacedBlocks() {
        File f = placedBlocksDat;
        PlacedBlocksManager placedBlocksManager = new PlacedBlocksManager();
        HashSet<Location> blocks = (HashSet<Location>) placedBlocksManager.getBlocksMap().clone(); //We clone here to avoid concurrent modification exception
        String path = f.getPath();
        if (f.exists()) {
            try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(path,false))) {
                for (Location location : blocks) {
                    World world = location.getWorld();
                    if (world != null) {
                        String worldName = world.getName();
                        String x = Integer.toString(location.getBlockX());
                        String y = Integer.toString(location.getBlockY());
                        String z = Integer.toString(location.getBlockZ());
                        fileWriter.write(worldName + "," + x + "," + y + "," + z);
                        fileWriter.write("\n");
                    }
                }
            } catch (IOException error) {
                error.printStackTrace();
            }
        }
    }

    public void initializePlacedBlocksFile(){
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        File serverData = new File(plugin.getDataFolder(), File.separator + "ServerData");
        File f = new File(serverData,"blockLocations.dat");
        f.setReadable(true,false);
        f.setWritable(true,false);
        placedBlocksDat = f;
        String path = f.getPath();
        World world = Bukkit.getServer().getWorlds().get(0);
        String worldName = world.getName();
        //When BlocksPlacedData file is created for the first time...
        try {
            if (!f.exists()) {
                f.createNewFile();
                try (FileWriter fileWriter = new FileWriter(path)) {
                    fileWriter.write(worldName + ",0,0,0");
                    fileWriter.write("\n");
                    fileWriter.write(worldName + ",1,1,1");
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
        catch (IOException error) {
            error.printStackTrace();
        }

    }
}
