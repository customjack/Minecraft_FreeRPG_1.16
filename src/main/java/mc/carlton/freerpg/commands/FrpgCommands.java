package mc.carlton.freerpg.commands;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.PsuedoEnchanting;
import mc.carlton.freerpg.playerAndServerInfo.ChangeStats;
import mc.carlton.freerpg.playerAndServerInfo.HeldStats;
import mc.carlton.freerpg.playerAndServerInfo.PlayerLeaderboard;
import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.*;

public class FrpgCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);

        if (args.length == 0) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                Inventory gui = Bukkit.createInventory(p, 45, "Skills");

                //Menu Options(Items)
                ItemStack global = new ItemStack(Material.NETHER_STAR);
                ItemStack digging = new ItemStack(Material.IRON_SHOVEL);
                ItemStack woodcutting = new ItemStack(Material.IRON_AXE);
                ItemStack mining = new ItemStack(Material.IRON_PICKAXE);
                ItemStack farming = new ItemStack(Material.IRON_HOE);
                ItemStack fishing = new ItemStack(Material.FISHING_ROD);
                ItemStack archery = new ItemStack(Material.BOW);
                ItemStack beastMastery = new ItemStack(Material.BONE);
                ItemStack swords = new ItemStack(Material.IRON_SWORD);
                ItemStack armor = new ItemStack(Material.IRON_CHESTPLATE);
                ItemStack axes = new ItemStack(Material.GOLDEN_AXE);
                ItemStack repair = new ItemStack(Material.ANVIL);
                ItemStack agility = new ItemStack(Material.LEATHER_LEGGINGS);
                ItemStack alchemy = new ItemStack(Material.POTION);
                ItemStack smelting = new ItemStack(Material.COAL);
                ItemStack enchanting = new ItemStack(Material.ENCHANTING_TABLE);
                ItemStack tokens = new ItemStack(Material.GOLD_INGOT);
                ItemStack configuration = new ItemStack(Material.REDSTONE);

                ItemStack[] menu_items = {global,digging,woodcutting,mining,farming,fishing,archery,beastMastery,swords,armor,axes,repair,agility,alchemy,smelting,enchanting,tokens,configuration};
                String[] labels = {"Global","Digging","Woodcutting","Mining","Farming","Fishing","Archery","Beast Mastery","Swordsmanship","Defense","Axe Mastery","Repair","Agility","Alchemy","Smelting","Enchanting","Tokens","Configuration"};
                String[] labels0 = {"global","digging", "woodcutting", "mining", "farming", "fishing", "archery", "beastMastery", "swordsmanship", "defense", "axeMastery", "repair", "agility", "alchemy", "smelting", "enchanting"};
                Integer[] indices = {4,11,12,13,14,15,20,21,22,23,24,29,30,31,32,33,36,44};
                PlayerStats pStatClass = new PlayerStats(p);
                Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();

                //Set item positions and lore for all items in the GUI
                for (int i = 0; i < labels.length; i++) {
                    ItemStack item = menu_items[i];
                    ArrayList<String> lore_skills = new ArrayList<>();
                    if (i < 16 && i!=0) {
                        int passiveTokens = (int) pStat.get(labels0[i]).get(2);
                        int skillTokens = (int) pStat.get(labels0[i]).get(3);
                        if (passiveTokens > 0 || skillTokens > 0) {
                            item.addUnsafeEnchantment(Enchantment.DURABILITY,1);
                        }
                        int level = pStat.get(labels0[i]).get(0).intValue();
                        int EXP = pStat.get(labels0[i]).get(1).intValue();
                        lore_skills.add(ChatColor.GRAY + "Level: " + ChatColor.BLUE + String.valueOf(level));
                        lore_skills.add(ChatColor.GRAY + "Experience: " + ChatColor.BLUE + String.valueOf(EXP));
                        ChangeStats getEXP = new ChangeStats(p);
                        int nextEXP = getEXP.getEXPfromLevel(level+1);
                        int EXPtoNext = nextEXP - EXP;
                        lore_skills.add(ChatColor.GRAY + "EXP to next level: " + ChatColor.GREEN + String.valueOf(EXPtoNext));

                    }
                    else if (i==0) {
                        int globalTokens = (int) pStat.get(labels0[i]).get(1);
                        int totalLevel = pStat.get("global").get(0).intValue();
                        lore_skills.add(ChatColor.GRAY + "Total Level: " + ChatColor.GOLD + String.valueOf(totalLevel));
                        if (globalTokens > 0) {
                            item.addUnsafeEnchantment(Enchantment.DURABILITY,1);
                        }
                    }
                    ItemMeta meta = item.getItemMeta();
                    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                    meta.setDisplayName(ChatColor.AQUA + ChatColor.BOLD.toString() + labels[i]);
                    meta.setLore(lore_skills);


                    if (indices[i] == 36) {
                        int totTokens_S = 0;
                        int totTokens_P = 0;
                        int gTokens = 0;
                        for (String j : pStat.keySet()){

                            if (j.equalsIgnoreCase("global")){
                                gTokens = pStat.get(j).get(1).intValue();
                            }
                            else {
                                totTokens_P += pStat.get(j).get(2).intValue();
                                totTokens_S += pStat.get(j).get(3).intValue();
                            }
                        }
                        ArrayList<String> lore = new ArrayList<>();
                        lore.add(ChatColor.GRAY + "Global Tokens: " + ChatColor.GOLD + String.valueOf(gTokens));
                        lore.add(ChatColor.GRAY + "SKill Tokens: " + ChatColor.GOLD + String.valueOf(totTokens_S));
                        lore.add(ChatColor.GRAY + "Passive Tokens: " + ChatColor.GOLD + String.valueOf(totTokens_P));
                        meta.setLore(lore);
                    }
                    else if (indices[i] == 44) {
                        ArrayList<String> lore = new ArrayList<>();
                        lore.add(ChatColor.GRAY + "Click for options");
                        meta.setLore(lore);
                    }
                    item.setItemMeta(meta);
                    gui.setItem(indices[i], item);
                }

                //Put the items in the inventory
                p.openInventory(gui);
            }
            else {
                System.out.println("You need to be a player to cast this command");
            }
            return true;
        }

        //Help
        if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
            if (args.length >= 1) {
                int totalPages = 3;
                int page = 1;
                if (args.length == 2) {
                    page = Integer.valueOf(args[1]);
                }
                if (page > totalPages || page < 1) {
                    page = 1;
                }
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                        p.sendMessage(ChatColor.RED + "------| " + ChatColor.GREEN + ChatColor.BOLD.toString() + " Help" +
                                ChatColor.RESET + ChatColor.GREEN.toString() + " Page [" + Integer.toString(page) + "/" + Integer.toString(totalPages) + "]" +
                                ChatColor.RED.toString() + " |-----");
                        switch (page) {
                            case 1:
                                p.sendMessage(ChatColor.GOLD  + "/frpg" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + "Opens the main GUI with all skills");
                                p.sendMessage(ChatColor.GOLD  + "/frpg globalGUI" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + "Opens the global GUI");
                                p.sendMessage(ChatColor.GOLD  + "/frpg skillTreeGUI [skillName]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + "Opens a skill tree GUI of choice");
                                p.sendMessage(ChatColor.GOLD  + "/frpg configurationGUI" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + "Opens the configuration GUI");
                                p.sendMessage(ChatColor.GOLD  + "/frpg giveEXP [playerName] [skillName] [amount]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + "Gives a player of choice EXP in a specified skill");
                                p.sendMessage(ChatColor.GOLD  + "/frpg setLevel [playerName] [skillName] [level]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + "Sets a given player's level in a specified skill");
                                p.sendMessage(ChatColor.GOLD  + "/frpg statReset [playerName] [skillName]" + ChatColor.RESET + ChatColor.GRAY.toString() +" - " +
                                        ChatColor.RESET + ChatColor.WHITE + "Resets a player's stats in a specified skill (does not refund stats)");
                                p.sendMessage(ChatColor.GOLD  + "/frpg statLeaders [skillName] [(Optional) page #]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + "Displays a leaderboard for a specified stat");
                                break;
                            case 2:
                                p.sendMessage(ChatColor.GOLD  + "/frpg flintToggle [(Optional) \"ON\"/\"OFF\"]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + "Manually toggles the flint finder perk");
                                p.sendMessage(ChatColor.GOLD  + "/frpg speedToggle [(Optional) \"ON\"/\"OFF\"]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + "Manually toggles the gracious feet perk");
                                p.sendMessage(ChatColor.GOLD  + "/frpg potionToggle [(Optional) \"ON\"/\"OFF\"]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + "Manually toggles the potion master perk");
                                p.sendMessage(ChatColor.GOLD  + "/frpg flamePickToggle [(Optional) \"ON\"/\"OFF\"]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + "Manually toggles the flame pick perk");
                                p.sendMessage(ChatColor.GOLD  + "/frpg grappleToggle [(Optional) \"ON\"/\"OFF\"]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + "Manually toggles the grappling hook perk");
                                p.sendMessage(ChatColor.GOLD  + "/frpg hotRodToggle [(Optional) \"ON\"/\"OFF\"]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + "Manually toggles the hot rod perk");
                                p.sendMessage(ChatColor.GOLD  + "/frpg veinMinerToggle [(Optional) \"ON\"/\"OFF\"]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + "Manually toggles the vein miner perk");
                                p.sendMessage(ChatColor.GOLD  + "/frpg megaDigToggle [(Optional) \"ON\"/\"OFF\"]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + "Manually toggles the mega dig perk");
                                break;
                            case 3:
                                p.sendMessage(ChatColor.GOLD  + "/frpg enchantItem [level]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + "Attempts to enchant an item in the users hand at a specified level");
                                break;
                            default:
                                break;
                        }
                } else {
                    System.out.println("You must be a player to use this command");
                }
            } else {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    p.sendMessage(ChatColor.RED + "Improper Arguments, try /frpg help [(Optional) page]");
                } else {
                    System.out.println("Improper Arguments, try /frpg help [(Optional) page]");
                }
            }
        }

        //Enchant Item Command
        if (args[0].equalsIgnoreCase("enchantItem") || args[0].equalsIgnoreCase("enchant")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("freeRPG.enchantItem")) {
                    if (args.length == 2) {
                        int level = Integer.valueOf(args[1]);
                        if (level < 40) {
                            ItemStack item = p.getInventory().getItemInMainHand();
                            PsuedoEnchanting enchant = new PsuedoEnchanting();
                            item = enchant.enchantItem(item, level, false);
                            p.getInventory().setItemInMainHand(item);
                        } else {
                            p.sendMessage(ChatColor.RED + "Level argument must be less than 40");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "Improper Arguments, try /frpg enchantItem [level]");
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "You do not have permission to cast this command");
                }
            } else {
                System.out.println("You need to be a player to cast this command");
            }
        }

        //Leader Board
        else if (args[0].equalsIgnoreCase("leaderboard") || args[0].equalsIgnoreCase("statLeaders")) {
            if (args.length >= 2) {
                String skillName = args[1];
                int page = 1;
                if (args.length == 3) {
                    page = Integer.valueOf(args[2]);
                }
                String[] titles_0 = {"Digging", "Woodcutting", "Mining", "Farming", "Fishing", "Archery", "Beast Mastery", "Swordsmanship", "Defense", "Axe Mastery", "Repair", "Agility", "Alchemy", "Smelting", "Enchanting", "Global"};
                String[] labels_0 = {"digging", "woodcutting", "mining", "farming", "fishing", "archery", "beastMastery", "swordsmanship", "defense", "axeMastery", "repair", "agility", "alchemy", "smelting", "enchanting"};
                List<String> labels_arr = Arrays.asList(labels_0);
                if (labels_arr.contains(skillName)) {
                    String skillTitle = titles_0[labels_arr.indexOf(skillName)];
                    PlayerLeaderboard getStats = new PlayerLeaderboard();
                    ArrayList<HeldStats> sortedStats = getStats.getLeaders(skillName);
                    int totalPlayers = sortedStats.size();
                    int totalPages = (int) Math.ceil(totalPlayers / 10.0);
                    if (page > totalPages) {
                        page = 1;
                    }
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (p.hasPermission("freeRPG.leaderboard")) {
                            p.sendMessage(ChatColor.RED + "------| " + ChatColor.GREEN + ChatColor.BOLD.toString() + skillTitle + " Leaderboard" +
                                    ChatColor.RESET + ChatColor.GREEN.toString() + " Page [" + Integer.toString(page) + "/" + Integer.toString(totalPages) + "]" +
                                    ChatColor.RED.toString() + " |-----");
                            for (int i = 10 * (page - 1); i < (int) Math.min(10 * page, totalPlayers); i++) {
                                HeldStats info = sortedStats.get(i);
                                p.sendMessage(ChatColor.GREEN + Integer.toString(i + 1) + ". " + ChatColor.YELLOW + info.get_pName() + " (" + ChatColor.BLUE + Integer.toString(info.get_level()) + ChatColor.YELLOW + ")");
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
                        }
                    } else {
                        System.out.println("------| " + skillTitle + " Leaderboard" + " Page [" + Integer.toString(page) + "/" + Integer.toString(totalPages) + "]" + " |-----");
                        for (int i = 10 * (page - 1); i < (int) Math.min(10 * page, totalPlayers); i++) {
                            HeldStats info = sortedStats.get(i);
                            System.out.println(Integer.toString(i + 1) + ". " + info.get_pName() + " (" + Integer.toString(info.get_level()) + ")");
                        }
                    }
                } else {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (p.hasPermission("freeRPG.leaderboard")) {
                            p.sendMessage(ChatColor.RED + "Improper Arguments, try /frpg statLeaders [skillName] [(Optional) page]");
                        } else {
                            p.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
                        }
                    } else {
                        System.out.println("Improper Arguments, try /frpg statLeaders [skillName] [(Optional) page]");
                    }
                }
            } else {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (p.hasPermission("freeRPG.leaderboard")) {
                        p.sendMessage(ChatColor.RED + "Improper Arguments, try /frpg statLeaders [skillName] [(Optional) page]");
                    } else {
                        p.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
                    }
                } else {
                    System.out.println("Improper Arguments, try /frpg statLeaders [skillName] [(Optional) page]");
                }
            }
        }

        //GiveEXP
        else if (args[0].equalsIgnoreCase("giveEXP") || args[0].equalsIgnoreCase("expGive")) {
            if (args.length == 4) {
                String playerName = args[1];
                Player target = plugin.getServer().getPlayer(playerName);
                if (target == null) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        p.sendMessage(ChatColor.RED+"That player is not online");
                    }
                    else {
                        System.out.println("Player not online");
                    }
                }
                String skillName = args[2];
                int exp = Integer.valueOf(args[3]);
                String[] labels_0 = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting"};
                List<String> labels_arr = Arrays.asList(labels_0);
                if (labels_arr.contains(skillName) && target.isOnline()) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (p.hasPermission("freeRPG.giveEXP")) {
                            ChangeStats increaseStats = new ChangeStats(target);
                            increaseStats.set_isCommand(true);
                            if (exp < 0) {
                                p.sendMessage(ChatColor.RED + "Please only increase exp with this command, otherwise, use /frpg statReset then /frpg giveEXP");
                                return true;
                            }
                            increaseStats.changeEXP(skillName, exp);
                            increaseStats.setTotalLevel();
                        } else {
                            p.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
                        }
                    } else {
                        ChangeStats increaseStats = new ChangeStats(target);
                        increaseStats.set_isCommand(true);
                        if (exp < 0) {
                            System.out.println("Please only increase exp with this command, otherwise, use /frpg statReset then /frpg giveEXP");
                            return true;
                        }
                        increaseStats.changeEXP(skillName, exp);
                        increaseStats.setTotalLevel();
                    }
                }
                else {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (p.hasPermission("freeRPG.giveEXP")) {
                            p.sendMessage(ChatColor.RED + "Improper Arguments, try /frpg giveEXP [playerName] [skillName] [exp]");
                        } else {
                            p.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
                        }
                    }
                    else {
                        System.out.println("Improper Arguments, try /frpg giveEXP [playerName] [skillName] [exp]");
                    }
                }
            }
            else {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (p.hasPermission("freeRPG.giveEXP")) {
                        p.sendMessage(ChatColor.RED + "Improper Arguments, try /frpg giveEXP [playerName] [skillName] [exp]");
                    } else {
                        p.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
                    }
                }
                else {
                    System.out.println("Improper Arguments, try /frpg giveEXP [playerName] [skillName] [exp]");
                }
            }
        }

        //setLevel
        else if (args[0].equalsIgnoreCase("setLevel") || args[0].equalsIgnoreCase("levelSet")) {
            if (args.length == 4) {
                String playerName = args[1];
                Player target = plugin.getServer().getPlayer(playerName);
                if (target == null) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        p.sendMessage(ChatColor.RED+"That player is not online");
                    }
                    else {
                        System.out.println("Player not online");
                    }
                }
                String skillName = args[2];
                int level = Integer.valueOf(args[3]);
                String[] labels_0 = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting"};
                List<String> labels_arr = Arrays.asList(labels_0);
                if (labels_arr.contains(skillName) && target.isOnline()) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (p.hasPermission("freeRPG.setLevel")) {
                            ChangeStats increaseStats = new ChangeStats(target);
                            increaseStats.set_isCommand(true);
                            PlayerStats pStatClass = new PlayerStats(target);
                            Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
                            int exp = increaseStats.getEXPfromLevel(level);
                            int currentExp = (int) pStat.get(skillName).get(1);
                            if (exp <= currentExp) {
                                p.sendMessage(ChatColor.RED + "Please only increase levels with this command, otherwise, use /frpg statReset then /frpg setLevel");
                                return true;
                            }
                            increaseStats.changeEXP(skillName,exp-currentExp+1);
                            increaseStats.setTotalLevel();
                        } else {
                            p.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
                        }
                    } else {
                        ChangeStats increaseStats = new ChangeStats(target);
                        increaseStats.set_isCommand(true);
                        PlayerStats pStatClass = new PlayerStats(target);
                        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
                        int exp = increaseStats.getEXPfromLevel(level);
                        int currentExp = (int) pStat.get(skillName).get(1);
                        if (exp <= currentExp) {
                            System.out.println("Please only increase levels with this command, otherwise, use /frpg statReset then /frpg setLevel");
                            return true;
                        }
                        increaseStats.changeEXP(skillName,exp-currentExp+1);
                        increaseStats.setTotalLevel();
                    }
                }
                else {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (p.hasPermission("freeRPG.setLevel")) {
                            p.sendMessage(ChatColor.RED + "Improper Arguments, try /frpg setLevel [playerName] [skillName] [level]");
                        } else {
                            p.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
                        }
                    }
                    else {
                        System.out.println("Improper Arguments, try /frpg setLevel [playerName] [skillName] [level]");
                    }
                }
            }
            else {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (p.hasPermission("freeRPG.setLevel")) {
                        p.sendMessage(ChatColor.RED + "Improper Arguments, try /frpg setLevel [playerName] [skillName] [level]");
                    } else {
                        p.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
                    }
                }
                else {
                    System.out.println("Improper Arguments, try /frpg setLevel [playerName] [skillName] [level]");
                }
            }
        }
        //statReset
        else if (args[0].equalsIgnoreCase("statReset") || args[0].equalsIgnoreCase("resetStat")) {
            if (args.length == 3) {
                String playerName = args[1];
                Player target = plugin.getServer().getPlayer(playerName);
                if (target == null) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        p.sendMessage(ChatColor.RED+"That player is not online");
                    }
                    else {
                        System.out.println("Player not online");
                    }
                }
                String skillName = args[2];
                String[] labels_0 = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting"};
                List<String> labels_arr = Arrays.asList(labels_0);
                if (labels_arr.contains(skillName) && target.isOnline()) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (p.hasPermission("freeRPG.statReset")) {
                            ChangeStats increaseStats = new ChangeStats(target);
                            PlayerStats pStatClass = new PlayerStats(target);
                            Map<UUID,Map<String, ArrayList<Number>>> allStats = pStatClass.getData();
                            Map<String, ArrayList<Number>> pStats = allStats.get(target.getUniqueId());
                            ArrayList<Number> pSpecificStat = pStats.get(skillName);
                            for (int i=0; i < pSpecificStat.size() ;i++) {
                                pSpecificStat.set(i,0);
                            }
                            pStats.put(skillName,pSpecificStat);
                            allStats.put(target.getUniqueId(),pStats);
                            pStatClass.setData(allStats);
                            increaseStats.setTotalLevel();

                        } else {
                            p.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
                        }
                    } else {
                        ChangeStats increaseStats = new ChangeStats(target);
                        PlayerStats pStatClass = new PlayerStats(target);
                        Map<UUID,Map<String, ArrayList<Number>>> allStats = pStatClass.getData();
                        Map<String, ArrayList<Number>> pStats = allStats.get(target.getUniqueId());
                        ArrayList<Number> pSpecificStat = pStats.get(skillName);
                        for (int i=0; i < pSpecificStat.size() ;i++) {
                            pSpecificStat.set(i,0);
                        }
                        pStats.put(skillName,pSpecificStat);
                        allStats.put(target.getUniqueId(),pStats);
                        pStatClass.setData(allStats);
                        increaseStats.setTotalLevel();
                    }
                }
                else {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (p.hasPermission("freeRPG.statReset")) {
                            p.sendMessage(ChatColor.RED + "Improper Arguments, try /frpg statReset [playername] [statName]");
                        } else {
                            p.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
                        }
                    }
                    else {
                        System.out.println("Improper Arguments, try /frpg statReset [playername] [statName]");
                    }
                }
            }
            else {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (p.hasPermission("freeRPG.setLevel")) {
                        p.sendMessage(ChatColor.RED + "Improper Arguments, try /frpg statReset [playername] [statName]");
                    } else {
                        p.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
                    }
                }
                else {
                    System.out.println("Improper Arguments, try /frpg statReset [playername] [statName]");
                }
            }
        }
        //flamePickToggle
        else if (args[0].equalsIgnoreCase("toggleFlamePick") || args[0].equalsIgnoreCase("flamePickToggle")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                PlayerStats pStatClass = new PlayerStats(p);
                Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
                Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
                int flamePickLevel = (int) pStat.get("smelting").get(13);
                if (flamePickLevel > 0) {
                    if (args.length == 1) {
                        int flamePickToggle = (int) pStat.get("global").get(13);
                        if (flamePickToggle > 0) {
                            p.sendMessage(ChatColor.RED + "Flame Pickaxe status change to: OFF");
                            pStat.get("global").set(13,0);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                        }
                        else {
                            p.sendMessage(ChatColor.GREEN + "Flame Pickaxe status change to: ON");
                            pStat.get("global").set(13,1);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                        }
                    } else if (args.length == 2) {
                        if (args[1].equalsIgnoreCase("off")) {
                            p.sendMessage(ChatColor.RED + "Flame Pickaxe status change to: OFF");
                            pStat.get("global").set(13,0);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                        }
                        else if (args[1].equalsIgnoreCase("on")) {
                            p.sendMessage(ChatColor.GREEN + "Flame Pickaxe status change to: ON");
                            pStat.get("global").set(13,1);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                        }
                        else {
                            p.sendMessage(ChatColor.RED + "Invalid use, try /frpg flamePickToggle ON or /frpg flamePickToggle OFF");
                        }
                    }
                    else {
                        p.sendMessage(ChatColor.RED + "Invalid use, try /frpg flamePickToggle ON or /frpg flamePickToggle OFF");
                    }
                }
                else {
                    p.sendMessage(ChatColor.RED + "You need to unlock " +ChatColor.BOLD + "Flame Pickaxe" + ChatColor.RESET + ChatColor.RED.toString() + " to use this command");
                }
            } else {
                System.out.println("You need to be a player to cast this command");
            }
        }

        //flintToggle
        else if (args[0].equalsIgnoreCase("toggleFlint") || args[0].equalsIgnoreCase("flintToggle")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                PlayerStats pStatClass = new PlayerStats(p);
                Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
                Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
                int flintFinderLevel = (int) pStat.get("digging").get(11);
                if (flintFinderLevel > 0) {
                    if (args.length == 1) {
                        int flintFinderToggle = (int) pStat.get("global").get(12);
                        if (flintFinderToggle > 0) {
                            p.sendMessage(ChatColor.RED + "Flint Finder status change to: OFF");
                            pStat.get("global").set(12,0);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                        }
                        else {
                            p.sendMessage(ChatColor.GREEN + "Flint Finder status change to: ON");
                            pStat.get("global").set(12,1);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                        }
                    } else if (args.length == 2) {
                        if (args[1].equalsIgnoreCase("off")) {
                            p.sendMessage(ChatColor.RED + "Flint Finder status change to: OFF");
                            pStat.get("global").set(12,0);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                        }
                        else if (args[1].equalsIgnoreCase("on")) {
                            p.sendMessage(ChatColor.GREEN + "Flint Finder status change to: ON");
                            pStat.get("global").set(12,1);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                        }
                        else {
                            p.sendMessage(ChatColor.RED + "Invalid use, try /frpg flintToggle ON or /frpg flintToggle OFF");
                        }
                    }
                    else {
                        p.sendMessage(ChatColor.RED + "Invalid use, try /frpg flintToggle ON or /frpg flintToggle OFF");
                    }
                }
                else {
                    p.sendMessage(ChatColor.RED + "You need to unlock " +ChatColor.BOLD + "Flint Finder" + ChatColor.RESET + ChatColor.RED.toString() + " to use this command");
                }
            } else {
                System.out.println("You need to be a player to cast this command");
            }
        }

        //grappleToggle
        else if (args[0].equalsIgnoreCase("toggleGrapple") || args[0].equalsIgnoreCase("grappleToggle")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                PlayerStats pStatClass = new PlayerStats(p);
                Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
                Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
                int grappleLevel = (int) pStat.get("fishing").get(11);
                if (grappleLevel > 0) {
                    if (args.length == 1) {
                        int grappleToggle = (int) pStat.get("global").get(16);
                        if (grappleToggle > 0) {
                            p.sendMessage(ChatColor.RED + "Grappling Hook status change to: OFF");
                            pStat.get("global").set(16,0);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                        }
                        else {
                            p.sendMessage(ChatColor.GREEN + "Grappling Hook status change to: ON");
                            pStat.get("global").set(16,1);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                        }
                    } else if (args.length == 2) {
                        if (args[1].equalsIgnoreCase("off")) {
                            p.sendMessage(ChatColor.RED + "Grappling Hook status change to: OFF");
                            pStat.get("global").set(16,0);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                        }
                        else if (args[1].equalsIgnoreCase("on")) {
                            p.sendMessage(ChatColor.GREEN + "Grappling Hook status change to: ON");
                            pStat.get("global").set(16,1);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                        }
                        else {
                            p.sendMessage(ChatColor.RED + "Invalid use, try /frpg grappleToggle ON or /frpg grappleToggle OFF");
                        }
                    }
                    else {
                        p.sendMessage(ChatColor.RED + "Invalid use, try /frpg grappleToggle ON or /frpg grappleToggle OFF");
                    }
                }
                else {
                    p.sendMessage(ChatColor.RED + "You need to unlock " +ChatColor.BOLD + "Grappling Hook" + ChatColor.RESET + ChatColor.RED.toString() + " to use this command");
                }
            } else {
                System.out.println("You need to be a player to cast this command");
            }
        }

        //HotRodToggle
        else if (args[0].equalsIgnoreCase("toggleHotRod") || args[0].equalsIgnoreCase("hotRodToggle")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                PlayerStats pStatClass = new PlayerStats(p);
                Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
                Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
                int hotRodLevel = (int) pStat.get("fishing").get(12);
                if (hotRodLevel > 0) {
                    if (args.length == 1) {
                        int hotRodToggle = (int) pStat.get("global").get(17);
                        if (hotRodToggle > 0) {
                            p.sendMessage(ChatColor.RED + "Hot Rod status change to: OFF");
                            pStat.get("global").set(17,0);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                        }
                        else {
                            p.sendMessage(ChatColor.GREEN + "Hot Rod status change to: ON");
                            pStat.get("global").set(17,1);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                        }
                    } else if (args.length == 2) {
                        if (args[1].equalsIgnoreCase("off")) {
                            p.sendMessage(ChatColor.RED + "Hot Rod status change to: OFF");
                            pStat.get("global").set(17,0);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                        }
                        else if (args[1].equalsIgnoreCase("on")) {
                            p.sendMessage(ChatColor.GREEN + "Hot Rod status change to: ON");
                            pStat.get("global").set(17,1);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                        }
                        else {
                            p.sendMessage(ChatColor.RED + "Invalid use, try /frpg hotRodToggle ON or /frpg hotRodToggle OFF");
                        }
                    }
                    else {
                        p.sendMessage(ChatColor.RED + "Invalid use, try /frpg hotRodToggle ON or /frpg hotRodToggle OFF");
                    }
                }
                else {
                    p.sendMessage(ChatColor.RED + "You need to unlock " +ChatColor.BOLD + "Hot Rod" + ChatColor.RESET + ChatColor.RED.toString() + " to use this command");
                }
            } else {
                System.out.println("You need to be a player to cast this command");
            }
        }

        //MegaDigToggle
        else if (args[0].equalsIgnoreCase("toggleMegaDig") || args[0].equalsIgnoreCase("megaDigToggle")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                PlayerStats pStatClass = new PlayerStats(p);
                Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
                Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
                int megaDigLevel = (int) pStat.get("digging").get(13);
                if (megaDigLevel > 0) {
                    if (args.length == 1) {
                        int megaDigToggle = (int) pStat.get("global").get(19);
                        if (megaDigToggle > 0) {
                            p.sendMessage(ChatColor.RED + "Mega Dig status change to: OFF");
                            pStat.get("global").set(19,0);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                        }
                        else {
                            p.sendMessage(ChatColor.GREEN + "Mega Dig status change to: ON");
                            pStat.get("global").set(19,1);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                        }
                    } else if (args.length == 2) {
                        if (args[1].equalsIgnoreCase("off")) {
                            p.sendMessage(ChatColor.RED + "Mega Dig status change to: OFF");
                            pStat.get("global").set(19,0);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                        }
                        else if (args[1].equalsIgnoreCase("on")) {
                            p.sendMessage(ChatColor.GREEN + "Mega Dig status change to: ON");
                            pStat.get("global").set(19,1);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                        }
                        else {
                            p.sendMessage(ChatColor.RED + "Invalid use, try /frpg megaDigToggle ON or /frpg megaDigToggle OFF");
                        }
                    }
                    else {
                        p.sendMessage(ChatColor.RED + "Invalid use, try /frpg megaDigToggle ON or /frpg megaDigToggle OFF");
                    }
                }
                else {
                    p.sendMessage(ChatColor.RED + "You need to unlock " +ChatColor.BOLD + "Mega Dig" + ChatColor.RESET + ChatColor.RED.toString() + " to use this command");
                }
            } else {
                System.out.println("You need to be a player to cast this command");
            }
        }

        //PotionToggle
        else if (args[0].equalsIgnoreCase("togglePotion") || args[0].equalsIgnoreCase("potionToggle")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                PlayerStats pStatClass = new PlayerStats(p);
                Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
                Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
                int potionLevel = (int) pStat.get("fishing").get(12);
                if (potionLevel > 0) {
                    if (args.length == 1) {
                        int potionToggle = (int) pStat.get("global").get(5);
                        if (potionToggle > 0) {
                            p.sendMessage(ChatColor.RED + "Potion Master status change to: OFF");
                            pStat.get("global").set(5,0);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                        }
                        else {
                            p.sendMessage(ChatColor.GREEN + "Potion Master status change to: ON");
                            pStat.get("global").set(5,1);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                        }
                    } else if (args.length == 2) {
                        if (args[1].equalsIgnoreCase("off")) {
                            p.sendMessage(ChatColor.RED + "Potion Master status change to: OFF");
                            pStat.get("global").set(5,0);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                        }
                        else if (args[1].equalsIgnoreCase("on")) {
                            p.sendMessage(ChatColor.GREEN + "Potion Master status change to: ON");
                            pStat.get("global").set(5,1);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                        }
                        else {
                            p.sendMessage(ChatColor.RED + "Invalid use, try /frpg potionToggle ON or /frpg potionToggle OFF");
                        }
                    }
                    else {
                        p.sendMessage(ChatColor.RED + "Invalid use, try /frpg potionToggle ON or /frpg potionToggle OFF");
                    }
                }
                else {
                    p.sendMessage(ChatColor.RED + "You need to unlock " +ChatColor.BOLD + "Potion Master" + ChatColor.RESET + ChatColor.RED.toString() + " to use this command");
                }
            } else {
                System.out.println("You need to be a player to cast this command");
            }
        }

        //SpeedToggle
        else if (args[0].equalsIgnoreCase("toggleSpeed") || args[0].equalsIgnoreCase("speedToggle")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                PlayerStats pStatClass = new PlayerStats(p);
                Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
                Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
                int gracefulFeetLevel = (int) pStat.get("agility").get(13);
                if (gracefulFeetLevel > 0) {
                    if (args.length == 1) {
                        int gracefulFeetToggle = (int) pStat.get("global").get(14);
                        if (gracefulFeetToggle > 0) {
                            p.sendMessage(ChatColor.RED + "Graceful Feet status change to: OFF");
                            pStat.get("global").set(14,0);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                            if (p.getPotionEffect(PotionEffectType.SPEED) != null) {
                                if (p.getPotionEffect(PotionEffectType.SPEED).getAmplifier() < 1) {
                                    p.removePotionEffect(PotionEffectType.SPEED);
                                }
                            }
                        }
                        else {
                            p.sendMessage(ChatColor.GREEN + "Graceful Feet status change to: ON");
                            pStat.get("global").set(14,1);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                        }
                    } else if (args.length == 2) {
                        if (args[1].equalsIgnoreCase("off")) {
                            p.sendMessage(ChatColor.RED + "Graceful Feet status change to: OFF");
                            pStat.get("global").set(14,0);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                            if (p.getPotionEffect(PotionEffectType.SPEED) != null) {
                                if (p.getPotionEffect(PotionEffectType.SPEED).getAmplifier() < 1) {
                                    p.removePotionEffect(PotionEffectType.SPEED);
                                }
                            }
                        }
                        else if (args[1].equalsIgnoreCase("on")) {
                            p.sendMessage(ChatColor.GREEN + "Graceful Feet status change to: ON");
                            pStat.get("global").set(14,1);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                        }
                        else {
                            p.sendMessage(ChatColor.RED + "Invalid use, try /frpg speedToggle ON or /frpg speedToggle OFF");
                        }
                    }
                    else {
                        p.sendMessage(ChatColor.RED + "Invalid use, try /frpg speedToggle ON or /frpg speedToggle OFF");
                    }
                }
                else {
                    p.sendMessage(ChatColor.RED + "You need to unlock " +ChatColor.BOLD + "Graceful Feet" + ChatColor.RESET + ChatColor.RED.toString() + " to use this command");
                }
            } else {
                System.out.println("You need to be a player to cast this command");
            }
        }

        //VeinMinerToggle
        else if (args[0].equalsIgnoreCase("toggleVeinMiner") || args[0].equalsIgnoreCase("veinMinerToggle")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                PlayerStats pStatClass = new PlayerStats(p);
                Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
                Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
                int veinMinerLevel = (int)pStat.get("mining").get(11);
                if ( veinMinerLevel > 0) {
                    if (args.length == 1) {
                        int veinMinerToggle = (int) pStat.get("global").get(18);
                        if (veinMinerToggle > 0) {
                            p.sendMessage(ChatColor.RED + "Vein Miner status change to: OFF");
                            pStat.get("global").set(18,0);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                        }
                        else {
                            p.sendMessage(ChatColor.GREEN + "Vein Miner status change to: ON");
                            pStat.get("global").set(18,1);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                        }
                    } else if (args.length == 2) {
                        if (args[1].equalsIgnoreCase("off")) {
                            p.sendMessage(ChatColor.RED + "Vein Miner status change to: OFF");
                            pStat.get("global").set(18,0);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                        }
                        else if (args[1].equalsIgnoreCase("on")) {
                            p.sendMessage(ChatColor.GREEN + "Vein Miner status change to: ON");
                            pStat.get("global").set(18,1);
                            statAll.put(p.getUniqueId(), pStat);
                            pStatClass.setData(statAll);
                        }
                        else {
                            p.sendMessage(ChatColor.RED + "Invalid use, try /frpg hotRodToggle ON or /frpg hotRodToggle OFF");
                        }
                    }
                    else {
                        p.sendMessage(ChatColor.RED + "Invalid use, try /frpg hotRodToggle ON or /frpg hotRodToggle OFF");
                    }
                }
                else {
                    p.sendMessage(ChatColor.RED + "You need to unlock " +ChatColor.BOLD + "Vein Miner" + ChatColor.RESET + ChatColor.RED.toString() + " to use this command");
                }
            } else {
                System.out.println("You need to be a player to cast this command");
            }
        }

        //ConfigGUI
        else if (args[0].equalsIgnoreCase("configGUI") || args[0].equalsIgnoreCase("configurationGUI")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length != 1) {
                    p.sendMessage(ChatColor.RED + "Error: Try /frpg configurationGUI");
                }
                else {
                    Inventory gui = Bukkit.createInventory(p, 54, "Configuration Window");
                    PlayerStats pStatClass = new PlayerStats(p);
                    Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();

                    //Back button
                    ItemStack back = new ItemStack(Material.ARROW);
                    ItemMeta backMeta = back.getItemMeta();
                    backMeta.setDisplayName(ChatColor.BOLD + "Back");
                    ArrayList<String> lore = new ArrayList<String>();
                    lore.add(ChatColor.ITALIC+ChatColor.GRAY.toString()+"Takes you back to the skills menu");
                    backMeta.setLore(lore);
                    back.setItemMeta(backMeta);
                    gui.setItem(45,back);

                    //Level up Notifications
                    ItemStack levelUp = new ItemStack(Material.EXPERIENCE_BOTTLE);
                    ItemMeta levelUpMeta = levelUp.getItemMeta();
                    levelUpMeta.setDisplayName(ChatColor.WHITE + ChatColor.BOLD.toString() + "Level Up Notifications");
                    levelUp.setItemMeta(levelUpMeta);
                    gui.setItem(12,levelUp);

                    ItemStack levelUpToggle = new ItemStack(Material.LIME_DYE);
                    ItemMeta levelUpToggleMeta = levelUpToggle.getItemMeta();
                    if ( (int) pStat.get("global").get(21) > 0) {
                        levelUpToggleMeta.setDisplayName(ChatColor.BOLD + ChatColor.GREEN.toString() + "ON");
                    }
                    else {
                        levelUpToggle.setType(Material.GRAY_DYE);
                        levelUpToggleMeta.setDisplayName(ChatColor.BOLD + ChatColor.RED.toString() + "OFF");
                    }
                    levelUpToggle.setItemMeta(levelUpToggleMeta);
                    gui.setItem(21,levelUpToggle);

                    //Ability Notifications
                    ItemStack abilityNotifications = new ItemStack(Material.STICK);
                    ItemMeta abilityNotificationsMeta = abilityNotifications.getItemMeta();
                    abilityNotificationsMeta.setDisplayName(ChatColor.WHITE + ChatColor.BOLD.toString() + "Ability Preparation Notifications");
                    abilityNotifications.setItemMeta(abilityNotificationsMeta);
                    gui.setItem(14,abilityNotifications);

                    ItemStack abilityNotificationsToggle = new ItemStack(Material.LIME_DYE);
                    ItemMeta abilityNotificationsToggleMeta = abilityNotificationsToggle.getItemMeta();
                    if ( (int) pStat.get("global").get(22) > 0) {
                        abilityNotificationsToggleMeta.setDisplayName(ChatColor.BOLD + ChatColor.GREEN.toString() + "ON");
                    }
                    else {
                        abilityNotificationsToggle.setType(Material.GRAY_DYE);
                        abilityNotificationsToggleMeta.setDisplayName(ChatColor.BOLD + ChatColor.RED.toString() + "OFF");
                    }
                    abilityNotificationsToggle.setItemMeta(abilityNotificationsToggleMeta);
                    gui.setItem(23,abilityNotificationsToggle);



                    //Put the items in the inventory
                    p.openInventory(gui);
                }
            }
            else {
                System.out.println("You need to be a player to cast this command");
            }
        }

        //ConfirmationGUI
        else if (args[0].equalsIgnoreCase("confirmGUI") || args[0].equalsIgnoreCase("confirmationGUI")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                String[] labels_0 = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting","global"};
                String[] titles_0 = {"Digging","Woodcutting","Mining","Farming","Fishing","Archery","Beast Mastery","Swordsmanship","Defense","Axe Mastery","Repair","Agility","Alchemy","Smelting","Enchanting","Global"};
                List<String> labels_arr = Arrays.asList(labels_0);
                if (args.length != 2) {
                    p.sendMessage(ChatColor.RED + "Error: Try /frpg confirmationGUI [skillName]");
                }
                else if (labels_arr.indexOf(args[1]) == -1) {
                    p.sendMessage(ChatColor.RED+"Error: Try /frpg confirmationGUI [skillName]");
                }
                else {
                    String skillName = labels_0[labels_arr.indexOf(args[1])];
                    String skillTitle = titles_0[labels_arr.indexOf(args[1])];
                    Inventory gui = Bukkit.createInventory(p, 54, "Confirmation Window");
                    //Information
                    ItemStack info = new ItemStack(Material.PAPER);
                    ItemMeta infoMeta = info.getItemMeta();
                    infoMeta.setDisplayName(ChatColor.BOLD + ChatColor.YELLOW.toString() + "WARNING");
                    ArrayList<String> lore = new ArrayList<String>();
                    lore.add(ChatColor.ITALIC+ChatColor.GRAY.toString()+"Refunding a skill tree costs 1000 souls and");
                    lore.add(ChatColor.ITALIC+ChatColor.GRAY.toString()+"is not reversible, are you sure you want to");
                    lore.add(ChatColor.ITALIC+ChatColor.GRAY.toString()+"refund the " + ChatColor.BOLD.toString() + ChatColor.WHITE.toString() +
                            skillTitle + ChatColor.RESET.toString() + ChatColor.ITALIC.toString() + ChatColor.GRAY.toString()+" skill?");
                    infoMeta.setLore(lore);
                    info.setItemMeta(infoMeta);
                    gui.setItem(22,info);

                    //Yes Button
                    ItemStack yes = new ItemStack(Material.LIME_TERRACOTTA);
                    ItemMeta yesMeta = yes.getItemMeta();
                    yesMeta.setDisplayName(ChatColor.BOLD + ChatColor.GREEN.toString() + "YES");
                    yes.setItemMeta(yesMeta);
                    gui.setItem(39,yes);

                    //No Button
                    ItemStack no = new ItemStack(Material.RED_TERRACOTTA);
                    ItemMeta noMeta = no.getItemMeta();
                    noMeta.setDisplayName(ChatColor.BOLD + ChatColor.RED.toString() + "NO");
                    no.setItemMeta(noMeta);
                    gui.setItem(41,no);

                    //Skill Item (Indicator)
                    ItemStack skill = new ItemStack(Material.IRON_SHOVEL);
                    ItemMeta skillMeta = skill.getItemMeta();
                    switch (skillName) {
                        case "digging":
                            skill.setType(Material.IRON_SHOVEL);
                            break;
                        case "woodcutting":
                            skill.setType(Material.IRON_AXE);
                            break;
                        case "mining":
                            skill.setType(Material.IRON_PICKAXE);
                            break;
                        case "farming":
                            skill.setType(Material.IRON_HOE);
                            break;
                        case "fishing":
                            skill.setType(Material.FISHING_ROD);
                            break;
                        case "archery":
                            skill.setType(Material.BOW);
                            break;
                        case "beastMastery":
                            skill.setType(Material.BONE);
                            break;
                        case "swordsmanship":
                            skill.setType(Material.IRON_SWORD);
                            break;
                        case "defense":
                            skill.setType(Material.IRON_CHESTPLATE);
                            break;
                        case "axeMastery":
                            skill.setType(Material.GOLDEN_AXE);
                            break;
                        case "repair":
                            skill.setType(Material.ANVIL);
                            break;
                        case "agility":
                            skill.setType(Material.LEATHER_LEGGINGS);
                            break;
                        case "alchemy":
                            skill.setType(Material.POTION);
                            break;
                        case "smelting":
                            skill.setType(Material.COAL);
                            break;
                        case "enchanting":
                            skill.setType(Material.ENCHANTING_TABLE);
                            break;
                        default:
                            break;
                    }
                    skillMeta.setDisplayName(ChatColor.BOLD + skillTitle);
                    skill.setItemMeta(skillMeta);
                    gui.setItem(4,skill);

                    //Put the items in the inventory
                    p.openInventory(gui);
                }
            }
            else {
                System.out.println("You need to be a player to cast this command");
            }
        }

        //CraftingGUI
        else if (args[0].equalsIgnoreCase("craftingGUI") || args[0].equalsIgnoreCase("recipeGUI")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                String[] labels_0 = {"archery1", "farming1", "farming2", "farming3", "farming4", "farming5",
                        "enchanting1", "enchanting2", "enchanting3", "enchanting4", "enchanting5",
                        "enchanting6", "enchanting7", "enchanting8", "enchanting9", "enchanting10",
                        "alchemy1","alchemy2","alchemy3","alchemy4","alchemy5"};
                List<String> labels_arr = Arrays.asList(labels_0);
                if (args.length != 2) {
                    p.sendMessage(ChatColor.RED + "Error: Try /frpg craftingRecipe [skillName[Number]]");
                } else if (labels_arr.indexOf(args[1]) == -1) {
                    p.sendMessage(ChatColor.RED + "Error: Try /frpg craftingRecipe [skillName[Number]]");
                } else {
                    String craftingLabel = args[1];
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
        }

        //MainGUI
        else if (args[0].equalsIgnoreCase("mainGUI") || args[0].equalsIgnoreCase("skills")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                Inventory gui = Bukkit.createInventory(p, 45, "Skills");

                //Menu Options(Items)
                ItemStack global = new ItemStack(Material.NETHER_STAR);
                ItemStack digging = new ItemStack(Material.IRON_SHOVEL);
                ItemStack woodcutting = new ItemStack(Material.IRON_AXE);
                ItemStack mining = new ItemStack(Material.IRON_PICKAXE);
                ItemStack farming = new ItemStack(Material.IRON_HOE);
                ItemStack fishing = new ItemStack(Material.FISHING_ROD);
                ItemStack archery = new ItemStack(Material.BOW);
                ItemStack beastMastery = new ItemStack(Material.BONE);
                ItemStack swords = new ItemStack(Material.IRON_SWORD);
                ItemStack armor = new ItemStack(Material.IRON_CHESTPLATE);
                ItemStack axes = new ItemStack(Material.GOLDEN_AXE);
                ItemStack repair = new ItemStack(Material.ANVIL);
                ItemStack agility = new ItemStack(Material.LEATHER_LEGGINGS);
                ItemStack alchemy = new ItemStack(Material.POTION);
                ItemStack smelting = new ItemStack(Material.COAL);
                ItemStack enchanting = new ItemStack(Material.ENCHANTING_TABLE);
                ItemStack tokens = new ItemStack(Material.GOLD_INGOT);
                ItemStack configuration = new ItemStack(Material.REDSTONE);

                ItemStack[] menu_items = {global,digging,woodcutting,mining,farming,fishing,archery,beastMastery,swords,armor,axes,repair,agility,alchemy,smelting,enchanting,tokens,configuration};
                String[] labels = {"Global","Digging","Woodcutting","Mining","Farming","Fishing","Archery","Beast Mastery","Swordsmanship","Defense","Axe Mastery","Repair","Agility","Alchemy","Smelting","Enchanting","Tokens","Configuration"};
                String[] labels0 = {"global","digging", "woodcutting", "mining", "farming", "fishing", "archery", "beastMastery", "swordsmanship", "defense", "axeMastery", "repair", "agility", "alchemy", "smelting", "enchanting"};
                Integer[] indices = {4,11,12,13,14,15,20,21,22,23,24,29,30,31,32,33,36,44};
                PlayerStats pStatClass = new PlayerStats(p);
                Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();

                //Set item positions and lore for all items in the GUI
                for (int i = 0; i < labels.length; i++) {
                    ItemStack item = menu_items[i];
                    ArrayList<String> lore_skills = new ArrayList<>();
                    if (i < 16 && i!=0) {
                        int passiveTokens = (int) pStat.get(labels0[i]).get(2);
                        int skillTokens = (int) pStat.get(labels0[i]).get(3);
                        if (passiveTokens > 0 || skillTokens > 0) {
                            item.addUnsafeEnchantment(Enchantment.DURABILITY,1);
                        }
                        int level = pStat.get(labels0[i]).get(0).intValue();
                        int EXP = pStat.get(labels0[i]).get(1).intValue();
                        lore_skills.add(ChatColor.GRAY + "Level: " + ChatColor.BLUE + String.valueOf(level));
                        lore_skills.add(ChatColor.GRAY + "Experience: " + ChatColor.BLUE + String.valueOf(EXP));
                        ChangeStats getEXP = new ChangeStats(p);
                        int nextEXP = getEXP.getEXPfromLevel(level+1);
                        int EXPtoNext = nextEXP - EXP;
                        lore_skills.add(ChatColor.GRAY + "EXP to next level: " + ChatColor.GREEN + String.valueOf(EXPtoNext));

                    }
                    else if (i==0) {
                        int globalTokens = (int) pStat.get(labels0[i]).get(1);
                        int totalLevel = pStat.get("global").get(0).intValue();
                        lore_skills.add(ChatColor.GRAY + "Total Level: " + ChatColor.GOLD + String.valueOf(totalLevel));
                        if (globalTokens > 0) {
                            item.addUnsafeEnchantment(Enchantment.DURABILITY,1);
                        }
                    }
                    ItemMeta meta = item.getItemMeta();
                    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                    meta.setDisplayName(ChatColor.AQUA + ChatColor.BOLD.toString() + labels[i]);
                    meta.setLore(lore_skills);


                    if (indices[i] == 36) {
                        int totTokens_S = 0;
                        int totTokens_P = 0;
                        int gTokens = 0;
                        for (String j : pStat.keySet()){

                            if (j.equalsIgnoreCase("global")){
                                gTokens = pStat.get(j).get(1).intValue();
                            }
                            else {
                                totTokens_P += pStat.get(j).get(2).intValue();
                                totTokens_S += pStat.get(j).get(3).intValue();
                            }
                        }
                        ArrayList<String> lore = new ArrayList<>();
                        lore.add(ChatColor.GRAY + "Global Tokens: " + ChatColor.GOLD + String.valueOf(gTokens));
                        lore.add(ChatColor.GRAY + "SKill Tokens: " + ChatColor.GOLD + String.valueOf(totTokens_S));
                        lore.add(ChatColor.GRAY + "Passive Tokens: " + ChatColor.GOLD + String.valueOf(totTokens_P));
                        meta.setLore(lore);
                    }
                    else if (indices[i] == 44) {
                        ArrayList<String> lore = new ArrayList<>();
                        lore.add(ChatColor.GRAY + "Click for options");
                        meta.setLore(lore);
                    }
                    item.setItemMeta(meta);
                    gui.setItem(indices[i], item);
                }

                //Put the items in the inventory
                p.openInventory(gui);
            }
            else {
                System.out.println("You need to be a player to cast this command");
            }
        }

        //SkillTreeGUI
        else if (args[0].equalsIgnoreCase("skillTree") || args[0].equalsIgnoreCase("skillTreeGUI")) {
            Map<String, String[]> perksMap = new HashMap<>();
            Map<String, String[]> descriptionsMap = new HashMap<>();
            Map<String, String[]> passivePerksMap = new HashMap<>();
            Map<String, String[]> passiveDescriptionsMap = new HashMap<>();

            String s_Name = "";
            String[] perks = {};
            String[] descriptions = {};
            String[] passivePerks = {};
            String[] passiveDescriptions = {};

            //digging
            s_Name = "digging";
            perks = new String[]{"Mo' drops", "Double Treasure", "Rarer Drops", "Soul Stealer", "Flint Finder", "Shovel Knight", "Mega Dig"};
            descriptions = new String[]{"Expands treasure drop table by 1 item per level","+5% chance of receiving double treasure drop per level (when treasure is rolled)","Further expands drop table by item per level","Soul sand is +5% more likely to drop treasure per level","Gravel has 100% flint drop rate (toggleable  by /flintToggle)","Shovels do double damage","When using ability, you now break a 3x3 block section (Treasure rate is halved)"};
            passivePerks = new String[]{"Passive Tokens","Back","Skill Tokens","Big Dig Duration","Treasure Chance"};
            passiveDescriptions = new String[]{"Tokens to invest in passive skills (dyes)","Takes you back to the main skills menu","Tokens to invest in skill tree","Increases duration of Big Dig by 0.02 s","Increases chance of digging up treasure by 0.005%"};
            perksMap.put(s_Name,perks);
            descriptionsMap.put(s_Name,descriptions);
            passivePerksMap.put(s_Name,passivePerks);
            passiveDescriptionsMap.put(s_Name,passiveDescriptions);

            //woodcutting
            s_Name = "woodcutting";
            perks = new String[]{"Zealous Roots", "Fresh Arms", "Hidden Knowledge", "Leaf Scavenger", "Timber+", "Leaf Blower", "Able Axe"};
            descriptions = new String[]{"+20% chance for logs to drop 1 XP per level","+12 s per level of Haste I after first log broken in 5 minutes","Logs have a +0.2% chance per level to drop an enchanted book","Leaves have a 1% chance to drop +1 treasure item per level","Timber break limit increased from 64 to 128","Instantly break leaves by holding left click with an axe","Double drops, Zealous Roots, and Hidden Knowledge all now apply to timber (at half effectiveness)"};
            passivePerks = new String[]{"Passive Tokens","Back","Skill Tokens","Timber Duration","Double Drops"};
            passiveDescriptions = new String[]{"Tokens to invest in passive skills (dyes)","Takes you back to the main skills menu","Tokens to invest in skill tree","Increases duration of Timber by 0.02 s","Increases chance to receive a double drop by 0.05%"};
            perksMap.put(s_Name,perks);
            descriptionsMap.put(s_Name,descriptions);
            passivePerksMap.put(s_Name,passivePerks);
            passiveDescriptionsMap.put(s_Name,passiveDescriptions);

            //mining
            s_Name = "mining";
            perks = new String[]{"Wasteless Haste", "More Bombs", "Treasure Seeker", "Bomb-boyage", "Vein Miner", "Demolition Man", "Triple Trouble"};
            descriptions = new String[]{"Gain haste after mining ores for each level","The crafting recipe for TNT produces +1 TNT block per level","When using ability on stones, +1% per level chance for an ore to drop (extra exp is earned from dropped ores)","Increases TNT blast radius (when lit by flint and steel) each level","Ore veins are instantly mined upon breaking one block (toggle-able)","No damage is taken from TNT explosions","Double drops are now triple drops"};
            passivePerks = new String[]{"Passive Tokens","Back","Skill Tokens","Berserk Pick Duration","Double Drops","Blast Mining"};
            passiveDescriptions = new String[]{"Tokens to invest in passive skills (dyes)","Takes you back to the main skills menu","Tokens to invest in skill tree","Increases duration of Berserk Pick by 0.02s","Increases chance to receive a double drop from ores by 0.05%","Increases chances for ore to be created from TNT explosions by 0.01% (rolled 10 times per explosion)"};
            perksMap.put(s_Name,perks);
            descriptionsMap.put(s_Name,descriptions);
            passivePerksMap.put(s_Name,passivePerks);
            passiveDescriptionsMap.put(s_Name,passiveDescriptions);

            //farming
            s_Name = "farming";
            perks = new String[]{"Better Fertilizer", "Animal Farm", "Farmer's Diet", "Carnivore", "Green Thumb", "Growth Hormones", "One with Nature"};
            descriptions = new String[]{"+10% chance to not consume bonemeal on use","Can craft an additional spawn egg per level","Farm food is +20% more effective at restoring hunger and saturation per level","Meat is +20% more effective at restoring hunger and saturation per level","Ability may replant crops fully grown with higher chance of replanting in later growth stages; ability now effects Melons and Pumpkins","Sugar can be used on baby animals to make them grow instantly","Gain Regeneration I when standing still on grass"};
            passivePerks = new String[]{"Passive Tokens","Back","Skill Tokens","Natural Regeneration Duration","Double Drops (Crops)","Double Drops (Animals)"};
            passiveDescriptions = new String[]{"Tokens to invest in passive skills (dyes)","Takes you back to the main skills menu","Tokens to invest in skill tree","Increases duration of Natural Regeneration by 0.02s","Increases chance to receive a double drop from crops by 0.05%","Increases chance to receive a double drop from most passive animals by 0.05%"};
            perksMap.put(s_Name,perks);
            descriptionsMap.put(s_Name,descriptions);
            passivePerksMap.put(s_Name,passivePerks);
            passiveDescriptionsMap.put(s_Name,passiveDescriptions);

            //fishing
            s_Name = "fishing";
            perks = new String[]{"Rob", "Scavenger", "Fisherman's Diet", "Filtration", "Grappling Hook", "Hot Rod", "Fish Person"};
            descriptions = new String[]{"+15% chance to pull item off a mob per level","Unlocks new tier of fishing treasure","Fish restore +20% hunger per level","Higher tier (II-V) loot becomes more common, lower tier (I) becomes less common","Fishing rod now acts as a grappling hook (toggleable with /grappleToggle )","Fish are now cooked when caught, some fishing treasures are changed (toggleable with /hotRodToggle)","Infinite night vision when underwater, infinite dolphin's grace when in the water"};
            passivePerks = new String[]{"Passive Tokens","Back","Skill Tokens","Super Bait Duration","Double catches","Treasure Finder"};
            passiveDescriptions = new String[]{"Tokens to invest in passive skills (dyes)","Takes you back to the main skills menu","Tokens to invest in skill tree","Increases duration of Super Bait by 0.01s","Increases chance to receive a double drop by 0.05%","Decreases chance of finding junk by 0.005%, increases chance of finding treasure by 0.005%"};
            perksMap.put(s_Name,perks);
            descriptionsMap.put(s_Name,descriptions);
            passivePerksMap.put(s_Name,passivePerks);
            passiveDescriptionsMap.put(s_Name,passiveDescriptions);

            //archery
            s_Name = "archery";
            perks = new String[]{"Extra Arrows", "Sniper", "Arrow of Light", "Explosive Arrows", "Dragon-less Arrows", "Crossbow Rapid Load", "Deadly Strike"};
            descriptions = new String[]{"+1 arrow gained from crafting per level","Arrow speed increases by +2% per level (~4% damage increase/level)","Spectral arrows get a +5% damage boost per level","Arrows have a +1% of creating an explosion on hit","Allows crafting all tipped arrows with regular potions instead of lingering potions","Ability can now be used with crossbows, making all shots load instantly","Fireworks shot from crossbows do double damage (up to 16 hearts of damage)"};
            passivePerks = new String[]{"Passive Tokens","Back","Skill Tokens","Rapid Fire Duration","Retrieval"};
            passiveDescriptions = new String[]{"Tokens to invest in passive skills (dyes)","Takes you back to the main skills menu","Tokens to invest in skill tree","Increases duration of Rapid Fire by 0.02s","Increases chance for arrow shot to not consume arrow by 0.05% per level"};
            perksMap.put(s_Name,perks);
            descriptionsMap.put(s_Name,descriptions);
            passivePerksMap.put(s_Name,passivePerks);
            passiveDescriptionsMap.put(s_Name,passiveDescriptions);

            //beastMastery
            s_Name = "beastMastery";
            perks = new String[]{"Thick Fur","Sharp Teeth","Healthy Bites","Keep Away","Graceful Feet","Identify","Adrenaline Boost"};
            descriptions = new String[]{"Dogs take -10% damage per level","Dogs do +10% more damage per level","Dogs heal +1/2 heart per level from killing","Dogs have gain +5% chance of knocking back foes","Dogs do not take fall damage","Using a compass on a horse or wolf now shows their stats","Spur kick buff is now speed III"};
            passivePerks = new String[]{"Passive Tokens","Back","Skill Tokens","Spur Kick Duration","Critical Bite"};
            passiveDescriptions = new String[]{"Tokens to invest in passive skills (dyes)","Takes you back to the main skills menu","Tokens to invest in skill tree","Increases duration of Spur Kick by 0.02s","Increases chance for a dog to have a critical hit by 0.025%"};
            perksMap.put(s_Name,perks);
            descriptionsMap.put(s_Name,descriptions);
            passivePerksMap.put(s_Name,passivePerks);
            passiveDescriptionsMap.put(s_Name,passiveDescriptions);

            //swordsmanship
            s_Name = "swordsmanship";
            perks = new String[]{"Adrenaline","Killing Spree","Adrenaline+","Killing Frenzy","Thirst for Blood","Sharper!","Sword Mastery"};
            descriptions = new String[]{"Killing hostile mobs with a sword provides +2 s of speed per level","Killing hostile mobs with a sword provides +2 s of strength per level","+20% of speed I buff from Adrenaline is now speed II","+20% of strength I buff from Killing Spree is now strength II","Killing certain aggressive mobs with a sword restores hunger","Swift strikes now adds a level of sharpness to your sword","Swords permanently do +1 heart of damage"};
            passivePerks = new String[]{"Passive Tokens","Back","Skill Tokens","Swift Strikes Duration","Double Hit"};
            passiveDescriptions = new String[]{"Tokens to invest in passive skills (dyes)","Takes you back to the main skills menu","Tokens to invest in skill tree","Increases duration of Swift Strikes by 0.02s","Increases chance to hit mob twice (second hit does 50% damage) by 0.02%"};
            perksMap.put(s_Name,perks);
            descriptionsMap.put(s_Name,descriptions);
            passivePerksMap.put(s_Name,passivePerks);
            passiveDescriptionsMap.put(s_Name,passiveDescriptions);

            //defense
            s_Name = "defense";
            perks = new String[]{"Healer","Stiffen","Hard Headed","Stiffen+","Gift From Above","Stronger Legs","Hearty"};
            descriptions = new String[]{"Gain +3s of regen per level on kill","+2% chance to gain resistance I for 5s when hit","Hard Body decreases damage by an additional 6.6% per level","+2% chance to gain resistance II for 5s when hit","Stone Solid now grants 4 absorption hearts for ability length +1 minute","Stone Solid now gives slowness I instead of slowness IV","+2 hearts permanently"};
            passivePerks = new String[]{"Passive Tokens","Back","Skill Tokens","Stone Solid Duration","Hard Body","Double Drops (Hostile Mobs)"};
            passiveDescriptions = new String[]{"Tokens to invest in passive skills (dyes)","Takes you back to the main skills menu","Tokens to invest in skill tree","Increases duration of Stone Solid by 0.02s","Increases chance to take reduced (base -33%) damage by 0.01% per level","Increases chance to receive double drops from aggressive mobs by 0.05%"};
            perksMap.put(s_Name,perks);
            descriptionsMap.put(s_Name,descriptions);
            passivePerksMap.put(s_Name,passivePerks);
            passiveDescriptionsMap.put(s_Name,passiveDescriptions);

            //axeMastery
            s_Name = "axeMastery";
            perks = new String[]{"Greater Axe","Holy Axe","Revitalized","Warrior Blood","Earthquake","Better Crits","Axe Man"};
            descriptions = new String[]{"Great Axe damage radius increases by 1 block per level","+2% chance for lighting to strike mobs on axe hit","+1% chance for full heal on kill per level","+3 s per level of Strength I on kills with an axe","Ability's AOE damage is doubled (25% -> 50% of damage)","Divine Crits now have 1.6x multiplier instead of 1.25x","Axes permanently do +1 heart of damage"};
            passivePerks = new String[]{"Passive Tokens","Back","Skill Tokens","Great Axe Duration","Divine Crits"};
            passiveDescriptions = new String[]{"Tokens to invest in passive skills (dyes)","Takes you back to the main skills menu","Tokens to invest in skill tree","Increases duration of Great Axe by 0.02s","Increases random crit chance (base 1.25x damage) by 0.01%"};
            perksMap.put(s_Name,perks);
            descriptionsMap.put(s_Name,descriptions);
            passivePerksMap.put(s_Name,passivePerks);
            passiveDescriptionsMap.put(s_Name,passiveDescriptions);

            //Repair
            s_Name = "repair";
            perks = new String[]{"Salvaging","Resourceful","Magic Repair Mastery"};
            descriptions = new String[]{"Get more materials on average from salvaging","+10% chance of keeping material used when repairing","Guarenteed to keep enchants on repair"};
            passivePerks = new String[]{"Back","Skill Tokens","Proficiency"};
            passiveDescriptions = new String[]{"Takes you back to the main skills menu","Tokens to invest in skill tree","Materials restore more durability on repair"};
            perksMap.put(s_Name,perks);
            descriptionsMap.put(s_Name,descriptions);
            passivePerksMap.put(s_Name,passivePerks);
            passiveDescriptionsMap.put(s_Name,passiveDescriptions);

            //Agility
            s_Name = "agility";
            perks = new String[]{"Dodge","Steel Bones","Graceful Feet"};
            descriptions = new String[]{"+4% chance to dodge attacks per level","-10% fall damage per level","Permanent speed I buff (toggleable by /speedToggle)"};
            passivePerks = new String[]{"Back","Skill Tokens","Roll"};
            passiveDescriptions = new String[]{"Takes you back to the main skills menu","Tokens to invest in skill tree","Chance to roll and take reduced fall damage"};
            perksMap.put(s_Name,perks);
            descriptionsMap.put(s_Name,descriptions);
            passivePerksMap.put(s_Name,passivePerks);
            passiveDescriptionsMap.put(s_Name,passiveDescriptions);

            //Alchemy
            s_Name = "alchemy";
            perks = new String[]{"Alchemical Summoning","Ancient Knowledge","Potion Master"};
            descriptions = new String[]{"Allows crafting of some potions without a brewing stand","Unlocks ability to brew new potions","All used potions are increased in level by 1 (toggleable with /togglePotion)"};
            passivePerks = new String[]{"Back","Skill Tokens","Half-life+"};
            passiveDescriptions = new String[]{"Takes you back to the main skills menu","Tokens to invest in skill tree","Increase in duration of potions when used"};
            perksMap.put(s_Name,perks);
            descriptionsMap.put(s_Name,descriptions);
            passivePerksMap.put(s_Name,passivePerks);
            passiveDescriptionsMap.put(s_Name,passiveDescriptions);

            //Smelting
            s_Name = "smelting";
            perks = new String[]{"Fuel Efficiency","Double Smelt","Flame Pickaxe"};
            descriptions = new String[]{"Fuel last +20% longer per level","+5% chance for smelted ore to be doubled per level","Mined ores are instantly smelted (toggleable with /toggleFlamePick)"};
            passivePerks = new String[]{"Back","Skill Tokens","Fuel Speed"};
            passiveDescriptions = new String[]{"Takes you back to the main skills menu","Tokens to invest in skill tree","Increasing cooking speed"};
            perksMap.put(s_Name,perks);
            descriptionsMap.put(s_Name,descriptions);
            passivePerksMap.put(s_Name,passivePerks);
            passiveDescriptionsMap.put(s_Name,passiveDescriptions);

            //Enchanting
            s_Name = "enchanting";
            perks = new String[]{"Efficient Enchanting","Booksmart","Immortal Experience"};
            descriptions = new String[]{"Levels needed to enchant -1 per level, anvil repair costs -1 (minimum of 2) XP levels per level","Unlocks crafting recipes for some enchanted books","Keep xp on death"};
            passivePerks = new String[]{"Back","Skill Tokens","Quicker Development"};
            passiveDescriptions = new String[]{"Takes you back to the main skills menu","Tokens to invest in skill tree","All xp received increased"};
            perksMap.put(s_Name,perks);
            descriptionsMap.put(s_Name,descriptions);
            passivePerksMap.put(s_Name,passivePerks);
            passiveDescriptionsMap.put(s_Name,passiveDescriptions);

            //Enchanting
            s_Name = "global";
            perks = new String[]{"Gatherer","Scholar","Fighter","Hard Work","Research","Training","Reincarnation+","Soul Harvesting","Avatar","Master of the Arts"};
            descriptions = new String[]{"+20% exp gained in Digging, Woodcutting, Mining, Farming, and Fishing","+20% exp gained in Repair, Agility, Brewing, Smelting, and Enchanting","+20% exp gained in Archery, Beast Mastery, Swordsmanship, Defense, and Axe Mastery","+1 skill token in all Gatherer skills","+1 skill token in all Scholar skills","+1 skill token in all Fighter skills","On death, 50% chance to keep some of each valuable item in your inventory","You now harvest souls from killing aggressive mobs, which can be used to refund skill trees","10% chance to take no damage and gain all in-game buffs for 10s on a hit that would normallu kill you","Ability cooldowns decreased by 33%"};
            passivePerks = new String[]{"Global Tokens","Back"};
            passiveDescriptions = new String[]{"Tokens to invest in skill tree","Takes you back to the main skills menu"};
            perksMap.put(s_Name,perks);
            descriptionsMap.put(s_Name,descriptions);
            passivePerksMap.put(s_Name,passivePerks);
            passiveDescriptionsMap.put(s_Name,passiveDescriptions);

            String[] titles_0 = {"Digging","Woodcutting","Mining","Farming","Fishing","Archery","Beast Mastery","Swordsmanship","Defense","Axe Mastery","Repair","Agility","Alchemy","Smelting","Enchanting","Global"};
            String[] labels_0 = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting","global"};
            List<String> labels_arr = Arrays.asList(labels_0);
            if (sender instanceof Player && args.length!= 2){
                Player p = (Player) sender;
                p.sendMessage(ChatColor.RED+"Error: Try /frpg skillTree [skillName]");
            }
            else if (sender instanceof Player && labels_arr.indexOf(args[1]) == -1) {
                Player p = (Player) sender;
                p.sendMessage(ChatColor.RED+"Error: Try /frpg skillTree [skillName]");
            }
            else if (sender instanceof Player && labels_arr.indexOf(args[1]) < 10) {
                Player p = (Player) sender;
                String skillName = labels_0[labels_arr.indexOf(args[1])];
                String skillTitle = titles_0[labels_arr.indexOf(args[1])];
                Inventory gui = Bukkit.createInventory(p, 54,skillTitle);

                //Skills
                PlayerStats pStatClass = new PlayerStats(p);
                Map<String, ArrayList<Number>> pStatAll = pStatClass.getPlayerData();
                ArrayList<Number> pStats = pStatAll.get(skillName);
                int tokens_P = (Integer) pStats.get(2);
                int tokens_S = (Integer) pStats.get(3);
                Number passive1 = pStats.get(4);
                Number passive2 = pStats.get(5);
                Number passive3 = pStats.get(6);
                int skill_1a_level =  (Integer) pStats.get(7);
                int skill_1b_level =  (Integer) pStats.get(8);
                int skill_2a_level =  (Integer) pStats.get(9);
                int skill_2b_level =  (Integer) pStats.get(10);
                int skill_3a_level =  (Integer) pStats.get(11);
                int skill_3b_level =  (Integer) pStats.get(12);
                int skill_M_level =  (Integer) pStats.get(13);

                ItemStack skill_1a = new ItemStack(Material.PINK_TERRACOTTA);
                ItemStack skill_2a = new ItemStack(Material.RED_TERRACOTTA);
                ItemStack skill_3a = new ItemStack(Material.RED_TERRACOTTA);
                ItemStack skill_1b = new ItemStack(Material.PINK_TERRACOTTA);
                ItemStack skill_2b = new ItemStack(Material.RED_TERRACOTTA);
                ItemStack skill_3b = new ItemStack(Material.RED_TERRACOTTA);
                ItemStack skill_M = new ItemStack(Material.RED_TERRACOTTA);


                if (skill_1a_level == 0) {
                    skill_1a.setType(Material.PINK_TERRACOTTA);
                }
                else if (skill_1a_level > 0 && skill_1a_level < 5) {
                    skill_1a.setType(Material.YELLOW_TERRACOTTA);
                }
                else {
                    skill_1a.setType(Material.GREEN_TERRACOTTA);
                }


                if (skill_1b_level == 0) {
                    skill_1b.setType(Material.PINK_TERRACOTTA);
                }
                else if (skill_1b_level > 0 && skill_1b_level < 5) {
                    skill_1b.setType(Material.YELLOW_TERRACOTTA);
                }
                else {
                    skill_1b.setType(Material.GREEN_TERRACOTTA);
                }


                if (skill_2a_level == 0) {
                    if (skill_1a_level >= 2) {
                        skill_2a.setType(Material.PINK_TERRACOTTA);
                    }
                }
                else if (skill_2a_level > 0 && skill_2a_level < 5) {
                    skill_2a.setType(Material.YELLOW_TERRACOTTA);
                }
                else {
                    skill_2a.setType(Material.GREEN_TERRACOTTA);
                }


                if (skill_2b_level == 0) {
                    if (skill_1b_level >= 2) {
                        skill_2b.setType(Material.PINK_TERRACOTTA);
                    }
                }
                else if (skill_2b_level > 0 && skill_2b_level < 5) {
                    skill_2b.setType(Material.YELLOW_TERRACOTTA);
                }
                else {
                    skill_2b.setType(Material.GREEN_TERRACOTTA);
                }


                if (skill_3a_level == 0) {
                    if (skill_2a_level >= 2) {
                        skill_3a.setType(Material.PINK_TERRACOTTA);
                    }
                }
                else {
                    skill_3a.setType(Material.GREEN_TERRACOTTA);
                }


                if (skill_3b_level == 0) {
                    if (skill_2b_level >= 2) {
                        skill_3b.setType(Material.PINK_TERRACOTTA);
                    }
                }
                else {
                    skill_3b.setType(Material.GREEN_TERRACOTTA);
                }

                if (skill_M_level == 0) {
                    if (skill_1a_level+skill_1b_level+skill_2a_level+skill_2b_level+skill_3a_level+skill_3b_level >=10) {
                        skill_M.setType(Material.PINK_TERRACOTTA);
                    }
                }
                else {
                    skill_M.setType(Material.GREEN_TERRACOTTA);
                }



                ItemStack[] menu_items = {skill_1a,skill_1b,skill_2a,skill_2b,skill_3a,skill_3b,skill_M};
                String[] labels = perksMap.get(skillName);
                String[] lores_line2 = descriptionsMap.get(skillName);
                String desc = "";
                int special_index = 0;
                switch (skillName){
                    case "mining":
                        special_index = 0;
                        switch (skill_1a_level) {
                            case 0:
                                desc = "2 seconds of Haste I after mining any ore";
                                lores_line2[special_index] = desc;
                                break;
                            case 1:
                                desc = "5 seconds of Haste I after mining any ore";
                                lores_line2[special_index] = desc;
                                break;
                            case 2:
                                desc = "10 seconds of Haste I after mining any ore";
                                lores_line2[special_index] = desc;
                                break;
                            case 3:
                                desc = "5 seconds of Haste II after mining any ore, followed by 5 seconds of Haste I";
                                lores_line2[special_index] = desc;
                                break;
                            case 4:
                                desc = "10 seconds of Haste II after mining any ore";
                                lores_line2[special_index] = desc;
                                break;
                            default:
                                break;
                        }
                        break;
                    case "woodcutting":
                        special_index = 3;
                        desc = "Leaves may drop ";
                        switch (skill_2b_level) {
                            case 0:
                                desc += "feathers when cut by player";
                                lores_line2[special_index] = desc;
                                break;
                            case 1:
                                desc += "golden nuggets when cut by player";
                                lores_line2[special_index] = desc;
                                break;
                            case 2:
                                desc += "golden apples when cut by player";
                                lores_line2[special_index] = desc;
                                break;
                            case 3:
                                desc += "bottles o' enchanting when cut by player";
                                lores_line2[special_index] = desc;
                                break;
                            case 4:
                                desc += "enchanted golden apple when cut by player";
                                lores_line2[special_index] = desc;
                                break;
                            default:
                                break;
                        }
                        break;
                    case "fishing":
                        special_index = 1;
                        desc = "Unlocks treasure tier ";
                        switch (skill_1b_level) {
                            case 0:
                                desc += "I (common)";
                                lores_line2[special_index] = desc;
                                break;
                            case 1:
                                desc += "II (uncommon)";
                                lores_line2[special_index] = desc;
                                break;
                            case 2:
                                desc += "III (rare)";
                                lores_line2[special_index] = desc;
                                break;
                            case 3:
                                desc += "IV (very rare)";
                                lores_line2[special_index] = desc;
                                break;
                            case 4:
                                desc += "V (legendary)";
                                lores_line2[special_index] = desc;
                                break;
                            default:
                                break;
                        }
                        break;
                    case "farming":
                        special_index = 1;
                        desc = "Gain the ability to craft ";
                        switch (skill_1b_level) {
                            case 0:
                                desc += "cow spawn eggs";
                                lores_line2[special_index] = desc;
                                break;
                            case 1:
                                desc += "bee spawn eggs";
                                lores_line2[special_index] = desc;
                                break;
                            case 2:
                                desc += "mooshroom spawn eggs";
                                lores_line2[special_index] = desc;
                                break;
                            case 3:
                                desc += "horse spawn eggs";
                                lores_line2[special_index] = desc;
                                break;
                            case 4:
                                desc += "slime spawn eggs";
                                lores_line2[special_index] = desc;
                                break;
                            default:
                                break;
                        }
                        break;
                    case "digging":
                        special_index = 0;
                        switch (skill_1a_level) {
                            case 0:
                                desc = "May find ";
                                desc += "golden ingots";
                                desc += " while digging";
                                lores_line2[special_index] = desc;
                                break;
                            case 1:
                                desc = "May find ";
                                desc += "name tags";
                                desc += " while digging";
                                lores_line2[special_index] = desc;
                                break;
                            case 2:
                                desc = "May find ";
                                desc += "music discs";
                                desc += " while digging";
                                lores_line2[special_index] = desc;
                                break;
                            case 3:
                                desc = "May find ";
                                desc += "Horse armor";
                                desc += " while digging";
                                lores_line2[special_index] = desc;
                                break;
                            case 4:
                                desc = "May find ";
                                desc += "diamonds";
                                desc += " while digging";
                                lores_line2[special_index] = desc;
                                break;
                            default:
                                break;
                        }
                        switch (skill_2a_level) {
                            case 0:
                                desc = "May find ";
                                desc += "emeralds";
                                desc += " while digging";
                                lores_line2[2] = desc;
                                break;
                            case 1:
                                desc = "May find ";
                                desc += "enchanted books";
                                desc += " while digging";
                                lores_line2[2] = desc;
                                break;
                            case 2:
                                desc = "May find ";
                                desc += "dragon's breaths";
                                desc += " while digging";
                                lores_line2[2] = desc;
                                break;
                            case 3:
                                desc = "May find ";
                                desc += "totems of undying";
                                desc += " while digging";
                                lores_line2[2] = desc;
                                break;
                            case 4:
                                desc = "May find ";
                                desc += "nether stars";
                                desc += " while digging";
                                lores_line2[2] = desc;
                                break;
                            default:
                                break;
                        }
                        break;
                    case "defense":
                        special_index = 0;
                        switch (skill_1a_level) {
                            case 0:
                            case 2:
                            case 1:
                                desc = "Gain +3 s of regen per level on kills";
                                lores_line2[special_index] = desc;
                                break;
                            case 3:
                            case 4:
                                desc = "Gain +0.5 hearts per level instantly on kills";
                                lores_line2[special_index] = desc;
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }

                String[] lores_line1 = {"Level: 0/5","Level: 0/5","Level: 0/5","Level: 0/5","Level: 0/1","Level: 0/1","Level: 0/1"}; //Data lines 7-13
                for (int i = 0; i < 4; i++) {
                    String level = pStats.get(7+i).toString();
                    lores_line1[i] = ChatColor.GRAY + "Level " + ChatColor.GREEN + level + "/5";
                }
                for (int i = 0; i < 3; i++) {
                    String level = pStats.get(11+i).toString();
                    if (i != 2) {
                        lores_line1[i + 4] = ChatColor.GRAY + "Level " + ChatColor.BLUE + level + "/1";
                    }
                    else {
                        lores_line1[i + 4] = ChatColor.GRAY + "Level " + ChatColor.DARK_PURPLE + level + "/1";
                    }
                }

                Integer[] indices = {11,29,13,31,7,43,26};
                //
                for (int i = 0; i < labels.length; i++) {
                    int iter = 0;
                    ArrayList<String> splitDescs = new ArrayList<>();
                    ItemMeta meta = menu_items[i].getItemMeta();
                    meta.setDisplayName(ChatColor.BOLD + labels[i]);
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(lores_line1[i]);
                    splitDescs.add(lores_line2[i]);
                    while (splitDescs.get(splitDescs.size()-1).length() > 30) {
                        int lastIndex = splitDescs.size()-1;
                        boolean foundSpace = false;
                        int counter = 30;
                        while (foundSpace == false && counter > 0){
                            if (splitDescs.get(lastIndex).charAt(counter) == ' ') {
                                splitDescs.add(splitDescs.get(lastIndex).substring(0, counter));
                                splitDescs.add(splitDescs.get(lastIndex).substring(counter+1));
                                splitDescs.remove(iter);
                                iter += 1;
                                foundSpace = true;
                                if (iter > 4) {
                                    break;
                                }
                            }
                            counter = counter - 1;
                        }
                    }
                    for (int j = 0; j < splitDescs.size(); j++) {
                        lore.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + splitDescs.get(j));
                    }
                    meta.setLore(lore);
                    menu_items[i].setItemMeta(meta);
                    gui.setItem(indices[i], menu_items[i]);
                }
                //Tokens, passives, and back button
                ItemStack passive_token = new ItemStack(Material.IRON_NUGGET);
                ItemStack skill_token = new ItemStack(Material.GOLD_NUGGET);
                ItemStack passive_1 = new ItemStack(Material.RED_DYE,1);
                ItemStack passive_2 = new ItemStack(Material.GREEN_DYE,2);
                ItemStack passive_3 = new ItemStack(Material.BLUE_DYE,3);
                ItemStack back_button = new ItemStack(Material.ARROW);



                ItemStack[] menu_items_2 = {passive_token,back_button,skill_token,passive_1,passive_2,passive_3};
                String[] labels_2 = passivePerksMap.get(skillName);
                String[] lores_line1_2 = {"Total: 0","","Total: 0","Duration: 1 s","Percentage: 0%","Percentage: 0%"};
                String[] lores_line2_2 = passiveDescriptionsMap.get(skillName);

                //Total Passive Tokens
                lores_line1_2[0] = ChatColor.BLUE + "Total: " + ChatColor.GOLD + String.valueOf(tokens_P);
                if (tokens_P > 1 && tokens_P < 64){
                    passive_token.setAmount(tokens_P);
                }
                else if (tokens_P >= 64){
                    passive_token.setAmount(64);
                }

                //Total skill tokens
                lores_line1_2[2] = ChatColor.BLUE + "Total: " + ChatColor.GOLD + String.valueOf(tokens_S);
                if (tokens_S > 1 && tokens_S < 64){
                    skill_token.setAmount(tokens_S);
                }
                else if (tokens_S >= 64){
                    skill_token.setAmount(64);
                }

                double duration = passive1.doubleValue();
                double chance1 = passive2.doubleValue();
                double chance2 = passive3.doubleValue();
                //Passive 1
                duration = duration*0.02+ 2;
                duration = Math.round(duration*1000)/1000.0d;
                lores_line1_2[3] = ChatColor.GRAY + "Duration " + ChatColor.AQUA + String.valueOf(duration) + " s";
                switch (skillName){
                    case "digging":
                        //Passive2
                        chance1 = 1 + chance1*0.01;
                        chance1 = Math.round(chance1*1000)/1000.0d;
                        lores_line1_2[4] = ChatColor.GRAY + "Likelihood " + ChatColor.AQUA + String.valueOf(chance1) + "%";

                        //Passive 3
                        break;
                    case "woodcutting":
                    case "archery":
                        //Passive 2
                        chance1 = chance1*0.05;
                        chance1 = Math.round(chance1*1000)/1000.0d;
                        lores_line1_2[4] = ChatColor.GRAY + "Likelihood " + ChatColor.AQUA + String.valueOf(chance1) + "%";

                        //Passive 3
                        break;

                    //Passive 3
                    case "mining":
                        //Passive 2
                        chance1 = chance1*0.05;
                        chance1 = Math.round(chance1*1000)/1000.0d;
                        lores_line1_2[4] = ChatColor.GRAY + "Likelihood " + ChatColor.AQUA + String.valueOf(chance1) + "%";

                        //Passive 3
                        chance2 = chance2*0.01;
                        chance2 = Math.round(chance2*1000)/1000.0d;
                        lores_line1_2[5] = ChatColor.GRAY + "Likelihood " + ChatColor.AQUA + String.valueOf(chance2) + "%";
                        break;
                    case "farming":
                        //Passive 2
                        chance1 = chance1*0.05;
                        chance1 = Math.round(chance1*1000)/1000.0d;
                        lores_line1_2[4] = ChatColor.GRAY + "Likelihood " + ChatColor.AQUA + String.valueOf(chance1) + "%";

                        //Passive 3
                        chance2 = chance2*0.05;
                        chance2 = Math.round(chance2*1000)/1000.0d;
                        lores_line1_2[5] = ChatColor.GRAY + "Likelihood " + ChatColor.AQUA + String.valueOf(chance2) + "%";
                        break;
                    case "fishing":
                        //Passive 1
                        duration = duration/2.0;
                        duration = Math.round(duration*1000)/1000.0d;
                        lores_line1_2[3] = ChatColor.GRAY + "Duration " + ChatColor.AQUA + String.valueOf(duration) + " s";

                        //Passive 2
                        chance1 = chance1*0.05;
                        chance1 = Math.round(chance1*1000)/1000.0d;
                        lores_line1_2[4] = ChatColor.GRAY + "Likelihood " + ChatColor.AQUA + String.valueOf(chance1) + "%";

                        //Passive 3
                        chance2 = 10 - chance2*0.005;
                        chance2 = Math.round(chance2*1000)/1000.0d;
                        lores_line1_2[5] = ChatColor.GRAY + "Junk Chance " + ChatColor.AQUA + String.valueOf(chance2) + "%";
                        break;

                    //Passive 3
                    case "beastMastery":
                        //Passive 2
                        chance1 = chance1*0.025;
                        chance1 = Math.round(chance1*1000)/1000.0d;
                        lores_line1_2[4] = ChatColor.GRAY + "Likelihood " + ChatColor.AQUA + String.valueOf(chance1) + "%";

                        //Passive 3
                        break;
                    case "swordsmanship":
                        //Passive 2
                        chance1 = chance1*0.02;
                        chance1 = Math.round(chance1*1000)/1000.0d;
                        lores_line1_2[4] = ChatColor.GRAY + "Likelihood " + ChatColor.AQUA + String.valueOf(chance1) + "%";

                        //Passive 3
                        break;
                    case "defense":
                        //Passive 2
                        chance1 = 1 + chance1*0.01;
                        chance1 = Math.round(chance1*1000)/1000.0d;
                        lores_line1_2[4] = ChatColor.GRAY + "Likelihood " + ChatColor.AQUA + String.valueOf(chance1) + "%";

                        //Passive 3
                        chance2 = chance2*0.05;
                        chance2 = Math.round(chance2*1000)/1000.0d;
                        lores_line1_2[5] = ChatColor.GRAY + "Likelihood " + ChatColor.AQUA + String.valueOf(chance2) + "%";

                        break;


                    case "axeMastery":
                        //Passive 2
                        chance1 = chance1*0.01;
                        chance1 = Math.round(chance1*1000)/1000.0d;
                        lores_line1_2[4] = ChatColor.GRAY + "Likelihood " + ChatColor.AQUA + String.valueOf(chance1) + "%";

                        //Passive 3
                        break;

                    //Passive 3
                    default:
                        break;
                }

                Integer[] indices_2 = {0,45,9,18,27,36};
                for (int i = 0; i < labels_2.length; i++) {
                    ArrayList<String> splitDescs = new ArrayList<>();
                    ItemMeta meta = menu_items_2[i].getItemMeta();
                    meta.setDisplayName(ChatColor.BOLD + labels_2[i]);
                    ArrayList<String> lore = new ArrayList<>();
                    splitDescs.add(lores_line2_2[i]);
                    int iter = 0;
                    while (splitDescs.get(splitDescs.size()-1).length() > 30) {
                        int lastIndex = splitDescs.size()-1;
                        boolean foundSpace = false;
                        int counter = 30;
                        while (foundSpace == false && counter > 0){
                            if (splitDescs.get(lastIndex).charAt(counter) == ' ') {
                                splitDescs.add(splitDescs.get(lastIndex).substring(0, counter));
                                splitDescs.add(splitDescs.get(lastIndex).substring(counter+1));
                                splitDescs.remove(iter);
                                iter += 1;
                                foundSpace = true;
                                if (iter > 6) {
                                    break;
                                }
                            }
                            counter = counter - 1;
                        }
                    }
                    lore.add(lores_line1_2[i]);
                    for (int j = 0; j < splitDescs.size(); j++) {
                        lore.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + splitDescs.get(j));
                    }
                    meta.setLore(lore);
                    menu_items_2[i].setItemMeta(meta);
                    gui.setItem(indices_2[i], menu_items_2[i]);
                }

                if (skillName.equalsIgnoreCase("farming")) {
                    Integer[] indices_crafting = {48,49,50,51,52};
                    ItemStack[] crafting = {new ItemStack(Material.CRAFTING_TABLE),new ItemStack(Material.CRAFTING_TABLE),
                            new ItemStack(Material.CRAFTING_TABLE),new ItemStack(Material.CRAFTING_TABLE),
                            new ItemStack(Material.CRAFTING_TABLE)};
                    String[] craftingNames = {"Cow Egg","Bee Egg","Mooshroom Egg","Horse Egg","Slime Egg"};
                    int animalFarmLevel = (int) pStats.get(8);
                    for (int i=0; i < craftingNames.length; i++) {
                        ArrayList<String> lore = new ArrayList<>();
                        ItemMeta craftingMeta = crafting[i].getItemMeta();
                        if ( animalFarmLevel >= i+1) {
                            lore.add(ChatColor.GREEN + ChatColor.ITALIC.toString() + "UNLOCKED" );
                        }
                        else {
                            lore.add(ChatColor.RED + ChatColor.ITALIC.toString() + "LOCKED" );
                        }
                        craftingMeta.setDisplayName(ChatColor.BOLD + craftingNames[i]);
                        craftingMeta.setLore(lore);
                        crafting[i].setItemMeta(craftingMeta);
                        gui.setItem(indices_crafting[i],crafting[i]);
                    }
                }
                else if (skillName.equalsIgnoreCase("archery")) {
                    Integer[] indices_crafting = {48,49,50,51,52};
                    ItemStack[] crafting = {new ItemStack(Material.CRAFTING_TABLE)};
                    String[] craftingNames = {"Dragonless Arrows"};
                    int dragonlessArrowsLevel = (int) pStats.get(11);
                    for (int i=0; i < craftingNames.length; i++) {
                        ArrayList<String> lore = new ArrayList<>();
                        ItemMeta craftingMeta = crafting[i].getItemMeta();
                        if ( dragonlessArrowsLevel >= 1) {
                            lore.add(ChatColor.GREEN + ChatColor.ITALIC.toString() + "UNLOCKED" );
                        }
                        else {
                            lore.add(ChatColor.RED + ChatColor.ITALIC.toString() + "LOCKED" );
                        }
                        craftingMeta.setDisplayName(ChatColor.BOLD + craftingNames[i]);
                        craftingMeta.setLore(lore);
                        crafting[i].setItemMeta(craftingMeta);
                        gui.setItem(indices_crafting[i],crafting[i]);
                    }
                }
                //Toggle Buttons
                if (skillName.equalsIgnoreCase("digging")) {
                    //Flint Finder Toggle
                    ItemStack flintFinder = new ItemStack(Material.GRAY_WOOL);
                    ItemMeta flintFinderMeta = flintFinder.getItemMeta();
                    ArrayList<String> flintFinderLore = new ArrayList<>();
                    flintFinderMeta.setDisplayName(ChatColor.BOLD + "Flint Finder Toggle");
                    if (skill_3a_level < 1) {
                        flintFinderLore.add(ChatColor.RED + ChatColor.ITALIC.toString() + "LOCKED" );
                        flintFinderMeta.setLore(flintFinderLore);
                        flintFinder.setItemMeta(flintFinderMeta);
                        gui.setItem(47,flintFinder);
                    }
                    else {
                        if ((int) pStatAll.get("global").get(12) < 1) {
                            flintFinderLore.add(ChatColor.RED + ChatColor.ITALIC.toString() + "OFF" );
                            flintFinderMeta.setLore(flintFinderLore);
                            flintFinder.setItemMeta(flintFinderMeta);
                            gui.setItem(47,flintFinder);
                        }
                        else {
                            flintFinderLore.add(ChatColor.GREEN + ChatColor.ITALIC.toString() + "ON" );
                            flintFinderMeta.setLore(flintFinderLore);
                            flintFinder.setItemMeta(flintFinderMeta);
                            flintFinder.setType(Material.LIME_WOOL);
                            gui.setItem(47,flintFinder);
                        }
                    }

                    //Mega Dig Toggle
                    ItemStack megaDig = new ItemStack(Material.GRAY_WOOL);
                    ItemMeta megaDigMeta = megaDig.getItemMeta();
                    ArrayList<String> megaDigLore = new ArrayList<>();
                    megaDigMeta.setDisplayName(ChatColor.BOLD + "Mega Dig Toggle");
                    if (skill_M_level < 1) {
                        megaDigLore.add(ChatColor.RED + ChatColor.ITALIC.toString() + "LOCKED" );
                        megaDigMeta.setLore(megaDigLore);
                        megaDig.setItemMeta(megaDigMeta);
                        gui.setItem(48,megaDig);
                    }
                    else {
                        if ((int) pStatAll.get("global").get(19) < 1) {
                            megaDigLore.add(ChatColor.RED + ChatColor.ITALIC.toString() + "OFF" );
                            megaDigMeta.setLore(megaDigLore);
                            megaDig.setItemMeta(megaDigMeta);
                            gui.setItem(48,megaDig);
                        }
                        else {
                            megaDigLore.add(ChatColor.GREEN + ChatColor.ITALIC.toString() + "ON" );
                            megaDigMeta.setLore(megaDigLore);
                            megaDig.setItemMeta(megaDigMeta);
                            megaDig.setType(Material.LIME_WOOL);
                            gui.setItem(48,megaDig);
                        }
                    }

                }
                else if (skillName.equalsIgnoreCase("mining")) {
                    //Vein Miner Toggle
                    ItemStack veinMiner = new ItemStack(Material.GRAY_WOOL);
                    ItemMeta veinMinerMeta = veinMiner.getItemMeta();
                    ArrayList<String> veinMinerLore = new ArrayList<>();
                    veinMinerMeta.setDisplayName(ChatColor.BOLD + "Vein Miner Toggle");
                    if (skill_3a_level < 1) {
                        veinMinerLore.add(ChatColor.RED + ChatColor.ITALIC.toString() + "LOCKED" );
                        veinMinerMeta.setLore(veinMinerLore);
                        veinMiner.setItemMeta(veinMinerMeta);
                        gui.setItem(47,veinMiner);
                    }
                    else {
                        if ((int) pStatAll.get("global").get(18) < 1) {
                            veinMinerLore.add(ChatColor.RED + ChatColor.ITALIC.toString() + "OFF" );
                            veinMinerMeta.setLore(veinMinerLore);
                            veinMiner.setItemMeta(veinMinerMeta);
                            gui.setItem(47,veinMiner);
                        }
                        else {
                            veinMinerLore.add(ChatColor.GREEN + ChatColor.ITALIC.toString() + "ON" );
                            veinMinerMeta.setLore(veinMinerLore);
                            veinMiner.setItemMeta(veinMinerMeta);
                            veinMiner.setType(Material.LIME_WOOL);
                            gui.setItem(47,veinMiner);
                        }
                    }

                }
                else if (skillName.equalsIgnoreCase("fishing")) {
                    //Grappling Hook Toggle
                    ItemStack grapplingHook = new ItemStack(Material.GRAY_WOOL);
                    ItemMeta grapplingHookMeta = grapplingHook.getItemMeta();
                    ArrayList<String> grapplingHookLore = new ArrayList<>();
                    grapplingHookMeta.setDisplayName(ChatColor.BOLD + "Grappling Hook Toggle");
                    if (skill_3a_level < 1) {
                        grapplingHookLore.add(ChatColor.RED + ChatColor.ITALIC.toString() + "LOCKED" );
                        grapplingHookMeta.setLore(grapplingHookLore);
                        grapplingHook.setItemMeta(grapplingHookMeta);
                        gui.setItem(47,grapplingHook);
                    }
                    else {
                        if ((int) pStatAll.get("global").get(16) < 1) {
                            grapplingHookLore.add(ChatColor.RED + ChatColor.ITALIC.toString() + "OFF" );
                            grapplingHookMeta.setLore(grapplingHookLore);
                            grapplingHook.setItemMeta(grapplingHookMeta);
                            gui.setItem(47,grapplingHook);
                        }
                        else {
                            grapplingHookLore.add(ChatColor.GREEN + ChatColor.ITALIC.toString() + "ON" );
                            grapplingHookMeta.setLore(grapplingHookLore);
                            grapplingHook.setItemMeta(grapplingHookMeta);
                            grapplingHook.setType(Material.LIME_WOOL);
                            gui.setItem(47,grapplingHook);
                        }
                    }

                    //Hot rod Toggle
                    ItemStack hotRod = new ItemStack(Material.GRAY_WOOL);
                    ItemMeta hotRodMeta = hotRod.getItemMeta();
                    ArrayList<String> hotRodLore = new ArrayList<>();
                    hotRodMeta.setDisplayName(ChatColor.BOLD + "Hot Rod Toggle");
                    if (skill_3b_level < 1) {
                        hotRodLore.add(ChatColor.RED + ChatColor.ITALIC.toString() + "LOCKED" );
                        hotRodMeta.setLore(hotRodLore);
                        hotRod.setItemMeta(hotRodMeta);
                        gui.setItem(48,hotRod);
                    }
                    else {
                        if ((int) pStatAll.get("global").get(17) < 1) {
                            hotRodLore.add(ChatColor.RED + ChatColor.ITALIC.toString() + "OFF" );
                            hotRodMeta.setLore(hotRodLore);
                            hotRod.setItemMeta(hotRodMeta);
                            gui.setItem(48,hotRod);
                        }
                        else {
                            hotRodLore.add(ChatColor.GREEN + ChatColor.ITALIC.toString() + "ON" );
                            hotRodMeta.setLore(hotRodLore);
                            hotRod.setItemMeta(hotRodMeta);
                            hotRod.setType(Material.LIME_WOOL);
                            gui.setItem(48,hotRod);
                        }
                    }
                }

                //Soul Bucket (Refunding)
                ItemStack soul = new ItemStack(Material.COMPOSTER);
                ItemMeta soulMeta = soul.getItemMeta();
                ArrayList<String> soulLore = new ArrayList<>();
                soulMeta.setDisplayName(ChatColor.BOLD + "Refund Skill");
                if ((int) pStatAll.get("global").get(9) < 1) {
                    soulLore.add(ChatColor.RED + ChatColor.ITALIC.toString() + "LOCKED" );
                }
                else {
                    int souls = (int) pStatAll.get("global").get(20);
                    soulLore.add("Souls: " + ChatColor.AQUA + ChatColor.ITALIC.toString() + souls + "/250");
                    soulLore.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Click to refund this skill tree");
                    soulLore.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + "(Costs 250 souls)");
                }
                soulMeta.setLore(soulLore);
                soul.setItemMeta(soulMeta);
                gui.setItem(53,soul);


                //Connectors
                ItemStack connector = new ItemStack(Material.GLASS_PANE);
                ItemMeta connectorMeta = connector.getItemMeta();
                connectorMeta.setDisplayName("");
                connector.setItemMeta(connectorMeta);
                Integer[] indices_3 = {6,12,14,24,25,30,32,42};
                for (int i = 0; i < indices_3.length; i++) {
                    gui.setItem(indices_3[i], connector);
                }
                //Put the items in the inventory
                p.openInventory(gui);
            }
            else if (sender instanceof Player && labels_arr.indexOf(args[1]) >= 10 && labels_arr.indexOf(args[1]) < 15) {
                Player p = (Player) sender;
                String skillName = labels_0[labels_arr.indexOf(args[1])];
                String skillTitle = titles_0[labels_arr.indexOf(args[1])];
                Inventory gui = Bukkit.createInventory(p, 54, skillTitle);

                //Skills
                PlayerStats pStatClass = new PlayerStats(p);
                Map<String, ArrayList<Number>> pStatAll = pStatClass.getPlayerData();
                ArrayList<Number> pStats = pStatAll.get(skillName);
                int tokens_S = (Integer) pStats.get(3);
                Number passive1 = pStats.get(4);
                int skill_1a_level = (Integer) pStats.get(7);
                int skill_2a_level = (Integer) pStats.get(9);
                int skill_M_level = (Integer) pStats.get(13);

                ItemStack skill_1a = new ItemStack(Material.PINK_TERRACOTTA);
                ItemStack skill_2a = new ItemStack(Material.RED_TERRACOTTA);
                ItemStack skill_M = new ItemStack(Material.RED_TERRACOTTA);

                if (skill_1a_level == 0) {
                    skill_1a.setType(Material.PINK_TERRACOTTA);
                } else if (skill_1a_level > 0 && skill_1a_level < 5) {
                    skill_1a.setType(Material.YELLOW_TERRACOTTA);
                } else {
                    skill_1a.setType(Material.GREEN_TERRACOTTA);
                }

                if (skill_2a_level == 0) {
                    if (skill_1a_level >= 2) {
                        skill_2a.setType(Material.PINK_TERRACOTTA);
                    }
                } else if (skill_2a_level > 0 && skill_2a_level < 5) {
                    skill_2a.setType(Material.YELLOW_TERRACOTTA);
                } else {
                    skill_2a.setType(Material.GREEN_TERRACOTTA);
                }

                if (skill_M_level == 0) {
                    if (skill_1a_level + skill_2a_level >= 10) {
                        skill_M.setType(Material.PINK_TERRACOTTA);
                    }
                } else {
                    skill_M.setType(Material.GREEN_TERRACOTTA);
                }

                ItemStack[] menu_items = {skill_1a, skill_2a, skill_M};
                String[] labels = perksMap.get(skillName);
                String[] lores_line2 = descriptionsMap.get(skillName);
                String desc = "";
                int special_index = 0;
                switch (skillName) {
                    case "alchemy":
                        special_index = 1;
                        switch (skill_2a_level) {
                            case 0:
                                desc = "Unlocks ability to brew ";
                                desc += "Hero of The Village potions (Ingredient: Emerald)";
                                lores_line2[special_index] = desc;
                                break;
                            case 1:
                                desc = "Unlocks ability to brew ";
                                desc += "Mining Fatigue potions (Ingredient: Slimeball)";
                                lores_line2[special_index] = desc;
                                break;
                            case 2:
                                desc = "Unlocks ability to brew ";
                                desc += "Haste potions (Ingredient: Clock)";
                                lores_line2[special_index] = desc;
                                break;
                            case 3:
                                desc = "Unlocks ability to brew ";
                                desc += "Wither potions (Ingredient: Poisonous Potato)";
                                lores_line2[special_index] = desc;
                                break;
                            case 4:
                                desc = "Unlocks ability to brew ";
                                desc += "Resistance potions (Ingredient: Golden Apple)";
                                lores_line2[special_index] = desc;
                                break;
                            default:
                                break;
                        }
                        switch (skill_1a_level) {
                            case 0:
                                desc = "Gain the ability to craft water breathing potions in a crafting table";
                                lores_line2[0] = desc;
                                break;
                            case 1:
                                desc = "Gain the ability to craft speed potions in a crafting table";
                                lores_line2[0] = desc;
                                break;
                            case 2:
                                desc = "Gain the ability to craft fire resistance potions in a crafting table";
                                lores_line2[0] = desc;
                                break;
                            case 3:
                                desc = "Gain the ability to craft healing potions in a crafting table";
                                lores_line2[0] = desc;
                                break;
                            case 4:
                                desc = "Gain the ability to craft strength potions in a crafting table";
                                lores_line2[0] = desc;
                                break;
                            default:
                                break;
                        }
                        break;
                    case "enchanting":
                        special_index = 1;
                        desc = "Unlocks ability to craft ";
                        switch (skill_2a_level) {
                            case 0:
                                desc += "Power I and Efficiency I enchanted books (costs 1 level to craft)";
                                lores_line2[special_index] = desc;
                                break;
                            case 1:
                                desc += "Sharpness I and Protection I enchanted books (costs 1 level to craft)";
                                lores_line2[special_index] = desc;
                                break;
                            case 2:
                                desc += "Luck of the Sea I and Lure I enchanted books (costs 1 level to craft)";
                                lores_line2[special_index] = desc;
                                break;
                            case 3:
                                desc += "Depth Strider I and Frost Walker I enchanted books (costs 1 level to craft)";
                                lores_line2[special_index] = desc;
                                break;
                            case 4:
                                desc += "Mending (costs 10 levels to craft) and Fortune I (costs 2 levels to craft) enchanted books";
                                lores_line2[special_index] = desc;
                                break;
                            default:
                                break;
                        }
                    case "repair":
                        special_index = 0;
                        switch (skill_1a_level) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                                desc += "Gain more materials from salvaging on average";
                                lores_line2[special_index] = desc;
                                break;
                            case 4:
                                desc += "Gain more materials from salvaging on average, salvaging now stores item enchants in a book";
                                lores_line2[special_index] = desc;
                                break;
                            default:
                                break;
                        }
                        break;
                }
                String[] lores_line1 = {"Level: 0/5","Level: 0/5","Level: 0/1"};
                String level = pStats.get(7).toString();
                lores_line1[0] = ChatColor.GRAY + "Level " + ChatColor.GREEN + level + "/5";
                level = pStats.get(9).toString();
                lores_line1[1] = ChatColor.GRAY + "Level " + ChatColor.GREEN + level + "/5";
                level = pStats.get(13).toString();
                lores_line1[2] = ChatColor.GRAY + "Level " + ChatColor.DARK_PURPLE + level + "/1";

                Integer[] indices = {20,23,26};
                //Set skills
                for (int i = 0; i < labels.length; i++) {
                    int iter = 0;
                    ArrayList<String> splitDescs = new ArrayList<>();
                    ItemMeta meta = menu_items[i].getItemMeta();
                    meta.setDisplayName(ChatColor.BOLD + labels[i]);
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(lores_line1[i]);
                    splitDescs.add(lores_line2[i]);
                    while (splitDescs.get(splitDescs.size()-1).length() > 30) {
                        int lastIndex = splitDescs.size()-1;
                        boolean foundSpace = false;
                        int counter = 30;
                        while (foundSpace == false && counter > 0){
                            if (splitDescs.get(lastIndex).charAt(counter) == ' ') {
                                splitDescs.add(splitDescs.get(lastIndex).substring(0, counter));
                                splitDescs.add(splitDescs.get(lastIndex).substring(counter+1));
                                splitDescs.remove(iter);
                                iter += 1;
                                foundSpace = true;
                                if (iter > 4) {
                                    break;
                                }
                            }
                            counter = counter - 1;
                        }
                    }
                    for (int j = 0; j < splitDescs.size(); j++) {
                        lore.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + splitDescs.get(j));
                    }
                    meta.setLore(lore);
                    menu_items[i].setItemMeta(meta);
                    gui.setItem(indices[i], menu_items[i]);
                }

                //Tokens, passives, and back button
                ItemStack skill_token = new ItemStack(Material.GOLD_NUGGET);
                ItemStack passive_1 = new ItemStack(Material.RED_DYE,1);
                ItemStack back_button = new ItemStack(Material.ARROW);



                ItemStack[] menu_items_2 = {back_button,skill_token,passive_1};
                String[] labels_2 = passivePerksMap.get(skillName);
                String[] lores_line1_2 = {"","Total: 0","Chance: 0 %"};
                String[] lores_line2_2 = passiveDescriptionsMap.get(skillName);

                //Total skill tokens
                lores_line1_2[1] = ChatColor.BLUE + "Total: " + ChatColor.GOLD + String.valueOf(tokens_S);
                if (tokens_S > 1 && tokens_S < 64){
                    skill_token.setAmount(tokens_S);
                }
                else if (tokens_S >= 64){
                    skill_token.setAmount(64);
                }

                double passive = passive1.doubleValue();
                switch (skillName){
                    case "repair":
                        passive = passive;
                        //passive = Math.round(passive*1000)/1000.0d;
                        lores_line1_2[2] = ChatColor.GRAY + "Level: " + ChatColor.AQUA + String.valueOf(passive);
                        break;
                    case "agility":
                        passive = passive*0.05;
                        passive = Math.round(passive*1000)/1000.0d;
                        lores_line1_2[2] = ChatColor.GRAY + "Likelihood: " + ChatColor.AQUA + String.valueOf(passive)+"%";
                        break;
                    case "enchanting":
                        passive = passive*0.2;
                        passive = Math.round(passive*1000)/1000.0d;
                        lores_line1_2[2] = ChatColor.GRAY + "XP Boost: " + ChatColor.AQUA + "+"+String.valueOf(passive)+"%";
                        break;
                    case "smelting":
                        passive = passive*0.2;
                        passive = Math.round(passive*1000)/1000.0d;
                        lores_line1_2[2] = ChatColor.GRAY + "Speed Boost: " + ChatColor.AQUA + "+"+String.valueOf(passive)+"%";
                        break;
                    case "alchemy":
                        passive = passive*0.1;
                        passive = Math.round(passive*1000)/1000.0d;
                        lores_line1_2[2] = ChatColor.GRAY + "Time Extension: " + ChatColor.AQUA + "+"+String.valueOf(passive)+"%";
                        break;
                    default:
                        break;
                }

                Integer[] indices_2 = {45,0,18};
                for (int i = 0; i < labels_2.length; i++) {
                    ArrayList<String> splitDescs = new ArrayList<>();
                    ItemMeta meta = menu_items_2[i].getItemMeta();
                    meta.setDisplayName(ChatColor.BOLD + labels_2[i]);
                    ArrayList<String> lore = new ArrayList<>();
                    splitDescs.add(lores_line2_2[i]);
                    int iter = 0;
                    while (splitDescs.get(splitDescs.size()-1).length() > 30) {
                        int lastIndex = splitDescs.size()-1;
                        boolean foundSpace = false;
                        int counter = 30;
                        while (foundSpace == false && counter > 0){
                            if (splitDescs.get(lastIndex).charAt(counter) == ' ') {
                                splitDescs.add(splitDescs.get(lastIndex).substring(0, counter));
                                splitDescs.add(splitDescs.get(lastIndex).substring(counter+1));
                                splitDescs.remove(iter);
                                iter += 1;
                                foundSpace = true;
                                if (iter > 6) {
                                    break;
                                }
                            }
                            counter = counter - 1;
                        }
                    }
                    lore.add(lores_line1_2[i]);
                    for (int j = 0; j < splitDescs.size(); j++) {
                        lore.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + splitDescs.get(j));
                    };
                    meta.setLore(lore);
                    menu_items_2[i].setItemMeta(meta);
                    gui.setItem(indices_2[i], menu_items_2[i]);
                }

                //Crafting
                if (skillName.equalsIgnoreCase("enchanting")) {
                    Integer[] indices_crafting = {39,40,41,42,43,48,49,50,51,52};
                    ItemStack[] crafting = {new ItemStack(Material.CRAFTING_TABLE),new ItemStack(Material.CRAFTING_TABLE),
                            new ItemStack(Material.CRAFTING_TABLE),new ItemStack(Material.CRAFTING_TABLE),
                            new ItemStack(Material.CRAFTING_TABLE),new ItemStack(Material.CRAFTING_TABLE),
                            new ItemStack(Material.CRAFTING_TABLE),new ItemStack(Material.CRAFTING_TABLE),
                            new ItemStack(Material.CRAFTING_TABLE),new ItemStack(Material.CRAFTING_TABLE)};
                    String[] craftingNames = {"Power I Book","Efficiency I Book","Sharpness I Book","Protection I Book","Luck of the Sea I Book",
                            "Lure I Book","Frost Walker I Book","Depth Strider I Book", "Mending Book","Fortune I Book"};
                    int bookSmartLevel = (int) pStats.get(9);
                    for (int i=0; i < craftingNames.length; i++) {
                        ArrayList<String> lore = new ArrayList<>();
                        ItemMeta craftingMeta = crafting[i].getItemMeta();
                        if ( bookSmartLevel >= Math.ceil((double) (i+1)/2.0)) {
                            lore.add(ChatColor.GREEN + ChatColor.ITALIC.toString() + "UNLOCKED" );
                        }
                        else {
                            lore.add(ChatColor.RED + ChatColor.ITALIC.toString() + "LOCKED" );
                        }
                        craftingMeta.setDisplayName(ChatColor.BOLD + craftingNames[i]);
                        craftingMeta.setLore(lore);
                        crafting[i].setItemMeta(craftingMeta);
                        gui.setItem(indices_crafting[i],crafting[i]);
                    }
                }
                else if (skillName.equalsIgnoreCase("alchemy")) {
                    Integer[] indices_crafting = {48,49,50,51,52};
                    ItemStack[] crafting = {new ItemStack(Material.CRAFTING_TABLE),new ItemStack(Material.CRAFTING_TABLE),
                            new ItemStack(Material.CRAFTING_TABLE),new ItemStack(Material.CRAFTING_TABLE),
                            new ItemStack(Material.CRAFTING_TABLE)};
                    String[] craftingNames = {"Water Breathing Potion","Speed Potion","Fire Resistance Potion","Healing Potion","Strength Potion"};
                    int alchemicalSummoningLevel = (int) pStats.get(7);
                    for (int i=0; i < craftingNames.length; i++) {
                        ArrayList<String> lore = new ArrayList<>();
                        ItemMeta craftingMeta = crafting[i].getItemMeta();
                        if ( alchemicalSummoningLevel > i) {
                            lore.add(ChatColor.GREEN + ChatColor.ITALIC.toString() + "UNLOCKED" );
                        }
                        else {
                            lore.add(ChatColor.RED + ChatColor.ITALIC.toString() + "LOCKED" );
                        }
                        craftingMeta.setDisplayName(ChatColor.BOLD + craftingNames[i]);
                        craftingMeta.setLore(lore);
                        crafting[i].setItemMeta(craftingMeta);
                        gui.setItem(indices_crafting[i],crafting[i]);
                    }
                }

                //Toggle Buttons
                if (skillName.equalsIgnoreCase("smelting")) {
                    //Flame Pick Toggle
                    ItemStack flamePick = new ItemStack(Material.GRAY_WOOL);
                    ItemMeta flamePickMeta = flamePick.getItemMeta();
                    ArrayList<String> flamePickLore = new ArrayList<>();
                    flamePickMeta.setDisplayName(ChatColor.BOLD + "Flame Pick Toggle");
                    if (skill_M_level < 1) {
                        flamePickLore.add(ChatColor.RED + ChatColor.ITALIC.toString() + "LOCKED" );
                        flamePickMeta.setLore(flamePickLore);
                        flamePick.setItemMeta(flamePickMeta);
                        gui.setItem(47,flamePick);
                    }
                    else {
                        if ((int) pStatAll.get("global").get(13) < 1) {
                            flamePickLore.add(ChatColor.RED + ChatColor.ITALIC.toString() + "OFF" );
                            flamePickMeta.setLore(flamePickLore);
                            flamePick.setItemMeta(flamePickMeta);
                            gui.setItem(47,flamePick);
                        }
                        else {
                            flamePickLore.add(ChatColor.GREEN + ChatColor.ITALIC.toString() + "ON" );
                            flamePickMeta.setLore(flamePickLore);
                            flamePick.setItemMeta(flamePickMeta);
                            flamePick.setType(Material.LIME_WOOL);
                            gui.setItem(47,flamePick);
                        }
                    }
                }
                else if (skillName.equalsIgnoreCase("agility")) {
                    //graceful Feet Toggle
                    ItemStack gracefulFeet = new ItemStack(Material.GRAY_WOOL);
                    ItemMeta gracefulFeetMeta = gracefulFeet.getItemMeta();
                    ArrayList<String> gracefulFeetLore = new ArrayList<>();
                    gracefulFeetMeta.setDisplayName(ChatColor.BOLD + "Graceful Feet Toggle");
                    if (skill_M_level < 1) {
                        gracefulFeetLore.add(ChatColor.RED + ChatColor.ITALIC.toString() + "LOCKED" );
                        gracefulFeetMeta.setLore(gracefulFeetLore);
                        gracefulFeet.setItemMeta(gracefulFeetMeta);
                        gui.setItem(47,gracefulFeet);
                    }
                    else {
                        if ((int) pStatAll.get("global").get(14) < 1) {
                            gracefulFeetLore.add(ChatColor.RED + ChatColor.ITALIC.toString() + "OFF" );
                            gracefulFeetMeta.setLore(gracefulFeetLore);
                            gracefulFeet.setItemMeta(gracefulFeetMeta);
                            gui.setItem(47,gracefulFeet);
                        }
                        else {
                            gracefulFeetLore.add(ChatColor.GREEN + ChatColor.ITALIC.toString() + "ON" );
                            gracefulFeetMeta.setLore(gracefulFeetLore);
                            gracefulFeet.setItemMeta(gracefulFeetMeta);
                            gracefulFeet.setType(Material.LIME_WOOL);
                            gui.setItem(47,gracefulFeet);
                        }
                    }
                }
                else if (skillName.equalsIgnoreCase("alchemy")) {
                    //Potion Master Toggle
                    ItemStack potionMaster = new ItemStack(Material.GRAY_WOOL);
                    ItemMeta potionMasterMeta = potionMaster.getItemMeta();
                    ArrayList<String> potionMasterLore = new ArrayList<>();
                    potionMasterMeta.setDisplayName(ChatColor.BOLD + "Potion Master Toggle");
                    if (skill_M_level < 1) {
                        potionMasterLore.add(ChatColor.RED + ChatColor.ITALIC.toString() + "LOCKED" );
                        potionMasterMeta.setLore(potionMasterLore);
                        potionMaster.setItemMeta(potionMasterMeta);
                        gui.setItem(47,potionMaster);
                    }
                    else {
                        if ((int) pStatAll.get("global").get(15) < 1) {
                            potionMasterLore.add(ChatColor.RED + ChatColor.ITALIC.toString() + "OFF" );
                            potionMasterMeta.setLore(potionMasterLore);
                            potionMaster.setItemMeta(potionMasterMeta);
                            gui.setItem(47,potionMaster);
                        }
                        else {
                            potionMasterLore.add(ChatColor.GREEN + ChatColor.ITALIC.toString() + "ON" );
                            potionMasterMeta.setLore(potionMasterLore);
                            potionMaster.setItemMeta(potionMasterMeta);
                            potionMaster.setType(Material.LIME_WOOL);
                            gui.setItem(47,potionMaster);
                        }
                    }
                }

                //Souls (refunding)
                ItemStack soul = new ItemStack(Material.COMPOSTER);
                ItemMeta soulMeta = soul.getItemMeta();
                ArrayList<String> soulLore = new ArrayList<>();
                soulMeta.setDisplayName(ChatColor.BOLD + "Refund Skill");
                if ((int) pStatAll.get("global").get(9) < 1) {
                    soulLore.add(ChatColor.RED + ChatColor.ITALIC.toString() + "LOCKED" );
                }
                else {
                    int souls = (int) pStatAll.get("global").get(20);
                    soulLore.add("Souls: " + ChatColor.AQUA + ChatColor.ITALIC.toString() + souls + "/250");
                    soulLore.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Click to refund this skill tree");
                    soulLore.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + "(Costs 250 souls)");
                }
                soulMeta.setLore(soulLore);
                soul.setItemMeta(soulMeta);
                gui.setItem(53,soul);

                //Connectors
                ItemStack connector = new ItemStack(Material.GLASS_PANE);
                ItemMeta connectorMeta = connector.getItemMeta();
                connectorMeta.setDisplayName("");
                connector.setItemMeta(connectorMeta);
                Integer[] indices_3 = {21,22,24,25};
                for (int i = 0; i < indices_3.length; i++) {
                    gui.setItem(indices_3[i], connector);
                }
                //Put the items in the inventory
                p.openInventory(gui);

            }
            else if (sender instanceof Player && labels_arr.indexOf(args[1]) > 10 && labels_arr.indexOf(args[1]) == 15) {
                Player p = (Player) sender;
                String skillName = labels_0[labels_arr.indexOf(args[1])];
                String skillTitle = titles_0[labels_arr.indexOf(args[1])];
                Inventory gui = Bukkit.createInventory(p, 54,skillTitle);
                PlayerStats pStatClass = new PlayerStats(p);
                Map<String, ArrayList<Number>> pStatAll = pStatClass.getPlayerData();
                ArrayList<Number> pStats = pStatAll.get(skillName);

                int tokens_G = (Integer) pStats.get(1);
                int skill_1a_level = (Integer) pStats.get(2);
                int skill_1b_level = (Integer) pStats.get(3);
                int skill_1c_level = (Integer) pStats.get(4);
                int skill_2a_level = (Integer) pStats.get(5);
                int skill_2b_level = (Integer) pStats.get(6);
                int skill_2c_level = (Integer) pStats.get(7);
                int skill_3a_level = (Integer) pStats.get(8);
                int skill_3b_level = (Integer) pStats.get(9);
                int skill_3c_level = (Integer) pStats.get(10);
                int skill_M_level =  (Integer) pStats.get(11);

                ItemStack skill_1a = new ItemStack(Material.PINK_TERRACOTTA);
                ItemStack skill_2a = new ItemStack(Material.PINK_TERRACOTTA);
                ItemStack skill_3a = new ItemStack(Material.PINK_TERRACOTTA);
                ItemStack skill_1b = new ItemStack(Material.RED_TERRACOTTA);
                ItemStack skill_2b = new ItemStack(Material.RED_TERRACOTTA);
                ItemStack skill_3b = new ItemStack(Material.RED_TERRACOTTA);
                ItemStack skill_1c = new ItemStack(Material.RED_TERRACOTTA);
                ItemStack skill_2c = new ItemStack(Material.RED_TERRACOTTA);
                ItemStack skill_3c = new ItemStack(Material.RED_TERRACOTTA);
                ItemStack skill_M = new ItemStack(Material.RED_TERRACOTTA);


                if (skill_1a_level == 0) {
                    skill_1a.setType(Material.PINK_TERRACOTTA);
                }
                else {
                    skill_1a.setType(Material.GREEN_TERRACOTTA);
                }

                if (skill_1b_level == 0) {
                    skill_1b.setType(Material.PINK_TERRACOTTA);
                }
                else {
                    skill_1b.setType(Material.GREEN_TERRACOTTA);
                }

                if (skill_1c_level == 0) {
                    skill_1c.setType(Material.PINK_TERRACOTTA);
                }
                else {
                    skill_1c.setType(Material.GREEN_TERRACOTTA);
                }

                if (skill_2a_level == 0) {
                    if (skill_1a_level == 1) {
                        skill_2a.setType(Material.PINK_TERRACOTTA);
                    }
                    else {
                        skill_2a.setType(Material.RED_TERRACOTTA);
                    }
                }
                else {
                    skill_2a.setType(Material.GREEN_TERRACOTTA);
                }

                if (skill_2b_level == 0) {
                    if (skill_1b_level == 1) {
                        skill_2b.setType(Material.PINK_TERRACOTTA);
                    }
                    else {
                        skill_2b.setType(Material.RED_TERRACOTTA);
                    }
                }
                else {
                    skill_2b.setType(Material.GREEN_TERRACOTTA);
                }

                if (skill_2c_level == 0) {
                    if (skill_1c_level == 1) {
                        skill_2c.setType(Material.PINK_TERRACOTTA);
                    }
                    else {
                        skill_2c.setType(Material.RED_TERRACOTTA);
                    }
                }
                else {
                    skill_2c.setType(Material.GREEN_TERRACOTTA);
                }

                if (skill_3a_level == 0) {
                    if (skill_3b_level == 1) {
                        skill_3a.setType(Material.PINK_TERRACOTTA);
                    }
                    else {
                        skill_3a.setType(Material.RED_TERRACOTTA);
                    }
                }
                else {
                    skill_3a.setType(Material.GREEN_TERRACOTTA);
                }

                if (skill_3c_level == 0) {
                    if (skill_3b_level == 1) {
                        skill_3c.setType(Material.PINK_TERRACOTTA);
                    }
                    else {
                        skill_3c.setType(Material.RED_TERRACOTTA);
                    }
                }
                else {
                    skill_3c.setType(Material.GREEN_TERRACOTTA);
                }

                if (skill_3b_level == 0) {
                    if (skill_2a_level == 1 && skill_2b_level == 1 && skill_2c_level == 1) {
                        skill_3b.setType(Material.PINK_TERRACOTTA);
                    }
                    else {
                        skill_3b.setType(Material.RED_TERRACOTTA);
                    }
                }
                else {
                    skill_3b.setType(Material.GREEN_TERRACOTTA);
                }

                if (skill_M_level == 0) {
                    if (skill_3a_level == 1 && skill_3b_level == 1 && skill_3c_level == 1) {
                        skill_M.setType(Material.PINK_TERRACOTTA);
                    }
                    else {
                        skill_M.setType(Material.RED_TERRACOTTA);
                    }
                }
                else {
                    skill_M.setType(Material.GREEN_TERRACOTTA);
                }

                ItemStack[] menu_items = {skill_1a,skill_1b,skill_1c,skill_2a,skill_2b,skill_2c,skill_3a,skill_3b,skill_3c,skill_M};
                String[] labels = perksMap.get(skillName);
                String[] lores_line2 = descriptionsMap.get(skillName);
                String[] lores_line1 = {"1","2","3","4","5","6","7","8","9","10"}; //Data lines 7-13
                for (int i = 0; i < labels.length; i++) {
                    String level = pStats.get(2+i).toString();
                    lores_line1[i] = ChatColor.GRAY + "Level " + ChatColor.BLUE + level + "/1";
                    if (i==9) {
                        lores_line1[i] = ChatColor.GRAY + "Level " + ChatColor.DARK_PURPLE + level + "/1";
                    }
                }

                Integer[] indices = {1,19,37,3,21,39,6,24,42,26};
                //
                for (int i = 0; i < labels.length; i++) {
                    int iter = 0;
                    ArrayList<String> splitDescs = new ArrayList<>();
                    ItemMeta meta = menu_items[i].getItemMeta();
                    meta.setDisplayName(ChatColor.BOLD + labels[i]);
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(lores_line1[i]);
                    splitDescs.add(lores_line2[i]);
                    while (splitDescs.get(splitDescs.size()-1).length() > 30) {
                        int lastIndex = splitDescs.size()-1;
                        boolean foundSpace = false;
                        int counter = 30;
                        while (foundSpace == false && counter > 0){
                            if (splitDescs.get(lastIndex).charAt(counter) == ' ') {
                                splitDescs.add(splitDescs.get(lastIndex).substring(0, counter));
                                splitDescs.add(splitDescs.get(lastIndex).substring(counter+1));
                                splitDescs.remove(iter);
                                iter += 1;
                                foundSpace = true;
                                if (iter > 4) {
                                    break;
                                }
                            }
                            counter = counter - 1;
                        }
                    }
                    for (int j = 0; j < splitDescs.size(); j++) {
                        lore.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + splitDescs.get(j));
                    }
                    meta.setLore(lore);
                    menu_items[i].setItemMeta(meta);
                    gui.setItem(indices[i], menu_items[i]);
                }
                ItemStack g_token = new ItemStack(Material.DIAMOND);
                ItemStack back_button = new ItemStack(Material.ARROW);
                ItemStack[] menu_items_2 = {g_token,back_button};
                String[] labels_2 = passivePerksMap.get(skillName);
                String[] lores_line1_2 = {"Total: 0",""};
                String[] lores_line2_2 = passiveDescriptionsMap.get(skillName);

                //Total global Tokens
                lores_line1_2[0] = ChatColor.BLUE + "Total: " + ChatColor.GOLD + String.valueOf(tokens_G);
                if (tokens_G > 1 && tokens_G < 64){
                    g_token.setAmount(tokens_G);
                }
                else if (tokens_G >= 64){
                    g_token.setAmount(64);
                }
                Integer[] indices_2 = {0,45};
                for (int i = 0; i < labels_2.length; i++) {
                    ArrayList<String> splitDescs = new ArrayList<>();
                    ItemMeta meta = menu_items_2[i].getItemMeta();
                    meta.setDisplayName(ChatColor.BOLD + labels_2[i]);
                    ArrayList<String> lore = new ArrayList<>();
                    splitDescs.add(lores_line2_2[i]);
                    int iter = 0;
                    while (splitDescs.get(splitDescs.size()-1).length() > 30) {
                        int lastIndex = splitDescs.size()-1;
                        boolean foundSpace = false;
                        int counter = 30;
                        while (foundSpace == false && counter > 0){
                            if (splitDescs.get(lastIndex).charAt(counter) == ' ') {
                                splitDescs.add(splitDescs.get(lastIndex).substring(0, counter));
                                splitDescs.add(splitDescs.get(lastIndex).substring(counter+1));
                                splitDescs.remove(iter);
                                iter += 1;
                                foundSpace = true;
                                if (iter > 6) {
                                    break;
                                }
                            }
                            counter = counter - 1;
                        }
                    }
                    lore.add(lores_line1_2[i]);
                    for (int j = 0; j < splitDescs.size(); j++) {
                        lore.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + splitDescs.get(j));
                    }
                    meta.setLore(lore);
                    menu_items_2[i].setItemMeta(meta);
                    gui.setItem(indices_2[i], menu_items_2[i]);
                }
                //Connectors
                ItemStack connector = new ItemStack(Material.GLASS_PANE);
                ItemMeta connectorMeta = connector.getItemMeta();
                connectorMeta.setDisplayName("");
                connector.setItemMeta(connectorMeta);
                Integer[] indices_3 = {2,4,7,8,13,15,17,20,22,23,25,31,33,35,38,40,43,44};
                for (int i = 0; i < indices_3.length; i++) {
                    gui.setItem(indices_3[i], connector);
                }
                //Put the items in the inventory
                p.openInventory(gui);


            }

            else {
                System.out.println("You need to be a player to cast this command");
            }
        }

        return true;
    }
}
