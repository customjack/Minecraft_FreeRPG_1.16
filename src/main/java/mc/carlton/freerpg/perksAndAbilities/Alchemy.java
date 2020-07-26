package mc.carlton.freerpg.perksAndAbilities;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.playerAndServerInfo.ChangeStats;
import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Alchemy {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    private Player p;
    private String pName;
    private ItemStack itemInHand;

    ChangeStats increaseStats; //Changing Stats

    PlayerStats pStatClass;
    //GET PLAYER STATS LIKE THIS:        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData(p);

    Random rand = new Random(); //Random class Import
    private static Map<BrewerInventory, Integer> counter = new HashMap<>();
    private static Map<BrewerInventory, Integer> failSafe = new HashMap<>();


    public Alchemy(Player p) {
        this.p = p;
        this.pName = p.getDisplayName();
        this.itemInHand = p.getInventory().getItemInMainHand();
        this.increaseStats = new ChangeStats(p);
        this.pStatClass = new PlayerStats(p);
    }

    public void startBrewing(BrewerInventory inventory, ItemStack output, ItemStack input) {
        if (counter.containsKey(inventory)) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        //int speedBrewingLevel = (int) pStat.get("alchemy").get(7);
        int time = 400;
        BrewingStand stand = inventory.getHolder();
        World world = stand.getWorld();
        ItemStack[] originalContents = {inventory.getItem(0), inventory.getItem(1), inventory.getItem(2), inventory.getItem(3), inventory.getItem(4)};
        counter.put(inventory, time);
        failSafe.put(inventory, time + 2);
        stand.setBrewingTime(time);
        if (stand.getFuelLevel() == 0) {
            return;
        }
        stand.update();
        new BukkitRunnable() {
            @Override
            public void run() {
                failSafe.put(inventory, failSafe.get(inventory) - 1);
                if (failSafe.get(inventory) < 0) {
                    failSafe.remove(inventory);
                    counter.remove(inventory);
                    cancel();
                }
                int timer = counter.get(inventory);

                if (timer == 0) //Finished brewing item changes
                {
                    originalContents[3].setAmount(originalContents[3].getAmount() - 1);
                    stand.getSnapshotInventory().setItem(3, originalContents[3]);
                    PotionMeta outputMeta = (PotionMeta) output.getItemMeta();
                    PotionEffect oldEffect = outputMeta.getCustomEffects().get(0);
                    String normalName = outputMeta.getDisplayName();
                    for (int i = 0; i < 3; i++) {
                        if (inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR) {
                            continue;
                        }

                        if (inventory.getItem(i).getType() == Material.SPLASH_POTION) {
                            output.setType(Material.SPLASH_POTION);
                            outputMeta.setDisplayName(ChatColor.RESET + "Splash " + normalName);
                            output.setItemMeta(outputMeta);
                        } else if (inventory.getItem(i).getType() == Material.LINGERING_POTION) {
                            output.setType(Material.LINGERING_POTION);
                            outputMeta.setDisplayName(ChatColor.RESET + "Lingering " + normalName);
                            PotionEffectType effect = outputMeta.getCustomEffects().get(0).getType();
                            int newLength = (int) Math.round(outputMeta.getCustomEffects().get(0).getDuration() / 4.0);
                            outputMeta.addCustomEffect(new PotionEffect(effect, newLength, 0), true);
                            output.setItemMeta(outputMeta);
                        } else {
                            output.setType(Material.POTION);
                            outputMeta.setDisplayName(ChatColor.RESET + normalName);
                            output.setItemMeta(outputMeta);
                        }
                        inventory.setItem(i, output);
                        stand.getSnapshotInventory().setItem(i, output);
                        outputMeta.addCustomEffect(oldEffect, true);
                        increaseStats.changeEXP("alchemy",4000);

                    }

                    stand.setFuelLevel(stand.getFuelLevel() - 1);
                    stand.update();
                    world.playEffect(stand.getLocation(), Effect.BREWING_STAND_BREW, 1);
                    counter.remove(inventory);
                    failSafe.remove(inventory);
                    cancel();
                    return;
                }

                // ingredient removed checks
                if (inventory.getIngredient() == null) {
                    counter.remove(inventory);
                    failSafe.remove(inventory);
                    cancel();
                    return;
                }
                if (inventory.getIngredient().getType() != input.getType()) {
                    counter.remove(inventory);
                    failSafe.remove(inventory);
                    cancel();
                    return;
                }

                // water bottles removed check
                boolean[] bottles = {false, false, false};
                for (int i = 0; i < 3; i++) {
                    if (inventory.getItem(i) != null && inventory.getItem(i).getType() != Material.AIR) {
                        bottles[i] = true;
                    }
                }
                if (!bottles[0] && !bottles[1] && !bottles[2]) {
                    counter.remove(inventory);
                    failSafe.remove(inventory);
                    cancel();
                    return;
                }
                //Update counter progress
                counter.put(inventory, timer - 1);
                stand.setBrewingTime(timer - 1);
                for (int i = 0; i < 5; i++) {
                    if (inventory.getItem(i) == null && originalContents[i] == null) {
                        continue;
                    } else if (inventory.getItem(i) == null) {
                        stand.getSnapshotInventory().setItem(i, inventory.getItem(i));
                        originalContents[i] = inventory.getItem(i);
                    } else if (originalContents[i] == null) {
                        stand.getSnapshotInventory().setItem(i, inventory.getItem(i));
                        originalContents[i] = inventory.getItem(i);
                    } else if (!inventory.getItem(i).equals(originalContents[i])) {
                        stand.getSnapshotInventory().setItem(i, inventory.getItem(i));
                        originalContents[i] = inventory.getItem(i);
                    }
                }
                stand.update();

            }
        }.runTaskTimer(plugin, 1, 1);
    }

    public void upgradeBrewing(BrewerInventory inventory, ItemStack input, boolean[] slotsToCheck) {
        if (input.getType() != Material.REDSTONE && input.getType() != Material.GLOWSTONE_DUST) {
            return;
        }
        double durationMultiplier = 1;
        int potency = 0;
        if (input.getType() == Material.REDSTONE) {
            durationMultiplier = 8.0 / 3.0;
        } else if (input.getType() == Material.GLOWSTONE_DUST) {
            potency = 1;
            durationMultiplier = 0.5;
        }
        if (counter.containsKey(inventory)) {
            return;
        }
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        //int speedBrewingLevel = (int) pStat.get("alchemy").get(7);
        int time = 400; //(int) Math.round((1 - speedBrewingLevel * 0.15) * 400);
        BrewingStand stand = inventory.getHolder();
        World world = stand.getWorld();
        ItemStack[] originalContents = {inventory.getItem(0), inventory.getItem(1), inventory.getItem(2), inventory.getItem(3), inventory.getItem(4)};
        counter.put(inventory, time);
        failSafe.put(inventory, time + 2);
        stand.setBrewingTime(time);
        if (stand.getFuelLevel() == 0) {
            return;
        }
        stand.update();
        double finalDurationMultiplier = durationMultiplier;
        int finalPotency = potency;
        new BukkitRunnable() {
            @Override
            public void run() {
                failSafe.put(inventory, failSafe.get(inventory) - 1);
                if (failSafe.get(inventory) < 0) {
                    failSafe.remove(inventory);
                    counter.remove(inventory);
                    cancel();
                }
                int timer = counter.get(inventory);

                if (timer == 0) //Finished brewing item changes
                {
                    originalContents[3].setAmount(originalContents[3].getAmount() - 1);
                    stand.getSnapshotInventory().setItem(3, originalContents[3]);
                    for (int i = 0; i < 3; i++) {
                        if (!slotsToCheck[i]) {
                            continue;
                        }
                        if (inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR) {
                            continue;
                        }
                        ItemStack potion = inventory.getItem(i);
                        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
                        PotionEffectType effect = potionMeta.getCustomEffects().get(0).getType();
                        int newLength = (int) Math.round(potionMeta.getCustomEffects().get(0).getDuration() * finalDurationMultiplier);
                        potionMeta.addCustomEffect(new PotionEffect(effect, newLength, finalPotency), true);
                        if (finalPotency == 1) { //Glowstone
                            potionMeta.setDisplayName(potionMeta.getDisplayName() + " II");
                        }

                        potion.setItemMeta(potionMeta);
                        stand.getSnapshotInventory().setItem(i, potion);
                        increaseStats.changeEXP("alchemy",2500);
                    }

                    stand.setFuelLevel(stand.getFuelLevel() - 1);
                    stand.update();
                    world.playEffect(stand.getLocation(), Effect.BREWING_STAND_BREW, 1);
                    counter.remove(inventory);
                    failSafe.remove(inventory);
                    cancel();
                    return;
                }

                // ingredient removed checks
                if (inventory.getIngredient() == null) {
                    counter.remove(inventory);
                    failSafe.remove(inventory);
                    cancel();
                    return;
                }
                if (inventory.getIngredient().getType() != input.getType()) {
                    counter.remove(inventory);
                    failSafe.remove(inventory);
                    cancel();
                    return;
                }

                // water bottles removed check
                boolean[] bottles = {false, false, false};
                for (int i = 0; i < 3; i++) {
                    if (inventory.getItem(i) != null && inventory.getItem(i).getType() != Material.AIR) {
                        bottles[i] = true;
                    }
                }
                if (!bottles[0] && !bottles[1] && !bottles[2]) {
                    counter.remove(inventory);
                    failSafe.remove(inventory);
                    cancel();
                    return;
                }
                //Update counter progress
                counter.put(inventory, timer - 1);
                stand.setBrewingTime(timer - 1);

                //Check for item changes
                for (int i = 0; i < 5; i++) {
                    if (inventory.getItem(i) == null && originalContents[i] == null) {
                        continue;
                    } else if (inventory.getItem(i) == null) {
                        stand.getSnapshotInventory().setItem(i, inventory.getItem(i));
                        originalContents[i] = inventory.getItem(i);
                    } else if (originalContents[i] == null) {
                        stand.getSnapshotInventory().setItem(i, inventory.getItem(i));
                        originalContents[i] = inventory.getItem(i);
                    } else if (!(inventory.getItem(i).equals(originalContents[i]))) {
                        stand.getSnapshotInventory().setItem(i, inventory.getItem(i));
                        originalContents[i] = inventory.getItem(i);
                    }
                }
                stand.update();

            }
        }.runTaskTimer(plugin, 1, 1);
    }

    public boolean comparePotionEffects(ItemStack p1, ItemStack p2) {
        if (p1 == null || p2 == null) {
            return false;
        }
        if (p1.getType() == Material.AIR || p2.getType() == Material.AIR) {
            return false;
        }

        if (!(p1.getItemMeta() instanceof PotionMeta) || !(p2.getItemMeta() instanceof PotionMeta)) {
            return false;
        }
        PotionType p1Type = ((PotionMeta) p1.getItemMeta()).getBasePotionData().getType();
        PotionType p2Type = ((PotionMeta) p2.getItemMeta()).getBasePotionData().getType();
        if (p1Type == p2Type) {
            return true;
        }
        return false;
    }


    public void drinkPotion(ItemStack potion) {
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int lengthBoostLevel = (int) pStat.get("alchemy").get(4);
        double durationMultiplier = 1.0 + 0.001 * lengthBoostLevel;
        int potionMasterLevel = (int) Math.min((int) pStat.get("alchemy").get(13), 1);
        if ((int) pStat.get("global").get(15) != 1) {
            potionMasterLevel = 0;
        }
        if (potion.getType() != Material.POTION) {
            return;
        }
        if (potion.getItemMeta() instanceof PotionMeta) {
            PotionType[] noEXPPots0 = {PotionType.MUNDANE,PotionType.WATER,PotionType.AWKWARD,PotionType.THICK,PotionType.UNCRAFTABLE};
            List<PotionType> noEXPPots = Arrays.asList(noEXPPots0);
            if (!noEXPPots.contains( ((PotionMeta) potion.getItemMeta()).getBasePotionData().getType() ) ) {
                if ( ((PotionMeta) potion.getItemMeta()).getBasePotionData().isUpgraded() ) {
                    increaseStats.changeEXP("alchemy", 2000);
                }
                else if ( ((PotionMeta) potion.getItemMeta()).getBasePotionData().isExtended() ) {
                    increaseStats.changeEXP("alchemy", 2000);
                }
                else {
                    increaseStats.changeEXP("alchemy", 1000);
                }
            }


            if (((PotionMeta) potion.getItemMeta()).hasCustomEffects()) {
                PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
                increaseStats.changeEXP("alchemy", 2000);
                for (PotionEffect effect : potionMeta.getCustomEffects()) {
                    p.addPotionEffect(new PotionEffect(effect.getType(), (int) Math.round(effect.getDuration() * durationMultiplier), effect.getAmplifier() + potionMasterLevel), true);
                }
            } else {
                PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
                PotionData potionData = potionMeta.getBasePotionData();
                PotionEffect pEffect = potionToEffect(potionData);
                if (pEffect.getType() != PotionEffectType.BAD_OMEN) {
                    p.addPotionEffect(pEffect, true);
                }

            }
        }
    }

    public PotionEffect potionToEffect(PotionData potionData) {
        PotionEffect pEffect = new PotionEffect(PotionEffectType.BAD_OMEN, 1, 1);
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int lengthBoostLevel = (int) pStat.get("alchemy").get(4);
        double durationMultiplier = 1.0 + 0.001 * lengthBoostLevel;
        int potionMasterLevel = (int) Math.min((int) pStat.get("alchemy").get(13), 1);
        switch (potionData.getType()) {
            case WEAKNESS:
                if (potionData.isExtended()) {
                    pEffect = new PotionEffect(PotionEffectType.WEAKNESS, (int) Math.round(20 * 60 * 4 * durationMultiplier), potionMasterLevel);
                } else if (potionData.isUpgraded()) {
                    pEffect = new PotionEffect(PotionEffectType.WEAKNESS, (int) Math.round(20 * 90 * durationMultiplier), 1 + potionMasterLevel);
                } else {
                    pEffect = new PotionEffect(PotionEffectType.WEAKNESS, (int) Math.round(20 * 90 * durationMultiplier), potionMasterLevel);
                }
                break;
            case POISON:
                if (potionData.isExtended()) {
                    pEffect = new PotionEffect(PotionEffectType.POISON, (int) Math.round(20 * 90 * durationMultiplier), potionMasterLevel);
                } else if (potionData.isUpgraded()) {
                    pEffect = new PotionEffect(PotionEffectType.POISON, (int) Math.round(20 * 21 * durationMultiplier), 1 + potionMasterLevel);
                } else {
                    pEffect = new PotionEffect(PotionEffectType.POISON, (int) Math.round(20 * 45 * durationMultiplier), potionMasterLevel);
                }
                break;
            case JUMP:
                if (potionData.isExtended()) {
                    pEffect = new PotionEffect(PotionEffectType.JUMP, (int) Math.round(20 * 8 * 60 * durationMultiplier), potionMasterLevel);
                } else if (potionData.isUpgraded()) {
                    pEffect = new PotionEffect(PotionEffectType.JUMP, (int) Math.round(20 * 90 * durationMultiplier), 1 + potionMasterLevel);
                } else {
                    pEffect = new PotionEffect(PotionEffectType.JUMP, (int) Math.round(20 * 180 * durationMultiplier), potionMasterLevel);
                }
                break;
            case SPEED:
                if (potionData.isExtended()) {
                    pEffect = new PotionEffect(PotionEffectType.SPEED, (int) Math.round(20 * 8 * 60 * durationMultiplier), potionMasterLevel);
                } else if (potionData.isUpgraded()) {
                    pEffect = new PotionEffect(PotionEffectType.SPEED, (int) Math.round(20 * 90 * durationMultiplier), 1 + potionMasterLevel);
                } else {
                    pEffect = new PotionEffect(PotionEffectType.SPEED, (int) Math.round(20 * 180 * durationMultiplier), potionMasterLevel);
                }
                break;
            case WATER_BREATHING:
                if (potionData.isExtended()) {
                    pEffect = new PotionEffect(PotionEffectType.WATER_BREATHING, (int) Math.round(20 * 8 * 60 * durationMultiplier), potionMasterLevel);
                } else if (potionData.isUpgraded()) {
                    pEffect = new PotionEffect(PotionEffectType.WATER_BREATHING, (int) Math.round(20 * 90 * durationMultiplier), 1 + potionMasterLevel);
                } else {
                    pEffect = new PotionEffect(PotionEffectType.WATER_BREATHING, (int) Math.round(20 * 180 * durationMultiplier), potionMasterLevel);
                }
                break;
            case FIRE_RESISTANCE:
                if (potionData.isExtended()) {
                    pEffect = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, (int) Math.round(20 * 8 * 60 * durationMultiplier), potionMasterLevel);
                } else if (potionData.isUpgraded()) {
                    pEffect = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, (int) Math.round(20 * 90 * durationMultiplier), 1 + potionMasterLevel);
                } else {
                    pEffect = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, (int) Math.round(20 * 180 * durationMultiplier), potionMasterLevel);
                }
                break;
            case INSTANT_DAMAGE:
                if (potionData.isExtended()) {
                    pEffect = new PotionEffect(PotionEffectType.HARM, (int) Math.round(20 * 1 * durationMultiplier), potionMasterLevel);
                } else if (potionData.isUpgraded()) {
                    pEffect = new PotionEffect(PotionEffectType.HARM, (int) Math.round(20 * 1 * durationMultiplier), 1 + potionMasterLevel);
                } else {
                    pEffect = new PotionEffect(PotionEffectType.HARM, (int) Math.round(20 * 1 * durationMultiplier), potionMasterLevel);
                }
                break;
            case SLOW_FALLING:
                if (potionData.isExtended()) {
                    pEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, (int) Math.round(20 * 4 * 60 * durationMultiplier), potionMasterLevel);
                } else if (potionData.isUpgraded()) {
                    pEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, (int) Math.round(20 * 90 * durationMultiplier), 1 + potionMasterLevel);
                } else {
                    pEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, (int) Math.round(20 * 90 * durationMultiplier), potionMasterLevel);
                }
                break;
            case NIGHT_VISION:
                if (potionData.isExtended()) {
                    pEffect = new PotionEffect(PotionEffectType.NIGHT_VISION, (int) Math.round(20 * 8 * 60 * durationMultiplier), potionMasterLevel);
                } else if (potionData.isUpgraded()) {
                    pEffect = new PotionEffect(PotionEffectType.NIGHT_VISION, (int) Math.round(20 * 90 * durationMultiplier), 1 + potionMasterLevel);
                } else {
                    pEffect = new PotionEffect(PotionEffectType.NIGHT_VISION, (int) Math.round(20 * 180 * durationMultiplier), potionMasterLevel);
                }
                break;
            case INVISIBILITY:
                if (potionData.isExtended()) {
                    pEffect = new PotionEffect(PotionEffectType.INVISIBILITY, (int) Math.round(20 * 8 * 60 * durationMultiplier), potionMasterLevel);
                } else if (potionData.isUpgraded()) {
                    pEffect = new PotionEffect(PotionEffectType.INVISIBILITY, (int) Math.round(20 * 90 * durationMultiplier), 1 + potionMasterLevel);
                } else {
                    pEffect = new PotionEffect(PotionEffectType.INVISIBILITY, (int) Math.round(20 * 180 * durationMultiplier), potionMasterLevel);
                }
                break;
            case INSTANT_HEAL:
                if (potionData.isExtended()) {
                    pEffect = new PotionEffect(PotionEffectType.HEAL, (int) Math.round(20 * 1 * durationMultiplier), potionMasterLevel);
                } else if (potionData.isUpgraded()) {
                    pEffect = new PotionEffect(PotionEffectType.HEAL, (int) Math.round(20 * 1 * durationMultiplier), 1 + potionMasterLevel);
                } else {
                    pEffect = new PotionEffect(PotionEffectType.HEAL, (int) Math.round(20 * 1 * durationMultiplier), potionMasterLevel);
                }
                break;
            case STRENGTH:
                if (potionData.isExtended()) {
                    pEffect = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, (int) Math.round(20 * 8 * 60 * durationMultiplier), potionMasterLevel);
                } else if (potionData.isUpgraded()) {
                    pEffect = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, (int) Math.round(20 * 90 * durationMultiplier), 1 + potionMasterLevel);
                } else {
                    pEffect = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, (int) Math.round(20 * 180 * durationMultiplier), potionMasterLevel);
                }
                break;
            case SLOWNESS:
                if (potionData.isExtended()) {
                    pEffect = new PotionEffect(PotionEffectType.SLOW, (int) Math.round(20 * 4 * 60 * durationMultiplier), potionMasterLevel);
                } else if (potionData.isUpgraded()) {
                    pEffect = new PotionEffect(PotionEffectType.SLOW, (int) Math.round(20 * 20 * durationMultiplier), 3 + potionMasterLevel);
                } else {
                    pEffect = new PotionEffect(PotionEffectType.SLOW, (int) Math.round(20 * 90 * durationMultiplier), potionMasterLevel);
                }
                break;
            case REGEN:
                if (potionData.isExtended()) {
                    pEffect = new PotionEffect(PotionEffectType.REGENERATION, (int) Math.round(20 * 90 * durationMultiplier), potionMasterLevel);
                } else if (potionData.isUpgraded()) {
                    pEffect = new PotionEffect(PotionEffectType.REGENERATION, (int) Math.round(20 * 22 * durationMultiplier), 1 + potionMasterLevel);
                } else {
                    pEffect = new PotionEffect(PotionEffectType.REGENERATION, (int) Math.round(20 * 45 * durationMultiplier), potionMasterLevel);
                }
                break;
            default:
                break;
        }
        return pEffect;
    }

    public void giveBrewingEXP(ItemStack ingredient, ItemStack[] slots) {
        int brewedPotions = 0;
        for (int i = 0; i < 3; i++) {
            if (slots[i] != null) {
                if (slots[i].getType() != Material.AIR) {
                    brewedPotions += 1;
                }
            }
        }
        int expToGive = 0;
        switch (ingredient.getType()) {
            case SUGAR:
                expToGive = 1500;
                break;
            case RABBIT_FOOT:
                expToGive = 1750;
                break;
            case BLAZE_POWDER:
                expToGive = 1500;
                break;
            case GLISTERING_MELON_SLICE:
                expToGive = 1600;
                break;
            case SPIDER_EYE:
                expToGive = 1400;
                break;
            case GHAST_TEAR:
                expToGive = 1700;
                break;
            case MAGMA_CREAM:
                expToGive = 1650;
                break;
            case PUFFERFISH:
                expToGive = 1800;
                break;
            case GOLDEN_CARROT:
                expToGive = 1600;
                break;
            case TURTLE_HELMET:
                expToGive = 1650;
                break;
            case PHANTOM_MEMBRANE:
                expToGive = 1600;
                break;
            case FERMENTED_SPIDER_EYE:
                expToGive = 1550;
                break;
            case NETHER_WART:
                expToGive = 700;
                break;
            case GUNPOWDER:
                expToGive = 800;
                break;
            case GLOWSTONE:
            case REDSTONE:
                expToGive = 900;
                break;
            default:
                expToGive = 400;

        }
        increaseStats.changeEXP("alchemy",expToGive*brewedPotions);


    }
}
