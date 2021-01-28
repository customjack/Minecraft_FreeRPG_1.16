package mc.carlton.freerpg.combatEvents;


import mc.carlton.freerpg.perksAndAbilities.Agility;
import mc.carlton.freerpg.perksAndAbilities.Global;
import mc.carlton.freerpg.serverConfig.ConfigLoad;
import mc.carlton.freerpg.playerInfo.PlayerStats;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class PlayerTakeDamage implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    void onPlayerTakeDamage(EntityDamageEvent e){
        if (e.isCancelled()) {
            return;
        }
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            PlayerStats pStatClass = new PlayerStats(p);
            Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
            Random rand = new Random();
            if (e.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
                ConfigLoad configLoad = new ConfigLoad();
                if (!configLoad.getAllowedSkillsMap().get("mining")) {
                    return;
                }
                if ( (int)pStat.get("mining").get(12) >0) {
                    e.setDamage(0);
                }
            }
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                ConfigLoad configLoad = new ConfigLoad();
                if (!configLoad.getAllowedSkillsMap().get("agility")) {
                    return;
                }
                Agility agilityClass = new Agility(p);
                double damageReduction =  agilityClass.roll(e.getFinalDamage());
                e.setDamage(e.getDamage()*damageReduction);
            }

            if (e.getFinalDamage() > p.getHealth()) {
                ConfigLoad configLoad = new ConfigLoad();
                if (!configLoad.getAllowedSkillsMap().get("global")) {
                    return;
                }
                if ((int) pStat.get("global").get(10) > 0) {
                    if ( 0.1 > rand.nextFloat()) {
                        e.setDamage(0);
                        Global globalClass = new Global(p);
                        globalClass.avatar();
                    }
                }
            }
        }

    }
}
