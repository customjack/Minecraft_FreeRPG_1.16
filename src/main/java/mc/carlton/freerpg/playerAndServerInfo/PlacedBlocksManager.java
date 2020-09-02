package mc.carlton.freerpg.playerAndServerInfo;

import mc.carlton.freerpg.FreeRPG;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlacedBlocksManager {
    static Map<Location,Boolean> blocks = new ConcurrentHashMap<>();


    public void initializePlacedBlocks(){
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        File f = new File(plugin.getDataFolder(), "blockLocations.dat");
        f.setReadable(true,false);
        f.setWritable(true,false);
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
                    blocks.putIfAbsent(location,true);
                    line = fileReader.readLine();
                }
                fileReader.close();
            } catch (IOException error) {
                error.printStackTrace();
            }
        }
    }
    public void writePlacedBlocks() {
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        File f = new File(plugin.getDataFolder(), "blockLocations.dat");
        f.setReadable(true,false);
        f.setWritable(true,false);
        String path = f.getPath();
        if (f.exists()) {
            try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(path,false))) {
                for (Location location : blocks.keySet()) {
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

    public void startConditions(){
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        File f = new File(plugin.getDataFolder(),"blockLocations.dat");
        f.setReadable(true,false);
        f.setWritable(true,false);
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

    public boolean isBlockTracked(Block block) {
        Location location = block.getLocation();
        return blocks.containsKey(location);
    }

    public Map<Location,Boolean> getBlocksMap() {
        return blocks;
    }

    public void setBlocksMap(Map<Location,Boolean> newblocks) {
        this.blocks = newblocks;
    }

    public void addBlock(Block block) {
        Location location = block.getLocation();
        blocks.putIfAbsent(location,true);
    }

    public void addLocation(Location location) {
        blocks.putIfAbsent(location,true);
    }
    public void removeBlock(Block block) {
        Location location = block.getLocation();
        if (blocks.containsKey(location)) {
            blocks.remove(location);
        }
    }
    public void removeLocation(Location location) {
        if (blocks.containsKey(location)) {
            blocks.remove(location);
        }
    }

}
