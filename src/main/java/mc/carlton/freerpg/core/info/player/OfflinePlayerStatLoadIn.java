package mc.carlton.freerpg.core.info.player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;
import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.core.info.server.RecentLogouts;
import mc.carlton.freerpg.core.leaveAndJoin.LoginProcedure;
import mc.carlton.freerpg.core.serverFileManagement.PlayerStatsFilePreparation;
import org.apache.logging.log4j.Level;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class OfflinePlayerStatLoadIn {

  Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
  private int safeDeleteTaskID;

  public void loadInOfflinePlayers() {
    RecentLogouts recentLogouts = new RecentLogouts();
    ArrayList<UUID> playersToLoadIn = recentLogouts.getLastLogouts();
    new BukkitRunnable() {
      @Override
      public void run() {
        for (UUID pUUID : playersToLoadIn) {
          PlayerStats playerStats = new PlayerStats(pUUID);
          if (playerStats.isPlayerRegistered()) {
            continue;
          }
          PlayerStatsFilePreparation playerStatsFilePreparation = new PlayerStatsFilePreparation();
          playerStatsFilePreparation.preparePlayerFile(null, pUUID, false);
          LoginProcedure loginProcedure = new LoginProcedure(pUUID);
          loginProcedure.addStatsToPlayerMap(false);
        }
      }
    }.runTaskAsynchronously(plugin);
  }

  public void loadInAllOfflinePlayers() {
    new BukkitRunnable() {
      @Override
      public void run() {
        File userdata = new File(
            Bukkit.getServer().getPluginManager().getPlugin("FreeRPG").getDataFolder(),
            File.separator + "PlayerDatabase");
        File[] allUsers = userdata.listFiles();
        HashSet<Long> times = new HashSet<>();
        double N = 0;
        for (File f : allUsers) {
          UUID pUUID = UUID.fromString(f.getName().replace(".yml", ""));
          PlayerStats playerStats = new PlayerStats(pUUID);
          if (playerStats.isPlayerRegistered()) {
            continue;
          }
          long time = System.currentTimeMillis();
          PlayerStatsFilePreparation playerStatsFilePreparation = new PlayerStatsFilePreparation();
          playerStatsFilePreparation.preparePlayerFile(null, pUUID, false);
          LoginProcedure loginProcedure = new LoginProcedure(pUUID);
          loginProcedure.addStatsToPlayerMap(false);
          times.add(System.currentTimeMillis() - time);
          N += 1.0;
        }
        long sum = 0;
        for (long time : times) {
          sum += time;
        }
        double avg = Math.round((sum / N) * 100) / 100.0;
        FreeRPG.log(Level.INFO, "Total time Taken: " + sum + " ms");
        FreeRPG.log(Level.INFO, "Average time Taken: " + avg + " ms");
      }
    }.runTaskAsynchronously(plugin);

  }

  public void unloadAllOfflinePlayers() {
    //One of the rare cases we need to access the playerStats class without a player, so we use a random UUID
    PlayerStats allStats = new PlayerStats(UUID.randomUUID());
    for (UUID playerUUID : allStats.getData().keySet()) {
      unloadOfflinePlayer(playerUUID);
    }
  }

  public void unloadOfflinePlayer(UUID playerUUID) {
    safeRemovePlayer(playerUUID);
  }

  public void safeRemovePlayer(UUID playerUUID) {
    int taskID = new BukkitRunnable() {
      @Override
      public void run() {
        PlayerStats playerStats = new PlayerStats(playerUUID);
        if (isPlayerOnline(playerUUID)) { //We don't want to remove the stats of an online player
          Bukkit.getScheduler().cancelTask(safeDeleteTaskID);
        } else if (playerStats.arePlayerStatsSaved()) {
          playerStats.removePlayer();
          Bukkit.getScheduler().cancelTask(safeDeleteTaskID);
        }
      }
    }.runTaskTimer(plugin, 1, 50).getTaskId();
    safeDeleteTaskID = taskID;
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
