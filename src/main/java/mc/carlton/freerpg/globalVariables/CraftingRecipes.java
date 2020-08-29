package mc.carlton.freerpg.globalVariables;

import mc.carlton.freerpg.playerAndServerInfo.ConfigLoad;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class CraftingRecipes {
    static ItemStack[] cowEgg;
    static ItemStack[] beeEgg;
    static ItemStack[] mooshroomEgg1;
    static ItemStack[] mooshroomEgg2;
    static ItemStack[] horseEgg;
    static ItemStack[] slimeEgg;
    static ItemStack[] tippedArrow;
    static ItemStack[] power;
    static ItemStack[] efficiency;
    static ItemStack[] sharpness;
    static ItemStack[] protection;
    static ItemStack[] luck;
    static ItemStack[] lure;
    static ItemStack[] frost;
    static ItemStack[] depth;
    static ItemStack[] mending;
    static ItemStack[] fortune;
    static ItemStack[] waterBreathing;
    static ItemStack[] speed;
    static ItemStack[] fireResistance;
    static ItemStack[] healing;
    static ItemStack[] strength;

    public void initializeAllCraftingRecipes() {
        initializeCraftingRecipes();
    }

    public void initializeCraftingRecipes() {
        ConfigLoad configLoad = new ConfigLoad();
        ArrayList<Object> alchemyInfo = configLoad.getAlchemyInfo();
        cowEgg = new ItemStack[]{new ItemStack(Material.LEATHER, 1), new ItemStack(Material.BEEF, 1), new ItemStack(Material.LEATHER, 1),
                new ItemStack(Material.BEEF, 1), new ItemStack(Material.BONE, 1), new ItemStack(Material.BEEF, 1),
                new ItemStack(Material.LEATHER, 1), new ItemStack(Material.BEEF, 1), new ItemStack(Material.LEATHER, 1)};
        beeEgg = new ItemStack[]{new ItemStack(Material.AIR, 0), new ItemStack(Material.OXEYE_DAISY, 1), new ItemStack(Material.AIR, 0),
                new ItemStack(Material.DANDELION, 1), new ItemStack(Material.HONEY_BOTTLE, 1), new ItemStack(Material.POPPY, 1),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.AZURE_BLUET, 1), new ItemStack(Material.AIR, 0)};
        mooshroomEgg1 = new ItemStack[]{new ItemStack(Material.LEATHER, 1), new ItemStack(Material.RED_MUSHROOM, 1), new ItemStack(Material.LEATHER, 1),
                new ItemStack(Material.BEEF, 1), new ItemStack(Material.BONE, 1), new ItemStack(Material.BEEF, 1),
                new ItemStack(Material.LEATHER, 1), new ItemStack(Material.BEEF, 1), new ItemStack(Material.LEATHER, 1)};
        mooshroomEgg2 = new ItemStack[]{new ItemStack(Material.LEATHER, 1), new ItemStack(Material.BROWN_MUSHROOM, 1), new ItemStack(Material.LEATHER, 1),
                new ItemStack(Material.BEEF, 1), new ItemStack(Material.BONE, 1), new ItemStack(Material.BEEF, 1),
                new ItemStack(Material.LEATHER, 1), new ItemStack(Material.BEEF, 1), new ItemStack(Material.LEATHER, 1)};
        horseEgg = new ItemStack[]{new ItemStack(Material.LEATHER, 1), new ItemStack(Material.SADDLE, 1), new ItemStack(Material.LEATHER, 1),
                new ItemStack(Material.LEATHER, 1), new ItemStack(Material.BONE, 1), new ItemStack(Material.LEATHER, 1),
                new ItemStack(Material.HAY_BLOCK, 1), new ItemStack(Material.HAY_BLOCK, 1), new ItemStack(Material.HAY_BLOCK, 1)};
        slimeEgg = new ItemStack[]{new ItemStack(Material.AIR, 0), new ItemStack(Material.AIR, 0), new ItemStack(Material.AIR, 0),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.SLIME_BALL, 1), new ItemStack(Material.SLIME_BALL, 1),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.SLIME_BALL, 1), new ItemStack(Material.SLIME_BALL, 1)};
        tippedArrow = new ItemStack[]{new ItemStack(Material.ARROW, 1), new ItemStack(Material.ARROW, 1), new ItemStack(Material.ARROW, 1),
                new ItemStack(Material.ARROW, 1), new ItemStack(Material.POTION, 1), new ItemStack(Material.ARROW, 1),
                new ItemStack(Material.ARROW, 1), new ItemStack(Material.ARROW, 1), new ItemStack(Material.ARROW, 1)};

        power = new ItemStack[]{new ItemStack(Material.AIR, 0), new ItemStack(Material.AIR, 0), new ItemStack(Material.AIR, 0),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.PAPER, 1), new ItemStack(Material.PAPER, 1),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.PAPER, 1), new ItemStack(Material.BOW, 1)};

        efficiency = new ItemStack[]{new ItemStack(Material.AIR, 0), new ItemStack(Material.AIR, 0), new ItemStack(Material.AIR, 0),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.PAPER, 1), new ItemStack(Material.PAPER, 1),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.PAPER, 1), new ItemStack(Material.IRON_PICKAXE, 1)};

        sharpness = new ItemStack[]{new ItemStack(Material.IRON_INGOT, 1), new ItemStack(Material.AIR, 0), new ItemStack(Material.AIR, 0),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.PAPER, 1), new ItemStack(Material.PAPER, 1),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.PAPER, 1), new ItemStack(Material.IRON_SWORD, 1)};

        protection = new ItemStack[]{new ItemStack(Material.AIR, 0), new ItemStack(Material.IRON_INGOT, 1), new ItemStack(Material.AIR, 0),
                new ItemStack(Material.IRON_INGOT, 1), new ItemStack(Material.PAPER, 1), new ItemStack(Material.PAPER, 1),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.PAPER, 1), new ItemStack(Material.IRON_INGOT, 1)};

        luck = new ItemStack[]{new ItemStack(Material.RABBIT_FOOT, 1), new ItemStack(Material.AIR, 0), new ItemStack(Material.AIR, 0),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.PAPER, 1), new ItemStack(Material.PAPER, 1),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.PAPER, 1), new ItemStack(Material.FISHING_ROD, 1)};

        lure = new ItemStack[]{new ItemStack(Material.COD_BUCKET, 1), new ItemStack(Material.AIR, 0), new ItemStack(Material.AIR, 0),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.PAPER, 1), new ItemStack(Material.PAPER, 1),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.PAPER, 1), new ItemStack(Material.FISHING_ROD, 1)};

        frost = new ItemStack[]{new ItemStack(Material.AIR, 0), new ItemStack(Material.AIR, 0), new ItemStack(Material.AIR, 0),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.PAPER, 1), new ItemStack(Material.PAPER, 1),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.PAPER, 1), new ItemStack(Material.BLUE_ICE, 1)};

        depth = new ItemStack[]{new ItemStack(Material.AIR, 0), new ItemStack(Material.AIR, 0), new ItemStack(Material.AIR, 0),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.PAPER, 1), new ItemStack(Material.PAPER, 1),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.PAPER, 1), new ItemStack(Material.NAUTILUS_SHELL, 1)};

        mending = new ItemStack[]{new ItemStack(Material.AIR, 0), new ItemStack(Material.AIR, 0), new ItemStack(Material.AIR, 0),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.PAPER, 1), new ItemStack(Material.PAPER, 1),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.PAPER, 1), new ItemStack(Material.DIAMOND_BLOCK, 1)};

        fortune = new ItemStack[]{new ItemStack(Material.AIR, 0), new ItemStack(Material.AIR, 0), new ItemStack(Material.AIR, 0),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.PAPER, 1), new ItemStack(Material.PAPER, 1),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.PAPER, 1), new ItemStack(Material.GOLD_BLOCK, 1)};

        waterBreathing = new ItemStack[]{new ItemStack(Material.AIR, 0), new ItemStack((Material) alchemyInfo.get(21), 1), new ItemStack(Material.AIR, 0),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.GLASS_BOTTLE, 1), new ItemStack(Material.AIR, 0),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.AIR, 0), new ItemStack(Material.AIR, 0)};

        speed = new ItemStack[]{new ItemStack(Material.AIR, 0), new ItemStack((Material) alchemyInfo.get(23), 1), new ItemStack(Material.AIR, 0),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.GLASS_BOTTLE, 1), new ItemStack(Material.AIR, 0),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.AIR, 0), new ItemStack(Material.AIR, 0)};

        fireResistance = new ItemStack[]{new ItemStack(Material.AIR, 0), new ItemStack((Material) alchemyInfo.get(25), 1), new ItemStack(Material.AIR, 0),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.GLASS_BOTTLE, 1), new ItemStack(Material.AIR, 0),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.AIR, 0), new ItemStack(Material.AIR, 0)};

        healing = new ItemStack[]{new ItemStack(Material.AIR, 0), new ItemStack((Material) alchemyInfo.get(27), 1), new ItemStack(Material.AIR, 0),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.GLASS_BOTTLE, 1), new ItemStack(Material.AIR, 0),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.AIR, 0), new ItemStack(Material.AIR, 0)};

        strength = new ItemStack[]{new ItemStack(Material.AIR, 0), new ItemStack((Material) alchemyInfo.get(29), 1), new ItemStack(Material.AIR, 0),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.GLASS_BOTTLE, 1), new ItemStack(Material.AIR, 0),
                new ItemStack(Material.AIR, 0), new ItemStack(Material.AIR, 0), new ItemStack(Material.AIR, 0)};
    }

    public ItemStack[] getBeeEggRecipe() {
        return beeEgg;
    }

    public ItemStack[] getCowEggRecipe() {
        return cowEgg;
    }

    public ItemStack[] getDepthRecipe() {
        return depth;
    }

    public ItemStack[] getEfficiencyRecipe() {
        return efficiency;
    }

    public ItemStack[] getFireResistanceRecipe() {
        return fireResistance;
    }

    public ItemStack[] getFortuneRecipe() {
        return fortune;
    }

    public ItemStack[] getFrostRecipe() {
        return frost;
    }

    public ItemStack[] getHealingRecipe() {
        return healing;
    }

    public ItemStack[] getHorseEggRecipe() {
        return horseEgg;
    }

    public ItemStack[] getLuckRecipe() {
        return luck;
    }

    public ItemStack[] getLureRecipe() {
        return lure;
    }

    public ItemStack[] getMendingRecipe() {
        return mending;
    }

    public ItemStack[] getMooshroomEgg1Recipe() {
        return mooshroomEgg1;
    }

    public ItemStack[] getMooshroomEgg2Recipe() {
        return mooshroomEgg2;
    }

    public ItemStack[] getPowerRecipe() {
        return power;
    }

    public ItemStack[] getProtectionRecipe() {
        return protection;
    }

    public ItemStack[] getSharpnessRecipe() {
        return sharpness;
    }

    public ItemStack[] getSlimeEggRecipe() {
        return slimeEgg;
    }

    public ItemStack[] getSpeedRecipe() {
        return speed;
    }

    public ItemStack[] getStrengthRecipe() {
        return strength;
    }

    public ItemStack[] getTippedArrowRecipe() {
        return tippedArrow;
    }

    public ItemStack[] getWaterBreathingRecipe() {
        return waterBreathing;
    }
}
