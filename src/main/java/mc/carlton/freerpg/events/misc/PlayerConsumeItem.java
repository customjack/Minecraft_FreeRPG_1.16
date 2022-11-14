package mc.carlton.freerpg.events.misc;

import mc.carlton.freerpg.skills.perksAndAbilities.Alchemy;
import mc.carlton.freerpg.skills.perksAndAbilities.Farming;
import mc.carlton.freerpg.skills.perksAndAbilities.Fishing;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerConsumeItem implements Listener {

  @EventHandler(priority = EventPriority.HIGH)
  void onConsumeItem(PlayerItemConsumeEvent e) {
    if (e.isCancelled()) {
      return;
    }
    Player p = e.getPlayer();
    ItemStack consumedItem = e.getItem();

    // Farming
    Farming farmingClass = new Farming(p);
    farmingClass.eatFarmFood(consumedItem);

    // Fishing
    Fishing fishingClass = new Fishing(p);
    fishingClass.eatFishFood(consumedItem);

    // Alchemy
    Alchemy alchemyClass = new Alchemy(p);
    alchemyClass.drinkPotion(consumedItem);

  }
}
