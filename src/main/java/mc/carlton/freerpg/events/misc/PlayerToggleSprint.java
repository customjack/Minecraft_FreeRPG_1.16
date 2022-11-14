package mc.carlton.freerpg.events.misc;

import mc.carlton.freerpg.skills.perksAndAbilities.Agility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSprintEvent;

public class PlayerToggleSprint implements Listener {

  @EventHandler(priority = EventPriority.HIGH)
  void onSprintToggle(PlayerToggleSprintEvent e) {
    if (e.isCancelled()) {
      return;
    }
    Player p = e.getPlayer();
    Agility agilityClass = new Agility(p);
    agilityClass.sprintingEXP(e.isSprinting());

  }
}
