package mc.carlton.freerpg.combatEvents;


import mc.carlton.freerpg.perksAndAbilities.Agility;
import mc.carlton.freerpg.perksAndAbilities.Global;
import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class PlayerTakeDamage implements Listener {

    @EventHandler
    void onPlayerTakeDamage(EntityDamageEvent e){
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            PlayerStats pStatClass = new PlayerStats(p);
            Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
            Random rand = new Random();
            if (e.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
                if ( (int)pStat.get("mining").get(12) >0) {
                    e.setDamage(0);
                }
            }
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                Agility agilityClass = new Agility(p);
                double damageReduction =  agilityClass.roll(e.getFinalDamage());
                if (damageReduction < 1) {
                    p.sendMessage(ChatColor.GREEN + ">>>ROLL<<<");
                }
                e.setDamage(e.getDamage()*damageReduction);
            }

            if (e.getFinalDamage() > p.getHealth()) {
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
