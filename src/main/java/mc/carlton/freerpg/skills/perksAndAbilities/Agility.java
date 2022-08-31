package mc.carlton.freerpg.skills.perksAndAbilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.config.ConfigLoad;
import org.apache.logging.log4j.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Agility extends Skill {

  static Map<Player, Integer> gracefulFeetMap = new HashMap<>();
  static Map<Player, Long> playerSprintMap = new HashMap<>();
  Random rand = new Random(); //Random class Import
  private String skillName = "agility";
  private boolean runMethods;


  public Agility(Player p) {
    super(p);
    ConfigLoad configLoad = new ConfigLoad();
    runMethods = configLoad.getAllowedSkillsMap().get(skillName);
    expMap = configLoad.getExpMapForSkill(skillName);
  }

  public double roll(double finalDamage) {
    if (!runMethods) {
      return 1.0;
    }
    Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
    int rollLevel = (int) pStat.get(skillName).get(4);
    int steelBonesLevel = (int) pStat.get(skillName).get(9);
    double multiplier = 1;
    if (rollLevel * 0.0005 > rand.nextDouble()) {
      multiplier = 0.5 - steelBonesLevel * 0.1;
      increaseStats.changeEXP(skillName, expMap.get("rollBaseEXP") + (int) Math.round(
          finalDamage * expMap.get("roll_EXPperFallDamagePoint")));
      actionMessage.sendMessage(ChatColor.GREEN + ">>>" + lang.getString("roll") + "<<<");
    } else {
      if (finalDamage < p.getHealth()) {
        increaseStats.changeEXP(skillName,
            (int) Math.round(finalDamage * expMap.get("roll_EXPperFallDamagePoint")));
      }
    }
    return multiplier;
  }

  public boolean dodge(double finalDamage) {
    if (!runMethods) {
      return false;
    }
    Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
    int dodgeLevel = (int) pStat.get(skillName).get(7);
    double dodgeChance = Math.min(0.2, dodgeLevel * 0.04);
    if (dodgeChance > rand.nextDouble()) {
      increaseStats.changeEXP(skillName,
          (int) Math.round(expMap.get("dodge_EXPperDamagePointAvoided") * finalDamage));
      actionMessage.sendMessage(ChatColor.GREEN + ">>>" + lang.getString("dodge") + "<<<");
      return true;
    } else {
      return false;
    }
  }

  public void gracefulFeetStart() {
    if (!runMethods) {
      return;
    }
    Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
    int gracefulFeetLevel = (int) pStat.get(skillName).get(13);
    int gracefulFeetToggle = (int) pStat.get("global").get(14);
    if (gracefulFeetLevel > 0 && gracefulFeetToggle > 0) {
      if (p.getPotionEffect(PotionEffectType.SPEED) == null) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 20, 0));
      }
      int natureID = new BukkitRunnable() {
        @Override
        public void run() {
          if (p.getPotionEffect(PotionEffectType.SPEED) == null) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 20, 0));
            new BukkitRunnable() {
              @Override
              public void run() {
                if (p.isOnline()) {
                  if (p.getPotionEffect(PotionEffectType.SPEED) == null) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 20, 0));
                  }
                }
              }
            }.runTaskLater(plugin, 20 * 60 * 20 + 1);
          }
        }
      }.runTaskTimer(plugin, 200, 200).getTaskId();
      gracefulFeetMap.put(p, natureID);
    }
  }

  public void gracefulFeetEnd() {
    if (!runMethods) {
      return;
    }
    Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
    int gracefulFeetLevel = (int) pStat.get(skillName).get(13);
    if (gracefulFeetLevel > 0) {
      if (gracefulFeetMap.containsKey(p)) {
        if (p.isOnline()) {
          if (p.hasPotionEffect(PotionEffectType.SPEED)) {
            if (p.getPotionEffect(PotionEffectType.SPEED).getAmplifier() < 1) {
              p.removePotionEffect(PotionEffectType.SPEED);
            }
          }
        }
        Bukkit.getScheduler().cancelTask(gracefulFeetMap.get(p));
        gracefulFeetMap.remove(p);
      }
    }

  }

  public void sprintingEXP(boolean beginSprint) {
    if (!runMethods) {
      return;
    }
    if (beginSprint) {
      playerSprintMap.put(p, (new java.util.Date()).getTime());
    } else if (!playerSprintMap.containsKey(p)) { //Somehow never began sprinting
      return;
    } else {
      try {
        if (p.isFlying()) {
          return;
        }
        ConfigLoad configLoad = new ConfigLoad();
        double timeThreshold = configLoad.getAgilityMinSprintTimeForExperience();
        long oldTime = playerSprintMap.get(p);
        playerSprintMap.remove(p);
        long newTime = (new java.util.Date()).getTime();
        long timeSprint = newTime - oldTime;
        double timeSprintInSeconds = (timeSprint / 1000.0);
        if (timeSprintInSeconds >= timeThreshold) {
          int expToGive = (int) Math.round(
              timeSprintInSeconds * expMap.get("sprint_EXPperSecondSprinted"));
          increaseStats.changeEXP(skillName, expToGive);
        }
      } catch (Exception e) {
        FreeRPG.log(Level.ERROR, e.getMessage());
      }
    }
  }
}
