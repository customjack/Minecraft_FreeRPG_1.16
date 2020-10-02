package mc.carlton.freerpg.perksAndAbilities;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.ActionBarMessages;
import mc.carlton.freerpg.gameTools.LanguageSelector;
import mc.carlton.freerpg.playerInfo.ChangeStats;
import mc.carlton.freerpg.serverInfo.ConfigLoad;
import mc.carlton.freerpg.playerInfo.PlayerStats;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

public class Agility extends Skill{
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    private Player p;
    private String pName;
    private ItemStack itemInHand;
    private String skillName = "agility";
    static Map<Player,Integer> gracefulFeetMap = new HashMap<>();
    static Map<Player,Long> playerSprintMap = new HashMap<>();
    Map<String,Integer> expMap;

    ChangeStats increaseStats; //Changing Stats

    PlayerStats pStatClass;
    //GET PLAYER STATS LIKE THIS:        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData(p);

    ActionBarMessages actionMessage;
    LanguageSelector lang;

    Random rand = new Random(); //Random class Import

    private boolean runMethods;



    public Agility(Player p) {
        this.p = p;
        this.pName = p.getDisplayName();
        this.itemInHand = p.getInventory().getItemInMainHand();
        this.increaseStats = new ChangeStats(p);
        this.pStatClass = new PlayerStats(p);
        this.actionMessage = new ActionBarMessages(p);
        this.lang = new LanguageSelector(p);
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
        if (rollLevel*0.0005 > rand.nextDouble()) {
            multiplier = 0.5 - steelBonesLevel*0.1;
            increaseStats.changeEXP(skillName, expMap.get("rollBaseEXP") + (int) Math.round(finalDamage*expMap.get("roll_EXPperFallDamagePoint")));
            actionMessage.sendMessage(ChatColor.GREEN + ">>>" + lang.getString("roll") + "<<<");
        }
        else {
            if (finalDamage < p.getHealth()) {
                increaseStats.changeEXP(skillName, (int) Math.round(finalDamage*expMap.get("roll_EXPperFallDamagePoint")));
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
        double dodgeChance = Math.min(0.2,dodgeLevel*0.04);
        if (dodgeChance > rand.nextDouble()) {
            increaseStats.changeEXP(skillName,(int) Math.round(expMap.get("dodge_EXPperDamagePointAvoided")*finalDamage));
            actionMessage.sendMessage(ChatColor.GREEN + ">>>"+lang.getString("dodge")+"<<<");
            return true;
        }
        else {
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
        if (!runMethods) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int gracefulFeetLevel = (int) pStat.get(skillName).get(13);
        if (gracefulFeetLevel > 0) {
            if (gracefulFeetMap.containsKey(p)) {
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
            playerSprintMap.put(p,(new java.util.Date()).getTime());
        }
        else {
            try {
                if (p.isFlying()) {
                    return;
                }
                long oldTime = playerSprintMap.get(p);
                long newTime = (new java.util.Date()).getTime();
                long timeSprint = newTime-oldTime;
                int expToGive = (int)Math.round((timeSprint/1000.0)*expMap.get("sprint_EXPperSecondSprinted"));
                increaseStats.changeEXP(skillName, expToGive);
            }
            catch (Exception e) {

            }
        }
    }
}
