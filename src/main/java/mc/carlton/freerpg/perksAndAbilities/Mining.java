package mc.carlton.freerpg.perksAndAbilities;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.ActionBarMessages;
import mc.carlton.freerpg.gameTools.LanguageSelector;
import mc.carlton.freerpg.gameTools.TrackItem;
import mc.carlton.freerpg.globalVariables.ItemGroups;
import mc.carlton.freerpg.playerAndServerInfo.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.time.Instant;
import java.util.*;

public class Mining {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    private Player p;
    private String pName;
    private ItemStack itemInHand;
    private String skillName = "mining";
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
    ArrayList<Block> veinOres = new ArrayList<Block>();

    private boolean runMethods;

    public Mining(Player p) {
        this.p = p;
        this.pName = p.getDisplayName();
        this.itemInHand = p.getInventory().getItemInMainHand();
        this.increaseStats = new ChangeStats(p);
        this.abilities = new AbilityTracker(p);
        this.timers = new AbilityTimers(p);
        this.pStatClass=  new PlayerStats(p);
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
        if (!p.hasPermission("freeRPG.miningAbility")) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        if ((int) pStat.get("global").get(24) < 1 || !pStatClass.isPlayerSkillAbilityOn(skillName)) {
            return;
        }
        Integer[] pTimers = timers.getPlayerTimers();
        Integer[] pAbilities = abilities.getPlayerAbilities();
        if (pAbilities[2] == -1) {
            int cooldown = pTimers[2];
            if (cooldown < 1) {
                int prepMessages = (int) pStatClass.getPlayerData().get("global").get(22); //Toggle for preparation messages
                if (prepMessages > 0) {
                    actionMessage.sendMessage(ChatColor.GRAY + ">>>" + lang.getString("prepare") + " " + lang.getString("pickaxe") + "...<<<");
                }
                int taskID = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (prepMessages > 0) {
                            actionMessage.sendMessage(ChatColor.GRAY + ">>>..." + lang.getString("rest") + " " +lang.getString("pickaxe") + "<<<");
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
                actionMessage.sendMessage(ChatColor.RED +lang.getString("berserkPick") + " " + lang.getString("cooldown") + ": " + cooldown + "s");
            }
        }
    }

    public void enableAbility() {
        if (!runMethods) {
            return;
        }
        Integer[] pAbilities = abilities.getPlayerAbilities();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        actionMessage.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + ">>>" + lang.getString("berserkPick") + " " + lang.getString("activated") + "<<<");
        int effLevel = itemInHand.getEnchantmentLevel(Enchantment.DIG_SPEED);
        itemInHand.removeEnchantment(Enchantment.DIG_SPEED);
        itemInHand.addUnsafeEnchantment(Enchantment.DIG_SPEED, effLevel + 5);

        //Mark the item
        long unixTime = Instant.now().getEpochSecond();
        String keyName = p.getUniqueId().toString() + "-" + String.valueOf(unixTime) + "-" + skillName;
        NamespacedKey key = new NamespacedKey(plugin,keyName);
        ItemMeta itemMeta = itemInHand.getItemMeta();
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING,"nothing");
        itemInHand.setItemMeta(itemMeta);


        //TrackItem trackPickaxe = new TrackItem(itemInHand,ChatColor.RED);
        int durationLevel = (int) pStat.get(skillName).get(4);
        double duration0 = Math.ceil(durationLevel * 0.4) + 40;
        int cooldown = 300;
        if ((int) pStat.get("global").get(11) > 0) {
            cooldown = 200;
        }
        int finalCooldown = cooldown;
        long duration = (long) duration0;
        timers.setPlayerTimer( skillName, finalCooldown);
        Bukkit.getScheduler().cancelTask(pAbilities[2]);
        abilities.setPlayerAbility( skillName, -2);
        int taskID = new BukkitRunnable() {
            @Override
            public void run() {
                TrackItem trackItem = new TrackItem();
                ItemStack potentialAbilityItem = trackItem.findTrackedItemInInventory(p,key);
                if (potentialAbilityItem != null) {
                    itemInHand = potentialAbilityItem;
                }
                actionMessage.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + ">>>" + lang.getString("berserkPick") + " " + lang.getString("ended") + "<<<");
                itemInHand.removeEnchantment(Enchantment.DIG_SPEED);
                if (effLevel != 0) {
                    itemInHand.addUnsafeEnchantment(Enchantment.DIG_SPEED, effLevel);
                }
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
                                    actionMessage.sendMessage(ChatColor.GREEN + ">>>" + lang.getString("berserkPick") + " " + lang.getString("readyToUse") + "<<<");
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

    public void tntExplode(Block blockLit) {
        if (!runMethods) {
            return;
        }
        WorldGuardChecks BuildingCheck = new WorldGuardChecks();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        blockLit.setType(Material.AIR);
        World world = p.getWorld();
        TNTPrimed tnt = world.spawn(blockLit.getLocation().add(0, 0.25, 0), TNTPrimed.class);
        int blastRadiusLevel = (int) pStat.get(skillName).get(10);
        double power0 = 4 + blastRadiusLevel * 0.5;
        float power = (float) power0;
        tnt.setFuseTicks(41);
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                Location tntLoc = tnt.getLocation();
                int x0 = tntLoc.getBlockX();
                int y0 = tntLoc.getBlockY();
                int z0 = tntLoc.getBlockZ();
                boolean causeExplosion = true;
                outerLoop:
                for (int x = -5; x < 6; x++) {
                    for (int y = -5; y < 6; y++) {
                        for (int z = -5; z < 6; z++) {
                            Location newLoc = new Location(world,x+x0,y+y0,z+z0);
                            boolean canExplode = BuildingCheck.canExplode(p,newLoc);
                            boolean canBuild = BuildingCheck.canBuild(p,newLoc);
                            if (!canBuild || !canExplode) {
                                causeExplosion = false;
                                break outerLoop;
                            }
                        }
                    }
                }

                if (causeExplosion) {
                    Map<org.bukkit.util.Vector,Block> location_block = new HashMap<>();
                    Map<org.bukkit.util.Vector,Material> location_blockType = new HashMap<>();
                    Block center = world.getBlockAt(tntLoc);
                    for (int x = -2; x < 3; x++) {
                        for (int y = -2; y < 3; y++) {
                            for (int z = -2; z < 3; z++) {
                                location_block.put(new org.bukkit.util.Vector(x,y,z),center.getRelative(x,y,z));
                                location_blockType.put(new org.bukkit.util.Vector(x,y,z),center.getRelative(x,y,z).getType());
                            }
                        }
                    }
                    tnt.getWorld().createExplosion(tntLoc, power, false, true);
                    tnt.remove();
                    //Check blocks around the tnt, see if they even will explode
                    Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                        @Override
                        public void run() {
                            int changedBlocks = 0;
                            for (Vector vector : location_block.keySet()) {
                                if (location_block.get(vector).getType() != location_blockType.get(vector)) {
                                    changedBlocks+=1;
                                }
                            }
                            if (changedBlocks > 3) {
                                increaseStats.changeEXP(skillName, expMap.get("explodeTNT"));
                                int passive3_mining = (int) pStat.get(skillName).get(6);
                                double explosionDrop = passive3_mining * 0.0001;
                                for (int i = 0; i < (int) Math.floor(changedBlocks/2.0); i++) {
                                    Mining miningClass = new Mining(p);
                                    miningClass.miningTreasureDrop(explosionDrop, world, tntLoc);
                                }
                            }
                        }
                    }, 2L);

                }
            }
        }, 40L);
    }

    public void miningTreasureDrop(double treasureChance, World world, Location loc) {
        if (!runMethods) {
            return;
        }
        double randomNum = rand.nextDouble();
        if (treasureChance > randomNum) {
            double randomNum2 = rand.nextDouble();
            if (randomNum2 < 0.5) {
                world.dropItemNaturally(loc, new ItemStack(Material.COAL,1));
                increaseStats.changeEXP(skillName,expMap.get("breakCoal_Ore"));
            }
            else if (randomNum2 < 0.7) {
                world.dropItemNaturally(loc, new ItemStack(Material.IRON_ORE,1));
                increaseStats.changeEXP(skillName,expMap.get("breakIron_Ore"));
            }
            else if (randomNum2 < 0.8) {
                world.dropItemNaturally(loc, new ItemStack(Material.GOLD_ORE,1));
                increaseStats.changeEXP(skillName,expMap.get("breakGold_Ore"));
            }
            else if (randomNum2 < 0.85) {
                world.dropItemNaturally(loc, new ItemStack(Material.LAPIS_LAZULI,1));
                increaseStats.changeEXP(skillName,expMap.get("breakLapis_Ore"));
            }
            else if (randomNum2 < 0.90) {
                world.dropItemNaturally(loc, new ItemStack(Material.EMERALD,1));
                increaseStats.changeEXP(skillName,expMap.get("breakEmerald_Ore"));
            }
            else if (randomNum2 < 0.99) {
                world.dropItemNaturally(loc, new ItemStack(Material.REDSTONE,1));
                increaseStats.changeEXP(skillName,expMap.get("breakRedstone_Ore"));
            }
            else if (randomNum2 < 0.999){
                world.dropItemNaturally(loc, new ItemStack(Material.DIAMOND,1));
                increaseStats.changeEXP(skillName,expMap.get("breakDiamond_Ore"));
            }
            else {
                world.dropItemNaturally(loc, new ItemStack(Material.NETHERITE_SCRAP,1));
                increaseStats.changeEXP(skillName,expMap.get("breakAncient_Debris"));
            }
        }
    }

    public void wastelessHaste() {
        if (!runMethods) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int hasteSeconds = 0;
        int wastelessHasteLevel = (int) pStat.get(skillName).get(7);
        switch(wastelessHasteLevel) {
            case 1:
                hasteSeconds = 2;
                break;
            case 2:
                hasteSeconds = 5;
                break;
            case 3:
            case 4:
            case 5:
                hasteSeconds = 10;
                break;
            default:
                break;
        }
        if (p.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
            if (p.getPotionEffect(PotionEffectType.FAST_DIGGING).getDuration() > 20*hasteSeconds + 20) {
                return;
        }
    }
        if (hasteSeconds > 1) {
            switch (wastelessHasteLevel) {
                case 4: //Half of haste is haste 2, second half is haste 1
                    p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 10 * hasteSeconds, 1));
                    int finalHasteSeconds = hasteSeconds;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (p.isOnline()) {
                                p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 10 * finalHasteSeconds, 0));
                            }
                        }
                    }.runTaskLater(plugin, 10 * hasteSeconds+2);
                    break;
                case 5: //All of buff is haste 2
                    p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20 * hasteSeconds, 1));
                    break;
                default: //All of buff is haste 1
                    p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20 * hasteSeconds, 0));
                    break;
            }
        }
    }

    public void miningDoubleDrop(Block block, World world) {
        if (!runMethods) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();

        int doubleDropLevel = (int) pStat.get(skillName).get(5);
        double chance = 0.0005*doubleDropLevel;
        double randomNum = rand.nextDouble();
        if (chance > randomNum) {
            for (ItemStack stack : block.getDrops(itemInHand)) {
                world.dropItemNaturally(block.getLocation(), stack);
                if ((int) pStat.get(skillName).get(13) > 0) {
                    world.dropItemNaturally(block.getLocation(), stack);
                }
            }
        }
    }

    public void preventLogoutTheft(int taskID_mining,ItemStack itemInHand_mining) {
        if (!runMethods) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        Integer[] pAbilities = abilities.getPlayerAbilities();
        if (pAbilities[2] == -2) {
            Bukkit.getScheduler().cancelTask(taskID_mining);
            int effLevel = itemInHand_mining.getEnchantmentLevel(Enchantment.DIG_SPEED)-5;
            itemInHand_mining.removeEnchantment(Enchantment.DIG_SPEED);
            if (effLevel != 0) {
                itemInHand_mining.addUnsafeEnchantment(Enchantment.DIG_SPEED, effLevel);
            }
            int cooldown = 300;
            if ((int) pStat.get("global").get(11) > 0) {
                cooldown = 200;
            }
            int finalCooldown = cooldown;
            abilities.setPlayerAbility( skillName, -1);
            p.sendMessage(ChatColor.RED+ChatColor.BOLD.toString() + ">>>" + lang.getString("magicForce") + "<<<");
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

    public void getVeinOres(Block b1,final int x1, final int y1, final int z1,Material oreType) {
        if (!runMethods) {
            return;
        }
        WorldGuardChecks BuildingCheck = new WorldGuardChecks();
        int searchCubeSize = 7;
        if (veinOres.size() > 29) {
            return;
        }
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }
                    Block b2 = b1.getRelative(x, y, z);
                    if (b2.getType().equals(oreType)) {
                        if (b2.getX() > x1 + searchCubeSize || b2.getX() < x1 - searchCubeSize || b2.getY() > y1 + searchCubeSize || b2.getY() < y1 - searchCubeSize || b2.getZ() > z1 + searchCubeSize || b2.getZ() < z1 - searchCubeSize) {
                            break;
                        }
                        else if (!(veinOres.contains(b2))) {
                            if (veinOres.size() > 29) {
                                return;
                            }
                            if (BuildingCheck.canBuild(p, b2.getLocation())) {
                                veinOres.add(b2);
                                this.getVeinOres(b2, x1, y1, z1,oreType);
                            }
                        }
                    }
                }
            }
        }
    }

    public void veinMiner(Block initialBlock,Material blockType) {
        if (!runMethods) {
            return;
        }
        ItemGroups itemGroups = new ItemGroups();
        List<Material> ores = itemGroups.getOres();
        if (!(ores.contains(blockType))) {
            return;
        }
        World world = initialBlock.getWorld();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int veinMinerLevel = (int)pStat.get(skillName).get(11);
        if (veinMinerLevel < 1) {
            return;
        }
        int veinMinerToggle = (int) pStat.get("global").get(18);
        if (veinMinerToggle < 1) {
            return;
        }
        getVeinOres(initialBlock,initialBlock.getX(),initialBlock.getY(),initialBlock.getZ(),initialBlock.getType()); //Get Ores in Vein
        int numOres = veinOres.size();
        int totalOres = numOres;
        int doubleDropsLevel = (int)pStat.get(skillName).get(5);
        double chance = 0.0005*doubleDropsLevel;

        ItemMeta toolMeta = itemInHand.getItemMeta();
        if (toolMeta instanceof Damageable) {
            ((Damageable) toolMeta).setDamage(((Damageable) toolMeta).getDamage()+numOres);
            itemInHand.setItemMeta(toolMeta);
            if (((Damageable) toolMeta).getDamage() > itemInHand.getType().getMaxDurability()) {
                itemInHand.setAmount(0);
                p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, 1);
            }
        }
        for (Block block : veinOres) {
            Location blockLoc = block.getLocation();
            //Checks if any of the blocks weren't natural
            PlacedBlocksManager placedBlocksManager = new PlacedBlocksManager();
            boolean natural = !placedBlocksManager.isBlockTracked(block);
            if (!natural) {
                placedBlocksManager.removeBlock(block);
                numOres += 1;
            }
            //Flame Pick Conditional
            if ((int) pStat.get("global").get(13) > 0 && (int) pStat.get("smelting").get(13) > 0 && (block.getType() == Material.IRON_ORE || block.getType() == Material.GOLD_ORE)) {
                int doubleSmeltLevel = (int) pStat.get("smelting").get(9);
                double chanceSmelt = doubleSmeltLevel*0.05;
                int dropAmount = 1;
                if (chanceSmelt > rand.nextDouble()) {
                    dropAmount *= 2;
                }
                world.spawnParticle(Particle.FLAME, block.getLocation(), 5);
                switch (block.getType()) {
                    case IRON_ORE:
                        block.setType(Material.AIR);
                        if (chance > rand.nextDouble() && natural) {
                            if ((int) pStat.get(skillName).get(13) > 0) {
                                dropAmount *= 3;
                            } else {
                                dropAmount *= 2;
                            }
                        }
                        world.dropItemNaturally(block.getLocation(), new ItemStack(Material.IRON_INGOT, dropAmount));
                        damageTool();
                        if (0.7 > rand.nextDouble()) {
                            ((ExperienceOrb) world.spawn(block.getLocation(), ExperienceOrb.class)).setExperience(1);
                        }
                        break;
                    case GOLD_ORE:
                        block.setType(Material.AIR);
                        if (chance > rand.nextDouble() && natural) {
                            if ((int) pStat.get(skillName).get(13) > 0) {
                                dropAmount *= 3;
                            } else {
                                dropAmount *= 2;
                            }
                        }
                        world.dropItemNaturally(block.getLocation(), new ItemStack(Material.GOLD_INGOT, dropAmount));
                        damageTool();
                        ((ExperienceOrb) world.spawn(block.getLocation(), ExperienceOrb.class)).setExperience(1);
                        break;
                    default:
                        break;
                }
            }
            //Not Flame Pick
            else {
                Collection<ItemStack> drops = block.getDrops(itemInHand);
                block.setType(Material.AIR);
                for (ItemStack drop : drops) {
                    world.dropItemNaturally(blockLoc, drop);
                }
                if (chance > rand.nextDouble() && natural) {
                    for (ItemStack drop : drops) {
                        world.dropItemNaturally(blockLoc, drop);
                        if ((int) pStat.get(skillName).get(13) > 0) {
                            world.dropItemNaturally(blockLoc, drop);
                        }
                    }
                }
            }

        }
        //Give EXP
        if ((int) pStat.get("global").get(13) > 0 && (int) pStat.get("smelting").get(13) > 0) {
            ConfigLoad configLoad1 = new ConfigLoad();
            Map<String,Integer> smeltingEXPmap = configLoad1.getExpMapForSkill("smelting");
            if (initialBlock.getType() == Material.IRON_ORE) {
                increaseStats.changeEXP("smelting",smeltingEXPmap.get("smeltIronIngot")*totalOres);
            }
            else if (initialBlock.getType() == Material.GOLD_ORE) {
                increaseStats.changeEXP("smelting",smeltingEXPmap.get("smeltGoldIngot")*totalOres);
            }
        }
        increaseStats.changeEXP(skillName,getEXP(blockType)*numOres);
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
    public int getEXP(Material brokenOre) {
        if (!runMethods) {
            return 0;
        }
        int EXP = 0;
        switch (brokenOre) {
            case COAL_ORE:
                EXP = expMap.get("breakCoal_Ore");
                break;
            case NETHER_QUARTZ_ORE:
                EXP = expMap.get("breakNether_Quartz_Ore");
                break;
            case IRON_ORE:
                EXP = expMap.get("breakIron_Ore");
                break;
            case GOLD_ORE:
                EXP = expMap.get("breakGold_Ore");
                break;
            case EMERALD_ORE:
                EXP = expMap.get("breakEmerald_Ore");
                break;
            case REDSTONE_ORE:
                EXP = expMap.get("breakRedstone_Ore");
                break;
            case LAPIS_ORE:
                EXP = expMap.get("breakLapis_Ore");
                break;
            case DIAMOND_ORE:
                EXP = expMap.get("breakDiamond_Ore");
                break;
            case ANCIENT_DEBRIS:
                EXP = expMap.get("breakAncient_Debris");
                break;
            case GILDED_BLACKSTONE:
                EXP = expMap.get("breakGilded_Blackstone");
                break;
            case NETHER_GOLD_ORE:
                EXP = expMap.get("breakNether_Gold_Ore");
                break;
            default:
                break;
        }
        return EXP;
    }
}
