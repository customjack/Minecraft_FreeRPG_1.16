package mc.carlton.freerpg.playerAndServerInfo;

import mc.carlton.freerpg.FreeRPG;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerStatsLoadIn {
    private Player p;
    Map<String, ArrayList<Number>> statsMap = new HashMap<String, ArrayList<Number>>();
    File f;
    UUID pUUID;
    FileConfiguration playerData;

    public PlayerStatsLoadIn(Player player) {
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        this.p = player;
        this.pUUID = p.getUniqueId();
        this.f = new File(plugin.getDataFolder(), File.separator + "PlayerDatabase" + File.separator + pUUID.toString() + ".yml");
        this.f.setReadable(true,false);
        this.f.setWritable(true,false);
        this.playerData = YamlConfiguration.loadConfiguration(f);
    }

    public long getLoginTime() {
        long loginTime = 0;
        if (f.exists()) {
            loginTime = Long.parseLong(playerData.get("general.lastLogin").toString());
            return loginTime;
        }
        return Instant.now().getEpochSecond();
    }

    public long getPlayTime() {
        long playTime = 0;
        if (f.exists()) {
            playTime = Long.parseLong(playerData.get("general.playTime").toString());
            return playTime;
        }
        return Instant.now().getEpochSecond();
    }

    public String getPlayerLanguage() {
        String language = "enUs";
        if (f.exists()) {
            language = playerData.get("general.language").toString();
            return language;
        }
        return language;
    }

    public Map<String,Integer> getSkillExpBarToggles(){
        Map<String,Integer> skillExpBarToggleMap = new HashMap<>();
        String[] labels = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting"};
        for (String label : labels) {
            skillExpBarToggleMap.put(label,playerData.getInt(label + ".showEXPBarToggle"));
        }
        return skillExpBarToggleMap;
    }
    public Map<String,Integer> getSkillAbilityToggles(){
        Map<String,Integer> skillAbilityToggleMap = new HashMap<>();
        String[] labels = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting"};
        for (String label : labels) {
            skillAbilityToggleMap.put(label,playerData.getInt(label + ".triggerAbilityToggle"));
        }
        return skillAbilityToggleMap;
    }

    public  Map<String, ArrayList<Number>>  getPlayerStatsMap() {
        String[] labels = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting"};
        ArrayList<Number> stats = new ArrayList<Number>();
        if(f.exists()) {
            stats.add(playerData.getInt("globalStats.totalLevel"));
            stats.add(playerData.getInt("globalStats.globalTokens"));
            stats.add(playerData.getInt("globalStats.skill_1a"));
            stats.add(playerData.getInt("globalStats.skill_1b"));
            stats.add(playerData.getInt("globalStats.skill_1c"));
            stats.add(playerData.getInt("globalStats.skill_2a"));
            stats.add(playerData.getInt("globalStats.skill_2b"));
            stats.add(playerData.getInt("globalStats.skill_2c"));
            stats.add(playerData.getInt("globalStats.skill_3a"));
            stats.add(playerData.getInt("globalStats.skill_3b"));
            stats.add(playerData.getInt("globalStats.skill_3c"));
            stats.add(playerData.getInt("globalStats.skill_M"));
            stats.add(playerData.getInt("globalStats.flintToggle"));
            stats.add(playerData.getInt("globalStats.oreToggle"));
            stats.add(playerData.getInt("globalStats.speedToggle"));
            stats.add(playerData.getInt("globalStats.potionToggle"));
            stats.add(playerData.getInt("globalStats.grappleToggle"));
            stats.add(playerData.getInt("globalStats.hotRodToggle"));
            stats.add(playerData.getInt("globalStats.veinMinerToggle"));
            stats.add(playerData.getInt("globalStats.megaDigToggle"));
            stats.add(playerData.getInt("globalStats.souls"));
            stats.add(playerData.getInt("globalStats.levelUpMessageToggle"));
            stats.add(playerData.getInt("globalStats.abilityPrepareMessageToggle"));
            stats.add(playerData.getDouble("globalStats.personalEXPMultiplier"));
            stats.add(playerData.getInt("globalStats.triggerAbilitiesToggle"));
            stats.add(playerData.getInt("globalStats.showEXPBarToggle"));
            statsMap.put("global", stats);

            for (int i = 0; i < labels.length; i++) {
                String skillName = labels[i];
                ArrayList<Number> statsi = new ArrayList<Number>();
                statsi.add(playerData.getInt(labels[i] + ".level"));
                statsi.add(playerData.getInt(labels[i] + ".experience"));
                statsi.add(playerData.getInt(labels[i] + ".passiveTokens"));
                statsi.add(playerData.getInt(labels[i] + ".skillTokens"));
                statsi.add(playerData.getInt(labels[i] + ".passive1"));
                statsi.add(playerData.getInt(labels[i] + ".passive2"));
                statsi.add(playerData.getInt(labels[i] + ".passive3"));
                statsi.add(playerData.getInt(labels[i] + ".skill_1a"));
                statsi.add(playerData.getInt(labels[i] + ".skill_1b"));
                statsi.add(playerData.getInt(labels[i] + ".skill_2a"));
                statsi.add(playerData.getInt(labels[i] + ".skill_2b"));
                statsi.add(playerData.getInt(labels[i] + ".skill_3a"));
                statsi.add(playerData.getInt(labels[i] + ".skill_3b"));
                statsi.add(playerData.getInt(labels[i] + ".skill_M"));

                statsMap.put(skillName, statsi);
            }

        }
    return statsMap;
    }
    public void setPlayerStatsMap() throws IOException {
        PlayerStats pStatClass = new PlayerStats(p);
        Map<String, ArrayList<Number>> pStatAll = pStatClass.getPlayerData();
        Map<String,Integer> expBarToggles = pStatClass.getSkillToggleExpBar();
        Map<String,Integer> abilityToggles = pStatClass.getSkillToggleAbility();
        String pName = p.getName();
        long unixTime = Instant.now().getEpochSecond();
        if(f.exists()) {
            playerData.set("general.username", pName);

            //Setting playTime in seconds
            playerData.set("general.lastLogout",unixTime);
            long lastLoginTime = (long) Long.parseLong(playerData.get("general.lastLogin").toString());
            long playTime = unixTime-lastLoginTime;
            playerData.set("general.playTime",playTime);

            //Setting player Language
            String playerLanguage = pStatClass.getPlayerLanguage();
            playerData.set("general.language",playerLanguage);

            for (String i : pStatAll.keySet()) {
                if (i.equalsIgnoreCase("global")) {
                    playerData.set("globalStats.totalLevel",pStatAll.get(i).get(0));
                    playerData.set("globalStats.globalTokens",pStatAll.get(i).get(1));
                    playerData.set("globalStats.skill_1a",pStatAll.get(i).get(2));
                    playerData.set("globalStats.skill_1b",pStatAll.get(i).get(3));
                    playerData.set("globalStats.skill_1c",pStatAll.get(i).get(4));
                    playerData.set("globalStats.skill_2a",pStatAll.get(i).get(5));
                    playerData.set("globalStats.skill_2b",pStatAll.get(i).get(6));
                    playerData.set("globalStats.skill_2c",pStatAll.get(i).get(7));
                    playerData.set("globalStats.skill_3a",pStatAll.get(i).get(8));
                    playerData.set("globalStats.skill_3b",pStatAll.get(i).get(9));
                    playerData.set("globalStats.skill_3c",pStatAll.get(i).get(10));
                    playerData.set("globalStats.skill_M",pStatAll.get(i).get(11));
                    playerData.set("globalStats.flintToggle",pStatAll.get(i).get(12));
                    playerData.set("globalStats.oreToggle",pStatAll.get(i).get(13));
                    playerData.set("globalStats.speedToggle",pStatAll.get(i).get(14));
                    playerData.set("globalStats.potionToggle",pStatAll.get(i).get(15));
                    playerData.set("globalStats.grappleToggle",pStatAll.get(i).get(16));
                    playerData.set("globalStats.hotRodToggle",pStatAll.get(i).get(17));
                    playerData.set("globalStats.veinMinerToggle",pStatAll.get(i).get(18));
                    playerData.set("globalStats.megaDigToggle",pStatAll.get(i).get(19));
                    playerData.set("globalStats.souls",pStatAll.get(i).get(20));
                    playerData.set("globalStats.levelUpMessageToggle",pStatAll.get(i).get(21));
                    playerData.set("globalStats.abilityPrepareMessageToggle",pStatAll.get(i).get(22));
                    playerData.set("globalStats.personalEXPMultiplier",pStatAll.get(i).get(23));
                    playerData.set("globalStats.triggerAbilitiesToggle",pStatAll.get(i).get(24));
                    playerData.set("globalStats.showEXPBarToggle",pStatAll.get(i).get(25));
                }
                else {
                    playerData.set(i+".level",pStatAll.get(i).get(0));
                    playerData.set(i+".experience",pStatAll.get(i).get(1));
                    playerData.set(i+".passiveTokens",pStatAll.get(i).get(2));
                    playerData.set(i+".skillTokens",pStatAll.get(i).get(3));
                    playerData.set(i+".passive1",pStatAll.get(i).get(4));
                    playerData.set(i+".passive2",pStatAll.get(i).get(5));
                    playerData.set(i+".passive3",pStatAll.get(i).get(6));
                    playerData.set(i+".skill_1a",pStatAll.get(i).get(7));
                    playerData.set(i+".skill_1b",pStatAll.get(i).get(8));
                    playerData.set(i+".skill_2a",pStatAll.get(i).get(9));
                    playerData.set(i+".skill_2b",pStatAll.get(i).get(10));
                    playerData.set(i+".skill_3a",pStatAll.get(i).get(11));
                    playerData.set(i+".skill_3b",pStatAll.get(i).get(12));
                    playerData.set(i+".skill_M",pStatAll.get(i).get(13));
                    playerData.set(i+".triggerAbilityToggle",expBarToggles.get(i));
                    playerData.set(i+".showEXPBarToggle",abilityToggles.get(i));
                }
                }
            playerData.save(f);
            System.out.println("[FreeRPG] Saved " + pName + " stats successfully");
            }
    }
}
