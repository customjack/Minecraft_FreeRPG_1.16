package mc.carlton.freerpg.combatEvents;

import mc.carlton.freerpg.gameTools.FireworkShotByPlayerTracker;
import mc.carlton.freerpg.perksAndAbilities.Archery;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerShootBow implements Listener {
    @EventHandler
    void onEntityShoot(EntityShootBowEvent e){
        Entity entity = e.getEntity();
        if (entity instanceof  Player && (e.getProjectile() instanceof Arrow || e.getProjectile() instanceof SpectralArrow)) {
            Player p = (Player) entity;
            ItemStack bow = e.getBow();
            Entity projectile = e.getProjectile();
            Archery archeryClass =  new Archery(p);
            archeryClass.rapidFire(projectile,bow);
            archeryClass.sniper(projectile);
            archeryClass.retrieval(projectile,bow);

        }
        else if (entity instanceof  Player && e.getProjectile() instanceof Firework) {
            Player p = (Player) entity;
            Entity projectile = e.getProjectile();
            FireworkShotByPlayerTracker fireworkTracker = new FireworkShotByPlayerTracker();
            fireworkTracker.addFirework(projectile,p);
        }

    }
}
