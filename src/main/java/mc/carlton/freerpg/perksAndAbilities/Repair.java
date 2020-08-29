package mc.carlton.freerpg.perksAndAbilities;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.ActionBarMessages;
import mc.carlton.freerpg.gameTools.LanguageSelector;
import mc.carlton.freerpg.globalVariables.ItemGroups;
import mc.carlton.freerpg.playerAndServerInfo.ChangeStats;
import mc.carlton.freerpg.playerAndServerInfo.ConfigLoad;
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
    private String skillName = "repair";
    Map<String,Integer> expMap;

    ChangeStats increaseStats; //Changing Stats

    PlayerStats pStatClass;
    //GET PLAYER STATS LIKE THIS:        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData(p);

    ActionBarMessages actionMessage;
    LanguageSelector lang;

    Random rand = new Random(); //Random class Import

    private boolean runMethods;

    public Repair(Player p) {
        this.p = p;
        this.pName = p.getDisplayName();
        this.itemInHand = p.getInventory().getItemInMainHand();
        this.increaseStats = new ChangeStats(p);
        this.pStatClass = new PlayerStats(p);
        this.actionMessage = new ActionBarMessages(p);
        this.lang = new LanguageSelector(p);
        ConfigLoad configLoad = new ConfigLoad();
        this.runMethods = configLoad.getAllowedSkillsMap().get(skillName);
        expMap = configLoad.getExpMapForSkill(skillName);

    }

    public boolean repairItem() {
        if (!runMethods) {
            return false;
        }
        if (!p.hasPermission("freeRPG.canRepair")) {
            return false;
        }
        Material toolType = itemInHand.getType();
        boolean repaired = false;
        ItemGroups itemGroups = new ItemGroups();
        Map<Material,Material> repairItems = itemGroups.getRepairItems();
        if (repairItems.containsKey(toolType)) {
            Material mats = repairItems.get(itemInHand.getType());
            if (p.getInventory().contains(mats)) {
                Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
                int repairLevel = (int) pStat.get(skillName).get(4);
                int resourcefulLevel = (int) pStat.get(skillName).get(9);
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
                        expToGive += expMap.get("wooden_baseEXP");
                        expRepairMultiplier = expMap.get("wooden_EXPMultiplier");
                        break;
                    case WOODEN_HOE:
                        repairPercentage  = (0.9 + repairBonus)/2.0;
                        a = 2.0;
                        expToGive += expMap.get("wooden_baseEXP");
                        expRepairMultiplier = expMap.get("wooden_EXPMultiplier");
                        break;
                    case WOODEN_PICKAXE:
                        repairPercentage  = (0.9 + repairBonus)/3.0;
                        a = 3.0;
                        expToGive += expMap.get("wooden_baseEXP");
                        expRepairMultiplier = expMap.get("wooden_EXPMultiplier");
                        break;
                    case WOODEN_SHOVEL:
                        repairPercentage  = (0.9 + repairBonus)/1.0;
                        a = 1.0;
                        expToGive += expMap.get("wooden_baseEXP");
                        expRepairMultiplier = expMap.get("wooden_EXPMultiplier");
                        break;
                    case WOODEN_SWORD:
                        repairPercentage  = (0.9 + repairBonus)/2.0;
                        a = 2.0;
                        expToGive += expMap.get("wooden_baseEXP");
                        expRepairMultiplier = expMap.get("wooden_EXPMultiplier");
                        break;
                    case LEATHER_HELMET:
                        repairPercentage  = (0.9 + repairBonus)/5.0;
                        a = 5.0;
                        expToGive += expMap.get("leather_baseEXP");
                        expRepairMultiplier = expMap.get("leather_EXPMultiplier");
                        break;
                    case LEATHER_CHESTPLATE:
                        repairPercentage  = (0.9 + repairBonus)/8.0;
                        a = 8.0;
                        expToGive += expMap.get("leather_baseEXP");
                        expRepairMultiplier = expMap.get("leather_EXPMultiplier");
                        break;
                    case LEATHER_LEGGINGS:
                        repairPercentage  = (0.9 + repairBonus)/7.0;
                        a = 7.0;
                        expToGive += expMap.get("leather_baseEXP");
                        expRepairMultiplier = expMap.get("leather_EXPMultiplier");
                        break;
                    case LEATHER_BOOTS:
                        repairPercentage  = (0.9 + repairBonus)/4.0;
                        a = 4.0;
                        expToGive += expMap.get("leather_baseEXP");
                        expRepairMultiplier = expMap.get("leather_EXPMultiplier");
                        break;
                    case STONE_AXE:
                        repairPercentage  = (0.8 + repairBonus)/3.0;
                        a = 3.0;
                        expToGive += expMap.get("stone_baseEXP");
                        expRepairMultiplier = expMap.get("stone_EXPMultiplier");
                        break;
                    case STONE_HOE:
                        repairPercentage  = (0.8 + repairBonus)/2.0;
                        a = 2.0;
                        expToGive += expMap.get("stone_baseEXP");
                        expRepairMultiplier = expMap.get("stone_EXPMultiplier");
                        break;
                    case STONE_PICKAXE:
                        repairPercentage  = (0.8 + repairBonus)/3.0;
                        a = 3.0;
                        expToGive += expMap.get("stone_baseEXP");
                        expRepairMultiplier = expMap.get("stone_EXPMultiplier");
                        break;
                    case STONE_SHOVEL:
                        repairPercentage  = (0.8 + repairBonus)/1.0;
                        a = 1.0;
                        expToGive += expMap.get("stone_baseEXP");
                        expRepairMultiplier = expMap.get("stone_EXPMultiplier");
                        break;
                    case STONE_SWORD:
                        repairPercentage  = (0.8 + repairBonus)/2.0;
                        a = 2.0;
                        expToGive += expMap.get("stone_baseEXP");
                        expRepairMultiplier = expMap.get("stone_EXPMultiplier");
                        break;
                    case CHAINMAIL_HELMET:
                        repairPercentage  = (0.11 + repairBonus)/5.0;
                        a = 5.0;
                        expToGive += expMap.get("chainmail_baseEXP");
                        expRepairMultiplier = expMap.get("chainmail_EXPMultiplier");
                        break;
                    case CHAINMAIL_CHESTPLATE:
                        repairPercentage  = (0.11 + repairBonus)/8.0;
                        a = 8.0;
                        expToGive += expMap.get("chainmail_baseEXP");
                        expRepairMultiplier = expMap.get("chainmail_EXPMultiplier");
                        break;
                    case CHAINMAIL_LEGGINGS:
                        repairPercentage  = (0.11 + repairBonus)/11.0;
                        a = 11.0;
                        expToGive += expMap.get("chainmail_baseEXP");
                        expRepairMultiplier = expMap.get("chainmail_EXPMultiplier");
                        break;
                    case CHAINMAIL_BOOTS:
                        repairPercentage  = (0.11 + repairBonus)/4.0;
                        a = 4.0;
                        expToGive += expMap.get("chainmail_baseEXP");
                        expRepairMultiplier = expMap.get("chainmail_EXPMultiplier");
                        break;
                    case GOLDEN_AXE:
                        repairPercentage  = (0.7 + repairBonus)/3.0;
                        a = 3.0;
                        expToGive += expMap.get("gold_baseEXP");
                        expRepairMultiplier = expMap.get("gold_EXPMultiplier");
                        break;
                    case GOLDEN_HOE:
                        repairPercentage  = (0.7 + repairBonus)/2.0;
                        a = 2.0;
                        expToGive += expMap.get("gold_baseEXP");
                        expRepairMultiplier = expMap.get("gold_EXPMultiplier");
                        break;
                    case GOLDEN_PICKAXE:
                        repairPercentage  = (0.7 + repairBonus)/3.0;
                        a = 3.0;
                        expToGive += expMap.get("gold_baseEXP");
                        expRepairMultiplier = expMap.get("gold_EXPMultiplier");
                        break;
                    case GOLDEN_SHOVEL:
                        repairPercentage  = (0.7 + repairBonus)/1.0;
                        a = 1.0;
                        expToGive += expMap.get("gold_baseEXP");
                        expRepairMultiplier = expMap.get("gold_EXPMultiplier");
                        break;
                    case GOLDEN_SWORD:
                        repairPercentage  = (0.7 + repairBonus)/2.0;
                        a = 2.0;
                        expToGive += expMap.get("gold_baseEXP");
                        expRepairMultiplier = expMap.get("gold_EXPMultiplier");
                        break;
                    case GOLDEN_HELMET:
                        repairPercentage  = (0.7 + repairBonus)/5.0;
                        a = 5.0;
                        expToGive += expMap.get("gold_baseEXP");
                        expRepairMultiplier = expMap.get("gold_EXPMultiplier");
                        break;
                    case GOLDEN_CHESTPLATE:
                        repairPercentage  = (0.7 + repairBonus)/8.0;
                        a = 8.0;
                        expToGive += expMap.get("gold_baseEXP");
                        expRepairMultiplier = expMap.get("gold_EXPMultiplier");
                        break;
                    case GOLDEN_LEGGINGS:
                        repairPercentage  = (0.7 + repairBonus)/7.0;
                        a = 7.0;
                        expToGive += expMap.get("gold_baseEXP");
                        expRepairMultiplier = expMap.get("gold_EXPMultiplier");
                        break;
                    case GOLDEN_BOOTS:
                        repairPercentage  = (0.7 + repairBonus)/4.0;
                        a = 4.0;
                        expToGive += expMap.get("gold_baseEXP");
                        expRepairMultiplier = expMap.get("gold_EXPMultiplier");
                        break;
                    case IRON_AXE:
                        repairPercentage  = (0.5 + repairBonus)/3.0;
                        a = 3.0;
                        expToGive += expMap.get("iron_baseEXP");
                        expRepairMultiplier = expMap.get("iron_EXPMultiplier");
                        break;
                    case IRON_HOE:
                        repairPercentage  = (0.5 + repairBonus)/2.0;
                        a = 2.0;
                        expToGive += expMap.get("iron_baseEXP");
                        expRepairMultiplier = expMap.get("iron_EXPMultiplier");
                        break;
                    case IRON_PICKAXE:
                        repairPercentage  = (0.5 + repairBonus)/3.0;
                        a = 3.0;
                        expToGive += expMap.get("iron_baseEXP");
                        expRepairMultiplier = expMap.get("iron_EXPMultiplier");
                        break;
                    case IRON_SHOVEL:
                        repairPercentage  = (0.5 + repairBonus)/1.0;
                        a = 1.0;
                        expToGive += expMap.get("iron_baseEXP");
                        expRepairMultiplier = expMap.get("iron_EXPMultiplier");
                        break;
                    case IRON_SWORD:
                        repairPercentage  = (0.5 + repairBonus)/2.0;
                        a = 2.0;
                        expToGive += expMap.get("iron_baseEXP");
                        expRepairMultiplier = expMap.get("iron_EXPMultiplier");
                        break;
                    case IRON_HELMET:
                        repairPercentage  = (0.5 + repairBonus)/5.0;
                        a = 5.0;
                        expToGive += expMap.get("iron_baseEXP");
                        expRepairMultiplier = expMap.get("iron_EXPMultiplier");
                        break;
                    case IRON_CHESTPLATE:
                        repairPercentage  = (0.5 + repairBonus)/8.0;
                        a = 8.0;
                        expToGive += expMap.get("iron_baseEXP");
                        expRepairMultiplier = expMap.get("iron_EXPMultiplier");
                        break;
                    case IRON_LEGGINGS:
                        repairPercentage  = (0.5 + repairBonus)/7.0;
                        a = 7.0;
                        expToGive += expMap.get("iron_baseEXP");
                        expRepairMultiplier = expMap.get("iron_EXPMultiplier");
                        break;
                    case IRON_BOOTS:
                        repairPercentage  = (0.5 + repairBonus)/4.0;
                        a = 4.0;
                        expToGive += expMap.get("iron_baseEXP");
                        expRepairMultiplier = expMap.get("iron_EXPMultiplier");
                        break;
                    case DIAMOND_AXE:
                        repairPercentage  = (0.00 + repairBonus)/3.0;
                        a = 3.0;
                        expToGive += expMap.get("diamond_baseEXP");
                        expRepairMultiplier = expMap.get("diamond_EXPMultiplier");
                        break;
                    case DIAMOND_HOE:
                        repairPercentage  = (0.00 + repairBonus)/2.0;
                        a = 2.0;
                        expToGive += expMap.get("diamond_baseEXP");
                        expRepairMultiplier = expMap.get("diamond_EXPMultiplier");
                        break;
                    case DIAMOND_PICKAXE:
                        repairPercentage  = (0.00 + repairBonus)/3.0;
                        a = 3.0;
                        expToGive += expMap.get("diamond_baseEXP");
                        expRepairMultiplier = expMap.get("diamond_EXPMultiplier");
                        break;
                    case DIAMOND_SHOVEL:
                        repairPercentage  = (0.00 + repairBonus)/1.0;
                        a = 1.0;
                        expToGive += expMap.get("diamond_baseEXP");
                        expRepairMultiplier = expMap.get("diamond_EXPMultiplier");
                        break;
                    case DIAMOND_SWORD:
                        repairPercentage  = (0.00 + repairBonus)/2.0;
                        a = 2.0;
                        expToGive += expMap.get("diamond_baseEXP");
                        expRepairMultiplier = expMap.get("diamond_EXPMultiplier");
                        break;
                    case DIAMOND_HELMET:
                        repairPercentage  = (0.00 + repairBonus)/5.0;
                        a = 5.0;
                        expToGive += expMap.get("diamond_baseEXP");
                        expRepairMultiplier = expMap.get("diamond_EXPMultiplier");
                        break;
                    case DIAMOND_CHESTPLATE:
                        repairPercentage  = (0.00 + repairBonus)/8.0;
                        a = 8.0;
                        expToGive += expMap.get("diamond_baseEXP");
                        expRepairMultiplier = expMap.get("diamond_EXPMultiplier");
                        break;
                    case DIAMOND_LEGGINGS:
                        repairPercentage  = (0.00 + repairBonus)/7.0;
                        a = 7.0;
                        expToGive += expMap.get("diamond_baseEXP");
                        expRepairMultiplier = expMap.get("diamond_EXPMultiplier");
                        break;
                    case DIAMOND_BOOTS:
                        repairPercentage  = (0.0 + repairBonus)/4.0;
                        a = 4.0;
                        expToGive += expMap.get("diamond_baseEXP");
                        expRepairMultiplier = expMap.get("diamond_EXPMultiplier");
                        break;
                    case NETHERITE_AXE:
                        repairPercentage  = (0.00 + repairBonus*0.5)/3.0;
                        a = 3.0;
                        expToGive += expMap.get("netherite_baseEXP");
                        expRepairMultiplier = expMap.get("netherite_EXPMultiplier");
                        break;
                    case NETHERITE_HOE:
                        repairPercentage  = (0.00 + repairBonus*0.5)/2.0;
                        a = 2.0;
                        expToGive += expMap.get("netherite_baseEXP");
                        expRepairMultiplier = expMap.get("netherite_EXPMultiplier");
                        break;
                    case NETHERITE_PICKAXE:
                        repairPercentage  = (0.00 + repairBonus*0.5)/3.0;
                        a = 3.0;
                        expToGive += expMap.get("netherite_baseEXP");
                        expRepairMultiplier = expMap.get("netherite_EXPMultiplier");
                        break;
                    case NETHERITE_SHOVEL:
                        repairPercentage  = (0.00 + repairBonus*0.5)/1.0;
                        a = 1.0;
                        expToGive += expMap.get("netherite_baseEXP");
                        expRepairMultiplier = expMap.get("netherite_EXPMultiplier");
                        break;
                    case NETHERITE_SWORD:
                        repairPercentage  = (0.00 + repairBonus*0.5)/2.0;
                        a = 2.0;
                        expToGive += expMap.get("netherite_baseEXP");
                        expRepairMultiplier = expMap.get("netherite_EXPMultiplier");
                        break;
                    case NETHERITE_HELMET:
                        repairPercentage  = (0.00 + repairBonus*0.5)/5.0;
                        a = 5.0;
                        expToGive += expMap.get("netherite_baseEXP");
                        expRepairMultiplier = expMap.get("netherite_EXPMultiplier");
                        break;
                    case NETHERITE_CHESTPLATE:
                        repairPercentage  = (0.00 + repairBonus*0.5)/8.0;
                        a = 8.0;
                        expToGive += expMap.get("netherite_baseEXP");
                        expRepairMultiplier = expMap.get("netherite_EXPMultiplier");
                        break;
                    case NETHERITE_LEGGINGS:
                        repairPercentage  = (0.00 + repairBonus*0.5)/7.0;
                        a = 7.0;
                        expToGive += expMap.get("netherite_baseEXP");
                        expRepairMultiplier = expMap.get("netherite_EXPMultiplier");
                        break;
                    case NETHERITE_BOOTS:
                        repairPercentage  = (0.0 + repairBonus*0.5)/4.0;
                        a = 4.0;
                        expToGive += expMap.get("netherite_baseEXP");
                        expRepairMultiplier = expMap.get("netherite_EXPMultiplier");
                        break;
                    case SHEARS:
                        repairPercentage  = (0.5 + repairBonus)/2.0;
                        a = 2.0;
                        expToGive += expMap.get("shears_baseEXP");
                        expRepairMultiplier = expMap.get("shear_expMultiplier");
                        break;
                    case FISHING_ROD:
                        repairPercentage  = (0.5 + repairBonus)/2.0;
                        a = 2.0;
                        expToGive += expMap.get("fishingRod_baseEXP");
                        expRepairMultiplier = expMap.get("fishingRod_expMultiplier");
                        break;
                    case CARROT_ON_A_STICK:
                        repairPercentage  = (0.8 + repairBonus)/1.0;
                        a = 1.0;
                        expToGive += expMap.get("carrotOnAStick_baseEXP");
                        expRepairMultiplier = expMap.get("carrotOnAStick_expMultiplier");
                        break;
                    case FLINT_AND_STEEL:
                        repairPercentage  = (0.8 + repairBonus)/1.0;
                        a = 1.0;
                        expToGive += expMap.get("flintAndSteel_baseEXP");
                        expRepairMultiplier = expMap.get("flintAndSteel_expMultiplier");
                        break;
                    case BOW:
                        repairPercentage  = (0.8 + repairBonus)/3.0;
                        a = 3.0;
                        expToGive += expMap.get("bow_baseEXP");
                        expRepairMultiplier = expMap.get("bow_expMultiplier");
                        break;
                    case TRIDENT:
                        repairPercentage  = (0.8 + repairBonus)/3.0;
                        a = 1.0;
                        expToGive += expMap.get("trident_baseEXP");
                        expRepairMultiplier = expMap.get("trident_expMultiplier");
                        break;
                    case ELYTRA:
                        repairPercentage  = (0.8 + repairBonus)/3.0;
                        a = 1.0;
                        expToGive += expMap.get("elytra_baseEXP");
                        expRepairMultiplier = expMap.get("elytra_expMultiplier");
                        break;
                    case SHIELD:
                        repairPercentage  = (0.8 + repairBonus)/3.0;
                        a = 1.0;
                        expToGive += expMap.get("shield_baseEXP");
                        expRepairMultiplier = expMap.get("shield_expMultiplier");
                        break;
                    case CROSSBOW:
                        repairPercentage  = (0.8 + repairBonus)/3.0;
                        a = 3.0;
                        expToGive += expMap.get("crossbow_baseEXP");
                        expRepairMultiplier = expMap.get("crossbow_expMultiplier");
                        break;
                    default:
                        break;
                }
                ItemMeta itemInHandMeta = itemInHand.getItemMeta();
                if (repairPercentage*a < 0.2) {
                    actionMessage.sendMessage(ChatColor.RED + lang.getString("repairFail0"));
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
                        increaseStats.changeEXP(skillName,enchantEXP+expToGive+(expRepairMultiplier*expDamage));
                        p.getWorld().playEffect(p.getLocation(), Effect.ANVIL_USE,1);
                    }
                }
            }
        }
        return repaired;
    }

    public void salvaging() {
        if (!runMethods) {
            return;
        }
        if (!p.hasPermission("freeRPG.canSalvage")) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int salvageLevel = (int) pStat.get(skillName).get(7);
        ItemGroups itemGroups = new ItemGroups();
        Map<Material,Integer> repairItemsAmount = itemGroups.getRepairItemsAmount();
        if (repairItemsAmount.containsKey(itemInHand.getType())) {
            p.getWorld().playEffect(p.getLocation(), Effect.ANVIL_USE,1);
            ItemMeta itemInHandMeta = itemInHand.getItemMeta();
            Material itemType = itemInHand.getType();
            int amount = repairItemsAmount.get(itemType);
            Map<Material,Material> repairItems = itemGroups.getRepairItems();
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
                    actionMessage.sendMessage(ChatColor.RED + lang.getString("repairFail1"));
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
                    increaseStats.changeEXP(skillName,expMap.get("salvageStick"));
                    break;
                case LEATHER:
                    increaseStats.changeEXP(skillName,expMap.get("salvageLeather"));
                    break;
                case COBBLESTONE:
                    increaseStats.changeEXP(skillName,expMap.get("salvageCobblestone"));
                    break;
                case IRON_INGOT:
                    increaseStats.changeEXP(skillName,expMap.get("salvageIron_Ingot"));
                    break;
                case GOLD_INGOT:
                    increaseStats.changeEXP(skillName,expMap.get("salvageGold_Ingot"));
                    break;
                case STRING:
                    increaseStats.changeEXP(skillName,expMap.get("salvageString"));
                    break;
                case PHANTOM_MEMBRANE:
                    increaseStats.changeEXP(skillName,expMap.get("salvagePhantom_Membrane"));
                    break;
                case DIAMOND:
                    increaseStats.changeEXP(skillName,expMap.get("salvageDiamond"));
                    break;
                case NETHERITE_INGOT:
                    increaseStats.changeEXP(skillName,expMap.get("salvageNetherite_Ingot"));
                default:
                    break;

            }
        }
    }

    public int magicRepair() {
        if (!runMethods) {
            return 0;
        }
        if (itemInHand.getEnchantments().size() == 0) {
            return 0;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        double sucessChance = 0;
        if ((int) pStat.get(skillName).get(13) > 0) {
            sucessChance = 1;
        }
        if (sucessChance < rand.nextDouble()) {
            actionMessage.sendMessage(ChatColor.RED + lang.getString("repairFail2"));
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
