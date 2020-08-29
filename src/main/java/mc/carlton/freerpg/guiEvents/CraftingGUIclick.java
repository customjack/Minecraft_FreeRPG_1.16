package mc.carlton.freerpg.guiEvents;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class CraftingGUIclick implements Listener {
    @EventHandler
    public void clickEvent(InventoryClickEvent e) {
        boolean proceed = true;
        try {
            InventoryType invType = e.getClickedInventory().getType();
        } catch (Exception except) {
            proceed = false;
            return;
        }
        if (e.getView().getTitle().equalsIgnoreCase("Crafting Recipe")) {
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
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().getType() == Material.ARROW) {
                    switch (inv.getItem(25).getType()) {
                        case TIPPED_ARROW:
                            p.performCommand("frpg skillTreeGUI archery");
                            break;
                        case COW_SPAWN_EGG:
                        case BEE_SPAWN_EGG:
                        case MOOSHROOM_SPAWN_EGG:
                        case HORSE_SPAWN_EGG:
                        case SLIME_SPAWN_EGG:
                            p.performCommand("frpg skillTreeGUI farming");
                            break;
                        case POTION:
                            p.performCommand("frpg skillTreeGUI alchemy");
                            break;
                        default:
                            p.performCommand("frpg skillTreeGUI enchanting");
                            break;
                    }
                }
            }

            e.setCancelled(true); //So they cant take the items
        }
    }
}
