package mc.carlton.freerpg.miscEvents;

import mc.carlton.freerpg.perksAndAbilities.Digging;
import mc.carlton.freerpg.perksAndAbilities.Mining;
import mc.carlton.freerpg.perksAndAbilities.Swordsmanship;
import mc.carlton.freerpg.playerInfo.AbilityLogoutTracker;
import mc.carlton.freerpg.playerInfo.AbilityTracker;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class PlayerDropItem implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    void onItemDrop(PlayerDropItemEvent e){
        if (e.isCancelled()) {
            return;
        }
        Player p = (Player) e.getPlayer();
        AbilityTracker abilities = new AbilityTracker(p);
        Integer[] pAbilities = abilities.getPlayerAbilities();
        if (pAbilities[0] != -1) {
            AbilityLogoutTracker logoutTracker = new AbilityLogoutTracker(p);
            NamespacedKey key = logoutTracker.getPlayerKeys().get("digging");
            int taskID_digging = logoutTracker.getPlayerTasks().get("digging");
            if (e.getItemDrop().getItemStack().getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                ItemStack abilityItem = e.getItemDrop().getItemStack();
                Digging diggingClass = new Digging(p);
                diggingClass.preventLogoutTheft(taskID_digging,abilityItem,key,false);
            }
        }
        if (pAbilities[2] != -1) {
            AbilityLogoutTracker logoutTracker = new AbilityLogoutTracker(p);
            NamespacedKey key = logoutTracker.getPlayerKeys().get("mining");
            int taskID_mining = logoutTracker.getPlayerTasks().get("mining");
            if (e.getItemDrop().getItemStack().getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                ItemStack abilityItem = e.getItemDrop().getItemStack();
                Mining miningClass = new Mining(p);
                miningClass.preventLogoutTheft(taskID_mining,abilityItem,key,false);
            }
        }

        if (pAbilities[7] != -1) {
            AbilityLogoutTracker logoutTracker = new AbilityLogoutTracker(p);
            NamespacedKey key = logoutTracker.getPlayerKeys().get("swordsmanship");
            int taskID_swordsmanship = logoutTracker.getPlayerTasks().get("swordsmanship");
            if (e.getItemDrop().getItemStack().getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                ItemStack abilityItem = e.getItemDrop().getItemStack();
                Swordsmanship swordsmanshipClass = new Swordsmanship(p);
                swordsmanshipClass.preventLogoutTheft(taskID_swordsmanship,abilityItem,key,false);
            }
        }

    }
}
