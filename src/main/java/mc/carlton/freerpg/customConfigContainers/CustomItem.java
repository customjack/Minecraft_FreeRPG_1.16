package mc.carlton.freerpg.customConfigContainers;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class CustomItem extends CustomContainer {
    protected Material material;
    protected int amount = 1;
    protected double minDurabilityPortion = 1.00;
    protected double maxDurabilityPortion = 1.00;
    protected int minEnchantmentLevel = 0;
    protected int maxEnchantmentLevel = 0;
    protected boolean isTreasure = true;
    protected Map<Enchantment, Integer> enchantments = new HashMap<>();
    protected double weight = 1.0;
    protected double probability = -1.0;
    protected int experienceDrop = 0;
    protected String itemName;

    /**
     * Constructor for CustomItem
     * @param material the item type
     */
    public CustomItem(Material material) {
        this.material = material;
    }

    @Override
    public void setContainerInformation(Map<String, Object> containerInformation) {

    }

    /**
     *
     * @return true if item is potion
     */
    public boolean isPotion() {
        return (material.equals(Material.POTION));
    }

    /**
     * Setter for amount
     * @param amount quantity of item
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Setter for minimum durability portion
     * @param minDurabilityPortion minimum durability portion (relative to it's full durability) the item can be set to (<= 1)
     */
    public void setMinDurabilityPortion(double minDurabilityPortion) {
        this.minDurabilityPortion = minDurabilityPortion;
    }

    /**
     * Setter for maximum durability portion
     * @param maxDurabilityPortion maximum durability portion (relative to it's full durability) the item can be set to (<=1)
     */
    public void setMaxDurabilityPortion(double maxDurabilityPortion) {
        this.maxDurabilityPortion = maxDurabilityPortion;
    }

    /**
     * Setter for both minDurabilityPortion and maxDurabilityPortion
     * @param minDurabilityPortion minimum durability portion (relative to it's full durability) the item can be set to (<= 1)
     * @param maxDurabilityPortion maximum durability portion (relative to it's full durability) the item can be set to (<=1)
     */
    public void setDurabilityRange(double minDurabilityPortion, double maxDurabilityPortion) {
        setMinDurabilityPortion(minDurabilityPortion);
        setMaxDurabilityPortion(maxDurabilityPortion);
    }

    /**
     * Setter for maximum random enchantment level
     * @param maxEnchantmentLevel maximum enchantment level (can be greater than 30)
     */
    public void setMaxEnchantmentLevel(int maxEnchantmentLevel) {
        this.maxEnchantmentLevel = maxEnchantmentLevel;
    }

    /**
     * Setter for minimum random enchantment level
     * @param minEnchantmentLevel minimum enchantment level
     */
    public void setMinEnchantmentLevel(int minEnchantmentLevel) {
        this.minEnchantmentLevel = minEnchantmentLevel;
    }

    /**
     * Setter for both minimum and maximum random enchantment level
     * @param minEnchantmentLevel minimum enchantment level
     * @param maxEnchantmentLevel maximum enchantment level (can be greater than 30)
     */
    public void setEnchantmentLevelRange(int minEnchantmentLevel, int maxEnchantmentLevel) {
        setMinEnchantmentLevel(minEnchantmentLevel);
        setMaxEnchantmentLevel(maxEnchantmentLevel);
    }

    /**
     * Setter for the map of static enchantments on the item
     * @param enchantments Map of enchantments with corresponding level
     */
    public void setEnchantments(Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
    }

    /**
     * Adds an enchantment to the map of static enchantments on the item
     * @param enchantment
     * @param level
     */
    public void addEnchantment(Enchantment enchantment, int level) {
        this.enchantments.put(enchantment,level);
    }

    /**
     * Add multiple enchantments to the map of static enchantments on the item
     * @param enchantments Map of enchantments with level
     */
    public void addEnchantments(Map<Enchantment, Integer> enchantments) {
        for (Enchantment enchantment : enchantments.keySet()) {
            addEnchantment(enchantment,enchantments.get(enchantment));
        }
    }

    /**
     * Sets the material for an item
     * @param material Item type
     */
    public void setMaterial(Material material) {
        this.material = material;
    }

    /**
     * Getter for material
     * @return material
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Amount of FreeRPG experience that is dropped when a player recieves this item
     * @param experienceDrop integer experience amount
     */
    public void setExperienceDrop(int experienceDrop) {
        this.experienceDrop = experienceDrop;
    }

    /**
     * Whether or not the random enchantment on this item is a treasure enchantment or not
     * @param isTreasure true if the item is a treasure, false otherwise
     */
    public void setTreasure(boolean isTreasure) {
        this.isTreasure = isTreasure;
    }

    /**
     * Sets the static probability this item drops
     * @param probability Static drop rate for this item (should be <=1)
     */
    public void setProbability(double probability) {
        this.probability = probability;
    }

    /**
     * Sets the drop probability weight for this item. This value is compared to other weights of items in a drop table
     * @param weight Weight value (higher --> more likely)
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        String stringValue = "";
        stringValue += "[";
        stringValue += "Material: " + this.material.toString() + ", ";
        stringValue += "Amount: " + this.amount + ", ";
        stringValue += "Durability: (" + this.minDurabilityPortion + ", " + this.maxDurabilityPortion + "), ";
        stringValue += "Random Enchantment Range: (" + this.minEnchantmentLevel + ", " + this.maxEnchantmentLevel + ", treasure=" + isTreasure + "), ";
        stringValue += "Static Enchantments: {";
        int counter = 0;
        for (Enchantment enchantment : enchantments.keySet()) {
            stringValue += enchantment.getKey().getKey() + "-" + enchantments.get(enchantment).toString();
            counter += 1;
            if (counter < enchantments.size()) {
                stringValue += ", ";
            }
        }
        stringValue += "}, ";
        if (probability != -1) {
            stringValue += "Probability: " + probability + ", ";
        } else {
            stringValue += "Weight: " + weight + ", ";
        }
        stringValue += "Exp: " + experienceDrop;
        stringValue += "]";
        return stringValue;
    }

}
