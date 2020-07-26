package mc.carlton.freerpg.commands;

import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class HotRodToggle implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            PlayerStats pStatClass = new PlayerStats(p);
            Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
            Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
            int hotRodLevel = (int) pStat.get("fishing").get(12);
            if (hotRodLevel > 0) {
                if (args.length == 0) {
                    int hotRodToggle = (int) pStat.get("global").get(17);
                    if (hotRodToggle > 0) {
                        p.sendMessage(ChatColor.RED + "Hot Rod status change to: OFF");
                        pStat.get("global").set(17,0);
                        statAll.put(p.getUniqueId(), pStat);
                        pStatClass.setData(statAll);
                    }
                    else {
                        p.sendMessage(ChatColor.GREEN + "Hot Rod status change to: ON");
                        pStat.get("global").set(17,1);
                        statAll.put(p.getUniqueId(), pStat);
                        pStatClass.setData(statAll);
                    }
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("off")) {
                        p.sendMessage(ChatColor.RED + "Hot Rod status change to: OFF");
                        pStat.get("global").set(17,0);
                        statAll.put(p.getUniqueId(), pStat);
                        pStatClass.setData(statAll);
                    }
                    else if (args[0].equalsIgnoreCase("on")) {
                        p.sendMessage(ChatColor.GREEN + "Hot Rod status change to: ON");
                        pStat.get("global").set(17,1);
                        statAll.put(p.getUniqueId(), pStat);
                        pStatClass.setData(statAll);
                    }
                    else {
                        p.sendMessage(ChatColor.RED + "Invalid use, try /hotRodToggle ON or /hotRodToggle OFF");
                    }
                }
                else {
                    p.sendMessage(ChatColor.RED + "Invalid use, try /hotRodToggle ON or /hotRodToggle OFF");
                }
            }
            else {
                p.sendMessage(ChatColor.RED + "You need to unlock " +ChatColor.BOLD + "Hot Rod" + ChatColor.RESET + ChatColor.RED.toString() + " to use this command");
            }
        } else {
            System.out.println("You need to be a player to cast this command");
        }
        return true;
    }
}
