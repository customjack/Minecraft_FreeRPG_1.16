package mc.carlton.freerpg.events.misc;

import mc.carlton.freerpg.skills.perksAndAbilities.Global;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawn implements Listener {

  @EventHandler
  void onRespawn(PlayerRespawnEvent e) {
    Player p = (Player) e.getPlayer();
    Global globalClass = new Global(p);
    globalClass.betterResurrectionRespawn();

  }
}
