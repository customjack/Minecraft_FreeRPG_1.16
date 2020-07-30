package mc.carlton.freerpg.guiEvents;
import mc.carlton.freerpg.gameTools.LanguageSelector;
import mc.carlton.freerpg.perksAndAbilities.*;
import mc.carlton.freerpg.playerAndServerInfo.ConfigLoad;
import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class SkillsGUIclick implements Listener {

    @EventHandler
    public void clickEvent(InventoryClickEvent e) {
        boolean proceed = true;
        try {
            InventoryType invType = e.getClickedInventory().getType();
        } catch (Exception except) {
            proceed = false;
            return;
        }

        String[] titles_0 = {"Digging", "Woodcutting", "Mining", "Farming", "Fishing", "Archery", "Beast Mastery", "Swordsmanship", "Defense", "Axe Mastery", "Repair", "Agility", "Alchemy", "Smelting", "Enchanting"};
        String[] labels = {"digging", "woodcutting", "mining", "farming", "fishing", "archery", "beastMastery", "swordsmanship", "defense", "axeMastery", "repair", "agility", "alchemy", "smelting", "enchanting"};
        List<String> titles = Arrays.asList(titles_0);
        String title = e.getView().getTitle();
        int titleIndex = titles.indexOf(title);
        //Check to see if its the GUI menu
        if (proceed) {
            if (titleIndex != -1) {
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
                LanguageSelector langManager = new LanguageSelector(p);
                UUID uuid = p.getUniqueId();
                String skillName = labels[titleIndex];
                PlayerStats pStatClass = new PlayerStats(p);
                Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
                Map<String, ArrayList<Number>> pStatAll = statAll.get(uuid);
                ArrayList<Number> pStats = pStatAll.get(skillName);
                MaxPassiveLevels passiveMax = new MaxPassiveLevels();

                //Determine what they selected and what to do
                if (e.getCurrentItem() != null) {
                    switch (e.getCurrentItem().getType()) {
                        case ARROW:
                            p.closeInventory();
                            p.performCommand("frpg skills");
                            break;
                        case COMPOSTER:
                            if ((int) pStatAll.get("global").get(20) >= 250 && (int) pStatAll.get("global").get(9) > 0) {
                                p.closeInventory();
                                p.performCommand("frpg confirmationGUI " + skillName);
                            }
                            else {
                                if ((int) pStatAll.get("global").get(9) > 0) {
                                    ConfigLoad loadConfig = new ConfigLoad();
                                    ArrayList<Integer> soulsInfo = loadConfig.getSoulsInfo();
                                    String refundCost = Integer.toString(soulsInfo.get(1));
                                    p.sendMessage(ChatColor.RED + refundCost + " " + langManager.getString("refundSkill"));
                                }
                                else {
                                    p.sendMessage(ChatColor.RED + langManager.getString("needToUnlock") + " " + ChatColor.BOLD + langManager.getString("globalPerkTitle7") + ChatColor.RESET + ChatColor.RED.toString() + " " + langManager.getString("refundSkill2"));
                                }
                            }
                            break;
                        case RED_TERRACOTTA:
                            if (e.getSlot() == 13 || e.getSlot() == 31 || e.getSlot() == 7 || e.getSlot() == 43 || e.getSlot() == 20 || e.getSlot() == 23) {
                                p.sendMessage(ChatColor.RED + langManager.getString("perkRequirement"));
                            } else if (e.getSlot() == 26) {
                                p.sendMessage(ChatColor.RED + langManager.getString("perkRequirementM"));
                            }
                            break;
                        case PINK_TERRACOTTA:
                        case YELLOW_TERRACOTTA:
                            if (pStats.get(3).intValue() > 0) {
                                pStats.set(3, pStats.get(3).intValue() - 1);
                                switch (e.getSlot()) {
                                    case 20:
                                    case 11:
                                        pStats.set(7, pStats.get(7).intValue() + 1);
                                        break;
                                    case 29:
                                        pStats.set(8, pStats.get(8).intValue() + 1);
                                        break;
                                    case 23:
                                    case 13:
                                        pStats.set(9, pStats.get(9).intValue() + 1);
                                        break;
                                    case 31:
                                        pStats.set(10, pStats.get(10).intValue() + 1);
                                        break;
                                    case 7:
                                        pStats.set(11, pStats.get(11).intValue() + 1);
                                        break;
                                    case 43:
                                        pStats.set(12, pStats.get(12).intValue() + 1);
                                        break;
                                    case 26:
                                        pStats.set(13, pStats.get(13).intValue() + 1);
                                        if (skillName.equals("farming")) {
                                            pStatAll.put(skillName, pStats);
                                            statAll.put(uuid, pStatAll);
                                            pStatClass.setData(statAll);
                                            Farming farmingClass =  new Farming(p);
                                            farmingClass.oneWithNatureStart();
                                        }
                                        else if (skillName.equals("fishing")) {
                                            pStatAll.put(skillName, pStats);
                                            statAll.put(uuid, pStatAll);
                                            pStatClass.setData(statAll);
                                            Fishing fishingClass = new Fishing(p);
                                            fishingClass.fishPersonStart();
                                        }
                                        else if (skillName.equals("defense")) {
                                            pStatAll.put(skillName, pStats);
                                            statAll.put(uuid, pStatAll);
                                            pStatClass.setData(statAll);
                                            Defense defenseClass = new Defense(p);
                                            defenseClass.hearty();
                                        }
                                        else if (skillName.equals("agility")) {
                                            pStatAll.put(skillName, pStats);
                                            statAll.put(uuid, pStatAll);
                                            pStatClass.setData(statAll);
                                            Agility agilityClass = new Agility(p);
                                            agilityClass.gracefulFeetStart();
                                        }
                                        break;
                                }
                                pStatAll.put(skillName, pStats);
                                statAll.put(uuid, pStatAll);
                                pStatClass.setData(statAll);
                            } else {
                                p.sendMessage(ChatColor.RED + langManager.getString("noSkillTokens"));
                            }
                            p.performCommand("frpg skillTreeGUI " + skillName);
                            break;

                        case GREEN_TERRACOTTA:
                            p.sendMessage(ChatColor.RED + langManager.getString("maxedOutPerk"));
                            break;
                        case RED_DYE:
                            if (pStats.get(2).intValue() > 0) {
                                pStats.set(2, pStats.get(2).intValue() - 1);
                                pStats.set(4, pStats.get(4).intValue() + 1);
                                pStatAll.put(skillName, pStats);
                                statAll.put(uuid, pStatAll);
                                pStatClass.setData(statAll);
                            } else {
                                p.sendMessage(ChatColor.RED + langManager.getString("noPassiveTokens"));
                            }

                            p.performCommand("frpg skillTreeGUI " + skillName);
                            break;
                        case GREEN_DYE:
                            int maxLevel2 = passiveMax.findMaxLevel(skillName,2);
                            if (pStats.get(5).intValue() >= maxLevel2) {
                                p.sendMessage(ChatColor.RED + langManager.getString("maxedOutPerk"));
                            }
                            else {
                                if (pStats.get(2).intValue() > 0) {
                                    pStats.set(2, pStats.get(2).intValue() - 1);
                                    pStats.set(5, pStats.get(5).intValue() + 1);
                                    pStatAll.put(skillName, pStats);
                                    statAll.put(uuid, pStatAll);
                                    pStatClass.setData(statAll);
                                } else {
                                    p.sendMessage(ChatColor.RED + langManager.getString("noPassiveTokens"));
                                }
                            }
                            p.performCommand("frpg skillTreeGUI " + skillName);
                            break;
                        case BLUE_DYE:
                            int maxLevel3 = passiveMax.findMaxLevel(skillName,3);
                            if (pStats.get(6).intValue() >= maxLevel3) {
                                p.sendMessage(ChatColor.RED + langManager.getString("maxedOutPerk"));
                            }
                            else {
                                if (pStats.get(2).intValue() > 0) {
                                    pStats.set(2, pStats.get(2).intValue() - 1);
                                    pStats.set(6, pStats.get(6).intValue() + 1);
                                    pStatAll.put(skillName, pStats);
                                    statAll.put(uuid, pStatAll);
                                    pStatClass.setData(statAll);
                                } else {
                                    p.sendMessage(ChatColor.RED + langManager.getString("noPassiveTokens"));
                                }
                            }
                            p.performCommand("frpg skillTreeGUI " + skillName);
                            break;
                        case LIME_WOOL:
                        case GRAY_WOOL:
                            switch (skillName) {
                                case "digging":
                                    if ((int) pStats.get(11) > 0 && e.getSlot() == 47) {
                                        p.performCommand("frpg flintToggle");
                                        p.performCommand("frpg skillTreeGUI " + skillName);
                                    }
                                    else if ((int) pStats.get(13) > 0  && e.getSlot() == 48 ) {
                                        p.performCommand("frpg megaDigToggle");
                                        p.performCommand("frpg skillTreeGUI " + skillName);
                                    }
                                    break;
                                case "mining":
                                    if ((int) pStats.get(11) > 0) {
                                        p.performCommand("frpg veinMinerToggle");
                                        p.performCommand("frpg skillTreeGUI " + skillName);
                                    }
                                    break;
                                case "fishing":
                                    if ((int) pStats.get(11) > 0  && e.getSlot() == 47) {
                                        p.performCommand("frpg grappleToggle");
                                        p.performCommand("frpg skillTreeGUI " + skillName);
                                    }
                                    else if ((int) pStats.get(12) > 0  && e.getSlot() == 48) {
                                        p.performCommand("frpg hotRodToggle");
                                        p.performCommand("frpg skillTreeGUI " + skillName);
                                    }
                                    break;
                                case "agility":
                                    if ((int) pStats.get(13) > 0) {
                                        p.performCommand("frpg speedTogggle");
                                        p.performCommand("frpg skillTreeGUI " + skillName);
                                    }
                                    break;
                                case "alchemy":
                                    if ((int) pStats.get(13) > 0) {
                                        p.performCommand("frpg potionToggle");
                                        p.performCommand("frpg skillTreeGUI " + skillName);
                                    }
                                    break;
                                case "smelting":
                                    if ((int) pStats.get(13) > 0) {
                                        p.performCommand("frpg flamePickToggle");
                                        p.performCommand("frpg skillTreeGUI " + skillName);
                                    }
                                    break;
                                default:
                                    break;
                            }
                                break;
                        case CRAFTING_TABLE:
                            if (skillName.equalsIgnoreCase("farming")) {
                                switch (e.getRawSlot()) {
                                    case 48:
                                        p.closeInventory();
                                        p.performCommand("frpg craftingGUI " + "farming1");
                                        break;
                                    case 49:
                                        p.closeInventory();
                                        p.performCommand("frpg craftingGUI " + "farming2");
                                        break;
                                    case 50:
                                        p.closeInventory();
                                        p.performCommand("frpg craftingGUI " + "farming3");
                                        break;
                                    case 51:
                                        p.closeInventory();
                                        p.performCommand("frpg craftingGUI " + "farming4");
                                        break;
                                    case 52:
                                        p.closeInventory();
                                        p.performCommand("frpg craftingGUI " + "farming5");
                                        break;
                                    default:
                                        break;
                                }
                            }
                            else if (skillName.equalsIgnoreCase("archery")) {
                                p.closeInventory();
                                p.performCommand("frpg craftingGUI " + "archery1");
                            }
                            else if (skillName.equalsIgnoreCase("enchanting")) {
                                switch (e.getRawSlot()) {
                                    case 39:
                                        p.closeInventory();
                                        p.performCommand("frpg craftingGUI " + "enchanting1");
                                        break;
                                    case 40:
                                        p.closeInventory();
                                        p.performCommand("frpg craftingGUI " + "enchanting2");
                                        break;
                                    case 41:
                                        p.closeInventory();
                                        p.performCommand("frpg craftingGUI " + "enchanting3");
                                        break;
                                    case 42:
                                        p.closeInventory();
                                        p.performCommand("frpg craftingGUI " + "enchanting4");
                                        break;
                                    case 43:
                                        p.closeInventory();
                                        p.performCommand("frpg craftingGUI " + "enchanting5");
                                        break;
                                    case 48:
                                        p.closeInventory();
                                        p.performCommand("frpg craftingGUI " + "enchanting6");
                                        break;
                                    case 49:
                                        p.closeInventory();
                                        p.performCommand("frpg craftingGUI " + "enchanting7");
                                        break;
                                    case 50:
                                        p.closeInventory();
                                        p.performCommand("frpg craftingGUI " + "enchanting8");
                                        break;
                                    case 51:
                                        p.closeInventory();
                                        p.performCommand("frpg craftingGUI " + "enchanting9");
                                        break;
                                    case 52:
                                        p.closeInventory();
                                        p.performCommand("frpg craftingGUI " + "enchanting10");
                                        break;
                                    default:
                                        break;
                                }
                            }
                            if (skillName.equalsIgnoreCase("alchemy")) {
                                switch (e.getRawSlot()) {
                                    case 48:
                                        p.closeInventory();
                                        p.performCommand("frpg craftingGUI " + "alchemy1");
                                        break;
                                    case 49:
                                        p.closeInventory();
                                        p.performCommand("frpg craftingGUI " + "alchemy2");
                                        break;
                                    case 50:
                                        p.closeInventory();
                                        p.performCommand("frpg craftingGUI " + "alchemy3");
                                        break;
                                    case 51:
                                        p.closeInventory();
                                        p.performCommand("frpg craftingGUI " + "alchemy4");
                                        break;
                                    case 52:
                                        p.closeInventory();
                                        p.performCommand("frpg craftingGUI " + "alchemy5");
                                        break;
                                }
                            }
                        default:
                            break;
                    }
                    e.setCancelled(true); //So they cant take the items
                }
            } else if (title.equalsIgnoreCase("Global")) {
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
                LanguageSelector langManager = new LanguageSelector(p);
                String skillName = "global";
                PlayerStats pStatClass = new PlayerStats(p);
                Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
                Map<String, ArrayList<Number>> pStatAll = statAll.get(p.getUniqueId());
                ArrayList<Number> pStats = pStatAll.get(skillName);
                if (e.getCurrentItem() != null && e.getSlot() < 54) {
                    switch (e.getCurrentItem().getType()) {
                        case ARROW:
                            p.closeInventory();
                            p.performCommand("frpg skills");
                            break;
                        case RED_TERRACOTTA:
                            if (e.getSlot() == 3 || e.getSlot() == 21 || e.getSlot() == 39) {
                                p.sendMessage(ChatColor.RED + langManager.getString("requiredGlobalPerks0"));
                            } else if (e.getSlot() == 24) {
                                p.sendMessage(ChatColor.RED + langManager.getString("globalPerkTitle3") + ", " + langManager.getString("globalPerkTitle4") + ", " + langManager.getString("globalPerkTitle5") + " " + langManager.getString("requiredGlobalPerks1"));
                            } else if (e.getSlot() == 6 || e.getSlot() == 42) {
                                p.sendMessage(ChatColor.RED + langManager.getString("globalPerkTitle7") + " " + langManager.getString("requiredGlobalPerks2"));
                            } else if (e.getSlot() == 26) {
                                p.sendMessage(ChatColor.RED + langManager.getString("requiredGlobalPerks3"));
                            }
                            break;
                        case PINK_TERRACOTTA:
                            if (pStats.get(1).intValue() > 0) {
                                pStats.set(1, pStats.get(1).intValue() - 1);
                                switch (e.getSlot()) {
                                    case 1:
                                        pStats.set(2, pStats.get(2).intValue() + 1);
                                        break;
                                    case 19:
                                        pStats.set(3, pStats.get(3).intValue() + 1);
                                        break;
                                    case 37:
                                        pStats.set(4, pStats.get(4).intValue() + 1);
                                        break;
                                    case 3:
                                        pStats.set(5, pStats.get(5).intValue() + 1);
                                        Global globalClass5 = new Global(p);
                                        globalClass5.skillTokenBoost(5);
                                        break;
                                    case 21:
                                        pStats.set(6, pStats.get(6).intValue() + 1);
                                        Global globalClass6 = new Global(p);
                                        globalClass6.skillTokenBoost(6);
                                        break;
                                    case 39:
                                        pStats.set(7, pStats.get(7).intValue() + 1);
                                        Global globalClass7 = new Global(p);
                                        globalClass7.skillTokenBoost(7);
                                        break;
                                    case 6:
                                        pStats.set(8, pStats.get(8).intValue() + 1);
                                        break;
                                    case 24:
                                        pStats.set(9, pStats.get(9).intValue() + 1);

                                        /* Old Skill_3b perk
                                        Global globalClass9 = new Global(p);
                                        globalClass9.passiveTokenBoost();
                                         */
                                        break;
                                    case 42:
                                        pStats.set(10, pStats.get(10).intValue() + 1);
                                        break;
                                    case 26:
                                        pStats.set(11, pStats.get(11).intValue() + 1);
                                        break;
                                }
                                pStatAll.put(skillName, pStats);
                                statAll.put(p.getUniqueId(), pStatAll);
                                pStatClass.setData(statAll);
                            } else {
                                p.sendMessage(ChatColor.RED + langManager.getString("noSkillTokens"));
                            }
                            p.performCommand("frpg skillTreeGUI " + skillName);
                            break;

                        case GREEN_TERRACOTTA:
                            p.sendMessage(ChatColor.RED + langManager.getString("maxedOutPerk"));
                            break;

                    }
                    e.setCancelled(true); //So they cant take the items
                }
            }
        }
    }
}
