package mc.carlton.freerpg.gameTools;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.playerAndServerInfo.ConfigLoad;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FurnaceUserTracker {
    static Map<Location, Player> furnaceLocationPlayerMap = new ConcurrentHashMap<>();
    static Map<UUID, Integer> playerRemoveFurnacesTaskIdMap = new ConcurrentHashMap<>();
    static Map<Location,Boolean> waitingOnTaskMap = new ConcurrentHashMap<>();

    public void addfurnaceLocation(Location furnaceLocation, Player player) {
        if (furnaceLocation instanceof Location && player instanceof Player) {
            furnaceLocationPlayerMap.put(furnaceLocation, player);
        }
    }

    public Player getPlayer(Location furnaceLocation) {
        if (furnaceLocation instanceof Location) {
            if (furnaceLocationPlayerMap.containsKey(furnaceLocation)) {
                return furnaceLocationPlayerMap.get(furnaceLocation);
            }
        }
        return null;
    }

    public boolean getWaitingOnTask(Location furnaceLoc) {
        if (waitingOnTaskMap.containsKey(furnaceLoc)) {
            return waitingOnTaskMap.get(furnaceLoc);
        }
        else {
            return false;
        }
    }

    public void setWaitingOnTaskMap(boolean isWaiting,Location furnaceLoc) {
        waitingOnTaskMap.put(furnaceLoc,isWaiting);
    }
    public void removeWaitingOnTaskMap(Location furnaceLoc) {
        if (waitingOnTaskMap.containsKey(furnaceLoc)) {
            waitingOnTaskMap.remove(furnaceLoc);
        }
    }

    public void removefurnaceLocation(Location furnaceLocation) {
        furnaceLocationPlayerMap.remove(furnaceLocation);
    }
    public void removeAllPlayerfurnaceLocations(Player p) {
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        ConfigLoad configLoad = new ConfigLoad();
        int removeFurnaceTimer = 20*configLoad.getFurnaceDeleteTimer();
        int taskID = new BukkitRunnable() {
            @Override
            public void run() {
                for (Location furnaceLocation : furnaceLocationPlayerMap.keySet()) {
                    if (furnaceLocationPlayerMap.get(furnaceLocation) == p) {
                        furnaceLocationPlayerMap.remove(furnaceLocation);
                    }
                }
                if (playerRemoveFurnacesTaskIdMap.containsKey(p.getUniqueId())) {
                    playerRemoveFurnacesTaskIdMap.remove(p.getUniqueId());
                }
            }
        }.runTaskLater(plugin, removeFurnaceTimer).getTaskId();
        playerRemoveFurnacesTaskIdMap.put(p.getUniqueId(),taskID);

    }

    public void playerLogin(Player p) {
        if (playerRemoveFurnacesTaskIdMap.containsKey(p.getUniqueId())) {
            int taskID = (int) playerRemoveFurnacesTaskIdMap.get(p.getUniqueId());
            Bukkit.getScheduler().cancelTask(taskID);
        }
    }
}
