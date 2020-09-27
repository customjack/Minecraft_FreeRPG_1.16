package mc.carlton.freerpg.serverInfo;

import mc.carlton.freerpg.globalVariables.StringsAndOtherData;
import mc.carlton.freerpg.playerInfo.ChangeStats;
import mc.carlton.freerpg.playerInfo.Leaderboards;
import mc.carlton.freerpg.playerInfo.PlayerLeaderboardStat;
import mc.carlton.freerpg.playerInfo.PlayerStats;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;

/**
 * This class will automatically register as a placeholder expansion
 * when a jar including this class is added to the /plugins/placeholderapi/expansions/ folder
 *
 */
public class FreeRPGPlaceHolders extends PlaceholderExpansion {
    private Plugin freeRPG;

    public FreeRPGPlaceHolders(Plugin plugin) {
        this.freeRPG = plugin;
    }
    /**
     * Because this is an internal class,
     * you must override this method to let PlaceholderAPI know to not unregister your expansion class when
     * PlaceholderAPI is reloaded
     *
     * @return true to persist through reloads
     */
    @Override
    public boolean persist(){
        return true;
    }

    /**
     * This method should always return true unless we
     * have a dependency we need to make sure is on the server
     * for our placeholders to work!
     * This expansion does not require a dependency so we will always return true
     */
    @Override
    public boolean canRegister() {
        return true;
    }

    /**
     * The name of the person who created this expansion should go here
     */
    /**
     * The name of the person who created this expansion should go here.
     * <br>For convienience do we return the author from the plugin.yml
     *
     * @return The name of the author as a String.
     */
    @Override
    public String getAuthor(){
        return freeRPG.getDescription().getAuthors().toString();
    }

    /**
     * The placeholder identifier should go here
     * This is what tells PlaceholderAPI to call our onPlaceholderRequest method to obtain
     * a value if a placeholder starts with our identifier.
     * This must be unique and can not contain % or _
     */
    @Override
    public String getIdentifier() {
        return "freeRPG";
    }

    /**
     * This is the version of this expansion
     */
    @Override
    public String getVersion(){
        return freeRPG.getDescription().getVersion();
    }

    /**
     * This is the method called when a placeholder with our identifier
     * is found and needs a value.
     * <br>We specify the value identifier in this method.
     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * @param  p
     *         Player
     * @param  identifier
     *         A String containing the identifier/value.
     *
     * @return possibly-null String of the requested identifier.
     */
    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        String[] identifierParts = identifier.split("_");

        //Checks if the request is a leaderboard request
        if (identifierParts.length == 4 && identifierParts[0].equalsIgnoreCase("leaderboard")) { //Required for any leaderboard request
            Leaderboards leaderboards = new Leaderboards();
            List<String> leaderboardNames = leaderboards.getLeaderboardNames();
            String leaderboardName = "";
            for (String leaderboardTypeName : leaderboardNames) { //Is the first
                if (identifierParts[1].equalsIgnoreCase(leaderboardTypeName)) {
                    leaderboardName = leaderboardTypeName;
                }
            }
            if (!leaderboardName.equalsIgnoreCase("")) {
                int position = 0;
                try {
                    position = Integer.valueOf(identifierParts[2]);
                }
                catch (NumberFormatException e) {
                    return null;
                }
                PlayerLeaderboardStat playerStatFromLeaderboard = leaderboards.getPlayerStatAtLeaderBoardPosition(leaderboardName,position);
                String stat = identifierParts[3];
                ArrayList<String> leaderboardIdentifiers = getLeaderboardIdentifiers(leaderboardName);
                if (stat.equalsIgnoreCase(leaderboardIdentifiers.get(0))) {
                    return playerStatFromLeaderboard.get_playerUUID().toString();
                }
                else if (stat.equalsIgnoreCase(leaderboardIdentifiers.get(1)) || stat.equalsIgnoreCase("username")) {
                    return playerStatFromLeaderboard.get_pName();
                }
                else if (stat.equalsIgnoreCase(leaderboardIdentifiers.get(2)) || stat.equalsIgnoreCase("sortedStat")) {
                    if (leaderboardName.equalsIgnoreCase("playTime")) {
                        return playerStatFromLeaderboard.get_playTimeString();
                    }
                    else {
                        return String.format("%,d",playerStatFromLeaderboard.get_sortedStat());
                    }
                }
                else if (stat.equalsIgnoreCase(leaderboardIdentifiers.get(3)) || stat.equalsIgnoreCase("stat2")) {
                    if (playerStatFromLeaderboard.get_stat2() != null) {
                        return String.format("%,d",playerStatFromLeaderboard.get_stat2());
                    }
                    else {
                        return null;
                    }
                }
            }
        }

        if(p == null){
            return "";
        }
        StringsAndOtherData stringsAndOtherData = new StringsAndOtherData();
        ArrayList<String> skillNames = stringsAndOtherData.getSkillNames();

        PlayerStats playerStats = new PlayerStats(p);
        Map<String,ArrayList<Number>> pStats = playerStats.getPlayerData();


        //Global Parameters (The else-ifs aren't really necessary, but it helps me organize)
        if (identifier.equalsIgnoreCase("globalLevel")) { //Global Level
            return String.format("%,d",pStats.get("global").get(0));
        }
        else if (identifier.equalsIgnoreCase("globalTokens")) {
            return String.format("%,d",pStats.get("global").get(1)); //Global Tokens
        }
        else if (identifier.equalsIgnoreCase("personalMultiplier")) {
            return String.valueOf(pStats.get("global").get(23)); //Personal Mulitplier
        }
        else if (identifier.equalsIgnoreCase("totalSkillTokens")) {
            int totalSkilTokens = 0;
            for (String skillName : pStats.keySet()){
                if (!skillName.equalsIgnoreCase("global")){
                    totalSkilTokens += pStats.get(skillName).get(3).intValue();
                }
            }
            return String.format("%,d",totalSkilTokens); //Total Skill Tokens
        }
        else if (identifier.equalsIgnoreCase("totalPassiveTokens")) {
            int totalPassiveTokens = 0;
            for (String skillName : pStats.keySet()){
                if (!skillName.equalsIgnoreCase("global")){
                    totalPassiveTokens += pStats.get(skillName).get(2).intValue();
                }
            }
            return String.format("%,d",totalPassiveTokens); //Total Passive Tokens
        }
        else if (identifier.equalsIgnoreCase("souls")) {
            return String.format("%,d",pStats.get("global").get(20)); //Souls
        }
        else if (identifier.equalsIgnoreCase("totalEXP") || identifier.equalsIgnoreCase("totalExperience") ) {
            return String.format("%,d",pStats.get("global").get(29)); //Total experience
        }
        else if (identifier.equalsIgnoreCase("playTime")) {
            return playerStats.getPlayerPlayTimeString(); //Total play time (with FreeRPG installed)
        }
        else if (identifier.equalsIgnoreCase("globalLevelRank")) {
            Leaderboards leaderboards = new Leaderboards();
            return String.valueOf(leaderboards.getLeaderboardPosition(p,"global")); //Total play time (with FreeRPG installed)
        }
        else if (identifier.equalsIgnoreCase("playTimeRank")) {
            Leaderboards leaderboards = new Leaderboards();
            return String.valueOf(leaderboards.getLeaderboardPosition(p,"playTime")); //Total play time (with FreeRPG installed)
        }

        // Check if the request contains a skill name
        boolean potentialSkillSpecificRequest = false;
        String skillName = "";
        for (String sName : skillNames) {
            if (identifierParts[0].toLowerCase().equalsIgnoreCase(sName)) {
                potentialSkillSpecificRequest = true;
                skillName = sName;
            }
        }

        //All skill specific requests
        if (potentialSkillSpecificRequest) {
            if (identifier.equalsIgnoreCase(skillName+"_Level")) { //Level
                return String.format("%,d",pStats.get(skillName).get(0));
            }
            else if (identifier.equalsIgnoreCase(skillName+"_EXP") || identifier.equalsIgnoreCase(skillName+"experience")) { //Experience
                return String.format("%,d",pStats.get(skillName).get(1));
            }
            else if (identifier.equalsIgnoreCase(skillName+"_passiveTokens")) { //Passive Tokens
                return String.format("%,d",pStats.get(skillName).get(2));
            }
            else if (identifier.equalsIgnoreCase(skillName+"_skillTokens")) { //Skill Tokens
                return String.format("%,d",pStats.get(skillName).get(3));
            }
            else if (identifier.equalsIgnoreCase(skillName+"_Multiplier")) { //Skill Multiplier
                ChangeStats changeStats = new ChangeStats(p);
                return String.valueOf(changeStats.getSkillMultiplier(skillName));
            }
            else if (identifier.equalsIgnoreCase(skillName+"_EXPtoNext")) { //Skill EXP to next
                int EXP = pStats.get(skillName).get(1).intValue();
                int level = pStats.get(skillName).get(0).intValue();
                ChangeStats getEXP = new ChangeStats(p);
                int nextEXP = getEXP.getEXPfromLevel(level+1);
                int EXPtoNext = nextEXP - EXP;
                return String.format("%,d",EXPtoNext);
            }
            else if (identifier.equalsIgnoreCase(skillName+"_rank")) {
                Leaderboards leaderboards = new Leaderboards();
                return String.valueOf(leaderboards.getLeaderboardPosition(p,skillName)); //Total play time (with FreeRPG installed)
            }
        }


        return null;
    }

    public ArrayList<String> getLeaderboardIdentifiers(String leaderboardName){
        ArrayList<String> identifiers = new ArrayList<>();
        identifiers.add("UUID");
        identifiers.add("playerName");
        if (leaderboardName.equalsIgnoreCase("global")) {
            identifiers.add("totalLevel");
            identifiers.add("totalEXP");
        }
        else if (leaderboardName.equalsIgnoreCase("playTime")) {
            identifiers.add("totalTimePlayed");
            identifiers.add("null");
        }
        else {
            identifiers.add("exp");
            identifiers.add("level");
        }
        return identifiers;
    }

}
