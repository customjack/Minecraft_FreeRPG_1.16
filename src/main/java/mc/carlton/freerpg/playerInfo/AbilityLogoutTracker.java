package mc.carlton.freerpg.playerInfo;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class AbilityLogoutTracker {
    private Player player;
    static Map<Player, Integer[]> playerTasks = new HashMap<Player, Integer[]>();
    static Map<Player, NamespacedKey[]> playerItems = new HashMap<Player, NamespacedKey[]>();

    public AbilityLogoutTracker(Player player) {
        this.player = player;
    }
    public Map<Player, Integer[]>  getTasks() {
        return playerTasks;
    }
    public Integer[]  getPlayerTasks(Player p) {
        return playerTasks.get(p);
    }
    public Map<Player, NamespacedKey[]>  getItems() {
        return playerItems;
    }
    public NamespacedKey[]  getPlayerItems(Player p) {
        return playerItems.get(p);
    }
    public void setTasks(Map<Player, Integer[]> player_tasks) {
        playerTasks = player_tasks;
    }
    public void setItems(Map<Player, NamespacedKey[]> player_items) {
        playerItems = player_items;
    }

    public void setPlayerTasks(Integer[] tasks,Player p) {
        playerTasks.put(p,tasks);
    }
    public void setPlayerItems(NamespacedKey[] items,Player p) {
        playerItems.put(p,items);
    }
    public void setPlayerTask(Player p,String ability,int taskID) {
        Integer[] pTasks = playerTasks.get(p);
        switch (ability) {
            case "digging":
                pTasks[0] = taskID;
                break;
            case "woodcutting":
                pTasks[1] = taskID;
                break;
            case "mining":
                pTasks[2] = taskID;
                break;
            case "farming":
                pTasks[3] = taskID;
                break;
            case "fishing":
                pTasks[4] = taskID;
                break;
            case "archery":
                pTasks[5] = taskID;
                break;
            case "beastMastery":
                pTasks[6] = taskID;
                break;
            case "swordsmanship":
                pTasks[7] = taskID;
                break;
            case "defense":
                pTasks[8] = taskID;
                break;
            case "axeMastery":
                pTasks[9] = taskID;
                break;
            case "woodcuttingHaste":
                pTasks[10] = taskID;
                break;
            case "swordSpeed":
                pTasks[11] = taskID;
                break;
            case "swordStrength":
                pTasks[12] = taskID;
                break;
            default:
                break;
        }
        playerTasks.put(p, pTasks);
    }

    public void setPlayerItem(Player p,String ability,NamespacedKey key) {
        NamespacedKey[] pItems = playerItems.get(p);
        switch (ability) {
            case "digging":
                pItems[0] = key;
                break;
            case "woodcutting":
                pItems[1] = key;
                break;
            case "mining":
                pItems[2] = key;
                break;
            case "farming":
                pItems[3] = key;
                break;
            case "fishing":
                pItems[4] = key;
                break;
            case "archery":
                pItems[5] = key;
                break;
            case "beastMastery":
                pItems[6] = key;
                break;
            case "swordsmanship":
                pItems[7] = key;
                break;
            case "defense":
                pItems[8] = key;
                break;
            case "axeMastery":
                pItems[9] = key;
                break;
            case "woodcuttingHaste":
                pItems[10] = key;
                break;
            case "swordSpeed":
                pItems[11] = key;
                break;
            case "swordStrength":
                pItems[12] = key;
                break;
            default:
                break;
        }
        playerItems.put(p, pItems);
    }
    public void removePlayer(Player p) {
        playerTasks.remove(p);
        playerItems.remove(p);
    }

}
