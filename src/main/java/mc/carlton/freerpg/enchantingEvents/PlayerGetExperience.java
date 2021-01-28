package mc.carlton.freerpg.enchantingEvents;

import mc.carlton.freerpg.gameTools.ExperienceBottleTracking;
import mc.carlton.freerpg.perksAndAbilities.Enchanting;
import mc.carlton.freerpg.serverConfig.ConfigLoad;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class PlayerGetExperience implements Listener {
    @EventHandler
    void onExperienceGain(PlayerExpChangeEvent e){
        ConfigLoad configLoad = new ConfigLoad();
        if (!configLoad.getAllowedSkillsMap().get("enchanting")) {
            return;
        }
        Player p = e.getPlayer();
        boolean naturalEXPGain = false;
        boolean fromBottle = false;
        for(Entity entity: p.getNearbyEntities(1,1,1) ) {
            if (entity.getType().equals(EntityType.EXPERIENCE_ORB)) {
                naturalEXPGain = true;
                if (!configLoad.isGetEXPFromEnchantingBottles()) {
                    ExperienceBottleTracking experienceBottleTracking = new ExperienceBottleTracking();
                    fromBottle = experienceBottleTracking.fromEnchantingBottle(entity);
                    break;
                }
            }
        }
        if (naturalEXPGain && !fromBottle) {
            Enchanting enchantingClass = new Enchanting(p);
            enchantingClass.giveEXP(e.getAmount());
            int oldAmount = e.getAmount();
            int newAmount = enchantingClass.xpIncrease(oldAmount);
            e.setAmount(newAmount);
        }


    }
}
