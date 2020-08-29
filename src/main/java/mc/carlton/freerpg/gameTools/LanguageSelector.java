package mc.carlton.freerpg.gameTools;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.globalVariables.StringsAndOtherData;
import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LanguageSelector {
    Player p;
    FileConfiguration languages;
    String playerLanguage;
    Map<String,String> idToStringMap = new HashMap<>();


    public LanguageSelector(Player player) {
        this.p = player;
        PlayerStats languageStat = new PlayerStats(p);
        this.playerLanguage = languageStat.getPlayerLanguage();
        StringsAndOtherData stringsAndOtherData = new StringsAndOtherData();
        this.idToStringMap = stringsAndOtherData.getIdToStringMap();
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


}
