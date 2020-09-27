package mc.carlton.freerpg.miscEvents;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.EntityPickedUpItemStorage;
import mc.carlton.freerpg.globalVariables.EntityGroups;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class CreatureSpawn implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    void onEntitySpawn(CreatureSpawnEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        if (e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.SPAWNER)) {
            e.getEntity().setMetadata("frpgSpawnerMob",new FixedMetadataValue(plugin,"frpg"));
        }
    }
}
