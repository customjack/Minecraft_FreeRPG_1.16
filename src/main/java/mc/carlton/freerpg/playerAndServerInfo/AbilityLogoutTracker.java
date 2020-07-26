package mc.carlton.freerpg.playerAndServerInfo;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class AbilityLogoutTracker {
    private Player player;
    static Map<Player, Integer[]> playerTasks = new HashMap<Player, Integer[]>();
    static Map<Player, ItemStack[]> playerItems = new HashMap<Player, ItemStack[]>();

    public AbilityLogoutTracker(Player player) {
        this.player = player;
    }
    public Map<Player, Integer[]>  getTasks() {
        return playerTasks;
    }
    public Integer[]  getPlayerTasks(Player p) {
        return playerTasks.get(p);
    }
    public Map<Player, ItemStack[]>  getItems() {
        return playerItems;
    }
    public ItemStack[]  getPlayerItems(Player p) {
        return playerItems.get(p);
    }
    public void setTasks(Map<Player, Integer[]> player_tasks) {
        playerTasks = player_tasks;
    }
    public void setItems(Map<Player, ItemStack[]> player_items) {
        playerItems = player_items;
    }

    public void setPlayerTasks(Integer[] tasks,Player p) {
        playerTasks.put(p,tasks);
    }
    public void setPlayerItems(ItemStack[] items,Player p) {
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

    public void setPlayerItem(Player p,String ability,ItemStack item) {
        ItemStack[] pItems = playerItems.get(p);
        switch (ability) {
            case "digging":
                pItems[0] = item;
                break;
            case "woodcutting":
                pItems[1] = item;
                break;
            case "mining":
                pItems[2] = item;
                break;
            case "farming":
                pItems[3] = item;
                break;
            case "fishing":
                pItems[4] = item;
                break;
            case "archery":
                pItems[5] = item;
                break;
            case "beastMastery":
                pItems[6] = item;
                break;
            case "swordsmanship":
                pItems[7] = item;
                break;
            case "defense":
                pItems[8] = item;
                break;
            case "axeMastery":
                pItems[9] = item;
                break;
            case "woodcuttingHaste":
                pItems[10] = item;
                break;
            case "swordSpeed":
                pItems[11] = item;
                break;
            case "swordStrength":
                pItems[12] = item;
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
