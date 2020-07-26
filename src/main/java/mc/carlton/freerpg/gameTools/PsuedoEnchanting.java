package mc.carlton.freerpg.gameTools;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class PsuedoEnchanting {
    Map<String, Enchantment[]> toolEnchantMap = new HashMap<>();
    Map<Material,Object[]> itemEnchantTypeMap = new HashMap<>();
    Map<Enchantment,Integer> enchantmentWeightMap = new HashMap<>();
    Map<Enchantment,Integer[]> enchantmentBracketMap = new HashMap<>();

    Random rand = new Random();

    public PsuedoEnchanting() {
        //toolEnchantMao
        toolEnchantMap.put("chestplate",new Enchantment[]{Enchantment.DURABILITY,Enchantment.MENDING,Enchantment.VANISHING_CURSE,Enchantment.PROTECTION_ENVIRONMENTAL,Enchantment.PROTECTION_EXPLOSIONS,Enchantment.PROTECTION_FIRE,Enchantment.PROTECTION_PROJECTILE,Enchantment.THORNS,Enchantment.BINDING_CURSE});
        toolEnchantMap.put("leggings",new Enchantment[]{Enchantment.DURABILITY,Enchantment.MENDING,Enchantment.VANISHING_CURSE,Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.PROTECTION_EXPLOSIONS, Enchantment.PROTECTION_FIRE, Enchantment.PROTECTION_PROJECTILE, Enchantment.BINDING_CURSE});
        toolEnchantMap.put("boots",new Enchantment[]{Enchantment.DURABILITY,Enchantment.MENDING,Enchantment.VANISHING_CURSE,Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.PROTECTION_EXPLOSIONS, Enchantment.PROTECTION_FIRE, Enchantment.PROTECTION_PROJECTILE, Enchantment.BINDING_CURSE,Enchantment.DEPTH_STRIDER,Enchantment.FROST_WALKER, Enchantment.PROTECTION_FALL});
        toolEnchantMap.put("helmet",new Enchantment[]{Enchantment.DURABILITY,Enchantment.MENDING,Enchantment.VANISHING_CURSE,Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.PROTECTION_EXPLOSIONS, Enchantment.PROTECTION_FIRE, Enchantment.PROTECTION_PROJECTILE, Enchantment.BINDING_CURSE,Enchantment.WATER_WORKER,Enchantment.OXYGEN});
        toolEnchantMap.put("sword", new Enchantment[]{Enchantment.DURABILITY,Enchantment.MENDING,Enchantment.VANISHING_CURSE,Enchantment.DAMAGE_ALL,Enchantment.DAMAGE_ARTHROPODS,Enchantment.DAMAGE_UNDEAD,Enchantment.KNOCKBACK,Enchantment.FIRE_ASPECT,Enchantment.LOOT_BONUS_MOBS,Enchantment.SWEEPING_EDGE});
        toolEnchantMap.put("tool", new Enchantment[]{Enchantment.DURABILITY,Enchantment.MENDING,Enchantment.VANISHING_CURSE,Enchantment.DIG_SPEED,Enchantment.LOOT_BONUS_BLOCKS,Enchantment.SILK_TOUCH});
        toolEnchantMap.put("bow", new Enchantment[]{Enchantment.DURABILITY,Enchantment.MENDING,Enchantment.VANISHING_CURSE,Enchantment.ARROW_DAMAGE,Enchantment.ARROW_FIRE,Enchantment.ARROW_INFINITE,Enchantment.ARROW_KNOCKBACK});
        toolEnchantMap.put("rod", new Enchantment[]{Enchantment.DURABILITY,Enchantment.MENDING,Enchantment.VANISHING_CURSE,Enchantment.LUCK,Enchantment.LURE});
        toolEnchantMap.put("trident",new Enchantment[]{Enchantment.DURABILITY,Enchantment.MENDING,Enchantment.VANISHING_CURSE,Enchantment.LOYALTY,Enchantment.IMPALING,Enchantment.RIPTIDE,Enchantment.CHANNELING});
        toolEnchantMap.put("crossbow", new Enchantment[]{Enchantment.DURABILITY,Enchantment.MENDING,Enchantment.VANISHING_CURSE,Enchantment.QUICK_CHARGE,Enchantment.MULTISHOT,Enchantment.PIERCING});
        Enchantment[] holder = new Enchantment[]{Enchantment.DURABILITY,Enchantment.MENDING,Enchantment.VANISHING_CURSE,Enchantment.PIERCING,Enchantment.MULTISHOT,Enchantment.QUICK_CHARGE,Enchantment.CHANNELING,Enchantment.RIPTIDE,Enchantment.IMPALING,Enchantment.LOYALTY,Enchantment.LURE,Enchantment.LUCK,
                                   Enchantment.ARROW_INFINITE,Enchantment.ARROW_KNOCKBACK,Enchantment.ARROW_FIRE,Enchantment.ARROW_DAMAGE,Enchantment.SILK_TOUCH,Enchantment.LOOT_BONUS_BLOCKS,Enchantment.DIG_SPEED,Enchantment.SWEEPING_EDGE,Enchantment.LOOT_BONUS_MOBS,Enchantment.FIRE_ASPECT,
                                   Enchantment.DAMAGE_UNDEAD,Enchantment.KNOCKBACK,Enchantment.DAMAGE_ARTHROPODS,Enchantment.DAMAGE_ALL,Enchantment.BINDING_CURSE,Enchantment.THORNS,Enchantment.FROST_WALKER,Enchantment.DEPTH_STRIDER,Enchantment.OXYGEN,Enchantment.PROTECTION_EXPLOSIONS,
                                   Enchantment.WATER_WORKER,Enchantment.PROTECTION_PROJECTILE,Enchantment.PROTECTION_FIRE,Enchantment.PROTECTION_FALL,Enchantment.PROTECTION_ENVIRONMENTAL};
        toolEnchantMap.put("book",holder);

        //itemEnchantMap
        itemEnchantTypeMap.put(Material.DIAMOND_SWORD,new Object[]{"sword",10});
        itemEnchantTypeMap.put(Material.STONE_SWORD,new Object[]{"sword",5});
        itemEnchantTypeMap.put(Material.GOLDEN_SWORD,new Object[]{"sword",22});
        itemEnchantTypeMap.put(Material.IRON_SWORD,new Object[]{"sword",14});
        itemEnchantTypeMap.put(Material.WOODEN_SWORD,new Object[]{"sword",15});
        itemEnchantTypeMap.put(Material.DIAMOND_AXE,new Object[]{"tool",10});
        itemEnchantTypeMap.put(Material.STONE_AXE,new Object[]{"tool",5});
        itemEnchantTypeMap.put(Material.GOLDEN_AXE,new Object[]{"tool",22});
        itemEnchantTypeMap.put(Material.IRON_AXE,new Object[]{"tool",14});
        itemEnchantTypeMap.put(Material.WOODEN_AXE,new Object[]{"tool",15});
        itemEnchantTypeMap.put(Material.DIAMOND_PICKAXE,new Object[]{"tool",10});
        itemEnchantTypeMap.put(Material.STONE_PICKAXE,new Object[]{"tool",5});
        itemEnchantTypeMap.put(Material.GOLDEN_PICKAXE,new Object[]{"tool",22});
        itemEnchantTypeMap.put(Material.IRON_PICKAXE,new Object[]{"tool",14});
        itemEnchantTypeMap.put(Material.WOODEN_PICKAXE,new Object[]{"tool",15});
        itemEnchantTypeMap.put(Material.DIAMOND_SHOVEL,new Object[]{"tool",10});
        itemEnchantTypeMap.put(Material.STONE_SHOVEL,new Object[]{"tool",5});
        itemEnchantTypeMap.put(Material.GOLDEN_SHOVEL,new Object[]{"tool",22});
        itemEnchantTypeMap.put(Material.IRON_SHOVEL,new Object[]{"tool",14});
        itemEnchantTypeMap.put(Material.WOODEN_SHOVEL,new Object[]{"tool",15});
        itemEnchantTypeMap.put(Material.BOW,new Object[]{"bow",1});
        itemEnchantTypeMap.put(Material.ENCHANTED_BOOK,new Object[]{"book",1});
        itemEnchantTypeMap.put(Material.BOOK,new Object[]{"book",1});
        itemEnchantTypeMap.put(Material.FISHING_ROD,new Object[]{"rod",1});
        itemEnchantTypeMap.put(Material.TRIDENT,new Object[]{"trident",1});
        itemEnchantTypeMap.put(Material.CROSSBOW,new Object[]{"crossbow",1});
        itemEnchantTypeMap.put(Material.CHAINMAIL_HELMET,new Object[]{"helmet",12});
        itemEnchantTypeMap.put(Material.DIAMOND_HELMET,new Object[]{"helmet",10});
        itemEnchantTypeMap.put(Material.GOLDEN_HELMET,new Object[]{"helmet",25});
        itemEnchantTypeMap.put(Material.IRON_HELMET,new Object[]{"helmet",9});
        itemEnchantTypeMap.put(Material.LEATHER_HELMET,new Object[]{"helmet",15});
        itemEnchantTypeMap.put(Material.TURTLE_HELMET,new Object[]{"helmet",1});
        itemEnchantTypeMap.put(Material.CHAINMAIL_CHESTPLATE,new Object[]{"chestplate",12});
        itemEnchantTypeMap.put(Material.DIAMOND_CHESTPLATE,new Object[]{"chestplate",10});
        itemEnchantTypeMap.put(Material.GOLDEN_CHESTPLATE,new Object[]{"chestplate",25});
        itemEnchantTypeMap.put(Material.IRON_CHESTPLATE,new Object[]{"chestplate",9});
        itemEnchantTypeMap.put(Material.LEATHER_CHESTPLATE,new Object[]{"chestplate",15});
        itemEnchantTypeMap.put(Material.LEATHER_LEGGINGS,new Object[]{"leggings",15});
        itemEnchantTypeMap.put(Material.CHAINMAIL_LEGGINGS,new Object[]{"leggings",12});
        itemEnchantTypeMap.put(Material.DIAMOND_LEGGINGS,new Object[]{"leggings",10});
        itemEnchantTypeMap.put(Material.GOLDEN_LEGGINGS,new Object[]{"leggings",25});
        itemEnchantTypeMap.put(Material.IRON_LEGGINGS,new Object[]{"leggings",9});
        itemEnchantTypeMap.put(Material.CHAINMAIL_BOOTS,new Object[]{"boots",12});
        itemEnchantTypeMap.put(Material.DIAMOND_BOOTS,new Object[]{"boots",10});
        itemEnchantTypeMap.put(Material.GOLDEN_BOOTS,new Object[]{"boots",25});
        itemEnchantTypeMap.put(Material.IRON_BOOTS,new Object[]{"boots",9});
        itemEnchantTypeMap.put(Material.LEATHER_BOOTS,new Object[]{"boots",15});

        enchantmentWeightMap.put(Enchantment.PROTECTION_ENVIRONMENTAL,10);
        enchantmentWeightMap.put(Enchantment.PROTECTION_FALL,5);
        enchantmentWeightMap.put(Enchantment.PROTECTION_FIRE,5);
        enchantmentWeightMap.put(Enchantment.PROTECTION_PROJECTILE,5);
        enchantmentWeightMap.put(Enchantment.WATER_WORKER,2);
        enchantmentWeightMap.put(Enchantment.PROTECTION_EXPLOSIONS,2);
        enchantmentWeightMap.put(Enchantment.OXYGEN,2);
        enchantmentWeightMap.put(Enchantment.DEPTH_STRIDER,2);
        enchantmentWeightMap.put(Enchantment.FROST_WALKER,2);
        enchantmentWeightMap.put(Enchantment.THORNS,1);
        enchantmentWeightMap.put(Enchantment.BINDING_CURSE,1);
        enchantmentWeightMap.put(Enchantment.DAMAGE_ALL,10);
        enchantmentWeightMap.put(Enchantment.DAMAGE_ARTHROPODS,5);
        enchantmentWeightMap.put(Enchantment.KNOCKBACK,5);
        enchantmentWeightMap.put(Enchantment.DAMAGE_UNDEAD,5);
        enchantmentWeightMap.put(Enchantment.FIRE_ASPECT,2);
        enchantmentWeightMap.put(Enchantment.LOOT_BONUS_MOBS,2);
        enchantmentWeightMap.put(Enchantment.SWEEPING_EDGE,2);
        enchantmentWeightMap.put(Enchantment.DIG_SPEED,10);
        enchantmentWeightMap.put(Enchantment.LOOT_BONUS_BLOCKS,2);
        enchantmentWeightMap.put(Enchantment.SILK_TOUCH,2);
        enchantmentWeightMap.put(Enchantment.ARROW_DAMAGE,10);
        enchantmentWeightMap.put(Enchantment.ARROW_FIRE,2);
        enchantmentWeightMap.put(Enchantment.ARROW_KNOCKBACK,2);
        enchantmentWeightMap.put(Enchantment.ARROW_INFINITE,1);
        enchantmentWeightMap.put(Enchantment.LUCK,2);
        enchantmentWeightMap.put(Enchantment.LURE,2);
        enchantmentWeightMap.put(Enchantment.LOYALTY,5);
        enchantmentWeightMap.put(Enchantment.IMPALING,2);
        enchantmentWeightMap.put(Enchantment.RIPTIDE,2);
        enchantmentWeightMap.put(Enchantment.CHANNELING,1);
        enchantmentWeightMap.put(Enchantment.QUICK_CHARGE,10);
        enchantmentWeightMap.put(Enchantment.MULTISHOT,3);
        enchantmentWeightMap.put(Enchantment.PIERCING,30);
        enchantmentWeightMap.put(Enchantment.DURABILITY,5);
        enchantmentWeightMap.put(Enchantment.MENDING,2);
        enchantmentWeightMap.put(Enchantment.VANISHING_CURSE,1);

        enchantmentBracketMap.put(Enchantment.PROTECTION_ENVIRONMENTAL,new Integer[]{1,12,12,23,23,34,34,45});
        enchantmentBracketMap.put(Enchantment.PROTECTION_FALL,new Integer[]{5,11,11,17,17,23,23,29});
        enchantmentBracketMap.put(Enchantment.PROTECTION_FIRE,new Integer[]{10,18,18,26,26,34,34,42});
        enchantmentBracketMap.put(Enchantment.PROTECTION_PROJECTILE,new Integer[]{3,9,9,15,15,21,21,27});
        enchantmentBracketMap.put(Enchantment.WATER_WORKER,new Integer[]{1,41});
        enchantmentBracketMap.put(Enchantment.PROTECTION_EXPLOSIONS,new Integer[]{5,13,13,21,21,29,29,37});
        enchantmentBracketMap.put(Enchantment.OXYGEN,new Integer[]{10,40,20,50,30,60});
        enchantmentBracketMap.put(Enchantment.DEPTH_STRIDER,new Integer[]{10,25,20,35,30,45});
        enchantmentBracketMap.put(Enchantment.FROST_WALKER,new Integer[]{10,25});
        enchantmentBracketMap.put(Enchantment.THORNS,new Integer[]{10,61,30,71,50,81});
        enchantmentBracketMap.put(Enchantment.BINDING_CURSE,new Integer[]{25,50});
        enchantmentBracketMap.put(Enchantment.DAMAGE_ALL,new Integer[]{1,21,12,32,23,43,34,54,45,65});
        enchantmentBracketMap.put(Enchantment.DAMAGE_ARTHROPODS,new Integer[]{5,12,13,33,21,41,29,49,37,57});
        enchantmentBracketMap.put(Enchantment.KNOCKBACK,new Integer[]{5,61,25,71});
        enchantmentBracketMap.put(Enchantment.DAMAGE_UNDEAD,new Integer[]{5,12,13,33,21,41,29,49,37,57});
        enchantmentBracketMap.put(Enchantment.FIRE_ASPECT,new Integer[]{10,61,30,71});
        enchantmentBracketMap.put(Enchantment.LOOT_BONUS_MOBS,new Integer[]{15,61,24,71,33,81});
        enchantmentBracketMap.put(Enchantment.SWEEPING_EDGE,new Integer[]{5,20,14,29,23,38});
        enchantmentBracketMap.put(Enchantment.DIG_SPEED,new Integer[]{1,61,11,71,21,81,31,91,41,101});
        enchantmentBracketMap.put(Enchantment.LOOT_BONUS_BLOCKS,new Integer[]{15,61,24,71,33,81});
        enchantmentBracketMap.put(Enchantment.SILK_TOUCH,new Integer[]{15,81});
        enchantmentBracketMap.put(Enchantment.ARROW_DAMAGE,new Integer[]{1,16,11,26,21,36,31,46,41,56});
        enchantmentBracketMap.put(Enchantment.ARROW_FIRE,new Integer[]{20,50});
        enchantmentBracketMap.put(Enchantment.ARROW_KNOCKBACK,new Integer[]{12,37,32,57});
        enchantmentBracketMap.put(Enchantment.ARROW_INFINITE,new Integer[]{20,50});
        enchantmentBracketMap.put(Enchantment.LUCK,new Integer[]{15,61,24,71,33,81});
        enchantmentBracketMap.put(Enchantment.LURE,new Integer[]{15,61,24,71,33,81});
        enchantmentBracketMap.put(Enchantment.LOYALTY,new Integer[]{12,50,19,50,26,50});
        enchantmentBracketMap.put(Enchantment.IMPALING,new Integer[]{1,21,9,29,17,37,25,45,33,53});
        enchantmentBracketMap.put(Enchantment.RIPTIDE,new Integer[]{17,50,24,50,31,50});
        enchantmentBracketMap.put(Enchantment.CHANNELING,new Integer[]{25,50});
        enchantmentBracketMap.put(Enchantment.QUICK_CHARGE,new Integer[]{12,50,32,50,42,50});
        enchantmentBracketMap.put(Enchantment.MULTISHOT,new Integer[]{20,50});
        enchantmentBracketMap.put(Enchantment.PIERCING,new Integer[]{1,50,11,50,21,50,31,50});
        enchantmentBracketMap.put(Enchantment.DURABILITY,new Integer[]{5,61,13,71,21,81});
        enchantmentBracketMap.put(Enchantment.MENDING,new Integer[]{25,75});
        enchantmentBracketMap.put(Enchantment.VANISHING_CURSE,new Integer[]{25,50});
    }

    public ItemStack enchantItem(ItemStack item, int level,boolean isTreasure) {
        //Getting data
        Material itemType = item.getType();
        int enchantability = (int)itemEnchantTypeMap.get(itemType)[1];
        Enchantment[] possibleEnchants0 = toolEnchantMap.get((String)itemEnchantTypeMap.get(itemType)[0]);
        Map<Enchantment,Integer> enchantment_level= new HashMap<>();
        ItemStack enchantedItem = item;
        ItemMeta meta = enchantedItem.getItemMeta();

        ArrayList<Enchantment> possibleEnchants = new ArrayList<>(Arrays.asList(possibleEnchants0));
        if (!isTreasure) {
            if (possibleEnchants.contains(Enchantment.BINDING_CURSE)) {
                possibleEnchants.remove(Enchantment.BINDING_CURSE);
            }
            if (possibleEnchants.contains(Enchantment.VANISHING_CURSE)) {
                possibleEnchants.remove(Enchantment.VANISHING_CURSE);
            }
            if (possibleEnchants.contains(Enchantment.MENDING)) {
                possibleEnchants.remove(Enchantment.MENDING);
            }
        }

        //Determining modified level
        int rand_enchantability = 1 + rand.nextInt(Math.round(enchantability / 4 + 1)) + rand.nextInt(Math.round(enchantability / 4 + 1));

        int k = level + rand_enchantability;

        double rand_bonus_percent = 1 + (rand.nextDouble() + rand.nextDouble() - 1) * 0.15;

        int modifiedLevel = (int) Math.round(k*rand_bonus_percent);
        if (modifiedLevel < 1) {
            modifiedLevel = 1;
        }

        //Finding possible enchants
        int T = 0;
        for (Enchantment enchant : possibleEnchants) {
            Integer[] brackets = enchantmentBracketMap.get(enchant);
            int power = brackets.length/2;
            for (int i=0; i<power;i++) {
                if (brackets[2*i] <= modifiedLevel && brackets[2*i + 1] >= modifiedLevel) {
                    if (enchantment_level.containsKey(enchant)) {
                        enchantment_level.put(enchant,i+1);
                    }
                    else {
                        enchantment_level.put(enchant,i+1);
                        T += enchantmentWeightMap.get(enchant);
                    }
                }
            }
        }
        if (T < 1) {
            T  = 1;
        }

        //Picking enchantment
        int w = rand.nextInt(T);
        for (Enchantment enchant : enchantment_level.keySet()) {
            w = w - enchantmentWeightMap.get(enchant);
            if (w < 0) {
                if (enchantedItem.getType() == Material.BOOK || enchantedItem.getType() == Material.ENCHANTED_BOOK) {
                    ((EnchantmentStorageMeta) meta).addStoredEnchant(enchant,enchantment_level.get(enchant),false);
                    enchantedItem.setItemMeta(meta);
                }
                else {
                    enchantedItem.addUnsafeEnchantment(enchant,enchantment_level.get(enchant));
                }
                break;
            }
        }

        return moreEnchants(enchantedItem, (int) Math.round(modifiedLevel), enchantment_level);


    }

    public ItemStack moreEnchants(ItemStack enchanted_Item, int modifiedLevel, Map<Enchantment,Integer> possibleEnchants) {
        ItemStack enchantedItem = enchanted_Item;
        ItemMeta meta = enchantedItem.getItemMeta();
        Map<Enchantment, Integer> enchants = enchanted_Item.getEnchantments();
        if (enchantedItem.getType() == Material.BOOK && enchantedItem.getType() == Material.ENCHANTED_BOOK) {
            enchants = ((EnchantmentStorageMeta) meta).getStoredEnchants();
        }
        double prob = (modifiedLevel + 1) / 50.0;
        if (prob < rand.nextDouble()) {
            return enchantedItem;
        }
        for (Enchantment enchantment : enchants.keySet()) {
            if (enchantment.equals(Enchantment.DAMAGE_ALL) || enchantment.equals(Enchantment.DAMAGE_ARTHROPODS) || enchantment.equals(Enchantment.DAMAGE_UNDEAD)) {
                if (possibleEnchants.containsKey(Enchantment.DAMAGE_ALL)) {
                    possibleEnchants.remove(Enchantment.DAMAGE_ALL);
                }
                if (possibleEnchants.containsKey(Enchantment.DAMAGE_ARTHROPODS)) {
                    possibleEnchants.remove(Enchantment.DAMAGE_ARTHROPODS);
                }
                if (possibleEnchants.containsKey(Enchantment.DAMAGE_UNDEAD)) {
                    possibleEnchants.remove(Enchantment.DAMAGE_UNDEAD);
                }
            } else if (enchantment.equals(Enchantment.PROTECTION_ENVIRONMENTAL) || enchantment.equals(Enchantment.PROTECTION_EXPLOSIONS) || enchantment.equals(Enchantment.PROTECTION_FIRE) || enchantment.equals(Enchantment.PROTECTION_PROJECTILE)) {
                if (possibleEnchants.containsKey(Enchantment.PROTECTION_ENVIRONMENTAL)) {
                    possibleEnchants.remove(Enchantment.PROTECTION_ENVIRONMENTAL);
                }
                if (possibleEnchants.containsKey(Enchantment.PROTECTION_EXPLOSIONS)) {
                    possibleEnchants.remove(Enchantment.PROTECTION_ENVIRONMENTAL);
                }
                if (possibleEnchants.containsKey(Enchantment.PROTECTION_PROJECTILE)) {
                    possibleEnchants.remove(Enchantment.PROTECTION_PROJECTILE);
                }
                if (possibleEnchants.containsKey(Enchantment.PROTECTION_FIRE)) {
                    possibleEnchants.remove(Enchantment.PROTECTION_FIRE);
                }
            } else if (enchantment.equals(Enchantment.SILK_TOUCH) || enchantment.equals(Enchantment.LOOT_BONUS_BLOCKS)) {
                if (possibleEnchants.containsKey(Enchantment.SILK_TOUCH)) {
                    possibleEnchants.remove(Enchantment.SILK_TOUCH);
                }
                if (possibleEnchants.containsKey(Enchantment.LOOT_BONUS_BLOCKS)) {
                    possibleEnchants.remove(Enchantment.LOOT_BONUS_BLOCKS);
                }
            } else if (enchantment.equals(Enchantment.DEPTH_STRIDER) || enchantment.equals(Enchantment.FROST_WALKER)) {
                if (possibleEnchants.containsKey(Enchantment.DEPTH_STRIDER)) {
                    possibleEnchants.remove(Enchantment.DEPTH_STRIDER);
                }
                if (possibleEnchants.containsKey(Enchantment.FROST_WALKER)) {
                    possibleEnchants.remove(Enchantment.FROST_WALKER);
                }
            } else if (enchantment.equals(Enchantment.MENDING) || enchantment.equals(Enchantment.ARROW_INFINITE)) {
                if (possibleEnchants.containsKey(Enchantment.MENDING)) {
                    possibleEnchants.remove(Enchantment.MENDING);
                }
                if (possibleEnchants.containsKey(Enchantment.ARROW_INFINITE)) {
                    possibleEnchants.remove(Enchantment.ARROW_INFINITE);
                }
            } else if (enchantment.equals(Enchantment.RIPTIDE)) {
                if (possibleEnchants.containsKey(Enchantment.LOYALTY)) {
                    possibleEnchants.remove(Enchantment.LOYALTY);
                }
                if (possibleEnchants.containsKey(Enchantment.RIPTIDE)) {
                    possibleEnchants.remove(Enchantment.RIPTIDE);
                }
                if (possibleEnchants.containsKey(Enchantment.CHANNELING)) {
                    possibleEnchants.remove(Enchantment.CHANNELING);
                }
            } else if (enchantment.equals(Enchantment.CHANNELING) || enchantment.equals(Enchantment.LOYALTY)) {
                if (possibleEnchants.containsKey(Enchantment.RIPTIDE)) {
                    possibleEnchants.remove(Enchantment.RIPTIDE);
                }
            } else if (enchantment.equals(Enchantment.MULTISHOT) || enchantment.equals(Enchantment.PIERCING)) {
                if (possibleEnchants.containsKey(Enchantment.MULTISHOT)) {
                    possibleEnchants.remove(Enchantment.MULTISHOT);
                }
                if (possibleEnchants.containsKey(Enchantment.PIERCING)) {
                    possibleEnchants.remove(Enchantment.PIERCING);
                }
            }
            if (possibleEnchants.containsKey(enchantment)) {
                possibleEnchants.remove(enchantment);
            }
        }
        int T = 0;
        for (Enchantment enchantment : possibleEnchants.keySet()) {
            T += enchantmentWeightMap.get(enchantment);
        }
        if (T < 1) {
            T  = 1;
        }

        int w = rand.nextInt(T);
        for (Enchantment enchant : possibleEnchants.keySet()) {
            w = w - enchantmentWeightMap.get(enchant);
            if (w < 0) {
                if (enchantedItem.getType() == Material.BOOK || enchantedItem.getType() == Material.ENCHANTED_BOOK) {
                    ((EnchantmentStorageMeta) meta).addStoredEnchant(enchant,possibleEnchants.get(enchant),false);
                    enchantedItem.setItemMeta(meta);
                }
                else {
                    enchantedItem.addUnsafeEnchantment(enchant,possibleEnchants.get(enchant));
                }
                break;
            }
        }

        return moreEnchants(enchantedItem, (int) Math.round(modifiedLevel / 2), possibleEnchants);
    }

    public ItemStack addEnchant(ItemStack preEnchantedItem, int level,boolean isTreasure) {
        //Getting data
        Material itemType = preEnchantedItem.getType();
        int enchantAbility = (int)itemEnchantTypeMap.get(itemType)[1];
        Enchantment[] possibleEnchants0 = toolEnchantMap.get((String)itemEnchantTypeMap.get(itemType)[0]);
        Map<Enchantment,Integer> enchantment_level= new HashMap<>();
        ItemStack enchantedItem = preEnchantedItem;

        ArrayList<Enchantment> possibleEnchants = new ArrayList<>(Arrays.asList(possibleEnchants0));
        if (!isTreasure) {
            if (possibleEnchants.contains(Enchantment.BINDING_CURSE)) {
                possibleEnchants.remove(Enchantment.BINDING_CURSE);
            }
            if (possibleEnchants.contains(Enchantment.VANISHING_CURSE)) {
                possibleEnchants.remove(Enchantment.VANISHING_CURSE);
            }
            if (possibleEnchants.contains(Enchantment.MENDING)) {
                possibleEnchants.remove(Enchantment.MENDING);
            }
        }

        //Determining modified level
        int rand_enchantability = 1 + rand.nextInt(Math.round(enchantAbility / 4 + 1)) + rand.nextInt(Math.round(enchantAbility / 4 + 1));

        int k = level + rand_enchantability;

        double rand_bonus_percent = 1 + (rand.nextDouble() + rand.nextDouble() - 1) * 0.15;

        int modifiedLevel = (int) Math.round(k*rand_bonus_percent);
        if (modifiedLevel < 1) {
            modifiedLevel = 1;
        }

        //Finding possible enchants
        int T = 0;
        for (Enchantment enchant : possibleEnchants) {
            Integer[] brackets = enchantmentBracketMap.get(enchant);
            int power = brackets.length/2;
            for (int i=0; i<power;i++) {
                if (brackets[2*i] <= modifiedLevel && brackets[2*i + 1] >= modifiedLevel) {
                    if (enchantment_level.containsKey(enchant)) {
                        enchantment_level.put(enchant,i+1);
                    }
                    else {
                        enchantment_level.put(enchant,i+1);
                        T += enchantmentWeightMap.get(enchant);
                    }
                }
            }
        }

        return moreEnchants(enchantedItem, (int) Math.round(modifiedLevel), enchantment_level);
    }
}
