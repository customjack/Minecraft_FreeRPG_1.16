package mc.carlton.freerpg.gameTools;

import mc.carlton.freerpg.FreeRPG;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EntityPickedUpItemStorage {
    public static Map<LivingEntity, HashSet<Material>> entityPickedUpItemsMap = new ConcurrentHashMap<>();

    public void addEntity(LivingEntity entity) {
        entityPickedUpItemsMap.putIfAbsent(entity,new HashSet<>());
    }
    public void addItemKey(ItemStack itemStack,LivingEntity entity) {
        Material material = itemStack.getType();
        addEntity(entity);
        HashSet<Material> currentItemsPickedUp = entityPickedUpItemsMap.get(entity);
        currentItemsPickedUp.add(material);
        entityPickedUpItemsMap.put(entity,currentItemsPickedUp);
    }

    public boolean wasItemPickedUp(ItemStack itemStack, LivingEntity entity) {
        if (entityPickedUpItemsMap.containsKey(entity)) {
            if (entityPickedUpItemsMap.get(entity).contains(itemStack.getType())) {
                return true;
            }
        }
        return false;
    }
    public void removeEntity(LivingEntity entity) {
        if (entityPickedUpItemsMap.containsKey(entity)) {
            entityPickedUpItemsMap.remove(entity);
        }
    }
}
