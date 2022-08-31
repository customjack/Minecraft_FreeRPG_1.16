package mc.carlton.freerpg.customContainers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mc.carlton.freerpg.customContainers.collections.CustomEffect;
import mc.carlton.freerpg.customContainers.collections.CustomRecipe;
import mc.carlton.freerpg.customContainers.collections.DropTable;
import mc.carlton.freerpg.customContainers.collections.MobDropTable;
import mc.carlton.freerpg.utils.FrpgPrint;
import mc.carlton.freerpg.utils.UtilityMethods;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

/*
This Class' purpose is to import information from a config node into an actually a container class (CustomItem, CustomPotion, etc...)
 */
public class CustomContainerImporter {

  /*
   Error Messages to display
   */
  private static final String NO_PATH = "Unknown Config Path";
  private static final String IMPROPER_CONFIG = "WARNING: Improper Config at ";
  private static final String MATERIAL_NOT_FOUND = "Material not found: ";
  private static final String IMPROPER_AMOUNT = "Amount cannot be less than 1 ";
  private static final String DURABILITY_PARAMETERS_INVALID = "Durability Parameters Invalid";
  private static final String INVALID_DURABILITY_BOUND = "Minimum durability cannot be greater than maximum durability";
  private static final String RANDOM_ENCHANTMENT_PARAMETERS_INVALID = "Random Enchantment Parameters Invalid";
  private static final String INVALID_ENCHANTMENT_BOUNDS = "Minimum Enchantment level cannot be greater than Maximum enchantment level";
  private static final String STATIC_ENCHANTMENT_PARAMETERS_INVALID = "Static Enchantment Parameters Invalid";
  private static final String NEGATIVE_WEIGHT = "Weight Value cannot be negative";
  private static final String INVALID_PROBABILITY = "Probability value must be between 0 and 1";
  private static final String IMPROPER_DURABILITY_LIMITS = "Durability Limits cannot be less than 0";
  private static final String IMPROPER_ENCHANTMENT_LIMITS = "Enchantment Limits cannot be less than 0";
  private static final String INVALID_ENCHANTMENT = "Invalid Enchantment: ";
  private static final String INVALID_ENCHANTMENT_LEVEL = "Invalid Enchantment Level: ";
  private static final String INVALID_POTION_TYPE = "Invalid potion type: ";
  private static final String INVALID_EFFECT_TYPE = "Invalid effect type: ";
  private static final String INVALID_DURATION = "Potion Duration must be greater than 0";
  private static final String INVALID_POTION_LEVEL = "Invalid Potion Level: ";
  private static final String EXPECTED_LIST = "Expected List at config node: ";
  private static final String EXPECTED_LIST_OF_MAPS = "Expected List elements to be Maps/Dictionaries at config node: ";
  private static final String UNKNOWN_CONFIG_LOCATION = "Unknown Config Location";
  private static final String POTION_EFFECT_TYPE_INVALID = "Potion Effect Type Parameters Invalid";
  private static final String INVALID_POTION_COLOR = "RGB values must be in the range 0 - 255";
  private static final String ITEM_TYPE_REQUIRED = "An Item Type is required for a custom item";
  private static final String POTION_EFFECT_TYPE_REQUIRED = "A Potion Effect Type is required for a custom effect";
  private static final String INVALID_DELAY = "Delay cannot be negative";
  private static final String INVALID_DURATION_MULTIPLIER = "Duration Multiplier cannot be negative";
  private static final String INVALID_RECIPE_ARRAYLIST = "Invalid Recipe Array: ";
  private static final String RECIPE_REQUIRED = "Invalid Recipe Array: ";
  private static final String OUTPUT_REQUIRED = "Invalid Recipe Array: ";
  private static final String INVALID_ENTITY = "EntityType not found, ";
  private static final String INVALID_DAMAGE_CAUSE = "DamageCause not found, ";
  /*
   Acceptable keywords for expected information to be read in
   */
  private static final List<String> MATERIAL_KEYWORDS = Arrays.asList(new String[]{"item", "drop"});
  private static final List<String> AMOUNT_KEYWORDS = Arrays.asList(new String[]{"amount"});
  private static final List<String> NANE_KEYWORDS = Arrays.asList(new String[]{"itemName", "name"});
  private static final List<String> DURABILITY_KEYWORDS = Arrays.asList(
      new String[]{"durability", "durabilityModifier"});
  private static final List<String> ENCHANTMENT_BOUNDS_KEYWORDS = Arrays.asList(
      new String[]{"randomEnchantment", "enchantmentBounds"});
  private static final List<String> ENCHANTMENTS_KEYWORDS = Arrays.asList(
      new String[]{"staticEnchantments", "enchantments"});
  private static final List<String> WEIGHT_KEYWORDS = Arrays.asList(new String[]{"weight"});
  private static final List<String> PROBABILITY_KEYWORDS = Arrays.asList(
      new String[]{"prob", "probability"});
  private static final List<String> EXPERIENCE_KEYWORDS = Arrays.asList(
      new String[]{"experience", "exp"});
  private static final List<String> POTION_KEYWORDS = Arrays.asList(
      new String[]{"potion", "potionEffect"});
  private static final List<String> EFFECT_TYPE_KEYWORDS = Arrays.asList(
      new String[]{"effect", "potionEffect"});
  private static final List<String> EFFECT_LEVEL_KEYWORDS = Arrays.asList(
      new String[]{"level", "effectLevel", "potionLevel"});
  private static final List<String> EFFECT_DURATION_KEYWORDS = Arrays.asList(
      new String[]{"duration", "length"});
  private static final List<String> EFFECT_DELAY_KEYWORDS = Arrays.asList(
      new String[]{"delay", "timeDelay"});
  private static final List<String> EFFECT_DURATION_MULTIPLIER_KEYWORDS = Arrays.asList(
      new String[]{"durationMultiplier", "relativeDuration"});
  private static final List<String> EFFECT_DURATION_ADDED_KEYWORDS = Arrays.asList(
      new String[]{"durationAdded", "relativeDurationAdded"});
  private static final List<String> RECIPE_OUTPUT_KEYWORDS = Arrays.asList(
      new String[]{"output", "result"});
  private static final List<String> RECIPE_RECIPE_KEYWORDS = Arrays.asList(
      new String[]{"recipe", "craftingGrid"});
  private static final List<String> RECIPE_XP_COST_KEYWORDS = Arrays.asList(
      new String[]{"xpCost", "experienceCost"});
  private static final String LOWER_BOUND_KEY_WORD = "lower";
  private static final String UPPER_BOUND_KEY_WORD = "upper";
  private static final String IS_TREASURE_KEYWORD = "isTreasure";
  private static final String ENCHANTMENT_KEYWORD = "enchant";
  private static final String LEVEL_KEYWORD = "level";
  private static final String EFFECT_KEYWORD = "effect";
  private static final String DURATION_KEYWORD = "duration";
  private static final String COLOR_KEYWORD = "color";
  private static final String IS_UPGRADED_KEYWORD = "isUpgraded";
  private static final String IS_EXTENDED_KEYWORD = "isExtended";
  private static final String MOB_KEYWORD = "mob";
  private static final String OLD_DROP_KEYWORD = "oldDrop";
  private static final String NEW_DROP_KEYWORD = "newDrop";
  String configPath;

  public CustomContainerImporter(String configPath) {
    this.configPath = configPath;
  }

  public CustomContainerImporter() {
    this(NO_PATH);
  }

  public List<EntityDamageEvent.DamageCause> getDamageCauseList(Object configDamageCauseList) {
    if (!(configDamageCauseList instanceof List)) {
      printReadInError();
      return null;
    }
    ArrayList<EntityDamageEvent.DamageCause> damageCauseList = new ArrayList<>();
    for (Object damageCauseString : (List) configDamageCauseList) {
      EntityDamageEvent.DamageCause damageCause = UtilityMethods.matchDamageCause(
          damageCauseString.toString());
      if (damageCause != null) {
        damageCauseList.add(damageCause);
      } else {
        printReadInError(INVALID_DAMAGE_CAUSE + damageCauseString);
      }
    }
    return damageCauseList;
  }

  public List<Material> getMaterialList(Object configMaterialList) {
    if (!(configMaterialList instanceof List)) {
      printReadInError();
      return null;
    }
    ArrayList<Material> materialList = new ArrayList<>();
    for (Object materialTypeString : (List) configMaterialList) {
      Material material = Material.matchMaterial(materialTypeString.toString());
      if (material != null) {
        materialList.add(material);
      } else {
        printReadInError(MATERIAL_NOT_FOUND + materialTypeString);
      }
    }
    return materialList;
  }

  public List<EntityType> getEntityTypeList(Object configEntityTypeList) {
    if (!(configEntityTypeList instanceof List)) {
      printReadInError();
      return null;
    }
    ArrayList<EntityType> entityTypeList = new ArrayList<>();
    for (Object entityTypeString : (List) configEntityTypeList) {
      EntityType entityType = UtilityMethods.matchEntityType(entityTypeString.toString());
      if (entityType != null) {
        entityTypeList.add(entityType);
      } else {
        printReadInError(INVALID_ENTITY + entityTypeString);
      }
    }
    return entityTypeList;
  }

  public Map<CustomItem, CustomItem> getCustomItemMapping(Object configCustomItemMapping) {
    if (!(configCustomItemMapping instanceof List)) {
      printReadInError();
      return null;
    }
    Map<CustomItem, CustomItem> customItemMap = new HashMap<>();
    for (Object configCustomItemMap : (List) configCustomItemMapping) {
      if (configCustomItemMap instanceof Map) {
        Map oldDropNewDropNodes = (Map) configCustomItemMapping;
        if (oldDropNewDropNodes.containsKey(OLD_DROP_KEYWORD) && oldDropNewDropNodes.containsKey(
            NEW_DROP_KEYWORD)) {
          CustomItem oldCustomItem = getCustomItem(oldDropNewDropNodes.get(OLD_DROP_KEYWORD));
          CustomItem newCustomItem = getCustomItem(oldDropNewDropNodes.get(NEW_DROP_KEYWORD));
          if (oldCustomItem != null && newCustomItem != null) {
            customItemMap.put(oldCustomItem, newCustomItem);
          } else {
            printReadInError();
          }
        } else {
          printReadInError();
        }
      } else {
        printReadInError();
      }
    }
    return customItemMap;
  }

  public List<PotionData> getPotionDataList(Object configPotionDataList) {
    if (!(configPotionDataList instanceof List)) {
      printReadInError();
      return null;
    }
    ArrayList<PotionData> potionDataList = new ArrayList<>();
    for (Object configPotionDataRow : (List) configPotionDataList) {
      Map<String, Object> potionDataRow = convertListedTableRowToMap(configPotionDataRow);
      PotionData potionData = null;
      if (potionDataRow.containsKey(EFFECT_KEYWORD) && potionDataRow.containsKey(
          IS_EXTENDED_KEYWORD)) {
        potionData = new PotionData(
            UtilityMethods.matchPotionType(potionDataRow.get(EFFECT_KEYWORD).toString()),
            Boolean.valueOf(potionDataRow.get(IS_EXTENDED_KEYWORD).toString()), false);
      } else if (potionDataRow.containsKey(EFFECT_KEYWORD) && potionDataRow.containsKey(
          IS_EXTENDED_KEYWORD)) {
        potionData = new PotionData(
            UtilityMethods.matchPotionType(potionDataRow.get(EFFECT_KEYWORD).toString()), false,
            Boolean.valueOf(potionDataRow.get(IS_UPGRADED_KEYWORD).toString()));
      }
      if (potionData != null) {
        potionDataList.add(potionData);
      }
    }
    return potionDataList;


  }

  public CustomPotion getCustomPotion(Object configPotionInformation) {
    if (!(configPotionInformation instanceof List)) {
      printReadInError();
      return null;
    }
    Map<String, Object> itemInformation = convertListedTableRowToMap(configPotionInformation);
    CustomPotion customPotion = new CustomPotion(itemInformation);
    constructCustomItem(customPotion, itemInformation);
    return customPotion;
  }

  public MobDropTable getMobDropTable(Object configMobDropTable) {
    if (!(configMobDropTable instanceof List)) {
      printReadInError();
      return null;
    }
    MobDropTable mobdropTable = new MobDropTable();
    for (Object configRow : (List) configMobDropTable) {
      CustomItem dropTableItem = getCustomItem(configRow);
      if (dropTableItem != null) {
        Map<String, Object> row = convertListedTableRowToMap(configRow);
        if (row.containsKey(MOB_KEYWORD)) {
          EntityType entityType = UtilityMethods.matchEntityType(row.get(MOB_KEYWORD).toString());
          if (entityType != null) {
            mobdropTable.addDropToMob(entityType, dropTableItem);
          }
        }
      }
    }
    return mobdropTable;
  }

  public DropTable getDropTable(Object configDropTable) {
    if (!(configDropTable instanceof List)) {
      printReadInError();
      return null;
    }
    ArrayList<CustomItem> dropTableList = new ArrayList<>();
    for (Object configItem : (List) configDropTable) {
      CustomItem dropTableItem = getCustomItem(configItem);
      if (dropTableItem != null) {
        dropTableList.add(dropTableItem);
      }
    }
    return new DropTable(dropTableList);
  }

  public List<CustomRecipe> getCustomRecipeList(Object configRecipes, String newRecipesBaseId) {
    if (!(configRecipes instanceof List)) {
      printReadInError();
      return null;
    }
    ArrayList<CustomRecipe> customRecipes = new ArrayList<>();
    List configRecipesList = (List) configRecipes;
    for (int i = 0; i < configRecipesList.size(); i++) {
      CustomRecipe newRecipe = getCustomRecipe(configRecipesList.get(i),
          newRecipesBaseId + UtilityMethods.toAlphabetic(i));
      if (newRecipe != null) {
        customRecipes.add(newRecipe);
      }
    }
    return customRecipes;
  }

  public CustomRecipe getCustomRecipe(Object configRecipe, String newRecipeId) {
    if (!(configRecipe instanceof Map)) {
      printReadInError();
      return null;
    }
    Map<String, Object> recipeInformation = new HashMap<>();
    for (Object object : ((Map) configRecipe).keySet()) {
      recipeInformation.put(object.toString(), ((Map) configRecipe).get(object));
    }
    CustomItem output = null;
    ArrayList<Material> recipe = null;
    Integer xpCost = 0;
    for (String key : recipeInformation.keySet()) {
      if (UtilityMethods.stringCollectionContainsIgnoreCase(RECIPE_OUTPUT_KEYWORDS, key)) {
        output = getCustomItem(recipeInformation.get(key));
      }
      if (UtilityMethods.stringCollectionContainsIgnoreCase(RECIPE_RECIPE_KEYWORDS, key)) {
        recipe = getRecipeArrayList(recipeInformation.get(key));
      }
      if (UtilityMethods.stringCollectionContainsIgnoreCase(RECIPE_XP_COST_KEYWORDS, key)) {
        xpCost = Integer.valueOf(recipeInformation.get(key).toString());
      }
    }
    if (output == null) {
      printReadInError(OUTPUT_REQUIRED);
      return null;
    }
    if (recipe == null) {
      printReadInError(RECIPE_REQUIRED);
      return null;
    }
    return new CustomRecipe(recipe, output.getItemStack(), xpCost, newRecipeId);
  }

  public CustomItem getCustomItem(Object configItem) {
    if (!(configItem instanceof List)) {
      printReadInError();
      return null;
    }
    Map<String, Object> itemInformation = convertListedTableRowToMap(configItem);
    Material material = null;
    for (String key : itemInformation.keySet()) {
      if (UtilityMethods.stringCollectionContainsIgnoreCase(MATERIAL_KEYWORDS, key)) {
        material = getMaterial(itemInformation.get(key));
        break;
      }
    }
    if (material == null) {
      printReadInError(ITEM_TYPE_REQUIRED);
      return null;
    }
    if (material.equals(Material.POTION)) {
      CustomPotion customPotion = new CustomPotion(itemInformation);
      constructCustomItem(customPotion, itemInformation);
      return customPotion;
    } else {
      CustomItem customItem = new CustomItem(material, itemInformation);
      constructCustomItem(customItem, itemInformation);
      return customItem;
    }
  }

  public CustomEffect getCustomEffect(Object configEffect) {
    if (!(configEffect instanceof List)) {
      printReadInError();
      return null;
    }
    CustomEffect customEffect = new CustomEffect();
    for (Object configEffectPiece : (List) configEffect) {
      CustomEffectPiece customEffectPiece = getCustomEffectPiece(configEffectPiece);
      if (customEffectPiece != null) {
        customEffect.addEffect(customEffectPiece);
      }
    }
    return customEffect;
  }

  public CustomEffectPiece getCustomEffectPiece(Object configEffect) {
    if (!(configEffect instanceof List)) {
      printReadInError();
      return null;
    }
    Map<String, Object> effectInformation = convertListedTableRowToMap(configEffect);
    PotionEffectType potionEffectType = null;
    for (String key : effectInformation.keySet()) {
      if (UtilityMethods.stringCollectionContainsIgnoreCase(EFFECT_TYPE_KEYWORDS, key)) {
        potionEffectType = getEffectType(effectInformation.get(key));
        break;
      }
    }
    if (potionEffectType == null) {
      printReadInError(POTION_EFFECT_TYPE_REQUIRED);
      return null;
    }
    CustomEffectPiece customEffect = new CustomEffectPiece(potionEffectType);
    constructCustomEffect(customEffect, effectInformation);
    return customEffect;
  }

  private void constructCustomItem(CustomItem customItem, Map<String, Object> itemInformation) {
    for (String key : itemInformation.keySet()) {
      Object value = itemInformation.get(key);
      assignCustomItemValue(customItem, value, key);
    }
  }

  private void constructCustomItem(CustomPotion customPotion, Map<String, Object> itemInformation) {
    for (String key : itemInformation.keySet()) {
      Object value = itemInformation.get(key);
      assignCustomItemValue(customPotion, value, key);
      assignCustomPotionValues(customPotion, value, key);
    }
  }

  private void constructCustomEffect(CustomEffectPiece customEffect,
      Map<String, Object> effectInformation) {
    for (String key : effectInformation.keySet()) {
      Object value = effectInformation.get(key);
      assignCustomEffectValue(customEffect, value, key);
    }
  }

  private void assignCustomItemValue(CustomItem customItemContainer, Object value, String mapKey) {
    if (UtilityMethods.stringCollectionContainsIgnoreCase(AMOUNT_KEYWORDS, mapKey)) {
      Integer amount = Integer.valueOf(value.toString());
      if (amount < 0) {
        printReadInError(IMPROPER_AMOUNT + amount);
        return;
      }
      customItemContainer.setAmount(amount);
    } else if (UtilityMethods.stringCollectionContainsIgnoreCase(DURABILITY_KEYWORDS, mapKey)) {
      Map<String, Object> durabilityInfo = getMap(value);
      if (durabilityInfo == null) {
        return;
      }
      //Get lower and upper bound doubles
      boolean lowerBoundPresent = UtilityMethods.stringCollectionContainsIgnoreCase(
          durabilityInfo.keySet(), LOWER_BOUND_KEY_WORD);
      boolean upperBoundPresent = UtilityMethods.stringCollectionContainsIgnoreCase(
          durabilityInfo.keySet(), UPPER_BOUND_KEY_WORD);
      if (!lowerBoundPresent && !upperBoundPresent) {
        printReadInError(DURABILITY_PARAMETERS_INVALID);
      }
      double lowerBound =
          (lowerBoundPresent) ? Double.valueOf(durabilityInfo.get(LOWER_BOUND_KEY_WORD).toString())
              : 1.0;
      double upperBound =
          (upperBoundPresent) ? Double.valueOf(durabilityInfo.get(UPPER_BOUND_KEY_WORD).toString())
              : 1.0;
      if (lowerBound > upperBound) {
        printReadInError(INVALID_DURABILITY_BOUND);
        lowerBound = upperBound;
      }
      if (lowerBound < 0 || upperBound < 0) {
        printReadInError(IMPROPER_DURABILITY_LIMITS);
        return;
      }
      customItemContainer.setDurabilityRange(lowerBound, upperBound);
    } else if (UtilityMethods.stringCollectionContainsIgnoreCase(ENCHANTMENT_BOUNDS_KEYWORDS,
        mapKey)) {
      Map<String, Object> enchantmentInfo = getMap(value);
      if (enchantmentInfo == null) {
        return;
      }
      //Get lower and upper bound doubles
      boolean lowerBoundPresent = UtilityMethods.stringCollectionContainsIgnoreCase(
          enchantmentInfo.keySet(), LOWER_BOUND_KEY_WORD);
      boolean upperBoundPresent = UtilityMethods.stringCollectionContainsIgnoreCase(
          enchantmentInfo.keySet(), UPPER_BOUND_KEY_WORD);
      boolean isTreasurePresent = UtilityMethods.stringCollectionContainsIgnoreCase(
          enchantmentInfo.keySet(), IS_TREASURE_KEYWORD);
      if (!lowerBoundPresent && !upperBoundPresent && !isTreasurePresent) {
        printReadInError(RANDOM_ENCHANTMENT_PARAMETERS_INVALID);
      }
      int lowerBound = (lowerBoundPresent) ? Integer.valueOf(
          enchantmentInfo.get(LOWER_BOUND_KEY_WORD).toString()) : 0;
      int upperBound = (upperBoundPresent) ? Integer.valueOf(
          enchantmentInfo.get(UPPER_BOUND_KEY_WORD).toString()) : 0;
      boolean isTreasure =
          (isTreasurePresent) ? Boolean.valueOf(enchantmentInfo.get(IS_TREASURE_KEYWORD).toString())
              : true;
      if (lowerBound > upperBound) {
        printReadInError(INVALID_ENCHANTMENT_BOUNDS);
        lowerBound = upperBound;
      }
      if (lowerBound < 0 || upperBound < 0) {
        printReadInError(IMPROPER_ENCHANTMENT_LIMITS);
        return;
      }
      customItemContainer.setEnchantmentLevelRange(lowerBound, upperBound);
      customItemContainer.setTreasure(isTreasure);
    } else if (UtilityMethods.stringCollectionContainsIgnoreCase(ENCHANTMENTS_KEYWORDS, mapKey)) {
      List enchantmentsListUnParsed = getList(value);
      if (enchantmentsListUnParsed == null) {
        return;
      }
      List<Map<String, Object>> enchantmentsList = getConfigTableInformation(
          enchantmentsListUnParsed);
      if (enchantmentsList == null) {
        return;
      }
      for (Map<String, Object> enchantmentInfo : enchantmentsList) {
        Enchantment enchantment =
            (UtilityMethods.stringCollectionContainsIgnoreCase(enchantmentInfo.keySet(),
                ENCHANTMENT_KEYWORD)) ? getEnchantment(enchantmentInfo.get(ENCHANTMENT_KEYWORD))
                : null;
        if (enchantment == null) {
          printReadInError(STATIC_ENCHANTMENT_PARAMETERS_INVALID);
          continue;
        }
        int level = (UtilityMethods.stringCollectionContainsIgnoreCase(enchantmentInfo.keySet(),
            LEVEL_KEYWORD)) ? Integer.valueOf(enchantmentInfo.get(LEVEL_KEYWORD).toString()) : 1;
        if (level < 0) {
          printReadInError(INVALID_ENCHANTMENT_LEVEL + level);
          level = 1;
        }
        customItemContainer.addEnchantment(enchantment, level);
      }
    } else if (UtilityMethods.stringCollectionContainsIgnoreCase(WEIGHT_KEYWORDS, mapKey)) {
      Double weight = Double.valueOf(value.toString());
      if (weight < 0) {
        printReadInError(NEGATIVE_WEIGHT);
        return;
      }
      customItemContainer.setWeight(weight);
    } else if (UtilityMethods.stringCollectionContainsIgnoreCase(PROBABILITY_KEYWORDS, mapKey)) {
      Double prob = Double.valueOf(value.toString());
      if (prob < 0 || prob > 1) {
        printReadInError(INVALID_PROBABILITY);
        return;
      }
      customItemContainer.setProbability(prob);
    } else if (UtilityMethods.stringCollectionContainsIgnoreCase(EXPERIENCE_KEYWORDS, mapKey)) {
      Integer exp = Integer.valueOf(value.toString());
      customItemContainer.setAmount(exp);
    } else if (UtilityMethods.stringCollectionContainsIgnoreCase(NANE_KEYWORDS, mapKey)) {
      String name = ChatColor.translateAlternateColorCodes('&', String.valueOf(mapKey));
      customItemContainer.setItemName(name);
    }
  }

  private void assignCustomPotionValues(CustomPotion customPotionContainer, Object value,
      String mapKey) {
    if (UtilityMethods.stringCollectionContainsIgnoreCase(POTION_KEYWORDS, mapKey)) {
      Map<String, Object> minecraftPotionInfo = getMap(value, true);

      if (minecraftPotionInfo == null) { //Custom Potion Case
        List customEffectInfoUnParsed = getList(value);
        if (customEffectInfoUnParsed == null) {
          return;
        }
        List<Map<String, Object>> customEffectInfoList = getConfigTableInformation(
            customEffectInfoUnParsed);
        if (customEffectInfoList == null) {
          return;
        }

        //Custom Effect Case
        for (Map<String, Object> customEffectInfo : customEffectInfoList) {
          boolean effectPresent = UtilityMethods.stringCollectionContainsIgnoreCase(
              customEffectInfo.keySet(), EFFECT_KEYWORD);
          boolean durationPresent = UtilityMethods.stringCollectionContainsIgnoreCase(
              customEffectInfo.keySet(), DURATION_KEYWORD);
          boolean levelPresent = UtilityMethods.stringCollectionContainsIgnoreCase(
              customEffectInfo.keySet(), LEVEL_KEYWORD);
          boolean colorPresent = UtilityMethods.stringCollectionContainsIgnoreCase(
              customEffectInfo.keySet(), COLOR_KEYWORD);

          PotionEffectType effectType =
              (effectPresent) ? getEffectType(customEffectInfo.get(EFFECT_KEYWORD)) : null;
          Double duration =
              (durationPresent) ? Double.valueOf(customEffectInfo.get(DURATION_KEYWORD).toString())
                  : 0.0;
          Integer level =
              (levelPresent) ? Integer.valueOf(customEffectInfo.get(LEVEL_KEYWORD).toString()) : 1;
          Color color =
              (colorPresent) ? getPotionColor(customEffectInfo.get(COLOR_KEYWORD).toString())
                  : null;

          if (effectType == null) {
            printReadInError(POTION_EFFECT_TYPE_INVALID);
            continue;
          }
          if (duration < 0) {
            printReadInError(INVALID_DURATION);
            duration = 0.0;
          }
          if (level < 0) {
            printReadInError(INVALID_POTION_LEVEL + level);
            level = 1;
          }
          if (color != null) {
            customPotionContainer.addColor(color);
          }
          customPotionContainer.addPotionEffect(effectType, duration, level);
        }
        return;
      } else { //Vanilla Potion Case
        boolean effectPresent = UtilityMethods.stringCollectionContainsIgnoreCase(
            minecraftPotionInfo.keySet(), EFFECT_KEYWORD);
        boolean isUpgradedPresent = UtilityMethods.stringCollectionContainsIgnoreCase(
            minecraftPotionInfo.keySet(), IS_UPGRADED_KEYWORD);
        boolean isExtendedPresent = UtilityMethods.stringCollectionContainsIgnoreCase(
            minecraftPotionInfo.keySet(), IS_EXTENDED_KEYWORD);

        PotionType effectType =
            (effectPresent) ? getPotionType(minecraftPotionInfo.get(EFFECT_KEYWORD)) : null;
        boolean isUpgraded = (isUpgradedPresent) ? Boolean.valueOf(
            minecraftPotionInfo.get(IS_UPGRADED_KEYWORD).toString()) : false;
        boolean isExtended = (isExtendedPresent) ? Boolean.valueOf(
            minecraftPotionInfo.get(IS_EXTENDED_KEYWORD).toString()) : false;
        customPotionContainer.setPotion(effectType, isUpgraded, isExtended);
      }
    }
  }

  private void assignCustomEffectValue(CustomEffectPiece customEffect, Object value,
      String mapKey) {
    if (UtilityMethods.stringCollectionContainsIgnoreCase(EFFECT_LEVEL_KEYWORDS, mapKey)) {
      Integer level = Integer.valueOf(value.toString());
      if (level < 1) {
        printReadInError(INVALID_POTION_LEVEL + level);
        return;
      }
      customEffect.setLevel(level);
    } else if (UtilityMethods.stringCollectionContainsIgnoreCase(EFFECT_DURATION_KEYWORDS,
        mapKey)) {
      Double duration = Double.valueOf(value.toString());
      if (duration < 0) {
        printReadInError(INVALID_DURATION + duration);
        return;
      }
      customEffect.setDuration(duration);

    } else if (UtilityMethods.stringCollectionContainsIgnoreCase(EFFECT_DELAY_KEYWORDS, mapKey)) {
      Double delay = Double.valueOf(value.toString());
      if (delay < 0) {
        printReadInError(INVALID_DELAY + delay);
        return;
      }
      customEffect.setDelay(delay);
    } else if (UtilityMethods.stringCollectionContainsIgnoreCase(PROBABILITY_KEYWORDS, mapKey)) {
      Double prob = Double.valueOf(value.toString());
      if (prob < 0 || prob > 1) {
        printReadInError(INVALID_PROBABILITY + prob);
        return;
      }
      customEffect.setProbability(prob);
    } else if (UtilityMethods.stringCollectionContainsIgnoreCase(
        EFFECT_DURATION_MULTIPLIER_KEYWORDS, mapKey)) {
      Double durationMultiplier = Double.valueOf(value.toString());
      if (durationMultiplier < 0) {
        printReadInError(INVALID_DURATION_MULTIPLIER + durationMultiplier);
        return;
      }
      customEffect.setRelativeDurationMultiplier(durationMultiplier);
    } else if (UtilityMethods.stringCollectionContainsIgnoreCase(EFFECT_DURATION_ADDED_KEYWORDS,
        mapKey)) {
      Double durationAdded = Double.valueOf(value.toString());
      customEffect.setRelativeDurationAdded(durationAdded);
    }
  }

  private Material getMaterial(Object value) {
    Material material = Material.getMaterial(value.toString());
    if (material == null) {
      printReadInError(MATERIAL_NOT_FOUND + value.toString());
    }
    return material;
  }

  private ArrayList<Material> getRecipeArrayList(Object value) {
    if (!(value instanceof List)) {
      printReadInError(INVALID_RECIPE_ARRAYLIST + value.toString());
      return null;
    }
    List valueList = (List) value;
    if (valueList.size() != 9) {
      printReadInError(INVALID_RECIPE_ARRAYLIST + value.toString());
      return null;
    }
    ArrayList<Material> recipe = new ArrayList<>();
    for (Object materialObject : valueList) {
      recipe.add(getMaterial(materialObject));
    }
    for (int i = 0; i < recipe.size(); i++) {
      if (recipe.get(i) == null) {
        recipe.set(i, Material.AIR);
      }
    }
    return recipe;
  }

  private Map<String, Object> getMap(Object value) {
    return getMap(value, false);
  }

  private Map<String, Object> getMap(Object value, boolean silent) {
    if (!(value instanceof Map)) {
      if (!silent) {
        printReadInError();
      }
      return null;
    }
    for (Object object : ((Map) value).keySet()) {
      if (!(object instanceof String)) {
        if (!silent) {
          printReadInError();
        }
        return null;
      }
    }
    return (Map<String, Object>) value;
  }

  private List getList(Object value) {
    if (!(value instanceof List)) {
      printReadInError();
      return null;
    }
    return (List) value;
  }

  private Enchantment getEnchantment(Object value) {
    Enchantment enchantment = EnchantmentWrapper.getByKey(
        NamespacedKey.minecraft(value.toString().toLowerCase()));
    if (enchantment == null) {
      printReadInError(INVALID_ENCHANTMENT + value.toString());
      return null;
    }
    return enchantment;
  }

  private PotionType getPotionType(Object value) {
    PotionType potionType = PotionType.valueOf(value.toString().toUpperCase());
    if (potionType == null) {
      printReadInError(INVALID_POTION_TYPE + value.toString());
      return null;
    }
    return potionType;
  }

  private PotionEffectType getEffectType(Object value) {
    PotionEffectType effectType = PotionEffectType.getByName(value.toString().toUpperCase());
    if (effectType == null) {
      printReadInError(INVALID_EFFECT_TYPE + value.toString());
      return null;
    }
    return effectType;
  }

  private Color getPotionColor(Object value) {
    String colorString = value.toString();
    colorString = colorString.substring(1, colorString.length() - 1);
    List<String> RGB = Arrays.asList(colorString.trim().split(","));
    int red = 0;
    int green = 0;
    int blue = 0;
    if (RGB.size() == 3) {
      red = Integer.parseInt(RGB.get(0));
      green = Integer.parseInt(RGB.get(1));
      blue = Integer.parseInt(RGB.get(2));
    } else {
      printReadInError(INVALID_POTION_COLOR);
      return null;
    }
    return Color.fromRGB(red, green, blue);
  }

  private void printReadInError() {
    FrpgPrint.print(IMPROPER_CONFIG + configPath);
  }

  private void printReadInError(String extraMessage) {
    FrpgPrint.print(IMPROPER_CONFIG + configPath + " (" + extraMessage + ")");
  }

  private Object getOnlyKey(Map map) {
    Object mapKey = null;
    if (map.keySet().size() > 1) {
      printReadInError();
    }
    for (Object key : map.keySet()) { //We assume the keyset is size one
      mapKey = key;
    }
    return mapKey;
  }

  private Object getOnlyKey(Map map, String extraErrorMessage) {
    Object mapKey = null;
    if (map.keySet().size() > 1) {
      printReadInError(extraErrorMessage);
    }
    for (Object key : map.keySet()) { //We assume the keyset is size one
      mapKey = key;
    }
    return mapKey;
  }


  /**
   * Converts a "Table" (which is a list of a list of config nodes) to an easier to read form
   *
   * @param config the section of config where the table is located
   * @return A list of maps of config nodes, for each item in the "table" This acts as a list of
   * rows.
   */
  public List<Map<String, Object>> getConfigTableInformation(YamlConfiguration config) {
    List configTable = config.getList(configPath);
    if (configTable == null) {
      FrpgPrint.print(EXPECTED_LIST + configPath);
      return null;
    }
    return getConfigTableInformation(configTable);
  }

  /**
   * Converts a "Table" (which is a list of a list of config nodes) to an easier to read form
   *
   * @param configTable A "table" from a config file, a list of a list of nodes.
   * @return A list of maps of config nodes, for each item in the "table" This acts as a list of
   * rows.
   */
  public List<Map<String, Object>> getConfigTableInformation(Object configTable) {
    if (!(configTable instanceof List)) {
      FrpgPrint.print(EXPECTED_LIST + configPath);
      return null;
    }
    ArrayList<Map<String, Object>> tableInformation = new ArrayList<>();
    for (Object tableRow : (List) configTable) {
      tableInformation.add(convertListedTableRowToMap(tableRow));
    }
    return tableInformation;
  }


  /*
   * Converts a "Table" (which is a list of a list of config nodes) to an easier to read form
   * This version will not print where the config is if an errors occurs
   * @param configTable A "table" from a config file, a list of a list of nodes.
   * @return A list of maps of config nodes, for each item in the "table" This acts as a list of rows.
   */
    /*
    public List<Map<String,Object>> getConfigTableInformation(List configTable) {
        return getConfigTableInformation(configTable,UNKNOWN_CONFIG_LOCATION);
    }
    */

  /**
   * Converts List of config nodes to a Map format (removes extraneous lists used to compact lines
   * in .yml files)
   *
   * @param listOfConfigNodes List that contains config nodes (Maps)
   * @return A map format of the list
   */
  public Map<String, Object> convertListedTableRowToMap(Object listOfConfigNodes) {
    if (!(listOfConfigNodes instanceof List)) {
      FrpgPrint.print(EXPECTED_LIST + configPath);
      return null;
    }
    Map<String, Object> tableRow = new HashMap<>();
    if (!UtilityMethods.collectionOnlyContainsOneClass((List) listOfConfigNodes,
        Map.class)) { //Checks if the list only contains maps first
      FrpgPrint.print(EXPECTED_LIST_OF_MAPS + configPath);
      return null;
    }
    for (Object tableElementObject : (List) listOfConfigNodes) {
      Map tableElement = (Map) tableElementObject;
      for (Object key : tableElement.keySet()) {
        Object value = tableElement.get(key);
        if (value instanceof List) {
          if (!((List) value).isEmpty()) {
            if (UtilityMethods.collectionOnlyContainsOneClass((List) value, Map.class)) {
              value = convertListedTableRowToMap(value);
            }
          }
        }
        tableRow.put(key.toString(), value);
      }
    }
    return tableRow;
  }

}
