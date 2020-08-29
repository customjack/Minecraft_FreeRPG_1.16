package mc.carlton.freerpg.perksAndAbilities;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.ActionBarMessages;
import mc.carlton.freerpg.gameTools.LanguageSelector;
import mc.carlton.freerpg.gameTools.TrackItem;
import mc.carlton.freerpg.globalVariables.EntityGroups;
import mc.carlton.freerpg.globalVariables.ItemGroups;
import mc.carlton.freerpg.playerAndServerInfo.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.time.Instant;
import java.util.*;

public class Swordsmanship {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    private Player p;
    private String pName;
    private ItemStack itemInHand;
    private String skillName = "swordsmanship";
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

    public Swordsmanship(Player p) {
        this.p = p;
        this.pName = p.getDisplayName();
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
        if (!p.hasPermission("freeRPG.swordsmanshipAbility")) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        if ((int) pStat.get("global").get(24) < 1 || !pStatClass.isPlayerSkillAbilityOn(skillName)) {
            return;
        }
        Integer[] pTimers = timers.getPlayerTimers();
        Integer[] pAbilities = abilities.getPlayerAbilities();
        if (pAbilities[7] == -1) {
            int cooldown = pTimers[7];
            if (cooldown < 1) {
                int prepMessages = (int) pStatClass.getPlayerData().get("global").get(22); //Toggle for preparation messages
                if (prepMessages > 0) {
                    actionMessage.sendMessage(ChatColor.GRAY + ">>>" + lang.getString("prepare") + " " + lang.getString("sword") + "...<<<");
                }
                int taskID = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (prepMessages > 0) {
                            actionMessage.sendMessage(ChatColor.GRAY + ">>>..." + lang.getString("rest") + " " +lang.getString("sword") + "<<<");
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
                actionMessage.sendMessage(ChatColor.RED +lang.getString("swiftStrikes") + " " + lang.getString("cooldown") + ": " + cooldown+ "s");
            }
        }
    }

    public void enableAbility() {
        if (!runMethods) {
            return;
        }
        Integer[] pAbilities = abilities.getPlayerAbilities();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        actionMessage.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + ">>>" + lang.getString("swiftStrikes") + " " + lang.getString("activated") + "<<<");
        int durationLevel = (int) pStat.get(skillName).get(4);
        double duration0 = Math.ceil(durationLevel * 0.4) + 40;
        int cooldown = 300;
        if ((int) pStat.get("global").get(11) > 0) {
            cooldown = 200;
        }
        int finalCooldown = cooldown;
        long duration = (long) duration0;
        int sharperLevel = (int) pStat.get(skillName).get(12);
        int sharpLevel = itemInHand.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
        if (sharperLevel > 0) {
            itemInHand.removeEnchantment(Enchantment.DAMAGE_ALL);
            itemInHand.addUnsafeEnchantment(Enchantment.DAMAGE_ALL,sharpLevel+1);
        }

        //Mark the item
        long unixTime = Instant.now().getEpochSecond();
        String keyName = p.getUniqueId().toString() + "-" + String.valueOf(unixTime) + "-" + skillName;
        NamespacedKey key = new NamespacedKey(plugin,keyName);
        ItemMeta itemMeta = itemInHand.getItemMeta();
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING,"nothing");
        itemInHand.setItemMeta(itemMeta);

        ((Attributable) p).getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(1024.0);
        timers.setPlayerTimer( skillName, finalCooldown);
        Bukkit.getScheduler().cancelTask(pAbilities[7]);
        abilities.setPlayerAbility( skillName, -2);
        int taskID = new BukkitRunnable() {
            @Override
            public void run() {
                TrackItem trackItem = new TrackItem();
                ItemStack potentialAbilityItem = trackItem.findTrackedItemInInventory(p,key);
                if (potentialAbilityItem != null) {
                    itemInHand = potentialAbilityItem;
                }
                actionMessage.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + ">>>" + lang.getString("swiftStrikes") + " " + lang.getString("ended") + "<<<");
                abilities.setPlayerAbility( skillName, -1);
                ((Attributable) p).getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.0);
                if (sharperLevel > 0) {
                    if (sharpLevel > 0) {
                        itemInHand.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, sharpLevel);
                    }
                    else {
                        itemInHand.removeEnchantment(Enchantment.DAMAGE_ALL);
                    }
                }
                for (int i = 1; i < finalCooldown+1; i++) {
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
                                    actionMessage.sendMessage(ChatColor.GREEN + ">>>" + lang.getString("swiftStrikes") + " " + lang.getString("readyToUse") + "<<<");
                                }
                            }
                        }
                    }.runTaskLater(plugin, 20 * i);
                }
            }
        }.runTaskLater(plugin, duration).getTaskId();
        AbilityLogoutTracker incaseLogout = new AbilityLogoutTracker(p);
        incaseLogout.setPlayerItem(p,skillName,key);
        incaseLogout.setPlayerTask(p,skillName,taskID);
    }

    public void preventLogoutTheft(int taskID_swordsmanship,ItemStack itemInHand_swords) {
        if (!runMethods) {
            return;
        }
        Integer[] pAbilities = abilities.getPlayerAbilities();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int sharperLevel = (int) pStat.get(skillName).get(12);
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
                actionMessage.sendMessage(ChatColor.RED+ChatColor.BOLD.toString() + ">>>"+lang.getString("magicForce")+"<<<");
                abilities.setPlayerAbility( skillName, -1);
                for(int i = 1; i < finalCooldown+1; i++) {
                    int timeRemaining = finalCooldown - i;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            AbilityTimers timers2 = new AbilityTimers(p);
                            timers2.setPlayerTimer( skillName, timeRemaining);
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
        if (!runMethods) {
            return;
        }
        EntityGroups entityGroups = new EntityGroups();
        List<EntityType> thirstMobs = entityGroups.getThirstMobs();
        if ( !(thirstMobs.contains(entity.getType())) ) {
            return;
        }
        ItemGroups itemGroups = new ItemGroups();
        List<Material> swords = itemGroups.getSwords();
        if ( !(swords.contains(itemInHand.getType())) ) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        if ((int) pStat.get(skillName).get(11) < 1) {
            return;
        }
        int foodLevel = p.getFoodLevel();
        float saturation = p.getSaturation();
        p.setFoodLevel(Math.min(foodLevel+2,20));
        p.setSaturation(Math.min(saturation+2,p.getFoodLevel()));
        increaseStats.changeEXP(skillName, expMap.get("thirstForBloodActivate"));
    }

    public void doubleHit(Entity entity, double damage, Player p) {
        if (!runMethods) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int doubleHitLevel = (int) pStat.get(skillName).get(5);
        if (doubleHitLevel * 0.0002 > rand.nextDouble()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!entity.isDead()) {
                        LivingEntity aliveEntity = (LivingEntity) entity;
                        double hpRemaining = aliveEntity.getHealth();
                        Vector knockback = aliveEntity.getVelocity();
                        aliveEntity.setNoDamageTicks(0);
                        if (aliveEntity instanceof Player) {
                            aliveEntity.damage(2);
                            increaseStats.changeEXP(skillName, expMap.get("doubleHitActivate"));
                        }
                        else {
                            aliveEntity.damage(Math.min(damage * 0.5,hpRemaining-1));
                            increaseStats.changeEXP(skillName, (int) Math.round(damage * expMap.get("doubleHit_EXPperDamagePointDone")));
                        }
                        aliveEntity.setVelocity(knockback.multiply(2));
                        aliveEntity.setNoDamageTicks(20);

                    }
                }
            }.runTaskLater(plugin, 4);
        }
    }

    public void killBuffs(Entity entity) { //This is written very inefficiently and is very messy
        if (!runMethods) {
            return;
        }
        EntityGroups entityGroups = new EntityGroups();
        List<EntityType> hostileMobs = entityGroups.getHostileMobs();
        if ( !(hostileMobs.contains(entity.getType())) ) {
            return;
        }
        ItemGroups itemGroups = new ItemGroups();
        List<Material> swords = itemGroups.getSwords();
        if ( !(swords.contains(itemInHand.getType())) ) {
            return;
        }

        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int adrenaline1Level = Math.min((int) pStat.get(skillName).get(7), 5);
        int adrenaline2Level = Math.min((int) pStat.get(skillName).get(9), 5);
        int killingSpree1Level = Math.min((int) pStat.get(skillName).get(8), 5);
        int kilingSpree2Level = Math.min((int) pStat.get(skillName).get(10), 5);
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
        if (!runMethods) {
            return;
        }
        increaseStats.changeEXP(skillName,expMap.get("dealDamage"));
        increaseStats.changeEXP(skillName, (int) Math.round(finalDamage * expMap.get("dealDamage_EXPperDamagePointDone")));
    }

    public void giveKillEXP(Entity entity) {
        if (!runMethods) {
            return;
        }
        ItemGroups itemGroups = new ItemGroups();
        List<Material> swords = itemGroups.getSwords();
        if (!(swords.contains(itemInHand.getType()))) {
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
