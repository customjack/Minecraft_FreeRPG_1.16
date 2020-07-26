package mc.carlton.freerpg.clickEvents;

import mc.carlton.freerpg.perksAndAbilities.BeastMastery;
import mc.carlton.freerpg.perksAndAbilities.Farming;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerRightClickEntity implements Listener {
    @EventHandler
    void onRightClick(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        if (p.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        Entity entity = e.getRightClicked();

        //Farming
        Farming farmingClass = new Farming(p);
        farmingClass.babyAnimalGrow(entity);
        farmingClass.milkingEXP(entity);

        //Beast Mastery
        BeastMastery beastMasteryClass = new BeastMastery(p);
        beastMasteryClass.getHorseStats(entity);

    }
}
