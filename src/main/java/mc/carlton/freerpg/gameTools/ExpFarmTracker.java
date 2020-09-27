package mc.carlton.freerpg.gameTools;

import com.google.gson.internal.$Gson$Preconditions;
import mc.carlton.freerpg.serverInfo.ConfigLoad;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Golem;
import org.bukkit.entity.Monster;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

public class ExpFarmTracker {
    static HashSet<Location> expFarmLocations = new HashSet<>();

    public double getExpFarmAndSpawnerCombinedMultiplier(Entity entity,String skillName) {
        ConfigLoad configLoad = new ConfigLoad();
        Map<String,Double> spawnerEXPMultipliers = configLoad.getSpawnerEXPMultipliers();
        Map<String,Double> mobFarmEXPMultipliers = configLoad.getMobFarmEXPMultipliers();
        double multiplier = 1.0;
        if (spawnerEXPMultipliers.containsKey(skillName)) {
            if (entity.hasMetadata("frpgSpawnerMob")) {
                multiplier *= spawnerEXPMultipliers.get(skillName);
            }
        }
        if (mobFarmEXPMultipliers.containsKey(skillName)) {
            if (isExpFarm(entity)) {
                multiplier *= mobFarmEXPMultipliers.get(skillName);
            }
        }
        return multiplier;
    }


    public boolean isExpFarm(Entity entity) {
        Location location = entity.getLocation();
        World world = location.getWorld();
        for (Location expFarmLocation : expFarmLocations) {
            if (world.equals(expFarmLocation.getWorld())) {
                if (expFarmLocation.distance(location) < 3.0) {
                    return true;
                }
            }
        }
        return checkSurroundingMobs(world,location,entity);
    }

    public boolean checkSurroundingMobs(World world, Location location, Entity entity) {
        if (!(entity instanceof Monster) && !(entity instanceof Golem)) {
            return false;
        }
        Collection<Entity> nearbyEntities = world.getNearbyEntities(location, 1, 1, 1);
        double HP = ((Attributable)entity).getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        int numberOfLowHPEntitiesOfSameType = 0;
        for (Entity surroundingEntity : nearbyEntities) {
            if (surroundingEntity.getType().equals(entity.getType())) {
                if (((Creature)surroundingEntity).getHealth() < HP*0.2) {
                    numberOfLowHPEntitiesOfSameType += 1;
                }
            }
            if (numberOfLowHPEntitiesOfSameType >= 5) {
                break;
            }
        }
        if (numberOfLowHPEntitiesOfSameType >= 5) {
            expFarmLocations.add(location);
            return true;
        }
        return false;
    }
}
