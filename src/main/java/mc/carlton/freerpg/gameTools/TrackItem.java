package mc.carlton.freerpg.gameTools;

import mc.carlton.freerpg.playerInfo.AbilityLogoutTracker;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;

public class TrackItem {

    public ItemStack findTrackedItemInInventory(Player p, NamespacedKey key) {
        for (ItemStack item : p.getInventory().getContents()) {
           if (doesItemHaveKey(item,key)) {
               removeItemKey(item,key);
               return item;
           }
        }
        ItemStack cursorItem = p.getItemOnCursor();
        if (doesItemHaveKey(cursorItem,key)) {
            removeItemKey(cursorItem,key);
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
        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        if (container.has(key,PersistentDataType.STRING)) {
            return true;
        }
        return false;
    }

    public NamespacedKey getFreeRPGItemKey(ItemStack item,Player p) { //This assumes there is only one NamespacedKey per item (which should be a safe assumption)
        if (item == null) {
            return null;
        }
        if (item.getType() == Material.AIR) {
            return null;
        }
        AbilityLogoutTracker abilityLogoutTracker = new AbilityLogoutTracker(p);
        Map<String,NamespacedKey> allPlayerKeys = abilityLogoutTracker.getPlayerKeys();
        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        for (String name : allPlayerKeys.keySet()) {
            NamespacedKey key = allPlayerKeys.get(name);
            if (container.has(key,PersistentDataType.STRING)) {
                if (key.getKey().contains("frpg")) {
                    return key;
                }
            }
        }
        return null;
    }

    public void removeItemKey(ItemStack item, NamespacedKey key) {
        if (key == null) {
            return;
        }
        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        if (container.has(key,PersistentDataType.STRING)) {
            container.remove(key);
            item.setItemMeta(itemMeta);
        }
    }
}
