package mc.carlton.freerpg.playerAndServerInfo;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.ArrowTypes;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigLoad {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
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
    static Map<String,Double> specialMultiplier = new HashMap<>();
    static ArrayList<Integer> maxLevels = new ArrayList<>();
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
    static ArrayList<Object> alchemyInfo = new ArrayList<>();
    static Map<String,Map<String,Integer>> expMap = new HashMap<>();


    public void initializeConfig(){
        setConfig();
        setConfigData();
    }

    public void setConfig(){
        File f = new File(plugin.getDataFolder(),"config.yml");
        f.setReadable(true,false);
        f.setWritable(true,false);
        FileConfiguration config = YamlConfiguration.loadConfiguration(f);
        LanguagesYMLManager getFile = new LanguagesYMLManager();
        File f1 = getFile.inputStreamToFile(plugin.getResource("config.yml"),"TEMP_config.yml");
        f1.setReadable(true,false);
        f1.setWritable(true,false);
        FileConfiguration configTrue = YamlConfiguration.loadConfiguration(f1);
        if (!config.getKeys(true).equals(configTrue.getKeys(true))) {
            updateConfigYML();
        }
        f1.delete();
    }

    public void updateConfigYML() {
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        System.out.println("[FreeRPG] config.yml is not up to date or is missing a key");
        LanguagesYMLManager languagesYMLManager = new LanguagesYMLManager();
        languagesYMLManager.storeOldFile("config.yml");
        System.out.println("[FreeRPG] Old config.yml stored in /.../FreeRPG/OutdatedYMLFiles");

        plugin.saveResource("config.yml",true); //Saves default config

        //Loads the new files
        File f = new File(plugin.getDataFolder(),"config.yml");
        f.setReadable(true,false);
        f.setWritable(true,false);
        File outdatedYAML = new File(plugin.getDataFolder(),File.separator + "OutdatedYMLFiles");
        File f2 = new File(outdatedYAML,"OUTDATED_config.yml");
        f2.setReadable(true,false);
        f2.setWritable(true,false);
        FileConfiguration oldConfig = YamlConfiguration.loadConfiguration(f2);
        File f3 = new File(plugin.getDataFolder(),"TEMP_config.yml");
        f3.setReadable(true,false);
        f3.setWritable(true,false);
        FileConfiguration newConfig = YamlConfiguration.loadConfiguration(f3);
        boolean changeMade = false;

        ArrayList<String> lastLevelKeys = getAllLastLevelKeys(newConfig);
        for (String key : lastLevelKeys) {
            if (oldConfig.contains(key) && newConfig.contains(key)) {
                if (!oldConfig.get(key).equals(newConfig.get(key))) {
                    newConfig.set(key, oldConfig.get(key)); //Sets the new config to whatever data was in the old config
                    changeMade = true;
                }
            }
        }

        if (changeMade) {
            try {
                newConfig.save(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("[FreeRPG] config.yml updated succesfully");
    }

    public ArrayList<String> getAllLastLevelKeys(FileConfiguration configuration) {
        ArrayList<String> lastLevelKeys = new ArrayList<>();
        for (String key : configuration.getKeys(true)) {
            if (configuration.getConfigurationSection(key) == null) {
                lastLevelKeys.add(key);
            }
        }
        return lastLevelKeys;
    }

    public void setConfigData() {
        File f = new File(plugin.getDataFolder(),"config.yml");
        f.setReadable(true,false);
        f.setWritable(true,false);
        FileConfiguration config = YamlConfiguration.loadConfiguration(f);

        saveRunTimeData = config.getBoolean("general.saveRunTimeData");
        verboseRunTimeData = config.getBoolean("general.verboseRunTimeData");
        saveStatsTimer = config.getInt("general.saveStatsTimer");
        defaultLanguage = config.getString("general.defaultLanguage");
        allowExplosions = config.getBoolean("general.allowCustomExplosions");
        allowBuild = config.getBoolean("general.allowBuild");
        allowPvP = config.getBoolean("general.allowPvP");
        allowHurtAnimals = config.getBoolean("general.allowHurtAnimals");
        furnaceDeleteTimer = config.getInt("smelting.removePlayerFurnacesTimer");

        String[] labels = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting"};
        maxLevels.add(Integer.valueOf(config.getString("leveling.maxLevel")));
        allowedSkillsMap.put("global",config.getBoolean("global.skillAllowed"));
        for (String label : labels) {
            maxLevels.add(Integer.valueOf(config.getString(label+".maxLevel")));
            allowedSkillsMap.put(label,config.getBoolean(label+".skillAllowed"));
            allowedSkillGainEXPMap.put(label,config.getBoolean(label+".expDrops.enableEXPDrops"));
        }

        soulsInfo.add(Integer.valueOf(config.getString("souls.startingSouls")));
        soulsInfo.add(Integer.valueOf(config.getString("souls.refundCost")));

        multipliers.add(Double.valueOf(config.getString("multipliers.globalMultiplier")));
        multipliers.add(Double.valueOf(config.getString("multipliers.diggingMultiplier")));
        multipliers.add(Double.valueOf(config.getString("multipliers.woodcuttingMultiplier")));
        multipliers.add(Double.valueOf(config.getString("multipliers.miningMultiplier")));
        multipliers.add(Double.valueOf(config.getString("multipliers.farmingMultiplier")));
        multipliers.add(Double.valueOf(config.getString("multipliers.fishingMultiplier")));
        multipliers.add(Double.valueOf(config.getString("multipliers.archeryMultiplier")));
        multipliers.add(Double.valueOf(config.getString("multipliers.beastMasteryMultiplier")));
        multipliers.add(Double.valueOf(config.getString("multipliers.swordsmanshipMultiplier")));
        multipliers.add(Double.valueOf(config.getString("multipliers.defenseMultiplier")));
        multipliers.add(Double.valueOf(config.getString("multipliers.axeMasteryMultiplier")));
        multipliers.add(Double.valueOf(config.getString("multipliers.repairMultiplier")));
        multipliers.add(Double.valueOf(config.getString("multipliers.agilityMultiplier")));
        multipliers.add(Double.valueOf(config.getString("multipliers.alchemyMultiplier")));
        multipliers.add(Double.valueOf(config.getString("multipliers.smeltingMultiplier")));
        multipliers.add(Double.valueOf(config.getString("multipliers.enchantingMultiplier")));

        tokensInfo.add(Double.valueOf(config.getString("tokens.automaticPassiveUpgradesPerLevel")));
        tokensInfo.add(Double.valueOf(config.getString("tokens.levelsPerPassiveToken")));
        tokensInfo.add(Double.valueOf(config.getString("tokens.levelPerSkillToken")));
        tokensInfo.add(Double.valueOf(config.getString("tokens.levelsPerGlobalToken")));
        tokensInfo.add(Double.valueOf(config.getString("tokens.startingPassiveTokens")));
        tokensInfo.add(Double.valueOf(config.getString("tokens.startingSkillTokens")));
        tokensInfo.add(Double.valueOf(config.getString("tokens.startingGlobalTokens")));
        tokensInfo.add(Double.valueOf(config.getString("tokens.skillTokenToPassiveTokenConversion")));
        tokensInfo.add(Double.valueOf(config.getString("tokens.globalTokenToEXPbuff")));
        tokensInfo.add(Double.valueOf(config.getString("tokens.passiveRightClickInvestment")));
        tokensInfo.add(Double.valueOf(config.getString("tokens.passiveShiftClickInvestment")));

        levelingInfo.add(Double.valueOf(config.getString("leveling.maxLevel")));
        levelingInfo.add(Double.valueOf(config.getString("leveling.exponentialGrowthFactor")));
        levelingInfo.add(Double.valueOf(config.getString("leveling.exponentialReferenceLevel")));
        levelingInfo.add(Double.valueOf(config.getString("leveling.exponentialReferenceEXP")));
        levelingInfo.add(Double.valueOf(config.getString("leveling.levelBeginLinear")));
        levelingInfo.add(Double.valueOf(config.getString("leveling.LinearEXPperLevel")));

        for (int i = 1; i<= 9; i++) {
            diggingInfo.add(Material.matchMaterial(config.getString("digging.drops.drop"+i+"Name")));
            diggingInfo.add(Integer.valueOf(config.getString("digging.drops.drop"+i+"Amount")));
        }
        for (int i = 10; i<=15;i++) {
            diggingInfo.add(Material.matchMaterial(config.getString("digging.drops.drop"+i+"Name")));
            diggingInfo.add(Integer.valueOf(config.getString("digging.drops.drop"+i+"Amount")));
            diggingInfo.add(Double.valueOf(config.getString("digging.drops.drop"+i+"BaseChance")));
        }

        for (int i = 1; i<=5 ; i++) {
            woodcuttingInfo.add(Material.matchMaterial(config.getString("woodcutting.drops.leavesDrop"+i+"Name")));
            woodcuttingInfo.add(Integer.valueOf(config.getString("woodcutting.drops.leavesDrop"+i+"Amount")));
            woodcuttingInfo.add(Double.valueOf(config.getString("woodcutting.drops.leavesDrop"+i+"Chance")));
        }

        fishingInfoBaseChances.add(Double.valueOf(config.getString("fishing.drops.tier1_baseChance")));
        fishingInfoBaseChances.add(Double.valueOf(config.getString("fishing.drops.tier2_baseChance")));
        fishingInfoBaseChances.add(Double.valueOf(config.getString("fishing.drops.tier3_baseChance")));
        fishingInfoBaseChances.add(Double.valueOf(config.getString("fishing.drops.tier4_baseChance")));
        fishingInfoBaseChances.add(Double.valueOf(config.getString("fishing.drops.tier5_baseChance")));

        fishingInfoEnchants.add(Integer.valueOf(config.getString("fishing.drops.tier1_enchantedArmor")));
        fishingInfoEnchants.add(Integer.valueOf(config.getString("fishing.drops.tier2_enchantedArmor")));
        fishingInfoEnchants.add(Integer.valueOf(config.getString("fishing.drops.tier3_enchantedArmor")));
        fishingInfoEnchants.add(Integer.valueOf(config.getString("fishing.drops.tier4_enchantedArmor")));
        fishingInfoEnchants.add(Integer.valueOf(config.getString("fishing.drops.tier5_enchantedArmor")));

        for (int i = 1; i<=5; i++) {
            for (int j = 1; j<=4; j++) {
                fishingInfo.add(Material.matchMaterial(config.getString("fishing.drops.tier"+i+"_drop"+j+"Name")));
                fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier"+i+"_drop"+j+"Amount")));
                fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier"+i+"_drop"+j+"Random")));
            }
        }

        fishingInfoHotRod.add(Material.matchMaterial(config.getString("fishing.drops.tier1_drop1Name_HotRod")));
        fishingInfoHotRod.add(Integer.valueOf(config.getString("fishing.drops.tier1_drop1Amount_HotRod")));
        fishingInfoHotRod.add(Integer.valueOf(config.getString("fishing.drops.tier1_drop1Random_HotRod")));
        fishingInfoHotRod.add(Material.matchMaterial(config.getString("fishing.drops.tier1_drop2Name_HotRod")));
        fishingInfoHotRod.add(Integer.valueOf(config.getString("fishing.drops.tier1_drop2Amount_HotRod")));
        fishingInfoHotRod.add(Integer.valueOf(config.getString("fishing.drops.tier1_drop2Random_HotRod")));
        fishingInfoHotRod.add(Material.matchMaterial(config.getString("fishing.drops.tier2_drop1Name_HotRod")));
        fishingInfoHotRod.add(Integer.valueOf(config.getString("fishing.drops.tier2_drop1Amount_HotRod")));
        fishingInfoHotRod.add(Integer.valueOf(config.getString("fishing.drops.tier2_drop1Random_HotRod")));
        fishingInfoHotRod.add(Material.matchMaterial(config.getString("fishing.drops.tier2_drop3Name_HotRod")));
        fishingInfoHotRod.add(Integer.valueOf(config.getString("fishing.drops.tier2_drop3Amount_HotRod")));
        fishingInfoHotRod.add(Integer.valueOf(config.getString("fishing.drops.tier2_drop3Random_HotRod")));
        fishingInfoHotRod.add(Material.matchMaterial(config.getString("fishing.drops.tier2_drop4Name_HotRod")));
        fishingInfoHotRod.add(Integer.valueOf(config.getString("fishing.drops.tier2_drop4Amount_HotRod")));
        fishingInfoHotRod.add(Integer.valueOf(config.getString("fishing.drops.tier2_drop4Random_HotRod")));

        //Alchemy Info
        for (int i = 1; i<= 5; i++) {
            alchemyInfo.add(PotionEffectType.getByName(config.getString("alchemy.customPotions.potionType"+i)));
            alchemyInfo.add(Material.matchMaterial(config.getString("alchemy.customPotions.potionIngredient"+i)));
            alchemyInfo.add(config.getInt("alchemy.customPotions.potionDuration"+i));
            alchemyInfo.add(getColorFromString(config.getString("alchemy.customPotions.potionColor"+i)));
        }
        for (int i = 1; i<= 5; i++) {
            alchemyInfo.add(PotionType.valueOf(config.getString("alchemy.craftablePotions.potionType"+i)));
            alchemyInfo.add(Material.matchMaterial(config.getString("alchemy.craftablePotions.potionIngredient"+i)));
        }

        //Enchanting Bottle info
        getEXPFromEnchantingBottles = config.getBoolean("enchanting.gainEXPfromEnchantingBottles");

        //EXP Info
        for (String label : labels) {
            Map<String,Integer> skillExpMap = new HashMap<>();
            ConfigurationSection skillExpDrops = config.getConfigurationSection(label+".expDrops");
            for (String id : skillExpDrops.getKeys(false)) {
                if (!id.equalsIgnoreCase("enableEXPDrops")) {
                    skillExpMap.put(id,config.getInt(label+".expDrops." + id));
                }
            }
            expMap.put(label,skillExpMap);
        }

        //Special Multipliers
        specialMultiplier.put("megaDigEXPMultiplier",config.getDouble("digging.megaDigEXPMultiplier"));
        specialMultiplier.put("superBaitEXPMultiplier",config.getDouble("fishing.superBaitEXPMultiplier"));
        specialMultiplier.put("timberEXPMultiplier",config.getDouble("woodcutting.timberEXPMultiplier"));

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
    public ArrayList<Integer> getMaxLevels(){
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
    public  ArrayList<Object> getAlchemyInfo() { return alchemyInfo; }
    public Map<String,Map<String,Integer>> getExpMap() {return expMap;}
    public Map<String,Integer> getExpMapForSkill(String skillName) {
        if (expMap.containsKey(skillName)) {
            return expMap.get(skillName);
        }
        return null;
    }
    public Map<String,Double> getSpecialMultiplier(){return specialMultiplier;}
}
