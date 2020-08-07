package mc.carlton.freerpg.miscEvents;

import mc.carlton.freerpg.perksAndAbilities.Fishing;
import mc.carlton.freerpg.playerAndServerInfo.AbilityTracker;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class PlayerFish implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    void onPlayerFish(PlayerFishEvent e) {
        Player p = e.getPlayer();
        if (e.isCancelled()) {
            return;
        }
        if (!p.hasPermission("freeRPG.fish")) {
            return;
        }
        World world = p.getWorld();
        Entity caughtThing = e.getCaught();
        AbilityTracker abilities = new AbilityTracker(p);
        Integer[] pAbilities = abilities.getPlayerAbilities();


        if (e.getState() == PlayerFishEvent.State.FISHING) {
            if (pAbilities[4] > -1) {
                Fishing fishingClass = new Fishing(p);
                fishingClass.enableAbility();
            }
        }
        else if (e.getState() == PlayerFishEvent.State.REEL_IN) {
            Fishing fishingClass = new Fishing(p);
            if (pAbilities[4] == -2) {
                fishingClass.superBait(e.getHook(),caughtThing,world);
            }

        }
        else if (e.getState() == PlayerFishEvent.State.IN_GROUND) {
            Fishing fishingClass = new Fishing(p);
            fishingClass.grapplingHook(e.getHook(),world);
        }
        else if (e.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            Fishing fishingClass = new Fishing(p);
            if (pAbilities[4] == -2) {
                fishingClass.superBait(e.getHook(),caughtThing,world);
            }
            else {
                fishingClass.normalCatch(e.getHook(),caughtThing,world);
            }
        }
        else if (e.getState() == PlayerFishEvent.State.CAUGHT_ENTITY) {
            Fishing fishingClass = new Fishing(p);
            fishingClass.rob(e.getHook(),e.getCaught(),world);
        }



        /*
        System.out.println(e.getState());;
        System.out.println(e.getHook().getLocation().getBlock().getType());
        if (caughtThing == null && e.getState() == PlayerFishEvent.State.REEL_IN) {
            double dx = p.getLocation().getX() - e.getHook().getLocation().getX();
            double dy = p.getLocation().getY() - e.getHook().getLocation().getY();
            double dz = p.getLocation().getZ() - e.getHook().getLocation().getZ();
            double distance = Math.sqrt(dx*dx + dy*dy + dz+dz);
            double multiplier = 0.08;
            Item droppedItem = world.dropItemNaturally(e.getHook().getLocation(),new ItemStack(Material.GOLD_INGOT,1));
            Vector velocity = new Vector(dx*multiplier,dy*multiplier + (double) Math.sqrt(distance)*0.1 ,dz*multiplier);
            droppedItem.setVelocity(velocity);


        }

         */
    }
}
