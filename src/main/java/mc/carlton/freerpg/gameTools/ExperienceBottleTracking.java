package mc.carlton.freerpg.gameTools;

import mc.carlton.freerpg.FreeRPG;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExperienceBottleTracking {
    static Map<Location,Boolean> expBottleBreakLocation = new ConcurrentHashMap<>();
    static Map<ExperienceOrb,Boolean> trackedEXPOrbs = new ConcurrentHashMap<>();

    public void addLocation(Location location){
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Entity entity : location.getWorld().getNearbyEntities(location,1,1,1)) {
                    if (entity.getType().equals(EntityType.EXPERIENCE_ORB)) {
                        trackedEXPOrbs.putIfAbsent((ExperienceOrb) entity,true);
                    }
                }
            }
        }.runTaskLater(plugin, 1);
    }


    public boolean fromEnchantingBottle(){ //Bug: False true for first exp picked up after throwing 2+(?) enchanting bottles and not collecting exp
        boolean fromBottle = false;
        for (ExperienceOrb experienceOrb : trackedEXPOrbs.keySet()) {
            if (experienceOrb.isDead()) {
                trackedEXPOrbs.remove(experienceOrb);
                fromBottle = true;
            }
        }
        return fromBottle;
    }

}
