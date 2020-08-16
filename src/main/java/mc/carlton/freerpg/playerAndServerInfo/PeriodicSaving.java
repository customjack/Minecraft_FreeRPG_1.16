package mc.carlton.freerpg.playerAndServerInfo;

import mc.carlton.freerpg.FreeRPG;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class PeriodicSaving {
    public void periodicallySaveStats() {
        int secondsWait = 900;
        ConfigLoad loadConfig = new ConfigLoad();
        secondsWait = loadConfig.getSaveStatsTimer();
        if (secondsWait < 1) {
            return;
        }
        int ticksWait = secondsWait*20;
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        new BukkitRunnable() {
            @Override
            public void run() {
                saveAllStats();
            }
        }.runTaskTimer(plugin,ticksWait,ticksWait);
    }

    public void saveAllStats() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            try {
                PlayerStatsLoadIn loadIn = new PlayerStatsLoadIn(p);
                loadIn.setPlayerStatsMap(p);
                PlacedBlocksLoadIn saveBlocks = new PlacedBlocksLoadIn();
                saveBlocks.setPlacedBlocks();
            }
            catch (IOException e) {
                System.out.println("[FreeRPG] FAILED TO SAVE STATS OF PLAYER: " + p.getDisplayName());
            }
        }
    }
}
