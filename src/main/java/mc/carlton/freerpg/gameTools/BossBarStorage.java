package mc.carlton.freerpg.gameTools;

import mc.carlton.freerpg.FreeRPG;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BossBarStorage {
    static Map<Player, BossBar> playerBossBarMap = new ConcurrentHashMap<>();

    public void initializeNewPlayer(Player p){
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        BossBar newBossBar = Bukkit.createBossBar(new NamespacedKey(plugin,p.getUniqueId().toString()),p.getDisplayName()+"'s EXP Bar", BarColor.GREEN, BarStyle.SOLID);
        newBossBar.setVisible(false);
        newBossBar.addPlayer(p);
        playerBossBarMap.putIfAbsent(p,newBossBar);
    }

    public BossBar getPlayerBossBar(Player p) {
        if (!playerBossBarMap.containsKey(p)) {
            initializeNewPlayer(p);
        }
        return playerBossBarMap.get(p);
    }

    public void removePlayer(Player p) {
        if (playerBossBarMap.containsKey(p)) {
            playerBossBarMap.get(p).removePlayer(p);
            playerBossBarMap.remove(p);
        }
    }
}
