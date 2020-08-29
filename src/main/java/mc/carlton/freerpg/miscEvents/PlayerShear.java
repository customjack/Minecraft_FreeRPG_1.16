package mc.carlton.freerpg.miscEvents;

import mc.carlton.freerpg.perksAndAbilities.Farming;
import mc.carlton.freerpg.playerAndServerInfo.ConfigLoad;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;

public class PlayerShear implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    void onPlayerShear(PlayerShearEntityEvent e){
        if (e.isCancelled()) {
            return;
        }
        ConfigLoad configLoad = new ConfigLoad();
        if (!configLoad.getAllowedSkillsMap().get("farming")) {
            return;
        }
        Player p = e.getPlayer();
        Entity entity = e.getEntity();
        World world = p.getWorld();
        e.setCancelled(true);

        //Farming
        Farming farmingClass = new Farming(p);
        farmingClass.shearSheep(entity,world);

    }
}
