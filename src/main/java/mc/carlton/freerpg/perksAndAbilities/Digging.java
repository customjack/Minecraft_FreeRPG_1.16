package mc.carlton.freerpg.perksAndAbilities;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.ActionBarMessages;
import mc.carlton.freerpg.gameTools.BlockFaceTracker;
import mc.carlton.freerpg.gameTools.LanguageSelector;
import mc.carlton.freerpg.gameTools.TrackItem;
import mc.carlton.freerpg.globalVariables.ItemGroups;
import mc.carlton.freerpg.playerInfo.*;
import mc.carlton.freerpg.serverInfo.ConfigLoad;
import mc.carlton.freerpg.serverInfo.WorldGuardChecks;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.time.Instant;
import java.util.*;

public class Digging extends Skill{
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    private Player p;
    private String pName;
    private ItemStack itemInHand;
    private String skillName = "digging";
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

    public Digging(Player p) {
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
        if (!p.hasPermission("freeRPG.diggingAbility")) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        if ((int) pStat.get("global").get(24) < 1 || !pStatClass.isPlayerSkillAbilityOn(skillName)) {
            return;
        }
        Integer[] pTimers = timers.getPlayerCooldownTimes();
        Integer[] pAbilities = abilities.getPlayerAbilities();
        if (pAbilities[0] == -1) {
            int cooldown = pTimers[0];
            if (cooldown < 1) {
                int prepMessages = (int) pStatClass.getPlayerData().get("global").get(22); //Toggle for preparation messages
                if (prepMessages >0) {
                    actionMessage.sendMessage(ChatColor.GRAY + ">>>" + lang.getString("prepare") + " " + lang.getString("shovel") + "...<<<");
                }
                int taskID = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (prepMessages > 0) {
                            actionMessage.sendMessage(ChatColor.GRAY + ">>>..." + lang.getString("rest") + " " +lang.getString("shovel") + "<<<");
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
                actionMessage.sendMessage(ChatColor.RED +lang.getString("bigDig") + " " + lang.getString("cooldown") + ": " + ChatColor.WHITE + cooldown+ ChatColor.RED + "s");
            }
        }
    }

    public void enableAbility() {
        if (!runMethods) {
            return;
        }
        Integer[] pAbilities = abilities.getPlayerAbilities();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        actionMessage.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + ">>>" + lang.getString("bigDig") + " " + lang.getString("activated") + "<<<");
        int effLevel = itemInHand.getEnchantmentLevel(Enchantment.DIG_SPEED);
        itemInHand.removeEnchantment(Enchantment.DIG_SPEED);
        itemInHand.addUnsafeEnchantment(Enchantment.DIG_SPEED,effLevel+5);

        //Mark the item
        long unixTime = Instant.now().getEpochSecond();
        String keyName = p.getUniqueId().toString() + "-frpg-" + skillName + "-" + String.valueOf(unixTime);
        NamespacedKey key = new NamespacedKey(plugin,keyName);
        ItemMeta itemMeta = itemInHand.getItemMeta();
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING,"frpg-digging");
        itemInHand.setItemMeta(itemMeta);

        int durationLevel = (int) pStat.get(skillName).get(4);
        double duration0 = Math.ceil(durationLevel*0.4) + 40;
        long duration = (long) duration0;
        Bukkit.getScheduler().cancelTask(pAbilities[0]);
        abilities.setPlayerAbility( skillName, -2);
        String coolDownEndMessage = ChatColor.GREEN + ">>>" + lang.getString("bigDig") + " " + lang.getString("readyToUse") + "<<<";
        String endMessage = ChatColor.RED + ChatColor.BOLD.toString() + ">>>" + lang.getString("bigDig") + " " + lang.getString("ended") + "<<<";
        timers.abilityDurationTimer(skillName,duration,endMessage,coolDownEndMessage,key,itemInHand,effLevel,0);
    }

    public void storeBlockFace(BlockFace blockFace) {
        if (!runMethods) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int megaDigLevel = (int) pStat.get(skillName).get(13);
        if (megaDigLevel > 0) {
            BlockFaceTracker blockFaceTracker = new BlockFaceTracker();
            blockFaceTracker.addBlockFace(blockFace,p);
        }
    }

    public void megaDig(Block block, Map<Material, Integer> diggingEXP) {
        if (!runMethods) {
            return;
        }
        WorldGuardChecks BuildingCheck = new WorldGuardChecks();
        World world = p.getWorld();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int megaDigLevel = (int) pStat.get(skillName).get(13);
        if (megaDigLevel < 1) {
            return;
        }
        int megaDigToggle = (int) pStat.get("global").get(19);
        if (megaDigToggle < 1) {
            return;
        }
        BlockFaceTracker blockFaceTracker = new BlockFaceTracker();
        BlockFace blockFace = blockFaceTracker.getBlockface(p);
        Vector normalVector = blockFace.getDirection();
        ConfigLoad configLoad = new ConfigLoad();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x==0 && y==0 && z==0) {
                        continue;
                    }
                    Vector relativeVector = new Vector(x, y, z);
                    if (relativeVector.dot(normalVector) == 0) {
                        Block planeBlock = block.getRelative(x, y, z);
                        Material blockType = planeBlock.getType();
                        Location blockLocation = planeBlock.getLocation();
                        if (!BuildingCheck.canBuild(p, blockLocation)) {
                            continue;
                        }
                        if (diggingEXP.containsKey(blockType)) {
                            damageTool();
                            increaseStats.changeEXP(skillName,(int) Math.round(diggingEXP.get(blockType)*configLoad.getSpecialMultiplier().get("megaDigEXPMultiplier")));
                            Collection<ItemStack> drops = planeBlock.getDrops(itemInHand);
                            for (ItemStack stack : drops) {
                                dropItemNaturally(blockLocation, stack);
                            }
                            diggingTreasureDrop(world,blockLocation,blockType);
                            planeBlock.setType(Material.AIR);
                        }
                    }
                }
            }
        }
    }

    public void damageTool() {
        ItemMeta toolMeta = itemInHand.getItemMeta();
        if (toolMeta instanceof Damageable) {
            ((Damageable) toolMeta).setDamage(((Damageable) toolMeta).getDamage()+1);
            itemInHand.setItemMeta(toolMeta);
            if (((Damageable) toolMeta).getDamage() > itemInHand.getType().getMaxDurability()) {
                itemInHand.setAmount(0);
                p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, 1);
            }
        }
    }

    public void diggingTreasureDrop(World world, Location loc,Material blockType) {
        if (!runMethods) {
            return;
        }
        ConfigLoad loadConfig = new ConfigLoad();
        ArrayList<Object> treasureData = loadConfig.getDiggingInfo();
        double randomNum = rand.nextDouble();
        double randomNum2 = 0;
        Integer[] pAbilities = abilities.getPlayerAbilities();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int treasureRoll = (int) pStat.get(skillName).get(5);
        int soulStealing = (int) pStat.get(skillName).get(10);
        double treasureChance = treasureRoll * 0.00005 + 0.01;
        if (soulStealing > 0 && blockType == Material.SOUL_SAND) {
            treasureChance = treasureChance*(1+0.05*soulStealing);
        }
        if (treasureChance < randomNum) {
            return;
        }

        int rolls = 1;
        int megaDig = (int) pStat.get(skillName).get(13);
        int doubleTreasure = (int) pStat.get(skillName).get(8);
        int dropLevel_I = (int) pStat.get(skillName).get(7);
        int dropLevel_II = (int) pStat.get(skillName).get(9);
        double[] rates = {0, 0, 0, 0, 0, 0, 0, 0, 0, (double)treasureData.get(20), (double)treasureData.get(23),
                          (double)treasureData.get(26), (double)treasureData.get(29), (double)treasureData.get(32), (double)treasureData.get(35)};

        if (megaDig > 0 && pAbilities[0] == -2) {
            for (int i = 0; i < rates.length; i++) {
                rates[i] = rates[i] * 0.2;
            }
        }

        /* Old Treasure Magnet Perk, Replaced with Mega Dig
        int treasureMagnet = (int) pStat.get(skillName).get(13);
        if (treasureMagnet > 0 && pAbilities[0] == -2) {
            for (int i = 0; i < rates.length; i++) {
                rates[i] = rates[i] * 1.5;
            }
        }

         */
        //Set unobtained drops to 0
        switch (dropLevel_II) {
            case 0:
                rates[14] = 0;
                rates[13] = 0;
                rates[12] = 0;
                rates[11] = 0;
                rates[10] = 0;
                break;
            case 1:
                rates[14] = 0;
                rates[13] = 0;
                rates[12] = 0;
                rates[11] = 0;
                break;
            case 2:
                rates[14] = 0;
                rates[13] = 0;
                rates[12] = 0;
                break;
            case 3:
                rates[14] = 0;
                rates[13] = 0;
                break;
            case 4:
                rates[14] = 0;
                break;
            default:
                break;
        }
        if (dropLevel_I < 5) {
            rates[9] = 0;
        }

        //Get total static drop chances, used to determine other drop chances
        double total = 0.0;
        for (int i = 0; i < rates.length; i++) {
            total += rates[i];
        }
        double remaining = 1 - total;

        //distributes remaining possible drops chance such that the sum is 1
        double dropLevel_1_double = dropLevel_I;
        for (int i = 0; i < 5; i++) {
            rates[i] = (4.0 / 5.0) * (remaining / 5.0);
        }
        if (dropLevel_I > 3) {
            for (int i = 0; i < 4; i++) {
                rates[i + 5] = (1.0 / 5.0) * (remaining / 4.0);
            }
        } else {
            for (int i = 0; i < dropLevel_I; i++) {
                rates[i + 5] = (1.0 / 5.0) * (remaining / dropLevel_1_double);
            }
        }

        //Creates list of running sums, highest value should be 1, duplicates occur when an item is impossible and should be sequential.
        double[] rateSums = {rates[0], 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        for (int i = 1; i < rates.length; i++) {
            rateSums[i] = rateSums[i - 1] + rates[i];
        }

        Material[] music_discs = {Material.MUSIC_DISC_11, Material.MUSIC_DISC_13, Material.MUSIC_DISC_BLOCKS, Material.MUSIC_DISC_CAT,
                Material.MUSIC_DISC_CHIRP, Material.MUSIC_DISC_FAR, Material.MUSIC_DISC_MALL, Material.MUSIC_DISC_MELLOHI,
                Material.MUSIC_DISC_STAL, Material.MUSIC_DISC_STRAD, Material.MUSIC_DISC_WAIT, Material.MUSIC_DISC_WARD};
        Material[] horse_armor = {Material.LEATHER_HORSE_ARMOR, Material.IRON_HORSE_ARMOR, Material.GOLDEN_HORSE_ARMOR, Material.DIAMOND_HORSE_ARMOR};

        for (int i = 0; i < rolls; i++) {
            //Double drop roll
            double doubleTreasureChance = doubleTreasure * 0.05;
            randomNum = rand.nextDouble();
            randomNum2 = rand.nextDouble();

            boolean doubleDrop = false;
            if (doubleTreasureChance > randomNum2) {
                doubleDrop = true;
            }

            randomNum = rand.nextDouble();
            if (randomNum < rateSums[0]) {
                ItemStack drop = new ItemStack(Material.DIRT,1);
                if (treasureData.get(0) == null) {
                    drop.setType(Material.GLOWSTONE_DUST);
                }
                else {
                    drop.setType((Material)treasureData.get(0));
                    drop.setAmount((int) treasureData.get(1));
                }
                world.dropItemNaturally(loc, drop);
                increaseStats.changeEXP(skillName, expMap.get("drop1EXP"));
                if (doubleDrop) {
                    world.dropItemNaturally(loc, drop);
                }
            } else if (randomNum < rateSums[1]) {
                ItemStack drop = new ItemStack(Material.DIRT,1);
                if (treasureData.get(2) == null) {
                    drop.setType(Material.GOLD_NUGGET);
                }
                else {
                    drop.setType((Material)treasureData.get(2));
                    drop.setAmount((int) treasureData.get(3));
                }
                world.dropItemNaturally(loc, drop);
                increaseStats.changeEXP(skillName, expMap.get("drop2EXP"));
                if (doubleDrop) {
                    world.dropItemNaturally(loc, drop);
                }
            } else if (randomNum < rateSums[2]) {
                ItemStack drop = new ItemStack(Material.DIRT,1);
                if (treasureData.get(4) == null) {
                    drop.setType(Material.STRING);
                }
                else {
                    drop.setType((Material)treasureData.get(4));
                    drop.setAmount((int) treasureData.get(5));
                }
                world.dropItemNaturally(loc, drop);
                increaseStats.changeEXP(skillName, expMap.get("drop3EXP"));
                if (doubleDrop) {
                    world.dropItemNaturally(loc, drop);
                }
            } else if (randomNum < rateSums[3]) {
                ItemStack drop = new ItemStack(Material.DIRT,1);
                if (treasureData.get(6) == null) {
                    drop.setType(Material.IRON_NUGGET);
                }
                else {
                    drop.setType((Material)treasureData.get(6));
                    drop.setAmount((int) treasureData.get(7));
                }
                world.dropItemNaturally(loc, drop);
                increaseStats.changeEXP(skillName, expMap.get("drop4EXP"));
                if (doubleDrop) {
                    world.dropItemNaturally(loc, drop);
                }
            } else if (randomNum < rateSums[4]) {
                ItemStack drop = new ItemStack(Material.DIRT,1);
                if (treasureData.get(8) == null) {
                    drop.setType(Material.COBWEB);
                }
                else {
                    drop.setType((Material)treasureData.get(8));
                    drop.setAmount((int) treasureData.get(9));
                }
                world.dropItemNaturally(loc, drop);
                increaseStats.changeEXP(skillName, expMap.get("drop5EXP"));
                if (doubleDrop) {
                    world.dropItemNaturally(loc, drop);
                }
            } else if (randomNum < rateSums[5] && dropLevel_I >= 1) {
                ItemStack drop = new ItemStack(Material.DIRT,1);
                if (treasureData.get(10) == null) {
                    drop.setType(Material.GOLD_INGOT);
                }
                else {
                    drop.setType((Material)treasureData.get(10));
                    drop.setAmount((int) treasureData.get(11));
                }
                world.dropItemNaturally(loc, drop);
                increaseStats.changeEXP(skillName, expMap.get("drop6EXP"));
                if (doubleDrop) {
                    world.dropItemNaturally(loc, drop);
                }
            } else if (randomNum < rateSums[6] && dropLevel_I >= 2) {
                ItemStack drop = new ItemStack(Material.DIRT,1);
                if (treasureData.get(12) == null) {
                    drop.setType(Material.NAME_TAG);
                }
                else {
                    drop.setType((Material)treasureData.get(12));
                    drop.setAmount((int) treasureData.get(13));
                }
                world.dropItemNaturally(loc, drop);
                increaseStats.changeEXP(skillName, expMap.get("drop7EXP"));
                if (doubleDrop) {
                    world.dropItemNaturally(loc, drop);
                }
            } else if (randomNum < rateSums[7] && dropLevel_I >= 3) {
                ItemStack drop = new ItemStack(Material.DIRT,1);
                if (treasureData.get(14) == null) {
                    drop.setType(music_discs[rand.nextInt(music_discs.length)]);
                }
                else {
                    drop.setType((Material)treasureData.get(14));
                    drop.setAmount((int) treasureData.get(15));
                }
                world.dropItemNaturally(loc, drop);
                increaseStats.changeEXP(skillName, expMap.get("drop8EXP"));
                if (doubleDrop) {
                    world.dropItemNaturally(loc, drop);
                }
            } else if (randomNum < rateSums[8] && dropLevel_I >= 4) {
                ItemStack drop = new ItemStack(Material.DIRT,1);
                if (treasureData.get(16) == null) {
                    drop.setType(horse_armor[rand.nextInt(horse_armor.length)]);
                }
                else {
                    drop.setType((Material)treasureData.get(16));
                    drop.setAmount((int) treasureData.get(17));
                }
                world.dropItemNaturally(loc, drop);
                increaseStats.changeEXP(skillName, expMap.get("drop9EXP"));
                if (doubleDrop) {
                    world.dropItemNaturally(loc, drop);
                }
            } else if (randomNum < rateSums[9] && dropLevel_I >= 5) {
                ItemStack drop = new ItemStack(Material.DIRT,1);
                if (treasureData.get(18) == null) {
                    drop.setType(Material.DIAMOND);
                }
                else {
                    drop.setType((Material)treasureData.get(18));
                    drop.setAmount((int) treasureData.get(19));
                }
                world.dropItemNaturally(loc, drop);
                increaseStats.changeEXP(skillName, expMap.get("drop10EXP"));
                if (doubleDrop) {
                    world.dropItemNaturally(loc, drop);
                }
            } else if (randomNum < rateSums[10] && dropLevel_II >= 1) {
                ItemStack drop = new ItemStack(Material.DIRT,1);
                if (treasureData.get(21) == null) {
                    drop.setType(Material.EMERALD);
                }
                else {
                    drop.setType((Material)treasureData.get(21));
                    drop.setAmount((int) treasureData.get(22));
                }
                world.dropItemNaturally(loc, drop);
                increaseStats.changeEXP(skillName, expMap.get("drop11EXP"));
                if (doubleDrop) {
                    world.dropItemNaturally(loc, drop);
                }
            } else if (randomNum < rateSums[11] && dropLevel_II >= 2) {
                ItemStack drop = new ItemStack(Material.DIRT,1);
                if (treasureData.get(24) == null) {
                    ItemGroups itemGroups = new ItemGroups();
                    Map<Enchantment, Integer> enchantmentLevelMap = itemGroups.getEnchantmentLevelMap();
                    drop.setType(Material.ENCHANTED_BOOK);
                    List<Enchantment> keysAsArray = new ArrayList<Enchantment>(enchantmentLevelMap.keySet());
                    Enchantment randomEnchant = keysAsArray.get(rand.nextInt(keysAsArray.size()));
                    int randomLevel = rand.nextInt(enchantmentLevelMap.get(randomEnchant)) + 1;
                    EnchantmentStorageMeta meta = ((EnchantmentStorageMeta) drop.getItemMeta());
                    meta.addStoredEnchant(randomEnchant,randomLevel,true);
                    drop.setItemMeta(meta);
                    if (randomEnchant == Enchantment.BINDING_CURSE || randomEnchant == Enchantment.VANISHING_CURSE) {
                        Map<Enchantment, Integer> enchantmentLevelMapGood = enchantmentLevelMap;
                        enchantmentLevelMapGood.remove(Enchantment.BINDING_CURSE);
                        enchantmentLevelMapGood.remove(Enchantment.VANISHING_CURSE);
                        keysAsArray = new ArrayList<Enchantment>(enchantmentLevelMapGood.keySet());
                        randomEnchant = keysAsArray.get(rand.nextInt(keysAsArray.size()));
                        meta.addStoredEnchant(randomEnchant,randomLevel,true);
                        drop.setItemMeta(meta);
                    }
                }
                else {
                    drop.setType((Material)treasureData.get(24));
                    drop.setAmount((int) treasureData.get(25));
                }
                world.dropItemNaturally(loc, drop);
                increaseStats.changeEXP(skillName, expMap.get("drop12EXP"));
                if (doubleDrop) {
                    world.dropItemNaturally(loc, drop);
                }
            } else if (randomNum < rateSums[12] && dropLevel_II >= 3) {
                ItemStack drop = new ItemStack(Material.DIRT,1);
                if (treasureData.get(27) == null) {
                    drop.setType(Material.DRAGON_BREATH);
                }
                else {
                    drop.setType((Material)treasureData.get(27));
                    drop.setAmount((int) treasureData.get(28));
                }
                world.dropItemNaturally(loc, drop);
                increaseStats.changeEXP(skillName, expMap.get("drop13EXP"));
                if (doubleDrop) {
                    world.dropItemNaturally(loc, drop);
                }
            } else if (randomNum < rateSums[13] && dropLevel_II >= 4) {
                ItemStack drop = new ItemStack(Material.DIRT,1);
                if (treasureData.get(30) == null) {
                    drop.setType(Material.TOTEM_OF_UNDYING);
                }
                else {
                    drop.setType((Material)treasureData.get(30));
                    drop.setAmount((int) treasureData.get(31));
                }
                world.dropItemNaturally(loc, drop);
                increaseStats.changeEXP(skillName, expMap.get("drop14EXP"));
                if (doubleDrop) {
                    world.dropItemNaturally(loc, drop);
                }
            } else if (randomNum < rateSums[14] && dropLevel_II >= 5) {
                ItemStack drop = new ItemStack(Material.DIRT,1);
                if (treasureData.get(33) == null) {
                    drop.setType(Material.NETHER_STAR);
                }
                else {
                    drop.setType((Material)treasureData.get(33));
                    drop.setAmount((int) treasureData.get(34));
                }
                world.dropItemNaturally(loc, drop);
                increaseStats.changeEXP(skillName, expMap.get("drop15EXP"));
                if (doubleDrop) {
                    world.dropItemNaturally(loc, drop);
                }
            }
        }
    }

    public boolean flintFinder(Material blockType) {
        if (!runMethods) {
            return false;
        }
        if (blockType == Material.GRAVEL) {
            Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
            int flintFinderLevel = (int) pStat.get(skillName).get(11);
            int flintFinderToggle = (int) pStat.get("global").get(12);
            if (flintFinderLevel > 0 && flintFinderToggle > 0) {
                return true;
            }
        }
        return false;
    }

    public void preventLogoutTheft(int taskID_digging, ItemStack itemInHand_digging,NamespacedKey key,boolean pluginDisabled){
        if (!runMethods) {
            return;
        }
        Integer[] pAbilities = abilities.getPlayerAbilities();
        if (pAbilities[0] == -2) {
            Bukkit.getScheduler().cancelTask(taskID_digging);
            int effLevel = itemInHand_digging.getEnchantmentLevel(Enchantment.DIG_SPEED)-5;
            String endMessage = ChatColor.RED+ChatColor.BOLD.toString() + ">>>" + lang.getString("magicForce") + "<<<";
            String coolDownEndMessage = ChatColor.GREEN + ">>>" + lang.getString("bigDig") + " " + lang.getString("readyToUse") + "<<<";
            timers.endAbility(skillName,endMessage,coolDownEndMessage,key,itemInHand_digging,effLevel,0,pluginDisabled);
            TrackItem trackItem = new TrackItem();
            trackItem.removeItemKey(itemInHand_digging,key);
        }
    }
}
