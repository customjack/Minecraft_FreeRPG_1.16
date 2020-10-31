package mc.carlton.freerpg.serverInfo;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.CustomPotion;
import mc.carlton.freerpg.gameTools.CustomRecipe;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.io.File;
import java.util.*;

public class ConfigLoad {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    static int playerStatFilesLoadedInOnStartup;
    static double basePlayerHP;
    static boolean saveRunTimeData;
    static boolean verboseRunTimeData;
    static int saveStatsTimer;
    static String defaultLanguage;
    static boolean allowExplosions;
    static boolean allowBuild;
    static boolean allowPvP;
    static boolean allowHurtAnimals;
    static int furnaceDeleteTimer;
    static boolean getEXPFromEnchantingBottles;
    static boolean trackFewerBlocks;
    static boolean flamePickGiveXP;
    static boolean shiftRightClickInvestAll;
    static int veinMinerMaxBreakSize;
    static int superBaitCooldown;
    static boolean leaderboardDyanmicUpdate;
    static int leaderboardUpdateTimer;
    static boolean preventUnsafeRepair;
    static boolean preventUnsafeSalvage;
    static boolean earnExperiencePastMaxLevel;
    static double agilityMinSprintTimeForExperience;
    static int maxLevelForBeginnerMessage;
    static String beginnerLevelUpMessage;
    static Map<String,Double> specialMultiplier = new HashMap<>();
    static Map<String,Integer> maxLevels = new HashMap<>();
    static ArrayList<Integer> soulsInfo = new ArrayList<>();
    static ArrayList<Double> multipliers = new ArrayList<>();
    static ArrayList<Double> tokensInfo = new ArrayList<>();
    static ArrayList<Double> levelingInfo = new ArrayList<>();
    static ArrayList<Object> diggingInfo = new ArrayList<>();
    static ArrayList<Object> woodcuttingInfo = new ArrayList<>();
    static ArrayList<Object> fishingInfo = new ArrayList<>();
    static ArrayList<Object> fishingInfoBaseChances = new ArrayList<>();
    static ArrayList<Object> fishingInfoHotRod = new ArrayList<>();
    static ArrayList<Object> fishingInfoEnchants = new ArrayList<>();
    static Map<String,Boolean> allowedSkillsMap = new HashMap<>();
    static Map<String,Boolean> allowedSkillGainEXPMap = new HashMap<>();
    static Map<String, CustomPotion> alchemyInfo = new HashMap<>();
    static Map<String,Map<String,Integer>> expMap = new HashMap<>();
    static Map<String,CustomRecipe> craftingRecipes = new HashMap<>();
    static HashSet<Material> veinMinerBlocks = new HashSet<>();
    static ArrayList<Integer> timberBreakLimits = new ArrayList<>();
    static Map<String,Integer> abilityCooldowns = new HashMap<>();
    static Map<String,Double> spawnerEXPMultipliers = new HashMap<>();
    static Map<String,Double> mobFarmEXPMultipliers = new HashMap<>();
    static Map<String,Double> durabilityModifiers = new HashMap<>();


    public void initializeConfig(){
        setConfigData();
    }


    public void setConfigData() {
        File f = new File(plugin.getDataFolder(),"config.yml");
        f.setReadable(true,false);
        f.setWritable(true,false);
        FileConfiguration config = YamlConfiguration.loadConfiguration(f);
        File f1 = new File(plugin.getDataFolder(),"advancedConfig.yml");
        f.setReadable(true,false);
        f.setWritable(true,false);
        FileConfiguration advancedConfig = YamlConfiguration.loadConfiguration(f1);

        //Useful Label Groups:
        String[] labels = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting"};
        String[] combatLabels = {"archery","beastMastery","swordsmanship","defense","axeMastery"};

        //General Config and Config that has no real category
        defaultLanguage = config.getString("general.defaultLanguage");
        basePlayerHP = config.getDouble("general.playerBaseHP");
        playerStatFilesLoadedInOnStartup = advancedConfig.getInt("general.playerStatFilesLoadedInOnStartup");
        saveRunTimeData = advancedConfig.getBoolean("general.saveRunTimeData");
        verboseRunTimeData = advancedConfig.getBoolean("general.verboseRunTimeData");
        saveStatsTimer = advancedConfig.getInt("general.saveStatsTimer");
        allowExplosions = advancedConfig.getBoolean("general.allowCustomExplosions");
        allowBuild = advancedConfig.getBoolean("general.allowBuild");
        allowPvP = advancedConfig.getBoolean("general.allowPvP");
        allowHurtAnimals = advancedConfig.getBoolean("general.allowHurtAnimals");
        furnaceDeleteTimer = advancedConfig.getInt("smelting.removePlayerFurnacesTimer");
        trackFewerBlocks = advancedConfig.getBoolean("general.trackFewerBlocks");
        leaderboardDyanmicUpdate = advancedConfig.getBoolean("general.leaderboardDynamicUpdate");
        leaderboardUpdateTimer = advancedConfig.getInt("general.leaderboardUpdateTimer");
        getEXPFromEnchantingBottles = advancedConfig.getBoolean("enchanting.gainEXPfromEnchantingBottles");
        flamePickGiveXP = advancedConfig.getBoolean("smelting.flamePickGiveMinecraftXP");
        preventUnsafeRepair = advancedConfig.getBoolean("repair.preventRepairOfItemsWithUnsafeEnchantments");
        preventUnsafeSalvage = advancedConfig.getBoolean("repair.preventSalvageOfItemsWithUnsafeEnchantments");
        maxLevelForBeginnerMessage = advancedConfig.getInt("leveling.beginnerLevelUpMessageMaxLevel");
        beginnerLevelUpMessage = advancedConfig.getString("leveling.customBegginerLevelUpMessage");
        List<String> veinMinerBlockStrings = advancedConfig.getStringList("mining.veinMinerBlocks");
        for (String matString : veinMinerBlockStrings) {
            if (Material.matchMaterial(matString) != null)
            veinMinerBlocks.add(Material.matchMaterial(matString));
        }
        veinMinerMaxBreakSize = advancedConfig.getInt("mining.veinMinerMaximumBlocksBroken");
        timberBreakLimits.add(advancedConfig.getInt("woodcutting.timberMaxBreakInitial"));
        timberBreakLimits.add(advancedConfig.getInt("woodcutting.timberMaxBreakUpgraded"));
        shiftRightClickInvestAll = advancedConfig.getBoolean("tokens.passiveShiftClickAndRightClickInvestAll");
        superBaitCooldown = advancedConfig.getInt("fishing.superBaitCooldown");
        earnExperiencePastMaxLevel = config.getBoolean("leveling.earnExperiencePastMaxLevel");
        agilityMinSprintTimeForExperience = advancedConfig.getDouble("agility.sprintTimeThreshold");


        maxLevels.put("global",Integer.valueOf(config.getString("leveling.maxLevel")));
        allowedSkillsMap.put("global",config.getBoolean("global.skillAllowed"));
        for (String label : labels) {
            maxLevels.put(label,Integer.valueOf(config.getString(label+".maxLevel")));
            allowedSkillsMap.put(label,config.getBoolean(label+".skillAllowed"));
            abilityCooldowns.put(label,config.getInt(label+".abilityCooldown"));
            allowedSkillGainEXPMap.put(label,advancedConfig.getBoolean(label+".expDrops.enableEXPDrops"));
        }
        abilityCooldowns.put("fishingRob",config.getInt(".robCooldown"));

        durabilityModifiers.put("megaDig",advancedConfig.getDouble("digging.megaDigDurabilityModifier"));
        durabilityModifiers.put("timber",advancedConfig.getDouble("woodcutting.timberDurabilityModifier"));
        durabilityModifiers.put("leafBlower",advancedConfig.getDouble("woodcutting.leafBlowerDurabilityModifier"));
        durabilityModifiers.put("veinMiner",advancedConfig.getDouble("mining.veinMinerDurabilityModifier"));
        durabilityModifiers.put("flamePick",advancedConfig.getDouble("smelting.smeltingDurabilityModifier"));


        //Config that is skill dependant (not used in all skills)
        for (String label : combatLabels) {
            spawnerEXPMultipliers.put(label,advancedConfig.getDouble(label+".spawnerMobsEXPMultiplier"));
            mobFarmEXPMultipliers.put(label,advancedConfig.getDouble(label+".mobFarmEXPMultiplier"));
        }

        soulsInfo.add(Integer.valueOf(advancedConfig.getString("souls.startingSouls")));
        soulsInfo.add(Integer.valueOf(advancedConfig.getString("souls.refundCost")));

        multipliers.add(Double.valueOf(config.getString("global.EXP_Multiplier")));
        for (String label : labels) {
            multipliers.add(config.getDouble(label+".EXP_Multiplier"));
        }

        tokensInfo.add(Double.valueOf(advancedConfig.getString("tokens.automaticPassiveUpgradesPerLevel")));
        tokensInfo.add(Double.valueOf(advancedConfig.getString("tokens.levelsPerPassiveToken")));
        tokensInfo.add(Double.valueOf(advancedConfig.getString("tokens.levelPerSkillToken")));
        tokensInfo.add(Double.valueOf(advancedConfig.getString("tokens.levelsPerGlobalToken")));
        tokensInfo.add(Double.valueOf(advancedConfig.getString("tokens.startingPassiveTokens")));
        tokensInfo.add(Double.valueOf(advancedConfig.getString("tokens.startingSkillTokens")));
        tokensInfo.add(Double.valueOf(advancedConfig.getString("tokens.startingGlobalTokens")));
        tokensInfo.add(Double.valueOf(advancedConfig.getString("tokens.skillTokenToPassiveTokenConversion")));
        tokensInfo.add(Double.valueOf(advancedConfig.getString("tokens.globalTokenToEXPbuff")));
        tokensInfo.add(Double.valueOf(advancedConfig.getString("tokens.passiveRightClickInvestment")));
        tokensInfo.add(Double.valueOf(advancedConfig.getString("tokens.passiveShiftClickInvestment")));

        levelingInfo.add(Double.valueOf(config.getString("leveling.maxLevel")));
        levelingInfo.add(Double.valueOf(advancedConfig.getString("leveling.exponentialGrowthFactor")));
        levelingInfo.add(Double.valueOf(advancedConfig.getString("leveling.exponentialReferenceLevel")));
        levelingInfo.add(Double.valueOf(advancedConfig.getString("leveling.exponentialReferenceEXP")));
        levelingInfo.add(Double.valueOf(advancedConfig.getString("leveling.levelBeginLinear")));
        levelingInfo.add(Double.valueOf(advancedConfig.getString("leveling.LinearEXPperLevel")));

        for (int i = 1; i<= 9; i++) {
            diggingInfo.add(Material.matchMaterial(advancedConfig.getString("digging.drops.drop"+i+"Name")));
            diggingInfo.add(Integer.valueOf(advancedConfig.getString("digging.drops.drop"+i+"Amount")));
        }
        for (int i = 10; i<=15;i++) {
            diggingInfo.add(Material.matchMaterial(advancedConfig.getString("digging.drops.drop"+i+"Name")));
            diggingInfo.add(Integer.valueOf(advancedConfig.getString("digging.drops.drop"+i+"Amount")));
            diggingInfo.add(Double.valueOf(advancedConfig.getString("digging.drops.drop"+i+"BaseChance")));
        }

        for (int i = 1; i<=5 ; i++) {
            woodcuttingInfo.add(Material.matchMaterial(advancedConfig.getString("woodcutting.drops.leavesDrop"+i+"Name")));
            woodcuttingInfo.add(Integer.valueOf(advancedConfig.getString("woodcutting.drops.leavesDrop"+i+"Amount")));
            woodcuttingInfo.add(Double.valueOf(advancedConfig.getString("woodcutting.drops.leavesDrop"+i+"Chance")));
        }

        fishingInfoBaseChances.add(Double.valueOf(advancedConfig.getString("fishing.drops.tier1_baseChance")));
        fishingInfoBaseChances.add(Double.valueOf(advancedConfig.getString("fishing.drops.tier2_baseChance")));
        fishingInfoBaseChances.add(Double.valueOf(advancedConfig.getString("fishing.drops.tier3_baseChance")));
        fishingInfoBaseChances.add(Double.valueOf(advancedConfig.getString("fishing.drops.tier4_baseChance")));
        fishingInfoBaseChances.add(Double.valueOf(advancedConfig.getString("fishing.drops.tier5_baseChance")));

        fishingInfoEnchants.add(Integer.valueOf(advancedConfig.getString("fishing.drops.tier1_enchantedArmor")));
        fishingInfoEnchants.add(Integer.valueOf(advancedConfig.getString("fishing.drops.tier2_enchantedArmor")));
        fishingInfoEnchants.add(Integer.valueOf(advancedConfig.getString("fishing.drops.tier3_enchantedArmor")));
        fishingInfoEnchants.add(Integer.valueOf(advancedConfig.getString("fishing.drops.tier4_enchantedArmor")));
        fishingInfoEnchants.add(Integer.valueOf(advancedConfig.getString("fishing.drops.tier5_enchantedArmor")));

        for (int i = 1; i<=5; i++) {
            for (int j = 1; j<=4; j++) {
                fishingInfo.add(Material.matchMaterial(advancedConfig.getString("fishing.drops.tier"+i+"_drop"+j+"Name")));
                fishingInfo.add(Integer.valueOf(advancedConfig.getString("fishing.drops.tier"+i+"_drop"+j+"Amount")));
                fishingInfo.add(Integer.valueOf(advancedConfig.getString("fishing.drops.tier"+i+"_drop"+j+"Random")));
            }
        }

        fishingInfoHotRod.add(Material.matchMaterial(advancedConfig.getString("fishing.drops.tier1_drop1Name_HotRod")));
        fishingInfoHotRod.add(Integer.valueOf(advancedConfig.getString("fishing.drops.tier1_drop1Amount_HotRod")));
        fishingInfoHotRod.add(Integer.valueOf(advancedConfig.getString("fishing.drops.tier1_drop1Random_HotRod")));
        fishingInfoHotRod.add(Material.matchMaterial(advancedConfig.getString("fishing.drops.tier1_drop2Name_HotRod")));
        fishingInfoHotRod.add(Integer.valueOf(advancedConfig.getString("fishing.drops.tier1_drop2Amount_HotRod")));
        fishingInfoHotRod.add(Integer.valueOf(advancedConfig.getString("fishing.drops.tier1_drop2Random_HotRod")));
        fishingInfoHotRod.add(Material.matchMaterial(advancedConfig.getString("fishing.drops.tier2_drop1Name_HotRod")));
        fishingInfoHotRod.add(Integer.valueOf(advancedConfig.getString("fishing.drops.tier2_drop1Amount_HotRod")));
        fishingInfoHotRod.add(Integer.valueOf(advancedConfig.getString("fishing.drops.tier2_drop1Random_HotRod")));
        fishingInfoHotRod.add(Material.matchMaterial(advancedConfig.getString("fishing.drops.tier2_drop3Name_HotRod")));
        fishingInfoHotRod.add(Integer.valueOf(advancedConfig.getString("fishing.drops.tier2_drop3Amount_HotRod")));
        fishingInfoHotRod.add(Integer.valueOf(advancedConfig.getString("fishing.drops.tier2_drop3Random_HotRod")));
        fishingInfoHotRod.add(Material.matchMaterial(advancedConfig.getString("fishing.drops.tier2_drop4Name_HotRod")));
        fishingInfoHotRod.add(Integer.valueOf(advancedConfig.getString("fishing.drops.tier2_drop4Amount_HotRod")));
        fishingInfoHotRod.add(Integer.valueOf(advancedConfig.getString("fishing.drops.tier2_drop4Random_HotRod")));

        //Alchemy Info
        for (int i = 1; i<= 5; i++) {
            CustomPotion customPotion = new CustomPotion();
            customPotion.setPotionEffectType(PotionEffectType.getByName(advancedConfig.getString("alchemy.customPotions.potionType"+i)));
            customPotion.setIngredient(Material.matchMaterial(advancedConfig.getString("alchemy.customPotions.potionIngredient"+i)));
            customPotion.setPotionDuration(advancedConfig.getInt("alchemy.customPotions.potionDuration"+i));
            customPotion.setColor(getColorFromString(advancedConfig.getString("alchemy.customPotions.potionColor"+i)));
            customPotion.setPotionName();
            alchemyInfo.put("customPotion"+i,customPotion);
        }

        //EXP Info
        for (String label : labels) {
            Map<String,Integer> skillExpMap = new HashMap<>();
            ConfigurationSection skillExpDrops = advancedConfig.getConfigurationSection(label+".expDrops");
            for (String id : skillExpDrops.getKeys(false)) {
                if (!id.equalsIgnoreCase("enableEXPDrops")) {
                    skillExpMap.put(id,advancedConfig.getInt(label+".expDrops." + id));
                }
            }
            expMap.put(label,skillExpMap);
        }

        //Special Multipliers
        specialMultiplier.put("megaDigEXPMultiplier",advancedConfig.getDouble("digging.megaDigEXPMultiplier"));
        specialMultiplier.put("superBaitEXPMultiplier",advancedConfig.getDouble("fishing.superBaitEXPMultiplier"));
        specialMultiplier.put("timberEXPMultiplier",advancedConfig.getDouble("woodcutting.timberEXPMultiplier"));

        //Crafting arrays
        for (int i = 1; i <= 5; i++) {
            CustomRecipe customRecipe = new CustomRecipe();
            customRecipe.setOutput(Material.matchMaterial(advancedConfig.getString("farming.crafting.recipeOutput"+i)));
            customRecipe.setOutputAmount(advancedConfig.getInt("farming.crafting.recipeOutputAmount"+i));
            List<String> recipeStrings = advancedConfig.getStringList("farming.crafting.recipe"+i);
            ArrayList<Material> recipeMaterials = new ArrayList<>();
            for (String item : recipeStrings) {
                recipeMaterials.add(Material.matchMaterial(item));
            }
            customRecipe.setRecipe(recipeMaterials);
            craftingRecipes.put("farming"+i,customRecipe);
        }
        for (int i = 1; i <= 10; i++) {
            CustomRecipe customRecipe = new CustomRecipe();
            customRecipe.setOutput(Material.matchMaterial(advancedConfig.getString("enchanting.crafting.recipeOutput"+i)));
            customRecipe.setOutputAmount(advancedConfig.getInt("enchanting.crafting.recipeOutputAmount"+i));
            String enchantType = advancedConfig.getString("enchanting.crafting.recipeEnchant"+i);
            if (!enchantType.equalsIgnoreCase("none")) {
                customRecipe.setEnchantment(EnchantmentWrapper.getByKey(NamespacedKey.minecraft(enchantType)));
                customRecipe.setEnchantmentLevel(advancedConfig.getInt("enchanting.crafting.recipeEnchantLevel"+i));
                customRecipe.setXPcraftCost(advancedConfig.getInt("enchanting.crafting.XPcostToCraft"+i));
            }
            List<String> recipeStrings = advancedConfig.getStringList("enchanting.crafting.recipe"+i);
            ArrayList<Material> recipeMaterials = new ArrayList<>();
            for (String item : recipeStrings) {
                recipeMaterials.add(Material.matchMaterial(item));
            }
            customRecipe.setRecipe(recipeMaterials);
            craftingRecipes.put("enchanting"+i,customRecipe);
        }
        for (int i = 1; i <= 5; i++) {
            CustomRecipe customRecipe = new CustomRecipe();
            String materialString = advancedConfig.getString("alchemy.crafting.recipeOutput"+i);
            customRecipe.setOutput(Material.matchMaterial(materialString));
            customRecipe.setOutputAmount(advancedConfig.getInt("alchemy.crafting.recipeOutputAmount"+i));
            if (materialString.equalsIgnoreCase("POTION")) {
                customRecipe.setPotionType(PotionType.valueOf(advancedConfig.getString("alchemy.crafting.recipePotionType"+i)));
                customRecipe.setExtended(advancedConfig.getBoolean("alchemy.crafting.recipePotionExtended"+i));
                customRecipe.setUpgraded(advancedConfig.getBoolean("alchemy.crafting.recipePotionUpgraded"+i));
            }
            List<String> recipeStrings = advancedConfig.getStringList("alchemy.crafting.recipe"+i);
            ArrayList<Material> recipeMaterials = new ArrayList<>();
            for (String item : recipeStrings) {
                recipeMaterials.add(Material.matchMaterial(item));
            }
            customRecipe.setRecipe(recipeMaterials);
            craftingRecipes.put("alchemy"+i,customRecipe);
        }

    }

    public Color getColorFromString(String colorString) {
        colorString = colorString.substring(1,colorString.length()-1);
        List<String> RGB = Arrays.asList(colorString.trim().split(","));
        int red = 0;
        int green = 0;
        int blue = 0;
        if (RGB.size() == 3) {
            red = Integer.parseInt(RGB.get(0));
            green = Integer.parseInt(RGB.get(1));
            blue = Integer.parseInt(RGB.get(2));
        }
        return Color.fromRGB(red,green,blue);
    }

    public int getSaveStatsTimer() {return  saveStatsTimer;}
    public String getDefaultLanguage() {return  defaultLanguage;}
    public boolean isAllowExplosions() {return allowExplosions;}
    public boolean isAllowBuild() {return allowBuild;}
    public boolean isAllowPvP() {return allowPvP;}
    public boolean isAllowHurtAnimals() {return allowHurtAnimals;}
    public Map<String,Integer> getMaxLevels(){
        return maxLevels;
    }
    public ArrayList<Integer> getSoulsInfo(){
        return soulsInfo;
    }
    public ArrayList<Double> getMultipliers(){
        return multipliers;
    }
    public ArrayList<Double> getTokensInfo(){
        return tokensInfo;
    }
    public ArrayList<Double> getLevelingInfo(){
        return levelingInfo;
    }
    public ArrayList<Object> getDiggingInfo(){
        return diggingInfo;
    }
    public ArrayList<Object> getWoodcuttingInfo(){
        return woodcuttingInfo;
    }
    public ArrayList<Object> getFishingInfo1(){
        return fishingInfo;
    }
    public ArrayList<Object> getFishingInfo2(){
        return fishingInfoBaseChances;
    }
    public ArrayList<Object> getFishingInfo3(){
        return fishingInfoHotRod;
    }
    public ArrayList<Object> getFishingInfo4(){
        return fishingInfoEnchants;
    }
    public boolean isGetEXPFromEnchantingBottles() {return getEXPFromEnchantingBottles;}
    public boolean isSaveRunTimeData() {return saveRunTimeData;}
    public boolean isVerboseRunTimeData() {return verboseRunTimeData;}
    public int getFurnaceDeleteTimer() {return furnaceDeleteTimer;}
    public Map<String,Boolean> getAllowedSkillsMap() {return allowedSkillsMap;}
    public Map<String,Boolean> getAllowedSkillGainEXPMap() {return allowedSkillGainEXPMap;}
    public  Map<String, CustomPotion> getAlchemyInfo() { return alchemyInfo; }
    public Map<String,Map<String,Integer>> getExpMap() {return expMap;}
    public Map<String,Integer> getExpMapForSkill(String skillName) {
        if (expMap.containsKey(skillName)) {
            return expMap.get(skillName);
        }
        return null;
    }
    public Map<String,Double> getSpecialMultiplier(){return specialMultiplier;}

    public Map<String,CustomRecipe> getCraftingRecipes() {
        return craftingRecipes;
    }

    public boolean isTrackFewerBlocks() {
        return trackFewerBlocks;
    }

    public boolean isFlamePickGiveXP() {
        return flamePickGiveXP;
    }

    public HashSet<Material> getVeinMinerBlocks() {
        return veinMinerBlocks;
    }

    public int getVeinMinerMaxBreakSize() {
        return veinMinerMaxBreakSize;
    }

    public ArrayList<Integer> getTimberBreakLimits() {
        return timberBreakLimits;
    }

    public Map<String, Integer> getAbilityCooldowns() {
        return abilityCooldowns;
    }

    public boolean isShiftRightClickInvestAll() {
        return shiftRightClickInvestAll;
    }

    public Map<String, Double> getMobFarmEXPMultipliers() {
        return mobFarmEXPMultipliers;
    }

    public Map<String, Double> getSpawnerEXPMultipliers() {
        return spawnerEXPMultipliers;
    }

    public int getPlayerStatFilesLoadedInOnStartup() {
        return playerStatFilesLoadedInOnStartup;
    }

    public double getBasePlayerHP() {
        return basePlayerHP;
    }

    public int getSuperBaitCooldown() {
        return superBaitCooldown;
    }

    public int getLeaderboardUpdateTimer() {
        return leaderboardUpdateTimer;
    }

    public boolean isLeaderboardDyanmicUpdate() {
        return leaderboardDyanmicUpdate;
    }

    public boolean isPreventUnsafeRepair() {
        return preventUnsafeRepair;
    }

    public boolean isPreventUnsafeSalvage() {
        return preventUnsafeSalvage;
    }

    public boolean isEarnExperiencePastMaxLevel() {
        return earnExperiencePastMaxLevel;
    }

    public double getAgilityMinSprintTimeForExperience() {
        return agilityMinSprintTimeForExperience;
    }
    public void setDefaultLanguage(String languageCode) {
        defaultLanguage = languageCode;
    }

    public Map<String, Double> getDurabilityModifiers() {
        return durabilityModifiers;
    }

    public int getMaxLevelForBeginnerMessage() {
        return maxLevelForBeginnerMessage;
    }

    public String getBeginnerLevelUpMessage() {
        return beginnerLevelUpMessage;
    }

}
