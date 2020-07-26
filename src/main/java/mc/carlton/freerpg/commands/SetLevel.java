package mc.carlton.freerpg.commands;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.playerAndServerInfo.ChangeStats;
import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SetLevel implements CommandExecutor {
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
            int level = Integer.valueOf(args[2]);
            String[] labels_0 = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting"};
            List<String> labels_arr = Arrays.asList(labels_0);
            if (labels_arr.contains(skillName) && target.isOnline()) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (p.hasPermission("freeRPG.setLevel")) {
                        ChangeStats increaseStats = new ChangeStats(target);
                        increaseStats.set_isCommand(true);
                        PlayerStats pStatClass = new PlayerStats(target);
                        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
                        int exp = increaseStats.getEXPfromLevel(level);
                        int currentExp = (int) pStat.get(skillName).get(1);
                        if (exp <= currentExp) {
                            p.sendMessage(ChatColor.RED + "Please only increase levels with this command, otherwise, use /statReset then /setStatLevel");
                            return true;
                        }
                        increaseStats.changeEXP(skillName,exp-currentExp+1);
                        increaseStats.setTotalLevel();
                    } else {
                        p.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
                    }
                } else {
                    ChangeStats increaseStats = new ChangeStats(target);
                    increaseStats.set_isCommand(true);
                    PlayerStats pStatClass = new PlayerStats(target);
                    Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
                    int exp = increaseStats.getEXPfromLevel(level);
                    int currentExp = (int) pStat.get(skillName).get(1);
                    if (exp <= currentExp) {
                        System.out.println("Please only increase levels with this command, otherwise, use /statReset then /setStatLevel");
                        return true;
                    }
                    increaseStats.changeEXP(skillName,exp-currentExp+1);
                    increaseStats.setTotalLevel();
                }
            }
            else {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (p.hasPermission("freeRPG.setLevel")) {
                        p.sendMessage(ChatColor.RED + "Improper Arguments, try /setStatLevel [playerName] [skillName] [level]");
                    } else {
                        p.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
                    }
                }
                else {
                    System.out.println("Improper Arguments, try /setStatLevel [playerName] [skillName] [level]");
                }
            }
        }
        else {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("freeRPG.setLevel")) {
                    p.sendMessage(ChatColor.RED + "Improper Arguments, try /setStatLevel [playerName] [skillName] [level]");
                } else {
                    p.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
                }
            }
            else {
                System.out.println("Improper Arguments, try /setStatLevel [playerName] [skillName] [level]");
            }
        }
        return true;
    }
}
