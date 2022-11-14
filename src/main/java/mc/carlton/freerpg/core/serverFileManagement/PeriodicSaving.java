package mc.carlton.freerpg.core.serverFileManagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.config.ConfigLoad;
import mc.carlton.freerpg.core.info.player.PlayerStats;
import mc.carlton.freerpg.core.info.player.PlayerStatsLoadIn;
import org.apache.logging.log4j.Level;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PeriodicSaving {

  public void periodicallySaveStats() {
    int secondsWait;
    ConfigLoad loadConfig = new ConfigLoad();
    secondsWait = loadConfig.getSaveStatsTimer();
    if (secondsWait < 1) {
      return;
    }
    int ticksWait = secondsWait * 20;
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    new BukkitRunnable() {
      @Override
      public void run() {
        saveAllStats(false);
      }
    }.runTaskTimerAsynchronously(plugin, ticksWait, ticksWait);
  }

  public void saveAllStats(boolean thoroughSave) {
    if (thoroughSave) {
      //A random UUID is used here for the rare occasion where we the need the full players' stats map without a player
      PlayerStats playerStats = new PlayerStats(UUID.randomUUID());
      Map<UUID, Map<String, ArrayList<Number>>> player_statsMap = playerStats.getData();
      HashSet<Long> times = new HashSet<>();
      double N = 0;
      for (UUID pUUId : player_statsMap.keySet()) {
        long time = System.currentTimeMillis();
        savePlayer(pUUId);
        times.add(System.currentTimeMillis() - time);
        N += 1.0;
      }
      long sum = 0;
      for (long time : times) {
        sum += time;
      }
      double avg = Math.round((sum / N) * 100) / 100.0;
      FreeRPG.log(Level.INFO, "[FreeRPG] Total time Taken: " + sum + " ms");
      FreeRPG.log(Level.INFO, "[FreeRPG] Average time Taken: " + avg + " ms");
    } else {
      for (Player p : Bukkit.getOnlinePlayers()) {
        savePlayer(p);
      }
    }
    RecentPlayersFileManager recentPlayersFileManager = new RecentPlayersFileManager();
    recentPlayersFileManager.writeRecentPlayers();
    LeaderBoardFilesManager leaderBoardFilesManager = new LeaderBoardFilesManager();
    leaderBoardFilesManager.writeOutPlayerLeaderBoardFile();
    PlacedBlockFileManager saveBlocks = new PlacedBlockFileManager();
    saveBlocks.writePlacedBlocks();
  }

  public void savePlayer(Player p) {
    try {
      PlayerStatsLoadIn loadIn = new PlayerStatsLoadIn(p);
      loadIn.setPlayerStatsMap();
    } catch (IOException e) {
      FreeRPG.log(Level.ERROR, "[FreeRPG] FAILED TO SAVE STATS OF PLAYER: " + p.getDisplayName());
    }
  }

  public void savePlayer(UUID playerUUID) {
    try {
      PlayerStatsLoadIn loadIn = new PlayerStatsLoadIn(playerUUID);
      loadIn.setPlayerStatsMap(isPlayerOnline(playerUUID));
    } catch (IOException e) {
      FreeRPG.log(Level.ERROR,
          "[FreeRPG] FAILED TO SAVE STATS OF PLAYER UUID:" + playerUUID.toString());
    }
  }

  public boolean isPlayerOnline(UUID playerUUID) {
    if (Bukkit.getPlayer(playerUUID) != null) {
      if (Bukkit.getPlayer(playerUUID).isOnline()) {
        return true;
      }
    }
    return false;
  }
}
