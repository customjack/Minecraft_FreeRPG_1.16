package mc.carlton.freerpg.enchantingEvents;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.perksAndAbilities.Enchanting;
import mc.carlton.freerpg.playerAndServerInfo.ConfigLoad;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.plugin.Plugin;

public class PrepareEnchanting implements Listener {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    @EventHandler(priority = EventPriority.HIGH)
    void onEnchantOpen(PrepareItemEnchantEvent e) {
        if (e.isCancelled()) {
            return;
        }
        ConfigLoad configLoad = new ConfigLoad();
        if (!configLoad.getAllowedSkillsMap().get("enchanting")) {
            return;
        }
        Player p = (Player) e.getEnchanter();
        Enchanting enchantingClass = new Enchanting(p);
        EnchantmentOffer[] oldOffers = e.getOffers();
        EnchantmentOffer[] newOffers = enchantingClass.enchantmentDiscount(oldOffers);

    }
}
