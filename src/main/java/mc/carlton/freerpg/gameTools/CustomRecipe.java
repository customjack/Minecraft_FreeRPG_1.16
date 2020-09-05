package mc.carlton.freerpg.gameTools;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;

public class CustomRecipe {
    public ArrayList<Material> recipe;
    public Material output;
    public int outputAmount;
    public Enchantment ench;
    public int enchantmentLevel;
    public PotionType potionType;
    public boolean isExtended;
    public boolean isUpgraded;
    public int XPcraftCost;

    public CustomRecipe(){
        this.recipe = null;
        this.output = null;
        this.outputAmount = 0;
        this.ench = null;
        this.enchantmentLevel = 0;
        this.potionType = null;
        this.isExtended = false;
        this.isUpgraded = false;
        this.XPcraftCost = 0;
    }

    public ArrayList<Material> getRecipe() {
        return recipe;
    }

    public boolean isExtended() {
        return isExtended;
    }

    public boolean isUpgraded() {
        return isUpgraded;
    }

    public Enchantment getEnchantment() {
        return ench;
    }

    public int getEnchantmentLevel() {
        return enchantmentLevel;
    }

    public int getOutputAmount() {
        return outputAmount;
    }

    public Material getOutput() {
        return output;
    }

    public PotionType getPotionType() {
        return potionType;
    }

    public int getXPcraftCost() {
        return XPcraftCost;
    }

    public void setEnchantment(Enchantment enchantment) {
        this.ench = enchantment;
    }

    public void setEnchantmentLevel(int enchantmentLevel) {
        this.enchantmentLevel = enchantmentLevel;
    }

    public void setExtended(boolean extended) {
        isExtended = extended;
    }

    public void setOutput(Material output) {
        this.output = output;
    }

    public void setOutputAmount(int outputAmount) {
        this.outputAmount = outputAmount;
    }

    public void setPotionType(PotionType potionType) {
        this.potionType = potionType;
    }

    public void setRecipe(ArrayList<Material> recipe) {
        this.recipe = recipe;
    }

    public void setUpgraded(boolean upgraded) {
        isUpgraded = upgraded;
    }

    public void setXPcraftCost(int XPcraftCost) {
        this.XPcraftCost = XPcraftCost;
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
        ItemStack item = new ItemStack(output, outputAmount);
        if (outputIsEnchanted()) {
            if (output.equals(Material.ENCHANTED_BOOK)) {
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
                meta.addStoredEnchant(ench,enchantmentLevel,true);
                item.setItemMeta(meta);
            }
            else {
                ItemMeta meta =item.getItemMeta();
                meta.addEnchant(ench,enchantmentLevel,true);
                item.setItemMeta(meta);
            }
        }
        else if (outputIsPotion()) {
            PotionMeta meta = (PotionMeta) item.getItemMeta();
            meta.setBasePotionData(new PotionData(potionType,isExtended,isUpgraded));
            item.setItemMeta(meta);
        }
        return item;
    }
}
