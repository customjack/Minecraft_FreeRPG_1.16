package mc.carlton.freerpg.customContainers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import mc.carlton.freerpg.FreeRPG;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

public class CustomRecipeCraftingGrid {

  private ArrayList<Material> recipe = new ArrayList<>();
  private NamespacedKey key;
  private boolean recipeRegistered;
  private String keyString;

  public CustomRecipeCraftingGrid(ArrayList<Material> recipeCraftingGrid) {
    this.recipe = recipeCraftingGrid;
  }

  public ArrayList<Material> getRecipe() {
    return recipe;
  }

  public void setRecipe(ArrayList<Material> recipe) {
    this.recipe = recipe;
  }

  public Recipe getBukkitRecipe() {
    if (key != null) {
      return Bukkit.getRecipe(key);
    }
    return null;
  }

  public boolean doesBukkitRecipeMatch(Recipe recipe) {
    if (recipe.equals(getBukkitRecipe())) {
      return true;
    } else {
      return false;
    }
  }

  public NamespacedKey getKey() {
    return key;
  }

  public void setKey(NamespacedKey key) {
    this.keyString = key.getKey();
    this.key = key;
  }

  public void setKey(String keyString) {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    this.keyString = keyString;
    this.key = new NamespacedKey(plugin, keyString);
  }

  public String getKeyString() {
    return keyString;
  }

  private Map<Integer, String> getItemToCharacterMap(ArrayList<Material> recipe) {
    String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I"};
    Map<Integer, String> recipeLettersMap = new HashMap<>();
    for (int i = 0; i < 9; i++) {
      if (!recipe.get(i).equals(Material.AIR) && !recipe.get(i).equals(Material.CAVE_AIR)
          && !recipe.get(i).equals(Material.VOID_AIR) && recipe.get(i) != null) {
        recipeLettersMap.put(i, letters[i]);
      } else {
        recipeLettersMap.put(i, " ");
      }
    }
    return recipeLettersMap;
  }

  private ArrayList<String> getShapedRecipeLines(Map<Integer, String> recipeLettersMap) {
    String line1 = recipeLettersMap.get(0) + recipeLettersMap.get(1) + recipeLettersMap.get(2);
    String line2 = recipeLettersMap.get(3) + recipeLettersMap.get(4) + recipeLettersMap.get(5);
    String line3 = recipeLettersMap.get(6) + recipeLettersMap.get(7) + recipeLettersMap.get(8);
    ArrayList<String> craftingLines = new ArrayList<>();
    craftingLines.add(line1);
    craftingLines.add(line2);
    craftingLines.add(line3);
    return craftingLines;
  }

  public void unRegisterRecipe() {
    if (key == null) {
      return;
    }
    if (recipeRegistered) {
      Bukkit.removeRecipe(key);
      recipeRegistered = false;
    }
  }

  public void registerRecipe(ItemStack itemStack, String id) {
    if (recipe.isEmpty()) {
      return; //No recipe to register
    }
    unRegisterRecipe(); //Unregisters recipe (if an old recipe was registered)
    setKey(id);
    registerRecipeHelper(itemStack);
  }

  public void registerRecipe(ItemStack itemStack) {
    if (recipe.isEmpty()) {
      return; //No recipe to register
    }
    if (key == null) {
      return;
    }
    unRegisterRecipe(); //Unregisters recipe (if it is registered already)
    registerRecipeHelper(itemStack);
  }

  private void registerRecipeHelper(ItemStack itemStack) {
    ShapedRecipe shapedRecipe = new ShapedRecipe(key, itemStack);
    Map<Integer, String> recipeLettersMap = getItemToCharacterMap(recipe);
    ArrayList<String> shapeLines = getShapedRecipeLines(recipeLettersMap);
    shapedRecipe.shape(shapeLines.get(0), shapeLines.get(1), shapeLines.get(2));
    for (int index : recipeLettersMap.keySet()) {
      String character = recipeLettersMap.get(index);
      if (!character.equalsIgnoreCase(" ")) {
        shapedRecipe.setIngredient(character.charAt(0), recipe.get(index));
      }
    }
    Bukkit.addRecipe(shapedRecipe);
    recipeRegistered = true;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof CustomRecipeCraftingGrid)) {
      return false;
    }
    CustomRecipeCraftingGrid otherCustomRecipeCraftingGrid = (CustomRecipeCraftingGrid) object;
    if (this.recipe.equals(otherCustomRecipeCraftingGrid.recipe)) {
      return true;
    }
    return false;
  }

  @Override
  public String toString() {
    return "CustomRecipeCraftingGrid{" +
        "recipe=" + recipe +
        ", key=" + key +
        ", recipeRegistered=" + recipeRegistered +
        ", keyString='" + keyString + '\'' +
        '}';
  }

  @Override
  public int hashCode() {
    return Objects.hash(recipe);
  }
}
