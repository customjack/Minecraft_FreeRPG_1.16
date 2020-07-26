package mc.carlton.freerpg.perksAndAbilities;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.playerAndServerInfo.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Woodcutting {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    private Player p;
    private String pName;
    private ItemStack itemInHand;
    private Map<Enchantment,Integer> enchantmentLevelMap = new HashMap<>();

    ChangeStats increaseStats; //Changing Stats

    AbilityTracker abilities; //Abilities class
    // GET ABILITY STATUSES LIKE THIS:   Integer[] pAbilities = abilities.getPlayerAbilities(p);

    AbilityTimers timers; //Ability Timers class
    //GET TIMERS LIKE THIS:              Integer[] pTimers = timers.getPlayerTimers(p);

    PlayerStats pStatClass;
    //GET PLAYER STATS LIKE THIS:        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData(p);

    PlacedBlocks placedClass;
    //GET TRACKED BLOCKS LIKE THIS:        ArrayList<Location> blocksLocations = placedClass.getBlocks();

    Random rand = new Random(); //Random class Import

    Material[] logs0 = {Material.ACACIA_LOG,Material.BIRCH_LOG,Material.DARK_OAK_LOG,Material.OAK_LOG,Material.SPRUCE_LOG,Material.JUNGLE_LOG,Material.CRIMSON_STEM,Material.WARPED_STEM};
    List<Material> logs = Arrays.asList(logs0);
    Material[] strippedLogs0 = {Material.STRIPPED_SPRUCE_LOG,Material.STRIPPED_OAK_LOG,Material.STRIPPED_JUNGLE_LOG,Material.STRIPPED_DARK_OAK_LOG,Material.STRIPPED_BIRCH_LOG,Material.STRIPPED_ACACIA_LOG};
    List<Material> strippedLogs = Arrays.asList(strippedLogs0);
    Material[] leaves0 = {Material.ACACIA_LEAVES,Material.BIRCH_LEAVES,Material.DARK_OAK_LEAVES,Material.OAK_LEAVES,Material.SPRUCE_LEAVES,Material.JUNGLE_LEAVES};
    List<Material> leaves = Arrays.asList(leaves0);
    Material[] axes0 = {Material.DIAMOND_AXE,Material.GOLDEN_AXE,Material.IRON_AXE, Material.STONE_AXE,Material.WOODEN_AXE};
    List<Material> axes = Arrays.asList(axes0);

    ArrayList<Block> timberLogs = new ArrayList<Block>();

    public Woodcutting(Player p) {
        this.p = p;
        this.pName = p.getDisplayName();
        this.itemInHand = p.getInventory().getItemInMainHand();
        this.increaseStats = new ChangeStats(p);
        this.abilities = new AbilityTracker(p);
        this.timers = new AbilityTimers(p);
        this.pStatClass=  new PlayerStats(p);
        this.placedClass = new PlacedBlocks();

        this.enchantmentLevelMap.put(Enchantment.ARROW_KNOCKBACK,2);
        this.enchantmentLevelMap.put(Enchantment.ARROW_DAMAGE,5);
        this.enchantmentLevelMap.put(Enchantment.ARROW_FIRE,1);
        this.enchantmentLevelMap.put(Enchantment.ARROW_INFINITE,1);
        this.enchantmentLevelMap.put(Enchantment.BINDING_CURSE,1);
        this.enchantmentLevelMap.put(Enchantment.CHANNELING,1);
        this.enchantmentLevelMap.put(Enchantment.DAMAGE_ALL,5);
        this.enchantmentLevelMap.put(Enchantment.DAMAGE_ARTHROPODS,5);
        this.enchantmentLevelMap.put(Enchantment.DAMAGE_UNDEAD,5);
        this.enchantmentLevelMap.put(Enchantment.DEPTH_STRIDER,2);
        this.enchantmentLevelMap.put(Enchantment.DIG_SPEED,5);
        this.enchantmentLevelMap.put(Enchantment.DURABILITY,3);
        this.enchantmentLevelMap.put(Enchantment.FIRE_ASPECT,2);
        this.enchantmentLevelMap.put(Enchantment.FROST_WALKER,2);
        this.enchantmentLevelMap.put(Enchantment.IMPALING,5);
        this.enchantmentLevelMap.put(Enchantment.KNOCKBACK,2);
        this.enchantmentLevelMap.put(Enchantment.LOOT_BONUS_BLOCKS,3);
        this.enchantmentLevelMap.put(Enchantment.LUCK,3);
        this.enchantmentLevelMap.put(Enchantment.LOOT_BONUS_MOBS,3);
        this.enchantmentLevelMap.put(Enchantment.LOYALTY,3);
        this.enchantmentLevelMap.put(Enchantment.LURE,3);
        this.enchantmentLevelMap.put(Enchantment.MENDING,1);
        this.enchantmentLevelMap.put(Enchantment.MULTISHOT,1);
        this.enchantmentLevelMap.put(Enchantment.OXYGEN,3);
        this.enchantmentLevelMap.put(Enchantment.PIERCING,4);
        this.enchantmentLevelMap.put(Enchantment.PROTECTION_ENVIRONMENTAL,4);
        this.enchantmentLevelMap.put(Enchantment.PROTECTION_EXPLOSIONS,4);
        this.enchantmentLevelMap.put(Enchantment.PROTECTION_FALL,4);
        this.enchantmentLevelMap.put(Enchantment.PROTECTION_FIRE,4);
        this.enchantmentLevelMap.put(Enchantment.PROTECTION_PROJECTILE,4);
        this.enchantmentLevelMap.put(Enchantment.QUICK_CHARGE,3);
        this.enchantmentLevelMap.put(Enchantment.RIPTIDE,3);
        this.enchantmentLevelMap.put(Enchantment.SILK_TOUCH,1);
        this.enchantmentLevelMap.put(Enchantment.SWEEPING_EDGE,3);
        this.enchantmentLevelMap.put(Enchantment.THORNS,3);
        this.enchantmentLevelMap.put(Enchantment.VANISHING_CURSE,1);
        this.enchantmentLevelMap.put(Enchantment.WATER_WORKER,1);
    }

    public void initiateAbility() {
        Integer[] pTimers = timers.getPlayerTimers();
        Integer[] pAbilities = abilities.getPlayerAbilities();
        if (pAbilities[1] == -1) {
            int cooldown = pTimers[1];
            if (cooldown < 1) {
                int prepMessages = (int) pStatClass.getPlayerData().get("global").get(22); //Toggle for preparation messages
                if (prepMessages > 0) {
                    p.sendMessage(ChatColor.GRAY + ">>>You prepare your axe...<<<");
                }
                int taskID = new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            Integer[] pAbilities2 = abilities.getPlayerAbilities();
                            if (pAbilities2[9] != -2 && prepMessages > 0) {
                                p.sendMessage(ChatColor.GRAY + ">>>...You rest your axe<<<");
                            }
                            abilities.setPlayerAbility( "woodcutting", -1);
                        }
                        catch (Exception e) {

                        }
                    }
                }.runTaskLater(plugin, 20 * 4).getTaskId();
                abilities.setPlayerAbility( "woodcutting", taskID);
            } else {
                p.sendMessage(ChatColor.RED + "You must wait " + cooldown + " seconds to use Timber again.");
            }
        }
    }
    public void enableAbility() {
        Integer[] pAbilities = abilities.getPlayerAbilities();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        p.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + ">>>Timber Activated!<<<");
        int durationLevel = (int) pStat.get("woodcutting").get(4);
        double duration0 = Math.ceil(durationLevel*0.4) + 40;
        int cooldown = 300;
        if ((int) pStat.get("global").get(11) > 0) {
            cooldown = 200;
        }
        int finalCooldown = cooldown;
        long duration = (long) duration0;
        Bukkit.getScheduler().cancelTask(pAbilities[1]);
        abilities.setPlayerAbility( "woodcutting", -2);
        new BukkitRunnable() {
            @Override
            public void run() {
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + ">>>Timber has ended<<<");
                abilities.setPlayerAbility( "woodcutting", -1);
                timers.setPlayerTimer( "woodcutting", finalCooldown);
                for(int i = 1; i < finalCooldown+1; i++) {
                    int timeRemaining = finalCooldown - i;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            timers.setPlayerTimer( "woodcutting", timeRemaining);
                            AbilityTimers timers2 = new AbilityTimers(p);
                            if (timeRemaining ==0) {
                                if (!p.isOnline()) {
                                    timers2.removePlayer();
                                }
                                else {
                                    p.sendMessage(ChatColor.GREEN + ">>>Timber is ready to use again<<<");
                                }
                            }
                        }
                    }.runTaskLater(plugin, 20*i);
                }
            }
        }.runTaskLater(plugin, duration);
    }
    public void getTimberLogs(Block b1, final int x1, final int z1) {
        int searchSquareSize = 7;
        for (int x = -1; x <= 1; x++) {
            for (int y = 0; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }
                    Block b2 = b1.getRelative(x, y, z);
                    if (logs.contains(b2.getType())) {
                        if (b2.getX() > x1 + searchSquareSize || b2.getX() < x1 - searchSquareSize || b2.getZ() > z1 + searchSquareSize || b2.getZ() < z1 - searchSquareSize) {
                            break;
                        }
                        else if (!(timberLogs.contains(b2))) {
                            timberLogs.add(b2);
                            this.getTimberLogs(b2, x1, z1);
                        }
                    }
                }
            }
        }
    }

    public void timber(Block initialBlock) {
        if (!(logs.contains(initialBlock.getType()))) {
            return;
        }
        World world = initialBlock.getWorld();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        ArrayList<Location> blocksLocations = placedClass.getBlocks();
        int doubleDropsLevel = (int)pStat.get("woodcutting").get(5);
        int timber_plus = (int)pStat.get("woodcutting").get(11);
        int able_axe = (int)pStat.get("woodcutting").get(13);
        int hiddenKnowledgeLevel = (int) pStat.get("woodcutting").get(9);
        getTimberLogs(initialBlock,initialBlock.getX(),initialBlock.getZ());
        int numLogs = timberLogs.size();
        if (timber_plus < 1) {
            if (numLogs > 64) {
                p.sendMessage(ChatColor.RED + "This tree is too big for you to chop in one go!");
                return;
            }
        }
        else {
            if (numLogs > 128) {
                p.sendMessage(ChatColor.RED + "This tree can never be chopped in one go like this...");
                return;
            }
        }
        ItemMeta toolMeta = itemInHand.getItemMeta();
        if (toolMeta instanceof Damageable) {
            ((Damageable) toolMeta).setDamage(((Damageable) toolMeta).getDamage()+numLogs);
            itemInHand.setItemMeta(toolMeta);
            if (((Damageable) toolMeta).getDamage() > itemInHand.getType().getMaxDurability()) {
                itemInHand.setAmount(0);
                p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, 1);
            }
        }
        for (Block block : timberLogs) {
            Location blockLoc = block.getLocation();
            boolean natural = true;
            for (Location blockLocation : blocksLocations) {
                if (blockLoc.equals(blockLocation)) {
                    blocksLocations.remove(blockLocation);
                    placedClass.setBlocks(blocksLocations);
                    numLogs += -1;
                    natural = false;
                    break;
                }
            }

            Collection<ItemStack> drops = block.getDrops(itemInHand);
            block.setType(Material.AIR);
            for (ItemStack drop : drops) {
                world.dropItemNaturally(blockLoc, drop);
            }
            if (able_axe > 0 && natural) {
                double randomNum = rand.nextDouble();
                double doubleDropChance = doubleDropsLevel * 0.00025;
                if (doubleDropChance > randomNum) {
                    for (ItemStack drop : drops) {
                        world.dropItemNaturally(blockLoc, drop);
                    }
                }
                //Book spawns
                double bookDropChance = hiddenKnowledgeLevel*0.001;
                if (bookDropChance > rand.nextDouble()) {
                    List<Enchantment> keysAsArray = new ArrayList<Enchantment>(enchantmentLevelMap.keySet());
                    Enchantment randomEnchant = keysAsArray.get(rand.nextInt(keysAsArray.size()));
                    int randomLevel = rand.nextInt(enchantmentLevelMap.get(randomEnchant)) + 1;
                    ItemStack enchantedBook = new ItemStack(Material.ENCHANTED_BOOK, 1);
                    EnchantmentStorageMeta meta = ((EnchantmentStorageMeta) enchantedBook.getItemMeta());
                    meta.addStoredEnchant(randomEnchant,randomLevel,true);
                    enchantedBook.setItemMeta(meta);
                    if (randomEnchant == Enchantment.BINDING_CURSE || randomEnchant == Enchantment.VANISHING_CURSE) {
                        Map<Enchantment,Integer> enchantmentLevelMapGood = enchantmentLevelMap;
                        enchantmentLevelMapGood.remove(Enchantment.BINDING_CURSE);
                        enchantmentLevelMapGood.remove(Enchantment.VANISHING_CURSE);
                        keysAsArray = new ArrayList<Enchantment>(enchantmentLevelMapGood.keySet());
                        randomEnchant = keysAsArray.get(rand.nextInt(keysAsArray.size()));
                        randomLevel = rand.nextInt(enchantmentLevelMap.get(randomEnchant)) + 1;
                        meta.addStoredEnchant(randomEnchant,randomLevel,true);
                        enchantedBook.setItemMeta(meta);
                    }
                    world.dropItemNaturally(block.getLocation(), enchantedBook);
                }
            }
        }
        if (able_axe > 0) {
            //XP all in one go
            int zealousRootsLevel = (int) pStat.get("woodcutting").get(7);
            double xpDrop0 = zealousRootsLevel*0.1*numLogs;
            int xpDrop = 0;
            double roundChance = xpDrop0 - Math.floor(xpDrop0);
            double randomNum = rand.nextDouble();
            if (roundChance > randomNum) {
                xpDrop = (int) Math.ceil(xpDrop0);
            }
            else {
                xpDrop = (int) Math.floor(xpDrop0);
            }
            ((ExperienceOrb)world.spawn(initialBlock.getLocation(), ExperienceOrb.class)).setExperience(xpDrop);

        }
        if (initialBlock.getType() == Material.CRIMSON_STEM || initialBlock.getType() == Material.WARPED_STEM) {
            increaseStats.changeEXP("woodcutting", 150 * numLogs);
        }
        else {
            increaseStats.changeEXP("woodcutting", 120 * numLogs);
        }
        timberLogs.clear();

    }
    public void woodcuttingDoubleDrop(Block block, World world) {
        Material[] planks0 = {Material.ACACIA_PLANKS,Material.BIRCH_PLANKS,Material.DARK_OAK_PLANKS,Material.JUNGLE_PLANKS,Material.OAK_PLANKS,Material.SPRUCE_PLANKS};
        List<Material> planks = Arrays.asList(planks0);
        if (planks.contains(block.getType())) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int doubleDropLevel = (int) pStat.get("woodcutting").get(5);
        double chance = 0.0005*doubleDropLevel;
        double randomNum = rand.nextDouble();
        if (chance > randomNum) {
            for (ItemStack stack : block.getDrops(itemInHand)) {
                world.dropItemNaturally(block.getLocation(), stack);
            }
        }
    }

    public void logXPdrop(Block block,World world) {
        if (logs.contains(block.getType())) {
            Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
            int zealousRootsLevel = (int) pStat.get("woodcutting").get(7);
            double xpChance = zealousRootsLevel*0.2;
            double randomNum = rand.nextDouble();
            if (xpChance > randomNum) {
                ((ExperienceOrb)world.spawn(block.getLocation(), ExperienceOrb.class)).setExperience(1);
            }
        }
    }
    public void logBookDrop(Block block, World world) {
        if (logs.contains(block.getType())) {
            Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
            int hiddenKnowledgeLevel = (int) pStat.get("woodcutting").get(9);
            double bookChance = hiddenKnowledgeLevel*0.002;
            double randomNum = rand.nextDouble();
            if (bookChance > randomNum) {
                List<Enchantment> keysAsArray = new ArrayList<Enchantment>(enchantmentLevelMap.keySet());
                Enchantment randomEnchant = keysAsArray.get(rand.nextInt(keysAsArray.size()));
                int randomLevel = rand.nextInt(enchantmentLevelMap.get(randomEnchant)) + 1;
                ItemStack enchantedBook = new ItemStack(Material.ENCHANTED_BOOK, 1);
                EnchantmentStorageMeta meta = ((EnchantmentStorageMeta) enchantedBook.getItemMeta());
                meta.addStoredEnchant(randomEnchant,randomLevel,true);
                enchantedBook.setItemMeta(meta);
                if (randomEnchant == Enchantment.BINDING_CURSE || randomEnchant == Enchantment.VANISHING_CURSE) {
                    Map<Enchantment,Integer> enchantmentLevelMapGood = enchantmentLevelMap;
                    enchantmentLevelMapGood.remove(Enchantment.BINDING_CURSE);
                    enchantmentLevelMapGood.remove(Enchantment.VANISHING_CURSE);
                    keysAsArray = new ArrayList<Enchantment>(enchantmentLevelMapGood.keySet());
                    randomEnchant = keysAsArray.get(rand.nextInt(keysAsArray.size()));
                    randomLevel = rand.nextInt(enchantmentLevelMap.get(randomEnchant)) + 1;
                    meta.addStoredEnchant(randomEnchant,randomLevel,true);
                    enchantedBook.setItemMeta(meta);
                }
                world.dropItemNaturally(block.getLocation(), enchantedBook);
            }

        }

    }
    public void leavesDrops(Block block, World world) {
        if (leaves.contains(block.getType()) && itemInHand.getType() != Material.SHEARS) {
            Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
            ConfigLoad loadConfig = new ConfigLoad();
            ArrayList<Object> leafDropData = loadConfig.getWoodcuttingInfo();
            int leafLevel = (int) pStat.get("woodcutting").get(10);
            double randomNum = rand.nextDouble();
            Double[] prob = {(double)leafDropData.get(2),(double)leafDropData.get(5),(double)leafDropData.get(8),(double)leafDropData.get(11),(double)leafDropData.get(14)};
            Double[] dropChanceSums = {prob[0],prob[0]+prob[1],prob[0]+prob[1]+prob[2],prob[0]+prob[1]+prob[2]+prob[3],
                                       prob[0]+prob[1]+prob[2]+prob[3]+prob[4]};
            if (randomNum < dropChanceSums[0] && leafLevel >= 1)  {
                ItemStack drop = new ItemStack(Material.DIRT, 1);
                if (leafDropData.get(0) == null) {
                    drop.setType(Material.FEATHER);
                }
                else {
                    drop.setType((Material)leafDropData.get(0));
                    drop.setAmount((int)leafDropData.get(1));
                }
                world.dropItemNaturally(block.getLocation(), drop);
                increaseStats.changeEXP("woodcutting", 200);
            }
            else if (randomNum < dropChanceSums[1] && leafLevel >= 2) {
                ItemStack drop = new ItemStack(Material.DIRT, 1);
                if (leafDropData.get(3) == null) {
                    drop.setType(Material.GOLD_NUGGET);
                }
                else {
                    drop.setType((Material)leafDropData.get(3));
                    drop.setAmount((int)leafDropData.get(4));
                }
                world.dropItemNaturally(block.getLocation(), drop);
                increaseStats.changeEXP("woodcutting",250);
            }
            else if (randomNum < dropChanceSums[2] && leafLevel >= 3) {
                ItemStack drop = new ItemStack(Material.DIRT, 1);
                if (leafDropData.get(6) == null) {
                    drop.setType(Material.GOLDEN_APPLE);
                }
                else {
                    drop.setType((Material)leafDropData.get(6));
                    drop.setAmount((int)leafDropData.get(7));
                }
                world.dropItemNaturally(block.getLocation(), drop);
                increaseStats.changeEXP("woodcutting",1000);
            }
            else if (randomNum < dropChanceSums[3] && leafLevel >= 4) {
                ItemStack drop = new ItemStack(Material.DIRT, 1);
                if (leafDropData.get(9) == null) {
                    drop.setType(Material.EXPERIENCE_BOTTLE);
                }
                else {
                    drop.setType((Material)leafDropData.get(9));
                    drop.setAmount((int)leafDropData.get(10));
                }
                world.dropItemNaturally(block.getLocation(), drop);
                increaseStats.changeEXP("woodcutting",750);
            }
            else if (randomNum < dropChanceSums[4] && leafLevel >= 5) {
                ItemStack drop = new ItemStack(Material.DIRT, 1);
                if (leafDropData.get(12) == null) {
                    drop.setType(Material.ENCHANTED_GOLDEN_APPLE);
                }
                else {
                    drop.setType((Material)leafDropData.get(12));
                    drop.setAmount((int)leafDropData.get(13));
                }
                world.dropItemNaturally(block.getLocation(), drop);
                increaseStats.changeEXP("woodcutting",3000);
            }
        }

    }
    public void leafBlower(Block block, World world) {
        if (leaves.contains(block.getType()) && axes.contains(itemInHand.getType())) {
            increaseStats.changeEXP("woodcutting",35);
            Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
            int leafLevel = (int) pStat.get("woodcutting").get(12);
            if (leafLevel > 0) {
                leavesDrops(block,world);
                block.setType(Material.AIR);
                ItemMeta toolMeta = itemInHand.getItemMeta();
                if (toolMeta instanceof Damageable) {
                    ((Damageable) toolMeta).setDamage(((Damageable) toolMeta).getDamage()+1);
                    itemInHand.setItemMeta(toolMeta);
                    if (((Damageable) toolMeta).getDamage() > itemInHand.getType().getMaxDurability()) {
                        itemInHand.setAmount(0);
                        p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, 1);
                    }
                }

                double randomNum1 = rand.nextDouble();
                if (randomNum1 < 0.05) {
                    double randomNum2 = rand.nextDouble();
                    if (randomNum2 < 0.5) {
                        switch (block.getType()) {
                            case ACACIA_LEAVES:
                                world.dropItemNaturally(block.getLocation(), new ItemStack(Material.ACACIA_SAPLING,1));
                                break;
                            case BIRCH_LEAVES:
                                world.dropItemNaturally(block.getLocation(), new ItemStack(Material.BIRCH_SAPLING,1));
                                break;
                            case DARK_OAK_LEAVES:
                                world.dropItemNaturally(block.getLocation(), new ItemStack(Material.DARK_OAK_SAPLING,1));
                                break;
                            case JUNGLE_LEAVES:
                                world.dropItemNaturally(block.getLocation(), new ItemStack(Material.JUNGLE_SAPLING,1));
                                break;
                            case SPRUCE_LEAVES:
                                world.dropItemNaturally(block.getLocation(), new ItemStack(Material.SPRUCE_SAPLING,1));
                                break;
                            case OAK_LEAVES:
                                world.dropItemNaturally(block.getLocation(), new ItemStack(Material.OAK_SAPLING,1));
                                break;
                            default:
                                break;
                        }

                    }
                    else if (randomNum2 < 0.75) {
                        world.dropItemNaturally(block.getLocation(), new ItemStack(Material.STICK,1));
                    }
                    else {
                        world.dropItemNaturally(block.getLocation(), new ItemStack(Material.STICK,2));
                    }
                }
                double randomNum3 = rand.nextDouble();
                if (randomNum3 < 0.005 && (block.getType() == Material.OAK_LEAVES || block.getType() == Material.DARK_OAK_LEAVES)) {
                    world.dropItemNaturally(block.getLocation(), new ItemStack(Material.APPLE,1));
                }
            }
        }
    }
    public void timedHaste(Block block){
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int freshArmsLevel = (int) pStat.get("woodcutting").get(8);
        if (logs.contains(block.getType()) && freshArmsLevel > 0) {
            Integer[] pAbilities = abilities.getPlayerAbilities();
            if (pAbilities[10] == -1) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20*12 * freshArmsLevel, 0));
                int taskID = new BukkitRunnable() {
                    @Override
                    public void run() {
                        abilities.setPlayerAbility( "woodcuttingHaste", -1);
                    }
                }.runTaskLater(plugin, 20 * 300).getTaskId();
                abilities.setPlayerAbility( "woodcuttingHaste", taskID);
            } else {
                Bukkit.getScheduler().cancelTask(pAbilities[10]);
                int taskID = new BukkitRunnable() {
                    @Override
                    public void run() {
                        abilities.setPlayerAbility( "woodcuttingHaste", -1);
                    }
                }.runTaskLater(plugin, 20 * 300).getTaskId();
            }
        }
    }

    public boolean blacklistedBlock(Block block) {
        boolean isBlacklisted = false;
        Material blockType = block.getType();
        if (logs.contains(blockType) || strippedLogs.contains(blockType)) {
            isBlacklisted = true;
        }
        return isBlacklisted;
    }
}
