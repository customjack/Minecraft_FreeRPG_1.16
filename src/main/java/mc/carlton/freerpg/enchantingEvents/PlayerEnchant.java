package mc.carlton.freerpg.enchantingEvents;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.perksAndAbilities.Enchanting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.plugin.Plugin;

public class PlayerEnchant implements Listener {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    @EventHandler
    void onEnchant(EnchantItemEvent e){
        Player p = (Player) e.getEnchanter();
        Enchanting enchantingClass = new Enchanting(p);
        enchantingClass.enchantItem(e.getItem(),e.whichButton(),(EnchantingInventory) e.getInventory());

    }
}
