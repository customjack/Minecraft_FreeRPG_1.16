package mc.carlton.freerpg.perksAndAbilities;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.ExperienceBottleTracking;
import mc.carlton.freerpg.gameTools.PsuedoEnchanting;
import mc.carlton.freerpg.playerAndServerInfo.ChangeStats;
import mc.carlton.freerpg.playerAndServerInfo.ConfigLoad;
import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Enchanting {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    private Player p;
    private String pName;
    private ItemStack itemInHand;
    private String skillName = "enchanting";
    Map<String,Integer> expMap;

    ChangeStats increaseStats; //Changing Stats

    PlayerStats pStatClass;
    //GET PLAYER STATS LIKE THIS:        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData(p);

    Random rand = new Random(); //Random class Import
    static Map<Player,EnchantmentOffer[]> offersHolder = new HashMap<Player, EnchantmentOffer[]>();

    private boolean runMethods;



    public Enchanting(Player p) {
        this.p = p;
        this.pName = p.getDisplayName();
        this.itemInHand = p.getInventory().getItemInMainHand();
        this.increaseStats = new ChangeStats(p);
        this.pStatClass = new PlayerStats(p);
        ConfigLoad configLoad = new ConfigLoad();
        this.runMethods = configLoad.getAllowedSkillsMap().get(skillName);
        expMap = configLoad.getExpMapForSkill(skillName);
    }

    public int xpIncrease(int oldAmount) {
        if (!runMethods) {
            return oldAmount;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int expBuffLevel = (int) pStat.get(skillName).get(4);
        double multiplier = 1 + expBuffLevel*0.002;
        int newAmount = (int) Math.floor(oldAmount*multiplier);
        double roundUpChance = oldAmount*multiplier - Math.floor(oldAmount*multiplier);
        if (roundUpChance > rand.nextDouble()) {
            newAmount += 1;
        }
        return newAmount;
    }

    public EnchantmentOffer[] enchantmentDiscount(EnchantmentOffer[] oldOffers) {
        if (!runMethods) {
            return oldOffers;
        }
        if (oldOffers.length == 0) {
            return oldOffers;
        }
        offersHolder.put(p,oldOffers.clone());
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int levelSubtract = (int) pStat.get(skillName).get(7);
        for (EnchantmentOffer offer : oldOffers) {
            if (offer != null) {
                int newCost = Math.max(offer.getCost() - levelSubtract, 1);
                offer.setCost(newCost);
            }
        }
        return oldOffers;
    }

    public void enchantItem(ItemStack enchantedItem, int buttonClicked, EnchantingInventory enchantingInventory) {
        if (!runMethods) {
            return;
        }
        EnchantmentOffer[] originalOffers = offersHolder.get(p);
        Enchantment enchant0 = originalOffers[buttonClicked].getEnchantment();
        int level0 = originalOffers[buttonClicked].getEnchantmentLevel();
        int cost0 = originalOffers[buttonClicked].getCost();
        increaseStats.changeEXP(skillName,expMap.get("enchantItem_EXPperLevelOfCost")*cost0);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (enchantedItem.getType() != Material.BOOK) {
                    for (Enchantment enchant : enchantedItem.getEnchantments().keySet()) {
                        enchantedItem.removeEnchantment(enchant);
                    }
                    enchantedItem.addUnsafeEnchantment(enchant0, level0);
                    PsuedoEnchanting fakeEnchant = new PsuedoEnchanting();
                    fakeEnchant.addEnchant(enchantedItem, cost0,false);
                }
                else {
                    ItemStack newBook = new ItemStack(Material.ENCHANTED_BOOK,1);
                    EnchantmentStorageMeta bookMeta =  (EnchantmentStorageMeta) newBook.getItemMeta();
                    bookMeta.addStoredEnchant(enchant0,level0,false);
                    newBook.setItemMeta(bookMeta);
                    PsuedoEnchanting fakeEnchant = new PsuedoEnchanting();
                    fakeEnchant.addEnchant(newBook, cost0,false);
                    enchantingInventory.setItem(newBook);
                }

            }
        }.runTaskLater(plugin, 1);

    }

    public void giveEXP(int exp) {
        if (!runMethods) {
            return;
        }
        int taskID = new BukkitRunnable() {
            @Override
            public void run() {
                ConfigLoad configLoad = new ConfigLoad();
                boolean giveEXP = true;
                if (!configLoad.isGetEXPFromEnchantingBottles()) {
                    ExperienceBottleTracking experienceBottleTracking = new ExperienceBottleTracking();
                    giveEXP = !experienceBottleTracking.fromEnchantingBottle();
                }
                if (giveEXP) {
                    increaseStats.changeEXP(skillName, expMap.get("EXPperMinecraftXPGained") * exp);
                }
            }
        }.runTaskLater(plugin, 2).getTaskId();

    }

}
