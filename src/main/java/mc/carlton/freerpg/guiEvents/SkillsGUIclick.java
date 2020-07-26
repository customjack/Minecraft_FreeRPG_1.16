package mc.carlton.freerpg.guiEvents;
import mc.carlton.freerpg.perksAndAbilities.*;
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
                            p.performCommand("skills");
                            break;
                        case COMPOSTER:
                            if ((int) pStatAll.get("global").get(20) >= 250 && (int) pStatAll.get("global").get(9) > 0) {
                                p.closeInventory();
                                p.performCommand("confirmationGUI " + skillName);
                            }
                            else {
                                if ((int) pStatAll.get("global").get(9) > 0) {
                                    p.sendMessage(ChatColor.RED + "You need at least 250 souls to refund a skill tree");
                                }
                                else {
                                    p.sendMessage(ChatColor.RED + "You need to unlock " +ChatColor.BOLD + "Soul Harvesting" + ChatColor.RESET + ChatColor.RED.toString() + " to refund skill trees");
                                }
                            }
                            break;
                        case RED_TERRACOTTA:
                            if (e.getSlot() == 13 || e.getSlot() == 31 || e.getSlot() == 7 || e.getSlot() == 43 || e.getSlot() == 20 || e.getSlot() == 23) {
                                p.sendMessage(ChatColor.RED + "You need at least 2 skill tokens invested in the previous perks to unlock this perk.");
                            } else if (e.getSlot() == 26) {
                                p.sendMessage(ChatColor.RED + "You need at least 10 total skill tokens invested in the skill tree to unlock this mastery perk.");
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
                                p.sendMessage(ChatColor.RED + "You do not have any skill tokens.");
                            }
                            p.performCommand("skillTreeGUI " + skillName);
                            break;

                        case GREEN_TERRACOTTA:
                            p.sendMessage(ChatColor.RED + "You have already maxed out this perk!");
                            break;
                        case RED_DYE:
                            if (pStats.get(2).intValue() > 0) {
                                pStats.set(2, pStats.get(2).intValue() - 1);
                                pStats.set(4, pStats.get(4).intValue() + 1);
                                pStatAll.put(skillName, pStats);
                                statAll.put(uuid, pStatAll);
                                pStatClass.setData(statAll);
                            } else {
                                p.sendMessage(ChatColor.RED + "You do not have any passive tokens.");
                            }

                            p.performCommand("skillTreeGUI " + skillName);
                            break;
                        case GREEN_DYE:
                            int maxLevel2 = passiveMax.findMaxLevel(skillName,2);
                            if (pStats.get(5).intValue() >= maxLevel2) {
                                p.sendMessage(ChatColor.RED + "You have reached the max level in this perk!");
                            }
                            else {
                                if (pStats.get(2).intValue() > 0) {
                                    pStats.set(2, pStats.get(2).intValue() - 1);
                                    pStats.set(5, pStats.get(5).intValue() + 1);
                                    pStatAll.put(skillName, pStats);
                                    statAll.put(uuid, pStatAll);
                                    pStatClass.setData(statAll);
                                } else {
                                    p.sendMessage(ChatColor.RED + "You do not have any passive tokens.");
                                }
                            }
                            p.performCommand("skillTreeGUI " + skillName);
                            break;
                        case BLUE_DYE:
                            int maxLevel3 = passiveMax.findMaxLevel(skillName,3);
                            if (pStats.get(6).intValue() >= maxLevel3) {
                                p.sendMessage(ChatColor.RED + "You have reached the max level in this perk!");
                            }
                            else {
                                if (pStats.get(2).intValue() > 0) {
                                    pStats.set(2, pStats.get(2).intValue() - 1);
                                    pStats.set(6, pStats.get(6).intValue() + 1);
                                    pStatAll.put(skillName, pStats);
                                    statAll.put(uuid, pStatAll);
                                    pStatClass.setData(statAll);
                                } else {
                                    p.sendMessage(ChatColor.RED + "You do not have any passive tokens.");
                                }
                            }
                            p.performCommand("skillTreeGUI " + skillName);
                            break;
                        case LIME_WOOL:
                        case GRAY_WOOL:
                            switch (skillName) {
                                case "digging":
                                    if ((int) pStats.get(11) > 0 && e.getSlot() == 47) {
                                        p.performCommand("flintToggle");
                                        p.performCommand("skillTreeGUI " + skillName);
                                    }
                                    else if ((int) pStats.get(13) > 0  && e.getSlot() == 48 ) {
                                        p.performCommand("megaDigToggle");
                                        p.performCommand("skillTreeGUI " + skillName);
                                    }
                                    break;
                                case "mining":
                                    if ((int) pStats.get(11) > 0) {
                                        p.performCommand("veinMinerToggle");
                                        p.performCommand("skillTreeGUI " + skillName);
                                    }
                                    break;
                                case "fishing":
                                    if ((int) pStats.get(11) > 0  && e.getSlot() == 47) {
                                        p.performCommand("grappleToggle");
                                        p.performCommand("skillTreeGUI " + skillName);
                                    }
                                    else if ((int) pStats.get(12) > 0  && e.getSlot() == 48) {
                                        p.performCommand("hotRodToggle");
                                        p.performCommand("skillTreeGUI " + skillName);
                                    }
                                    break;
                                case "agility":
                                    if ((int) pStats.get(13) > 0) {
                                        p.performCommand("speedTogggle");
                                        p.performCommand("skillTreeGUI " + skillName);
                                    }
                                    break;
                                case "alchemy":
                                    if ((int) pStats.get(13) > 0) {
                                        p.performCommand("potionToggle");
                                        p.performCommand("skillTreeGUI " + skillName);
                                    }
                                    break;
                                case "smelting":
                                    if ((int) pStats.get(13) > 0) {
                                        p.performCommand("flamePickToggle");
                                        p.performCommand("skillTreeGUI " + skillName);
                                    }
                                    break;
                                default:
                                    break;
                            }
                                break;
                        case CRAFTING_TABLE:
                            ItemStack craftingItem = e.getCurrentItem();
                            String craftingName = craftingItem.getItemMeta().getDisplayName();
                            craftingName = craftingName.substring(2);
                            switch (craftingName) {
                                case "Cow Egg":
                                    p.closeInventory();
                                    p.performCommand("craftingGUI " + "farming1");
                                    break;
                                case "Bee Egg":
                                    p.closeInventory();
                                    p.performCommand("craftingGUI " + "farming2");
                                    break;
                                case "Mooshroom Egg":
                                    p.closeInventory();
                                    p.performCommand("craftingGUI " + "farming3");
                                    break;
                                case "Horse Egg":
                                    p.closeInventory();
                                    p.performCommand("craftingGUI " + "farming4");
                                    break;
                                case "Slime Egg":
                                    p.closeInventory();
                                    p.performCommand("craftingGUI " + "farming5");
                                    break;
                                case "Dragonless Arrows":
                                    p.closeInventory();
                                    p.performCommand("craftingGUI " + "archery1");
                                    break;
                                case "Power I Book":
                                    p.closeInventory();
                                    p.performCommand("craftingGUI " + "enchanting1");
                                    break;
                                case "Efficiency I Book":
                                    p.closeInventory();
                                    p.performCommand("craftingGUI " + "enchanting2");
                                    break;
                                case "Sharpness I Book":
                                    p.closeInventory();
                                    p.performCommand("craftingGUI " + "enchanting3");
                                    break;
                                case "Protection I Book":
                                    p.closeInventory();
                                    p.performCommand("craftingGUI " + "enchanting4");
                                    break;
                                case "Luck of the Sea I Book":
                                    p.closeInventory();
                                    p.performCommand("craftingGUI " + "enchanting5");
                                    break;
                                case "Lure I Book":
                                    p.closeInventory();
                                    p.performCommand("craftingGUI " + "enchanting6");
                                    break;
                                case "Frost Walker I Book":
                                    p.closeInventory();
                                    p.performCommand("craftingGUI " + "enchanting7");
                                    break;
                                case "Depth Strider I Book":
                                    p.closeInventory();
                                    p.performCommand("craftingGUI " + "enchanting8");
                                    break;
                                case "Mending Book":
                                    p.closeInventory();
                                    p.performCommand("craftingGUI " + "enchanting9");
                                    break;
                                case "Fortune I Book":
                                    p.closeInventory();
                                    p.performCommand("craftingGUI " + "enchanting10");
                                    break;
                                case "Water Breathing Potion":
                                    p.closeInventory();
                                    p.performCommand("craftingGUI " + "alchemy1");
                                    break;
                                case "Speed Potion":
                                    p.closeInventory();
                                    p.performCommand("craftingGUI " + "alchemy2");
                                    break;
                                case "Fire Resistance Potion":
                                    p.closeInventory();
                                    p.performCommand("craftingGUI " + "alchemy3");
                                    break;
                                case "Healing Potion":
                                    p.closeInventory();
                                    p.performCommand("craftingGUI " + "alchemy4");
                                    break;
                                case "Strength Potion":
                                    p.closeInventory();
                                    p.performCommand("craftingGUI " + "alchemy5");
                                    break;
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
                String skillName = "global";
                PlayerStats pStatClass = new PlayerStats(p);
                Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
                Map<String, ArrayList<Number>> pStatAll = statAll.get(p.getUniqueId());
                ArrayList<Number> pStats = pStatAll.get(skillName);
                if (e.getCurrentItem() != null && e.getSlot() < 54) {
                    switch (e.getCurrentItem().getType()) {
                        case ARROW:
                            p.closeInventory();
                            p.performCommand("skills");
                            break;
                        case RED_TERRACOTTA:
                            if (e.getSlot() == 3 || e.getSlot() == 21 || e.getSlot() == 39) {
                                p.sendMessage(ChatColor.RED + "You need the previous perk to unlock this perk.");
                            } else if (e.getSlot() == 24) {
                                p.sendMessage(ChatColor.RED + "You need Hard Work, Research, and Training to unlock this perk.");
                            } else if (e.getSlot() == 6 || e.getSlot() == 42) {
                                p.sendMessage(ChatColor.RED + "You need Job Bonus to unlock this perk.");
                            } else if (e.getSlot() == 26) {
                                p.sendMessage(ChatColor.RED + "You need every global perk to unlock this perk.");
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
                                p.sendMessage(ChatColor.RED + "You do not have any skill tokens.");
                            }
                            p.performCommand("skillTreeGUI " + skillName);
                            break;

                        case GREEN_TERRACOTTA:
                            p.sendMessage(ChatColor.RED + "You have already maxed out this perk!");
                            break;

                    }
                    e.setCancelled(true); //So they cant take the items
                }
            }
        }
    }
}
