package mc.carlton.freerpg.perksAndAbilities;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.playerAndServerInfo.ChangeStats;
import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class Repair {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    private Player p;
    private String pName;
    private ItemStack itemInHand;

    ChangeStats increaseStats; //Changing Stats

    PlayerStats pStatClass;
    //GET PLAYER STATS LIKE THIS:        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData(p);

    Random rand = new Random(); //Random class Import

    Map<Material,Material> repairItems = new HashMap<>();

    Map<Material,Integer> repairItemsAmount = new HashMap<>();

    public Repair(Player p) {
        this.p = p;
        this.pName = p.getDisplayName();
        this.itemInHand = p.getInventory().getItemInMainHand();
        this.increaseStats = new ChangeStats(p);
        this.pStatClass = new PlayerStats(p);

        repairItems.put(Material.WOODEN_AXE,Material.STICK);
        repairItems.put(Material.WOODEN_HOE,Material.STICK);
        repairItems.put(Material.WOODEN_PICKAXE,Material.STICK);
        repairItems.put(Material.WOODEN_SWORD,Material.STICK);
        repairItems.put(Material.WOODEN_SHOVEL,Material.STICK);
        repairItems.put(Material.LEATHER_HELMET,Material.LEATHER);
        repairItems.put(Material.LEATHER_CHESTPLATE,Material.LEATHER);
        repairItems.put(Material.LEATHER_LEGGINGS,Material.LEATHER);
        repairItems.put(Material.LEATHER_BOOTS,Material.LEATHER);
        repairItems.put(Material.IRON_AXE,Material.IRON_INGOT);
        repairItems.put(Material.IRON_HOE,Material.IRON_INGOT);
        repairItems.put(Material.IRON_PICKAXE,Material.IRON_INGOT);
        repairItems.put(Material.IRON_SWORD,Material.IRON_INGOT);
        repairItems.put(Material.IRON_SHOVEL,Material.IRON_INGOT);
        repairItems.put(Material.IRON_HELMET,Material.IRON_INGOT);
        repairItems.put(Material.IRON_CHESTPLATE,Material.IRON_INGOT);
        repairItems.put(Material.IRON_LEGGINGS,Material.IRON_INGOT);
        repairItems.put(Material.IRON_BOOTS,Material.IRON_INGOT);
        repairItems.put(Material.GOLDEN_AXE,Material.GOLD_INGOT);
        repairItems.put(Material.GOLDEN_HOE,Material.GOLD_INGOT);
        repairItems.put(Material.GOLDEN_PICKAXE,Material.GOLD_INGOT);
        repairItems.put(Material.GOLDEN_SWORD,Material.GOLD_INGOT);
        repairItems.put(Material.GOLDEN_SHOVEL,Material.GOLD_INGOT);
        repairItems.put(Material.GOLDEN_HELMET,Material.GOLD_INGOT);
        repairItems.put(Material.GOLDEN_CHESTPLATE,Material.GOLD_INGOT);
        repairItems.put(Material.GOLDEN_LEGGINGS,Material.GOLD_INGOT);
        repairItems.put(Material.GOLDEN_BOOTS,Material.GOLD_INGOT);
        repairItems.put(Material.STONE_AXE,Material.COBBLESTONE);
        repairItems.put(Material.STONE_HOE,Material.COBBLESTONE);
        repairItems.put(Material.STONE_PICKAXE,Material.COBBLESTONE);
        repairItems.put(Material.STONE_SWORD,Material.COBBLESTONE);
        repairItems.put(Material.STONE_SHOVEL,Material.COBBLESTONE);
        repairItems.put(Material.CHAINMAIL_HELMET,Material.IRON_INGOT);
        repairItems.put(Material.CHAINMAIL_CHESTPLATE,Material.IRON_INGOT);
        repairItems.put(Material.CHAINMAIL_LEGGINGS,Material.IRON_INGOT);
        repairItems.put(Material.CHAINMAIL_BOOTS,Material.IRON_INGOT);
        repairItems.put(Material.DIAMOND_AXE,Material.DIAMOND);
        repairItems.put(Material.DIAMOND_HOE,Material.DIAMOND);
        repairItems.put(Material.DIAMOND_PICKAXE,Material.DIAMOND);
        repairItems.put(Material.DIAMOND_SWORD,Material.DIAMOND);
        repairItems.put(Material.DIAMOND_SHOVEL,Material.DIAMOND);
        repairItems.put(Material.DIAMOND_HELMET,Material.DIAMOND);
        repairItems.put(Material.DIAMOND_CHESTPLATE,Material.DIAMOND);
        repairItems.put(Material.DIAMOND_LEGGINGS,Material.DIAMOND);
        repairItems.put(Material.DIAMOND_BOOTS,Material.DIAMOND);
        repairItems.put(Material.NETHERITE_AXE,Material.NETHERITE_INGOT);
        repairItems.put(Material.NETHERITE_HOE,Material.NETHERITE_INGOT);
        repairItems.put(Material.NETHERITE_PICKAXE,Material.NETHERITE_INGOT);
        repairItems.put(Material.NETHERITE_SWORD,Material.NETHERITE_INGOT);
        repairItems.put(Material.NETHERITE_SHOVEL,Material.NETHERITE_INGOT);
        repairItems.put(Material.NETHERITE_HELMET,Material.NETHERITE_INGOT);
        repairItems.put(Material.NETHERITE_CHESTPLATE,Material.NETHERITE_INGOT);
        repairItems.put(Material.NETHERITE_LEGGINGS,Material.NETHERITE_INGOT);
        repairItems.put(Material.NETHERITE_BOOTS,Material.NETHERITE_INGOT);
        repairItems.put(Material.SHEARS,Material.IRON_INGOT);
        repairItems.put(Material.FISHING_ROD,Material.STRING);
        repairItems.put(Material.CARROT_ON_A_STICK,Material.CARROT);
        repairItems.put(Material.FLINT_AND_STEEL,Material.FLINT);
        repairItems.put(Material.BOW,Material.STRING);
        repairItems.put(Material.TRIDENT,Material.PRISMARINE_BRICKS);
        repairItems.put(Material.ELYTRA,Material.PHANTOM_MEMBRANE);
        repairItems.put(Material.SHIELD,Material.IRON_INGOT);
        repairItems.put(Material.CROSSBOW,Material.STRING);

        repairItemsAmount.put(Material.WOODEN_AXE,3);
        repairItemsAmount.put(Material.WOODEN_HOE,2);
        repairItemsAmount.put(Material.WOODEN_PICKAXE,3);
        repairItemsAmount.put(Material.WOODEN_SWORD,2);
        repairItemsAmount.put(Material.WOODEN_SHOVEL,1);
        repairItemsAmount.put(Material.LEATHER_HELMET,5);
        repairItemsAmount.put(Material.LEATHER_CHESTPLATE,8);
        repairItemsAmount.put(Material.LEATHER_LEGGINGS,7);
        repairItemsAmount.put(Material.LEATHER_BOOTS,4);
        repairItemsAmount.put(Material.IRON_AXE,3);
        repairItemsAmount.put(Material.IRON_HOE,2);
        repairItemsAmount.put(Material.IRON_PICKAXE,3);
        repairItemsAmount.put(Material.IRON_SWORD,2);
        repairItemsAmount.put(Material.IRON_SHOVEL,1);
        repairItemsAmount.put(Material.IRON_HELMET,5);
        repairItemsAmount.put(Material.IRON_CHESTPLATE,8);
        repairItemsAmount.put(Material.IRON_LEGGINGS,7);
        repairItemsAmount.put(Material.IRON_BOOTS,4);
        repairItemsAmount.put(Material.GOLDEN_AXE,3);
        repairItemsAmount.put(Material.GOLDEN_HOE,2);
        repairItemsAmount.put(Material.GOLDEN_PICKAXE,3);
        repairItemsAmount.put(Material.GOLDEN_SWORD,2);
        repairItemsAmount.put(Material.GOLDEN_SHOVEL,1);
        repairItemsAmount.put(Material.GOLDEN_HELMET,5);
        repairItemsAmount.put(Material.GOLDEN_CHESTPLATE,8);
        repairItemsAmount.put(Material.GOLDEN_LEGGINGS,7);
        repairItemsAmount.put(Material.GOLDEN_BOOTS,4);
        repairItemsAmount.put(Material.STONE_AXE,3);
        repairItemsAmount.put(Material.STONE_HOE,3);
        repairItemsAmount.put(Material.STONE_PICKAXE,3);
        repairItemsAmount.put(Material.STONE_SWORD,2);
        repairItemsAmount.put(Material.STONE_SHOVEL,1);
        repairItemsAmount.put(Material.CHAINMAIL_HELMET,5);
        repairItemsAmount.put(Material.CHAINMAIL_CHESTPLATE,8);
        repairItemsAmount.put(Material.CHAINMAIL_LEGGINGS,7);
        repairItemsAmount.put(Material.CHAINMAIL_BOOTS,4);
        repairItemsAmount.put(Material.DIAMOND_AXE,3);
        repairItemsAmount.put(Material.DIAMOND_HOE,2);
        repairItemsAmount.put(Material.DIAMOND_PICKAXE,3);
        repairItemsAmount.put(Material.DIAMOND_SWORD,2);
        repairItemsAmount.put(Material.DIAMOND_SHOVEL,1);
        repairItemsAmount.put(Material.DIAMOND_HELMET,5);
        repairItemsAmount.put(Material.DIAMOND_CHESTPLATE,8);
        repairItemsAmount.put(Material.DIAMOND_LEGGINGS,7);
        repairItemsAmount.put(Material.DIAMOND_BOOTS,4);
        repairItemsAmount.put(Material.NETHERITE_AXE,3);
        repairItemsAmount.put(Material.NETHERITE_HOE,2);
        repairItemsAmount.put(Material.NETHERITE_PICKAXE,3);
        repairItemsAmount.put(Material.NETHERITE_SWORD,2);
        repairItemsAmount.put(Material.NETHERITE_SHOVEL,1);
        repairItemsAmount.put(Material.NETHERITE_HELMET,5);
        repairItemsAmount.put(Material.NETHERITE_CHESTPLATE,8);
        repairItemsAmount.put(Material.NETHERITE_LEGGINGS,7);
        repairItemsAmount.put(Material.NETHERITE_BOOTS,4);
        repairItemsAmount.put(Material.SHEARS,2);
        repairItemsAmount.put(Material.FISHING_ROD,2);

        repairItemsAmount.put(Material.BOW,3);

        repairItemsAmount.put(Material.ELYTRA,10);
        repairItemsAmount.put(Material.SHIELD,1);
        repairItemsAmount.put(Material.CROSSBOW,2);


    }

    public boolean repairItem() {
        if (!p.hasPermission("freeRPG.canRepair")) {
            return false;
        }
        Material toolType = itemInHand.getType();
        boolean repaired = false;
        if (repairItems.containsKey(toolType)) {
            Material mats = repairItems.get(itemInHand.getType());
            if (p.getInventory().contains(mats)) {
                Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
                int repairLevel = (int) pStat.get("repair").get(4);
                int resourcefulLevel = (int) pStat.get("repair").get(9);
                double keepMatsChance = resourcefulLevel*0.1;
                double repairBonus = repairLevel*0.002;
                double repairPercentage = 0;
                int expToGive = 0;
                int expRepairMultiplier = 0;
                repaired = true;
                double a = 1.0;
                switch (toolType) {
                    case WOODEN_AXE:
                        repairPercentage  = (0.9 + repairBonus)/3.0;
                        a = 3.0;
                        expToGive += 90;
                        expRepairMultiplier = 9;
                        break;
                    case WOODEN_HOE:
                        repairPercentage  = (0.9 + repairBonus)/2.0;
                        a = 2.0;
                        expToGive += 90;
                        expRepairMultiplier = 9;
                        break;
                    case WOODEN_PICKAXE:
                        repairPercentage  = (0.9 + repairBonus)/3.0;
                        a = 3.0;
                        expToGive += 90;
                        expRepairMultiplier = 9;
                        break;
                    case WOODEN_SHOVEL:
                        repairPercentage  = (0.9 + repairBonus)/1.0;
                        a = 1.0;
                        expToGive += 90;
                        expRepairMultiplier = 9;
                        break;
                    case WOODEN_SWORD:
                        repairPercentage  = (0.9 + repairBonus)/2.0;
                        a = 2.0;
                        expToGive += 90;
                        expRepairMultiplier = 9;
                        break;
                    case LEATHER_HELMET:
                        repairPercentage  = (0.9 + repairBonus)/5.0;
                        a = 5.0;
                        expToGive += 90;
                        expRepairMultiplier = 9;
                        break;
                    case LEATHER_CHESTPLATE:
                        repairPercentage  = (0.9 + repairBonus)/8.0;
                        a = 8.0;
                        expToGive += 90;
                        expRepairMultiplier = 9;
                        break;
                    case LEATHER_LEGGINGS:
                        repairPercentage  = (0.9 + repairBonus)/7.0;
                        a = 7.0;
                        expToGive += 90;
                        expRepairMultiplier = 9;
                        break;
                    case LEATHER_BOOTS:
                        repairPercentage  = (0.9 + repairBonus)/4.0;
                        a = 4.0;
                        expToGive += 90;
                        expRepairMultiplier = 9;
                        break;
                    case STONE_AXE:
                        repairPercentage  = (0.8 + repairBonus)/3.0;
                        a = 3.0;
                        expToGive += 75;
                        expRepairMultiplier = 10;
                        break;
                    case STONE_HOE:
                        repairPercentage  = (0.8 + repairBonus)/2.0;
                        a = 2.0;
                        expToGive += 75;
                        expRepairMultiplier = 10;
                        break;
                    case STONE_PICKAXE:
                        repairPercentage  = (0.8 + repairBonus)/3.0;
                        a = 3.0;
                        expToGive += 75;
                        expRepairMultiplier = 10;
                        break;
                    case STONE_SHOVEL:
                        repairPercentage  = (0.8 + repairBonus)/1.0;
                        a = 1.0;
                        expToGive += 75;
                        expRepairMultiplier = 10;
                        break;
                    case STONE_SWORD:
                        repairPercentage  = (0.8 + repairBonus)/2.0;
                        a = 2.0;
                        expToGive += 75;
                        expRepairMultiplier = 10;
                        break;
                    case CHAINMAIL_HELMET:
                        repairPercentage  = (0.11 + repairBonus)/5.0;
                        a = 5.0;
                        expToGive += 180;
                        expRepairMultiplier = 11;
                        break;
                    case CHAINMAIL_CHESTPLATE:
                        repairPercentage  = (0.11 + repairBonus)/8.0;
                        a = 8.0;
                        expToGive += 180;
                        expRepairMultiplier = 11;
                        break;
                    case CHAINMAIL_LEGGINGS:
                        repairPercentage  = (0.11 + repairBonus)/11.0;
                        a = 11.0;
                        expToGive += 180;
                        expRepairMultiplier = 11;
                        break;
                    case CHAINMAIL_BOOTS:
                        repairPercentage  = (0.11 + repairBonus)/4.0;
                        a = 4.0;
                        expToGive += 180;
                        expRepairMultiplier = 11;
                        break;
                    case GOLDEN_AXE:
                        repairPercentage  = (0.7 + repairBonus)/3.0;
                        a = 3.0;
                        expToGive += 300;
                        expRepairMultiplier = 25;
                        break;
                    case GOLDEN_HOE:
                        repairPercentage  = (0.7 + repairBonus)/2.0;
                        a = 2.0;
                        expToGive += 300;
                        expRepairMultiplier = 25;
                        break;
                    case GOLDEN_PICKAXE:
                        repairPercentage  = (0.7 + repairBonus)/3.0;
                        a = 3.0;
                        expToGive += 300;
                        expRepairMultiplier = 25;
                        break;
                    case GOLDEN_SHOVEL:
                        repairPercentage  = (0.7 + repairBonus)/1.0;
                        a = 1.0;
                        expToGive += 300;
                        expRepairMultiplier = 25;
                        break;
                    case GOLDEN_SWORD:
                        repairPercentage  = (0.7 + repairBonus)/2.0;
                        a = 2.0;
                        expToGive += 300;
                        expRepairMultiplier = 25;
                        break;
                    case GOLDEN_HELMET:
                        repairPercentage  = (0.7 + repairBonus)/5.0;
                        a = 5.0;
                        expToGive += 300;
                        expRepairMultiplier = 25;
                        break;
                    case GOLDEN_CHESTPLATE:
                        repairPercentage  = (0.7 + repairBonus)/8.0;
                        a = 8.0;
                        expToGive += 300;
                        expRepairMultiplier = 25;
                        break;
                    case GOLDEN_LEGGINGS:
                        repairPercentage  = (0.7 + repairBonus)/7.0;
                        a = 7.0;
                        expToGive += 300;
                        expRepairMultiplier = 25;
                        break;
                    case GOLDEN_BOOTS:
                        repairPercentage  = (0.7 + repairBonus)/4.0;
                        a = 4.0;
                        expToGive += 300;
                        expRepairMultiplier = 25;
                        break;
                    case IRON_AXE:
                        repairPercentage  = (0.5 + repairBonus)/3.0;
                        a = 3.0;
                        expToGive += 300;
                        expRepairMultiplier = 18;
                        break;
                    case IRON_HOE:
                        repairPercentage  = (0.5 + repairBonus)/2.0;
                        a = 2.0;
                        expToGive += 300;
                        expRepairMultiplier = 18;
                        break;
                    case IRON_PICKAXE:
                        repairPercentage  = (0.5 + repairBonus)/3.0;
                        a = 3.0;
                        expToGive += 300;
                        expRepairMultiplier = 18;
                        break;
                    case IRON_SHOVEL:
                        repairPercentage  = (0.5 + repairBonus)/1.0;
                        a = 1.0;
                        expToGive += 300;
                        expRepairMultiplier = 18;
                        break;
                    case IRON_SWORD:
                        repairPercentage  = (0.5 + repairBonus)/2.0;
                        a = 2.0;
                        expToGive += 300;
                        expRepairMultiplier = 18;
                        break;
                    case IRON_HELMET:
                        repairPercentage  = (0.5 + repairBonus)/5.0;
                        a = 5.0;
                        expToGive += 300;
                        expRepairMultiplier = 18;
                        break;
                    case IRON_CHESTPLATE:
                        repairPercentage  = (0.5 + repairBonus)/8.0;
                        a = 8.0;
                        expToGive += 300;
                        expRepairMultiplier = 18;
                        break;
                    case IRON_LEGGINGS:
                        repairPercentage  = (0.5 + repairBonus)/7.0;
                        a = 7.0;
                        expToGive += 300;
                        expRepairMultiplier = 18;
                        break;
                    case IRON_BOOTS:
                        repairPercentage  = (0.5 + repairBonus)/4.0;
                        a = 4.0;
                        expToGive += 300;
                        expRepairMultiplier = 18;
                        break;
                    case DIAMOND_AXE:
                        repairPercentage  = (0.00 + repairBonus)/3.0;
                        a = 3.0;
                        expToGive += 750;
                        expRepairMultiplier = 20;
                        break;
                    case DIAMOND_HOE:
                        repairPercentage  = (0.00 + repairBonus)/2.0;
                        a = 2.0;
                        expToGive += 750;
                        expRepairMultiplier = 20;
                        break;
                    case DIAMOND_PICKAXE:
                        repairPercentage  = (0.00 + repairBonus)/3.0;
                        a = 3.0;
                        expToGive += 750;
                        expRepairMultiplier = 20;
                        break;
                    case DIAMOND_SHOVEL:
                        repairPercentage  = (0.00 + repairBonus)/1.0;
                        a = 1.0;
                        expToGive += 750;
                        expRepairMultiplier = 20;
                        break;
                    case DIAMOND_SWORD:
                        repairPercentage  = (0.00 + repairBonus)/2.0;
                        a = 2.0;
                        expToGive += 750;
                        expRepairMultiplier = 20;
                        break;
                    case DIAMOND_HELMET:
                        repairPercentage  = (0.00 + repairBonus)/5.0;
                        a = 5.0;
                        expToGive += 750;
                        expRepairMultiplier = 20;
                        break;
                    case DIAMOND_CHESTPLATE:
                        repairPercentage  = (0.00 + repairBonus)/8.0;
                        a = 8.0;
                        expToGive += 750;
                        expRepairMultiplier = 20;
                        break;
                    case DIAMOND_LEGGINGS:
                        repairPercentage  = (0.00 + repairBonus)/7.0;
                        a = 7.0;
                        expToGive += 750;
                        expRepairMultiplier = 20;
                        break;
                    case DIAMOND_BOOTS:
                        repairPercentage  = (0.0 + repairBonus)/4.0;
                        a = 4.0;
                        expToGive += 750;
                        expRepairMultiplier = 20;
                        break;
                    case NETHERITE_AXE:
                        repairPercentage  = (0.00 + repairBonus*0.5)/3.0;
                        a = 3.0;
                        expToGive += 1500;
                        expRepairMultiplier = 25;
                        break;
                    case NETHERITE_HOE:
                        repairPercentage  = (0.00 + repairBonus*0.5)/2.0;
                        a = 2.0;
                        expToGive += 1500;
                        expRepairMultiplier = 25;
                        break;
                    case NETHERITE_PICKAXE:
                        repairPercentage  = (0.00 + repairBonus*0.5)/3.0;
                        a = 3.0;
                        expToGive += 1500;
                        expRepairMultiplier = 25;
                        break;
                    case NETHERITE_SHOVEL:
                        repairPercentage  = (0.00 + repairBonus*0.5)/1.0;
                        a = 1.0;
                        expToGive += 1500;
                        expRepairMultiplier = 25;
                        break;
                    case NETHERITE_SWORD:
                        repairPercentage  = (0.00 + repairBonus*0.5)/2.0;
                        a = 2.0;
                        expToGive += 1500;
                        expRepairMultiplier = 25;
                        break;
                    case NETHERITE_HELMET:
                        repairPercentage  = (0.00 + repairBonus*0.5)/5.0;
                        a = 5.0;
                        expToGive += 1500;
                        expRepairMultiplier = 25;
                        break;
                    case NETHERITE_CHESTPLATE:
                        repairPercentage  = (0.00 + repairBonus*0.5)/8.0;
                        a = 8.0;
                        expToGive += 1500;
                        expRepairMultiplier = 25;
                        break;
                    case NETHERITE_LEGGINGS:
                        repairPercentage  = (0.00 + repairBonus*0.5)/7.0;
                        a = 7.0;
                        expToGive += 1500;
                        expRepairMultiplier = 25;
                        break;
                    case NETHERITE_BOOTS:
                        repairPercentage  = (0.0 + repairBonus*0.5)/4.0;
                        a = 4.0;
                        expToGive += 1500;
                        expRepairMultiplier = 25;
                        break;
                    case SHEARS:
                        repairPercentage  = (0.5 + repairBonus)/2.0;
                        a = 2.0;
                        expToGive += 300;
                        expRepairMultiplier = 18;
                        break;
                    case FISHING_ROD:
                        repairPercentage  = (0.5 + repairBonus)/2.0;
                        a = 2.0;
                        expToGive += 500;
                        expRepairMultiplier = 25;
                        break;
                    case CARROT_ON_A_STICK:
                        repairPercentage  = (0.8 + repairBonus)/1.0;
                        a = 1.0;
                        expToGive += 500;
                        expRepairMultiplier = 25;
                        break;
                    case FLINT_AND_STEEL:
                        repairPercentage  = (0.8 + repairBonus)/1.0;
                        a = 1.0;
                        expToGive += 500;
                        expRepairMultiplier = 25;
                        break;
                    case BOW:
                        repairPercentage  = (0.8 + repairBonus)/3.0;
                        a = 3.0;
                        expToGive += 300;
                        expRepairMultiplier = 18;
                        break;
                    case TRIDENT:
                        repairPercentage  = (0.8 + repairBonus)/3.0;
                        a = 1.0;
                        expRepairMultiplier = 18;
                        break;
                    case ELYTRA:
                        repairPercentage  = (0.8 + repairBonus)/3.0;
                        a = 1.0;
                        expToGive += 500;
                        expRepairMultiplier = 18;
                        break;
                    case SHIELD:
                        repairPercentage  = (0.8 + repairBonus)/3.0;
                        a = 1.0;
                        expToGive += 300;
                        expRepairMultiplier = 18;
                        break;
                    case CROSSBOW:
                        repairPercentage  = (0.8 + repairBonus)/3.0;
                        a = 3.0;
                        expToGive += 300;
                        expRepairMultiplier = 18;
                        break;
                    default:
                        break;
                }
                ItemMeta itemInHandMeta = itemInHand.getItemMeta();
                if (repairPercentage*a < 0.2) {
                    p.sendMessage(ChatColor.RED + "You are not skilled enough to adequately repair this item yet");
                    repaired = false;
                }
                else {
                    if (itemInHandMeta instanceof Damageable) {
                        int currentDamage = ((Damageable) itemInHandMeta).getDamage();
                        if (currentDamage == 0) {
                            return false;
                        }
                        int mats_index = p.getInventory().first(mats);
                        int mats_amount = p.getInventory().getItem(mats_index).getAmount();
                        if (keepMatsChance < rand.nextDouble()) {
                            p.getInventory().getItem(mats_index).setAmount(mats_amount - 1);
                        }
                        int maxDamage = itemInHand.getType().getMaxDurability();
                        int repairedDamage = (int) Math.round(maxDamage*repairPercentage);
                        int expDamage = (int) a*Math.min(repairedDamage,currentDamage);
                        ((Damageable) itemInHandMeta).setDamage(Math.max(0,currentDamage-repairedDamage));
                        itemInHand.setItemMeta(itemInHandMeta);
                        int enchantEXP = magicRepair();
                        increaseStats.changeEXP("repair",enchantEXP+expToGive+(expRepairMultiplier*expDamage));
                        p.getWorld().playEffect(p.getLocation(), Effect.ANVIL_USE,1);
                    }
                }
            }
        }
        return repaired;
    }

    public void salvaging() {
        if (!p.hasPermission("freeRPG.canSalvage")) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int salvageLevel = (int) pStat.get("repair").get(7);
        if (repairItemsAmount.containsKey(itemInHand.getType())) {
            p.getWorld().playEffect(p.getLocation(), Effect.ANVIL_USE,1);
            ItemMeta itemInHandMeta = itemInHand.getItemMeta();
            Material itemType = itemInHand.getType();
            int amount = repairItemsAmount.get(itemType);
            Material type = repairItems.get(itemType);
            p.getInventory().getItemInMainHand().setAmount(0);
            double multiplier = 0.1*salvageLevel + (1 - 0.1*salvageLevel)*rand.nextDouble();
            double currentDamage = ((Damageable) itemInHandMeta).getDamage();
            double maxDamage = itemType.getMaxDurability();
            double percentDurability = (1 - (currentDamage/maxDamage));

            double amountToReturn_pre = amount*percentDurability*multiplier;
            int amountToReturn = (int) Math.round(amountToReturn_pre);
            if (amountToReturn == 0){
                if (amountToReturn_pre > rand.nextDouble()) {
                    p.getInventory().addItem(new ItemStack(type,1));
                }
                else {
                    p.sendMessage(ChatColor.RED + "You failed to salvage any materials");
                }
            }
            else {
                p.getInventory().addItem(new ItemStack(type,amountToReturn));
            }
            if (salvageLevel >= 5) {
                if (itemInHandMeta.hasEnchants()) {
                    ItemStack enchantedBook = new ItemStack(Material.ENCHANTED_BOOK,1);
                    EnchantmentStorageMeta meta = ((EnchantmentStorageMeta) enchantedBook.getItemMeta());
                    Map<Enchantment,Integer> enchants = itemInHandMeta.getEnchants();
                    for (Enchantment enchant : enchants.keySet()) {
                        meta.addStoredEnchant(enchant,enchants.get(enchant),true);
                    }
                    enchantedBook.setItemMeta(meta);
                    if (p.getInventory().firstEmpty() == -1) {
                        World world = p.getWorld();
                        world.dropItemNaturally(p.getLocation().add(0,0.5,0),enchantedBook);
                    }
                    else {
                        p.getInventory().addItem(enchantedBook);
                    }
                }
            }

            switch (type) {
                case STICK:
                    increaseStats.changeEXP("repair",400);
                    break;
                case LEATHER:
                    increaseStats.changeEXP("repair",500);
                    break;
                case COBBLESTONE:
                    increaseStats.changeEXP("repair",700);
                    break;
                case IRON_INGOT:
                    increaseStats.changeEXP("repair",1500);
                    break;
                case GOLD_INGOT:
                    increaseStats.changeEXP("repair",2000);
                    break;
                case STRING:
                    increaseStats.changeEXP("repair",1000);
                    break;
                case PHANTOM_MEMBRANE:
                    increaseStats.changeEXP("repair",4000);
                    break;
                case DIAMOND:
                    increaseStats.changeEXP("repair",6000);
                    break;
                case NETHERITE_INGOT:
                    increaseStats.changeEXP("repair",10000);
                default:
                    break;

            }
        }
    }

    public int magicRepair() {
        if (itemInHand.getEnchantments().size() == 0) {
            return 0;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        double sucessChance = 0;
        if ((int) pStat.get("repair").get(13) > 0) {
            sucessChance = 1;
        }
        if (sucessChance < rand.nextDouble()) {
            p.sendMessage(ChatColor.RED + "You failed to retain the item's enchantment power");
            Map<Enchantment,Integer> enchantments = itemInHand.getEnchantments();
            for (Enchantment enchantment : enchantments.keySet()) {
                int level = enchantments.get(enchantment);
                itemInHand.removeEnchantment(enchantment);
                if (level > 1) {
                    itemInHand.addEnchantment(enchantment,level-1);
                }
            }
        }
        else {
            return 1000;
        }
        return 0;
    }
}
