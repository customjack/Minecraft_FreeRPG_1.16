package mc.carlton.freerpg.guiEvents;

import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class ConfigurationGUIClick implements Listener {
    @EventHandler
    public void clickEvent(InventoryClickEvent e) {
        boolean proceed = true;
        try {
            InventoryType invType = e.getClickedInventory().getType();
        } catch (Exception except) {
            proceed = false;
            return;
        }
        if (e.getView().getTitle().equalsIgnoreCase("Configuration Window")) {
            if (e.getClickedInventory().getType() == InventoryType.PLAYER) {
                e.setCancelled(true);
                return;
            }
            if (e.getClick() != ClickType.LEFT) {
                e.setCancelled(true);
            }
            if (e.getCursor().getType() != Material.AIR) {
                e.setCancelled(true);
            }
            Player p = (Player) e.getWhoClicked();
            Inventory inv = e.getClickedInventory();
            PlayerStats pStatClass = new PlayerStats(p);
            Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
            Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
            if (e.getCurrentItem() != null) {
                switch (e.getRawSlot()) {
                    case 21:
                        if ((int)pStat.get("global").get(21) > 0) {
                            pStat.get("global").set(21, 0);
                        }
                        else {
                            pStat.get("global").set(21, 1);
                        }
                        statAll.put(p.getUniqueId(), pStat);
                        pStatClass.setData(statAll);
                        p.performCommand("frpg configurationGUI");
                        break;
                    case 23:
                        if ((int)pStat.get("global").get(22) > 0) {
                            pStat.get("global").set(22, 0);
                        }
                        else {
                            pStat.get("global").set(22, 1);
                        }
                        statAll.put(p.getUniqueId(), pStat);
                        pStatClass.setData(statAll);
                        p.performCommand("frpg configurationGUI");
                        break;
                    case 45:
                        p.performCommand("frpg skills");
                    default:
                        break;
                }
            }

            e.setCancelled(true); //So they cant take the items
        }
    }
}

