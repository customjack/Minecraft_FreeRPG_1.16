package mc.carlton.freerpg.playerAndServerInfo;

import mc.carlton.freerpg.gameTools.ActionBarMessages;
import mc.carlton.freerpg.gameTools.LanguageSelector;
import mc.carlton.freerpg.guiEvents.MaxPassiveLevels;
import mc.carlton.freerpg.perksAndAbilities.Global;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;

public class ChangeStats {
    private Player p;
    private String pName;
    private UUID uuid;
    private boolean isCommand;
    ArrayList<Double> multipliers = new ArrayList<>();
    ArrayList<Double> tokensInfo = new ArrayList<>();
    ArrayList<Double> levelingInfo = new ArrayList<>();
    ArrayList<Integer> maxLevels = new ArrayList<>();
    ActionBarMessages actionMessage;

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
        this.actionMessage = new ActionBarMessages(p);
    }

    public void set_isCommand(boolean isFromCommand) {
        this.isCommand = isFromCommand;
    }

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
        if (!skillName.equals("global")) {
            Global globalClass = new Global(p);
            LanguageSelector lang = new LanguageSelector(p);
            String[] titles_0 = {lang.getString("digging"),lang.getString("woodcutting"),lang.getString("mining"),lang.getString("farming"),lang.getString("fishing"),lang.getString("archery"),lang.getString("beastMastery"),lang.getString("swordsmanship"),lang.getString("defense"),lang.getString("axeMastery"),lang.getString("repair"),lang.getString("agility"),lang.getString("alchemy"),lang.getString("smelting"),lang.getString("enchanting"),lang.getString("global")};
            String[] labels_0 = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting","global"};
            List<String> labels_arr = Arrays.asList(labels_0);
            String skillTitle = titles_0[labels_arr.indexOf(skillName)];

            //Get stats
            PlayerStats pStatClass = new PlayerStats(p);
            Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
            Map<String, ArrayList<Number>> pStatAll = statAll.get(uuid);
            ArrayList<Number> pStats = pStatAll.get(skillName);
            ArrayList<Number> pGlobalStats = pStatAll.get("global");

            //Multipliers
            if (!isCommand) {
                expChange = (int) Math.ceil(expChange * ((double)pGlobalStats.get(23) * multipliers.get(0)) * (multipliers.get(labels_arr.indexOf(skillName) + 1)) * (globalClass.expBoost(skillName))); //multiplies exp by global multiplier
            }

            //Get Corresponding maxLevel
            int maxLevel = (int) maxLevels.get(labels_arr.indexOf(skillName) + 1);
            if (maxLevel == -1) {
                maxLevel = maxLevels.get(0);
                if (maxLevel == - 1) {
                    maxLevel = Integer.MAX_VALUE;
                }
            }

            //Max skill tokens



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
                return;
            }

            // set new stats
            exp += expChange;
            int level = 0;
            int newTokens_S = 0;
            int newTokens_P = 0;
            int newTokens_G = 0;
            level = getLevelfromEXP(exp);

            //if new level is greater than the max level, set it to the max level
            if (level >= maxLevel) {
                level = maxLevel;
            }

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
                if ((int)pGlobalStats.get(21) > 0) { //Level Up Message Toggle Conditional
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
                        if (oldLevel < 3) {
                            p.sendMessage("");
                            p.sendMessage(ChatColor.ITALIC + lang.getString("passiveImprove"));
                            p.sendMessage(ChatColor.ITALIC + lang.getString("try0") + " /frpg skillTree " + skillName);
                        }
                        p.sendMessage(bars);
                    } else {
                        actionMessage.sendMessage(ChatColor.YELLOW + skillTitle + " "+lang.getString("increasedBy")+" " + Integer.toString(levelChange) + ". (" + level + ")");
                    }
                }
                pStats.set(0, level);
                pStats.set(2, tokens_P);
                pStats.set(3, tokens_S);
                int autoPassivesChange = 0;
                int oldLevel_auto_p = (int)Math.floor(oldLevel/autoPassive);
                int level_auto_p = (int) Math.floor(level/autoPassive);
                autoPassivesChange = level_auto_p-oldLevel_auto_p;
                pStats.set(4, (int) pStats.get(4) + autoPassivesChange+passiveBoost);
                MaxPassiveLevels passiveMax = new MaxPassiveLevels();
                int passiveMax2 = passiveMax.findMaxLevel(skillName, 2);
                int passiveMax3 = passiveMax.findMaxLevel(skillName, 3);
                pStats.set(5, (int) Math.min((int) pStats.get(5) + autoPassivesChange+passiveBoost, passiveMax2));
                pStats.set(6, (int) Math.min((int) pStats.get(6) + autoPassivesChange+passiveBoost, passiveMax3));
                pGlobalStats.set(0, globalLevel);
                pGlobalStats.set(1, tokens_G);
            }

            pStats.set(1, exp);

            // Sets stats
            pStatAll.put(skillName, pStats);
            pStatAll.put("global", pGlobalStats);
            statAll.put(uuid, pStatAll);
            pStatClass.setData(statAll);
        }
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
                pGlobalStats.set(1, tokens_G+amount);
                break;
            case "passive":
                int tokens_P = (int) pStats.get(2);
                pStats.set(2, tokens_P+amount);
                break;
            case "skill":
                int tokens_S = (int) pStats.get(3);
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
            pStats.set(statIndex, originalValue + valueChangeDouble);
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

    public void setTotalLevel() {
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
    }
}
