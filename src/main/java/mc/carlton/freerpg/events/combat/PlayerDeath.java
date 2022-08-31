package mc.carlton.freerpg.events.combat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import mc.carlton.freerpg.config.ConfigLoad;
import mc.carlton.freerpg.skills.perksAndAbilities.Digging;
import mc.carlton.freerpg.skills.perksAndAbilities.Global;
import mc.carlton.freerpg.skills.perksAndAbilities.Mining;
import mc.carlton.freerpg.skills.perksAndAbilities.Swordsmanship;
import mc.carlton.freerpg.core.info.player.AbilityLogoutTracker;
import mc.carlton.freerpg.core.info.player.AbilityTracker;
import mc.carlton.freerpg.core.info.player.PlayerStats;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class PlayerDeath implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  void onPlayerDie(PlayerDeathEvent e) {
    Player p = e.getEntity();
    PlayerStats pStatClass = new PlayerStats(p);
    Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
    ConfigLoad configLoad = new ConfigLoad();
    if (configLoad.getAllowedSkillsMap().get("alchemy")) {
      int immortalExperienceLevel = (int) pStat.get("enchanting").get(13);
      int expBuffLevel = (int) pStat.get("enchanting").get(4);
      double multiplier = 1 + expBuffLevel * 0.002;
      e.setDroppedExp((int) Math.round(e.getDroppedExp() / multiplier));
      if (immortalExperienceLevel > 0) {
        e.setKeepLevel(true);
        e.setDroppedExp(0);
      }
    }
    List<ItemStack> drops = e.getDrops();
    AbilityTracker abilities = new AbilityTracker(p);
    Integer[] pAbilities = abilities.getPlayerAbilities();
    if (pAbilities[0] != -1) {
      AbilityLogoutTracker logoutTracker = new AbilityLogoutTracker(p);
      NamespacedKey key = logoutTracker.getPlayerKeys().get("digging");
      int taskID_digging = logoutTracker.getPlayerTasks().get("digging");
      for (ItemStack drop : drops) {
        if (drop.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
          ItemStack abilityItem = drop;
          Digging diggingClass = new Digging(p);
          diggingClass.preventLogoutTheft(taskID_digging, abilityItem, key, false);
          break;
        }
      }
    }
    if (pAbilities[2] != -1) {
      AbilityLogoutTracker logoutTracker = new AbilityLogoutTracker(p);
      NamespacedKey key = logoutTracker.getPlayerKeys().get("mining");
      int taskID_mining = logoutTracker.getPlayerTasks().get("mining");
      for (ItemStack drop : drops) {
        if (drop.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
          ItemStack abilityItem = drop;
          Mining miningClass = new Mining(p);
          miningClass.preventLogoutTheft(taskID_mining, abilityItem, key, false);
          break;
        }
      }
    }
    if (pAbilities[7] != -1) {
      AbilityLogoutTracker logoutTracker = new AbilityLogoutTracker(p);
      NamespacedKey key = logoutTracker.getPlayerKeys().get("swordsmanship");
      int taskID_swordsmanship = logoutTracker.getPlayerTasks().get("swordsmanship");
      for (ItemStack drop : drops) {
        if (drop.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
          ItemStack abilityItem = drop;
          Swordsmanship swordsmanshipClass = new Swordsmanship(p);
          swordsmanshipClass.preventLogoutTheft(taskID_swordsmanship, abilityItem, key, false);
          break;
        }
      }
    }

    if (!e.getKeepInventory()) { //Player does not keep inventory
      if (!configLoad.isKeepinventory()) { //Server is not using some advanced keep inventory plugin
        Global globalClass = new Global(p);
        globalClass.betterResurrectionDeath(drops);
      }
    }

  }
}
