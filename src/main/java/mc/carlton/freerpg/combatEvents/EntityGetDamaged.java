package mc.carlton.freerpg.combatEvents;

import mc.carlton.freerpg.serverConfig.ConfigLoad;
import mc.carlton.freerpg.playerInfo.PlayerStats;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.*;

public class EntityGetDamaged implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)

    void onEntityDamaged(EntityDamageEvent e) {
        if (e.isCancelled()) {
            return;
        }
        ConfigLoad configLoad = new ConfigLoad();
        if (!configLoad.getAllowedSkillsMap().get("beastMastery")) {
            return;
        }
        if (e.getEntity() instanceof Entity) {
            Entity wolf = e.getEntity();
            if (wolf.getType() == EntityType.WOLF) {
                Tameable dog = (Tameable) wolf;
                if (dog.isTamed()){
                    if (!(dog.getOwner() instanceof Player)) { //Player is offline or something like that.
                        return;
                    }
                    Player p = (Player) dog.getOwner();
                    PlayerStats pStatClass = new PlayerStats(p);
                    Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
                    if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                        if ((int)pStat.get("beastMastery").get(11) > 0) {
                            e.setDamage(0);
                        }
                    }
                    else {
                        int thickFurLevel = (int)pStat.get("beastMastery").get(7);
                        double thickFurMultiplier = 1 - thickFurLevel*0.1;
                        e.setDamage(e.getDamage()*thickFurMultiplier);
                    }
                }
            }
        }
    }
}
