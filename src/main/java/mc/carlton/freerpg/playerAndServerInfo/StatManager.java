package mc.carlton.freerpg.playerAndServerInfo;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;


public class StatManager {
    public static void playJoinConditions(Player p) {
        String pName = p.getName();
        UUID pUUID = p.getUniqueId();

        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:ss:mm");

        File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("FreeRPG").getDataFolder(), File.separator + "PlayerDatabase");
        if(!userdata.exists()){
            userdata.mkdir();
        }
        File f = new File(userdata, File.separator + pUUID.toString() + ".yml");
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);

        String[] labels = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting"};
        //When player file is created for the first time...
        if(!f.exists()){
            try{
                //Load some file creation config
                ConfigLoad loadConfig = new ConfigLoad();
                ArrayList<Double> tokensInfo = loadConfig.getTokensInfo();
                int passiveTokens0 = (int) Math.round(tokensInfo.get(4));
                int skillTokens0 = (int) Math.round(tokensInfo.get(5));
                int globalTokens0 = (int) Math.round(tokensInfo.get(6));

                //General Player infomration
                playerData.createSection("general");
                playerData.set("general.username", pName);
                playerData.set("general.firstLogin",format.getCalendar());

                //Global stats data
                playerData.createSection("globalStats");
                playerData.set("globalStats.totalLevel", 0);
                playerData.set("globalStats.globalTokens",globalTokens0);
                playerData.set("globalStats.skill_1a",0);
                playerData.set("globalStats.skill_1b",0);
                playerData.set("globalStats.skill_1c",0);
                playerData.set("globalStats.skill_2a",0);
                playerData.set("globalStats.skill_2b",0);
                playerData.set("globalStats.skill_2c",0);
                playerData.set("globalStats.skill_3a",0);
                playerData.set("globalStats.skill_3b",0);
                playerData.set("globalStats.skill_3c",0);
                playerData.set("globalStats.skill_M",0);
                playerData.set("globalStats.flintToggle",1);
                playerData.set("globalStats.oreToggle",1);
                playerData.set("globalStats.speedToggle",1);
                playerData.set("globalStats.potionToggle",1);
                playerData.set("globalStats.grappleToggle",1);
                playerData.set("globalStats.hotRodToggle",1);
                playerData.set("globalStats.veinMinerToggle",1);
                playerData.set("globalStats.megaDigToggle",1);
                playerData.set("globalStats.souls",1);
                playerData.set("globalStats.levelUpMessageToggle",1);
                playerData.set("globalStats.abilityPrepareMessageToggle",1);

                // Skill Type Data
                for (int i = 0; i < labels.length; i++) {
                    playerData.createSection(labels[i] + "");
                    playerData.set(labels[i] + ".level", 0);
                    playerData.set(labels[i] + ".experience", 0);
                    playerData.set(labels[i] + ".passiveTokens", passiveTokens0);
                    playerData.set(labels[i] + ".skillTokens", skillTokens0);
                    playerData.set(labels[i] + ".passive1", 0);
                    playerData.set(labels[i] + ".passive2", 0);
                    playerData.set(labels[i] + ".passive3", 0);
                    playerData.set(labels[i] + ".skill_1a", 0);
                    playerData.set(labels[i] + ".skill_1b", 0);
                    playerData.set(labels[i] + ".skill_2a", 0);
                    playerData.set(labels[i] + ".skill_2b", 0);
                    playerData.set(labels[i] + ".skill_3a", 0);
                    playerData.set(labels[i] + ".skill_3b", 0);
                    playerData.set(labels[i] + ".skill_M", 0);
                }
                playerData.save(f);
            } catch (IOException exception){
                exception.printStackTrace();
            }
        }
        else {
            try{
                //General Player information
                if (!playerData.contains("general")) {
                    playerData.createSection("general");
                }
                if (!playerData.contains("general.username") || !p.getDisplayName().equals(pName) ) {
                    playerData.set("general.username", pName);
                }
                if (!playerData.contains("general.firstLogin")) {
                    playerData.set("general.firstLogin", format.getCalendar());
                }

                //Global stats data
                if (!playerData.contains("globalStats")) {
                   playerData.createSection("globalStats");
                }
                if (!playerData.contains("globalStats.totalLevel")) {
                    playerData.set("globalStats.totalLevel", 0);
                }
                if (!playerData.contains("globalStats.globalTokens")) {
                    playerData.set("globalStats.globalTokens", 0);
                }
                if (!playerData.contains("globalStats.skill_1a")) {
                    playerData.set("globalStats.skill_1a", 0);
                }
                if (!playerData.contains("globalStats.skill_1b")) {
                    playerData.set("globalStats.skill_1b", 0);
                }
                if (!playerData.contains("globalStats.skill_1c")) {
                    playerData.set("globalStats.skill_1c", 0);
                }
                if (!playerData.contains("globalStats.skill_2a")) {
                    playerData.set("globalStats.skill_2a", 0);
                }
                if (!playerData.contains("globalStats.skill_2b")) {
                    playerData.set("globalStats.skill_2b", 0);
                }
                if (!playerData.contains("globalStats.skill_2c")) {
                    playerData.set("globalStats.skill_2c", 0);
                }
                if (!playerData.contains("globalStats.skill_3a")) {
                    playerData.set("globalStats.skill_3a", 0);
                }
                if (!playerData.contains("globalStats.skill_3b")) {
                    playerData.set("globalStats.skill_3b", 0);
                }
                if (!playerData.contains("globalStats.skill_3c")) {
                    playerData.set("globalStats.skill_3c", 0);
                }
                if (!playerData.contains("globalStats.skill_M")) {
                    playerData.set("globalStats.skill_M", 0);
                }
                if (!playerData.contains("globalStats.flintToggle")) {
                    playerData.set("globalStats.flintToggle", 1);
                }
                if (!playerData.contains("globalStats.oreToggle")) {
                    playerData.set("globalStats.oreToggle", 1);
                }
                if (!playerData.contains("globalStats.speedToggle")) {
                    playerData.set("globalStats.speedToggle", 1);
                }
                if (!playerData.contains("globalStats.potionToggle")) {
                    playerData.set("globalStats.potionToggle", 1);
                }
                if (!playerData.contains("globalStats.grappleToggle")) {
                    playerData.set("globalStats.grappleToggle", 1);
                }
                if (!playerData.contains("globalStats.hotRodToggle")) {
                    playerData.set("globalStats.hotRodToggle", 1);
                }
                if (!playerData.contains("globalStats.veinMinerToggle")) {
                    playerData.set("globalStats.veinMinerToggle", 1);
                }
                if (!playerData.contains("globalStats.megaDigToggle")) {
                    playerData.set("globalStats.megaDigToggle", 1);
                }
                if (!playerData.contains("globalStats.souls")) {
                    playerData.set("globalStats.souls", 0);
                }
                if (!playerData.contains("globalStats.levelUpMessageToggle")) {
                    playerData.set("globalStats.levelUpMessageToggle", 1);
                }
                if (!playerData.contains("globalStats.abilityPrepareMessageToggle")) {
                    playerData.set("globalStats.abilityPrepareMessageToggle", 1);
                }

                // Skill Type Data
                for (int i = 0; i < labels.length; i++) {
                    if (!playerData.contains(labels[i] + "")) {
                        playerData.createSection(labels[i] + "");
                    }
                    if (!playerData.contains(labels[i] + ".level")) {
                        playerData.set(labels[i] + ".level", 0);
                    }
                    if (!playerData.contains(labels[i] + ".experience")) {
                        playerData.set(labels[i] + ".experience", 0);
                    }
                    if (!playerData.contains(labels[i] + ".passiveTokens")) {
                        playerData.set(labels[i] + ".passiveTokens", 0);
                    }
                    if (!playerData.contains(labels[i] + ".skillTokens")) {
                        playerData.set(labels[i] + ".skillTokens", 0);
                    }
                    if (!playerData.contains(labels[i] + ".passive1")) {
                        playerData.set(labels[i] + ".passive1", 0);
                    }
                    if (!playerData.contains(labels[i] + ".passive2")) {
                        playerData.set(labels[i] + ".passive2", 0);
                    }
                    if (!playerData.contains(labels[i] + ".passive3")) {
                        playerData.set(labels[i] + ".passive3", 0);
                    }
                    if (!playerData.contains(labels[i] + ".skill_1a")) {
                        playerData.set(labels[i] + ".skill_1a", 0);
                    }
                    if (!playerData.contains(labels[i] + ".skill_1b")) {
                        playerData.set(labels[i] + ".skill_1b", 0);
                    }
                    if (!playerData.contains(labels[i] + ".skill_2a")) {
                        playerData.set(labels[i] + ".skill_2a", 0);
                    }
                    if (!playerData.contains(labels[i] + ".skill_2b")) {
                        playerData.set(labels[i] + ".skill_2b", 0);
                    }
                    if (!playerData.contains(labels[i] + ".skill_3a")) {
                        playerData.set(labels[i] + ".skill_3a", 0);
                    }
                    if (!playerData.contains(labels[i] + ".skill_3b")) {
                        playerData.set(labels[i] + ".skill_3b", 0);
                    }
                    if (!playerData.contains(labels[i] + ".skill_M")) {
                        playerData.set(labels[i] + ".skill_M", 0);
                    }
                }

                playerData.save(f);
            } catch (IOException exception){
                exception.printStackTrace();
            }
        }

    }
}
