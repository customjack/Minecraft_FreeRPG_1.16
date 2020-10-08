package mc.carlton.freerpg.leaveAndJoin;

import mc.carlton.freerpg.gameTools.*;
import mc.carlton.freerpg.perksAndAbilities.*;
import mc.carlton.freerpg.playerInfo.*;
import mc.carlton.freerpg.serverInfo.RecentLogouts;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

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
        recentLogouts.playerLogout(p,disablePlugin);

        //Saves Player Stats to file
        PlayerStatsLoadIn saveStats = new PlayerStatsLoadIn(p);

        if (disablePlugin) { //If the plugin is disabled, I don't care about performance (Plus I can't run an async task)
            saveStats.setPlayerStatsMap();
        }
        else { //If the plugin is not disabled, we async remove the player's stats to not affect performance
            saveStats.asyncStatSave();
        }

        //Ensures no items stay permanently altered from abilities
        AbilityLogoutTracker logoutTracker = new AbilityLogoutTracker(p);
        TrackItem trackItem = new TrackItem();


        NamespacedKey key_digging = logoutTracker.getPlayerKeys().get("digging");
        int taskID_digging = logoutTracker.getPlayerTasks().get("digging");
        ItemStack itemInHand_digging = trackItem.findTrackedItemInInventory(p,key_digging);

        NamespacedKey key_mining = logoutTracker.getPlayerKeys().get("mining");
        int taskID_mining = logoutTracker.getPlayerTasks().get("mining");
        ItemStack itemInHand_mining = trackItem.findTrackedItemInInventory(p,key_mining);

        NamespacedKey key_swordsmanship = logoutTracker.getPlayerKeys().get("swordsmanship");
        int taskID_swordsmanship = logoutTracker.getPlayerTasks().get("swordsmanship");
        ItemStack itemInHand_swordsmanship = trackItem.findTrackedItemInInventory(p,key_swordsmanship);

        int taskID_defense = logoutTracker.getPlayerTasks().get("defense");

        if (itemInHand_digging != null) {
            Digging diggingClass = new Digging(p);
            diggingClass.preventLogoutTheft(taskID_digging, itemInHand_digging,key_digging,disablePlugin);
        }
        if (itemInHand_mining != null) {
            Mining miningClass = new Mining(p);
            miningClass.preventLogoutTheft(taskID_mining, itemInHand_mining,key_mining,disablePlugin);
        }
        if (itemInHand_swordsmanship != null) {
            Swordsmanship swordsmanshipClass = new Swordsmanship(p);
            swordsmanshipClass.preventLogoutTheft(taskID_swordsmanship, itemInHand_swordsmanship,key_swordsmanship,disablePlugin);
        }
        Defense defenseClass = new Defense(p);
        defenseClass.preventLogoutTheft(taskID_defense,disablePlugin);

        //Ends all tasks that track players' buffs gained from some skills
        Farming farmingClass =  new Farming(p);
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
}
