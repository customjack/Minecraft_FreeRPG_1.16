package mc.carlton.freerpg.events.misc;

import mc.carlton.freerpg.skills.perksAndAbilities.BeastMastery;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;

public class TameEntityEvent implements Listener {

  @EventHandler(priority = EventPriority.HIGH)
  void onEntityTame(EntityTameEvent e) {
    if (e.isCancelled()) {
      return;
    }
    Entity entity = e.getEntity();
    Player p = (Player) e.getOwner();
    BeastMastery beastMasteryClass = new BeastMastery(p);
    beastMasteryClass.tamingEXP(entity);

  }
}
