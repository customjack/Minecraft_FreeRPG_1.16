package mc.carlton.freerpg.events.combat;

import mc.carlton.freerpg.skills.perksAndAbilities.Archery;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.ProjectileSource;

public class ArrowLand implements Listener {

  @EventHandler
  void onArrowLand(ProjectileHitEvent e) {
    Entity projectile = e.getEntity();
    if (projectile instanceof Projectile) {
      if (projectile instanceof Arrow || projectile instanceof SpectralArrow) {
        ProjectileSource shooter = ((Projectile) projectile).getShooter();
        if (shooter instanceof Player) {
          Player p = (Player) shooter;
          Archery archeryClass = new Archery(p);
          archeryClass.retrievalRemoval(projectile);

        }
      }
    }

  }
}
