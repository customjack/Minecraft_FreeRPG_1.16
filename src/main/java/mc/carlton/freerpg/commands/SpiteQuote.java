package mc.carlton.freerpg.commands;

import mc.carlton.freerpg.gameTools.LanguageSelector;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class SpiteQuote implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            LanguageSelector langManager = new LanguageSelector(p);
            p.sendMessage(ChatColor.ITALIC + langManager.getString("spite"));
        } else {
            String version = "v1.1.1";
            System.out.println("[COMMAND_OUT] Running FreeRPG version " + version);
        }
        return true;
    }

}
