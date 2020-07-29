package mc.carlton.freerpg.miscEvents;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.perksAndAbilities.*;
import mc.carlton.freerpg.playerAndServerInfo.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Cocoa;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class PlayerBlockBreak implements Listener {
    @EventHandler
    void onblockBreak(BlockBreakEvent e){
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);

        Player p = e.getPlayer();
        Block block = e.getBlock();
        Location loc = block.getLocation();
        Material blockType = block.getType();
        World world = e.getBlock().getWorld();

        //WorldGuard Check
        WorldGuardChecks BuildingCheck = new WorldGuardChecks();
        if (!BuildingCheck.canBuild(p, loc)) {
            return;
        }


        // Tools and Other
        Material[] pickaxes0 = {Material.NETHERITE_PICKAXE,Material.DIAMOND_PICKAXE,Material.GOLDEN_PICKAXE,Material.IRON_PICKAXE, Material.STONE_PICKAXE,Material.WOODEN_PICKAXE};
        List<Material> pickaxes = Arrays.asList(pickaxes0);
        Material[] axes0 = {Material.NETHERITE_AXE,Material.DIAMOND_AXE,Material.GOLDEN_AXE,Material.IRON_AXE, Material.STONE_AXE,Material.WOODEN_AXE};
        List<Material> axes = Arrays.asList(axes0);
        Material[] shovels0 = {Material.NETHERITE_SHOVEL,Material.DIAMOND_SHOVEL,Material.GOLDEN_SHOVEL,Material.IRON_SHOVEL, Material.STONE_SHOVEL,Material.WOODEN_SHOVEL};
        List<Material> shovels = Arrays.asList(shovels0);
        Material[] hoes0 = {Material.NETHERITE_HOE,Material.DIAMOND_HOE,Material.GOLDEN_HOE,Material.IRON_HOE, Material.STONE_HOE,Material.WOODEN_HOE};
        List<Material> hoes = Arrays.asList(hoes0);

        Material[] tallCrops0 = {Material.SUGAR_CANE,Material.BAMBOO,Material.CACTUS,Material.KELP,Material.KELP_PLANT};
        List<Material> tallCrops = Arrays.asList(tallCrops0);
        Material[] logs0 = {Material.ACACIA_LOG,Material.BIRCH_LOG,Material.DARK_OAK_LOG,Material.OAK_LOG,Material.SPRUCE_LOG,Material.JUNGLE_LOG,
                            Material.STRIPPED_ACACIA_LOG,Material.STRIPPED_BIRCH_LOG,Material.STRIPPED_DARK_OAK_LOG,Material.STRIPPED_JUNGLE_LOG,
                            Material.STRIPPED_OAK_LOG,Material.STRIPPED_SPRUCE_LOG,Material.CRIMSON_STEM,Material.WARPED_STEM, Material.STRIPPED_CRIMSON_STEM,Material.STRIPPED_WARPED_STEM};
        List<Material> logs = Arrays.asList(logs0);
        Material[] leaves0 = {Material.ACACIA_LEAVES,Material.BIRCH_LEAVES,Material.DARK_OAK_LEAVES,Material.OAK_LEAVES,Material.SPRUCE_LEAVES,Material.JUNGLE_LEAVES};
        List<Material> leaves = Arrays.asList(leaves0);

        //Creates diggingEXP blocks
        Map<Material,Integer> diggingEXP = new HashMap<Material,Integer>();
        diggingEXP.put(Material.CLAY,150);
        diggingEXP.put(Material.FARMLAND,100);
        diggingEXP.put(Material.GRASS_BLOCK,100);
        diggingEXP.put(Material.GRASS_PATH,100);
        diggingEXP.put(Material.GRAVEL,100);
        diggingEXP.put(Material.MYCELIUM,100);
        diggingEXP.put(Material.PODZOL,100);
        diggingEXP.put(Material.COARSE_DIRT,100);
        diggingEXP.put(Material.DIRT,100);
        diggingEXP.put(Material.RED_SAND,130);
        diggingEXP.put(Material.SAND,100);
        diggingEXP.put(Material.SOUL_SAND,150);
        diggingEXP.put(Material.SNOW_BLOCK,110);
        diggingEXP.put(Material.SNOW,15);
        diggingEXP.put(Material.WHITE_CONCRETE_POWDER,100);
        diggingEXP.put(Material.ORANGE_CONCRETE_POWDER,100);
        diggingEXP.put(Material.MAGENTA_CONCRETE_POWDER,100);
        diggingEXP.put(Material.LIGHT_BLUE_CONCRETE_POWDER,100);
        diggingEXP.put(Material.YELLOW_CONCRETE_POWDER,100);
        diggingEXP.put(Material.LIME_CONCRETE_POWDER,100);
        diggingEXP.put(Material.PINK_CONCRETE_POWDER,100);
        diggingEXP.put(Material.GRAY_CONCRETE_POWDER,100);
        diggingEXP.put(Material.LIGHT_GRAY_CONCRETE_POWDER,100);
        diggingEXP.put(Material.CYAN_CONCRETE_POWDER,100);
        diggingEXP.put(Material.PURPLE_CONCRETE_POWDER,100);
        diggingEXP.put(Material.BLUE_CONCRETE_POWDER,100);
        diggingEXP.put(Material.BROWN_CONCRETE_POWDER,100);
        diggingEXP.put(Material.GREEN_CONCRETE_POWDER,100);
        diggingEXP.put(Material.RED_CONCRETE_POWDER,100);
        diggingEXP.put(Material.BLACK_CONCRETE_POWDER,100);



        //Creates blocks that yield woodcutting xp
        Map<Material,Integer> woodcuttingEXP = new HashMap<Material,Integer>();
        woodcuttingEXP.put(Material.ACACIA_LOG,200);
        woodcuttingEXP.put(Material.BIRCH_LOG,200);
        woodcuttingEXP.put(Material.DARK_OAK_LOG,200);
        woodcuttingEXP.put(Material.OAK_LOG,200);
        woodcuttingEXP.put(Material.SPRUCE_LOG,200);
        woodcuttingEXP.put(Material.JUNGLE_LOG,200);
        woodcuttingEXP.put(Material.ACACIA_PLANKS,55);
        woodcuttingEXP.put(Material.BIRCH_PLANKS,55);
        woodcuttingEXP.put(Material.DARK_OAK_PLANKS,55);
        woodcuttingEXP.put(Material.OAK_PLANKS,55);
        woodcuttingEXP.put(Material.SPRUCE_PLANKS,55);
        woodcuttingEXP.put(Material.JUNGLE_PLANKS,55);
        woodcuttingEXP.put(Material.ACACIA_LEAVES,45);
        woodcuttingEXP.put(Material.BIRCH_LEAVES,45);
        woodcuttingEXP.put(Material.DARK_OAK_LEAVES,45);
        woodcuttingEXP.put(Material.JUNGLE_LEAVES,45);
        woodcuttingEXP.put(Material.OAK_LEAVES,45);
        woodcuttingEXP.put(Material.SPRUCE_LEAVES,45);
        woodcuttingEXP.put(Material.BROWN_MUSHROOM_BLOCK,250);
        woodcuttingEXP.put(Material.RED_MUSHROOM_BLOCK,250);
        //1.16 Blocks, EXP subject to change
        woodcuttingEXP.put(Material.CRIMSON_STEM,250);
        woodcuttingEXP.put(Material.WARPED_STEM,250);
        woodcuttingEXP.put(Material.WARPED_PLANKS,55);
        woodcuttingEXP.put(Material.CRIMSON_PLANKS,55);

        //Blocks that yield mining EXP
        Map<Material,Integer> miningEXP = new HashMap<Material,Integer>();
        miningEXP.put(Material.ICE,75);
        miningEXP.put(Material.BLUE_ICE,75);
        miningEXP.put(Material.PACKED_ICE,75);
        miningEXP.put(Material.FROSTED_ICE,75);
        miningEXP.put(Material.ANDESITE,95);
        miningEXP.put(Material.COAL_ORE,300);
        miningEXP.put(Material.DIORITE,95);
        miningEXP.put(Material.END_STONE,75);
        miningEXP.put(Material.GRANITE,95);
        miningEXP.put(Material.NETHERRACK,50);
        miningEXP.put(Material.NETHER_QUARTZ_ORE,325);
        miningEXP.put(Material.MOSSY_COBBLESTONE,120);
        miningEXP.put(Material.SANDSTONE,75);
        miningEXP.put(Material.RED_SANDSTONE,75);
        miningEXP.put(Material.SPAWNER,7500);
        miningEXP.put(Material.STONE,75);
        miningEXP.put(Material.TERRACOTTA,75);
        miningEXP.put(Material.RED_TERRACOTTA,75);
        miningEXP.put(Material.ORANGE_TERRACOTTA,75);
        miningEXP.put(Material.YELLOW_TERRACOTTA,75);
        miningEXP.put(Material.BROWN_TERRACOTTA,75);
        miningEXP.put(Material.WHITE_TERRACOTTA,75);
        miningEXP.put(Material.LIGHT_GRAY_TERRACOTTA,75);
        miningEXP.put(Material.IRON_ORE,500);
        miningEXP.put(Material.REDSTONE_ORE,700);
        miningEXP.put(Material.LAPIS_ORE,1250);
        miningEXP.put(Material.DIAMOND_ORE,2500);
        miningEXP.put(Material.GOLD_ORE,700);
        miningEXP.put(Material.EMERALD_ORE,4000);
        miningEXP.put(Material.OBSIDIAN,150);
        //1.16 Additions, Subject to EXP change
        miningEXP.put(Material.ANCIENT_DEBRIS,5000);
        miningEXP.put(Material.NETHER_GOLD_ORE,750);
        miningEXP.put(Material.BASALT,80);
        miningEXP.put(Material.BLACKSTONE,100);
        miningEXP.put(Material.CRYING_OBSIDIAN,200);
        miningEXP.put(Material.CRIMSON_NYLIUM,80);
        miningEXP.put(Material.WARPED_NYLIUM,80);
        miningEXP.put(Material.GILDED_BLACKSTONE,750);


        Map<Material,Integer> farmingEXP = new HashMap<Material,Integer>();
        farmingEXP.put(Material.WHEAT,200);
        farmingEXP.put(Material.BEETROOTS,200);
        farmingEXP.put(Material.CARROTS,200);
        farmingEXP.put(Material.POTATOES,200);
        farmingEXP.put(Material.MELON,400);
        farmingEXP.put(Material.PUMPKIN,400);
        farmingEXP.put(Material.BAMBOO,60);
        farmingEXP.put(Material.COCOA,300);
        farmingEXP.put(Material.SUGAR_CANE,125);
        farmingEXP.put(Material.CACTUS,200);
        farmingEXP.put(Material.RED_MUSHROOM,300);
        farmingEXP.put(Material.BROWN_MUSHROOM,300);
        farmingEXP.put(Material.SWEET_BERRIES,250);
        farmingEXP.put(Material.KELP,20);
        farmingEXP.put(Material.SEA_PICKLE,300);
        farmingEXP.put(Material.NETHER_WART,225);
        farmingEXP.put(Material.CHORUS_PLANT,200);

        Map<Material,Object[]> flamePickEXP = new HashMap<Material,Object[]>();
        flamePickEXP.put(Material.IRON_ORE,new Object[]{"mining",500});
        flamePickEXP.put(Material.COBBLESTONE,new Object[]{"mining",0});
        flamePickEXP.put(Material.STONE,new Object[]{"mining",75});
        flamePickEXP.put(Material.SANDSTONE,new Object[]{"mining",75});
        flamePickEXP.put(Material.RED_SANDSTONE,new Object[]{"mining",75});
        flamePickEXP.put(Material.SAND,new Object[]{"digging",75});
        flamePickEXP.put(Material.QUARTZ_BLOCK,new Object[]{"mining",0});
        flamePickEXP.put(Material.NETHERRACK,new Object[]{"mining",50});
        flamePickEXP.put(Material.CLAY,new Object[]{"digging",75});
        flamePickEXP.put(Material.STONE_BRICKS,new Object[]{"mining",0});
        flamePickEXP.put(Material.WET_SPONGE,new Object[]{"mining",75});
        flamePickEXP.put(Material.CACTUS,new Object[]{"farming",200});
        flamePickEXP.put(Material.JUNGLE_LOG,new Object[]{"woodcutting",200});
        flamePickEXP.put(Material.SPRUCE_LOG,new Object[]{"woodcutting",200});
        flamePickEXP.put(Material.OAK_LOG,new Object[]{"woodcutting",200});
        flamePickEXP.put(Material.DARK_OAK_LOG,new Object[]{"woodcutting",200});
        flamePickEXP.put(Material.BIRCH_LOG,new Object[]{"woodcutting",200});
        flamePickEXP.put(Material.ACACIA_LOG,new Object[]{"woodcutting",200});


        if (p.getGameMode() == GameMode.CREATIVE) {
            return;
        }



        ChangeStats increaseStats = new ChangeStats(p);

        AbilityTracker abilities = new AbilityTracker(p);
        Integer[] pAbilities = abilities.getPlayerAbilities();

        PlayerStats pStatClass = new PlayerStats(p);
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();

        ItemStack itemInHand = p.getInventory().getItemInMainHand();

        //Tracked Blocks
        PlacedBlocks placedClass = new PlacedBlocks();
        ArrayList<Location> blocksLocations = placedClass.getBlocks();
        boolean natural = true;
        for (Location blockLocation : blocksLocations) {
            if (loc.equals(blockLocation)) {
                blocksLocations.remove(blockLocation);
                placedClass.setBlocks(blocksLocations);
                natural = false;
                break;
            }
        }

        //EXP drops
        if (flamePickEXP.containsKey(blockType) && pickaxes.contains(itemInHand.getType()) && (int) pStat.get("global").get(13) > 0 && (int) pStat.get("smelting").get(13) > 0) {
            Object[] flamePickData = flamePickEXP.get(blockType);
            if (natural) {
                increaseStats.changeEXP((String) flamePickData[0], (int) flamePickData[1]);
            }
            int veinMinerLevel = (int)pStat.get("mining").get(11);
            int veinMinerToggle = (int) pStat.get("global").get(18);
            if ((blockType == Material.IRON_ORE || blockType == Material.GOLD_ORE)) {
                Mining miningClass = new Mining(p);
                miningClass.wastelessHaste();
                if (veinMinerLevel > 0 && veinMinerToggle > 0) {
                    miningClass.veinMiner(block,blockType);
                }
            }
            else {
                Smelting smeltingClass = new Smelting(p);
                smeltingClass.flamePick(block, world,blockType);
            }
        }

        else if(diggingEXP.containsKey(blockType)) {
            increaseStats.changeEXP("digging",diggingEXP.get(blockType));
            Material[] treasureBlocks0 = {Material.CLAY,Material.GRASS_BLOCK,Material.GRAVEL,Material.MYCELIUM, Material.PODZOL,Material.COARSE_DIRT,
                                          Material.DIRT,Material.RED_SAND,Material.SAND,Material.SOUL_SAND,Material.SNOW_BLOCK};
            List<Material> treasureBlocks = Arrays.asList(treasureBlocks0);
            Digging diggingClass = new Digging(p);
            boolean dropFlint = diggingClass.flintFinder(blockType);
            if (dropFlint) {
                e.setDropItems(false);
                world.dropItemNaturally(loc,new ItemStack(Material.FLINT,1));
            }
            if (treasureBlocks.contains(blockType)) {
                diggingClass.diggingTreasureDrop(world,loc,blockType);
            }

        }
        else if(woodcuttingEXP.containsKey(blockType) && natural) {
            increaseStats.changeEXP("woodcutting",woodcuttingEXP.get(blockType));
            Woodcutting woodcuttingClass = new Woodcutting(p);
            woodcuttingClass.woodcuttingDoubleDrop(block,world);
            woodcuttingClass.logXPdrop(block,world);
            woodcuttingClass.logBookDrop(block,world);
            woodcuttingClass.leavesDrops(block,world);
            woodcuttingClass.timedHaste(block);

        }
        else if (miningEXP.containsKey(blockType) && natural) {
            increaseStats.changeEXP("mining", miningEXP.get(blockType));
            Material[] ores0 = {Material.COAL_ORE, Material.DIAMOND_ORE, Material.EMERALD_ORE, Material.GOLD_ORE,
                    Material.IRON_ORE, Material.LAPIS_ORE, Material.NETHER_QUARTZ_ORE, Material.REDSTONE_ORE,Material.NETHER_GOLD_ORE,Material.ANCIENT_DEBRIS,Material.GILDED_BLACKSTONE};
            List<Material> ores = Arrays.asList(ores0);
            Mining miningClass = new Mining(p);
            if (pAbilities[2] == -2) {
                //Treasure Drops:
                int passive2_mining = (int) pStat.get("mining").get(9);
                double treasureDropChance = passive2_mining * 0.01;
                miningClass.miningTreasureDrop(treasureDropChance, world, loc);
            }
            if (ores.contains(blockType)) {
                miningClass.wastelessHaste();
                miningClass.miningDoubleDrop(block, world);
                miningClass.veinMiner(block,blockType);

            }
            if (blockType == Material.SPAWNER) {
                increaseStats.changeEXP("defense", miningEXP.get(blockType));
                increaseStats.changeEXP("swordsmanship", miningEXP.get(blockType));
                increaseStats.changeEXP("archery", miningEXP.get(blockType));
                increaseStats.changeEXP("axeMastery", miningEXP.get(blockType));
            }
        }
        else if (farmingEXP.containsKey(blockType) && natural) {
            BlockData block_data = block.getBlockData();
            Farming farmingClass = new Farming(p);
            if (tallCrops.contains(blockType)) {
                farmingClass.tallCrops(block,world);
            }
            else if (block_data instanceof Ageable) {
                Ageable age = (Ageable) block_data;
                if (age.getAge() == age.getMaximumAge()) {
                    increaseStats.changeEXP("farming",farmingEXP.get(blockType));
                    if (blockType == Material.NETHER_WART) {
                        increaseStats.changeEXP("alchemy",500);
                    }
                    farmingClass.farmingDoubleDropCrop(block,world);
                }
            }
            else if (block_data instanceof Cocoa) {
                Cocoa coco = (Cocoa) block_data;
                if (coco.getAge() == coco.getMaximumAge()) {
                    increaseStats.changeEXP("farming",farmingEXP.get(blockType));
                    farmingClass.farmingDoubleDropCrop(block,world);
                }
            }
            else {
                increaseStats.changeEXP("farming",farmingEXP.get(blockType));
                farmingClass.farmingDoubleDropCrop(block,world);
                }
        }

        //Abilities

        //Digging
        if (shovels.contains(itemInHand.getType()) && diggingEXP.containsKey(blockType)) {
            Digging diggingClass = new Digging(p);
            if (pAbilities[0] > -1) {
                diggingClass.enableAbility();
            }
            else if (pAbilities[0] == -2) {
                diggingClass.megaDig(block,diggingEXP);
            }
        }

        //Woodcutting
        else if (axes.contains(itemInHand.getType()) && logs.contains(blockType)) {
            Woodcutting woodcuttingClass = new Woodcutting(p);
            if (pAbilities[1] > -1 && natural) {
                woodcuttingClass.enableAbility();
                woodcuttingClass.timber(block);
            }
            else if (pAbilities[1] == -2 && natural) {
                woodcuttingClass.timber(block);
            }
        }
        //Mining
        else if (pickaxes.contains(itemInHand.getType()) && pAbilities[2] > -1 && miningEXP.containsKey(blockType)) {
            Mining miningClass = new Mining(p);
            miningClass.enableAbility();
        }

        //Farming
        else if ((hoes.contains(itemInHand.getType()) || axes.contains(itemInHand.getType())) && farmingEXP.containsKey(blockType)) {
            Farming farmingClass = new Farming(p);
            if (pAbilities[3] > -1 && natural && axes.contains(itemInHand.getType()) && (blockType == Material.MELON || blockType == Material.PUMPKIN)) {
                farmingClass.enableAbility();
                farmingClass.naturalRegeneration(block,world);
            }
            else if (pAbilities[3] == -2 && natural && axes.contains(itemInHand.getType()) && (blockType == Material.MELON || blockType == Material.PUMPKIN)) {
                farmingClass.naturalRegeneration(block,world);
            }
            else if (pAbilities[3] > -1 && natural && (hoes.contains(itemInHand.getType()))) {
                farmingClass.enableAbility();

            }
            else if (pAbilities[3] == -2 && natural && (hoes.contains(itemInHand.getType()))) {
                farmingClass.naturalRegeneration(block,world);
            }

        }
    }
}
