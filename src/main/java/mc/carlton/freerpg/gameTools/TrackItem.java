package mc.carlton.freerpg.gameTools;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class TrackItem {

    public ItemStack findTrackedItemInInventory(Player p, NamespacedKey key) {
        for (ItemStack item : p.getInventory().getContents()) {
            if (item == null) {
                continue;
            }
            if (item.getType() == Material.AIR) {
                continue;
            }
            ItemMeta itemMeta = item.getItemMeta();
            PersistentDataContainer container = itemMeta.getPersistentDataContainer();
            if (container.has(key,PersistentDataType.STRING)) {
                container.remove(key);
                item.setItemMeta(itemMeta);
                return item;
            }
        }
        return null;
    }
}
