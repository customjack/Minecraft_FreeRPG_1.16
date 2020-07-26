package mc.carlton.freerpg.enchantingEvents;

import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;

import java.util.ArrayList;
import java.util.Map;

public class PrepareRepair implements Listener {
    @EventHandler
    void onRepairOpen(PrepareAnvilEvent e){
        AnvilInventory anvil = e.getInventory();
        Player p = (Player) anvil.getViewers().get(0);
        PlayerStats pStatClass = new PlayerStats(p);
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int levelSubtract = (int) pStat.get("enchanting").get(7);
        int newCost = Math.max(2,anvil.getRepairCost()-levelSubtract);
        anvil.setRepairCost(newCost);


    }
}
