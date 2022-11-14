package mc.carlton.freerpg.events.enchanting;

import java.util.Map;
import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.config.ConfigLoad;
import mc.carlton.freerpg.core.info.player.ChangeStats;
import org.apache.logging.log4j.Level;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class AnvilClick implements Listener {

  Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);

  @EventHandler(priority = EventPriority.HIGH)
  void onInventoryClick(InventoryClickEvent e) {
    if (e.isCancelled()) {
      return;
    }
    try {
      InventoryType invType = e.getClickedInventory().getType();
    } catch (Exception except) {
      FreeRPG.log(Level.WARN, except.getMessage());
      return;
    }
    if (e.getInventory() instanceof AnvilInventory) { //This section is very buggy, and not clean. Hopefully I can improve it in the future
      Player p = (Player) e.getWhoClicked();
      int level = p.getLevel();
      new BukkitRunnable() {
        @Override
        public void run() {
          int newLevel = p.getLevel();
          if (newLevel < level) {
            ConfigLoad configLoad1 = new ConfigLoad();
            Map<String, Integer> expMap = configLoad1.getExpMapForSkill("enchanting");
            ChangeStats increaseStats = new ChangeStats(p);
            increaseStats.changeEXP("enchanting",
                expMap.get("useAnvil_EXPperLevelUsed") * (level - newLevel));
          }
        }
      }.runTaskLater(plugin, 2);
    }
  }
}
