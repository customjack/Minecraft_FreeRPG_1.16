package mc.carlton.freerpg.pistonEvents;

import java.util.ArrayList;
import java.util.List;
import mc.carlton.freerpg.serverInfo.PlacedBlocksManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.util.Vector;

public class PistonEvents implements Listener {

  @EventHandler(priority = EventPriority.HIGH)
  void onPistonExtend(BlockPistonExtendEvent e) {
    pistonChangeBlockLocation(e);
  }

  @EventHandler(priority = EventPriority.HIGH)
  void onPistonRetract(BlockPistonRetractEvent e) {
    pistonChangeBlockLocation(e);
  }

  private void pistonChangeBlockLocation(BlockPistonExtendEvent e) {
    if (e.isCancelled()) {
      return;
    }
    List<Block> blocks = e.getBlocks();
    PlacedBlocksManager blockTracker = new PlacedBlocksManager();
    if (blocks.size() != 0) {
      ArrayList<Location> trackedLocations = new ArrayList<>();
      World world = blocks.get(0).getWorld();
      Vector dir = e.getDirection().getDirection();
      for (Block block : blocks) {
        boolean natural = !blockTracker.isBlockTracked(block);
        if (natural == false) {
          trackedLocations.add(block.getLocation());
          blockTracker.removeBlock(block);
        }
      }
      for (Location loc : trackedLocations) {
        int newX = loc.getBlockX() + dir.getBlockX();
        int newY = loc.getBlockY() + dir.getBlockY();
        int newZ = loc.getBlockZ() + dir.getBlockZ();
        blockTracker.addLocation(new Location(world, newX, newY, newZ));
      }
    }
  }

  private void pistonChangeBlockLocation(BlockPistonRetractEvent e) {
    if (e.isCancelled()) {
      return;
    }
    List<Block> blocks = e.getBlocks();
    PlacedBlocksManager blockTracker = new PlacedBlocksManager();
    if (blocks.size() != 0) {
      ArrayList<Location> trackedLocations = new ArrayList<>();
      World world = blocks.get(0).getWorld();
      Vector dir = e.getDirection().getDirection();
      for (Block block : blocks) {
        boolean natural = !blockTracker.isBlockTracked(block);
        if (natural == false) {
          trackedLocations.add(block.getLocation());
          blockTracker.removeBlock(block);
        }
      }
      for (Location loc : trackedLocations) {
        int newX = loc.getBlockX() + dir.getBlockX();
        int newY = loc.getBlockY() + dir.getBlockY();
        int newZ = loc.getBlockZ() + dir.getBlockZ();
        blockTracker.addLocation(new Location(world, newX, newY, newZ));
      }
    }
  }

}
