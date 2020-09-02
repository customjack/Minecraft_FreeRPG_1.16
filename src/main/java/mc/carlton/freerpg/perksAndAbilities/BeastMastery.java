package mc.carlton.freerpg.perksAndAbilities;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.ActionBarMessages;
import mc.carlton.freerpg.gameTools.HorseRiding;
import mc.carlton.freerpg.gameTools.LanguageSelector;
import mc.carlton.freerpg.globalVariables.EntityGroups;
import mc.carlton.freerpg.globalVariables.ItemGroups;
import mc.carlton.freerpg.playerAndServerInfo.*;
import org.bukkit.*;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class BeastMastery {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    private Player p;
    private ItemStack itemInHand;
    private String skillName = "beastMastery";
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

    private boolean runMethods;


    public BeastMastery(Player p) {
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
        if (!p.hasPermission("freeRPG.beastMasteryAbility")) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        if ((int) pStat.get("global").get(24) < 1 || !pStatClass.isPlayerSkillAbilityOn(skillName)) {
            return;
        }
        Integer[] pTimers = timers.getPlayerTimers();
        Integer[] pAbilities = abilities.getPlayerAbilities();
        if (pAbilities[6] == -1) {
            int cooldown = pTimers[6];
            if (cooldown < 1) {
                int prepMessages = (int) pStatClass.getPlayerData().get("global").get(22); //Toggle for preparation messages
                if (prepMessages >0) {
                    actionMessage.sendMessage(ChatColor.GRAY + ">>>" + lang.getString("prepare") + " " + lang.getString("leg") + "...<<<");
                }
                int taskID = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (prepMessages > 0) {
                            actionMessage.sendMessage(ChatColor.GRAY + ">>>..." + lang.getString("rest") + " " +lang.getString("leg") + "<<<");
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
                actionMessage.sendMessage(ChatColor.RED +lang.getString("spurKick") + " " + lang.getString("cooldown") + ": " + cooldown+ "s");
            }
        }
    }

    public void enableAbility() {
        if (!runMethods) {
            return;
        }
        Integer[] pAbilities = abilities.getPlayerAbilities();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int durationLevel = (int) pStat.get(skillName).get(4);
        double duration0 = Math.ceil(durationLevel * 0.4) + 40;
        int cooldown = 150;
        if ((int) pStat.get("global").get(11) > 0) {
            cooldown = 100;
        }
        int finalCooldown = cooldown;
        long duration = (long) duration0;
        int level = (int) pStat.get(skillName).get(13);
        Entity horse0 = p.getVehicle();
        if (horse0.getType() == EntityType.HORSE || horse0.getType() == EntityType.DONKEY || horse0.getType() == EntityType.MULE) {
            LivingEntity horse = (LivingEntity) horse0;
            for (PotionEffect potionEffect : horse.getActivePotionEffects()) {
                if ((potionEffect.getType() == PotionEffectType.SPEED && potionEffect.getDuration() > duration) || (potionEffect.getType() == PotionEffectType.SPEED && potionEffect.getAmplifier() > level)) {
                    actionMessage.sendMessage(ChatColor.RED + ">>>" +lang.getString("hyperHorse") + "<<<");
                    return;
                }

            }
            for (PotionEffect potionEffect : horse.getActivePotionEffects()) {
                if (potionEffect.getType() == PotionEffectType.SPEED) {
                    horse.removePotionEffect(PotionEffectType.SPEED);
                }
            }
            horse.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (int) duration, level+1));
        }
        else {
            return;
        }

        actionMessage.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + ">>>" + lang.getString("spurKick") + " " + lang.getString("activated") + "<<<");
        timers.setPlayerTimer( skillName, finalCooldown);
        Bukkit.getScheduler().cancelTask(pAbilities[6]);
        abilities.setPlayerAbility( skillName, -2);
        int taskID = new BukkitRunnable() {
            @Override
            public void run() {
                actionMessage.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + ">>>" + lang.getString("spurKick") + " " + lang.getString("ended") + "<<<");
                abilities.setPlayerAbility( skillName, -1);
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
                                    actionMessage.sendMessage(ChatColor.GREEN + ">>>" + lang.getString("spurKick") + " " + lang.getString("readyToUse") + "<<<");
                                }
                            }
                        }
                    }.runTaskLater(plugin, 20 * i);
                }
            }
        }.runTaskLater(plugin, duration).getTaskId();
    }

    public void getHorseStats(Entity entity) {
        if (!runMethods) {
            return;
        }
        if (itemInHand.getType() != Material.COMPASS) {
            return;
        }
        if (entity.getType() == EntityType.HORSE || entity.getType() == EntityType.DONKEY || entity.getType() == EntityType.MULE) {
            Entity animal = entity;
            if (entity.getType() == EntityType.HORSE) {
                animal = (Horse) entity;
            } else if (entity.getType() == EntityType.DONKEY) {
                animal = (Donkey) entity;
            } else if (entity.getType() == EntityType.MULE) {
                animal = (Mule) entity;
            }
            Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
            int identifyLevel = (int) pStat.get(skillName).get(12);
            if (identifyLevel > 0) {
                double maxHealth = ((Attributable) animal).getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                double speed = ((Attributable) animal).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
                double jump = ((Attributable) animal).getAttribute(Attribute.HORSE_JUMP_STRENGTH).getBaseValue();

                double healthReadable = Math.round((maxHealth / 2.0d) * 10) / 10.0d;
                double speedReadable = Math.round(speed * 431.1111) / 10.0d;
                double jumpReadable = Math.round((3.31 * jump * jump + 2.38 * jump - 0.40) * 100) / 100.0d;

                double healthPercentage = (healthReadable - 7.5d) / 7.5d;
                double speedPercentage = (speedReadable - 4.85d) / (14.55d - 4.85d);
                double jumpPercentage = (jumpReadable - 1.086d) / (5.293d - 1.086d);

                String healthRead = "";
                String speedRead = "";
                String jumpRead = "";

                if (healthPercentage < 0.25) {
                    healthRead = ChatColor.DARK_RED + Double.toString(healthReadable);
                } else if (healthPercentage < 0.75) {
                    healthRead = ChatColor.YELLOW + Double.toString(healthReadable);
                } else {
                    healthRead = ChatColor.DARK_GREEN + Double.toString(healthReadable);
                }

                if (speedPercentage < 0.25) {
                    speedRead = ChatColor.DARK_RED + Double.toString(speedReadable);
                } else if (speedPercentage < 0.75) {
                    speedRead = ChatColor.YELLOW + Double.toString(speedReadable);
                } else {
                    speedRead = ChatColor.DARK_GREEN + Double.toString(speedReadable);
                }

                if (jumpPercentage < 0.25) {
                    jumpRead = ChatColor.DARK_RED + Double.toString(jumpReadable);
                } else if (jumpPercentage < 0.75) {
                    jumpRead = ChatColor.YELLOW + Double.toString(jumpReadable);
                } else {
                    jumpRead = ChatColor.DARK_GREEN + Double.toString(jumpReadable);
                }

                String bars = ChatColor.GREEN + "-------------------------------------------------";
                String title = ChatColor.GOLD + ChatColor.BOLD.toString() + "                   Horse Stats                   ";
                String health = ChatColor.AQUA + "HP: " + healthRead + ChatColor.WHITE + "/15.0 hearts";
                String maxSpeed = ChatColor.AQUA + "Speed: " + speedRead + ChatColor.WHITE + "/14.6 blocks per second";
                String jumpHeight = ChatColor.AQUA + "Jump Height: " + jumpRead + ChatColor.WHITE + "/5.30 blocks";
                p.sendMessage(bars);
                p.sendMessage(title);
                p.sendMessage(health);
                p.sendMessage(maxSpeed);
                p.sendMessage(jumpHeight);
                p.sendMessage(bars);
            }
        } else if (entity.getType() == EntityType.WOLF) {
            Tameable dog = (Tameable) entity;
            if (dog.isTamed()) {
                LivingEntity livingDog = (LivingEntity) dog;
                Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
                int sharpTeethLevel = (int) pStat.get(skillName).get(8);
                double damageBoost = sharpTeethLevel * 2 / 10.0d + 2.0;
                String damageRead = ChatColor.YELLOW + Double.toString(damageBoost);

                double healthRemaining = ((LivingEntity) dog).getHealth();
                double healthReadable = Math.round((healthRemaining / 2.0d) * 10) / 10.0d;
                double healthPercentage = (healthReadable) / 10.0d;
                String healthRead = "";
                if (healthPercentage < 0.26) {
                    healthRead = ChatColor.DARK_RED + Double.toString(healthReadable);
                } else if (healthPercentage < 0.74) {
                    healthRead = ChatColor.YELLOW + Double.toString(healthReadable);
                } else {
                    healthRead = ChatColor.DARK_GREEN + Double.toString(healthReadable);
                }

                String health = ChatColor.AQUA + "Max Health: " + healthRead + ChatColor.WHITE + "/10.0 hearts";
                String damage = ChatColor.AQUA + "Damage: " + damageRead + ChatColor.WHITE + "/3.0 hearts";
                String bars = ChatColor.GREEN + "------------------------------------------------";
                String title = ChatColor.GOLD + ChatColor.BOLD.toString() + "                   Wolf Stats                   ";
                p.sendMessage(bars);
                p.sendMessage(title);
                p.sendMessage(health);
                p.sendMessage(damage);
                p.sendMessage(bars);

            }
        }
    }

    public void tamingEXP(Entity entity){
        if (!runMethods) {
            return;
        }
        if (entity.getType() == EntityType.HORSE || entity.getType() == EntityType.DONKEY || entity.getType() == EntityType.MULE || entity.getType() == EntityType.LLAMA) {
            increaseStats.changeEXP(skillName, expMap.get("tameHorse"));
        } else if (entity.getType() == EntityType.WOLF) {
            increaseStats.changeEXP(skillName, expMap.get("tameWolf"));
        } else if (entity.getType() == EntityType.CAT) {
            increaseStats.changeEXP(skillName, expMap.get("tameCat"));
        } else if (entity.getType() == EntityType.PARROT) {
            increaseStats.changeEXP(skillName, expMap.get("tameParrot"));
        }
    }

    public void horseRidingEXP(Entity entity) {
        if (!runMethods) {
            return;
        }
        if (entity.getType() == EntityType.HORSE || entity.getType() == EntityType.DONKEY || entity.getType() == EntityType.MULE || entity.getType() == EntityType.LLAMA) {
            Entity mount = entity;
            HorseRiding data_set = new HorseRiding();
            int taskID = new BukkitRunnable() {
                @Override
                public void run() {
                    if (p.isInsideVehicle()) {
                        if (p.getVehicle().equals(entity)) {
                            HorseRiding data = new HorseRiding();
                            Location oldLocation = data.getOldLocation(p);
                            Location currentLocation = p.getLocation();
                            int dx = oldLocation.getBlockX()-currentLocation.getBlockX();
                            int dy = oldLocation.getBlockY()-currentLocation.getBlockY();
                            int dz = oldLocation.getBlockZ()-currentLocation.getBlockZ();
                            double distance = Math.sqrt(dx*dx + dy*dy + dz*dz);
                            data.setPlayerLocationMap(p);
                            if (distance > 5 && distance < 200) { //Moved far enough, but also not teleportation
                                Block waterCheck1 = currentLocation.getBlock().getRelative(BlockFace.DOWN);
                                Block waterCheck2 = currentLocation.getBlock();
                                if (waterCheck1.getType() != Material.WATER && waterCheck2.getType() != Material.WATER) { //Prevents water afk machines
                                    increaseStats.changeEXP(skillName,expMap.get("horseRiding_EXPperSecondMoving")*5);
                                }
                            }
                        }
                    }
                }
            }.runTaskTimer(plugin, 100,100).getTaskId();
            data_set.setTaskMap(taskID,p);
            data_set.setPlayerLocationMap(p);
            data_set.setMountPlayerMap(entity.getUniqueId(),p);
        }
    }
    public void breedingEXP(Entity entity) {
        if (!runMethods) {
            return;
        }
        EntityGroups entityGroups = new EntityGroups();
        List<EntityType> breedingAnimals = entityGroups.getBreedingAnimals();
        if (breedingAnimals.contains(entity.getType())) {
            increaseStats.changeEXP(skillName,expMap.get("breedTameableAnimal"));
        }
    }

    public void dogKillEntity(Entity entity) {
        if (!runMethods) {
            return;
        }
        EntityGroups entityGroups = new EntityGroups();
        entityGroups.killEntity(entity,skillName,expMap,increaseStats);

    }


}
