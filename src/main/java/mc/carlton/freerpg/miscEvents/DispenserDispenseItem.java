package mc.carlton.freerpg.miscEvents;

import mc.carlton.freerpg.gameTools.EntityPickedUpItemStorage;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class DispenserDispenseItem implements Listener {

  @EventHandler(priority = EventPriority.HIGH)
  void onDispenserDispense(BlockDispenseEvent e) {
    if (e.isCancelled()) {
      return;
    }
    ItemStack item = e.getItem();
    Block dispenser = e.getBlock();
    if (dispenser.getBlockData() instanceof Directional) {
      Directional directional = (Directional) dispenser.getBlockData();
      Vector normalVector = directional.getFacing().getDirection();
      Location location = e.getBlock().getLocation().add(normalVector);
      EntityPickedUpItemStorage entityPickedUpItemStorage = new EntityPickedUpItemStorage();
      entityPickedUpItemStorage.addPickedUpItemFromDispenser(item, location);
    }
  }
}
