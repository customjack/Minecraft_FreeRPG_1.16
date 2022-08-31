package mc.carlton.freerpg.utils.globalVariables;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import mc.carlton.freerpg.utils.game.ExpFarmTracker;
import mc.carlton.freerpg.core.info.player.ChangeStats;
import mc.carlton.freerpg.core.info.server.MinecraftVersion;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;

public class EntityGroups {

  static List<EntityType> hostileMobs;
  static List<EntityType> breedingAnimals;
  static List<EntityType> animals;
  static List<EntityType> babyAnimals;
  static List<EntityType> breedingAnimalsFarming;
  static List<EntityType> thirstMobs;
  static List<EntityType> hookableEntities;
  private MinecraftVersion minecraftVersion = new MinecraftVersion();
  private double mcVersion = minecraftVersion.getMinecraftVersion_Double();

  public void initializeAllEntityGroups() {
    initializeHostileMobs();
    initializeTameableAnimals();
    initializeFarmingAnimals();
    initializeThirstMobs();
    initializeHookableMobs();
  }

  public void initializeHookableMobs() {
    EntityType[] hookableEntities0 = {EntityType.BLAZE, EntityType.GHAST, EntityType.ZOMBIE,
        EntityType.SPIDER,
        EntityType.CAVE_SPIDER, EntityType.PIG, EntityType.CREEPER, EntityType.WITCH,
        EntityType.CHICKEN,
        EntityType.SKELETON, EntityType.WITHER_SKELETON, EntityType.MAGMA_CUBE, EntityType.COW,
        EntityType.MUSHROOM_COW,
        EntityType.ENDERMAN, EntityType.SHEEP, EntityType.IRON_GOLEM, EntityType.SNOWMAN,
        EntityType.SHULKER};
    hookableEntities = new LinkedList<>(Arrays.asList(hookableEntities0));
    if (mcVersion >= 1.16) {
      hookableEntities.add(EntityType.ZOMBIFIED_PIGLIN);
    }
  }

  public void initializeHostileMobs() {
    EntityType[] hostileMobs0 = {EntityType.SPIDER, EntityType.CAVE_SPIDER, EntityType.ENDERMAN,
        EntityType.BLAZE, EntityType.CREEPER, EntityType.DROWNED, EntityType.ELDER_GUARDIAN,
        EntityType.ENDERMITE, EntityType.EVOKER, EntityType.GHAST, EntityType.GUARDIAN,
        EntityType.HUSK, EntityType.MAGMA_CUBE, EntityType.PHANTOM, EntityType.PILLAGER,
        EntityType.RAVAGER, EntityType.SHULKER, EntityType.SKELETON, EntityType.SLIME,
        EntityType.STRAY, EntityType.VEX, EntityType.VINDICATOR, EntityType.WITCH,
        EntityType.WITHER_SKELETON, EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER};
    hostileMobs = new LinkedList<>(Arrays.asList(hostileMobs0));
    if (mcVersion >= 1.16) {
      hostileMobs.add(EntityType.HOGLIN);
      hostileMobs.add(EntityType.PIGLIN);
      hostileMobs.add(EntityType.ZOMBIFIED_PIGLIN);
      hostileMobs.add(EntityType.ZOGLIN);
    }
  }

  public void initializeTameableAnimals() {
    EntityType[] breedingAnimals0 = {EntityType.HORSE, EntityType.WOLF, EntityType.CAT,
        EntityType.OCELOT, EntityType.PARROT};
    breedingAnimals = Arrays.asList(breedingAnimals0);
  }

  public void initializeFarmingAnimals() {
    EntityType[] animals0 = {EntityType.CHICKEN, EntityType.COW, EntityType.DONKEY, EntityType.FOX,
        EntityType.HORSE, EntityType.MUSHROOM_COW,
        EntityType.MULE, EntityType.PARROT, EntityType.PIG, EntityType.RABBIT, EntityType.SHEEP,
        EntityType.SQUID,
        EntityType.SKELETON_HORSE, EntityType.TURTLE, EntityType.GOAT};
    animals = Arrays.asList(animals0);
    EntityType[] babyAnimals0 = {EntityType.MUSHROOM_COW, EntityType.COW, EntityType.SHEEP,
        EntityType.PIG, EntityType.CHICKEN, EntityType.RABBIT,
        EntityType.WOLF, EntityType.CAT, EntityType.OCELOT, EntityType.LLAMA, EntityType.POLAR_BEAR,
        EntityType.HORSE, EntityType.DONKEY, EntityType.MULE, EntityType.SKELETON_HORSE,
        EntityType.TURTLE,
        EntityType.PANDA, EntityType.FOX, EntityType.BEE, EntityType.GOAT};
    babyAnimals = Arrays.asList(babyAnimals0);
    EntityType[] breedingAnimals0 = {EntityType.MUSHROOM_COW, EntityType.COW, EntityType.SHEEP,
        EntityType.PIG, EntityType.CHICKEN, EntityType.RABBIT,
        EntityType.TURTLE, EntityType.PANDA, EntityType.FOX, EntityType.BEE, EntityType.GOAT};
    breedingAnimalsFarming = Arrays.asList(breedingAnimals0);
  }

  public void initializeThirstMobs() {
    EntityType[] thirstMobs0 = {EntityType.DROWNED, EntityType.ELDER_GUARDIAN, EntityType.EVOKER,
        EntityType.GUARDIAN,
        EntityType.HUSK, EntityType.PILLAGER, EntityType.RAVAGER, EntityType.VINDICATOR,
        EntityType.WITCH, EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER};
    thirstMobs = new LinkedList<>(Arrays.asList(thirstMobs0));
    if (mcVersion >= 1.16) {
      thirstMobs.add(EntityType.HOGLIN);
      thirstMobs.add(EntityType.PIGLIN);
      thirstMobs.add(EntityType.ZOMBIFIED_PIGLIN);
      thirstMobs.add(EntityType.ZOGLIN);
    }
  }

  public void killEntity(Entity entity, String skillName, Map<String, Integer> expMap,
      ChangeStats increaseStats) {
    if (entity instanceof LivingEntity) {
      if (entity instanceof Mob) {
        Mob mob = (Mob) entity;
        EntityType type = mob.getType();
        int expReward = expMap.get("killAnythingElse");
        if (type.equals(EntityType.BAT)) {
          expReward = expMap.get("killBat");
        } else if (type.equals(EntityType.CAT)) {
          expReward = expMap.get("killCat");
        } else if (type.equals(EntityType.CHICKEN)) {
          expReward = expMap.get("killChicken");
        } else if (type.equals(EntityType.COD)) {
          expReward = expMap.get("killCod");
        } else if (type.equals(EntityType.COW)) {
          expReward = expMap.get("killCow");
        } else if (type.equals(EntityType.DONKEY)) {
          expReward = expMap.get("killDonkey");
        } else if (type.equals(EntityType.FOX)) {
          expReward = expMap.get("killFox");
        } else if (type.equals(EntityType.HORSE)) {
          expReward = expMap.get("killHorse");
        } else if (type.equals(EntityType.POLAR_BEAR)) {
          expReward = expMap.get("killPolarBear");
        } else if (type.equals(EntityType.MUSHROOM_COW)) {
          expReward = expMap.get("killMooshroom");
        } else if (type.equals(EntityType.MULE)) {
          expReward = expMap.get("killMule");
        } else if (type.equals(EntityType.OCELOT)) {
          expReward = expMap.get("killOcelot");
        } else if (type.equals(EntityType.PARROT)) {
          expReward = expMap.get("killParrot");
        } else if (type.equals(EntityType.PIG)) {
          expReward = expMap.get("killPig");
        } else if (type.equals(EntityType.RABBIT)) {
          expReward = expMap.get("killRabbit");
        } else if (type.equals(EntityType.SALMON)) {
          expReward = expMap.get("killSalmon");
        } else if (type.equals(EntityType.SHEEP)) {
          expReward = expMap.get("killSheep");
        } else if (type.equals(EntityType.SKELETON_HORSE)) {
          expReward = expMap.get("killSkeleton_Horse");
        } else if (type.equals(EntityType.SNOWMAN)) {
          expReward = expMap.get("killSnowman");
        } else if (type.equals(EntityType.SQUID)) {
          expReward = expMap.get("killSquid");
        } else if (type.equals(EntityType.TROPICAL_FISH)) {
          expReward = expMap.get("killTropical_Fish");
        } else if (type.equals(EntityType.TURTLE)) {
          expReward = expMap.get("killTurtle");
        } else if (type.equals(EntityType.VILLAGER)) {
          expReward = expMap.get("killVillager");
        } else if (type.equals(EntityType.WANDERING_TRADER)) {
          expReward = expMap.get("killWandering_Trader");
        } else if (type.equals(EntityType.BEE)) {
          expReward = expMap.get("killBee");
        } else if (type.equals(EntityType.CAVE_SPIDER)) {
          expReward = expMap.get("killCaveSpider");
        } else if (type.equals(EntityType.DOLPHIN)) {
          expReward = expMap.get("killDolphin");
        } else if (type.equals(EntityType.ENDERMAN)) {
          expReward = expMap.get("killEnderman");
        } else if (type.equals(EntityType.IRON_GOLEM)) {
          expReward = expMap.get("killIron_Golem");
        } else if (type.equals(EntityType.LLAMA)) {
          expReward = expMap.get("killLlama");
        } else if (type.equals(EntityType.PANDA)) {
          expReward = expMap.get("killPanda");
        } else if (type.equals(EntityType.PUFFERFISH)) {
          expReward = expMap.get("killPufferfish");
        } else if (type.equals(EntityType.SPIDER)) {
          expReward = expMap.get("killSpider");
        } else if (type.equals(EntityType.WOLF)) {
          expReward = expMap.get("killWolf");
        } else if (type.equals(EntityType.BLAZE)) {
          expReward = expMap.get("killBlaze");
        } else if (type.equals(EntityType.CREEPER)) {
          expReward = expMap.get("killCreeper");
        } else if (type.equals(EntityType.DROWNED)) {
          expReward = expMap.get("killDrowned");
        } else if (type.equals(EntityType.ELDER_GUARDIAN)) {
          expReward = expMap.get("killElder_Guardian");
        } else if (type.equals(EntityType.ENDERMITE)) {
          expReward = expMap.get("killEndermite");
        } else if (type.equals(EntityType.EVOKER)) {
          expReward = expMap.get("killEvoker");
        } else if (type.equals(EntityType.GHAST)) {
          expReward = expMap.get("killGhast");
        } else if (type.equals(EntityType.GUARDIAN)) {
          expReward = expMap.get("killGuardian");
        } else if (type.equals(EntityType.HUSK)) {
          expReward = expMap.get("killHusk");
        } else if (type.equals(EntityType.MAGMA_CUBE)) {
          expReward = expMap.get("killMagma_Cube");
        } else if (type.equals(EntityType.PHANTOM)) {
          expReward = expMap.get("killPhantom");
        } else if (type.equals(EntityType.PILLAGER)) {
          expReward = expMap.get("killPillager");
        } else if (type.equals(EntityType.RAVAGER)) {
          expReward = expMap.get("killRavager");
        } else if (type.equals(EntityType.SHULKER)) {
          expReward = expMap.get("killShulker");
        } else if (type.equals(EntityType.SILVERFISH)) {
          expReward = expMap.get("killSilverfish");
        } else if (type.equals(EntityType.SKELETON)) {
          expReward = expMap.get("killSkeleton");
        } else if (type.equals(EntityType.SLIME)) {
          expReward = expMap.get("killSlime");
        } else if (type.equals(EntityType.STRAY)) {
          expReward = expMap.get("killStray");
        } else if (type.equals(EntityType.VEX)) {
          expReward = expMap.get("killVex");
        } else if (type.equals(EntityType.VINDICATOR)) {
          expReward = expMap.get("killVindicator");
        } else if (type.equals(EntityType.WITCH)) {
          expReward = expMap.get("killWitch");
        } else if (type.equals(EntityType.WITHER_SKELETON)) {
          expReward = expMap.get("killWitherSkeleton");
        } else if (type.equals(EntityType.ZOMBIE)) {
          expReward = expMap.get("killZombie");
        } else if (type.equals(EntityType.ZOMBIE_VILLAGER)) {
          expReward = expMap.get("killZombie_Villager");
        } else if (type.equals(EntityType.ENDER_DRAGON)) {
          expReward = expMap.get("killEnder_Dragon");
        } else if (type.equals(EntityType.WITHER)) {
          expReward = expMap.get("killWither");
        } else if (type.equals(EntityType.ZOMBIE_HORSE)) {
          expReward = expMap.get("killZombie_Horse");
        } else if (type.equals(EntityType.ILLUSIONER)) {
          expReward = expMap.get("killIllusioner");
        } else if (type.equals(EntityType.GIANT)) {
          expReward = expMap.get("killGiant");
        }
        if (mcVersion >= 1.16) {
          if (type.equals(EntityType.PIGLIN)) {
            expReward = expMap.get("killPiglin");
          } else if (type.equals(EntityType.ZOGLIN)) {
            expReward = expMap.get("killZoglin");
          } else if (type.equals(EntityType.HOGLIN)) {
            expReward = expMap.get("killHoglin");
          } else if (type.equals(EntityType.ZOMBIFIED_PIGLIN)) {
            expReward = expMap.get("killZombie_Pigman");
          } else if (type.equals(EntityType.STRIDER)) {
            expReward = expMap.get("killStrider");
          }
        }
        if (mcVersion >= 1.17) {
          if (type.equals(EntityType.GLOW_SQUID)) {
            expReward = expMap.get("killGlow_Squid");
          } else if (type.equals(EntityType.GOAT)) {
            expReward = expMap.get("killGoat");
          }
        }
        ExpFarmTracker expFarmTracker = new ExpFarmTracker();
        double multiplier = expFarmTracker.getExpFarmAndSpawnerCombinedMultiplier(entity,
            skillName);
        increaseStats.changeEXP(skillName, (int) Math.round(expReward * multiplier));
      }
    }
  }

  public List<EntityType> getBreedingAnimals() {
    return breedingAnimals;
  }

  public List<EntityType> getHostileMobs() {
    return hostileMobs;
  }

  public List<EntityType> getAnimals() {
    return animals;
  }

  public List<EntityType> getBabyAnimals() {
    return babyAnimals;
  }

  public List<EntityType> getBreedingAnimalsFarming() {
    return breedingAnimalsFarming;
  }

  public List<EntityType> getThirstMobs() {
    return thirstMobs;
  }

  public List<EntityType> getHookableEntities() {
    return hookableEntities;
  }
}
