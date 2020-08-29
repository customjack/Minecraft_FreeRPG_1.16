package mc.carlton.freerpg.combatEvents;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.playerAndServerInfo.ConfigLoad;
import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Map;

public class LingeringPotionSplash implements Listener {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    @EventHandler(priority = EventPriority.HIGH)
    void onPotionSplash(LingeringPotionSplashEvent e) {
        if (e.isCancelled()) {
            return;
        }
        ConfigLoad configLoad = new ConfigLoad();
        if (!configLoad.getAllowedSkillsMap().get("alchemy")) {
            return;
        }

        ThrownPotion potionEntity = e.getEntity();
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
        double durationMultiplier = potionDurationLevel * 0.001 + 1;
        for (PotionEffect effect : potionEntity.getEffects()) {
            e.getAreaEffectCloud().addCustomEffect(new PotionEffect(effect.getType(), (int) Math.round(effect.getDuration() * durationMultiplier), effect.getAmplifier() + potionMasterLevel), true);
        }

    }
}
