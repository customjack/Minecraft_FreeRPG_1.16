package mc.carlton.freerpg.utils.game;

import java.util.Map;
import mc.carlton.freerpg.config.ConfigLoad;
import mc.carlton.freerpg.utils.globalVariables.StringsAndOtherData;
import mc.carlton.freerpg.core.info.player.PlayerStats;
import org.bukkit.entity.Player;

public class LanguageSelector {

  Player p;
  String playerLanguage;


  public LanguageSelector(Player player) {
    if (player != null) {
      this.p = player;
      PlayerStats languageStat = new PlayerStats(p);
      ConfigLoad configLoad = new ConfigLoad();
      if (configLoad.isForceLanguage()) {
        this.playerLanguage = configLoad.getDefaultLanguage();
      } else {
        this.playerLanguage = languageStat.getPlayerLanguage();
      }
      StringsAndOtherData stringsAndOtherData = new StringsAndOtherData();
    }
  }

  /**
   * Translates a string into english assuming pieces identified by ::translateable;: are a id
   *
   * @param message a String that may contain identifier for parts of string to be translated
   *                Example: "/frpg help [::page::]" --> "/frpg help [page]"
   * @return
   */
  public static String getEnglishMessage(String message) {
    return translateMessage(message, "enUs");
  }

  /**
   * @param message      a String that may contain identifier for parts of string to be translated
   *                     Example: "/frpg help [$:page:$]" --> "/frpg help [page]"
   * @param languageCode a valid language identifier found in languages.yml
   * @return
   */
  public static String translateMessage(String message, String languageCode) {
    String[] splitString = message.split("::");
    String translatedMessage = "";
    for (int i = 0; i < splitString.length; i++) {
      if (i % 2 == 0) { //Even indicies are never translated
        translatedMessage += splitString[i];
      } else { //Odd indicies are always translated
        translatedMessage += getString(splitString[i], languageCode);
      }
    }
    return translatedMessage;
  }

  public static String getString(String id,
      String languageCode) { //Used if we want to ignore the player's language choice
    String text = "";
    StringsAndOtherData stringsAndOtherData = new StringsAndOtherData();
    Map<String, String> idToStringMap = stringsAndOtherData.getIdToStringMap();
    Object text0 = idToStringMap.get(languageCode + "." + id);
    if (text0 != null) {
      text = text0.toString();
    } else {
      text = id; //If the text isn't found, the next best thing is the identifier
    }
    return text;
  }

  public String getLanguage() {
    return playerLanguage;
  }

  public String getString(String id) {
    return getString(id, playerLanguage);
  }

  /**
   * Translates a string into the player's preffered language assuming pieces identified by
   * $:translateable;$ are a id
   *
   * @param message a String that may contain identifier for parts of string to be translated
   *                Example: "/frpg help [$:page:$]" --> "/frpg help [page]"
   * @return
   */
  public String translateMessage(String message) {
    return translateMessage(message, playerLanguage);
  }


}
