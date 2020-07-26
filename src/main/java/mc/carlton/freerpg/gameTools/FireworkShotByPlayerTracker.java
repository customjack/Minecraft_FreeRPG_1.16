package mc.carlton.freerpg.gameTools;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FireworkShotByPlayerTracker {
    static Map<UUID, Player> fireWorkPlayerMap = new HashMap<>();

    public void addFirework(Entity firework, Player player) {
        if (firework instanceof Firework) {
            fireWorkPlayerMap.put(firework.getUniqueId(), player);
        }
    }

    public Player getPlayer(Entity firework) {
        if (firework instanceof Firework) {
            if (fireWorkPlayerMap.containsKey(firework.getUniqueId())) {
                return fireWorkPlayerMap.get(firework.getUniqueId());
            }
        }
        return null;
    }

    public void removeFireWork(Entity firework) {
        fireWorkPlayerMap.remove(firework);
    }
}
