package mc.carlton.freerpg.commands;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.CustomRecipe;
import mc.carlton.freerpg.gameTools.LanguageSelector;
import mc.carlton.freerpg.gameTools.PsuedoEnchanting;
import mc.carlton.freerpg.globalVariables.CraftingRecipes;
import mc.carlton.freerpg.globalVariables.ItemGroups;
import mc.carlton.freerpg.globalVariables.StringsAndOtherData;
import mc.carlton.freerpg.playerInfo.*;
import mc.carlton.freerpg.serverFileManagement.PeriodicSaving;
import mc.carlton.freerpg.serverFileManagement.PlayerStatsFilePreparation;
import mc.carlton.freerpg.serverInfo.ConfigLoad;
import mc.carlton.freerpg.utilities.UtilityMethods;
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
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FrpgCommands implements CommandExecutor {

    public boolean togglePerk(String id,Player p,String[] args) {
        String permission;
        String langID;
        String skillName;
        int skillIndex;
        int globalIndex;
        switch (id) {
            case "flintToggle":
                permission = "toggleFlint";
                langID = "diggingPerkTitle4";
                skillName = "digging";
                skillIndex = 11;
                globalIndex = 12;
                break;
            case "flamePickToggle":
                permission = "toggleFlamePick";
                langID = "smeltingPerkTitle2";
                skillName = "smelting";
                skillIndex = 13;
                globalIndex = 13;
                break;
            case "speedToggle":
                permission = "toggleSpeed";
                langID = "agilityPerkTitle2";
                skillName = "agility";
                skillIndex = 13;
                globalIndex = 14;
                break;
            case "potionToggle":
                permission = "togglePotion";
                langID = "alchemyPerkTitle2";
                skillName = "alchemy";
                skillIndex = 13;
                globalIndex = 15;
                break;
            case "grappleToggle":
                permission = "toggleGrapple";
                langID = "fishingPerkTitle4";
                skillName = "fishing";
                skillIndex = 11;
                globalIndex = 16;
                break;
            case "hotRodToggle":
                permission = "toggleHotRod";
                langID = "fishingPerkTitle5";
                skillName = "fishing";
                skillIndex = 12;
                globalIndex = 17;
                break;
            case "veinMinerToggle":
                permission = "toggleVeinMiner";
                langID = "miningPerkTitle4";
                skillName = "mining";
                skillIndex = 11;
                globalIndex = 18;
                break;
            case "megaDigToggle":
                permission = "toggleMegaDig";
                langID = "diggingPerkTitle6";
                skillName = "digging";
                skillIndex = 13;
                globalIndex = 19;
                break;
            case "leafBlowerToggle":
                permission = "toggleLeafBlower";
                langID = "woodcuttingPerkTitle5";
                skillName = "woodcutting";
                skillIndex = 12;
                globalIndex = 26;
                break;
            case "holyAxeToggle":
                permission = "toggleHolyAxe";
                langID = "axeMasteryPerkTitle1";
                skillName = "axeMastery";
                skillIndex = 8;
                globalIndex = 27;
                break;
            default:
                return true;
        }



        LanguageSelector lang = new LanguageSelector(p);
        if (!p.hasPermission("freeRPG."+permission)) {
            p.sendMessage(ChatColor.RED+lang.getString("noPermission"));
            return true;
        }
        PlayerStats pStatClass = new PlayerStats(p);
        Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        int level = (int)pStat.get(skillName).get(skillIndex);
        if ( level > 0) {
            if (args.length == 1) {
                int toggle = (int) pStat.get("global").get(globalIndex);
                if (toggle > 0) {
                    p.sendMessage(ChatColor.RED + lang.getString(langID) + ": " + lang.getString("off0"));
                    pStat.get("global").set(globalIndex,0);
                }
                else {
                    p.sendMessage(ChatColor.GREEN + lang.getString(langID) + ": " + lang.getString("on0"));
                    pStat.get("global").set(globalIndex,1);
                }
                statAll.put(p.getUniqueId(), pStat);
                pStatClass.setData(statAll);
            } else if (args.length == 2) {
                if (args[1].equalsIgnoreCase("off")) {
                    p.sendMessage(ChatColor.RED + lang.getString(langID) + ": " + lang.getString("off0"));
                    pStat.get("global").set(globalIndex,0);
                    statAll.put(p.getUniqueId(), pStat);
                    pStatClass.setData(statAll);
                }
                else if (args[1].equalsIgnoreCase("on")) {
                    p.sendMessage(ChatColor.GREEN + lang.getString(langID) + ": " + lang.getString("on0"));
                    pStat.get("global").set(globalIndex,1);
                    statAll.put(p.getUniqueId(), pStat);
                    pStatClass.setData(statAll);
                }
                else {
                    p.sendMessage(ChatColor.RED + lang.getString("improperArguments") + " /frpg "+id);
                }
            }
            else {
                p.sendMessage(ChatColor.RED + lang.getString("improperArguments") + " /frpg "+id);
            }
        }
        else {
            p.sendMessage(ChatColor.RED + lang.getString("unlockToggle") + " " +ChatColor.BOLD + lang.getString(langID));
        }
        return true;
    }
    public void togglePerkSetGuiItem(String id, Player p, Inventory gui) {
        String langID;
        int globalIndex;
        int guiIndex = 28;
        boolean enchanted = false;
        Material icon;
        switch (id) {
            case "flintToggle":
                langID = "diggingPerkTitle4";
                globalIndex = 12;
                icon = Material.FLINT;
                break;
            case "flamePickToggle":
                langID = "smeltingPerkTitle2";
                globalIndex = 13;
                icon = Material.BLAZE_POWDER;
                break;
            case "speedToggle":
                langID = "agilityPerkTitle2";
                globalIndex = 14;
                icon = Material.LEATHER_BOOTS;
                break;
            case "potionToggle":
                langID = "alchemyPerkTitle2";
                globalIndex = 15;
                icon = Material.POTION;
                break;
            case "grappleToggle":
                langID = "fishingPerkTitle4";
                globalIndex = 16;
                icon = Material.LEAD;
                break;
            case "hotRodToggle":
                langID = "fishingPerkTitle5";
                globalIndex = 17;
                guiIndex = 29;
                icon = Material.FISHING_ROD;
                break;
            case "veinMinerToggle":
                langID = "miningPerkTitle4";
                globalIndex = 18;
                icon = Material.DIAMOND_PICKAXE;
                break;
            case "megaDigToggle":
                langID = "diggingPerkTitle6";
                globalIndex = 19;
                guiIndex = 29;
                icon = Material.DIAMOND_SHOVEL;
                break;
            case "leafBlowerToggle":
                langID = "woodcuttingPerkTitle5";
                globalIndex = 26;
                icon = Material.OAK_LEAVES;
                break;
            case "holyAxeToggle":
                langID = "axeMasteryPerkTitle1";
                globalIndex = 27;
                icon = Material.DIAMOND_AXE;
                enchanted = true;
                break;
            default:
                return;
        }


        PlayerStats pStatClass = new PlayerStats(p);
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        //item
        LanguageSelector lang = new LanguageSelector(p);
        ItemStack item = new ItemStack(icon);
        if (enchanted) {
            item.addUnsafeEnchantment(Enchantment.LOYALTY,1);
        }
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BOLD + lang.getString(langID) + " " + lang.getString("toggle"));
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemMeta);
        gui.setItem(guiIndex,item);

        ItemStack itemToggle = new ItemStack(Material.LIME_DYE);
        ItemMeta itemToggleMeta = itemToggle.getItemMeta();
        if ( (int) pStat.get("global").get(globalIndex) > 0) {
            itemToggleMeta.setDisplayName(ChatColor.BOLD + ChatColor.GREEN.toString() + lang.getString("on0"));
        }
        else {
            itemToggle.setType(Material.GRAY_DYE);
            itemToggleMeta.setDisplayName(ChatColor.BOLD + ChatColor.RED.toString() + lang.getString("off0"));
        }
        itemToggle.setItemMeta(itemToggleMeta);
        gui.setItem(guiIndex+9,itemToggle);
    }
    public boolean isTargetOnline(Player target,CommandSender sender) {
        if (target == null) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                LanguageSelector lang = new LanguageSelector(p);
                p.sendMessage(ChatColor.RED + lang.getString("playerOffline"));
            }
            else {
                System.out.println("Player not online");
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);


        // /frpg aka MainGUI
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                LanguageSelector lang = new LanguageSelector(p);
                if (p.isSleeping()) {
                    p.sendMessage(ChatColor.RED + lang.getString("bedGUI"));
                    return true;
                }
                if (!p.hasPermission("freeRPG.mainGUI")) {
                    p.sendMessage(ChatColor.RED + lang.getString("noPermission"));
                    return true;
                }
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
                ItemStack info = new ItemStack(Material.MAP);
                ItemStack configuration = new ItemStack(Material.REDSTONE);

                ItemStack[] menu_items = {global,digging,woodcutting,mining,farming,fishing,archery,beastMastery,swords,armor,axes,repair,agility,alchemy,smelting,enchanting,info,configuration};
                String[] labels = {lang.getString("global"),lang.getString("digging"),lang.getString("woodcutting"),lang.getString("mining"),lang.getString("farming"),lang.getString("fishing"),lang.getString("archery"),lang.getString("beastMastery"),lang.getString("swordsmanship"),lang.getString("defense"),lang.getString("axeMastery"),lang.getString("repair"),lang.getString("agility"),lang.getString("alchemy"),lang.getString("smelting"),lang.getString("enchanting"),lang.getString("information"),lang.getString("configuration")};
                String[] labels0 = {"global","digging", "woodcutting", "mining", "farming", "fishing", "archery", "beastMastery", "swordsmanship", "defense", "axeMastery", "repair", "agility", "alchemy", "smelting", "enchanting"};
                Integer[] indices = {4,11,12,13,14,15,20,21,22,23,24,29,30,31,32,33,36,44};
                PlayerStats pStatClass = new PlayerStats(p);
                Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();

                //Set item positions and lore for all items in the GUI
                Leaderboards leaderboards = new Leaderboards();
                for (int i = 0; i < menu_items.length; i++) {
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
                        int totalPlayers = leaderboards.getLeaderboardSize(labels0[i]);
                        int rank = leaderboards.getLeaderboardPosition(p,labels0[i]);
                        lore_skills.add(ChatColor.GRAY + lang.getString("level") + ": " + ChatColor.BLUE + String.format("%,d",level));
                        lore_skills.add(ChatColor.GRAY + lang.getString("experience") + ": " + ChatColor.BLUE + String.format("%,d",EXP));
                        ChangeStats getEXP = new ChangeStats(p);
                        int maxLevel = getEXP.getMaxLevel(labels0[i]);
                        String EXPtoNextString;
                        if (level < maxLevel) {
                            int nextEXP = getEXP.getEXPfromLevel(level+1);
                            int EXPtoNext = nextEXP - EXP;
                            EXPtoNextString = String.format("%,d",EXPtoNext);
                        }
                        else {
                            EXPtoNextString = "N/A";
                        }
                        lore_skills.add(ChatColor.GRAY + lang.getString("expToLevel") + ": " + ChatColor.GREEN + EXPtoNextString);
                        lore_skills.add(ChatColor.GRAY + lang.getString("rank") +": " +ChatColor.WHITE+ChatColor.BOLD.toString() + "" + String.format("%,d",rank) + ChatColor.RESET + ChatColor.GRAY +" " + lang.getString("outOf")+ " "  + ChatColor.WHITE + String.format("%,d",totalPlayers));


                    }
                    else if (i==0) {
                        int globalTokens = (int) pStat.get(labels0[i]).get(1);
                        int totalLevel = pStat.get("global").get(0).intValue();
                        int totalExperience = (int) pStat.get("global").get(29);
                        int totalPlayers = leaderboards.getLeaderboardSize("global");
                        int rank = leaderboards.getLeaderboardPosition(p,"global");
                        lore_skills.add(ChatColor.GRAY + lang.getString("total") + " " + lang.getString("level") +": " + ChatColor.GOLD + String.format("%,d",totalLevel));
                        lore_skills.add(ChatColor.GRAY + lang.getString("total") + " " + lang.getString("exp") +": " + ChatColor.GOLD + String.format("%,d",totalExperience));
                        lore_skills.add(ChatColor.GRAY + lang.getString("rank") +": " + ChatColor.WHITE+ChatColor.BOLD.toString()  + "" + String.format("%,d",rank) + ChatColor.RESET + ChatColor.GRAY +" " + lang.getString("outOf")+ " "  + ChatColor.WHITE + String.format("%,d",totalPlayers));
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
                        PlayerStats timeStats = new PlayerStats(p);
                        String playTimeString = timeStats.getPlayerPlayTimeString();
                        double personalMultiplier = (double) pStat.get("global").get(23);
                        int souls = (int) pStat.get("global").get(20);
                        String soulsString = UtilityMethods.capitalizeString(lang.getString("souls"));
                        ArrayList<String> lore = new ArrayList<>();
                        lore.add(ChatColor.GRAY + lang.getString("totalPlayTime")+ ": " + ChatColor.GOLD + playTimeString);
                        lore.add(ChatColor.GRAY + lang.getString("personalMultiplier")+": " + ChatColor.GOLD + String.valueOf(personalMultiplier)+"x");
                        lore.add(ChatColor.GRAY + lang.getString("globalPassiveTitle0")+ ": " + ChatColor.GOLD + String.format("%,d",gTokens));
                        lore.add(ChatColor.GRAY + lang.getString("diggingPassiveTitle2")+": " + ChatColor.GOLD + String.format("%,d",totTokens_S));
                        lore.add(ChatColor.GRAY + lang.getString("diggingPassiveTitle0")+": " + ChatColor.GOLD + String.format("%,d",totTokens_P));
                        lore.add(ChatColor.GRAY + soulsString +": " + ChatColor.GOLD + String.format("%,d",souls));
                        meta.setLore(lore);
                    }
                    else if (indices[i] == 44) {
                        ArrayList<String> lore = new ArrayList<>();
                        lore.add(ChatColor.GRAY + lang.getString("clickForOptions"));
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
        else if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
            if (args.length >= 1) {
                int totalPages = 4;
                int page = 1;
                if (args.length == 2) {
                    try {
                        page = Integer.valueOf(args[1]);
                    }
                    catch (NumberFormatException e) {
                        if (sender instanceof Player) {
                            Player p = (Player) sender;
                            LanguageSelector lang = new LanguageSelector(p);
                            p.sendMessage(ChatColor.RED +lang.getString("improperArguments") +" /frpg help ["+lang.getString("page")+"]");
                        } else {
                            System.out.println("Improper Arguments, try /frpg help [(Optional) page]");
                        }
                        return true;
                    }
                }
                if (page > totalPages || page < 1) {
                    page = 1;
                }
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    LanguageSelector lang = new LanguageSelector(p);
                    if (!p.hasPermission("freeRPG.help")) {
                        p.sendMessage(ChatColor.RED + lang.getString("noPermission"));
                        return true;
                    }
                        p.sendMessage(ChatColor.RED + "------| " + ChatColor.GREEN + ChatColor.BOLD.toString() + " Help" +
                                ChatColor.RESET + ChatColor.GREEN.toString() + " "+lang.getString("page")+" [" + Integer.toString(page) + "/" + Integer.toString(totalPages) + "]" +
                                ChatColor.RED.toString() + " |-----");
                        switch (page) {
                            case 1:
                                p.sendMessage(ChatColor.GOLD  + "/frpg" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc0"));
                                p.sendMessage(ChatColor.GOLD  + "/frpg skillTreeGUI [" +lang.getString("skillName")+ "]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc1"));
                                p.sendMessage(ChatColor.GOLD  + "/frpg configurationGUI" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc2"));
                                p.sendMessage(ChatColor.GOLD  + "/frpg giveEXP ["+lang.getString("playerName")+"] ["+lang.getString("skillName")+"] [amount]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc3"));
                                p.sendMessage(ChatColor.GOLD  + "/frpg setLevel ["+lang.getString("playerName")+"] ["+lang.getString("skillName")+"] ["+lang.getString("level")+"]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc4"));
                                p.sendMessage(ChatColor.GOLD  + "/frpg statReset ["+lang.getString("playerName")+"] ["+lang.getString("skillName")+"]" + ChatColor.RESET + ChatColor.GRAY.toString() +" - " +
                                        ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc5"));
                                p.sendMessage(ChatColor.GOLD  + "/frpg statLeaders ["+lang.getString("skillName")+"] ["+lang.getString("page")+"]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc6"));
                                p.sendMessage(ChatColor.GOLD  + "/frpg info" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc7"));
                                break;
                            case 2:
                                p.sendMessage(ChatColor.GOLD  + "/frpg flintToggle ["+lang.getString("onOrOff")+"]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + lang.getString("manuallyToggles") + " " + lang.getString("diggingPerkTitle4"));
                                p.sendMessage(ChatColor.GOLD  + "/frpg speedToggle ["+lang.getString("onOrOff")+"]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + lang.getString("manuallyToggles") + " " + lang.getString("agilityPerkTitle2"));
                                p.sendMessage(ChatColor.GOLD  + "/frpg potionToggle ["+lang.getString("onOrOff")+"]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + lang.getString("manuallyToggles") + " " + lang.getString("alchemyPerkTitle2"));
                                p.sendMessage(ChatColor.GOLD  + "/frpg flamePickToggle ["+lang.getString("onOrOff")+"]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + lang.getString("manuallyToggles") + " " + lang.getString("smeltingPerkTitle2"));
                                p.sendMessage(ChatColor.GOLD  + "/frpg grappleToggle ["+lang.getString("onOrOff")+"]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + lang.getString("manuallyToggles") + " " + lang.getString("fishingPerkTitle4"));
                                p.sendMessage(ChatColor.GOLD  + "/frpg hotRodToggle ["+lang.getString("onOrOff")+"]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + lang.getString("manuallyToggles") + " " + lang.getString("fishingPerkTitle5"));
                                p.sendMessage(ChatColor.GOLD  + "/frpg veinMinerToggle ["+lang.getString("onOrOff")+"" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + lang.getString("manuallyToggles") + " " + lang.getString("miningPerkTitle4"));
                                p.sendMessage(ChatColor.GOLD  + "/frpg megaDigToggle ["+lang.getString("onOrOff")+"]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + lang.getString("manuallyToggles") + " " + lang.getString("diggingPerkTitle6"));
                                break;
                            case 3:
                                p.sendMessage(ChatColor.GOLD  + "/frpg leafBlowerToggle ["+lang.getString("onOrOff")+"]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + lang.getString("manuallyToggles") + " " + lang.getString("woodcuttingPerkTitle5"));
                                p.sendMessage(ChatColor.GOLD  + "/frpg holyAxeToggle ["+lang.getString("onOrOff")+"]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + lang.getString("manuallyToggles") + " " + lang.getString("axeMasteryPerkTitle1"));
                                p.sendMessage(ChatColor.GOLD  + "/frpg enchantItem ["+lang.getString("level")+"]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc8"));
                                p.sendMessage(ChatColor.GOLD  + "/frpg setSouls" + " ["+lang.getString("playerName")+"]" + " ["+lang.getString("amount")+"]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc9"));
                                p.sendMessage(ChatColor.GOLD  + "/frpg setTokens" + " ["+lang.getString("playerName")+"]" + " ["+lang.getString("skillName")+"]" + " [skill/passive]" +" ["+lang.getString("amount")+"]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc10"));
                                p.sendMessage(ChatColor.GOLD  + "/frpg setTokens" + " ["+lang.getString("playerName")+"]" + " global" + " ["+lang.getString("amount")+"]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc11"));
                                p.sendMessage(ChatColor.GOLD  + "/frpg saveStats ["+lang.getString("playerName")+"]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                    ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc12"));
                                p.sendMessage(ChatColor.GOLD  + "/frpg setMultiplier ["+lang.getString("playerName")+"] " + "[" + lang.getString("amount")+"]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc13"));
                                break;
                            case 4:
                                p.sendMessage(ChatColor.GOLD  + "/frpg resetCooldown ["+lang.getString("playerName")+"] ["+lang.getString("skillName")+"]" + ChatColor.RESET + ChatColor.GRAY.toString() +" - " +
                                        ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc14"));
                                p.sendMessage(ChatColor.GOLD  + "/frpg statLookup ["+lang.getString("playerName")+"]" + ChatColor.RESET + ChatColor.GRAY.toString() +" - " +
                                        ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc15"));
                                p.sendMessage(ChatColor.GOLD  + "/frpg changeMultiplier ["+lang.getString("playerName")+"] " + "[" + lang.getString("expIncrease")+"]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                        ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc16"));
                            default:
                                break;
                        }
                } else {
                    System.out.println("You must be a player to use this command");
                }
            } else {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    LanguageSelector lang = new LanguageSelector(p);
                    p.sendMessage(ChatColor.RED +lang.getString("improperArguments") +" /frpg help ["+lang.getString("page")+"]");
                } else {
                    System.out.println("Improper Arguments, try /frpg help [(Optional) page]");
                }
            }
        }

        //saveStats
        else if (args[0].equalsIgnoreCase("saveStats") || args[0].equalsIgnoreCase("statSave")) {
            if (args.length == 1) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (p.hasPermission("saveStats")) {
                        PeriodicSaving saveAll = new PeriodicSaving();
                        saveAll.saveAllStats(false);
                    }
                    else {
                        LanguageSelector lang = new LanguageSelector(p);
                        p.sendMessage(ChatColor.RED+lang.getString("noPermission"));

                    }
                }
                else {
                    PeriodicSaving saveAll = new PeriodicSaving();
                    saveAll.saveAllStats(false);
                }
            }
            else if (args.length == 2) {
                String playerName = args[1];
                Player target = plugin.getServer().getPlayer(playerName);
                boolean targetOnline = isTargetOnline(target,sender);
                if (!targetOnline) {
                    return true;
                }
                else {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (p.hasPermission("saveStats")) {
                            PlayerStatsLoadIn saveStats = new PlayerStatsLoadIn(target);
                            try {
                                saveStats.setPlayerStatsMap();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            LanguageSelector lang = new LanguageSelector(p);
                            p.sendMessage(ChatColor.RED+lang.getString("noPermission"));

                        }
                    }
                else {
                        PlayerStatsLoadIn saveStats = new PlayerStatsLoadIn(target);
                        try {
                            saveStats.setPlayerStatsMap();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            else {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    LanguageSelector lang = new LanguageSelector(p);
                    p.sendMessage(ChatColor.RED +lang.getString("improperArguments") +" /frpg saveStats");
                }
                else {
                    System.out.println("Improper arguments, try /frpg saveStats");
                }

            }
        }

        //Tutorial/info
        else if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("use")) {
            if (args.length == 1) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    LanguageSelector lang = new LanguageSelector(p);
                    if (!p.hasPermission("freeRPG.info")) {
                        p.sendMessage(ChatColor.RED + lang.getString("noPermission"));
                        return true;
                    }
                    p.sendMessage(lang.getString("informationURL")+": " + ChatColor.AQUA + ChatColor.UNDERLINE.toString() + "shorturl.at/ptCDX" +
                            ChatColor.RESET + ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "<-- " +lang.getString("click"));
                } else {
                    System.out.println("You must be a player to use this command");
                }
            } else {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    LanguageSelector lang = new LanguageSelector(p);
                    p.sendMessage(ChatColor.RED +lang.getString("improperArguments") +" /frpg info");
                } else {
                    System.out.println("You must be a player to use this command");
                }
            }
        }

        //Enchant Item Command
        else if (args[0].equalsIgnoreCase("enchantItem") || args[0].equalsIgnoreCase("enchant")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("freeRPG.enchantItem")) {
                    if (args.length == 2) {
                        int level = 0;
                        try {
                            level = Integer.valueOf(args[1]);
                        }
                        catch (NumberFormatException e) {
                            LanguageSelector lang = new LanguageSelector(p);
                            p.sendMessage(ChatColor.RED +lang.getString("improperArguments") +" /frpg enchantItem ["+lang.getString("level")+"]");
                            return true;
                        }
                        if (level < 40) {
                            ItemStack item = p.getInventory().getItemInMainHand();
                            PsuedoEnchanting enchant = new PsuedoEnchanting();
                            item = enchant.enchantItem(item, level, false);
                            p.getInventory().setItemInMainHand(item);
                        } else {
                            LanguageSelector lang = new LanguageSelector(p);
                            p.sendMessage(ChatColor.RED +lang.getString("levelArgument"));
                        }
                    } else {
                        LanguageSelector lang = new LanguageSelector(p);
                        p.sendMessage(ChatColor.RED +lang.getString("improperArguments") +" /frpg enchantItem ["+lang.getString("level")+"]");
                    }
                } else {
                    LanguageSelector lang = new LanguageSelector(p);
                    p.sendMessage(ChatColor.RED +lang.getString("noPermission"));
                }
            } else {
                System.out.println("You need to be a player to cast this command");
            }
        }

        //Leader Board
        else if (args[0].equalsIgnoreCase("leaderboard") || args[0].equalsIgnoreCase("statLeaders")) {
            if (args.length >= 2) {
                int page = 1;
                if (args.length == 3) {
                    try {
                        page = Integer.valueOf(args[2]);;
                    }
                    catch (NumberFormatException e) {
                        if (sender instanceof Player) {
                            Player p = (Player) sender;
                            LanguageSelector lang = new LanguageSelector(p);
                            p.sendMessage(ChatColor.RED +lang.getString("improperArguments") +" /frpg statLeaders [skillName] [(Optional) page]");
                        } else {
                            System.out.println("Improper Arguments, try /frpg statLeaders [skillName] [(Optional) page]");
                        }
                        return true;
                    }
                }
                String[] labels_0 = {"digging", "woodcutting", "mining", "farming", "fishing", "archery", "beastMastery", "swordsmanship", "defense", "axeMastery", "repair", "agility", "alchemy", "smelting", "enchanting","global","playTime"};
                List<String> labels_arr = Arrays.asList(labels_0);
                String skillName = UtilityMethods.convertStringToListCasing(labels_arr,args[1]);
                if (labels_arr.contains(skillName)) {
                    Leaderboards getStats = new Leaderboards();
                    if (!getStats.isLeaderboardsLoaded()) {
                        if (sender instanceof Player) {
                            Player p = (Player) sender;
                            LanguageSelector lang = new LanguageSelector(p);
                            p.sendMessage(ChatColor.RED +"Leaderboard is not yet loaded, try again soon");
                        } else {
                            System.out.println("Leaderboard is not yet loaded, try again soon");
                        }
                    }
                    //Sorts and Updates leaderboard (if dynamic updates are on)
                    getStats.sortLeaderBoard(skillName);

                    ArrayList<PlayerLeaderboardStat> sortedStats = getStats.getLeaderboard(skillName);
                    int totalPlayers = sortedStats.size();
                    int totalPages = (int) Math.ceil(totalPlayers / 10.0);
                    if (page > totalPages) {
                        page = 1;
                    }
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (p.hasPermission("freeRPG.leaderboard")) {
                            LanguageSelector lang = new LanguageSelector(p);
                            String[] titles_0 = {lang.getString("digging"),lang.getString("woodcutting"),lang.getString("mining"),lang.getString("farming"),
                                    lang.getString("fishing"),lang.getString("archery"),lang.getString("beastMastery"),lang.getString("swordsmanship"),
                                    lang.getString("defense"),lang.getString("axeMastery"),lang.getString("repair"),lang.getString("agility"),lang.getString("alchemy"),
                                    lang.getString("smelting"),lang.getString("enchanting"),lang.getString("global"),lang.getString("totalPlayTime")};
                            String skillTitle = titles_0[labels_arr.indexOf(skillName)];
                            p.sendMessage(ChatColor.AQUA + "------| " + ChatColor.WHITE + ChatColor.BOLD.toString() + skillTitle + " "+lang.getString("leaderboard")+"" +
                                    ChatColor.RESET + ChatColor.WHITE.toString() + " "+lang.getString("page")+" [" + Integer.toString(page) + "/" + Integer.toString(totalPages) + "]" +
                                    ChatColor.AQUA.toString() + " |-----");
                            for (int i = 10 * (page - 1); i < (int) Math.min(10 * page, totalPlayers); i++) {
                                PlayerLeaderboardStat info = sortedStats.get(i);
                                if (skillName.equalsIgnoreCase("global")) {
                                    p.sendMessage(ChatColor.WHITE + Integer.toString(i + 1) + ". " + ChatColor.AQUA + info.get_pName() + ChatColor.WHITE.toString() + " - " + ChatColor.WHITE + String.format("%,d", info.get_sortedStat()) + ChatColor.WHITE + " (" + ChatColor.GRAY + String.format("%,d", info.get_stat2()) + ChatColor.WHITE + ")");
                                }
                                else if (skillName.equalsIgnoreCase("playTime")){
                                    p.sendMessage(ChatColor.WHITE + Integer.toString(i + 1) + ". " + ChatColor.AQUA + info.get_pName() + ChatColor.WHITE.toString() + " - " + ChatColor.WHITE + info.get_playTimeString() );
                                }
                                else {
                                    p.sendMessage(ChatColor.WHITE + Integer.toString(i + 1) + ". " + ChatColor.AQUA + info.get_pName() + ChatColor.WHITE.toString() + " - " + ChatColor.WHITE + String.format("%,d", info.get_stat2()) + ChatColor.WHITE + " (" + ChatColor.GRAY + String.format("%,d", info.get_sortedStat()) + ChatColor.WHITE + ")");
                                }
                            }
                        } else {
                            LanguageSelector lang = new LanguageSelector(p);
                            p.sendMessage(ChatColor.RED + lang.getString("noPermission"));
                        }
                    } else {
                        String[] titles_0 = {"Digging", "Woodcutting", "Mining", "Farming", "Fishing", "Archery", "Beast Mastery", "Swordsmanship", "Defense", "Axe Mastery", "Repair", "Agility", "Alchemy", "Smelting", "Enchanting", "Global"};
                        String skillTitle = titles_0[labels_arr.indexOf(skillName)];
                        System.out.println("------| " + skillTitle + " Leaderboard" + " Page [" + Integer.toString(page) + "/" + Integer.toString(totalPages) + "]" + " |-----");
                        for (int i = 10 * (page - 1); i < (int) Math.min(10 * page, totalPlayers); i++) {
                            PlayerLeaderboardStat info = sortedStats.get(i);
                            if (skillName.equalsIgnoreCase("global")) {
                                System.out.println(Integer.toString(i + 1) + ". " + info.get_pName() + " - " + Integer.toString((int)info.get_sortedStat()) + " (" + info.get_stat2() + ")");
                            }
                            else if (skillName.equalsIgnoreCase("playTime")) {
                                System.out.println(Integer.toString(i + 1) + ". " + info.get_pName() + " - " + info.get_playTimeString());

                            }
                            else {
                                System.out.println(Integer.toString(i + 1) + ". " + info.get_pName() + " - " + Integer.toString((int)info.get_stat2()) + " (" + info.get_sortedStat() + ")");
                            }

                        }
                    }
                } else {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (p.hasPermission("freeRPG.leaderboard")) {
                            LanguageSelector lang = new LanguageSelector(p);
                            p.sendMessage(ChatColor.RED +lang.getString("improperArguments") +" /frpg statLeaders ["+lang.getString("skillName")+"] ["+lang.getString("page")+"]");
                        } else {
                            LanguageSelector lang = new LanguageSelector(p);
                            p.sendMessage(ChatColor.RED + lang.getString("noPermission"));
                        }
                    } else {
                        System.out.println("Improper Arguments, try /frpg statLeaders [skillName] [(Optional) page]");
                    }
                }
            } else {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (p.hasPermission("freeRPG.leaderboard")) {
                        LanguageSelector lang = new LanguageSelector(p);
                        p.sendMessage(ChatColor.RED +lang.getString("improperArguments") +" /frpg statLeaders ["+lang.getString("skillName")+"] ["+lang.getString("page")+"]");
                    } else {
                        LanguageSelector lang = new LanguageSelector(p);
                        p.sendMessage(ChatColor.RED + lang.getString("noPermission"));
                    }
                } else {
                    System.out.println("Improper Arguments, try /frpg statLeaders [skillName] [(Optional) page]");
                }
            }
        }

        //Stat Lookup
        else if (args[0].equalsIgnoreCase("statLookup") || args[0].equalsIgnoreCase("lookupStats") || args[0].equalsIgnoreCase("stats")) {
            //Permission Check
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (!p.hasPermission("freeRPG.statLookup")) {
                    LanguageSelector lang = new LanguageSelector(p);
                    p.sendMessage(ChatColor.RED + lang.getString("noPermission"));
                    return true;
                }
            }
            if (args.length == 1) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    p.performCommand("frpg statLookup " + p.getName());
                }
                else {
                    System.out.println("Improper Arguments, try /frpg statLookup [playerName]");
                }
            }
            else if (args.length == 2) {
                String playerName = args[1];
                Leaderboards leaderboards = new Leaderboards();
                if (!leaderboards.isPlayerOnLeaderboards(playerName)) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        LanguageSelector lang = new LanguageSelector(p);
                        p.sendMessage(ChatColor.RED + lang.getString("playerNotInLeaderboard"));
                    } else {
                        System.out.println("That player is not on any leaderboards");
                    }
                    return true;
                }
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    LanguageSelector lang = new LanguageSelector(p);
                    List<String> leaderboardNames = leaderboards.getLeaderboardNames();
                    p.sendMessage(ChatColor.AQUA + "--------| " + ChatColor.WHITE + ChatColor.BOLD.toString() + playerName + ChatColor.RESET + ChatColor.WHITE.toString() + " " + lang.getString("stats") +
                            ChatColor.AQUA.toString() + " |-------");
                    StringsAndOtherData stringsAndOtherData = new StringsAndOtherData();
                    for (String leaderboardName : leaderboardNames) {
                        int rank = leaderboards.getLeaderboardPosition(playerName, leaderboardName);
                        PlayerLeaderboardStat playerStat = leaderboards.getPlayerStat(playerName, leaderboardName);
                        String displayedStat;
                        String displayTitle;
                        String rankString = UtilityMethods.intToRankingString(rank);
                        if (rankString.equals("1st")) {
                            rankString = ChatColor.YELLOW + rankString;
                        } else if (rankString.equals("2nd")) {
                            rankString = ChatColor.WHITE + rankString;
                        } else if (rankString.equals("3rd")) {
                            rankString = ChatColor.GOLD + rankString;
                        } else {
                            rankString = ChatColor.GRAY + rankString;
                        }
                        if (leaderboardName.equalsIgnoreCase("playTime")) {
                            displayedStat = playerStat.get_playTimeString();
                            displayTitle = "Play Time";
                        } else if (leaderboardName.equalsIgnoreCase("global")) {
                            displayedStat = String.format("%,d", playerStat.get_sortedStat());
                            displayTitle = "Global Level";
                        } else {
                            displayedStat = String.format("%,d", playerStat.get_stat2());
                            displayTitle = stringsAndOtherData.camelCaseToTitle(leaderboardName);
                        }
                        p.sendMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + displayTitle + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                                ChatColor.WHITE + displayedStat + " (" + rankString + ChatColor.WHITE + ")");
                    }

                } else {
                    List<String> leaderboardNames = leaderboards.getLeaderboardNames();
                    System.out.println("--------| " + playerName + " stats |-------");
                    StringsAndOtherData stringsAndOtherData = new StringsAndOtherData();
                    for (String leaderboardName : leaderboardNames) {
                        int rank = leaderboards.getLeaderboardPosition(playerName, leaderboardName);
                        PlayerLeaderboardStat playerStat = leaderboards.getPlayerStat(playerName, leaderboardName);
                        String displayedStat;
                        String displayTitle;
                        String rankString = UtilityMethods.intToRankingString(rank);
                        if (leaderboardName.equalsIgnoreCase("playTime")) {
                            displayedStat = playerStat.get_playTimeString();
                            displayTitle = "Play Time";
                        } else if (leaderboardName.equalsIgnoreCase("global")) {
                            displayedStat = String.format("%,d", playerStat.get_sortedStat());
                            displayTitle = "Global Level";
                        } else {
                            displayedStat = String.format("%,d", playerStat.get_stat2());
                            displayTitle = stringsAndOtherData.camelCaseToTitle(leaderboardName);
                        }
                        System.out.println(displayTitle  + " - " + displayedStat + " (" + rankString +")");
                    }
                }
            }
            else {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    LanguageSelector lang = new LanguageSelector(p);
                    p.sendMessage(ChatColor.RED +lang.getString("improperArguments")+" /frpg resetCooldown ["+lang.getString("playerName")+"] ["+lang.getString("skillName")+"]");
                } else {
                    System.out.println("Improper Arguments, try /frpg statLookup [playerName]");
                }
            }
            return true;
        }

        //CooldownReset
        else if (args[0].equalsIgnoreCase("resetCooldown") || args[0].equalsIgnoreCase("cooldownReset")) {
            //Permission Check
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (!p.hasPermission("freeRPG.resetCooldown")) {
                    LanguageSelector lang = new LanguageSelector(p);
                    p.sendMessage(ChatColor.RED + lang.getString("noPermission"));
                    return true;
                }
            }
            //Argument length check
            if (args.length != 3) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    LanguageSelector lang = new LanguageSelector(p);
                    p.sendMessage(ChatColor.RED +lang.getString("improperArguments")+" /frpg resetCooldown ["+lang.getString("playerName")+"] ["+lang.getString("skillName")+"]");
                } else {
                    System.out.println("Improper Arguments, try /frpg resetCooldown [playerName] [skillName]");
                }
                return true;
            }
            //Target online check
            String playerName = args[1];
            Player target = plugin.getServer().getPlayer(playerName);
            boolean targetOnline = isTargetOnline(target,sender);
            if (!targetOnline) {
                return true;
            }

            //Skill name match check
            String[] labels_0 = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery"};
            List<String> labels = Arrays.asList(labels_0);
            String skillName = UtilityMethods.convertStringToListCasing(labels,args[2]);
            if (!labels.contains(skillName)) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    LanguageSelector lang = new LanguageSelector(p);
                    p.sendMessage(ChatColor.RED +lang.getString("improperArguments")+" /frpg resetCooldown ["+lang.getString("playerName")+"] ["+lang.getString("skillName")+"]");
                } else {
                    System.out.println("Improper Arguments, try /frpg resetCooldown [playerName] [skillName]");
                }
                return true;
            }

            AbilityTimers abilityTimers = new AbilityTimers(target);
            abilityTimers.setPlayerCooldownTime(skillName,0);

        }

        //createFakePlayers
        else if (args[0].equalsIgnoreCase("createFakePlayers") || args[0].equalsIgnoreCase("createFakeProfiles")) {
            //Permission Check
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (!p.hasPermission("freeRPG.createFakePlayers")) {
                    LanguageSelector lang = new LanguageSelector(p);
                    p.sendMessage(ChatColor.RED + lang.getString("noPermission"));
                    return true;
                }
            }
            //Argument length check
            if (args.length != 2) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    LanguageSelector lang = new LanguageSelector(p);
                    p.sendMessage(ChatColor.RED +lang.getString("improperArguments")+" /frpg createFakePlayers [#]");
                } else {
                    System.out.println("Improper Arguments, try /frpg createFakePlayers [#]");
                }
                return true;
            }

            int numPlayers = 0;
            try {
                numPlayers = Integer.valueOf(args[1]);
            }
            catch (NumberFormatException e) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    LanguageSelector lang = new LanguageSelector(p);
                    p.sendMessage(ChatColor.RED +lang.getString("improperArguments")+" /frpg createFakePlayers [#]");
                } else {
                    System.out.println("Improper Arguments, try /frpg createFakePlayers [#]");
                }
                return true;
            }

            PlayerStatsFilePreparation playerStatsFilePreparation = new PlayerStatsFilePreparation();
            Random rand = new Random();
            for (int i = 0; i < numPlayers; i++) {
                String fakeName = "FakePlayer" + rand.nextInt(100000);
                UUID fakeUUID = UUID.fromString("badf"+UUID.randomUUID().toString().substring(4)); //"badf" hexadecimal identifier for "bad files"
                playerStatsFilePreparation.preparePlayerFile(fakeName,fakeUUID,false);
            }

        }

        //removeFakePlayers
        else if (args[0].equalsIgnoreCase("deleteFakePlayers") || args[0].equalsIgnoreCase("deleteFakeProfiles") ||  args[0].equalsIgnoreCase("removeFakePlayers") ||  args[0].equalsIgnoreCase("removeFakeProfiles")) {
            //Permission Check
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (!p.hasPermission("freeRPG.createFakePlayers")) {
                    LanguageSelector lang = new LanguageSelector(p);
                    p.sendMessage(ChatColor.RED + lang.getString("noPermission"));
                    return true;
                }
            }
            //Argument length check
            if (args.length != 1) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    LanguageSelector lang = new LanguageSelector(p);
                    p.sendMessage(ChatColor.RED +lang.getString("improperArguments")+" /frpg deleteFakePlayers");
                } else {
                    System.out.println("Improper Arguments, try /frpg deleteFakePlayers");
                }
                return true;
            }

            File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("FreeRPG").getDataFolder(), File.separator + "PlayerDatabase");
            File[] allUsers = userdata.listFiles();
            for (File f : allUsers) {
                if (f.getName().substring(0,4).equalsIgnoreCase("badf")) {
                    f.delete();
                }
            }

        }

        //Save FRPG Data
        else if (args[0].equalsIgnoreCase("thoroughSave")) {
            //Permission Check
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (!p.hasPermission("freeRPG.saveStats")) {
                    LanguageSelector lang = new LanguageSelector(p);
                    p.sendMessage(ChatColor.RED + lang.getString("noPermission"));
                    return true;
                }
            }
            //Argument length check
            if (args.length != 1) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    LanguageSelector lang = new LanguageSelector(p);
                    p.sendMessage(ChatColor.RED +lang.getString("improperArguments")+" /frpg save");
                } else {
                    System.out.println("Improper Arguments, try /frpg save");
                }
                return true;
            }

            PeriodicSaving periodicSaving = new PeriodicSaving();
            new BukkitRunnable() {
                @Override
                public void run() {
                    periodicSaving.saveAllStats(true);
                }
            }.runTaskAsynchronously(plugin);

        }

        //Load in all players
        else if (args[0].equalsIgnoreCase("loadInAllPlayerFiles") || args[0].equalsIgnoreCase("loadInPlayerFiles")) {
            //Permission Check
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (!p.hasPermission("freeRPG.saveData")) {
                    LanguageSelector lang = new LanguageSelector(p);
                    p.sendMessage(ChatColor.RED + lang.getString("noPermission"));
                    return true;
                }
            }

            OfflinePlayerStatLoadIn offlinePlayerStatLoadIn = new OfflinePlayerStatLoadIn();
            offlinePlayerStatLoadIn.loadInAllOfflinePlayers();

        }

        //GiveEXP
        else if (args[0].equalsIgnoreCase("giveEXP") || args[0].equalsIgnoreCase("expGive")) {
            if (args.length == 4) {
                String playerName = args[1];
                Player target = plugin.getServer().getPlayer(playerName);
                boolean targetOnline = isTargetOnline(target,sender);
                if (!targetOnline) {
                    return true;
                }
                int exp = 0;
                try {
                    exp = Integer.valueOf(args[3]);
                }
                catch (NumberFormatException e) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        LanguageSelector lang = new LanguageSelector(p);
                        p.sendMessage(ChatColor.RED +lang.getString("improperArguments")+" /frpg giveEXP ["+lang.getString("playerName")+"] ["+lang.getString("skillName")+"] [exp]");
                    } else {
                        System.out.println("Improper Arguments, try /frpg giveEXP [playerName] [skillName] [exp]");
                    }
                    return true;
                }

                String[] labels_0 = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting"};
                List<String> labels_arr = Arrays.asList(labels_0);
                StringsAndOtherData stringsAndOtherData = new StringsAndOtherData();
                String skillName = UtilityMethods.convertStringToListCasing(labels_arr,args[2]);
                if (labels_arr.contains(skillName) && target.isOnline()) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        LanguageSelector lang = new LanguageSelector(p);
                        if (p.hasPermission("freeRPG.giveEXP")) {
                            ChangeStats increaseStats = new ChangeStats(target);
                            increaseStats.set_isCommand(true);
                            if (exp < 0) {
                                p.sendMessage(ChatColor.RED + lang.getString("onlyIncrease"));
                                return true;
                            }
                            increaseStats.changeEXP(skillName, exp);
                            increaseStats.setTotalLevel();
                            increaseStats.setTotalExperience();
                        } else {
                            p.sendMessage(ChatColor.RED + lang.getString("noPermission"));
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
                        increaseStats.setTotalExperience();
                    }
                }
                else {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (p.hasPermission("freeRPG.giveEXP")) {
                            LanguageSelector lang = new LanguageSelector(p);
                            p.sendMessage(ChatColor.RED +lang.getString("improperArguments")+" /frpg giveEXP ["+lang.getString("playerName")+"] ["+lang.getString("skillName")+"] [exp]");
                        } else {
                            LanguageSelector lang = new LanguageSelector(p);
                            p.sendMessage(ChatColor.RED + lang.getString("noPermission"));
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
                        LanguageSelector lang = new LanguageSelector(p);
                        p.sendMessage(ChatColor.RED +lang.getString("improperArguments")+" /frpg giveEXP ["+lang.getString("playerName")+"] ["+lang.getString("skillName")+"] [exp]");
                    } else {
                        LanguageSelector lang = new LanguageSelector(p);
                        p.sendMessage(ChatColor.RED + lang.getString("noPermission"));
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
                boolean targetOnline = isTargetOnline(target,sender);
                if (!targetOnline) {
                    return true;
                }
                int level = 0;
                try {
                    level = Integer.valueOf(args[3]);
                }
                catch (NumberFormatException e) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        LanguageSelector lang = new LanguageSelector(p);
                        p.sendMessage(ChatColor.RED +lang.getString("improperArguments") +" /frpg setLevel ["+ lang.getString("playerName")+"] ["+ lang.getString("skillName")+"] ["+ lang.getString("level")+"]");
                    } else {
                        System.out.println("Improper Arguments, try /frpg setLevel [playerName] [skillName] [level]");
                    }
                    return true;
                }
                String[] labels_0 = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting"};
                List<String> labels_arr = Arrays.asList(labels_0);
                StringsAndOtherData stringsAndOtherData = new StringsAndOtherData();
                String skillName = UtilityMethods.convertStringToListCasing(labels_arr,args[2]);
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
                                LanguageSelector lang = new LanguageSelector(p);
                                p.sendMessage(ChatColor.RED + lang.getString("onlyIncrease"));
                                return true;
                            }
                            increaseStats.changeEXP(skillName,exp-currentExp+1);
                            increaseStats.setTotalLevel();
                            increaseStats.setTotalExperience();
                        } else {
                            LanguageSelector lang = new LanguageSelector(p);
                            p.sendMessage(ChatColor.RED + lang.getString("noPermission"));
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
                        increaseStats.setTotalExperience();
                    }
                }
                else {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (p.hasPermission("freeRPG.setLevel")) {
                            LanguageSelector lang = new LanguageSelector(p);
                            p.sendMessage(ChatColor.RED +lang.getString("improperArguments") +" /frpg setLevel ["+lang.getString("playerName")+"] ["+lang.getString("skillName")+"] ["+lang.getString("level")+"]");
                        } else {
                            LanguageSelector lang = new LanguageSelector(p);
                            p.sendMessage(ChatColor.RED+lang.getString("noPermission"));
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
                        LanguageSelector lang = new LanguageSelector(p);
                        p.sendMessage(ChatColor.RED +lang.getString("improperArguments") +" /frpg setLevel ["+lang.getString("playerName")+"] ["+lang.getString("skillName")+"] ["+lang.getString("level")+"]");
                    } else {
                        LanguageSelector lang = new LanguageSelector(p);
                        p.sendMessage(ChatColor.RED+lang.getString("noPermission"));
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
                boolean targetOnline = isTargetOnline(target,sender);
                if (!targetOnline) {
                    return true;
                }
                String[] labels_0 = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting"};
                List<String> labels_arr = Arrays.asList(labels_0);
                StringsAndOtherData stringsAndOtherData = new StringsAndOtherData();
                String skillName = UtilityMethods.convertStringToListCasing(labels_arr,args[2]);
                if (labels_arr.contains(skillName) && target.isOnline()) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (p.hasPermission("freeRPG.statReset")) {
                            ChangeStats increaseStats = new ChangeStats(target);
                            increaseStats.resetStat(skillName);

                        } else {
                            LanguageSelector lang = new LanguageSelector(p);
                            p.sendMessage(ChatColor.RED+lang.getString("noPermission"));
                        }
                    } else {
                        ChangeStats increaseStats = new ChangeStats(target);
                        increaseStats.resetStat(skillName);
                    }
                }
                else {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (p.hasPermission("freeRPG.statReset")) {
                            LanguageSelector lang = new LanguageSelector(p);
                            p.sendMessage(ChatColor.RED +lang.getString("improperArguments") +" /frpg statReset ["+lang.getString("playerName")+"] ["+lang.getString("skillName")+"]");
                        } else {
                            LanguageSelector lang = new LanguageSelector(p);
                            p.sendMessage(ChatColor.RED+lang.getString("noPermission"));
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
                        LanguageSelector lang = new LanguageSelector(p);
                        p.sendMessage(ChatColor.RED +lang.getString("improperArguments") +" /frpg statReset ["+lang.getString("playerName")+"] ["+lang.getString("skillName")+"]");
                    } else {
                        LanguageSelector lang = new LanguageSelector(p);
                        p.sendMessage(ChatColor.RED+lang.getString("noPermission"));
                    }
                }
                else {
                    System.out.println("Improper Arguments, try /frpg statReset [playername] [statName]");
                }
            }
        }

        //setSouls
        else if (args[0].equalsIgnoreCase("setSouls") || args[0].equalsIgnoreCase("soulsSet")) {
            if (args.length == 3) {
                String playerName = args[1];

                //Checks if target is online and exists
                Player target = plugin.getServer().getPlayer(playerName);
                boolean targetOnline = isTargetOnline(target,sender);
                if (!targetOnline) {
                    return true;
                }
                if (target.isOnline()) { //THis may be redudant but I'm doing it to be safe
                    int souls = 0;
                    try {
                        souls = Integer.valueOf(args[2]);
                    }
                    catch (NumberFormatException e) {
                        if (sender instanceof Player) {
                            Player p = (Player) sender;
                            LanguageSelector lang = new LanguageSelector(p);
                            p.sendMessage(ChatColor.RED +lang.getString("improperArguments") +" /frpg setLevel ["+lang.getString("playerName")+"] ["+lang.getString("skillName")+"] ["+lang.getString("level")+"]");
                        } else {
                            System.out.println("Improper Arguments, try /frpg setLevel [playerName] [skillName] [level]");
                        }
                        return true;
                    }
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (p.hasPermission("freeRPG.setSouls")) {
                            ChangeStats increaseStats = new ChangeStats(target);
                            increaseStats.setStat("global",20,souls);
                        } else {
                            LanguageSelector lang = new LanguageSelector(p);
                            p.sendMessage(ChatColor.RED+lang.getString("noPermission"));
                        }
                    }
                    else {
                        ChangeStats increaseStats = new ChangeStats(target);
                        increaseStats.setStat("global",20,souls);
                    }
                }
            }
            else {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (p.hasPermission("freeRPG.setSouls")) {
                        LanguageSelector lang = new LanguageSelector(p);
                        p.sendMessage(ChatColor.RED +lang.getString("improperArguments") +" /frpg setSouls ["+lang.getString("[playerName]")+"] ["+lang.getString("amount")+"]");
                    } else {
                        LanguageSelector lang = new LanguageSelector(p);
                        p.sendMessage(ChatColor.RED+lang.getString("noPermission"));
                    }
                }
                else {
                    System.out.println("Improper Arguments, try /frpg setSouls [playername] [amount]");
                }
            }
        }

        //setTokens
        else if (args[0].equalsIgnoreCase("setTokens") || args[0].equalsIgnoreCase("tokensSet")) {

            if (args.length == 5 || args.length == 4) {
                String playerName = args[1];

                //Checks if target is online and exists
                Player target = plugin.getServer().getPlayer(playerName);
                boolean targetOnline = isTargetOnline(target,sender);
                if (!targetOnline) {
                    return true;
                }

                //Global tokens case
                if (args.length == 4) {
                    if (args[2].equalsIgnoreCase("global")) {
                        int globalTokens = 0;
                        try {
                            globalTokens = Integer.valueOf(args[3]);
                        }
                        catch (NumberFormatException e) {
                            if (sender instanceof Player) {
                                Player p = (Player) sender;
                                LanguageSelector lang = new LanguageSelector(p);
                                p.sendMessage(ChatColor.RED +lang.getString("improperArguments") +" /frpg setTokens ["+lang.getString("playerName")+"] global ["+lang.getString("amount")+"]");
                            } else {
                                System.out.println("Improper Arguments, try /frpg setTokens [playername] global [amount]");
                            }
                            return true;
                        }
                        if (sender instanceof Player) {
                            Player p = (Player) sender;
                            if (p.hasPermission("freeRPG.setTokens")) {
                                ChangeStats increaseStats = new ChangeStats(target);
                                increaseStats.setStat("global",1,globalTokens);
                            }
                        } else {
                            ChangeStats increaseStats = new ChangeStats(target);
                            increaseStats.setStat("global",1,globalTokens);
                        }
                    }
                    else {
                        if (sender instanceof Player) {
                            Player p = (Player) sender;
                            LanguageSelector lang = new LanguageSelector(p);
                            p.sendMessage(ChatColor.RED +lang.getString("improperArguments") +" /frpg setTokens ["+lang.getString("playerName")+"] global ["+lang.getString("amount")+"]");
                        } else {
                            System.out.println("Improper Arguments, /frpg setTokens [PlayerName] global [amount]");
                        }
                    }
                    return true;
                }

                String tokenType = args[3];
                String[] labels_0 = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting"};
                List<String> labels_arr = Arrays.asList(labels_0);
                StringsAndOtherData stringsAndOtherData = new StringsAndOtherData();
                String skillName = UtilityMethods.convertStringToListCasing(labels_arr,args[2]);
                if (labels_arr.contains(skillName) && target.isOnline()) { //THis may be redudant but I'm doing it to be safe
                    //Skill/Passive Token Case
                    if (tokenType.equalsIgnoreCase("skill") || tokenType.equalsIgnoreCase("passive")) {
                        int tokens = 0;
                        try {
                            tokens = Integer.valueOf(args[4]);
                        } catch (NumberFormatException e) {
                            if (sender instanceof Player) {
                                Player p = (Player) sender;
                                LanguageSelector lang = new LanguageSelector(p);
                                p.sendMessage(ChatColor.RED +lang.getString("improperArguments") +" /frpg setTokens ["+lang.getString("playerName")+"] ["+lang.getString("skillName")+"] [skill/passive] ["+lang.getString("amount")+"]");
                            } else {
                                System.out.println("Improper Arguments, try /frpg setTokens [playername] [skillName] [global/skill/passive] [amount]");
                            }
                            return true;
                        }
                        if (sender instanceof Player) {
                            Player p = (Player) sender;
                            if (p.hasPermission("freeRPG.setTokens")) {
                                ChangeStats increaseStats = new ChangeStats(target);
                                if (tokenType.equalsIgnoreCase("skill")) {
                                    increaseStats.setStat(skillName, 3, tokens);
                                } else {
                                    increaseStats.setStat(skillName, 2, tokens);
                                }
                            }
                        } else {
                            ChangeStats increaseStats = new ChangeStats(target);
                            if (tokenType.equalsIgnoreCase("skill")) {
                                increaseStats.setStat(skillName, 3, tokens);
                            } else {
                                increaseStats.setStat(skillName, 2, tokens);
                            }
                        }
                    } else {
                        if (sender instanceof Player) {
                            Player p = (Player) sender;
                            LanguageSelector lang = new LanguageSelector(p);
                            p.sendMessage(ChatColor.RED +lang.getString("improperArguments") +" /frpg setTokens ["+lang.getString("playerName")+"] ["+lang.getString("skillName")+"] [skill/passive] ["+lang.getString("amount")+"]");
                        } else {
                            System.out.println("Improper Arguments, try /frpg setTokens [playerName] [skillName] [passive/skill] [amount]");
                        }
                    }
                }
            }
            else {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (p.hasPermission("freeRPG.setTokens")) {
                        LanguageSelector lang = new LanguageSelector(p);
                        p.sendMessage(ChatColor.RED +lang.getString("improperArguments") +" /frpg setTokens ["+lang.getString("playerName")+"] ["+lang.getString("skillName")+"] [skill/passive] ["+lang.getString("amount")+"]");
                    } else {
                        LanguageSelector lang = new LanguageSelector(p);
                        p.sendMessage(ChatColor.RED+lang.getString("noPermission"));
                    }
                }
                else {
                    System.out.println("Improper Arguments, try /frpg setTokens [playername] [skillName] [skill/passive] [amount]");
                }
            }
        }

        //setMultiplier
        else if (args[0].equalsIgnoreCase("setMultiplier") || args[0].equalsIgnoreCase("multiplierSet")) {
            if (args.length == 3) {
                String playerName = args[1];

                //Checks if target is online and exists
                Player target = plugin.getServer().getPlayer(playerName);
                boolean targetOnline = isTargetOnline(target,sender);
                if (!targetOnline) {
                    return true;
                }

                //Checks if value is a double
                double multiplier = 1.0;
                try {
                    multiplier = Double.valueOf(args[2]);
                }
                catch (NumberFormatException e) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        LanguageSelector lang = new LanguageSelector(p);
                        p.sendMessage(ChatColor.RED + lang.getString("improperArguments") + " /frpg setMultiplier [" + lang.getString("playerName") + "] " + "[" + lang.getString("expIncrease")+"]");
                    }
                    else {
                        System.out.println("Improper arguments, try /frpg setMultiplier [playerName] [EXP Multiplier]");
                    }
                    return true;
                }

                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (p.hasPermission("setMultiplier")) {
                        ChangeStats setMultiplier = new ChangeStats(target);
                        setMultiplier.setStat("global",23,multiplier);
                    }
                }
                else {
                    ChangeStats setMultiplier = new ChangeStats(target);
                    setMultiplier.setStat("global",23,multiplier);
                }


            }
            else {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    LanguageSelector lang = new LanguageSelector(p);
                    p.sendMessage(ChatColor.RED + lang.getString("improperArguments") + " /frpg setMultiplier [" + lang.getString("playerName") + "] " + "[" + lang.getString("expIncrease")+"]");
                }
                else {
                    System.out.println("Improper arguments, try /frpg setMultiplier [playerName] [EXP Multiplier]");
                }
            }
        }

        //addMultiplier
        else if (args[0].equalsIgnoreCase("addMultiplier") || args[0].equalsIgnoreCase("changeMultiplier")) {
            if (args.length == 3) {
                String playerName = args[1];

                //Checks if target is online and exists
                Player target = plugin.getServer().getPlayer(playerName);
                boolean targetOnline = isTargetOnline(target,sender);
                if (!targetOnline) {
                    return true;
                }

                //Checks if value is a double
                double multiplier = 1.0;
                try {
                    multiplier = Double.valueOf(args[2]);
                }
                catch (NumberFormatException e) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        LanguageSelector lang = new LanguageSelector(p);
                        p.sendMessage(ChatColor.RED + lang.getString("improperArguments") + " /frpg setMultiplier [" + lang.getString("playerName") + "] " + "[" + lang.getString("expIncrease")+"]");
                    }
                    else {
                        System.out.println("Improper arguments, try /frpg setMultiplier [playerName] [EXP Multiplier]");
                    }
                    return true;
                }

                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (p.hasPermission("setMultiplier")) {
                        ChangeStats setMultiplier = new ChangeStats(target);
                        setMultiplier.changeStat("global",23,multiplier);
                    }
                }
                else {
                    ChangeStats setMultiplier = new ChangeStats(target);
                    setMultiplier.changeStat("global",23,multiplier);
                }


            }
            else {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    LanguageSelector lang = new LanguageSelector(p);
                    p.sendMessage(ChatColor.RED + lang.getString("improperArguments") + " /frpg setMultiplier [" + lang.getString("playerName") + "] " + "[" + lang.getString("expIncrease")+"]");
                }
                else {
                    System.out.println("Improper arguments, try /frpg setMultiplier [playerName] [EXP Multiplier]");
                }
            }
        }

        //flamePickToggle
        else if (args[0].equalsIgnoreCase("toggleFlamePick") || args[0].equalsIgnoreCase("flamePickToggle")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                return togglePerk("flamePickToggle",p,args);

            } else {
                System.out.println("You need to be a player to cast this command");
            }
        }

        //flintToggle
        else if (args[0].equalsIgnoreCase("toggleFlint") || args[0].equalsIgnoreCase("flintToggle")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                return togglePerk("flintToggle",p,args);

            } else {
                System.out.println("You need to be a player to cast this command");
            }
        }

        //grappleToggle
        else if (args[0].equalsIgnoreCase("toggleGrapple") || args[0].equalsIgnoreCase("grappleToggle")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                return togglePerk("grappleToggle",p,args);

            } else {
                System.out.println("You need to be a player to cast this command");
            }

        }

        //HotRodToggle
        else if (args[0].equalsIgnoreCase("toggleHotRod") || args[0].equalsIgnoreCase("hotRodToggle")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                return togglePerk("hotRodToggle",p,args);

            } else {
                System.out.println("You need to be a player to cast this command");
            }
        }

        //MegaDigToggle
        else if (args[0].equalsIgnoreCase("toggleMegaDig") || args[0].equalsIgnoreCase("megaDigToggle")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                return togglePerk("megaDigToggle",p,args);

            } else {
                System.out.println("You need to be a player to cast this command");
            }
        }

        //PotionToggle
        else if (args[0].equalsIgnoreCase("togglePotion") || args[0].equalsIgnoreCase("potionToggle")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                return togglePerk("potionToggle",p,args);

            } else {
                System.out.println("You need to be a player to cast this command");
            }
        }

        //SpeedToggle
        else if (args[0].equalsIgnoreCase("toggleSpeed") || args[0].equalsIgnoreCase("speedToggle")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                return togglePerk("speedToggle",p,args);

            } else {
                System.out.println("You need to be a player to cast this command");
            }
        }

        //VeinMinerToggle
        else if (args[0].equalsIgnoreCase("toggleVeinMiner") || args[0].equalsIgnoreCase("veinMinerToggle")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                return togglePerk("veinMinerToggle",p,args);

            } else {
                System.out.println("You need to be a player to cast this command");
            }
        }

        //LeafBlowerToggle
        else if (args[0].equalsIgnoreCase("toggleLeafBlower") || args[0].equalsIgnoreCase("leafBlowerToggle")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                return togglePerk("leafBlowerToggle",p,args);

            } else {
                System.out.println("You need to be a player to cast this command");
            }
        }

        //holyAxeToggle
        else if (args[0].equalsIgnoreCase("toggleHolyAxe") || args[0].equalsIgnoreCase("holyAxeToggle")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                return togglePerk("holyAxeToggle",p,args);

            } else {
                System.out.println("You need to be a player to cast this command");
            }
        }

        //ConfigGUI
        else if (args[0].equalsIgnoreCase("configGUI") || args[0].equalsIgnoreCase("configurationGUI")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                LanguageSelector lang = new LanguageSelector(p);
                if (p.isSleeping()) {
                    p.sendMessage(ChatColor.RED + lang.getString("bedGUI"));
                    return true;
                }
                if (!p.hasPermission("freeRPG.configGUI")) {
                    p.sendMessage(ChatColor.RED + lang.getString("noPermission"));
                    return true;
                }
                if (args.length != 1) {
                    p.sendMessage(ChatColor.RED + lang.getString("improperArguments") + " /frpg configurationGUI");
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
                    lore.add(ChatColor.ITALIC+ChatColor.GRAY.toString()+lang.getString("diggingPassiveDesc1"));
                    backMeta.setLore(lore);
                    back.setItemMeta(backMeta);
                    gui.setItem(45,back);

                    //Level up Notifications
                    ItemStack levelUp = new ItemStack(Material.OAK_SIGN);
                    ItemMeta levelUpMeta = levelUp.getItemMeta();
                    levelUpMeta.setDisplayName(ChatColor.WHITE + ChatColor.BOLD.toString() + lang.getString("levelUpNotif"));
                    levelUp.setItemMeta(levelUpMeta);
                    gui.setItem(1,levelUp);

                    ItemStack levelUpToggle = new ItemStack(Material.LIME_DYE);
                    ItemMeta levelUpToggleMeta = levelUpToggle.getItemMeta();
                    if ( (int) pStat.get("global").get(21) > 0) {
                        levelUpToggleMeta.setDisplayName(ChatColor.BOLD + ChatColor.GREEN.toString() + lang.getString("on0"));
                    }
                    else {
                        levelUpToggle.setType(Material.GRAY_DYE);
                        levelUpToggleMeta.setDisplayName(ChatColor.BOLD + ChatColor.RED.toString() + lang.getString("off0"));
                    }
                    levelUpToggle.setItemMeta(levelUpToggleMeta);
                    gui.setItem(10,levelUpToggle);

                    //Ability Notifications
                    ItemStack abilityNotifications = new ItemStack(Material.OAK_SIGN);
                    ItemMeta abilityNotificationsMeta = abilityNotifications.getItemMeta();
                    abilityNotificationsMeta.setDisplayName(ChatColor.WHITE + ChatColor.BOLD.toString() + lang.getString("abilityPreparationNotif"));
                    abilityNotifications.setItemMeta(abilityNotificationsMeta);
                    gui.setItem(2,abilityNotifications);

                    ItemStack abilityNotificationsToggle = new ItemStack(Material.LIME_DYE);
                    ItemMeta abilityNotificationsToggleMeta = abilityNotificationsToggle.getItemMeta();
                    if ( (int) pStat.get("global").get(22) > 0) {
                        abilityNotificationsToggleMeta.setDisplayName(ChatColor.BOLD + ChatColor.GREEN.toString() + lang.getString("on0"));
                    }
                    else {
                        abilityNotificationsToggle.setType(Material.GRAY_DYE);
                        abilityNotificationsToggleMeta.setDisplayName(ChatColor.BOLD + ChatColor.RED.toString() + lang.getString("off0"));
                    }
                    abilityNotificationsToggle.setItemMeta(abilityNotificationsToggleMeta);
                    gui.setItem(11,abilityNotificationsToggle);


                    //Trigger Abilities
                    ItemStack triggerAbilities = new ItemStack(Material.WOODEN_PICKAXE);
                    ItemMeta triggerAbilitiesMeta = triggerAbilities.getItemMeta();
                    triggerAbilitiesMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    triggerAbilitiesMeta.setDisplayName(ChatColor.WHITE + ChatColor.BOLD.toString() + lang.getString("triggerAbilities"));
                    triggerAbilities.setItemMeta(triggerAbilitiesMeta);
                    gui.setItem(3,triggerAbilities);

                    ItemStack triggerAbilitiesToggle = new ItemStack(Material.LIME_DYE);
                    ItemMeta triggerAbilitiesToggleMeta = triggerAbilitiesToggle.getItemMeta();
                    if ( (int) pStat.get("global").get(24) > 0) {
                        triggerAbilitiesToggleMeta.setDisplayName(ChatColor.BOLD + ChatColor.GREEN.toString() + lang.getString("on0"));
                    }
                    else {
                        triggerAbilitiesToggle.setType(Material.GRAY_DYE);
                        triggerAbilitiesToggleMeta.setDisplayName(ChatColor.BOLD + ChatColor.RED.toString() + lang.getString("off0"));
                    }
                    triggerAbilitiesToggle.setItemMeta(triggerAbilitiesToggleMeta);
                    gui.setItem(12,triggerAbilitiesToggle);


                    //Experience Bar
                    ItemStack expBar = new ItemStack(Material.EXPERIENCE_BOTTLE);
                    ItemMeta expBarMeta = expBar.getItemMeta();
                    expBarMeta.setDisplayName(ChatColor.WHITE + ChatColor.BOLD.toString() + lang.getString("showEXPBar"));
                    expBar.setItemMeta(expBarMeta);
                    gui.setItem(4,expBar);

                    ItemStack expBarToggle = new ItemStack(Material.LIME_DYE);
                    ItemMeta expBarToggleMeta = expBarToggle.getItemMeta();
                    if ( (int) pStat.get("global").get(25) > 0) {
                        expBarToggleMeta.setDisplayName(ChatColor.BOLD + ChatColor.GREEN.toString() + lang.getString("on0"));
                    }
                    else {
                        expBarToggle.setType(Material.GRAY_DYE);
                        expBarToggleMeta.setDisplayName(ChatColor.BOLD + ChatColor.RED.toString() + lang.getString("off0"));
                    }
                    expBarToggle.setItemMeta(expBarToggleMeta);
                    gui.setItem(13,expBarToggle);

                    //Ability Duration Bar
                    ItemStack durationBar = new ItemStack(Material.CLOCK,1);
                    ItemMeta durationBarMeta = durationBar.getItemMeta();
                    durationBarMeta.setDisplayName(ChatColor.WHITE + ChatColor.BOLD.toString() + lang.getString("numberOfAbilityTimersDisplayed"));
                    durationBar.setItemMeta(durationBarMeta);
                    gui.setItem(5,durationBar);

                    ItemStack durationBarToggle = new ItemStack(Material.LIME_DYE);
                    ItemMeta durationBarToggleMeta = durationBarToggle.getItemMeta();
                    int numberOfBars = (int) pStat.get("global").get(28);
                    if (numberOfBars  > 0) {
                        durationBarToggleMeta.setDisplayName(ChatColor.BOLD + ChatColor.GREEN.toString() + numberOfBars);
                    }
                    else {
                        durationBarToggle.setType(Material.GRAY_DYE);
                        durationBarToggleMeta.setDisplayName(ChatColor.BOLD + ChatColor.RED.toString() + 0);
                    }
                    durationBarToggle.setAmount(Math.max(1,numberOfBars));
                    durationBarToggle.setItemMeta(durationBarToggleMeta);
                    gui.setItem(14,durationBarToggle);


                    //LANGUAGES
                    StringsAndOtherData stringsAndOtherData = new StringsAndOtherData();
                    stringsAndOtherData.setLanguageItems(p,gui);



                    //Put the items in the inventory
                    p.openInventory(gui);
                }
            }
            else {
                System.out.println("You need to be a player to cast this command");
            }
        }

        //SkillConfigGUI
        else if (args[0].equalsIgnoreCase("skillConfigGUI") || args[0].equalsIgnoreCase("skillConfigurationGUI") || args[0].equalsIgnoreCase("skillConfig")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                LanguageSelector lang = new LanguageSelector(p);
                if (p.isSleeping()) {
                    p.sendMessage(ChatColor.RED + lang.getString("bedGUI"));
                    return true;
                }
                if (!p.hasPermission("freeRPG.skillConfigGUI")) {
                    p.sendMessage(ChatColor.RED + lang.getString("noPermission"));
                    return true;
                }
                if (args.length != 2) {
                    p.sendMessage(ChatColor.RED + lang.getString("improperArguments") + " /frpg skillConfigGUI [" + lang.getString("skillName") + "]");
                    return true;
                }
                String[] titles_0 = {"Digging","Woodcutting","Mining","Farming","Fishing","Archery","Beast Mastery","Swordsmanship","Defense","Axe Mastery","Repair","Agility","Alchemy","Smelting","Enchanting"};
                String[] labels_0 = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting"};
                String[] passiveLabels0 = {"repair","agility","alchemy","smelting","enchanting"};
                List<String> labels = Arrays.asList(labels_0);
                List<String> passiveLabels = Arrays.asList(passiveLabels0);
                StringsAndOtherData stringsAndOtherData = new StringsAndOtherData();
                String skillName = UtilityMethods.convertStringToListCasing(labels,args[1]);
                if (!labels.contains(skillName) ) {
                    p.sendMessage(ChatColor.RED + lang.getString("improperArguments") + " /frpg skillConfigGUI [" + lang.getString("skillName") + "]");
                    return true;
                }
                String skillTitle = titles_0[labels.indexOf(skillName)];
                Inventory gui = Bukkit.createInventory(p, 54, skillTitle + " Configuration");
                PlayerStats pStatClass = new PlayerStats(p);
                Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();

                //UNIVERSAL CONFIG STUFF


                //Experience Bar
                ItemStack expBar = new ItemStack(Material.EXPERIENCE_BOTTLE);
                ItemMeta expBarMeta = expBar.getItemMeta();
                expBarMeta.setDisplayName(ChatColor.WHITE + ChatColor.BOLD.toString() + lang.getString("showEXPBar"));
                expBar.setItemMeta(expBarMeta);
                gui.setItem(10,expBar);

                ItemStack expBarToggle = new ItemStack(Material.LIME_DYE);
                ItemMeta expBarToggleMeta = expBarToggle.getItemMeta();
                if ( pStatClass.isPlayerSkillExpBarOn(skillName) ) {
                    expBarToggleMeta.setDisplayName(ChatColor.BOLD + ChatColor.GREEN.toString() + lang.getString("on0"));
                }
                else {
                    expBarToggle.setType(Material.GRAY_DYE);
                    expBarToggleMeta.setDisplayName(ChatColor.BOLD + ChatColor.RED.toString() + lang.getString("off0"));
                }
                expBarToggle.setItemMeta(expBarToggleMeta);
                gui.setItem(19,expBarToggle);

                //Trigger Abilities
                if (!passiveLabels.contains(skillName)) {
                    ItemStack triggerAbilities = new ItemStack(Material.WOODEN_PICKAXE);
                    ItemMeta triggerAbilitiesMeta = triggerAbilities.getItemMeta();
                    triggerAbilitiesMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    triggerAbilitiesMeta.setDisplayName(ChatColor.BOLD.toString() + lang.getString("triggerAbilities"));
                    triggerAbilities.setItemMeta(triggerAbilitiesMeta);
                    gui.setItem(11, triggerAbilities);

                    ItemStack triggerAbilitiesToggle = new ItemStack(Material.LIME_DYE);
                    ItemMeta triggerAbilitiesToggleMeta = triggerAbilitiesToggle.getItemMeta();
                    if (pStatClass.isPlayerSkillAbilityOn(skillName)) {
                        triggerAbilitiesToggleMeta.setDisplayName(ChatColor.BOLD + ChatColor.GREEN.toString() + lang.getString("on0"));
                    } else {
                        triggerAbilitiesToggle.setType(Material.GRAY_DYE);
                        triggerAbilitiesToggleMeta.setDisplayName(ChatColor.BOLD + ChatColor.RED.toString() + lang.getString("off0"));
                    }
                    triggerAbilitiesToggle.setItemMeta(triggerAbilitiesToggleMeta);
                    gui.setItem(20, triggerAbilitiesToggle);
                }


                //Back button
                ItemStack back = new ItemStack(Material.ARROW);
                ItemMeta backMeta = back.getItemMeta();
                backMeta.setDisplayName(ChatColor.BOLD + "Back");
                ArrayList<String> lore = new ArrayList<String>();
                lore.add(ChatColor.ITALIC+ChatColor.GRAY.toString()+lang.getString("backToSkillTree"));
                backMeta.setLore(lore);
                back.setItemMeta(backMeta);
                gui.setItem(45,back);

                ItemStack skillIcon = new ItemStack(Material.WOODEN_PICKAXE);
                ItemMeta skillIconMeta = skillIcon.getItemMeta();
                skillIconMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                skillIconMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                skillIconMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                skillIconMeta.setDisplayName(ChatColor.AQUA + ChatColor.BOLD.toString() + lang.getString(skillName));
                skillIcon.setItemMeta(skillIconMeta);

                switch (skillName) {
                    case "digging":
                        skillIcon.setType(Material.IRON_SHOVEL);
                        //flintFinder
                        togglePerkSetGuiItem("flintToggle",p,gui);

                        //Mega Dig
                        togglePerkSetGuiItem("megaDigToggle",p,gui);
                        break;
                    case "woodcutting":
                        skillIcon.setType(Material.IRON_AXE);

                        //leafBlower
                        togglePerkSetGuiItem("leafBlowerToggle",p,gui);
                        break;
                    case "mining":
                        skillIcon.setType(Material.IRON_PICKAXE);

                        //veinMiner
                        togglePerkSetGuiItem("veinMinerToggle",p,gui);

                        break;
                    case "farming":
                        skillIcon.setType(Material.IRON_HOE);
                        break;
                    case "fishing":
                        skillIcon.setType(Material.FISHING_ROD);

                        //grappling Hook
                        togglePerkSetGuiItem("grappleToggle",p,gui);

                        //hot Rod
                        togglePerkSetGuiItem("hotRodToggle",p,gui);
                        break;
                    case "archery":
                        skillIcon.setType(Material.BOW);
                        break;
                    case "beastMastery":
                        skillIcon.setType(Material.BONE);
                        break;
                    case "swordsmanship":
                        skillIcon.setType(Material.IRON_SWORD);
                        break;
                    case "defense":
                        skillIcon.setType(Material.IRON_CHESTPLATE);
                        break;
                    case "axeMastery":
                        skillIcon.setType(Material.GOLDEN_AXE);

                        //Holy Axe
                        togglePerkSetGuiItem("holyAxeToggle",p,gui);
                        break;
                    case "repair":
                        skillIcon.setType(Material.ANVIL);
                        break;
                    case "agility":
                        skillIcon.setType(Material.LEATHER_LEGGINGS);

                        //gracefulFeet
                        togglePerkSetGuiItem("speedToggle",p,gui);

                        break;
                    case "alchemy":
                        skillIcon.setType(Material.POTION);

                        //potionMaster
                        togglePerkSetGuiItem("potionToggle",p,gui);
                        break;
                    case "smelting":
                        skillIcon.setType(Material.COAL);

                        //flamePick
                        togglePerkSetGuiItem("flamePickToggle",p,gui);
                        break;
                    case "enchanting":
                        skillIcon.setType(Material.ENCHANTING_TABLE);
                        break;
                    default:
                        break;

                }
                gui.setItem(4,skillIcon);




                //Put the items in the inventory
                p.openInventory(gui);
            }
            else {
                System.out.println("You need to be a player to cast this command");
            }
        }

        //ConfirmationGUI
        else if (args[0].equalsIgnoreCase("confirmGUI") || args[0].equalsIgnoreCase("confirmationGUI")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                LanguageSelector lang = new LanguageSelector(p);
                if (p.isSleeping()) {
                    p.sendMessage(ChatColor.RED + lang.getString("bedGUI"));
                    return true;
                }
                if (!p.hasPermission("freeRPG.confirmGUI")) {
                    p.sendMessage(ChatColor.RED + lang.getString("noPermission"));
                    return true;
                }
                String[] labels_0 = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting","global"};
                String[] titles_0 = {lang.getString("digging"),lang.getString("woodcutting"),lang.getString("mining"),lang.getString("farming"),lang.getString("fishing"),lang.getString("archery"),lang.getString("beastMastery"),lang.getString("swordsmanship"),lang.getString("defense"),lang.getString("axeMastery"),lang.getString("repair"),lang.getString("agility"),lang.getString("alchemy"),lang.getString("smelting"),lang.getString("enchanting"),lang.getString("global")};
                List<String> labels_arr = Arrays.asList(labels_0);
                if (args.length != 2) {
                    p.sendMessage(ChatColor.RED +  lang.getString("improperArguments") + " /frpg confirmationGUI [skillName]");
                }
                else if (labels_arr.indexOf(args[1]) == -1) {
                    p.sendMessage(ChatColor.RED+ lang.getString("improperArguments") + " /frpg confirmationGUI [skillName]");
                }
                else {
                    String skillName = labels_0[labels_arr.indexOf(args[1])];
                    String skillTitle = titles_0[labels_arr.indexOf(args[1])];
                    Inventory gui = Bukkit.createInventory(p, 54, "Confirmation Window");

                    //Load souls data
                    ConfigLoad loadConfig = new ConfigLoad();
                    ArrayList<Integer> soulsInfo = loadConfig.getSoulsInfo();
                    String refundCost = Integer.toString(soulsInfo.get(1));

                    //Information
                    ItemStack info = new ItemStack(Material.PAPER);
                    ItemMeta infoMeta = info.getItemMeta();
                    infoMeta.setDisplayName(ChatColor.BOLD + ChatColor.YELLOW.toString() + lang.getString("warning"));
                    ArrayList<String> lore = new ArrayList<String>();
                    lore.add(ChatColor.ITALIC+ChatColor.GRAY.toString()+lang.getString("refundSkillTree0") + " " + refundCost + " " + lang.getString("souls"));
                    lore.add(ChatColor.ITALIC+ChatColor.GRAY.toString()+lang.getString("refundSkillTree1"));
                    lore.add(ChatColor.ITALIC+ChatColor.GRAY.toString()+lang.getString("refundSkillTree2") + " " + ChatColor.BOLD.toString() + ChatColor.WHITE.toString() +
                            skillTitle + ChatColor.RESET.toString() + ChatColor.ITALIC.toString() + ChatColor.GRAY.toString()+ " " +lang.getString("skill") +"?");
                    infoMeta.setLore(lore);
                    info.setItemMeta(infoMeta);
                    gui.setItem(22,info);

                    //Yes Button
                    ItemStack yes = new ItemStack(Material.LIME_TERRACOTTA);
                    ItemMeta yesMeta = yes.getItemMeta();
                    yesMeta.setDisplayName(ChatColor.BOLD + ChatColor.GREEN.toString() + lang.getString("yes0"));
                    yes.setItemMeta(yesMeta);
                    gui.setItem(39,yes);

                    //No Button
                    ItemStack no = new ItemStack(Material.RED_TERRACOTTA);
                    ItemMeta noMeta = no.getItemMeta();
                    noMeta.setDisplayName(ChatColor.BOLD + ChatColor.RED.toString() + lang.getString("no0"));
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
                LanguageSelector lang = new LanguageSelector(p);
                if (p.isSleeping()) {
                    p.sendMessage(ChatColor.RED + lang.getString("bedGUI"));
                    return true;
                }
                if (!p.hasPermission("freeRPG.craftGUI")) {
                    p.sendMessage(ChatColor.RED + lang.getString("noPermission"));
                    return true;
                }
                String[] labels_0 = {"archery1", "farming1", "farming2", "farming3", "farming4", "farming5",
                        "enchanting1", "enchanting2", "enchanting3", "enchanting4", "enchanting5",
                        "enchanting6", "enchanting7", "enchanting8", "enchanting9", "enchanting10",
                        "alchemy1","alchemy2","alchemy3","alchemy4","alchemy5"};
                List<String> labels_arr = Arrays.asList(labels_0);
                if (args.length != 2) {
                    p.sendMessage(ChatColor.RED + lang.getString("improperArguments") +" /frpg craftingRecipe [skillName[Number]]");
                } else if (labels_arr.indexOf(args[1]) == -1) {
                    p.sendMessage(ChatColor.RED + lang.getString("improperArguments") +" /frpg craftingRecipe [skillName[Number]]");
                } else {
                    CraftingRecipes craftingRecipes = new CraftingRecipes();
                    ConfigLoad configLoad = new ConfigLoad();
                    Map<String, CustomRecipe> customCraftingRecipes = configLoad.getCraftingRecipes();
                    String craftingLabel = args[1];
                    ArrayList<Material> cowEgg = craftingRecipes.getCowEggRecipe();
                    ArrayList<Material> beeEgg = craftingRecipes.getBeeEggRecipe();
                    ArrayList<Material> mooshroomEgg = craftingRecipes.getMooshroomEggRecipe();
                    ArrayList<Material> horseEgg = craftingRecipes.getHorseEggRecipe();
                    ArrayList<Material> slimeEgg = craftingRecipes.getSlimeEggRecipe();
                    ArrayList<Material> tippedArrow = craftingRecipes.getTippedArrowRecipe();
                    ArrayList<Material> power = craftingRecipes.getPowerRecipe();
                    ArrayList<Material> efficiency = craftingRecipes.getEfficiencyRecipe();
                    ArrayList<Material> sharpness = craftingRecipes.getSharpnessRecipe();
                    ArrayList<Material> protection = craftingRecipes.getProtectionRecipe();
                    ArrayList<Material> luck = craftingRecipes.getLuckRecipe();
                    ArrayList<Material> lure = craftingRecipes.getLureRecipe();
                    ArrayList<Material> frost = craftingRecipes.getFrostRecipe();
                    ArrayList<Material> depth = craftingRecipes.getDepthRecipe();
                    ArrayList<Material> mending = craftingRecipes.getMendingRecipe();
                    ArrayList<Material> fortune = craftingRecipes.getFortuneRecipe();
                    ArrayList<Material> waterBreathing = craftingRecipes.getWaterBreathingRecipe();
                    ArrayList<Material> speed = craftingRecipes.getSpeedRecipe();
                    ArrayList<Material> fireResistance = craftingRecipes.getFireResistanceRecipe();
                    ArrayList<Material> healing = craftingRecipes.getHealingRecipe();
                    ArrayList<Material> strength = craftingRecipes.getStrengthRecipe();
                    ArrayList<Material> recipe = new ArrayList<>();
                    for (int i = 0; i < 9; i++) {
                        recipe.add(Material.AIR);
                    }

                    ItemStack output = new ItemStack(Material.TIPPED_ARROW,8);
                    if (!craftingLabel.equalsIgnoreCase("archery1")) {
                        output = customCraftingRecipes.get(craftingLabel).getItemStack();
                    }
                    switch (craftingLabel) {
                        case "archery1":
                            recipe = tippedArrow;
                            output.setType(Material.TIPPED_ARROW);
                            output.setAmount(8);
                            break;
                        case "farming1":
                            recipe = cowEgg;
                            break;
                        case "farming2":
                            recipe = beeEgg;
                            break;
                        case "farming3":
                            recipe = mooshroomEgg;
                            break;
                        case "farming4":
                            recipe = horseEgg;
                            break;
                        case "farming5":
                            recipe = slimeEgg;
                            break;
                        case "enchanting1":
                            recipe = power;
                            break;
                        case "enchanting2":
                            recipe = efficiency;
                            break;
                        case "enchanting3":
                            recipe = sharpness;
                            break;
                        case "enchanting4":
                            recipe = protection;
                            break;
                        case "enchanting5":
                            recipe = luck;
                            break;
                        case "enchanting6":
                            recipe = lure;
                            break;
                        case "enchanting7":
                            recipe = frost;
                            break;
                        case "enchanting8":
                            recipe = depth;
                            break;
                        case "enchanting9":
                            recipe = mending;
                            break;
                        case "enchanting10":
                            recipe = fortune;
                            break;
                        case "alchemy1":
                            recipe = waterBreathing;
                            break;
                        case "alchemy2":
                            recipe = speed;
                            break;
                        case "alchemy3":
                            recipe = fireResistance;
                            break;
                        case "alchemy4":
                            recipe = healing;
                            break;
                        case "alchemy5":
                            recipe = strength;
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
                    lore.add(ChatColor.ITALIC+ChatColor.GRAY.toString()+lang.getString("backToSkillTree"));
                    backMeta.setLore(lore);
                    back.setItemMeta(backMeta);
                    gui.setItem(45,back);

                    //Crafting Tables and Connector
                    Integer[] indicies = {1,2,3,4,5,10,14,19,23,28,32,37,38,39,40,41};
                    ItemStack craftingTable = new ItemStack(Material.CRAFTING_TABLE);
                    ItemMeta craftingTableMeta = craftingTable.getItemMeta();
                    craftingTableMeta.setDisplayName(ChatColor.WHITE.toString());
                    craftingTable.setItemMeta(craftingTableMeta);
                    ItemStack connector = new ItemStack(Material.GLASS_PANE);
                    ItemMeta connectorMeta = connector.getItemMeta();
                    connectorMeta.setDisplayName(ChatColor.WHITE.toString());
                    connector.setItemMeta(connectorMeta);
                    for (int i : indicies) {
                        gui.setItem(i,craftingTable);
                    }
                    gui.setItem(24,connector);

                    //Inputs and Output
                    Integer[] recipeIndices = {11,12,13,20,21,22,29,30,31};
                    for (int i=0; i <9; i++) {
                        gui.setItem(recipeIndices[i],new ItemStack(recipe.get(i),1));
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
                p.performCommand("frpg");
            }
            else {
                System.out.println("You must be a player to perform this command");
            }
        }



        /*
        This next argument is the biggest by far
        It handles all the skill tree GUIs, which are the most adaptable with the most unique buttons
        */


        //SkillTreeGUI
        else if (args[0].equalsIgnoreCase("skillTree") || args[0].equalsIgnoreCase("skillTreeGUI")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                LanguageSelector lang = new LanguageSelector(p);
                if (p.isSleeping()) {
                    p.sendMessage(ChatColor.RED + lang.getString("bedGUI"));
                    return true;
                }
                if (!p.hasPermission("freeRPG.skillsGUI")) {
                    p.sendMessage(ChatColor.RED + lang.getString("noPermission"));
                    return true;
                }
            }
            StringsAndOtherData getMaps = new StringsAndOtherData();
            Map<String, String[]> perksMap = getMaps.getPerksMap();
            Map<String, String[]> descriptionsMap = getMaps.getDescriptionsMap();
            Map<String, String[]> passivePerksMap = getMaps.getPassivePerksMap();
            Map<String, String[]> passiveDescriptionsMap = getMaps.getPassiveDescriptionsMap();

            String[] titles_0 = {"Digging","Woodcutting","Mining","Farming","Fishing","Archery","Beast Mastery","Swordsmanship","Defense","Axe Mastery","Repair","Agility","Alchemy","Smelting","Enchanting","Global"};
            String[] labels_0 = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting","global"};
            List<String> labels_arr = Arrays.asList(labels_0);

            ConfigLoad loadConfig = new ConfigLoad();
            ArrayList<Integer> soulsInfo = loadConfig.getSoulsInfo();
            String refundCost = Integer.toString(soulsInfo.get(1));

            if (sender instanceof Player) {
                if (args.length == 2) {
                    StringsAndOtherData stringsAndOtherData = new StringsAndOtherData();
                    String skillName = UtilityMethods.convertStringToListCasing(labels_arr,args[1]);
                    if (labels_arr.contains(skillName)) {
                        Player p = (Player) sender;
                        LanguageSelector langManager = new LanguageSelector(p);
                        String skName = labels_0[labels_arr.indexOf(args[1])];
                        if (!loadConfig.getAllowedSkillsMap().get(skName)) {
                            p.sendMessage(ChatColor.RED + langManager.getString("disabledSkill"));
                            return true;
                        }
                        String[] newTitles = perksMap.get(skName);
                        String[] newDescs = descriptionsMap.get(skName);
                        String[] newPassiveTitles = passivePerksMap.get(skName);
                        String[] newPassiveDescs = passiveDescriptionsMap.get(skName);
                        int i = 0;
                        for (String title : perksMap.get(skName)) {
                            String id = skName + "PerkTitle" + i;
                            newTitles[i] = langManager.getString(id);
                            i += 1;
                        }
                        i = 0;
                        for (String desc : descriptionsMap.get(skName)) {
                            String id = skName + "PerkDesc" + i;
                            newDescs[i] = langManager.getString(id);
                            i += 1;
                        }
                        i = 0;
                        for (String passiveTitle : passivePerksMap.get(skName)) {
                            String id = skName + "PassiveTitle" + i;
                            newPassiveTitles[i] = langManager.getString(id);
                            i += 1;
                        }
                        i = 0;
                        for (String passiveDesc : passiveDescriptionsMap.get(skName)) {
                            String id = skName + "PassiveDesc" + i;
                            newPassiveDescs[i] = langManager.getString(id);
                            i += 1;
                        }
                        perksMap.put(skName, newTitles);
                        descriptionsMap.put(skName, newDescs);
                        passivePerksMap.put(skName, newPassiveTitles);
                        passiveDescriptionsMap.put(skName, newPassiveDescs);
                    }
                }
            }

            if (sender instanceof Player && args.length!= 2){
                Player p = (Player) sender;
                LanguageSelector lang = new LanguageSelector(p);
                p.sendMessage(ChatColor.RED+lang.getString("improperArguments")+" /frpg skillTree ["+lang.getString("skillName")+"]");
            }
            else if (sender instanceof Player && labels_arr.indexOf(args[1]) == -1) {
                Player p = (Player) sender;
                LanguageSelector lang = new LanguageSelector(p);
                p.sendMessage(ChatColor.RED+lang.getString("improperArguments")+" /frpg skillTree ["+lang.getString("skillName")+"]");
            }
            else if (sender instanceof Player && labels_arr.indexOf(args[1]) < 10) {
                Player p = (Player) sender;
                LanguageSelector lang = new LanguageSelector(p);
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


                //Beginning state of the menu
                ItemStack[] menu_items = {skill_1a,skill_1b,skill_2a,skill_2b,skill_3a,skill_3b,skill_M};
                String[] labels = perksMap.get(skillName);
                String[] lores_line2 = descriptionsMap.get(skillName);

                //Initialize some varibales for use
                String desc = "";
                Map<String,CustomRecipe> customRecipeMap = loadConfig.getCraftingRecipes();
                StringsAndOtherData stringsAndOtherData = new StringsAndOtherData();

                //Make changes to some skills independent of level
                if (skillName.equalsIgnoreCase("woodcutting")) {
                    ArrayList<Integer> timberBreakLimits = loadConfig.getTimberBreakLimits();
                    String timberBreakLimitInitial = String.valueOf(timberBreakLimits.get(0));
                    String timberBreakLimitUpgraded = String.valueOf(timberBreakLimits.get(1));
                    String newLore0 = stringsAndOtherData.replaceIfPresent(lores_line2[4],"64",timberBreakLimitInitial);
                    String newLore1 = stringsAndOtherData.replaceIfPresent(newLore0,"128",timberBreakLimitUpgraded);
                    lores_line2[4] = newLore1;
                }

                int special_index = 0;
                switch (skillName){
                    case "mining":
                        special_index = 0;
                        switch (skill_1a_level) {
                            case 0:
                                desc = lang.getString("miningPerkDesc0_1");
                                lores_line2[special_index] = desc;
                                break;
                            case 1:
                                desc = lang.getString("miningPerkDesc0_2");
                                lores_line2[special_index] = desc;
                                break;
                            case 2:
                                desc = lang.getString("miningPerkDesc0_3");
                                lores_line2[special_index] = desc;
                                break;
                            case 3:
                                desc = lang.getString("miningPerkDesc0_4");
                                lores_line2[special_index] = desc;
                                break;
                            case 4:
                                desc = lang.getString("miningPerkDesc0_5");
                                lores_line2[special_index] = desc;
                                break;
                            default:
                                break;
                        }
                        break;
                    case "woodcutting":
                        special_index = 3;
                        desc = lang.getString("woodcuttingPerkDesc3_1") + " ";
                        ArrayList<Object> woodcuttingInfo = loadConfig.getWoodcuttingInfo();
                        switch (skill_2b_level) {
                            case 0:
                                Material mat0 = (Material) woodcuttingInfo.get(0);
                                String itemName0 = "";
                                if (mat0 != null) {
                                    itemName0 = mat0.toString().replaceAll("_", " ").toLowerCase();
                                }
                                else {
                                    itemName0 = "feather";
                                }
                                desc += itemName0;
                                lores_line2[special_index] = desc;
                                break;
                            case 1:
                                Material mat1 = (Material) woodcuttingInfo.get(3);
                                String itemName1 = "";
                                if (mat1 != null) {
                                    itemName1 = mat1.toString().replaceAll("_", " ").toLowerCase();
                                }
                                else {
                                    itemName1 = "gold nugget";
                                }
                                desc += itemName1;
                                lores_line2[special_index] = desc;
                                break;
                            case 2:
                                Material mat2 = (Material) woodcuttingInfo.get(6);
                                String itemName2 = "";
                                if (mat2 != null) {
                                    itemName2 = mat2.toString().replaceAll("_", " ").toLowerCase();
                                }
                                else {
                                    itemName2 = "golden apple";
                                }
                                desc += itemName2;
                                lores_line2[special_index] = desc;
                                break;
                            case 3:
                                Material mat3 = (Material) woodcuttingInfo.get(9);
                                String itemName3 = "";
                                if (mat3 != null) {
                                    itemName3 = mat3.toString().replaceAll("_", " ").toLowerCase();
                                }
                                else {
                                    itemName3 = "experience bottle";
                                }
                                desc += itemName3;
                                lores_line2[special_index] = desc;
                                break;
                            case 4:
                                Material mat4 = (Material) woodcuttingInfo.get(12);
                                String itemName4 = "";
                                if (mat4 != null) {
                                    itemName4 = mat4.toString().replaceAll("_", " ").toLowerCase();
                                }
                                else {
                                    itemName4 = "enchanted golden apple";
                                }
                                desc += itemName4;
                                lores_line2[special_index] = desc;
                                break;
                            default:
                                break;
                        }
                        break;
                    case "fishing":
                        special_index = 1;
                        desc = lang.getString("fishingPerkDesc1_1") + " ";
                        switch (skill_1b_level) {
                            case 0:
                                desc += "I ("+lang.getString("common") +")";
                                lores_line2[special_index] = desc;
                                break;
                            case 1:
                                desc += "II ("+lang.getString("uncommon") +")";
                                lores_line2[special_index] = desc;
                                break;
                            case 2:
                                desc += "III ("+lang.getString("rare") +")";
                                lores_line2[special_index] = desc;
                                break;
                            case 3:
                                desc += "IV ("+lang.getString("veryRare") +")";
                                lores_line2[special_index] = desc;
                                break;
                            case 4:
                                desc += "V ("+lang.getString("legendary") +")";
                                lores_line2[special_index] = desc;
                                break;
                            default:
                                break;
                        }
                        break;
                    case "farming":
                        special_index = 1;
                        desc = lang.getString("farmingPerkDesc1_1")  + " ";
                        switch (skill_1b_level) {
                            case 0:
                                Material output1 = customRecipeMap.get("farming"+1).getOutput();
                                if (output1.equals(Material.COW_SPAWN_EGG)) {
                                    desc += lang.getString("cowSpawnEgg");
                                }
                                else {
                                    desc += stringsAndOtherData.cleanUpTitleString(output1.toString());
                                }
                                lores_line2[special_index] = desc;
                                break;
                            case 1:
                                Material output2 = customRecipeMap.get("farming"+2).getOutput();
                                if (output2.equals(Material.BEE_SPAWN_EGG)) {
                                    desc += lang.getString("beeSpawnEgg");
                                }
                                else {
                                    desc += stringsAndOtherData.cleanUpTitleString(output2.toString());
                                }
                                lores_line2[special_index] = desc;
                                break;
                            case 2:
                                Material output3 = customRecipeMap.get("farming"+3).getOutput();
                                if (output3.equals(Material.MOOSHROOM_SPAWN_EGG)) {
                                    desc += lang.getString("mooshroomSpawnEgg");
                                }
                                else {
                                    desc += stringsAndOtherData.cleanUpTitleString(output3.toString());
                                }
                                lores_line2[special_index] = desc;
                                break;
                            case 3:
                                Material output4 = customRecipeMap.get("farming"+4).getOutput();
                                if (output4.equals(Material.HORSE_SPAWN_EGG)) {
                                    desc += lang.getString("horseSpawnEgg");
                                }
                                else {
                                    desc += stringsAndOtherData.cleanUpTitleString(output4.toString());
                                }
                                lores_line2[special_index] = desc;
                                break;
                            case 4:
                                Material output5 = customRecipeMap.get("farming"+5).getOutput();
                                if (output5.equals(Material.SLIME_SPAWN_EGG)) {
                                    desc += lang.getString("slimeSpawnEgg");
                                }
                                else {
                                    desc += stringsAndOtherData.cleanUpTitleString(output5.toString());
                                }
                                lores_line2[special_index] = desc;
                                break;
                            default:
                                break;
                        }
                        break;
                    case "digging":
                        special_index = 0;
                        ArrayList<Object> diggingInfo = loadConfig.getDiggingInfo();
                        switch (skill_1a_level) {
                            case 0:
                                desc = lang.getString("diggingPerkDesc0_1") + " ";
                                Material mat0 = (Material) diggingInfo.get(10);
                                String itemName0 = "";
                                if (mat0 != null) {
                                    itemName0 = mat0.toString().replaceAll("_", " ").toLowerCase();
                                }
                                else {
                                    itemName0 = "gold ingot";
                                }
                                desc += itemName0;
                                lores_line2[special_index] = desc;
                                break;
                            case 1:
                                desc = lang.getString("diggingPerkDesc0_1")+ " ";
                                Material mat1 = (Material) diggingInfo.get(12);
                                String itemName1 = "";
                                if (mat1 != null) {
                                    itemName1 = mat1.toString().replaceAll("_", " ").toLowerCase();
                                }
                                else {
                                    itemName1 = "name tag";
                                }
                                desc += itemName1;
                                lores_line2[special_index] = desc;
                                break;
                            case 2:
                                desc = lang.getString("diggingPerkDesc0_1")+ " ";
                                Material mat2 = (Material) diggingInfo.get(14);
                                String itemName2 = "";
                                if (mat2 != null) {
                                    itemName2 = mat2.toString().replaceAll("_", " ").toLowerCase();
                                }
                                else {
                                    itemName2 = "music discs";
                                }
                                desc += itemName2;
                                lores_line2[special_index] = desc;
                                break;
                            case 3:
                                desc = lang.getString("diggingPerkDesc0_1")+ " ";
                                Material mat3 = (Material) diggingInfo.get(16);
                                String itemName3 = "";
                                if (mat3 != null) {
                                    itemName3 = mat3.toString().replaceAll("_", " ").toLowerCase();
                                }
                                else {
                                    itemName3 = "horse armor";
                                }
                                desc += itemName3;
                                lores_line2[special_index] = desc;
                                break;
                            case 4:
                                desc = lang.getString("diggingPerkDesc0_1")+ " ";
                                Material mat4 = (Material) diggingInfo.get(18);
                                String itemName4 = "";
                                if (mat4 != null) {
                                    itemName4 = mat4.toString().replaceAll("_", " ").toLowerCase();
                                }
                                else {
                                    itemName4 = "diamond";
                                }
                                desc += itemName4;
                                lores_line2[special_index] = desc;
                                break;
                            default:
                                break;
                        }
                        switch (skill_2a_level) {
                            case 0:
                                desc = lang.getString("diggingPerkDesc0_1")+ " ";
                                Material mat5 = (Material) diggingInfo.get(21);
                                String itemName5 = "";
                                if (mat5 != null) {
                                    itemName5 = mat5.toString().replaceAll("_", " ").toLowerCase();
                                }
                                else {
                                    itemName5 = "emerald";
                                }
                                desc += itemName5;
                                lores_line2[2] = desc;
                                break;
                            case 1:
                                desc = lang.getString("diggingPerkDesc0_1")+ " ";
                                Material mat6 = (Material) diggingInfo.get(24);
                                String itemName6 = "";
                                if (mat6 != null) {
                                    itemName6 = mat6.toString().replaceAll("_", " ").toLowerCase();
                                }
                                else {
                                    itemName6 = "enchanted book";
                                }
                                desc += itemName6;
                                lores_line2[2] = desc;
                                break;
                            case 2:
                                desc = lang.getString("diggingPerkDesc0_1")+ " ";
                                Material mat7 = (Material) diggingInfo.get(27);
                                String itemName7 = "";
                                if (mat7 != null) {
                                    itemName7 = mat7.toString().replaceAll("_", " ").toLowerCase();
                                }
                                else {
                                    itemName7 = "dragon breath";
                                }
                                desc += itemName7;
                                lores_line2[2] = desc;
                                break;
                            case 3:
                                desc = lang.getString("diggingPerkDesc0_1")+ " ";
                                Material mat8 = (Material) diggingInfo.get(30);
                                String itemName8 = "";
                                if (mat8 != null) {
                                    itemName8 = mat8.toString().replaceAll("_", " ").toLowerCase();
                                }
                                else {
                                    itemName8 = "totem of undying";
                                }
                                desc += itemName8;
                                lores_line2[2] = desc;
                                break;
                            case 4:
                                desc = lang.getString("diggingPerkDesc0_1")+ " ";
                                Material mat9 = (Material) diggingInfo.get(30);
                                String itemName9 = "";
                                if (mat9 != null) {
                                    itemName9 = mat9.toString().replaceAll("_", " ").toLowerCase();
                                }
                                else {
                                    itemName9 = "nether star";
                                }
                                desc += itemName9;
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
                                desc = lang.getString("defensePerkDesc0_1");
                                lores_line2[special_index] = desc;
                                break;
                            case 3:
                            case 4:
                                desc = lang.getString("defensePerkDesc0_2");
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
                    lores_line1[i] = ChatColor.GRAY +lang.getString("level") +" " + ChatColor.GREEN + level + "/5";
                }
                for (int i = 0; i < 3; i++) {
                    String level = pStats.get(11+i).toString();
                    if (i != 2) {
                        lores_line1[i + 4] = ChatColor.GRAY + lang.getString("level") +" " + ChatColor.BLUE + level + "/1";
                    }
                    else {
                        lores_line1[i + 4] = ChatColor.GRAY + lang.getString("level") +" " + ChatColor.DARK_PURPLE + level + "/1";
                    }
                }

                Integer[] indices = {11,29,13,31,7,43,26};
                //
                for (int i = 0; i < labels.length; i++) {
                    ItemMeta meta = menu_items[i].getItemMeta();
                    meta.setDisplayName(ChatColor.BOLD + labels[i]);
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(lores_line1[i]);
                    String longString = lores_line2[i];
                    ArrayList<String> splitDescs = stringsAndOtherData.getStringLines(longString);
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
                lores_line1_2[0] = ChatColor.BLUE + lang.getString("total") +": " + ChatColor.GOLD + String.valueOf(tokens_P);
                if (tokens_P > 1 && tokens_P < 64){
                    passive_token.setAmount(tokens_P);
                }
                else if (tokens_P >= 64){
                    passive_token.setAmount(64);
                }

                //Total skill tokens
                lores_line1_2[2] = ChatColor.BLUE + lang.getString("total") +": " + ChatColor.GOLD + String.valueOf(tokens_S);
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
                lores_line1_2[3] = ChatColor.GRAY + lang.getString("duration") +": " + ChatColor.AQUA + String.valueOf(duration) + " s";
                switch (skillName){
                    case "digging":
                        //Passive2
                        chance1 = 1 + chance1*0.01;
                        chance1 = Math.round(chance1*1000)/1000.0d;
                        lores_line1_2[4] = ChatColor.GRAY + lang.getString("likelihood") +": " + ChatColor.AQUA + String.valueOf(chance1) + "%";

                        //Passive 3
                        break;
                    case "woodcutting":
                    case "archery":
                        //Passive 2
                        chance1 = chance1*0.05;
                        chance1 = Math.round(chance1*1000)/1000.0d;
                        lores_line1_2[4] = ChatColor.GRAY + lang.getString("likelihood") +": " + ChatColor.AQUA + String.valueOf(chance1) + "%";

                        //Passive 3
                        break;

                    //Passive 3
                    case "mining":
                        //Passive 2
                        chance1 = chance1*0.05;
                        chance1 = Math.round(chance1*1000)/1000.0d;
                        lores_line1_2[4] = ChatColor.GRAY + lang.getString("likelihood") +": " + ChatColor.AQUA + String.valueOf(chance1) + "%";

                        //Passive 3
                        chance2 = chance2*0.01;
                        chance2 = Math.round(chance2*1000)/1000.0d;
                        lores_line1_2[5] = ChatColor.GRAY + lang.getString("likelihood") +": " + ChatColor.AQUA + String.valueOf(chance2) + "%";
                        break;
                    case "farming":
                        //Passive 2
                        chance1 = chance1*0.05;
                        chance1 = Math.round(chance1*1000)/1000.0d;
                        lores_line1_2[4] = ChatColor.GRAY + lang.getString("likelihood") +": " + ChatColor.AQUA + String.valueOf(chance1) + "%";

                        //Passive 3
                        chance2 = chance2*0.05;
                        chance2 = Math.round(chance2*1000)/1000.0d;
                        lores_line1_2[5] = ChatColor.GRAY + lang.getString("likelihood") +": " + ChatColor.AQUA + String.valueOf(chance2) + "%";
                        break;
                    case "fishing":
                        //Passive 1
                        duration = duration/2.0;
                        duration = Math.round(duration*1000)/1000.0d;
                        lores_line1_2[3] = ChatColor.GRAY + lang.getString("duration") +": " + ChatColor.AQUA + String.valueOf(duration) + " s";

                        //Passive 2
                        chance1 = chance1*0.05;
                        chance1 = Math.round(chance1*1000)/1000.0d;
                        lores_line1_2[4] = ChatColor.GRAY + lang.getString("likelihood") +": " + ChatColor.AQUA + String.valueOf(chance1) + "%";

                        //Passive 3
                        chance2 = 10 - chance2*0.005;
                        chance2 = Math.round(chance2*1000)/1000.0d;
                        lores_line1_2[5] = ChatColor.GRAY + lang.getString("junkChance") +": " + ChatColor.AQUA + String.valueOf(chance2) + "%";
                        break;

                    //Passive 3
                    case "beastMastery":
                        //Passive 2
                        chance1 = chance1*0.025;
                        chance1 = Math.round(chance1*1000)/1000.0d;
                        lores_line1_2[4] = ChatColor.GRAY + lang.getString("likelihood") +": " + ChatColor.AQUA + String.valueOf(chance1) + "%";

                        //Passive 3
                        break;
                    case "swordsmanship":
                        //Passive 2
                        chance1 = chance1*0.02;
                        chance1 = Math.round(chance1*1000)/1000.0d;
                        lores_line1_2[4] = ChatColor.GRAY + lang.getString("likelihood") +": " + ChatColor.AQUA + String.valueOf(chance1) + "%";

                        //Passive 3
                        break;
                    case "defense":
                        //Passive 2
                        chance1 = 1 + chance1*0.01;
                        chance1 = Math.round(chance1*1000)/1000.0d;
                        lores_line1_2[4] = ChatColor.GRAY + lang.getString("likelihood") +": " + ChatColor.AQUA + String.valueOf(chance1) + "%";

                        //Passive 3
                        chance2 = chance2*0.05;
                        chance2 = Math.round(chance2*1000)/1000.0d;
                        lores_line1_2[5] = ChatColor.GRAY + lang.getString("likelihood") +": " + ChatColor.AQUA + String.valueOf(chance2) + "%";

                        break;


                    case "axeMastery":
                        //Passive 2
                        chance1 = chance1*0.01;
                        chance1 = Math.round(chance1*1000)/1000.0d;
                        lores_line1_2[4] = ChatColor.GRAY + lang.getString("likelihood") +": " + ChatColor.AQUA + String.valueOf(chance1) + "%";

                        //Passive 3
                        break;

                    //Passive 3
                    default:
                        break;
                }

                Integer[] indices_2 = {0,45,9,18,27,36};
                for (int i = 0; i < labels_2.length; i++) {
                    ItemMeta meta = menu_items_2[i].getItemMeta();
                    meta.setDisplayName(ChatColor.BOLD + labels_2[i]);
                    ArrayList<String> lore = new ArrayList<>();
                    String longString = lores_line2_2[i];
                    ArrayList<String> splitDescs = stringsAndOtherData.getStringLines(longString);

                    if (i == 3) {
                        splitDescs.add("");
                        splitDescs.add(ChatColor.UNDERLINE + lang.getString("abilityDescription"));
                        ArrayList<String> appendingDesc = stringsAndOtherData.getStringLines(lang.getString("abilityDescription_"+skillName));
                        splitDescs.addAll(appendingDesc);
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
                    String[] craftingNames = {lang.getString("cowEgg"),lang.getString("beeEgg"),lang.getString("mooshroomEgg"),lang.getString("horseEgg"),lang.getString("slimeEgg")};
                    Material[] defaultMaterials = {Material.COW_SPAWN_EGG,Material.BEE_SPAWN_EGG,Material.MOOSHROOM_SPAWN_EGG,Material.HORSE_SPAWN_EGG,Material.SLIME_SPAWN_EGG};
                    for (int i = 0; i < craftingNames.length; i++) {
                        int stringIndex = i+1;
                        Material output = customRecipeMap.get("farming"+stringIndex).getOutput();
                        if (!output.equals(defaultMaterials[i])) {
                            craftingNames[i] = stringsAndOtherData.cleanUpTitleString(output.toString());
                        }
                    }
                    int animalFarmLevel = (int) pStats.get(8);
                    for (int i=0; i < craftingNames.length; i++) {
                        ArrayList<String> lore = new ArrayList<>();
                        ItemMeta craftingMeta = crafting[i].getItemMeta();
                        if ( animalFarmLevel >= i+1) {
                            lore.add(ChatColor.GREEN + ChatColor.ITALIC.toString() + lang.getString("unlocked") );
                        }
                        else {
                            lore.add(ChatColor.RED + ChatColor.ITALIC.toString() + lang.getString("locked") );
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
                    String[] craftingNames = {lang.getString("tippedArrows")};
                    int dragonlessArrowsLevel = (int) pStats.get(11);
                    for (int i=0; i < craftingNames.length; i++) {
                        ArrayList<String> lore = new ArrayList<>();
                        ItemMeta craftingMeta = crafting[i].getItemMeta();
                        if ( dragonlessArrowsLevel >= 1) {
                            lore.add(ChatColor.GREEN + ChatColor.ITALIC.toString() + lang.getString("unlocked") );
                        }
                        else {
                            lore.add(ChatColor.RED + ChatColor.ITALIC.toString() + lang.getString("locked") );
                        }
                        craftingMeta.setDisplayName(ChatColor.BOLD + craftingNames[i]);
                        craftingMeta.setLore(lore);
                        crafting[i].setItemMeta(craftingMeta);
                        gui.setItem(indices_crafting[i],crafting[i]);
                    }
                }

                //Soul Bucket (Refunding)
                ItemStack soul = new ItemStack(Material.COMPOSTER);
                ItemMeta soulMeta = soul.getItemMeta();
                ArrayList<String> soulLore = new ArrayList<>();
                soulMeta.setDisplayName(ChatColor.BOLD + lang.getString("refundSkillTitle"));
                if ((int) pStatAll.get("global").get(9) < 1) {
                    soulLore.add(ChatColor.RED + ChatColor.ITALIC.toString() + lang.getString("locked") );
                }
                else {
                    int souls = (int) pStatAll.get("global").get(20);
                    String soulsString = lang.getString("souls");
                    String soulsCapitilized = UtilityMethods.capitalizeString(soulsString);
                    soulLore.add(soulsCapitilized +": " + ChatColor.AQUA + ChatColor.ITALIC.toString() + souls + "/" + refundCost);
                    soulLore.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + lang.getString("refundSkillTreeDesc"));
                    soulLore.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + "(-"+refundCost+" "+ lang.getString("souls")+ ")");
                }
                soulMeta.setLore(soulLore);
                soul.setItemMeta(soulMeta);
                gui.setItem(47,soul);

                //Configuration Menu:
                ItemStack configItem = new ItemStack(Material.REDSTONE);
                ItemMeta configItemMeta = configItem.getItemMeta();
                ArrayList<String> configItemLore = new ArrayList<>();
                configItemMeta.setDisplayName(ChatColor.BOLD + lang.getString("configuration"));
                configItemLore.addAll(stringsAndOtherData.getStringLines(lang.getString("skillConfigDesc")));
                configItemMeta.setLore(configItemLore);
                configItem.setItemMeta(configItemMeta);
                gui.setItem(53,configItem);


                //Connectors
                ItemStack connector = new ItemStack(Material.GLASS_PANE);
                ItemMeta connectorMeta = connector.getItemMeta();
                connectorMeta.setDisplayName(ChatColor.WHITE.toString());
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
                LanguageSelector lang = new LanguageSelector(p);
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
                        StringsAndOtherData stringsAndOtherData = new StringsAndOtherData();
                        switch (skill_2a_level) {
                            case 0:
                                desc = lang.getString("alchemyPerkDesc1_0") + " ";
                                desc += stringsAndOtherData.getPotionEffectTypeString(1,p);
                                lores_line2[special_index] = desc;
                                break;
                            case 1:
                                desc = lang.getString("alchemyPerkDesc1_0") + " ";
                                desc += stringsAndOtherData.getPotionEffectTypeString(2,p);
                                lores_line2[special_index] = desc;
                                break;
                            case 2:
                                desc = lang.getString("alchemyPerkDesc1_0") + " ";
                                desc += stringsAndOtherData.getPotionEffectTypeString(3,p);
                                lores_line2[special_index] = desc;
                                break;
                            case 3:
                                desc = lang.getString("alchemyPerkDesc1_0") + " ";
                                desc += stringsAndOtherData.getPotionEffectTypeString(4,p);
                                lores_line2[special_index] = desc;
                                break;
                            case 4:
                                desc = lang.getString("alchemyPerkDesc1_0") + " ";
                                desc += stringsAndOtherData.getPotionEffectTypeString(5,p);
                                lores_line2[special_index] = desc;
                                break;
                            default:
                                break;
                        }
                        switch (skill_1a_level) {
                            case 0:
                                desc = lang.getString("alchemyPerkDesc0_0") + " ";
                                desc += stringsAndOtherData.getPotionTypeString(1,p);
                                lores_line2[0] = desc;
                                break;
                            case 1:
                                desc = lang.getString("alchemyPerkDesc0_0") + " ";
                                desc += stringsAndOtherData.getPotionTypeString(2,p);
                                lores_line2[0] = desc;
                                break;
                            case 2:
                                desc = lang.getString("alchemyPerkDesc0_0") + " ";
                                desc += stringsAndOtherData.getPotionTypeString(3,p);
                                lores_line2[0] = desc;
                                break;
                            case 3:
                                desc = lang.getString("alchemyPerkDesc0_0") + " ";
                                desc += stringsAndOtherData.getPotionTypeString(4,p);
                                lores_line2[0] = desc;
                                break;
                            case 4:
                                desc = lang.getString("alchemyPerkDesc0_0") + " ";
                                desc += stringsAndOtherData.getPotionTypeString(5,p);
                                lores_line2[0] = desc;
                                break;
                            default:
                                break;
                        }
                        break;
                    case "enchanting":
                        special_index = 1;
                        desc = lang.getString("enchantingPerkDesc1_0")+" ";
                        StringsAndOtherData stringsAndOtherData1 = new StringsAndOtherData();
                        switch (skill_2a_level) {
                            case 0:
                                desc += stringsAndOtherData1.getEnchantmentPerkDescString(1,p);
                                lores_line2[special_index] = desc;
                                break;
                            case 1:
                                desc += stringsAndOtherData1.getEnchantmentPerkDescString(2,p);
                                lores_line2[special_index] = desc;
                                break;
                            case 2:
                                desc += stringsAndOtherData1.getEnchantmentPerkDescString(3,p);
                                lores_line2[special_index] = desc;
                                break;
                            case 3:
                                desc += stringsAndOtherData1.getEnchantmentPerkDescString(4,p);
                                lores_line2[special_index] = desc;
                                break;
                            case 4:
                                desc += stringsAndOtherData1.getEnchantmentPerkDescString(5,p);
                                lores_line2[special_index] = desc;
                                break;
                            default:
                                break;
                        }
                        break;
                    case "repair":
                        special_index = 0;
                        switch (skill_1a_level) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                                desc += lang.getString("repairPerkDesc0_1");
                                lores_line2[special_index] = desc;
                                break;
                            case 4:
                                desc += lang.getString("repairPerkDesc0_2");
                                lores_line2[special_index] = desc;
                                break;
                            default:
                                break;
                        }
                        break;
                }
                String[] lores_line1 = {"Level: 0/5","Level: 0/5","Level: 0/1"};
                String level = pStats.get(7).toString();
                lores_line1[0] = ChatColor.GRAY + lang.getString("level") + " " + ChatColor.GREEN + level + "/5";
                level = pStats.get(9).toString();
                lores_line1[1] = ChatColor.GRAY + lang.getString("level") + " " + ChatColor.GREEN + level + "/5";
                level = pStats.get(13).toString();
                lores_line1[2] = ChatColor.GRAY + lang.getString("level") + " " + ChatColor.DARK_PURPLE + level + "/1";

                Integer[] indices = {20,23,26};
                //Set skills
                for (int i = 0; i < labels.length; i++) {
                    ItemMeta meta = menu_items[i].getItemMeta();
                    meta.setDisplayName(ChatColor.BOLD + labels[i]);
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(lores_line1[i]);
                    String longString = lores_line2[i];
                    StringsAndOtherData stringsAndOtherData = new StringsAndOtherData();
                    ArrayList<String> splitDescs = stringsAndOtherData.getStringLines(longString);
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
                lores_line1_2[1] = ChatColor.BLUE + lang.getString("total")+": " + ChatColor.GOLD + String.valueOf(tokens_S);
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
                        lores_line1_2[2] = ChatColor.GRAY + lang.getString("level")+": " + ChatColor.AQUA + String.valueOf(passive);
                        break;
                    case "agility":
                        passive = passive*0.05;
                        passive = Math.round(passive*1000)/1000.0d;
                        lores_line1_2[2] = ChatColor.GRAY + lang.getString("likelihood")+": " + ChatColor.AQUA + String.valueOf(passive)+"%";
                        break;
                    case "enchanting":
                        passive = passive*0.2;
                        passive = Math.round(passive*1000)/1000.0d;
                        lores_line1_2[2] = ChatColor.GRAY + lang.getString("xpBoost")+": " + ChatColor.AQUA + "+"+String.valueOf(passive)+"%";
                        break;
                    case "smelting":
                        passive = passive*0.2;
                        passive = Math.round(passive*1000)/1000.0d;
                        lores_line1_2[2] = ChatColor.GRAY + lang.getString("speedBoost")+": " + ChatColor.AQUA + "+"+String.valueOf(passive)+"%";
                        break;
                    case "alchemy":
                        passive = passive*0.1;
                        passive = Math.round(passive*1000)/1000.0d;
                        lores_line1_2[2] = ChatColor.GRAY + lang.getString("timeExtension")+": " + ChatColor.AQUA + "+"+String.valueOf(passive)+"%";
                        break;
                    default:
                        break;
                }

                Integer[] indices_2 = {45,0,18};
                for (int i = 0; i < labels_2.length; i++) {
                    ItemMeta meta = menu_items_2[i].getItemMeta();
                    meta.setDisplayName(ChatColor.BOLD + labels_2[i]);
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(lores_line1_2[i]);
                    String longString = lores_line2_2[i];
                    StringsAndOtherData stringsAndOtherData = new StringsAndOtherData();
                    ArrayList<String> splitDescs = stringsAndOtherData.getStringLines(longString);
                    for (int j = 0; j < splitDescs.size(); j++) {
                        lore.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + splitDescs.get(j));
                    };
                    meta.setLore(lore);
                    menu_items_2[i].setItemMeta(meta);
                    gui.setItem(indices_2[i], menu_items_2[i]);
                }

                //Crafting
                if (skillName.equalsIgnoreCase("enchanting")) {
                    StringsAndOtherData stringsAndOtherData = new StringsAndOtherData();
                    Integer[] indices_crafting = {39,40,41,42,43,48,49,50,51,52};
                    ItemStack[] crafting = {new ItemStack(Material.CRAFTING_TABLE),new ItemStack(Material.CRAFTING_TABLE),
                            new ItemStack(Material.CRAFTING_TABLE),new ItemStack(Material.CRAFTING_TABLE),
                            new ItemStack(Material.CRAFTING_TABLE),new ItemStack(Material.CRAFTING_TABLE),
                            new ItemStack(Material.CRAFTING_TABLE),new ItemStack(Material.CRAFTING_TABLE),
                            new ItemStack(Material.CRAFTING_TABLE),new ItemStack(Material.CRAFTING_TABLE)};
                    String[] craftingNames = stringsAndOtherData.getEnchantingCraftingNames(p);
                    int bookSmartLevel = (int) pStats.get(9);
                    for (int i=0; i < craftingNames.length; i++) {
                        ArrayList<String> lore = new ArrayList<>();
                        ItemMeta craftingMeta = crafting[i].getItemMeta();
                        if ( bookSmartLevel >= Math.ceil((double) (i+1)/2.0)) {
                            lore.add(ChatColor.GREEN + ChatColor.ITALIC.toString() + lang.getString("unlocked") );
                        }
                        else {
                            lore.add(ChatColor.RED + ChatColor.ITALIC.toString() + lang.getString("locked") );
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
                    StringsAndOtherData stringsAndOtherData = new StringsAndOtherData();
                    String[] craftingNames = {stringsAndOtherData.getPotionTypeString(1,p),
                            stringsAndOtherData.getPotionTypeString(2,p),
                            stringsAndOtherData.getPotionTypeString(3,p),
                            stringsAndOtherData.getPotionTypeString(4,p),
                            stringsAndOtherData.getPotionTypeString(5,p)};
                    int alchemicalSummoningLevel = (int) pStats.get(7);
                    for (int i=0; i < craftingNames.length; i++) {
                        ArrayList<String> lore = new ArrayList<>();
                        ItemMeta craftingMeta = crafting[i].getItemMeta();
                        if ( alchemicalSummoningLevel > i) {
                            lore.add(ChatColor.GREEN + ChatColor.ITALIC.toString() + lang.getString("unlocked") );
                        }
                        else {
                            lore.add(ChatColor.RED + ChatColor.ITALIC.toString() + lang.getString("locked") );
                        }
                        craftingMeta.setDisplayName(ChatColor.BOLD + craftingNames[i]);
                        craftingMeta.setLore(lore);
                        crafting[i].setItemMeta(craftingMeta);
                        gui.setItem(indices_crafting[i],crafting[i]);
                    }
                    Integer[] indices_brewing = {39,40,41,42,43};
                    ItemStack[] brewing = {new ItemStack(Material.IRON_BARS),new ItemStack(Material.IRON_BARS),
                            new ItemStack(Material.IRON_BARS),new ItemStack(Material.IRON_BARS),
                            new ItemStack(Material.IRON_BARS)};
                    ItemGroups itemGroups = new ItemGroups();
                    List<Material> brewingUnlocked = itemGroups.getNewIngredients();
                    int ancientKnowledgeLevel = (int) pStats.get(9);
                    for (int i=0; i < brewing.length; i++) {
                        String lvl = String.valueOf(i+1);
                        ArrayList<String> lore = new ArrayList<>();
                        ItemStack brewingItem = brewing[i];
                        ItemMeta brewingMeta = brewingItem.getItemMeta();
                        brewingMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                        brewingMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        brewingMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        String brewingName = lang.getString("alchemyPerkTitle1") + " " + lang.getString("lvl") + " " + lvl + " " + lang.getString("ingredient");
                        if (ancientKnowledgeLevel > i) {
                            brewingItem.setType(brewingUnlocked.get(i));
                            lore.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + lang.getString("usedToBrew") );
                            lore.add(ChatColor.GRAY + stringsAndOtherData.getPotionEffectTypeString(i+1,p));
                        }
                        else {
                            lore.add(ChatColor.RED + ChatColor.ITALIC.toString() + lang.getString("locked") );
                        }
                        brewingMeta.setDisplayName(ChatColor.WHITE + ChatColor.BOLD.toString() +brewingName);
                        brewingMeta.setLore(lore);
                        brewingItem.setItemMeta(brewingMeta);
                        gui.setItem(indices_brewing[i],brewingItem);

                    }
                }

                //Souls (refunding)
                ItemStack soul = new ItemStack(Material.COMPOSTER);
                ItemMeta soulMeta = soul.getItemMeta();
                ArrayList<String> soulLore = new ArrayList<>();
                soulMeta.setDisplayName(ChatColor.BOLD + lang.getString("refundSkillTitle"));
                if ((int) pStatAll.get("global").get(9) < 1) {
                    soulLore.add(ChatColor.RED + ChatColor.ITALIC.toString() + lang.getString("locked") );
                }
                else {
                    int souls = (int) pStatAll.get("global").get(20);
                    String soulsString = lang.getString("souls");
                    String soulsCapitilized = UtilityMethods.capitalizeString(soulsString);
                    soulLore.add(soulsCapitilized +": " + ChatColor.AQUA + ChatColor.ITALIC.toString() + souls + "/" + refundCost);
                    soulLore.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + lang.getString("refundSkillTreeDesc"));
                    soulLore.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + "(-"+refundCost+" "+ lang.getString("souls")+ ")");
                }
                soulMeta.setLore(soulLore);
                soul.setItemMeta(soulMeta);
                gui.setItem(47,soul);

                ItemStack configItem = new ItemStack(Material.REDSTONE);
                ItemMeta configItemMeta = configItem.getItemMeta();
                ArrayList<String> configItemLore = new ArrayList<>();
                configItemMeta.setDisplayName(ChatColor.BOLD + lang.getString("configuration"));
                StringsAndOtherData stringsAndOtherData = new StringsAndOtherData();
                configItemLore.addAll(stringsAndOtherData.getStringLines(lang.getString("skillConfigDesc")));
                configItemMeta.setLore(configItemLore);
                configItem.setItemMeta(configItemMeta);
                gui.setItem(53,configItem);

                //Connectors
                ItemStack connector = new ItemStack(Material.GLASS_PANE);
                ItemMeta connectorMeta = connector.getItemMeta();
                connectorMeta.setDisplayName(ChatColor.WHITE.toString());
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
                LanguageSelector lang = new LanguageSelector(p);
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
                    lores_line1[i] = ChatColor.GRAY + lang.getString("level")+" " + ChatColor.BLUE + level + "/1";
                    if (i==9) {
                        lores_line1[i] = ChatColor.GRAY + lang.getString("level")+" " + ChatColor.DARK_PURPLE + level + "/1";
                    }
                }

                Integer[] indices = {1,19,37,3,21,39,6,24,42,26};
                //
                for (int i = 0; i < labels.length; i++) {
                    ItemMeta meta = menu_items[i].getItemMeta();
                    meta.setDisplayName(ChatColor.BOLD + labels[i]);
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(lores_line1[i]);
                    String longString = lores_line2[i];
                    StringsAndOtherData stringsAndOtherData = new StringsAndOtherData();
                    ArrayList<String> splitDescs = stringsAndOtherData.getStringLines(longString);
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
                lores_line1_2[0] = ChatColor.BLUE + lang.getString("total")+": " + ChatColor.GOLD + String.valueOf(tokens_G);
                if (tokens_G > 1 && tokens_G < 64){
                    g_token.setAmount(tokens_G);
                }
                else if (tokens_G >= 64){
                    g_token.setAmount(64);
                }
                Integer[] indices_2 = {0,45};
                for (int i = 0; i < labels_2.length; i++) {
                    ItemMeta meta = menu_items_2[i].getItemMeta();
                    meta.setDisplayName(ChatColor.BOLD + labels_2[i]);
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(lores_line1_2[i]);
                    String longString = lores_line2_2[i];
                    StringsAndOtherData stringsAndOtherData = new StringsAndOtherData();
                    ArrayList<String> splitDescs = stringsAndOtherData.getStringLines(longString);
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
                connectorMeta.setDisplayName(ChatColor.WHITE.toString());
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

        else {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                LanguageSelector lang = new LanguageSelector(p);
                p.sendMessage(ChatColor.RED + lang.getString("unknownCommand"));
            }
            else {
                System.out.println("Unknown command");
            }
        }

        return true;
    }
}
