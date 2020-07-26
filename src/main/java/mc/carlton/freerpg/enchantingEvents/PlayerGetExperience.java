package mc.carlton.freerpg.enchantingEvents;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.perksAndAbilities.Enchanting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.plugin.Plugin;

public class PlayerGetExperience implements Listener {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    @EventHandler
    void onExperienceGain(PlayerExpChangeEvent e){
        Player p = (Player) e.getPlayer();
        Enchanting enchantingClass = new Enchanting(p);
        enchantingClass.giveEXP(e.getAmount());
        int oldAmount = e.getAmount();
        int newAmount = enchantingClass.xpIncrease(oldAmount);
        e.setAmount(newAmount);


    }
}
