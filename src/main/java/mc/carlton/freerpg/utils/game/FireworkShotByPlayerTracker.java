package mc.carlton.freerpg.utils.game;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;

public class FireworkShotByPlayerTracker {

  static Map<UUID, Player> fireWorkPlayerMap = new ConcurrentHashMap<>();

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
