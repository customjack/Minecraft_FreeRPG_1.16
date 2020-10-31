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
    static ConcurrentHashMap<Location,Boolean> blocks = new ConcurrentHashMap<>();


    public boolean isBlockTracked(Block block) {
        Location location = block.getLocation();
        return blocks.contains(location);
    }

    public ConcurrentHashMap<Location,Boolean> getBlocksMap() {
        return blocks;
    }

    public void setBlocksMap(ConcurrentHashMap<Location,Boolean> newblocks) {
        this.blocks = newblocks;
    }

    public void addBlock(Block block) {
        Location location = block.getLocation();
        blocks.put(location,true);
    }

    public void addLocation(Location location) {
        blocks.put(location,true);
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
