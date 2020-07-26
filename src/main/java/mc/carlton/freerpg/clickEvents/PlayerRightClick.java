package mc.carlton.freerpg.clickEvents;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.perksAndAbilities.*;
import mc.carlton.freerpg.playerAndServerInfo.AbilityTracker;
import mc.carlton.freerpg.playerAndServerInfo.ChangeStats;
import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;


public class PlayerRightClick implements Listener {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);

    @EventHandler
    void onRightClick(PlayerInteractEvent e) {
        Action a = e.getAction();
        if (e.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }
        if ((a.equals(Action.RIGHT_CLICK_AIR)) || (a.equals(Action.RIGHT_CLICK_BLOCK))) {
            Player p = e.getPlayer();
            PlayerStats pStatClass = new PlayerStats(p);
            if (p.getGameMode() == GameMode.CREATIVE) {
                return;
            }
            Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
            ChangeStats increaseStats = new ChangeStats(p);
            AbilityTracker abilities = new AbilityTracker(p);
            Integer[] pAbilities = abilities.getPlayerAbilities();
            ItemStack itemInHand = p.getInventory().getItemInMainHand();
            Material itemInHandType = itemInHand.getType();

            Material[] pickaxes0 = {Material.NETHERITE_PICKAXE,Material.DIAMOND_PICKAXE, Material.GOLDEN_PICKAXE, Material.IRON_PICKAXE, Material.STONE_PICKAXE, Material.WOODEN_PICKAXE};
            List<Material> pickaxes = Arrays.asList(pickaxes0);
            Material[] axes0 = {Material.NETHERITE_AXE,Material.DIAMOND_AXE,Material.GOLDEN_AXE,Material.IRON_AXE, Material.STONE_AXE,Material.WOODEN_AXE};
            List<Material> axes = Arrays.asList(axes0);
            Material[] shovels0 = {Material.NETHERITE_SHOVEL,Material.DIAMOND_SHOVEL,Material.GOLDEN_SHOVEL,Material.IRON_SHOVEL, Material.STONE_SHOVEL,Material.WOODEN_SHOVEL};
            List<Material> shovels = Arrays.asList(shovels0);
            Material[] hoes0 = {Material.NETHERITE_HOE,Material.DIAMOND_HOE,Material.GOLDEN_HOE,Material.IRON_HOE, Material.STONE_HOE,Material.WOODEN_HOE};
            List<Material> hoes = Arrays.asList(hoes0);
            Material[] swords0 = {Material.NETHERITE_SWORD,Material.WOODEN_SWORD,Material.STONE_SWORD,Material.GOLDEN_SWORD,Material.DIAMOND_SWORD,Material.IRON_SWORD};
            List<Material> swords = Arrays.asList(swords0);
            Material[] noRightClick0 = {Material.AIR,Material.ARROW,Material.SPECTRAL_ARROW, Material.TIPPED_ARROW,Material.BLAZE_POWDER,
                                        Material.BLAZE_ROD, Material.BONE,Material.BOOK,Material.BOWL, Material.CHARCOAL,
                                        Material.COAL,Material.CLAY,Material.BRICK,Material.COMPASS,Material.DIAMOND_HORSE_ARMOR,Material.DRAGON_BREATH,
                                        Material.MAP,Material.ENCHANTED_BOOK,Material.FEATHER,Material.FERMENTED_SPIDER_EYE,Material.FIREWORK_STAR,
                                        Material.FLINT,Material.GHAST_TEAR,Material.GLISTERING_MELON_SLICE,Material.GLOWSTONE_DUST,Material.GOLDEN_HORSE_ARMOR,
                                        Material.GOLD_NUGGET,Material.GOLD_INGOT,Material.GUNPOWDER,Material.INK_SAC,Material.IRON_HORSE_ARMOR,
                                        Material.MAGMA_CREAM,Material.NETHER_BRICK,Material.NETHER_WART,Material.PAPER,Material.PRISMARINE_SHARD,Material.PRISMARINE_CRYSTALS,
                                        Material.RABBIT_HIDE,Material.LEATHER,Material.LEATHER_HORSE_ARMOR,Material.RABBIT_FOOT,Material.SADDLE,Material.SHEARS,
                                        Material.SLIME_BALL,Material.SHULKER_SHELL,Material.SPIDER_EYE,Material.STICK,Material.STRING,Material.TOTEM_OF_UNDYING,
                                        Material.CLOCK,Material.WRITTEN_BOOK};
            List<Material> noRightClick = Arrays.asList(noRightClick0);
            Material[] actionableBlocks0 = {Material.ANVIL, Material.BLACK_BED, Material.BLUE_BED, Material.BROWN_BED, Material.CYAN_BED, Material.GRAY_BED,
                                           Material.GREEN_BED,Material.LIGHT_BLUE_BED,Material.LIGHT_GRAY_BED,Material.LIME_BED,Material.MAGENTA_BED,
                                           Material.ORANGE_BED,Material.PINK_BED,Material.PURPLE_BED,Material.RED_BED,Material.WHITE_BED,Material.YELLOW_BED,
                                           Material.BELL,Material.BLAST_FURNACE,Material.BREWING_STAND,Material.CARTOGRAPHY_TABLE,Material.CHEST,Material.COMPOSTER,
                                           Material.CRAFTING_TABLE,Material.ACACIA_DOOR,Material.BIRCH_DOOR,Material.DARK_OAK_DOOR,Material.IRON_DOOR,Material.JUNGLE_DOOR,
                                           Material.OAK_DOOR,Material.SPRUCE_DOOR,Material.ENCHANTING_TABLE,Material.ACACIA_FENCE_GATE,Material.BIRCH_FENCE_GATE,
                                           Material.DARK_OAK_FENCE_GATE,Material.JUNGLE_FENCE_GATE,Material.OAK_FENCE_GATE,Material.SPRUCE_FENCE_GATE,Material.FURNACE,
                                           Material.GRINDSTONE,Material.JUKEBOX,Material.LECTERN,Material.LOOM,Material.NOTE_BLOCK,Material.SMOKER,Material.STONECUTTER,
                                           Material.TRAPPED_CHEST,Material.ACACIA_TRAPDOOR,Material.BIRCH_TRAPDOOR,Material.DARK_OAK_TRAPDOOR,Material.IRON_TRAPDOOR,
                                           Material.JUNGLE_TRAPDOOR,Material.OAK_TRAPDOOR,Material.SPRUCE_TRAPDOOR,Material.BARREL,Material.ACACIA_BUTTON,Material.BIRCH_BUTTON,
                                           Material.DARK_OAK_BUTTON,Material.OAK_BUTTON,Material.JUNGLE_BUTTON,Material.SPRUCE_BUTTON,Material.STONE_BUTTON,Material.REPEATER,
                                           Material.COMPARATOR,Material.HOPPER,Material.HOPPER_MINECART,Material.DAYLIGHT_DETECTOR,Material.LEVER,Material.FURNACE_MINECART,
                                           Material.DISPENSER,Material.DROPPER,Material.CHIPPED_ANVIL,Material.DAMAGED_ANVIL,Material.SHULKER_BOX,Material.ENDER_CHEST,Material.BLACK_SHULKER_BOX,
                                           Material.BLUE_SHULKER_BOX,Material.BROWN_SHULKER_BOX,Material.CYAN_SHULKER_BOX,Material.GRAY_SHULKER_BOX,Material.GREEN_SHULKER_BOX,Material.LIGHT_BLUE_SHULKER_BOX,
                                           Material.LIGHT_GRAY_SHULKER_BOX,Material.LIME_SHULKER_BOX,Material.MAGENTA_SHULKER_BOX, Material.RED_SHULKER_BOX,Material.WHITE_SHULKER_BOX,Material.YELLOW_SHULKER_BOX,
                                           Material.ORANGE_SHULKER_BOX, Material.PURPLE_SHULKER_BOX,Material.PINK_SHULKER_BOX};
            List<Material> actionableBlocks = Arrays.asList(actionableBlocks0);



            //Right clicking blocks
            if (a.equals(Action.RIGHT_CLICK_BLOCK)) {
                Farming farmingClass = new Farming(p);
                farmingClass.composterEXP(e.getClickedBlock());
                if (e.getClickedBlock().getType() == Material.IRON_BLOCK && !itemInHandType.isBlock()) {
                    Repair repairClass = new Repair(p);
                    repairClass.repairItem();
                    e.setCancelled(true);
                    return;
                }
                if (e.getClickedBlock().getType() == Material.GOLD_BLOCK && !itemInHandType.isBlock()) {
                    if (p.isSneaking()) {
                        Repair repairClass = new Repair(p);
                        repairClass.salvaging();
                        e.setCancelled(true);
                        return;
                    }
                }
                if (actionableBlocks.contains(e.getClickedBlock().getType())) {
                    return;
                }
            }

            if (p.getInventory().getItemInOffHand().getType() == Material.TORCH && a.equals(Action.RIGHT_CLICK_BLOCK)) {
                return;
            }

            int waitTicks = 0;
            //Shield smoothness
            if (p.getInventory().getItemInOffHand().getType() == Material.SHIELD) {
                waitTicks = 6;
            }
            

            //Explosions
            if (itemInHandType == Material.FLINT_AND_STEEL) {
                Block blockLit = e.getClickedBlock();
                if (blockLit.getType() == Material.TNT) {
                    e.setCancelled(true);
                    blockLit.setType(Material.AIR);
                    World world = p.getWorld();
                    TNTPrimed tnt = world.spawn(blockLit.getLocation().add(0, 0.25, 0), TNTPrimed.class);
                    int blastRadiusLevel = (int) pStat.get("mining").get(10);
                    double power0 = 4 + blastRadiusLevel * 0.5;
                    float power = (float) power0;
                    tnt.setFuseTicks(41);
                    Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                        @Override
                        public void run() {
                            Location tntLoc = tnt.getLocation();
                            tnt.getWorld().createExplosion(tntLoc, power, false, true);
                            tnt.remove();
                            increaseStats.changeEXP("mining", 1000);
                            int passive3_mining = (int) pStat.get("mining").get(6);
                            double explosionDrop = passive3_mining * 0.0001;
                            for (int i = 0; i < 10; i++) {
                                Mining miningClass = new Mining(p);
                                miningClass.miningTreasureDrop(explosionDrop, world, tntLoc);
                            }
                        }
                    }, 40L);
                }
            }

            //Digging
            else if (shovels.contains(itemInHandType)) {
                Digging diggingClass = new Digging(p);
                if (a.equals(Action.RIGHT_CLICK_BLOCK)) {
                    if (!(e.getClickedBlock().getType() == Material.GRASS_BLOCK)) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (p.isOnline()) {
                                    if (!p.isBlocking()) {
                                        diggingClass.initiateAbility();
                                    }
                                }
                            }
                        }.runTaskLater(plugin, waitTicks);

                    }
                } else {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (p.isOnline()) {
                                if (!p.isBlocking()) {
                                    diggingClass.initiateAbility();
                                }
                            }
                        }
                    }.runTaskLater(plugin, waitTicks);
                }
            }
            //Woodcutting and AxeMastery
            else if (axes.contains(itemInHandType)) {
                Woodcutting woodcuttingClass = new Woodcutting(p);
                if (a.equals(Action.RIGHT_CLICK_BLOCK)) {
                    if (woodcuttingClass.blacklistedBlock(e.getClickedBlock())) {
                        return;
                    }
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (p.isOnline()) {
                            if (!p.isBlocking()) {
                                woodcuttingClass.initiateAbility();
                            }
                        }
                    }
                }.runTaskLater(plugin, waitTicks);
                AxeMastery axeMasteryClass = new AxeMastery(p);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (p.isOnline()) {
                            if (!p.isBlocking()) {
                                axeMasteryClass.initiateAbility();
                            }
                        }
                    }
                }.runTaskLater(plugin, waitTicks);
            }
            //Mining
            else if (pickaxes.contains(itemInHandType)) {
                Mining miningClass = new Mining(p);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (p.isOnline()) {
                            if (!p.isBlocking()) {
                                miningClass.initiateAbility();
                            }
                        }
                    }
                }.runTaskLater(plugin, waitTicks);
            }

            //Farming
            else if (hoes.contains(itemInHandType)) {
                if (a.equals(Action.RIGHT_CLICK_BLOCK)) {
                    if (!(e.getClickedBlock().getType() == Material.DIRT || e.getClickedBlock().getType() == Material.GRASS_BLOCK)) {
                        Farming farmingClass = new Farming(p);
                        farmingClass.initiateAbility();
                    } else {
                        increaseStats.changeEXP("farming", 15);
                    }
                } else {
                    Farming farmingClass = new Farming(p);
                    farmingClass.initiateAbility();
                }

            } else if (itemInHandType == Material.BONE_MEAL && a.equals(Action.RIGHT_CLICK_BLOCK)) {
                Farming farmingClass = new Farming(p);
                farmingClass.fertilizerSave(e.getClickedBlock());
                increaseStats.changeEXP("farming", 40);
            }

            //Archery
            else if (itemInHandType == Material.BOW) {
                if (pAbilities[5] > -1) {
                    Archery archeryClass = new Archery(p);
                    archeryClass.enableAbility();
                }
            } else if (itemInHandType == Material.CROSSBOW) {
                if ((int) pStat.get("archery").get(12) > 0) {
                    Archery archeryClass = new Archery(p);
                    if (pAbilities[5] > -1) {
                        archeryClass.enableAbility();
                        archeryClass.crossbowAbility();
                    } else if (pAbilities[5] == -2) {
                        archeryClass.crossbowAbility();
                    }
                }
            }

            //beastMastery
            else if (p.getVehicle() != null) {
                EntityType[] acceptableVehicles0 = {EntityType.HORSE,EntityType.SKELETON_HORSE,EntityType.ZOMBIE_HORSE,EntityType.PIG,EntityType.DONKEY,EntityType.MULE,EntityType.LLAMA};
                List<EntityType> acceptableVehicles = Arrays.asList(acceptableVehicles0);
                if (acceptableVehicles.contains(p.getVehicle().getType())) {
                    if ((noRightClick.contains(itemInHandType) || (a == Action.RIGHT_CLICK_AIR && itemInHand.getType().isBlock()))) {
                        BeastMastery beastMasteryClass = new BeastMastery(p);
                        beastMasteryClass.initiateAbility();
                    }
                }
            }

            //Swordsmanship
            else if (swords.contains(itemInHandType)) {
                Swordsmanship swordsmanshipClass = new Swordsmanship(p);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (p.isOnline()) {
                            if (!p.isBlocking()) {
                                swordsmanshipClass.initiateAbility();
                            }
                        }
                    }
                }.runTaskLater(plugin, waitTicks);
            }

            //Defense
            else if (itemInHandType == Material.AIR) {
                Defense defenseClass = new Defense(p);
                defenseClass.initiateAbility();
            }


        }
    }
}
