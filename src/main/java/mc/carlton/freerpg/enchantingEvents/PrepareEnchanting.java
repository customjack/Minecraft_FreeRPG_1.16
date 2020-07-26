package mc.carlton.freerpg.enchantingEvents;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.perksAndAbilities.Enchanting;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.plugin.Plugin;

public class PrepareEnchanting implements Listener {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    @EventHandler
    void onEnchantOpen(PrepareItemEnchantEvent e) {
        Player p = (Player) e.getEnchanter();
        Enchanting enchantingClass = new Enchanting(p);
        EnchantmentOffer[] oldOffers = e.getOffers();
        EnchantmentOffer[] newOffers = enchantingClass.enchantmentDiscount(oldOffers);

    }
}
