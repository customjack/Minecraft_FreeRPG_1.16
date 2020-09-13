package mc.carlton.freerpg.perksAndAbilities;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.ActionBarMessages;
import mc.carlton.freerpg.gameTools.LanguageSelector;
import mc.carlton.freerpg.globalVariables.EntityGroups;
import mc.carlton.freerpg.playerAndServerInfo.*;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class Archery {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    private Player p;
    private ItemStack itemInHand;
    private static ArrayList<Entity> arrowsToRemove = new ArrayList<>();
    private String skillName = "archery";
    Map<String,Integer> expMap;

    ChangeStats increaseStats; //Changing Stats

    AbilityTracker abilities; //Abilities class
    // GET ABILITY STATUSES LIKE THIS:   Integer[] pAbilities = abilities.getPlayerAbilities(p);

    AbilityTimers timers; //Ability Timers class
    //GET TIMERS LIKE THIS:              Integer[] pTimers = timers.getPlayerTimers(p);

    PlayerStats pStatClass;
    //GET PLAYER STATS LIKE THIS:        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData(p);

    ActionBarMessages actionMessage;
    LanguageSelector lang;

    Random rand = new Random(); //Random class Import

    private boolean runMethods;

    public Archery(Player p) {
        this.p = p;
        this.itemInHand = p.getInventory().getItemInMainHand();
        this.increaseStats = new ChangeStats(p);
        this.abilities = new AbilityTracker(p);
        this.timers = new AbilityTimers(p);
        this.pStatClass = new PlayerStats(p);
        this.actionMessage = new ActionBarMessages(p);
        this.lang = new LanguageSelector(p);
        ConfigLoad configLoad = new ConfigLoad();
        this.runMethods = configLoad.getAllowedSkillsMap().get(skillName);
        expMap = configLoad.getExpMapForSkill(skillName);
    }

    public void initiateAbility() {
        if (!runMethods) {
            return;
        }
        if (!p.hasPermission("freeRPG.archeryAbility")) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        if ((int) pStat.get("global").get(24) < 1 || !pStatClass.isPlayerSkillAbilityOn(skillName)) {
            return;
        }
        Integer[] pAbilities = abilities.getPlayerAbilities();
        if (pAbilities[5] == -1) {
            int cooldown = timers.getPlayerCooldownTime(skillName);
            if (cooldown < 1) {
                int prepMessages = (int) pStatClass.getPlayerData().get("global").get(22); //Toggle for preparation messages
                if (prepMessages > 0) {
                    actionMessage.sendMessage(ChatColor.GRAY + ">>>" + lang.getString("prepare") + " " + lang.getString("bow") + "...<<<");
                }
                int taskID = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (prepMessages > 0) {
                            actionMessage.sendMessage(ChatColor.GRAY + ">>>..." + lang.getString("rest") + " " +lang.getString("bow") + "<<<");
                        }
                        try {
                            abilities.setPlayerAbility( skillName, -1);
                        }
                        catch (Exception e) {

                        }
                    }
                }.runTaskLater(plugin, 20 * 4).getTaskId();
                abilities.setPlayerAbility( skillName, taskID);
            } else {
                actionMessage.sendMessage(ChatColor.RED +lang.getString("rapidFire") + " " + lang.getString("cooldown") + ": " + ChatColor.WHITE + cooldown+ ChatColor.RED + "s");
            }
        }
    }

    public void enableAbility() {
        if (!runMethods) {
            return;
        }
        Integer[] pAbilities = abilities.getPlayerAbilities();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        actionMessage.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + ">>>" + lang.getString("rapidFire") + " " + lang.getString("activated") + "<<<");
        int durationLevel = (int) pStat.get(skillName).get(4);
        double duration0 = Math.ceil(durationLevel * 0.4) + 40;
        long duration = (long) duration0;
        Bukkit.getScheduler().cancelTask(pAbilities[5]);
        abilities.setPlayerAbility( skillName, -2);
        String coolDownEndMessage = ChatColor.GREEN + ">>>" + lang.getString("rapidFire") + " " + lang.getString("readyToUse") + "<<<";
        String endMessage = ChatColor.RED + ChatColor.BOLD.toString() + ">>>" + lang.getString("rapidFire") + " " + lang.getString("ended") + "<<<";
        timers.abilityDurationTimer(skillName,duration,endMessage,coolDownEndMessage);
    }

    public void rapidFire(Entity projectile,ItemStack bow) {
        if (!runMethods) {
            return;
        }
        Integer[] pAbilities = abilities.getPlayerAbilities();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int sniperLevel = (int) pStat.get(skillName).get(8);
        double sniperMultiplier = sniperLevel * 0.02 + 1;
        if (pAbilities[5] == -2) {
            Vector velocity = projectile.getVelocity();
            Vector zero = new Vector(0, 0, 0);
            double speed = velocity.distance(zero);
            double multiplier = (3.05 / speed) * sniperMultiplier;
            if (bow.getType() == Material.CROSSBOW) {
                multiplier = (3.15 / speed) * sniperMultiplier;
            }
            Vector newVelocity = velocity.multiply(multiplier);
            projectile.setVelocity(newVelocity);
        }
    }

    public void sniper(Entity projectile) {
        if (!runMethods) {
            return;
        }
        Integer[] pAbilities = abilities.getPlayerAbilities();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        if (pAbilities[5] != -2) {
            int sniperLevel = (int) pStat.get(skillName).get(8);
            double sniperMultiplier = sniperLevel * 0.02 + 1;
            Vector velocity = projectile.getVelocity();
            Vector newVelocity = velocity.multiply(sniperMultiplier);
            projectile.setVelocity(newVelocity);
        }
    }

    public void retrieval(Entity projectile,ItemStack bow) {
        if (!runMethods) {
            return;
        }
        if (bow.getEnchantments().containsKey(Enchantment.ARROW_INFINITE)) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int retrievalLevel = (int) pStat.get(skillName).get(5);
        double retrievalChance = retrievalLevel * 0.0005;
        if (retrievalChance > rand.nextDouble()) {
            if (projectile instanceof Arrow || projectile instanceof SpectralArrow) {
                ItemStack arrow = new ItemStack(Material.ARROW, 1);
                ItemStack arrow0 = new ItemStack(Material.ARROW, 1);
                int closest = 50;
                int arrowIndex = p.getInventory().first(Material.ARROW);
                int arrowIndex_s = p.getInventory().first(Material.SPECTRAL_ARROW);
                int arrowIndex_t = p.getInventory().first(Material.TIPPED_ARROW);
                if (arrowIndex < closest && arrowIndex != -1) {
                    arrow0 = p.getInventory().getItem(arrowIndex);
                    closest = arrowIndex;
                }
                if (arrowIndex_s < closest && arrowIndex_s != -1) {
                    arrow0 = p.getInventory().getItem(arrowIndex_s);
                    closest = arrowIndex_s;
                }
                if (arrowIndex_t < closest && arrowIndex_t != -1) {
                    arrow0 = p.getInventory().getItem(arrowIndex_t);
                    closest = arrowIndex_t;
                }
                arrow.setType(arrow0.getType());
                arrow.setItemMeta(arrow0.getItemMeta());
                arrow.setAmount(1);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        p.getInventory().addItem(arrow);
                    }
                }.runTaskLater(plugin, 2);
                arrowsToRemove.add(projectile);
            }
        }
    }

    public void retrievalRemoval(Entity projectile){
        if (!runMethods) {
            return;
        }
        if (arrowsToRemove.contains(projectile)) {
            arrowsToRemove.remove(projectile);
            projectile.remove();
        }
    }

    public void explosiveArrows(Entity projectile, Location loc) {
        if (!runMethods) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int explosiveLevel = (int) pStat.get(skillName).get(10);
        double explosionChance = explosiveLevel * 0.01;
        if (explosionChance > rand.nextDouble()) {
            World world = projectile.getWorld();
            world.createExplosion(loc, 3, false, false);
            increaseStats.changeEXP(skillName,expMap.get("explosiveArrowEXP"));
        }
    }

    public Material getArrowType() {
        int closest = 50;
        int arrowIndex = p.getInventory().first(Material.ARROW);
        int arrowIndex_s = p.getInventory().first(Material.SPECTRAL_ARROW);
        int arrowIndex_t = p.getInventory().first(Material.TIPPED_ARROW);
        Material arrowType = Material.AIR;
        if (arrowIndex < closest && arrowIndex != -1) {
            closest = arrowIndex;
            arrowType = Material.ARROW;
        }
        if (arrowIndex_s < closest && arrowIndex_s != -1) {
            closest = arrowIndex_s;
            arrowType = Material.SPECTRAL_ARROW;
        }
        if (arrowIndex_t < closest && arrowIndex_t != -1) {
            closest = arrowIndex_t;
            arrowType = Material.TIPPED_ARROW;
        }
        return arrowType;
    }

    public void crossbowAbility() {
        if (!runMethods) {
            return;
        }
        Integer[] pAbilities = abilities.getPlayerAbilities();
        if (pAbilities[5] != -2) {
            return;
        }
        ItemStack crossbow = itemInHand;
        CrossbowMeta crossbowMeta = (CrossbowMeta) crossbow.getItemMeta();
        if (!crossbowMeta.hasChargedProjectiles()) {
            if (pAbilities[12] > -1) {
                Bukkit.getScheduler().cancelTask(pAbilities[12]);
            }
            int taskID = new BukkitRunnable() {
                @Override
                public void run() {
                    Material arrowType = getArrowType();
                    if (arrowType != Material.AIR) {
                        int arrowIndex = p.getInventory().first(arrowType);
                        crossbowMeta.addChargedProjectile(new ItemStack(arrowType, 1));
                        crossbow.setItemMeta(crossbowMeta);
                        ItemStack arrow = p.getInventory().getItem(arrowIndex);
                        if (arrow.getType() == arrowType) {
                            arrow.setAmount(arrow.getAmount() - 1);
                        }
                    }
                }
            }.runTaskLater(plugin, 3).getTaskId();
            abilities.setPlayerAbility( "archeryCrossbow", taskID);
        }
    }

    public void giveHitEXP(double finalDamage) {
        if (!runMethods) {
            return;
        }
        increaseStats.changeEXP(skillName,expMap.get("hitArrow"));
        increaseStats.changeEXP(skillName, (int) Math.round(finalDamage *expMap.get("arrowDamage_EXPperDamagePointDone")));
    }

    public void giveKillEXP(Entity entity) {
        if (!runMethods) {
            return;
        }
        EntityGroups entityGroups = new EntityGroups();
        entityGroups.killEntity(entity,skillName,expMap,increaseStats);
    }


}
