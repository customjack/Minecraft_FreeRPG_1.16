package mc.carlton.freerpg.perksAndAbilities;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.FurnaceUserTracker;
import mc.carlton.freerpg.globalVariables.ItemGroups;
import mc.carlton.freerpg.playerAndServerInfo.ChangeStats;
import mc.carlton.freerpg.playerAndServerInfo.ConfigLoad;
import mc.carlton.freerpg.playerAndServerInfo.MinecraftVersion;
import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Furnace;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

public class Smelting {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    private Player p;
    private String pName;
    private ItemStack itemInHand;
    private String skillName = "smelting";
    Map<String,Integer> expMap;

    ChangeStats increaseStats; //Changing Stats

    PlayerStats pStatClass;
    //GET PLAYER STATS LIKE THIS:        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData(p);

    Random rand = new Random(); //Random class Import

    private boolean runMethods;

    public Smelting(Player p) {
        this.p = p;
        this.pName = p.getDisplayName();
        this.itemInHand = p.getInventory().getItemInMainHand();
        this.increaseStats = new ChangeStats(p);
        this.pStatClass = new PlayerStats(p);
        ConfigLoad configLoad = new ConfigLoad();
        this.runMethods = configLoad.getAllowedSkillsMap().get(skillName);
        expMap = configLoad.getExpMapForSkill(skillName);
    }

    public void speedUpFurnace(Furnace furnace,boolean isBlastFurnace) {
        if (!runMethods) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int fastFuelLevel = (int) pStat.get(skillName).get(4);
        int doubleSmeltLevel = (int) pStat.get(skillName).get(9);
        boolean doubleSmelt = false;
        if (doubleSmeltLevel*0.05 > rand.nextDouble()) {
            doubleSmelt = true;
        }
        double defaultCookTime = 200.0;
        if (isBlastFurnace) {
            defaultCookTime = 100.0;
        }
        double speedUpFactor = 1 + fastFuelLevel*0.002;


        World world =furnace.getWorld();
        Location furnaceLoc = furnace.getLocation();
        Material smeltingMaterial = furnace.getInventory().getSmelting().getType();
        double finalDefaultCookTime = defaultCookTime;
        boolean finalDoubleSmelt = doubleSmelt;

        int waitTicks = 1;
        FurnaceUserTracker furnaceUserTracker = new FurnaceUserTracker();
        if (furnaceUserTracker.getWaitingOnTask(furnaceLoc)) {
            waitTicks = 2;
        }
        furnaceUserTracker.setWaitingOnTaskMap(true,furnaceLoc);

        //Stat Increase
        ItemGroups itemGroups = new ItemGroups();
        Map<Material,Material> smeltingMap = itemGroups.getSmeltableItemsMap();
        if (smeltingMap.containsKey(smeltingMaterial)) {
            increaseStats.changeEXP(skillName, getEXP(smeltingMap.get(smeltingMaterial)));
        }
        else {
            increaseStats.changeEXP(skillName, expMap.get("smeltAnythingElse"));
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (checkIfBlockIsFurnace(world, furnaceLoc)) {
                    Furnace FurnaceOneTickLater = (Furnace) world.getBlockAt(furnaceLoc).getState();
                    FurnaceInventory furnaceInventory = FurnaceOneTickLater.getSnapshotInventory();
                    FurnaceOneTickLater.setCookTimeTotal((int) Math.round(finalDefaultCookTime / speedUpFactor));
                    if (furnaceInventory.getResult() != null) {
                        ItemStack result = furnaceInventory.getResult();
                        if (finalDoubleSmelt) {
                            int resultAmount = result.getAmount();
                            result.setAmount(Math.min(64,resultAmount + 1));
                        }
                        furnaceInventory.setResult(result);
                    }
                    FurnaceOneTickLater.update();
                }
                furnaceUserTracker.removeWaitingOnTaskMap(furnaceLoc);
            }
        }.runTaskLater(plugin, waitTicks);


    }
    public void printContents(ItemStack[] contents) {
        ArrayList<ItemStack> newContents = new ArrayList<>(Arrays.asList(contents));
        System.out.println(newContents);
    }

    public boolean checkIfBlockIsFurnace(World world,Location location) {
        Block block =world.getBlockAt(location);
        if (block.getState() instanceof Furnace) {
            return true;
        }
        else {
            return false;
        }
    }

    public void fuelBurn(Furnace furnace,boolean isBlastFurnace) {
        if (!runMethods) {
            return;
        }
        World world = furnace.getWorld();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int fastFuelLevel = (int) pStat.get(skillName).get(4);
        int fuelEfficiencyLevel = (int) pStat.get(skillName).get(7);
        double defaultCookTime = 200.0;
        if (isBlastFurnace) {
            defaultCookTime = 100.0;
        }
        double speedUpFactor = 1 + fastFuelLevel*0.002;
        double burnLengthMultiplier = 1 + fuelEfficiencyLevel*0.2;
        Location furnaceLoc = furnace.getLocation();
        int cookTimeTotal = (int)Math.floor(defaultCookTime /speedUpFactor);
        int cookTimeSoFar = (int) furnace.getCookTime();
        int newCookTime = Math.min(cookTimeSoFar,cookTimeTotal-1);


        //Set furnace data
        furnace.setCookTimeTotal(cookTimeTotal);
        furnace.setCookTime((short)newCookTime);
        ItemStack fuel = furnace.getSnapshotInventory().getFuel();
        fuel.setAmount(fuel.getAmount()-1);
        furnace.getSnapshotInventory().setFuel(fuel);
        furnace.update();

        int waitTicks = 1;
        FurnaceUserTracker furnaceUserTracker = new FurnaceUserTracker();
        if (furnaceUserTracker.getWaitingOnTask(furnaceLoc)) {
            waitTicks = 2;
        }
        furnaceUserTracker.setWaitingOnTaskMap(true,furnaceLoc);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (checkIfBlockIsFurnace(world,furnaceLoc)) {
                    Furnace runningFurnace = (Furnace) world.getBlockAt(furnaceLoc).getState();
                    double defaultBurnTime = runningFurnace.getBurnTime();
                    runningFurnace.setBurnTime((short) Math.ceil(3 + (burnLengthMultiplier * defaultBurnTime / speedUpFactor)));
                    runningFurnace.update();
                    furnaceUserTracker.removeWaitingOnTaskMap(furnaceLoc);
                }
            }
        }.runTaskLater(plugin, waitTicks);
    }

    public void flamePick(Block block,World world,Material blockType) {
        if (!runMethods) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int doubleDropLevel = (int) pStat.get("mining").get(5);
        int doubleDropWoodcuttingLevel = (int) pStat.get("woodcutting").get(5);
        int doubleSmeltLevel = (int) pStat.get(skillName).get(9);
        double chanceDrop = 0.0005*doubleDropLevel;
        double chanceSmelt = doubleSmeltLevel*0.05;
        double chanceLogDrop = 0.0005*doubleDropWoodcuttingLevel;
        int dropAmount = 1;
        if (chanceSmelt > rand.nextDouble()) {
            dropAmount *= 2;
        }
        world.spawnParticle(Particle.FLAME, block.getLocation(), 5);
        switch (blockType) {
            case IRON_ORE:
                block.setType(Material.AIR);
                if (chanceDrop > rand.nextDouble()) {
                    if ((int) pStat.get("mining").get(13) > 0) {
                        dropAmount *= 3;
                    }
                    else {
                        dropAmount *= 2;
                    }
                }
                world.dropItemNaturally(block.getLocation(), new ItemStack(Material.IRON_INGOT, dropAmount));
                increaseStats.changeEXP(skillName,getEXP(Material.IRON_INGOT));
                damageTool();
                if (0.7 > rand.nextDouble()) {
                    ((ExperienceOrb) world.spawn(block.getLocation(), ExperienceOrb.class)).setExperience(1);
                }
                break;
            case GOLD_ORE:
                block.setType(Material.AIR);
                if (chanceDrop > rand.nextDouble()) {
                    if ((int) pStat.get("mining").get(13) > 0) {
                        dropAmount *= 3;
                    }
                    else {
                        dropAmount *= 2;
                    }
                }
                world.dropItemNaturally(block.getLocation(), new ItemStack(Material.GOLD_INGOT, dropAmount));
                increaseStats.changeEXP(skillName,getEXP(Material.GOLD_INGOT));
                damageTool();
                ((ExperienceOrb) world.spawn(block.getLocation(), ExperienceOrb.class)).setExperience(1);
                break;
            case COBBLESTONE:
                block.setType(Material.AIR);
                world.dropItemNaturally(block.getLocation(), new ItemStack(Material.STONE, dropAmount));
                increaseStats.changeEXP(skillName,getEXP(Material.STONE));
                damageTool();
                break;
            case SANDSTONE:
                block.setType(Material.AIR);
                world.dropItemNaturally(block.getLocation(), new ItemStack(Material.SMOOTH_SANDSTONE, dropAmount));
                increaseStats.changeEXP(skillName,getEXP(Material.SMOOTH_SANDSTONE));
                damageTool();
                break;
            case RED_SANDSTONE:
                block.setType(Material.AIR);
                world.dropItemNaturally(block.getLocation(), new ItemStack(Material.SMOOTH_RED_SANDSTONE, dropAmount));
                increaseStats.changeEXP(skillName,getEXP(Material.SMOOTH_RED_SANDSTONE));
                damageTool();
                break;
            case STONE:
                block.setType(Material.AIR);
                world.dropItemNaturally(block.getLocation(), new ItemStack(Material.SMOOTH_STONE, dropAmount));
                increaseStats.changeEXP(skillName,getEXP(Material.SMOOTH_STONE));
                damageTool();
                break;
            case SAND:
                block.setType(Material.AIR);
                world.dropItemNaturally(block.getLocation(), new ItemStack(Material.GLASS, dropAmount));
                increaseStats.changeEXP(skillName,getEXP(Material.GLASS));
                damageTool();
                break;
            case QUARTZ_BLOCK:
                block.setType(Material.AIR);
                world.dropItemNaturally(block.getLocation(), new ItemStack(Material.SMOOTH_QUARTZ, dropAmount));
                increaseStats.changeEXP(skillName,getEXP(Material.SMOOTH_QUARTZ));
                damageTool();
                break;
            case NETHERRACK:
                block.setType(Material.AIR);
                world.dropItemNaturally(block.getLocation(), new ItemStack(Material.NETHER_BRICK, dropAmount));
                increaseStats.changeEXP(skillName,getEXP(Material.NETHER_BRICK));
                damageTool();
                break;
            case CLAY:
                block.setType(Material.AIR);
                world.dropItemNaturally(block.getLocation(), new ItemStack(Material.TERRACOTTA, dropAmount));
                increaseStats.changeEXP(skillName,getEXP(Material.TERRACOTTA));
                damageTool();
                break;
            case WET_SPONGE:
                block.setType(Material.AIR);
                world.dropItemNaturally(block.getLocation(), new ItemStack(Material.SPONGE, dropAmount));
                increaseStats.changeEXP(skillName,getEXP(Material.SPONGE));
                damageTool();
                break;
            case CACTUS:
                block.setType(Material.AIR);
                world.dropItemNaturally(block.getLocation(), new ItemStack(Material.GREEN_DYE, dropAmount));
                increaseStats.changeEXP(skillName,getEXP(Material.GREEN_DYE));
                damageTool();
                break;
            case ACACIA_LOG:
            case BIRCH_LOG:
            case DARK_OAK_LOG:
            case JUNGLE_LOG:
            case OAK_LOG:
            case SPRUCE_LOG:
                block.setType(Material.AIR);
                if (chanceLogDrop > rand.nextDouble()) {
                    dropAmount *= 2;
                }
                world.dropItemNaturally(block.getLocation(), new ItemStack(Material.CHARCOAL, dropAmount));
                increaseStats.changeEXP(skillName,getEXP(Material.CHARCOAL));
                damageTool();
                break;
            default:
                break;
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

    public int getEXP(Material smeltedMaterial) {
        if (!runMethods) {
            return 0;
        }
        int EXP = 0;
        switch (smeltedMaterial) {
            case COOKED_BEEF:
                EXP = expMap.get("smeltBeef");
                break;
            case COOKED_CHICKEN:
                EXP = expMap.get("smeltChicken");
                break;
            case COOKED_MUTTON:
                EXP = expMap.get("smeltMutton");
                break;
            case COOKED_RABBIT:
                EXP = expMap.get("smeltRabbit");
                break;
            case COOKED_PORKCHOP:
                EXP = expMap.get("smeltPorkchop");
                break;
            case GREEN_DYE:
                EXP = expMap.get("smeltGreen_Dye");
                break;
            case LIME_DYE:
                EXP = expMap.get("smeltLime_Dye");
                break;
            case COOKED_COD:
                EXP = expMap.get("smeltCod");
                break;
            case COOKED_SALMON:
                EXP = expMap.get("smeltSalmon");
                break;
            case POPPED_CHORUS_FRUIT:
                EXP = expMap.get("smeltPopped_Chorus_Fruit");
                break;
            case DRIED_KELP:
                EXP = expMap.get("smeltDried_Kelp");
                break;
            case GLASS:
                EXP = expMap.get("smeltGlass");
                break;
            case BRICK:
                EXP = expMap.get("smeltBrick");
                break;
            case NETHER_BRICK:
                EXP = expMap.get("smeltNether_Brick");
                break;
            case STONE:
                EXP = expMap.get("smeltStone");
                break;
            case SMOOTH_SANDSTONE:
                EXP = expMap.get("smeltSmooth_Sandstone");
                break;
            case BAKED_POTATO:
                EXP = expMap.get("smeltBakedPotato");
                break;
            case SMOOTH_RED_SANDSTONE:
                EXP = expMap.get("smeltSmooth_Red_Sandstone");
                break;
            case SMOOTH_STONE:
                EXP = expMap.get("smeltSmooth_Stone");
                break;
            case SMOOTH_QUARTZ:
                EXP = expMap.get("smeltSmoothQuartz");
                break;
            case TERRACOTTA:
                EXP = expMap.get("smeltTerracotta");
                break;
            case GRAY_GLAZED_TERRACOTTA:
            case BLACK_GLAZED_TERRACOTTA:
            case GREEN_GLAZED_TERRACOTTA:
            case BLUE_GLAZED_TERRACOTTA:
            case BROWN_GLAZED_TERRACOTTA:
            case CYAN_GLAZED_TERRACOTTA:
            case LIGHT_BLUE_GLAZED_TERRACOTTA:
            case LIGHT_GRAY_GLAZED_TERRACOTTA:
            case LIME_GLAZED_TERRACOTTA:
            case MAGENTA_GLAZED_TERRACOTTA:
            case ORANGE_GLAZED_TERRACOTTA:
            case PINK_GLAZED_TERRACOTTA:
            case PURPLE_GLAZED_TERRACOTTA:
            case RED_GLAZED_TERRACOTTA:
            case WHITE_GLAZED_TERRACOTTA:
            case YELLOW_GLAZED_TERRACOTTA:
                EXP = expMap.get("smeltGlazed_Terracotta");
                break;
            case CHARCOAL:
                EXP = expMap.get("smeltCharcoal");
                break;
            case IRON_INGOT:
                EXP = expMap.get("smeltIronIngot");
                break;
            case GOLD_INGOT:
                EXP = expMap.get("smeltGoldIngot");
                break;
            case DIAMOND:
                EXP = expMap.get("smeltDiamond");
                break;
            case LAPIS_LAZULI:
                EXP = expMap.get("smeltLapis_Lazuli");
                break;
            case EMERALD:
                EXP = expMap.get("smeltEmerald");
                break;
            case REDSTONE:
                EXP = expMap.get("smeltRedstone");
                break;
            case QUARTZ:
                EXP = expMap.get("smeltQuartz");
                break;
            case SPONGE:
                EXP = expMap.get("smeltSponge");
                break;
            case IRON_NUGGET:
                EXP = expMap.get("smeltIron_Nugget");
                break;
            case GOLD_NUGGET:
                EXP = expMap.get("smeltGold_Nugget");
                break;
            case CRACKED_STONE_BRICKS:
                EXP = expMap.get("smeltCracked_Stone_Bricks");
                break;
            default:
                EXP = expMap.get("smeltAnythingElse");
                break;
        }
        MinecraftVersion minecraftVersion = new MinecraftVersion();
        if (minecraftVersion.getMinecraftVersion_Double() >= 1.16 && EXP != expMap.get("smeltAnythingElse")) {
            if (smeltedMaterial.equals(Material.NETHERITE_SCRAP)) {
                EXP = expMap.get("smeltNetherite_Scrap");
            }
            if (smeltedMaterial.equals(Material.CRACKED_NETHER_BRICKS)) {
                EXP = expMap.get("smeltCracked_Nether_Bricks");
            }
        }
        return EXP;
    }




}
