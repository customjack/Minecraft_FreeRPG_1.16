package mc.carlton.freerpg.perksAndAbilities;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.FurnaceUserTracker;
import mc.carlton.freerpg.globalVariables.ExpMaps;
import mc.carlton.freerpg.globalVariables.ItemGroups;
import mc.carlton.freerpg.playerInfo.ChangeStats;
import mc.carlton.freerpg.serverInfo.ConfigLoad;
import mc.carlton.freerpg.serverInfo.MinecraftVersion;
import mc.carlton.freerpg.playerInfo.PlayerStats;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

public class Smelting extends Skill{
    private String skillName = "smelting";

    Random rand = new Random(); //Random class Import

    private boolean runMethods;

    public Smelting(Player p) {
        super(p);
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
        double speedUpFactor = 1 + fastFuelLevel * 0.002;
        double burnLengthMultiplier = 1 + fuelEfficiencyLevel * 0.2;
        Location furnaceLoc = furnace.getLocation();
        int cookTimeTotal = (int) Math.floor(defaultCookTime / speedUpFactor);
        int cookTimeSoFar = (int) furnace.getCookTime();
        int newCookTime = Math.min(cookTimeSoFar, cookTimeTotal - 1);


        //Set furnace data
        furnace.setCookTimeTotal(cookTimeTotal);
        furnace.setCookTime((short) newCookTime);
        ItemStack fuel = furnace.getSnapshotInventory().getFuel();
        fuel.setAmount(fuel.getAmount() - 1);
        furnace.getSnapshotInventory().setFuel(fuel);
        furnace.update();

        int waitTicks = 1;
        FurnaceUserTracker furnaceUserTracker = new FurnaceUserTracker();
        if (furnaceUserTracker.getWaitingOnTask(furnaceLoc)) {
            waitTicks = 2;
        }
        furnaceUserTracker.setWaitingOnTaskMap(true, furnaceLoc);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (checkIfBlockIsFurnace(world, furnaceLoc)) {
                    Furnace runningFurnace = (Furnace) world.getBlockAt(furnaceLoc).getState();
                    double defaultBurnTime = runningFurnace.getBurnTime();
                    runningFurnace.setBurnTime((short) Math.ceil(3 + (burnLengthMultiplier * defaultBurnTime / speedUpFactor)));
                    runningFurnace.update();
                    furnaceUserTracker.removeWaitingOnTaskMap(furnaceLoc);
                }
            }
        }.runTaskLater(plugin, waitTicks);
    }

    public boolean flamePick(Block block,World world,Material blockType,boolean giveEXP) {
        if (!runMethods) {
            return false;
        }
        ExpMaps expMaps = new ExpMaps();
        if (!expMaps.getFlamePickEXP().containsKey(blockType)) {
            return false;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        if ((int) pStat.get("global").get(13) < 1 || (int) pStat.get("smelting").get(13) < 1) {
            return false;
        }
        ItemGroups itemGroups = new ItemGroups();
        int doubleDropLevel = (int) pStat.get("mining").get(5);
        int doubleDropWoodcuttingLevel = (int) pStat.get("woodcutting").get(5);
        int doubleSmeltLevel = (int) pStat.get(skillName).get(9);
        double chanceDoubleOreDrop = 0.0005*doubleDropLevel;
        double chanceDoubleSmelt = doubleSmeltLevel*0.05;
        double chanceDoubleLogDrop = 0.0005*doubleDropWoodcuttingLevel;
        int dropAmount = 1;
        if (chanceDoubleSmelt > rand.nextDouble()) {
            dropAmount *= 2;
        }
        world.spawnParticle(Particle.FLAME, block.getLocation(), 5);
        block.setType(Material.AIR);
        ConfigLoad configLoad = new ConfigLoad();
        damageTool(configLoad.getDurabilityModifiers().get("flamePick"));
        if (giveEXP) {
            increaseStats.changeEXP(skillName, getEXP(blockType));
        }
        dropXP(itemGroups.getSmeltingXPMap().get(blockType),block.getLocation());
        if (itemGroups.getOres().contains(blockType)) {
            if (chanceDoubleOreDrop > rand.nextDouble()) {
                if ((int) pStat.get("mining").get(13) > 0) {
                    dropAmount *= 3;
                }
                else {
                    dropAmount *= 2;
                }
            }
        }
        else if (itemGroups.getAllLogs().contains(blockType)) {
            if (chanceDoubleLogDrop > rand.nextDouble()) {
                dropAmount *= 2;
            }
        }
        for (int i = 0; i < dropAmount; i++) {
            if (chanceDoubleSmelt > rand.nextDouble()) {
                dropAmount += 1;
            }
        }
        world.dropItemNaturally(block.getLocation(),new ItemStack(itemGroups.getSmeltableItemsMap().get(blockType),dropAmount));
        return true;

    }

    public void dropXP(double avgEXP,Location loc) {
        ConfigLoad configLoad = new ConfigLoad();
        if (!configLoad.isFlamePickGiveXP()) {
            return;
        }
        World world = loc.getWorld();
        int minXPDrop = (int) Math.floor(avgEXP);
        double chanceToRoundUp = avgEXP - minXPDrop;
        if (chanceToRoundUp > rand.nextDouble()) {
            ((ExperienceOrb) world.spawn(loc, ExperienceOrb.class)).setExperience(minXPDrop+1);
        }
        else {
            ((ExperienceOrb) world.spawn(loc, ExperienceOrb.class)).setExperience(minXPDrop);
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
