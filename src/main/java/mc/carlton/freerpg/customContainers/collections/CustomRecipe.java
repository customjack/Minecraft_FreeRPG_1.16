package mc.carlton.freerpg.customContainers.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import mc.carlton.freerpg.customContainers.CustomRecipeCraftingGrid;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class CustomRecipe {

  private CustomRecipeCraftingGrid initialRecipe;
  private HashSet<CustomRecipeCraftingGrid> recipeVariants = new HashSet<>();
  private ItemStack outputItem;
  private int xpCost;
  private String keyStringBase;

  public CustomRecipe(CustomRecipeCraftingGrid initialRecipe, ItemStack outputItem, int xpCost,
      String keyBase) {
    this.initialRecipe = initialRecipe;
    recipeVariants.add(initialRecipe);
    this.outputItem = outputItem;
    this.xpCost = xpCost;
    this.keyStringBase = keyBase;
  }

  public CustomRecipe(CustomRecipeCraftingGrid initialRecipe, ItemStack outputItem,
      String keyBase) {
    this(initialRecipe, outputItem, 0, keyBase);
  }

  public CustomRecipe(ArrayList<Material> recipe, ItemStack outputItem, String keyBase) {
    this(new CustomRecipeCraftingGrid(recipe), outputItem, keyBase);
  }

  public CustomRecipe(ArrayList<Material> recipe, ItemStack outputItem, int xpCost,
      String keyBase) {
    this(new CustomRecipeCraftingGrid(recipe), outputItem, xpCost, keyBase);
  }

  public ItemStack getOutputCustomItem() {
    return outputItem;
  }

  public Material getOutputMaterial() {
    return outputItem.getType();
  }

  public int getXpCost() {
    return xpCost;
  }

  public void setXpCost(int xpCost) {
    this.xpCost = xpCost;
  }

  public String getKeyStringBase() {
    return keyStringBase;
  }

  public void setKeyStringBase(String keyStringBase) {
    this.keyStringBase = keyStringBase;
  }

  public void setOutputItem(ItemStack outputItem) {
    this.outputItem = outputItem;
  }

  public void addRecipeVariant(CustomRecipeCraftingGrid recipeVariant) {
    this.recipeVariants.add(recipeVariant);
  }

  public void addRecipeVariants(Collection<CustomRecipeCraftingGrid> recipeVariants) {
    for (CustomRecipeCraftingGrid recipeVariant : recipeVariants) {
      addRecipeVariant(recipeVariant);
    }
  }

  public void addTranslatedVariants() {
    addRecipeVariants(getTranslatedVariants());
  }

  public void registerRecipeAndVariants() {
    int counter = 0;
    for (CustomRecipeCraftingGrid recipeVariant : recipeVariants) {
      recipeVariant.registerRecipe(this.outputItem, keyStringBase + "_" + counter);
      counter += 1;
    }
  }

  public boolean isRecipeMatch(Recipe recipe) {
    for (CustomRecipeCraftingGrid recipeVariant : recipeVariants) {
      if (recipeVariant.doesBukkitRecipeMatch(recipe)) {
        return true;
      }
    }
    return false;
  }

  private HashSet<CustomRecipeCraftingGrid> getTranslatedVariants() {
    if (!initialRecipe.getRecipe().isEmpty()) {
      return getTranslatedVariants(initialRecipe);
    }
    return null;
  }

  private HashSet<CustomRecipeCraftingGrid> getTranslatedVariants(
      CustomRecipeCraftingGrid initialRecipe) {
    return getTranslatedVariants(initialRecipe.getRecipe());
  }

  private HashSet<CustomRecipeCraftingGrid> getTranslatedVariants(
      ArrayList<Material> initialRecipe) {
    HashSet<CustomRecipeCraftingGrid> translatedVariants = new HashSet<>();
    ArrayList<Integer> indices = getOccupiedIndicesInRecipe(initialRecipe);

    if (indices.size()
        >= 7) { //If there are 7 unique items in the grid, there is never a possible shift
      translatedVariants.add(new CustomRecipeCraftingGrid(initialRecipe));
      return translatedVariants;
    }

    for (int i = -2; i <= 2; i++) { //Up and down shifting
      for (int j = -2; j <= 2; j++) { //Left and right shifting
        //create shifted indices
        boolean possibleFormat = true;
        ArrayList<Integer> shiftedIndices = new ArrayList<>();
        shiftedIndicesLoop:
        for (int index : indices) {
          int newIndex = getShiftedCraftingIndex(index, i, j);
          if (newIndex == -1) {
            possibleFormat = false;
            break shiftedIndicesLoop;
          } else {
            shiftedIndices.add(newIndex);
          }
        }

        //If all shifted indices are possible, add it to the list of possible recipes
        if (possibleFormat) {
          ArrayList<Material> possibleRecipe = getTransformedRecipe(initialRecipe, shiftedIndices);
          translatedVariants.add(new CustomRecipeCraftingGrid(possibleRecipe));
        }
      }
    }
    return translatedVariants;
  }

  private int getShiftedCraftingIndex(int originalIndex, int vertShift, int horzShift) {
    //Convert original index to row/column
    int row = Math.floorDiv(originalIndex, 3);
    int column = originalIndex % 3;
    //Add the shift
    int newRow = row + vertShift;
    int newColumn = column + horzShift;
    //Determine if the shift yeilds a new recipe still inside the crafting grid
    if (newRow < 0 || newRow > 2) {
      return -1;
    }
    if (newColumn < 0 || newColumn > 2) {
      return -1;
    }
    return newRow * 3 + newColumn;

  }

  private ArrayList<Integer> getOccupiedIndicesInRecipe(ArrayList<Material> recipe) {
    ArrayList<Integer> indices = new ArrayList<>();
    for (int i = 0; i < recipe.size(); i++) {
      if (!recipe.get(i).equals(Material.AIR) && !recipe.get(i).equals(Material.CAVE_AIR)
          && !recipe.get(i).equals(Material.VOID_AIR) && recipe.get(i) != null) {
        indices.add(i);
      }
    }
    return indices;
  }

  private ArrayList<Material> getTransformedRecipe(ArrayList<Material> initialRecipe,
      ArrayList<Integer> newIndices) {
    ArrayList<Integer> indices = getOccupiedIndicesInRecipe(initialRecipe);
    Map<Integer, Integer> transformationMap = new HashMap<>();
    for (int i = 0; i < indices.size(); i++) {
      transformationMap.put(indices.get(i), newIndices.get(i));
    }
    ArrayList<Material> newRecipe = new ArrayList<>();
    for (int i = 0; i < 9; i++) {
      newRecipe.add(Material.AIR);
    }
    for (int index : transformationMap.keySet()) {
      int newIndex = transformationMap.get(index);
      newRecipe.set(newIndex, initialRecipe.get(index));
    }
    return newRecipe;
  }

  @Override
  public String toString() {
    return "CustomRecipe{" +
        "initialRecipe=" + initialRecipe +
        ", Number of Variants=" + recipeVariants.size() +
        ", outputItem=" + outputItem +
        ", xpCost=" + xpCost +
        ", keyStringBase='" + keyStringBase + '\'' +
        '}';
  }
}
