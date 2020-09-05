package mc.carlton.freerpg.perksAndAbilities;

import com.google.gson.internal.$Gson$Preconditions;
import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.ActionBarMessages;
import mc.carlton.freerpg.gameTools.LanguageSelector;
import mc.carlton.freerpg.globalVariables.ExpMaps;
import mc.carlton.freerpg.globalVariables.ItemGroups;
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
    private String skillName = "woodcutting";
    Map<String,Integer> expMap;
    Map<Material,Integer> woodcuttingEXP;

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

    List<Material> logs;

    ArrayList<Block> timberLogs = new ArrayList<Block>();

    private boolean runMethods;

    public Woodcutting(Player p) {
        this.p = p;
        this.pName = p.getDisplayName();
        this.itemInHand = p.getInventory().getItemInMainHand();
        this.increaseStats = new ChangeStats(p);
        this.abilities = new AbilityTracker(p);
        this.timers = new AbilityTimers(p);
        this.pStatClass=  new PlayerStats(p);
        this.actionMessage = new ActionBarMessages(p);
        this.lang = new LanguageSelector(p);

        ItemGroups itemGroups = new ItemGroups();
        this.logs = itemGroups.getLogs();

        ExpMaps expMaps = new ExpMaps();
        this.woodcuttingEXP = expMaps.getWoodcuttingEXP();

        ConfigLoad configLoad = new ConfigLoad();
        this.runMethods = configLoad.getAllowedSkillsMap().get(skillName);
        expMap = configLoad.getExpMapForSkill(skillName);
    }

    public void initiateAbility() {
        if (!runMethods) {
            return;
        }
        if (!p.hasPermission("freeRPG.woodcuttingAbility")) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        if ((int) pStat.get("global").get(24) < 1 || !pStatClass.isPlayerSkillAbilityOn(skillName)) {
            return;
        }
        Integer[] pTimers = timers.getPlayerTimers();
        Integer[] pAbilities = abilities.getPlayerAbilities();
        if (pAbilities[1] == -1) {
            int cooldown = pTimers[1];
            if (cooldown < 1) {
                int prepMessages = (int) pStatClass.getPlayerData().get("global").get(22); //Toggle for preparation messages
                if (prepMessages > 0) {
                    actionMessage.sendMessage(ChatColor.GRAY + ">>>" + lang.getString("prepare") + " " + lang.getString("axe") + "...<<<");
                }
                int taskID = new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            Integer[] pAbilities2 = abilities.getPlayerAbilities();
                            if (pAbilities2[9] != -2 && prepMessages > 0) {
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
                actionMessage.sendMessage(ChatColor.RED +lang.getString("timber") + " " + lang.getString("cooldown") + ": " + cooldown+ "s");
            }
        }
    }
    public void enableAbility() {
        if (!runMethods) {
            return;
        }
        Integer[] pAbilities = abilities.getPlayerAbilities();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        actionMessage.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + ">>>" + lang.getString("timber") + " " + lang.getString("activated") + "<<<");
        int durationLevel = (int) pStat.get(skillName).get(4);
        double duration0 = Math.ceil(durationLevel*0.4) + 40;
        int cooldown = 300;
        if ((int) pStat.get("global").get(11) > 0) {
            cooldown = 200;
        }
        int finalCooldown = cooldown;
        long duration = (long) duration0;
        Bukkit.getScheduler().cancelTask(pAbilities[1]);
        abilities.setPlayerAbility( skillName, -2);
        new BukkitRunnable() {
            @Override
            public void run() {
                actionMessage.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + ">>>" + lang.getString("timber") + " " + lang.getString("ended") + "<<<");
                abilities.setPlayerAbility( skillName, -1);
                timers.setPlayerTimer( skillName, finalCooldown);
                for(int i = 1; i < finalCooldown+1; i++) {
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
                                    actionMessage.sendMessage(ChatColor.GREEN + ">>>" + lang.getString("timber") + " " + lang.getString("readyToUse") + "<<<");
                                }
                            }
                        }
                    }.runTaskLater(plugin, 20*i);
                }
            }
        }.runTaskLater(plugin, duration);
    }
    public void getTimberLogs(Block b1, final int x1, final int z1) {
        if (!runMethods) {
            return;
        }
        WorldGuardChecks BuildingCheck = new WorldGuardChecks();
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
                            if (BuildingCheck.canBuild(p, b2.getLocation())) {
                                timberLogs.add(b2);
                                this.getTimberLogs(b2, x1, z1);
                            }
                        }
                    }
                }
            }
        }
    }

    public void timber(Block initialBlock) {
        if (!runMethods) {
            return;
        }
        if (!(logs.contains(initialBlock.getType()))) {
            return;
        }
        World world = initialBlock.getWorld();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int doubleDropsLevel = (int)pStat.get(skillName).get(5);
        int timber_plus = (int)pStat.get(skillName).get(11);
        int able_axe = (int)pStat.get(skillName).get(13);
        int hiddenKnowledgeLevel = (int) pStat.get(skillName).get(9);
        getTimberLogs(initialBlock,initialBlock.getX(),initialBlock.getZ());
        int numLogs = timberLogs.size();
        if (timber_plus < 1) {
            if (numLogs > 64) {
                actionMessage.sendMessage(ChatColor.RED + lang.getString("treeTooBig0"));
                return;
            }
        }
        else {
            if (numLogs > 128) {
                actionMessage.sendMessage(ChatColor.RED + lang.getString("treeTooBig1"));
                return;
            }
        }
        damageTool(numLogs);
        for (Block block : timberLogs) {
            Location blockLoc = block.getLocation();
            //Checks if any of the blocks weren't natural
            PlacedBlocksManager placedBlocksManager = new PlacedBlocksManager();
            boolean natural = !placedBlocksManager.isBlockTracked(block);
            if (!natural) {
                placedBlocksManager.removeBlock(block);
                numLogs -= 1;
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
                    ItemGroups itemGroups = new ItemGroups();
                    Map<Enchantment, Integer> enchantmentLevelMap = itemGroups.getEnchantmentLevelMap();
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
            int zealousRootsLevel = (int) pStat.get(skillName).get(7);
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
        ConfigLoad configLoad = new ConfigLoad();
        increaseStats.changeEXP(skillName, (int) Math.round(woodcuttingEXP.get(initialBlock.getType()) * numLogs * configLoad.getSpecialMultiplier().get("timberEXPMultiplier")));
        timberLogs.clear();

    }
    public void woodcuttingDoubleDrop(Block block, World world) {
        if (!runMethods) {
            return;
        }
        Material[] planks0 = {Material.ACACIA_PLANKS,Material.BIRCH_PLANKS,Material.DARK_OAK_PLANKS,Material.JUNGLE_PLANKS,Material.OAK_PLANKS,Material.SPRUCE_PLANKS};
        List<Material> planks = Arrays.asList(planks0);
        if (planks.contains(block.getType())) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int doubleDropLevel = (int) pStat.get(skillName).get(5);
        double chance = 0.0005*doubleDropLevel;
        double randomNum = rand.nextDouble();
        if (chance > randomNum) {
            for (ItemStack stack : block.getDrops(itemInHand)) {
                world.dropItemNaturally(block.getLocation(), stack);
            }
        }
    }

    public void logXPdrop(Block block,World world) {
        if (!runMethods) {
            return;
        }
        if (logs.contains(block.getType())) {
            Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
            int zealousRootsLevel = (int) pStat.get(skillName).get(7);
            double xpChance = zealousRootsLevel*0.2;
            double randomNum = rand.nextDouble();
            if (xpChance > randomNum) {
                ((ExperienceOrb)world.spawn(block.getLocation(), ExperienceOrb.class)).setExperience(1);
            }
        }
    }
    public void logBookDrop(Block block, World world) {
        if (!runMethods) {
            return;
        }
        if (logs.contains(block.getType())) {
            Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
            int hiddenKnowledgeLevel = (int) pStat.get(skillName).get(9);
            double bookChance = hiddenKnowledgeLevel*0.002;
            double randomNum = rand.nextDouble();
            if (bookChance > randomNum) {
                ItemGroups itemGroups = new ItemGroups();
                Map<Enchantment, Integer> enchantmentLevelMap = itemGroups.getEnchantmentLevelMap();
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
        if (!runMethods) {
            return;
        }
        ItemGroups itemGroups = new ItemGroups();
        List<Material> leaves = itemGroups.getLeaves();
        if (leaves.contains(block.getType()) && itemInHand.getType() != Material.SHEARS) {
            Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
            ConfigLoad loadConfig = new ConfigLoad();
            ArrayList<Object> leafDropData = loadConfig.getWoodcuttingInfo();
            int leafLevel = (int) pStat.get(skillName).get(10);
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
                increaseStats.changeEXP(skillName, expMap.get("leafDrop1"));
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
                increaseStats.changeEXP(skillName,expMap.get("leafDrop2"));
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
                increaseStats.changeEXP(skillName,expMap.get("leafDrop3"));
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
                increaseStats.changeEXP(skillName,expMap.get("leafDrop4"));
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
                increaseStats.changeEXP(skillName,expMap.get("leafDrop5"));
            }
        }

    }
    public void leafBlower(Block block, World world) {
        if (!runMethods) {
            return;
        }
        ItemGroups itemGroups = new ItemGroups();
        List<Material> leaves = itemGroups.getLeaves();
        List<Material> axes = itemGroups.getAxes();
        if (leaves.contains(block.getType()) && axes.contains(itemInHand.getType())) {
            WorldGuardChecks BuildingCheck = new WorldGuardChecks();
            if (!BuildingCheck.canBuild(p, block.getLocation())) {
                return;
            }
            increaseStats.changeEXP(skillName,expMap.get("useLeafBlower"));
            Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
            int leafLevel = (int) pStat.get(skillName).get(12);
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
        if (!runMethods) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int freshArmsLevel = (int) pStat.get(skillName).get(8);
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
        if (!runMethods) {
            return false;
        }
        boolean isBlacklisted = false;
        Material blockType = block.getType();
        ItemGroups itemGroups = new ItemGroups();
        List<Material> strippedLogs = itemGroups.getStrippedLogs();
        if (logs.contains(blockType) || strippedLogs.contains(blockType)) {
            isBlacklisted = true;
        }
        return isBlacklisted;
    }

    public void damageTool(int damage) {
        ItemMeta toolMeta = itemInHand.getItemMeta();
        if (toolMeta instanceof Damageable) {
            ((Damageable) toolMeta).setDamage(((Damageable) toolMeta).getDamage()+damage);
            itemInHand.setItemMeta(toolMeta);
            if (((Damageable) toolMeta).getDamage() > itemInHand.getType().getMaxDurability()) {
                itemInHand.setAmount(0);
                p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, 1);
            }
        }
    }
}
