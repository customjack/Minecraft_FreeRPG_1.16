package mc.carlton.freerpg.perksAndAbilities;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.ActionBarMessages;
import mc.carlton.freerpg.gameTools.LanguageSelector;
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
        Integer[] pTimers = timers.getPlayerTimers();
        Integer[] pAbilities = abilities.getPlayerAbilities();
        if (pAbilities[5] == -1) {
            int cooldown = pTimers[5];
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
                actionMessage.sendMessage(ChatColor.RED +lang.getString("rapidFire") + " " + lang.getString("cooldown") + ": " + cooldown+ "s");
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
        int cooldown = 300;
        if ((int) pStat.get("global").get(11) > 0) {
            cooldown = 200;
        }
        int finalCooldown = cooldown;
        long duration = (long) duration0;
        timers.setPlayerTimer( skillName, finalCooldown);
        Bukkit.getScheduler().cancelTask(pAbilities[5]);
        abilities.setPlayerAbility( skillName, -2);
        int taskID = new BukkitRunnable() {
            @Override
            public void run() {
                actionMessage.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + ">>>" + lang.getString("rapidFire") + " " + lang.getString("ended") + "<<<");
                abilities.setPlayerAbility( skillName, -1);
                for (int i = 1; i < finalCooldown +1; i++) {
                    int timeRemaining = finalCooldown - i;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            timers.setPlayerTimer( skillName, timeRemaining);
                            AbilityTimers timers2 = new AbilityTimers(p);
                            if (timeRemaining ==0) {
                                if (!p.isOnline()) {
                                    timers2.removePlayer();
                                }
                                else {
                                    actionMessage.sendMessage(ChatColor.GREEN + ">>>" + lang.getString("rapidFire") + " " + lang.getString("readyToUse") + "<<<");
                                }
                            }
                        }
                    }.runTaskLater(plugin, 20 * i);
                }
            }
        }.runTaskLater(plugin, duration).getTaskId();
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
        if (entity instanceof LivingEntity) {
            if (entity instanceof Mob) {
                Mob mob = (Mob) entity;
                EntityType type = mob.getType();
                switch (type) {
                    case BAT:
                        increaseStats.changeEXP(skillName, expMap.get("killBat"));
                        break;
                    case CAT:
                        increaseStats.changeEXP(skillName, expMap.get("killCat"));
                        break;
                    case CHICKEN:
                        increaseStats.changeEXP(skillName, expMap.get("killChicken"));
                        break;
                    case COD:
                        increaseStats.changeEXP(skillName, expMap.get("killCod"));
                        break;
                    case COW:
                        increaseStats.changeEXP(skillName, expMap.get("killCow"));
                        break;
                    case DONKEY:
                        increaseStats.changeEXP(skillName, expMap.get("killDonkey"));
                        break;
                    case FOX:
                        increaseStats.changeEXP(skillName, expMap.get("killFox"));
                        break;
                    case HORSE:
                        increaseStats.changeEXP(skillName, expMap.get("killHorse"));
                        break;
                    case POLAR_BEAR:
                        increaseStats.changeEXP(skillName, expMap.get("killPolarBear"));
                        break;
                    case MUSHROOM_COW:
                        increaseStats.changeEXP(skillName, expMap.get("killMooshroom"));
                        break;
                    case MULE:
                        increaseStats.changeEXP(skillName, expMap.get("killMule"));
                        break;
                    case OCELOT:
                        increaseStats.changeEXP(skillName, expMap.get("killOcelot"));
                        break;
                    case PARROT:
                        increaseStats.changeEXP(skillName, expMap.get("killParrot"));
                        break;
                    case PIG:
                        increaseStats.changeEXP(skillName, expMap.get("killPig"));
                        break;
                    case PIGLIN:
                        increaseStats.changeEXP(skillName, expMap.get("killPiglin"));
                        break;
                    case RABBIT:
                        increaseStats.changeEXP(skillName, expMap.get("killRabbit"));
                        break;
                    case SALMON:
                        increaseStats.changeEXP(skillName, expMap.get("killSalmon"));
                        break;
                    case SHEEP:
                        increaseStats.changeEXP(skillName, expMap.get("killSheep"));
                        break;
                    case SKELETON_HORSE:
                        increaseStats.changeEXP(skillName, expMap.get("killSkeleton_Horse"));
                        break;
                    case SNOWMAN:
                        increaseStats.changeEXP(skillName, expMap.get("killSnowman"));
                        break;
                    case SQUID:
                        increaseStats.changeEXP(skillName, expMap.get("killSquid"));
                        break;
                    case STRIDER:
                        increaseStats.changeEXP(skillName, expMap.get("killStrider"));
                        break;
                    case TROPICAL_FISH:
                        increaseStats.changeEXP(skillName, expMap.get("killTropical_Fish"));
                        break;
                    case TURTLE:
                        increaseStats.changeEXP(skillName, expMap.get("killTurtle"));
                        break;
                    case VILLAGER:
                        increaseStats.changeEXP(skillName, expMap.get("killVillager"));
                        break;
                    case WANDERING_TRADER:
                        increaseStats.changeEXP(skillName, expMap.get("killWandering_Trader"));
                        break;
                    case BEE:
                        increaseStats.changeEXP(skillName, expMap.get("killBee"));
                        break;
                    case CAVE_SPIDER:
                        increaseStats.changeEXP(skillName, expMap.get("killCaveSpider"));
                        break;
                    case DOLPHIN:
                        increaseStats.changeEXP(skillName, expMap.get("killDolphin"));
                        break;
                    case ENDERMAN:
                        increaseStats.changeEXP(skillName, expMap.get("killEnderman"));
                        break;
                    case IRON_GOLEM:
                        increaseStats.changeEXP(skillName, expMap.get("killIron_Golem"));
                        break;
                    case LLAMA:
                        increaseStats.changeEXP(skillName, expMap.get("killLlama"));
                        break;
                    case PANDA:
                        increaseStats.changeEXP(skillName, expMap.get("killPanda"));
                        break;
                    case PUFFERFISH:
                        increaseStats.changeEXP(skillName, expMap.get("killPufferfish"));
                        break;
                    case SPIDER:
                        increaseStats.changeEXP(skillName, expMap.get("killSpider"));
                        break;
                    case WOLF:
                        increaseStats.changeEXP(skillName, expMap.get("killWolf"));
                        break;
                    case ZOMBIFIED_PIGLIN:
                        increaseStats.changeEXP(skillName, expMap.get("killZombie_Pigman"));
                        break;
                    case BLAZE:
                        increaseStats.changeEXP(skillName, expMap.get("killBlaze"));
                        break;
                    case CREEPER:
                        increaseStats.changeEXP(skillName, expMap.get("killCreeper"));
                        break;
                    case DROWNED:
                        increaseStats.changeEXP(skillName, expMap.get("killDrowned"));
                        break;
                    case ELDER_GUARDIAN:
                        increaseStats.changeEXP(skillName, expMap.get("killElder_Guardian"));
                        break;
                    case ENDERMITE:
                        increaseStats.changeEXP(skillName, expMap.get("killEndermite"));
                        break;
                    case EVOKER:
                        increaseStats.changeEXP(skillName, expMap.get("killEvoker"));
                        break;
                    case GHAST:
                        increaseStats.changeEXP(skillName, expMap.get("killGhast"));
                        break;
                    case GUARDIAN:
                        increaseStats.changeEXP(skillName, expMap.get("killGuardian"));
                        break;
                    case HOGLIN:
                        increaseStats.changeEXP(skillName, expMap.get("killHoglin"));
                        break;
                    case HUSK:
                        increaseStats.changeEXP(skillName, expMap.get("killHusk"));
                        break;
                    case MAGMA_CUBE:
                        increaseStats.changeEXP(skillName, expMap.get("killMagma_Cube"));
                        break;
                    case PHANTOM:
                        increaseStats.changeEXP(skillName, expMap.get("killPhantom"));
                        break;
                    case PILLAGER:
                        increaseStats.changeEXP(skillName, expMap.get("killPillager"));
                        break;
                    case RAVAGER:
                        increaseStats.changeEXP(skillName, expMap.get("killRavager"));
                        break;
                    case SHULKER:
                        increaseStats.changeEXP(skillName, expMap.get("killShulker"));
                        break;
                    case SILVERFISH:
                        increaseStats.changeEXP(skillName, expMap.get("killSilverfish"));
                        break;
                    case SKELETON:
                        increaseStats.changeEXP(skillName, expMap.get("killSkeleton"));
                        break;
                    case SLIME:
                        increaseStats.changeEXP(skillName, expMap.get("killSlime"));
                        break;
                    case STRAY:
                        increaseStats.changeEXP(skillName, expMap.get("killStray"));
                        break;
                    case VEX:
                        increaseStats.changeEXP(skillName, expMap.get("killVex"));
                        break;
                    case VINDICATOR:
                        increaseStats.changeEXP(skillName, expMap.get("killVindicator"));
                        break;
                    case WITCH:
                        increaseStats.changeEXP(skillName, expMap.get("killWitch"));
                        break;
                    case WITHER_SKELETON:
                        increaseStats.changeEXP(skillName, expMap.get("killWitherSkeleton"));
                        break;
                    case ZOGLIN:
                        increaseStats.changeEXP(skillName, expMap.get("killZoglin"));
                        break;
                    case ZOMBIE:
                        increaseStats.changeEXP(skillName, expMap.get("killZombie"));
                        break;
                    case ZOMBIE_VILLAGER:
                        increaseStats.changeEXP(skillName, expMap.get("killZombie_Villager"));
                        break;
                    case ENDER_DRAGON:
                        increaseStats.changeEXP(skillName, expMap.get("killEnder_Dragon"));
                        break;
                    case WITHER:
                        increaseStats.changeEXP(skillName, expMap.get("killWither"));
                        break;
                    case ZOMBIE_HORSE:
                        increaseStats.changeEXP(skillName, expMap.get("killZombie_Horse"));
                        break;
                    case ILLUSIONER:
                        increaseStats.changeEXP(skillName, expMap.get("killIllusioner"));
                        break;
                    case GIANT:
                        increaseStats.changeEXP(skillName, expMap.get("killGiant"));
                        break;
                    default:
                        increaseStats.changeEXP(skillName, expMap.get("killAnythingElse"));
                        break;
                }
            }
        }
    }


}
