package mc.carlton.freerpg.core.serverFileManagement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.core.info.player.Leaderboards;
import mc.carlton.freerpg.core.info.player.PlayerLeaderboardStat;
import org.apache.logging.log4j.Level;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class LeaderBoardFilesManager {

  static File leaderBoardsYML;
  Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);

  public void initializePlayerFile() {
    File serverData = new File(plugin.getDataFolder(), File.separator + "ServerData");
    File f = new File(serverData, "leaderboards.yml");
    f.setReadable(true, false);
    f.setWritable(true, false);
    leaderBoardsYML = f;
  }

  public void writeOutPlayerLeaderBoardFile() {
    //Each time we write the leaderboard file, we want to make the yaml config from scratch so we don't include
    //Player's whose files have been deleted. They could linger and mess up rankings order (probably not, but let's be safe,
    // as it costs no extra computation)
    YamlConfiguration newYamlConfiguration = new YamlConfiguration();
    HashSet<UUID> allPlayerUUIDs = getHashSetOfAllPlayerUUIDs();
    Leaderboards leaderboards = new Leaderboards();
    leaderboards.removeForbiddenUUIDs(
        allPlayerUUIDs); //Get rid of players no longer stored in player files
    Map<String, ArrayList<PlayerLeaderboardStat>> allLeaderboards = leaderboards.getLeaderboards();
    for (String skillName : allLeaderboards.keySet()) {
      ArrayList<PlayerLeaderboardStat> leaderboard = (ArrayList<PlayerLeaderboardStat>) allLeaderboards.get(
          skillName).clone(); //We clone here to avoid concurrent modification exception
      newYamlConfiguration.createSection(skillName);
      if (skillName.equalsIgnoreCase("global")) {
        for (int i = 0; i < leaderboard.size(); i++) {
          PlayerLeaderboardStat player = leaderboard.get(i);
          String section = skillName + "." + i;
          newYamlConfiguration.createSection(section);
          newYamlConfiguration.set(section + ".UUID", player.get_playerUUID().toString());
          newYamlConfiguration.set(section + ".playerName", player.get_pName());
          newYamlConfiguration.set(section + ".level", player.get_sortedStat());
          newYamlConfiguration.set(section + ".totalExperience", player.get_stat2());
        }
      } else if (skillName.equalsIgnoreCase("playTime")) {
        for (int i = 0; i < leaderboard.size(); i++) {
          PlayerLeaderboardStat player = leaderboard.get(i);
          String section = skillName + "." + i;
          newYamlConfiguration.createSection(section);
          newYamlConfiguration.set(section + ".UUID", player.get_playerUUID().toString());
          newYamlConfiguration.set(section + ".playerName", player.get_pName());
          newYamlConfiguration.set(section + ".totalPlayTime", player.get_sortedStat());
        }
      } else {
        for (int i = 0; i < leaderboard.size(); i++) {
          PlayerLeaderboardStat player = leaderboard.get(i);
          String section = skillName + "." + i;
          newYamlConfiguration.createSection(section);
          newYamlConfiguration.set(section + ".UUID", player.get_playerUUID().toString());
          newYamlConfiguration.set(section + ".playerName", player.get_pName());
          newYamlConfiguration.set(section + ".experience", player.get_sortedStat());
        }
      }
    }
    try {
      newYamlConfiguration.save(leaderBoardsYML);
    } catch (IOException e) {
      FreeRPG.log(Level.ERROR, e.getMessage());
    }
  }

  public HashSet<UUID> getHashSetOfAllPlayerUUIDs() {
    // ATTENTION
    // THIS NEEDS TO BE CHANGED WHEN MYSQL SUPPORT IS ADDED
    // OTHERWISE, LEADERBOARD STATS WILL NOT SAVE PROPERLY
    File userdata = new File(plugin.getDataFolder(), File.separator + "PlayerDatabase");
    File[] allUsers = userdata.listFiles();
    HashSet<UUID> allPlayerUUIDs = new HashSet<>();
    for (File userFile : allUsers) {
      UUID playerUUID = UUID.fromString(userFile.getName().substring(0, 36));
      allPlayerUUIDs.add(playerUUID);
    }
    return allPlayerUUIDs;
  }

  public void readInLeaderBoardFile() {
    YamlConfiguration leaderboardsConfig = YamlConfiguration.loadConfiguration(leaderBoardsYML);
    Leaderboards leaderboards = new Leaderboards();
    for (String skillName : leaderboardsConfig.getKeys(false)) {
      for (String playerRank : leaderboardsConfig.getConfigurationSection(skillName)
          .getKeys(false)) {
        String rankKey = skillName + "." + playerRank;
        if (skillName.equalsIgnoreCase("global")) {
          UUID playerUUID = UUID.fromString(leaderboardsConfig.getString(rankKey + ".UUID"));
          String playerName = leaderboardsConfig.getString(rankKey + ".playerName");
          int level = leaderboardsConfig.getInt(rankKey + ".level");
          int totalExperience = leaderboardsConfig.getInt(rankKey + ".totalExperience");
          leaderboards.addPlayerGlobalStat(playerUUID, playerName, level, totalExperience);
        } else if (skillName.equalsIgnoreCase("playTime")) {
          UUID playerUUID = UUID.fromString(leaderboardsConfig.getString(rankKey + ".UUID"));
          String playerName = leaderboardsConfig.getString(rankKey + ".playerName");
          long totalPlayTime = leaderboardsConfig.getLong(rankKey + ".totalPlayTime");
          leaderboards.addPlayerTimeStat(playerUUID, playerName, totalPlayTime);
        } else {
          UUID playerUUID = UUID.fromString(leaderboardsConfig.getString(rankKey + ".UUID"));
          String playerName = leaderboardsConfig.getString(rankKey + ".playerName");
          int experience = leaderboardsConfig.getInt(rankKey + ".experience");
          leaderboards.addPlayerSkillStat(playerUUID, playerName, experience, skillName);
        }
      }
    }
    leaderboards.sortAllLeaderBoards(true); //Sorts all leaderboards
    leaderboards.setLeaderboardsLoaded(true);
  }

  public void deleteAbsentPlayersFromFile() { //This is likely best run aysnc
    deleteLeaderBoardFile();
    PeriodicSaving periodicSaving = new PeriodicSaving();
    periodicSaving.saveAllStats(false);
    createLeaderBoardFile(
        true); //We just recreate the file, it will only include players in the directory
  }

  public void deleteLeaderBoardFile() {
    leaderBoardsYML.delete();
  }

  public boolean createLeaderBoardFile(
      boolean forceCreate) { //This creates a leaderboard file using every player's file on the server, it may take a while to execute
    initializePlayerFile();
    if (!leaderBoardsYML.exists() || forceCreate) {
      FreeRPG.log(Level.INFO, "[FreeRPG] Creating Leaderboard File, this may take a while...");
      //Load playerLeaderBoard class and set it to false (since this will likely be done async)
      Leaderboards leaderboards = new Leaderboards();
      leaderboards.setLeaderboardsLoaded(false);

      File userdata = new File(plugin.getDataFolder(), File.separator + "PlayerDatabase");
      File[] allUsers = userdata.listFiles();
      YamlConfiguration leaderboardConfig = YamlConfiguration.loadConfiguration(leaderBoardsYML);
      List<String> leaderboardNames = leaderboards.getLeaderboardNames();

      for (String skillName : leaderboardNames) {
        leaderboardConfig.createSection(skillName);
      }
      for (File userFile : allUsers) { //First we must get the stats of all users and create an arraylist "leaderboard"
        Object[] outputOfPlayerDataLoad = loadPlayerDataFromFile(userFile);
        YamlConfiguration playerData = (YamlConfiguration) outputOfPlayerDataLoad[0];
        UUID playerUUID = (UUID) outputOfPlayerDataLoad[1];
        for (String skillName : leaderboardNames) {
          if (skillName.equalsIgnoreCase("global")) {
            String playerName = playerData.getString("general.username");
            int level = playerData.getInt("globalStats.totalLevel");
            int totalExperience = playerData.getInt("globalStats.totalExperience");
            leaderboards.addPlayerGlobalStat(playerUUID, playerName, level, totalExperience);

          } else if (skillName.equalsIgnoreCase("playTime")) {
            String playerName = playerData.getString("general.username");
            long playTime = playerData.getLong("general.playTime");
            leaderboards.addPlayerTimeStat(playerUUID, playerName, playTime);
          } else {
            String playerName = playerData.getString("general.username");
            int experience = playerData.getInt(skillName + ".experience");
            leaderboards.addPlayerSkillStat(playerUUID, playerName, experience, skillName);
          }
        }
      }
      //Now that all the player's information is added to the PlayerLeaderboardClass, we can sort the data
      addDataToFile(leaderBoardsYML,
          leaderboardConfig); //Adds data to leaderboards yaml configuration and saves
      leaderboards.setLeaderboardsLoaded(true);
      FreeRPG.log(Level.INFO, "[FreeRPG] leaderboards.yml created successfully!");
      return true;
    }
    return false;

  }

  public void addDataToFile(File f, YamlConfiguration leaderboardsConfig) {
    Leaderboards leaderboards = new Leaderboards();
    List<String> leaderboardNames = leaderboards.getLeaderboardNames();
    leaderboards.sortAllLeaderBoards(true);
    //Once it's sorted, we can iterate through every leaderboard
    for (String skillName : leaderboardNames) {
      ArrayList<PlayerLeaderboardStat> leaderboard = leaderboards.getLeaderboard(skillName);
      if (skillName.equalsIgnoreCase("global")) {
        for (int i = 0; i < leaderboard.size(); i++) { //Index each leaderboard player
          addPlayerSkillStatsToLeaderBoardGlobal(leaderboardsConfig, i, leaderboard);
        }
      } else if (skillName.equalsIgnoreCase("playTime")) {
        for (int i = 0; i < leaderboard.size(); i++) { //Index each leaderboard player
          addPlayerSkillStatsToLeaderBoardTime(leaderboardsConfig, i, leaderboard);
        }
      } else {
        for (int i = 0; i < leaderboard.size(); i++) { //Index each leaderboard player
          addPlayerSkillStatsToLeaderBoard(leaderboardsConfig, skillName, i, leaderboard);
        }
      }
    }
    try {
      leaderboardsConfig.save(f);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public void addPlayerSkillStatsToLeaderBoard(YamlConfiguration leaderBoardYAML, String skillName,
      int position, ArrayList<PlayerLeaderboardStat> leaderboard) {
    PlayerLeaderboardStat player = leaderboard.get(position);
    UUID playerUUID = player.get_playerUUID();
    String playerName = player.get_pName();
    int experience = (int) player.get_sortedStat();
    String section = skillName + "." + position;
    if (!leaderBoardYAML.contains(section)) {
      leaderBoardYAML.createSection(section);
    }
    leaderBoardYAML.set(section + ".UUID", playerUUID.toString());
    leaderBoardYAML.set(section + ".playerName", playerName);
    leaderBoardYAML.set(section + ".experience", experience);
  }

  public void addPlayerSkillStatsToLeaderBoardGlobal(YamlConfiguration leaderBoardYAML,
      int position, ArrayList<PlayerLeaderboardStat> leaderboard) {
    PlayerLeaderboardStat player = leaderboard.get(position);
    UUID playerUUID = player.get_playerUUID();
    String playerName = player.get_pName();
    int level = (int) player.get_sortedStat();
    int totalExperience = (int) player.get_stat2();
    String section = "global" + "." + position;
    if (!leaderBoardYAML.contains(section)) {
      leaderBoardYAML.createSection(section);
    }
    leaderBoardYAML.set(section + ".UUID", playerUUID.toString());
    leaderBoardYAML.set(section + ".playerName", playerName);
    leaderBoardYAML.set(section + ".level", level);
    leaderBoardYAML.set(section + ".totalExperience", totalExperience);
  }

  public void addPlayerSkillStatsToLeaderBoardTime(YamlConfiguration leaderBoardYAML, int position,
      ArrayList<PlayerLeaderboardStat> leaderboard) {
    PlayerLeaderboardStat player = leaderboard.get(position);
    UUID playerUUID = player.get_playerUUID();
    String playerName = player.get_pName();
    long playTime = (long) player.get_sortedStat();
    String section = "playTime" + "." + position;
    if (!leaderBoardYAML.contains(section)) {
      leaderBoardYAML.createSection(section);
    }
    leaderBoardYAML.set(section + ".UUID", playerUUID.toString());
    leaderBoardYAML.set(section + ".playerName", playerName);
    leaderBoardYAML.set(section + ".totalPlayTime", playTime);
  }

  public Object[] loadPlayerDataFromFile(File userFile) {
    PlayerFilesManager playerFilesManager = new PlayerFilesManager();
    UUID playerUUID = UUID.fromString(userFile.getName().substring(0, 36));
    File f1 = playerFilesManager.getPlayerFile(playerUUID);
    YamlConfiguration playerData = YamlConfiguration.loadConfiguration(f1);
    return new Object[]{playerData, playerUUID};
  }

}
