package mc.carlton.freerpg.perksAndAbilities;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.ActionBarMessages;
import mc.carlton.freerpg.playerAndServerInfo.AbilityTimers;
import mc.carlton.freerpg.playerAndServerInfo.AbilityTracker;
import mc.carlton.freerpg.playerAndServerInfo.ChangeStats;
import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
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
    private String pName;
    private ItemStack itemInHand;
    private static ArrayList<Entity> arrowsToRemove = new ArrayList<>();

    ChangeStats increaseStats; //Changing Stats

    AbilityTracker abilities; //Abilities class
    // GET ABILITY STATUSES LIKE THIS:   Integer[] pAbilities = abilities.getPlayerAbilities(p);

    AbilityTimers timers; //Ability Timers class
    //GET TIMERS LIKE THIS:              Integer[] pTimers = timers.getPlayerTimers(p);

    PlayerStats pStatClass;
    //GET PLAYER STATS LIKE THIS:        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData(p);

    ActionBarMessages actionMessage;

    Random rand = new Random(); //Random class Import

    public Archery(Player p) {
        this.p = p;
        this.pName = p.getDisplayName();
        this.itemInHand = p.getInventory().getItemInMainHand();
        this.increaseStats = new ChangeStats(p);
        this.abilities = new AbilityTracker(p);
        this.timers = new AbilityTimers(p);
        this.pStatClass = new PlayerStats(p);
        this.actionMessage = new ActionBarMessages(p);
    }

    public void initiateAbility() {
        if (!p.hasPermission("freeRPG.archeryAbility")) {
            return;
        }
        Integer[] pTimers = timers.getPlayerTimers();
        Integer[] pAbilities = abilities.getPlayerAbilities();
        if (pAbilities[5] == -1) {
            int cooldown = pTimers[5];
            if (cooldown < 1) {
                int prepMessages = (int) pStatClass.getPlayerData().get("global").get(22); //Toggle for preparation messages
                if (prepMessages > 0) {
                    actionMessage.sendMessage(ChatColor.GRAY + ">>>You prepare your bow...<<<");
                }
                int taskID = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (prepMessages > 0) {
                            actionMessage.sendMessage(ChatColor.GRAY + ">>>...You rest your bow<<<");
                        }
                        try {
                            abilities.setPlayerAbility( "archery", -1);
                        }
                        catch (Exception e) {

                        }
                    }
                }.runTaskLater(plugin, 20 * 4).getTaskId();
                abilities.setPlayerAbility( "archery", taskID);
            } else {
                actionMessage.sendMessage(ChatColor.RED + "You must wait " + cooldown + " seconds to use Rapid Fire again.");
            }
        }
    }

    public void enableAbility() {
        Integer[] pAbilities = abilities.getPlayerAbilities();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        actionMessage.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + ">>>Rapid Fire Activated!<<<");
        int durationLevel = (int) pStat.get("archery").get(4);
        double duration0 = Math.ceil(durationLevel * 0.4) + 40;
        int cooldown = 300;
        if ((int) pStat.get("global").get(11) > 0) {
            cooldown = 200;
        }
        int finalCooldown = cooldown;
        long duration = (long) duration0;
        timers.setPlayerTimer( "archery", finalCooldown);
        Bukkit.getScheduler().cancelTask(pAbilities[5]);
        abilities.setPlayerAbility( "archery", -2);
        int taskID = new BukkitRunnable() {
            @Override
            public void run() {
                actionMessage.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + ">>>Rapid Fire has ended<<<");
                abilities.setPlayerAbility( "archery", -1);
                for (int i = 1; i < finalCooldown +1; i++) {
                    int timeRemaining = finalCooldown - i;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            timers.setPlayerTimer( "archery", timeRemaining);
                            AbilityTimers timers2 = new AbilityTimers(p);
                            if (timeRemaining ==0) {
                                if (!p.isOnline()) {
                                    timers2.removePlayer();
                                }
                                else {
                                    actionMessage.sendMessage(ChatColor.GREEN + ">>>Rapid Fire is ready to use again<<<");
                                }
                            }
                        }
                    }.runTaskLater(plugin, 20 * i);
                }
            }
        }.runTaskLater(plugin, duration).getTaskId();
    }

    public void rapidFire(Entity projectile,ItemStack bow) {
        Integer[] pAbilities = abilities.getPlayerAbilities();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int sniperLevel = (int) pStat.get("archery").get(8);
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
        Integer[] pAbilities = abilities.getPlayerAbilities();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        if (pAbilities[5] != -2) {
            int sniperLevel = (int) pStat.get("archery").get(8);
            double sniperMultiplier = sniperLevel * 0.02 + 1;
            Vector velocity = projectile.getVelocity();
            Vector newVelocity = velocity.multiply(sniperMultiplier);
            projectile.setVelocity(newVelocity);
        }
    }

    public void retrieval(Entity projectile,ItemStack bow) {
        if (bow.getEnchantments().containsKey(Enchantment.ARROW_INFINITE)) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int retrievalLevel = (int) pStat.get("archery").get(5);
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
        if (arrowsToRemove.contains(projectile)) {
            arrowsToRemove.remove(projectile);
            projectile.remove();
        }
    }

    public void explosiveArrows(Entity projectile) {
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int explosiveLevel = (int) pStat.get("archery").get(10);
        double explosionChance = explosiveLevel * 0.01;
        if (explosionChance > rand.nextDouble()) {
            World world = projectile.getWorld();
            Location loc = projectile.getLocation();
            world.createExplosion(loc, 3, false, false);
            increaseStats.changeEXP("archery",200);
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
        increaseStats.changeEXP("archery",50);
        increaseStats.changeEXP("archery", (int) Math.round(finalDamage * 8) * 10);
    }

    public void giveKillEXP(Entity entity) {
        if (entity instanceof LivingEntity) {
            EntityType type = entity.getType();
            if (entity instanceof Monster) {
                switch (type) {
                    case BLAZE:
                    case SKELETON:
                    case ZOMBIE:
                    case CAVE_SPIDER:
                    case SPIDER:
                        increaseStats.changeEXP("archery", 400);
                        break;
                    case STRIDER:
                    case CREEPER:
                        increaseStats.changeEXP("archery",1000);
                        break;
                    case HOGLIN:
                    case ZOGLIN:
                        increaseStats.changeEXP("archery",1200);
                        break;
                    case WITHER:
                        increaseStats.changeEXP("archery",30000);
                        break;
                    case ELDER_GUARDIAN:
                        increaseStats.changeEXP("archery",10000);
                        break;
                    default:
                        increaseStats.changeEXP("archery", 600);
                        break;
                }
            }
            else {
                switch (type) {
                    case ENDER_DRAGON:
                        increaseStats.changeEXP("archery",50000);
                        break;
                    case IRON_GOLEM:
                        increaseStats.changeEXP("archery", 750);
                        break;
                    case BEE:
                    case DOLPHIN:
                    case LLAMA:
                    case POLAR_BEAR:
                    case TRADER_LLAMA:
                    case WOLF:
                        increaseStats.changeEXP("archery",375);
                        break;
                    default:
                        increaseStats.changeEXP("archery",150);
                        break;
                }
            }
        }
    }


}
