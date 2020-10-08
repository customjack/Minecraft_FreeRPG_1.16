package mc.carlton.freerpg.playerInfo;

import mc.carlton.freerpg.FreeRPG;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AbilityLogoutTracker {
    private Player p;
    private UUID playerUUID;
    static Map<UUID, Map<String,Integer>> playerTasks = new HashMap<>();
    static Map<UUID, Map<String,NamespacedKey>> playerKeys = new HashMap<>();

    public AbilityLogoutTracker(Player p) {
        this.p = p;
        this.playerUUID = p.getUniqueId();
    }
    public Map<UUID, Map<String,Integer>>  getTasks() {
        return playerTasks;
    }


    public Map<String,Integer> getPlayerTasks() {
        return playerTasks.get(playerUUID);
    }
    public Map<UUID,Map<String,NamespacedKey>> getKeys() {
        return playerKeys;
    }
    public Map<String,NamespacedKey> getPlayerKeys() {
        return playerKeys.get(playerUUID);
    }
    public void setTasks(Map<UUID, Map<String,Integer>> player_tasks) {
        playerTasks = player_tasks;
    }
    public void setItems(Map<UUID, Map<String,NamespacedKey>> player_keys) {
        playerKeys = player_keys;
    }

    public void setPlayerTasks(Map<String,Integer> tasks) {
        playerTasks.put(playerUUID,tasks);
    }
    public void setPlayerItems(Map<String,NamespacedKey> items) {
        playerKeys.put(playerUUID,items);
    }
    public void setPlayerTask(String ability,int taskID) {
        Map<String,Integer> pTasks = playerTasks.get(playerUUID);
        pTasks.put(ability,taskID);
        playerTasks.put(playerUUID, pTasks);
    }

    public void setPlayerItem(String ability,NamespacedKey key) {
        Map<String,NamespacedKey> pItems = playerKeys.get(playerUUID);
        pItems.put(ability,key);
        playerKeys.put(playerUUID, pItems);
    }
    public void removePlayer() {
        playerTasks.remove(playerUUID);
        playerKeys.remove(playerUUID);
    }

    public void intiializePlayer() {
        String[] names = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense",
            "axeMastery","woodcuttingHaste","swordSpeed","swordStrength"};
        Map<String, Integer> player_Tasks = new HashMap<>();
        Map<String,NamespacedKey> player_Keys = new HashMap<>();
        for (String name : names) {
            player_Tasks.put(name,-1);
            player_Keys.put(name,new NamespacedKey(FreeRPG.getPlugin(FreeRPG.class),"x"));
        }
        playerTasks.put(playerUUID,player_Tasks);
        playerKeys.put(playerUUID,player_Keys);
    }
}
