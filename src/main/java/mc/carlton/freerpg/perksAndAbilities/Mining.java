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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Mining {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    private Player p;
    private String pName;
    private ItemStack itemInHand;
    ChangeStats increaseStats; //Changing Stats

    AbilityTracker abilities; //Abilities class
    // GET ABILITY STATUSES LIKE THIS:   Integer[] pAbilities = abilities.getPlayerAbilities(p);

    AbilityTimers timers; //Ability Timers class
    //GET TIMERS LIKE THIS:              Integer[] pTimers = timers.getPlayerTimers(p);

    PlayerStats pStatClass;
    //GET PLAYER STATS LIKE THIS:        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData(p);

    PlacedBlocks placedClass;
    //GET TRACKED BLOCKS LIKE THIS:        ArrayList<Location> blocksLocations = placedClass.getBlocks();

    Material[] ores0 = {Material.REDSTONE_ORE,Material.NETHER_QUARTZ_ORE,Material.LAPIS_ORE,Material.IRON_ORE,Material.GOLD_ORE,
                        Material.EMERALD_ORE,Material.DIAMOND_ORE,Material.COAL_ORE};
    List<Material> ores = Arrays.asList(ores0);

    Random rand = new Random(); //Random class Import
    ArrayList<Block> veinOres = new ArrayList<Block>();

    public Mining(Player p) {
        this.p = p;
        this.pName = p.getDisplayName();
        this.itemInHand = p.getInventory().getItemInMainHand();
        this.increaseStats = new ChangeStats(p);
        this.abilities = new AbilityTracker(p);
        this.timers = new AbilityTimers(p);
        this.pStatClass=  new PlayerStats(p);
        this.placedClass = new PlacedBlocks();
    }

    public void miningTreasureDrop(double treasureChance, World world, Location loc) {
        double randomNum = rand.nextDouble();
        if (treasureChance > randomNum) {
            double randomNum2 = rand.nextDouble();
            if (randomNum2 < 0.5) {
                world.dropItemNaturally(loc, new ItemStack(Material.COAL,1));
                increaseStats.changeEXP("mining",140);
            }
            else if (randomNum2 < 0.7) {
                world.dropItemNaturally(loc, new ItemStack(Material.IRON_ORE,1));
                increaseStats.changeEXP("mining",350);
            }
            else if (randomNum2 < 0.8) {
                world.dropItemNaturally(loc, new ItemStack(Material.GOLD_ORE,1));
                increaseStats.changeEXP("mining",750);
            }
            else if (randomNum2 < 0.85) {
                world.dropItemNaturally(loc, new ItemStack(Material.LAPIS_LAZULI,1));
                increaseStats.changeEXP("mining",1600);
            }
            else if (randomNum2 < 0.90) {
                world.dropItemNaturally(loc, new ItemStack(Material.EMERALD,1));
                increaseStats.changeEXP("mining",1700);
            }
            else if (randomNum2 < 0.99) {
                world.dropItemNaturally(loc, new ItemStack(Material.REDSTONE,1));
                increaseStats.changeEXP("mining",100);
            }
            else {
                world.dropItemNaturally(loc, new ItemStack(Material.DIAMOND,1));
                increaseStats.changeEXP("mining",9000);
            }
        }
    }

    public void initiateAbility() {
        Integer[] pTimers = timers.getPlayerTimers();
        Integer[] pAbilities = abilities.getPlayerAbilities();
        if (pAbilities[2] == -1) {
            int cooldown = pTimers[2];
            if (cooldown < 1) {
                int prepMessages = (int) pStatClass.getPlayerData().get("global").get(22); //Toggle for preparation messages
                if (prepMessages > 0) {
                    p.sendMessage(ChatColor.GRAY + ">>>You prepare your pickaxe...<<<");
                }
                int taskID = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (prepMessages > 0) {
                            p.sendMessage(ChatColor.GRAY + ">>>...You rest your pickaxe<<<");
                        }
                        try {
                            abilities.setPlayerAbility( "mining", -1);
                        }
                        catch (Exception e) {

                        }
                    }
                }.runTaskLater(plugin, 20 * 4).getTaskId();
                abilities.setPlayerAbility( "mining", taskID);
            } else {
                p.sendMessage(ChatColor.RED + "You must wait " + cooldown + " seconds to use Berserk Pick again.");
            }
        }
    }

    public void enableAbility() {
        Integer[] pTimers = timers.getPlayerTimers();
        Integer[] pAbilities = abilities.getPlayerAbilities();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        p.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + ">>>Berserk Pick Activated!<<<");
        int effLevel = itemInHand.getEnchantmentLevel(Enchantment.DIG_SPEED);
        itemInHand.removeEnchantment(Enchantment.DIG_SPEED);
        itemInHand.addUnsafeEnchantment(Enchantment.DIG_SPEED, effLevel + 5);
        int durationLevel = (int) pStat.get("mining").get(4);
        double duration0 = Math.ceil(durationLevel * 0.4) + 40;
        int cooldown = 300;
        if ((int) pStat.get("global").get(11) > 0) {
            cooldown = 200;
        }
        int finalCooldown = cooldown;
        long duration = (long) duration0;
        timers.setPlayerTimer( "mining", finalCooldown);
        Bukkit.getScheduler().cancelTask(pAbilities[2]);
        abilities.setPlayerAbility( "mining", -2);
        int taskID = new BukkitRunnable() {
            @Override
            public void run() {
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + ">>>Berserk Pick has ended<<<");
                itemInHand.removeEnchantment(Enchantment.DIG_SPEED);
                if (effLevel != 0) {
                    itemInHand.addUnsafeEnchantment(Enchantment.DIG_SPEED, effLevel);
                }
                abilities.setPlayerAbility( "mining", -1);
                for (int i = 1; i < finalCooldown+1; i++) {
                    int timeRemaining = finalCooldown - i;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            timers.setPlayerTimer( "mining", timeRemaining);
                            AbilityTimers timers2 = new AbilityTimers(p);
                            if (timeRemaining ==0) {
                                if (!p.isOnline()) {
                                    timers2.removePlayer();
                                }
                                else {
                                    p.sendMessage(ChatColor.GREEN + ">>>Berserk Pick is ready to use again<<<");
                                }
                            }
                        }
                    }.runTaskLater(plugin, 20 * i);
                }
            }
        }.runTaskLater(plugin, duration).getTaskId();
        AbilityLogoutTracker incaseLogout = new AbilityLogoutTracker(p);
        incaseLogout.setPlayerItem(p,"mining",itemInHand);
        incaseLogout.setPlayerTask(p,"mining",taskID);
    }

    public void wastelessHaste() {
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int hasteSeconds = 0;
        int wastelessHasteLevel = (int) pStat.get("mining").get(7);
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
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int doubleDropLevel = (int) pStat.get("mining").get(5);
        double chance = 0.0005*doubleDropLevel;
        double randomNum = rand.nextDouble();
        if (chance > randomNum) {
            for (ItemStack stack : block.getDrops(itemInHand)) {
                world.dropItemNaturally(block.getLocation(), stack);
                if ((int) pStat.get("mining").get(13) > 0) {
                    world.dropItemNaturally(block.getLocation(), stack);
                }
            }
        }
    }

    public void preventLogoutTheft(int taskID_mining,ItemStack itemInHand_mining) {
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
            abilities.setPlayerAbility( "mining", -1);
            p.sendMessage(ChatColor.RED+ChatColor.BOLD.toString() + ">>>A magic force ends your ability<<<");
            for(int i = 1; i < finalCooldown+1; i++) {
                int timeRemaining = finalCooldown - i;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        AbilityTimers timers2 = new AbilityTimers(p);
                        timers2.setPlayerTimer( "mining", timeRemaining);
                        if (timeRemaining==0 && !p.isOnline()){
                            timers2.removePlayer();
                        }
                    }
                }.runTaskLater(plugin, 20*i);
            }

        }
    }

    public void getVeinOres(Block b1,final int x1, final int y1, final int z1) {
        int searchCubeSize = 7;
        Material ore = b1.getType();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }
                    Block b2 = b1.getRelative(x, y, z);
                    if (b2.getType().equals(ore)) {
                        if (b2.getX() > x1 + searchCubeSize || b2.getX() < x1 - searchCubeSize || b2.getY() > y1 + searchCubeSize || b2.getY() < y1 - searchCubeSize || b2.getZ() > z1 + searchCubeSize || b2.getZ() < z1 - searchCubeSize) {
                            break;
                        }
                        else if (!(veinOres.contains(b2))) {
                            veinOres.add(b2);
                            this.getVeinOres(b2, x1,y1,z1);
                        }
                    }
                }
            }
        }
    }

    public void veinMiner(Block initialBlock) {
        if (!(ores.contains(initialBlock.getType()))) {
            return;
        }
        World world = initialBlock.getWorld();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int veinMinerLevel = (int)pStat.get("mining").get(11);
        if (veinMinerLevel < 1) {
            return;
        }
        int veinMinerToggle = (int) pStat.get("global").get(18);
        if (veinMinerToggle < 1) {
            return;
        }
        ArrayList<Location> blocksLocations = placedClass.getBlocks();
        getVeinOres(initialBlock,initialBlock.getX(),initialBlock.getY(),initialBlock.getZ()); //Get Ores in Vein
        int numOres = veinOres.size();
        int totalOres = numOres;
        int doubleDropsLevel = (int)pStat.get("mining").get(5);
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
            boolean natural = true;
            for (Location blockLocation : blocksLocations) {
                if (blockLoc.equals(blockLocation)) {
                    blocksLocations.remove(blockLocation);
                    placedClass.setBlocks(blocksLocations);
                    numOres += -1; //Decrease the effective number of ores (used for giving EXP)
                    natural = false;
                    break;
                }
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
                            if ((int) pStat.get("mining").get(13) > 0) {
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
                            if ((int) pStat.get("mining").get(13) > 0) {
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
                        if ((int) pStat.get("mining").get(13) > 0) {
                            world.dropItemNaturally(blockLoc, drop);
                        }
                    }
                }
            }

        }
        //Give EXP
        if ((int) pStat.get("global").get(13) > 0 && (int) pStat.get("smelting").get(13) > 0) {
            if (initialBlock.getType() == Material.IRON_ORE) {
                increaseStats.changeEXP("smelting",300*totalOres);
            }
            else if (initialBlock.getType() == Material.GOLD_ORE) {
                increaseStats.changeEXP("smelting",320*totalOres);
            }
        }
        increaseStats.changeEXP("mining",getEXP(initialBlock.getType())*numOres);
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
        int EXP = 0;
        switch (brokenOre) {
            case COAL_ORE:
                EXP = 300;
                break;
            case NETHER_QUARTZ_ORE:
                EXP = 325;
                break;
            case IRON_ORE:
                EXP = 500;
                break;
            case GOLD_ORE:
                EXP = 700;
                break;
            case EMERALD_ORE:
                EXP = 4000;
                break;
            case REDSTONE_ORE:
                EXP = 700;
                break;
            case LAPIS_ORE:
                EXP = 1250;
                break;
            case DIAMOND_ORE:
                EXP = 2500;
                break;
            default:
                break;
        }
        return EXP;
    }
}
