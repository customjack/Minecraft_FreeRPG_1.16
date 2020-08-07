package mc.carlton.freerpg.miscEvents;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.perksAndAbilities.BeastMastery;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityMountEvent;

public class PlayerMount implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    void onPlayerMount(EntityMountEvent e){
        if (e.isCancelled()) {
            return;
        }
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        new BukkitRunnable() {
            @Override
            public void run() {
                Entity mount = e.getMount();
                for (Entity passenger : e.getMount().getPassengers()) {
                    if (passenger instanceof Player) {
                        Player p = (Player) passenger;
                        BeastMastery beastMasteryClass = new BeastMastery(p);
                        beastMasteryClass.horseRidingEXP(mount);
                    }
                }

            }
        }.runTaskLater(plugin, 1);

    }
}
