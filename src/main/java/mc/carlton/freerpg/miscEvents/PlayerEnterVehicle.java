package mc.carlton.freerpg.miscEvents;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.perksAndAbilities.Agility;
import mc.carlton.freerpg.perksAndAbilities.BeastMastery;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityMountEvent;

public class PlayerEnterVehicle implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    void onEnterVehicle(VehicleEnterEvent e){
        if (e.isCancelled()) {
            return;
        }
        if (e.getEntered() instanceof Player) {
            Player p = (Player) e.getEntered();
            Agility agilityClass = new Agility(p);
            agilityClass.sprintingEXP(false);
        }


    }
}
