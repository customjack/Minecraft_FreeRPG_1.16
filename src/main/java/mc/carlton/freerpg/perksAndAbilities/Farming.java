package mc.carlton.freerpg.perksAndAbilities;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.playerAndServerInfo.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.type.Cocoa;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Farming {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    private Player p;
    private String pName;
    private ItemStack itemInHand;
    ChangeStats increaseStats; //Changing Stats
    static Map<Player,Integer> oneWithNatureMap = new HashMap<>();
    static Map<Player,Integer> oneWithNatureCounters = new HashMap<>();

    AbilityTracker abilities; //Abilities class
    // GET ABILITY STATUSES LIKE THIS:   Integer[] pAbilities = abilities.getPlayerAbilities(p);

    AbilityTimers timers; //Ability Timers class
    //GET TIMERS LIKE THIS:              Integer[] pTimers = timers.getPlayerTimers(p);

    PlayerStats pStatClass;
    //GET PLAYER STATS LIKE THIS:        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData(p);

    PlacedBlocks placedClass;
    //GET TRACKED BLOCKS LIKE THIS:        ArrayList<Location> blocksLocations = placedClass.getBlocks();

    Random rand = new Random(); //Random class Import

    Material[] crops0 = {Material.WHEAT,Material.BEETROOTS,Material.CARROTS,Material.CHORUS_FLOWER,Material.MELON_STEM,Material.MELON,
                         Material.NETHER_WART,Material.POTATOES,Material.PUMPKIN_STEM,Material.PUMPKIN,Material.SWEET_BERRY_BUSH,Material.COCOA};
    List<Material> crops = Arrays.asList(crops0);
    Material[] tallCrops0 = {Material.SUGAR_CANE,Material.BAMBOO,Material.CACTUS,Material.KELP,Material.KELP_PLANT};
    List<Material> tallCrops = Arrays.asList(tallCrops0);
    EntityType[] animals0 = {EntityType.CHICKEN,EntityType.COW,EntityType.DONKEY,EntityType.FOX,EntityType.HORSE,EntityType.MUSHROOM_COW,
                            EntityType.MULE,EntityType.PARROT,EntityType.PIG,EntityType.RABBIT,EntityType.SHEEP,EntityType.SQUID,
                            EntityType.SKELETON_HORSE,EntityType.TURTLE};
    List<EntityType> animals = Arrays.asList(animals0);
    EntityType[] babyAnimals0 = {EntityType.MUSHROOM_COW,EntityType.COW,EntityType.SHEEP,EntityType.PIG,EntityType.CHICKEN,EntityType.RABBIT,
                                EntityType.WOLF,EntityType.CAT,EntityType.OCELOT,EntityType.LLAMA,EntityType.POLAR_BEAR,
                                EntityType.HORSE,EntityType.DONKEY,EntityType.MULE,EntityType.SKELETON_HORSE,EntityType.TURTLE,
                                EntityType.PANDA,EntityType.FOX,EntityType.BEE};
    List<EntityType> babyAnimals = Arrays.asList(babyAnimals0);
    Material[] hoes0 = {Material.DIAMOND_HOE,Material.GOLDEN_HOE,Material.IRON_HOE, Material.STONE_HOE,Material.WOODEN_HOE};
    List<Material> hoes = Arrays.asList(hoes0);
    EntityType[] breedingAnimals0 = {EntityType.MUSHROOM_COW,EntityType.COW,EntityType.SHEEP,EntityType.PIG,EntityType.CHICKEN,EntityType.RABBIT,
                                    EntityType.TURTLE, EntityType.PANDA,EntityType.FOX,EntityType.BEE};
    List<EntityType> breedingAnimals = Arrays.asList(breedingAnimals0);

    Map<Material,Integer> farmFood = new HashMap<Material,Integer>();
    Map<Material,Integer> meatFood = new HashMap<Material,Integer>();
    Map<Material,Double> farmFoodSaturation = new HashMap<Material,Double>();
    Map<Material,Double> meatFoodSaturation = new HashMap<Material,Double>();


    public Farming(Player p) {
        this.p = p;
        this.pName = p.getDisplayName();
        this.itemInHand = p.getInventory().getItemInMainHand();
        this.increaseStats = new ChangeStats(p);
        this.abilities = new AbilityTracker(p);
        this.timers = new AbilityTimers(p);
        this.pStatClass=  new PlayerStats(p);
        this.placedClass = new PlacedBlocks();

        farmFood.put(Material.GOLDEN_APPLE,4);
        farmFoodSaturation.put(Material.GOLDEN_APPLE,13.6);
        farmFood.put(Material.GOLDEN_CARROT,6);
        farmFoodSaturation.put(Material.GOLDEN_CARROT,14.4);
        farmFood.put(Material.BAKED_POTATO,5);
        farmFoodSaturation.put(Material.BAKED_POTATO,6.0);
        farmFood.put(Material.BEETROOT,1);
        farmFoodSaturation.put(Material.BEETROOT,1.2);
        farmFood.put(Material.BEETROOT_SOUP,6);
        farmFoodSaturation.put(Material.BEETROOT_SOUP,7.2);
        farmFood.put(Material.BREAD,5);
        farmFoodSaturation.put(Material.BREAD,6.0);
        farmFood.put(Material.CARROT,3);
        farmFoodSaturation.put(Material.CARROT,3.6);
        farmFood.put(Material.MUSHROOM_STEW,6);
        farmFoodSaturation.put(Material.MUSHROOM_STEW,13.2);
        farmFood.put(Material.APPLE,4);
        farmFoodSaturation.put(Material.APPLE,2.4);
        farmFood.put(Material.CHORUS_FRUIT,4);
        farmFoodSaturation.put(Material.CHORUS_FRUIT,2.4);
        farmFood.put(Material.DRIED_KELP,1);
        farmFoodSaturation.put(Material.DRIED_KELP,0.6);
        farmFood.put(Material.MELON_SLICE,2);
        farmFoodSaturation.put(Material.MELON_SLICE,1.2);
        farmFood.put(Material.POTATO,1);
        farmFoodSaturation.put(Material.POTATO,0.6);
        farmFood.put(Material.PUMPKIN_PIE,8);
        farmFoodSaturation.put(Material.PUMPKIN_PIE,4.8);
        farmFood.put(Material.CAKE,2);
        farmFoodSaturation.put(Material.CAKE,0.4);
        farmFood.put(Material.COOKIE,2);
        farmFoodSaturation.put(Material.COOKIE,0.4);
        farmFood.put(Material.HONEY_BOTTLE,6);
        farmFoodSaturation.put(Material.HONEY_BOTTLE,1.2);
        farmFood.put(Material.SWEET_BERRIES,2);
        farmFoodSaturation.put(Material.SWEET_BERRIES,0.4);

        meatFood.put(Material.COOKED_MUTTON,6);
        meatFoodSaturation.put(Material.COOKED_MUTTON,9.6);
        meatFood.put(Material.COOKED_PORKCHOP,8);
        meatFoodSaturation.put(Material.COOKED_PORKCHOP,12.8);
        meatFood.put(Material.COOKED_BEEF,8);
        meatFoodSaturation.put(Material.COOKED_BEEF,12.8);
        meatFood.put(Material.COOKED_CHICKEN,6);
        meatFoodSaturation.put(Material.COOKED_CHICKEN,7.2);
        meatFood.put(Material.COOKED_RABBIT,5);
        meatFoodSaturation.put(Material.COOKED_RABBIT,6.0);
        meatFood.put(Material.RABBIT_STEW,10);
        meatFoodSaturation.put(Material.RABBIT_STEW,12.0);
        meatFood.put(Material.BEEF,3);
        meatFoodSaturation.put(Material.BEEF,1.8);
        meatFood.put(Material.MUTTON,2);
        meatFoodSaturation.put(Material.MUTTON,1.2);
        meatFood.put(Material.PORKCHOP,3);
        meatFoodSaturation.put(Material.PORKCHOP,1.8);
        meatFood.put(Material.RABBIT,3);
        meatFoodSaturation.put(Material.RABBIT,1.8);
        meatFood.put(Material.CHICKEN,2);
        meatFoodSaturation.put(Material.CHICKEN,1.2);
        meatFood.put(Material.ROTTEN_FLESH,4);
        meatFoodSaturation.put(Material.ROTTEN_FLESH,0.8);
    }

    public void initiateAbility() {
        Integer[] pTimers = timers.getPlayerTimers();
        Integer[] pAbilities = abilities.getPlayerAbilities();
        if (pAbilities[3] == -1) {
            int cooldown = pTimers[3];
            if (cooldown < 1) {
                int prepMessages = (int) pStatClass.getPlayerData().get("global").get(22); //Toggle for preparation messages
                if (prepMessages > 0) {
                    p.sendMessage(ChatColor.GRAY + ">>>You prepare your hoe...<<<");
                }
                int taskID = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (prepMessages > 0) {
                            p.sendMessage(ChatColor.GRAY + ">>>...You rest your hoe<<<");
                        }
                        try {
                            abilities.setPlayerAbility( "farming", -1);
                        }
                        catch (Exception e) {

                        }
                    }
                }.runTaskLater(plugin, 20 * 4).getTaskId();
                abilities.setPlayerAbility( "farming", taskID);
            } else {
                p.sendMessage(ChatColor.RED + "You must wait " + cooldown + " seconds to use Natural Regeneration again.");
            }
        }
    }

    public void enableAbility() {
        Integer[] pAbilities = abilities.getPlayerAbilities();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        p.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + ">>>Natural Regeneration Activated!<<<");
        int durationLevel = (int) pStat.get("farming").get(4);
        double duration0 = Math.ceil(durationLevel * 0.4) + 40;
        int cooldown = 300;
        if ((int) pStat.get("global").get(11) > 0) {
            cooldown = 200;
        }
        int finalCooldown = cooldown;
        long duration = (long) duration0;
        timers.setPlayerTimer("farming", finalCooldown);
        Bukkit.getScheduler().cancelTask(pAbilities[3]);
        abilities.setPlayerAbility("farming", -2);
        int taskID = new BukkitRunnable() {
            @Override
            public void run() {
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + ">>>Natural Regeneration has ended<<<");
                abilities.setPlayerAbility( "farming", -1);
                for (int i = 1; i < finalCooldown+1; i++) {
                    int timeRemaining = finalCooldown - i;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            timers.setPlayerTimer("farming", timeRemaining);
                            AbilityTimers timers2 = new AbilityTimers(p);
                            if (timeRemaining ==0) {
                                if (!p.isOnline()) {
                                    timers2.removePlayer();
                                }
                                else {
                                    p.sendMessage(ChatColor.GREEN + ">>>Natural Regeneration is ready to use again<<<");
                                }
                            }
                        }
                    }.runTaskLater(plugin, 20 * i);
                }
            }
        }.runTaskLater(plugin, duration).getTaskId();
    }

    public void killFarmAnimalEXP(Entity animal) {
        Map<EntityType,Integer> farmAnimalsEXP = new HashMap<>();
        farmAnimalsEXP.put(EntityType.SHEEP,100);
        farmAnimalsEXP.put(EntityType.COW,100);
        farmAnimalsEXP.put(EntityType.CHICKEN,100);
        farmAnimalsEXP.put(EntityType.PIG,100);
        farmAnimalsEXP.put(EntityType.HORSE,50);
        farmAnimalsEXP.put(EntityType.RABBIT,150);
        farmAnimalsEXP.put(EntityType.LLAMA,50);
        farmAnimalsEXP.put(EntityType.TURTLE,100);
        if (farmAnimalsEXP.containsKey(animal.getType())) {
            increaseStats.changeEXP("farming",farmAnimalsEXP.get(animal.getType()));
        }
    }

    public int getRandomAge(int maximumAge, int greenThumbLevel) {
        int age = 0;
        ArrayList<Double> pDist = new ArrayList<>();
        ArrayList<Double> pMass = new ArrayList<>();
        double sum = 0;
        if (greenThumbLevel > 0) {
            if (maximumAge == 0) {
                if (rand.nextDouble() > 0.5) {
                    return 1;
                }
                else {
                    return 0;
                }
            }
            else {
                double c = -0.06;
                for (int i = 0; i <= maximumAge; i++) {
                    pDist.add(0.0);
                    pMass.add(0.0);
                }
                for (int i = 0; i <= maximumAge; i++) {
                    double unNormalizedProb = Math.exp(c*i);
                    pDist.set(i,unNormalizedProb);
                    sum+= unNormalizedProb;
                }
                for (int i = 0; i <= maximumAge; i++) {
                    pDist.set(i,pDist.get(i)/sum);
                }
                for (int i = 0; i <= maximumAge; i++) {
                    if (i==0) {
                        pMass.set(i,pDist.get(i));
                    }
                    else {
                        pMass.set(i,pMass.get(i-1)+pDist.get(i));
                    }
                }
                double randomNum = rand.nextDouble();
                randomAgeLoop:
                for (int i = 0; i <= maximumAge; i++) {
                    if (randomNum < pMass.get(i)){
                        age = i;
                        break randomAgeLoop;
                    }
                }

            }
        }
        else {
            if (maximumAge == 0) {
                return 0;
            }
            else {
                double c = -0.2;
                for (int i = 0; i < maximumAge; i++) {
                    pDist.add(0.0);
                    pMass.add(0.0);
                }
                for (int i = 0; i < maximumAge; i++) {
                    double unNormalizedProb = Math.exp(c*i);
                    pDist.set(i,unNormalizedProb);
                    sum+= unNormalizedProb;
                }
                for (int i = 0; i < maximumAge; i++) {
                    pDist.set(i,pDist.get(i)/sum);
                }
                for (int i = 0; i < maximumAge; i++) {
                    if (i==0) {
                        pMass.set(i,pDist.get(i));
                    }
                    else {
                        pMass.set(i,pMass.get(i-1)+pDist.get(i));
                    }
                }
                double randomNum = rand.nextDouble();
                randomAgeLoop:
                for (int i = 0; i < maximumAge; i++) {
                    if (randomNum < pMass.get(i)){
                        age = i;
                        break randomAgeLoop;
                    }
                }

            }
        }

        return age;
    }

    public void naturalRegeneration(Block block, World world) {
        BlockData block_data = block.getBlockData();
        Material blockType = block.getType();
        if (tallCrops.contains(blockType)) {
            return;
        }

        Integer[] pAbilities = abilities.getPlayerAbilities();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int greenThumbLevel = (int)pStat.get("farming").get(11);
        if (pAbilities[3] == -2) {
            ItemMeta toolMeta = itemInHand.getItemMeta();
            if (toolMeta instanceof Damageable) {
                ((Damageable) toolMeta).setDamage(((Damageable) toolMeta).getDamage()+1);
                itemInHand.setItemMeta(toolMeta);
                if (((Damageable) toolMeta).getDamage() > itemInHand.getType().getMaxDurability()) {
                    itemInHand.setAmount(0);
                    p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, 1);
                }
            }
            if (block_data instanceof Ageable) {
                Ageable age = (Ageable) block_data;
                if (age.getAge() != age.getMaximumAge()) {
                    block.setType(Material.AIR);
                }
                int randomAge = getRandomAge(age.getMaximumAge(),greenThumbLevel);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        block.setType(blockType);
                        ((Ageable) block_data).setAge(randomAge);
                        block.setBlockData(block_data);
                    }
                }.runTaskLater(plugin, 5);
            } else if (block_data instanceof Cocoa) {
                Cocoa coco = (Cocoa) block_data;
                if (coco.getAge() != coco.getMaximumAge()) {
                    block.setType(Material.AIR);
                }
                int randomAge = getRandomAge(coco.getMaximumAge(),greenThumbLevel);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        block.setType(blockType);
                        ((Cocoa) block_data).setAge(randomAge);
                        block.setBlockData(block_data);
                    }
                }.runTaskLater(plugin, 5);
            } else {
                int randomAge = getRandomAge(0,greenThumbLevel);
                if (randomAge == 1) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            block.setType(blockType);
                        }
                    }.runTaskLater(plugin, 1);
                }
            }
        }
    }

    public void animalDoubleDrops(Entity entity, World world,List<ItemStack> drops) {
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int doubleDropsAnimals = (int) pStat.get("farming").get(6);
        double doubleDropChance = doubleDropsAnimals*0.0005;
        if (doubleDropChance < rand.nextDouble()) {
            return;
        }
        if (animals.contains(entity.getType())) {
            for (ItemStack drop : drops) {
                world.dropItemNaturally(entity.getLocation().add(0,0.25,0), drop);
            }
        }
    }

    public void farmingDoubleDropCrop(Block block, World world) {
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int doubleDropsCrop = (int) pStat.get("farming").get(5);

        double doubleDropChance = doubleDropsCrop*0.0005;
        if (doubleDropChance < rand.nextDouble()) {
            return;
        }
        if (crops.contains(block.getType())) {
            Collection<ItemStack> drops = block.getDrops(itemInHand);
            for (ItemStack drop : drops) {
                world.dropItemNaturally(block.getLocation(), drop);
            }
        }
    }

    public ArrayList<Block> getTallCropsBlocks(Block b_0, World world) {
        ArrayList<Block> tallCropBlocks = new ArrayList<>();
        Block b_i = b_0;
        while (b_0.getType() == b_i.getType()) {
            tallCropBlocks.add(b_i);
            b_i = b_i.getRelative(0,1,0);
        }
        return tallCropBlocks;
    }

    public void tallCrops(Block block, World world) {
        ArrayList<Location> blocksLocations = placedClass.getBlocks();
        ArrayList<Block> tallCropBlocks = getTallCropsBlocks(block,world);

        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int doubleDropsCrop = (int) pStat.get("farming").get(5);

        double doubleDropChance = doubleDropsCrop*0.0005;
        int totalNatural = 0;
        for (Block b : tallCropBlocks) {
            Location blockLoc = b.getLocation();
            boolean natural = true;
            innerLoop:
            for (Location blockLocation : blocksLocations) {
                if (blockLoc.equals(blockLocation)) {
                    blocksLocations.remove(blockLocation);
                    placedClass.setBlocks(blocksLocations);
                    natural = false;
                    break innerLoop;
                }
            }
            if (natural) {
                totalNatural+=1;
                double randomNum = rand.nextDouble();
                if (doubleDropChance > randomNum) {
                    for (ItemStack item : b.getDrops(itemInHand)) {
                        world.dropItemNaturally(b.getLocation(),item);
                    }
                }
                }
            }
        switch (block.getType()) {
            case CACTUS:
            case SUGAR_CANE:
                increaseStats.changeEXP("farming",125*totalNatural);
                break;
            case BAMBOO:
                increaseStats.changeEXP("farming",30*totalNatural);
                break;
            case KELP:
            case KELP_PLANT:
                increaseStats.changeEXP("farming",20*totalNatural);
                break;
            default:
                break;
        }
    }

    public void eatFarmFood(ItemStack food) {
        if (!(farmFood.containsKey(food.getType()) || meatFood.containsKey(food.getType()))) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int farmDietLevel = (int) pStat.get("farming").get(9);
        int meatEaterLevel = (int) pStat.get("farming").get(10);
        if (farmDietLevel < 1 && meatEaterLevel < 1) {
            return;
        }
        Material foodType = food.getType();
        if (farmFood.containsKey(food.getType())) {
            double foodMultiplier = farmDietLevel*0.2;
            p.setFoodLevel((int)Math.min(20,p.getFoodLevel() + Math.round(foodMultiplier * farmFood.get(foodType)) ));
            p.setSaturation((float)Math.min(p.getFoodLevel(),p.getSaturation()+(foodMultiplier*farmFoodSaturation.get(foodType)) ));
        }
        else if (meatFood.containsKey(food.getType())) {
            double foodMultiplier = meatEaterLevel*0.2;
            p.setFoodLevel((int)Math.min(20, p.getFoodLevel()+Math.round(foodMultiplier*meatFood.get(foodType)) ));
            p.setSaturation((float)Math.min(p.getFoodLevel(),p.getSaturation()+(foodMultiplier*meatFoodSaturation.get(foodType)) ));
        }
    }

    public void babyAnimalGrow(Entity entity) {
        if (babyAnimals.contains(entity.getType())) {
            if (entity instanceof org.bukkit.entity.Ageable) {
                boolean isAdult = ((org.bukkit.entity.Ageable) entity).isAdult();
                if (isAdult) {
                    return;
                }
                if (itemInHand.getType() == Material.SUGAR) {
                    Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
                    int growthHormonesLevel = (int) pStat.get("farming").get(12);
                    if (growthHormonesLevel > 0) {
                        ((org.bukkit.entity.Ageable) entity).setAdult();
                        int numSugar = itemInHand.getAmount();
                        itemInHand.setAmount(numSugar-1);
                    }
                }
            }
        }
    }
    public void fertilizerSave(Block block) {
        int amount = itemInHand.getAmount();
        new BukkitRunnable() {
            @Override
            public void run() {
                int newAmount = itemInHand.getAmount();
                if (amount > newAmount) {
                    Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
                    int betterFertilizerLevel = (int) pStat.get("farming").get(7);
                    double refundChance = betterFertilizerLevel*0.1;
                    if (refundChance > rand.nextDouble()) {
                        itemInHand.setAmount(itemInHand.getAmount() + 1);
                    }
                }
            }
        }.runTaskLater(plugin, 1);
    }
    public void composterEXP(Block block) {
        if (block.getType() == Material.COMPOSTER) {
            BlockData data = block.getBlockData();
            int level = ((Levelled) data).getLevel();
            int maxLevel = ((Levelled) data).getMaximumLevel();
            if (level == maxLevel) {
                increaseStats.changeEXP("farming",300);
            }
            else {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        BlockData newData = block.getBlockData();
                        int newLevel = ((Levelled) newData).getLevel();
                        if (newLevel > level) {
                            increaseStats.changeEXP("farming",100);
                        }
                    }
                }.runTaskLater(plugin, 3);
            }
        }
    }
    public void oneWithNatureStart() {
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int oneWithNatureLevel = (int) pStat.get("farming").get(13);
        if (oneWithNatureLevel > 0) {
            int natureID = new BukkitRunnable() {
                @Override
                public void run() {
                    oneWithNatureCounters.put(p,0);
                    Block blockBelow = p.getLocation().getBlock().getRelative(BlockFace.DOWN);
                    int counter = 0;
                    if (blockBelow.getType() == Material.GRASS_BLOCK) {
                        for (int i = 1; i <= 20; i++) {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    int count = 0;
                                    try {
                                        count = oneWithNatureCounters.get(p);
                                    }
                                    catch (NullPointerException playerLeft) {
                                        return;
                                    }
                                    if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(blockBelow)) {
                                        count += 1;
                                        oneWithNatureCounters.put(p,count);
                                    }
                                    if (count == 20) {
                                        boolean addEffect = true;
                                        boolean hasEffect = false;
                                        potionEffectLoop:
                                        for (PotionEffect effect : p.getActivePotionEffects()) {
                                            if (effect.getType().equals(PotionEffectType.REGENERATION)) {
                                                hasEffect = true;
                                                if (effect.getDuration() > 2*20) {
                                                    addEffect = false;
                                                }
                                                break potionEffectLoop;
                                            }
                                        }
                                        if (addEffect) {
                                            if (hasEffect){
                                                p.removePotionEffect(PotionEffectType.REGENERATION);
                                            }
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 3 * 20, 0));
                                        }
                                    }
                                }
                            }.runTaskLater(plugin,i);
                        }
                    }
                }
            }.runTaskTimer(plugin, 40,40).getTaskId();
            oneWithNatureMap.put(p,natureID);
            oneWithNatureCounters.put(p,0);
        }
    }
    public void oneWithNatureEnd(){
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int oneWithNatureLevel = (int) pStat.get("farming").get(13);
        if (oneWithNatureLevel > 0) {
            Bukkit.getScheduler().cancelTask(oneWithNatureMap.get(p));
            oneWithNatureMap.remove(p);
            oneWithNatureCounters.remove(p);
        }

    }

    public void shearSheep(Entity entity,World world) {
        if (!(entity instanceof Sheep)) {
            return;
        }
        increaseStats.changeEXP("farming",200);
        Location dropLoc = entity.getLocation().add(0,0.5,0);
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        DyeColor color = ((Sheep) entity).getColor();
        ((Sheep) entity).setSheared(true);
        ItemStack wool = new ItemStack(Material.WHITE_WOOL,1);
        switch (color) {
            case ORANGE:
                wool.setType(Material.ORANGE_WOOL);
                break;
            case MAGENTA:
                wool.setType(Material.MAGENTA_WOOL);
                break;
            case LIGHT_BLUE:
                wool.setType(Material.LIGHT_BLUE_WOOL);
                break;
            case YELLOW:
                wool.setType(Material.YELLOW_WOOL);
                break;
            case LIME:
                wool.setType(Material.LIME_WOOL);
                break;
            case PINK:
                wool.setType(Material.PINK_WOOL);
                break;
            case GRAY:
                wool.setType(Material.GRAY_WOOL);
                break;
            case LIGHT_GRAY:
                wool.setType(Material.LIGHT_GRAY_WOOL);
                break;
            case CYAN:
                wool.setType(Material.CYAN_WOOL);
                break;
            case PURPLE:
                wool.setType(Material.PURPLE_WOOL);
                break;
            case BLUE:
                wool.setType(Material.BLUE_WOOL);
                break;
            case BROWN:
                wool.setType(Material.BROWN_WOOL);
                break;
            case GREEN:
                wool.setType(Material.GREEN_WOOL);
                break;
            case RED:
                wool.setType(Material.RED_WOOL);
                break;
            case BLACK:
                wool.setType(Material.BLACK_WOOL);
                break;
            default:
                break;
        }
        int animalDoubleDrop = (int) pStat.get("farming").get(6);
        double doubleDropChance = animalDoubleDrop*0.0005;
        int dropMultiplier = 1;
        if (doubleDropChance > rand.nextDouble()) {
            dropMultiplier = 2;
        }
        wool.setAmount(dropMultiplier);
        double woolRoll = rand.nextDouble();
        if (woolRoll < 0.3333) {
            world.dropItemNaturally(dropLoc,wool);
        }
        else if (woolRoll < 0.6666) {
            wool.setAmount(wool.getAmount()*2);
            world.dropItemNaturally(dropLoc,wool);
        }
        else {
            wool.setAmount(wool.getAmount()*3);
            world.dropItemNaturally(dropLoc,wool);
        }
    }

    public void milkingEXP(Entity entity) {
        if (entity.getType() == EntityType.COW || entity.getType() == EntityType.MUSHROOM_COW) {
            if (itemInHand.getType() == Material.BUCKET) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        ItemStack newItem = p.getInventory().getItemInMainHand();
                        if (newItem.getType() == Material.MILK_BUCKET) {
                            increaseStats.changeEXP("farming", 100);
                        }
                    }
                }.runTaskLater(plugin, 1);
            }
        }
    }

    public void breedingEXP(Entity entity) {
        if (breedingAnimals.contains(entity.getType())) {
            increaseStats.changeEXP("farming",500);
        }
    }


}

