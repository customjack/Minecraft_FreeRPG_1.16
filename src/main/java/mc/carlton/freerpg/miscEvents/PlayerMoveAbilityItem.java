package mc.carlton.freerpg.miscEvents;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.ActionBarMessages;
import mc.carlton.freerpg.gameTools.TrackItem;
import mc.carlton.freerpg.perksAndAbilities.Digging;
import mc.carlton.freerpg.perksAndAbilities.Mining;
import mc.carlton.freerpg.perksAndAbilities.Swordsmanship;
import mc.carlton.freerpg.playerInfo.AbilityLogoutTracker;
import mc.carlton.freerpg.playerInfo.AbilityTimers;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerMoveAbilityItem implements Listener {
    @EventHandler(priority =  EventPriority.HIGH)
    public void clickEvent(InventoryClickEvent e) {
        if (e.isCancelled()) {
            return;
        }
        InventoryType topInventoryType;
        Player p;
        Inventory topInventory;
        Inventory clickedInventory;
        InventoryType clickedInventoryType;
        try {
            p = (Player) e.getWhoClicked();
            clickedInventory = e.getClickedInventory();
            clickedInventoryType = clickedInventory.getType();
        } catch (Exception except) {
            return;
        }
        if (e.isShiftClick()) {
            ItemStack clickedItem = e.getCurrentItem();
            TrackItem trackItem = new TrackItem();
            NamespacedKey key = trackItem.getFreeRPGItemKey(clickedItem);
            if (key != null) {
                ItemMeta itemMeta = clickedItem.getItemMeta();
                PersistentDataContainer container = itemMeta.getPersistentDataContainer();
                String specialItemType = container.get(key, PersistentDataType.STRING);
                if (specialItemType == null) {
                    return;
                }
                if (specialItemType.equalsIgnoreCase("frpg-digging") || specialItemType.equalsIgnoreCase("frpg-mining")
                        || specialItemType.equalsIgnoreCase("frpg-swordsmanship")) {
                    e.setCancelled(true);
                }
            }
        }
        else if (e.getCursor() != null) {
            if (!e.getCursor().getType().equals(Material.AIR)) {
                ItemStack clickedItem = e.getCursor();
                TrackItem trackItem = new TrackItem();
                NamespacedKey key = trackItem.getFreeRPGItemKey(clickedItem);
                if (key != null) {
                    if (!clickedInventoryType.equals(InventoryType.PLAYER)) {
                        e.setCancelled(true);
                    }
                }
            /*
                ItemMeta itemMeta = clickedItem.getItemMeta();
                PersistentDataContainer container = itemMeta.getPersistentDataContainer();
                String specialItemType = container.get(key, PersistentDataType.STRING);
                if (specialItemType == null) {
                    return;
                }
                System.out.println(specialItemType);
                AbilityLogoutTracker logoutTracker = new AbilityLogoutTracker(p);
                if (specialItemType.equalsIgnoreCase("frpg-digging")) {
                    Digging diggingClass = new Digging(p);
                    int taskID = logoutTracker.getPlayerTasks(p)[0];
                    diggingClass.preventLogoutTheft(taskID,clickedItem,key,false);
                }
                else if (specialItemType.equalsIgnoreCase("frpg-mining")) {
                    Mining MiningClass = new Mining(p);
                    int taskID = logoutTracker.getPlayerTasks(p)[2];
                    MiningClass.preventLogoutTheft(taskID,clickedItem,key,false);
                }
                else if (specialItemType.equalsIgnoreCase("frpg-swordsmanship")) {
                    Swordsmanship SwordsmanshipClass = new Swordsmanship(p);
                    int taskID = logoutTracker.getPlayerTasks(p)[7];
                    SwordsmanshipClass.preventLogoutTheft(taskID, clickedItem, key, false);
                }
            }

             */
            }
        }
    }
}
