package mc.carlton.freerpg.miscEvents;


import mc.carlton.freerpg.playerAndServerInfo.PlacedBlocks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;


public class PlayerBlockPlace implements Listener {
    @EventHandler
    void onblockPlace(BlockPlaceEvent e){
        Material[] trackedBlocks = {Material.ACACIA_LOG,Material.ACACIA_LEAVES,Material.BIRCH_LOG,Material.BIRCH_LEAVES,
                                    Material.DARK_OAK_LOG,Material.DARK_OAK_LEAVES,Material.JUNGLE_LOG,Material.JUNGLE_LEAVES,
                                    Material.OAK_LOG,Material.OAK_LEAVES,Material.SPRUCE_LOG,Material.SPRUCE_LEAVES,
                                    Material.COAL_ORE,Material.DIAMOND_ORE,Material.EMERALD_ORE,Material.GOLD_ORE,
                                    Material.IRON_ORE,Material.LAPIS_ORE,Material.NETHER_QUARTZ_ORE,Material.REDSTONE_ORE,
                                    Material.SUGAR_CANE,Material.MELON,Material.PUMPKIN,Material.RED_MUSHROOM,Material.BROWN_MUSHROOM,
                                    Material.BAMBOO,Material.CACTUS,
                                    Material.ANCIENT_DEBRIS,Material.NETHER_GOLD_ORE,Material.WARPED_STEM,Material.CRIMSON_STEM,
                                    Material.GILDED_BLACKSTONE};
        Block block = e.getBlockPlaced();
        Material blockType = block.getType();
        boolean isTracked = false;
        for (Material mat : trackedBlocks) {
            if (mat.equals(blockType)) {
                isTracked = true;
                break;
            }
        }
        if (isTracked) {
            Location loc = block.getLocation();
            PlacedBlocks placedClass = new PlacedBlocks();
            ArrayList<Location> blocksLocations = placedClass.getBlocks();
            blocksLocations.add(loc);
            placedClass.setBlocks(blocksLocations);
        }
    }
}
