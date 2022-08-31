package mc.carlton.freerpg.core.leaveAndJoin;

import java.io.IOException;
import mc.carlton.freerpg.utils.game.BlockFaceTracker;
import mc.carlton.freerpg.utils.game.BossBarStorage;
import mc.carlton.freerpg.utils.game.BrewingStandUserTracker;
import mc.carlton.freerpg.utils.game.FurnaceUserTracker;
import mc.carlton.freerpg.utils.game.TrackItem;
import mc.carlton.freerpg.skills.perksAndAbilities.Agility;
import mc.carlton.freerpg.skills.perksAndAbilities.Defense;
import mc.carlton.freerpg.skills.perksAndAbilities.Digging;
import mc.carlton.freerpg.skills.perksAndAbilities.Farming;
import mc.carlton.freerpg.skills.perksAndAbilities.Fishing;
import mc.carlton.freerpg.skills.perksAndAbilities.Mining;
import mc.carlton.freerpg.skills.perksAndAbilities.Swordsmanship;
import mc.carlton.freerpg.core.info.player.AbilityLogoutTracker;
import mc.carlton.freerpg.core.info.player.AbilityTimers;
import mc.carlton.freerpg.core.info.player.AbilityTracker;
import mc.carlton.freerpg.core.info.player.PlayerStats;
import mc.carlton.freerpg.core.info.player.PlayerStatsLoadIn;
import mc.carlton.freerpg.core.info.server.RecentLogouts;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LogoutProcedure {

  Player p;
  private String pName;

  public LogoutProcedure(Player p) {
    this.p = p;
    this.pName = p.getDisplayName();
  }

  public void playerLogout(boolean disablePlugin) throws IOException {
    // The player stats have not been saved yet, so we don't want to remove the player
    PlayerStats playerStats = new PlayerStats(p);
    playerStats.setPlayerAreStatsSaved(false);

    //Adds player to the list of last n logouts ("n" specified in config)
    RecentLogouts recentLogouts = new RecentLogouts();
    recentLogouts.playerLogout(p, disablePlugin);

    //Saves Player Stats to file
    PlayerStatsLoadIn saveStats = new PlayerStatsLoadIn(p);

    if (disablePlugin) { //If the plugin is disabled, I don't care about performance (Plus I can't run an async task)
      saveStats.setPlayerStatsMap();
    } else { //If the plugin is not disabled, we async remove the player's stats to not affect performance
      saveStats.asyncStatSave();
    }

    //Ensures no items stay permanently altered from abilities
    preventLogoutTheft(disablePlugin);

    //Ends all tasks that track players' buffs gained from some skills
    Farming farmingClass = new Farming(p);
    farmingClass.oneWithNatureEnd();
    Fishing fishingClass = new Fishing(p);
    fishingClass.fishPersonEnd();
    Agility agilityClass = new Agility(p);
    agilityClass.gracefulFeetEnd();

    BossBarStorage bossBarStorage = new BossBarStorage();
    bossBarStorage.removePlayer(p);

    //If the plugin has been disabled, the rest will take care of itself.
    if (disablePlugin) {
      return;
    }

    //Removes tracked player data
    BrewingStandUserTracker brewDelete = new BrewingStandUserTracker();
    brewDelete.removeAllPlayerStands(p);
    FurnaceUserTracker furnaceDelete = new FurnaceUserTracker();
    furnaceDelete.removeAllPlayerfurnaceLocations(p);
    BlockFaceTracker blockFaceDelete = new BlockFaceTracker();
    blockFaceDelete.removePlayerBlockFace(p);

    //Removes players stats,abilities,timers,and logout trackers from the hashmaps, all stat information should be saved

        /*Player stats SHOULD be removed by OfflinePlayerStatLoadIn.
        PlayerStats deleteStats = new PlayerStats(p);
        deleteStats.removePlayer();
         */

    AbilityTracker deleteAbilities = new AbilityTracker(p);
    deleteAbilities.removePlayer();

    AbilityTimers deleteTimers = new AbilityTimers(p);
    deleteTimers.removePlayer();

    AbilityLogoutTracker deleteLogoutTracked = new AbilityLogoutTracker(p);
    deleteLogoutTracked.removePlayer();
  }

  public void preventLogoutTheft(boolean disablePlugin) {
    preventLogoutTheftDefense(disablePlugin);
    preventLogoutTheftMining(disablePlugin);
    preventLogoutTheftDigging(disablePlugin);
    preventLogoutTheftSwordsmanship(disablePlugin);
  }

  public void preventLogoutTheftMining(boolean disablePlugin) {
    AbilityLogoutTracker logoutTracker = new AbilityLogoutTracker(p);
    TrackItem trackItem = new TrackItem();

    NamespacedKey key_mining = logoutTracker.getPlayerKeys().get("mining");
    int taskID_mining = logoutTracker.getPlayerTasks().get("mining");
    ItemStack itemInHand_mining = trackItem.findTrackedItemInInventory(p, key_mining);

    if (itemInHand_mining != null) {
      Mining miningClass = new Mining(p);
      miningClass.preventLogoutTheft(taskID_mining, itemInHand_mining, key_mining, disablePlugin);
    }
  }

  public void preventLogoutTheftDigging(boolean disablePlugin) {
    AbilityLogoutTracker logoutTracker = new AbilityLogoutTracker(p);
    TrackItem trackItem = new TrackItem();

    NamespacedKey key_digging = logoutTracker.getPlayerKeys().get("digging");
    int taskID_digging = logoutTracker.getPlayerTasks().get("digging");
    ItemStack itemInHand_digging = trackItem.findTrackedItemInInventory(p, key_digging);

    if (itemInHand_digging != null) {
      Digging diggingClass = new Digging(p);
      diggingClass.preventLogoutTheft(taskID_digging, itemInHand_digging, key_digging,
          disablePlugin);
    }
  }

  public void preventLogoutTheftSwordsmanship(boolean disablePlugin) {
    AbilityLogoutTracker logoutTracker = new AbilityLogoutTracker(p);
    TrackItem trackItem = new TrackItem();

    NamespacedKey key_swordsmanship = logoutTracker.getPlayerKeys().get("swordsmanship");
    int taskID_swordsmanship = logoutTracker.getPlayerTasks().get("swordsmanship");
    ItemStack itemInHand_swordsmanship = trackItem.findTrackedItemInInventory(p, key_swordsmanship);

    if (itemInHand_swordsmanship != null) {
      Swordsmanship swordsmanshipClass = new Swordsmanship(p);
      swordsmanshipClass.preventLogoutTheft(taskID_swordsmanship, itemInHand_swordsmanship,
          key_swordsmanship, disablePlugin);
    }
  }

  public void preventLogoutTheftDefense(boolean disablePlugin) {
    AbilityLogoutTracker logoutTracker = new AbilityLogoutTracker(p);
    int taskID_defense = logoutTracker.getPlayerTasks().get("defense");

    Defense defenseClass = new Defense(p);
    defenseClass.preventLogoutTheft(taskID_defense, disablePlugin);
  }
}
