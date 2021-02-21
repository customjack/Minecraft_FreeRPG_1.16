package mc.carlton.freerpg.perksAndAbilities;

import mc.carlton.freerpg.gameTools.ExpFarmTracker;
import mc.carlton.freerpg.globalVariables.EntityGroups;
import mc.carlton.freerpg.globalVariables.ItemGroups;
import mc.carlton.freerpg.configStorage.ConfigLoad;
import org.bukkit.*;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class AxeMastery extends Skill{
    private String skillName = "axeMastery";


    Random rand = new Random(); //Random class Import

    private boolean runMethods;


    public AxeMastery(Player p) {
        super(p);
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
        Integer[] pTimers = timers.getPlayerCooldownTimes();
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
                            Integer[] pTimers2 = timers.getPlayerCooldownTimes();
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
                actionMessage.sendMessage(ChatColor.RED +lang.getString("greatAxe") + " " + lang.getString("cooldown") + ": " + ChatColor.WHITE + cooldown+ ChatColor.RED + "s");
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
        long duration = (long) duration0;
        Bukkit.getScheduler().cancelTask(pAbilities[9]);
        abilities.setPlayerAbility( skillName, -2);
        String cooldownEndMessage = ChatColor.GREEN + ">>>" + lang.getString("greatAxe") + " " + lang.getString("readyToUse") + "<<<";
        String endMessage = ChatColor.RED + ChatColor.BOLD.toString() + ">>>" + lang.getString("greatAxe") + " " + lang.getString("ended") + "<<<";
        timers.abilityDurationTimer(skillName,duration,endMessage,cooldownEndMessage);
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
            if ((int)pStat.get("global").get(27) < 1) { //Holy axe Toggle
                return;
            }
            int holyAxeLevel = (int) pStat.get(skillName).get(8);
            if (holyAxeLevel*0.02 > rand.nextDouble()) {
                world.strikeLightning(entity.getLocation());
                increaseStats.changeEXP(skillName,expMap.get("holyAxeActivateEXP"));
            }
        }
    }

    public void giveHitEXP(double finalDamage,Entity entity) {
        if (!runMethods || entity.getType().equals(EntityType.ARMOR_STAND)) {
            return;
        }
        ExpFarmTracker expFarmTracker = new ExpFarmTracker();
        double multiplier = expFarmTracker.getExpFarmAndSpawnerCombinedMultiplier(entity,skillName);
        increaseStats.changeEXP(skillName, (int) Math.round((finalDamage * expMap.get("axeDamage_EXPperDamagePointDone")+expMap.get("dealAxeDamage"))*multiplier));
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
        EntityGroups entityGroups = new EntityGroups();
        entityGroups.killEntity(entity,skillName,expMap,increaseStats);
    }

}
