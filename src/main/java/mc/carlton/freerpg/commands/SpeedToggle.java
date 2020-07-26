package mc.carlton.freerpg.commands;

import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class SpeedToggle implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            PlayerStats pStatClass = new PlayerStats(p);
            Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
            Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
            int gracefulFeetLevel = (int) pStat.get("agility").get(13);
            if (gracefulFeetLevel > 0) {
                if (args.length == 0) {
                    int gracefulFeetToggle = (int) pStat.get("global").get(14);
                    if (gracefulFeetToggle > 0) {
                        p.sendMessage(ChatColor.RED + "Graceful Feet status change to: OFF");
                        pStat.get("global").set(14,0);
                        statAll.put(p.getUniqueId(), pStat);
                        pStatClass.setData(statAll);
                        if (p.getPotionEffect(PotionEffectType.SPEED) != null) {
                            if (p.getPotionEffect(PotionEffectType.SPEED).getAmplifier() < 1) {
                                p.removePotionEffect(PotionEffectType.SPEED);
                            }
                        }
                    }
                    else {
                        p.sendMessage(ChatColor.GREEN + "Graceful Feet status change to: ON");
                        pStat.get("global").set(14,1);
                        statAll.put(p.getUniqueId(), pStat);
                        pStatClass.setData(statAll);
                    }
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("off")) {
                        p.sendMessage(ChatColor.RED + "Graceful Feet status change to: OFF");
                        pStat.get("global").set(14,0);
                        statAll.put(p.getUniqueId(), pStat);
                        pStatClass.setData(statAll);
                        if (p.getPotionEffect(PotionEffectType.SPEED) != null) {
                            if (p.getPotionEffect(PotionEffectType.SPEED).getAmplifier() < 1) {
                                p.removePotionEffect(PotionEffectType.SPEED);
                            }
                        }
                    }
                    else if (args[0].equalsIgnoreCase("on")) {
                        p.sendMessage(ChatColor.GREEN + "Graceful Feet status change to: ON");
                        pStat.get("global").set(14,1);
                        statAll.put(p.getUniqueId(), pStat);
                        pStatClass.setData(statAll);
                    }
                    else {
                        p.sendMessage(ChatColor.RED + "Invalid use, try /speedToggle ON or /speedToggle OFF");
                    }
                }
                else {
                    p.sendMessage(ChatColor.RED + "Invalid use, try /speedToggle ON or /speedToggle OFF");
                }
            }
            else {
                p.sendMessage(ChatColor.RED + "You need to unlock " +ChatColor.BOLD + "Graceful Feet" + ChatColor.RESET + ChatColor.RED.toString() + " to use this command");
            }
        } else {
            System.out.println("You need to be a player to cast this command");
        }
        return true;
    }
}
