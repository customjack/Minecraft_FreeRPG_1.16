package mc.carlton.freerpg.newEvents.eventCallers;

import mc.carlton.freerpg.newEvents.FrpgPlayerRightClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.PluginManager;

public class FrpgPlayerRightClickEventCaller implements Listener {

  @EventHandler
  void onRightClick(PlayerInteractEvent e) {
    Action a = e.getAction();
    if (e.getHand() == EquipmentSlot.OFF_HAND) {
      return;
    }
    if ((a.equals(Action.RIGHT_CLICK_AIR)) || (a.equals(Action.RIGHT_CLICK_BLOCK))) {
      FrpgPlayerRightClickEvent playerRightClickEvent = new FrpgPlayerRightClickEvent(e);
      PluginManager pluginManager = Bukkit.getServer().getPluginManager();
      pluginManager.callEvent(playerRightClickEvent); //Call event
    }
  }
}
