package mc.carlton.freerpg.globalVariables;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.customContainers.collections.OldCustomRecipe;
import mc.carlton.freerpg.configStorage.ConfigLoad;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CraftingRecipes {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    static Map<String,ArrayList<Material>> identifierRecipeMap = new HashMap<>();
    static ArrayList<Material> tippedArrow;
    static ArrayList<Material> cowEgg;
    static ArrayList<Material> beeEgg;
    static ArrayList<Material> mooshroomEgg;
    static ArrayList<Material> horseEgg;
    static ArrayList<Material> slimeEgg;
    static ArrayList<Material> power;
    static ArrayList<Material> efficiency;
    static ArrayList<Material> sharpness;
    static ArrayList<Material> protection;
    static ArrayList<Material> luck;
    static ArrayList<Material> lure;
    static ArrayList<Material> frost;
    static ArrayList<Material> depth;
    static ArrayList<Material> mending;
    static ArrayList<Material> fortune;
    static ArrayList<Material> waterBreathing;
    static ArrayList<Material> speed;
    static ArrayList<Material> fireResistance;
    static ArrayList<Material> healing;
    static ArrayList<Material> strength;

    public void initializeAllCraftingRecipes() {
        initializeCraftingRecipes();
        //Recipes
        dragonLessArrows();
        farmingRecipes();
        enchantingRecipes();
        alchemyRecipes();
    }

    public void initializeCraftingRecipes() {
        ConfigLoad configLoad = new ConfigLoad();
        Map<String, OldCustomRecipe> craftingRecipes = configLoad.getCraftingRecipes();
        Material[] tippedArrow0 = {Material.ARROW,Material.ARROW,Material.ARROW,
                                   Material.ARROW,Material.POTION,Material.ARROW,
                                   Material.ARROW,Material.ARROW,Material.ARROW};
        tippedArrow = new ArrayList<>(Arrays.asList(tippedArrow0));
        cowEgg =  craftingRecipes.get("farming1").getRecipe();
        beeEgg =  craftingRecipes.get("farming2").getRecipe();
        mooshroomEgg = craftingRecipes.get("farming3").getRecipe();
        horseEgg =  craftingRecipes.get("farming4").getRecipe();
        slimeEgg =  craftingRecipes.get("farming5").getRecipe();
        power =  craftingRecipes.get("enchanting1").getRecipe();
        efficiency =  craftingRecipes.get("enchanting2").getRecipe();
        sharpness =  craftingRecipes.get("enchanting3").getRecipe();
        protection =  craftingRecipes.get("enchanting4").getRecipe();
        luck =  craftingRecipes.get("enchanting5").getRecipe();
        lure =  craftingRecipes.get("enchanting6").getRecipe();
        frost =  craftingRecipes.get("enchanting7").getRecipe();
        depth =  craftingRecipes.get("enchanting8").getRecipe();
        mending =  craftingRecipes.get("enchanting9").getRecipe();
        fortune =  craftingRecipes.get("enchanting10").getRecipe();
        waterBreathing =  craftingRecipes.get("alchemy1").getRecipe();
        speed =  craftingRecipes.get("alchemy2").getRecipe();
        fireResistance =  craftingRecipes.get("alchemy3").getRecipe();
        healing =  craftingRecipes.get("alchemy4").getRecipe();
        strength =  craftingRecipes.get("alchemy5").getRecipe();


    }

    public int getShiftedCraftingIndex(int originalIndex,int vertShift,int horzShift) {
        //Convert original index to row/column
        int row = Math.floorDiv(originalIndex,3);
        int column = originalIndex % 3;
        //Add the shift
        int newRow = row+vertShift;
        int newColumn = column+horzShift;
        //Determine if the shift yeilds a new recipe still inside the crafting grid
        if (newRow < 0 || newRow > 2) {
            return -1;
        }
        if (newColumn <0 || newColumn > 2) {
            return -1;
        }
        return newRow*3 + newColumn;

    }

    public ArrayList<ArrayList<Material>> getTranslatedVariants(ArrayList<Material> initialRecipe) {
        ArrayList<ArrayList<Material>> translatedVariants = new ArrayList<>();
        ArrayList<Integer> indices = getOccupiedIndicesInRecipe(initialRecipe);

        if (indices.size() >= 7) { //If there are 7 unique items in the grid, there is never a possible shift
            translatedVariants.add(initialRecipe);
            return translatedVariants;
        }

        for (int i = -2; i <= 2 ; i++) { //Up and down shifting
            for (int j = -2; j <= 2; j++) { //Left and right shifting
                //create shifted indices
                boolean possibleFormat = true;
                ArrayList<Integer> shiftedIndices = new ArrayList<>();
                shiftedIndicesLoop:
                for (int index : indices) {
                    int newIndex = getShiftedCraftingIndex(index,i,j);
                    if (newIndex == -1) {
                        possibleFormat = false;
                        break shiftedIndicesLoop;
                    }
                    else{
                        shiftedIndices.add(newIndex);
                    }
                }

                //If all shifted indices are possible, add it to the list of possible recipes
                if (possibleFormat) {
                    ArrayList<Material> possibleRecipe = getTransformedRecipe(initialRecipe,shiftedIndices);
                    translatedVariants.add(possibleRecipe);
                }
            }
        }
        return translatedVariants;
    }

    public ArrayList<Integer> getOccupiedIndicesInRecipe(ArrayList<Material> recipe){
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i=0; i< recipe.size();i++) {
            if (!recipe.get(i).equals(Material.AIR) && !recipe.get(i).equals(Material.CAVE_AIR) && !recipe.get(i).equals(Material.VOID_AIR) && recipe.get(i) != null) {
                indices.add(i);
            }
        }
        return indices;
    }

    public ArrayList<Material> getTransformedRecipe(ArrayList<Material> initialRecipe,ArrayList<Integer> newIndices) {
        ArrayList<Integer> indices = getOccupiedIndicesInRecipe(initialRecipe);
        Map<Integer,Integer> transformationMap = new HashMap<>();
        for (int i = 0; i < indices.size(); i++) {
            transformationMap.put(indices.get(i),newIndices.get(i));
        }
        ArrayList<Material> newRecipe = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            newRecipe.add(Material.AIR);
        }
        for (int index : transformationMap.keySet()) {
            int newIndex = transformationMap.get(index);
            newRecipe.set(newIndex,initialRecipe.get(index));
        }
        return newRecipe;
    }

    public Map<Integer,String> getItemToCharacterMap(ArrayList<Material> recipe){
        String[] letters = {"A","B","C","D","E","F","G","H","I"};
        Map<Integer,String> recipeLettersMap = new HashMap<>();
        for (int i = 0; i < 9; i++) {
            if (!recipe.get(i).equals(Material.AIR) && !recipe.get(i).equals(Material.CAVE_AIR) && !recipe.get(i).equals(Material.VOID_AIR) && recipe.get(i) != null) {
                recipeLettersMap.put(i,letters[i]);
            }
            else {
                recipeLettersMap.put(i," ");
            }
        }
        return recipeLettersMap;
    }


    public ArrayList<String> getShapedRecipeLines(Map<Integer,String> recipeLettersMap){
        String line1 = recipeLettersMap.get(0) + recipeLettersMap.get(1) + recipeLettersMap.get(2);
        String line2 = recipeLettersMap.get(3) + recipeLettersMap.get(4) + recipeLettersMap.get(5);
        String line3 = recipeLettersMap.get(6) + recipeLettersMap.get(7) + recipeLettersMap.get(8);
        ArrayList<String> craftingLines = new ArrayList<>();
        craftingLines.add(line1);
        craftingLines.add(line2);
        craftingLines.add(line3);
        return craftingLines;
    }

    public void setShapedRecipe(String id){
        ConfigLoad configLoad = new ConfigLoad();
        OldCustomRecipe recipeInfo = configLoad.getCraftingRecipes().get(id);
        ArrayList<Material> recipeItems = recipeInfo.getRecipe();
        ItemStack item = recipeInfo.getItemStack();

        ArrayList<ArrayList<Material>> allPossibleRecipes = getTranslatedVariants(recipeItems);
        int recipeVariantNumber = 0;
        for (ArrayList<Material> recipeVariant : allPossibleRecipes) {
            String keyString = "frpg_" + id + "_" + recipeVariantNumber;
            NamespacedKey key = new NamespacedKey(plugin, keyString);
            ShapedRecipe recipe = new ShapedRecipe(key, item);
            Map<Integer,String> recipeLettersMap = getItemToCharacterMap(recipeVariant);
            ArrayList<String> shapeLines = getShapedRecipeLines(recipeLettersMap);
            recipe.shape(shapeLines.get(0), shapeLines.get(1), shapeLines.get(2));
            for (int index : recipeLettersMap.keySet()) {
                String character = recipeLettersMap.get(index);
                if (!character.equalsIgnoreCase(" ")) {
                    recipe.setIngredient(character.charAt(0), recipeVariant.get(index));
                }
            }
            Bukkit.addRecipe(recipe);
            recipeVariantNumber += 1;
        }
    }

    //Recipe methods
    public void dragonLessArrows() {
        ItemStack item = new ItemStack(Material.TIPPED_ARROW, 8);
        NamespacedKey key = new NamespacedKey(plugin, "frpgTippedArrows");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("AAA", "APA", "AAA");
        recipe.setIngredient('A', Material.ARROW);
        recipe.setIngredient('P', Material.POTION);
        Bukkit.addRecipe(recipe);
    }

    public void farmingRecipes() {
        setShapedRecipe("farming1");
        setShapedRecipe("farming2");
        setShapedRecipe("farming3");
        setShapedRecipe("farming4");
        setShapedRecipe("farming5");
    }
    public void enchantingRecipes() {
        setShapedRecipe("enchanting1");
        setShapedRecipe("enchanting2");
        setShapedRecipe("enchanting3");
        setShapedRecipe("enchanting4");
        setShapedRecipe("enchanting5");
        setShapedRecipe("enchanting6");
        setShapedRecipe("enchanting7");
        setShapedRecipe("enchanting8");
        setShapedRecipe("enchanting9");
        setShapedRecipe("enchanting10");
    }
    public void alchemyRecipes() {
        setShapedRecipe("alchemy1");
        setShapedRecipe("alchemy2");
        setShapedRecipe("alchemy3");
        setShapedRecipe("alchemy4");
        setShapedRecipe("alchemy5");
    }

    public ArrayList<Material> getBeeEggRecipe() {
        return beeEgg;
    }

    public ArrayList<Material> getCowEggRecipe() {
        return cowEgg;
    }

    public ArrayList<Material> getDepthRecipe() {
        return depth;
    }

    public ArrayList<Material> getEfficiencyRecipe() {
        return efficiency;
    }

    public ArrayList<Material> getFireResistanceRecipe() {
        return fireResistance;
    }

    public ArrayList<Material> getFortuneRecipe() {
        return fortune;
    }

    public ArrayList<Material> getFrostRecipe() {
        return frost;
    }

    public ArrayList<Material> getHealingRecipe() {
        return healing;
    }

    public ArrayList<Material> getHorseEggRecipe() {
        return horseEgg;
    }

    public ArrayList<Material> getLuckRecipe() {
        return luck;
    }

    public ArrayList<Material> getLureRecipe() {
        return lure;
    }

    public ArrayList<Material> getMendingRecipe() {
        return mending;
    }

    public ArrayList<Material> getMooshroomEggRecipe() {
        return mooshroomEgg;
    }

    public ArrayList<Material> getPowerRecipe() {
        return power;
    }

    public ArrayList<Material> getProtectionRecipe() {
        return protection;
    }

    public ArrayList<Material> getSharpnessRecipe() {
        return sharpness;
    }

    public ArrayList<Material> getSlimeEggRecipe() {
        return slimeEgg;
    }

    public ArrayList<Material> getSpeedRecipe() {
        return speed;
    }

    public ArrayList<Material> getStrengthRecipe() {
        return strength;
    }

    public ArrayList<Material> getTippedArrowRecipe() {
        return tippedArrow;
    }

    public ArrayList<Material> getWaterBreathingRecipe() {
        return waterBreathing;
    }

    /*
    public void beeEgg() {
        ItemStack item = new ItemStack(Material.BEE_SPAWN_EGG, 1);
        NamespacedKey key = new NamespacedKey(plugin, "frpgBeeSpawnEgg");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape(" O ", "DHR", " A ");
        recipe.setIngredient('O', Material.OXEYE_DAISY);
        recipe.setIngredient('D', Material.DANDELION);
        recipe.setIngredient('H', Material.HONEY_BOTTLE);
        recipe.setIngredient('R', Material.POPPY);
        recipe.setIngredient('A', Material.AZURE_BLUET);
        Bukkit.addRecipe(recipe);
    }
    public void mooshroomEgg1() {
        ItemStack item = new ItemStack(Material.MOOSHROOM_SPAWN_EGG, 1);
        NamespacedKey key = new NamespacedKey(plugin, "frpgMooshroomSpawnEgg1");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("LML", "BbB", "LBL");
        recipe.setIngredient('L', Material.LEATHER);
        recipe.setIngredient('B', Material.BEEF);
        recipe.setIngredient('b', Material.BONE);
        recipe.setIngredient('M', Material.RED_MUSHROOM);
        Bukkit.addRecipe(recipe);
    }
    public void mooshroomEgg2() {
        ItemStack item = new ItemStack(Material.MOOSHROOM_SPAWN_EGG, 1);
        NamespacedKey key = new NamespacedKey(plugin, "frpgMooshroomSpawnEgg2");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("LML", "BbB", "LBL");
        recipe.setIngredient('L', Material.LEATHER);
        recipe.setIngredient('B', Material.BEEF);
        recipe.setIngredient('b', Material.BONE);
        recipe.setIngredient('M', Material.BROWN_MUSHROOM);
        Bukkit.addRecipe(recipe);
    }
    public void horseEgg() {
        ItemStack item = new ItemStack(Material.HORSE_SPAWN_EGG, 1);
        NamespacedKey key = new NamespacedKey(plugin, "frpgHorseSpawnEgg");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("LSL", "LbL", "HHH");
        recipe.setIngredient('L', Material.LEATHER);
        recipe.setIngredient('S', Material.SADDLE);
        recipe.setIngredient('b', Material.BONE);
        recipe.setIngredient('H', Material.HAY_BLOCK);
        Bukkit.addRecipe(recipe);
    }
    public void slimeEgg() {
        ItemStack item = new ItemStack(Material.SLIME_SPAWN_EGG, 1);
        NamespacedKey key = new NamespacedKey(plugin, "frpgSlimeSpawnEgg");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("   ", " SS", " SS");
        recipe.setIngredient('S', Material.SLIME_BALL);
        Bukkit.addRecipe(recipe);
    }

    public void powerBook() {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
        meta.addStoredEnchant(Enchantment.ARROW_DAMAGE,1,true);
        item.setItemMeta(meta);
        NamespacedKey key = new NamespacedKey(plugin, "frpgPowerBook");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("   ", " PP", " P*");
        recipe.setIngredient('*', Material.BOW);
        recipe.setIngredient('P', Material.PAPER);
        Bukkit.addRecipe(recipe);
    }

    public void efficiencyBook() {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
        meta.addStoredEnchant(Enchantment.DIG_SPEED,1,true);
        item.setItemMeta(meta);
        NamespacedKey key = new NamespacedKey(plugin, "frpgEfficiencyBook");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("   ", " PP", " P*");
        recipe.setIngredient('*', Material.IRON_PICKAXE);
        recipe.setIngredient('P', Material.PAPER);
        Bukkit.addRecipe(recipe);
    }

    public void sharpnessBook() {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
        meta.addStoredEnchant(Enchantment.DAMAGE_ALL,1,true);
        item.setItemMeta(meta);
        NamespacedKey key = new NamespacedKey(plugin, "frpgSharpnessBook");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("I  ", " PP", " P*");
        recipe.setIngredient('*', Material.IRON_SWORD);
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('P', Material.PAPER);
        Bukkit.addRecipe(recipe);
    }

    public void protectionBook() {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
        meta.addStoredEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,1,true);
        item.setItemMeta(meta);
        NamespacedKey key = new NamespacedKey(plugin, "frpgProtectionBook");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape(" * ", "*PP", " P*");
        recipe.setIngredient('*', Material.IRON_INGOT);
        recipe.setIngredient('P', Material.PAPER);
        Bukkit.addRecipe(recipe);
    }

    public void luckBook() {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
        meta.addStoredEnchant(Enchantment.LUCK,1,true);
        item.setItemMeta(meta);
        NamespacedKey key = new NamespacedKey(plugin, "frpgLuckBook");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("R  ", " PP", " P*");
        recipe.setIngredient('*', Material.FISHING_ROD);
        recipe.setIngredient('R', Material.RABBIT_FOOT);
        recipe.setIngredient('P', Material.PAPER);
        Bukkit.addRecipe(recipe);
    }

    public void lureBook() {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
        meta.addStoredEnchant(Enchantment.LURE,1,true);
        item.setItemMeta(meta);
        NamespacedKey key = new NamespacedKey(plugin, "frpgLureBook");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("R  ", " PP", " P*");
        recipe.setIngredient('*', Material.FISHING_ROD);
        recipe.setIngredient('R', Material.COD_BUCKET);
        recipe.setIngredient('P', Material.PAPER);
        Bukkit.addRecipe(recipe);
    }

    public void frostBook() {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
        meta.addStoredEnchant(Enchantment.FROST_WALKER,1,true);
        item.setItemMeta(meta);
        NamespacedKey key = new NamespacedKey(plugin, "frpgFrostBook");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("   ", " PP", " P*");
        recipe.setIngredient('*', Material.BLUE_ICE);
        recipe.setIngredient('P', Material.PAPER);
        Bukkit.addRecipe(recipe);
    }

    public void depthBook() {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
        meta.addStoredEnchant(Enchantment.DEPTH_STRIDER,1,true);
        item.setItemMeta(meta);
        NamespacedKey key = new NamespacedKey(plugin, "frpgDepthBook");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("   ", " PP", " P*");
        recipe.setIngredient('*', Material.NAUTILUS_SHELL);
        recipe.setIngredient('P', Material.PAPER);
        Bukkit.addRecipe(recipe);
    }

    public void mendingBook() {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
        meta.addStoredEnchant(Enchantment.MENDING,1,true);
        item.setItemMeta(meta);
        NamespacedKey key = new NamespacedKey(plugin, "frpgMendingBook");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("   ", " PP", " P*");
        recipe.setIngredient('*', Material.DIAMOND_BLOCK);
        recipe.setIngredient('P', Material.PAPER);
        Bukkit.addRecipe(recipe);
    }

    public void fortuneBook() {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
        meta.addStoredEnchant(Enchantment.LOOT_BONUS_BLOCKS,1,true);
        item.setItemMeta(meta);
        NamespacedKey key = new NamespacedKey(plugin, "frpgFortuneBook");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("   ", " PP", " P*");
        recipe.setIngredient('*', Material.GOLD_BLOCK);
        recipe.setIngredient('P', Material.PAPER);
        Bukkit.addRecipe(recipe);
    }
     public void waterBreathingPotion() {
        ConfigLoad configLoad = new ConfigLoad();
        ArrayList<Object> alchemyInfo =configLoad.getAlchemyInfo();
        PotionType potionType = (PotionType) alchemyInfo.get(20);
        Material potionIngredient = (Material) alchemyInfo.get(21);
        ItemStack item = new ItemStack(Material.POTION, 1);
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        meta.setBasePotionData(new PotionData(potionType,false,false));
        item.setItemMeta(meta);
        NamespacedKey key = new NamespacedKey(plugin, "frpgWaterBreathingPotion");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape(" I ", " W ", "   ");
        recipe.setIngredient('I', potionIngredient);
        recipe.setIngredient('W', Material.GLASS_BOTTLE);
        Bukkit.addRecipe(recipe);
    }

    public void speedPotion() {
        ConfigLoad configLoad = new ConfigLoad();
        ArrayList<Object> alchemyInfo =configLoad.getAlchemyInfo();
        PotionType potionType = (PotionType) alchemyInfo.get(22);
        Material potionIngredient = (Material) alchemyInfo.get(23);
        ItemStack item = new ItemStack(Material.POTION, 1);
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        meta.setBasePotionData(new PotionData(potionType,false,false));
        item.setItemMeta(meta);
        NamespacedKey key = new NamespacedKey(plugin, "frpgSpeedPotion");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape(" I ", " W ", "   ");
        recipe.setIngredient('I', potionIngredient);
        recipe.setIngredient('W', Material.GLASS_BOTTLE);
        Bukkit.addRecipe(recipe);
    }

    public void fireResistancePotion() {
        ConfigLoad configLoad = new ConfigLoad();
        ArrayList<Object> alchemyInfo =configLoad.getAlchemyInfo();
        PotionType potionType = (PotionType) alchemyInfo.get(24);
        Material potionIngredient = (Material) alchemyInfo.get(25);
        ItemStack item = new ItemStack(Material.POTION, 1);
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        meta.setBasePotionData(new PotionData(potionType,false,false));
        item.setItemMeta(meta);
        NamespacedKey key = new NamespacedKey(plugin, "frpgFireResistancePotion");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape(" I ", " W ", "   ");
        recipe.setIngredient('I', potionIngredient);
        recipe.setIngredient('W', Material.GLASS_BOTTLE);
        Bukkit.addRecipe(recipe);
    }

    public void healingPotion() {
        ConfigLoad configLoad = new ConfigLoad();
        ArrayList<Object> alchemyInfo =configLoad.getAlchemyInfo();
        PotionType potionType = (PotionType) alchemyInfo.get(26);
        Material potionIngredient = (Material) alchemyInfo.get(27);
        ItemStack item = new ItemStack(Material.POTION, 1);
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        meta.setBasePotionData(new PotionData(potionType,false,false));
        item.setItemMeta(meta);
        NamespacedKey key = new NamespacedKey(plugin, "frpgHealingPotion");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape(" I ", " W ", "   ");
        recipe.setIngredient('I', potionIngredient);
        recipe.setIngredient('W', Material.GLASS_BOTTLE);
        Bukkit.addRecipe(recipe);
    }

    public void strengthPotion() {
        ConfigLoad configLoad = new ConfigLoad();
        ArrayList<Object> alchemyInfo =configLoad.getAlchemyInfo();
        PotionType potionType = (PotionType) alchemyInfo.get(28);
        Material potionIngredient = (Material) alchemyInfo.get(29);
        ItemStack item = new ItemStack(Material.POTION, 1);
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        meta.setBasePotionData(new PotionData(potionType,false,false));
        item.setItemMeta(meta);
        NamespacedKey key = new NamespacedKey(plugin, "frpgStrengthPotion");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape(" I ", " W ", "   ");
        recipe.setIngredient('I', potionIngredient);
        recipe.setIngredient('W', Material.GLASS_BOTTLE);
        Bukkit.addRecipe(recipe);
    }

     */
}
