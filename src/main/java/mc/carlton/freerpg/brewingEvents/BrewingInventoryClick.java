package mc.carlton.freerpg.brewingEvents;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.perksAndAbilities.Alchemy;
import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BrewingInventoryClick implements Listener {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    @EventHandler
    void onInventoryClick(InventoryClickEvent e) {
        try {
            InventoryType invType = e.getClickedInventory().getType();
        } catch (Exception except) {
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
        if (e.getClick() != ClickType.LEFT) {
            return;
        }
        if (e.getClickedInventory() instanceof BrewerInventory) {
            Material[] newIngredients0 = {Material.EMERALD,Material.SLIME_BALL,Material.CLOCK,Material.POISONOUS_POTATO,Material.GOLDEN_APPLE};
            List<Material> newIngredients = Arrays.asList(newIngredients0);
            Material[] oldIngredients0 = {Material.NETHER_WART,Material.GUNPOWDER,Material.GLOWSTONE_DUST,Material.SPIDER_EYE,Material.GHAST_TEAR,
                                          Material.RABBIT_FOOT,Material.BLAZE_POWDER,Material.GLISTERING_MELON_SLICE,Material.SUGAR,Material.MAGMA_CREAM,
                                          Material.REDSTONE, Material.PUFFERFISH, Material.GOLDEN_CARROT,Material.TURTLE_HELMET,Material.PHANTOM_MEMBRANE,
                                          Material.FERMENTED_SPIDER_EYE};
            List<Material> oldIngredients = Arrays.asList(oldIngredients0);

            //Haste Potion
            ItemStack hastePotion = new ItemStack(Material.POTION,1);
            hastePotion.addUnsafeEnchantment(Enchantment.LOYALTY,1);
            PotionMeta hasteMeta = (PotionMeta) hastePotion.getItemMeta();
            hasteMeta.addCustomEffect(new PotionEffect(PotionEffectType.FAST_DIGGING,20*180,0),true);
            hasteMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            hasteMeta.setColor(Color.fromBGR(186,232,159));
            hasteMeta.setDisplayName("Potion of Haste");
            hastePotion.setItemMeta(hasteMeta);


            //Mining Fatigue Potion
            ItemStack fatiguePotion = new ItemStack(Material.POTION,1);
            fatiguePotion.addUnsafeEnchantment(Enchantment.LOYALTY,1);
            PotionMeta fatigueMeta = (PotionMeta) fatiguePotion.getItemMeta();
            fatigueMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,20*60,0),true);
            fatigueMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            fatigueMeta.setColor(Color.fromBGR(10,212,164));
            fatigueMeta.setDisplayName("Potion of Fatigue");
            fatiguePotion.setItemMeta(fatigueMeta);


            //Absorption Potion
            ItemStack heroPotion = new ItemStack(Material.POTION,1);
            heroPotion.addUnsafeEnchantment(Enchantment.LOYALTY,1);
            PotionMeta heroMeta = (PotionMeta) heroPotion.getItemMeta();
            heroMeta.addCustomEffect(new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE,20*180,0),true);
            heroMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            heroMeta.setColor(Color.fromBGR(64, 210, 30));
            heroMeta.setDisplayName("Potion of the Hero");
            heroPotion.setItemMeta(heroMeta);

            //decay Potion
            ItemStack decayPotion = new ItemStack(Material.POTION,1);
            decayPotion.addUnsafeEnchantment(Enchantment.LOYALTY,1);
            PotionMeta decayMeta = (PotionMeta) decayPotion.getItemMeta();
            decayMeta.addCustomEffect(new PotionEffect(PotionEffectType.WITHER,20*30,0),true);
            decayMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            decayMeta.setColor(Color.fromBGR(0,0,0));
            decayMeta.setDisplayName("Potion of Decay");
            decayPotion.setItemMeta(decayMeta);

            //resistance Potion
            ItemStack resistancePotion = new ItemStack(Material.POTION,1);
            resistancePotion.addUnsafeEnchantment(Enchantment.LOYALTY,1);
            PotionMeta resistanceMeta = (PotionMeta) resistancePotion.getItemMeta();
            resistanceMeta.addCustomEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,20*180,0),true);
            resistanceMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            resistanceMeta.setColor(Color.fromBGR(175,140,150));
            resistanceMeta.setDisplayName("Potion of Resistance");
            resistancePotion.setItemMeta(resistanceMeta);


            ItemStack[] newPotions0 = {hastePotion,fatiguePotion,heroPotion,decayPotion,resistancePotion};
            List<ItemStack> newPotions = Arrays.asList(newPotions0);
            Integer[] durations0 = {20*180,20*30,20*60};
            List<Integer> durations = Arrays.asList(durations0);

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
                            ArrayList<ItemMeta> slotMetas = new ArrayList<>();
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
