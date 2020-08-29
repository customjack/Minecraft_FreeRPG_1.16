package mc.carlton.freerpg.globalVariables;

import org.bukkit.entity.EntityType;

import javax.swing.text.html.parser.Entity;
import java.util.Arrays;
import java.util.List;

public class EntityGroups {
    static List<EntityType> hostileMobs;
    static List<EntityType> breedingAnimals;
    static List<EntityType> animals;
    static List<EntityType> babyAnimals;
    static List<EntityType> breedingAnimalsFarming;
    static List<EntityType> thirstMobs;

    public void initializeAllEntityGroups() {
        initializeHostileMobs();
        initializeTameableAnimals();
        initializeFarmingAnimals();
        initializeThirstMobs();

    }

    public void initializeHostileMobs() {
        EntityType[] hostileMobs0 = {EntityType.SPIDER,EntityType.CAVE_SPIDER,EntityType.ENDERMAN,EntityType.ZOMBIFIED_PIGLIN,
                EntityType.BLAZE,EntityType.CREEPER,EntityType.DROWNED,EntityType.ELDER_GUARDIAN,
                EntityType.ENDERMITE,EntityType.EVOKER,EntityType.GHAST,EntityType.GUARDIAN,
                EntityType.HUSK,EntityType.MAGMA_CUBE,EntityType.PHANTOM,EntityType.PILLAGER,
                EntityType.RAVAGER,EntityType.SHULKER,EntityType.SKELETON,EntityType.SLIME,
                EntityType.STRAY,EntityType.VEX,EntityType.VINDICATOR,EntityType.WITCH,
                EntityType.WITHER_SKELETON,EntityType.ZOMBIE,EntityType.ZOMBIE_VILLAGER,
                EntityType.HOGLIN,EntityType.PIGLIN,EntityType.ZOMBIFIED_PIGLIN,EntityType.ZOGLIN};
         hostileMobs = Arrays.asList(hostileMobs0);
    }

    public void initializeTameableAnimals() {
        EntityType[] breedingAnimals0 = {EntityType.HORSE,EntityType.WOLF,EntityType.CAT,EntityType.OCELOT,EntityType.PARROT};
        breedingAnimals = Arrays.asList(breedingAnimals0);
    }

    public void initializeFarmingAnimals() {
        EntityType[] animals0 = {EntityType.CHICKEN,EntityType.COW,EntityType.DONKEY,EntityType.FOX,EntityType.HORSE,EntityType.MUSHROOM_COW,
                EntityType.MULE,EntityType.PARROT,EntityType.PIG,EntityType.RABBIT,EntityType.SHEEP,EntityType.SQUID,
                EntityType.SKELETON_HORSE,EntityType.TURTLE};
        animals = Arrays.asList(animals0);
        EntityType[] babyAnimals0 = {EntityType.MUSHROOM_COW,EntityType.COW,EntityType.SHEEP,EntityType.PIG,EntityType.CHICKEN,EntityType.RABBIT,
                EntityType.WOLF,EntityType.CAT,EntityType.OCELOT,EntityType.LLAMA,EntityType.POLAR_BEAR,
                EntityType.HORSE,EntityType.DONKEY,EntityType.MULE,EntityType.SKELETON_HORSE,EntityType.TURTLE,
                EntityType.PANDA,EntityType.FOX,EntityType.BEE};
        babyAnimals = Arrays.asList(babyAnimals0);
        EntityType[] breedingAnimals0 = {EntityType.MUSHROOM_COW,EntityType.COW,EntityType.SHEEP,EntityType.PIG,EntityType.CHICKEN,EntityType.RABBIT,
                EntityType.TURTLE, EntityType.PANDA,EntityType.FOX,EntityType.BEE};
        breedingAnimalsFarming = Arrays.asList(breedingAnimals0);
    }

    public void initializeThirstMobs() {
        EntityType[] thirstMobs0 = {EntityType.ZOMBIFIED_PIGLIN, EntityType.DROWNED,EntityType.ELDER_GUARDIAN, EntityType.EVOKER,EntityType.GUARDIAN,
                EntityType.HUSK,EntityType.PILLAGER, EntityType.RAVAGER, EntityType.VINDICATOR,EntityType.WITCH, EntityType.ZOMBIE,EntityType.ZOMBIE_VILLAGER,
                EntityType.PIGLIN,EntityType.HOGLIN,EntityType.ZOGLIN};
        thirstMobs = Arrays.asList(thirstMobs0);
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
}
