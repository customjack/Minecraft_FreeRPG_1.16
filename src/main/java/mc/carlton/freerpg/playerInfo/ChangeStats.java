package mc.carlton.freerpg.playerInfo;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.ActionBarMessages;
import mc.carlton.freerpg.gameTools.BossBarStorage;
import mc.carlton.freerpg.gameTools.LanguageSelector;
import mc.carlton.freerpg.guiEvents.MaxPassiveLevels;
import mc.carlton.freerpg.leaveAndJoin.LogoutProcedure;
import mc.carlton.freerpg.perksAndAbilities.*;
import mc.carlton.freerpg.serverConfig.ConfigLoad;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChangeStats {
    private Player p;
    private String pName;
    private UUID uuid;
    private boolean isCommand;
    private boolean isSilent;
    ArrayList<Double> multipliers;
    ArrayList<Double> tokensInfo;
    ArrayList<Double> levelingInfo;
    Map<String,Integer> maxLevels;
    ActionBarMessages actionMessage;
    static Map<Player,Boolean> isPlayerFlashingText = new ConcurrentHashMap<>();
    static Map<Player,Integer> playerRemoveEXPBarTaskIdMap = new ConcurrentHashMap<>();
    Map<String,Boolean> allowedSkillsMap = new HashMap<>();
    Map<String,Boolean> allowedSkillGainEXPMap = new HashMap<>();
    String beginnerLevelUpMessage;
    int maxLevelForBeginnerMessage;

    public ChangeStats(Player p) {
        this.p = p;
        this.pName = p.getDisplayName();
        this.uuid = p.getUniqueId();
        ConfigLoad loadConfig = new ConfigLoad();
        maxLevels = loadConfig.getMaxLevels();
        multipliers = loadConfig.getMultipliers();
        tokensInfo = loadConfig.getTokensInfo();
        levelingInfo = loadConfig.getLevelingInfo();
        this.isCommand = false;
        this.isSilent = false;
        this.actionMessage = new ActionBarMessages(p);
        this.allowedSkillsMap = loadConfig.getAllowedSkillsMap();
        this.allowedSkillGainEXPMap = loadConfig.getAllowedSkillGainEXPMap();
        this.beginnerLevelUpMessage = loadConfig.getBeginnerLevelUpMessage();
        this.maxLevelForBeginnerMessage = loadConfig.getMaxLevelForBeginnerMessage();
    }

    public void set_isCommand(boolean isFromCommand) {
        this.isCommand = isFromCommand;
    }
    public void set_silent(boolean isSilentExectuion) {this.isSilent = isSilentExectuion;}
    public boolean checkPerms(String skillName) {
        boolean hasPerms = true;
        String perm = "freeRPG." + skillName +"EXP";
        if (!p.hasPermission(perm)) {
            hasPerms = false;
        }
        return hasPerms;
    }

    public void changeEXP(String skillName,int expChange) {
        boolean hasPerms = checkPerms(skillName);
        if (!hasPerms) {
            return;
        }
        if (p.getGameMode() == GameMode.CREATIVE && !isCommand) {
            return;
        }
        if (expChange <= 0) {
            return;
        }
        if (!skillName.equals("global")) {
            LanguageSelector lang = new LanguageSelector(p);
            String[] titles_0 = {lang.getString("digging"),lang.getString("woodcutting"),lang.getString("mining"),lang.getString("farming"),lang.getString("fishing"),lang.getString("archery"),lang.getString("beastMastery"),lang.getString("swordsmanship"),lang.getString("defense"),lang.getString("axeMastery"),lang.getString("repair"),lang.getString("agility"),lang.getString("alchemy"),lang.getString("smelting"),lang.getString("enchanting"),lang.getString("global")};
            String[] labels_0 = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting","global"};
            List<String> labels_arr = Arrays.asList(labels_0);
            String skillTitle = titles_0[labels_arr.indexOf(skillName)];

            //Check if gaining skill EXP is disabled or the skill is disabled
            if (!allowedSkillGainEXPMap.get(skillName) || !allowedSkillsMap.get(skillName)) {
                return;
            }

            //Get stats
            PlayerStats pStatClass = new PlayerStats(p);
            Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
            Map<String, ArrayList<Number>> pStatAll = statAll.get(uuid);
            ArrayList<Number> pStats = pStatAll.get(skillName);
            ArrayList<Number> pGlobalStats = pStatAll.get("global");

            //Multipliers
            if (!isCommand) {
                expChange = (int) Math.ceil(expChange * getSkillMultiplier(skillName)); //multiplies exp by all mutlipliers
            }

            //Get Corresponding maxLevel
            int maxLevel = getMaxLevel(skillName);

            //TokensInfo
            double autoPassive = tokensInfo.get(0);
            double levelsPerPassive = tokensInfo.get(1);
            double levelsPerSkill = tokensInfo.get(2);
            double levelsPerGlobal = tokensInfo.get(3);

            // get old stats
            int exp = pStats.get(1).intValue();
            int oldLevel = pStats.get(0).intValue();
            int oldGlobalLevel = pGlobalStats.get(0).intValue();
            int tokens_S = (int)pStats.get(3);
            int tokens_P = (int)pStats.get(2);
            int tokens_G = (int)pGlobalStats.get(1);

            //If currently level is already maxed, do nothing
            if (oldLevel >= maxLevel) {
                ConfigLoad configLoad = new ConfigLoad();
                if (!configLoad.isEarnExperiencePastMaxLevel()) {
                    return;
                }
            }


            // set new stats
            exp += expChange;
            int totalEXP = (int)pGlobalStats.get(29)+expChange;
            int level = 0;
            int newTokens_S = 0;
            int newTokens_P = 0;
            int newTokens_G = 0;
            level = getLevelfromEXP(exp);

            //if new level is greater than the max level, set it to the max level
            boolean isMaxLevel = false;
            if (level >= maxLevel) {
                level = maxLevel;
                isMaxLevel = true;
            }


            //EXP bar
            setupBossBar(exp,oldLevel,level,skillTitle,skillName,expChange,isMaxLevel);


            int levelChange = level - oldLevel;
            int globalLevel = oldGlobalLevel + levelChange;
            if (levelChange > 0) {
                int oldLevel_s = (int)Math.floor(oldLevel/levelsPerSkill);
                int level_s = (int) Math.floor(level/levelsPerSkill);
                int oldLevel_g = (int)Math.floor(oldGlobalLevel/levelsPerGlobal);
                int level_g = (int) Math.floor(globalLevel/levelsPerGlobal);
                newTokens_S = level_s-oldLevel_s;
                newTokens_G = level_g-oldLevel_g;
                int extraSkillTokens = -100;
                int extraGlobalTokens = -100;
                boolean gainedGlobalToken = false;
                int passiveBoost = 0;
                if (newTokens_S > 0) {
                     extraSkillTokens = areSkillsMaxed(skillName,newTokens_S);
                    if (extraSkillTokens >= 0) {
                        newTokens_S -= extraSkillTokens;
                    }
                }
                if (newTokens_G > 0) {
                    gainedGlobalToken = true;
                    extraGlobalTokens = areGlobalPerksMaxed(newTokens_G);
                    if (extraGlobalTokens >= 0) {
                        newTokens_G -= extraGlobalTokens;
                        ConfigLoad loadConfig = new ConfigLoad();
                        double multiplierIncrease = extraGlobalTokens*loadConfig.getTokensInfo().get(8);
                        changeStat("global",23,multiplierIncrease);
                    }
                }
                if (!skillName.equals("repair") && !skillName.equals("agility") && !skillName.equals("alchemy") && !skillName.equals("smelting") && !skillName.equals("enchanting")) {
                    int oldLevel_p = (int)Math.floor(oldLevel/levelsPerPassive);
                    int level_p = (int) Math.floor(level/levelsPerPassive);
                    newTokens_P = level_p-oldLevel_p;
                    if (extraSkillTokens > 0) {
                        ConfigLoad loadConfig = new ConfigLoad();
                        newTokens_P +=(int) (extraSkillTokens*loadConfig.getTokensInfo().get(7));
                    }
                }
                else {
                    if (extraSkillTokens > 0) {
                        ConfigLoad loadConfig = new ConfigLoad();
                        passiveBoost += (int) (extraSkillTokens*loadConfig.getTokensInfo().get(7));
                    }
                }
                tokens_S += newTokens_S;
                tokens_P += newTokens_P;
                tokens_G += newTokens_G;
                String bars = "------------------------------------------------";
                String levelUpMessage = skillTitle + " " + lang.getString("level") + " " + level + "!";
                String sbSpace = "";
                int spaces = bars.length() - levelUpMessage.length();
                if (!(spaces % 2 == 0)){
                    bars += "-";
                    spaces -= 1;
                }
                for (int i=0; i < spaces/2.0; i++) {
                    sbSpace += " ";
                }
                if ((int)pGlobalStats.get(21) > 0 && !isSilent) { //Level Up Message Toggle Conditional
                    if (gainedGlobalToken) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
                        if (newTokens_G>0 && extraGlobalTokens <= 0) {
                            p.sendTitle(ChatColor.DARK_PURPLE + lang.getString("globalPassiveTitle0") + " " +  "+" + newTokens_G, ChatColor.YELLOW + lang.getString("try0") + " /frpg skillTree global", 5, 40, 20);
                        }
                        else {
                            ConfigLoad loadConfig = new ConfigLoad();
                            double multiplierIncrease = extraGlobalTokens*loadConfig.getTokensInfo().get(8);
                            String percentageIncreaseString = String.valueOf(multiplierIncrease*100)+"%";
                            p.sendTitle(ChatColor.DARK_PURPLE + lang.getString("expIncrease") + " +" + percentageIncreaseString,ChatColor.RED + lang.getString("globalPassiveTitle0") + " " + "+" + newTokens_G, 5, 40, 20);
                        }
                    }
                    if (newTokens_S > 0 || oldLevel < 3) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
                        p.sendMessage(bars);
                        p.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + sbSpace + levelUpMessage);
                        if (newTokens_P > 0) {
                            p.sendMessage(ChatColor.LIGHT_PURPLE + "+" + newTokens_P + " " + lang.getString("diggingPassiveTitle0"));
                        }
                        if (newTokens_S > 0) {
                            p.sendMessage(ChatColor.GOLD + "+" + newTokens_S + " " + lang.getString("diggingPassiveTitle2"));
                        }
                        if (newTokens_G > 0) {
                            p.sendMessage(ChatColor.DARK_PURPLE + "+" + newTokens_G + " " + lang.getString("globalPassiveTitle0"));
                        }
                        if (oldLevel < maxLevelForBeginnerMessage) {
                            p.sendMessage("");
                            if (!beginnerLevelUpMessage.equalsIgnoreCase("")) {
                                p.sendMessage(ChatColor.ITALIC + beginnerLevelUpMessage);
                            }
                            else {
                                p.sendMessage(ChatColor.ITALIC + lang.getString("passiveImprove"));
                                p.sendMessage(ChatColor.ITALIC + lang.getString("try0") + " /frpg skillTree " + skillName);
                            }
                        }
                        p.sendMessage(bars);
                    } else {
                        actionMessage.sendMessage(ChatColor.YELLOW + skillTitle + " "+lang.getString("increasedBy")+" " + Integer.toString(levelChange) + ". (" + level + ")");
                    }
                }
                pStats.set(0, level); //Level change
                pStats.set(2, tokens_P); //Passive token change
                pStats.set(3, tokens_S); //Skill token change
                int autoPassivesChange = 0;
                int oldLevel_auto_p = (int)Math.floor(oldLevel/autoPassive);
                int level_auto_p = (int) Math.floor(level/autoPassive);
                autoPassivesChange = level_auto_p-oldLevel_auto_p;
                pStats.set(4, (int) pStats.get(4) + autoPassivesChange+passiveBoost); //Duration (passive 1) Boost
                MaxPassiveLevels passiveMax = new MaxPassiveLevels();
                int passiveMax2 = passiveMax.findMaxLevel(skillName, 2);
                int passiveMax3 = passiveMax.findMaxLevel(skillName, 3);
                pStats.set(5, (int) Math.min((int) pStats.get(5) + autoPassivesChange+passiveBoost, passiveMax2)); //Passive 2 Boost
                pStats.set(6, (int) Math.min((int) pStats.get(6) + autoPassivesChange+passiveBoost, passiveMax3)); //Passive 3 Boost
                pGlobalStats.set(0, globalLevel); //Global level change
                pGlobalStats.set(1, tokens_G); //Global token change
            }

            pStats.set(1, exp); //Set skill exp
            pGlobalStats.set(29,totalEXP); //Set global exp

            // Sets stats
            pStatAll.put(skillName, pStats);
            pStatAll.put("global", pGlobalStats);
            statAll.put(uuid, pStatAll);
            pStatClass.setData(statAll);

            //Update Player Leaderboard
            ConfigLoad configLoad = new ConfigLoad();
            if (configLoad.isLeaderboardDyanmicUpdate()) {
                Leaderboards leaderboards = new Leaderboards();
                leaderboards.updatePlayerStats(p, skillName, exp, level); //Updates skill
                leaderboards.updatePlayerStats(p, "global", globalLevel, totalEXP); //Updates Global
            }
        }
    }

    /* Currently Broken
    private int getPassiveTokenIncrease(int oldLevel, int newLevel){
        double levelsPerPassive = tokensInfo.get(1);
        int oldLevel_p = (int)Math.floor(oldLevel/levelsPerPassive);
        int level_p = (int) Math.floor(newLevel/levelsPerPassive);
        int newTokens_P = level_p-oldLevel_p;
        return newTokens_P;
    }

     */

    private int getAutomaticPassiveSkillIncrease(String skillName,int oldLevel, int newLevel,int passiveSkillIndex) {

        //Get current passive tokens
        ArrayList<Number> pStats = getPlayerSkillStats(skillName);
        int passiveSkillLevel = (int) pStats.get(3+passiveSkillIndex); //Get the passive skill level

        //Get automatic passive tokens gained
        int autoPassivesChange = getAutoPassiveChange(oldLevel,newLevel);

        //Get the max passive skill level
        MaxPassiveLevels passiveMax = new MaxPassiveLevels();
        int passiveMaxLevel = passiveMax.findMaxLevel(skillName, passiveSkillIndex);

        //Determine how many tokens to add
        int automaticPassiveTokensGained;
        if (passiveSkillLevel + autoPassivesChange >= passiveMaxLevel) {
            automaticPassiveTokensGained = passiveMaxLevel - passiveSkillLevel; //Reward the player just enough to get them the max
        } else {
            automaticPassiveTokensGained = autoPassivesChange; //Reward the player the normal amount
        }

        return automaticPassiveTokensGained;
    }

    private int getAutoPassiveChange(int oldLevel, int newLevel) {
        double autoPassive = tokensInfo.get(0);
        int oldLevel_auto_p = (int)Math.floor(oldLevel/autoPassive);
        int level_auto_p = (int) Math.floor(newLevel/autoPassive);
        int autoPassivesChange = level_auto_p-oldLevel_auto_p;
        return autoPassivesChange;
    }

    /* CURRENTLY BROKEN
    private int getSkillTokenIncrease(int oldLevel, int newLevel) {
        double levelsPerSkill = tokensInfo.get(2);
        int oldLevel_s = (int)Math.floor(oldLevel/levelsPerSkill);
        int level_s = (int) Math.floor(newLevel/levelsPerSkill);
        int newTokens_S = level_s-oldLevel_s;
        return newTokens_S;
    }

     */

    private int getGlobalTokenIncrease(int oldLevel, int newLevel) {
        double levelsPerGlobal = tokensInfo.get(3);
        int oldLevel_g = (int)Math.floor(oldLevel/levelsPerGlobal);
        int level_g = (int) Math.floor(newLevel/levelsPerGlobal);
        int newTokens_G = level_g-oldLevel_g;
        if (newTokens_G > 0) {
            int extraGlobalTokens = areGlobalPerksMaxed(newTokens_G);
            if (extraGlobalTokens >= 0) {
                newTokens_G = newTokens_G - extraGlobalTokens;
            }
        }
        return newTokens_G;
    }

    public double getSkillMultiplier(String skillName) {
        PlayerStats pStatClass = new PlayerStats(p);
        ArrayList<Number> pGlobalStats = pStatClass.getPlayerData().get("global");
        Global globalClass = new Global(p);
        String[] labels_0 = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting","global"};
        List<String> labels_arr = Arrays.asList(labels_0);
        return ((double)pGlobalStats.get(23) * multipliers.get(0)) * (multipliers.get(labels_arr.indexOf(skillName) + 1)) * (globalClass.expBoost(skillName));
    }

    public void checkPlayerLevelEXPCurveConsistency() {
        String[] labels_0 = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting"};
        set_silent(true);
        set_isCommand(true);
        PlayerStats pStatClass = new PlayerStats(p);
        Map<UUID,Map<String, ArrayList<Number>>> allStats = pStatClass.getData();
        Map<String, ArrayList<Number>> pStats = allStats.get(p.getUniqueId());
        Map<String, Integer> expTotalMap = new HashMap<>();
        boolean statsNeedToBeChanged = false;
        for (String label : labels_0) {
            int maxLevel = getMaxLevel(label);
            ArrayList<Number> pStat = pStats.get(label);
            int exp = (int) pStat.get(1);
            expTotalMap.put(label,exp);
            int level = (int) pStat.get(0);
            int expectedLevel = getLevelfromEXP(exp);
            if (level != expectedLevel && level < maxLevel) {
                statsNeedToBeChanged = true;
            }
        }
        if (statsNeedToBeChanged) {
            resetStat("global");
            String[] labels_1 = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting"};
            for (String label : labels_1) {
                resetStat(label); //Reset all stats
            }
            for (String label : labels_1) { //Might cause global token errors if done in the same loop
                changeEXP(label, expTotalMap.get(label)); //Increase experience for all stats
            }
            LanguageSelector lang = new LanguageSelector(p);
            p.sendMessage(ChatColor.RED + lang.getString("statsUpdated"));
        }
    }

    public void resetStat(String skillName) {
        PlayerStats pStatClass = new PlayerStats(p);
        Map<UUID,Map<String, ArrayList<Number>>> allStats = pStatClass.getData();
        Map<String, ArrayList<Number>> pStats = allStats.get(p.getUniqueId());
        ArrayList<Number> pStat = pStats.get(skillName);
        int maxIndex = 13;
        if (skillName.equalsIgnoreCase("global")) {
            maxIndex = 11;
        }
        for (int i = 0; i <= maxIndex; i++) {
            pStat.set(i, 0);
        }
        pStats.put(skillName, pStat);
        allStats.put(p.getUniqueId(), pStats);
        pStatClass.setData(allStats);
        setTotalLevel();
        setTotalExperience();
        if (!skillName.equalsIgnoreCase("global")) {
            //Currently disabled to prevent accidental infinite skill token gain
            //reevaluatedGlobalTree();
        }
    }

    public void reevaluatedGlobalTree() {
        resetStat("global");
        setTotalLevel();
        ArrayList<Number> globalStats = getPlayerSkillStats("global");
        int globalTokens = getGlobalTokenIncrease(0,(int) globalStats.get(0));
        setStat("global",1,globalTokens);
    }

    public void refundSkillTree(String skillName) {
        endSkillTasks(skillName); //Ends tasks like "Fish Person" and other repeating tasks
        resetSkillTokens(skillName); //Refund skill tokens
        resetPassiveTokens(skillName); //Refund passive tokens
    }

    private void resetSkillTokens(String skillName) {
        ArrayList<Number> pStats = getPlayerSkillStats(skillName);
        //Find Skill Tokens, Refund skill tokens, set skills to 0
        if (!skillName.equalsIgnoreCase("global")) {
            int totalSkillTokens = ((int) pStats.get(3) + (int) pStats.get(7) + (int) pStats.get(8) + (int) pStats.get(9) + (int) pStats.get(10) + (int) pStats.get(11) + (int) pStats.get(12) + (int) pStats.get(13));
            setStat(skillName, 3, totalSkillTokens);
            setStat(skillName, 7, 0);
            setStat(skillName, 8, 0);
            setStat(skillName, 9, 0);
            setStat(skillName, 10, 0);
            setStat(skillName, 11, 0);
            setStat(skillName, 12, 0);
            setStat(skillName, 13, 0);
        } else {
            int totalGlobalTokens = ((int) pStats.get(1) + (int) pStats.get(2) + (int) pStats.get(3) + (int) pStats.get(4)
                                    + (int) pStats.get(5) + (int) pStats.get(6) + (int) pStats.get(7) + (int) pStats.get(8)
                                    + (int) pStats.get(9) + (int) pStats.get(10) + (int) pStats.get(11));
            setStat(skillName, 1, totalGlobalTokens);
            setStat(skillName, 2, 0);
            setStat(skillName, 3, 0);
            setStat(skillName, 4, 0);
            setStat(skillName, 5, 0);
            setStat(skillName, 6, 0);
            setStat(skillName, 7, 0);
            setStat(skillName, 8, 0);
            setStat(skillName, 9, 0);
            setStat(skillName, 10, 0);
            setStat(skillName, 11, 0);
        }
    }

    private void resetPassiveTokens(String skillName) {
        String[] mainSkills_0 = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery"};
        List<String> mainSkills = Arrays.asList(mainSkills_0);
        ArrayList<Number> pStats = getPlayerSkillStats(skillName);
        if (mainSkills.contains(skillName)) {
            int level = (int) pStats.get(0);
            int automaticPassiveTokens = getAutoPassiveChange(0,level);

            // Set new passive tokens to spend
            // passive tokens to spend = Total current tokens + investments into 3 passive skills - automatic tokens gained in 3 passive skills
            int totalPassiveTokens = (int) pStats.get(2) + //Current passive tokens
                                     Math.max((int) pStats.get(4) - automaticPassiveTokens,0) + //Tokens invested into passive 1
                                     Math.max((int) pStats.get(5) - automaticPassiveTokens,0) + //Tokens invested into passive 2
                                     Math.max((int) pStats.get(6) - automaticPassiveTokens,0); //Tokens invested into passive 3
            setStat(skillName, 2, totalPassiveTokens);

            //Get maximum passive skill levels
            MaxPassiveLevels maxPassiveLevels = new MaxPassiveLevels();

            //Set passive skills to what they would have been if no skill points were invested
            setStat(skillName, 4, Math.min(automaticPassiveTokens,maxPassiveLevels.findMaxLevel(skillName,1)));
            setStat(skillName, 5, Math.min(automaticPassiveTokens,maxPassiveLevels.findMaxLevel(skillName,2)));
            setStat(skillName, 6, Math.min(automaticPassiveTokens,maxPassiveLevels.findMaxLevel(skillName,3)));
        }
    }

    private void endSkillTasks(String skillName){
        ArrayList<Number> pStats = getPlayerSkillStats(skillName);

        //We use some methods from logoutProcedure
        LogoutProcedure logoutProcedure = new LogoutProcedure(p);
        if (skillName.equalsIgnoreCase("swordsmanship")) {
            logoutProcedure.preventLogoutTheftSwordsmanship(false);
        } else if (skillName.equalsIgnoreCase("defense")) {
            logoutProcedure.preventLogoutTheftDefense(false);
        } else if (skillName.equalsIgnoreCase("mining")) {
            logoutProcedure.preventLogoutTheftMining(false);
        } else if (skillName.equalsIgnoreCase("digging")) {
            logoutProcedure.preventLogoutTheftDigging(false);
        }

        //Ends all tasks that track players' buffs gained from some skills
        if (skillName.equals("farming") && (int) pStats.get(13) > 0) {
            Farming farmingClass = new Farming(p);
            farmingClass.oneWithNatureEnd();
        } else if (skillName.equals("fishing") && (int) pStats.get(13) > 0) {
            Fishing fishingClass = new Fishing(p);
            fishingClass.fishPersonEnd();
        } else if (skillName.equals("agility") && (int) pStats.get(13) > 0) {
            Agility agilityClass = new Agility(p);
            agilityClass.gracefulFeetEnd();
        } else if (skillName.equals("defense") && (int) pStats.get(13) > 0) {
            Defense defenseClass = new Defense(p);
            defenseClass.toggleHearty(0,(int) getPlayerSkillStats("global").get(30));
        }
    }

    private ArrayList<Number> getPlayerSkillStats(String skillName) {
        PlayerStats pStatClass = new PlayerStats(p);
        Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
        Map<String, ArrayList<Number>> pStatAll = statAll.get(uuid);
        ArrayList<Number> pStats = pStatAll.get(skillName);
        return pStats;
    }

    public int areSkillsMaxed(String skillName,int skillTokensGained){
        String[] passive_skills_0 = {"repair","agility","alchemy","smelting","enchanting"};
        List<String> passiveSkills = Arrays.asList(passive_skills_0);
        int maxSkillTokens = 23;
        int currentSkillTotal = 0;
        if (passiveSkills.contains(skillName)) {
            maxSkillTokens = 11;
        }

        PlayerStats pStatClass = new PlayerStats(p);
        Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
        Map<String, ArrayList<Number>> pStatAll = statAll.get(uuid);
        ArrayList<Number> pStats = pStatAll.get(skillName);
        currentSkillTotal = (int) pStats.get(3) + (int) pStats.get(7) + (int) pStats.get(8) + (int) pStats.get(9) + (int) pStats.get(10) + (int) pStats.get(11) + (int) pStats.get(12) + (int) pStats.get(13);
        currentSkillTotal += skillTokensGained;
        return currentSkillTotal-maxSkillTokens;
    }

    public int areGlobalPerksMaxed(int globalTokensGained) {
        int maxGlobalTotal = 10;
        PlayerStats pStatClass = new PlayerStats(p);
        Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
        Map<String, ArrayList<Number>> pStatAll = statAll.get(uuid);
        ArrayList<Number> pStats = pStatAll.get("global");
        int currentGlobalTotal = globalTokensGained + (int) pStats.get(1) + (int) pStats.get(2) + (int) pStats.get(3) + (int) pStats.get(4) + (int) pStats.get(5) + (int) pStats.get(6) + (int) pStats.get(7) + (int) pStats.get(8) +(int) pStats.get(9) +(int) pStats.get(10) +(int) pStats.get(11);
        return currentGlobalTotal - maxGlobalTotal;
    }

    public int getMaxLevel(String skillName) {
        int maxLevel = (int) maxLevels.get(skillName);
        if (maxLevel == -1) {
            maxLevel = maxLevels.get("global");
            if (maxLevel == - 1) {
                maxLevel = Integer.MAX_VALUE;
            }
        }
        return maxLevel;
    }

    public int getEXPfromLevel(int level) {
        double B = levelingInfo.get(1);
        int referenceLevel = (int) Math.round(levelingInfo.get(2));
        int referenceEXP = (int) Math.round(levelingInfo.get(3));
        int linearStartingLevel = (int) Math.round(levelingInfo.get(4));
        int linearEXP = (int) Math.round(levelingInfo.get(5));
        int EXP = 0;
        if (level > linearStartingLevel) {
            int exponentialMaxEXP = getEXPfromLevel(linearStartingLevel);
            EXP = exponentialMaxEXP + linearEXP*(level-linearStartingLevel);
        }
        else {
            EXP = (int) Math.floor(referenceEXP * ((Math.pow(B, level) - 1) / (Math.pow(B, referenceLevel) - 1)));
        }
        return EXP;
    }

    public int getLevelfromEXP(int exp) {
        double B = levelingInfo.get(1);
        int referenceLevel = (int) Math.round(levelingInfo.get(2));
        int referenceEXP = (int) Math.round(levelingInfo.get(3));
        int linearStartingLevel = (int) Math.round(levelingInfo.get(4));
        int linearEXP = (int) Math.round(levelingInfo.get(5));
        int exponentialMaxEXP = getEXPfromLevel(linearStartingLevel);
        int level = 0;
        if (exp <= exponentialMaxEXP) {
            level = (int) Math.floor(((Math.log( (exp*(1.0/referenceEXP)*(Math.pow(B, referenceLevel)-1)) +1) / Math.log(B))));
        }
        else {
            level = (int) Math.floor((exp-exponentialMaxEXP)/linearEXP) + linearStartingLevel;
        }
        return level;
    }

    public void increaseTokens(String skillName,String type,int amount) {
        //Get stats
        PlayerStats pStatClass = new PlayerStats(p);
        Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
        Map<String, ArrayList<Number>> pStatAll = statAll.get(uuid);
        ArrayList<Number> pStats = pStatAll.get(skillName);
        ArrayList<Number> pGlobalStats = pStatAll.get("global");
        switch (type) {
            case "global":
                int tokens_G = (int) pGlobalStats.get(1);
                int extraGlobalTokens = areGlobalPerksMaxed(amount);
                if (extraGlobalTokens > 0) {
                    amount -= extraGlobalTokens;
                    ConfigLoad loadConfig = new ConfigLoad();
                    double multiplierIncrease = extraGlobalTokens*loadConfig.getTokensInfo().get(8);
                    changeStat("global",23,multiplierIncrease);
                }
                pGlobalStats.set(1, tokens_G + amount);
                break;
            case "passive":
                int tokens_P = (int) pStats.get(2);
                pStats.set(2, tokens_P+amount);
                break;
            case "skill":
                int tokens_S = (int) pStats.get(3);
                int extraSkillTokens = areSkillsMaxed(skillName,amount);
                if (extraSkillTokens > 0) {
                    amount -= extraSkillTokens;
                    if (!skillName.equals("repair") && !skillName.equals("agility") && !skillName.equals("alchemy") && !skillName.equals("smelting") && !skillName.equals("enchanting")) {
                        int newTokens_P = 0;
                        if (extraSkillTokens > 0) {
                            ConfigLoad loadConfig = new ConfigLoad();
                            newTokens_P += (int) (extraSkillTokens * loadConfig.getTokensInfo().get(7));
                        }
                        pStats.set(2, (int) pStats.get(2) + newTokens_P);
                    } else {
                        int passiveBoost = 0;
                        if (extraSkillTokens > 0) {
                            ConfigLoad loadConfig = new ConfigLoad();
                            passiveBoost += (int) (extraSkillTokens * loadConfig.getTokensInfo().get(7));
                        }
                        pStats.set(4, (int) pStats.get(4) + passiveBoost);
                    }
                }
                pStats.set(3, tokens_S+amount);
                break;
            default:
                break;
        }

        // Sets stats
        pStatAll.put(skillName, pStats);
        pStatAll.put("global", pGlobalStats);
        statAll.put(uuid, pStatAll);
        pStatClass.setData(statAll);
    }

    public void changeStat(String skillName,int statIndex, Number valueChange) {
        //Get stats
        PlayerStats pStatClass = new PlayerStats(p);
        Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
        Map<String, ArrayList<Number>> pStatAll = statAll.get(uuid);
        ArrayList<Number> pStats = pStatAll.get(skillName);

        //change stat
        if (skillName.equalsIgnoreCase("global") && statIndex == 23) {
            double originalValue = (double) pStats.get(statIndex);
            double valueChangeDouble = (double) valueChange;
            pStats.set(statIndex, Math.round((originalValue + valueChangeDouble)*100000.0)/100000.0);
        }
        else {
            int originalValue = (int) pStats.get(statIndex);
            int valueChangeInt = (int) valueChange;
            pStats.set(statIndex, originalValue + valueChangeInt);
        }

        // Sets stats
        pStatAll.put(skillName, pStats);
        statAll.put(uuid, pStatAll);
        pStatClass.setData(statAll);
    }

    public void setStat(String skillName,int statIndex, Number newValue ) {
        //Get stats
        PlayerStats pStatClass = new PlayerStats(p);
        Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
        Map<String, ArrayList<Number>> pStatAll = statAll.get(uuid);
        ArrayList<Number> pStats = pStatAll.get(skillName);

        //change stat
        if (skillName.equalsIgnoreCase("global") && statIndex == 23) {
            double newValueDouble = (double) newValue;
            pStats.set(statIndex, newValueDouble);
        }
        else {
            int newValueInt = (int) newValue;
            pStats.set(statIndex, newValueInt);
        }

        // Sets stats
        pStatAll.put(skillName, pStats);
        statAll.put(uuid, pStatAll);
        pStatClass.setData(statAll);
    }

    public int setTotalLevel() {
        int totalLevel = 0;
        String[] labels_0 = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting"};
        //Get stats
        PlayerStats pStatClass = new PlayerStats(p);
        Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
        Map<String, ArrayList<Number>> pStatAll = statAll.get(uuid);
        ArrayList<Number> pStats = pStatAll.get("global");

        //Calculate total Level
        for (String skillName : labels_0) {
            totalLevel += (int) pStatAll.get(skillName).get(0);
        }

        //set total Level
        pStats.set(0,totalLevel);
        pStatAll.put("global", pStats);
        statAll.put(uuid, pStatAll);
        pStatClass.setData(statAll);

        return totalLevel;
    }

    public int setTotalExperience() {
        int totalExperience = 0;
        String[] labels_0 = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting"};
        //Get stats
        PlayerStats pStatClass = new PlayerStats(p);
        Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
        Map<String, ArrayList<Number>> pStatAll = statAll.get(uuid);
        ArrayList<Number> pStats = pStatAll.get("global");

        //Calculate total Level
        for (String skillName : labels_0) {
            totalExperience += (int) pStatAll.get(skillName).get(1);
        }

        //set total Level
        pStats.set(29,totalExperience);
        pStatAll.put("global", pStats);
        statAll.put(uuid, pStatAll);
        pStatClass.setData(statAll);

        return totalExperience;
    }

    public void setupBossBar(int exp, int oldLevel,int newLevel,String skillTitle,String skillName,int expChange,boolean isMaxed){
        PlayerStats pStatClass = new PlayerStats(p);
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        if ((int)pStat.get("global").get(25) < 1 || !pStatClass.isPlayerSkillExpBarOn(skillName)) {
            return;
        }
        if (isPlayerFlashingText.containsKey(p)) {
            if (isPlayerFlashingText.get(p)) {
                return;
            }
        }
        LanguageSelector lang = new LanguageSelector(p);
        if (newLevel > oldLevel) {
            BossBarStorage bossBarStorage = new BossBarStorage();
            BossBar expBar = bossBarStorage.getPlayerExpBar(p);
            expBar.setProgress(1.0);
            String message = skillTitle.toUpperCase() + " " + lang.getString("level").toUpperCase() + " " + newLevel + "!";
            expBar.setVisible(true);
            flashEXPBarText(message,ChatColor.GOLD,ChatColor.DARK_PURPLE,expBar);
            removeEXPBar(expBar);
        }
        else {
            if (isMaxed) {
                oldLevel = getLevelfromEXP(exp);
            }
            int lastLevelEXP = getEXPfromLevel(oldLevel);
            int nextLevelEXP = getEXPfromLevel(oldLevel+1);
            double progressEXP = Math.max(exp - lastLevelEXP,0);
            double neededEXP = nextLevelEXP-lastLevelEXP;
            double percentProgress = Math.min(progressEXP/neededEXP,1.0);
            BossBarStorage bossBarStorage = new BossBarStorage();
            BossBar expBar = bossBarStorage.getPlayerExpBar(p);
            expBar.setProgress(percentProgress);
            if (!isMaxed) {
                expBar.setColor(BarColor.GREEN);
                expBar.setTitle(ChatColor.GRAY + skillTitle + ChatColor.GOLD + " " + lang.getString("lvl") + " " + newLevel +
                        ChatColor.YELLOW + " (+" + expChange + " " + lang.getString("exp") + ")");
            }
            else {
                expBar.setColor(BarColor.BLUE);
                expBar.setTitle(ChatColor.LIGHT_PURPLE + "("+lang.getString("virtual")+") " + ChatColor.GRAY + skillTitle + ChatColor.GOLD + " " + lang.getString("lvl") + " " + oldLevel +
                        ChatColor.YELLOW + " (+" + expChange + " " + lang.getString("exp") + ")");
            }
            expBar.setVisible(true);
            removeEXPBar(expBar);
        }

    }

    public void flashEXPBarText(String message,ChatColor color1,ChatColor color2,BossBar expBar){
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        expBar.setProgress(1.0);
        isPlayerFlashingText.put(p,true);
        //Sets up 6 flashes
        for (int i = 0; i<=5;i++) {
            int finalI = i;
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (p.isOnline()) {
                        if (finalI % 2 == 0) {
                            expBar.setTitle(color1 + message);
                        } else {
                            expBar.setTitle(color2 + message);
                        }
                    }
                    if (finalI == 5) {
                        isPlayerFlashingText.remove(p);
                    }
                }
            }.runTaskLater(plugin, 10*i);
        }
    }

    public void removeEXPBar(BossBar expBar) {
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        if (playerRemoveEXPBarTaskIdMap.containsKey(p)) {
            Bukkit.getScheduler().cancelTask(playerRemoveEXPBarTaskIdMap.get(p));
        }
        int taskId = new BukkitRunnable() {
            @Override
            public void run() {
                expBar.setVisible(false);
                if (playerRemoveEXPBarTaskIdMap.containsKey(p)) {
                    playerRemoveEXPBarTaskIdMap.remove(p);
                }
            }
        }.runTaskLater(plugin,60).getTaskId();
        playerRemoveEXPBarTaskIdMap.put(p,taskId);
    }

}
