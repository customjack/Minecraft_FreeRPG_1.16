package mc.carlton.freerpg.commands;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.PsuedoEnchanting;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class EnchantItem implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("freeRPG.enchantItem")) {
                if (args.length == 1) {
                    int level = Integer.valueOf(args[0]);
                    if (level < 40) {
                        ItemStack item = p.getInventory().getItemInMainHand();
                        PsuedoEnchanting enchant = new PsuedoEnchanting();
                        item = enchant.enchantItem(item, level,false);
                        p.getInventory().setItemInMainHand(item);
                    } else {
                        p.sendMessage(ChatColor.RED + "Level argument must be less than 40");
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "Improper Arguments, try /enchantItem [level]");
                }
            }
            else {
                p.sendMessage(ChatColor.RED + "You do not have permission to cast this command");
            }
        } else {
            System.out.println("You need to be a player to cast this command");
        }
        return true;
    }
}
