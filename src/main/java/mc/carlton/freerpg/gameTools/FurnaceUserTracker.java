package mc.carlton.freerpg.gameTools;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class FurnaceUserTracker {
    static Map<Location, Player> furnaceLocationPlayerMap = new HashMap<>();

    public void addfurnaceLocation(Location furnaceLocation, Player player) {
        if (furnaceLocation instanceof Location && player instanceof Player) {
            furnaceLocationPlayerMap.put(furnaceLocation, player);
        }
    }

    public Player getPlayer(Location furnaceLocation) {
        if (furnaceLocation instanceof Location) {
            if (furnaceLocationPlayerMap.containsKey(furnaceLocation)) {
                return furnaceLocationPlayerMap.get(furnaceLocation);
            }
        }
        return null;
    }

    public void removefurnaceLocation(Location furnaceLocation) {
        furnaceLocationPlayerMap.remove(furnaceLocation);
    }
    public void removeAllPlayerfurnaceLocations(Player p) {
        for (Location furnaceLocation : furnaceLocationPlayerMap.keySet()) {
            if (furnaceLocationPlayerMap.get(furnaceLocation) == p) {
                furnaceLocationPlayerMap.remove(furnaceLocation);
            }
        }
    }
}
