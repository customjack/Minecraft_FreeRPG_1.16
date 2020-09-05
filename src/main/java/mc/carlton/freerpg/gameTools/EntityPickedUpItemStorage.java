package mc.carlton.freerpg.gameTools;

import mc.carlton.freerpg.FreeRPG;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EntityPickedUpItemStorage {
    public static Map<LivingEntity, ArrayList<NamespacedKey>> entityPickedUpItemsMap = new ConcurrentHashMap<>();

    public void addEntity(LivingEntity entity) {
        entityPickedUpItemsMap.putIfAbsent(entity,new ArrayList<>());
    }
    public void addItemKey(ItemStack itemStack,LivingEntity entity) {
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        ItemMeta itemMeta = itemStack.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin,"frpg_"+ UUID.randomUUID().toString());
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING,"HeldItem");
        itemStack.setItemMeta(itemMeta);
        addEntity(entity);
        ArrayList<NamespacedKey> currentkeys = entityPickedUpItemsMap.get(entity);
        currentkeys.add(key);
        entityPickedUpItemsMap.put(entity,currentkeys);
    }
    public boolean wasItemPickedUp(ItemStack itemStack, LivingEntity entity) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (entityPickedUpItemsMap.containsKey(entity)) {
            for (NamespacedKey key : entityPickedUpItemsMap.get(entity)) {
                if (itemMeta.getPersistentDataContainer().has(key,PersistentDataType.STRING)) {
                    itemMeta.getPersistentDataContainer().remove(key);
                    itemStack.setItemMeta(itemMeta);
                    return true;
                }
            }
            return true;
        }
        else {
            return false;
        }
    }
    public void removeEntity(LivingEntity entity) {
        if (entityPickedUpItemsMap.containsKey(entity)) {
            entityPickedUpItemsMap.remove(entity);
        }
    }
}
