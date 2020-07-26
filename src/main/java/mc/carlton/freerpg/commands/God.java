package mc.carlton.freerpg.commands;

import mc.carlton.freerpg.FreeRPG;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class God implements CommandExecutor {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("freeRPG.god")) {
                if (p.isInvulnerable() == false) {
                    String food = plugin.getConfig().getString("Food"); //This does nothing, but shows how to important config in external files
                    p.setInvulnerable(true);
                    p.sendMessage(ChatColor.GOLD + "God mode Enabled");
                } else {
                    p.setInvulnerable(false);
                    p.sendMessage(ChatColor.DARK_RED + "God mode Disabled");
                }
            }
            else {
                p.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
            }
        }
        else {
            System.out.println("You need to be a player to cast this command");
        }
        return true;
    }

}
