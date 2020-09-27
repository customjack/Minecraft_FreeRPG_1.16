package mc.carlton.freerpg.perksAndAbilities;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.ActionBarMessages;
import mc.carlton.freerpg.gameTools.LanguageSelector;
import mc.carlton.freerpg.globalVariables.EntityGroups;
import mc.carlton.freerpg.globalVariables.ItemGroups;
import mc.carlton.freerpg.playerInfo.*;
import mc.carlton.freerpg.serverInfo.ConfigLoad;
import mc.carlton.freerpg.serverInfo.PlacedBlocksManager;
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
    private String skillName = "farming";
    ChangeStats increaseStats; //Changing Stats
    static Map<Player,Integer> oneWithNatureMap = new HashMap<>();
    static Map<Player,Integer> oneWithNatureCounters = new HashMap<>();
    Map<String,Integer> expMap;

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



    public Farming(Player p) {
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
        if (!p.hasPermission("freeRPG.farmingAbility")) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        if ((int) pStat.get("global").get(24) < 1 || !pStatClass.isPlayerSkillAbilityOn(skillName)) {
            return;
        }
        Integer[] pTimers = timers.getPlayerCooldownTimes();
        Integer[] pAbilities = abilities.getPlayerAbilities();
        if (pAbilities[3] == -1) {
            int cooldown = pTimers[3];
            if (cooldown < 1) {
                int prepMessages = (int) pStatClass.getPlayerData().get("global").get(22); //Toggle for preparation messages
                if (prepMessages > 0) {
                    actionMessage.sendMessage(ChatColor.GRAY + ">>>" + lang.getString("prepare") + " " + lang.getString("hoe") + "...<<<");
                }
                int taskID = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (prepMessages > 0) {
                            actionMessage.sendMessage(ChatColor.GRAY + ">>>..." + lang.getString("rest") + " " +lang.getString("hoe") + "<<<");
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
                actionMessage.sendMessage(ChatColor.RED +lang.getString("naturalRegeneration") + " " + lang.getString("cooldown") + ": " + ChatColor.WHITE + cooldown+ ChatColor.RED + "s");
            }
        }
    }

    public void enableAbility() {
        if (!runMethods) {
            return;
        }
        Integer[] pAbilities = abilities.getPlayerAbilities();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        actionMessage.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + ">>>" + lang.getString("naturalRegeneration") + " " + lang.getString("activated") + "<<<");
        int durationLevel = (int) pStat.get(skillName).get(4);
        double duration0 = Math.ceil(durationLevel * 0.4) + 40;
        long duration = (long) duration0;
        Bukkit.getScheduler().cancelTask(pAbilities[3]);
        abilities.setPlayerAbility( skillName, -2);
        String coolDownEndMessage = ChatColor.GREEN + ">>>" + lang.getString("naturalRegeneration") + " " + lang.getString("readyToUse") + "<<<";
        String endMessage = ChatColor.RED + ChatColor.BOLD.toString() + ">>>" + lang.getString("naturalRegeneration") + " " + lang.getString("ended") + "<<<";
        timers.abilityDurationTimer(skillName,duration,endMessage,coolDownEndMessage);
    }

    public void killFarmAnimalEXP(Entity animal) {
        if (!runMethods) {
            return;
        }
        Map<EntityType,Integer> farmAnimalsEXP = new HashMap<>();
        farmAnimalsEXP.put(EntityType.SHEEP,expMap.get("killSheep"));
        farmAnimalsEXP.put(EntityType.COW,expMap.get("killCow"));
        farmAnimalsEXP.put(EntityType.CHICKEN,expMap.get("killChicken"));
        farmAnimalsEXP.put(EntityType.PIG,expMap.get("killPig"));
        farmAnimalsEXP.put(EntityType.HORSE,expMap.get("killHorse"));
        farmAnimalsEXP.put(EntityType.RABBIT,expMap.get("killRabbit"));
        farmAnimalsEXP.put(EntityType.LLAMA,expMap.get("killLlama"));
        farmAnimalsEXP.put(EntityType.TURTLE,expMap.get("killTurtle"));
        if (farmAnimalsEXP.containsKey(animal.getType())) {
            increaseStats.changeEXP(skillName,farmAnimalsEXP.get(animal.getType()));
        }
    }

    public int getRandomAge(int maximumAge, int greenThumbLevel) {
        if (!runMethods) {
            return 0;
        }
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
        if (!runMethods) {
            return;
        }
        BlockData block_data = block.getBlockData();
        Material blockType = block.getType();
        ItemGroups itemGroups = new ItemGroups();
        List<Material> tallCrops = itemGroups.getTallCrops();
        if (tallCrops.contains(blockType)) {
            return;
        }

        Integer[] pAbilities = abilities.getPlayerAbilities();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int greenThumbLevel = (int)pStat.get(skillName).get(11);
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
        if (!runMethods) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int doubleDropsAnimals = (int) pStat.get(skillName).get(6);
        double doubleDropChance = doubleDropsAnimals*0.0005;
        if (doubleDropChance < rand.nextDouble()) {
            return;
        }
        EntityGroups entityGroups = new EntityGroups();
        List<EntityType> animals = entityGroups.getAnimals();
        if (animals.contains(entity.getType())) {
            for (ItemStack drop : drops) {
                world.dropItemNaturally(entity.getLocation().add(0,0.25,0), drop);
            }
        }
    }

    public void farmingDoubleDropCrop(Block block, World world) {
        if (!runMethods) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int doubleDropsCrop = (int) pStat.get(skillName).get(5);

        double doubleDropChance = doubleDropsCrop*0.0005;
        if (doubleDropChance < rand.nextDouble()) {
            return;
        }
        ItemGroups itemGroups = new ItemGroups();
        List<Material> crops = itemGroups.getCrops();
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
        if (!runMethods) {
            return;
        }
        ArrayList<Block> tallCropBlocks = getTallCropsBlocks(block,world);

        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int doubleDropsCrop = (int) pStat.get(skillName).get(5);

        double doubleDropChance = doubleDropsCrop*0.0005;
        int totalNatural = 0;
        for (Block b : tallCropBlocks) {
            //Checks if any of the blocks weren't natural
            PlacedBlocksManager placedBlocksManager = new PlacedBlocksManager();
            boolean natural = !placedBlocksManager.isBlockTracked(block);
            if (!natural) {
                placedBlocksManager.removeBlock(block);
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
                increaseStats.changeEXP(skillName,expMap.get("breakCactus"));
            case SUGAR_CANE:
                increaseStats.changeEXP(skillName,expMap.get("breakSugarCane")*totalNatural);
                break;
            case BAMBOO:
                increaseStats.changeEXP(skillName,expMap.get("breakBamboo")*totalNatural);
                break;
            case KELP:
            case KELP_PLANT:
                increaseStats.changeEXP(skillName,expMap.get("breakKelp")*totalNatural);
                break;
            default:
                break;
        }
    }

    public void eatFarmFood(ItemStack food) {
        if (!runMethods) {
            return;
        }
        ItemGroups itemGroups = new ItemGroups();
        Map<Material,Integer> farmFood = itemGroups.getFarmFood();
        Map<Material,Integer> meatFood = itemGroups.getMeatFood();
        if (!(farmFood.containsKey(food.getType()) || meatFood.containsKey(food.getType()))) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int farmDietLevel = (int) pStat.get(skillName).get(9);
        int meatEaterLevel = (int) pStat.get(skillName).get(10);
        if (farmDietLevel < 1 && meatEaterLevel < 1) {
            return;
        }
        Material foodType = food.getType();
        if (farmFood.containsKey(food.getType())) {
            double foodMultiplier = farmDietLevel*0.2;
            Map<Material,Double> farmFoodSaturation = itemGroups.getFarmFoodSaturation();
            p.setFoodLevel((int)Math.min(20,p.getFoodLevel() + Math.round(foodMultiplier * farmFood.get(foodType)) ));
            p.setSaturation((float)Math.min(p.getFoodLevel(),p.getSaturation()+(foodMultiplier*farmFoodSaturation.get(foodType)) ));
        }
        else if (meatFood.containsKey(food.getType())) {
            double foodMultiplier = meatEaterLevel*0.2;
            Map<Material,Double> meatFoodSaturation = itemGroups.getMeatFoodSaturation();
            p.setFoodLevel((int)Math.min(20, p.getFoodLevel()+Math.round(foodMultiplier*meatFood.get(foodType)) ));
            p.setSaturation((float)Math.min(p.getFoodLevel(),p.getSaturation()+(foodMultiplier*meatFoodSaturation.get(foodType)) ));
        }
    }

    public void babyAnimalGrow(Entity entity) {
        if (!runMethods) {
            return;
        }
        EntityGroups entityGroups = new EntityGroups();
        List<EntityType> babyAnimals = entityGroups.getBabyAnimals();
        if (babyAnimals.contains(entity.getType())) {
            if (entity instanceof org.bukkit.entity.Ageable) {
                boolean isAdult = ((org.bukkit.entity.Ageable) entity).isAdult();
                if (isAdult) {
                    return;
                }
                if (itemInHand.getType() == Material.SUGAR) {
                    Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
                    int growthHormonesLevel = (int) pStat.get(skillName).get(12);
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
        if (!runMethods) {
            return;
        }
        int amount = itemInHand.getAmount();
        new BukkitRunnable() {
            @Override
            public void run() {
                int newAmount = itemInHand.getAmount();
                if (amount > newAmount) {
                    increaseStats.changeEXP("farming", expMap.get("useBonemeal"));
                    Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
                    int betterFertilizerLevel = (int) pStat.get(skillName).get(7);
                    double refundChance = betterFertilizerLevel*0.1;
                    if (refundChance > rand.nextDouble()) {
                        itemInHand.setAmount(itemInHand.getAmount() + 1);
                    }
                }
            }
        }.runTaskLater(plugin, 1);
    }
    public void composterEXP(Block block) {
        if (!runMethods) {
            return;
        }
        if (block.getType() == Material.COMPOSTER) {
            BlockData data = block.getBlockData();
            int level = ((Levelled) data).getLevel();
            int maxLevel = ((Levelled) data).getMaximumLevel();
            if (level == maxLevel) {
                increaseStats.changeEXP(skillName,expMap.get("useComposter"));
            }
            else {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        BlockData newData = block.getBlockData();
                        int newLevel = ((Levelled) newData).getLevel();
                        if (newLevel > level) {
                            increaseStats.changeEXP(skillName,expMap.get("maximizeComposter"));
                        }
                    }
                }.runTaskLater(plugin, 3);
            }
        }
    }
    public void oneWithNatureStart() {
        if (!runMethods) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int oneWithNatureLevel = (int) pStat.get(skillName).get(13);
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
        if (!runMethods) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int oneWithNatureLevel = (int) pStat.get(skillName).get(13);
        if (oneWithNatureLevel > 0) {
            Bukkit.getScheduler().cancelTask(oneWithNatureMap.get(p));
            oneWithNatureMap.remove(p);
            oneWithNatureCounters.remove(p);
        }

    }

    public void shearSheep(Entity entity,World world) {
        if (!runMethods) {
            return;
        }
        if (!(entity instanceof Sheep)) {
            return;
        }
        increaseStats.changeEXP(skillName,expMap.get("shearSheep"));
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
        int animalDoubleDrop = (int) pStat.get(skillName).get(6);
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
        if (!runMethods) {
            return;
        }
        if (entity.getType() == EntityType.COW || entity.getType() == EntityType.MUSHROOM_COW) {
            if (itemInHand.getType() == Material.BUCKET) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        ItemStack newItem = p.getInventory().getItemInMainHand();
                        if (newItem.getType() == Material.MILK_BUCKET) {
                            increaseStats.changeEXP(skillName, expMap.get("milkAnimal"));
                        }
                    }
                }.runTaskLater(plugin, 1);
            }
        }
    }

    public void breedingEXP(Entity entity) {
        if (!runMethods) {
            return;
        }
        EntityGroups entityGroups = new EntityGroups();
        List<EntityType> breedingAnimals = entityGroups.getBreedingAnimalsFarming();
        if (breedingAnimals.contains(entity.getType())) {
            increaseStats.changeEXP(skillName,expMap.get("breedFarmAnimal"));
        }
    }


}

