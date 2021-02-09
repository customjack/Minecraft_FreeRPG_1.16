package mc.carlton.freerpg.customContainers.collections;

import mc.carlton.freerpg.customContainers.CustomItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class CustomRecipe {
    ArrayList<Material> recipe = new ArrayList<>();
    CustomItem outputItem;
    int xpCost;

    public CustomRecipe(ArrayList<Material> recipe, CustomItem outputItem, int xpCost) {
        this.recipe = recipe;
        this.outputItem = outputItem;
        this.xpCost = xpCost;
    }

    public CustomRecipe(ArrayList<Material> recipe, CustomItem outputItem) {
        this(recipe,outputItem,0);
    }

    public ArrayList<Material> getRecipe() {
        return recipe;
    }

    public CustomItem getOutputCustomItem() {
        return outputItem;
    }

    public Material getOutputMaterial() {
        return outputItem.getMaterial();
    }

    public ItemStack getOutputItemStack() {
        return getOutputItemStack();
    }

    public int getXpCost() {
        return xpCost;
    }

    public void setRecipe(ArrayList<Material> recipe) {
        this.recipe = recipe;
    }

    public void setOutputItem(CustomItem outputItem) {
        this.outputItem = outputItem;
    }

    public void setXpCost(int xpCost) {
        this.xpCost = xpCost;
    }

    /* TODO:
    1) Get translational recipe variants
    2) Add recipe to be recognized by server
     */
}
