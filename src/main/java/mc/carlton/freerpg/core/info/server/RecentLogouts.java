package mc.carlton.freerpg.core.info.server;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;
import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.config.ConfigLoad;
import mc.carlton.freerpg.core.info.player.OfflinePlayerStatLoadIn;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class RecentLogouts {

  static ArrayList<UUID> lastLogouts = new ArrayList<>();
  private int maxPlayersStored;

  public RecentLogouts() {
    ConfigLoad configLoad = new ConfigLoad();
    maxPlayersStored = configLoad.getPlayerStatFilesLoadedInOnStartup();
  }

  public ArrayList<UUID> getLastLogouts() {
    return lastLogouts;
  }

  public void setLastLogouts(ArrayList<UUID> lastLogouts) {
    RecentLogouts.lastLogouts = lastLogouts;
    if (lastLogouts.size() < maxPlayersStored) {
      fillLastLogoutSlots();
    } else if (lastLogouts.size() > maxPlayersStored) {
      removeLastEntries(false);
    }
  }

  public void playerLogout(Player p, boolean disablePlugin) {
    UUID playerUUID = p.getUniqueId();
    if (lastLogouts.contains(
        playerUUID)) { //If a player logs out while they were already in the list of recent logouts, remove them
      lastLogouts.remove(playerUUID);
    }
    removeLastEntries(
        disablePlugin); //Ensure the recent logout list never exceeds a specified limit (maxPlayerStored)
    lastLogouts.add(0, playerUUID); //Adds this player as the most recent logout
  }

  public void removeLastEntries(boolean disablePlugin) {
    int playersStored = lastLogouts.size();
    if (playersStored
        >= maxPlayersStored) { //We are about to add a person, so if the list is full we need to remove the last person on this list
      OfflinePlayerStatLoadIn offlinePlayerStatLoadIn = new OfflinePlayerStatLoadIn();
      for (int i = 0; i <= (playersStored - maxPlayersStored); i++) {
        if (lastLogouts.isEmpty()) {
          return;
        }
        UUID removedPlayerUUID = lastLogouts.get(lastLogouts.size() - 1);
        if (!disablePlugin) { //They will be removed anyway if the plugin is disabling
          offlinePlayerStatLoadIn.unloadOfflinePlayer(
              removedPlayerUUID); //Unloads the players stats ONLY if they're offline
        }
        lastLogouts.remove(lastLogouts.size() - 1); //Removes the player's stats from the queue.
        //At this point the player is either online and will be added to lastLogouts soon
        //Or they are too inactive for their stats to be preloaded with the server.
      }
    }
  }

  public void fillLastLogoutSlots() {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    File userdata = new File(plugin.getDataFolder(), File.separator + "PlayerDatabase");
    File[] allUsers = userdata.listFiles();
    for (File f : allUsers) {
      UUID playerUUID = UUID.fromString(f.getName().replace(".yml", ""));
      if (!lastLogouts.contains(playerUUID)) {
        lastLogouts.add(playerUUID);
      }
      if (lastLogouts.size() >= maxPlayersStored) {
        break;
      }
    }
  }


}
