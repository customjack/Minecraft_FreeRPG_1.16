package mc.carlton.freerpg.guiCommands;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Map;

public class ConfigurationGUI implements CommandExecutor {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length != 0) {
                p.sendMessage(ChatColor.RED + "Error: Try /configurationGUI");
            }
            else {
                Inventory gui = Bukkit.createInventory(p, 54, "Configuration Window");
                PlayerStats pStatClass = new PlayerStats(p);
                Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();

                //Back button
                ItemStack back = new ItemStack(Material.ARROW);
                ItemMeta backMeta = back.getItemMeta();
                backMeta.setDisplayName(ChatColor.BOLD + "Back");
                ArrayList<String> lore = new ArrayList<String>();
                lore.add(ChatColor.ITALIC+ChatColor.GRAY.toString()+"Takes you back to the skills menu");
                backMeta.setLore(lore);
                back.setItemMeta(backMeta);
                gui.setItem(45,back);

                //Level up Notifications
                ItemStack levelUp = new ItemStack(Material.EXPERIENCE_BOTTLE);
                ItemMeta levelUpMeta = levelUp.getItemMeta();
                levelUpMeta.setDisplayName(ChatColor.WHITE + ChatColor.BOLD.toString() + "Level Up Notifications");
                levelUp.setItemMeta(levelUpMeta);
                gui.setItem(12,levelUp);

                ItemStack levelUpToggle = new ItemStack(Material.LIME_DYE);
                ItemMeta levelUpToggleMeta = levelUpToggle.getItemMeta();
                if ( (int) pStat.get("global").get(21) > 0) {
                    levelUpToggleMeta.setDisplayName(ChatColor.BOLD + ChatColor.GREEN.toString() + "ON");
                }
                else {
                    levelUpToggle.setType(Material.GRAY_DYE);
                    levelUpToggleMeta.setDisplayName(ChatColor.BOLD + ChatColor.RED.toString() + "OFF");
                }
                levelUpToggle.setItemMeta(levelUpToggleMeta);
                gui.setItem(21,levelUpToggle);

                //Ability Notifications
                ItemStack abilityNotifications = new ItemStack(Material.STICK);
                ItemMeta abilityNotificationsMeta = abilityNotifications.getItemMeta();
                abilityNotificationsMeta.setDisplayName(ChatColor.WHITE + ChatColor.BOLD.toString() + "Ability Preparation Notifications");
                abilityNotifications.setItemMeta(abilityNotificationsMeta);
                gui.setItem(14,abilityNotifications);

                ItemStack abilityNotificationsToggle = new ItemStack(Material.LIME_DYE);
                ItemMeta abilityNotificationsToggleMeta = abilityNotificationsToggle.getItemMeta();
                if ( (int) pStat.get("global").get(22) > 0) {
                    abilityNotificationsToggleMeta.setDisplayName(ChatColor.BOLD + ChatColor.GREEN.toString() + "ON");
                }
                else {
                    abilityNotificationsToggle.setType(Material.GRAY_DYE);
                    abilityNotificationsToggleMeta.setDisplayName(ChatColor.BOLD + ChatColor.RED.toString() + "OFF");
                }
                abilityNotificationsToggle.setItemMeta(abilityNotificationsToggleMeta);
                gui.setItem(23,abilityNotificationsToggle);



                //Put the items in the inventory
                p.openInventory(gui);
            }
        }
        else {
            System.out.println("You need to be a player to cast this command");
        }
        return true;
    }
}

