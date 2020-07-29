package mc.carlton.freerpg.playerAndServerInfo;

import mc.carlton.freerpg.FreeRPG;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class ConfigLoad {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
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


    public void setConfigData() {
        FileConfiguration config = plugin.getConfig();

        maxLevels.add(Integer.valueOf(config.getString("leveling.maxLevel")));
        maxLevels.add(Integer.valueOf(config.getString("digging.maxLevel")));
        maxLevels.add(Integer.valueOf(config.getString("woodcutting.maxLevel")));
        maxLevels.add(Integer.valueOf(config.getString("mining.maxLevel")));
        maxLevels.add(Integer.valueOf(config.getString("farming.maxLevel")));
        maxLevels.add(Integer.valueOf(config.getString("fishing.maxLevel")));
        maxLevels.add(Integer.valueOf(config.getString("archery.maxLevel")));
        maxLevels.add(Integer.valueOf(config.getString("beastMastery.maxLevel")));
        maxLevels.add(Integer.valueOf(config.getString("swordsmanship.maxLevel")));
        maxLevels.add(Integer.valueOf(config.getString("defense.maxLevel")));
        maxLevels.add(Integer.valueOf(config.getString("axeMastery.maxLevel")));
        maxLevels.add(Integer.valueOf(config.getString("repair.maxLevel")));
        maxLevels.add(Integer.valueOf(config.getString("agility.maxLevel")));
        maxLevels.add(Integer.valueOf(config.getString("alchemy.maxLevel")));
        maxLevels.add(Integer.valueOf(config.getString("smelting.maxLevel")));
        maxLevels.add(Integer.valueOf(config.getString("enchanting.maxLevel")));

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

        levelingInfo.add(Double.valueOf(config.getString("leveling.maxLevel")));
        levelingInfo.add(Double.valueOf(config.getString("leveling.exponentialGrowthFactor")));
        levelingInfo.add(Double.valueOf(config.getString("leveling.exponentialReferenceLevel")));
        levelingInfo.add(Double.valueOf(config.getString("leveling.exponentialReferenceEXP")));
        levelingInfo.add(Double.valueOf(config.getString("leveling.levelBeginLinear")));
        levelingInfo.add(Double.valueOf(config.getString("leveling.LinearEXPperLevel")));

        diggingInfo.add(Material.matchMaterial(config.getString("digging.drops.drop1Name")));
        diggingInfo.add(Integer.valueOf(config.getString("digging.drops.drop1Amount")));
        diggingInfo.add(Material.matchMaterial(config.getString("digging.drops.drop2Name")));
        diggingInfo.add(Integer.valueOf(config.getString("digging.drops.drop2Amount")));
        diggingInfo.add(Material.matchMaterial(config.getString("digging.drops.drop3Name")));
        diggingInfo.add(Integer.valueOf(config.getString("digging.drops.drop3Amount")));
        diggingInfo.add(Material.matchMaterial(config.getString("digging.drops.drop4Name")));
        diggingInfo.add(Integer.valueOf(config.getString("digging.drops.drop4Amount")));
        diggingInfo.add(Material.matchMaterial(config.getString("digging.drops.drop5Name")));
        diggingInfo.add(Integer.valueOf(config.getString("digging.drops.drop5Amount")));
        diggingInfo.add(Material.matchMaterial(config.getString("digging.drops.drop6Name")));
        diggingInfo.add(Integer.valueOf(config.getString("digging.drops.drop6Amount")));
        diggingInfo.add(Material.matchMaterial(config.getString("digging.drops.drop7Name")));
        diggingInfo.add(Integer.valueOf(config.getString("digging.drops.drop7Amount")));
        diggingInfo.add(Material.matchMaterial(config.getString("digging.drops.drop8Name")));
        diggingInfo.add(Integer.valueOf(config.getString("digging.drops.drop8Amount")));
        diggingInfo.add(Material.matchMaterial(config.getString("digging.drops.drop9Name")));
        diggingInfo.add(Integer.valueOf(config.getString("digging.drops.drop9Amount")));
        diggingInfo.add(Material.matchMaterial(config.getString("digging.drops.drop10Name")));
        diggingInfo.add(Integer.valueOf(config.getString("digging.drops.drop10Amount")));
        diggingInfo.add(Double.valueOf(config.getString("digging.drops.drop10BaseChance")));
        diggingInfo.add(Material.matchMaterial(config.getString("digging.drops.drop11Name")));
        diggingInfo.add(Integer.valueOf(config.getString("digging.drops.drop11Amount")));
        diggingInfo.add(Double.valueOf(config.getString("digging.drops.drop11BaseChance")));
        diggingInfo.add(Material.matchMaterial(config.getString("digging.drops.drop12Name")));
        diggingInfo.add(Integer.valueOf(config.getString("digging.drops.drop12Amount")));
        diggingInfo.add(Double.valueOf(config.getString("digging.drops.drop12BaseChance")));
        diggingInfo.add(Material.matchMaterial(config.getString("digging.drops.drop13Name")));
        diggingInfo.add(Integer.valueOf(config.getString("digging.drops.drop13Amount")));
        diggingInfo.add(Double.valueOf(config.getString("digging.drops.drop13BaseChance")));
        diggingInfo.add(Material.matchMaterial(config.getString("digging.drops.drop14Name")));
        diggingInfo.add(Integer.valueOf(config.getString("digging.drops.drop14Amount")));
        diggingInfo.add(Double.valueOf(config.getString("digging.drops.drop14BaseChance")));
        diggingInfo.add(Material.matchMaterial(config.getString("digging.drops.drop15Name")));
        diggingInfo.add(Integer.valueOf(config.getString("digging.drops.drop15Amount")));
        diggingInfo.add(Double.valueOf(config.getString("digging.drops.drop15BaseChance")));

        woodcuttingInfo.add(Material.matchMaterial(config.getString("woodcutting.drops.leavesDrop1Name")));
        woodcuttingInfo.add(Integer.valueOf(config.getString("woodcutting.drops.leavesDrop1Amount")));
        woodcuttingInfo.add(Double.valueOf(config.getString("woodcutting.drops.leavesDrop1Chance")));
        woodcuttingInfo.add(Material.matchMaterial(config.getString("woodcutting.drops.leavesDrop2Name")));
        woodcuttingInfo.add(Integer.valueOf(config.getString("woodcutting.drops.leavesDrop2Amount")));
        woodcuttingInfo.add(Double.valueOf(config.getString("woodcutting.drops.leavesDrop2Chance")));
        woodcuttingInfo.add(Material.matchMaterial(config.getString("woodcutting.drops.leavesDrop3Name")));
        woodcuttingInfo.add(Integer.valueOf(config.getString("woodcutting.drops.leavesDrop3Amount")));
        woodcuttingInfo.add(Double.valueOf(config.getString("woodcutting.drops.leavesDrop3Chance")));
        woodcuttingInfo.add(Material.matchMaterial(config.getString("woodcutting.drops.leavesDrop4Name")));
        woodcuttingInfo.add(Integer.valueOf(config.getString("woodcutting.drops.leavesDrop4Amount")));
        woodcuttingInfo.add(Double.valueOf(config.getString("woodcutting.drops.leavesDrop4Chance")));
        woodcuttingInfo.add(Material.matchMaterial(config.getString("woodcutting.drops.leavesDrop5Name")));
        woodcuttingInfo.add(Integer.valueOf(config.getString("woodcutting.drops.leavesDrop5Amount")));
        woodcuttingInfo.add(Double.valueOf(config.getString("woodcutting.drops.leavesDrop5Chance")));

        fishingInfoBaseChances.add(Double.valueOf(config.getString("fishing.drops.tier1_baseChance")));
        fishingInfoBaseChances.add(Double.valueOf(config.getString("fishing.drops.tier2_baseChance")));
        fishingInfoBaseChances.add(Double.valueOf(config.getString("fishing.drops.tier3_baseChance")));
        fishingInfoBaseChances.add(Double.valueOf(config.getString("fishing.drops.tier4_baseChance")));
        fishingInfoBaseChances.add(Double.valueOf(config.getString("fishing.drops.tier5_baseChance")));
        fishingInfo.add(Material.matchMaterial(config.getString("fishing.drops.tier1_drop1Name")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier1_drop1Amount")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier1_drop1Random")));
        fishingInfo.add(Material.matchMaterial(config.getString("fishing.drops.tier1_drop2Name")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier1_drop2Amount")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier1_drop2Random")));
        fishingInfo.add(Material.matchMaterial(config.getString("fishing.drops.tier1_drop3Name")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier1_drop3Amount")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier1_drop3Random")));
        fishingInfo.add(Material.matchMaterial(config.getString("fishing.drops.tier1_drop4Name")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier1_drop4Amount")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier1_drop4Random")));
        fishingInfo.add(Material.matchMaterial(config.getString("fishing.drops.tier2_drop1Name")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier2_drop1Amount")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier2_drop1Random")));
        fishingInfo.add(Material.matchMaterial(config.getString("fishing.drops.tier2_drop2Name")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier2_drop2Amount")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier2_drop2Random")));
        fishingInfo.add(Material.matchMaterial(config.getString("fishing.drops.tier2_drop3Name")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier2_drop3Amount")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier2_drop3Random")));
        fishingInfo.add(Material.matchMaterial(config.getString("fishing.drops.tier2_drop4Name")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier2_drop4Amount")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier2_drop4Random")));
        fishingInfo.add(Material.matchMaterial(config.getString("fishing.drops.tier3_drop1Name")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier3_drop1Amount")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier3_drop1Random")));
        fishingInfo.add(Material.matchMaterial(config.getString("fishing.drops.tier3_drop2Name")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier3_drop2Amount")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier3_drop2Random")));
        fishingInfo.add(Material.matchMaterial(config.getString("fishing.drops.tier3_drop3Name")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier3_drop3Amount")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier3_drop3Random")));
        fishingInfo.add(Material.matchMaterial(config.getString("fishing.drops.tier3_drop4Name")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier3_drop4Amount")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier3_drop4Random")));
        fishingInfo.add(Material.matchMaterial(config.getString("fishing.drops.tier4_drop1Name")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier4_drop1Amount")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier4_drop1Random")));
        fishingInfo.add(Material.matchMaterial(config.getString("fishing.drops.tier4_drop2Name")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier4_drop2Amount")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier4_drop2Random")));
        fishingInfo.add(Material.matchMaterial(config.getString("fishing.drops.tier4_drop3Name")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier4_drop3Amount")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier4_drop3Random")));
        fishingInfo.add(Material.matchMaterial(config.getString("fishing.drops.tier4_drop4Name")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier4_drop4Amount")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier4_drop4Random")));
        fishingInfo.add(Material.matchMaterial(config.getString("fishing.drops.tier5_drop1Name")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier5_drop1Amount")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier5_drop1Random")));
        fishingInfo.add(Material.matchMaterial(config.getString("fishing.drops.tier5_drop2Name")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier5_drop2Amount")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier5_drop2Random")));
        fishingInfo.add(Material.matchMaterial(config.getString("fishing.drops.tier5_drop3Name")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier5_drop3Amount")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier5_drop3Random")));
        fishingInfo.add(Material.matchMaterial(config.getString("fishing.drops.tier5_drop4Name")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier5_drop4Amount")));
        fishingInfo.add(Integer.valueOf(config.getString("fishing.drops.tier5_drop4Random")));
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
        fishingInfoEnchants.add(Integer.valueOf(config.getString("fishing.drops.tier1_enchantedArmor")));
        fishingInfoEnchants.add(Integer.valueOf(config.getString("fishing.drops.tier2_enchantedArmor")));
        fishingInfoEnchants.add(Integer.valueOf(config.getString("fishing.drops.tier3_enchantedArmor")));
        fishingInfoEnchants.add(Integer.valueOf(config.getString("fishing.drops.tier4_enchantedArmor")));
        fishingInfoEnchants.add(Integer.valueOf(config.getString("fishing.drops.tier5_enchantedArmor")));
    }

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
}
