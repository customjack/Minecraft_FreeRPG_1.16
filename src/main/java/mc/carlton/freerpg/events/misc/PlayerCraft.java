package mc.carlton.freerpg.events.misc;

import java.util.ArrayList;
import java.util.Map;
import mc.carlton.freerpg.config.ConfigLoad;
import mc.carlton.freerpg.core.info.player.PlayerStats;
import mc.carlton.freerpg.customContainers.collections.OldCustomRecipe;
import mc.carlton.freerpg.events.newEvents.FrpgPlayerCraftItemEvent;
import mc.carlton.freerpg.skills.perksAndAbilities.Defense;
import mc.carlton.freerpg.utils.game.LanguageSelector;
import mc.carlton.freerpg.utils.globalVariables.CraftingRecipes;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerCraft implements Listener {

  private boolean craftingMatch(ArrayList<Material> customRecipe, ArrayList<Material> crafting) {
    if (customRecipe.size() != 9 || crafting.size() != 9) {
      return false;
    }
    CraftingRecipes craftingRecipes = new CraftingRecipes();
    ArrayList<ArrayList<Material>> allCustomRecipes = craftingRecipes.getTranslatedVariants(
        customRecipe);
    for (ArrayList<Material> customRecipeVariant : allCustomRecipes) {
      if (customRecipeVariant.equals(crafting)) {
        return true;
      }
    }
    return false;
  }

  private int getAmountToBeCrafted(ItemStack[] craftingMatrix) {
    int limitingMaterialAmount = Integer.MAX_VALUE;
    for (int i = 0; i < 9; i++) {
      if (craftingMatrix[i] != null) {
        if (!craftingMatrix[i].getType().equals(Material.AIR)) {
          int amountOfMaterial = craftingMatrix[i].getAmount();
          if (amountOfMaterial < limitingMaterialAmount) {
            limitingMaterialAmount = amountOfMaterial;
          }
        }
      }
    }
    return (limitingMaterialAmount == Integer.MAX_VALUE) ? 0 : limitingMaterialAmount;
  }

  private ArrayList<Material> getRecipeMaterial(ItemStack[] craftingMatrix) {
    ArrayList<Material> crafting = new ArrayList<>();
    for (int i = 0; i < 9; i++) {
      if (craftingMatrix[i] == null) {
        crafting.add(Material.AIR);
      } else {
        crafting.add(craftingMatrix[i].getType());
      }
    }
    return crafting;
  }

  @EventHandler(priority = EventPriority.HIGH)
  void onFrpgPlayerCraft(FrpgPlayerCraftItemEvent e) {
    Defense defenseClass = new Defense(e.getPlayer());
    defenseClass.armorEXP(e.getResult());
  }

  @EventHandler(priority = EventPriority.HIGH)
  void onPlayerCraft(CraftItemEvent e) {
    Player p = (Player) e.getWhoClicked();
    PlayerStats pStatClass = new PlayerStats(p);
    Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
    LanguageSelector lang = new LanguageSelector(p);
    ConfigLoad configLoad = new ConfigLoad();

    CraftingRecipes craftingRecipes = new CraftingRecipes();
    ItemStack[] craftingMatrix = e.getInventory().getMatrix();
    if (craftingMatrix.length != 9) {
      return;
    }
    ArrayList<Material> crafting = getRecipeMaterial(craftingMatrix);
    int amountCrafted = getAmountToBeCrafted(craftingMatrix);

    Map<String, OldCustomRecipe> craftingRecipeClasses = configLoad.getCraftingRecipes();
    ArrayList<Material> cowEgg = craftingRecipes.getCowEggRecipe();
    ArrayList<Material> beeEgg = craftingRecipes.getBeeEggRecipe();
    ArrayList<Material> mooshroomEgg = craftingRecipes.getMooshroomEggRecipe();
    ArrayList<Material> horseEgg = craftingRecipes.getHorseEggRecipe();
    ArrayList<Material> slimeEgg = craftingRecipes.getSlimeEggRecipe();
    ArrayList<Material> tippedArrow = craftingRecipes.getTippedArrowRecipe();
    ArrayList<Material> power = craftingRecipes.getPowerRecipe();
    ArrayList<Material> efficiency = craftingRecipes.getEfficiencyRecipe();
    ArrayList<Material> sharpness = craftingRecipes.getSharpnessRecipe();
    ArrayList<Material> protection = craftingRecipes.getProtectionRecipe();
    ArrayList<Material> luck = craftingRecipes.getLuckRecipe();
    ArrayList<Material> lure = craftingRecipes.getLureRecipe();
    ArrayList<Material> depth = craftingRecipes.getDepthRecipe();
    ArrayList<Material> frost = craftingRecipes.getFrostRecipe();
    ArrayList<Material> mending = craftingRecipes.getMendingRecipe();
    ArrayList<Material> fortune = craftingRecipes.getFortuneRecipe();
    ArrayList<Material> waterBreathing = craftingRecipes.getWaterBreathingRecipe();
    ArrayList<Material> speed = craftingRecipes.getSpeedRecipe();
    ArrayList<Material> fireResistance = craftingRecipes.getFireResistanceRecipe();
    ArrayList<Material> healing = craftingRecipes.getHealingRecipe();
    ArrayList<Material> strength = craftingRecipes.getStrengthRecipe();

    if (craftingMatch(cowEgg, crafting)) {
      if ((int) pStat.get("farming").get(8) < 1 || !configLoad.getAllowedSkillsMap()
          .get("farming")) {
        e.setCancelled(true);
        p.sendMessage(
            ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("farmingPerkTitle1")
                + " (1/5)" + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString(
                "craftRequirement"));
      }
    } else if (craftingMatch(beeEgg, crafting)) {
      if ((int) pStat.get("farming").get(8) < 2 || !configLoad.getAllowedSkillsMap()
          .get("farming")) {
        e.setCancelled(true);
        p.sendMessage(
            ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("farmingPerkTitle1")
                + " (2/5)" + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString(
                "craftRequirement"));
      }
    } else if (craftingMatch(mooshroomEgg, crafting)) {
      if ((int) pStat.get("farming").get(8) < 3 || !configLoad.getAllowedSkillsMap()
          .get("farming")) {
        e.setCancelled(true);
        p.sendMessage(
            ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("farmingPerkTitle1")
                + " (3/5)" + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString(
                "craftRequirement"));
      }
    } else if (craftingMatch(horseEgg, crafting)) {
      if ((int) pStat.get("farming").get(8) < 4 || !configLoad.getAllowedSkillsMap()
          .get("farming")) {
        e.setCancelled(true);
        p.sendMessage(
            ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("farmingPerkTitle1")
                + " (4/5)" + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString(
                "craftRequirement"));
      }
    } else if (craftingMatch(slimeEgg, crafting)) {
      if ((int) pStat.get("farming").get(8) < 5 || !configLoad.getAllowedSkillsMap()
          .get("farming")) {
        e.setCancelled(true);
        p.sendMessage(
            ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("farmingPerkTitle1")
                + " (5/5)" + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString(
                "craftRequirement"));
      }
    } else if (craftingMatch(tippedArrow, crafting)) {
      if ((int) pStat.get("archery").get(11) < 1 || !configLoad.getAllowedSkillsMap()
          .get("archery")) {
        e.setCancelled(true);
        p.sendMessage(
            ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("archeryPerkTitle4")
                + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString(
                "craftRequirement"));
      }
    } else if (craftingMatch(power, crafting) || craftingMatch(efficiency, crafting)) {
      int xpLevelCost = 0;
      if (craftingMatch(power, crafting)) {
        xpLevelCost = craftingRecipeClasses.get("enchanting1").getXPcraftCost();
      } else {
        xpLevelCost = craftingRecipeClasses.get("enchanting2").getXPcraftCost();
      }
      xpLevelCost *= (e.isShiftClick()) ? amountCrafted : 1;

      String xpLevel_Id = "xpLevel";
      if (xpLevelCost != 1) {
        xpLevel_Id += "s";
      }
      if ((int) pStat.get("enchanting").get(9) < 1 || !configLoad.getAllowedSkillsMap()
          .get("enchanting")) {
        e.setCancelled(true);
        p.sendMessage(
            ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("enchantingPerkTitle1")
                + " (1/5)" + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString(
                "craftRequirement"));
      } else if (p.getLevel() < xpLevelCost) {
        e.setCancelled(true);
        p.sendMessage(ChatColor.RED + lang.getString("craftXPRequirement") + " " + xpLevelCost + " "
            + lang.getString(xpLevel_Id));
      } else {
        p.setLevel(p.getLevel() - xpLevelCost);
        p.getWorld().playEffect(p.getLocation(), Effect.ANVIL_USE, 1);
      }
    } else if (craftingMatch(sharpness, crafting) || craftingMatch(protection, crafting)) {
      int xpLevelCost = 0;
      if (craftingMatch(sharpness, crafting)) {
        xpLevelCost = craftingRecipeClasses.get("enchanting3").getXPcraftCost();
      } else {
        xpLevelCost = craftingRecipeClasses.get("enchanting4").getXPcraftCost();
      }
      xpLevelCost *= (e.isShiftClick()) ? amountCrafted : 1;

      String xpLevel_Id = "xpLevel";
      if (xpLevelCost != 1) {
        xpLevel_Id += "s";
      }
      if ((int) pStat.get("enchanting").get(9) < 2 || !configLoad.getAllowedSkillsMap()
          .get("enchanting")) {
        e.setCancelled(true);
        p.sendMessage(
            ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("enchantingPerkTitle1")
                + " (2/5)" + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString(
                "craftRequirement"));
      } else if (p.getLevel() < xpLevelCost) {
        e.setCancelled(true);
        p.sendMessage(ChatColor.RED + lang.getString("craftXPRequirement") + " " + xpLevelCost + " "
            + lang.getString(xpLevel_Id));
      } else {
        p.setLevel(p.getLevel() - xpLevelCost);
        p.getWorld().playEffect(p.getLocation(), Effect.ANVIL_USE, 1);
      }
    } else if (craftingMatch(luck, crafting) || craftingMatch(lure, crafting)) {
      int xpLevelCost = 0;
      if (craftingMatch(luck, crafting)) {
        xpLevelCost = craftingRecipeClasses.get("enchanting5").getXPcraftCost();
      } else {
        xpLevelCost = craftingRecipeClasses.get("enchanting6").getXPcraftCost();
      }
      xpLevelCost *= (e.isShiftClick()) ? amountCrafted : 1;

      String xpLevel_Id = "xpLevel";
      if (xpLevelCost != 1) {
        xpLevel_Id += "s";
      }
      if ((int) pStat.get("enchanting").get(9) < 3 || !configLoad.getAllowedSkillsMap()
          .get("enchanting")) {
        e.setCancelled(true);
        p.sendMessage(
            ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("enchantingPerkTitle1")
                + " (3/5)" + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString(
                "craftRequirement"));
      } else if (p.getLevel() < xpLevelCost) {
        e.setCancelled(true);
        p.sendMessage(ChatColor.RED + lang.getString("craftXPRequirement") + " " + xpLevelCost + " "
            + lang.getString(xpLevel_Id));
      } else {
        p.setLevel(p.getLevel() - xpLevelCost);
        p.getWorld().playEffect(p.getLocation(), Effect.ANVIL_USE, 1);
      }
    } else if (craftingMatch(depth, crafting) || craftingMatch(frost, crafting)) {
      int xpLevelCost = 0;
      if (craftingMatch(frost, crafting)) {
        xpLevelCost = craftingRecipeClasses.get("enchanting7").getXPcraftCost();
      } else {
        xpLevelCost = craftingRecipeClasses.get("enchanting8").getXPcraftCost();
      }
      xpLevelCost *= (e.isShiftClick()) ? amountCrafted : 1;

      String xpLevel_Id = "xpLevel";
      if (xpLevelCost != 1) {
        xpLevel_Id += "s";
      }
      if ((int) pStat.get("enchanting").get(9) < 4 || !configLoad.getAllowedSkillsMap()
          .get("enchanting")) {
        e.setCancelled(true);
        p.sendMessage(
            ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("enchantingPerkTitle1")
                + " (4/5)" + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString(
                "craftRequirement"));
      } else if (p.getLevel() < xpLevelCost) {
        e.setCancelled(true);
        p.sendMessage(ChatColor.RED + lang.getString("craftXPRequirement") + " " + xpLevelCost + " "
            + lang.getString(xpLevel_Id));
      } else {
        p.setLevel(p.getLevel() - xpLevelCost);
        p.getWorld().playEffect(p.getLocation(), Effect.ANVIL_USE, 1);
      }
    } else if (craftingMatch(mending, crafting) || craftingMatch(fortune, crafting)) {
      int xpLevelCost = 0;
      if (craftingMatch(mending, crafting)) {
        xpLevelCost = craftingRecipeClasses.get("enchanting9").getXPcraftCost();
      } else {
        xpLevelCost = craftingRecipeClasses.get("enchanting10").getXPcraftCost();
      }
      xpLevelCost *= (e.isShiftClick()) ? amountCrafted : 1;

      String xpLevel_Id = "xpLevel";
      if (xpLevelCost != 1) {
        xpLevel_Id += "s";
      }
      if ((int) pStat.get("enchanting").get(9) < 5 || !configLoad.getAllowedSkillsMap()
          .get("enchanting")) {
        e.setCancelled(true);
        p.sendMessage(
            ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("enchantingPerkTitle1")
                + " (5/5)" + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString(
                "craftRequirement"));
      } else if (p.getLevel() < xpLevelCost) {
        e.setCancelled(true);
        p.sendMessage(ChatColor.RED + lang.getString("craftXPRequirement") + " " + xpLevelCost + " "
            + lang.getString(xpLevel_Id));
      } else {
        p.setLevel(p.getLevel() - xpLevelCost);
        p.getWorld().playEffect(p.getLocation(), Effect.ANVIL_USE, 1);
      }
    } else if (craftingMatch(waterBreathing, crafting)) {
      if ((int) pStat.get("alchemy").get(7) < 1 || !configLoad.getAllowedSkillsMap()
          .get("alchemy")) {
        e.setCancelled(true);
        p.sendMessage(
            ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("alchemyPerkTitle0")
                + " (1/5)" + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString(
                "craftRequirement"));
      }
    } else if (craftingMatch(speed, crafting)) {
      if ((int) pStat.get("alchemy").get(7) < 2 || !configLoad.getAllowedSkillsMap()
          .get("alchemy")) {
        e.setCancelled(true);
        p.sendMessage(
            ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("alchemyPerkTitle0")
                + " (2/5)" + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString(
                "craftRequirement"));
      }
    } else if (craftingMatch(fireResistance, crafting)) {
      if ((int) pStat.get("alchemy").get(7) < 3 || !configLoad.getAllowedSkillsMap()
          .get("alchemy")) {
        e.setCancelled(true);
        p.sendMessage(
            ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("alchemyPerkTitle0")
                + " (3/5)" + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString(
                "craftRequirement"));
      }
    } else if (craftingMatch(healing, crafting)) {
      if ((int) pStat.get("alchemy").get(7) < 4 || !configLoad.getAllowedSkillsMap()
          .get("alchemy")) {
        e.setCancelled(true);
        p.sendMessage(
            ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("alchemyPerkTitle0")
                + " (4/5)" + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString(
                "craftRequirement"));
      }
    } else if (craftingMatch(strength, crafting)) {
      if ((int) pStat.get("alchemy").get(7) < 5 || !configLoad.getAllowedSkillsMap()
          .get("alchemy")) {
        e.setCancelled(true);
        p.sendMessage(
            ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("alchemyPerkTitle0")
                + " (5/5)" + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString(
                "craftRequirement"));
      }
    }





        /* EXAMPLE FOR CRAFTING PERMS
        ItemStack[] TNT2 = {null,                                   new ItemStack(Material.SAND,1),      null,
                            new ItemStack(Material.SAND,1), new ItemStack(Material.GUNPOWDER,1), new ItemStack(Material.SAND,1),
                            null,                                   new ItemStack(Material.SAND,1),      null                                   };

        //TNT crafting
        if (craftingMatch(TNT2,crafting)) {

             if ((int)pStat.get("mining").get(12) < 1) {
                 e.setCancelled(true);
                 p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "More Bombs"  + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString("craftRequirement));
             }
        }
        */

  }

}
