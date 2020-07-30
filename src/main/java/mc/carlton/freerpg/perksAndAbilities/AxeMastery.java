package mc.carlton.freerpg.perksAndAbilities;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.ActionBarMessages;
import mc.carlton.freerpg.gameTools.LanguageSelector;
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
    private String pName;
    private ItemStack itemInHand;
    private Map<Enchantment,Integer> enchantmentLevelMap = new HashMap<>();

    ChangeStats increaseStats; //Changing Stats

    AbilityTracker abilities; //Abilities class
    // GET ABILITY STATUSES LIKE THIS:   Integer[] pAbilities = abilities.getPlayerAbilities(p);

    AbilityTimers timers; //Ability Timers class
    //GET TIMERS LIKE THIS:              Integer[] pTimers = timers.getPlayerTimers(p);

    PlayerStats pStatClass;
    //GET PLAYER STATS LIKE THIS:        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData(p);

    PlacedBlocks placedClass;
    //GET TRACKED BLOCKS LIKE THIS:        ArrayList<Location> blocksLocations = placedClass.getBlocks();

    ActionBarMessages actionMessage;
    LanguageSelector lang;


    Random rand = new Random(); //Random class Import

    Material[] axes0 = {Material.DIAMOND_AXE,Material.GOLDEN_AXE,Material.IRON_AXE, Material.STONE_AXE,Material.WOODEN_AXE};
    List<Material> axes = Arrays.asList(axes0);


    public AxeMastery(Player p) {
        this.p = p;
        this.pName = p.getDisplayName();
        this.itemInHand = p.getInventory().getItemInMainHand();
        this.increaseStats = new ChangeStats(p);
        this.abilities = new AbilityTracker(p);
        this.timers = new AbilityTimers(p);
        this.pStatClass=  new PlayerStats(p);
        this.placedClass = new PlacedBlocks();
        this.actionMessage = new ActionBarMessages(p);
        this.lang = new LanguageSelector(p);
    }

    public void initiateAbility() {
        if (!p.hasPermission("freeRPG.axeMasteryAbility")) {
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
                            abilities.setPlayerAbility( "axeMastery", -1);
                        }
                        catch (Exception e) {

                        }
                    }
                }.runTaskLater(plugin, 20 * 4).getTaskId();
                abilities.setPlayerAbility( "axeMastery", taskID);
            } else {
                actionMessage.sendMessage(ChatColor.RED +lang.getString("greatAxe") + " " + lang.getString("cooldown") + ": " + cooldown+ "s");
            }
        }
    }
    public void enableAbility() {
        Integer[] pAbilities = abilities.getPlayerAbilities();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        actionMessage.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + ">>>" + lang.getString("greatAxe") + " " + lang.getString("activated") + "<<<");
        int durationLevel = (int) pStat.get("axeMastery").get(4);
        double duration0 = Math.ceil(durationLevel*0.4) + 40;
        int cooldown = 300;
        if ((int) pStat.get("global").get(11) > 0) {
            cooldown = 200;
        }
        int finalCooldown = cooldown;
        long duration = (long) duration0;
        Bukkit.getScheduler().cancelTask(pAbilities[9]);
        abilities.setPlayerAbility( "axeMastery", -2);
        new BukkitRunnable() {
            @Override
            public void run() {
                actionMessage.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + ">>>" + lang.getString("greatAxe") + " " + lang.getString("ended") + "<<<");
                abilities.setPlayerAbility( "axeMastery", -1);
                timers.setPlayerTimer( "axeMastery", finalCooldown);
                for(int i = 1; i < finalCooldown+1; i++) {
                    int timeRemaining = finalCooldown - i;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            timers.setPlayerTimer( "axeMastery", timeRemaining);
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
        Integer[] pAbilities = abilities.getPlayerAbilities();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        if (pAbilities[9] == -2) {
            int greaterAxeLevel = (int) pStat.get("axeMastery").get(7);
            double radius = 2 + 0.5*greaterAxeLevel;
            if ((int) pStat.get("axeMastery").get(11) > 0) {
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
                    increaseStats.changeEXP("axeMastery",(int) Math.round(finalDamage*0.25*3)*10);
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
        double multiplier = 1;
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int divineCriticalsLevel = (int) pStat.get("axeMastery").get(5);
        int betterCritsLevel = (int) pStat.get("axeMastery").get(12);
        if (divineCriticalsLevel*0.0001 < rand.nextDouble()) {
            return multiplier;
        }
        multiplier = 1.25 + 0.35*betterCritsLevel;
        return multiplier;
    }

    public void revitalized() {
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int revitalizedLevel = (int) pStat.get("axeMastery").get(9);
        if (revitalizedLevel*0.01 > rand.nextDouble()) {
            double maxHP = ((Attributable) p).getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
            p.setHealth(maxHP);
            increaseStats.changeEXP("axeMastery",200);
        }
    }

    public void warriorBlood() {
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int warriorBloodLevel = (int) pStat.get("axeMastery").get(10);
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
        if (entity instanceof LivingEntity) {
            if (((LivingEntity) entity).getHealth() < finalDamage) {
                return;
            }
            Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
            int holyAxeLevel = (int) pStat.get("axeMastery").get(8);
            if (holyAxeLevel*0.02 > rand.nextDouble()) {
                world.strikeLightning(entity.getLocation());
                increaseStats.changeEXP("axeMastery",100);
            }
        }
    }

    public void giveHitEXP(double finalDamage) {
        increaseStats.changeEXP("axeMastery",20);
        increaseStats.changeEXP("axeMastery", (int) Math.round(finalDamage * 5) * 10);
    }

    public void giveKillEXP(Entity entity) {
        if (!(axes.contains(itemInHand.getType()))) {
            return;
        }
        if (entity instanceof LivingEntity) {
            EntityType type = entity.getType();
            if (entity instanceof Monster) {
                switch (type) {
                    case BLAZE:
                    case SKELETON:
                    case ZOMBIE:
                    case CAVE_SPIDER:
                    case SPIDER:
                        increaseStats.changeEXP("axeMastery", 250);
                        break;
                    case STRIDER:
                    case CREEPER:
                        increaseStats.changeEXP("axeMastery",750);
                        break;
                    case HOGLIN:
                    case ZOGLIN:
                        increaseStats.changeEXP("axeMastery",1400);
                        break;
                    case WITHER:
                        increaseStats.changeEXP("axeMastery",30000);
                        break;
                    case ELDER_GUARDIAN:
                        increaseStats.changeEXP("axeMastery",10000);
                        break;
                    default:
                        increaseStats.changeEXP("axeMastery", 400);
                        break;
                }
            }
            else {
                switch (type) {
                    case ENDER_DRAGON:
                        increaseStats.changeEXP("axeMastery",50000);
                        break;
                    case IRON_GOLEM:
                        increaseStats.changeEXP("axeMastery", 500);
                        break;
                    case BEE:
                    case DOLPHIN:
                    case LLAMA:
                    case POLAR_BEAR:
                    case TRADER_LLAMA:
                    case WOLF:
                        increaseStats.changeEXP("axeMastery",250);
                        break;
                    default:
                        increaseStats.changeEXP("axeMastery",100);
                        break;
                }
            }
        }
    }

}
