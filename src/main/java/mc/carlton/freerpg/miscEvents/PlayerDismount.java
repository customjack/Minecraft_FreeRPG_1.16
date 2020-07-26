package mc.carlton.freerpg.miscEvents;

import mc.carlton.freerpg.gameTools.HorseRiding;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;

public class PlayerDismount implements Listener {
    @EventHandler
    void onPlayerTakeDamage(EntityDismountEvent e){
        Entity mount = e.getDismounted();
        HorseRiding horseRiding = new HorseRiding();
        Player p = horseRiding.getPlayerFromMount(mount.getUniqueId());
        if (p!=null){
            horseRiding.deletePlayerData(p);
        }

    }
}
