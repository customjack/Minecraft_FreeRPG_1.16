package mc.carlton.freerpg.playerAndServerInfo;

import org.bukkit.Location;

import java.util.ArrayList;

public class PlacedBlocks {
    static ArrayList<Location> blocks = new ArrayList<Location>();

    public ArrayList<Location> getBlocks() {
        return blocks;
    }

    public void setBlocks(ArrayList<Location> newblocks) {
        this.blocks = newblocks;
    }

}
