package mc.carlton.freerpg.playerInfo;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.ActionBarMessages;
import mc.carlton.freerpg.gameTools.BossBarStorage;
import mc.carlton.freerpg.gameTools.LanguageSelector;
import mc.carlton.freerpg.gameTools.TrackItem;
import mc.carlton.freerpg.serverConfig.ConfigLoad;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AbilityTimers {
    private Player player;
    private UUID uuid;
    static Map<UUID, Integer[]> cooldownTimes = new ConcurrentHashMap<>();
    static Map<UUID,Integer> cooldownTaskIds = new ConcurrentHashMap<>();
    static Map<UUID,Integer> abilityDurationTaskIds = new ConcurrentHashMap<>();
    static Map<UUID,Map<String,Integer>> abilityDurationTimes = new ConcurrentHashMap<>();
    static Map<UUID,Map<String,BossBar>> abilityDurationBossBar = new ConcurrentHashMap<>();
    ItemStack itemInHand;

    public AbilityTimers(Player p) {
        this.player = p;
        this.uuid = player.getUniqueId();
    }

    public Integer[] getPlayerCooldownTimes() {
        if (!cooldownTimes.containsKey(uuid)) {
            Integer[] initTimers = {0,0,0,0,0,0,0,0,0,0,0,0};
            cooldownTimes.put(uuid,initTimers);
        }
        return cooldownTimes.get(uuid);
    }
    public Map<UUID, Integer[]> getCooldownTimes() {
        if (!cooldownTimes.containsKey(uuid)) {
            Integer[] initTimers = {0,0,0,0,0,0,0,0,0,0,0,0};
            cooldownTimes.put(uuid,initTimers);
        }
        return cooldownTimes;
    }

    public void setPlayerCooldownTime(String ability, int timeRemaining) {
        Integer[] pTimes = cooldownTimes.get(uuid);
        switch(ability) {
            case "digging":
                pTimes[0] = timeRemaining;
                break;
            case "woodcutting":
                pTimes[1] = timeRemaining;
                break;
            case "mining":
                pTimes[2] = timeRemaining;
                break;
            case "farming":
                pTimes[3] = timeRemaining;
                break;
            case "fishing":
                pTimes[4] = timeRemaining;
                break;
            case "archery":
                pTimes[5] = timeRemaining;
                break;
            case "beastMastery":
                pTimes[6] = timeRemaining;
                break;
            case "swordsmanship":
                pTimes[7] = timeRemaining;
                break;
            case "defense":
                pTimes[8] = timeRemaining;
                break;
            case "axeMastery":
                pTimes[9] = timeRemaining;
                break;
            case "woodcuttingHaste":
                pTimes[10] = timeRemaining;
                break;
            case "fishingRob":
                pTimes[11] = timeRemaining;
            default:
                break;
        }
        cooldownTimes.put(uuid,pTimes);
    }

    public int getPlayerCooldownTime(String ability) {
        if (!cooldownTimes.containsKey(uuid)) {
            Integer[] initTimers = {0,0,0,0,0,0,0,0,0,0,0,0};
            cooldownTimes.put(uuid,initTimers);
        }
        Integer[] pTimes = cooldownTimes.get(uuid);
        switch(ability) {
            case "digging":
                return pTimes[0];
            case "woodcutting":
                return pTimes[1];
            case "mining":
                return pTimes[2];
            case "farming":
                return pTimes[3];
            case "fishing":
                return pTimes[4];
            case "archery":
                return pTimes[5];
            case "beastMastery":
                return pTimes[6];
            case "swordsmanship":
                return pTimes[7];
            case "defense":
                return pTimes[8];
            case "axeMastery":
                return pTimes[9];
            case "woodcuttingHaste":
                return pTimes[10];
            case "fishingRob":
                return pTimes[11];
            default:
                return 0;
        }
    }
    public void setTimes(Map<UUID, Integer[]> timers) {
        this.cooldownTimes = timers;
    }
    public void removePlayer() {
        Integer[] pTimers = getPlayerCooldownTimes();
        boolean allZero = true;
        for (int i : pTimers) {
            if (i > 0){
                allZero = false;
                break;
            }
        }
        if (allZero) {
            cooldownTimes.remove(uuid);
            abilityDurationTimes.remove(uuid);
            abilityDurationBossBar.remove(uuid);
        }
    }

    public void killCooldownTask(UUID taskUUID) {
        int taskId = cooldownTaskIds.get(taskUUID);
        Bukkit.getScheduler().cancelTask(taskId);
        cooldownTaskIds.remove(taskUUID);
    }

    public void initializeCooldownTimer(String skillName) { //Used to make sure the cooldown if the player logs out before it starts
        setPlayerCooldownTime(skillName,1);
    }

    public void cooldownTimer(String skillName,String cooldownCompleteMessage) {
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        PlayerStats pStatClass = new PlayerStats(player);
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        ConfigLoad configLoad = new ConfigLoad();
        int cooldown = configLoad.getAbilityCooldowns().get(skillName);
        if ((int) pStat.get("global").get(11) > 0) {
            cooldown = (int) Math.round(cooldown*2.0/3.0);
        }
        setPlayerCooldownTime( skillName, cooldown);
        UUID taskUUID = UUID.randomUUID();
        int taskId = new BukkitRunnable() {
            @Override
            public void run() {
                int timeRemaining = getPlayerCooldownTime(skillName)-1;
                if (timeRemaining  <= 0) {
                    setPlayerCooldownTime(skillName,timeRemaining);
                    if (!player.isOnline()) {
                        removePlayer();
                    }
                    else {
                        ActionBarMessages actionMessage = new ActionBarMessages(player);
                        actionMessage.sendMessage(cooldownCompleteMessage);
                    }
                    killCooldownTask(taskUUID);
                }
                else {
                    setPlayerCooldownTime(skillName,timeRemaining);
                }
            }
        }.runTaskTimer(plugin, 20,20).getTaskId();
        cooldownTaskIds.put(taskUUID,taskId);
    }

    public void abilityDurationTimer(String skillName,long duration,String endMessage,String cooldownEndMessage) {
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        initializeCooldownTimer(skillName);
        setAbilityDurationTime(skillName,(int) duration);
        int refreshTicks = 2;
        UUID taskUUID = UUID.randomUUID();
        int taskID = new BukkitRunnable() { //The outer task is used for Ability time remaining bars and is accurate to 0.1 seconds on default
            @Override
            public void run() {
                int timeRemaining = getPlayerDurationTime(skillName)-refreshTicks;
                durationBarUpdate(skillName,timeRemaining,duration);
                if (timeRemaining < refreshTicks) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            endAbility(skillName,endMessage,cooldownEndMessage,false);
                        }
                    }.runTaskLater(plugin,timeRemaining);
                    killDurationTask(taskUUID); //Kill this task as soon as the new task is scheduled
                }
                else {
                    setAbilityDurationTime(skillName,timeRemaining);
                }
            }
        }.runTaskTimer(plugin,refreshTicks,refreshTicks).getTaskId();
        abilityDurationTaskIds.put(taskUUID,taskID);
        AbilityLogoutTracker incaseLogout = new AbilityLogoutTracker(player);
        incaseLogout.setPlayerTask(skillName,taskID);
    }

    public void abilityDurationTimer(String skillName, long duration, String endMessage, String cooldownEndMessage, NamespacedKey key, ItemStack itemHeldInHand,int enchantLevel, int levelReqLevel) { //OverSpecified for Mining/Digging/Swordsmanship
        this.itemInHand = itemHeldInHand;
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        initializeCooldownTimer(skillName);
        setAbilityDurationTime(skillName,(int) duration);
        int refreshTicks = 2;
        UUID taskUUID = UUID.randomUUID();
        int taskID = new BukkitRunnable() { //The outer task is used for Ability time remaining bars and is accurate to 0.1 seconds on default
            @Override
            public void run() {
                int timeRemaining = getPlayerDurationTime(skillName)-refreshTicks;
                durationBarUpdate(skillName,timeRemaining,duration);
                if (timeRemaining < refreshTicks) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            endAbility(skillName, endMessage, cooldownEndMessage, key, itemHeldInHand, enchantLevel, levelReqLevel,false);
                        }
                    }.runTaskLater(plugin,timeRemaining);
                    killDurationTask(taskUUID); //Kill this task as soon as the new task is scheduled
                }
                else {
                    setAbilityDurationTime(skillName,timeRemaining);
                }
            }
        }.runTaskTimer(plugin,refreshTicks,refreshTicks).getTaskId();
        abilityDurationTaskIds.put(taskUUID,taskID);
        AbilityLogoutTracker incaseLogout = new AbilityLogoutTracker(player);
        incaseLogout.setPlayerItem(skillName,key);
        incaseLogout.setPlayerTask(skillName,taskID);
    }

    public void endAbility(String skillName,String endMessage,String cooldownEndMessage,boolean isDisabling) {
        ActionBarMessages actionMessage = new ActionBarMessages(player);
        AbilityTracker abilities = new AbilityTracker(player);
        if (player.isOnline()) {
            actionMessage.sendMessage(endMessage);
        }
        abilities.setPlayerAbility(skillName, -1);
        durationBarUpdate(skillName,0,1); //The total duration doesn't matter here, the bar should be removed
        if (!isDisabling) {
            cooldownTimer(skillName, cooldownEndMessage);
        }
    }

    public void endAbility(String skillName,String endMessage, String cooldownEndMessage,NamespacedKey key,ItemStack itemHeldInHand, int enchantLevel, int levelReqLevel,boolean isDisabling) {
        ActionBarMessages actionMessage = new ActionBarMessages(player);
        AbilityTracker abilities = new AbilityTracker(player);
        if (player.isOnline()) {
            actionMessage.sendMessage(endMessage);
        }
        abilities.setPlayerAbility(skillName, -1);
        durationBarUpdate(skillName,0,1); //The total duration doesn't matter here, the bar should be removed
        if (skillName.equalsIgnoreCase("digging") || skillName.equalsIgnoreCase("mining")) {
            TrackItem trackItem = new TrackItem();
            ItemStack potentialAbilityItem = trackItem.findTrackedItemInInventory(player,key);
            if (potentialAbilityItem != null) {
                itemHeldInHand = potentialAbilityItem;
            }
            itemHeldInHand.removeEnchantment(Enchantment.DIG_SPEED);
            if (enchantLevel != 0) {
                itemHeldInHand.addUnsafeEnchantment(Enchantment.DIG_SPEED, enchantLevel);
            }
        }
        else if (skillName.equalsIgnoreCase("swordsmanship")) {
            TrackItem trackItem = new TrackItem();
            ItemStack potentialAbilityItem = trackItem.findTrackedItemInInventory(player,key);
            if (potentialAbilityItem != null) {
                itemHeldInHand = potentialAbilityItem;
            }
            ((Attributable) player).getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.0);
            if (levelReqLevel > 0) {
                if (enchantLevel > 0) {
                    itemHeldInHand.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, enchantLevel);
                }
                else {
                    itemHeldInHand.removeEnchantment(Enchantment.DAMAGE_ALL);
                }
            }
        }
        if (!isDisabling) {
            cooldownTimer(skillName, cooldownEndMessage);
        }
    }


    public void killDurationTask(UUID taskID) {
        int taskId = abilityDurationTaskIds.get(taskID);
        Bukkit.getScheduler().cancelTask(taskId);
        abilityDurationTaskIds.remove(taskID);
    }

    public void setAbilityDurationTime(String skillName, int duration) {
        initializeAbilityDurationTimes();
        Map<String,Integer> playerAbilityDurations = abilityDurationTimes.get(uuid);
        playerAbilityDurations.put(skillName,duration);
        abilityDurationTimes.put(uuid,playerAbilityDurations);
    }

    public void initializeAbilityDurationTimes() {
        Map<String,Integer> skillTimes = new ConcurrentHashMap<>();
        abilityDurationTimes.putIfAbsent(uuid,skillTimes);
    }

    public int getPlayerDurationTime(String skillName) {
        if (abilityDurationTimes.containsKey(uuid)) {
            if (abilityDurationTimes.get(uuid).containsKey(skillName)) {
                return abilityDurationTimes.get(uuid).get(skillName);
            }
        }
        return 0;
    }

    public void durationBarUpdate(String skillName,long timeRemaining,long totalDuration) {
        BossBarStorage bossBarStorage = new BossBarStorage();
        PlayerStats playerStats = new PlayerStats(player);
        int numberOfBossBarsAllowed = (int) playerStats.getPlayerData().get("global").get(28);

        if (!player.isOnline()) { //If the player is offline, the bossbar is gone, but we still want the task to run
            return;
        }

        //Get the bossbar object
        BossBar durationBar = getPlayerBossBar(skillName);
        if (durationBar == null) { //If the bossbar hasn't been stored yet, get it from the bossbarstorage class
            int numberOfActiveBars = bossBarStorage.numberOfActiveDurationBars(player);
            if (numberOfBossBarsAllowed <= numberOfActiveBars) { //If there's already too many ability bars running, don't add a new one
                return;
            }
            else {
                durationBar = bossBarStorage.getLowestUnoccupiedBar(player);
                setPlayerBossBar(skillName, durationBar);
            }
        }


        if (timeRemaining <= 0) { //If there's no time left, just remove the boss bar and set it to invisible/unused
            durationBar.setVisible(false);
            removeDurationBar(skillName);
            return;
        }

        //Set the bossbar for player viewing
        LanguageSelector lang = new LanguageSelector(player);
        double totalTime = (double) totalDuration;
        double timeLeft = (double) timeRemaining;
        double percentageTimeRemaining = timeLeft/totalTime;
        double integerMultiple = 0.2;
        double timeRemainingInSeconds = round((timeLeft/20.0),integerMultiple);
        int roundingPlace = 0;
        String integerMultipleString = String.valueOf(integerMultiple);
        if (integerMultipleString.contains(".")) {
            String[] splitter = integerMultipleString.split("\\.");
            roundingPlace = splitter[1].length();
        }
        String formatString = "%."+roundingPlace+"f";
        String timeRemainingInSecondsString = String.format(formatString, timeRemainingInSeconds);
        durationBar.setProgress(percentageTimeRemaining);
        durationBar.setTitle(ChatColor.GRAY + getAbilityName(skillName) + " " + lang.getString("timeRemaining") + ": " + ChatColor.WHITE + timeRemainingInSecondsString + ChatColor.GRAY + "s");
        durationBar.setVisible(true);

    }

    public double round(double num, double integerMultipleOf) {
        return Math.round(num/integerMultipleOf)*integerMultipleOf;
    }

    public String getAbilityName(String skillName) {
        LanguageSelector lang = new LanguageSelector(player);
        switch (skillName) {
            case "archery":
                return lang.getString("rapidFire");
            case "axeMastery":
                return lang.getString("greatAxe");
            case "beastMastery":
                return lang.getString("spurKick");
            case "defense":
                return lang.getString("stoneSoldier");
            case "digging":
                return lang.getString("bigDig");
            case "farming":
                return lang.getString("naturalRegeneration");
            case "fishing":
                return lang.getString("superBait");
            case "mining":
                return lang.getString("berserkPick");
            case "swordsmanship":
                return lang.getString("swiftStrikes");
            case "woodcutting":
                return lang.getString("timber");
            default:
                return "";

        }
    }

    public void initializePlayerBossBars() {
        Map<String,BossBar> skillBossBars = new ConcurrentHashMap<>();
        abilityDurationBossBar.putIfAbsent(uuid,skillBossBars);
    }

    public BossBar getPlayerBossBar(String skillName) {
        if (abilityDurationBossBar.containsKey(uuid)) {
            if (abilityDurationBossBar.get(uuid).containsKey(skillName)) {
                return abilityDurationBossBar.get(uuid).get(skillName);
            }
        }
        return null;
    }
    public void setPlayerBossBar(String skillName, BossBar durationBar) {
        initializePlayerBossBars();
        Map<String,BossBar> playerAbilityDurationBars = abilityDurationBossBar.get(uuid);
        playerAbilityDurationBars.put(skillName,durationBar);
        abilityDurationBossBar.put(uuid,playerAbilityDurationBars);
    }
    public void removeDurationBar(String skillName) {
        if (abilityDurationBossBar.containsKey(uuid)) {
            if (abilityDurationBossBar.get(uuid).containsKey(skillName)) {
                Map<String,BossBar> playerAbilityDurationBars = abilityDurationBossBar.get(uuid);
                playerAbilityDurationBars.remove(skillName);
                abilityDurationBossBar.putIfAbsent(uuid,playerAbilityDurationBars);
            }
        }
    }

}
