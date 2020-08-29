package mc.carlton.freerpg.pistonEvents;

import mc.carlton.freerpg.playerAndServerInfo.PlacedBlocksManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.util.Vector;

import java.util.List;

public class PistonExtend implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    void onPistonExtend(BlockPistonExtendEvent e) {
        if (e.isCancelled()) {
            return;
        }
        List<Block> blocks = e.getBlocks();
        PlacedBlocksManager blockTracker = new PlacedBlocksManager();
        if (blocks.size() != 0) {
            World world = blocks.get(0).getWorld();
            for (Block block : blocks) {
                boolean natural = !blockTracker.isBlockTracked(block);
                if (natural == false) {
                    blockTracker.removeBlock(block);
                    Vector dir = e.getDirection().getDirection();
                    int newX = block.getX()+dir.getBlockX();
                    int newY = block.getY()+dir.getBlockY();
                    int newZ = block.getZ()+dir.getBlockZ();
                    Location newLoc = new Location(world,newX,newY,newZ);
                    blockTracker.addLocation(newLoc);
                }

            }
        }
    }

}
