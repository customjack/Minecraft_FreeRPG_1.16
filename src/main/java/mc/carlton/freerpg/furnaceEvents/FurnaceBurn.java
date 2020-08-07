package mc.carlton.freerpg.furnaceEvents;

import mc.carlton.freerpg.gameTools.FurnaceUserTracker;
import mc.carlton.freerpg.perksAndAbilities.Smelting;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;

public class FurnaceBurn implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    void onBurn(FurnaceBurnEvent e){
        if (e.isCancelled()) {
            return;
        }
        boolean isBlast = false;
        if (e.getBlock().getType() == Material.BLAST_FURNACE || e.getBlock().getType() == Material.SMOKER) {
            isBlast = true;
        }
        Furnace furnace = (Furnace) e.getBlock().getState();
        FurnaceUserTracker furnaceTracker = new FurnaceUserTracker();
        Player p = furnaceTracker.getPlayer(furnace.getLocation());
        if (p != null) {
            Smelting smeltingClass = new Smelting(p);
            smeltingClass.fuelBurn(furnace,isBlast);
        }

    }
}
