package mc.carlton.freerpg.miscEvents;

import mc.carlton.freerpg.perksAndAbilities.BeastMastery;
import mc.carlton.freerpg.perksAndAbilities.Farming;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;

public class PlayerBreedEntity implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    void onPlayerBreed(EntityBreedEvent e){
        if (e.isCancelled()) {
            return;
        }
        if (e.getBreeder() instanceof Player) {
            Player p = (Player) e.getBreeder();
            Entity entity = e.getEntity();
            Farming farmingClass = new Farming(p);
            farmingClass.breedingEXP(entity);
            BeastMastery beastMasteryClass = new BeastMastery(p);
            beastMasteryClass.breedingEXP(entity);
        }

    }
}
