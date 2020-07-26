package mc.carlton.freerpg.guiCommands;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.playerAndServerInfo.ChangeStats;
import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Map;

public class MainGUI implements CommandExecutor {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Inventory gui = Bukkit.createInventory(p, 45, "Skills");

            //Menu Options(Items)
            ItemStack global = new ItemStack(Material.NETHER_STAR);
            ItemStack digging = new ItemStack(Material.IRON_SHOVEL);
            ItemStack woodcutting = new ItemStack(Material.IRON_AXE);
            ItemStack mining = new ItemStack(Material.IRON_PICKAXE);
            ItemStack farming = new ItemStack(Material.IRON_HOE);
            ItemStack fishing = new ItemStack(Material.FISHING_ROD);
            ItemStack archery = new ItemStack(Material.BOW);
            ItemStack beastMastery = new ItemStack(Material.BONE);
            ItemStack swords = new ItemStack(Material.IRON_SWORD);
            ItemStack armor = new ItemStack(Material.IRON_CHESTPLATE);
            ItemStack axes = new ItemStack(Material.GOLDEN_AXE);
            ItemStack repair = new ItemStack(Material.ANVIL);
            ItemStack agility = new ItemStack(Material.LEATHER_LEGGINGS);
            ItemStack alchemy = new ItemStack(Material.POTION);
            ItemStack smelting = new ItemStack(Material.COAL);
            ItemStack enchanting = new ItemStack(Material.ENCHANTING_TABLE);
            ItemStack tokens = new ItemStack(Material.GOLD_INGOT);
            ItemStack configuration = new ItemStack(Material.REDSTONE);

            ItemStack[] menu_items = {global,digging,woodcutting,mining,farming,fishing,archery,beastMastery,swords,armor,axes,repair,agility,alchemy,smelting,enchanting,tokens,configuration};
            String[] labels = {"Global","Digging","Woodcutting","Mining","Farming","Fishing","Archery","Beast Mastery","Swordsmanship","Defense","Axe Mastery","Repair","Agility","Alchemy","Smelting","Enchanting","Tokens","Configuration"};
            String[] labels0 = {"global","digging", "woodcutting", "mining", "farming", "fishing", "archery", "beastMastery", "swordsmanship", "defense", "axeMastery", "repair", "agility", "alchemy", "smelting", "enchanting"};
            Integer[] indices = {4,11,12,13,14,15,20,21,22,23,24,29,30,31,32,33,36,44};
            PlayerStats pStatClass = new PlayerStats(p);
            Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();

            //Set item positions and lore for all items in the GUI
            for (int i = 0; i < labels.length; i++) {
                ItemStack item = menu_items[i];
                ArrayList<String> lore_skills = new ArrayList<>();
                if (i < 16 && i!=0) {
                    int passiveTokens = (int) pStat.get(labels0[i]).get(2);
                    int skillTokens = (int) pStat.get(labels0[i]).get(3);
                    if (passiveTokens > 0 || skillTokens > 0) {
                        item.addUnsafeEnchantment(Enchantment.DURABILITY,1);
                    }
                    int level = pStat.get(labels0[i]).get(0).intValue();
                    int EXP = pStat.get(labels0[i]).get(1).intValue();
                    lore_skills.add(ChatColor.GRAY + "Level: " + ChatColor.BLUE + String.valueOf(level));
                    lore_skills.add(ChatColor.GRAY + "Experience: " + ChatColor.BLUE + String.valueOf(EXP));
                    ChangeStats getEXP = new ChangeStats(p);
                    int nextEXP = getEXP.getEXPfromLevel(level+1);
                    int EXPtoNext = nextEXP - EXP;
                    lore_skills.add(ChatColor.GRAY + "EXP to next level: " + ChatColor.GREEN + String.valueOf(EXPtoNext));

                }
                else if (i==0) {
                    int globalTokens = (int) pStat.get(labels0[i]).get(1);
                    int totalLevel = pStat.get("global").get(0).intValue();
                    lore_skills.add(ChatColor.GRAY + "Total Level: " + ChatColor.GOLD + String.valueOf(totalLevel));
                    if (globalTokens > 0) {
                        item.addUnsafeEnchantment(Enchantment.DURABILITY,1);
                    }
                }
                ItemMeta meta = item.getItemMeta();
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                meta.setDisplayName(ChatColor.AQUA + ChatColor.BOLD.toString() + labels[i]);
                meta.setLore(lore_skills);


                if (indices[i] == 36) {
                    int totTokens_S = 0;
                    int totTokens_P = 0;
                    int gTokens = 0;
                    for (String j : pStat.keySet()){

                        if (j.equalsIgnoreCase("global")){
                            gTokens = pStat.get(j).get(1).intValue();
                        }
                        else {
                            totTokens_P += pStat.get(j).get(2).intValue();
                            totTokens_S += pStat.get(j).get(3).intValue();
                        }
                    }
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.GRAY + "Global Tokens: " + ChatColor.GOLD + String.valueOf(gTokens));
                    lore.add(ChatColor.GRAY + "SKill Tokens: " + ChatColor.GOLD + String.valueOf(totTokens_S));
                    lore.add(ChatColor.GRAY + "Passive Tokens: " + ChatColor.GOLD + String.valueOf(totTokens_P));
                    meta.setLore(lore);
                }
                else if (indices[i] == 44) {
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.GRAY + "Click for options");
                    meta.setLore(lore);
                }
                item.setItemMeta(meta);
                gui.setItem(indices[i], item);
            }

            //Put the items in the inventory
            p.openInventory(gui);
        }
        else {
            System.out.println("You need to be a player to cast this command");
        }
        return true;
    }

}
