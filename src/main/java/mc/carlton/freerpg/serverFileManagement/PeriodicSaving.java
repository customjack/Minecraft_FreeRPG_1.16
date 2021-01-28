package mc.carlton.freerpg.serverFileManagement;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.playerInfo.PlayerStats;
import mc.carlton.freerpg.playerInfo.PlayerStatsLoadIn;
import mc.carlton.freerpg.serverConfig.ConfigLoad;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.*;

public class PeriodicSaving {
    public void periodicallySaveStats() {
        int secondsWait;
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
                saveAllStats(false);
            }
        }.runTaskTimerAsynchronously(plugin,ticksWait,ticksWait);
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
                times.add(System.currentTimeMillis()-time);
                N+=1.0;
            }
            long sum = 0;
            for (long time : times) {
                sum += time;
            }
            double avg = Math.round((sum/N)*100)/100.0;
            System.out.println("[FreeRPG] Total time Taken: " + sum + " ms");
            System.out.println("[FreeRPG] Average time Taken: " + avg + " ms");

        }
        else {
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
        }
        catch (IOException e) {
            System.out.println("[FreeRPG] FAILED TO SAVE STATS OF PLAYER: " + p.getDisplayName());
        }
    }

    public void savePlayer(UUID playerUUID) {
        try {
            PlayerStatsLoadIn loadIn = new PlayerStatsLoadIn(playerUUID);
            loadIn.setPlayerStatsMap(isPlayerOnline(playerUUID));
        }
        catch (IOException e) {
            System.out.println("[FreeRPG] FAILED TO SAVE STATS OF PLAYER UUID:" + playerUUID.toString());
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
