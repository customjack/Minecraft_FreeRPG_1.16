package mc.carlton.freerpg.newEvents.eventCallers;

import mc.carlton.freerpg.newEvents.FrpgPlayerCraftItemEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

public class FrpgPlayerCraftItemEventCaller implements Listener {

  @EventHandler(priority = EventPriority.HIGH)
  void onPlayerCraft(CraftItemEvent e) {
    Player p = (Player) e.getWhoClicked();
    FrpgPlayerCraftItemEvent playerCraftItemEvent = new FrpgPlayerCraftItemEvent(e, p);
    PluginManager pluginManager = Bukkit.getServer().getPluginManager();
    if (e.getClick().equals(ClickType.MIDDLE) || e.getClick().equals(ClickType.UNKNOWN) ||
        e.getClick().equals(ClickType.WINDOW_BORDER_LEFT) || e.getClick()
        .equals(ClickType.WINDOW_BORDER_RIGHT)) {
      return;
    } else if (e.getClick().equals(ClickType.SWAP_OFFHAND)) {
      ItemStack itemInOffhand = p.getInventory().getItemInOffHand();
      if (itemInOffhand != null) {
        if (!itemInOffhand.getType().equals(Material.AIR)) {
          return;
        }
      }
      pluginManager.callEvent(playerCraftItemEvent); //Call event
    } else if (e.getClick().equals(ClickType.SHIFT_RIGHT) || e.getClick()
        .equals(ClickType.SHIFT_LEFT) ||
        e.getClick().equals(ClickType.LEFT) || e.getClick().equals(ClickType.RIGHT) ||
        e.getClick().equals(ClickType.NUMBER_KEY)) {
            /*
            Only god knows what I was thinking when I wrote these nested conditionals, but it works.
             */
      if (e.getCursor() != null) { //Should never be the case, but just to prevent errors
        if (e.getCursor().getType().equals(Material.AIR)
            || e.isShiftClick()) { //User must click with empty cursor (or shift click)
          if (e.getHotbarButton() != -1
              || e.isShiftClick()) { //User used number key or shift clicked
            if (p.getInventory().firstEmpty() != -1) { //Inventory is not full
              if (e.getHotbarButton() != -1) { //Hotbar button click case
                if (p.getInventory().getItem(e.getHotbarButton())
                    == null) { //Null case, same as the "else if" (to be safe, should never occur)
                  pluginManager.callEvent(playerCraftItemEvent); //Call event
                } else if (p.getInventory().getItem(e.getHotbarButton()).getType()
                    .equals(Material.AIR)) { //Reward EXP if the hotbar slot is empty (item crafted)
                  pluginManager.callEvent(playerCraftItemEvent); //Call event
                }
              } else { //Shift click case
                pluginManager.callEvent(playerCraftItemEvent); //Call event
              }
            }
          } else { //If user clicked with empty cursor
            pluginManager.callEvent(playerCraftItemEvent); //Call event
          }
        }
      }
    }
  }
}
