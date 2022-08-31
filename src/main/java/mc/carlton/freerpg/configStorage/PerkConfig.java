package mc.carlton.freerpg.configStorage;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mc.carlton.freerpg.customContainers.CustomContainerImporter;
import mc.carlton.freerpg.customContainers.collections.CustomEffect;
import mc.carlton.freerpg.skillAndPerkInfo.LowestLevelInfo;
import mc.carlton.freerpg.skillAndPerkInfo.PassivePerkInfo;
import mc.carlton.freerpg.skillAndPerkInfo.PerkInfo;
import mc.carlton.freerpg.skillAndPerkInfo.SkillPerkInfo;
import mc.carlton.freerpg.skillAndPerkInfo.SkillPerkLevelInfo;
import mc.carlton.freerpg.skillAndPerkInfo.SkillTreeInfo;
import mc.carlton.freerpg.skills.SkillName;
import mc.carlton.freerpg.utilities.UtilityMethods;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class PerkConfig {

  private static final List<String> DROP_TABLE_KEYS = Arrays.asList(
      new String[]{"dropTable", "dropsAdded"});
  private static final List<String> MOB_DROP_TABLE_KEYS = Arrays.asList(
      new String[]{"mobDropTable", "entityDropTable"});
  private static final List<String> MOB_LIST_KEYS = Arrays.asList(
      new String[]{"mobList", "entityList", "entityTypeList"});
  private static final List<String> MATERIAL_LIST_KEYS = Arrays.asList(
      new String[]{"itemList", "blockList"});
  private static final List<String> DAMAGE_LIST_KEYS = Arrays.asList(
      new String[]{"damageTypeList", "damageCauseList"});
  private static final List<String> CUSTOM_RECIPE_LIST_KEYS = Arrays.asList(
      new String[]{"recipes", "customRecipes"});
  private static final List<String> CUSTOM_DROP_MAP_KEYS = Arrays.asList(
      new String[]{"dropChanges", "changedDrops"});
  private static final List<String> POTION_DATA_LIST_KEYS = Arrays.asList(
      new String[]{"potionDataList", "potionTypeList"});
  private static final List<String> CUSTOM_POTION_KEYS = Arrays.asList(
      new String[]{"decreasingTiers", "increasingTiers"});
  private static final List<String> CUSTOM_EFFECTS_GIVEN_KEYS = Arrays.asList(
      new String[]{"effectsGiven", "customEffect"});
  private static final List<String> SPECIAL_KEYS = Arrays.asList(
      new String[]{"decreasingTiers", "increasingTiers", "affectedMobs", "added-enchantmentList",
          "changedEffects"});
  private static final List<String> IGNORED_KEYS = Arrays.asList(
      new String[]{"effectChance", "activationChance", "enabled", "startingValue", "allowedRange",
          "changePerLevel"});
  private static final String ENABLED = "enabled";
  private static final String EFFECT_CHANCE = "effectChance";
  static private Map<SkillName, SkillTreeInfo> allPerkInfo = new HashMap<>();

  static public void readInAllPerkInfo(FileConfiguration config) {
    for (String skillNameString : config.getKeys(false)) {
      SkillName skillName = SkillName.matchSkillName(skillNameString);
      if (skillName != null) {
        allPerkInfo.put(skillName,
            getSkillTreeInfoFromConfig(skillName, config.getConfigurationSection(skillNameString)));
      }
    }
  }

  static public void readInAllPerkInfo(File f) {
    YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
    readInAllPerkInfo(config);
  }

  static private SkillTreeInfo getSkillTreeInfoFromConfig(SkillName skillName,
      ConfigurationSection configSection) {
    SkillTreeInfo skillTreeInfo = new SkillTreeInfo(skillName);
    for (String skillIdString : configSection.getKeys(false)) {
      skillTreeInfo.addPerkInfo(skillIdString,
          getSkillPerkInfoFromConfig(configSection.getConfigurationSection(skillIdString)));
    }
    return skillTreeInfo;
  }

  static private PerkInfo getSkillPerkInfoFromConfig(ConfigurationSection configSection) {
    if (configSection.getName().contains("passive")) { //Passive Skill
      PassivePerkInfo passivePerkInfo = new PassivePerkInfo();
      passivePerkInfo.setMinLevel(0);
      passivePerkInfo.setMaxLevel(getMaxPassivePerkLevel(configSection));
      passivePerkInfo.setEnabled(configSection.getBoolean(ENABLED));
      for (String key : configSection.getKeys(false)) {
        addNodeInformation(passivePerkInfo, configSection, key);
      }
      return passivePerkInfo;
    } else { //Regular Skill
      SkillPerkInfo skillPerkInfo = new SkillPerkInfo();
      skillPerkInfo.setEnabled(configSection.getBoolean(ENABLED));
      int minLevel = Integer.MAX_VALUE;
      int maxLevel = Integer.MIN_VALUE;
      for (String key : configSection.getKeys(
          false)) { //Get each level information, also determine min and max levels
        String[] splitKey = key.split("-");
        if (splitKey.length == 2 && splitKey[0].equalsIgnoreCase("level")) { //Level information
          int level = Integer.valueOf(splitKey[1]);
          skillPerkInfo.addSkillPerkInfo(level,
              getSkillPerkLevelFromConfig(configSection.getConfigurationSection(key),
                  level)); //Adds the skillPerkInfo
          if (level < minLevel) {
            minLevel = level;
          }
          if (level > maxLevel) {
            maxLevel = level;
          }
        }
      }
      skillPerkInfo.setMaxLevel(maxLevel);
      skillPerkInfo.setMinLevel(minLevel);
      return skillPerkInfo;
    }
  }

  static private int getMaxPassivePerkLevel(ConfigurationSection configSection) {
    double startingValue = configSection.getDouble("startingValue");
    double changePerLevel = configSection.getDouble("changePerLevel");
    List<Object> allowedRange = (List<Object>) configSection.getList("allowedRange");
    double lowerBound = parseDoubleWithInfinity(allowedRange.get(0).toString());
    double upperBound = parseDoubleWithInfinity(allowedRange.get(1).toString());
    if (changePerLevel > 0) {
      return (int) Math.max(Math.ceil(upperBound - startingValue / changePerLevel), 0);
    } else if (changePerLevel < 0) {
      return (int) Math.max(Math.ceil(lowerBound - startingValue / changePerLevel), 0);
    } else { //Change per level is 0, so the max level is 0
      return 0;
    }
  }

  static private double parseDoubleWithInfinity(String doubleString) {
    if (doubleString.equalsIgnoreCase("INF")) {
      return Double.MAX_VALUE;
    } else if (doubleString.equalsIgnoreCase("-INF")) {
      return Double.MIN_VALUE;
    } else {
      return Double.valueOf(doubleString);
    }
  }

  static private void addNodeInformation(LowestLevelInfo lowLevelInformationHolder,
      ConfigurationSection configurationSection, String key) {
    if (!UtilityMethods.stringCollectionContainsIgnoreCase(IGNORED_KEYS, key)) {
      //Some keys are ignored because they've already been handled or will be handled another way
      lowLevelInformationHolder.addInfo(key, getNodeInformation(configurationSection, key));
    }
  }

  static private SkillPerkLevelInfo getSkillPerkLevelFromConfig(ConfigurationSection configSection,
      int level) {
    SkillPerkLevelInfo skillPerkLevelInfo = new SkillPerkLevelInfo(level);
    for (String key : configSection.getKeys(false)) {
      addNodeInformation(skillPerkLevelInfo, configSection, key);
    }
    return skillPerkLevelInfo;
  }

  static private Object getNodeInformation(ConfigurationSection configSection,
      String localConfigNodeName) {
    final String PATH = configSection.getCurrentPath() + "." + localConfigNodeName;
    if (!configSection.contains(localConfigNodeName)) {
      return null;
    }
    Object configInformation = configSection.get(localConfigNodeName);

    //TODO: Make CustomBrewables behave like CustomRecipes, change the config to be more comprehensive
    //      Other things to think about:
    //          - Experience for crafting things
    CustomContainerImporter customContainerImporter = new CustomContainerImporter(PATH);
    if (UtilityMethods.stringContainsIgnoreCase(localConfigNodeName, MOB_DROP_TABLE_KEYS)) {
      //This must be checked before DROP_TABLE_KEYS, because any MobDropTable node contains the one of the DROP_TABLE_KEYS
      return customContainerImporter.getMobDropTable(configInformation);
    } else if (UtilityMethods.stringContainsIgnoreCase(localConfigNodeName, DROP_TABLE_KEYS)) {
      return customContainerImporter.getDropTable(configInformation);
    } else if (UtilityMethods.stringContainsIgnoreCase(localConfigNodeName, MOB_LIST_KEYS)) {
      return customContainerImporter.getEntityTypeList(configInformation);
    } else if (UtilityMethods.stringContainsIgnoreCase(localConfigNodeName, MATERIAL_LIST_KEYS)) {
      return customContainerImporter.getMaterialList(configInformation);
    } else if (UtilityMethods.stringContainsIgnoreCase(localConfigNodeName, DAMAGE_LIST_KEYS)) {
      return customContainerImporter.getDamageCauseList(configInformation);
    } else if (UtilityMethods.stringContainsIgnoreCase(localConfigNodeName,
        CUSTOM_RECIPE_LIST_KEYS)) {
      return customContainerImporter.getCustomRecipeList(configInformation, PATH);
    } else if (UtilityMethods.stringContainsIgnoreCase(localConfigNodeName, CUSTOM_DROP_MAP_KEYS)) {
      return customContainerImporter.getCustomItemMapping(configInformation);
    } else if (UtilityMethods.stringContainsIgnoreCase(localConfigNodeName,
        POTION_DATA_LIST_KEYS)) {
      return customContainerImporter.getPotionDataList(configInformation);
    } else if (UtilityMethods.stringContainsIgnoreCase(localConfigNodeName,
        CUSTOM_EFFECTS_GIVEN_KEYS)) {
      CustomEffect customEffect = customContainerImporter.getCustomEffect(configInformation);
      if (configSection.contains("effectChance")) {
        customEffect.setProbability(configSection.getDouble("effectChance", 1.0));
      }
      return customEffect;
    } else if (UtilityMethods.stringContainsIgnoreCase(localConfigNodeName, SPECIAL_KEYS)) {
      return customContainerImporter.convertListedTableRowToMap(configInformation);
    } else {
      return configInformation;
    }

  }

  public SkillTreeInfo getSkillTreeInfo(SkillName skillName) {
    if (allPerkInfo.containsKey(skillName)) {
      return allPerkInfo.get(skillName);
    }
    return null;
  }


}
