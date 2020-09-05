package mc.carlton.freerpg.miscEvents;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.globalVariables.ExpMaps;
import mc.carlton.freerpg.globalVariables.ItemGroups;
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
        //WorldGuard Check
        if (e.isCancelled()) {
            return;
        }

        Player p = e.getPlayer();
        Block block = e.getBlock();
        Location loc = block.getLocation();
        Material blockType = block.getType();
        World world = e.getBlock().getWorld();

        ItemGroups itemGroups = new ItemGroups();

        // Tools and Other
        List<Material> pickaxes = itemGroups.getPickaxes();
        List<Material> axes = itemGroups.getAxes();
        List<Material> shovels = itemGroups.getShovels();
        List<Material> hoes = itemGroups.getHoes();

        //Blocks
        List<Material> tallCrops = itemGroups.getTallCrops();
        List<Material> logs = itemGroups.getLogs();

        //Gets Block-EXP maps
        ExpMaps expMaps = new ExpMaps();
        Map<Material,Integer> diggingEXP = expMaps.getDiggingEXP();
        Map<Material,Integer> woodcuttingEXP = expMaps.getWoodcuttingEXP();
        Map<Material,Integer> miningEXP = expMaps.getMiningEXP();
        Map<Material,Integer> farmingEXP = expMaps.getFarmingEXP();
        Map<Material,Object[]> flamePickEXP = expMaps.getFlamePickEXP();


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
        PlacedBlocksManager placedBlocksManager = new PlacedBlocksManager();
        boolean natural = !placedBlocksManager.isBlockTracked(block);
        if (!natural) {
            placedBlocksManager.removeBlock(block);
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
                miningClass.wastelessHaste(blockType);
                if (veinMinerLevel > 0 && veinMinerToggle > 0) {
                    miningClass.veinMiner(block,blockType);
                }
                else {
                    Smelting smeltingClass = new Smelting(p);
                    smeltingClass.flamePick(block, world,blockType);
                }
            }
            else {
                Smelting smeltingClass = new Smelting(p);
                smeltingClass.flamePick(block, world,blockType);
            }
        }

        else if(diggingEXP.containsKey(blockType) &&natural) {
            ConfigLoad configLoad = new ConfigLoad();
            if (!configLoad.getAllowedSkillsMap().get("digging")) {
                return;
            }
                increaseStats.changeEXP("digging", diggingEXP.get(blockType));
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
            Mining miningClass = new Mining(p);
            miningClass.wastelessHaste(blockType);
            miningClass.miningDoubleDrop(block, world);
            miningClass.veinMiner(block,blockType);
            if (pAbilities[2] == -2) {
                //Treasure Drops:
                int passive2_mining = (int) pStat.get("mining").get(9);
                double treasureDropChance = passive2_mining * 0.01;
                miningClass.miningTreasureDrop(treasureDropChance, world, loc);
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
                        ConfigLoad configLoad = new ConfigLoad();
                        Map<String,Integer> expMap = configLoad.getExpMapForSkill("alchemy");
                        increaseStats.changeEXP("alchemy",expMap.get("breakNetherWart"));
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
