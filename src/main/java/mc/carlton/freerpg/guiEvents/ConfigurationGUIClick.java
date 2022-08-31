package mc.carlton.freerpg.guiEvents;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import mc.carlton.freerpg.gameTools.LanguageSelector;
import mc.carlton.freerpg.globalVariables.StringsAndOtherData;
import mc.carlton.freerpg.playerInfo.PlayerStats;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

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
      PlayerStats pStatClass = new PlayerStats(p);
      StringsAndOtherData stringsAndOtherData = new StringsAndOtherData();
      Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
      Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
      Map<Integer, String> bookIndexes = stringsAndOtherData.getBookIndexes();
      Map<Integer, String> dyeIndexes = stringsAndOtherData.getDyeIndexes();
      if (e.getCurrentItem() != null) {
        PlayerStats languageChange = new PlayerStats(p);
        for (int index : bookIndexes.keySet()) {
          if (e.getRawSlot() == index) {
            String languageCode = bookIndexes.get(index);
            switch (languageCode) {
              case "enUs":
                break;
              default:
                p.sendMessage(
                    ChatColor.GOLD + lang.getString("translators") + ChatColor.GRAY + ": " +
                        ChatColor.WHITE + lang.getString("translationCredit", languageCode));
                break;
            }
            e.setCancelled(true);
            return;
          }
        }
        for (int index : dyeIndexes.keySet()) {
          if (e.getRawSlot() == index) {
            String languageCode = dyeIndexes.get(index);
            languageChange.setPlayerLanguage(languageCode);
            p.performCommand("frpg configurationGUI");
            e.setCancelled(true);
            return;
          }
        }
        switch (e.getRawSlot()) {
          case 10:
            if ((int) pStat.get("global").get(21) > 0) {
              pStat.get("global").set(21, 0);
            } else {
              pStat.get("global").set(21, 1);
            }
            statAll.put(p.getUniqueId(), pStat);
            pStatClass.setData(statAll);
            p.performCommand("frpg configurationGUI");
            break;
          case 11:
            if ((int) pStat.get("global").get(22) > 0) {
              pStat.get("global").set(22, 0);
            } else {
              pStat.get("global").set(22, 1);
            }
            statAll.put(p.getUniqueId(), pStat);
            pStatClass.setData(statAll);
            p.performCommand("frpg configurationGUI");
            break;
          case 12:
            if ((int) pStat.get("global").get(24) > 0) {
              pStat.get("global").set(24, 0);

            } else {
              pStat.get("global").set(24, 1);
            }
            statAll.put(p.getUniqueId(), pStat);
            pStatClass.setData(statAll);
            p.performCommand("frpg configurationGUI");
            break;
          case 13:
            if ((int) pStat.get("global").get(25) > 0) {
              pStat.get("global").set(25, 0);
            } else {
              pStat.get("global").set(25, 1);
            }
            statAll.put(p.getUniqueId(), pStat);
            pStatClass.setData(statAll);
            p.performCommand("frpg configurationGUI");
            break;
          case 14:
            int numberOfBars = (int) pStat.get("global").get(28);
            if (numberOfBars < 3) {
              pStat.get("global").set(28, Math.max(numberOfBars + 1, 0));
            } else {
              pStat.get("global").set(28, 0);
            }
            statAll.put(p.getUniqueId(), pStat);
            pStatClass.setData(statAll);
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

