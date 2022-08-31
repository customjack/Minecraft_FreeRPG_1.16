package mc.carlton.freerpg.clickEvents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import mc.carlton.freerpg.globalVariables.ItemGroups;
import mc.carlton.freerpg.perksAndAbilities.Archery;
import mc.carlton.freerpg.perksAndAbilities.BeastMastery;
import mc.carlton.freerpg.perksAndAbilities.Digging;
import mc.carlton.freerpg.perksAndAbilities.Fishing;
import mc.carlton.freerpg.playerInfo.AbilityTracker;
import mc.carlton.freerpg.playerInfo.PlayerStats;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerLeftClick implements Listener {

  @EventHandler
  void onLeftClick(PlayerInteractEvent e) {
    Action a = e.getAction();

    if ((a.equals(Action.LEFT_CLICK_AIR)) || (a.equals(Action.LEFT_CLICK_BLOCK))) {
      Player p = e.getPlayer();
      if (p.getGameMode() == GameMode.CREATIVE) {
        return;
      }
      World world = p.getWorld();
      Material itemType = p.getInventory().getItemInMainHand().getType();

      ItemGroups itemGroups = new ItemGroups();
      List<Material> leftClickItems = itemGroups.getLeftClickItems();

      if (itemType == Material.FISHING_ROD && a.equals(Action.LEFT_CLICK_BLOCK)) {
        Fishing fishingClass = new Fishing(p);
        fishingClass.initiateAbility();
      } else if (itemType == Material.BOW) {
        Archery archeryClass = new Archery(p);
        archeryClass.initiateAbility();
      } else if (itemType == Material.CROSSBOW) {
        Archery archeryClass = new Archery(p);
        PlayerStats pStatClass = new PlayerStats(p);
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        if ((int) pStat.get("archery").get(12) > 0) {
          archeryClass.initiateAbility();
        }
      } else if (p.getVehicle() != null) {
        EntityType[] acceptableVehicles0 = {EntityType.HORSE, EntityType.SKELETON_HORSE,
            EntityType.ZOMBIE_HORSE, EntityType.PIG, EntityType.DONKEY, EntityType.MULE,
            EntityType.LLAMA};
        List<EntityType> acceptableVehicles = Arrays.asList(acceptableVehicles0);
        if (!leftClickItems.contains(itemType) && acceptableVehicles.contains(
            p.getVehicle().getType())) {
          AbilityTracker abilities = new AbilityTracker(p);
          Integer[] pAbilities = abilities.getPlayerAbilities();
          if (pAbilities[6] > -1) {
            BeastMastery beastMasteryClass = new BeastMastery(p);
            beastMasteryClass.enableAbility();
          }
        }
      }

      if (a.equals(Action.LEFT_CLICK_BLOCK)) {
        BlockFace blockface = e.getBlockFace();
        Digging diggingClass = new Digging(p);
        diggingClass.storeBlockFace(blockface);
      }
    }
  }
}
