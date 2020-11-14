package mc.carlton.freerpg.serverInfo;

import mc.carlton.freerpg.FreeRPG;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlacedBlocksManager {
    static HashSet<Location> blocks = new HashSet<>();


    public boolean isBlockTracked(Block block) {
        Location location = block.getLocation();
        return blocks.contains(location);
    }
    public boolean isLocationTracked(Location location) {
        return blocks.contains(location);
    }

    public HashSet<Location> getBlocksMap() {
        return blocks;
    }

    public void setBlocksMap(HashSet<Location> newblocks) {
        this.blocks = newblocks;
    }

    public void addBlock(Block block) {
        Location location = block.getLocation();
        blocks.add(location);
    }

    public void addLocation(Location location) {
        blocks.add(location);
    }
    public void removeBlock(Block block) {
        Location location = block.getLocation();
        removeLocation(location);
    }
    public void removeLocation(Location location) {
        if (blocks.contains(location)) {
            blocks.remove(location);
        }
    }

}
