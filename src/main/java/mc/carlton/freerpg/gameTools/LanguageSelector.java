package mc.carlton.freerpg.gameTools;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class LanguageSelector {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    Player p;
    File languagesYML;
    FileConfiguration languages;
    String playerLanguage;

    public LanguageSelector(Player player) {
        this.p = player;
        this.languagesYML = new File(plugin.getDataFolder()+"/languages.yml");
        this.languages = YamlConfiguration.loadConfiguration(languagesYML);
        PlayerStats languageStat = new PlayerStats(p);
        this.playerLanguage = languageStat.getPlayerLanguage();
    }

    public String getLanguage(){
        return playerLanguage;
    }

    public String getString(String id) {
        String text = "";
        Object text0 = languages.get("lang." + playerLanguage + "." + id);
        if (text0 != null) {
            text = text0.toString();
        }
        return text;
    }
}
