package mc.carlton.freerpg.combatEvents;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.globalVariables.ItemGroups;
import mc.carlton.freerpg.playerAndServerInfo.ConfigLoad;
import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PotionSplash implements Listener {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    @EventHandler(priority = EventPriority.HIGH)
    void onPotionSplash(PotionSplashEvent e){
        if (e.isCancelled()) {
            return;
        }
        ConfigLoad configLoad = new ConfigLoad();
        if (!configLoad.getAllowedSkillsMap().get("alchemy")) {
            return;
        }
        ItemGroups itemGroups = new ItemGroups();
        List<PotionEffectType> harmfulEffects = itemGroups.getHarmfulEffects();

        ThrownPotion potionEntity = e.getPotion();
        if (!(potionEntity.getShooter() instanceof Player)) {
            return;
        }
        Player p = (Player) potionEntity.getShooter();
        PlayerStats pStatClass = new PlayerStats(p);
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int potionMasterLevel = (int) pStat.get("alchemy").get(13);
        if ((int)pStat.get("global").get(15) != 1) {
            potionMasterLevel = 0;
        }
        int potionDurationLevel = (int) pStat.get("alchemy").get(4);
        double durationMultiplier = potionDurationLevel*0.001 + 1;
        for (LivingEntity entity : e.getAffectedEntities()) {
            for (PotionEffect effect : e.getPotion().getEffects()) {
                if (p.equals(entity)) {
                    if (harmfulEffects.contains(effect.getType())) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                entity.addPotionEffect(new PotionEffect(effect.getType(), (int) Math.round(effect.getDuration() * (1.0/durationMultiplier)), effect.getAmplifier()),true);
                            }
                        }.runTaskLater(plugin, 2);
                    }
                    else {
                        entity.addPotionEffect(new PotionEffect(effect.getType(), (int) Math.round(effect.getDuration() * durationMultiplier), effect.getAmplifier() + potionMasterLevel),true);
                    }
                }
                else {
                    entity.addPotionEffect(new PotionEffect(effect.getType(), (int) Math.round(effect.getDuration() * durationMultiplier), effect.getAmplifier() + potionMasterLevel),true);
                }
            }
        }


    }
}
