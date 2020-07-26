package mc.carlton.freerpg.playerAndServerInfo;

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

    public ChangeStats(Player p) {
        this.p = p;
        this.pName = p.getDisplayName();
        this.uuid = p.getUniqueId();
        ConfigLoad loadConfig = new ConfigLoad();
        multipliers = loadConfig.getMultipliers();
        tokensInfo = loadConfig.getTokensInfo();
        levelingInfo = loadConfig.getLevelingInfo();
        this.isCommand = false;
    }

    public void set_isCommand(boolean isFromCommand) {
        this.isCommand = isFromCommand;
    }

    public void changeEXP(String skillName,int expChange) {
        if (p.getGameMode() == GameMode.CREATIVE && !isCommand) {
            return;
        }
        if (!skillName.equals("global")) {
            Global globalClass = new Global(p);
            String[] titles_0 = {"Digging","Woodcutting","Mining","Farming","Fishing","Archery","Beast Mastery","Swordsmanship","Defense","Axe Mastery","Repair","Agility","Alchemy","Smelting","Enchanting","Global"};
            String[] labels_0 = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting","global"};
            List<String> labels_arr = Arrays.asList(labels_0);
            String skillTitle = titles_0[labels_arr.indexOf(skillName)];
            if (!isCommand) {
                expChange = (int) Math.ceil(expChange * (multipliers.get(0)) * (multipliers.get(labels_arr.indexOf(skillName) + 1)) * (globalClass.expBoost(skillName))); //multiplies exp by global multiplier
            }
            //TokensInfo
            double autoPassive = tokensInfo.get(0);
            double levelsPerPassive = tokensInfo.get(1);
            double levelsPerSkill = tokensInfo.get(2);
            double levelsPerGlobal = tokensInfo.get(3);

            //Get stats
            PlayerStats pStatClass = new PlayerStats(p);
            Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
            Map<String, ArrayList<Number>> pStatAll = statAll.get(uuid);
            ArrayList<Number> pStats = pStatAll.get(skillName);
            ArrayList<Number> pGlobalStats = pStatAll.get("global");

            // get old stats
            int exp = pStats.get(1).intValue();
            int oldLevel = pStats.get(0).intValue();
            int oldGlobalLevel = pGlobalStats.get(0).intValue();
            int tokens_S = (int)pStats.get(3);
            int tokens_P = (int)pStats.get(2);
            int tokens_G = (int)pGlobalStats.get(1);

            // set new stats
            exp += expChange;
            int level = 0;
            int newTokens_S = 0;
            int newTokens_P = 0;
            int newTokens_G = 0;
            level = getLevelfromEXP(exp);

            int levelChange = level - oldLevel;
            int globalLevel = oldGlobalLevel + levelChange;
            if (levelChange > 0) {
                int oldLevel_s = (int)Math.floor(oldLevel/levelsPerSkill);
                int level_s = (int) Math.floor(level/levelsPerSkill);
                int oldLevel_g = (int)Math.floor(oldGlobalLevel/levelsPerGlobal);
                int level_g = (int) Math.floor(globalLevel/levelsPerGlobal);
                if (!skillName.equals("repair") && !skillName.equals("agility") && !skillName.equals("alchemy") && !skillName.equals("smelting") && !skillName.equals("enchanting")) {
                    int oldLevel_p = (int)Math.floor(oldLevel/levelsPerPassive);
                    int level_p = (int) Math.floor(level/levelsPerPassive);
                    newTokens_P = level_p-oldLevel_p;
                }
                newTokens_S = level_s-oldLevel_s;
                newTokens_G = level_g-oldLevel_g;
                tokens_S += newTokens_S;
                tokens_P += newTokens_P;
                tokens_G += newTokens_G;
                String bars = "------------------------------------------------";
                String levelUpMessage = "Reached level " + level + " in " + skillTitle + "!";
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
                    if (newTokens_G > 0) {
                        p.sendTitle(ChatColor.DARK_PURPLE + "+1 Global Token", ChatColor.YELLOW + "Check /skillTree global", 5, 20, 20);
                    }
                    if (newTokens_S > 0 || oldLevel < 3) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
                        p.sendMessage(bars);
                        p.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + sbSpace + "Reached level " + level + " in " + skillTitle + "!");
                        if (newTokens_P > 0) {
                            p.sendMessage(ChatColor.LIGHT_PURPLE + "+" + newTokens_P + " Passive Tokens");
                        }
                        if (newTokens_S > 0) {
                            p.sendMessage(ChatColor.GOLD + "+" + newTokens_S + " Skill Tokens");
                        }
                        if (newTokens_G > 0) {
                            p.sendMessage(ChatColor.DARK_PURPLE + "+" + newTokens_G + " Global Tokens");
                        }
                        if (oldLevel < 3) {
                            p.sendMessage("");
                            p.sendMessage(ChatColor.ITALIC + "Your passive perks improve every level!");
                            p.sendMessage(ChatColor.ITALIC + "Check out /skillTree " + skillName + " to see your perks!");
                        }
                        p.sendMessage(bars);
                    } else {
                        p.sendMessage(ChatColor.YELLOW + skillTitle + " increased by " + Integer.toString(levelChange) + ". (" + level + ")");
                    }
                }
                pStats.set(0, level);
                pStats.set(2, tokens_P);
                pStats.set(3, tokens_S);
                int autoPassivesChange = 0;
                int oldLevel_auto_p = (int)Math.floor(oldLevel/autoPassive);
                int level_auto_p = (int) Math.floor(level/autoPassive);
                autoPassivesChange = level_auto_p-oldLevel_auto_p;
                pStats.set(4, (int) pStats.get(4) + autoPassivesChange);
                MaxPassiveLevels passiveMax = new MaxPassiveLevels();
                int passiveMax2 = passiveMax.findMaxLevel(skillName, 2);
                int passiveMax3 = passiveMax.findMaxLevel(skillName, 3);
                pStats.set(5, (int) Math.min((int) pStats.get(5) + autoPassivesChange, passiveMax2));
                pStats.set(6, (int) Math.min((int) pStats.get(6) + autoPassivesChange, passiveMax3));
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

    public void changeStat(String skillName,int statIndex, int valueChange) {
        //Get stats
        PlayerStats pStatClass = new PlayerStats(p);
        Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
        Map<String, ArrayList<Number>> pStatAll = statAll.get(uuid);
        ArrayList<Number> pStats = pStatAll.get(skillName);

        //change stat
        int originalValue = (int) pStats.get(statIndex);
        pStats.set(statIndex,originalValue+valueChange);

        // Sets stats
        pStatAll.put(skillName, pStats);
        statAll.put(uuid, pStatAll);
        pStatClass.setData(statAll);
    }

    public void setStat(String skillName,int statIndex, int newValue ) {
        //Get stats
        PlayerStats pStatClass = new PlayerStats(p);
        Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
        Map<String, ArrayList<Number>> pStatAll = statAll.get(uuid);
        ArrayList<Number> pStats = pStatAll.get(skillName);

        //change stat
        pStats.set(statIndex,newValue);

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
