package mc.carlton.freerpg.perksAndAbilities;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.playerAndServerInfo.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Defense {
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

    public Defense(Player p) {
        this.p = p;
        this.pName = p.getDisplayName();
        this.itemInHand = p.getInventory().getItemInMainHand();
        this.increaseStats = new ChangeStats(p);
        this.abilities = new AbilityTracker(p);
        this.timers = new AbilityTimers(p);
        this.pStatClass = new PlayerStats(p);
    }

    public void initiateAbility() {
        if (!p.hasPermission("freeRPG.defenseAbility")) {
            return;
        }
        Integer[] pTimers = timers.getPlayerTimers();
        Integer[] pAbilities = abilities.getPlayerAbilities();
        if (pAbilities[8] == -1) {
            int cooldown = pTimers[8];
            if (cooldown < 1) {
                int prepMessages = (int) pStatClass.getPlayerData().get("global").get(22); //Toggle for preparation messages
                if (prepMessages > 0) {
                    p.sendMessage(ChatColor.GRAY + ">>>You prepare yourself...<<<");
                }
                int taskID = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (prepMessages > 0) {
                            p.sendMessage(ChatColor.GRAY + ">>>...You rest yourself<<<");
                        }
                        try {
                            abilities.setPlayerAbility( "defense", -1);
                        }
                        catch (Exception e) {

                        }
                    }
                }.runTaskLater(plugin, 20 * 4).getTaskId();
                abilities.setPlayerAbility( "defense", taskID);
            } else {
                p.sendMessage(ChatColor.RED + "You must wait " + cooldown + " seconds to use Stone Soldier again.");
            }
        }
    }

    public void enableAbility() {
        Integer[] pAbilities = abilities.getPlayerAbilities();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        p.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + ">>>Stone Soldier Activated!<<<");
        int durationLevel = (int) pStat.get("defense").get(4);
        double duration0 = Math.ceil(durationLevel * 0.4) + 40;
        int cooldown = 300;
        if ((int) pStat.get("global").get(11) > 0) {
            cooldown = 200;
        }
        int finalCooldown = cooldown;
        long duration = (long) duration0;
        int strongerLegsLevel = (int) pStat.get("defense").get(12);
        int giftFromAboveLevel = (int) pStat.get("defense").get(11);
        boolean[] absorptionChecks = {false,false};
        boolean[] slownessChecks = {true,true};
        if (strongerLegsLevel > 0) {
            slownessChecks = buffCheckerSlowness(0,(int) duration);
        }
        else {
            slownessChecks = buffCheckerSlowness(3,(int) duration);
        }
        if (giftFromAboveLevel > 0) {
            absorptionChecks = buffCheckerAbsorption(1, (int) duration + 1200);
        }
        boolean[] resistanceChecks = buffCheckerResistance(2,(int) duration);

        if (absorptionChecks[0]) {
            if (absorptionChecks[1]) {
                p.removePotionEffect(PotionEffectType.ABSORPTION);
            }
            p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,(int)duration +1200,1));
        }

        if (slownessChecks[0]) {
            if (slownessChecks[1]) {
                p.removePotionEffect(PotionEffectType.SLOW);
            }
            if (strongerLegsLevel > 0) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) duration, 0));
            }
            else {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) duration, 3));
            }
        }

        if (resistanceChecks[0]) {
            if (resistanceChecks[1]) {
                p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            }
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,(int)duration,2));
        }

        timers.setPlayerTimer( "defense", finalCooldown);
        Bukkit.getScheduler().cancelTask(pAbilities[8]);
        abilities.setPlayerAbility( "defense", -2);
        int taskID = new BukkitRunnable() {
            @Override
            public void run() {
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + ">>>Stone Soldier has ended<<<");
                abilities.setPlayerAbility( "defense", -1);
                ((Attributable) p).getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.0);
                for (int i = 1; i < finalCooldown+1; i++) {
                    int timeRemaining = finalCooldown - i;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            timers.setPlayerTimer( "defense", timeRemaining);
                            AbilityTimers timers2 = new AbilityTimers(p);
                            if (timeRemaining ==0) {
                                if (!p.isOnline()) {
                                    timers2.removePlayer();
                                }
                                else {
                                    p.sendMessage(ChatColor.GREEN + ">>>Stone Soldier is ready to use again<<<");
                                }
                            }
                        }
                    }.runTaskLater(plugin, 20 * i);
                }
            }
        }.runTaskLater(plugin, duration).getTaskId();
        AbilityLogoutTracker incaseLogout = new AbilityLogoutTracker(p);
        incaseLogout.setPlayerTask(p,"defense",taskID);
    }

    public void preventLogoutTheft(int taskID_defense) {
        Integer[] pAbilities = abilities.getPlayerAbilities();
        if (pAbilities[8] == -2) {
            Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
            int durationLevel = (int) pStat.get("defense").get(4);
            double duration0 = Math.ceil(durationLevel * 0.4) + 40;
            long duration = (long) duration0;
            int strongerLegsLevel = (int) pStat.get("defense").get(12);
            int giftFromAboveLevel = (int) pStat.get("defense").get(11);
            int slowBuff = 3;
            boolean hasAbsorption = false;
            if (strongerLegsLevel > 0) {
                slowBuff = 0;
            }
            if (giftFromAboveLevel > 0) {
                hasAbsorption = true;
            }
            for (PotionEffect effect : p.getActivePotionEffects()) {
                if (effect.getType().equals(PotionEffectType.DAMAGE_RESISTANCE) && effect.getDuration() <= duration && effect.getAmplifier() == 2) {
                    p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                }
                else if (effect.getType().equals(PotionEffectType.SLOW) && effect.getDuration() <= duration && effect.getAmplifier() == slowBuff) {
                    p.removePotionEffect(PotionEffectType.SLOW);
                }
                else if (effect.getType().equals(PotionEffectType.ABSORPTION)) {
                    if (hasAbsorption) {
                        if (effect.getDuration() <= duration + 1200 && effect.getAmplifier() == 1) {
                            p.removePotionEffect(PotionEffectType.ABSORPTION);
                            p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,1200,1));
                        }
                    }
                }

            }
            Bukkit.getScheduler().cancelTask(taskID_defense);
            abilities.setPlayerAbility( "defense", -1);
            for(int i = 1; i < 301; i++) {
                int timeRemaining = 300 - i;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        AbilityTimers timers2 = new AbilityTimers(p);
                        timers2.setPlayerTimer( "defense", timeRemaining);
                        if (timeRemaining==0 && !p.isOnline()){
                            timers2.removePlayer();
                        }
                    }
                }.runTaskLater(plugin, 20*i);
            }
        }
    }

    public boolean[] buffCheckerResistance(int buffLevel, int duration) {
        boolean addEffect = true;
        boolean hasEffect = false;
        potionEffectLoop:
        for (PotionEffect effect : p.getActivePotionEffects()) {
            if (effect.getType().equals(PotionEffectType.DAMAGE_RESISTANCE)) {
                hasEffect = true;
                if ( (effect.getDuration() > duration && effect.getAmplifier() >= buffLevel) || (effect.getAmplifier() > buffLevel && effect.getDuration() > 40)) {
                    addEffect = false;
                }
                break potionEffectLoop;
            }
        }

        boolean[] returnThis = {addEffect,hasEffect};
        return returnThis;
    }

    public boolean[] buffCheckerSlowness( int buffLevel, int duration) {
        boolean addEffect = true;
        boolean hasEffect = false;
        potionEffectLoop:
        for (PotionEffect effect : p.getActivePotionEffects()) {
            if (effect.getType().equals(PotionEffectType.SLOW)) {
                hasEffect = true;
                if ( (effect.getDuration() > duration && effect.getAmplifier() >= buffLevel) ) {
                    addEffect = false;
                }
                break potionEffectLoop;
            }
        }

        boolean[] returnThis = {addEffect,hasEffect};
        return returnThis;
    }

    public boolean[] buffCheckerAbsorption( int buffLevel, int duration) {
        boolean addEffect = true;
        boolean hasEffect = false;
        potionEffectLoop:
        for (PotionEffect effect : p.getActivePotionEffects()) {
            if (effect.getType().equals(PotionEffectType.ABSORPTION)) {
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

    public boolean[] buffCheckerRegeneration( int buffLevel, int duration) {
        boolean addEffect = true;
        boolean hasEffect = false;
        potionEffectLoop:
        for (PotionEffect effect : p.getActivePotionEffects()) {
            if (effect.getType().equals(PotionEffectType.REGENERATION)) {
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

    public double hardBody() {
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int hardBodyLevel = (int) pStat.get("defense").get(5);
        int hardHeadedLevel = (int) pStat.get("defense").get(9);
        double chance = 0.01 + 0.0001*hardBodyLevel;
        if (chance > rand.nextDouble()) {
            double multiplier = Math.max((1- .33 - .06666*hardHeadedLevel),0);
            increaseStats.changeEXP("defense", (int) 100);
            return multiplier;
        }
        return 1;
    }

    public void reactions(double finalDamage) {
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int reactionsLevel_I = (int) pStat.get("defense").get(8);
        int reactionsLevel_II = (int) pStat.get("defense").get(10);
        if (finalDamage < 1.0) {
            return;
        }
        if (reactionsLevel_II*0.02 > rand.nextDouble()) {
            boolean[] resistanceChecks = buffCheckerResistance(1,100);
            if (resistanceChecks[0]) {
                if (resistanceChecks[1]) {
                    p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                }
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100,1));
            }
            increaseStats.changeEXP("defense", (int) 100);

        }
        else if (reactionsLevel_I*0.02 > rand.nextDouble()) {
            boolean[] resistanceChecks = buffCheckerResistance(0,100);
            if (resistanceChecks[0]) {
                if (resistanceChecks[1]) {
                    p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                }
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100,0));
            }
            increaseStats.changeEXP("defense", (int) 50);
        }
    }

    public void hearty() {
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int heartyLevel = (int) pStat.get("defense").get(13);
        if (heartyLevel > 0) {
            double HP = Double.valueOf(plugin.getConfig().getString("multipliers.globalMultiplier"));
            ((Attributable) p).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(HP + 4.0);
        }

    }

    public void doubleDrops(Entity entity, List<ItemStack> drops, World world) {
        if (hostileMobs.contains(entity.getType())) {
            Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
            int doubleDropsLevel = (int) pStat.get("defense").get(6);
            if (doubleDropsLevel*0.0005 > rand.nextDouble()) {
                for (ItemStack drop : drops) {
                    world.dropItemNaturally(entity.getLocation().add(0, 0.5, 0), drop);
                }
            }
        }

    }

    public void healer() {
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int healerLevel = (int) pStat.get("defense").get(7);
        if (healerLevel < 1) {
            return;
        }
        int duration = 20*Math.min(3*healerLevel,9);
        boolean[] regenerationChecks = buffCheckerRegeneration(0,duration);
        if (regenerationChecks[0]) {
            if (regenerationChecks[1]) {
                p.removePotionEffect(PotionEffectType.REGENERATION);
            }
            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,(int)duration,0));
        }
        increaseStats.changeEXP("defense", (int) 30);
        if (healerLevel < 4) {
            return;
        }
        double maxHP = ((Attributable) p).getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        int HP_to_add = (healerLevel-3);
        p.setHealth(Math.min(maxHP,p.getHealth()+HP_to_add));
        increaseStats.changeEXP("defense", (int) 20);
    }

    public void giveHitEXP(double damage) {
        increaseStats.changeEXP("defense",20);
        increaseStats.changeEXP("defense", (int) Math.round(damage * 12) * 10);
    }

    public void giveKillEXP(Entity entity) {
        if (entity instanceof LivingEntity) {
            EntityType type = entity.getType();
            if (entity instanceof Monster) {
                switch (type) {
                    case STRIDER:
                    case CREEPER:
                        increaseStats.changeEXP("defense",400);
                        break;
                    case HOGLIN:
                    case ZOGLIN:
                        increaseStats.changeEXP("defense",600);
                        break;
                    case WITHER:
                        increaseStats.changeEXP("defense",30000);
                        break;
                    case ELDER_GUARDIAN:
                        increaseStats.changeEXP("defense",10000);
                        break;
                    default:
                        increaseStats.changeEXP("defense", 300);
                        break;
                }
            }
            else {
                switch (type) {
                    case ENDER_DRAGON:
                        increaseStats.changeEXP("defense",50000);
                        break;
                    case IRON_GOLEM:
                        increaseStats.changeEXP("defense", 750);
                        break;
                    case BEE:
                    case DOLPHIN:
                    case LLAMA:
                    case POLAR_BEAR:
                    case TRADER_LLAMA:
                    case WOLF:
                        increaseStats.changeEXP("defense",250);
                        break;
                    default:
                        increaseStats.changeEXP("defense",80);
                        break;
                }
            }
        }
    }

    public void armorEXP(ItemStack armor) {
        Map<Material,Integer> armorEXP = new HashMap<>();
        armorEXP.put(Material.LEATHER_BOOTS,200*3);
        armorEXP.put(Material.LEATHER_LEGGINGS,350*3);
        armorEXP.put(Material.LEATHER_CHESTPLATE,400*3);
        armorEXP.put(Material.LEATHER_HELMET,250*3);

        armorEXP.put(Material.IRON_BOOTS,200*5);
        armorEXP.put(Material.IRON_LEGGINGS,350*5);
        armorEXP.put(Material.IRON_CHESTPLATE,400*5);
        armorEXP.put(Material.IRON_HELMET,250*5);

        armorEXP.put(Material.GOLDEN_BOOTS,200*7);
        armorEXP.put(Material.GOLDEN_LEGGINGS,350*7);
        armorEXP.put(Material.GOLDEN_CHESTPLATE,400*7);
        armorEXP.put(Material.GOLDEN_HELMET,250*7);

        armorEXP.put(Material.DIAMOND_BOOTS,200*10);
        armorEXP.put(Material.DIAMOND_LEGGINGS,350*10);
        armorEXP.put(Material.DIAMOND_CHESTPLATE,400*10);
        armorEXP.put(Material.DIAMOND_HELMET,250*10);

        armorEXP.put(Material.NETHERITE_BOOTS,200*15);
        armorEXP.put(Material.NETHERITE_LEGGINGS,350*15);
        armorEXP.put(Material.NETHERITE_CHESTPLATE,400*15);
        armorEXP.put(Material.NETHERITE_HELMET,250*15);

        if (armorEXP.keySet().contains(armor.getType())) {
            increaseStats.changeEXP("defense",armorEXP.get(armor.getType()));
        }
    }
}
