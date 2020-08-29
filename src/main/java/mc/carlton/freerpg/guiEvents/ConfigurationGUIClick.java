package mc.carlton.freerpg.guiEvents;

import mc.carlton.freerpg.gameTools.LanguageSelector;
import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class ConfigurationGUIClick implements Listener {
    @EventHandler
    public void clickEvent(InventoryClickEvent e) {
        boolean proceed = true;
        try {
            InventoryType invType = e.getClickedInventory().getType();
        } catch (Exception except) {
            proceed = false;
            return;
        }
        if (e.getView().getTitle().equalsIgnoreCase("Configuration Window")) {
            if (e.getClickedInventory().getType() == InventoryType.PLAYER) {
                e.setCancelled(true);
                return;
            }
            if (e.getClick() != ClickType.LEFT) {
                e.setCancelled(true);
            }
            if (e.getCursor().getType() != Material.AIR) {
                e.setCancelled(true);
            }
            Player p = (Player) e.getWhoClicked();
            LanguageSelector lang = new LanguageSelector(p);
            Inventory inv = e.getClickedInventory();
            PlayerStats pStatClass = new PlayerStats(p);
            Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
            Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
            if (e.getCurrentItem() != null) {
                PlayerStats languageChange = new PlayerStats(p);
                switch (e.getRawSlot()) {
                    case 20:
                        if ((int)pStat.get("global").get(21) > 0) {
                            pStat.get("global").set(21, 0);
                        }
                        else {
                            pStat.get("global").set(21, 1);
                        }
                        statAll.put(p.getUniqueId(), pStat);
                        pStatClass.setData(statAll);
                        p.performCommand("frpg configurationGUI");
                        break;
                    case 21:
                        if ((int)pStat.get("global").get(22) > 0) {
                            pStat.get("global").set(22, 0);
                        }
                        else {
                            pStat.get("global").set(22, 1);
                        }
                        statAll.put(p.getUniqueId(), pStat);
                        pStatClass.setData(statAll);
                        p.performCommand("frpg configurationGUI");
                        break;
                    case 22:
                        if ((int)pStat.get("global").get(24) > 0) {
                            pStat.get("global").set(24, 0);
                        }
                        else {
                            pStat.get("global").set(24, 1);
                        }
                        statAll.put(p.getUniqueId(), pStat);
                        pStatClass.setData(statAll);
                        p.performCommand("frpg configurationGUI");
                        break;
                    case 23:
                        if ((int)pStat.get("global").get(25) > 0) {
                            pStat.get("global").set(25, 0);
                        }
                        else {
                            pStat.get("global").set(25, 1);
                        }
                        statAll.put(p.getUniqueId(), pStat);
                        pStatClass.setData(statAll);
                        p.performCommand("frpg configurationGUI");
                        break;
                    case 30:
                        p.sendMessage(ChatColor.GOLD + lang.getString("translators") + ChatColor.GRAY + ": " +
                                ChatColor.WHITE + "vERKE");
                        break;
                    case 31:
                        p.sendMessage(ChatColor.GOLD + lang.getString("translators") + ChatColor.GRAY + ": " +
                                ChatColor.WHITE + "Temuel");
                        break;
                    case 32:
                        p.sendMessage(ChatColor.GOLD + lang.getString("translators") + ChatColor.GRAY + ": " +
                                ChatColor.WHITE + "FruitLab.gg");
                        break;
                    case 33:
                        p.sendMessage(ChatColor.GOLD + lang.getString("translators") + ChatColor.GRAY + ": " +
                                ChatColor.WHITE + "QuarVey");
                        break;
                    case 38:
                        languageChange.setPlayerLanguage("enUs");
                        p.performCommand("frpg configurationGUI");
                        break;
                    case 39:
                        languageChange.setPlayerLanguage("huHU");
                        p.performCommand("frpg configurationGUI");
                        break;
                    case 40:
                        languageChange.setPlayerLanguage("frFR");
                        p.performCommand("frpg configurationGUI");
                        break;
                    case 41:
                        languageChange.setPlayerLanguage("deDE");
                        p.performCommand("frpg configurationGUI");
                        break;
                    case 42:
                        languageChange.setPlayerLanguage("plPL");
                        p.performCommand("frpg configurationGUI");
                        break;
                    case 45:
                        p.performCommand("frpg skills");
                        break;
                    default:
                        break;
                }
            }

            e.setCancelled(true); //So they cant take the items
        }
    }
}

