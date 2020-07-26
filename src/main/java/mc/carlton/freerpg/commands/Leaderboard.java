package mc.carlton.freerpg.commands;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.playerAndServerInfo.HeldStats;
import mc.carlton.freerpg.playerAndServerInfo.PlayerLeaderboard;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class Leaderboard implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        if (args.length <= 2) {
            String skillName = args[0];
            int page = 1;
            if (args.length ==2) {
                page = Integer.valueOf(args[1]);
            }
            String[] titles_0 = {"Digging","Woodcutting","Mining","Farming","Fishing","Archery","Beast Mastery","Swordsmanship","Defense","Axe Mastery","Repair","Agility","Alchemy","Smelting","Enchanting","Global"};
            String[] labels_0 = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting"};
            List<String> labels_arr = Arrays.asList(labels_0);
            if (labels_arr.contains(skillName)) {
                String skillTitle = titles_0[labels_arr.indexOf(skillName)];
                PlayerLeaderboard getStats = new PlayerLeaderboard();
                ArrayList<HeldStats> sortedStats = getStats.getLeaders(skillName);
                int totalPlayers = sortedStats.size();
                int totalPages = (int) Math.ceil(totalPlayers/10.0);
                if (page > totalPages) {
                    page = 1;
                }
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (p.hasPermission("freeRPG.leaderboard")) {
                        p.sendMessage(ChatColor.RED + "------| " + ChatColor.GREEN + ChatColor.BOLD.toString() + skillTitle+ " Leaderboard" +
                                      ChatColor.RESET + ChatColor.GREEN.toString() + " Page ["+ Integer.toString(page) + "/" + Integer.toString(totalPages) + "]" +
                                      ChatColor.RED.toString() + " |-----");
                        for (int i = 10*(page-1); i < (int) Math.min(10*page,totalPlayers); i++) {
                            HeldStats info = sortedStats.get(i);
                            p.sendMessage(ChatColor.GREEN + Integer.toString(i+1) +". " + ChatColor.YELLOW + info.get_pName() + " (" + ChatColor.BLUE + Integer.toString(info.get_level()) + ChatColor.YELLOW + ")");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
                    }
                } else {
                    System.out.println( "------| " + skillTitle+ " Leaderboard" + " Page ["+ Integer.toString(page) + "/" + Integer.toString(totalPages) + "]" + " |-----");
                    for (int i = 10*(page-1); i < (int) Math.min(10*page,totalPlayers); i++) {
                        HeldStats info = sortedStats.get(i);
                        System.out.println(Integer.toString(i+1) +". " +  info.get_pName() + " (" + Integer.toString(info.get_level()) +  ")");
                    }
                }
            }
            else {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (p.hasPermission("freeRPG.leaderboard")) {
                        p.sendMessage(ChatColor.RED + "Improper Arguments, try /statLeaders [skillName] [(Optional) page]");
                    } else {
                        p.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
                    }
                }
                else {
                    System.out.println("Improper Arguments, try /statLeaders [skillName] [(Optional) page]");
                }
            }
        }
        else {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("freeRPG.setLevel")) {
                    p.sendMessage(ChatColor.RED + "Improper Arguments, try /statLeaders [skillName] [(Optional) page]");
                } else {
                    p.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
                }
            }
            else {
                System.out.println("Improper Arguments, try /statLeaders [skillName] [(Optional) page]");
            }
        }
        return true;
    }
}
