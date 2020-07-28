package mc.carlton.freerpg.perksAndAbilities;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.playerAndServerInfo.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class Swordsmanship {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    private Player p;
    private String pName;
    private ItemStack itemInHand;

    ChangeStats increaseStats; //Changing Stats

    AbilityTracker abilities; //Abilities class
    // GET ABILITY STATUSES LIKE THIS:   Integer[] pAbilities = abilities.getPlayerAbilities(p);

    AbilityTimers timers; //Ability Timers class
    //GET TIMERS LIKE THIS:              Integer[] pTimers = timers.getPlayerTimers(p);

    PlayerStats pStatClass;
    //GET PLAYER STATS LIKE THIS:        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData(p);

    Random rand = new Random(); //Random class Import
    EntityType[] hostileMobs0 = {EntityType.SPIDER,EntityType.CAVE_SPIDER,EntityType.ENDERMAN,EntityType.ZOMBIFIED_PIGLIN,
            EntityType.BLAZE,EntityType.CREEPER,EntityType.DROWNED,EntityType.ELDER_GUARDIAN,
            EntityType.ENDERMITE,EntityType.EVOKER,EntityType.GHAST,EntityType.GUARDIAN,
            EntityType.HUSK,EntityType.MAGMA_CUBE,EntityType.PHANTOM,EntityType.PILLAGER,
            EntityType.RAVAGER,EntityType.SHULKER,EntityType.SKELETON,EntityType.SLIME,
            EntityType.STRAY,EntityType.VEX,EntityType.VINDICATOR,EntityType.WITCH,
            EntityType.WITHER_SKELETON,EntityType.ZOMBIE,EntityType.ZOMBIE_VILLAGER,
            EntityType.HOGLIN,EntityType.PIGLIN,EntityType.ZOMBIFIED_PIGLIN,EntityType.ZOGLIN};
    List<EntityType> hostileMobs = Arrays.asList(hostileMobs0);
    EntityType[] thirstMobs0 = {EntityType.ZOMBIFIED_PIGLIN, EntityType.DROWNED,EntityType.ELDER_GUARDIAN, EntityType.EVOKER,EntityType.GUARDIAN,
                                EntityType.HUSK,EntityType.PILLAGER, EntityType.RAVAGER, EntityType.VINDICATOR,EntityType.WITCH, EntityType.ZOMBIE,EntityType.ZOMBIE_VILLAGER,
                                EntityType.PIGLIN,EntityType.HOGLIN,EntityType.ZOGLIN};
    List<EntityType> thirstMobs = Arrays.asList(thirstMobs0);

    Material[] swords0 = {Material.WOODEN_SWORD,Material.STONE_SWORD,Material.GOLDEN_SWORD,Material.DIAMOND_SWORD,Material.IRON_SWORD};
    List<Material> swords = Arrays.asList(swords0);

    public Swordsmanship(Player p) {
        this.p = p;
        this.pName = p.getDisplayName();
        this.itemInHand = p.getInventory().getItemInMainHand();
        this.increaseStats = new ChangeStats(p);
        this.abilities = new AbilityTracker(p);
        this.timers = new AbilityTimers(p);
        this.pStatClass = new PlayerStats(p);
    }

    public void initiateAbility() {
        if (!p.hasPermission("freeRPG.swordsmanshipAbility")) {
            return;
        }
        Integer[] pTimers = timers.getPlayerTimers();
        Integer[] pAbilities = abilities.getPlayerAbilities();
        if (pAbilities[7] == -1) {
            int cooldown = pTimers[7];
            if (cooldown < 1) {
                int prepMessages = (int) pStatClass.getPlayerData().get("global").get(22); //Toggle for preparation messages
                if (prepMessages > 0) {
                    p.sendMessage(ChatColor.GRAY + ">>>You prepare your sword...<<<");
                }
                int taskID = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (prepMessages > 0) {
                            p.sendMessage(ChatColor.GRAY + ">>>...You rest your sword<<<");
                        }
                        try {
                            abilities.setPlayerAbility( "swordsmanship", -1);
                        }
                        catch (Exception e) {

                        }
                    }
                }.runTaskLater(plugin, 20 * 4).getTaskId();
                abilities.setPlayerAbility( "swordsmanship", taskID);
            } else {
                p.sendMessage(ChatColor.RED + "You must wait " + cooldown + " seconds to use Swift Strikes again.");
            }
        }
    }

    public void enableAbility() {
        Integer[] pAbilities = abilities.getPlayerAbilities();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        p.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + ">>>Swift Strikes Activated!<<<");
        int durationLevel = (int) pStat.get("swordsmanship").get(4);
        double duration0 = Math.ceil(durationLevel * 0.4) + 40;
        int cooldown = 300;
        if ((int) pStat.get("global").get(11) > 0) {
            cooldown = 200;
        }
        int finalCooldown = cooldown;
        long duration = (long) duration0;
        int sharperLevel = (int) pStat.get("swordsmanship").get(12);
        if (sharperLevel > 0) {
            int sharpLevel = itemInHand.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
            itemInHand.removeEnchantment(Enchantment.DAMAGE_ALL);
            itemInHand.addUnsafeEnchantment(Enchantment.DAMAGE_ALL,sharpLevel+1);
        }

        ((Attributable) p).getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(1024.0);
        timers.setPlayerTimer( "swordsmanship", finalCooldown);
        Bukkit.getScheduler().cancelTask(pAbilities[7]);
        abilities.setPlayerAbility( "swordsmanship", -2);
        int taskID = new BukkitRunnable() {
            @Override
            public void run() {
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + ">>>Swift Strikes has ended<<<");
                abilities.setPlayerAbility( "swordsmanship", -1);
                ((Attributable) p).getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.0);
                for (int i = 1; i < finalCooldown+1; i++) {
                    int timeRemaining = finalCooldown - i;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            timers.setPlayerTimer( "swordsmanship", timeRemaining);
                            AbilityTimers timers2 = new AbilityTimers(p);
                            if (timeRemaining ==0) {
                                if (!p.isOnline()) {
                                    timers2.removePlayer();
                                }
                                else {
                                    p.sendMessage(ChatColor.GREEN + ">>>Swift Strikes is ready to use again<<<");
                                }
                            }
                        }
                    }.runTaskLater(plugin, 20 * i);
                }
            }
        }.runTaskLater(plugin, duration).getTaskId();
        AbilityLogoutTracker incaseLogout = new AbilityLogoutTracker(p);
        incaseLogout.setPlayerItem(p,"swordsmanship",itemInHand);
        incaseLogout.setPlayerTask(p,"swordsmanship",taskID);
    }

    public void preventLogoutTheft(int taskID_swordsmanship,ItemStack itemInHand_swords) {
        Integer[] pAbilities = abilities.getPlayerAbilities();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int sharperLevel = (int) pStat.get("swordsmanship").get(12);
        ((Attributable) p).getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.0);

        if (sharperLevel > 0) {
            if (pAbilities[7] == -2) {
                Bukkit.getScheduler().cancelTask(taskID_swordsmanship);
                int sharpLevel = itemInHand_swords.getEnchantmentLevel(Enchantment.DAMAGE_ALL)-1;
                itemInHand_swords.removeEnchantment(Enchantment.DAMAGE_ALL);
                if (sharpLevel > 0) {
                    itemInHand_swords.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, sharpLevel);
                }
                int cooldown = 300;
                if ((int) pStat.get("global").get(11) > 0) {
                    cooldown = 200;
                }
                int finalCooldown = cooldown;
                p.sendMessage(ChatColor.RED+ChatColor.BOLD.toString() + ">>>A magic force ends your ability<<<");
                abilities.setPlayerAbility( "swordsmanship", -1);
                for(int i = 1; i < finalCooldown+1; i++) {
                    int timeRemaining = finalCooldown - i;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            AbilityTimers timers2 = new AbilityTimers(p);
                            timers2.setPlayerTimer( "swordsmanship", timeRemaining);
                            if (timeRemaining==0 && !p.isOnline()){
                                timers2.removePlayer();
                            }
                        }
                    }.runTaskLater(plugin, 20*i);
                }

            }
        }
    }

    public void thirstForBlood(Entity entity) {
        if ( !(thirstMobs.contains(entity.getType())) ) {
            return;
        }
        if ( !(swords.contains(itemInHand.getType())) ) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        if ((int) pStat.get("swordsmanship").get(11) < 1) {
            return;
        }
        int foodLevel = p.getFoodLevel();
        float saturation = p.getSaturation();
        p.setFoodLevel(Math.min(foodLevel+2,20));
        p.setSaturation(Math.min(saturation+2,p.getFoodLevel()));
        increaseStats.changeEXP("swordsmanship", (int) 50);
    }

    public void doubleHit(Entity entity, double damage, Player p) {
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int doubleHitLevel = (int) pStat.get("swordsmanship").get(5);
        if (doubleHitLevel * 0.0002 > rand.nextDouble()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!entity.isDead()) {
                        LivingEntity aliveEntity = (LivingEntity) entity;
                        Vector knockback = aliveEntity.getVelocity();
                        aliveEntity.setNoDamageTicks(0);
                        if (aliveEntity instanceof Player) {
                            aliveEntity.damage(2);
                            increaseStats.changeEXP("swordsmanship", (int) 75);
                        }
                        else {
                            aliveEntity.damage(damage * 0.5);
                            increaseStats.changeEXP("swordsmanship", (int) Math.round(damage * 1.5) * 10);
                        }
                        aliveEntity.setVelocity(knockback.multiply(2));
                        aliveEntity.setNoDamageTicks(20);

                    }
                }
            }.runTaskLater(plugin, 4);
        }
    }

    public void killBuffs(Entity entity) { //This is written very inefficiently and is very messy
        if ( !(hostileMobs.contains(entity.getType())) ) {
            return;
        }
        if ( !(swords.contains(itemInHand.getType())) ) {
            return;
        }

        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int adrenaline1Level = Math.min((int) pStat.get("swordsmanship").get(7), 5);
        int adrenaline2Level = Math.min((int) pStat.get("swordsmanship").get(9), 5);
        int killingSpree1Level = Math.min((int) pStat.get("swordsmanship").get(8), 5);
        int kilingSpree2Level = Math.min((int) pStat.get("swordsmanship").get(10), 5);
        int totalSpeedTime = adrenaline1Level * 2 * 20;
        int totalStrengthtime = killingSpree1Level * 2 * 20;
        int speedIItime = (int) Math.round(totalSpeedTime * adrenaline2Level * 0.2);
        int strengthIItime = (int) Math.round(totalStrengthtime * kilingSpree2Level * 0.2);
        int speedItime = totalSpeedTime - speedIItime;
        int strengthItime = totalStrengthtime - strengthIItime;

        if (totalStrengthtime == 0 && totalSpeedTime == 0) {
            return;
        }


        //Speed
        boolean addEffect = true;
        boolean hasEffect = false;
        potionEffectLoop:
        for (PotionEffect effect : p.getActivePotionEffects()) {
            if (effect.getType().equals(PotionEffectType.SPEED)) {
                hasEffect = true;
                if ((effect.getDuration() > totalSpeedTime) || (effect.getAmplifier() > 1 && effect.getDuration() > totalSpeedTime * 0.2) || (effect.getAmplifier() == 1 && effect.getDuration() > speedIItime && effect.getDuration() > totalSpeedTime * 0.2) || totalSpeedTime == 0) {
                    addEffect = false;
                }
                break potionEffectLoop;
            }
        }

        if (addEffect) {
            if (hasEffect) {
                p.removePotionEffect(PotionEffectType.SPEED);
            }
            if (speedIItime == 0) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, totalSpeedTime, 0));
            } else {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, speedIItime+2, 1));
                int taskID = new BukkitRunnable() {
                    @Override
                    public void run() {
                        boolean addEffect = true;
                        boolean hasEffect = false;
                        potionEffectLoop:
                        for (PotionEffect effect : p.getActivePotionEffects()) {
                            if (effect.getType().equals(PotionEffectType.SPEED)) {
                                hasEffect = true;
                                if ((effect.getDuration() > totalSpeedTime) || (effect.getAmplifier() > 1 && effect.getDuration() > speedItime * 0.2)) {
                                    addEffect = false;
                                }
                                break potionEffectLoop;
                            }
                        }
                        if (addEffect && hasEffect) {
                            p.removePotionEffect(PotionEffectType.SPEED);
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, speedItime, 0));
                        }

                    }
                }.runTaskLater(plugin, speedIItime).getTaskId();
            }
        }

        //Strength
        addEffect = true;
        hasEffect = false;
        potionEffectLoop:
        for (PotionEffect effect : p.getActivePotionEffects()) {
            if (effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
                hasEffect = true;
                if ((effect.getDuration() > totalStrengthtime) || (effect.getAmplifier() > 1 && effect.getDuration() > totalStrengthtime * 0.2) || (effect.getAmplifier() == 1 && effect.getDuration() > strengthIItime && effect.getDuration() > totalStrengthtime * 0.2) || totalStrengthtime == 0) {
                    addEffect = false;
                }
                break potionEffectLoop;
            }
        }

        if (addEffect) {
            if (hasEffect) {
                p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            }
            if (strengthIItime == 0) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, totalStrengthtime, 0));
            } else {
                p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, strengthIItime+2, 1));
                int taskID = new BukkitRunnable() {
                    @Override
                    public void run() {
                        boolean addEffect = true;
                        boolean hasEffect = false;
                        potionEffectLoop:
                        for (PotionEffect effect : p.getActivePotionEffects()) {
                            if (effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
                                hasEffect = true;
                                if ((effect.getDuration() > totalStrengthtime) || (effect.getAmplifier() > 1 && effect.getDuration() > strengthItime * 0.2)) {
                                    addEffect = false;
                                }
                                break potionEffectLoop;
                            }
                        }
                        if (addEffect && hasEffect) {
                            p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                            p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, strengthItime, 0));
                        }

                    }
                }.runTaskLater(plugin, strengthIItime).getTaskId();
            }

        }

    }

    public void giveHitEXP(double finalDamage) {
        increaseStats.changeEXP("swordsmanship",20);
        increaseStats.changeEXP("swordsmanship", (int) Math.round(finalDamage * 5) * 10);
    }

    public void giveKillEXP(Entity entity) {
        if (!(swords.contains(itemInHand.getType()))) {
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
                        increaseStats.changeEXP("swordsmanship", 250);
                        break;
                    case CREEPER:
                        increaseStats.changeEXP("swordsmanship",750);
                        break;
                    case HOGLIN:
                    case ZOGLIN:
                        increaseStats.changeEXP("swordsmanship",1200);
                        break;
                    case WITHER:
                        increaseStats.changeEXP("swordsmanship",30000);
                        break;
                    case ELDER_GUARDIAN:
                        increaseStats.changeEXP("swordsmanship",10000);
                        break;
                    default:
                        increaseStats.changeEXP("swordsmanship", 400);
                        break;
                }
            }
            else {
                switch (type) {
                    case ENDER_DRAGON:
                        increaseStats.changeEXP("swordsmanship",50000);
                        break;
                    case IRON_GOLEM:
                        increaseStats.changeEXP("swordsmanship", 500);
                        break;
                    case BEE:
                    case DOLPHIN:
                    case LLAMA:
                    case POLAR_BEAR:
                    case TRADER_LLAMA:
                    case WOLF:
                        increaseStats.changeEXP("swordsmanship",250);
                        break;
                    default:
                        increaseStats.changeEXP("swordsmanship",100);
                        break;
                }
            }
        }
    }


}
