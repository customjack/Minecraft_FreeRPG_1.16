package mc.carlton.freerpg.miscEvents;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.perksAndAbilities.BeastMastery;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.plugin.Plugin;

public class TameEntityEvent implements Listener {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    @EventHandler(priority = EventPriority.HIGH)
    void onEntityTame(EntityTameEvent e){
        if (e.isCancelled()) {
            return;
        }
        Entity entity = e.getEntity();
        Player p = (Player) e.getOwner();
        BeastMastery beastMasteryClass = new BeastMastery(p);
        beastMasteryClass.tamingEXP(entity);

    }
}
