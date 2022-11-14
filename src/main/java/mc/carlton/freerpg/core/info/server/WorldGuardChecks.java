package mc.carlton.freerpg.core.info.server;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.config.ConfigLoad;
import org.apache.logging.log4j.Level;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * WorldGuardChecks is the class which checks if the Worldguard plugin is installed.
 */
public class WorldGuardChecks {

  static boolean worldGuardPresent;

  public void initializeWorldGuardPresent() {
    worldGuardPresent = true;
    try {
      WorldGuard.getInstance();
    } catch (NoClassDefFoundError e) {
      worldGuardPresent = false;
    }
    FreeRPG.log(Level.INFO, "Worldguard is present: " + worldGuardPresent);
  }

  public boolean canBuild(Player p, Location l) {
    if (!worldGuardPresent) {
      return true;
    }
    if (!inRegion(l)) {
      ConfigLoad loadConfig = new ConfigLoad();
      return loadConfig.isAllowBuild();
    }
    RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
    LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(p);
    com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(l);
    if (!hasBypass(localPlayer)) {
      return query.testState(loc, WorldGuardPlugin.inst().wrapPlayer(p), Flags.BUILD);
    } else {
      return true;
    }
  }

  public boolean canPvP(Player p, Location l) {
    if (!worldGuardPresent) {
      return true;
    }
    if (!inRegion(l)) {
      ConfigLoad loadConfig = new ConfigLoad();
      return loadConfig.isAllowPvP();
    }
    RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
    LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(p);
    com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(l);
    if (!hasBypass(localPlayer)) {
      return query.testState(loc, WorldGuardPlugin.inst().wrapPlayer(p), Flags.PVP);
    } else {
      return true;
    }
  }

  public boolean canDamageEntities(Player p, Location l) {
    if (!worldGuardPresent) {
      return true;
    }
    if (!inRegion(l)) {
      ConfigLoad loadConfig = new ConfigLoad();
      return loadConfig.isAllowHurtAnimals();
    }
    RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
    LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(p);
    com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(l);
    if (!hasBypass(localPlayer)) {
      return query.testState(loc, WorldGuardPlugin.inst().wrapPlayer(p), Flags.DAMAGE_ANIMALS);
    } else {
      return true;
    }
  }

  public boolean canExplode(Player p, Location l) {
    if (!worldGuardPresent) {
      return true;
    }
    if (!inRegion(l)) {
      ConfigLoad loadConfig = new ConfigLoad();
      return loadConfig.isAllowExplosions();
    }
    RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
    LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(p);
    com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(l);
    if (!hasBypass(localPlayer)) {
      return query.testState(loc, WorldGuardPlugin.inst().wrapPlayer(p), Flags.OTHER_EXPLOSION);
    } else {
      return true;
    }
  }


  public boolean hasBypass(LocalPlayer localPlayer) {
    return WorldGuard.getInstance().getPlatform().getSessionManager()
        .hasBypass(localPlayer, localPlayer.getWorld());
  }

  public boolean inRegion(Location l) {
    boolean inRegion = false;
    com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(l);
    RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
    RegionQuery query = container.createQuery();
    ApplicableRegionSet set = query.getApplicableRegions(loc);
    if (!set.getRegions().isEmpty()) {
      inRegion = true;
    }
    return inRegion;
  }
}
