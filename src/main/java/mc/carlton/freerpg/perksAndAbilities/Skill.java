package mc.carlton.freerpg.perksAndAbilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;


public class Skill {

    public void dropItemNaturally(Location location, ItemStack item) { //Won't try to drop air, like world.dropItemNaturally does
        if (item != null){
            if (!item.getType().equals(Material.AIR) && !item.getType().equals(Material.CAVE_AIR) && !item.getType().equals(Material.VOID_AIR)) {
                World world = location.getWorld();
                world.dropItemNaturally(location,item);
            }
        }
    }
}
