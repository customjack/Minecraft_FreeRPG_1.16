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

import java.util.*;

public class StatReset implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        if (args.length == 2) {
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
            String[] labels_0 = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting"};
            List<String> labels_arr = Arrays.asList(labels_0);
            if (labels_arr.contains(skillName) && target.isOnline()) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (p.hasPermission("freeRPG.statReset")) {
                        ChangeStats increaseStats = new ChangeStats(target);
                        PlayerStats pStatClass = new PlayerStats(target);
                        Map<UUID,Map<String, ArrayList<Number>>> allStats = pStatClass.getData();
                        Map<String, ArrayList<Number>> pStats = allStats.get(target.getUniqueId());
                        ArrayList<Number> pSpecificStat = pStats.get(skillName);
                        for (int i=0; i < pSpecificStat.size() ;i++) {
                            pSpecificStat.set(i,0);
                        }
                        pStats.put(skillName,pSpecificStat);
                        allStats.put(target.getUniqueId(),pStats);
                        pStatClass.setData(allStats);
                        increaseStats.setTotalLevel();

                    } else {
                        p.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
                    }
                } else {
                    ChangeStats increaseStats = new ChangeStats(target);
                    PlayerStats pStatClass = new PlayerStats(target);
                    Map<UUID,Map<String, ArrayList<Number>>> allStats = pStatClass.getData();
                    Map<String, ArrayList<Number>> pStats = allStats.get(target.getUniqueId());
                    ArrayList<Number> pSpecificStat = pStats.get(skillName);
                    for (int i=0; i < pSpecificStat.size() ;i++) {
                        pSpecificStat.set(i,0);
                    }
                    pStats.put(skillName,pSpecificStat);
                    allStats.put(target.getUniqueId(),pStats);
                    pStatClass.setData(allStats);
                    increaseStats.setTotalLevel();
                }
            }
            else {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (p.hasPermission("freeRPG.statReset")) {
                        p.sendMessage(ChatColor.RED + "Improper Arguments, try /statReset [playername] [statName]");
                    } else {
                        p.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
                    }
                }
                else {
                    System.out.println("Improper Arguments, try /statReset [playername] [statName]");
                }
            }
        }
        else {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("freeRPG.setLevel")) {
                    p.sendMessage(ChatColor.RED + "Improper Arguments, try /statReset [playername] [statName]");
                } else {
                    p.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
                }
            }
            else {
                System.out.println("Improper Arguments, try /statReset [playername] [statName]");
            }
        }
        return true;
    }
}
