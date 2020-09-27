package mc.carlton.freerpg.guiEvents;

import mc.carlton.freerpg.gameTools.LanguageSelector;
import mc.carlton.freerpg.playerInfo.PlayerStats;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class SkillsConfigGUIClick implements Listener {
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
        String[] labels_0 = {"digging", "woodcutting", "mining", "farming", "fishing", "archery", "beastMastery", "swordsmanship", "defense", "axeMastery", "repair", "agility", "alchemy", "smelting", "enchanting"};
        String[] passiveLabels_0 = {"repair", "agility", "alchemy", "smelting", "enchanting"};
        List<String> labels = Arrays.asList(labels_0);
        List<String> skillTitles = Arrays.asList(titles_0);
        List<String> passiveLabels = Arrays.asList(passiveLabels_0);
        for (String skillTitle : skillTitles) {
            if (e.getView().getTitle().equalsIgnoreCase(skillTitle + " Configuration")) {
                String skillName = labels.get(skillTitles.indexOf(skillTitle));
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
                    if (e.getCurrentItem().getType().equals(Material.ARROW)) {
                        p.performCommand("frpg skillTreeGUI " + skillName);
                    }
                    else if (e.getRawSlot() == 19) {//Toggle EXPBar for skill
                        pStatClass.togglePlayerSkillExpBar(skillName);
                        p.performCommand("frpg skillConfigGUI " + skillName);
                    }
                    else if(e.getRawSlot() == 20 && !passiveLabels.contains(skillName)) { //Toggle Ability for skill
                        pStatClass.togglePlayerSkillAbility(skillName);
                        p.performCommand("frpg skillConfigGUI " + skillName);
                    }
                    else {
                        switch (skillName) {
                            case "digging":
                                if (e.getSlot() == 37) {
                                    p.performCommand("frpg flintToggle");
                                    p.performCommand("frpg skillConfigGUI " + skillName);
                                } else if (e.getSlot() == 38) {
                                    p.performCommand("frpg megaDigToggle");
                                    p.performCommand("frpg skillConfigGUI " + skillName);
                                }
                                break;
                            case "woodcutting":
                                if (e.getSlot() == 37) {
                                    p.performCommand("frpg leafBlowerToggle");
                                    p.performCommand("frpg skillConfigGUI " + skillName);
                                }
                                break;
                            case "mining":
                                if (e.getSlot() == 37) {
                                    p.performCommand("frpg veinMinerToggle");
                                    p.performCommand("frpg skillConfigGUI " + skillName);
                                }
                                break;
                            case "fishing":
                                if (e.getSlot() == 37) {
                                    p.performCommand("frpg grappleToggle");
                                    p.performCommand("frpg skillConfigGUI " + skillName);
                                } else if (e.getSlot() == 38) {
                                    p.performCommand("frpg hotRodToggle");
                                    p.performCommand("frpg skillConfigGUI " + skillName);
                                }
                                break;
                            case "axeMastery":
                                if (e.getSlot() == 37) {
                                    p.performCommand("frpg holyAxeToggle");
                                    p.performCommand("frpg skillConfigGUI " + skillName);
                                }
                                break;
                            case "agility":
                                if (e.getSlot() == 37) {
                                    p.performCommand("frpg speedToggle");
                                    p.performCommand("frpg skillConfigGUI " + skillName);
                                }
                                break;
                            case "alchemy":
                                if (e.getSlot() == 37) {
                                    p.performCommand("frpg potionToggle");
                                    p.performCommand("frpg skillConfigGUI " + skillName);
                                }
                                break;
                            case "smelting":
                                if (e.getSlot() == 37) {
                                    p.performCommand("frpg flamePickToggle");
                                    p.performCommand("frpg skillConfigGUI " + skillName);
                                }
                                break;
                            default:
                                break;
                        }
                    }

                    e.setCancelled(true); //So they cant take the items
                }
                break;
            }
        }
    }
}

