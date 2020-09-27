package mc.carlton.freerpg.perksAndAbilities;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.ActionBarMessages;
import mc.carlton.freerpg.gameTools.LanguageSelector;
import mc.carlton.freerpg.globalVariables.ItemGroups;
import mc.carlton.freerpg.playerInfo.ChangeStats;
import mc.carlton.freerpg.serverInfo.ConfigLoad;
import mc.carlton.freerpg.serverInfo.MinecraftVersion;
import mc.carlton.freerpg.playerInfo.PlayerStats;
import org.bukkit.*;
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
    private

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
        if (!isItemVanilla(itemInHand)) {
            ConfigLoad configLoad = new ConfigLoad();
            if (configLoad.isPreventUnsafeRepair()) {
                LanguageSelector lang = new LanguageSelector(p);
                p.sendMessage(ChatColor.RED +lang.getString("repairUnsafeEnchant"));
                return false;
            }
        }
        Material toolType = itemInHand.getType();
        boolean repaired = false;
        boolean foundItem = true;
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
                MinecraftVersion minecraftVersion = new MinecraftVersion();
                double MCversion = minecraftVersion.getMinecraftVersion_Double();
                if (toolType.equals(Material.WOODEN_AXE)) {
                    repairPercentage = (0.9 + repairBonus) / 3.0;
                    a = 3.0;
                    expToGive += expMap.get("wooden_baseEXP");
                    expRepairMultiplier = expMap.get("wooden_EXPMultiplier");
                }
                else if(toolType.equals(Material.WOODEN_HOE)) {
                    repairPercentage = (0.9 + repairBonus) / 2.0;
                    a = 2.0;
                    expToGive += expMap.get("wooden_baseEXP");
                    expRepairMultiplier = expMap.get("wooden_EXPMultiplier");
                }
                else if(toolType.equals(Material.WOODEN_PICKAXE)) {
                    repairPercentage = (0.9 + repairBonus) / 3.0;
                    a = 3.0;
                    expToGive += expMap.get("wooden_baseEXP");
                    expRepairMultiplier = expMap.get("wooden_EXPMultiplier");
                }
                else if(toolType.equals(Material.WOODEN_SHOVEL)) {
                    repairPercentage = (0.9 + repairBonus) / 1.0;
                    a = 1.0;
                    expToGive += expMap.get("wooden_baseEXP");
                    expRepairMultiplier = expMap.get("wooden_EXPMultiplier");
                }
                else if(toolType.equals(Material.WOODEN_SWORD)) {
                    repairPercentage = (0.9 + repairBonus) / 2.0;
                    a = 2.0;
                    expToGive += expMap.get("wooden_baseEXP");
                    expRepairMultiplier = expMap.get("wooden_EXPMultiplier");
                }
                else if(toolType.equals(Material.LEATHER_HELMET)) {
                    repairPercentage = (0.9 + repairBonus) / 5.0;
                    a = 5.0;
                    expToGive += expMap.get("leather_baseEXP");
                    expRepairMultiplier = expMap.get("leather_EXPMultiplier");
                }
                else if(toolType.equals(Material.LEATHER_CHESTPLATE)) {
                    repairPercentage = (0.9 + repairBonus) / 8.0;
                    a = 8.0;
                    expToGive += expMap.get("leather_baseEXP");
                    expRepairMultiplier = expMap.get("leather_EXPMultiplier");
                }
                else if(toolType.equals(Material.LEATHER_LEGGINGS)) {
                    repairPercentage = (0.9 + repairBonus) / 7.0;
                    a = 7.0;
                    expToGive += expMap.get("leather_baseEXP");
                    expRepairMultiplier = expMap.get("leather_EXPMultiplier");
                }
                else if(toolType.equals(Material.LEATHER_BOOTS)) {
                    repairPercentage = (0.9 + repairBonus) / 4.0;
                    a = 4.0;
                    expToGive += expMap.get("leather_baseEXP");
                    expRepairMultiplier = expMap.get("leather_EXPMultiplier");
                }
                else if(toolType.equals(Material.STONE_AXE)) {
                    repairPercentage = (0.8 + repairBonus) / 3.0;
                    a = 3.0;
                    expToGive += expMap.get("stone_baseEXP");
                    expRepairMultiplier = expMap.get("stone_EXPMultiplier");
                }
                else if(toolType.equals(Material.STONE_HOE)) {
                    repairPercentage = (0.8 + repairBonus) / 2.0;
                    a = 2.0;
                    expToGive += expMap.get("stone_baseEXP");
                    expRepairMultiplier = expMap.get("stone_EXPMultiplier");
                }
                else if(toolType.equals(Material.STONE_PICKAXE)) {
                    repairPercentage = (0.8 + repairBonus) / 3.0;
                    a = 3.0;
                    expToGive += expMap.get("stone_baseEXP");
                    expRepairMultiplier = expMap.get("stone_EXPMultiplier");
                }
                else if(toolType.equals(Material.STONE_SHOVEL)) {
                    repairPercentage = (0.8 + repairBonus) / 1.0;
                    a = 1.0;
                    expToGive += expMap.get("stone_baseEXP");
                    expRepairMultiplier = expMap.get("stone_EXPMultiplier");
                }
                else if(toolType.equals(Material.STONE_SWORD)) {
                    repairPercentage = (0.8 + repairBonus) / 2.0;
                    a = 2.0;
                    expToGive += expMap.get("stone_baseEXP");
                    expRepairMultiplier = expMap.get("stone_EXPMultiplier");
                }
                else if(toolType.equals(Material.CHAINMAIL_HELMET)) {
                    repairPercentage = (0.11 + repairBonus) / 5.0;
                    a = 5.0;
                    expToGive += expMap.get("chainmail_baseEXP");
                    expRepairMultiplier = expMap.get("chainmail_EXPMultiplier");
                }
                else if(toolType.equals(Material.CHAINMAIL_CHESTPLATE)) {
                    repairPercentage = (0.11 + repairBonus) / 8.0;
                    a = 8.0;
                    expToGive += expMap.get("chainmail_baseEXP");
                    expRepairMultiplier = expMap.get("chainmail_EXPMultiplier");
                }
                else if(toolType.equals(Material.CHAINMAIL_LEGGINGS)) {
                    repairPercentage = (0.11 + repairBonus) / 11.0;
                    a = 11.0;
                    expToGive += expMap.get("chainmail_baseEXP");
                    expRepairMultiplier = expMap.get("chainmail_EXPMultiplier");
                }
                else if(toolType.equals(Material.CHAINMAIL_BOOTS)) {
                    repairPercentage = (0.11 + repairBonus) / 4.0;
                    a = 4.0;
                    expToGive += expMap.get("chainmail_baseEXP");
                    expRepairMultiplier = expMap.get("chainmail_EXPMultiplier");
                }
                else if(toolType.equals(Material.GOLDEN_AXE)) {
                    repairPercentage = (0.7 + repairBonus) / 3.0;
                    a = 3.0;
                    expToGive += expMap.get("gold_baseEXP");
                    expRepairMultiplier = expMap.get("gold_EXPMultiplier");
                }
                else if(toolType.equals(Material.GOLDEN_HOE)) {
                    repairPercentage = (0.7 + repairBonus) / 2.0;
                    a = 2.0;
                    expToGive += expMap.get("gold_baseEXP");
                    expRepairMultiplier = expMap.get("gold_EXPMultiplier");
                }
                else if(toolType.equals(Material.GOLDEN_PICKAXE)) {
                    repairPercentage = (0.7 + repairBonus) / 3.0;
                    a = 3.0;
                    expToGive += expMap.get("gold_baseEXP");
                    expRepairMultiplier = expMap.get("gold_EXPMultiplier");
                }
                else if(toolType.equals(Material.GOLDEN_SHOVEL)) {
                    repairPercentage = (0.7 + repairBonus) / 1.0;
                    a = 1.0;
                    expToGive += expMap.get("gold_baseEXP");
                    expRepairMultiplier = expMap.get("gold_EXPMultiplier");
                }
                else if(toolType.equals(Material.GOLDEN_SWORD)) {
                    repairPercentage = (0.7 + repairBonus) / 2.0;
                    a = 2.0;
                    expToGive += expMap.get("gold_baseEXP");
                    expRepairMultiplier = expMap.get("gold_EXPMultiplier");
                }
                else if(toolType.equals(Material.GOLDEN_HELMET)) {
                    repairPercentage = (0.7 + repairBonus) / 5.0;
                    a = 5.0;
                    expToGive += expMap.get("gold_baseEXP");
                    expRepairMultiplier = expMap.get("gold_EXPMultiplier");
                }
                else if(toolType.equals(Material.GOLDEN_CHESTPLATE)) {
                    repairPercentage = (0.7 + repairBonus) / 8.0;
                    a = 8.0;
                    expToGive += expMap.get("gold_baseEXP");
                    expRepairMultiplier = expMap.get("gold_EXPMultiplier");
                }
                else if(toolType.equals(Material.GOLDEN_LEGGINGS)) {
                    repairPercentage = (0.7 + repairBonus) / 7.0;
                    a = 7.0;
                    expToGive += expMap.get("gold_baseEXP");
                    expRepairMultiplier = expMap.get("gold_EXPMultiplier");
                }
                else if(toolType.equals(Material.GOLDEN_BOOTS)) {
                    repairPercentage = (0.7 + repairBonus) / 4.0;
                    a = 4.0;
                    expToGive += expMap.get("gold_baseEXP");
                    expRepairMultiplier = expMap.get("gold_EXPMultiplier");
                }
                else if(toolType.equals(Material.IRON_AXE)) {
                    repairPercentage = (0.5 + repairBonus) / 3.0;
                    a = 3.0;
                    expToGive += expMap.get("iron_baseEXP");
                    expRepairMultiplier = expMap.get("iron_EXPMultiplier");
                }
                else if(toolType.equals(Material.IRON_HOE)) {
                    repairPercentage = (0.5 + repairBonus) / 2.0;
                    a = 2.0;
                    expToGive += expMap.get("iron_baseEXP");
                    expRepairMultiplier = expMap.get("iron_EXPMultiplier");
                }
                else if(toolType.equals(Material.IRON_PICKAXE)) {
                    repairPercentage = (0.5 + repairBonus) / 3.0;
                    a = 3.0;
                    expToGive += expMap.get("iron_baseEXP");
                    expRepairMultiplier = expMap.get("iron_EXPMultiplier");
                }
                else if(toolType.equals(Material.IRON_SHOVEL)) {
                    repairPercentage = (0.5 + repairBonus) / 1.0;
                    a = 1.0;
                    expToGive += expMap.get("iron_baseEXP");
                    expRepairMultiplier = expMap.get("iron_EXPMultiplier");
                }
                else if(toolType.equals(Material.IRON_SWORD)) {
                    repairPercentage = (0.5 + repairBonus) / 2.0;
                    a = 2.0;
                    expToGive += expMap.get("iron_baseEXP");
                    expRepairMultiplier = expMap.get("iron_EXPMultiplier");
                }
                else if(toolType.equals(Material.IRON_HELMET)) {
                    repairPercentage = (0.5 + repairBonus) / 5.0;
                    a = 5.0;
                    expToGive += expMap.get("iron_baseEXP");
                    expRepairMultiplier = expMap.get("iron_EXPMultiplier");
                }
                else if(toolType.equals(Material.IRON_CHESTPLATE)) {
                    repairPercentage = (0.5 + repairBonus) / 8.0;
                    a = 8.0;
                    expToGive += expMap.get("iron_baseEXP");
                    expRepairMultiplier = expMap.get("iron_EXPMultiplier");
                }
                else if(toolType.equals(Material.IRON_LEGGINGS)) {
                    repairPercentage = (0.5 + repairBonus) / 7.0;
                    a = 7.0;
                    expToGive += expMap.get("iron_baseEXP");
                    expRepairMultiplier = expMap.get("iron_EXPMultiplier");
                }
                else if(toolType.equals(Material.IRON_BOOTS)) {
                    repairPercentage = (0.5 + repairBonus) / 4.0;
                    a = 4.0;
                    expToGive += expMap.get("iron_baseEXP");
                    expRepairMultiplier = expMap.get("iron_EXPMultiplier");
                }
                else if(toolType.equals(Material.DIAMOND_AXE)) {
                    repairPercentage = (0.00 + repairBonus) / 3.0;
                    a = 3.0;
                    expToGive += expMap.get("diamond_baseEXP");
                    expRepairMultiplier = expMap.get("diamond_EXPMultiplier");
                }
                else if(toolType.equals(Material.DIAMOND_HOE)) {
                    repairPercentage = (0.00 + repairBonus) / 2.0;
                    a = 2.0;
                    expToGive += expMap.get("diamond_baseEXP");
                    expRepairMultiplier = expMap.get("diamond_EXPMultiplier");
                }
                else if(toolType.equals(Material.DIAMOND_PICKAXE)) {
                    repairPercentage = (0.00 + repairBonus) / 3.0;
                    a = 3.0;
                    expToGive += expMap.get("diamond_baseEXP");
                    expRepairMultiplier = expMap.get("diamond_EXPMultiplier");
                }
                else if(toolType.equals(Material.DIAMOND_SHOVEL)) {
                    repairPercentage = (0.00 + repairBonus) / 1.0;
                    a = 1.0;
                    expToGive += expMap.get("diamond_baseEXP");
                    expRepairMultiplier = expMap.get("diamond_EXPMultiplier");
                }
                else if(toolType.equals(Material.DIAMOND_SWORD)) {
                    repairPercentage = (0.00 + repairBonus) / 2.0;
                    a = 2.0;
                    expToGive += expMap.get("diamond_baseEXP");
                    expRepairMultiplier = expMap.get("diamond_EXPMultiplier");
                }
                else if(toolType.equals(Material.DIAMOND_HELMET)) {
                    repairPercentage = (0.00 + repairBonus) / 5.0;
                    a = 5.0;
                    expToGive += expMap.get("diamond_baseEXP");
                    expRepairMultiplier = expMap.get("diamond_EXPMultiplier");
                }
                else if(toolType.equals(Material.DIAMOND_CHESTPLATE)) {
                    repairPercentage = (0.00 + repairBonus) / 8.0;
                    a = 8.0;
                    expToGive += expMap.get("diamond_baseEXP");
                    expRepairMultiplier = expMap.get("diamond_EXPMultiplier");
                }
                else if(toolType.equals(Material.DIAMOND_LEGGINGS)) {
                    repairPercentage = (0.00 + repairBonus) / 7.0;
                    a = 7.0;
                    expToGive += expMap.get("diamond_baseEXP");
                    expRepairMultiplier = expMap.get("diamond_EXPMultiplier");
                }
                else if(toolType.equals(Material.DIAMOND_BOOTS)) {
                    repairPercentage = (0.0 + repairBonus) / 4.0;
                    a = 4.0;
                    expToGive += expMap.get("diamond_baseEXP");
                    expRepairMultiplier = expMap.get("diamond_EXPMultiplier");
                }
                else if(toolType.equals(Material.SHEARS)) {
                    repairPercentage = (0.5 + repairBonus) / 2.0;
                    a = 2.0;
                    expToGive += expMap.get("shears_baseEXP");
                    expRepairMultiplier = expMap.get("shear_expMultiplier");
                }
                else if(toolType.equals(Material.FISHING_ROD)) {
                    repairPercentage = (0.5 + repairBonus) / 2.0;
                    a = 2.0;
                    expToGive += expMap.get("fishingRod_baseEXP");
                    expRepairMultiplier = expMap.get("fishingRod_expMultiplier");
                }
                else if(toolType.equals(Material.CARROT_ON_A_STICK)) {
                    repairPercentage = (0.8 + repairBonus) / 1.0;
                    a = 1.0;
                    expToGive += expMap.get("carrotOnAStick_baseEXP");
                    expRepairMultiplier = expMap.get("carrotOnAStick_expMultiplier");
                }
                else if(toolType.equals(Material.FLINT_AND_STEEL)) {
                    repairPercentage = (0.8 + repairBonus) / 1.0;
                    a = 1.0;
                    expToGive += expMap.get("flintAndSteel_baseEXP");
                    expRepairMultiplier = expMap.get("flintAndSteel_expMultiplier");
                }
                else if(toolType.equals(Material.BOW)) {
                    repairPercentage = (0.8 + repairBonus) / 3.0;
                    a = 3.0;
                    expToGive += expMap.get("bow_baseEXP");
                    expRepairMultiplier = expMap.get("bow_expMultiplier");
                }
                else if(toolType.equals(Material.TRIDENT)) {
                    repairPercentage = (0.8 + repairBonus) / 3.0;
                    a = 1.0;
                    expToGive += expMap.get("trident_baseEXP");
                    expRepairMultiplier = expMap.get("trident_expMultiplier");
                }
                else if(toolType.equals(Material.ELYTRA)) {
                    repairPercentage = (0.8 + repairBonus) / 3.0;
                    a = 1.0;
                    expToGive += expMap.get("elytra_baseEXP");
                    expRepairMultiplier = expMap.get("elytra_expMultiplier");
                }
                else if(toolType.equals(Material.SHIELD)) {
                    repairPercentage = (0.8 + repairBonus) / 3.0;
                    a = 1.0;
                    expToGive += expMap.get("shield_baseEXP");
                    expRepairMultiplier = expMap.get("shield_expMultiplier");
                }
                else if(toolType.equals(Material.CROSSBOW)) {
                    repairPercentage = (0.8 + repairBonus) / 3.0;
                    a = 3.0;
                    expToGive += expMap.get("crossbow_baseEXP");
                    expRepairMultiplier = expMap.get("crossbow_expMultiplier");
                }
                else if(MCversion >= 1.16) {
                    if (toolType.equals(Material.NETHERITE_AXE)) {
                        repairPercentage = (0.00 + repairBonus * 0.5) / 3.0;
                        a = 4.0;
                        expToGive += expMap.get("netherite_baseEXP");
                        expRepairMultiplier = expMap.get("netherite_EXPMultiplier");
                    } else if (toolType.equals(Material.NETHERITE_HOE)) {
                        repairPercentage = (0.00 + repairBonus * 0.5) / 2.0;
                        a = 4.0;
                        expToGive += expMap.get("netherite_baseEXP");
                        expRepairMultiplier = expMap.get("netherite_EXPMultiplier");
                    } else if (toolType.equals(Material.NETHERITE_PICKAXE)) {
                        repairPercentage = (0.00 + repairBonus * 0.5) / 3.0;
                        a = 4.0;
                        expToGive += expMap.get("netherite_baseEXP");
                        expRepairMultiplier = expMap.get("netherite_EXPMultiplier");
                    } else if (toolType.equals(Material.NETHERITE_SHOVEL)) {
                        repairPercentage = (0.00 + repairBonus * 0.5) / 1.0;
                        a = 4.0;
                        expToGive += expMap.get("netherite_baseEXP");
                        expRepairMultiplier = expMap.get("netherite_EXPMultiplier");
                    } else if (toolType.equals(Material.NETHERITE_SWORD)) {
                        repairPercentage = (0.00 + repairBonus * 0.5) / 2.0;
                        a = 4.0;
                        expToGive += expMap.get("netherite_baseEXP");
                        expRepairMultiplier = expMap.get("netherite_EXPMultiplier");
                    } else if (toolType.equals(Material.NETHERITE_HELMET)) {
                        repairPercentage = (0.00 + repairBonus * 0.5) / 5.0;
                        a = 4.0;
                        expToGive += expMap.get("netherite_baseEXP");
                        expRepairMultiplier = expMap.get("netherite_EXPMultiplier");
                    } else if (toolType.equals(Material.NETHERITE_CHESTPLATE)) {
                        repairPercentage = (0.00 + repairBonus * 0.5) / 8.0;
                        a = 4.0;
                        expToGive += expMap.get("netherite_baseEXP");
                        expRepairMultiplier = expMap.get("netherite_EXPMultiplier");
                    } else if (toolType.equals(Material.NETHERITE_LEGGINGS)) {
                        repairPercentage = (0.00 + repairBonus * 0.5) / 7.0;
                        a = 4.0;
                        expToGive += expMap.get("netherite_baseEXP");
                        expRepairMultiplier = expMap.get("netherite_EXPMultiplier");
                    } else if (toolType.equals(Material.NETHERITE_BOOTS)) {
                        repairPercentage = (0.0 + repairBonus * 0.5) / 4.0;
                        a = 4.0;
                        expToGive += expMap.get("netherite_baseEXP");
                        expRepairMultiplier = expMap.get("netherite_EXPMultiplier");
                    }
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
        if (!isItemVanilla(itemInHand)) {
            ConfigLoad configLoad = new ConfigLoad();
            if (configLoad.isPreventUnsafeSalvage()) {
                LanguageSelector lang = new LanguageSelector(p);
                p.sendMessage(ChatColor.RED +lang.getString("salvageUnsafeEnchant"));
                return;
            }
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
            MinecraftVersion minecraftVersion = new MinecraftVersion();
            double mcVersion = minecraftVersion.getMinecraftVersion_Double();

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
                if (!type.equals(Material.NETHERITE_SCRAP)) {
                    p.getInventory().addItem(new ItemStack(type, amountToReturn));
                }
                else {
                    double multiplier1 = 0.1*salvageLevel + (1 - 0.1*salvageLevel)*rand.nextDouble();
                    double multiplier2 = 0.1*salvageLevel + (1 - 0.1*salvageLevel)*rand.nextDouble();
                    double amountToReturnGold_pre = 4*percentDurability*multiplier1;
                    int amountToReturnGold = (int) Math.round(amountToReturnGold_pre);
                    double amountToReturnScrap_pre = 4*percentDurability*multiplier2;
                    int amountToReturnScrap = (int) Math.round(amountToReturnScrap_pre);
                    p.getInventory().addItem(new ItemStack(Material.DIAMOND, amountToReturn));
                    if (p.getInventory().firstEmpty() == -1) {
                        World world = p.getWorld();
                        world.dropItemNaturally(p.getLocation().add(0,0.5,0),new ItemStack(Material.GOLD_INGOT,amountToReturnGold));
                        world.dropItemNaturally(p.getLocation().add(0,0.5,0),new ItemStack(Material.NETHERITE_SCRAP,amountToReturnScrap));
                    }
                    else {
                        p.getInventory().addItem(new ItemStack(Material.GOLD_INGOT,amountToReturnGold));
                        if (p.getInventory().firstEmpty() == -1) {
                            World world = p.getWorld();
                            world.dropItemNaturally(p.getLocation().add(0,0.5,0),new ItemStack(Material.NETHERITE_SCRAP,amountToReturnScrap));
                        }
                        else {
                            p.getInventory().addItem(new ItemStack(Material.NETHERITE_SCRAP,amountToReturnScrap));
                        }
                    }
                }
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
            if (type.equals(Material.STICK)) {
                increaseStats.changeEXP(skillName, expMap.get("salvageStick"));
            }
            else if (type.equals(Material.LEATHER)) {
                increaseStats.changeEXP(skillName, expMap.get("salvageLeather"));
            }
            else if (type.equals(Material.COBBLESTONE)) {
                increaseStats.changeEXP(skillName, expMap.get("salvageCobblestone"));
            }
            else if (type.equals(Material.IRON_INGOT)) {
                increaseStats.changeEXP(skillName, expMap.get("salvageIron_Ingot"));
            }
            else if (type.equals(Material.GOLD_INGOT)) {
                increaseStats.changeEXP(skillName, expMap.get("salvageGold_Ingot"));
            }
            else if (type.equals(Material.STRING)) {
                increaseStats.changeEXP(skillName, expMap.get("salvageString"));
            }
            else if (type.equals(Material.PHANTOM_MEMBRANE)) {
                increaseStats.changeEXP(skillName, expMap.get("salvagePhantom_Membrane"));
            }
            else if (type.equals(Material.DIAMOND)) {
                increaseStats.changeEXP(skillName, expMap.get("salvageDiamond"));
            }
            else if(mcVersion >= 1.16) {
                if (type.equals(Material.NETHERITE_SCRAP)) {
                    increaseStats.changeEXP(skillName, expMap.get("salvageNetherite_Ingot"));
                }
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

    public boolean isItemVanilla(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        Map<Enchantment,Integer> itemEnchants = itemMeta.getEnchants();
        try {//Try to create a safe item with those enchants, if it fails, we know the item is not vanilla
            new ItemStack(itemStack.getType(),itemStack.getAmount()).addEnchantments(itemEnchants);
        }
        catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
