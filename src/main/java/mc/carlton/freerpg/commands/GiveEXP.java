package mc.carlton.freerpg.commands;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.playerAndServerInfo.ChangeStats;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

public class GiveEXP implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        if (args.length == 3) {
            String playerName = args[0];
            Player target = plugin.getServer().getPlayer(playerName);
            if (target == null) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    p.sendMessage(ChatColor.RED+"That player is not online");
                }
                else {
                    System.out.println("Player not online");
                }
            }
            String skillName = args[1];
            int exp = Integer.valueOf(args[2]);
            String[] labels_0 = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting"};
            List<String> labels_arr = Arrays.asList(labels_0);
            if (labels_arr.contains(skillName) && target.isOnline()) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (p.hasPermission("freeRPG.giveEXP")) {
                        ChangeStats increaseStats = new ChangeStats(target);
                        increaseStats.set_isCommand(true);
                        if (exp < 0) {
                            p.sendMessage(ChatColor.RED + "Please only increase exp with this command, otherwise, use /statReset then /giveEXP");
                            return true;
                        }
                        increaseStats.changeEXP(skillName, exp);
                        increaseStats.setTotalLevel();
                    } else {
                        p.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
                    }
                } else {
                    ChangeStats increaseStats = new ChangeStats(target);
                    increaseStats.set_isCommand(true);
                    if (exp < 0) {
                        System.out.println("Please only increase exp with this command, otherwise, use /statReset then /giveEXP");
                        return true;
                    }
                    increaseStats.changeEXP(skillName, exp);
                    increaseStats.setTotalLevel();
                }
            }
            else {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (p.hasPermission("freeRPG.giveEXP")) {
                        p.sendMessage(ChatColor.RED + "Improper Arguments, try /giveEXP [playerName] [skillName] [exp]");
                    } else {
                        p.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
                    }
                }
                else {
                    System.out.println("Improper Arguments, try /giveEXP [playerName] [skillName] [exp]");
                }
            }
        }
        else {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("freeRPG.giveEXP")) {
                    p.sendMessage(ChatColor.RED + "Improper Arguments, try /giveEXP [playerName] [skillName] [exp]");
                } else {
                    p.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
                }
            }
            else {
                System.out.println("Improper Arguments, try /giveEXP [playerName] [skillName] [exp]");
            }
        }
        return true;
    }
}
