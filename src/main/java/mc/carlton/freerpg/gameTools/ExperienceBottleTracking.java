package mc.carlton.freerpg.gameTools;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import mc.carlton.freerpg.FreeRPG;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ExperienceBottleTracking {

  static Map<Location, Object> trackedEXPOrbs = new ConcurrentHashMap<>();

  public void addLocation(Location location) {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    removeDeadEntities();
    trackedEXPOrbs.putIfAbsent(location, false);
    new BukkitRunnable() {
      @Override
      public void run() {
        for (Entity entity : location.getWorld().getNearbyEntities(location, 1, 1, 1)) {
          if (entity.getType().equals(EntityType.EXPERIENCE_ORB)) {
            trackedEXPOrbs.put(location, entity);
          }
        }
      }
    }.runTaskLater(plugin, 1);
  }


  public boolean fromEnchantingBottle(
      Entity entity) { //Bug: False true for first exp picked up after throwing 2+(?) enchanting bottles and not collecting exp
    Location orbLoc = entity.getLocation();
    for (Location originLocation : trackedEXPOrbs.keySet()) {
      if (originLocation.getWorld().equals(orbLoc.getWorld())) {
        if (originLocation.distance(orbLoc) <= 0.5) {
          removeCloseOriginLocations(originLocation);
          removeDeadEntities();
          return true;
        } else if (!(trackedEXPOrbs.get(originLocation) instanceof Boolean)) {
          if (entity.equals(trackedEXPOrbs.get(originLocation))) {
            removeCloseOriginLocations(originLocation);
            removeDeadEntities();
            return true;
          }
        }
      }
    }
    removeDeadEntities();
    return false;
        /*
        boolean fromBottle = false;
        for (ExperienceOrb experienceOrb : trackedEXPOrbs.keySet()) {
            if (experienceOrb.isDead()) {
                trackedEXPOrbs.remove(experienceOrb);
                fromBottle = true;
            }
        }
        return fromBottle;

         */
  }

  public void removeCloseOriginLocations(Location location) {
    for (Location originLocation : trackedEXPOrbs.keySet()) {
      if (originLocation.getWorld().equals(location.getWorld())) {
        if (location.distance(originLocation) <= 1) {
          trackedEXPOrbs.remove(originLocation);
        }
      }
    }
  }

  public void removeDeadEntities() {
    for (Location originLocation : trackedEXPOrbs.keySet()) {
      Object trackedObject = trackedEXPOrbs.get(originLocation);
      if (trackedObject instanceof Entity) {
        if (((ExperienceOrb) trackedObject).isDead()) {
          trackedEXPOrbs.remove(originLocation);
        }
      }
    }
  }

}
