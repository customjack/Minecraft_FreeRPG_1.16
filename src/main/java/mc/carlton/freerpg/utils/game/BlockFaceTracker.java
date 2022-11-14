package mc.carlton.freerpg.utils.game;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class BlockFaceTracker {

  static Map<Player, BlockFace> blockFacePlayerMap = new ConcurrentHashMap<>();

  public void addBlockFace(BlockFace blockface, Player player) {
    blockFacePlayerMap.put(player, blockface);
  }

  public BlockFace getBlockface(Player player) {
    if (blockFacePlayerMap.containsKey(player)) {
      return blockFacePlayerMap.get(player);
    }
    return null;
  }

  public void removePlayerBlockFace(Player player) {
    blockFacePlayerMap.remove(player);
  }
}
