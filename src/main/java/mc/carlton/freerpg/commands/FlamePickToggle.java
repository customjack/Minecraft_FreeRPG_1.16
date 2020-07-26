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

public class FlamePickToggle implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            PlayerStats pStatClass = new PlayerStats(p);
            Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
            Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
            int flamePickLevel = (int) pStat.get("smelting").get(13);
            if (flamePickLevel > 0) {
                if (args.length == 0) {
                    int flamePickToggle = (int) pStat.get("global").get(13);
                    if (flamePickToggle > 0) {
                        p.sendMessage(ChatColor.RED + "Flame Pickaxe status change to: OFF");
                        pStat.get("global").set(13,0);
                        statAll.put(p.getUniqueId(), pStat);
                        pStatClass.setData(statAll);
                    }
                    else {
                        p.sendMessage(ChatColor.GREEN + "Flame Pickaxe status change to: ON");
                        pStat.get("global").set(13,1);
                        statAll.put(p.getUniqueId(), pStat);
                        pStatClass.setData(statAll);
                    }
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("off")) {
                        p.sendMessage(ChatColor.RED + "Flame Pickaxe status change to: OFF");
                        pStat.get("global").set(13,0);
                        statAll.put(p.getUniqueId(), pStat);
                        pStatClass.setData(statAll);
                    }
                    else if (args[0].equalsIgnoreCase("on")) {
                        p.sendMessage(ChatColor.GREEN + "Flame Pickaxe status change to: ON");
                        pStat.get("global").set(13,1);
                        statAll.put(p.getUniqueId(), pStat);
                        pStatClass.setData(statAll);
                    }
                    else {
                        p.sendMessage(ChatColor.RED + "Invalid use, try /flamePickToggle ON or /flamePickToggle OFF");
                    }
                }
                else {
                    p.sendMessage(ChatColor.RED + "Invalid use, try /flamePickToggle ON or /flamePickToggle OFF");
                }
            }
            else {
                p.sendMessage(ChatColor.RED + "You need to unlock " +ChatColor.BOLD + "Flame Pickaxe" + ChatColor.RESET + ChatColor.RED.toString() + " to use this command");
            }
        } else {
            System.out.println("You need to be a player to cast this command");
        }
        return true;
    }
}
