package mc.carlton.freerpg.combatEvents;

import mc.carlton.freerpg.perksAndAbilities.*;
import mc.carlton.freerpg.playerAndServerInfo.AbilityLogoutTracker;
import mc.carlton.freerpg.playerAndServerInfo.AbilityTracker;
import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerDeath implements Listener {

    @EventHandler
    void onPlayerDie(PlayerDeathEvent e){
        Player p = e.getEntity();
        PlayerStats pStatClass = new PlayerStats(p);
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int immortalExperienceLevel = (int) pStat.get("enchanting").get(13);
        int expBuffLevel = (int) pStat.get("enchanting").get(4);
        double multiplier = 1 + expBuffLevel*0.002;
        e.setDroppedExp( (int) Math.round(e.getDroppedExp()/multiplier) );
        if (immortalExperienceLevel > 0) {
            e.setKeepLevel(true);
            e.setDroppedExp(0);
        }
        List<ItemStack> drops = e.getDrops();
        AbilityTracker abilities = new AbilityTracker(p);
        Integer[] pAbilities = abilities.getPlayerAbilities();
        if (pAbilities[0] != -1) {
            AbilityLogoutTracker logoutTracker = new AbilityLogoutTracker(p);
            ItemStack itemInHand_digging = logoutTracker.getPlayerItems(p)[0];
            int taskID_digging = logoutTracker.getPlayerTasks(p)[0];
            for (ItemStack drop : drops) {
                if (drop.getItemMeta().equals(itemInHand_digging.getItemMeta())) {
                    ItemStack abilityItem = drop;
                    Digging diggingClass = new Digging(p);
                    diggingClass.preventLogoutTheft(taskID_digging, abilityItem);
                    break;
                }
            }
        }
        else if (pAbilities[2] != -1) {
            AbilityLogoutTracker logoutTracker = new AbilityLogoutTracker(p);
            ItemStack itemInHand_mining = logoutTracker.getPlayerItems(p)[2];
            int taskID_mining = logoutTracker.getPlayerTasks(p)[2];
            for (ItemStack drop: drops) {
                if (drop.getItemMeta().equals(itemInHand_mining.getItemMeta())) {
                    ItemStack abilityItem = drop;
                    Mining miningClass = new Mining(p);
                    miningClass.preventLogoutTheft(taskID_mining, abilityItem);
                    break;
                }
            }
        }
        else if (pAbilities[7] != -1) {
            AbilityLogoutTracker logoutTracker = new AbilityLogoutTracker(p);
            ItemStack itemInHand_swordsmanship = logoutTracker.getPlayerItems(p)[7];
            int taskID_swordsmanship = logoutTracker.getPlayerTasks(p)[7];
            for (ItemStack drop: drops) {
                if (drop.getItemMeta().equals(itemInHand_swordsmanship.getItemMeta())) {
                    ItemStack abilityItem = drop;
                    Swordsmanship swordsmanshipClass = new Swordsmanship(p);
                    swordsmanshipClass.preventLogoutTheft(taskID_swordsmanship, abilityItem);
                    break;
                }
            }
        }


        Global globalClass = new Global(p);
        globalClass.betterResurrectionDeath(drops);

    }
}
