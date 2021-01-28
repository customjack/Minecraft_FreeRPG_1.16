package mc.carlton.freerpg.brewingEvents;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.BrewingStandUserTracker;
import mc.carlton.freerpg.globalVariables.ItemGroups;
import mc.carlton.freerpg.perksAndAbilities.Alchemy;
import mc.carlton.freerpg.serverConfig.ConfigLoad;
import mc.carlton.freerpg.playerInfo.PlayerStats;
import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BrewingInventoryClick implements Listener {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    @EventHandler(priority = EventPriority.HIGH)
    void onInventoryClick(InventoryClickEvent e) {
        if (e.isCancelled()) {
            return;
        }
        try {
            InventoryType invType = e.getClickedInventory().getType();
        } catch (Exception except) {
            return;
        }
        ConfigLoad configLoad = new ConfigLoad();
        if (!configLoad.getAllowedSkillsMap().get("alchemy")) {
            return;
        }

        /* Perk removed
        if (e.getInventory().getHolder() instanceof BrewingStand) { //This section is very buggy, and not clean. Hopefully I can improve it in the future
            Player p = (Player) e.getWhoClicked();
            BrewingStandUserTracker brewTracker = new BrewingStandUserTracker();
            brewTracker.addstand((BrewingStand) e.getInventory().getHolder(),p);
            int slot = e.getSlot();
            if ( (slot <= 5) || (e.getClick() == ClickType.SHIFT_LEFT  || e.getClick() == ClickType.SHIFT_RIGHT) ) {
                BrewerInventory brewingInventory = (BrewerInventory) e.getInventory();
                playerStats pStatClass = new playerStats(p);
                Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
                int speedBrewingLevel = (int) pStat.get("alchemy").get(7);
                int time = (int) Math.round((1 - speedBrewingLevel * 0.15) * 400);
                final BrewingStand stand = (BrewingStand) e.getInventory().getHolder();
                stand.setBrewingTime(time);
                stand.update();
            }
        }
         */

        if (e.getInventory().getHolder() instanceof BrewingStand) { //This section is very buggy, and not clean. Hopefully I can improve it in the future
            Player p = (Player) e.getWhoClicked();
            BrewingStandUserTracker brewTracker = new BrewingStandUserTracker();
            brewTracker.addstand((BrewingStand) e.getInventory().getHolder(), p);
        }

        if (e.getClick() != ClickType.LEFT) {
            return;
        }
        if (e.getClickedInventory() instanceof BrewerInventory) {
            ItemGroups itemGroups = new ItemGroups();
            List<Material> oldIngredients = itemGroups.getOldIngredients();
            List<Material> newIngredients = itemGroups.getNewIngredients();
            ItemStack heroPotion = itemGroups.getHeroPotion();
            ItemStack fatiguePotion = itemGroups.getFatiguePotion();
            ItemStack hastePotion = itemGroups.getHastePotion();
            ItemStack decayPotion = itemGroups.getDecayPotion();
            ItemStack resistancePotion = itemGroups.getResistancePotion();


            BrewerInventory brewingInventory = (BrewerInventory) e.getClickedInventory();
            Player p = (Player) e.getWhoClicked();
            if (e.getSlot() == 3) {
                PlayerStats pStatClass = new PlayerStats(p);
                Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
                ItemStack awkwardBottle = new ItemStack(Material.POTION,1);
                PotionMeta awkward = (PotionMeta) awkwardBottle.getItemMeta();
                awkward.setBasePotionData(new PotionData(PotionType.AWKWARD));
                awkwardBottle.setItemMeta(awkward);
                final ItemStack ingredient = e.getCurrentItem();
                final ItemStack cursorClone = e.getCursor().clone();
                final ItemStack potionSlot1 = e.getInventory().getItem(0);
                final ItemStack potionSlot2 = e.getInventory().getItem(1);
                final ItemStack potionSlot3 = e.getInventory().getItem(2);
                final ItemStack[] potionSlots = {potionSlot1,potionSlot2,potionSlot3};
                if (cursorClone == null) {
                    return;
                }
                if (cursorClone.getType() == Material.AIR) {
                    return;
                }
                if (cursorClone.getType() == ingredient.getType()) {
                    e.setCancelled(true);
                    return;
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!(oldIngredients.contains(cursorClone.getType()))) {
                            e.getView().setCursor(ingredient);
                            e.getClickedInventory().setItem(e.getSlot(), cursorClone);
                            p.updateInventory();
                        }
                        Alchemy alchemyClass = new Alchemy(p);
                        if (newIngredients.contains(cursorClone.getType())) {
                            ItemStack[] potionSlots = {potionSlot1,potionSlot2,potionSlot3};
                            boolean[] slotsAwkward = {alchemyClass.comparePotionEffects(potionSlot1, awkwardBottle), alchemyClass.comparePotionEffects(potionSlot2, awkwardBottle), alchemyClass.comparePotionEffects(potionSlot3, awkwardBottle)};
                            boolean proceed = true;
                            for (int i = 0; i < 3; i++) {
                                if (slotsAwkward[i]) {
                                    continue;
                                } else if (potionSlots[i] == null) {
                                    continue;
                                } else if (potionSlots[i].getType() == Material.AIR) {
                                    continue;
                                }
                                proceed = false;
                                break;
                            }
                            if (proceed) {
                                if (newIngredients.indexOf(cursorClone.getType()) == 0 && (int)pStat.get("alchemy").get(9) >= 1) {
                                    alchemyClass.startBrewing(brewingInventory, heroPotion, cursorClone);
                                } else if (newIngredients.indexOf(cursorClone.getType()) == 1 && (int)pStat.get("alchemy").get(9) >= 2) {
                                    alchemyClass.startBrewing(brewingInventory, fatiguePotion, cursorClone);
                                } else if (newIngredients.indexOf(cursorClone.getType()) == 2 && (int)pStat.get("alchemy").get(9) >= 3) {
                                    alchemyClass.startBrewing(brewingInventory, hastePotion, cursorClone);
                                } else if (newIngredients.indexOf(cursorClone.getType()) == 3 && (int)pStat.get("alchemy").get(9) >= 4) {
                                    alchemyClass.startBrewing(brewingInventory, decayPotion, cursorClone);
                                } else if (newIngredients.indexOf(cursorClone.getType()) == 4 && (int)pStat.get("alchemy").get(9) >= 5) {
                                    alchemyClass.startBrewing(brewingInventory, resistancePotion, cursorClone);
                                }
                            }
                        } else if (cursorClone.getType() == Material.GLOWSTONE_DUST || cursorClone.getType() == Material.REDSTONE) {
                            boolean[] slotsToCheck = {false,false,false};
                            for (int i = 0; i < 3; i++) {
                                if (potionSlots[i] != null) {
                                    if (potionSlots[i].getItemMeta().hasEnchant(Enchantment.LOYALTY)) {
                                        slotsToCheck[i] = true;
                                    }
                                }
                            }
                            if ( !slotsToCheck[0] && !slotsToCheck[1] && !slotsToCheck[2]) {
                                return;
                            }
                            alchemyClass.upgradeBrewing(brewingInventory, cursorClone,slotsToCheck);
                        }
                    }
                }.runTaskLater(plugin, 1);
            }
        }
    }
}
