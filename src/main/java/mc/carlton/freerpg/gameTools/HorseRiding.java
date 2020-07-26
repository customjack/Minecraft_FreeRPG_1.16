package mc.carlton.freerpg.gameTools;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HorseRiding {
    static Map<Player,Integer> taskMap = new HashMap<>();
    static Map<UUID,Player> mountPlayerMap = new HashMap<>();
    static Map<Player, Location> playerLocationMap = new HashMap<>();

    public void setTaskMap(int taskID,Player p){
        taskMap.put(p,taskID);
    }
    public void setMountPlayerMap(UUID entityUUID,Player p) {
        mountPlayerMap.put(entityUUID,p);
    }
    public void setPlayerLocationMap(Player p) {
        playerLocationMap.put(p,p.getLocation());
    }

    public Integer getTask(Player p) {
        return taskMap.get(p);
    }
    public Player getPlayerFromMount(UUID mountUUID) {
        if (mountPlayerMap.containsKey(mountUUID)) {
            Player toReturn = mountPlayerMap.get(mountUUID);
            mountPlayerMap.remove(mountUUID);
            return toReturn;
        }
        return null;

    }
    public Location getOldLocation(Player p) {
        return playerLocationMap.get(p);
    }

    public void deletePlayerData(Player p) {
        if (taskMap.containsKey(p)) {
            Bukkit.getScheduler().cancelTask(taskMap.get(p));
            taskMap.remove(p);
        }
        if (playerLocationMap.containsKey(p)) {
            playerLocationMap.remove(p);
        }
        for (UUID i : mountPlayerMap.keySet()) {
            if (mountPlayerMap.get(i) == p) {
                mountPlayerMap.remove(i);
            }
        }
    }

}
