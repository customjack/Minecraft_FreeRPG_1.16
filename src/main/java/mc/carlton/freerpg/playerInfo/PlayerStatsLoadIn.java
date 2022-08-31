package mc.carlton.freerpg.playerInfo;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.configStorage.ConfigLoad;
import mc.carlton.freerpg.serverFileManagement.PlayerFilesManager;
import org.apache.logging.log4j.Level;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerStatsLoadIn {

  Map<String, ArrayList<Number>> statsMap = new HashMap<String, ArrayList<Number>>();
  File f;
  UUID pUUID;
  FileConfiguration playerData;
  private Player p;

  public PlayerStatsLoadIn(Player player) {
    PlayerFilesManager playerFilesManager = new PlayerFilesManager();
    this.p = player;
    this.pUUID = p.getUniqueId();
    this.f = playerFilesManager.getPlayerFile(p);
    this.playerData = YamlConfiguration.loadConfiguration(f);
  }

  public PlayerStatsLoadIn(UUID pUUID) {
    PlayerFilesManager playerFilesManager = new PlayerFilesManager();
    if (Bukkit.getPlayer(pUUID) != null) {
      this.p = Bukkit.getPlayer(pUUID);
    }
    this.pUUID = pUUID;
    this.f = playerFilesManager.getPlayerFile(pUUID);
    this.playerData = YamlConfiguration.loadConfiguration(f);
  }

  public long getLoginTime() {
    long loginTime = 0;
    if (f.exists()) {
      long unixTime = Instant.now().getEpochSecond();
      loginTime = Long.valueOf(getStat("general.lastLogin", unixTime).toString());
      return loginTime;
    }
    return Instant.now().getEpochSecond();
  }

  public long getPlayTime() {
    long playTime = 0;
    if (f.exists()) {
      playTime = Long.valueOf(getStat("general.playTime", 0L).toString());
      return playTime;
    }
    return Instant.now().getEpochSecond();
  }

  public String getPlayerLanguage() {
    String language = "enUs";
    if (f.exists()) {
      String defaultLanguage = new ConfigLoad().getDefaultLanguage();
      language = String.valueOf(getInformation("general.language", defaultLanguage));
      return language;
    }
    return language;
  }

  public Map<String, Integer> getSkillExpBarToggles() {
    Map<String, Integer> skillExpBarToggleMap = new HashMap<>();
    String[] labels = {"digging", "woodcutting", "mining", "farming", "fishing", "archery",
        "beastMastery", "swordsmanship", "defense", "axeMastery", "repair", "agility", "alchemy",
        "smelting", "enchanting"};
    for (String label : labels) {
      skillExpBarToggleMap.put(label, (Integer) getStat(label + ".showEXPBarToggle", 1));
    }
    return skillExpBarToggleMap;
  }

  public Map<String, Integer> getSkillAbilityToggles() {
    Map<String, Integer> skillAbilityToggleMap = new HashMap<>();
    String[] labels = {"digging", "woodcutting", "mining", "farming", "fishing", "archery",
        "beastMastery", "swordsmanship", "defense", "axeMastery", "repair", "agility", "alchemy",
        "smelting", "enchanting"};
    for (String label : labels) {
      skillAbilityToggleMap.put(label, (Integer) getStat(label + ".triggerAbilityToggle", 1));
    }
    return skillAbilityToggleMap;
  }

  public Map<String, ArrayList<Number>> getPlayerStatsMapFromFile() {
    readInPlayerStats();

        /*

        // There might be some problems in the future, so I'm keeping this block of code

        if(f.exists()) {

        }
        else { //If for some reason, the player file doesn't exist, we still want the stat map.
            if (p != null) {
                PlayerFilesManager playerFilesManager = new PlayerFilesManager();
                this.f = playerFilesManager.getPlayerFile(pUUID);
                this.playerData = YamlConfiguration.loadConfiguration(f);
                PlayerStatsFilePreparation playerStatsFilePreparation = new PlayerStatsFilePreparation();
                playerStatsFilePreparation.preparePlayerFile(p.getName(),pUUID,true);
                return getPlayerStatsMapFromFile();
            }
        }

         */

    return statsMap;
  }

  public void readInPlayerStats() {
    String[] labels = {"digging", "woodcutting", "mining", "farming", "fishing", "archery",
        "beastMastery", "swordsmanship", "defense", "axeMastery", "repair", "agility", "alchemy",
        "smelting", "enchanting"};
    ArrayList<Number> globalStats = new ArrayList<Number>();
    addStat(globalStats, "globalStats.totalLevel", 0);                   //0
    addStat(globalStats, "globalStats.globalTokens", 0);                 //1
    addStat(globalStats, "globalStats.skill_1a", 0);                     //2
    addStat(globalStats, "globalStats.skill_1b", 0);                     //3
    addStat(globalStats, "globalStats.skill_1c", 0);                     //4
    addStat(globalStats, "globalStats.skill_2a", 0);                     //5
    addStat(globalStats, "globalStats.skill_2b", 0);                     //6
    addStat(globalStats, "globalStats.skill_2c", 0);                     //7
    addStat(globalStats, "globalStats.skill_3a", 0);                     //8
    addStat(globalStats, "globalStats.skill_3b", 0);                     //9
    addStat(globalStats, "globalStats.skill_3c", 0);                     //10
    addStat(globalStats, "globalStats.skill_M", 0);                      //11
    addStat(globalStats, "globalStats.flintToggle", 1);                  //12
    addStat(globalStats, "globalStats.oreToggle", 1);                    //13
    addStat(globalStats, "globalStats.speedToggle", 1);                  //14
    addStat(globalStats, "globalStats.potionToggle", 1);                 //15
    addStat(globalStats, "globalStats.grappleToggle", 1);                //16
    addStat(globalStats, "globalStats.hotRodToggle", 1);                 //17
    addStat(globalStats, "globalStats.veinMinerToggle", 1);              //18
    addStat(globalStats, "globalStats.megaDigToggle", 1);                //19
    addStat(globalStats, "globalStats.souls", 0);                        //20
    addStat(globalStats, "globalStats.levelUpMessageToggle", 1);         //21
    addStat(globalStats, "globalStats.abilityPrepareMessageToggle", 1);  //22
    addStat(globalStats, "globalStats.personalEXPMultiplier", 1.0);      //23
    addStat(globalStats, "globalStats.triggerAbilitiesToggle", 1);       //24
    addStat(globalStats, "globalStats.showEXPBarToggle", 1);             //25
    addStat(globalStats, "globalStats.leafBlowerToggle", 1);             //26
    addStat(globalStats, "globalStats.holyAxeToggle", 1);                //27
    addStat(globalStats, "globalStats.numberOfCooldownBars", 1);         //28
    addStat(globalStats, "globalStats.totalExperience", 0);              //29
    addStat(globalStats, "globalStats.heartyToggle", 0);                //30

    statsMap.put("global", globalStats);

    for (int i = 0; i < labels.length; i++) {
      String skillName = labels[i];
      ArrayList<Number> skillStats = new ArrayList<Number>();
      addStat(skillStats, labels[i] + ".level", 0);          //0
      addStat(skillStats, labels[i] + ".experience", 0);     //1
      addStat(skillStats, labels[i] + ".passiveTokens", 0);  //2
      addStat(skillStats, labels[i] + ".skillTokens", 0);    //3
      addStat(skillStats, labels[i] + ".passive1", 0);       //4
      addStat(skillStats, labels[i] + ".passive2", 0);       //5
      addStat(skillStats, labels[i] + ".passive3", 0);       //6
      addStat(skillStats, labels[i] + ".skill_1a", 0);       //7
      addStat(skillStats, labels[i] + ".skill_1b", 0);       //8
      addStat(skillStats, labels[i] + ".skill_2a", 0);       //9
      addStat(skillStats, labels[i] + ".skill_2b", 0);       //10
      addStat(skillStats, labels[i] + ".skill_3a", 0);       //11
      addStat(skillStats, labels[i] + ".skill_3b", 0);       //12
      addStat(skillStats, labels[i] + ".skill_M", 0);        //13
      statsMap.put(skillName, skillStats);
    }
  }

  private void addStat(ArrayList<Number> statList, String path,
      Number defaultValue) { //This exists because I am dumb
    statList.add(getStat(path, defaultValue));
  }

  private Number getStat(String path, Number defaultValue) {
    if (playerData.contains(path)) {
      return (Number) playerData.get(path);
    } else {
      return defaultValue;
    }
  }

  private Object getInformation(String path, Object defaultValue) { //General version of getStat
    if (playerData.contains(path)) {
      return playerData.get(path);
    } else {
      return defaultValue;
    }
  }

  public void setPlayerStatsMap() throws IOException {
    setPlayerStatsMap(true);
  }

  public void setPlayerStatsMap(boolean savePlayTime) throws IOException {
    PlayerStats pStatClass = new PlayerStats(pUUID);
    Map<String, ArrayList<Number>> pStatAll = pStatClass.getPlayerData();
    Map<String, Integer> expBarToggles = pStatClass.getSkillToggleExpBar();
    Map<String, Integer> abilityToggles = pStatClass.getSkillToggleAbility();
    String pName;
    if (p != null) {
      pName = p.getName();
    } else {
      pName = null;
    }
    long unixTime = Instant.now().getEpochSecond();
    if (f.exists()) {
      if (pName != null) {
        playerData.set("general.username", pName);
      }

      //Setting playTime in seconds
      if (savePlayTime) {
        playerData.set("general.lastLogout", unixTime);
        long lastLoginTime = pStatClass.getPlayerLoginTime();
        long playTime = unixTime - lastLoginTime;
        playerData.set("general.playTime", playTime);
      }

      //Setting player Language
      String playerLanguage = pStatClass.getPlayerLanguage();
      playerData.set("general.language", playerLanguage);

      for (String i : pStatAll.keySet()) {
        if (i.equalsIgnoreCase("global")) {
          playerData.set("globalStats.totalLevel", pStatAll.get(i).get(0));
          playerData.set("globalStats.globalTokens", pStatAll.get(i).get(1));
          playerData.set("globalStats.skill_1a", pStatAll.get(i).get(2));
          playerData.set("globalStats.skill_1b", pStatAll.get(i).get(3));
          playerData.set("globalStats.skill_1c", pStatAll.get(i).get(4));
          playerData.set("globalStats.skill_2a", pStatAll.get(i).get(5));
          playerData.set("globalStats.skill_2b", pStatAll.get(i).get(6));
          playerData.set("globalStats.skill_2c", pStatAll.get(i).get(7));
          playerData.set("globalStats.skill_3a", pStatAll.get(i).get(8));
          playerData.set("globalStats.skill_3b", pStatAll.get(i).get(9));
          playerData.set("globalStats.skill_3c", pStatAll.get(i).get(10));
          playerData.set("globalStats.skill_M", pStatAll.get(i).get(11));
          playerData.set("globalStats.flintToggle", pStatAll.get(i).get(12));
          playerData.set("globalStats.oreToggle", pStatAll.get(i).get(13));
          playerData.set("globalStats.speedToggle", pStatAll.get(i).get(14));
          playerData.set("globalStats.potionToggle", pStatAll.get(i).get(15));
          playerData.set("globalStats.grappleToggle", pStatAll.get(i).get(16));
          playerData.set("globalStats.hotRodToggle", pStatAll.get(i).get(17));
          playerData.set("globalStats.veinMinerToggle", pStatAll.get(i).get(18));
          playerData.set("globalStats.megaDigToggle", pStatAll.get(i).get(19));
          playerData.set("globalStats.souls", pStatAll.get(i).get(20));
          playerData.set("globalStats.levelUpMessageToggle", pStatAll.get(i).get(21));
          playerData.set("globalStats.abilityPrepareMessageToggle", pStatAll.get(i).get(22));
          playerData.set("globalStats.personalEXPMultiplier", pStatAll.get(i).get(23));
          playerData.set("globalStats.triggerAbilitiesToggle", pStatAll.get(i).get(24));
          playerData.set("globalStats.showEXPBarToggle", pStatAll.get(i).get(25));
          playerData.set("globalStats.leafBlowerToggle", pStatAll.get(i).get(26));
          playerData.set("globalStats.holyAxeToggle", pStatAll.get(i).get(27));
          playerData.set("globalStats.numberOfCooldownBars", pStatAll.get(i).get(28));
          playerData.set("globalStats.totalExperience", pStatAll.get(i).get(29));
          playerData.set("globalStats.heartyToggle", pStatAll.get(i).get(30));
        } else {
          playerData.set(i + ".level", pStatAll.get(i).get(0));
          playerData.set(i + ".experience", pStatAll.get(i).get(1));
          playerData.set(i + ".passiveTokens", pStatAll.get(i).get(2));
          playerData.set(i + ".skillTokens", pStatAll.get(i).get(3));
          playerData.set(i + ".passive1", pStatAll.get(i).get(4));
          playerData.set(i + ".passive2", pStatAll.get(i).get(5));
          playerData.set(i + ".passive3", pStatAll.get(i).get(6));
          playerData.set(i + ".skill_1a", pStatAll.get(i).get(7));
          playerData.set(i + ".skill_1b", pStatAll.get(i).get(8));
          playerData.set(i + ".skill_2a", pStatAll.get(i).get(9));
          playerData.set(i + ".skill_2b", pStatAll.get(i).get(10));
          playerData.set(i + ".skill_3a", pStatAll.get(i).get(11));
          playerData.set(i + ".skill_3b", pStatAll.get(i).get(12));
          playerData.set(i + ".skill_M", pStatAll.get(i).get(13));
          playerData.set(i + ".triggerAbilityToggle", expBarToggles.get(i));
          playerData.set(i + ".showEXPBarToggle", abilityToggles.get(i));
        }
      }
      playerData.save(f);
      PlayerStats playerStats = new PlayerStats(pUUID);
      playerStats.setPlayerAreStatsSaved(true);
      if (pName != null) {
        FreeRPG.log(Level.INFO, "[FreeRPG] Saved " + pName + " stats successfully");
      } else {
        FreeRPG.log(Level.INFO,
            "[FreeRPG] Saved player UUID " + pUUID.toString() + " stats successfully");
      }
    }
  }

  public void asyncStatSave() {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    new BukkitRunnable() {
      @Override
      public void run() {
        try {
          setPlayerStatsMap();
        } catch (IOException e) {
          FreeRPG.log(Level.ERROR, e.getMessage());
        }
      }
    }.runTaskAsynchronously(plugin);
  }
}
