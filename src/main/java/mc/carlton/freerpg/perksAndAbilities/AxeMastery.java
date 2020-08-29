package mc.carlton.freerpg.perksAndAbilities;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.ActionBarMessages;
import mc.carlton.freerpg.gameTools.LanguageSelector;
import mc.carlton.freerpg.globalVariables.ItemGroups;
import mc.carlton.freerpg.playerAndServerInfo.*;
import org.bukkit.*;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class AxeMastery {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    private Player p;
    private ItemStack itemInHand;
    private Map<Enchantment,Integer> enchantmentLevelMap = new HashMap<>();
    private String skillName = "axeMastery";
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


    public AxeMastery(Player p) {
        this.p = p;
        this.itemInHand = p.getInventory().getItemInMainHand();
        this.increaseStats = new ChangeStats(p);
        this.abilities = new AbilityTracker(p);
        this.timers = new AbilityTimers(p);
        this.pStatClass=  new PlayerStats(p);
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
        if (!p.hasPermission("freeRPG.axeMasteryAbility") || !pStatClass.isPlayerSkillAbilityOn(skillName)) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        if ((int) pStat.get("global").get(24) < 1 || !pStatClass.isPlayerSkillAbilityOn(skillName)) {
            return;
        }
        Integer[] pTimers = timers.getPlayerTimers();
        Integer[] pAbilities = abilities.getPlayerAbilities();
        if (pAbilities[9] == -1) {
            int cooldown = pTimers[9];
            if (cooldown < 1) {
                int prepMessages = (int) pStatClass.getPlayerData().get("global").get(22); //Toggle for preparation messages
                if (pAbilities[1] != -1 && pTimers[1] >= 1 && prepMessages > 0) {
                    actionMessage.sendMessage(ChatColor.GRAY + ">>>" + lang.getString("prepare") + " " + lang.getString("axe") + "...<<<");
                }
                int taskID = new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            Integer[] pTimers2 = timers.getPlayerTimers();
                            Integer[] pAbilities2 = abilities.getPlayerAbilities();
                            int prepMessages = (int) pStatClass.getPlayerData().get("global").get(22); //Toggle for preparation messages
                            if (pAbilities2[1] != -1 && pTimers2[1] >= 1 && prepMessages > 0) {
                                actionMessage.sendMessage(ChatColor.GRAY + ">>>..." + lang.getString("rest") + " " +lang.getString("axe") + "<<<");
                            }
                            abilities.setPlayerAbility( skillName, -1);
                        }
                        catch (Exception e) {

                        }
                    }
                }.runTaskLater(plugin, 20 * 4).getTaskId();
                abilities.setPlayerAbility( skillName, taskID);
            } else {
                actionMessage.sendMessage(ChatColor.RED +lang.getString("greatAxe") + " " + lang.getString("cooldown") + ": " + cooldown+ "s");
            }
        }
    }
    public void enableAbility() {
        if (!runMethods) {
            return;
        }
        Integer[] pAbilities = abilities.getPlayerAbilities();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        actionMessage.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + ">>>" + lang.getString("greatAxe") + " " + lang.getString("activated") + "<<<");
        int durationLevel = (int) pStat.get(skillName).get(4);
        double duration0 = Math.ceil(durationLevel*0.4) + 40;
        int cooldown = 300;
        if ((int) pStat.get("global").get(11) > 0) {
            cooldown = 200;
        }
        int finalCooldown = cooldown;
        long duration = (long) duration0;
        Bukkit.getScheduler().cancelTask(pAbilities[9]);
        abilities.setPlayerAbility( skillName, -2);
        new BukkitRunnable() {
            @Override
            public void run() {
                actionMessage.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + ">>>" + lang.getString("greatAxe") + " " + lang.getString("ended") + "<<<");
                abilities.setPlayerAbility( skillName, -1);
                timers.setPlayerTimer( skillName, finalCooldown);
                for(int i = 1; i < finalCooldown+1; i++) {
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
                                    actionMessage.sendMessage(ChatColor.GREEN + ">>>" + lang.getString("greatAxe") + " " + lang.getString("readyToUse") + "<<<");
                                }
                            }
                        }
                    }.runTaskLater(plugin, 20*i);
                }
            }
        }.runTaskLater(plugin, duration);
    }

    public void greaterAxe(Entity entity, World world,double finalDamage) {
        if (!runMethods) {
            return;
        }
        Integer[] pAbilities = abilities.getPlayerAbilities();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        if (pAbilities[9] == -2) {
            int greaterAxeLevel = (int) pStat.get(skillName).get(7);
            double radius = 2 + 0.5*greaterAxeLevel;
            if ((int) pStat.get(skillName).get(11) > 0) {
                finalDamage = finalDamage*2;
            }

            Location location = entity.getLocation();
            Block blockAboveGround = location.getBlock().getRelative(BlockFace.DOWN).getRelative(0,1,0);
            Collection<Entity> entities = world.getNearbyEntities(location,radius,2,radius);
            for (Entity mob : entities) {
                if (mob instanceof LivingEntity) {
                    if (mob instanceof Player) {
                        if (((Player) mob).getDisplayName().equalsIgnoreCase(p.getDisplayName())) {
                            continue;
                        }
                    }
                    ((LivingEntity) mob).damage(finalDamage*0.25);
                    increaseStats.changeEXP(skillName,(int) Math.round(finalDamage*0.25*expMap.get("greaterAxeAEO_EXPperDamagePointDone")));
                }
            }
            for (int x = -1*(int)Math.ceil(radius/2.0); x <= (int)Math.ceil(radius/2.0); x++ ) {
                for (int z = -1*(int)Math.ceil(radius/2.0); z <= (int)Math.ceil(radius/2.0); z++ ) {
                    Block b = blockAboveGround.getRelative(x,0,z);
                    Block below = b.getRelative(0,-1,0);
                    Material blockType = below.getType();
                    if (blockType != Material.AIR) {
                        if (blockType == Material.WATER) {
                            world.spawnParticle(Particle.WATER_SPLASH,b.getLocation(),50);
                        }
                        else {
                            if (blockType.isBlock()) {
                                world.spawnParticle(Particle.BLOCK_CRACK, b.getLocation(), 50, below.getBlockData());
                            }
                        }
                    }
                }
            }


        }

    }

    public boolean[] buffCheckerStrength( int buffLevel, int duration) {
        boolean addEffect = true;
        boolean hasEffect = false;
        potionEffectLoop:
        for (PotionEffect effect : p.getActivePotionEffects()) {
            if (effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
                hasEffect = true;
                if ( (effect.getDuration() > duration && effect.getAmplifier() >= buffLevel) || (effect.getAmplifier() > buffLevel && effect.getDuration() > 40) ) {
                    addEffect = false;
                }
                break potionEffectLoop;
            }
        }

        boolean[] returnThis = {addEffect,hasEffect};
        return returnThis;
    }

    public double divineCritical() {
        if (!runMethods) {
            return 1.0;
        }
        double multiplier = 1;
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int divineCriticalsLevel = (int) pStat.get(skillName).get(5);
        int betterCritsLevel = (int) pStat.get(skillName).get(12);
        if (divineCriticalsLevel*0.0001 < rand.nextDouble()) {
            return multiplier;
        }
        multiplier = 1.25 + 0.35*betterCritsLevel;
        return multiplier;
    }

    public void revitalized() {
        if (!runMethods) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int revitalizedLevel = (int) pStat.get(skillName).get(9);
        if (revitalizedLevel*0.01 > rand.nextDouble()) {
            double maxHP = ((Attributable) p).getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
            p.setHealth(maxHP);
            increaseStats.changeEXP(skillName,expMap.get("revitalizedActivateEXP"));
        }
    }

    public void warriorBlood() {
        if (!runMethods) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int warriorBloodLevel = (int) pStat.get(skillName).get(10);
        if (warriorBloodLevel < 1) {
            return;
        }
        int duration = 20*3*warriorBloodLevel;
        boolean[] strengthChecks = buffCheckerStrength(0,duration);
        if (strengthChecks[0]) {
            if (strengthChecks[1]) {
                p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            }
            p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,(int)duration,0));
        }
    }

    public void holyAxe(Entity entity,World world,double finalDamage) {
        if (!runMethods) {
            return;
        }
        if (entity instanceof LivingEntity) {
            if (((LivingEntity) entity).getHealth() < finalDamage) {
                return;
            }
            Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
            int holyAxeLevel = (int) pStat.get(skillName).get(8);
            if (holyAxeLevel*0.02 > rand.nextDouble()) {
                world.strikeLightning(entity.getLocation());
                increaseStats.changeEXP(skillName,expMap.get("holyAxeActivateEXP"));
            }
        }
    }

    public void giveHitEXP(double finalDamage) {
        if (!runMethods) {
            return;
        }
        increaseStats.changeEXP(skillName,20);
        increaseStats.changeEXP(skillName, (int) Math.round(finalDamage * expMap.get("axeDamage_EXPperDamagePointDone")));
    }

    public void giveKillEXP(Entity entity) {
        if (!runMethods) {
            return;
        }
        ItemGroups itemGroups = new ItemGroups();
        List<Material> axes = itemGroups.getAxes();
        if (!(axes.contains(itemInHand.getType()))) {
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
