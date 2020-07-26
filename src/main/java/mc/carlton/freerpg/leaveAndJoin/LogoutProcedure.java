package mc.carlton.freerpg.leaveAndJoin;

import mc.carlton.freerpg.gameTools.BlockFaceTracker;
import mc.carlton.freerpg.gameTools.BrewingStandUserTracker;
import mc.carlton.freerpg.gameTools.FurnaceUserTracker;
import mc.carlton.freerpg.perksAndAbilities.*;
import mc.carlton.freerpg.playerAndServerInfo.*;
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

    public void playerLogout() throws IOException {
        //Saves Player Stats to file
        PlayerStatsLoadIn saveStats = new PlayerStatsLoadIn(p);
        saveStats.setPlayerStatsMap(p);

        //Removes tracked player data
        BrewingStandUserTracker brewDelete = new BrewingStandUserTracker();
        brewDelete.removeAllPlayerStands(p);
        FurnaceUserTracker furnaceDelete = new FurnaceUserTracker();
        furnaceDelete.removeAllPlayerfurnaceLocations(p);
        BlockFaceTracker blockFaceDelete = new BlockFaceTracker();
        blockFaceDelete.removePlayerBlockFace(p);

        //Ensures no items stay permanently altered from abilities
        AbilityLogoutTracker logoutTracker = new AbilityLogoutTracker(p);

        ItemStack itemInHand_digging = logoutTracker.getPlayerItems(p)[0];
        int taskID_digging = logoutTracker.getPlayerTasks(p)[0];

        ItemStack itemInHand_mining = logoutTracker.getPlayerItems(p)[2];
        int taskID_mining = logoutTracker.getPlayerTasks(p)[2];

        ItemStack itemInHand_swordsmanship = logoutTracker.getPlayerItems(p)[7];
        int taskID_swordsmanship = logoutTracker.getPlayerTasks(p)[7];

        int taskID_defense = logoutTracker.getPlayerTasks(p)[8];

        Digging diggingClass = new Digging(p);
        diggingClass.preventLogoutTheft(taskID_digging,itemInHand_digging);
        Mining miningClass = new Mining(p);
        miningClass.preventLogoutTheft(taskID_mining,itemInHand_mining);
        Swordsmanship swordsmanshipClass = new Swordsmanship(p);
        swordsmanshipClass.preventLogoutTheft(taskID_swordsmanship,itemInHand_swordsmanship);
        Defense defenseClass = new Defense(p);
        defenseClass.preventLogoutTheft(taskID_defense);

        //Ends all tasks that track players' buffs gained from some skills
        Farming farmingClass =  new Farming(p);
        farmingClass.oneWithNatureEnd();
        Fishing fishingClass = new Fishing(p);
        fishingClass.fishPersonEnd();
        Agility agilityClass = new Agility(p);
        agilityClass.gracefulFeetEnd();

        //Removes players stats,abilities,timers,and logout trackers from the hashmaps, all stat information should be saved
        PlayerStats deleteStats = new PlayerStats(p);
        deleteStats.removePlayer();

        AbilityTracker deleteAbilities = new AbilityTracker(p);
        deleteAbilities.removePlayer();

        AbilityTimers deleteTimers = new AbilityTimers(p);
        deleteTimers.removePlayer();

        AbilityLogoutTracker deleteLogoutTracked = new AbilityLogoutTracker(p);
        deleteLogoutTracked.removePlayer(p);
    }
}
