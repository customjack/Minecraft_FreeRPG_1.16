package mc.carlton.freerpg.core.info.player;

import java.util.ArrayList;
import java.util.UUID;
import mc.carlton.freerpg.config.ConfigLoad;

public class PlayerLeaderboardStat {

  String pName;
  UUID playerUUID;
  Number sortedStat;
  Object stat2;

  //This is used for all skills
  public PlayerLeaderboardStat(UUID playerUUID, String pName,
      int experience) { //By default, levelSupplied = false
    super();
    this.playerUUID = playerUUID;
    this.pName = pName;
    this.sortedStat = experience;
    this.stat2 = getLevelfromEXP(experience);
  }

  public PlayerLeaderboardStat(UUID playerUUID, String pName, Number sortedStat,
      Object stat2) { //Used for global stats
    super();
    this.playerUUID = playerUUID;
    this.pName = pName;
    this.sortedStat = sortedStat;
    this.stat2 = stat2;
  }

  public PlayerLeaderboardStat(UUID playerUUID, String pName,
      Number sortedStat) { //By default, levelSupplied = false
    super();
    this.playerUUID = playerUUID;
    this.pName = pName;
    this.sortedStat = sortedStat;
  }


  public void updateStats(Number sortedStat, Object stat2) {
    this.sortedStat = sortedStat;
    this.stat2 = stat2;
  }

  public void updateStats(Number sortedStat) {
    this.sortedStat = sortedStat;
  }

  public String get_pName() {
    return this.pName;
  }

  public void set_pName(String pName) {
    this.pName = pName;
  }

  public UUID get_playerUUID() {
    return this.playerUUID;
  }

  public void set_playerUUID(UUID playerUUID) {
    this.playerUUID = playerUUID;
  }

  public Number get_sortedStat() {
    return this.sortedStat;
  }

  public void set_sortedStat(Number sortedStat) {
    this.sortedStat = sortedStat;
  }

  public Object get_stat2() {
    return this.stat2;
  }

  public void set_stat2(Object stat2) {
    this.stat2 = stat2;
  }

  public String get_playTimeString() {
    if (sortedStat instanceof Integer || sortedStat instanceof Long) {
      return getPlayerPlayTimeString(sortedStat.intValue());
    } else {
      return null;
    }
  }

  public int getLevelfromEXP(int exp) {
    ConfigLoad loadConfig = new ConfigLoad();
    ArrayList<Double> levelingInfo = loadConfig.getLevelingInfo();
    double B = levelingInfo.get(1);
    int referenceLevel = (int) Math.round(levelingInfo.get(2));
    int referenceEXP = (int) Math.round(levelingInfo.get(3));
    int linearStartingLevel = (int) Math.round(levelingInfo.get(4));
    int linearEXP = (int) Math.round(levelingInfo.get(5));
    int exponentialMaxEXP = getEXPfromLevel(linearStartingLevel);
    int level = 0;
    if (exp <= exponentialMaxEXP) {
      level = (int) Math.floor(
          ((Math.log((exp * (1.0 / referenceEXP) * (Math.pow(B, referenceLevel) - 1)) + 1)
              / Math.log(B))));
    } else {
      level = (int) Math.floor((exp - exponentialMaxEXP) / linearEXP) + linearStartingLevel;
    }
    return level;
  }

  public int getEXPfromLevel(int level) {
    ConfigLoad loadConfig = new ConfigLoad();
    ArrayList<Double> levelingInfo = loadConfig.getLevelingInfo();
    double B = levelingInfo.get(1);
    int referenceLevel = (int) Math.round(levelingInfo.get(2));
    int referenceEXP = (int) Math.round(levelingInfo.get(3));
    int linearStartingLevel = (int) Math.round(levelingInfo.get(4));
    int linearEXP = (int) Math.round(levelingInfo.get(5));
    int EXP = 0;
    if (level > linearStartingLevel) {
      int exponentialMaxEXP = getEXPfromLevel(linearStartingLevel);
      EXP = exponentialMaxEXP + linearEXP * (level - linearStartingLevel);
    } else {
      EXP = (int) Math.floor(
          referenceEXP * ((Math.pow(B, level) - 1) / (Math.pow(B, referenceLevel) - 1)));
    }
    return EXP;
  }

  public String getPlayerPlayTimeString(int playTime) {
    String playTime_string = "";
    int hours = (int) Math.floor(playTime / 3600.0);
    int minutes = (int) Math.floor((playTime - (hours * 3600)) / 60.0);
    int seconds = (int) Math.floor((playTime - (hours * 3600) - (minutes * 60)));
    String hoursString = Integer.toString(hours);
    if (hoursString.length() < 2) {
      hoursString = "0" + hoursString;
    }
    String minutesString = Integer.toString(minutes);
    if (minutesString.length() < 2) {
      minutesString = "0" + minutesString;
    }
    String secondsString = Integer.toString(seconds);
    if (secondsString.length() < 2) {
      secondsString = "0" + secondsString;
    }
    playTime_string = hoursString + ":" + minutesString + ":" + secondsString;
    return playTime_string;
  }

  @Override
  public String toString() {
    return "PlayerLeaderboardStats [username=" + pName + ", sortedStat=" + sortedStat + ", stat2="
        + stat2 + "]";
  }


}
