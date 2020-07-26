package mc.carlton.freerpg.combatEvents;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
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
    @EventHandler
    void onPotionSplash(PotionSplashEvent e){
        PotionEffectType[] harmfulEffects0 = {PotionEffectType.WEAKNESS,PotionEffectType.POISON,PotionEffectType.BLINDNESS,PotionEffectType.HUNGER,
                                              PotionEffectType.HARM,PotionEffectType.SLOW_DIGGING,PotionEffectType.SLOW,PotionEffectType.WEAKNESS,PotionEffectType.WITHER};
        List<PotionEffectType> harmfulEffects = Arrays.asList(harmfulEffects0);

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
        ItemStack potion = potionEntity.getItem();
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
