package mc.carlton.freerpg.gameTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.globalVariables.EntityGroups;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class EntityPickedUpItemStorage {

  public static Map<LivingEntity, HashSet<Material>> entityPickedUpItemsMap = new ConcurrentHashMap<>();

  public void addEntity(LivingEntity entity) {
    entityPickedUpItemsMap.putIfAbsent(entity, new HashSet<>());
  }

  public void addPickedUpItem(ItemStack itemStack, LivingEntity entity) {
    Material material = itemStack.getType();
    addEntity(entity);
    HashSet<Material> currentItemsPickedUp = entityPickedUpItemsMap.get(entity);
    currentItemsPickedUp.add(material);
    entityPickedUpItemsMap.put(entity, currentItemsPickedUp);
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

  public void addPickedUpItemFromDispenser(ItemStack item, Location location) {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    EntityGroups entityGroups = new EntityGroups();
    List<EntityType> hostileMobs = entityGroups.getHostileMobs();
    Map<LivingEntity, List<ItemStack>> entity_EquipmentMap = new HashMap<>();
    for (Entity entity : location.getWorld().getNearbyEntities(location, 1, 1, 1)) {
      if (hostileMobs.contains(entity.getType())) {
        LivingEntity hostileMob = (LivingEntity) entity;
        List<ItemStack> equipment = getEntityEquipment(hostileMob);
        entity_EquipmentMap.put(hostileMob, equipment);
      }
    }
    if (entity_EquipmentMap.isEmpty()) {
      return;
    }
    new BukkitRunnable() {
      @Override
      public void run() {
        for (LivingEntity entity : entity_EquipmentMap.keySet()) {
          List<ItemStack> equipment = entity_EquipmentMap.get(entity);
          List<ItemStack> newEquipment = getEntityEquipment(entity);
          for (int i = 0; i < equipment.size(); i++) {
            if (!equipment.get(i).equals(newEquipment.get(i))) {
              addPickedUpItem(item, entity);
              break;
            }
          }

        }
      }
    }.runTaskLater(plugin, 1);
  }

  public List<ItemStack> getEntityEquipment(LivingEntity entity) {
    EntityEquipment equipment = entity.getEquipment();
    List<ItemStack> equipmentItems = new ArrayList<>();
    equipmentItems.add(equipment.getItemInMainHand());
    equipmentItems.add(equipment.getItemInOffHand());
    equipmentItems.add(equipment.getHelmet());
    equipmentItems.add(equipment.getChestplate());
    equipmentItems.add(equipment.getLeggings());
    equipmentItems.add(equipment.getBoots());
    return equipmentItems;
  }
}
