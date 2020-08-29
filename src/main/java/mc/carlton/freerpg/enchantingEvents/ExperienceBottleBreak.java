package mc.carlton.freerpg.enchantingEvents;

import mc.carlton.freerpg.gameTools.ExperienceBottleTracking;
import mc.carlton.freerpg.perksAndAbilities.BeastMastery;
import mc.carlton.freerpg.perksAndAbilities.Enchanting;
import mc.carlton.freerpg.perksAndAbilities.Farming;
import mc.carlton.freerpg.playerAndServerInfo.ConfigLoad;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ExpBottleEvent;

public class ExperienceBottleBreak implements Listener {
    @EventHandler
    void onExpBottleBreak(ExpBottleEvent e){
        ConfigLoad configLoad = new ConfigLoad();
        if (!configLoad.isGetEXPFromEnchantingBottles()) {
            ExperienceBottleTracking experienceBottleTracking = new ExperienceBottleTracking();
            experienceBottleTracking.addLocation(e.getEntity().getLocation());
        }
    }
}
