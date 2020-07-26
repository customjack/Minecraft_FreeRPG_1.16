package mc.carlton.freerpg.furnaceEvents;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.FurnaceUserTracker;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;

public class FurnaceInventoryClick implements Listener {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    @EventHandler
    void onInventoryClick(InventoryClickEvent e) {
        try {
            InventoryType invType = e.getClickedInventory().getType();
        } catch (Exception except) {
            return;
        }
        if (e.getInventory().getHolder() instanceof Furnace) { //This section is very buggy, and not clean. Hopefully I can improve it in the future
            Furnace furnace = (Furnace) e.getInventory().getHolder();
            Player p = (Player) e.getWhoClicked();
            if (furnace.getBurnTime() == 0 || furnace.getCookTime() == 0) {
                FurnaceUserTracker furnaceTracker = new FurnaceUserTracker();
                furnaceTracker.addfurnaceLocation(furnace.getLocation(),p);
            }
        }
    }
}
