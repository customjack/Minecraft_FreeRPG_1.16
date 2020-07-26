package mc.carlton.freerpg.playerAndServerInfo;

import mc.carlton.freerpg.FreeRPG;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;

public class PlacedBlocksLoadIn {
    public ArrayList<Location> getPlacedBlocks() throws IOException {
        ArrayList<Location> locations = new ArrayList<>();
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        File f = new File(plugin.getDataFolder(), "blockLocations.dat");
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
                    locations.add(location);
                    line = fileReader.readLine();
                }
                fileReader.close();
            } catch (IOException error) {
                error.printStackTrace();
            }
        }
        return locations;
    }
    public void setPlacedBlocks() throws IOException {
        PlacedBlocks blocksClass = new PlacedBlocks();
        ArrayList<Location> locations = blocksClass.getBlocks();
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        File f = new File(plugin.getDataFolder(), "blockLocations.dat");
        String path = f.getPath();
        if (f.exists()) {
            try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(path,false))) {
                for (int i = 0; i < locations.size(); i++) {
                    Location location = locations.get(i);
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
                fileWriter.close();
            } catch (IOException error) {
                error.printStackTrace();
            }
        }
    }

}
