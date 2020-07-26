package mc.carlton.freerpg.guiCommands;

import mc.carlton.freerpg.FreeRPG;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CraftingGUI implements CommandExecutor {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            String[] labels_0 = {"archery1", "farming1", "farming2", "farming3", "farming4", "farming5",
                    "enchanting1", "enchanting2", "enchanting3", "enchanting4", "enchanting5",
                    "enchanting6", "enchanting7", "enchanting8", "enchanting9", "enchanting10",
                    "alchemy1","alchemy2","alchemy3","alchemy4","alchemy5"};
            List<String> labels_arr = Arrays.asList(labels_0);
            if (args.length != 1) {
                p.sendMessage(ChatColor.RED + "Error: Try /craftingRecipe [skillName[Number]]");
            } else if (labels_arr.indexOf(args[0]) == -1) {
                p.sendMessage(ChatColor.RED + "Error: Try /craftingRecipe [skillName[Number]]");
            } else {
                String craftingLabel = args[0];
                ItemStack[] cowEgg = {new ItemStack(Material.LEATHER,1), new ItemStack(Material.BEEF,1),new ItemStack(Material.LEATHER,1),
                        new ItemStack(Material.BEEF,1), new ItemStack(Material.BONE,1), new ItemStack(Material.BEEF,1),
                        new ItemStack(Material.LEATHER,1), new ItemStack(Material.BEEF,1),new ItemStack(Material.LEATHER,1)};
                ItemStack[] beeEgg = {new ItemStack(Material.AIR,0), new ItemStack(Material.OXEYE_DAISY,1),new ItemStack(Material.AIR,0),
                        new ItemStack(Material.DANDELION,1), new ItemStack(Material.HONEY_BOTTLE,1), new ItemStack(Material.POPPY,1),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.AZURE_BLUET,1),new ItemStack(Material.AIR,0)};
                ItemStack[] mooshroomEgg1 = {new ItemStack(Material.LEATHER,1), new ItemStack(Material.RED_MUSHROOM,1),new ItemStack(Material.LEATHER,1),
                        new ItemStack(Material.BEEF,1), new ItemStack(Material.BONE,1), new ItemStack(Material.BEEF,1),
                        new ItemStack(Material.LEATHER,1), new ItemStack(Material.BEEF,1),new ItemStack(Material.LEATHER,1)};
                ItemStack[] mooshroomEgg2 = {new ItemStack(Material.LEATHER,1), new ItemStack(Material.BROWN_MUSHROOM,1),new ItemStack(Material.LEATHER,1),
                        new ItemStack(Material.BEEF,1), new ItemStack(Material.BONE,1), new ItemStack(Material.BEEF,1),
                        new ItemStack(Material.LEATHER,1), new ItemStack(Material.BEEF,1),new ItemStack(Material.LEATHER,1)};
                ItemStack[] horseEgg = {new ItemStack(Material.LEATHER,1), new ItemStack(Material.SADDLE,1),new ItemStack(Material.LEATHER,1),
                        new ItemStack(Material.LEATHER,1), new ItemStack(Material.BONE,1), new ItemStack(Material.LEATHER,1),
                        new ItemStack(Material.HAY_BLOCK,1), new ItemStack(Material.HAY_BLOCK,1),new ItemStack(Material.HAY_BLOCK,1)};
                ItemStack[] slimeEgg = {new ItemStack(Material.AIR,0), new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.SLIME_BALL,1),new ItemStack(Material.SLIME_BALL,1),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.SLIME_BALL,1),new ItemStack(Material.SLIME_BALL,1)};
                ItemStack[] tippedArrow = {new ItemStack(Material.ARROW,1), new ItemStack(Material.ARROW,1),new ItemStack(Material.ARROW,1),
                        new ItemStack(Material.ARROW,1), new ItemStack(Material.POTION,1),new ItemStack(Material.ARROW,1),
                        new ItemStack(Material.ARROW,1), new ItemStack(Material.ARROW,1),new ItemStack(Material.ARROW,1)};

                ItemStack[] power = {new ItemStack(Material.AIR,0), new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.PAPER,1),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.BOW,1)};

                ItemStack[] efficiency = {new ItemStack(Material.AIR,0), new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.PAPER,1),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.IRON_PICKAXE,1)};

                ItemStack[] sharpness = {new ItemStack(Material.IRON_INGOT,1), new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.PAPER,1),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.IRON_SWORD,1)};

                ItemStack[] protection = {new ItemStack(Material.AIR,0), new ItemStack(Material.IRON_INGOT,1),new ItemStack(Material.AIR,0),
                        new ItemStack(Material.IRON_INGOT,1), new ItemStack(Material.PAPER,1),new ItemStack(Material.PAPER,1),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.IRON_INGOT,1)};

                ItemStack[] luck = {new ItemStack(Material.RABBIT_FOOT,1), new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.PAPER,1),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.FISHING_ROD,1)};

                ItemStack[] lure = {new ItemStack(Material.COD_BUCKET,1), new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.PAPER,1),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.FISHING_ROD,1)};

                ItemStack[] frost = {new ItemStack(Material.AIR,0), new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.PAPER,1),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.BLUE_ICE,1)};

                ItemStack[] depth = {new ItemStack(Material.AIR,0), new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.PAPER,1),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.NAUTILUS_SHELL,1)};

                ItemStack[] mending = {new ItemStack(Material.AIR,0), new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.PAPER,1),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.DIAMOND_BLOCK,1)};

                ItemStack[] fortune = {new ItemStack(Material.AIR,0), new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.PAPER,1),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.PAPER,1),new ItemStack(Material.GOLD_BLOCK,1)};

                ItemStack[] waterBreathing = {new ItemStack(Material.AIR,0), new ItemStack(Material.PUFFERFISH,1),new ItemStack(Material.AIR,0),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.GLASS_BOTTLE,1),new ItemStack(Material.AIR,0),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0)};

                ItemStack[] speed = {new ItemStack(Material.AIR,0), new ItemStack(Material.SUGAR,1),new ItemStack(Material.AIR,0),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.GLASS_BOTTLE,1),new ItemStack(Material.AIR,0),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0)};

                ItemStack[] fireResistance = {new ItemStack(Material.AIR,0), new ItemStack(Material.MAGMA_CREAM,1),new ItemStack(Material.AIR,0),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.GLASS_BOTTLE,1),new ItemStack(Material.AIR,0),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0)};

                ItemStack[] healing = {new ItemStack(Material.AIR,0), new ItemStack(Material.GLISTERING_MELON_SLICE,1),new ItemStack(Material.AIR,0),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.GLASS_BOTTLE,1),new ItemStack(Material.AIR,0),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0)};

                ItemStack[] strength = {new ItemStack(Material.AIR,0), new ItemStack(Material.BLAZE_POWDER,1),new ItemStack(Material.AIR,0),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.GLASS_BOTTLE,1),new ItemStack(Material.AIR,0),
                        new ItemStack(Material.AIR,0), new ItemStack(Material.AIR,0),new ItemStack(Material.AIR,0)};

                ItemStack[] recipe = {new ItemStack(Material.AIR,1), new ItemStack(Material.AIR,1),new ItemStack(Material.AIR,1),
                                      new ItemStack(Material.AIR,1), new ItemStack(Material.AIR,1),new ItemStack(Material.AIR,1),
                                      new ItemStack(Material.AIR,1), new ItemStack(Material.AIR,1),new ItemStack(Material.AIR,1)};


                ItemStack output = new ItemStack(Material.ENCHANTED_BOOK);
                switch (craftingLabel) {
                    case "archery1":
                        recipe = tippedArrow;
                        output.setType(Material.TIPPED_ARROW);
                        output.setAmount(8);
                        break;
                    case "farming1":
                        recipe = cowEgg;
                        output.setType(Material.COW_SPAWN_EGG);
                        break;
                    case "farming2":
                        recipe = beeEgg;
                        output.setType(Material.BEE_SPAWN_EGG);
                        break;
                    case "farming3":
                        recipe = mooshroomEgg1;
                        output.setType(Material.MOOSHROOM_SPAWN_EGG);
                        break;
                    case "farming4":
                        recipe = horseEgg;
                        output.setType(Material.HORSE_SPAWN_EGG);
                        break;
                    case "farming5":
                        recipe = slimeEgg;
                        output.setType(Material.SLIME_SPAWN_EGG);
                        break;
                    case "enchanting1":
                        recipe = power;
                        output.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
                        break;
                    case "enchanting2":
                        recipe = efficiency;
                        output.addUnsafeEnchantment(Enchantment.DIG_SPEED, 1);
                        break;
                    case "enchanting3":
                        recipe = sharpness;
                        output.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
                        break;
                    case "enchanting4":
                        recipe = protection;
                        output.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                        break;
                    case "enchanting5":
                        recipe = luck;
                        output.addUnsafeEnchantment(Enchantment.LUCK, 1);
                        break;
                    case "enchanting6":
                        recipe = lure;
                        output.addUnsafeEnchantment(Enchantment.LURE, 1);
                        break;
                    case "enchanting7":
                        recipe = frost;
                        output.addUnsafeEnchantment(Enchantment.FROST_WALKER, 1);
                        break;
                    case "enchanting8":
                        recipe = depth;
                        output.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, 1);
                        break;
                    case "enchanting9":
                        recipe = mending;
                        output.addUnsafeEnchantment(Enchantment.MENDING, 1);
                        break;
                    case "enchanting10":
                        recipe = fortune;
                        output.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 1);
                        break;
                    case "alchemy1":
                        recipe = waterBreathing;
                        output.setType(Material.POTION);
                        PotionMeta pm1 = (PotionMeta) output.getItemMeta();
                        pm1.setBasePotionData(new PotionData(PotionType.WATER_BREATHING,false,false));
                        output.setItemMeta(pm1);
                        break;
                    case "alchemy2":
                        recipe = speed;
                        output.setType(Material.POTION);
                        PotionMeta pm2 = (PotionMeta) output.getItemMeta();
                        pm2.setBasePotionData(new PotionData(PotionType.SPEED,false,false));
                        output.setItemMeta(pm2);
                        break;
                    case "alchemy3":
                        recipe = fireResistance;
                        output.setType(Material.POTION);
                        PotionMeta pm3 = (PotionMeta) output.getItemMeta();
                        pm3.setBasePotionData(new PotionData(PotionType.FIRE_RESISTANCE,false,false));
                        output.setItemMeta(pm3);
                        break;
                    case "alchemy4":
                        recipe = healing;
                        output.setType(Material.POTION);
                        PotionMeta pm4 = (PotionMeta) output.getItemMeta();
                        pm4.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL,false,false));
                        output.setItemMeta(pm4);
                        break;
                    case "alchemy5":
                        recipe = strength;
                        output.setType(Material.POTION);
                        PotionMeta pm5 = (PotionMeta) output.getItemMeta();
                        pm5.setBasePotionData(new PotionData(PotionType.STRENGTH,false,false));
                        output.setItemMeta(pm5);
                        break;
                    default:
                        break;
                }

                Inventory gui = Bukkit.createInventory(p, 54, "Crafting Recipe");
                //Back button
                ItemStack back = new ItemStack(Material.ARROW);
                ItemMeta backMeta = back.getItemMeta();
                backMeta.setDisplayName(ChatColor.BOLD + "Back");
                ArrayList<String> lore = new ArrayList<String>();
                lore.add(ChatColor.ITALIC+ChatColor.GRAY.toString()+"Takes you back to skill tree");
                backMeta.setLore(lore);
                back.setItemMeta(backMeta);
                gui.setItem(45,back);

                //Crafting Tables and Connector
                Integer[] indicies = {1,2,3,4,5,10,14,19,23,28,32,37,38,39,40,41};
                for (int i : indicies) {
                    gui.setItem(i,new ItemStack(Material.CRAFTING_TABLE));
                }
                gui.setItem(24,new ItemStack(Material.GLASS_PANE));

                //Inputs and Output
                Integer[] recipeIndices = {11,12,13,20,21,22,29,30,31};
                for (int i=0; i <9; i++) {
                    gui.setItem(recipeIndices[i],recipe[i]);
                }
                gui.setItem(25,output);


                //Put the items in the inventory
                p.openInventory(gui);
            }
        }
        else {
            System.out.println("You need to be a player to cast this command");
        }
        return true;
    }
}
