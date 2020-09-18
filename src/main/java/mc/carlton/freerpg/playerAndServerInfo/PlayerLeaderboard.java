package mc.carlton.freerpg.playerAndServerInfo;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

public class PlayerLeaderboard {

    public ArrayList<HeldStats> getLeaders(String skillName) {
        ArrayList<HeldStats> allPlayers = new ArrayList<>();
        File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("FreeRPG").getDataFolder(), File.separator + "PlayerDatabase");
        File[] allUsers = userdata.listFiles();
        for (File f : allUsers) {
            long time = System.currentTimeMillis();
            f.setReadable(true,false);
            f.setWritable(true,false);
            FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
            String username = (String) playerData.get("general.username");
            String uuidString = f.getName().substring(0,36);
            UUID playerUUID = UUID.fromString(uuidString);
            if (!skillName.equals("global")) {
                int level = (int) playerData.get(skillName + ".level");
                int exp = (int) playerData.get(skillName + ".experience");
                if (level > 0) {
                    allPlayers.add(new HeldStats(username, level, exp));
                }
            }
            else {
                int level = (int) playerData.get("globalStats.totalLevel");
                if (level > 0) {
                    allPlayers.add(new HeldStats(username, level, level));
                }
            }
            //System.out.println(System.currentTimeMillis()-time);
        }
        allPlayers.sort(new Comparator<HeldStats>() {
            @Override
            public int compare(HeldStats o1, HeldStats o2) {
                if (o1.get_experience() < o2.get_experience()) {
                    return 1;
                }
                else {
                    return -1;
                }
            }
        });


        return allPlayers;
    }
}
