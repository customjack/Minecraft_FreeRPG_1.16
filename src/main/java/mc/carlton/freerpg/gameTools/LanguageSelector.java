package mc.carlton.freerpg.gameTools;

import mc.carlton.freerpg.globalVariables.StringsAndOtherData;
import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
import org.bukkit.entity.Player;


import java.util.HashMap;
import java.util.Map;

public class LanguageSelector {
    Player p;
    String playerLanguage;
    Map<String,String> idToStringMap = new HashMap<>();


    public LanguageSelector(Player player) {
        if (player != null) {
            this.p = player;
            PlayerStats languageStat = new PlayerStats(p);
            this.playerLanguage = languageStat.getPlayerLanguage();
            StringsAndOtherData stringsAndOtherData = new StringsAndOtherData();
            this.idToStringMap = stringsAndOtherData.getIdToStringMap();
        }
    }

    public String getLanguage(){
        return playerLanguage;
    }

    public String getString(String id) {
        String text = "";
        Object text0 = idToStringMap.get(playerLanguage + "." + id);
        if (text0 != null) {
            text = text0.toString();
        }
        return text;
    }

    public String getString(String id,String languageCode) { //Used if we want to ignore the player's language choice
        String text = "";
        Object text0 = idToStringMap.get(languageCode + "." + id);
        if (text0 != null) {
            text = text0.toString();
        }
        return text;
    }


}
