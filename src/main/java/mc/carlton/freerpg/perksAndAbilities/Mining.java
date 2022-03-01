package mc.carlton.freerpg.perksAndAbilities;

import mc.carlton.freerpg.gameTools.TrackItem;
import mc.carlton.freerpg.globalVariables.ItemGroups;
import mc.carlton.freerpg.configStorage.ConfigLoad;
import mc.carlton.freerpg.serverInfo.MinecraftVersion;
import mc.carlton.freerpg.serverInfo.PlacedBlocksManager;
import mc.carlton.freerpg.serverInfo.WorldGuardChecks;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.time.Instant;
import java.util.*;

public class Mining extends Skill{
    private String skillName = "mining";

    Random rand = new Random(); //Random class Import
    ArrayList<Block> veinOres = new ArrayList<Block>();

    private boolean runMethods;

    public Mining(Player p) {
        super(p);
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
        Integer[] pTimers = timers.getPlayerCooldownTimes();
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
                actionMessage.sendMessage(ChatColor.RED +lang.getString("berserkPick") + " " + lang.getString("cooldown") + ": " + ChatColor.WHITE + cooldown+ ChatColor.RED + "s");
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
        String keyName = p.getUniqueId().toString() + "-frpg-" + skillName + "-" + String.valueOf(unixTime);
        NamespacedKey key = new NamespacedKey(plugin,keyName);
        ItemMeta itemMeta = itemInHand.getItemMeta();
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING,"frpg-mining");
        itemInHand.setItemMeta(itemMeta);


        int durationLevel = (int) pStat.get(skillName).get(4);
        double duration0 = Math.ceil(durationLevel * 0.4) + 40;
        long duration = (long) duration0;
        Bukkit.getScheduler().cancelTask(pAbilities[2]);
        abilities.setPlayerAbility( skillName, -2);
        String coolDownEndMessage = ChatColor.GREEN + ">>>" + lang.getString("berserkPick") + " " + lang.getString("readyToUse") + "<<<";
        String endMessage = ChatColor.RED + ChatColor.BOLD.toString() + ">>>" + lang.getString("berserkPick") + " " + lang.getString("ended") + "<<<";
        timers.abilityDurationTimer(skillName,duration,endMessage,coolDownEndMessage,key,itemInHand,effLevel,0);
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
                MinecraftVersion minecraftVersion = new MinecraftVersion();
                if (minecraftVersion.getMinecraftVersion_Double() >= 1.16) {
                    world.dropItemNaturally(loc, new ItemStack(Material.NETHERITE_SCRAP, 1));
                    increaseStats.changeEXP(skillName, expMap.get("breakAncient_Debris"));
                }
                else{
                    world.dropItemNaturally(loc, new ItemStack(Material.DIAMOND,1));
                    increaseStats.changeEXP(skillName,expMap.get("breakDiamond_Ore"));
                }
            }
        }
    }

    public void wastelessHaste(Material blockType) {
        if (!runMethods) {
            return;
        }
        ItemGroups itemGroups = new ItemGroups();
        List<Material> ores = itemGroups.getOres();
        if (!(ores.contains(blockType))) {
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
        ItemGroups itemGroups = new ItemGroups();
        List<Material> ores = itemGroups.getOres();
        if (!(ores.contains(block.getType()))) {
            return;
        }
        if (!runMethods) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();

        int doubleDropLevel = (int) pStat.get(skillName).get(5);
        double chance = 0.0005*doubleDropLevel;
        double randomNum = rand.nextDouble();
        if (chance > randomNum) {
            for (ItemStack stack : block.getDrops(itemInHand)) {
                dropItemNaturally(block.getLocation(), stack);
                if ((int) pStat.get(skillName).get(13) > 0) {
                    dropItemNaturally(block.getLocation(), stack);
                }
            }
        }
    }

    public void preventLogoutTheft(int taskID_mining,ItemStack itemInHand_mining, NamespacedKey key,boolean isDisabling) {
        if (!runMethods) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        Integer[] pAbilities = abilities.getPlayerAbilities();
        if (pAbilities[2] == -2) {
            Bukkit.getScheduler().cancelTask(taskID_mining);
            int effLevel = itemInHand_mining.getEnchantmentLevel(Enchantment.DIG_SPEED)-5;
            String coolDownEndMessage = ChatColor.GREEN + ">>>" + lang.getString("berserkPick") + " " + lang.getString("readyToUse") + "<<<";
            String endMessage = ChatColor.RED+ChatColor.BOLD.toString() + ">>>" + lang.getString("magicForce");
            timers.endAbility(skillName,endMessage,coolDownEndMessage,key,itemInHand_mining,effLevel,0,isDisabling);
            TrackItem trackItem = new TrackItem();
            trackItem.removeItemKey(itemInHand_mining,key);
        }
    }

    public void getVeinOres(Block b1,final int x1, final int y1, final int z1,Material oreType,int maxSize) {
        if (!runMethods) {
            return;
        }
        WorldGuardChecks BuildingCheck = new WorldGuardChecks();
        int searchCubeSize = 7;
        if (veinOres.size() >= maxSize) {
            return;
        }
        for (int x = -1; x <= 1; x++) { //These 3 for loops check a 3x3x3 cube around the block in question
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) { //We can skip the 0,0,0 case because that is the block in question
                        continue;
                    }
                    Block b2 = b1.getRelative(x, y, z);
                    int blockX = b2.getX();
                    int blockY = b2.getY();
                    int blockZ = b2.getZ();
                    if (blockX == x1 && blockY == y1 && blockZ == z1) { //Makes sure the original block is never added to veinOres
                        continue;
                    }
                    if (b2.getType().equals(oreType)) {
                        if (blockX > x1 + searchCubeSize || blockX < x1 - searchCubeSize || blockY > y1 + searchCubeSize || blockY < y1 - searchCubeSize || blockZ > z1 + searchCubeSize || blockZ < z1 - searchCubeSize) {
                            break;
                        }
                        else if (!(veinOres.contains(b2))) {
                            if (veinOres.size() >= maxSize) {
                                return;
                            }
                            if (BuildingCheck.canBuild(p, b2.getLocation())) {
                                veinOres.add(b2);
                                this.getVeinOres(b2, x1, y1, z1,oreType,maxSize);
                            }
                        }
                    }
                }
            }
        }
    }

    public void vanillaVeinMiner(Block initialBlock) {
        ConfigLoad configLoad = new ConfigLoad();
        int maxBreakSize = configLoad.getVeinMinerMaxBreakSize();
        getVeinOres(initialBlock,initialBlock.getX(),initialBlock.getY(),initialBlock.getZ(),initialBlock.getType(),maxBreakSize); //Get Ores in Vein
        int numOres = veinOres.size();
        World world = initialBlock.getWorld();
        damageTool(numOres,configLoad.getDurabilityModifiers().get("veinMiner"));
        for (Block block : veinOres) {
            Location blockLoc = block.getLocation();
            PlacedBlocksManager placedBlocksManager = new PlacedBlocksManager();
            if (placedBlocksManager.isBlockTracked(block)) {
                placedBlocksManager.removeBlock(block);
            }
            Collection<ItemStack> drops = block.getDrops(itemInHand);
            block.setType(Material.AIR);
            for (ItemStack drop : drops) {
                dropItemNaturally(blockLoc, drop);
            }
        }

    }

    public void veinMiner(Block initialBlock,Material blockType) {
        if (!runMethods) {
            return;
        }
        ItemGroups itemGroups = new ItemGroups();
        HashSet<Material> veinMinerBlocks = itemGroups.getVeinMinerBlocks();
        if (!veinMinerBlocks.contains(blockType)) {
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
        HashSet<Material> ores = itemGroups.getVeinMinerBlocks();
        ConfigLoad configLoad = new ConfigLoad();
        int maxBreakSize = configLoad.getVeinMinerMaxBreakSize();
        veinOres.add(initialBlock);
        getVeinOres(initialBlock,initialBlock.getX(),initialBlock.getY(),initialBlock.getZ(),initialBlock.getType(),maxBreakSize); //Get Ores in Vein
        int numOres = veinOres.size();
        int doubleDropsLevel = (int)pStat.get(skillName).get(5);
        double chance = 0.0005*doubleDropsLevel;

        damageTool(numOres,configLoad.getDurabilityModifiers().get("veinMiner"));

        boolean didRun = false;
        Smelting smeltingClass = new Smelting(p);
        for (Block block : veinOres) {
            Location blockLoc = block.getLocation();
            //Checks if any of the blocks weren't natural
            PlacedBlocksManager placedBlocksManager = new PlacedBlocksManager();
            boolean natural = !placedBlocksManager.isBlockTracked(block);
            if (!natural) {
                placedBlocksManager.removeBlock(block);
                numOres -= 1;
            }
            //Flame Pick Conditional
            didRun = smeltingClass.flamePick(block,world,blockType,false);
            //Not Flame Pick
            if (!didRun && ores.contains(blockType)) {
                Collection<ItemStack> drops = block.getDrops(itemInHand);
                block.setType(Material.AIR);
                for (ItemStack drop : drops) {
                    dropItemNaturally(blockLoc, drop);
                }
                if (chance > rand.nextDouble() && natural) {
                    for (ItemStack drop : drops) {
                        dropItemNaturally(blockLoc, drop);
                        if ((int) pStat.get(skillName).get(13) > 0) {
                            dropItemNaturally(blockLoc, drop);
                        }
                    }
                }
            }

        }
        //Give EXP
        if (didRun) {
            increaseStats.changeEXP(skillName,numOres*smeltingClass.getEXP(itemGroups.getSmeltableItemsMap().get(blockType)));
        }
        increaseStats.changeEXP(skillName,getEXP(blockType)*numOres);
    }

    public int getEXP(Material brokenOre) {
        if (!runMethods) {
            return 0;
        }
        MinecraftVersion minecraftVersion = new MinecraftVersion();
        int EXP = 0;
        if(brokenOre.equals(Material.COAL_ORE)) {
            EXP = expMap.get("breakCoal_Ore");
        }
        else if(brokenOre.equals(Material.NETHER_QUARTZ_ORE)) {
            EXP = expMap.get("breakNether_Quartz_Ore");
        }
        else if(brokenOre.equals(Material.IRON_ORE)) {
            EXP = expMap.get("breakIron_Ore");
        }
        else if(brokenOre.equals(Material.GOLD_ORE)) {
            EXP = expMap.get("breakGold_Ore");
        }
        else if(brokenOre.equals(Material.EMERALD_ORE)) {
            EXP = expMap.get("breakEmerald_Ore");
        }
        else if(brokenOre.equals(Material.REDSTONE_ORE)) {
            EXP = expMap.get("breakRedstone_Ore");
        }
        else if(brokenOre.equals(Material.LAPIS_ORE)) {
            EXP = expMap.get("breakLapis_Ore");
        }
        else if(brokenOre.equals(Material.DIAMOND_ORE)) {
            EXP = expMap.get("breakDiamond_Ore");
        }
        else if (minecraftVersion.getMinecraftVersion_Double() >= 1.16) {
            if (brokenOre.equals(Material.ANCIENT_DEBRIS)) {
                EXP = expMap.get("breakAncient_Debris");
            } else if (brokenOre.equals(Material.GILDED_BLACKSTONE)) {
                EXP = expMap.get("breakGilded_Blackstone");
            } else if (brokenOre.equals(Material.NETHER_GOLD_ORE)) {
                EXP = expMap.get("breakNether_Gold_Ore");
            }
        }
        else if (minecraftVersion.getMinecraftVersion_Double() >= 1.17) {
            if (brokenOre.equals(Material.DEEPSLATE_COAL_ORE)) {
                EXP = expMap.get("breakDeepslate_Coal_Ore");
            } else if (brokenOre.equals(Material.DEEPSLATE_COPPER_ORE)) {
                EXP = expMap.get("breakDeepslate_Copper_Ore");
            } else if (brokenOre.equals(Material.DEEPSLATE_DIAMOND_ORE)) {
                EXP = expMap.get("breakDeepslate_Diamond_Ore");
            } else if (brokenOre.equals(Material.DEEPSLATE_EMERALD_ORE)) {
                EXP = expMap.get("breakDeepslate_Emerald_Ore");
            } else if (brokenOre.equals(Material.DEEPSLATE_GOLD_ORE)) {
                EXP = expMap.get("breakDeepslate_Gold_Ore");
            } else if (brokenOre.equals(Material.DEEPSLATE_IRON_ORE)) {
                EXP = expMap.get("breakDeepslate_Iron_Ore");
            } else if (brokenOre.equals(Material.DEEPSLATE_LAPIS_ORE)) {
                EXP = expMap.get("breakDeepslate_Lapis_Ore");
            } else if (brokenOre.equals(Material.DEEPSLATE_REDSTONE_ORE)) {
                EXP = expMap.get("breakDeepslate_Redstone_Ore");
            }
        }
        return EXP;
    }
}
