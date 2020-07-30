package mc.carlton.freerpg.miscEvents;

import mc.carlton.freerpg.gameTools.LanguageSelector;
import mc.carlton.freerpg.perksAndAbilities.Defense;
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

        Defense defenseClass = new Defense(p);
        defenseClass.armorEXP(e.getRecipe().getResult());

        ItemStack[] crafting = e.getInventory().getMatrix();
        ItemStack[] cowEgg = {new ItemStack(Material.LEATHER,1), new ItemStack(Material.BEEF,1),new ItemStack(Material.LEATHER,1),
                              new ItemStack(Material.BEEF,1), new ItemStack(Material.BONE,1), new ItemStack(Material.BEEF,1),
                              new ItemStack(Material.LEATHER,1), new ItemStack(Material.BEEF,1),new ItemStack(Material.LEATHER,1)};
        ItemStack[] beeEgg = {new ItemStack(Material.AIR,0), new ItemStack(Material.OXEYE_DAISY,1),new ItemStack(Material.AIR,0),
                              new ItemStack(Material.DANDELION,1), new ItemStack(Material.HONEY_BOTTLE,1), new ItemStack(Material.POPPY,1),
                              new ItemStack(Material.AIR,0), new ItemStack(Material.AZURE_BLUET,1),new ItemStack(Material.AIR,0)};
        ItemStack[] mooshroomEgg1 = {new ItemStack(Material.LEATHER,1), new ItemStack(Material.RED_MUSHROOM,1),new ItemStack(Material.LEATHER,1),
                                     new ItemStack(Material.BEEF,1), new ItemStack(Material.BONE,1), new ItemStack(Material.BEEF,1),
                                     new ItemStack(Material.LEATHER,1), new ItemStack(Material.BEEF,1),new ItemStack(Material.LEATHER,1)};
        ItemStack[] mooshroomEgg2 = {new ItemStack(Material.LEATHER,1), new ItemStack(Material.BROWN_MUSHROOM,1),new ItemStack(Material.LEATHER,1),
                                     new ItemStack(Material.BEEF,1), new ItemStack(Material.BONE,1), new ItemStack(Material.BEEF,1),
                                     new ItemStack(Material.LEATHER,1), new ItemStack(Material.BEEF,1),new ItemStack(Material.LEATHER,1)};
        ItemStack[] horseEgg = {new ItemStack(Material.LEATHER,1), new ItemStack(Material.SADDLE,1),new ItemStack(Material.LEATHER,1),
                                new ItemStack(Material.LEATHER,1), new ItemStack(Material.BONE,1), new ItemStack(Material.LEATHER,1),
                                new ItemStack(Material.HAY_BLOCK,1), new ItemStack(Material.HAY_BLOCK,1),new ItemStack(Material.HAY_BLOCK,1)};
        ItemStack[] slimeEgg = {new ItemStack(Material.AIR,0), new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0),
                                new ItemStack(Material.AIR,0), new ItemStack(Material.SLIME_BALL,1),new ItemStack(Material.SLIME_BALL,1),
                                new ItemStack(Material.AIR,0), new ItemStack(Material.SLIME_BALL,1),new ItemStack(Material.SLIME_BALL,1)};
        ItemStack[] tippedArrow = {new ItemStack(Material.ARROW,1), new ItemStack(Material.ARROW,1),new ItemStack(Material.ARROW,1),
                                   new ItemStack(Material.ARROW,1), new ItemStack(Material.POTION,1),new ItemStack(Material.ARROW,1),
                                   new ItemStack(Material.ARROW,1), new ItemStack(Material.ARROW,1),new ItemStack(Material.ARROW,1)};

        ItemStack[] power = {new ItemStack(Material.AIR,0), new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0),
                             new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.PAPER,1),
                             new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.BOW,1)};

        ItemStack[] efficiency = {new ItemStack(Material.AIR,0), new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0),
                                 new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.PAPER,1),
                                 new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.IRON_PICKAXE,1)};

        ItemStack[] sharpness = {new ItemStack(Material.IRON_INGOT,1), new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0),
                                 new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.PAPER,1),
                                 new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.IRON_SWORD,1)};

        ItemStack[] protection = {new ItemStack(Material.AIR,0), new ItemStack(Material.IRON_INGOT,1),new ItemStack(Material.AIR,0),
                                  new ItemStack(Material.IRON_INGOT,1), new ItemStack(Material.PAPER,1),new ItemStack(Material.PAPER,1),
                                  new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.IRON_INGOT,1)};

        ItemStack[] luck = {new ItemStack(Material.RABBIT_FOOT,1), new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0),
                            new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.PAPER,1),
                            new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.FISHING_ROD,1)};

        ItemStack[] lure = {new ItemStack(Material.COD_BUCKET,1), new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0),
                            new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.PAPER,1),
                            new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.FISHING_ROD,1)};

        ItemStack[] frost = {new ItemStack(Material.AIR,0), new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0),
                             new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.PAPER,1),
                             new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.BLUE_ICE,1)};

        ItemStack[] depth = {new ItemStack(Material.AIR,0), new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0),
                             new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.PAPER,1),
                             new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.NAUTILUS_SHELL,1)};

        ItemStack[] mending = {new ItemStack(Material.AIR,0), new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0),
                               new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.PAPER,1),
                               new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.DIAMOND_BLOCK,1)};

        ItemStack[] fortune = {new ItemStack(Material.AIR,0), new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0),
                               new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.PAPER,1),
                               new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.GOLD_BLOCK,1)};

        ItemStack[] waterBreathing = {new ItemStack(Material.AIR,0), new ItemStack(Material.PUFFERFISH,1),new ItemStack(Material.AIR,0),
                                      new ItemStack(Material.AIR,0), new ItemStack(Material.GLASS_BOTTLE,1),new ItemStack(Material.AIR,0),
                                      new ItemStack(Material.AIR,0), new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0)};

        ItemStack[] speed = {new ItemStack(Material.AIR,0), new ItemStack(Material.SUGAR,1),new ItemStack(Material.AIR,0),
                             new ItemStack(Material.AIR,0), new ItemStack(Material.GLASS_BOTTLE,1),new ItemStack(Material.AIR,0),
                             new ItemStack(Material.AIR,0), new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0)};

        ItemStack[] fireResistance = {new ItemStack(Material.AIR,0), new ItemStack(Material.MAGMA_CREAM,1),new ItemStack(Material.AIR,0),
                                      new ItemStack(Material.AIR,0), new ItemStack(Material.GLASS_BOTTLE,1),new ItemStack(Material.AIR,0),
                                      new ItemStack(Material.AIR,0), new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0)};

        ItemStack[] healing = {new ItemStack(Material.AIR,0), new ItemStack(Material.GLISTERING_MELON_SLICE,1),new ItemStack(Material.AIR,0),
                               new ItemStack(Material.AIR,0), new ItemStack(Material.GLASS_BOTTLE,1),new ItemStack(Material.AIR,0),
                               new ItemStack(Material.AIR,0), new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0)};

        ItemStack[] strength = {new ItemStack(Material.AIR,0), new ItemStack(Material.BLAZE_POWDER,1),new ItemStack(Material.AIR,0),
                                new ItemStack(Material.AIR,0), new ItemStack(Material.GLASS_BOTTLE,1),new ItemStack(Material.AIR,0),
                                new ItemStack(Material.AIR,0), new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0)};



        if (craftingMatch(cowEgg,crafting)) {
            if ((int)pStat.get("farming").get(8) < 1) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("farmingPerkTitle1") + " (1/5)"  + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString("craftRequirement"));
            }
        }
        else if (craftingMatch(beeEgg,crafting)) {
            if ((int)pStat.get("farming").get(8) < 2) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("farmingPerkTitle1") + " (2/5)"   + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString("craftRequirement"));
            }
        }
        else if (craftingMatch(mooshroomEgg1,crafting) || craftingMatch(mooshroomEgg2,crafting)) {
            if ((int)pStat.get("farming").get(8) < 3) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("farmingPerkTitle1") + " (3/5)"   + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString("craftRequirement"));
            }
        }
        else if (craftingMatch(horseEgg,crafting)) {
            if ((int)pStat.get("farming").get(8) < 4) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("farmingPerkTitle1") + " (4/5)"   + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString("craftRequirement"));
            }
        }
        else if (craftingMatch(slimeEgg,crafting)) {
            if ((int)pStat.get("farming").get(8) < 5) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("farmingPerkTitle1") + " (5/5)"   + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString("craftRequirement"));
            }
        }
        else if (craftingMatch(tippedArrow,crafting)) {
            if ((int)pStat.get("archery").get(11) < 1) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("archeryPerkTitle4")  + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString("craftRequirement"));
            }
        }
        else if (craftingMatch(power,crafting) || craftingMatch(efficiency,crafting)) {
            if ((int)pStat.get("enchanting").get(9) < 1) {
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
            if ((int)pStat.get("enchanting").get(9) < 2) {
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
            if ((int)pStat.get("enchanting").get(9) < 3) {
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
            if ((int)pStat.get("enchanting").get(9) < 4) {
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
            if ((int)pStat.get("enchanting").get(9) < 5) {
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
            if ((int)pStat.get("enchanting").get(9) < 5) {
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
            if ((int)pStat.get("alchemy").get(7) < 1) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("alchemyPerk0") + " (1/5)"  + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString("craftRequirement"));
            }
        }
        else if (craftingMatch(speed,crafting)) {
            if ((int)pStat.get("alchemy").get(7) < 2) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("alchemyPerk0") + " (2/5)"  + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString("craftRequirement"));
            }
        }
        else if (craftingMatch(fireResistance,crafting)) {
            if ((int)pStat.get("alchemy").get(7) < 3) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("alchemyPerk0") + " (3/5)"  + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString("craftRequirement"));
            }
        }
        else if (craftingMatch(healing,crafting)) {
            if ((int)pStat.get("alchemy").get(7) < 4) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("alchemyPerk0") + " (4/5)"  + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString("craftRequirement"));
            }
        }
        else if (craftingMatch(strength,crafting)) {
            if ((int)pStat.get("alchemy").get(7) < 5) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + lang.getString("alchemyPerk0") + " (5/5)"  + ChatColor.RESET + ChatColor.RED.toString() + " " + lang.getString("craftRequirement"));
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
