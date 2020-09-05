package mc.carlton.freerpg.miscEvents;

import mc.carlton.freerpg.gameTools.EntityPickedUpItemStorage;
import mc.carlton.freerpg.perksAndAbilities.Fishing;
import mc.carlton.freerpg.playerAndServerInfo.AbilityTracker;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

public class EntityPickUpItem implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    void onEntityPickUpItem(EntityPickupItemEvent e) {
        if (e.isCancelled()) {
            return;
        }
        LivingEntity entity= e.getEntity();
        ItemStack item = e.getItem().getItemStack();
        if (!(entity instanceof Player)) {
            EntityPickedUpItemStorage entityPickedUpItemStorage = new EntityPickedUpItemStorage();
            entityPickedUpItemStorage.addItemKey(item, entity);
        }
    }
}
