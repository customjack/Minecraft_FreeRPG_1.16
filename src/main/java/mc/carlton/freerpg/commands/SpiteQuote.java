package mc.carlton.freerpg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class SpiteQuote implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            p.sendMessage("Everything good is made of spite");
        } else {
            String version = "Beta 1.0";
            System.out.println("[COMMAND_OUT] Running FreeRPG version " + version);
        }
        return true;
    }

}
