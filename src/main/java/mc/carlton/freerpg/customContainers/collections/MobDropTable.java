package mc.carlton.freerpg.customContainers.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import mc.carlton.freerpg.customContainers.CustomItem;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class MobDropTable {

  private Map<EntityType, DropTable> mobDropTable;

  public MobDropTable() {
    mobDropTable = new HashMap<>();
  }

  public void addMobDropTable(EntityType entityType, DropTable dropTable) {
    addMob(entityType); //Adds entity type if not there
    mobDropTable.put(entityType, dropTable);
  }

  private void addMob(EntityType entityType) {
    if (mobDropTable.containsKey(entityType)) {
      return;
    } else {
      mobDropTable.put(entityType, new DropTable());
    }
  }

  public void addDropToMob(EntityType entityType, CustomItem drop) {
    addMob(entityType);  //Adds entity type if not there
    mobDropTable.get(entityType).addDrop(drop);
  }

  public void addDropsToMobTable(EntityType entityType, Collection<CustomItem> drops) {
    addMob(entityType);  //Adds entity type if not there
    mobDropTable.get(entityType).addDrops(drops);
  }

  public void removeDropFromMob(EntityType entityType, CustomItem drop) {
    if (mobDropTable.containsKey(entityType)) {
      mobDropTable.get(entityType).removeDrop(drop);
      removeMobDropTableIfEmpty(entityType);
    }
  }

  public void removeDropsFromMob(EntityType entityType, Collection<CustomItem> drops) {
    if (mobDropTable.containsKey(entityType)) {
      mobDropTable.get(entityType).removeDrops(drops);
      removeMobDropTableIfEmpty(entityType);
    }
  }

  private void removeMobDropTableIfEmpty(EntityType entityType) {
    if (mobDropTable.get(entityType).isEmpty()) {
      mobDropTable.remove(entityType);
    }
  }

  public CustomItem getRolledCustomItem(EntityType entityType, long seed) {
    if (mobDropTable.containsKey(entityType)) {
      return mobDropTable.get(entityType).getRolledCustomItem(seed);
    }
    return null;
  }

  public ItemStack getRolledItem(EntityType entityType, long seed) {
    if (mobDropTable.containsKey(entityType)) {
      return mobDropTable.get(entityType).getRolledItemStack(seed);
    }
    return null;
  }

  public CustomItem getRolledCustomItem(EntityType entityType) {
    return getRolledCustomItem(entityType, new Random().nextLong());
  }

  public ItemStack getRolledItem(EntityType entityType) {
    return getRolledItem(entityType, new Random().nextLong());
  }
}
