package mc.carlton.freerpg.events.misc;


import java.util.Map;
import mc.carlton.freerpg.utils.globalVariables.ItemGroups;
import mc.carlton.freerpg.core.info.server.PlacedBlocksManager;
import mc.carlton.freerpg.core.info.server.WorldGuardChecks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;


public class PlayerBlockPlace implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  void onblockPlace(BlockPlaceEvent e) {
    Player p = e.getPlayer();
    Block block = e.getBlockPlaced();
    Location loc = block.getLocation();

    if (e.isCancelled()) {
      return;
    }

    //WorldGuard Check
    WorldGuardChecks BuildingCheck = new WorldGuardChecks();
    if (!BuildingCheck.canBuild(p, loc)) {
      return;
    }
    ItemGroups itemGroups = new ItemGroups();
    Map<Material, Boolean> trackedBlocks = itemGroups.getTrackedBlocks();
    Material blockType = block.getType();
    boolean isTracked = trackedBlocks.containsKey(blockType);
    if (isTracked) {
      PlacedBlocksManager blockTracker = new PlacedBlocksManager();
      blockTracker.addLocation(loc);
    }
  }
}
