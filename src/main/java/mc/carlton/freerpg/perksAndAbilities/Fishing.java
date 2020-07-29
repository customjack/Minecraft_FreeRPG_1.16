package mc.carlton.freerpg.perksAndAbilities;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.ActionBarMessages;
import mc.carlton.freerpg.gameTools.PsuedoEnchanting;
import mc.carlton.freerpg.playerAndServerInfo.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class Fishing {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    private Player p;
    private String pName;
    private ItemStack itemInHand;
    static Map<Player,Integer> fishPersonMap = new HashMap<>();
    static Map<Player,Integer> fishPersonCounters = new HashMap<>();
    ChangeStats increaseStats; //Changing Stats

    AbilityTracker abilities; //Abilities class
    // GET ABILITY STATUSES LIKE THIS:   Integer[] pAbilities = abilities.getPlayerAbilities(p);

    AbilityTimers timers; //Ability Timers class
    //GET TIMERS LIKE THIS:              Integer[] pTimers = timers.getPlayerTimers(p);

    PlayerStats pStatClass;
    //GET PLAYER STATS LIKE THIS:        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData(p);

    ActionBarMessages actionMessage;

    Random rand = new Random(); //Random class Import

    Map<Material,Integer> fishFood = new HashMap<Material,Integer>();
    Map<Material,Double> fishFoodSaturation = new HashMap<Material,Double>();
    ArrayList<UUID> superBaitBlock = new ArrayList<>();



    public Fishing(Player p) {
        this.p = p;
        this.pName = p.getDisplayName();
        this.itemInHand = p.getInventory().getItemInMainHand();
        this.increaseStats = new ChangeStats(p);
        this.abilities = new AbilityTracker(p);
        this.timers = new AbilityTimers(p);
        this.pStatClass=  new PlayerStats(p);
        this.actionMessage = new ActionBarMessages(p);

        fishFood.put(Material.COOKED_SALMON,6);
        fishFoodSaturation.put(Material.COOKED_SALMON,1.6);
        fishFood.put(Material.COOKED_COD,5);
        fishFoodSaturation.put(Material.COOKED_COD,1.2);
        fishFood.put(Material.COD,2);
        fishFoodSaturation.put(Material.COD,0.4);
        fishFood.put(Material.SALMON,2);
        fishFoodSaturation.put(Material.SALMON,0.4);
        fishFood.put(Material.TROPICAL_FISH,1);
        fishFoodSaturation.put(Material.TROPICAL_FISH,0.2);
        fishFood.put(Material.PUFFERFISH,1);
        fishFoodSaturation.put(Material.PUFFERFISH,0.2);
        fishFood.put(Material.DRIED_KELP,1);
        fishFoodSaturation.put(Material.DRIED_KELP,0.6);
    }


    public void initiateAbility() {
        if (!p.hasPermission("freeRPG.fishingAbility")) {
            return;
        }
        Integer[] pTimers = timers.getPlayerTimers();
        Integer[] pAbilities = abilities.getPlayerAbilities();
        if (pAbilities[4] == -1) {
            int cooldown = pTimers[4];
            if (cooldown < 1) {
                int prepMessages = (int) pStatClass.getPlayerData().get("global").get(22); //Toggle for preparation messages
                if (prepMessages > 0) {
                    actionMessage.sendMessage(ChatColor.GRAY + ">>>You prepare your fishing rod...<<<");
                }
                int taskID = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (prepMessages > 0) {
                            actionMessage.sendMessage(ChatColor.GRAY + ">>>...You rest your fishing rod<<<");
                        }
                        try {
                            abilities.setPlayerAbility( "fishing", -1);
                        }
                        catch (Exception e) {

                        }
                    }
                }.runTaskLater(plugin, 20 * 4).getTaskId();
                abilities.setPlayerAbility( "fishing", taskID);
            } else {
                actionMessage.sendMessage(ChatColor.RED + "You must wait " + cooldown + " seconds to use Super-bait again.");
            }
        }
    }

    public void enableAbility() {
        Integer[] pAbilities = abilities.getPlayerAbilities();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        actionMessage.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + ">>>Super-bait Activated!<<<");
        int durationLevel = (int) pStat.get("fishing").get(4);
        double duration0 = Math.ceil(durationLevel * 0.2) + 20;
        int cooldown = 300;
        if ((int) pStat.get("global").get(11) > 0) {
            cooldown = 200;
        }
        int finalCooldown = cooldown;
        long duration = (long) duration0;
        timers.setPlayerTimer( "fishing", finalCooldown);
        Bukkit.getScheduler().cancelTask(pAbilities[4]);
        abilities.setPlayerAbility( "fishing", -2);
        int taskID = new BukkitRunnable() {
            @Override
            public void run() {
                actionMessage.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + ">>>Super-bait has ended<<<");
                abilities.setPlayerAbility( "fishing", -1);
                for (int i = 1; i < finalCooldown+1; i++) {
                    int timeRemaining = finalCooldown - i;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            timers.setPlayerTimer( "fishing", timeRemaining);
                            AbilityTimers timers2 = new AbilityTimers(p);
                            if (timeRemaining ==0) {
                                if (!p.isOnline()) {
                                    timers2.removePlayer();
                                }
                                else {
                                    actionMessage.sendMessage(ChatColor.GREEN + ">>>Super-bait is ready to use again<<<");
                                }
                            }
                        }
                    }.runTaskLater(plugin, 20 * i);
                }
            }
        }.runTaskLater(plugin, duration).getTaskId();
        AbilityLogoutTracker incaseLogout = new AbilityLogoutTracker(p);
        incaseLogout.setPlayerItem(p,"fishing",itemInHand);
        incaseLogout.setPlayerTask(p,"fishing",taskID);
    }

    public void killFishEXP(Entity fish) {
        Map<EntityType,Integer> fishMap = new HashMap<>();
        fishMap.put(EntityType.COD,750);
        fishMap.put(EntityType.SALMON,1000);
        fishMap.put(EntityType.PUFFERFISH,2500);
        fishMap.put(EntityType.TROPICAL_FISH,1250);
        if (fishMap.containsKey(fish.getType())) {
            increaseStats.changeEXP("fishing",fishMap.get(fish.getType()));
        }
    }

    /* Old Perk
    public void waterBreather() {
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int waterBreatherLevel = (int) pStat.get("fishing").get(11);
        if (waterBreatherLevel > 0) {
            boolean addEffect = true;
            boolean hasEffect = false;
            potionEffectLoop:
            for (PotionEffect effect : p.getActivePotionEffects()) {
                if (effect.getType().equals(PotionEffectType.WATER_BREATHING)) {
                    hasEffect = true;
                    if (effect.getDuration() > 89 * 20) {
                        addEffect = false;
                    }
                    break potionEffectLoop;
                }
            }
            if (addEffect) {
                if (hasEffect) {
                    p.removePotionEffect(PotionEffectType.WATER_BREATHING);
                }
                p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 20 * 90, 0));
            }
        }
    }

     */

    public void grapplingHook(FishHook fishhook,World world) {
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int grapplingHookLevel = (int) pStat.get("fishing").get(11);
        int grappleToggle = (int) pStat.get("global").get(16);
        if (grapplingHookLevel < 1) {
            return;
        }
        if (grappleToggle < 1) {
            return;
        }
        Location location = fishhook.getLocation();
        double dx = location.getX() - p.getLocation().getX();
        double dy = location.getY() - p.getLocation().getY();
        double dz = location.getZ() - p.getLocation().getZ();
        double distance = Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
        double multiplier = 0.200;
        Vector velocity = new Vector(dx * multiplier, (dy * multiplier) + (double) Math.sqrt(distance) * 0.10, dz * multiplier);
        p.setVelocity(velocity);
    }

    public void superBait(FishHook fishhook, Entity hookedEntity,World world) {
        if (hookedEntity instanceof Item) {
            ((Item) hookedEntity).setItemStack(new ItemStack(Material.DIRT,0));
            ItemStack drop = dropTable(false);
            ((Item) hookedEntity).setItemStack(drop);
        }
        if (hookedEntity == null) {
            Location location = fishhook.getLocation();
            if (world.getBlockAt(location).getType() == Material.WATER) {
                if (!superBaitBlock.contains(p.getUniqueId())) {
                    double dx = p.getLocation().getX() - location.getX();
                    double dy = p.getLocation().getY() - location.getY();
                    double dz = p.getLocation().getZ() - location.getZ();
                    double distance = Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
                    double multiplier = 0.08;
                    ItemStack drop = dropTable(true);
                    Item droppedItem = world.dropItemNaturally(location, drop);
                    Vector velocity = new Vector(dx * multiplier, (dy * multiplier) + (double) Math.sqrt(distance) * 0.1, dz * multiplier);
                    droppedItem.setVelocity(velocity);
                    ((ExperienceOrb) world.spawn(p.getLocation(), ExperienceOrb.class)).setExperience(rand.nextInt(6) + 1);
                    ItemMeta toolMeta = itemInHand.getItemMeta();
                    if (toolMeta instanceof Damageable) {
                        ((Damageable) toolMeta).setDamage(((Damageable) toolMeta).getDamage()+1);
                        itemInHand.setItemMeta(toolMeta);
                        if (((Damageable) toolMeta).getDamage() > itemInHand.getType().getMaxDurability()) {
                            itemInHand.setAmount(0);
                            p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, 1);
                        }
                    }
                    superBaitBlock.add(p.getUniqueId());
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            superBaitBlock.remove(p.getUniqueId());
                        }
                    }.runTaskLater(plugin, 5);
                }


                /* I believe the damage is already taken into account
                ItemMeta toolMeta = itemInHand.getItemMeta();
                if (toolMeta instanceof Damageable) {
                    ((Damageable) toolMeta).setDamage(((Damageable) toolMeta).getDamage()+1);
                    itemInHand.setItemMeta(toolMeta);
                    if (((Damageable) toolMeta).getDamage() > itemInHand.getType().getMaxDurability()) {
                        itemInHand.setAmount(0);
                        p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, 1);
                    }
                }

                 */

            }
        }

    }

    public void normalCatch(FishHook fishhook, Entity hookedEntity,World world) {
        if (hookedEntity == null) {
            return;
        }
        if (hookedEntity instanceof Item) {
            ItemStack drop = dropTable(false);
            ((Item) hookedEntity).setItemStack(drop);

        }
    }

    public void fishPersonStart() {
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int fishPersonLevel = (int) pStat.get("fishing").get(13);
        if (fishPersonLevel > 0) {
            int fishID = new BukkitRunnable() {
                @Override
                public void run() {
                    fishPersonCounters.put(p,0);
                    Block block = p.getLocation().getBlock();
                    Block blockBelow = block.getRelative(BlockFace.DOWN);
                    if (block.getType() == Material.WATER || blockBelow.getType() == Material.WATER) {
                        for (int i = 1; i <= 20; i++) {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    int count = 0;
                                    try {
                                        count = fishPersonCounters.get(p);
                                    }
                                    catch (NullPointerException playerLeft) {
                                        return;
                                    }
                                    if (p.getLocation().getBlock().getType() == Material.WATER) {
                                        count += 1;
                                        fishPersonCounters.put(p,count);
                                    }

                                    if (count == 20) {
                                        boolean addEffect = true;
                                        boolean hasEffect = false;
                                        potionEffectLoop:
                                        for (PotionEffect effect : p.getActivePotionEffects()) {
                                            if (effect.getType().equals(PotionEffectType.DOLPHINS_GRACE)) {
                                                hasEffect = true;
                                                if (effect.getDuration() > 12*20) {
                                                    addEffect = false;
                                                }
                                                break potionEffectLoop;
                                            }
                                        }
                                        if (addEffect) {
                                            if (hasEffect){
                                                p.removePotionEffect(PotionEffectType.DOLPHINS_GRACE);
                                            }
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 15 * 20, 0));
                                        }

                                        boolean addEffect2 = true;
                                        boolean hasEffect2 = false;
                                        potionEffectLoop:
                                        for (PotionEffect effect : p.getActivePotionEffects()) {
                                            if (effect.getType().equals(PotionEffectType.NIGHT_VISION)) {
                                                hasEffect2 = true;
                                                if (effect.getDuration() > 12*20) {
                                                    addEffect2 = false;
                                                }
                                                break potionEffectLoop;
                                            }
                                        }
                                        if (addEffect2) {
                                            if (hasEffect2){
                                                p.removePotionEffect(PotionEffectType.NIGHT_VISION);
                                            }
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 15 * 20, 0));
                                        }
                                    }
                                }
                            }.runTaskLater(plugin,i);
                        }
                    }
                }
            }.runTaskTimer(plugin, 40,40).getTaskId();
            fishPersonMap.put(p,fishID);
            fishPersonCounters.put(p,0);
        }
    }

    public void fishPersonEnd(){
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int fishPersonLevel = (int) pStat.get("fishing").get(13);
        if (fishPersonLevel > 0) {
            Bukkit.getScheduler().cancelTask(fishPersonMap.get(p));
            fishPersonMap.remove(p);
            fishPersonCounters.remove(p);
        }

    }

    public void eatFishFood(ItemStack food) {
        if (!(fishFood.containsKey(food.getType()) )) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int fishDietLevel = (int) pStat.get("fishing").get(9);
        if (fishDietLevel < 1) {
            return;
        }
        Material foodType = food.getType();
        if (fishFood.containsKey(food.getType())) {
            double foodMultiplier = fishDietLevel*0.2;
            p.setFoodLevel((int)Math.min(20,p.getFoodLevel() + Math.round(foodMultiplier * fishFood.get(foodType)) ));
            p.setSaturation((float)Math.min(p.getFoodLevel(),p.getSaturation()+(foodMultiplier*fishFoodSaturation.get(foodType)) ));
        }

    }


    public void rob(FishHook fishhook, Entity hookedEntity,World world) {
        if (hookedEntity == null) {
            return;
        }
        EntityType[] hookableEntities0 = {EntityType.BLAZE,EntityType.ZOMBIFIED_PIGLIN,EntityType.GHAST,EntityType.ZOMBIE,EntityType.SPIDER,
                                         EntityType.CAVE_SPIDER,EntityType.PIG,EntityType.CREEPER,EntityType.WITCH,EntityType.CHICKEN,
                                         EntityType.SKELETON,EntityType.WITHER_SKELETON,EntityType.MAGMA_CUBE,EntityType.COW,EntityType.MUSHROOM_COW,
                                         EntityType.ENDERMAN,EntityType.SHEEP,EntityType.IRON_GOLEM,EntityType.SNOWMAN,EntityType.SHULKER};
        List<EntityType> hookableEntities = Arrays.asList(hookableEntities0);
        Integer[] pTimers = timers.getPlayerTimers();
        if (!hookableEntities.contains(hookedEntity.getType())) {
            return;
        }
        if (pTimers[11] > 0) {
            actionMessage.sendMessage(ChatColor.RED + "You must wait " + pTimers[11].toString() + " seconds to rob another mob");
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int robLevel = (int) pStat.get("fishing").get(7);
        if (robLevel * .15 < rand.nextDouble()) {
            return;
        }
        ItemStack drop = new ItemStack(Material.DIRT, 1);
        if (hookedEntity.getType() == EntityType.BLAZE) {
            drop.setType(Material.BLAZE_ROD);
            increaseStats.changeEXP("fishing",800);
        } else if (hookedEntity.getType() == EntityType.GHAST) {
            double randomNum = rand.nextDouble();
            if (randomNum < .5) {
                drop.setType(Material.GHAST_TEAR);
            } else {
                drop.setType(Material.GUNPOWDER);
            }
            increaseStats.changeEXP("fishing",800);
        } else if (hookedEntity.getType() == EntityType.ZOMBIFIED_PIGLIN) {
            double randomNum = rand.nextDouble();
            if (randomNum < .5) {
                drop.setType(Material.ROTTEN_FLESH);
            } else {
                drop.setType(Material.GOLD_NUGGET);
            }
            increaseStats.changeEXP("fishing",800);
        } else if (hookedEntity.getType() == EntityType.ZOMBIE) {
            double randomNum = rand.nextDouble();
            if (randomNum < .98) {
                drop.setType(Material.ROTTEN_FLESH);
            } else {
                drop.setType(Material.ZOMBIE_HEAD);
            }
            increaseStats.changeEXP("fishing",1000);
        } else if (hookedEntity.getType() == EntityType.SPIDER) {
            double randomNum = rand.nextDouble();
            if (randomNum < .5) {
                drop.setType(Material.STRING);
            } else {
                drop.setType(Material.SPIDER_EYE);
            }
            increaseStats.changeEXP("fishing",1000);
        } else if (hookedEntity.getType() == EntityType.CAVE_SPIDER) {
            double randomNum = rand.nextDouble();
            if (randomNum < .49) {
                drop.setType(Material.STRING);
            } else if (randomNum < .98) {
                drop.setType(Material.SPIDER_EYE);
            } else if (randomNum < .99) {
                drop.setType(Material.SPLASH_POTION);
                PotionMeta pm = (PotionMeta) drop.getItemMeta();
                assert pm != null;
                pm.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 15, 0), false);
                pm.setDisplayName(ChatColor.YELLOW + "Splash Potion of Poison");
                drop.setItemMeta(pm);

            } else {
                drop.setType(Material.COBWEB);
            }
            increaseStats.changeEXP("fishing",1000);
        } else if (hookedEntity.getType() == EntityType.PIG) {
            drop.setType(Material.PORKCHOP);
            increaseStats.changeEXP("fishing",600);
        } else if (hookedEntity.getType() == EntityType.CHICKEN) {
            double randomNum = rand.nextDouble();
            if (randomNum < .33) {
                drop.setType(Material.EGG);
            } else if (randomNum > .66) {
                drop.setType(Material.CHICKEN);
            } else {
                drop.setType(Material.FEATHER);
            }
            increaseStats.changeEXP("fishing",600);
        } else if (hookedEntity.getType() == EntityType.CREEPER) {
            double randomNum = rand.nextDouble();
            if (randomNum < .99) {
                drop.setType(Material.GUNPOWDER);
            } else {
                drop.setType(Material.CREEPER_HEAD);
            }
            increaseStats.changeEXP("fishing",1000);
        } else if (hookedEntity.getType() == EntityType.SKELETON) {
            double randomNum = rand.nextDouble();
            if (randomNum < .49) {
                drop.setType(Material.BONE);
            } else if (randomNum < .98) {
                drop.setType(Material.ARROW);
                drop.setAmount(rand.nextInt(3) + 1);
            } else {
                drop.setType(Material.SKELETON_SKULL);
            }
            increaseStats.changeEXP("fishing",1000);
        } else if (hookedEntity.getType() == EntityType.WITHER_SKELETON) {
            double randomNum = rand.nextDouble();
            if (randomNum < .50) {
                drop.setType(Material.BONE);
            } else if (randomNum < .99) {
                drop.setType(Material.COAL);
                drop.setAmount(rand.nextInt(3) + 1);
            } else {
                drop.setType(Material.WITHER_SKELETON_SKULL);
            }
            increaseStats.changeEXP("fishing",120);
        } else if (hookedEntity.getType() == EntityType.SLIME) {
            drop.setType(Material.SLIME_BALL);
            increaseStats.changeEXP("fishing",800);
        } else if (hookedEntity.getType() == EntityType.MAGMA_CUBE) {
            drop.setType(Material.MAGMA_CREAM);
            increaseStats.changeEXP("fishing",1000);
        } else if (hookedEntity.getType() == EntityType.COW) {
            double randomNum = rand.nextDouble();
            if (randomNum < .49) {
                drop.setType(Material.LEATHER);
            } else if (randomNum < .98) {
                drop.setType(Material.BEEF);
            } else {
                drop.setType(Material.MILK_BUCKET);
            }
            increaseStats.changeEXP("fishing",600);
        } else if (hookedEntity.getType() == EntityType.MUSHROOM_COW) {
            double randomNum = rand.nextDouble();
            if (randomNum < .05) {
                drop.setType(Material.MILK_BUCKET);
            } else if (randomNum < 0.10) {
                drop.setType(Material.MUSHROOM_STEW);
            } else if (randomNum < 0.40) {
                drop.setType(Material.LEATHER);
            } else if (randomNum < 0.70) {
                drop.setType(Material.BEEF);
            } else {
                drop.setType(Material.RED_MUSHROOM);
                drop.setAmount(rand.nextInt(3) + 1);
            }
            increaseStats.changeEXP("fishing",600);
        } else if (hookedEntity.getType() == EntityType.ENDERMAN) {
            drop.setType(Material.ENDER_PEARL);
            increaseStats.changeEXP("fishing",1000);
        } else if (hookedEntity.getType() == EntityType.SHEEP) {
            drop.setType(Material.WHITE_WOOL);
            increaseStats.changeEXP("fishing",600);
        } else if (hookedEntity.getType() == EntityType.IRON_GOLEM) {
            double randomNum = rand.nextDouble();
            if (randomNum < .03) {
                drop.setType(Material.PUMPKIN);
            } else if (randomNum < 0.15) {
                drop.setType(Material.IRON_INGOT);
            } else {
                drop.setType(Material.POPPY);
            }
            increaseStats.changeEXP("fishing",600);
        } else if (hookedEntity.getType() == EntityType.SNOWMAN) {
            double randomNum = rand.nextDouble();
            if (randomNum < .03) {
                drop.setType(Material.PUMPKIN);
            } else {
                drop.setType(Material.SNOWBALL);
            }
            increaseStats.changeEXP("fishing",600);
        } else if (hookedEntity.getType() == EntityType.WITCH) {
            double randomNum = rand.nextDouble();
            if (randomNum < 0.01) {
                drop.setType(Material.SPLASH_POTION);
                PotionMeta pm = (PotionMeta) drop.getItemMeta();
                assert pm != null;
                pm.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 15, 0), false);
                pm.setDisplayName(ChatColor.YELLOW + "Splash Potion of Healing");
                drop.setItemMeta(pm);
            } else if (randomNum < 0.02) {
                drop.setType(Material.SPLASH_POTION);
                PotionMeta pm = (PotionMeta) drop.getItemMeta();
                assert pm != null;
                pm.addCustomEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 15, 0), false);
                pm.setDisplayName(ChatColor.YELLOW + "Splash Potion of Fire Resistance");
                drop.setItemMeta(pm);
            } else if (randomNum < 0.03) {
                drop.setType(Material.SPLASH_POTION);
                PotionMeta pm = (PotionMeta) drop.getItemMeta();
                assert pm != null;
                pm.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 15, 0), false);
                pm.setDisplayName(ChatColor.YELLOW + "Splash Potion of Speed");
                drop.setItemMeta(pm);
            } else if (randomNum < 0.12) {
                drop.setType(Material.GLASS_BOTTLE);
            } else if (randomNum < 0.24) {
                drop.setType(Material.SUGAR);
            } else if (randomNum < 0.36) {
                drop.setType(Material.SPIDER_EYE);
            } else if (randomNum < 0.48) {
                drop.setType(Material.GUNPOWDER);
            } else if (randomNum < 0.61) {
                drop.setType(Material.FERMENTED_SPIDER_EYE);
            } else if (randomNum < 0.74) {
                drop.setType(Material.GLOWSTONE_DUST);
            } else if (randomNum < 0.87) {
                drop.setType(Material.REDSTONE);
            } else {
                drop.setType(Material.STICK);
            }
            increaseStats.changeEXP("fishing",1200);
        } else if (hookedEntity.getType() == EntityType.SHULKER) {
            double randomNum = rand.nextDouble();
            if (randomNum < .25) {
                drop.setType(Material.SHULKER_SHELL);
            } else {
                drop.setType(Material.PURPUR_BLOCK);
            }
            increaseStats.changeEXP("fishing",800);
        }

        Location location = hookedEntity.getLocation();
        double dx = p.getLocation().getX() - location.getX();
        double dy = p.getLocation().getY() - location.getY();
        double dz = p.getLocation().getZ() - location.getZ();
        double distance = Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
        double multiplier = 0.08;
        Item droppedItem = world.dropItemNaturally(location, drop);
        Vector velocity = new Vector(dx * multiplier, (dy * multiplier) + (double) Math.sqrt(distance) * 0.1, dz * multiplier);
        droppedItem.setVelocity(velocity);

        timers.setPlayerTimer( "fishingRob", 10);
        for (int i = 1; i <= 10; i++) {
            int timeRemaining = 10 - i;
            new BukkitRunnable() {
                @Override
                public void run() {
                    timers.setPlayerTimer( "fishingRob", timeRemaining);
                    if (timeRemaining == 0 && !p.isOnline()) {
                        timers.removePlayer();
                    }
                }
            }.runTaskLater(plugin, 20 * i);
        }

    }


    public ItemStack dropTable(boolean superBaitOn) {
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        Integer[] pAbilities = abilities.getPlayerAbilities();
        int doubleFishLevel = (int) pStat.get("fishing").get(5);
        int treasureFinderLevel = (int) pStat.get("fishing").get(6);
        int scavengerLevel = (int) pStat.get("fishing").get(8);
        int filtrationLevel = (int) pStat.get("fishing").get(10);
        int hotRodLevel = (int) pStat.get("fishing").get(12);
        int hotRodToggle = (int) pStat.get("global").get(17);
        int hotRod = 0;
        if (hotRodLevel > 0 && hotRodToggle > 0) {
            hotRod = 1;
        }
        double roll = rand.nextDouble();
        ItemStack item = new ItemStack(Material.COD, 1);
        if (roll > 0.4) {
            double roll2 = rand.nextDouble();
            if (roll2 < 0.6) {
                item.setType(Material.COD);
                if (hotRod > 0) {
                    item.setType(Material.COOKED_COD);
                }
            }
            else if (roll2 < 0.85) {
                item.setType(Material.SALMON);
                if (hotRod > 0) {
                    item.setType(Material.COOKED_SALMON);
                }
            }
            else if (roll2 < 0.87) {
                item.setType(Material.TROPICAL_FISH);
            }
            else {
                item.setType(Material.PUFFERFISH);
            }
            double roll3 = rand.nextDouble();
            if (doubleFishLevel*0.0005 > roll3) {
                item.setAmount(2);
            }
            increaseStats.changeEXP("fishing",2000);
            return item;
        }

        //Load Fishing config
        ConfigLoad loadConfig = new ConfigLoad();
        ArrayList<Object> fishingInfo = loadConfig.getFishingInfo1();
        ArrayList<Object> fishingInfoBaseChances = loadConfig.getFishingInfo2();
        ArrayList<Object> fishingInfoHotRod = loadConfig.getFishingInfo3();
        ArrayList<Object> fishingInfoEnchants = loadConfig.getFishingInfo4();

        //Initialize Sorted Arrays
        ArrayList<Double> baseChances = new ArrayList<>();
        ArrayList<Material> itemNames = new ArrayList<>();
        ArrayList<Integer> dropAmounts = new ArrayList<>();
        ArrayList<Integer> dropRandomness = new ArrayList<>();
        ArrayList<Material> itemNames_hotRod = new ArrayList<>();
        ArrayList<Integer> dropAmounts_hotRod = new ArrayList<>();
        ArrayList<Integer> dropRandomness_hotRod = new ArrayList<>();
        ArrayList<Boolean> dropEnchantedItems = new ArrayList<>();

        //Organize Fishing config
        for (Object chance : fishingInfoBaseChances) {
            baseChances.add((double)chance);
        }
        for (Object entry : fishingInfo) {
            int i = fishingInfo.indexOf(entry);
            if (i % 3 == 0) {
                itemNames.add((Material)entry);
            }
            else if (i % 3 == 1) {
                dropAmounts.add((int)entry);
            }
            else {
                dropRandomness.add((int)entry);
            }
        }
        for (Object entry : fishingInfoHotRod) {
            int i = fishingInfoHotRod.indexOf(entry);
            if (i % 3 == 0) {
                itemNames_hotRod.add((Material)entry);
            }
            else if (i % 3 == 1) {
                dropAmounts_hotRod.add((int)entry);
            }
            else {
                dropRandomness_hotRod.add((int)entry);
            }
        }
        for (Object enchantInfo : fishingInfoEnchants) {
            if ((int)enchantInfo > 0) {
                dropEnchantedItems.add(true);
            }
            else {
                dropEnchantedItems.add(false);
            }
        }

        //Luck of the sea level
        int luckOfTheSeaLevel = 0;
        ItemMeta rodMeta = itemInHand.getItemMeta();
        if (rodMeta.getEnchants().containsKey(Enchantment.LUCK)) {
            luckOfTheSeaLevel = rodMeta.getEnchantLevel(Enchantment.LUCK);
        }

        double[] tierChances = {Math.max(0.100 - 0.02*luckOfTheSeaLevel,0), Math.min(0.050 + 0.2*luckOfTheSeaLevel,0.15),
                                baseChances.get(0),baseChances.get(1),baseChances.get(2),baseChances.get(3),baseChances.get(4)};
        double[] junkToTreasurePortion = {0,0,0,0,0,0,0};

        //Determine if chance to roll into tier unlocked
        //Also determines the portion of junkToTreasure chances -> each treasure bracket
        switch (scavengerLevel) {
            case 0:
                tierChances[2] = 0;
                tierChances[3] = 0;
                tierChances[4] = 0;
                tierChances[5] = 0;
                tierChances[6] = 0;
                junkToTreasurePortion[1] = 1;
                break;
            case 1:
                tierChances[3] = 0;
                tierChances[4] = 0;
                tierChances[5] = 0;
                tierChances[6] = 0;
                junkToTreasurePortion[1] = 0.5;
                junkToTreasurePortion[2] = 0.5;
                break;
            case 2:
                tierChances[4] = 0;
                tierChances[5] = 0;
                tierChances[6] = 0;
                junkToTreasurePortion[1] = 0.334;
                junkToTreasurePortion[2] = 0.333;
                junkToTreasurePortion[3] = 0.333;
                break;
            case 3:
                tierChances[5] = 0;
                tierChances[6] = 0;
                junkToTreasurePortion[1] = 0.3;
                junkToTreasurePortion[2] = 0.3;
                junkToTreasurePortion[3] = 0.3;
                junkToTreasurePortion[4] = 0.1;
                junkToTreasurePortion[5] = 0;
                junkToTreasurePortion[6] = 0;
                break;
            case 4:
                tierChances[6] = 0;
                junkToTreasurePortion[1] = 0.267;
                junkToTreasurePortion[2] = 0.267;
                junkToTreasurePortion[3] = 0.266;
                junkToTreasurePortion[4] = 0.1;
                junkToTreasurePortion[5] = 0.1;
                break;
            case 5:
                junkToTreasurePortion[1] = 0.25;
                junkToTreasurePortion[2] = 0.25;
                junkToTreasurePortion[3] = 0.25;
                junkToTreasurePortion[4] = 0.1;
                junkToTreasurePortion[5] = 0.1;
                junkToTreasurePortion[6] = 0.05;
                break;
            default:
                break;
        }

        //Alters brackets chances based on filtrationLevel
        if (filtrationLevel <= 2 && filtrationLevel >0) {
            double total = 0;
            for (int i = 3; i <= 6; i++) {
                if (tierChances[i] != 0) {
                    switch(i) {
                        case 3:
                            tierChances[i] += 0.005*filtrationLevel;
                            total += 0.005*filtrationLevel;
                            break;
                        case 4:
                        case 5:
                            tierChances[i] += 0.002*filtrationLevel;
                            total += 0.002*filtrationLevel;
                            break;
                        case 6:
                            tierChances[i] += 0.001*filtrationLevel;
                            total += 0.001*filtrationLevel;
                            break;
                        default:
                            break;
                    }
                }
            }
            tierChances[2] -= total;
        }
        else if (filtrationLevel > 2) {
            double total = 0.01;
            tierChances[3] += 0.01;
            for (int i = 4; i <= 6; i++) {
                if (tierChances[i] != 0) {
                    switch(i) {
                        case 4:
                        case 5:
                            tierChances[i] += 0.002*filtrationLevel;
                            total += 0.002*filtrationLevel;
                            break;
                        case 6:
                            tierChances[i] += 0.001*filtrationLevel;
                            total += 0.001*filtrationLevel;
                            break;
                        default:
                            break;
                    }
                }
            }
            tierChances[2] -= total;
        }

        //removes junk chance in accordance to treasureFinder Perk
        double junkSubtract = Math.min(0.1,treasureFinderLevel*0.00005);
        tierChances[0] = Math.max(tierChances[0]-junkSubtract,0);
        for (int i = 1; i <= 6; i++) {
            tierChances[i] += junkSubtract*junkToTreasurePortion[i];
        }

        /* Old "Lucky Catch" Perk

        //Adds treasure chance in accordance to luckyCatch perk
        if (luckyCatchLevel > 0 && pAbilities[4] == -2) {
            for (int i = 1; i <= 6; i++) {
                tierChances[i] += 0.05 * junkToTreasurePortion[i];
            }
        }

         */

        /*
        System.out.println(Double.toString(tierChances[0]) + " " + Double.toString(tierChances[1]) + " " + Double.toString(tierChances[2]) + " " +
                           Double.toString(tierChances[3]) + " " + Double.toString(tierChances[4]) + " " + Double.toString(tierChances[5]) + " " +
                           Double.toString(tierChances[6]));

         */

        //Sets brackets for random number roll
        double[] rollBrackets = {tierChances[0],0,0,0,0,0,0};
        for (int i = 1; i <= 6; i++) {
            if (!superBaitOn) {
                rollBrackets[i] = rollBrackets[i - 1] + tierChances[i];
            }
            else {
                rollBrackets[i] = rollBrackets[i - 1] + (2.0/4.0)*tierChances[i];
            }
        }

        Material[] music_discs = {Material.MUSIC_DISC_11, Material.MUSIC_DISC_13, Material.MUSIC_DISC_BLOCKS, Material.MUSIC_DISC_CAT,
                Material.MUSIC_DISC_CHIRP, Material.MUSIC_DISC_FAR, Material.MUSIC_DISC_MALL, Material.MUSIC_DISC_MELLOHI,
                Material.MUSIC_DISC_STAL, Material.MUSIC_DISC_STRAD, Material.MUSIC_DISC_WAIT, Material.MUSIC_DISC_WARD};
        Material[] chainmail = {Material.CHAINMAIL_BOOTS,Material.CHAINMAIL_CHESTPLATE,Material.CHAINMAIL_HELMET,Material.CHAINMAIL_LEGGINGS};
        PsuedoEnchanting enchant = new PsuedoEnchanting();

        if (roll < rollBrackets[0]) { //Junk Tier (-1)
            double roll2 = rand.nextDouble();
            double[] naturalJunkBrackets = {0.12,0.144,0.264,0.384,0.504,0.564,0.624,0.744,0.864,0.878,1}; //Altered a bit due to rounding error
            ItemStack junkRod = new ItemStack(Material.FISHING_ROD,1);
            ItemMeta junkRodMeta = junkRod.getItemMeta();
            if (junkRodMeta instanceof Damageable) {
                ((Damageable) junkRodMeta).setDamage(rand.nextInt(57)+7);
            }
            junkRod.setItemMeta(junkRodMeta);

            ItemStack junkBoots = new ItemStack(Material.LEATHER_BOOTS,1);
            ItemMeta junkBootsMeta = junkBoots.getItemMeta();
            if (junkBoots instanceof Damageable) {
                ((Damageable) junkBoots).setDamage(rand.nextInt(56)+7);
            }
            junkBoots.setItemMeta(junkBootsMeta);
            ItemStack junkInk = new ItemStack(Material.INK_SAC,10);

            ItemStack[] junkItems =  {new ItemStack(Material.BOWL,1),junkRod,new ItemStack(Material.LEATHER,1),junkBoots,
                                      new ItemStack(Material.ROTTEN_FLESH,1),new ItemStack(Material.STICK,1),new ItemStack(Material.STRING,1),
                                      new ItemStack(Material.GLASS_BOTTLE,1),new ItemStack(Material.BONE,1), junkInk, new ItemStack(Material.TRIPWIRE_HOOK,1)};
            for (int i=0; i < naturalJunkBrackets.length;i++) {
                if (roll2 < naturalJunkBrackets[i]) {
                    return junkItems[i];
                }
            }
            increaseStats.changeEXP("fishing",3000);
        }
        else if (roll < rollBrackets[1]) { //Natural Tier (0)
            double roll2 = rand.nextDouble();
            double[] naturalRareBrackets = {0.143,0.283,0.429,0.572,0.715,0.858,1}; //Altered a bit due to rounding error
            ItemStack rareRod = new ItemStack(Material.FISHING_ROD,1);
            rareRod = enchant.enchantItem(rareRod,rand.nextInt(9)+22,true);
            ItemMeta rareRodItemMeta = rareRod.getItemMeta();
            if (rareRodItemMeta instanceof Damageable) {
                ((Damageable) rareRodItemMeta).setDamage(rand.nextInt(10)+54);
            }
            rareRod.setItemMeta(rareRodItemMeta);

            ItemStack rareBow = new ItemStack(Material.BOW,1);
            rareBow = enchant.enchantItem(rareBow,rand.nextInt(9)+22,true);
            ItemMeta rareBowItemMeta = rareBow.getItemMeta();
            if (rareBowItemMeta instanceof Damageable) {
                ((Damageable) rareBowItemMeta).setDamage(rand.nextInt(40)+343);
            }
            rareBow.setItemMeta(rareBowItemMeta);

            ItemStack enchantedBook = new ItemStack(Material.ENCHANTED_BOOK,1);
            enchantedBook = enchant.enchantItem(enchantedBook,30,true);

            ItemStack[] items =  {rareBow,rareRod,enchantedBook,new ItemStack(Material.NAME_TAG,1), new ItemStack(Material.NAUTILUS_SHELL,1),new ItemStack(Material.SADDLE,1), new ItemStack(Material.LILY_PAD,1)};
            for (int i=0; i < naturalRareBrackets.length;i++) {
                if (roll2 < naturalRareBrackets[i]) {
                    return items[i];
                }
            }
            increaseStats.changeEXP("fishing",4000);


        }
        else if (roll < rollBrackets[2]) { //Common Tier (1)
            double roll2 = rand.nextDouble();
            if (roll2 < 0.2) {
                if (itemNames.get(0) == null) {
                    item.setType(Material.ENDER_PEARL);
                    if (hotRod > 0) {
                        item.setType(Material.FIRE_CHARGE);
                        item.setAmount(item.getAmount() * 3);
                    }
                }
                else {
                    if (hotRod > 0) {
                        item.setType(itemNames_hotRod.get(0));
                        item.setAmount(dropAmounts_hotRod.get(0) + rand.nextInt(dropRandomness_hotRod.get(0)+1));
                    }
                    else {
                        item.setType(itemNames.get(0));
                        item.setAmount(dropAmounts.get(0) + rand.nextInt(dropRandomness.get(0)+1));
                    }
                }
            }
            else if (roll2 < 0.4) {
                if (itemNames.get(1) == null) {
                    item.setType(Material.GHAST_TEAR);
                    if (hotRod > 0) {
                        item.setType(Material.BLAZE_POWDER);
                    }
                }
                else {
                    if (hotRod > 0) {
                        item.setType(itemNames_hotRod.get(1));
                        item.setAmount(dropAmounts_hotRod.get(1) + rand.nextInt(dropRandomness_hotRod.get(1)+1));
                    }
                    else {
                        item.setType(itemNames.get(1));
                        item.setAmount(dropAmounts.get(1) + rand.nextInt(dropRandomness.get(1)+1));
                    }
                }
            }
            else if (roll2 < 0.6) {
                if (itemNames.get(2) == null) {
                    item.setType(Material.GOLD_INGOT);
                }
                else {
                    item.setType(itemNames.get(2));
                    item.setAmount(dropAmounts.get(2)+rand.nextInt(dropRandomness.get(2)+1));
                }
            }
            else if (roll2 < 0.8) {
                if (itemNames.get(3) == null) {
                    item.setType(Material.IRON_INGOT);
                }
                else {
                    item.setType(itemNames.get(3));
                    item.setAmount(dropAmounts.get(3)+rand.nextInt(dropRandomness.get(3)+1));
                }
            }
            else {
                if (dropEnchantedItems.get(0)) {
                    item = getTieredLoot(1);
                }
            }
            increaseStats.changeEXP("fishing",4000);
        }
        else if (roll < rollBrackets[3]) { //Uncommon Tier (2)
            double roll2 = rand.nextDouble();
            if (roll2 < 0.2) {
                if (itemNames.get(4) == null) {
                    item.setType(Material.WET_SPONGE);
                    if (hotRod > 0) {
                        item.setType(Material.SPONGE);
                    }
                }
                else {
                    if (hotRod > 0) {
                        item.setType(itemNames_hotRod.get(2));
                        item.setAmount(dropAmounts_hotRod.get(2) + rand.nextInt(dropRandomness_hotRod.get(2)+1));
                    }
                    else {
                        item.setType(itemNames.get(4));
                        item.setAmount(dropAmounts.get(4) + rand.nextInt(dropRandomness.get(4)+1));
                    }
                }
            }
            else if (roll2 < 0.4) {
                if (itemNames.get(5) == null) {
                    item.setType(Material.EMERALD);
                }
                else {
                    item.setType(itemNames.get(5));
                    item.setAmount(dropAmounts.get(5)+rand.nextInt(dropRandomness.get(5)+1));
                }
            }
            else if (roll2 < 0.6) {
                if (itemNames.get(6) == null) {
                    item.setType(Material.SLIME_BALL);
                    int roll3 = rand.nextInt(3)+1;
                    item.setAmount(roll3);
                    if (hotRod > 0) {
                        item.setType(Material.MAGMA_CREAM);
                    }
                }
                else {
                    if (hotRod > 0) {
                        item.setType(itemNames_hotRod.get(3));
                        item.setAmount(dropAmounts_hotRod.get(3) + rand.nextInt(dropRandomness_hotRod.get(3)+1));
                    }
                    else {
                        item.setType(itemNames.get(6));
                        item.setAmount(dropAmounts.get(6) + rand.nextInt(dropRandomness.get(6)+1));
                    }
                }
            }
            else if (roll2 < 0.8) {
                if (itemNames.get(7) == null) {
                    item.setType(Material.ENDER_PEARL);
                    int roll3 = rand.nextInt(4)+2;
                    item.setAmount(roll3);
                    if (hotRod > 0) {
                        item.setType(Material.FIRE_CHARGE);
                        item.setAmount(item.getAmount()*3);
                    }
                }
                else {
                    if (hotRod > 0) {
                        item.setType(itemNames_hotRod.get(4));
                        item.setAmount(dropAmounts_hotRod.get(4) + rand.nextInt(dropRandomness_hotRod.get(4)+1));
                    }
                    else {
                        item.setType(itemNames.get(7));
                        item.setAmount(dropAmounts.get(7) + rand.nextInt(dropRandomness.get(7)+1));
                    }
                }
            }
            else {
                if (dropEnchantedItems.get(1)) {
                    item = getTieredLoot(2);
                }
            }
            increaseStats.changeEXP("fishing",5000);
        }
        else if (roll < rollBrackets[4]) { //Rare Tier (3)
            double roll2 = rand.nextDouble();
            if (roll2 < 0.2) {
                if (itemNames.get(8) == null) {
                    item.setType(music_discs[rand.nextInt(music_discs.length)]);
                }
                else {
                    item.setType(itemNames.get(8));
                    item.setAmount(dropAmounts.get(8)+rand.nextInt(dropRandomness.get(8)+1));
                }
            }
            else if (roll2 < 0.4) {
                if (itemNames.get(9) == null) {
                    item.setType(Material.SPECTRAL_ARROW);
                    int roll3 = rand.nextInt(11)+5;
                    item.setAmount(roll3);
                }
                else {
                    item.setType(itemNames.get(9));
                    item.setAmount(dropAmounts.get(9)+rand.nextInt(dropRandomness.get(9)+1));
                }
            }
            else if (roll2 < 0.6) {
                if (itemNames.get(10) == null) {
                    item.setType(chainmail[rand.nextInt(chainmail.length)]);
                }
                else {
                    item.setType(itemNames.get(10));
                    item.setAmount(dropAmounts.get(10)+rand.nextInt(dropRandomness.get(10)+1));
                }
            }
            else if (roll2 < 0.8) {
                if (itemNames.get(11) == null) {
                    item.setType(Material.TRIDENT);
                }
                else {
                    item.setType(itemNames.get(11));
                    item.setAmount(dropAmounts.get(11)+rand.nextInt(dropRandomness.get(11)+1));
                }
            }
            else {
                if (dropEnchantedItems.get(2)) {
                    item = getTieredLoot(3);
                }
            }
            increaseStats.changeEXP("fishing",7500);
        }
        else if (roll < rollBrackets[5]) { //Very Rare Tier (4)
            double roll2 = rand.nextDouble();
            if (roll2 < 0.233) {
                if (itemNames.get(12) == null) {
                    item.setType(Material.DIAMOND);
                }
                else {
                    item.setType(itemNames.get(12));
                    item.setAmount(dropAmounts.get(12)+rand.nextInt(dropRandomness.get(12)+1));
                }
            }
            else if (roll2 < 0.466) {
                if (itemNames.get(13) == null) {
                    item.setType(Material.NAUTILUS_SHELL);
                    int roll3 = rand.nextInt(3)+2;
                    item.setAmount(roll3);
                }
                else {
                    item.setType(itemNames.get(13));
                    item.setAmount(dropAmounts.get(13)+rand.nextInt(dropRandomness.get(13)+1));
                }
            }
            else if (roll2 < 0.7) {
                if (itemNames.get(14) == null) {
                    item.setType(Material.EMERALD);
                    int roll3 = rand.nextInt(6)+3;
                    item.setAmount(roll3);
                }
                else {
                    item.setType(itemNames.get(14));
                    item.setAmount(dropAmounts.get(14)+rand.nextInt(dropRandomness.get(14)+1));
                }
            }
            else if (roll2 < 0.85) {
                if (itemNames.get(15) == null) {
                    item.setType(Material.CROSSBOW);
                    item = enchant.enchantItem(item,28+rand.nextInt(3),true);
                }
                else {
                    item.setType(itemNames.get(15));
                    item.setAmount(dropAmounts.get(15)+rand.nextInt(dropRandomness.get(15)+1));
                }
            }
            else {
                if (dropEnchantedItems.get(3)) {
                    item = getTieredLoot(4);
                }
            }
            increaseStats.changeEXP("fishing",10000);
        }
        else if (roll < rollBrackets[6]) { //Legendary Tier (5)
            double roll2 = rand.nextDouble();
            if (roll2 < 0.1) {
                if (itemNames.get(16) == null) {
                    item.setType(Material.ELYTRA);
                }
                else {
                    item.setType(itemNames.get(16));
                    item.setAmount(dropAmounts.get(16)+rand.nextInt(dropRandomness.get(16)+1));
                }
            }
            else if (roll2 < 0.4) {
                if (itemNames.get(17) == null) {
                    item.setType(Material.HEART_OF_THE_SEA);
                }
                else {
                    item.setType(itemNames.get(17));
                    item.setAmount(dropAmounts.get(17)+rand.nextInt(dropRandomness.get(17)+1));
                }
            }
            else if (roll2 < 0.6) {
                if (itemNames.get(18) == null) {
                    item.setType(Material.TOTEM_OF_UNDYING);
                }
                else {
                    item.setType(itemNames.get(18));
                    item.setAmount(dropAmounts.get(18)+rand.nextInt(dropRandomness.get(18)+1));
                }
            }
            else if (roll2 < 0.9) {
                if (itemNames.get(19) == null) {
                    item.setType(Material.DIAMOND);
                    int roll3 = rand.nextInt(4)+2;
                    item.setAmount(roll3);
                }
                else {
                    item.setType(itemNames.get(19));
                    item.setAmount(dropAmounts.get(19)+rand.nextInt(dropRandomness.get(19)+1));
                }
            }
            else {
                if (dropEnchantedItems.get(4)) {
                    item = getTieredLoot(5);
                }
            }
            increaseStats.changeEXP("fishing",15000);
        }
        else { //fish roll
            double roll2 = rand.nextDouble();
            if (roll2 < .6) {
                item.setType(Material.COD);
                if (hotRod > 0) {
                    item.setType(Material.COOKED_COD);
                }
            }
            else if (roll2 < 0.85) {
                item.setType(Material.SALMON);
                if (hotRod > 0) {
                    item.setType(Material.COOKED_SALMON);
                }
            }
            else if (roll2 < 0.87) {
                item.setType(Material.TROPICAL_FISH);
            }
            else {
                item.setType(Material.PUFFERFISH);
            }
            double roll3 = rand.nextDouble();
            if (doubleFishLevel*0.0005 > roll3) {
                item.setAmount(2);
            }
            increaseStats.changeEXP("fishing",2000);
            return item;
        }

        return item;

    }

    public ItemStack getTieredLoot(int tier) {
        Map<Material,Integer> lootChanceMap = new HashMap<>();
        Map<Material,Integer> possibleDrops = new HashMap<>();
        ItemStack drop = new ItemStack(Material.COD,1);
        switch (tier) {
            case 2:
                lootChanceMap.put(Material.STONE_SHOVEL,5);
                lootChanceMap.put(Material.STONE_SWORD,5);
                lootChanceMap.put(Material.STONE_AXE,5);
                lootChanceMap.put(Material.STONE_PICKAXE,5);
                lootChanceMap.put(Material.LEATHER_BOOTS,5);
                lootChanceMap.put(Material.LEATHER_LEGGINGS,5);
                lootChanceMap.put(Material.LEATHER_CHESTPLATE,5);
                lootChanceMap.put(Material.LEATHER_HELMET,5);

                lootChanceMap.put(Material.IRON_SHOVEL,3);
                lootChanceMap.put(Material.IRON_SWORD,3);
                lootChanceMap.put(Material.IRON_AXE,3);
                lootChanceMap.put(Material.IRON_PICKAXE,3);
                lootChanceMap.put(Material.IRON_BOOTS,3);
                lootChanceMap.put(Material.IRON_LEGGINGS,2);
                lootChanceMap.put(Material.IRON_CHESTPLATE,2);
                lootChanceMap.put(Material.IRON_HELMET,3);

                lootChanceMap.put(Material.GOLDEN_SHOVEL,0);
                lootChanceMap.put(Material.GOLDEN_SWORD,0);
                lootChanceMap.put(Material.GOLDEN_AXE,0);
                lootChanceMap.put(Material.GOLDEN_PICKAXE,0);
                lootChanceMap.put(Material.GOLDEN_BOOTS,1);
                lootChanceMap.put(Material.GOLDEN_LEGGINGS,1);
                lootChanceMap.put(Material.GOLDEN_CHESTPLATE,1);
                lootChanceMap.put(Material.GOLDEN_HELMET,1);

                lootChanceMap.put(Material.DIAMOND_SHOVEL,0);
                lootChanceMap.put(Material.DIAMOND_SWORD,0);
                lootChanceMap.put(Material.DIAMOND_AXE,0);
                lootChanceMap.put(Material.DIAMOND_PICKAXE,0);
                lootChanceMap.put(Material.DIAMOND_BOOTS,0);
                lootChanceMap.put(Material.DIAMOND_LEGGINGS,0);
                lootChanceMap.put(Material.DIAMOND_CHESTPLATE,0);
                lootChanceMap.put(Material.DIAMOND_HELMET,0);

            case 3:
                lootChanceMap.put(Material.STONE_SHOVEL,1);
                lootChanceMap.put(Material.STONE_SWORD,1);
                lootChanceMap.put(Material.STONE_AXE,1);
                lootChanceMap.put(Material.STONE_PICKAXE,1);
                lootChanceMap.put(Material.LEATHER_BOOTS,1);
                lootChanceMap.put(Material.LEATHER_LEGGINGS,1);
                lootChanceMap.put(Material.LEATHER_CHESTPLATE,1);
                lootChanceMap.put(Material.LEATHER_HELMET,1);

                lootChanceMap.put(Material.IRON_SHOVEL,7);
                lootChanceMap.put(Material.IRON_SWORD,7);
                lootChanceMap.put(Material.IRON_AXE,7);
                lootChanceMap.put(Material.IRON_PICKAXE,7);
                lootChanceMap.put(Material.IRON_BOOTS,8);
                lootChanceMap.put(Material.IRON_LEGGINGS,6);
                lootChanceMap.put(Material.IRON_CHESTPLATE,6);
                lootChanceMap.put(Material.IRON_HELMET,8);

                lootChanceMap.put(Material.GOLDEN_SHOVEL,2);
                lootChanceMap.put(Material.GOLDEN_SWORD,2);
                lootChanceMap.put(Material.GOLDEN_AXE,2);
                lootChanceMap.put(Material.GOLDEN_PICKAXE,2);
                lootChanceMap.put(Material.GOLDEN_BOOTS,4);
                lootChanceMap.put(Material.GOLDEN_LEGGINGS,2);
                lootChanceMap.put(Material.GOLDEN_CHESTPLATE,2);
                lootChanceMap.put(Material.GOLDEN_HELMET,4);

                lootChanceMap.put(Material.DIAMOND_SHOVEL,0);
                lootChanceMap.put(Material.DIAMOND_SWORD,0);
                lootChanceMap.put(Material.DIAMOND_AXE,0);
                lootChanceMap.put(Material.DIAMOND_PICKAXE,0);
                lootChanceMap.put(Material.DIAMOND_BOOTS,0);
                lootChanceMap.put(Material.DIAMOND_LEGGINGS,0);
                lootChanceMap.put(Material.DIAMOND_CHESTPLATE,0);
                lootChanceMap.put(Material.DIAMOND_HELMET,0);
                break;
            case 4:
                lootChanceMap.put(Material.STONE_SHOVEL,0);
                lootChanceMap.put(Material.STONE_SWORD,0);
                lootChanceMap.put(Material.STONE_AXE,0);
                lootChanceMap.put(Material.STONE_PICKAXE,0);
                lootChanceMap.put(Material.LEATHER_BOOTS,0);
                lootChanceMap.put(Material.LEATHER_LEGGINGS,0);
                lootChanceMap.put(Material.LEATHER_CHESTPLATE,0);
                lootChanceMap.put(Material.LEATHER_HELMET,0);

                lootChanceMap.put(Material.IRON_SHOVEL,10);
                lootChanceMap.put(Material.IRON_SWORD,10);
                lootChanceMap.put(Material.IRON_AXE,10);
                lootChanceMap.put(Material.IRON_PICKAXE,10);
                lootChanceMap.put(Material.IRON_BOOTS,10);
                lootChanceMap.put(Material.IRON_LEGGINGS,9);
                lootChanceMap.put(Material.IRON_CHESTPLATE,8);
                lootChanceMap.put(Material.IRON_HELMET,10);

                lootChanceMap.put(Material.GOLDEN_SHOVEL,0);
                lootChanceMap.put(Material.GOLDEN_SWORD,0);
                lootChanceMap.put(Material.GOLDEN_AXE,0);
                lootChanceMap.put(Material.GOLDEN_PICKAXE,0);
                lootChanceMap.put(Material.GOLDEN_BOOTS,0);
                lootChanceMap.put(Material.GOLDEN_LEGGINGS,0);
                lootChanceMap.put(Material.GOLDEN_CHESTPLATE,0);
                lootChanceMap.put(Material.GOLDEN_HELMET,0);

                lootChanceMap.put(Material.DIAMOND_SHOVEL,1);
                lootChanceMap.put(Material.DIAMOND_SWORD,1);
                lootChanceMap.put(Material.DIAMOND_AXE,1);
                lootChanceMap.put(Material.DIAMOND_PICKAXE,1);
                lootChanceMap.put(Material.DIAMOND_BOOTS,1);
                lootChanceMap.put(Material.DIAMOND_LEGGINGS,0);
                lootChanceMap.put(Material.DIAMOND_CHESTPLATE,0);
                lootChanceMap.put(Material.DIAMOND_HELMET,1);
                break;
            case 5:
                lootChanceMap.put(Material.STONE_SHOVEL,0);
                lootChanceMap.put(Material.STONE_SWORD,0);
                lootChanceMap.put(Material.STONE_AXE,0);
                lootChanceMap.put(Material.STONE_PICKAXE,0);
                lootChanceMap.put(Material.LEATHER_BOOTS,0);
                lootChanceMap.put(Material.LEATHER_LEGGINGS,0);
                lootChanceMap.put(Material.LEATHER_CHESTPLATE,0);
                lootChanceMap.put(Material.LEATHER_HELMET,0);

                lootChanceMap.put(Material.IRON_SHOVEL,2);
                lootChanceMap.put(Material.IRON_SWORD,2);
                lootChanceMap.put(Material.IRON_AXE,2);
                lootChanceMap.put(Material.IRON_PICKAXE,2);
                lootChanceMap.put(Material.IRON_BOOTS,2);
                lootChanceMap.put(Material.IRON_LEGGINGS,1);
                lootChanceMap.put(Material.IRON_CHESTPLATE,1);
                lootChanceMap.put(Material.IRON_HELMET,2);

                lootChanceMap.put(Material.GOLDEN_SHOVEL,0);
                lootChanceMap.put(Material.GOLDEN_SWORD,0);
                lootChanceMap.put(Material.GOLDEN_AXE,0);
                lootChanceMap.put(Material.GOLDEN_PICKAXE,0);
                lootChanceMap.put(Material.GOLDEN_BOOTS,0);
                lootChanceMap.put(Material.GOLDEN_LEGGINGS,0);
                lootChanceMap.put(Material.GOLDEN_CHESTPLATE,0);
                lootChanceMap.put(Material.GOLDEN_HELMET,0);

                lootChanceMap.put(Material.DIAMOND_SHOVEL,10);
                lootChanceMap.put(Material.DIAMOND_SWORD,10);
                lootChanceMap.put(Material.DIAMOND_AXE,10);
                lootChanceMap.put(Material.DIAMOND_PICKAXE,10);
                lootChanceMap.put(Material.DIAMOND_BOOTS,10);
                lootChanceMap.put(Material.DIAMOND_LEGGINGS,9);
                lootChanceMap.put(Material.DIAMOND_CHESTPLATE,8);
                lootChanceMap.put(Material.DIAMOND_HELMET,10);
                break;
            case 1:
            default:
                lootChanceMap.put(Material.STONE_SHOVEL,10);
                lootChanceMap.put(Material.STONE_SWORD,10);
                lootChanceMap.put(Material.STONE_AXE,10);
                lootChanceMap.put(Material.STONE_PICKAXE,10);
                lootChanceMap.put(Material.LEATHER_BOOTS,10);
                lootChanceMap.put(Material.LEATHER_LEGGINGS,10);
                lootChanceMap.put(Material.LEATHER_CHESTPLATE,10);
                lootChanceMap.put(Material.LEATHER_HELMET,10);

                lootChanceMap.put(Material.IRON_SHOVEL,3);
                lootChanceMap.put(Material.IRON_SWORD,3);
                lootChanceMap.put(Material.IRON_AXE,3);
                lootChanceMap.put(Material.IRON_PICKAXE,3);
                lootChanceMap.put(Material.IRON_BOOTS,3);
                lootChanceMap.put(Material.IRON_LEGGINGS,2);
                lootChanceMap.put(Material.IRON_CHESTPLATE,2);
                lootChanceMap.put(Material.IRON_HELMET,3);

                lootChanceMap.put(Material.GOLDEN_SHOVEL,0);
                lootChanceMap.put(Material.GOLDEN_SWORD,0);
                lootChanceMap.put(Material.GOLDEN_AXE,0);
                lootChanceMap.put(Material.GOLDEN_PICKAXE,0);
                lootChanceMap.put(Material.GOLDEN_BOOTS,0);
                lootChanceMap.put(Material.GOLDEN_LEGGINGS,0);
                lootChanceMap.put(Material.GOLDEN_CHESTPLATE,0);
                lootChanceMap.put(Material.GOLDEN_HELMET,0);

                lootChanceMap.put(Material.DIAMOND_SHOVEL,0);
                lootChanceMap.put(Material.DIAMOND_SWORD,0);
                lootChanceMap.put(Material.DIAMOND_AXE,0);
                lootChanceMap.put(Material.DIAMOND_PICKAXE,0);
                lootChanceMap.put(Material.DIAMOND_BOOTS,0);
                lootChanceMap.put(Material.DIAMOND_LEGGINGS,0);
                lootChanceMap.put(Material.DIAMOND_CHESTPLATE,0);
                lootChanceMap.put(Material.DIAMOND_HELMET,0);
                break;
        }
        Material[] tools0 = {Material.DIAMOND_PICKAXE,Material.IRON_PICKAXE,Material.DIAMOND_SHOVEL,Material.IRON_SHOVEL,Material.DIAMOND_AXE,Material.IRON_AXE};
        List<Material> tools = Arrays.asList(tools0);
        Material[] swords0 = {Material.DIAMOND_SWORD,Material.IRON_SWORD};
        List<Material> swords = Arrays.asList(swords0);
        Material[] helmet0 = {Material.DIAMOND_HELMET,Material.IRON_HELMET};
        List<Material> helmet = Arrays.asList(helmet0);
        Material[] chestplate0 = {Material.DIAMOND_CHESTPLATE,Material.IRON_CHESTPLATE};
        List<Material> chestplate = Arrays.asList(chestplate0);
        Material[] leggings0 = {Material.DIAMOND_LEGGINGS,Material.IRON_LEGGINGS};
        List<Material> leggings = Arrays.asList(leggings0);
        Material[] boots0 = {Material.DIAMOND_BOOTS,Material.IRON_BOOTS};
        List<Material> boots = Arrays.asList(boots0);

        int  T = 0;
        for (Material itemType : lootChanceMap.keySet()) {
            int weight = lootChanceMap.get(itemType);
            T+= weight;
            if (weight > 0) {
                possibleDrops.put(itemType,weight);
            }

        }
        int w = rand.nextInt(T);
        for (Material itemType : possibleDrops.keySet()) {
            w = w - possibleDrops.get(itemType);
            if ( w < 0) {
                drop.setType(itemType);
                break;
            }
        }

        PsuedoEnchanting enchant = new PsuedoEnchanting();
        switch (tier) {
            case 2:
                drop = enchant.enchantItem(drop,rand.nextInt(10)+10,true);
                break;
            case 3:
                drop = enchant.enchantItem(drop,rand.nextInt(10)+19,true);
                break;
            case 4:
                drop = enchant.enchantItem(drop,rand.nextInt(3)+28,true);
                break;
            case 5:
                if (tools.contains(drop)) {
                    int roll = rand.nextInt(2);
                    if (roll == 1) {
                        drop.addUnsafeEnchantment(Enchantment.DIG_SPEED, 5);
                    }
                    else {
                        drop.addUnsafeEnchantment(Enchantment.MENDING, 1);
                    }
                    drop = enchant.addEnchant(drop,30,true);
                }
                else if (swords.contains(drop)) {
                    int roll = rand.nextInt(5);
                    if (roll == 1) {
                        drop.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
                    }
                    else if (roll == 2) {
                        drop.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
                    }
                    else if (roll == 3) {
                        drop.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 3);
                    }
                    else if (roll == 4) {
                        drop.addUnsafeEnchantment(Enchantment.SWEEPING_EDGE, 3);
                    }
                    else {
                        drop.addUnsafeEnchantment(Enchantment.MENDING, 1);
                    }
                    drop = enchant.addEnchant(drop,30,true);
                }
                else if (boots.contains(drop)) {
                    int roll = rand.nextInt(5);
                    if (roll == 1) {
                        drop.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
                    }
                    else if (roll == 2) {
                        drop.addUnsafeEnchantment(Enchantment.FROST_WALKER, 2);
                    }
                    else if (roll == 3) {
                        drop.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, 2);
                    }
                    else if (roll == 4) {
                        drop.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 4);
                    }
                    else {
                        drop.addUnsafeEnchantment(Enchantment.MENDING, 1);
                    }
                    drop = enchant.addEnchant(drop,30,true);
                }
                else if (chestplate.contains(drop)) {
                    int roll = rand.nextInt(3);
                    if (roll == 1) {
                        drop.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
                    }
                    else if (roll == 2) {
                        drop.addUnsafeEnchantment(Enchantment.THORNS, 3);
                    }
                    else {
                        drop.addUnsafeEnchantment(Enchantment.MENDING, 1);
                    }
                    drop = enchant.addEnchant(drop,30,true);
                }
                else if (leggings.contains(drop)) {
                    int roll = rand.nextInt(2);
                    if (roll == 1) {
                        drop.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
                    }
                    else {
                        drop.addUnsafeEnchantment(Enchantment.MENDING, 1);
                    }
                    drop = enchant.addEnchant(drop,30,true);

                }
                else if (helmet.contains(drop)) {
                    int roll = rand.nextInt(4);
                    if (roll == 1) {
                        drop.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
                    }
                    else if (roll == 2) {
                        drop.addUnsafeEnchantment(Enchantment.OXYGEN, 3);
                    }
                    else if (roll == 3) {
                        drop.addUnsafeEnchantment(Enchantment.WATER_WORKER, 1);
                    }
                    else {
                        drop.addUnsafeEnchantment(Enchantment.MENDING, 1);
                    }
                    drop = enchant.addEnchant(drop,30,true);
                }
                break;
            case 1:
            default:
                drop = enchant.enchantItem(drop,rand.nextInt(3)+1,true);
                break;
        }
        ItemMeta dropMeta = drop.getItemMeta();
        if (dropMeta instanceof Damageable) {
            ((Damageable) dropMeta).setDamage((int) Math.round(drop.getType().getMaxDurability()*(0.85*rand.nextDouble()) + 1));
        }
        drop.setItemMeta(dropMeta);
        return drop;
    }
}
