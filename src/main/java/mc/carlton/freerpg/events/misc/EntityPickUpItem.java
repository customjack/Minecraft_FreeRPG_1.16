package mc.carlton.freerpg.events.misc;

import java.util.List;
import mc.carlton.freerpg.utils.game.EntityPickedUpItemStorage;
import mc.carlton.freerpg.utils.globalVariables.EntityGroups;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class EntityPickUpItem implements Listener {

  @EventHandler(priority = EventPriority.HIGH)
  void onEntityPickUpItem(EntityPickupItemEvent e) {
    if (e.isCancelled()) {
      return;
    }
    LivingEntity entity = e.getEntity();
    ItemStack item = e.getItem().getItemStack();
    EntityGroups entityGroups = new EntityGroups();
    List<EntityType> hostileMobs = entityGroups.getHostileMobs();
    if (hostileMobs.contains(entity.getType())) {
      EntityPickedUpItemStorage entityPickedUpItemStorage = new EntityPickedUpItemStorage();
      entityPickedUpItemStorage.addPickedUpItem(item, entity);
    }
  }
}
