package mc.carlton.freerpg.core.leaveAndJoin;

import java.io.IOException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  void onPlayerLeave(PlayerQuitEvent e) throws IOException {
    Player p = e.getPlayer();
    p.leaveVehicle();
    LogoutProcedure logout = new LogoutProcedure(p);
    logout.playerLogout(false);

  }
}
