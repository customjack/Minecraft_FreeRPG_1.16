package mc.carlton.freerpg.perksAndAbilities;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.ActionBarMessages;
import mc.carlton.freerpg.gameTools.EntityPickedUpItemStorage;
import mc.carlton.freerpg.gameTools.ExpFarmTracker;
import mc.carlton.freerpg.gameTools.LanguageSelector;
import mc.carlton.freerpg.globalVariables.EntityGroups;
import mc.carlton.freerpg.playerInfo.*;
import mc.carlton.freerpg.serverInfo.ConfigLoad;
import mc.carlton.freerpg.serverInfo.MinecraftVersion;
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

public class Defense extends Skill{
    private String skillName = "defense";

    Random rand = new Random(); //Random class Import

    private boolean runMethods;


    public Defense(Player p) {
        super(p);
        ConfigLoad configLoad = new ConfigLoad();
        this.runMethods = configLoad.getAllowedSkillsMap().get(skillName);
        expMap = configLoad.getExpMapForSkill(skillName);
    }

    public void initiateAbility() {
        if (!runMethods) {
            return;
        }
        if (!p.hasPermission("freeRPG.defenseAbility")) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        if ((int) pStat.get("global").get(24) < 1 || !pStatClass.isPlayerSkillAbilityOn(skillName)) {
            return;
        }
        Integer[] pTimers = timers.getPlayerCooldownTimes();
        Integer[] pAbilities = abilities.getPlayerAbilities();
        if (pAbilities[8] == -1) {
            int cooldown = pTimers[8];
            if (cooldown < 1) {
                int prepMessages = (int) pStatClass.getPlayerData().get("global").get(22); //Toggle for preparation messages
                if (prepMessages > 0) {
                    actionMessage.sendMessage(ChatColor.GRAY + ">>>" + lang.getString("prepare") + " " + lang.getString("yourself") + "...<<<");
                }
                int taskID = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (prepMessages > 0) {
                            actionMessage.sendMessage(ChatColor.GRAY + ">>>..." + lang.getString("rest") + " " +lang.getString("yourself") + "<<<");
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
                actionMessage.sendMessage(ChatColor.RED +lang.getString("stoneSoldier") + " " + lang.getString("cooldown") + ": " + ChatColor.WHITE + cooldown+ ChatColor.RED + "s");
            }
        }
    }

    public void enableAbility() {
        if (!runMethods) {
            return;
        }
        Integer[] pAbilities = abilities.getPlayerAbilities();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        actionMessage.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + ">>>" + lang.getString("stoneSoldier") + " " + lang.getString("activated") + "<<<");
        int durationLevel = (int) pStat.get(skillName).get(4);
        double duration0 = Math.ceil(durationLevel * 0.4) + 40;
        long duration = (long) duration0;
        int strongerLegsLevel = (int) pStat.get(skillName).get(12);
        int giftFromAboveLevel = (int) pStat.get(skillName).get(11);
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

        Bukkit.getScheduler().cancelTask(pAbilities[8]);
        abilities.setPlayerAbility( skillName, -2);
        String coolDownEndMessage = ChatColor.GREEN + ">>>" + lang.getString("stoneSoldier") + " " + lang.getString("readyToUse") + "<<<";
        String endMessage = ChatColor.RED + ChatColor.BOLD.toString() + ">>>" + lang.getString("stoneSoldier") + " " + lang.getString("ended") + "<<<";
        timers.abilityDurationTimer(skillName,duration,endMessage,coolDownEndMessage);
    }

    public void preventLogoutTheft(int taskID_defense,boolean pluginDisabled) {
        if (!runMethods) {
            return;
        }
        Integer[] pAbilities = abilities.getPlayerAbilities();
        if (pAbilities[8] == -2) {
            Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
            int durationLevel = (int) pStat.get(skillName).get(4);
            double duration0 = Math.ceil(durationLevel * 0.4) + 40;
            long duration = (long) duration0;
            int strongerLegsLevel = (int) pStat.get(skillName).get(12);
            int giftFromAboveLevel = (int) pStat.get(skillName).get(11);
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
            String coolDownEndMessage = ChatColor.GREEN + ">>>" + lang.getString("stoneSoldier") + " " + lang.getString("readyToUse") + "<<<";
            String endMessage = ChatColor.RED + ChatColor.BOLD.toString() + ">>>" + lang.getString("stoneSoldier") + " " + lang.getString("ended") + "<<<";
            timers.endAbility(skillName,endMessage,coolDownEndMessage,pluginDisabled);
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
        if (!runMethods) {
            return 1.0;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int hardBodyLevel = (int) pStat.get(skillName).get(5);
        int hardHeadedLevel = (int) pStat.get(skillName).get(9);
        double chance = 0.01 + 0.0001*hardBodyLevel;
        if (chance > rand.nextDouble()) {
            double multiplier = Math.max((1- .33 - .06666*hardHeadedLevel),0);
            increaseStats.changeEXP(skillName, expMap.get("hardBodyActivate"));
            return multiplier;
        }
        return 1;
    }

    public void reactions(double finalDamage) {
        if (!runMethods) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int reactionsLevel_I = (int) pStat.get(skillName).get(8);
        int reactionsLevel_II = (int) pStat.get(skillName).get(10);
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
            increaseStats.changeEXP(skillName, expMap.get("reactionsLevel2Activate"));

        }
        else if (reactionsLevel_I*0.02 > rand.nextDouble()) {
            boolean[] resistanceChecks = buffCheckerResistance(0,100);
            if (resistanceChecks[0]) {
                if (resistanceChecks[1]) {
                    p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                }
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100,0));
            }
            increaseStats.changeEXP(skillName, expMap.get("reactionsLevel1Activate"));
        }
    }

    public void hearty() {
        if (!runMethods) {
            ConfigLoad configLoad = new ConfigLoad();
            double HP = configLoad.getBasePlayerHP();
            ((Attributable) p).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(HP);
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int heartyLevel = (int) pStat.get(skillName).get(13);
        ConfigLoad configLoad = new ConfigLoad();
        double HP = configLoad.getBasePlayerHP();
        if (heartyLevel > 0) {
            if (((Attributable) p).getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() <= HP + 4.0) {
                ((Attributable) p).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(HP + 4.0);
            }
        }
        else {
            ((Attributable) p).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(HP);
        }

    }

    public void doubleDrops(LivingEntity entity, List<ItemStack> drops, World world) {
        if (!runMethods) {
            return;
        }
        EntityGroups entityGroups = new EntityGroups();
        List<EntityType> hostileMobs = entityGroups.getHostileMobs();
        if (hostileMobs.contains(entity.getType())) {
            Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
            int doubleDropsLevel = (int) pStat.get(skillName).get(6);
            EntityPickedUpItemStorage entityPickedUpItemStorage = new EntityPickedUpItemStorage();
            if (doubleDropsLevel*0.0005 > rand.nextDouble()) {
                for (ItemStack drop : drops) {
                    if (!entityPickedUpItemStorage.wasItemPickedUp(drop,entity)) {
                        dropItemNaturally(entity.getLocation().add(0, 0.5, 0), drop);
                    }
                }
            }
        }

    }

    public void healer() {
        if (!runMethods) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int healerLevel = (int) pStat.get(skillName).get(7);
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
        increaseStats.changeEXP(skillName, expMap.get("healerRegenActivate"));
        if (healerLevel < 4) {
            return;
        }
        double maxHP = ((Attributable) p).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        int HP_to_add = (healerLevel-3);
        p.setHealth(Math.min(maxHP,p.getHealth()+HP_to_add));
        increaseStats.changeEXP(skillName, expMap.get("healerHealActivate"));
    }

    public void giveHitEXP(double damage,Entity entity) {
        if (!runMethods || entity.getType().equals(EntityType.ARMOR_STAND)) {
            return;
        }
        ExpFarmTracker expFarmTracker = new ExpFarmTracker();
        double multiplier = expFarmTracker.getExpFarmAndSpawnerCombinedMultiplier(entity,skillName);
        if (p.isBlocking()) {
            ConfigLoad configLoad = new ConfigLoad();
            multiplier *= configLoad.getSpecialMultiplier().get("blockingEXPMultiplier");
        }
        double expToReward = (damage * expMap.get("takeDamage_EXPperDamagePointDone") + expMap.get("takeDamage"))*multiplier;
        increaseStats.changeEXP(skillName, (int) Math.round(expToReward));
    }

    public void giveKillEXP(Entity entity) {
        if (!runMethods) {
            return;
        }
        EntityGroups entityGroups = new EntityGroups();
        entityGroups.killEntity(entity,skillName,expMap,increaseStats);
    }

    public void armorEXP(ItemStack armor) {
        if (!runMethods) {
            return;
        }
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

        MinecraftVersion minecraftVersion = new MinecraftVersion();
        if (minecraftVersion.getMinecraftVersion_Double() >=1.16) {
            armorEXP.put(Material.NETHERITE_BOOTS, 200 * 15);
            armorEXP.put(Material.NETHERITE_LEGGINGS, 350 * 15);
            armorEXP.put(Material.NETHERITE_CHESTPLATE, 400 * 15);
            armorEXP.put(Material.NETHERITE_HELMET, 250 * 15);
        }

        if (armorEXP.keySet().contains(armor.getType())) {
            increaseStats.changeEXP(skillName,armorEXP.get(armor.getType()));
        }
    }
}
