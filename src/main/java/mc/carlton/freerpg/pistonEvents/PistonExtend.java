package mc.carlton.freerpg.pistonEvents;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.playerAndServerInfo.PlacedBlocks;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class PistonExtend implements Listener {
    @EventHandler
    void onPistonExtend(BlockPistonExtendEvent e) {
        List<Block> blocks = e.getBlocks();
        if (blocks.size() != 0) {
            World world = blocks.get(0).getWorld();
            Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
            for (Block block : blocks) {
                Location loc = block.getLocation();
                PlacedBlocks placedClass = new PlacedBlocks();
                ArrayList<Location> blocksLocations = placedClass.getBlocks();
                boolean natural = true;
                innerLoop:
                for (Location blockLocation : blocksLocations) {
                    if (loc.equals(blockLocation)) {
                        blocksLocations.remove(blockLocation);
                        placedClass.setBlocks(blocksLocations);
                        natural = false;
                        break innerLoop;
                    }
                }
                if (natural == false) {
                    Vector dir = e.getDirection().getDirection();
                    int newX = block.getX()+dir.getBlockX();
                    int newY = block.getY()+dir.getBlockX();
                    int newZ = block.getZ()+dir.getBlockZ();
                    Location newLoc = new Location(world,newX,newY,newZ);
                    blocksLocations.add(newLoc);
                    placedClass.setBlocks(blocksLocations);
                }

            }
        }
    }

}
