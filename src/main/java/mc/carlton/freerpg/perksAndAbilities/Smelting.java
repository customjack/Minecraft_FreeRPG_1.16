package mc.carlton.freerpg.perksAndAbilities;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.playerAndServerInfo.ChangeStats;
import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
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
import java.util.Map;
import java.util.Random;

public class Smelting {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    private Player p;
    private String pName;
    private ItemStack itemInHand;

    ChangeStats increaseStats; //Changing Stats

    PlayerStats pStatClass;
    //GET PLAYER STATS LIKE THIS:        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData(p);

    Random rand = new Random(); //Random class Import



    public Smelting(Player p) {
        this.p = p;
        this.pName = p.getDisplayName();
        this.itemInHand = p.getInventory().getItemInMainHand();
        this.increaseStats = new ChangeStats(p);
        this.pStatClass = new PlayerStats(p);
    }

    public void speedUpFurnace(Furnace furnace,boolean isBlastFurnace) {
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int fastFuelLevel = (int) pStat.get("smelting").get(4);
        int doubleSmeltLevel = (int) pStat.get("smelting").get(9);
        boolean doubleSmelt = false;
        if (doubleSmeltLevel*0.05 > rand.nextDouble()) {
            doubleSmelt = true;
        }
        double defaultCookTime = 200.0;
        if (isBlastFurnace) {
            defaultCookTime = 100.0;
        }
        double speedUpFactor = 1 + fastFuelLevel*0.002;

        FurnaceInventory furnaceInv = furnace.getInventory();
        double finalDefaultCookTime = defaultCookTime;
        boolean finalDoubleSmelt = doubleSmelt;
        new BukkitRunnable() {
            @Override
            public void run() {
                furnace.setCookTimeTotal((int) Math.round(finalDefaultCookTime / speedUpFactor));
                FurnaceInventory oldInv = furnace.getSnapshotInventory();
                if (furnaceInv.getResult() == null) {
                    increaseStats.changeEXP("smelting", 100);
                }
                else {
                    oldInv.setSmelting(furnaceInv.getSmelting());
                    ItemStack result = furnaceInv.getResult();
                    if (finalDoubleSmelt) {
                        int resultAmount = result.getAmount();
                        result.setAmount(resultAmount + 1);
                    }
                    oldInv.setResult(result);
                    increaseStats.changeEXP("smelting", getEXP(oldInv.getResult().getType()));
                    furnace.update();
                }
            }
        }.runTaskLater(plugin, 1);


    }

    public void fuelBurn(Furnace furnace,boolean isBlastFurnace) {
        World world = furnace.getWorld();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int fastFuelLevel = (int) pStat.get("smelting").get(4);
        int fuelEfficiencyLevel = (int) pStat.get("smelting").get(7);
        double defaultCookTime = 200.0;
        if (isBlastFurnace) {
            defaultCookTime = 100.0;
        }
        double speedUpFactor = 1 + fastFuelLevel*0.002;
        double burnLengthMultiplier = 1 + fuelEfficiencyLevel*0.2;
        Location furnaceLoc = furnace.getLocation();

        furnace.setCookTimeTotal((int)Math.floor(defaultCookTime /speedUpFactor));
        ItemStack fuel = furnace.getSnapshotInventory().getFuel();
        fuel.setAmount(fuel.getAmount()-1);
        furnace.getSnapshotInventory().setFuel(fuel);
        furnace.update();

        new BukkitRunnable() {
            @Override
            public void run() {
                Furnace runningFurnace = (Furnace) world.getBlockAt(furnaceLoc).getState();
                double defaultBurnTime = runningFurnace.getBurnTime();
                runningFurnace.setBurnTime((short) Math.ceil(3 + (burnLengthMultiplier*defaultBurnTime/speedUpFactor)));
                runningFurnace.update();
            }
        }.runTaskLater(plugin, 1);
    }

    public void flamePick(Block block,World world,Material blockType) {
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int doubleDropLevel = (int) pStat.get("mining").get(5);
        int doubleDropWoodcuttingLevel = (int) pStat.get("woodcutting").get(5);
        int doubleSmeltLevel = (int) pStat.get("smelting").get(9);
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
                increaseStats.changeEXP("smelting",getEXP(Material.IRON_INGOT));
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
                increaseStats.changeEXP("smelting",getEXP(Material.GOLD_INGOT));
                damageTool();
                ((ExperienceOrb) world.spawn(block.getLocation(), ExperienceOrb.class)).setExperience(1);
                break;
            case COBBLESTONE:
                block.setType(Material.AIR);
                world.dropItemNaturally(block.getLocation(), new ItemStack(Material.STONE, dropAmount));
                increaseStats.changeEXP("smelting",getEXP(Material.STONE));
                damageTool();
                break;
            case SANDSTONE:
                block.setType(Material.AIR);
                world.dropItemNaturally(block.getLocation(), new ItemStack(Material.SMOOTH_SANDSTONE, dropAmount));
                increaseStats.changeEXP("smelting",getEXP(Material.SMOOTH_SANDSTONE));
                damageTool();
                break;
            case RED_SANDSTONE:
                block.setType(Material.AIR);
                world.dropItemNaturally(block.getLocation(), new ItemStack(Material.SMOOTH_RED_SANDSTONE, dropAmount));
                increaseStats.changeEXP("smelting",getEXP(Material.SMOOTH_RED_SANDSTONE));
                damageTool();
                break;
            case STONE:
                block.setType(Material.AIR);
                world.dropItemNaturally(block.getLocation(), new ItemStack(Material.SMOOTH_STONE, dropAmount));
                increaseStats.changeEXP("smelting",getEXP(Material.SMOOTH_STONE));
                damageTool();
                break;
            case SAND:
                block.setType(Material.AIR);
                world.dropItemNaturally(block.getLocation(), new ItemStack(Material.GLASS, dropAmount));
                increaseStats.changeEXP("smelting",getEXP(Material.GLASS));
                damageTool();
                break;
            case QUARTZ_BLOCK:
                block.setType(Material.AIR);
                world.dropItemNaturally(block.getLocation(), new ItemStack(Material.SMOOTH_QUARTZ, dropAmount));
                increaseStats.changeEXP("smelting",getEXP(Material.SMOOTH_QUARTZ));
                damageTool();
                break;
            case NETHERRACK:
                block.setType(Material.AIR);
                world.dropItemNaturally(block.getLocation(), new ItemStack(Material.NETHER_BRICK, dropAmount));
                increaseStats.changeEXP("smelting",getEXP(Material.NETHER_BRICK));
                damageTool();
                break;
            case CLAY:
                block.setType(Material.AIR);
                world.dropItemNaturally(block.getLocation(), new ItemStack(Material.TERRACOTTA, dropAmount));
                increaseStats.changeEXP("smelting",getEXP(Material.TERRACOTTA));
                damageTool();
                break;
            case WET_SPONGE:
                block.setType(Material.AIR);
                world.dropItemNaturally(block.getLocation(), new ItemStack(Material.SPONGE, dropAmount));
                increaseStats.changeEXP("smelting",getEXP(Material.SPONGE));
                damageTool();
                break;
            case CACTUS:
                block.setType(Material.AIR);
                world.dropItemNaturally(block.getLocation(), new ItemStack(Material.GREEN_DYE, dropAmount));
                increaseStats.changeEXP("smelting",getEXP(Material.GREEN_DYE));
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
                increaseStats.changeEXP("smelting",getEXP(Material.CHARCOAL));
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
        int EXP = 0;
        switch (smeltedMaterial) {
            case COOKED_BEEF:
            case COOKED_CHICKEN:
            case COOKED_MUTTON:
            case COOKED_RABBIT:
            case COOKED_PORKCHOP:
            case GREEN_DYE:
            case LIME_DYE:
                EXP = 175;
                break;
            case COOKED_COD:
            case COOKED_SALMON:
            case POPPED_CHORUS_FRUIT:
                EXP = 200;
                break;
            case DRIED_KELP:
            case GLASS:
            case BRICK:
            case NETHER_BRICK:
                EXP = 110;
                break;
            case STONE:
            case SMOOTH_SANDSTONE:
            case BAKED_POTATO:
            case SMOOTH_RED_SANDSTONE:
            case SMOOTH_STONE:
            case SMOOTH_QUARTZ:
            case TERRACOTTA:
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
            case CHARCOAL:
                EXP = 130;
                break;
            case IRON_INGOT:
                EXP = 300;
                break;
            case GOLD_INGOT:
                EXP = 320;
                break;
            case DIAMOND:
                EXP = 650;
                break;
            case LAPIS_LAZULI:
                EXP = 520;
                break;
            case EMERALD:
                EXP = 800;
                break;
            case REDSTONE:
            case QUARTZ:
            case SPONGE:
                EXP = 260;
                break;
            case IRON_NUGGET:
                EXP = 330;
                break;
            case GOLD_NUGGET:
                EXP = 390;
                break;
                //1.16 Items, Subject to EXP changes
            case NETHERITE_SCRAP:
                EXP = 1000;
                break;
            case CRACKED_NETHER_BRICKS:
            case CRACKED_STONE_BRICKS:
                EXP = 140;
            default:
                break;
        }
        return EXP;
    }




}
