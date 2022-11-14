package mc.carlton.freerpg.customContainers.collections;

import java.util.ArrayList;
import mc.carlton.freerpg.customContainers.CustomItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class OldCustomRecipe {

  private ArrayList<Material> recipe;
  private Material output;
  private int outputAmount;
  private Enchantment ench;
  private int enchantmentLevel;
  private PotionType potionType;
  private boolean isExtended;
  private boolean isUpgraded;
  private int XPcraftCost;
  private String YAML_ID;

  /*
   * I'm too lazy to make like 20 constructors for every case. It's easiest just to
   * Create a "blank" custom recipe and fill in what you need
   */
  public OldCustomRecipe() {
    this.YAML_ID = "";
  }

  public OldCustomRecipe(ArrayList<Material> recipe, Material output, int outputAmount) {
    setRecipe(recipe);
    setOutput(output);
    setOutputAmount(outputAmount);
    this.YAML_ID = "";
  }

  public OldCustomRecipe(ArrayList<Material> recipe, CustomItem customItem) {

  }


  public ArrayList<Material> getRecipe() {
    return new ArrayList<>(recipe);
  }

  public void setRecipe(ArrayList<Material> recipe) {
    this.recipe = recipe;
  }

  public boolean isExtended() {
    return isExtended;
  }

  public void setExtended(boolean extended) {
    isExtended = extended;
  }

  public boolean isUpgraded() {
    return isUpgraded;
  }

  public void setUpgraded(boolean upgraded) {
    isUpgraded = upgraded;
  }

  public Enchantment getEnchantment() {
    return ench;
  }

  public void setEnchantment(Enchantment enchantment) {
    this.ench = enchantment;
  }

  public int getEnchantmentLevel() {
    return enchantmentLevel;
  }

  public void setEnchantmentLevel(int enchantmentLevel) {
    this.enchantmentLevel = enchantmentLevel;
  }

  public int getOutputAmount() {
    return outputAmount;
  }

  public void setOutputAmount(int outputAmount) {
    this.outputAmount = outputAmount;
  }

  public Material getOutput() {
    return output;
  }

  public void setOutput(Material output) {
    this.output = output;
  }

  public PotionType getPotionType() {
    return potionType;
  }

  public void setPotionType(PotionType potionType) {
    this.potionType = potionType;
  }

  public int getXPcraftCost() {
    return XPcraftCost;
  }

  public void setXPcraftCost(int XPcraftCost) {
    this.XPcraftCost = XPcraftCost;
  }

  public String getYAML_ID() {
    return YAML_ID;
  }

  public void setYAML_ID(String YAML_ID) {
    this.YAML_ID = YAML_ID;
  }

  public boolean outputIsPotion() {
    if (potionType != null) {
      return true;
    }
    return false;
  }

  public boolean outputIsEnchanted() {
    if (ench != null) {
      return true;
    }
    return false;
  }

  public ItemStack getItemStack() {
    if (output == null) {
      throw (new IllegalArgumentException("Unexpected Output Material at " + YAML_ID));
    }
    ItemStack item = new ItemStack(output, outputAmount);
    if (outputIsEnchanted()) {
      if (output.equals(Material.ENCHANTED_BOOK)) {
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
        meta.addStoredEnchant(ench, enchantmentLevel, true);
        item.setItemMeta(meta);
      } else {
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(ench, enchantmentLevel, true);
        item.setItemMeta(meta);
      }
    } else if (outputIsPotion()) {
      PotionMeta meta = (PotionMeta) item.getItemMeta();
      meta.setBasePotionData(new PotionData(potionType, isExtended, isUpgraded));
      item.setItemMeta(meta);
    }
    return item;
  }
}
