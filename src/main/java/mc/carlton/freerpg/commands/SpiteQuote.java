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
            String version = "Beta 1.0";
            p.sendMessage("Everything good is made of spite");
            System.out.println("[COMMAND_OUT] Running FreeRPG version " + version);
        } else {
            System.out.println("You need to be a player to cast this command");
        }
        return true;
    }

}
