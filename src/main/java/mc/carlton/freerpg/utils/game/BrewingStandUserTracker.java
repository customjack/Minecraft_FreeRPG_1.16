package mc.carlton.freerpg.utils.game;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;

public class BrewingStandUserTracker {

  static Map<BrewingStand, Player> standPlayerMap = new ConcurrentHashMap<>();

  public void addstand(BrewingStand stand, Player player) {
    if (stand instanceof BrewingStand) {
      standPlayerMap.put(stand, player);
    }
  }

  public Player getPlayer(BrewingStand stand) {
    if (stand instanceof BrewingStand) {
      if (standPlayerMap.containsKey(stand)) {
        return standPlayerMap.get(stand);
      }
    }
    return null;
  }

  public void removeStand(BrewingStand stand) {
    standPlayerMap.remove(stand);
  }

  public void removeAllPlayerStands(Player p) {
    for (BrewingStand stand : standPlayerMap.keySet()) {
      if (standPlayerMap.get(stand) == p) {
        standPlayerMap.remove(stand);
      }
    }
  }
}
