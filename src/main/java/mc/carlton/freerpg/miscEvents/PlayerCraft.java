package mc.carlton.freerpg.miscEvents;

import mc.carlton.freerpg.gameTools.LanguageSelector;
import mc.carlton.freerpg.globalVariables.CraftingRecipes;
import mc.carlton.freerpg.perksAndAbilities.Defense;
import mc.carlton.freerpg.playerAndServerInfo.ConfigLoad;
import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Map;

public class PlayerCraft implements Listener {

    public boolean craftingMatch(ItemStack[] customRecipe, ItemStack[] crafting) {
        if (customRecipe.length != 9 || crafting.length != 9) {
            return false;
        }
        for (int i = 0; i<9; i++) {
            if (crafting[i] == null) {
                crafting[i] = new ItemStack(Material.AIR,0);
            }
        }
        for (int i = 0; i<9; i++){
            if (!(customRecipe[i].getType().equals(crafting[i].getType()))) {
                return false;
            }
        }
        return true;
    }

    @EventHandler
    void onPlayerCraft(CraftItemEvent e) {
        Player p = (Player) e.getWhoClicked();
        PlayerStats pStatClass = new PlayerStats(p);
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        LanguageSelector lang = new LanguageSelector(p);
        ConfigLoad configLoad = new ConfigLoad();

        Defense defenseClass = new Defense(p);
        defenseClass.armorEXP(e.getRecipe().getResult());

        CraftingRecipes craftingRecipes = new CraftingRecipes();
        ItemStack[] crafting = e.getInventory().getMatrix();
        ItemStack[] cowEgg = craftingRecipes.getCowEggRecipe();
        ItemStack[] beeEgg = craftingRecipes.getBeeEggRecipe();
        ItemStack[] mooshroomEgg1 = craftingRecipes.getMooshroomEgg1Recipe();
        ItemStack[] mooshroomEgg2 = craftingRecipes.getMooshroomEgg2Recipe();
        ItemStack[] horseEgg = craftingRecipes.getHorseEggRecipe();
        ItemStack[] slimeEgg = craftingRecipes.getSlimeEggRecipe();
        ItemStack[] tippedArrow = craftingRecipes.getTippedArrowRecipe();
        ItemStack[] power = craftingRecipes.getPowerRecipe();
        ItemStack[] efficiency = craftingRecipes.getEfficiencyRecipe();
        ItemStack[] sharpness = craftingRecipes.getSharpnessRecipe();
        ItemStack[] protection = craftingRecipes.getProtectionRecipe();
        ItemStack[] luck = craftingRecipes.getLuckRecipe();
        ItemStack[] lure = craftingRecipes.getLureRecipe();
        ItemStack[] depth = craftingRecipes.getDepthRecipe();
        ItemStack[] frost = craftingRecipes.getFrostRecipe();
        ItemStack[] mending = craftingRecipes.getMendingRecipe();
        ItemStack[] fortune = craftingRecipes.getFortuneRecipe();
        ItemStack[] waterBreathing = craftingRecipes.getWaterBreathingRecipe();
        ItemStack[] speed = craftingRecipes.getSpeedRecipe();
        ItemStack[] fireResistance = craftingRecipes.getFireResistanceRecipe();
        ItemStack[] healing = craftingRecipes.getHealingRecipe();
        ItemStack[] strength = craftingRecipes.getStrengthRecipe();



        if (craftingMatch(cowEgg,crafting)) {
            if ((int)pStat.get("farming").get(8) < 1 || !configLoad.getAllowedSkillsMap().get("farming")) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("farmingPerkTitle1") + " (1/5)"  + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString("craftRequirement"));
            }
        }
        else if (craftingMatch(beeEgg,crafting)) {
            if ((int)pStat.get("farming").get(8) < 2 || !configLoad.getAllowedSkillsMap().get("farming")) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("farmingPerkTitle1") + " (2/5)"   + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString("craftRequirement"));
            }
        }
        else if (craftingMatch(mooshroomEgg1,crafting) || craftingMatch(mooshroomEgg2,crafting)) {
            if ((int)pStat.get("farming").get(8) < 3 || !configLoad.getAllowedSkillsMap().get("farming")) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("farmingPerkTitle1") + " (3/5)"   + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString("craftRequirement"));
            }
        }
        else if (craftingMatch(horseEgg,crafting)) {
            if ((int)pStat.get("farming").get(8) < 4 || !configLoad.getAllowedSkillsMap().get("farming")) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("farmingPerkTitle1") + " (4/5)"   + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString("craftRequirement"));
            }
        }
        else if (craftingMatch(slimeEgg,crafting)) {
            if ((int)pStat.get("farming").get(8) < 5 || !configLoad.getAllowedSkillsMap().get("farming")) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("farmingPerkTitle1") + " (5/5)"   + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString("craftRequirement"));
            }
        }
        else if (craftingMatch(tippedArrow,crafting)) {
            if ((int)pStat.get("archery").get(11) < 1 || !configLoad.getAllowedSkillsMap().get("archery")) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("archeryPerkTitle4")  + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString("craftRequirement"));
            }
        }
        else if (craftingMatch(power,crafting) || craftingMatch(efficiency,crafting)) {
            if ((int)pStat.get("enchanting").get(9) < 1 || !configLoad.getAllowedSkillsMap().get("enchanting")) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("enchantingPerkTitle1") + " (1/5)"  + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString("craftRequirement"));
            }
            else if (p.getLevel() < 1) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + lang.getString("craftXPRequirement0"));
            }
            else {
                p.setLevel(p.getLevel()-1);
                p.getWorld().playEffect(p.getLocation(), Effect.ANVIL_USE,1);
            }
        }
        else if (craftingMatch(sharpness,crafting) || craftingMatch(protection,crafting)) {
            if ((int)pStat.get("enchanting").get(9) < 2 || !configLoad.getAllowedSkillsMap().get("enchanting")) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("enchantingPerkTitle1") + " (2/5)"  + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString("craftRequirement"));
            }
            else if (p.getLevel() < 1) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + lang.getString("craftXPRequirement0"));
            }
            else {
                p.setLevel(p.getLevel()-1);
                p.getWorld().playEffect(p.getLocation(), Effect.ANVIL_USE,1);
            }
        }
        else if (craftingMatch(luck,crafting) || craftingMatch(lure,crafting)) {
            if ((int)pStat.get("enchanting").get(9) < 3 || !configLoad.getAllowedSkillsMap().get("enchanting")) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("enchantingPerkTitle1") + " (3/5)"  + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString("craftRequirement"));
            }
            else if (p.getLevel() < 1) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + lang.getString("craftXPRequirement0"));
            }
            else {
                p.setLevel(p.getLevel()-1);
                p.getWorld().playEffect(p.getLocation(), Effect.ANVIL_USE,1);
            }
        }
        else if (craftingMatch(depth,crafting) || craftingMatch(frost,crafting)) {
            if ((int)pStat.get("enchanting").get(9) < 4 || !configLoad.getAllowedSkillsMap().get("enchanting")) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("enchantingPerkTitle1") + " (4/5)"  + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString("craftRequirement"));
            }
            else if (p.getLevel() < 1) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + lang.getString("craftXPRequirement0"));
            }
            else {
                p.setLevel(p.getLevel()-1);
                p.getWorld().playEffect(p.getLocation(), Effect.ANVIL_USE,1);
            }
        }
        else if (craftingMatch(mending,crafting)) {
            if ((int)pStat.get("enchanting").get(9) < 5 || !configLoad.getAllowedSkillsMap().get("enchanting")) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("enchantingPerkTitle1") + " (5/5)"  + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString("craftRequirement"));
            }
            else if (p.getLevel() < 10) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + lang.getString("craftXPRequirement1"));
            }
            else {
                p.setLevel(p.getLevel()-10);
                p.getWorld().playEffect(p.getLocation(), Effect.ANVIL_USE,1);
            }
        }
        else if (craftingMatch(fortune,crafting)) {
            if ((int)pStat.get("enchanting").get(9) < 5 || !configLoad.getAllowedSkillsMap().get("enchanting")) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("enchantingPerkTitle1") + " (5/5)"  + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString("craftRequirement"));
            }
            else if (p.getLevel() < 2) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + lang.getString("craftXPRequirement2"));
            }
            else {
                p.setLevel(p.getLevel()-2);
                p.getWorld().playEffect(p.getLocation(), Effect.ANVIL_USE,1);
            }
        }
        else if (craftingMatch(waterBreathing,crafting)) {
            if ((int)pStat.get("alchemy").get(7) < 1 || !configLoad.getAllowedSkillsMap().get("alchemy")) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("alchemyPerkTitle0") + " (1/5)"  + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString("craftRequirement"));
            }
        }
        else if (craftingMatch(speed,crafting)) {
            if ((int)pStat.get("alchemy").get(7) < 2 || !configLoad.getAllowedSkillsMap().get("alchemy")) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("alchemyPerkTitle0") + " (2/5)"  + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString("craftRequirement"));
            }
        }
        else if (craftingMatch(fireResistance,crafting)) {
            if ((int)pStat.get("alchemy").get(7) < 3 || !configLoad.getAllowedSkillsMap().get("alchemy")) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("alchemyPerkTitle0") + " (3/5)"  + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString("craftRequirement"));
            }
        }
        else if (craftingMatch(healing,crafting)) {
            if ((int)pStat.get("alchemy").get(7) < 4 || !configLoad.getAllowedSkillsMap().get("alchemy")) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("alchemyPerkTitle0") + " (4/5)"  + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString("craftRequirement"));
            }
        }
        else if (craftingMatch(strength,crafting)) {
            if ((int)pStat.get("alchemy").get(7) < 5 || !configLoad.getAllowedSkillsMap().get("alchemy")) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("alchemyPerkTitle0") + " (5/5)"  + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString("craftRequirement"));
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
