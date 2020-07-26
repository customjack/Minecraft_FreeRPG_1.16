package mc.carlton.freerpg.playerAndServerInfo;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AbilityTracker {
    private Player player;
    private UUID uuid;
    static Map<UUID, Integer[]> playerAbilities = new HashMap<UUID, Integer[]>();

    public AbilityTracker(Player p) {
        this.player = p;
        this.uuid = player.getUniqueId();
    }

    public Integer[] getPlayerAbilities() {
        if (!playerAbilities.containsKey(uuid)) {
            Integer[] initAbils = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
            playerAbilities.put(uuid,initAbils);
        }
        return playerAbilities.get(uuid);
    }
    public Map<UUID, Integer[]> getAbilities() {
        if (!playerAbilities.containsKey(uuid)) {
            Integer[] initAbils = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
            playerAbilities.put(uuid,initAbils);
        }
        return playerAbilities;
    }

    public void setPlayerAbility(String ability,int taskID) {
        if (!playerAbilities.containsKey(uuid)) {
            return;
        }
        Integer[] pAbils = playerAbilities.get(uuid);
        switch(ability) {
            case "digging":
                pAbils[0] = taskID;
                break;
            case "woodcutting":
                pAbils[1] = taskID;
                break;
            case "mining":
                pAbils[2] = taskID;
                break;
            case "farming":
                pAbils[3] = taskID;
                break;
            case "fishing":
                pAbils[4] = taskID;
                break;
            case "archery":
                pAbils[5] = taskID;
                break;
            case "beastMastery":
                pAbils[6] = taskID;
                break;
            case "swordsmanship":
                pAbils[7] = taskID;
                break;
            case "defense":
                pAbils[8] = taskID;
                break;
            case "axeMastery":
                pAbils[9] = taskID;
                break;
            case "woodcuttingHaste":
                pAbils[10] = taskID;
                break;
            case "diggingToggle":
                pAbils[11] = taskID;
                break;
            case "archeryCrossbow":
                pAbils[12] = taskID;
                break;
            case "swordsSpeed":
                pAbils[13] = taskID;
                break;
            case "swordsStrength":
                pAbils[14] = taskID;
                break;
            default:
                break;
        }
        playerAbilities.put(uuid,pAbils);
    }
    public void setAbilities(Map<UUID, Integer[]> abilities) {
        this.playerAbilities = abilities;
    }
    public void removePlayer() {
        playerAbilities.remove(uuid);
    }


}
