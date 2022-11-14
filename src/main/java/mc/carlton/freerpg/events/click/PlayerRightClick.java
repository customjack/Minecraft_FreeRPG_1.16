package mc.carlton.freerpg.events.click;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.config.ConfigLoad;
import mc.carlton.freerpg.core.info.player.AbilityTracker;
import mc.carlton.freerpg.core.info.player.ChangeStats;
import mc.carlton.freerpg.core.info.player.PlayerStats;
import mc.carlton.freerpg.core.info.server.WorldGuardChecks;
import mc.carlton.freerpg.skills.perksAndAbilities.Archery;
import mc.carlton.freerpg.skills.perksAndAbilities.AxeMastery;
import mc.carlton.freerpg.skills.perksAndAbilities.BeastMastery;
import mc.carlton.freerpg.skills.perksAndAbilities.Defense;
import mc.carlton.freerpg.skills.perksAndAbilities.Digging;
import mc.carlton.freerpg.skills.perksAndAbilities.Farming;
import mc.carlton.freerpg.skills.perksAndAbilities.Mining;
import mc.carlton.freerpg.skills.perksAndAbilities.Repair;
import mc.carlton.freerpg.skills.perksAndAbilities.Swordsmanship;
import mc.carlton.freerpg.skills.perksAndAbilities.Woodcutting;
import mc.carlton.freerpg.utils.game.LanguageSelector;
import mc.carlton.freerpg.utils.globalVariables.ItemGroups;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;


public class PlayerRightClick implements Listener {

  Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);

  @EventHandler
  void onRightClick(PlayerInteractEvent e) {
    Action a = e.getAction();
    if (e.getHand() == EquipmentSlot.OFF_HAND) {
      return;
    }
    if ((a.equals(Action.RIGHT_CLICK_AIR)) || (a.equals(Action.RIGHT_CLICK_BLOCK))) {
      Player p = e.getPlayer();
      PlayerStats pStatClass = new PlayerStats(p);
      if (p.getGameMode() == GameMode.CREATIVE) {
        return;
      }
      Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
      ChangeStats increaseStats = new ChangeStats(p);
      AbilityTracker abilities = new AbilityTracker(p);
      Integer[] pAbilities = abilities.getPlayerAbilities();
      ItemStack itemInHand = p.getInventory().getItemInMainHand();
      Material itemInHandType = itemInHand.getType();
      ItemGroups itemGroups = new ItemGroups();
      List<Material> pickaxes = itemGroups.getPickaxes();
      List<Material> axes = itemGroups.getAxes();
      List<Material> hoes = itemGroups.getHoes();
      List<Material> swords = itemGroups.getSwords();
      List<Material> shovels = itemGroups.getShovels();
      List<Material> noRightClick = itemGroups.getNoRightClick();
      List<Material> actionableBlocks = itemGroups.getActionableBlocks();

      LanguageSelector langManager = new LanguageSelector(p);

      //Right clicking blocks
      if (a.equals(Action.RIGHT_CLICK_BLOCK)) {
        //Composter EXP
        Farming farmingClass = new Farming(p);
        farmingClass.composterEXP(e.getClickedBlock());

        //Repair
        if (e.getClickedBlock().getType() == Material.IRON_BLOCK && !itemInHandType.isBlock()) {
          if (pAbilities[0] != -2 && pAbilities[2] != -2 && pAbilities[7] != -2) {
            Repair repairClass = new Repair(p);
            repairClass.repairItem();
            e.setCancelled(true);
          } else {
            p.sendMessage(ChatColor.RED + langManager.getString("cannotRepair"));
          }
          return;
        }
        //Salvaging
        else if (e.getClickedBlock().getType() == Material.GOLD_BLOCK
            && !itemInHandType.isBlock()) {
          if (p.isSneaking()) {
            if (pAbilities[0] != -2 && pAbilities[2] != -2 && pAbilities[7] != -2) {
              Repair repairClass = new Repair(p);
              repairClass.salvaging();
              e.setCancelled(true);
            } else {
              p.sendMessage(ChatColor.RED + langManager.getString("cannotSalvage"));
            }
            return;
          }
        }
        //Stopping abilities from activating on stuff like doors
        if (actionableBlocks.contains(e.getClickedBlock().getType())) {
          return;
        }
      }

      if (p.getInventory().getItemInOffHand().getType() == Material.TORCH && a.equals(
          Action.RIGHT_CLICK_BLOCK)) {
        return;
      }

      int waitTicks = 0;
      //Shield smoothness
      if (p.getInventory().getItemInOffHand().getType() == Material.SHIELD) {
        waitTicks = 6;
      }

      //Explosions
      if (itemInHandType == Material.FLINT_AND_STEEL) {
        Block blockLit = e.getClickedBlock();
        if (blockLit == null) {
          return;
        }
        WorldGuardChecks BuildingCheck = new WorldGuardChecks();
        ConfigLoad canExplode = new ConfigLoad();
        if (!canExplode.isAllowExplosions()) {
          return;
        }
        if (!BuildingCheck.canBuild(p, blockLit.getLocation())) {
          return;
        }
        if (blockLit.getType() == Material.TNT) {
          e.setCancelled(true);
          Mining miningClass = new Mining(p);
          miningClass.tntExplode(blockLit);
        }
      }

      //Digging
      else if (shovels.contains(itemInHandType)) {
        Digging diggingClass = new Digging(p);
        if (a.equals(Action.RIGHT_CLICK_BLOCK)) {
          if (!(e.getClickedBlock().getType() == Material.GRASS_BLOCK)) {
            new BukkitRunnable() {
              @Override
              public void run() {
                if (p.isOnline()) {
                  if (!p.isBlocking()) {
                    diggingClass.initiateAbility();
                  }
                }
              }
            }.runTaskLater(plugin, waitTicks);

          }
        } else {
          new BukkitRunnable() {
            @Override
            public void run() {
              if (p.isOnline()) {
                if (!p.isBlocking()) {
                  diggingClass.initiateAbility();
                }
              }
            }
          }.runTaskLater(plugin, waitTicks);
        }
      }
      //Woodcutting and AxeMastery
      else if (axes.contains(itemInHandType)) {
        Woodcutting woodcuttingClass = new Woodcutting(p);
        if (a.equals(Action.RIGHT_CLICK_BLOCK)) {
          if (woodcuttingClass.blacklistedBlock(e.getClickedBlock())) {
            return;
          }
        }
        new BukkitRunnable() {
          @Override
          public void run() {
            if (p.isOnline()) {
              if (!p.isBlocking()) {
                woodcuttingClass.initiateAbility();
              }
            }
          }
        }.runTaskLater(plugin, waitTicks);
        AxeMastery axeMasteryClass = new AxeMastery(p);
        new BukkitRunnable() {
          @Override
          public void run() {
            if (p.isOnline()) {
              if (!p.isBlocking()) {
                axeMasteryClass.initiateAbility();
              }
            }
          }
        }.runTaskLater(plugin, waitTicks);
      }
      //Mining
      else if (pickaxes.contains(itemInHandType)) {
        Mining miningClass = new Mining(p);
        new BukkitRunnable() {
          @Override
          public void run() {
            if (p.isOnline()) {
              if (!p.isBlocking()) {
                miningClass.initiateAbility();
              }
            }
          }
        }.runTaskLater(plugin, waitTicks);
      }

      //Farming
      else if (hoes.contains(itemInHandType)) {
        if (a.equals(Action.RIGHT_CLICK_BLOCK)) {
          if (!(e.getClickedBlock().getType() == Material.DIRT
              || e.getClickedBlock().getType() == Material.GRASS_BLOCK
              || e.getClickedBlock().getType() == Material.DIRT_PATH)) {
            Farming farmingClass = new Farming(p);
            farmingClass.initiateAbility();
          } else {
            Material blockAbove = e.getClickedBlock().getRelative(0, 1, 0).getType();
            if (blockAbove == Material.AIR || blockAbove == Material.CAVE_AIR
                || blockAbove == Material.VOID_AIR) {
              ConfigLoad configLoad = new ConfigLoad();
              Map<String, Integer> expMap = configLoad.getExpMapForSkill("farming");
              increaseStats.changeEXP("farming", expMap.get("tillLand"));
            }
          }
        } else {
          Farming farmingClass = new Farming(p);
          farmingClass.initiateAbility();
        }

      } else if (itemInHandType == Material.BONE_MEAL && a.equals(Action.RIGHT_CLICK_BLOCK)) {
        Farming farmingClass = new Farming(p);
        farmingClass.fertilizerSave(e.getClickedBlock());
      }

      //Archery
      else if (itemInHandType == Material.BOW) {
        if (pAbilities[5] > -1) {
          Archery archeryClass = new Archery(p);
          archeryClass.enableAbility();
        }
      } else if (itemInHandType == Material.CROSSBOW) {
        if ((int) pStat.get("archery").get(12) > 0) {
          Archery archeryClass = new Archery(p);
          if (pAbilities[5] > -1) {
            archeryClass.enableAbility();
            archeryClass.crossbowAbility();
          } else if (pAbilities[5] == -2) {
            archeryClass.crossbowAbility();
          }
        }
      }

      //beastMastery
      else if (p.getVehicle() != null) {
        EntityType[] acceptableVehicles0 = {EntityType.HORSE, EntityType.SKELETON_HORSE,
            EntityType.ZOMBIE_HORSE, EntityType.PIG, EntityType.DONKEY, EntityType.MULE,
            EntityType.LLAMA};
        List<EntityType> acceptableVehicles = Arrays.asList(acceptableVehicles0);
        if (acceptableVehicles.contains(p.getVehicle().getType())) {
          if ((noRightClick.contains(itemInHandType) || (a == Action.RIGHT_CLICK_AIR
              && itemInHand.getType().isBlock()))) {
            BeastMastery beastMasteryClass = new BeastMastery(p);
            beastMasteryClass.initiateAbility();
          }
        }
      }

      //Swordsmanship
      else if (swords.contains(itemInHandType)) {
        Swordsmanship swordsmanshipClass = new Swordsmanship(p);
        new BukkitRunnable() {
          @Override
          public void run() {
            if (p.isOnline()) {
              if (!p.isBlocking()) {
                swordsmanshipClass.initiateAbility();
              }
            }
          }
        }.runTaskLater(plugin, waitTicks);
      }

      //Defense
      else if (itemInHandType == Material.AIR) {
        Defense defenseClass = new Defense(p);
        defenseClass.initiateAbility();
      }


    }
  }
}
