package mc.carlton.freerpg.playerAndServerInfo;

import org.bukkit.entity.Player;

import java.util.*;

public class PlayerStats {
    private Player player;
    private UUID uuid;
    static Map<UUID, Map<String, ArrayList<Number>>> player_statsMap = new HashMap<UUID, Map<String, ArrayList<Number>>>();

    public PlayerStats(Player p) {
        this.player = p;
        this.uuid = player.getUniqueId();
    }

    public Map<String, ArrayList<Number>> getPlayerData() {
        if (!player_statsMap.containsKey(uuid)) {
            PlayerStatsLoadIn loadInPlayer = new PlayerStatsLoadIn(player);
            Map<String, ArrayList<Number>> playerStats0 = loadInPlayer.getPlayerStatsMap(player);
            player_statsMap.put(uuid,playerStats0);
        }
        return player_statsMap.get(uuid);
    }

    public void setData(Map<UUID, Map<String, ArrayList<Number>>> playerStatsMap) {
        this.player_statsMap = playerStatsMap;
    }
    public Map<UUID, Map<String, ArrayList<Number>>> getData() {
        if (!player_statsMap.containsKey(uuid)) {
            PlayerStatsLoadIn loadInPlayer = new PlayerStatsLoadIn(player);
            Map<String, ArrayList<Number>> playerStats0 = loadInPlayer.getPlayerStatsMap(player);
            player_statsMap.put(uuid,playerStats0);
        }
        return player_statsMap;
    }
    public void removePlayer() {
        player_statsMap.remove(uuid);
    }

}
