package mc.carlton.freerpg.core.info.server;

import java.util.HashSet;
import mc.carlton.freerpg.FreeRPG;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PlacedBlocksManager {

  private static HashSet<Location> blocks = new HashSet<>(); //The set of all tracked blocks on the server
  private static HashSet<Location> temporaryBlocks = new HashSet<>();  //Temporary blocks to be added to blocks later, but are still checked to see if tracked
  private static boolean isFileMangerMakingCopy = false;
  private static int copiesBeingMade = 0;


  public boolean isBlockTracked(Block block) {
    Location location = block.getLocation();
    return isLocationTracked(location);
  }

  public boolean isLocationTracked(Location location) {
    return blocks.contains(location) || temporaryBlocks.contains(
        location); //If it is in EITHER list
  }

  public HashSet<Location> getBlocks() {
    isFileMangerMakingCopy = true; //Let all classes know a copy is being made
    copiesBeingMade += 1; //Add to to total number of copies currently being made
    HashSet<Location> blocksCopy = new HashSet<>(blocks);
    copiesBeingMade -= 1; //Copy is made, reduce the number of total copies currently being made by one
    if (copiesBeingMade
        <= 0) { //If there are no copies being made (It can be the case that multiple calls to getBlocksMap() were made around the same time
      isFileMangerMakingCopy = false; // we can say tell all classes that no copies are being made
    }
    return blocksCopy;
  }

  public void setBlocksMap(HashSet<Location> newblocks) {
    this.blocks = newblocks;
  }

  public void addBlock(Block block) {
    Location location = block.getLocation();
    addLocation(location);
  }

  public void addLocation(Location location) {
    temporaryBlocks.add(
        location); //Adds the location to a temporary list (NEVER saved to file or iterated through)
    if (!isFileMangerMakingCopy) { //If we are making a copy
      blocks.add(location); //add the location to the main list of block
      temporaryBlocks.remove(location);  //Remove from the secondary list
    } else { //If we're not making a copy
      Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
      new BukkitRunnable() { //Try again in 1 tick (0.05 s)
        @Override
        public void run() {
          addLocation(location);
        }
      }.runTaskLater(plugin, 1).getTaskId();
    }
  }

  public void removeBlock(Block block) {
    Location location = block.getLocation();
    removeLocation(location);
  }

  public void removeLocation(Location location) {
    if (!isFileMangerMakingCopy) { //If we are not currently making a copy, remove the block
      if (blocks.contains(location)) {
        blocks.remove(location);
      }
    } else { //If we are currently making a copy, wait a tick to try to remove the location again
      Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
      new BukkitRunnable() {
        @Override
        public void run() {
          removeLocation(location);
        }
      }.runTaskLater(plugin, 1).getTaskId();
    }
  }

}
