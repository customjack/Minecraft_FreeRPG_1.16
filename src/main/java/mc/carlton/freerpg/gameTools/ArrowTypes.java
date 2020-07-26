package mc.carlton.freerpg.gameTools;

import mc.carlton.freerpg.FreeRPG;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class ArrowTypes {
    static Map<String, ItemStack> effectArrows = new HashMap<>();
    private String[] effectTypes = {"water", "mundane", "thick", "awkward", "night_vision", "long_night_vision", "invisibility",
                                    "long_invisibility", "leaping", "long_leaping", "strong_leaping", "fire_resistance",
                                    "long_fire_resistance", "swiftness", "long_swiftness", "strong_swiftness", "slowness",
                                    "long_slowness", "breathing_water", "long_water_breathing", "healing", "strong_healing",
                                    "harming", "strong_harming", "poison", "long_poison", "strong_poison", "regeneration",
                                    "long_regeneration", "strong_regeneration", "strength", "long_strength", "strong_strength",
                                    "weakness", "long_weakness","luck","turtle_master","strong_turtle_master","long_turtle_master",
                                    "slow_falling","long_slow_falling"};

    public void getArrows(int timesRan) {
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        World world = Bukkit.getServer().getWorlds().get(0);
        Block chestBlock = world.getBlockAt(0,0,0);
        Material oldBlockType = chestBlock.getType();
        chestBlock.setType(Material.CHEST);
        Container cont = (Container) chestBlock.getState();
        Inventory inv = cont.getInventory();

        for (String effect : effectTypes) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "replaceitem block 0 0 0 container.0 minecraft:tipped_arrow{Potion:\"" + effect + "\"} 8");
            ItemStack arrow = inv.getItem(0);
            if (arrow == null) {
                if (timesRan > 30) {
                    System.out.println("[FreeRPG/ERROR]: Could not get list of tipped arrows, Dragonless Arrows perk will NOT work");
                }
                new BukkitRunnable() {
                        @Override
                    public void run() {
                        getArrows(timesRan+1);
                        return;
                    }
                }.runTaskLater(plugin, 20);
            }
            effectArrows.put(effect,arrow);
        }
        inv.clear();
        chestBlock.setType(Material.AIR);
        chestBlock.setType(oldBlockType);
    }

    public ItemStack getArrow(String type) {
        if (effectArrows.containsKey(type)) {
            return effectArrows.get(type);
        }
        return new ItemStack(Material.ARROW,1);
    }
}
