package mc.carlton.freerpg.customContainers.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import mc.carlton.freerpg.customContainers.CustomItem;
import org.bukkit.inventory.ItemStack;


public class DropTable {

  private ArrayList<CustomItem> weightedProbabilityDrops = new ArrayList<>();
  private ArrayList<CustomItem> staticProbabilityDrops = new ArrayList<>();

  /**
   * Constructor
   *
   * @param drops pre-made drop table
   */
  public DropTable(List<CustomItem> drops) {
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
   *
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
   *
   * @param drops a collection of CustomItems
   */
  public void addDrops(Collection<CustomItem> drops) {
    for (CustomItem drop : drops) {
      addDrop(drop);
    }
  }

  /**
   * Removes a drop from the drop table
   *
   * @param drop a CustomItem
   */
  public void removeDrop(CustomItem drop) {
    if (drop.isStaticProbability()) {
      staticProbabilityDrops.remove(drop);
    } else {
      weightedProbabilityDrops.remove(drop);
    }
  }

  /**
   * Removes multiple drops from the drop table
   *
   * @param drops a collection of CustomItems
   */
  public void removeDrops(Collection<CustomItem> drops) {
    for (CustomItem drop : drops) {
      removeDrop(drop);
    }
  }

  /**
   * Rolls for a custom item in the drop table
   *
   * @return the random CustomItem
   */
  public CustomItem getRolledCustomItem() {
    return getRolledCustomItem(
        new Random().nextLong()); //Uses another random class to generate a random seed
  }

  /**
   * Rolls for a custom item in the drop table using a set seed
   *
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
    double randomWeightValue = random.nextDouble() * totalWeight;
    double weightSum = 0.0;
    for (CustomItem drop : weightedProbabilityDrops) {
      weightSum += drop.getWeight();
      if (randomWeightValue < weightSum) {
        return drop;
      }
    }
    return null; //The drop tables are empty (or something went wrong)
  }

  public boolean isEmpty() {
    return (staticProbabilityDrops.isEmpty() && weightedProbabilityDrops.isEmpty());
  }

  /**
   * Rolls for a custom item in the drop table, and returns the ItemStack representation
   *
   * @return the ItemStack representation of the random CustomItem
   */
  public ItemStack getRolledItemStack() {
    return getRolledCustomItem().getItemStack();
  }

  /**
   * Rolls for a custom item in the drop table, and returns the ItemStack representation using a set
   * seed
   *
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

  private double getTotalStaticProbability() {
    double totalProb = 0.0;
    for (CustomItem drop : staticProbabilityDrops) {
      totalProb += drop.getProbability();
    }
    return totalProb;
  }

  @Override
  public String toString() {
    int numberOfItems = weightedProbabilityDrops.size() + staticProbabilityDrops.size();
    return "{Number of Items=" + numberOfItems +
        ", Total Static Probability=" + getTotalStaticProbability() +
        ", Total Weight=" + getTotalWeight() +
        "}";
  }
}
