package mc.carlton.freerpg.perksAndAbilities;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.playerAndServerInfo.ChangeStats;
import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Agility {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    private Player p;
    private String pName;
    private ItemStack itemInHand;
    static Map<Player,Integer> gracefulFeetMap = new HashMap<>();
    static Map<Player,Long> playerSprintMap = new HashMap<>();

    ChangeStats increaseStats; //Changing Stats

    PlayerStats pStatClass;
    //GET PLAYER STATS LIKE THIS:        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData(p);

    Random rand = new Random(); //Random class Import



    public Agility(Player p) {
        this.p = p;
        this.pName = p.getDisplayName();
        this.itemInHand = p.getInventory().getItemInMainHand();
        this.increaseStats = new ChangeStats(p);
        this.pStatClass = new PlayerStats(p);
    }

    public double roll(double finalDamage) {
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int rollLevel = (int) pStat.get("agility").get(4);
        int steelBonesLevel = (int) pStat.get("agility").get(9);
        double multiplier = 1;
        if (rollLevel*0.0005 > rand.nextDouble()) {
            multiplier = 0.5 - steelBonesLevel*0.1;
            increaseStats.changeEXP("agility", 250 + (int) Math.round(10*finalDamage)*10);
        }
        else {
            if (finalDamage < p.getHealth()) {
                increaseStats.changeEXP("agility", (int) Math.round(10 * finalDamage) * 10);
            }
        }
        return multiplier;
    }

    public boolean dodge(double finalDamage) {
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int dodgeLevel = (int) pStat.get("agility").get(7);
        double dodgeChance = Math.min(0.2,dodgeLevel*0.04);
        if (dodgeChance > rand.nextDouble()) {
            increaseStats.changeEXP("agility",(int) Math.round(10*finalDamage)*10);
            return true;
        }
        else {
            return false;
        }
    }

    public void gracefulFeetStart() {
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int gracefulFeetLevel = (int) pStat.get("agility").get(13);
        if (gracefulFeetLevel > 0) {
            if (p.getPotionEffect(PotionEffectType.SPEED) == null) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 20, 0));
            }
            int natureID = new BukkitRunnable() {
                @Override
                public void run() {
                    if (p.getPotionEffect(PotionEffectType.SPEED) == null) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,20*60*20,0));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (p.isOnline()) {
                                    if (p.getPotionEffect(PotionEffectType.SPEED) == null) {
                                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 20, 0));
                                    }
                                }
                            }
                        }.runTaskLater(plugin,20*60*20 + 1);
                    }
                }
            }.runTaskTimer(plugin, 200,200).getTaskId();
            gracefulFeetMap.put(p,natureID);
        }
    }
    public void gracefulFeetEnd(){
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int gracefulFeetLevel = (int) pStat.get("agility").get(13);
        if (gracefulFeetLevel > 0) {
            Bukkit.getScheduler().cancelTask(gracefulFeetMap.get(p));
            gracefulFeetMap.remove(p);
        }

    }

    public void sprintingEXP(boolean beginSprint) {
        if (beginSprint) {
            playerSprintMap.put(p,(new java.util.Date()).getTime());
        }
        else {
            try {
                long oldTime = playerSprintMap.get(p);
                long newTime = (new java.util.Date()).getTime();
                long timeSprint = newTime-oldTime;
                int expToGive = (int)Math.round(timeSprint/1000.0 *3)*12;
                increaseStats.changeEXP("agility", expToGive);
            }
            catch (Exception e) {

            }
        }
    }
}
