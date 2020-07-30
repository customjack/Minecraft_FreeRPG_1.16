package mc.carlton.freerpg.playerAndServerInfo;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.IOException;

public class WorldGuardChecks {
    static boolean worldGuardPresent;

    public void initializeWorldGuardPresent() {
        worldGuardPresent = true;
        try {
            WorldGuard.getInstance();
        } catch (NoClassDefFoundError e) {
            worldGuardPresent = false;
        }
    }

    public boolean canBuild(Player p, Location l) {
        if (!worldGuardPresent) {
            return true;
        }
        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(p);
        com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(l);
        if (!hasBypass(localPlayer)) {
            return query.testState(loc, WorldGuardPlugin.inst().wrapPlayer(p), Flags.BUILD);
        }else {
            return true;
        }
    }

    public boolean canPvP(Player p, Location l) {
        if (!worldGuardPresent) {
            return true;
        }
        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(p);
        com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(l);
        if (!hasBypass(localPlayer)) {
            return query.testState(loc, WorldGuardPlugin.inst().wrapPlayer(p), Flags.PVP);
        }else {
            return true;
        }
    }

    public boolean canDamageEntities(Player p, Location l) {
        if (!worldGuardPresent) {
            return true;
        }
        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(p);
        com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(l);
        if (!hasBypass(localPlayer)) {
            return query.testState(loc, WorldGuardPlugin.inst().wrapPlayer(p), Flags.DAMAGE_ANIMALS);
        }else {
            return true;
        }
    }


    public boolean hasBypass(LocalPlayer localPlayer) {
        return WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(localPlayer, localPlayer.getWorld());
    }
}
