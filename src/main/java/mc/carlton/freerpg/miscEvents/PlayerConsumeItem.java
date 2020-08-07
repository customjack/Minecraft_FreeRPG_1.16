package mc.carlton.freerpg.miscEvents;

import mc.carlton.freerpg.perksAndAbilities.Alchemy;
import mc.carlton.freerpg.perksAndAbilities.Farming;
import mc.carlton.freerpg.perksAndAbilities.Fishing;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerConsumeItem implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    void onConsumeItem(PlayerItemConsumeEvent e){
        if (e.isCancelled()) {
            return;
        }
        Player p  = e.getPlayer();
        ItemStack consumedItem = e.getItem();

        //Farming
        Farming farmingClass = new Farming(p);
        farmingClass.eatFarmFood(consumedItem);

        //Fishing
        Fishing fishingClass = new Fishing(p);
        fishingClass.eatFishFood(consumedItem);

        //
        Alchemy alchemyClass = new Alchemy(p);
        alchemyClass.drinkPotion(consumedItem);

    }
}
