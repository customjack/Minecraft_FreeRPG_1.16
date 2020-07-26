package mc.carlton.freerpg.perksAndAbilities;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.PsuedoEnchanting;
import mc.carlton.freerpg.playerAndServerInfo.ChangeStats;
import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
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

    ChangeStats increaseStats; //Changing Stats

    PlayerStats pStatClass;
    //GET PLAYER STATS LIKE THIS:        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData(p);

    Random rand = new Random(); //Random class Import
    static Map<Player,EnchantmentOffer[]> offersHolder = new HashMap<Player, EnchantmentOffer[]>();



    public Enchanting(Player p) {
        this.p = p;
        this.pName = p.getDisplayName();
        this.itemInHand = p.getInventory().getItemInMainHand();
        this.increaseStats = new ChangeStats(p);
        this.pStatClass = new PlayerStats(p);
    }

    public int xpIncrease(int oldAmount) {
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int expBuffLevel = (int) pStat.get("enchanting").get(4);
        double multiplier = 1 + expBuffLevel*0.002;
        int newAmount = (int) Math.floor(oldAmount*multiplier);
        double roundUpChance = oldAmount*multiplier - Math.floor(oldAmount*multiplier);
        if (roundUpChance > rand.nextDouble()) {
            newAmount += 1;
        }
        return newAmount;
    }

    public EnchantmentOffer[] enchantmentDiscount(EnchantmentOffer[] oldOffers) {
        if (oldOffers.length == 0) {
            return oldOffers;
        }
        offersHolder.put(p,oldOffers.clone());
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int levelSubtract = (int) pStat.get("enchanting").get(7);
        for (EnchantmentOffer offer : oldOffers) {
            if (offer != null) {
                int newCost = Math.max(offer.getCost() - levelSubtract, 1);
                offer.setCost(newCost);
            }
        }
        return oldOffers;
    }

    public void enchantItem(ItemStack enchantedItem, int buttonClicked, EnchantingInventory enchantingInventory) {
        EnchantmentOffer[] originalOffers = offersHolder.get(p);
        Enchantment enchant0 = originalOffers[buttonClicked].getEnchantment();
        int level0 = originalOffers[buttonClicked].getEnchantmentLevel();
        int cost0 = originalOffers[buttonClicked].getCost();
        increaseStats.changeEXP("enchanting",500*cost0);
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
        increaseStats.changeEXP("enchanting",25*exp);
    }

}
