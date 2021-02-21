package mc.carlton.freerpg.globalVariables;

import mc.carlton.freerpg.customContainers.OldCustomPotion;
import mc.carlton.freerpg.configStorage.ConfigLoad;
import mc.carlton.freerpg.serverInfo.MinecraftVersion;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ItemGroups {
    private MinecraftVersion minecraftVersion = new MinecraftVersion();
    private double mcVersion = minecraftVersion.getMinecraftVersion_Double();

    static List<Material> newIngredients = new ArrayList<>();
    static List<Material> oldIngredients = new ArrayList<>();
    static ItemStack hastePotion;
    static ItemStack fatiguePotion;
    static ItemStack heroPotion;
    static ItemStack decayPotion;
    static ItemStack resistancePotion;
    static List<Material> leftClickItems;
    static List<Material> pickaxes;
    static List<Material> axes;
    static List<Material> shovels;
    static List<Material> hoes;
    static List<Material> swords;
    static List<Material> noRightClick;
    static List<Material> actionableBlocks;
    static List<PotionEffectType> harmfulEffects;
    static List<Material> logs;
    static List<Material> tallCrops;
    static Map<Material,Boolean> trackedBlocks = new ConcurrentHashMap<>();
    static Map<Enchantment, Integer> enchantmentLevelMap = new HashMap<>();
    static List<Material> crops;
    static Map<Material,Integer> farmFood = new HashMap<Material,Integer>();
    static Map<Material,Integer> meatFood = new HashMap<Material,Integer>();
    static Map<Material,Double> farmFoodSaturation = new HashMap<Material,Double>();
    static Map<Material,Double> meatFoodSaturation = new HashMap<Material,Double>();
    static Map<Material,Integer> fishFood = new HashMap<Material,Integer>();
    static Map<Material,Double> fishFoodSaturation = new HashMap<Material,Double>();
    static List<Material> valuableItems;
    static List<Material> ores;
    static List<Material> flamePickOres;
    static HashSet<Material> veinMinerBlocks = new HashSet<>();
    static Map<Material,Material> repairItems = new HashMap<>();
    static Map<Material,Integer> repairItemsAmount = new HashMap<>();
    static List<Material> strippedLogs;
    static List<Material> wood;
    static List<Material> strippedWood;
    static ArrayList<Material> allLogs = new ArrayList<>();
    static List<Material> leaves;
    static Map<String, ItemStack> effectArrows = new HashMap<>();
    static Map<Material, Material> smeltableItemsMap = new HashMap<>();
    static Map<Material, Double> smeltingXPMap = new HashMap<>();


    public void initializeItemGroups(){
        initializeRepairItems();
        initializeIngredients();
        initializeCustomPostions();
        initializeLeftClickItems();
        initializeTools();
        initializeActionItems();
        initializeHarmfulPotions();
        initializeBlocks();
        initializeEnchantmentLevelMap();
        initializeFoodMaps();
        initializeValuableItems();
        initalizeArrows();
        initializeSmeltableItemsMap();
        initializeSmeltingXPMap();
        initializeTrackedBlocks();
    }

    public void initializeEnchantmentLevelMap() {
        enchantmentLevelMap.put(Enchantment.ARROW_KNOCKBACK, 2);
        enchantmentLevelMap.put(Enchantment.ARROW_DAMAGE, 5);
        enchantmentLevelMap.put(Enchantment.ARROW_FIRE, 1);
        enchantmentLevelMap.put(Enchantment.ARROW_INFINITE, 1);
        enchantmentLevelMap.put(Enchantment.BINDING_CURSE, 1);
        enchantmentLevelMap.put(Enchantment.CHANNELING, 1);
        enchantmentLevelMap.put(Enchantment.DAMAGE_ALL, 4);
        enchantmentLevelMap.put(Enchantment.DAMAGE_ARTHROPODS, 4);
        enchantmentLevelMap.put(Enchantment.DAMAGE_UNDEAD, 4);
        enchantmentLevelMap.put(Enchantment.DEPTH_STRIDER, 2);
        enchantmentLevelMap.put(Enchantment.DIG_SPEED, 4);
        enchantmentLevelMap.put(Enchantment.DURABILITY, 3);
        enchantmentLevelMap.put(Enchantment.FIRE_ASPECT, 2);
        enchantmentLevelMap.put(Enchantment.FROST_WALKER, 2);
        enchantmentLevelMap.put(Enchantment.IMPALING, 4);
        enchantmentLevelMap.put(Enchantment.KNOCKBACK, 2);
        enchantmentLevelMap.put(Enchantment.LOOT_BONUS_BLOCKS, 3);
        enchantmentLevelMap.put(Enchantment.LUCK, 3);
        enchantmentLevelMap.put(Enchantment.LOOT_BONUS_MOBS, 3);
        enchantmentLevelMap.put(Enchantment.LOYALTY, 3);
        enchantmentLevelMap.put(Enchantment.LURE, 3);
        enchantmentLevelMap.put(Enchantment.MENDING, 1);
        enchantmentLevelMap.put(Enchantment.MULTISHOT, 1);
        enchantmentLevelMap.put(Enchantment.OXYGEN, 3);
        enchantmentLevelMap.put(Enchantment.PIERCING, 4);
        enchantmentLevelMap.put(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        enchantmentLevelMap.put(Enchantment.PROTECTION_EXPLOSIONS, 4);
        enchantmentLevelMap.put(Enchantment.PROTECTION_FALL, 4);
        enchantmentLevelMap.put(Enchantment.PROTECTION_FIRE, 4);
        enchantmentLevelMap.put(Enchantment.PROTECTION_PROJECTILE, 4);
        enchantmentLevelMap.put(Enchantment.QUICK_CHARGE, 3);
        enchantmentLevelMap.put(Enchantment.RIPTIDE, 3);
        enchantmentLevelMap.put(Enchantment.SILK_TOUCH, 1);
        enchantmentLevelMap.put(Enchantment.SWEEPING_EDGE, 3);
        enchantmentLevelMap.put(Enchantment.THORNS, 3);
        enchantmentLevelMap.put(Enchantment.VANISHING_CURSE, 1);
        enchantmentLevelMap.put(Enchantment.WATER_WORKER, 1);
    }

    public void initializeTrackedBlocks() {
        Material[] trackedBlocks0 = new Material[]{Material.ACACIA_LOG, Material.ACACIA_LEAVES, Material.BIRCH_LOG, Material.BIRCH_LEAVES,
                Material.DARK_OAK_LOG, Material.DARK_OAK_LEAVES, Material.JUNGLE_LOG, Material.JUNGLE_LEAVES,
                Material.OAK_LOG, Material.OAK_LEAVES, Material.SPRUCE_LOG, Material.SPRUCE_LEAVES,
                Material.COAL_ORE, Material.DIAMOND_ORE, Material.EMERALD_ORE, Material.GOLD_ORE,
                Material.IRON_ORE, Material.LAPIS_ORE, Material.NETHER_QUARTZ_ORE, Material.REDSTONE_ORE,
                Material.SUGAR_CANE, Material.MELON, Material.PUMPKIN, Material.RED_MUSHROOM, Material.BROWN_MUSHROOM,
                Material.BAMBOO, Material.CACTUS, Material.SPAWNER};
        List<Material> trackedBlocks1 = new LinkedList<>(Arrays.asList(trackedBlocks0));
        if (mcVersion >= 1.16) {
            Material[] trackedBlocks_v1_16 = {Material.ANCIENT_DEBRIS, Material.NETHER_GOLD_ORE, Material.WARPED_STEM, Material.CRIMSON_STEM,
                                              Material.GILDED_BLACKSTONE};
            List<Material> trackedBlocks_append = Arrays.asList(trackedBlocks_v1_16);
            trackedBlocks1.addAll(trackedBlocks_append);
        }
        for (Material mat : trackedBlocks1) {
            trackedBlocks.putIfAbsent(mat,true);
        }
        ConfigLoad configLoad = new ConfigLoad();
        ExpMaps expMaps = new ExpMaps();
        if (!configLoad.isTrackFewerBlocks()) {
            for (Material mat : expMaps.getWoodcuttingEXP().keySet()) {
                trackedBlocks.putIfAbsent(mat,true);
            }
            for (Material mat : expMaps.getDiggingEXP().keySet()) {
                trackedBlocks.putIfAbsent(mat,true);
            }
            for (Material mat : expMaps.getFlamePickEXP().keySet()) {
                trackedBlocks.putIfAbsent(mat,true);
            }
            for (Material mat : expMaps.getMiningEXP().keySet()) {
                trackedBlocks.putIfAbsent(mat,true);
            }
        }
    }

    public void initializeBlocks() {
        Material[] tallCrops0 = {Material.SUGAR_CANE,Material.BAMBOO,Material.CACTUS,Material.KELP,Material.KELP_PLANT};
        tallCrops = Arrays.asList(tallCrops0);
        Material[] logs0 = {Material.ACACIA_LOG,Material.BIRCH_LOG,Material.DARK_OAK_LOG,Material.OAK_LOG,Material.SPRUCE_LOG,Material.JUNGLE_LOG,
                Material.STRIPPED_ACACIA_LOG,Material.STRIPPED_BIRCH_LOG,Material.STRIPPED_DARK_OAK_LOG,Material.STRIPPED_JUNGLE_LOG,
                Material.STRIPPED_OAK_LOG,Material.STRIPPED_SPRUCE_LOG};
        logs = new LinkedList<>(Arrays.asList(logs0));
        if (mcVersion >= 1.16) {
            Material[] logs_append0 = {Material.CRIMSON_STEM,Material.WARPED_STEM, Material.STRIPPED_CRIMSON_STEM,Material.STRIPPED_WARPED_STEM};
            List<Material> logs_append = Arrays.asList(logs_append0);
            logs.addAll(logs_append);
        }

        Material[] crops0 = {Material.WHEAT,Material.BEETROOTS,Material.CARROTS,Material.CHORUS_FLOWER,Material.MELON_STEM,Material.MELON,
                Material.NETHER_WART,Material.POTATOES,Material.PUMPKIN_STEM,Material.PUMPKIN,Material.SWEET_BERRY_BUSH,Material.COCOA};
        crops = Arrays.asList(crops0);
        Material[] ores0 = {Material.REDSTONE_ORE,Material.NETHER_QUARTZ_ORE,Material.LAPIS_ORE,Material.IRON_ORE,Material.GOLD_ORE,
                Material.EMERALD_ORE,Material.DIAMOND_ORE,Material.COAL_ORE};
        ores = new LinkedList<>(Arrays.asList(ores0));
        if (mcVersion >= 1.16) {
            Material[] ores_append0 = {Material.NETHER_GOLD_ORE,Material.ANCIENT_DEBRIS,Material.GILDED_BLACKSTONE};
            List<Material> ores_append = Arrays.asList(ores_append0);
            ores.addAll(ores_append);
        }
        Material[] flamePickOres0 = {Material.IRON_ORE,Material.GOLD_ORE};
        flamePickOres = new LinkedList<>(Arrays.asList(flamePickOres0));
        if (mcVersion >= 1.16) {
            Material[] flamePickOres_append0 = {Material.NETHER_GOLD_ORE,Material.ANCIENT_DEBRIS};
            List<Material> flamePickOres_append = Arrays.asList(flamePickOres_append0);
            flamePickOres.addAll(flamePickOres_append);
        }
        Material[] strippedLogs0 = {Material.STRIPPED_SPRUCE_LOG,Material.STRIPPED_OAK_LOG,Material.STRIPPED_JUNGLE_LOG,Material.STRIPPED_DARK_OAK_LOG,Material.STRIPPED_BIRCH_LOG,Material.STRIPPED_ACACIA_LOG};
        strippedLogs = Arrays.asList(strippedLogs0);
        Material[] wood0 = {Material.SPRUCE_WOOD,Material.OAK_WOOD,Material.JUNGLE_WOOD,Material.DARK_OAK_WOOD,Material.BIRCH_WOOD,Material.ACACIA_WOOD};
        wood = Arrays.asList(wood0);
        Material[] strippedWood0 = {Material.STRIPPED_SPRUCE_WOOD,Material.STRIPPED_OAK_WOOD,Material.STRIPPED_JUNGLE_WOOD,Material.STRIPPED_DARK_OAK_WOOD,Material.STRIPPED_BIRCH_WOOD,Material.STRIPPED_ACACIA_WOOD};
        strippedWood = Arrays.asList(strippedWood0);
        allLogs.addAll(logs);
        allLogs.addAll(strippedLogs);
        allLogs.addAll(wood);
        allLogs.addAll(strippedWood);
        Material[] leaves0 = {Material.ACACIA_LEAVES,Material.BIRCH_LEAVES,Material.DARK_OAK_LEAVES,Material.OAK_LEAVES,Material.SPRUCE_LEAVES,Material.JUNGLE_LEAVES};
        leaves = Arrays.asList(leaves0);

        ConfigLoad configLoad = new ConfigLoad();
        veinMinerBlocks = configLoad.getVeinMinerBlocks();
    }

    public void initializeHarmfulPotions(){
        PotionEffectType[] harmfulEffects0 = {PotionEffectType.WEAKNESS,PotionEffectType.POISON,PotionEffectType.BLINDNESS,PotionEffectType.HUNGER,
                PotionEffectType.HARM,PotionEffectType.SLOW_DIGGING,PotionEffectType.SLOW,PotionEffectType.WEAKNESS,PotionEffectType.WITHER};
        harmfulEffects = Arrays.asList(harmfulEffects0);
    }

    public void initializeActionItems() {
        Material[] noRightClick0 = {Material.AIR,Material.ARROW,Material.SPECTRAL_ARROW, Material.TIPPED_ARROW,Material.BLAZE_POWDER,
                Material.BLAZE_ROD, Material.BONE,Material.BOOK,Material.BOWL, Material.CHARCOAL,
                Material.COAL,Material.CLAY,Material.BRICK,Material.COMPASS,Material.DIAMOND_HORSE_ARMOR,Material.DRAGON_BREATH,
                Material.MAP,Material.ENCHANTED_BOOK,Material.FEATHER,Material.FERMENTED_SPIDER_EYE,Material.FIREWORK_STAR,
                Material.FLINT,Material.GHAST_TEAR,Material.GLISTERING_MELON_SLICE,Material.GLOWSTONE_DUST,Material.GOLDEN_HORSE_ARMOR,
                Material.GOLD_NUGGET,Material.GOLD_INGOT,Material.GUNPOWDER,Material.INK_SAC,Material.IRON_HORSE_ARMOR,
                Material.MAGMA_CREAM,Material.NETHER_BRICK,Material.NETHER_WART,Material.PAPER,Material.PRISMARINE_SHARD,Material.PRISMARINE_CRYSTALS,
                Material.RABBIT_HIDE,Material.LEATHER,Material.LEATHER_HORSE_ARMOR,Material.RABBIT_FOOT,Material.SADDLE,Material.SHEARS,
                Material.SLIME_BALL,Material.SHULKER_SHELL,Material.SPIDER_EYE,Material.STICK,Material.STRING,Material.TOTEM_OF_UNDYING,
                Material.CLOCK,Material.WRITTEN_BOOK};
        noRightClick = Arrays.asList(noRightClick0);
        Material[] actionableBlocks0 = {Material.ANVIL, Material.BLACK_BED, Material.BLUE_BED, Material.BROWN_BED, Material.CYAN_BED, Material.GRAY_BED,
                Material.GREEN_BED,Material.LIGHT_BLUE_BED,Material.LIGHT_GRAY_BED,Material.LIME_BED,Material.MAGENTA_BED,
                Material.ORANGE_BED,Material.PINK_BED,Material.PURPLE_BED,Material.RED_BED,Material.WHITE_BED,Material.YELLOW_BED,
                Material.BELL,Material.BLAST_FURNACE,Material.BREWING_STAND,Material.CARTOGRAPHY_TABLE,Material.CHEST,Material.COMPOSTER,
                Material.CRAFTING_TABLE,Material.ACACIA_DOOR,Material.BIRCH_DOOR,Material.DARK_OAK_DOOR,Material.IRON_DOOR,Material.JUNGLE_DOOR,
                Material.OAK_DOOR,Material.SPRUCE_DOOR,Material.ENCHANTING_TABLE,Material.ACACIA_FENCE_GATE,Material.BIRCH_FENCE_GATE,
                Material.DARK_OAK_FENCE_GATE,Material.JUNGLE_FENCE_GATE,Material.OAK_FENCE_GATE,Material.SPRUCE_FENCE_GATE,Material.FURNACE,
                Material.GRINDSTONE,Material.JUKEBOX,Material.LECTERN,Material.LOOM,Material.NOTE_BLOCK,Material.SMOKER,Material.STONECUTTER,
                Material.TRAPPED_CHEST,Material.ACACIA_TRAPDOOR,Material.BIRCH_TRAPDOOR,Material.DARK_OAK_TRAPDOOR,Material.IRON_TRAPDOOR,
                Material.JUNGLE_TRAPDOOR,Material.OAK_TRAPDOOR,Material.SPRUCE_TRAPDOOR,Material.BARREL,Material.ACACIA_BUTTON,Material.BIRCH_BUTTON,
                Material.DARK_OAK_BUTTON,Material.OAK_BUTTON,Material.JUNGLE_BUTTON,Material.SPRUCE_BUTTON,Material.STONE_BUTTON,Material.REPEATER,
                Material.COMPARATOR,Material.HOPPER,Material.HOPPER_MINECART,Material.DAYLIGHT_DETECTOR,Material.LEVER,Material.FURNACE_MINECART,
                Material.DISPENSER,Material.DROPPER,Material.CHIPPED_ANVIL,Material.DAMAGED_ANVIL,Material.SHULKER_BOX,Material.ENDER_CHEST,Material.BLACK_SHULKER_BOX,
                Material.BLUE_SHULKER_BOX,Material.BROWN_SHULKER_BOX,Material.CYAN_SHULKER_BOX,Material.GRAY_SHULKER_BOX,Material.GREEN_SHULKER_BOX,Material.LIGHT_BLUE_SHULKER_BOX,
                Material.LIGHT_GRAY_SHULKER_BOX,Material.LIME_SHULKER_BOX,Material.MAGENTA_SHULKER_BOX, Material.RED_SHULKER_BOX,Material.WHITE_SHULKER_BOX,Material.YELLOW_SHULKER_BOX,
                Material.ORANGE_SHULKER_BOX, Material.PURPLE_SHULKER_BOX,Material.PINK_SHULKER_BOX};
        actionableBlocks = Arrays.asList(actionableBlocks0);
    }

    public void initializeTools() {
        Material[] pickaxes0 = {Material.DIAMOND_PICKAXE, Material.GOLDEN_PICKAXE, Material.IRON_PICKAXE, Material.STONE_PICKAXE, Material.WOODEN_PICKAXE};
        pickaxes = new LinkedList<>(Arrays.asList(pickaxes0));
        Material[] axes0 = {Material.DIAMOND_AXE,Material.GOLDEN_AXE,Material.IRON_AXE, Material.STONE_AXE,Material.WOODEN_AXE};
        axes = new LinkedList<>(Arrays.asList(axes0));
        Material[] shovels0 = {Material.DIAMOND_SHOVEL,Material.GOLDEN_SHOVEL,Material.IRON_SHOVEL, Material.STONE_SHOVEL,Material.WOODEN_SHOVEL};
        shovels = new LinkedList<>(Arrays.asList(shovels0));
        Material[] hoes0 = {Material.DIAMOND_HOE,Material.GOLDEN_HOE,Material.IRON_HOE, Material.STONE_HOE,Material.WOODEN_HOE};
        hoes = new LinkedList<>(Arrays.asList(hoes0));
        Material[] swords0 = {Material.WOODEN_SWORD,Material.STONE_SWORD,Material.GOLDEN_SWORD,Material.DIAMOND_SWORD,Material.IRON_SWORD};
        swords = new LinkedList<>(Arrays.asList(swords0));
        if (mcVersion >= 1.16) {
            pickaxes.add(Material.NETHERITE_PICKAXE);
            axes.add(Material.NETHERITE_AXE);
            shovels.add(Material.NETHERITE_SHOVEL);
            hoes.add(Material.NETHERITE_HOE);
            swords.add(Material.NETHERITE_SWORD);
        }
    }

    public void initializeLeftClickItems(){
        Material[] leftClickItems0 = {Material.IRON_SWORD,Material.DIAMOND_SWORD,Material.GOLDEN_SWORD,Material.STONE_SWORD,Material.WOODEN_SWORD,
                Material.IRON_PICKAXE,Material.DIAMOND_PICKAXE,Material.STONE_PICKAXE,Material.GOLDEN_PICKAXE,Material.WOODEN_PICKAXE,
                Material.IRON_SHOVEL,Material.DIAMOND_SHOVEL,Material.GOLDEN_SHOVEL,Material.STONE_SHOVEL,Material.WOODEN_SHOVEL,
                Material.IRON_AXE,Material.DIAMOND_AXE,Material.GOLDEN_AXE,Material.STONE_AXE,Material.WOODEN_AXE,
                Material.BOW,Material.CROSSBOW};
        leftClickItems = new LinkedList<>(Arrays.asList(leftClickItems0));
        if (mcVersion >=  1.16) {
            Material[] leftClickItems_append0 = {Material.NETHERITE_SWORD,Material.NETHERITE_SHOVEL,Material.NETHERITE_AXE,Material.NETHERITE_PICKAXE};
            List<Material> leftClickItems_append = Arrays.asList(leftClickItems_append0);
            leftClickItems.addAll(leftClickItems_append);
        }
    }

    public void initializeIngredients() {
        ConfigLoad configLoad = new ConfigLoad();
        Map<String, OldCustomPotion> alchemyInfo = configLoad.getAlchemyInfo();
        Material[] newIngredients0 = {alchemyInfo.get("customPotion1").getIngredient(),alchemyInfo.get("customPotion2").getIngredient(),alchemyInfo.get("customPotion3").getIngredient(),
                                      alchemyInfo.get("customPotion4").getIngredient(),alchemyInfo.get("customPotion5").getIngredient()};
        newIngredients = Arrays.asList(newIngredients0);
        Material[] oldIngredients0 = {Material.NETHER_WART,Material.GUNPOWDER,Material.GLOWSTONE_DUST,Material.SPIDER_EYE,Material.GHAST_TEAR,
                Material.RABBIT_FOOT,Material.BLAZE_POWDER,Material.GLISTERING_MELON_SLICE,Material.SUGAR,Material.MAGMA_CREAM,
                Material.REDSTONE, Material.PUFFERFISH, Material.GOLDEN_CARROT,Material.TURTLE_HELMET,Material.PHANTOM_MEMBRANE,
                Material.FERMENTED_SPIDER_EYE};
        oldIngredients = Arrays.asList(oldIngredients0);
    }

    public void initializeCustomPostions() {
        ConfigLoad configLoad = new ConfigLoad();
        Map<String, OldCustomPotion> alchemyInfo = configLoad.getAlchemyInfo();

        //Hero Potion
        heroPotion = alchemyInfo.get("customPotion1").getPotionItemStack();

        //Mining Fatigue Potion
        fatiguePotion = alchemyInfo.get("customPotion2").getPotionItemStack();

        //Haste Potion
        hastePotion = alchemyInfo.get("customPotion3").getPotionItemStack();

        //decay Potion
        decayPotion = alchemyInfo.get("customPotion4").getPotionItemStack();

        //resistance Potion
        resistancePotion = alchemyInfo.get("customPotion5").getPotionItemStack();
    }

    public void initializeFoodMaps(){
        farmFood.put(Material.GOLDEN_APPLE,4);
        farmFoodSaturation.put(Material.GOLDEN_APPLE,13.6);
        farmFood.put(Material.GOLDEN_CARROT,6);
        farmFoodSaturation.put(Material.GOLDEN_CARROT,14.4);
        farmFood.put(Material.BAKED_POTATO,5);
        farmFoodSaturation.put(Material.BAKED_POTATO,6.0);
        farmFood.put(Material.BEETROOT,1);
        farmFoodSaturation.put(Material.BEETROOT,1.2);
        farmFood.put(Material.BEETROOT_SOUP,6);
        farmFoodSaturation.put(Material.BEETROOT_SOUP,7.2);
        farmFood.put(Material.BREAD,5);
        farmFoodSaturation.put(Material.BREAD,6.0);
        farmFood.put(Material.CARROT,3);
        farmFoodSaturation.put(Material.CARROT,3.6);
        farmFood.put(Material.MUSHROOM_STEW,6);
        farmFoodSaturation.put(Material.MUSHROOM_STEW,13.2);
        farmFood.put(Material.APPLE,4);
        farmFoodSaturation.put(Material.APPLE,2.4);
        farmFood.put(Material.CHORUS_FRUIT,4);
        farmFoodSaturation.put(Material.CHORUS_FRUIT,2.4);
        farmFood.put(Material.DRIED_KELP,1);
        farmFoodSaturation.put(Material.DRIED_KELP,0.6);
        farmFood.put(Material.MELON_SLICE,2);
        farmFoodSaturation.put(Material.MELON_SLICE,1.2);
        farmFood.put(Material.POTATO,1);
        farmFoodSaturation.put(Material.POTATO,0.6);
        farmFood.put(Material.PUMPKIN_PIE,8);
        farmFoodSaturation.put(Material.PUMPKIN_PIE,4.8);
        farmFood.put(Material.CAKE,2);
        farmFoodSaturation.put(Material.CAKE,0.4);
        farmFood.put(Material.COOKIE,2);
        farmFoodSaturation.put(Material.COOKIE,0.4);
        farmFood.put(Material.HONEY_BOTTLE,6);
        farmFoodSaturation.put(Material.HONEY_BOTTLE,1.2);
        farmFood.put(Material.SWEET_BERRIES,2);
        farmFoodSaturation.put(Material.SWEET_BERRIES,0.4);

        meatFood.put(Material.COOKED_MUTTON,6);
        meatFoodSaturation.put(Material.COOKED_MUTTON,9.6);
        meatFood.put(Material.COOKED_PORKCHOP,8);
        meatFoodSaturation.put(Material.COOKED_PORKCHOP,12.8);
        meatFood.put(Material.COOKED_BEEF,8);
        meatFoodSaturation.put(Material.COOKED_BEEF,12.8);
        meatFood.put(Material.COOKED_CHICKEN,6);
        meatFoodSaturation.put(Material.COOKED_CHICKEN,7.2);
        meatFood.put(Material.COOKED_RABBIT,5);
        meatFoodSaturation.put(Material.COOKED_RABBIT,6.0);
        meatFood.put(Material.RABBIT_STEW,10);
        meatFoodSaturation.put(Material.RABBIT_STEW,12.0);
        meatFood.put(Material.BEEF,3);
        meatFoodSaturation.put(Material.BEEF,1.8);
        meatFood.put(Material.MUTTON,2);
        meatFoodSaturation.put(Material.MUTTON,1.2);
        meatFood.put(Material.PORKCHOP,3);
        meatFoodSaturation.put(Material.PORKCHOP,1.8);
        meatFood.put(Material.RABBIT,3);
        meatFoodSaturation.put(Material.RABBIT,1.8);
        meatFood.put(Material.CHICKEN,2);
        meatFoodSaturation.put(Material.CHICKEN,1.2);
        meatFood.put(Material.ROTTEN_FLESH,4);
        meatFoodSaturation.put(Material.ROTTEN_FLESH,0.8);

        fishFood.put(Material.COOKED_SALMON,6);
        fishFoodSaturation.put(Material.COOKED_SALMON,1.6);
        fishFood.put(Material.COOKED_COD,5);
        fishFoodSaturation.put(Material.COOKED_COD,1.2);
        fishFood.put(Material.COD,2);
        fishFoodSaturation.put(Material.COD,0.4);
        fishFood.put(Material.SALMON,2);
        fishFoodSaturation.put(Material.SALMON,0.4);
        fishFood.put(Material.TROPICAL_FISH,1);
        fishFoodSaturation.put(Material.TROPICAL_FISH,0.2);
        fishFood.put(Material.PUFFERFISH,1);
        fishFoodSaturation.put(Material.PUFFERFISH,0.2);
        fishFood.put(Material.DRIED_KELP,1);
        fishFoodSaturation.put(Material.DRIED_KELP,0.6);
    }

    public void initializeValuableItems() {
        Material[] valuableItems0 = {Material.IRON_ORE,Material.GOLD_ORE,Material.DIAMOND_ORE,Material.EMERALD_ORE,Material.REDSTONE_ORE,Material.LAPIS_ORE,
                Material.IRON_BLOCK,Material.GOLD_BLOCK,Material.DIAMOND_BLOCK,Material.EMERALD_BLOCK,Material.REDSTONE_BLOCK,Material.LAPIS_BLOCK,
                Material.SLIME_BLOCK,Material.SPONGE,Material.NETHER_QUARTZ_ORE,Material.NETHER_WART_BLOCK,Material.DRAGON_EGG,Material.SHULKER_BOX,
                Material.ENCHANTING_TABLE,Material.ANVIL,Material.BEACON,Material.BREWING_STAND,Material.CAKE,Material.JUKEBOX,
                Material.TNT,Material.CREEPER_HEAD,Material.DRAGON_HEAD,Material.PLAYER_HEAD,Material.ZOMBIE_HEAD,Material.SKELETON_SKULL,Material.WITHER_SKELETON_SKULL,
                Material.ENDER_EYE,Material.ENDER_PEARL,Material.FIREWORK_ROCKET,Material.FIRE_CHARGE,Material.POTION,Material.SPLASH_POTION,Material.LINGERING_POTION,
                Material.NETHER_WART,Material.REDSTONE,Material.TRIDENT,Material.DIAMOND_AXE,Material.DIAMOND_BOOTS,Material.DIAMOND_CHESTPLATE,
                Material.DIAMOND_HELMET,Material.DIAMOND_HOE,Material.DIAMOND_LEGGINGS,Material.DIAMOND_PICKAXE,Material.DIAMOND_SHOVEL,Material.DIAMOND_SWORD,
                Material.ELYTRA,Material.ENCHANTED_BOOK,Material.ENCHANTED_GOLDEN_APPLE,Material.GOLDEN_APPLE,Material.IRON_INGOT,
                Material.MUSIC_DISC_11, Material.MUSIC_DISC_13, Material.MUSIC_DISC_BLOCKS, Material.MUSIC_DISC_CAT,
                Material.MUSIC_DISC_CHIRP, Material.MUSIC_DISC_FAR, Material.MUSIC_DISC_MALL, Material.MUSIC_DISC_MELLOHI,
                Material.MUSIC_DISC_STAL, Material.MUSIC_DISC_STRAD, Material.MUSIC_DISC_WAIT, Material.MUSIC_DISC_WARD,
                Material.NAME_TAG,Material.TIPPED_ARROW,Material.TOTEM_OF_UNDYING,Material.SPECTRAL_ARROW,Material.DIAMOND,Material.GOLD_INGOT,
                Material.HEART_OF_THE_SEA,Material.DRAGON_BREATH,Material.EMERALD,Material.NAUTILUS_SHELL,Material.NETHER_STAR,Material.SLIME_BALL,
                Material.RABBIT_FOOT};
        valuableItems = new LinkedList<>(Arrays.asList(valuableItems0));
        if (mcVersion >= 1.16) {
            Material[] valuableItems_append0 =  {Material.NETHERITE_SWORD,Material.NETHERITE_HOE,Material.NETHERITE_SHOVEL,Material.NETHERITE_AXE,Material.NETHERITE_PICKAXE,Material.NETHER_GOLD_ORE,
                    Material.NETHERITE_BLOCK,Material.NETHERITE_BOOTS,Material.NETHERITE_CHESTPLATE,Material.NETHERITE_HELMET,Material.NETHERITE_INGOT,Material.NETHERITE_LEGGINGS,
                    Material.NETHERITE_SCRAP,Material.ANCIENT_DEBRIS};
            List<Material> valueableItems_append = Arrays.asList(valuableItems_append0);
            valuableItems.addAll(valueableItems_append);
        }
    }

    public void initializeRepairItems() {
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
        repairItems.put(Material.SHEARS,Material.IRON_INGOT);
        repairItems.put(Material.FISHING_ROD,Material.STRING);
        repairItems.put(Material.CARROT_ON_A_STICK,Material.CARROT);
        repairItems.put(Material.FLINT_AND_STEEL,Material.FLINT);
        repairItems.put(Material.BOW,Material.STRING);
        repairItems.put(Material.TRIDENT,Material.PRISMARINE_BRICKS);
        repairItems.put(Material.ELYTRA,Material.PHANTOM_MEMBRANE);
        repairItems.put(Material.SHIELD,Material.IRON_INGOT);
        repairItems.put(Material.CROSSBOW,Material.STRING);
        if (mcVersion >= 1.16) {
            repairItems.put(Material.NETHERITE_AXE,Material.NETHERITE_SCRAP);
            repairItems.put(Material.NETHERITE_HOE,Material.NETHERITE_SCRAP);
            repairItems.put(Material.NETHERITE_PICKAXE,Material.NETHERITE_SCRAP);
            repairItems.put(Material.NETHERITE_SWORD,Material.NETHERITE_SCRAP);
            repairItems.put(Material.NETHERITE_SHOVEL,Material.NETHERITE_SCRAP);
            repairItems.put(Material.NETHERITE_HELMET,Material.NETHERITE_SCRAP);
            repairItems.put(Material.NETHERITE_CHESTPLATE,Material.NETHERITE_SCRAP);
            repairItems.put(Material.NETHERITE_LEGGINGS,Material.NETHERITE_SCRAP);
            repairItems.put(Material.NETHERITE_BOOTS,Material.NETHERITE_SCRAP);
        }

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
        repairItemsAmount.put(Material.SHEARS,2);
        repairItemsAmount.put(Material.FISHING_ROD,2);
        if (mcVersion >= 1.16) {
            repairItemsAmount.put(Material.NETHERITE_AXE,3);
            repairItemsAmount.put(Material.NETHERITE_HOE,2);
            repairItemsAmount.put(Material.NETHERITE_PICKAXE,3);
            repairItemsAmount.put(Material.NETHERITE_SWORD,2);
            repairItemsAmount.put(Material.NETHERITE_SHOVEL,1);
            repairItemsAmount.put(Material.NETHERITE_HELMET,5);
            repairItemsAmount.put(Material.NETHERITE_CHESTPLATE,8);
            repairItemsAmount.put(Material.NETHERITE_LEGGINGS,7);
            repairItemsAmount.put(Material.NETHERITE_BOOTS,4);
        }

        repairItemsAmount.put(Material.BOW,3);

        repairItemsAmount.put(Material.ELYTRA,10);
        repairItemsAmount.put(Material.SHIELD,1);
        repairItemsAmount.put(Material.CROSSBOW,2);
    }

    public void initalizeArrows() {
        PotionType[] potionType = {PotionType.WATER,PotionType.MUNDANE,PotionType.THICK,PotionType.AWKWARD,PotionType.NIGHT_VISION,
                PotionType.INVISIBILITY,PotionType.JUMP,PotionType.FIRE_RESISTANCE,PotionType.SPEED,PotionType.SLOWNESS,
                PotionType.WATER_BREATHING,PotionType.INSTANT_HEAL,PotionType.INSTANT_DAMAGE,PotionType.POISON,
                PotionType.REGEN,PotionType.STRENGTH,PotionType.WEAKNESS,PotionType.LUCK,PotionType.TURTLE_MASTER,PotionType.SLOW_FALLING};
        String[] labels = {"water","mundane","thick","awkward","night_vision","invisibility","leaping","fire_resistance","swiftness","slowness",
                "breathing_water","healing","harming","poison","regeneration","strength","weakness","luck","turtle_master","slow_falling"};
        String[] modifiers = {"","long_","strong_"};
        for (int i = 0; i<labels.length; i++) {
            for (String modifier : modifiers) {
                if (modifier.equalsIgnoreCase("")) {
                    ItemStack arrow = new ItemStack(Material.TIPPED_ARROW, 8);
                    PotionMeta arrowMeta = (PotionMeta) arrow.getItemMeta();
                    arrowMeta.setBasePotionData(new PotionData(potionType[i],false,false));
                    arrow.setItemMeta(arrowMeta);
                    effectArrows.put(modifier+labels[i],arrow);
                }
                else if (modifier.equalsIgnoreCase("long_")) {
                    if (potionType[i].isExtendable()) {
                        ItemStack arrow = new ItemStack(Material.TIPPED_ARROW, 8);
                        PotionMeta arrowMeta = (PotionMeta) arrow.getItemMeta();
                        arrowMeta.setBasePotionData(new PotionData(potionType[i], true, false));
                        arrow.setItemMeta(arrowMeta);
                        effectArrows.put(modifier + labels[i], arrow);
                    }
                }
                else {
                    if (potionType[i].isUpgradeable()) {
                        ItemStack arrow = new ItemStack(Material.TIPPED_ARROW, 8);
                        PotionMeta arrowMeta = (PotionMeta) arrow.getItemMeta();
                        arrowMeta.setBasePotionData(new PotionData(potionType[i], false, true));
                        arrow.setItemMeta(arrowMeta);
                        effectArrows.put(modifier + labels[i], arrow);
                    }
                }
            }
        }

    }

    public void initializeSmeltableItemsMap(){
        smeltableItemsMap.put(Material.BEEF,Material.COOKED_BEEF);
        smeltableItemsMap.put(Material.CHICKEN,Material.COOKED_CHICKEN);
        smeltableItemsMap.put(Material.COD,Material.COOKED_COD);
        smeltableItemsMap.put(Material.SALMON,Material.COOKED_SALMON);
        smeltableItemsMap.put(Material.POTATO,Material.BAKED_POTATO);
        smeltableItemsMap.put(Material.MUTTON,Material.COOKED_MUTTON);
        smeltableItemsMap.put(Material.RABBIT,Material.COOKED_RABBIT);
        smeltableItemsMap.put(Material.KELP,Material.DRIED_KELP);
        smeltableItemsMap.put(Material.IRON_ORE,Material.IRON_INGOT);
        smeltableItemsMap.put(Material.GOLD_ORE,Material.GOLD_INGOT);
        smeltableItemsMap.put(Material.SAND,Material.GLASS);
        smeltableItemsMap.put(Material.RED_SAND,Material.GLASS);
        smeltableItemsMap.put(Material.COBBLESTONE,Material.STONE);
        smeltableItemsMap.put(Material.SANDSTONE,Material.SMOOTH_SANDSTONE);
        smeltableItemsMap.put(Material.RED_SANDSTONE,Material.SMOOTH_RED_SANDSTONE);
        smeltableItemsMap.put(Material.STONE,Material.SMOOTH_STONE);
        smeltableItemsMap.put(Material.QUARTZ_BLOCK,Material.SMOOTH_QUARTZ);
        smeltableItemsMap.put(Material.CLAY_BALL,Material.BRICK);
        smeltableItemsMap.put(Material.NETHERRACK,Material.NETHER_BRICK);
        smeltableItemsMap.put(Material.CLAY,Material.TERRACOTTA);
        smeltableItemsMap.put(Material.STONE_BRICKS,Material.CRACKED_STONE_BRICKS);
        smeltableItemsMap.put(Material.BLACK_TERRACOTTA,Material.BLACK_GLAZED_TERRACOTTA);
        smeltableItemsMap.put(Material.WHITE_TERRACOTTA,Material.WHITE_GLAZED_TERRACOTTA);
        smeltableItemsMap.put(Material.LIGHT_GRAY_TERRACOTTA,Material.LIGHT_GRAY_GLAZED_TERRACOTTA);
        smeltableItemsMap.put(Material.GRAY_TERRACOTTA,Material.LIGHT_GRAY_GLAZED_TERRACOTTA);
        smeltableItemsMap.put(Material.BROWN_TERRACOTTA,Material.BROWN_GLAZED_TERRACOTTA);
        smeltableItemsMap.put(Material.RED_TERRACOTTA,Material.RED_GLAZED_TERRACOTTA);
        smeltableItemsMap.put(Material.ORANGE_TERRACOTTA,Material.ORANGE_GLAZED_TERRACOTTA);
        smeltableItemsMap.put(Material.YELLOW_TERRACOTTA,Material.YELLOW_GLAZED_TERRACOTTA);
        smeltableItemsMap.put(Material.LIME_TERRACOTTA,Material.LIME_GLAZED_TERRACOTTA);
        smeltableItemsMap.put(Material.GREEN_TERRACOTTA,Material.GREEN_GLAZED_TERRACOTTA);
        smeltableItemsMap.put(Material.CYAN_TERRACOTTA,Material.CYAN_GLAZED_TERRACOTTA);
        smeltableItemsMap.put(Material.LIGHT_BLUE_TERRACOTTA,Material.LIGHT_BLUE_GLAZED_TERRACOTTA);
        smeltableItemsMap.put(Material.BLUE_TERRACOTTA,Material.BLUE_GLAZED_TERRACOTTA);
        smeltableItemsMap.put(Material.PURPLE_TERRACOTTA,Material.PURPLE_GLAZED_TERRACOTTA);
        smeltableItemsMap.put(Material.MAGENTA_TERRACOTTA,Material.MAGENTA_GLAZED_TERRACOTTA);
        smeltableItemsMap.put(Material.PINK_TERRACOTTA,Material.PINK_GLAZED_TERRACOTTA);
        smeltableItemsMap.put(Material.CACTUS,Material.GREEN_DYE);
        for (Material log : logs) {
            smeltableItemsMap.put(log,Material.CHARCOAL);
        }
        for (Material strippedLog : strippedLogs) {
            smeltableItemsMap.put(strippedLog,Material.CHARCOAL);
        }
        for (Material strippedLog : wood) {
            smeltableItemsMap.put(strippedLog,Material.CHARCOAL);
        }
        for (Material strippedLog : strippedWood) {
            smeltableItemsMap.put(strippedLog,Material.CHARCOAL);
        }
        smeltableItemsMap.put(Material.CHORUS_FRUIT,Material.POPPED_CHORUS_FRUIT);
        smeltableItemsMap.put(Material.WET_SPONGE,Material.SPONGE);
        smeltableItemsMap.put(Material.SEA_PICKLE,Material.LIME_DYE);
        smeltableItemsMap.put(Material.DIAMOND_ORE,Material.DIAMOND);
        smeltableItemsMap.put(Material.LAPIS_ORE,Material.LAPIS_LAZULI);
        smeltableItemsMap.put(Material.REDSTONE_ORE,Material.REDSTONE);
        smeltableItemsMap.put(Material.COAL_ORE,Material.COAL);
        smeltableItemsMap.put(Material.EMERALD_ORE,Material.EMERALD);
        smeltableItemsMap.put(Material.NETHER_QUARTZ_ORE,Material.QUARTZ);

        smeltableItemsMap.put(Material.IRON_SWORD,Material.IRON_NUGGET);
        smeltableItemsMap.put(Material.IRON_PICKAXE,Material.IRON_NUGGET);
        smeltableItemsMap.put(Material.IRON_AXE,Material.IRON_NUGGET);
        smeltableItemsMap.put(Material.IRON_SHOVEL,Material.IRON_NUGGET);
        smeltableItemsMap.put(Material.IRON_HOE,Material.IRON_NUGGET);
        smeltableItemsMap.put(Material.CHAINMAIL_HELMET,Material.IRON_NUGGET);
        smeltableItemsMap.put(Material.CHAINMAIL_CHESTPLATE,Material.IRON_NUGGET);
        smeltableItemsMap.put(Material.CHAINMAIL_LEGGINGS,Material.IRON_NUGGET);
        smeltableItemsMap.put(Material.CHAINMAIL_BOOTS,Material.IRON_NUGGET);
        smeltableItemsMap.put(Material.IRON_HELMET,Material.IRON_NUGGET);
        smeltableItemsMap.put(Material.IRON_CHESTPLATE,Material.IRON_NUGGET);
        smeltableItemsMap.put(Material.IRON_LEGGINGS,Material.IRON_NUGGET);
        smeltableItemsMap.put(Material.IRON_BOOTS,Material.IRON_NUGGET);
        smeltableItemsMap.put(Material.IRON_HORSE_ARMOR,Material.IRON_NUGGET);

        smeltableItemsMap.put(Material.GOLDEN_SWORD,Material.GOLD_NUGGET);
        smeltableItemsMap.put(Material.GOLDEN_PICKAXE,Material.GOLD_NUGGET);
        smeltableItemsMap.put(Material.GOLDEN_AXE,Material.GOLD_NUGGET);
        smeltableItemsMap.put(Material.GOLDEN_SHOVEL,Material.GOLD_NUGGET);
        smeltableItemsMap.put(Material.GOLDEN_HOE,Material.GOLD_NUGGET);
        smeltableItemsMap.put(Material.GOLDEN_HELMET,Material.GOLD_NUGGET);
        smeltableItemsMap.put(Material.GOLDEN_CHESTPLATE,Material.GOLD_NUGGET);
        smeltableItemsMap.put(Material.GOLDEN_LEGGINGS,Material.GOLD_NUGGET);
        smeltableItemsMap.put(Material.GOLDEN_BOOTS,Material.GOLD_NUGGET);
        smeltableItemsMap.put(Material.GOLDEN_HORSE_ARMOR,Material.GOLD_NUGGET);


        if (mcVersion >= 1.16) {
            smeltableItemsMap.put(Material.ANCIENT_DEBRIS,Material.NETHERITE_SCRAP);
            smeltableItemsMap.put(Material.NETHER_GOLD_ORE,Material.GOLD_INGOT);
            smeltableItemsMap.put(Material.NETHER_BRICK,Material.CRACKED_NETHER_BRICKS);

        }

    }

    public void initializeSmeltingXPMap() {
        smeltingXPMap.put(Material.BEEF,0.35);
        smeltingXPMap.put(Material.CHICKEN,0.35);
        smeltingXPMap.put(Material.COD,0.35);
        smeltingXPMap.put(Material.SALMON,0.35);
        smeltingXPMap.put(Material.POTATO,0.35);
        smeltingXPMap.put(Material.MUTTON,0.35);
        smeltingXPMap.put(Material.RABBIT,0.35);
        smeltingXPMap.put(Material.KELP,0.35);

        smeltingXPMap.put(Material.IRON_ORE,0.7);
        smeltingXPMap.put(Material.GOLD_ORE,1.0);
        smeltingXPMap.put(Material.SAND,0.1);
        smeltingXPMap.put(Material.RED_SAND,0.1);
        smeltingXPMap.put(Material.COBBLESTONE,0.1);
        smeltingXPMap.put(Material.SANDSTONE,0.1);
        smeltingXPMap.put(Material.RED_SANDSTONE,0.1);
        smeltingXPMap.put(Material.STONE,0.1);
        smeltingXPMap.put(Material.CLAY_BALL,0.3);
        smeltingXPMap.put(Material.NETHERRACK,0.1);
        smeltingXPMap.put(Material.CLAY,0.35);
        smeltingXPMap.put(Material.STONE_BRICKS,0.1);
        smeltingXPMap.put(Material.BLACK_TERRACOTTA,0.1);
        smeltingXPMap.put(Material.WHITE_TERRACOTTA,0.1);
        smeltingXPMap.put(Material.LIGHT_GRAY_TERRACOTTA,0.1);
        smeltingXPMap.put(Material.GRAY_TERRACOTTA,0.1);
        smeltingXPMap.put(Material.BROWN_TERRACOTTA,0.1);
        smeltingXPMap.put(Material.RED_TERRACOTTA,0.1);
        smeltingXPMap.put(Material.ORANGE_TERRACOTTA,0.1);
        smeltingXPMap.put(Material.YELLOW_TERRACOTTA,0.1);
        smeltingXPMap.put(Material.LIME_TERRACOTTA,0.1);
        smeltingXPMap.put(Material.GREEN_TERRACOTTA,0.1);
        smeltingXPMap.put(Material.CYAN_TERRACOTTA,0.1);
        smeltingXPMap.put(Material.LIGHT_BLUE_TERRACOTTA,0.1);
        smeltingXPMap.put(Material.BLUE_TERRACOTTA,0.1);
        smeltingXPMap.put(Material.PURPLE_TERRACOTTA,0.1);
        smeltingXPMap.put(Material.MAGENTA_TERRACOTTA,0.1);
        smeltingXPMap.put(Material.PINK_TERRACOTTA,0.1);
        smeltingXPMap.put(Material.CACTUS,1.0);
        for (Material log : logs) {
            smeltingXPMap.put(log,0.15);
        }
        for (Material strippedLog : strippedLogs) {
            smeltingXPMap.put(strippedLog,0.15);
        }
        for (Material strippedLog : wood) {
            smeltingXPMap.put(strippedLog,0.15);
        }
        for (Material strippedLog : strippedWood) {
            smeltingXPMap.put(strippedLog,0.15);
        }
        smeltingXPMap.put(Material.CHORUS_FRUIT,0.1);
        smeltingXPMap.put(Material.WET_SPONGE,0.15);
        smeltingXPMap.put(Material.SEA_PICKLE,0.1);
        smeltingXPMap.put(Material.DIAMOND_ORE,1.0);
        smeltingXPMap.put(Material.LAPIS_ORE,0.2);
        smeltingXPMap.put(Material.REDSTONE_ORE,0.3);
        smeltingXPMap.put(Material.COAL_ORE,0.1);
        smeltingXPMap.put(Material.EMERALD_ORE,1.0);
        smeltingXPMap.put(Material.NETHER_QUARTZ_ORE,0.2);

        smeltingXPMap.put(Material.IRON_SWORD,0.1);
        smeltingXPMap.put(Material.IRON_PICKAXE,0.1);
        smeltingXPMap.put(Material.IRON_AXE,0.1);
        smeltingXPMap.put(Material.IRON_SHOVEL,0.1);
        smeltingXPMap.put(Material.IRON_HOE,0.1);
        smeltingXPMap.put(Material.CHAINMAIL_HELMET,0.1);
        smeltingXPMap.put(Material.CHAINMAIL_CHESTPLATE,0.1);
        smeltingXPMap.put(Material.CHAINMAIL_LEGGINGS,0.1);
        smeltingXPMap.put(Material.CHAINMAIL_BOOTS,0.1);
        smeltingXPMap.put(Material.IRON_HELMET,0.1);
        smeltingXPMap.put(Material.IRON_CHESTPLATE,0.1);
        smeltingXPMap.put(Material.IRON_LEGGINGS,0.1);
        smeltingXPMap.put(Material.IRON_BOOTS,0.1);
        smeltingXPMap.put(Material.IRON_HORSE_ARMOR,0.1);

        smeltingXPMap.put(Material.GOLDEN_SWORD,0.1);
        smeltingXPMap.put(Material.GOLDEN_PICKAXE,0.1);
        smeltingXPMap.put(Material.GOLDEN_AXE,0.1);
        smeltingXPMap.put(Material.GOLDEN_SHOVEL,0.1);
        smeltingXPMap.put(Material.GOLDEN_HOE,0.1);
        smeltingXPMap.put(Material.GOLDEN_HELMET,0.1);
        smeltingXPMap.put(Material.GOLDEN_CHESTPLATE,0.1);
        smeltingXPMap.put(Material.GOLDEN_LEGGINGS,0.1);
        smeltingXPMap.put(Material.GOLDEN_BOOTS,0.1);
        smeltingXPMap.put(Material.GOLDEN_HORSE_ARMOR,0.1);

        if (mcVersion >= 1.16) {
            smeltingXPMap.put(Material.ANCIENT_DEBRIS,2.0);
            smeltingXPMap.put(Material.NETHER_GOLD_ORE,1.0);
            smeltingXPMap.put(Material.NETHER_BRICK,0.1);

        }

    }



    //Getters
    public List<Material> getNewIngredients(){
        return newIngredients;
    }
    public List<Material> getOldIngredients(){
        return oldIngredients;
    }
    public ItemStack getHastePotion(){
        return hastePotion;
    }
    public ItemStack getFatiguePotion(){
        return fatiguePotion;
    }
    public ItemStack getHeroPotion(){
        return heroPotion;
    }
    public ItemStack getDecayPotion(){
        return decayPotion;
    }
    public ItemStack getResistancePotion(){
        return resistancePotion;
    }
    public List<Material> getLeftClickItems() {
        return leftClickItems;
    }
    public List<Material> getPickaxes() {
        return pickaxes;
    }
    public List<Material> getAxes() {
        return axes;
    }
    public List<Material> getShovels() {
        return shovels;
    }
    public List<Material> getHoes() {
        return hoes;
    }
    public List<Material> getSwords() {
        return swords;
    }
    public List<Material> getNoRightClick() {
        return noRightClick;
    }
    public List<Material> getActionableBlocks() {
        return actionableBlocks;
    }
    public List<PotionEffectType> getHarmfulEffects() {return harmfulEffects;}
    public List<Material> getLogs() {return logs;}
    public List<Material> getTallCrops() {return tallCrops;}
    public Map<Material,Boolean> getTrackedBlocks() {return trackedBlocks;}
    public Map<Enchantment, Integer> getEnchantmentLevelMap() {
        return enchantmentLevelMap;
    }

    public List<Material> getCrops() {
        return crops;
    }

    public Map<Material, Double> getFarmFoodSaturation() {
        return farmFoodSaturation;
    }

    public Map<Material, Double> getMeatFoodSaturation() {
        return meatFoodSaturation;
    }

    public Map<Material, Integer> getFarmFood() {
        return farmFood;
    }

    public Map<Material, Integer> getMeatFood() {
        return meatFood;
    }

    public Map<Material, Double> getFishFoodSaturation() {
        return fishFoodSaturation;
    }

    public Map<Material, Integer> getFishFood() {
        return fishFood;
    }

    public List<Material> getValuableItems() {
        return valuableItems;
    }
    public List<Material> getOres() { return ores;}

    public Map<Material, Integer> getRepairItemsAmount() {
        return repairItemsAmount;
    }

    public Map<Material, Material> getRepairItems() {
        return repairItems;
    }

    public List<Material> getLeaves() {
        return leaves;
    }

    public List<Material> getStrippedLogs() {
        return strippedLogs;
    }

    public Map<Material,Material> getSmeltableItemsMap() {
        return smeltableItemsMap;
    }

    public ItemStack getArrow(String type) {
        if (effectArrows.containsKey(type)) {
            effectArrows.get(type).setAmount(8);
            return effectArrows.get(type);
        }
        return new ItemStack(Material.ARROW,8);
    }

    public Map<Material, Double> getSmeltingXPMap() {
        return smeltingXPMap;
    }

    public static ArrayList<Material> getAllLogs() {
        return allLogs;
    }

    public HashSet<Material> getVeinMinerBlocks() {
        return veinMinerBlocks;
    }

    public List<Material> getFlamePickOres() {
        return flamePickOres;
    }
}
