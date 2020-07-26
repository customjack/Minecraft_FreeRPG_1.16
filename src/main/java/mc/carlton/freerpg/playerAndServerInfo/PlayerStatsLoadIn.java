package mc.carlton.freerpg.playerAndServerInfo;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerStatsLoadIn {
    private Player p;
    Map<String, ArrayList<Number>> statsMap = new HashMap<String, ArrayList<Number>>();

    public PlayerStatsLoadIn(Player player) {
        this.p = player;

    }
    public  Map<String, ArrayList<Number>>  getPlayerStatsMap(Player p) {
        String[] labels = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting"};
        ArrayList<Number> stats = new ArrayList<Number>();
        String pName = p.getName();
        UUID pUUID = p.getUniqueId();
        File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("FreeRPG").getDataFolder(), File.separator + "PlayerDatabase");
        File f = new File(userdata, File.separator + pUUID.toString() + ".yml");
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
        if(f.exists()) {
            stats.add(Integer.valueOf((playerData.get("globalStats.totalLevel").toString())));
            stats.add(Integer.valueOf((playerData.get("globalStats.globalTokens").toString())));
            stats.add(Integer.valueOf((playerData.get("globalStats.skill_1a").toString())));
            stats.add(Integer.valueOf((playerData.get("globalStats.skill_1b").toString())));
            stats.add(Integer.valueOf((playerData.get("globalStats.skill_1c").toString())));
            stats.add(Integer.valueOf((playerData.get("globalStats.skill_2a").toString())));
            stats.add(Integer.valueOf((playerData.get("globalStats.skill_2b").toString())));
            stats.add(Integer.valueOf((playerData.get("globalStats.skill_2c").toString())));
            stats.add(Integer.valueOf((playerData.get("globalStats.skill_3a").toString())));
            stats.add(Integer.valueOf((playerData.get("globalStats.skill_3b").toString())));
            stats.add(Integer.valueOf((playerData.get("globalStats.skill_3c").toString())));
            stats.add(Integer.valueOf((playerData.get("globalStats.skill_M").toString())));
            stats.add(Integer.valueOf((playerData.get("globalStats.flintToggle").toString())));
            stats.add(Integer.valueOf((playerData.get("globalStats.oreToggle").toString())));
            stats.add(Integer.valueOf((playerData.get("globalStats.speedToggle").toString())));
            stats.add(Integer.valueOf((playerData.get("globalStats.potionToggle").toString())));
            stats.add(Integer.valueOf((playerData.get("globalStats.grappleToggle").toString())));
            stats.add(Integer.valueOf((playerData.get("globalStats.hotRodToggle").toString())));
            stats.add(Integer.valueOf((playerData.get("globalStats.veinMinerToggle").toString())));
            stats.add(Integer.valueOf((playerData.get("globalStats.megaDigToggle").toString())));
            stats.add(Integer.valueOf((playerData.get("globalStats.souls").toString())));
            stats.add(Integer.valueOf((playerData.get("globalStats.levelUpMessageToggle").toString())));
            stats.add(Integer.valueOf((playerData.get("globalStats.abilityPrepareMessageToggle").toString())));
            statsMap.put("global", stats);

            for (int i = 0; i < labels.length; i++) {
                String skillName = labels[i];
                ArrayList<Number> statsi = new ArrayList<Number>();
                statsi.add(Integer.valueOf((playerData.get(labels[i] + ".level").toString())));
                statsi.add(Integer.valueOf((playerData.get(labels[i] + ".experience").toString())));
                statsi.add(Integer.valueOf((playerData.get(labels[i] + ".passiveTokens").toString())));
                statsi.add(Integer.valueOf((playerData.get(labels[i] + ".skillTokens").toString())));
                statsi.add(Integer.valueOf((playerData.get(labels[i] + ".passive1").toString())));
                statsi.add(Integer.valueOf((playerData.get(labels[i] + ".passive1").toString())));
                statsi.add(Integer.valueOf((playerData.get(labels[i] + ".passive3").toString())));
                statsi.add(Integer.valueOf((playerData.get(labels[i] + ".skill_1a").toString())));
                statsi.add(Integer.valueOf((playerData.get(labels[i] + ".skill_1b").toString())));
                statsi.add(Integer.valueOf((playerData.get(labels[i] + ".skill_2a").toString())));
                statsi.add(Integer.valueOf((playerData.get(labels[i] + ".skill_2b").toString())));
                statsi.add(Integer.valueOf((playerData.get(labels[i] + ".skill_3a").toString())));
                statsi.add(Integer.valueOf((playerData.get(labels[i] + ".skill_3b").toString())));
                statsi.add(Integer.valueOf((playerData.get(labels[i] + ".skill_M").toString())));
                statsMap.put(skillName, statsi);
            }

        }
    return statsMap;
    }
    public void setPlayerStatsMap(Player p) throws IOException {
        PlayerStats pStatClass = new PlayerStats(p);
        Map<String, ArrayList<Number>> pStatAll = pStatClass.getPlayerData();
        String pName = p.getName();
        UUID pUUID = p.getUniqueId();
        File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("FreeRPG").getDataFolder(), File.separator + "PlayerDatabase");
        File f = new File(userdata, File.separator + pUUID.toString() + ".yml");
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
        if(f.exists()) {
            playerData.set("general.username", pName);
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
                }
                }
            playerData.save(f);
            System.out.println("Saved " + pName + " stats successfully");
            }
    }
}
