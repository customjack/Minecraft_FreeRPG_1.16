package mc.carlton.freerpg.gameTools;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class TrackItem {

    public ItemStack findTrackedItemInInventory(Player p, NamespacedKey key) {
        for (ItemStack item : p.getInventory().getContents()) {
           if (doesItemHaveKey(item,key)) {
               return item;
           }
        }
        ItemStack cursorItem = p.getItemOnCursor();
        if (doesItemHaveKey(cursorItem,key)) {
            removeAllFreeRPGKeys(cursorItem);
            return cursorItem;
        }
        return null;
    }

    public boolean doesItemHaveKey(ItemStack item,NamespacedKey key) {
        if (item == null) {
            return false;
        }
        if (item.getType() == Material.AIR) {
            return false;
        }
        return removeItemKey(item,key);
    }

    public NamespacedKey getFreeRPGItemKey(ItemStack item) {
        if (item == null) {
            return null;
        }
        if (item.getType() == Material.AIR) {
            return null;
        }
        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        for (NamespacedKey key : container.getKeys()) {
            if (container.get(key, PersistentDataType.STRING).contains("frpg")) {
                return key;
            }
        }
        return null;
    }

    public boolean removeItemKey(ItemStack item, NamespacedKey key) {
        if (key == null) {
            return false;
        }
        boolean didRemove = false;
        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        if (container.has(key,PersistentDataType.STRING)) {
            container.remove(key);
            item.setItemMeta(itemMeta);
            didRemove = true;
        }
        return didRemove;
    }

    public void removeAllFreeRPGKeys(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        for (NamespacedKey key : container.getKeys()) {
            if (container.get(key, PersistentDataType.STRING).contains("frpg")) {
                container.remove(key);
            }
        }
        item.setItemMeta(itemMeta);
    }
}
