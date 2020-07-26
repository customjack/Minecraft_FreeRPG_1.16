package mc.carlton.freerpg.guiCommands;

import mc.carlton.freerpg.FreeRPG;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfirmationGUI implements CommandExecutor {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            String[] labels_0 = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting","global"};
            String[] titles_0 = {"Digging","Woodcutting","Mining","Farming","Fishing","Archery","Beast Mastery","Swordsmanship","Defense","Axe Mastery","Repair","Agility","Alchemy","Smelting","Enchanting","Global"};
            List<String> labels_arr = Arrays.asList(labels_0);
            if (args.length != 1) {
                p.sendMessage(ChatColor.RED + "Error: Try /confirmationGUI [skillName]");
            }
            else if (labels_arr.indexOf(args[0]) == -1) {
                p.sendMessage(ChatColor.RED+"Error: Try /confirmationGUI [skillName]");
            }
            else {
                String skillName = labels_0[labels_arr.indexOf(args[0])];
                String skillTitle = titles_0[labels_arr.indexOf(args[0])];
                Inventory gui = Bukkit.createInventory(p, 54, "Confirmation Window");
                //Information
                ItemStack info = new ItemStack(Material.PAPER);
                ItemMeta infoMeta = info.getItemMeta();
                infoMeta.setDisplayName(ChatColor.BOLD + ChatColor.YELLOW.toString() + "WARNING");
                ArrayList<String> lore = new ArrayList<String>();
                lore.add(ChatColor.ITALIC+ChatColor.GRAY.toString()+"Refunding a skill tree costs 1000 souls and");
                lore.add(ChatColor.ITALIC+ChatColor.GRAY.toString()+"is not reversible, are you sure you want to");
                lore.add(ChatColor.ITALIC+ChatColor.GRAY.toString()+"refund the " + ChatColor.BOLD.toString() + ChatColor.WHITE.toString() +
                         skillTitle + ChatColor.RESET.toString() + ChatColor.ITALIC.toString() + ChatColor.GRAY.toString()+" skill?");
                infoMeta.setLore(lore);
                info.setItemMeta(infoMeta);
                gui.setItem(22,info);

                //Yes Button
                ItemStack yes = new ItemStack(Material.LIME_TERRACOTTA);
                ItemMeta yesMeta = yes.getItemMeta();
                yesMeta.setDisplayName(ChatColor.BOLD + ChatColor.GREEN.toString() + "YES");
                yes.setItemMeta(yesMeta);
                gui.setItem(39,yes);

                //No Button
                ItemStack no = new ItemStack(Material.RED_TERRACOTTA);
                ItemMeta noMeta = no.getItemMeta();
                noMeta.setDisplayName(ChatColor.BOLD + ChatColor.RED.toString() + "NO");
                no.setItemMeta(noMeta);
                gui.setItem(41,no);

                //Skill Item (Indicator)
                ItemStack skill = new ItemStack(Material.IRON_SHOVEL);
                ItemMeta skillMeta = skill.getItemMeta();
                switch (skillName) {
                    case "digging":
                        skill.setType(Material.IRON_SHOVEL);
                        break;
                    case "woodcutting":
                        skill.setType(Material.IRON_AXE);
                        break;
                    case "mining":
                        skill.setType(Material.IRON_PICKAXE);
                        break;
                    case "farming":
                        skill.setType(Material.IRON_HOE);
                        break;
                    case "fishing":
                        skill.setType(Material.FISHING_ROD);
                        break;
                    case "archery":
                        skill.setType(Material.BOW);
                        break;
                    case "beastMastery":
                        skill.setType(Material.BONE);
                        break;
                    case "swordsmanship":
                        skill.setType(Material.IRON_SWORD);
                        break;
                    case "defense":
                        skill.setType(Material.IRON_CHESTPLATE);
                        break;
                    case "axeMastery":
                        skill.setType(Material.GOLDEN_AXE);
                        break;
                    case "repair":
                        skill.setType(Material.ANVIL);
                        break;
                    case "agility":
                        skill.setType(Material.LEATHER_LEGGINGS);
                        break;
                    case "alchemy":
                        skill.setType(Material.POTION);
                        break;
                    case "smelting":
                        skill.setType(Material.COAL);
                        break;
                    case "enchanting":
                        skill.setType(Material.ENCHANTING_TABLE);
                        break;
                    default:
                        break;
                }
                skillMeta.setDisplayName(ChatColor.BOLD + skillTitle);
                skill.setItemMeta(skillMeta);
                gui.setItem(4,skill);

                //Put the items in the inventory
                p.openInventory(gui);
            }
        }
        else {
            System.out.println("You need to be a player to cast this command");
        }
        return true;
    }
}
