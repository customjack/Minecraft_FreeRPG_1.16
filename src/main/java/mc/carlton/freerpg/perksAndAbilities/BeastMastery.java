package mc.carlton.freerpg.perksAndAbilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import mc.carlton.freerpg.configStorage.ConfigLoad;
import mc.carlton.freerpg.gameTools.ExpFarmTracker;
import mc.carlton.freerpg.gameTools.HorseRiding;
import mc.carlton.freerpg.globalVariables.EntityGroups;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mule;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class BeastMastery extends Skill {

  private String skillName = "beastMastery";

  private boolean runMethods;


  public BeastMastery(Player p) {
    super(p);
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
    Integer[] pTimers = timers.getPlayerCooldownTimes();
    Integer[] pAbilities = abilities.getPlayerAbilities();
    if (pAbilities[6] == -1) {
      int cooldown = pTimers[6];
      if (cooldown < 1) {
        int prepMessages = (int) pStatClass.getPlayerData().get("global")
            .get(22); //Toggle for preparation messages
        if (prepMessages > 0) {
          actionMessage.sendMessage(
              ChatColor.GRAY + ">>>" + lang.getString("prepare") + " " + lang.getString("leg")
                  + "...<<<");
        }
        int taskID = new BukkitRunnable() {
          @Override
          public void run() {
            if (prepMessages > 0) {
              actionMessage.sendMessage(
                  ChatColor.GRAY + ">>>..." + lang.getString("rest") + " " + lang.getString("leg")
                      + "<<<");
            }
            try {
              abilities.setPlayerAbility(skillName, -1);
            } catch (Exception e) {

            }
          }
        }.runTaskLater(plugin, 20 * 4).getTaskId();
        abilities.setPlayerAbility(skillName, taskID);
      } else {
        actionMessage.sendMessage(
            ChatColor.RED + lang.getString("spurKick") + " " + lang.getString("cooldown") + ": "
                + ChatColor.WHITE + cooldown + ChatColor.RED + "s");
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
    long duration = (long) duration0;
    int level = (int) pStat.get(skillName).get(13);
    Entity horse0 = p.getVehicle();
    if (horse0.getType() == EntityType.HORSE || horse0.getType() == EntityType.DONKEY
        || horse0.getType() == EntityType.MULE) {
      LivingEntity horse = (LivingEntity) horse0;
      for (PotionEffect potionEffect : horse.getActivePotionEffects()) {
        if ((potionEffect.getType() == PotionEffectType.SPEED
            && potionEffect.getDuration() > duration) || (
            potionEffect.getType() == PotionEffectType.SPEED
                && potionEffect.getAmplifier() > level)) {
          actionMessage.sendMessage(ChatColor.RED + ">>>" + lang.getString("hyperHorse") + "<<<");
          return;
        }

      }
      for (PotionEffect potionEffect : horse.getActivePotionEffects()) {
        if (potionEffect.getType() == PotionEffectType.SPEED) {
          horse.removePotionEffect(PotionEffectType.SPEED);
        }
      }
      horse.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (int) duration, level + 1));
    } else {
      return;
    }

    actionMessage.sendMessage(
        ChatColor.GREEN + ChatColor.BOLD.toString() + ">>>" + lang.getString("spurKick") + " "
            + lang.getString("activated") + "<<<");
    Bukkit.getScheduler().cancelTask(pAbilities[6]);
    abilities.setPlayerAbility(skillName, -2);
    String cooldownEndMessage =
        ChatColor.GREEN + ">>>" + lang.getString("spurKick") + " " + lang.getString("readyToUse")
            + "<<<";
    String endMessage =
        ChatColor.RED + ChatColor.BOLD.toString() + ">>>" + lang.getString("spurKick") + " "
            + lang.getString("ended") + "<<<";
    timers.abilityDurationTimer(skillName, duration, endMessage, cooldownEndMessage);
  }

  public void getHorseStats(Entity entity) {
    if (!runMethods) {
      return;
    }
    if (itemInHand.getType() != Material.COMPASS) {
      return;
    }
    if (entity.getType() == EntityType.HORSE || entity.getType() == EntityType.DONKEY
        || entity.getType() == EntityType.MULE) {
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
        double maxHealth = ((Attributable) animal).getAttribute(Attribute.GENERIC_MAX_HEALTH)
            .getBaseValue();
        double speed = ((Attributable) animal).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
            .getBaseValue();
        double jump = ((Attributable) animal).getAttribute(Attribute.HORSE_JUMP_STRENGTH)
            .getBaseValue();

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
        String title = ChatColor.GOLD + ChatColor.BOLD.toString()
            + "                   Horse Stats                   ";
        String health = ChatColor.AQUA + "HP: " + healthRead + ChatColor.WHITE + "/15.0 hearts";
        String maxSpeed =
            ChatColor.AQUA + "Speed: " + speedRead + ChatColor.WHITE + "/14.6 blocks per second";
        String jumpHeight =
            ChatColor.AQUA + "Jump Height: " + jumpRead + ChatColor.WHITE + "/5.30 blocks";
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

        String health =
            ChatColor.AQUA + "Max Health: " + healthRead + ChatColor.WHITE + "/10.0 hearts";
        String damage = ChatColor.AQUA + "Damage: " + damageRead + ChatColor.WHITE + "/3.0 hearts";
        String bars = ChatColor.GREEN + "------------------------------------------------";
        String title = ChatColor.GOLD + ChatColor.BOLD.toString()
            + "                   Wolf Stats                   ";
        p.sendMessage(bars);
        p.sendMessage(title);
        p.sendMessage(health);
        p.sendMessage(damage);
        p.sendMessage(bars);

      }
    }
  }

  public void tamingEXP(Entity entity) {
    if (!runMethods) {
      return;
    }
    if (entity.getType() == EntityType.HORSE || entity.getType() == EntityType.DONKEY
        || entity.getType() == EntityType.MULE || entity.getType() == EntityType.LLAMA) {
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
    List<EntityType> mounts = Arrays.asList(
        new EntityType[]{EntityType.HORSE, EntityType.DONKEY, EntityType.MULE, EntityType.LLAMA,
            EntityType.ZOMBIE_HORSE, EntityType.SKELETON_HORSE});
    if (mounts.contains(entity.getType())) {
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
              if (oldLocation.getWorld().equals(currentLocation.getWorld())) {
                int dx = oldLocation.getBlockX() - currentLocation.getBlockX();
                int dy = oldLocation.getBlockY() - currentLocation.getBlockY();
                int dz = oldLocation.getBlockZ() - currentLocation.getBlockZ();
                double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                data.setPlayerLocationMap(p);
                if (distance > 5 && distance < 200) { //Moved far enough, but also not teleportation
                  Block waterCheck1 = currentLocation.getBlock().getRelative(BlockFace.DOWN);
                  Block waterCheck2 = currentLocation.getBlock();
                  if (waterCheck1.getType() != Material.WATER
                      && waterCheck2.getType() != Material.WATER) { //Prevents water afk machines
                    increaseStats.changeEXP(skillName,
                        expMap.get("horseRiding_EXPperSecondMoving") * 5);
                  }
                }
              }
            }
          }
        }
      }.runTaskTimer(plugin, 100, 100).getTaskId();
      data_set.setTaskMap(taskID, p);
      data_set.setPlayerLocationMap(p);
      data_set.setMountPlayerMap(entity.getUniqueId(), p);
    }
  }

  public void breedingEXP(Entity entity) {
    if (!runMethods) {
      return;
    }
    EntityGroups entityGroups = new EntityGroups();
    List<EntityType> breedingAnimals = entityGroups.getBreedingAnimals();
    if (breedingAnimals.contains(entity.getType())) {
      increaseStats.changeEXP(skillName, expMap.get("breedTameableAnimal"));
    }
  }

  public void dogKillEntity(Entity entity) {
    if (!runMethods) {
      return;
    }
    EntityGroups entityGroups = new EntityGroups();
    entityGroups.killEntity(entity, skillName, expMap, increaseStats);

  }

  public void giveHitEXP(double finalDamage, Entity entity) {
    if (!runMethods || entity.getType().equals(EntityType.ARMOR_STAND)) {
      return;
    }
    ExpFarmTracker expFarmTracker = new ExpFarmTracker();
    double multiplier = expFarmTracker.getExpFarmAndSpawnerCombinedMultiplier(entity, skillName);
    increaseStats.changeEXP(skillName, (int) Math.round(
        (finalDamage * expMap.get("dogDamage_EXPperDamagePointDone") + expMap.get("dogDealDamage"))
            * multiplier));
  }


}
