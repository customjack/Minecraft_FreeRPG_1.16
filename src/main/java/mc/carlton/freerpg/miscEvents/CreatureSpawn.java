package mc.carlton.freerpg.miscEvents;

import mc.carlton.freerpg.FreeRPG;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

public class CreatureSpawn implements Listener {

  @EventHandler(priority = EventPriority.HIGH)
  void onEntitySpawn(CreatureSpawnEvent e) {
    if (e.isCancelled()) {
      return;
    }
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    if (e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.SPAWNER)) {
      e.getEntity().setMetadata("frpgSpawnerMob", new FixedMetadataValue(plugin, "frpg"));
    }
  }
}
