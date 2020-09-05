package mc.carlton.freerpg.globalVariables;

import mc.carlton.freerpg.playerAndServerInfo.ConfigLoad;
import mc.carlton.freerpg.playerAndServerInfo.MinecraftVersion;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class ExpMaps {
    private MinecraftVersion minecraftVersion = new MinecraftVersion();
    private double mcVersion = minecraftVersion.getMinecraftVersion_Double();

    static Map<Material,Integer> diggingEXP = new HashMap<Material,Integer>();
    static Map<Material,Integer> woodcuttingEXP = new HashMap<Material,Integer>();
    static Map<Material,Integer> miningEXP = new HashMap<Material,Integer>();
    static Map<Material,Integer> farmingEXP = new HashMap<Material,Integer>();
    static Map<Material,Object[]> flamePickEXP = new HashMap<Material,Object[]>();

    public void initializeAllExpMaps(){
        initializeDiggingEXP();
        initializeWoodcuttingEXP();
        initializeMiningEXP();
        initializeFarmingEXP();
        initializeFlamePickEXP();
    }
    public void initializeDiggingEXP() {
        ConfigLoad configLoad = new ConfigLoad();
        Map<String,Integer> expMap = configLoad.getExpMapForSkill("digging");
        diggingEXP.put(Material.CLAY, expMap.get("breakClay"));
        diggingEXP.put(Material.FARMLAND,expMap.get("breakFarmland"));
        diggingEXP.put(Material.GRASS_BLOCK,expMap.get("breakGrassBlock"));
        diggingEXP.put(Material.GRASS_PATH,expMap.get("breakGrassPath"));
        diggingEXP.put(Material.GRAVEL,expMap.get("breakGravel"));
        diggingEXP.put(Material.MYCELIUM,expMap.get("breakMycelium"));
        diggingEXP.put(Material.PODZOL,expMap.get("breakPodzol"));
        diggingEXP.put(Material.COARSE_DIRT,expMap.get("breakCoarse_Dirt"));
        diggingEXP.put(Material.DIRT,expMap.get("breakDirt"));
        diggingEXP.put(Material.RED_SAND,expMap.get("breakRed_Sand"));
        diggingEXP.put(Material.SAND,expMap.get("breakSand"));
        diggingEXP.put(Material.SOUL_SAND,expMap.get("breakSoulSand"));
        diggingEXP.put(Material.SNOW_BLOCK,expMap.get("breakSnowBlock"));
        diggingEXP.put(Material.SNOW,expMap.get("breakSnow"));
        diggingEXP.put(Material.WHITE_CONCRETE_POWDER,expMap.get("breakConcretePowder"));
        diggingEXP.put(Material.ORANGE_CONCRETE_POWDER,expMap.get("breakConcretePowder"));
        diggingEXP.put(Material.MAGENTA_CONCRETE_POWDER,expMap.get("breakConcretePowder"));
        diggingEXP.put(Material.LIGHT_BLUE_CONCRETE_POWDER,expMap.get("breakConcretePowder"));
        diggingEXP.put(Material.YELLOW_CONCRETE_POWDER,expMap.get("breakConcretePowder"));
        diggingEXP.put(Material.LIME_CONCRETE_POWDER,expMap.get("breakConcretePowder"));
        diggingEXP.put(Material.PINK_CONCRETE_POWDER,expMap.get("breakConcretePowder"));
        diggingEXP.put(Material.GRAY_CONCRETE_POWDER,expMap.get("breakConcretePowder"));
        diggingEXP.put(Material.LIGHT_GRAY_CONCRETE_POWDER,expMap.get("breakConcretePowder"));
        diggingEXP.put(Material.CYAN_CONCRETE_POWDER,expMap.get("breakConcretePowder"));
        diggingEXP.put(Material.PURPLE_CONCRETE_POWDER,expMap.get("breakConcretePowder"));
        diggingEXP.put(Material.BLUE_CONCRETE_POWDER,expMap.get("breakConcretePowder"));
        diggingEXP.put(Material.BROWN_CONCRETE_POWDER,expMap.get("breakConcretePowder"));
        diggingEXP.put(Material.GREEN_CONCRETE_POWDER,expMap.get("breakConcretePowder"));
        diggingEXP.put(Material.RED_CONCRETE_POWDER,expMap.get("breakConcretePowder"));
        diggingEXP.put(Material.BLACK_CONCRETE_POWDER,expMap.get("breakConcretePowder"));
    }

    public void initializeWoodcuttingEXP() {
        ConfigLoad configLoad = new ConfigLoad();
        Map<String,Integer> expMap = configLoad.getExpMapForSkill("woodcutting");
        woodcuttingEXP.put(Material.ACACIA_LOG,expMap.get("breakAcacia_Log"));
        woodcuttingEXP.put(Material.BIRCH_LOG,expMap.get("breakBirch_Log"));
        woodcuttingEXP.put(Material.DARK_OAK_LOG,expMap.get("breakDark_Oak_Log"));
        woodcuttingEXP.put(Material.OAK_LOG,expMap.get("breakOak_Log"));
        woodcuttingEXP.put(Material.SPRUCE_LOG,expMap.get("breakSpruce_Log"));
        woodcuttingEXP.put(Material.JUNGLE_LOG,expMap.get("breakJungle_Log"));
        woodcuttingEXP.put(Material.ACACIA_PLANKS,expMap.get("breakAcacia_Plank"));
        woodcuttingEXP.put(Material.BIRCH_PLANKS,expMap.get("breakBirch_Plank"));
        woodcuttingEXP.put(Material.DARK_OAK_PLANKS,expMap.get("breakDark_Oak_Plank"));
        woodcuttingEXP.put(Material.OAK_PLANKS,expMap.get("breakOak_Plank"));
        woodcuttingEXP.put(Material.SPRUCE_PLANKS,expMap.get("breakSpruce_Plank"));
        woodcuttingEXP.put(Material.JUNGLE_PLANKS,expMap.get("breakJungle_Plank"));
        woodcuttingEXP.put(Material.ACACIA_LEAVES,expMap.get("breakAcacia_Leaves"));
        woodcuttingEXP.put(Material.BIRCH_LEAVES,expMap.get("breakBirch_Leaves"));
        woodcuttingEXP.put(Material.DARK_OAK_LEAVES,expMap.get("breakDark_Oak_Leaves"));
        woodcuttingEXP.put(Material.OAK_LEAVES,expMap.get("breakOak_Leaves"));
        woodcuttingEXP.put(Material.SPRUCE_LEAVES,expMap.get("breakSpruce_Leaves"));
        woodcuttingEXP.put(Material.JUNGLE_LEAVES,expMap.get("breakJungle_Leaves"));
        woodcuttingEXP.put(Material.BROWN_MUSHROOM_BLOCK,expMap.get("breakBrown_Mushroom_Block"));
        woodcuttingEXP.put(Material.RED_MUSHROOM_BLOCK,expMap.get("breakRed_Mushroom_Block"));
        //1.16 Blocks, EXP subject to change
        if (mcVersion >= 1.16) {
            woodcuttingEXP.put(Material.CRIMSON_STEM, expMap.get("breakCrimson_Stem"));
            woodcuttingEXP.put(Material.WARPED_STEM, expMap.get("breakWarped_Stem"));
            woodcuttingEXP.put(Material.WARPED_PLANKS, expMap.get("breakCrimson_Planks"));
            woodcuttingEXP.put(Material.CRIMSON_PLANKS, expMap.get("breakWarped_Planks"));
        }
    }

    public void initializeMiningEXP() {
        ConfigLoad configLoad = new ConfigLoad();
        Map<String,Integer> expMap = configLoad.getExpMapForSkill("mining");
        miningEXP.put(Material.ICE,expMap.get("breakIce"));
        miningEXP.put(Material.BLUE_ICE,expMap.get("breakBlue_Ice"));
        miningEXP.put(Material.PACKED_ICE,expMap.get("breakPacked_Ice"));
        miningEXP.put(Material.FROSTED_ICE,expMap.get("breakFrosted_Ice"));
        miningEXP.put(Material.ANDESITE,expMap.get("breakAndesite"));
        miningEXP.put(Material.COAL_ORE,expMap.get("breakCoal_Ore"));
        miningEXP.put(Material.DIORITE,expMap.get("breakDiorite"));
        miningEXP.put(Material.END_STONE,expMap.get("breakEnd_Stone"));
        miningEXP.put(Material.GRANITE,expMap.get("breakGranite"));
        miningEXP.put(Material.NETHERRACK,expMap.get("breakNetherrack"));
        miningEXP.put(Material.NETHER_QUARTZ_ORE,expMap.get("breakNether_Quartz_Ore"));
        miningEXP.put(Material.MOSSY_COBBLESTONE,expMap.get("breakMossy_Cobblestone"));
        miningEXP.put(Material.SANDSTONE,expMap.get("breakSandstone"));
        miningEXP.put(Material.RED_SANDSTONE,expMap.get("breakRed_Sandstone"));
        miningEXP.put(Material.SPAWNER,expMap.get("breakSpawner"));
        miningEXP.put(Material.STONE,expMap.get("breakStone"));
        miningEXP.put(Material.TERRACOTTA,expMap.get("breakTerracotta"));
        miningEXP.put(Material.RED_TERRACOTTA,expMap.get("breakTerracotta"));
        miningEXP.put(Material.ORANGE_TERRACOTTA,expMap.get("breakTerracotta"));
        miningEXP.put(Material.YELLOW_TERRACOTTA,expMap.get("breakTerracotta"));
        miningEXP.put(Material.BROWN_TERRACOTTA,expMap.get("breakTerracotta"));
        miningEXP.put(Material.WHITE_TERRACOTTA,expMap.get("breakTerracotta"));
        miningEXP.put(Material.LIGHT_GRAY_TERRACOTTA,expMap.get("breakTerracotta"));
        miningEXP.put(Material.IRON_ORE,expMap.get("breakIron_Ore"));
        miningEXP.put(Material.REDSTONE_ORE,expMap.get("breakRedstone_Ore"));
        miningEXP.put(Material.LAPIS_ORE,expMap.get("breakLapis_Ore"));
        miningEXP.put(Material.DIAMOND_ORE,expMap.get("breakDiamond_Ore"));
        miningEXP.put(Material.GOLD_ORE,expMap.get("breakGold_Ore"));
        miningEXP.put(Material.EMERALD_ORE,expMap.get("breakEmerald_Ore"));
        miningEXP.put(Material.OBSIDIAN,expMap.get("breakObsidian"));
        miningEXP.put(Material.GLOWSTONE,0);
        if (mcVersion >= 1.16) {
            miningEXP.put(Material.ANCIENT_DEBRIS, expMap.get("breakAncient_Debris"));
            miningEXP.put(Material.NETHER_GOLD_ORE, expMap.get("breakNether_Gold_Ore"));
            miningEXP.put(Material.BASALT, expMap.get("breakBasalt"));
            miningEXP.put(Material.BLACKSTONE, expMap.get("breakBlackstone"));
            miningEXP.put(Material.CRYING_OBSIDIAN, expMap.get("breakCrying_Obsidian"));
            miningEXP.put(Material.CRIMSON_NYLIUM, expMap.get("breakCrimson_Nylium"));
            miningEXP.put(Material.WARPED_NYLIUM, expMap.get("breakWarped_Nylium"));
            miningEXP.put(Material.GILDED_BLACKSTONE, expMap.get("breakGilded_Blackstone"));
        }
    }

    public void initializeFarmingEXP() {
        ConfigLoad configLoad = new ConfigLoad();
        Map<String,Integer> expMap = configLoad.getExpMapForSkill("farming");
        farmingEXP.put(Material.WHEAT,expMap.get("breakWheat"));
        farmingEXP.put(Material.BEETROOTS,expMap.get("breakBeatRoots"));
        farmingEXP.put(Material.CARROTS,expMap.get("breakCarrots"));
        farmingEXP.put(Material.POTATOES,expMap.get("breakPotatoes"));
        farmingEXP.put(Material.MELON,expMap.get("breakMelon"));
        farmingEXP.put(Material.PUMPKIN,expMap.get("breakPumpkin"));
        farmingEXP.put(Material.BAMBOO,expMap.get("breakBamboo"));
        farmingEXP.put(Material.COCOA,expMap.get("breakCocoa"));
        farmingEXP.put(Material.SUGAR_CANE,expMap.get("breakSugarCane"));
        farmingEXP.put(Material.CACTUS,expMap.get("breakCactus"));
        farmingEXP.put(Material.RED_MUSHROOM,expMap.get("breakRed_Mushroom"));
        farmingEXP.put(Material.BROWN_MUSHROOM,expMap.get("breakBrown_Mushroom"));
        farmingEXP.put(Material.SWEET_BERRIES,expMap.get("breakSweetBerries"));
        farmingEXP.put(Material.KELP,expMap.get("breakKelp"));
        farmingEXP.put(Material.SEA_PICKLE,expMap.get("breakSea_Pickle"));
        farmingEXP.put(Material.NETHER_WART,expMap.get("breakNether_Wart"));
        farmingEXP.put(Material.CHORUS_PLANT,expMap.get("breakChorusPlant"));
    }
    public void initializeFlamePickEXP() {
        ConfigLoad configLoad = new ConfigLoad();
        Map<String,Map<String,Integer>> expMap = configLoad.getExpMap();
        flamePickEXP.put(Material.IRON_ORE,new Object[]{"mining",expMap.get("mining").get("breakIron_Ore")});
        flamePickEXP.put(Material.COBBLESTONE,new Object[]{"mining",0});
        flamePickEXP.put(Material.STONE,new Object[]{"mining",expMap.get("mining").get("breakStone")});
        flamePickEXP.put(Material.SANDSTONE,new Object[]{"mining",expMap.get("mining").get("breakSandstone")});
        flamePickEXP.put(Material.RED_SANDSTONE,new Object[]{"mining",expMap.get("mining").get("breakRed_Sandstone")});
        flamePickEXP.put(Material.SAND,new Object[]{"digging",expMap.get("digging").get("breakSand")});
        flamePickEXP.put(Material.QUARTZ_BLOCK,new Object[]{"mining",0});
        flamePickEXP.put(Material.NETHERRACK,new Object[]{"mining",expMap.get("mining").get("breakNetherrack")});
        flamePickEXP.put(Material.CLAY,new Object[]{"digging",expMap.get("digging").get("breakClay")});
        flamePickEXP.put(Material.STONE_BRICKS,new Object[]{"mining",0});
        flamePickEXP.put(Material.WET_SPONGE,new Object[]{"mining",0});
        flamePickEXP.put(Material.CACTUS,new Object[]{"farming",expMap.get("farming").get("breakCactus")});
        flamePickEXP.put(Material.JUNGLE_LOG,new Object[]{"woodcutting",expMap.get("woodcutting").get("breakJungle_Log")});
        flamePickEXP.put(Material.SPRUCE_LOG,new Object[]{"woodcutting",expMap.get("woodcutting").get("breakSpruce_Log")});
        flamePickEXP.put(Material.OAK_LOG,new Object[]{"woodcutting",expMap.get("woodcutting").get("breakOak_Log")});
        flamePickEXP.put(Material.DARK_OAK_LOG,new Object[]{"woodcutting",expMap.get("woodcutting").get("breakDark_Oak_Log")});
        flamePickEXP.put(Material.BIRCH_LOG,new Object[]{"woodcutting",expMap.get("woodcutting").get("breakBirch_Log")});
        flamePickEXP.put(Material.ACACIA_LOG,new Object[]{"woodcutting",expMap.get("woodcutting").get("breakAcacia_Log")});
    }


    public Map<Material,Integer> getDiggingEXP() {
        return diggingEXP;
    }
    public Map<Material,Integer> getWoodcuttingEXP() {
        return woodcuttingEXP;
    }
    public Map<Material,Integer> getMiningEXP() {
        return miningEXP;
    }
    public Map<Material,Integer> getFarmingEXP() {
        return farmingEXP;
    }
    public Map<Material,Object[]> getFlamePickEXP() {
        return flamePickEXP;
    }
}
