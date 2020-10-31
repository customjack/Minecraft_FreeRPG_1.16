package mc.carlton.freerpg.combatEvents;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.globalVariables.ItemGroups;
import mc.carlton.freerpg.serverInfo.ConfigLoad;
import mc.carlton.freerpg.playerInfo.PlayerStats;
import org.bukkit.GameMode;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
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
        if (p.getGameMode() == GameMode.CREATIVE) {
            return;
        }
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
                        int finalPotionMasterLevel = potionMasterLevel;
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (entity.equals(p)) {
                                    if (!effect.getType().equals(PotionEffectType.HARM)) {
                                        entity.addPotionEffect(new PotionEffect(effect.getType(), (int) Math.round(effect.getDuration() * (1.0 / durationMultiplier)), effect.getAmplifier()), true);
                                    }
                                }
                                else if (!(entity instanceof Player)){
                                    if (!effect.getType().equals(PotionEffectType.HARM)) {
                                        entity.addPotionEffect(new PotionEffect(effect.getType(), (int) Math.round(effect.getDuration()*durationMultiplier), effect.getAmplifier()+ finalPotionMasterLevel), true);
                                    }
                                    else {
                                        if (finalPotionMasterLevel > 0) {
                                            entity.addPotionEffect(new PotionEffect(effect.getType(), 1, 0)); //Add 3 HP to whatever the damage was
                                        }
                                    }
                                }
                                else {
                                    if (configLoad.isAllowPvP()) {
                                        if (!effect.getType().equals(PotionEffectType.HARM)) {
                                            entity.addPotionEffect(new PotionEffect(effect.getType(), (int) Math.round(effect.getDuration()*durationMultiplier), effect.getAmplifier()+ finalPotionMasterLevel), true);
                                        }
                                        else {
                                            if (finalPotionMasterLevel > 0) {
                                                entity.addPotionEffect(new PotionEffect(effect.getType(), 1, 0)); //Add 3 HP to whatever the damage was
                                            }
                                        }
                                    }
                                }
                            }
                        }.runTaskLater(plugin, 2);
                    }
                    else if (effect.getType().equals(PotionEffectType.HEAL)) {
                        if (potionMasterLevel > 0) {
                            entity.addPotionEffect(new PotionEffect(effect.getType(), 1, 0)); //Add 2 HP to whatever the healing was
                        }
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
