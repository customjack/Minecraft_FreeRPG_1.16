package mc.carlton.freerpg.guiEvents;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.LanguageSelector;
import mc.carlton.freerpg.perksAndAbilities.*;
import mc.carlton.freerpg.playerAndServerInfo.ChangeStats;
import mc.carlton.freerpg.playerAndServerInfo.ConfigLoad;
import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

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
                    case BONE:
                        skillName = labels_0[5];
                        break;
                    case BOW:
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
                    ArrayList<Number> pStats = pStatAll.get(skillName);
                    ConfigLoad loadConfig = new ConfigLoad();
                    ArrayList<Integer> soulsInfo = loadConfig.getSoulsInfo();
                    int refundCost = (int) soulsInfo.get(1);
                    int souls = (int) pStatAll.get("global").get(20);
                    if (souls >= refundCost) {

                        //Ends all tasks that track players' buffs gained from some skills
                        if (skillName.equals("farming") && (int) pStats.get(13) > 0) {
                            Farming farmingClass = new Farming(p);
                            farmingClass.oneWithNatureEnd();
                        }
                        else if (skillName.equals("fishing") && (int) pStats.get(13) > 0) {
                            Fishing fishingClass = new Fishing(p);
                            fishingClass.fishPersonEnd();
                        }
                        else if (skillName.equals("agility") && (int) pStats.get(13) > 0) {
                            Agility agilityClass = new Agility(p);
                            agilityClass.gracefulFeetEnd();
                        }
                        else if (skillName.equals("defense") && (int) pStats.get(13) > 0) {
                            Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
                            double HP = Double.valueOf(plugin.getConfig().getString("multipliers.globalMultiplier"));
                            ((Attributable) p).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(HP);
                        }

                        //Find Skill Tokens, Refund skill tokens, set skills to 0
                        int totalSkillTokens = ((int) pStats.get(3) + (int) pStats.get(7) + (int) pStats.get(8) + (int) pStats.get(9) + (int) pStats.get(10) + (int) pStats.get(11) + (int) pStats.get(12) + (int) pStats.get(13));
                        refundStat.setStat(skillName, 3, totalSkillTokens);
                        refundStat.setStat(skillName, 7, 0);
                        refundStat.setStat(skillName, 8, 0);
                        refundStat.setStat(skillName, 9, 0);
                        refundStat.setStat(skillName, 10, 0);
                        refundStat.setStat(skillName, 11, 0);
                        refundStat.setStat(skillName, 12, 0);
                        refundStat.setStat(skillName, 13, 0);

                        //Find Passive Tokens, Refund passive tokens, set passive skills to 0
                        if (mainSkills.contains(skillName)) {
                            int level = (int) pStats.get(0);
                            refundStat.setStat(skillName, 2, level);
                            refundStat.setStat(skillName, 4, level);
                            refundStat.setStat(skillName, 5, level);
                            refundStat.setStat(skillName, 6, level);
                        }

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

