package mc.carlton.freerpg.guiEvents;

import mc.carlton.freerpg.gameTools.LanguageSelector;
import mc.carlton.freerpg.perksAndAbilities.*;
import mc.carlton.freerpg.playerInfo.ChangeStats;
import mc.carlton.freerpg.configStorage.ConfigLoad;
import mc.carlton.freerpg.playerInfo.PlayerStats;
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

public class ConfirmationGUIClick implements Listener {
    @EventHandler
    public void clickEvent(InventoryClickEvent e) {
        boolean proceed = true;
        try {
            InventoryType invType = e.getClickedInventory().getType();
        } catch (Exception except) {
            proceed = false;
            return;
        }
        if (e.getView().getTitle().equalsIgnoreCase("Confirmation Window")) {
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
            Inventory inv = e.getClickedInventory();
            if (e.getCurrentItem() != null) {
                String skillName = "";
                String[] labels_0 = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting","global"};
                String[] mainSkills_0 = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery"};
                List<String> mainSkills = Arrays.asList(mainSkills_0);
                Material indicator = inv.getItem(4).getType();
                switch (indicator) {
                    case IRON_SHOVEL:
                        skillName = labels_0[0];
                        break;
                    case IRON_AXE:
                        skillName = labels_0[1];
                        break;
                    case IRON_PICKAXE:
                        skillName = labels_0[2];
                        break;
                    case IRON_HOE:
                        skillName = labels_0[3];
                        break;
                    case FISHING_ROD:
                        skillName = labels_0[4];
                        break;
                    case BOW:
                        skillName = labels_0[5];
                        break;
                    case BONE:
                        skillName = labels_0[6];
                        break;
                    case IRON_SWORD:
                        skillName = labels_0[7];
                        break;
                    case IRON_CHESTPLATE:
                        skillName = labels_0[8];
                        break;
                    case GOLDEN_AXE:
                        skillName = labels_0[9];
                        break;
                    case ANVIL:
                        skillName = labels_0[10];
                        break;
                    case LEATHER_LEGGINGS:
                        skillName = labels_0[11];
                        break;
                    case POTION:
                        skillName = labels_0[12];
                        break;
                    case COAL:
                        skillName = labels_0[13];
                        break;
                    case ENCHANTING_TABLE:
                        skillName = labels_0[14];
                        break;
                    default:
                        break;

                }
                if (e.getCurrentItem().getType() == Material.LIME_TERRACOTTA) {
                    UUID uuid = p.getUniqueId();
                    ChangeStats refundStat = new ChangeStats(p);
                    PlayerStats pStatClass = new PlayerStats(p);
                    Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
                    Map<String, ArrayList<Number>> pStatAll = statAll.get(uuid);
                    ConfigLoad loadConfig = new ConfigLoad();
                    ArrayList<Integer> soulsInfo = loadConfig.getSoulsInfo();
                    int refundCost = (int) soulsInfo.get(1);
                    int souls = (int) pStatAll.get("global").get(20);
                    if (souls >= refundCost) {

                        //Refund the skill tree
                        refundStat.refundSkillTree(skillName);

                        //Remove the souls
                        Global globalClass = new Global(p);
                        globalClass.loseSouls(refundCost);
                    }
                    else {
                        LanguageSelector langManager = new LanguageSelector(p);
                        String refundCostString = Integer.toString(refundCost);
                        p.sendMessage(ChatColor.RED + refundCostString + " " + langManager.getString("requiredSouls"));
                    }

                    p.closeInventory();
                    p.performCommand("frpg skillTreeGUI " + skillName);
                }
                else if (e.getCurrentItem().getType() == Material.RED_TERRACOTTA) {
                    p.closeInventory();
                    p.performCommand("frpg skillTreeGUI " + skillName);
                }
            }

            e.setCancelled(true); //So they cant take the items
        }
    }
}

