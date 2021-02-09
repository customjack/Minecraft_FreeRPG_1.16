package mc.carlton.freerpg.customContainers.collections;

import mc.carlton.freerpg.customContainers.CustomItem;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;


public class DropTable {
    private ArrayList<CustomItem> weightedProbabilityDrops = new ArrayList<>();
    private ArrayList<CustomItem> staticProbabilityDrops = new ArrayList<>();

    /**
     * Constructor
     * @param drops pre-made drop table
     */
    public DropTable(ArrayList<CustomItem> drops) {
        addDrops(drops);
    }

    /**
     * Constructor (empty drop table entries)
     */
    public DropTable() {
        this(new ArrayList<>());
    }

    /**
     * Adds a single drop to the dropTable and categorizes it
     * @param drop a CustomItem
     */
    public void addDrop(CustomItem drop) {
        if (drop.isStaticProbability()) {
            staticProbabilityDrops.add(drop);
        } else {
            weightedProbabilityDrops.add(drop);
        }
    }

    /**
     * Adds multiple drops to the drop table and categorizes them
     * @param drops a collection of CustomItems
     */
    public void addDrops(Collection<CustomItem> drops) {
        for (CustomItem drop : drops) {
            addDrop(drop);
        }
    }

    /**
     * Rolls for a custom item in the drop table
     * @return the random CustomItem
     */
    public CustomItem getRolledCustomItem() {
        return getRolledCustomItem(new Random().nextLong()); //Uses another random class to generate a random seed
    }

    /**
     * Rolls for a custom item in the drop table using a set seed
     * @param seed an initial seed used to construct Random()
     * @return the random CustomItem
     */
    public CustomItem getRolledCustomItem(long seed) {
        Random random = new Random(seed);
        double initialRoll = random.nextDouble();
        double staticProbabilitySum = 0.0;
        for (CustomItem drop : staticProbabilityDrops) {
            staticProbabilitySum += drop.getProbability();
            if (initialRoll < staticProbabilitySum) {
                return drop;
            }
        }
        double totalWeight = getTotalWeight();
        double randomWeightValue = random.nextDouble()*totalWeight;
        double weightSum = 0.0;
        for (CustomItem drop : weightedProbabilityDrops) {
            weightSum += drop.getWeight();
            if (randomWeightValue < weightSum) {
                return drop;
            }
        }
        return null; //The drop tables are empty (or something went wrong)
    }

    /**
     * Rolls for a custom item in the drop table, and returns the ItemStack representation
     * @return the ItemStack representation of the random CustomItem
     */
    public ItemStack getRolledItemStack() {
        return getRolledCustomItem().getItemStack();
    }

    /**
     * Rolls for a custom item in the drop table, and returns the ItemStack representation using a set seed
     * @param seed an initial seed used to construct Random()
     * @return the ItemStack representation of the random CustomItem
     */
    public ItemStack getRolledItemStack(long seed) {
        return getRolledCustomItem(seed).getItemStack();
    }

    private double getTotalWeight() {
        double totalWeight = 0.0;
        for (CustomItem drop : weightedProbabilityDrops) {
            totalWeight += drop.getWeight();
        }
        return totalWeight;
    }

}
