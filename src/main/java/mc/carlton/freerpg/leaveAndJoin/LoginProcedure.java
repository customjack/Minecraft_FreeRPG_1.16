package mc.carlton.freerpg.leaveAndJoin;

import mc.carlton.freerpg.perksAndAbilities.Agility;
import mc.carlton.freerpg.perksAndAbilities.Farming;
import mc.carlton.freerpg.perksAndAbilities.Fishing;
import mc.carlton.freerpg.playerAndServerInfo.*;
import org.bukkit.Material;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class LoginProcedure {
    Player p;
    private UUID uuid;

    public LoginProcedure(Player p) {
        this.p = p;
        this.uuid =uuid;
    }

    public void playerLogin() {
        //If the player is new, creates a new stats file for them,
        //If the player's stat file is not properly formatted, this also fixes that
        StatManager.playJoinConditions(p);

        //Read in player's past stats into an the playerStats Class
        PlayerStatsLoadIn loadInPlayer = new PlayerStatsLoadIn(p);
        Map<String, ArrayList<Number>> playerStats0 = loadInPlayer.getPlayerStatsMap(p);

        //Combine the player's stats with everyone on the server's
        PlayerStats pStatsClass = new PlayerStats(p);
        Map<UUID, Map<String, ArrayList<Number>>> allStats = pStatsClass.getData();
        allStats.put(uuid,playerStats0);
        pStatsClass.setData(allStats);

        //Makes sure player's attack speed is normal (TODO make base value a adjustable paramater in config)
        ((Attributable) p).getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.0);

        //Initiates player abilities
        Integer[] initAbils = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
        AbilityTracker abilitiesClass = new AbilityTracker(p);
        Map<UUID, Integer[]> allAbilities = abilitiesClass.getAbilities();
        allAbilities.put(uuid,initAbils);
        abilitiesClass.setAbilities(allAbilities);

        //Initiates player timers
        AbilityTimers timersClass = new AbilityTimers(p);
        Map<UUID, Integer[]> allTimers = timersClass.getTimers();
        if (!allTimers.containsKey(uuid)) {
            Integer[] initTimers = {0,0,0,0,0,0,0,0,0,0,0,0};
            allTimers.put(uuid, initTimers);
            timersClass.setTimes(allTimers);
        }

        //Initiates player abiliyLogoutTracker
        AbilityLogoutTracker abilityLogout = new AbilityLogoutTracker(p);
        Map<Player, Integer[]> logoutTasks = abilityLogout.getTasks();
        Map<Player, ItemStack[]> logoutItems = abilityLogout.getItems();
        if (!logoutTasks.containsKey(p)) {
            Integer[] initTasks = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
            ItemStack[] initItems = {new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0),
                    new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0),
                    new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0),
                    new ItemStack(Material.AIR,0)};
            logoutTasks.put(p, initTasks);
            abilityLogout.setTasks(logoutTasks);
            logoutItems.put(p,initItems);
            abilityLogout.setItems(logoutItems);
        }

        //Initializes Abilities
        Farming farmingClass =  new Farming(p);
        farmingClass.oneWithNatureStart();

        Fishing fishingClass = new Fishing(p);
        fishingClass.fishPersonStart();

        Agility agilityClass = new Agility(p);
        agilityClass.gracefulFeetStart();
    }
}
