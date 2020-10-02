package mc.carlton.freerpg.serverFileManagement;

import mc.carlton.freerpg.FreeRPG;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerFilesManager {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    static Map<UUID, File> playerFiles = new ConcurrentHashMap<>();

    public void addPlayerFile(Player p, File file) {
        UUID uuid = p.getUniqueId();
        playerFiles.put(uuid,file);
    }
    public void addPlayerFile(UUID playerUUID, File file) {
        playerFiles.put(playerUUID,file);
    }

    public File getPlayerFile(Player p) {
        UUID uuid = p.getUniqueId();
        if (playerFiles.containsKey(uuid)) {
            return playerFiles.get(uuid);
        }
        else {
            return loadPlayerFile(p);
        }
    }
    public File getPlayerFile(UUID playerUUID) {
        if (playerFiles.containsKey(playerUUID)) {
            return playerFiles.get(playerUUID);
        }
        else {
            return loadPlayerFile(playerUUID);
        }
    }

    public File loadPlayerFile(Player p) {
        return loadPlayerFile(p.getUniqueId());
    }

    public File loadPlayerFile(UUID playerUUID) {
        File f = new File(plugin.getDataFolder(), File.separator + "PlayerDatabase" + File.separator + playerUUID.toString() + ".yml");
        f.setReadable(true,false);
        f.setWritable(true,false);
        addPlayerFile(playerUUID,f);
        return f;
    }

    public void removePlayerFile(Player p) {
        UUID playerUUID = p.getUniqueId();
        if (playerFiles.containsKey(playerUUID)) {
            playerFiles.remove(playerUUID);
        }
    }

    public void removePlayerFile(UUID playerUUID) {
        if (playerFiles.containsKey(playerUUID)) {
            playerFiles.remove(playerUUID);
        }
    }


}
