package mc.carlton.freerpg.playerAndServerInfo;

import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.*;

public class PlayerStats {
    private Player player;
    private UUID uuid;
    static Map<UUID, Map<String, ArrayList<Number>>> player_statsMap = new HashMap<UUID, Map<String, ArrayList<Number>>>();
    static Map<UUID,Number> player_LoginTime = new HashMap<>();
    static Map<UUID,Number> player_playTime = new HashMap<>();
    static Map<UUID,String> player_language = new HashMap<>();
    static Map<UUID,Map<String,Integer>> playerSkillToggleEXPBar = new HashMap<>();
    static Map<UUID,Map<String,Integer>> playerSkillToggleAbility = new HashMap<>();

    public PlayerStats(Player p) {
        this.player = p;
        this.uuid = player.getUniqueId();
    }

    public Map<String, ArrayList<Number>> getPlayerData() {
        if (!player_statsMap.containsKey(uuid)) {
            PlayerStatsLoadIn loadInPlayer = new PlayerStatsLoadIn(player);
            Map<String, ArrayList<Number>> playerStats0 = loadInPlayer.getPlayerStatsMap();
            player_statsMap.put(uuid,playerStats0);
        }
        return player_statsMap.get(uuid);
    }

    public void setData(Map<UUID, Map<String, ArrayList<Number>>> playerStatsMap) {
        this.player_statsMap = playerStatsMap;
    }

    public Map<UUID, Map<String, ArrayList<Number>>> getData() {
        if (!player_statsMap.containsKey(uuid)) {
            PlayerStatsLoadIn loadInPlayer = new PlayerStatsLoadIn(player);
            Map<String, ArrayList<Number>> playerStats0 = loadInPlayer.getPlayerStatsMap();
            player_statsMap.put(uuid,playerStats0);
        }
        return player_statsMap;
    }

    public void addPlayerTimes(long loginTime,long playTime) {
        player_LoginTime.put(uuid,loginTime);
        player_playTime.put(uuid,playTime);
    }

    public String getPlayerPlayTimeString() {
        String playTime_string = "";
        long loginTime = (long) player_LoginTime.get(uuid);
        long playTime = (long) player_playTime.get(uuid);
        long currentTime = Instant.now().getEpochSecond();
        long newPlayTime = playTime + (currentTime-loginTime);
        int hours = (int) Math.floor(newPlayTime/3600.0);
        int minutes = (int) Math.floor( (newPlayTime-(hours*3600))/60.0 );
        int seconds =  (int) Math.floor((newPlayTime - (hours*3600) - (minutes*60)));
        String hoursString = Integer.toString(hours);
        if (hoursString.length() < 2) {
            hoursString = "0"+hoursString;
        }
        String minutesString = Integer.toString(minutes);
        if (minutesString.length() < 2) {
            minutesString = "0"+minutesString;
        }
        String secondsString = Integer.toString(seconds);
        if (secondsString.length() < 2) {
            secondsString = "0"+secondsString;
        }
        playTime_string = hoursString + ":" + minutesString + ":" + secondsString;
        return playTime_string;
    }

    public void setPlayerLanguage(String language) {
        player_language.put(uuid,language);
    }
    public String getPlayerLanguage() {
        return player_language.get(uuid);
    }

    public void addPlayerSkillToggleExpBar(Map<String,Integer> skillToggles){
        playerSkillToggleEXPBar.put(uuid,skillToggles);
    }
    public void addPlayerSkillToggleAbility(Map<String,Integer> skillToggles){
        playerSkillToggleAbility.put(uuid,skillToggles);
    }
    public Map<String,Integer> getSkillToggleExpBar(){
        if (!playerSkillToggleEXPBar.containsKey(uuid)) {
            PlayerStatsLoadIn loadInPlayer = new PlayerStatsLoadIn(player);
            Map<String,Integer> playerStats0 = loadInPlayer.getSkillExpBarToggles();
            playerSkillToggleEXPBar.put(uuid,playerStats0);
        }
        return playerSkillToggleEXPBar.get(uuid);
    }
    public Map<String,Integer> getSkillToggleAbility(){
        if (!playerSkillToggleAbility.containsKey(uuid)) {
            PlayerStatsLoadIn loadInPlayer = new PlayerStatsLoadIn(player);
            Map<String,Integer> playerStats0 = loadInPlayer.getSkillAbilityToggles();
            playerSkillToggleAbility.put(uuid,playerStats0);
        }
        return playerSkillToggleAbility.get(uuid);
    }

    public boolean isPlayerSkillExpBarOn(String skillName) {
        int expBarOn = playerSkillToggleEXPBar.get(uuid).get(skillName);
        if (expBarOn == 1) {
            return true;
        }
        else {
            return false;
        }
    }
    public boolean isPlayerSkillAbilityOn(String skillName){
        int abilityOn = playerSkillToggleAbility.get(uuid).get(skillName);
        if (abilityOn == 1) {
            return true;
        }
        else {
            return false;
        }
    }
    public void togglePlayerSkillExpBar(String skillName) {
        Map<String,Integer> playerSkillEXPBarMap = playerSkillToggleEXPBar.get(uuid);
        int expBarOn = playerSkillEXPBarMap.get(skillName);
        if (expBarOn == 1) {
            playerSkillEXPBarMap.put(skillName,0);
        }
        else {
            playerSkillEXPBarMap.put(skillName,1);
        }
        playerSkillToggleEXPBar.put(uuid,playerSkillEXPBarMap);
    }
    public void togglePlayerSkillAbility(String skillName) {
        Map<String,Integer> playerSkillAbiliytMap = playerSkillToggleAbility.get(uuid);
        int abilityOn = playerSkillAbiliytMap.get(skillName);
        if (abilityOn == 1) {
            playerSkillAbiliytMap.put(skillName,0);
        }
        else {
            playerSkillAbiliytMap.put(skillName,1);
        }
        playerSkillToggleAbility.put(uuid,playerSkillAbiliytMap);
    }


    public void removePlayer() {
        player_statsMap.remove(uuid);
        player_LoginTime.remove(uuid);
        player_playTime.remove(uuid);
        player_language.remove(uuid);
        playerSkillToggleEXPBar.remove(uuid);
        playerSkillToggleAbility.remove(uuid);
    }

}
