package mc.carlton.freerpg.playerAndServerInfo;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AbilityTimers {
    private Player player;
    private UUID uuid;
    static Map<UUID, Integer[]> playerTimers = new HashMap<UUID, Integer[]>();

    public AbilityTimers(Player p) {
        this.player = p;
        this.uuid = player.getUniqueId();
    }

    public Integer[] getPlayerTimers() {
        if (!playerTimers.containsKey(uuid)) {
            Integer[] initTimers = {0,0,0,0,0,0,0,0,0,0,0,0};
            playerTimers.put(uuid,initTimers);
        }
        return playerTimers.get(uuid);
    }
    public Map<UUID, Integer[]> getTimers() {
        if (!playerTimers.containsKey(uuid)) {
            Integer[] initTimers = {0,0,0,0,0,0,0,0,0,0,0,0};
            playerTimers.put(uuid,initTimers);
        }
        return playerTimers;
    }

    public void setPlayerTimer(String ability,int timeRemaining) {
        Integer[] pTimes = playerTimers.get(uuid);
        switch(ability) {
            case "digging":
                pTimes[0] = timeRemaining;
                break;
            case "woodcutting":
                pTimes[1] = timeRemaining;
                break;
            case "mining":
                pTimes[2] = timeRemaining;
                break;
            case "farming":
                pTimes[3] = timeRemaining;
                break;
            case "fishing":
                pTimes[4] = timeRemaining;
                break;
            case "archery":
                pTimes[5] = timeRemaining;
                break;
            case "beastMastery":
                pTimes[6] = timeRemaining;
                break;
            case "swordsmanship":
                pTimes[7] = timeRemaining;
                break;
            case "defense":
                pTimes[8] = timeRemaining;
                break;
            case "axeMastery":
                pTimes[9] = timeRemaining;
                break;
            case "woodcuttingHaste":
                pTimes[10] = timeRemaining;
                break;
            case "fishingRob":
                pTimes[11] = timeRemaining;
            default:
                break;
        }
        playerTimers.put(uuid,pTimes);
    }
    public void setTimes(Map<UUID, Integer[]> timers) {
        this.playerTimers = timers;
    }
    public void removePlayer() {
        Integer[] pTimers = getPlayerTimers();
        boolean allZero = true;
        for (int i : pTimers) {
            if (i > 0){
                allZero = false;
                break;
            }
        }
        if (allZero) {
            playerTimers.remove(uuid);
        }
    }
}
