package mc.carlton.freerpg.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.config.ConfigLoad;
import mc.carlton.freerpg.core.info.player.AbilityTimers;
import mc.carlton.freerpg.core.info.player.ChangeStats;
import mc.carlton.freerpg.core.info.player.Leaderboards;
import mc.carlton.freerpg.core.info.player.OfflinePlayerStatLoadIn;
import mc.carlton.freerpg.core.info.player.PlayerLeaderboardStat;
import mc.carlton.freerpg.core.info.player.PlayerStats;
import mc.carlton.freerpg.core.info.player.PlayerStatsLoadIn;
import mc.carlton.freerpg.core.serverFileManagement.PeriodicSaving;
import mc.carlton.freerpg.core.serverFileManagement.PlayerStatsFilePreparation;
import mc.carlton.freerpg.customContainers.collections.OldCustomRecipe;
import mc.carlton.freerpg.skills.perksAndAbilities.Agility;
import mc.carlton.freerpg.utils.UtilityMethods;
import mc.carlton.freerpg.utils.game.LanguageSelector;
import mc.carlton.freerpg.utils.game.PsuedoEnchanting;
import mc.carlton.freerpg.utils.globalVariables.CraftingRecipes;
import mc.carlton.freerpg.utils.globalVariables.ItemGroups;
import mc.carlton.freerpg.utils.globalVariables.StringsAndOtherData;
import mc.carlton.freerpg.utils.gui.GuiDisplayStatistic;
import mc.carlton.freerpg.utils.gui.GuiItem;
import mc.carlton.freerpg.utils.gui.GuiWrapper;
import org.apache.logging.log4j.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class FrpgCommands implements CommandExecutor {

  private boolean isTargetOnline(Player target, CommandSender sender) {
    if (target == null) {
      if (sender instanceof Player) {
        Player p = (Player) sender;
        LanguageSelector lang = new LanguageSelector(p);
        p.sendMessage(ChatColor.RED + lang.getString("playerOffline"));
      } else {
        sender.sendMessage("Player not online");
      }
      return false;
    }
    return true;
  }

  private int getArgumentAsInteger(String arg) {
    int integerValueOfArg = 0;
    try {
      integerValueOfArg = Integer.valueOf(arg);
      return integerValueOfArg;
    } catch (NumberFormatException e) {
      return Integer.MAX_VALUE; //This is a cheap trick; when we return max value, that's our way of saying "could not convert" without throwing error
    }
  }

  private double getArgumentAsDouble(String arg) {
    double doubleValueOfArg = 0;
    try {
      doubleValueOfArg = Double.valueOf(arg);
      return doubleValueOfArg;
    } catch (NumberFormatException e) {
      return Double.MAX_VALUE; //This is a cheap trick; when we return max value, that's our way of saying "could not convert" without throwing error
    }
  }

  private boolean togglePerk(String id, CommandSender sender, String[] args) {
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

    final String IMPROPER_ARGUMENTS_MESSSAGE = " /frpg " + id;
    CommandHelper commandHelper = new CommandHelper(sender, args, 1, 2,
        IMPROPER_ARGUMENTS_MESSSAGE);
    commandHelper.setPlayerOnlyCommand(true);
    commandHelper.setPermissionName(permission);
    if (!commandHelper.isProperCommand()) {
      return true; //Command Restricted or Improper
    }
    Player p = (Player) sender;

    LanguageSelector lang = new LanguageSelector(p);
    PlayerStats pStatClass = new PlayerStats(p);
    Agility agility = new Agility(p);
    Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
    Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
    int level = (int) pStat.get(skillName).get(skillIndex);

    if (level <= 0) {
      p.sendMessage(
          ChatColor.RED + lang.getString("unlockToggle") + " " + ChatColor.BOLD + lang.getString(
              langID));
      return true;
    }
    if (args.length == 1) {
      int toggle = (int) pStat.get("global").get(globalIndex);
      if (toggle > 0) {
        p.sendMessage(ChatColor.RED + lang.getString(langID) + ": " + lang.getString("off0"));
        pStat.get("global").set(globalIndex, 0);
        if (id.equalsIgnoreCase("speedToggle")) {
          agility.gracefulFeetEnd();
        }
      } else {
        p.sendMessage(ChatColor.GREEN + lang.getString(langID) + ": " + lang.getString("on0"));
        pStat.get("global").set(globalIndex, 1);
        if (id.equalsIgnoreCase("speedToggle")) {
          agility.gracefulFeetStart();
        }
      }
      statAll.put(p.getUniqueId(), pStat);
      pStatClass.setData(statAll);
    } else if (args.length == 2) {
      if (args[1].equalsIgnoreCase("off")) {
        p.sendMessage(ChatColor.RED + lang.getString(langID) + ": " + lang.getString("off0"));
        pStat.get("global").set(globalIndex, 0);
        statAll.put(p.getUniqueId(), pStat);
        pStatClass.setData(statAll);
        if (id.equalsIgnoreCase("speedToggle")) {
          agility.gracefulFeetEnd();
        }
      } else if (args[1].equalsIgnoreCase("on")) {
        p.sendMessage(ChatColor.GREEN + lang.getString(langID) + ": " + lang.getString("on0"));
        pStat.get("global").set(globalIndex, 1);
        statAll.put(p.getUniqueId(), pStat);
        pStatClass.setData(statAll);
        if (id.equalsIgnoreCase("speedToggle")) {
          agility.gracefulFeetStart();
        }
      } else {
        commandHelper.sendImproperArgumentsMessage();
      }
    }
    return true;
  }

  private void togglePerkSetGuiItem(String id, Player p, GuiWrapper gui) {
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

    String parsableName = "::" + langID + "::" + " " + "::toggle::";
    addToggleButton(p, gui, parsableName, icon, guiIndex, globalIndex);
    if (enchanted) {
      gui.getItem(guiIndex).addEnchantmentGlow();
    }
  }

  private String getExpToNextString(Player p, String skillName) {
    ChangeStats getEXP = new ChangeStats(p);
    PlayerStats pStatClass = new PlayerStats(p);
    Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();

    int maxLevel = getEXP.getMaxLevel(skillName);
    int EXP = pStat.get(skillName).get(1).intValue();
    int level = pStat.get(skillName).get(0).intValue();

    String EXPtoNextString;
    if (level < maxLevel) {
      int nextEXP = getEXP.getEXPfromLevel(level + 1);
      int EXPtoNext = nextEXP - EXP;
      EXPtoNextString = String.valueOf(EXPtoNext);
    } else {
      EXPtoNextString = "N/A";
    }
    return EXPtoNextString;
  }

  private int getTotalSkillTokens(Player p) {
    PlayerStats pStatClass = new PlayerStats(p);
    Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
    int totalSkillTokens = 0;
    for (String j : pStat.keySet()) {
      if (!j.equalsIgnoreCase("global")) {
        totalSkillTokens += pStat.get(j).get(3).intValue();
      }
    }
    return totalSkillTokens;
  }

  private int getTotalPassiveTokens(Player p) {
    PlayerStats pStatClass = new PlayerStats(p);
    Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
    int totalPassiveTokens = 0;
    for (String j : pStat.keySet()) {
      if (!j.equalsIgnoreCase("global")) {
        totalPassiveTokens += pStat.get(j).get(2).intValue();
      }
    }
    return totalPassiveTokens;
  }

  private void generateMainMenu(Player p) {
    //Player Information and Leaderboard
    LanguageSelector lang = new LanguageSelector(p);
    PlayerStats pStatClass = new PlayerStats(p);
    Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
    Leaderboards leaderboards = new Leaderboards();
    PlayerStats timeStats = new PlayerStats(p);

    //Gui Set-up
    GuiWrapper gui = new GuiWrapper(p, Bukkit.createInventory(p, 45, "Skills"));
    String nameColor = ChatColor.AQUA.toString() + ChatColor.BOLD.toString();
    String descriptionColor = ChatColor.GRAY.toString() + ChatColor.ITALIC.toString();
    String statisticNameColor = ChatColor.GRAY.toString();
    String statisticColor = ChatColor.BLUE.toString();

    //Menu Options (Items)
    GuiItem global = new GuiItem(Material.NETHER_STAR, 4, gui);
    GuiItem digging = new GuiItem(Material.IRON_SHOVEL, 11, gui);
    GuiItem woodcutting = new GuiItem(Material.IRON_AXE, 12, gui);
    GuiItem mining = new GuiItem(Material.IRON_PICKAXE, 13, gui);
    GuiItem farming = new GuiItem(Material.IRON_HOE, 14, gui);
    GuiItem fishing = new GuiItem(Material.FISHING_ROD, 15, gui);
    GuiItem archery = new GuiItem(Material.BOW, 20, gui);
    GuiItem beastMastery = new GuiItem(Material.BONE, 21, gui);
    GuiItem swords = new GuiItem(Material.IRON_SWORD, 22, gui);
    GuiItem armor = new GuiItem(Material.IRON_CHESTPLATE, 23, gui);
    GuiItem axes = new GuiItem(Material.GOLDEN_AXE, 24, gui);
    GuiItem repair = new GuiItem(Material.ANVIL, 29, gui);
    GuiItem agility = new GuiItem(Material.LEATHER_LEGGINGS, 30, gui);
    GuiItem alchemy = new GuiItem(Material.POTION, 31, gui);
    GuiItem smelting = new GuiItem(Material.COAL, 32, gui);
    GuiItem enchanting = new GuiItem(Material.ENCHANTING_TABLE, 33, gui);
    GuiItem info = new GuiItem(Material.MAP, 36, gui);
    GuiItem configuration = new GuiItem(Material.REDSTONE, 44, gui);

    GuiItem[] guiItems = {global, digging, woodcutting, mining, farming, fishing, archery,
        beastMastery, swords, armor, axes, repair, agility, alchemy, smelting, enchanting, info,
        configuration};
    String[] labels = {lang.getString("global"), lang.getString("digging"),
        lang.getString("woodcutting"), lang.getString("mining"), lang.getString("farming"),
        lang.getString("fishing"), lang.getString("archery"), lang.getString("beastMastery"),
        lang.getString("swordsmanship"), lang.getString("defense"), lang.getString("axeMastery"),
        lang.getString("repair"), lang.getString("agility"), lang.getString("alchemy"),
        lang.getString("smelting"), lang.getString("enchanting"), lang.getString("information"),
        lang.getString("configuration")};
    String[] labels0 = {"global", "digging", "woodcutting", "mining", "farming", "fishing",
        "archery", "beastMastery", "swordsmanship", "defense", "axeMastery", "repair", "agility",
        "alchemy", "smelting", "enchanting", "information", "configuration"};

    for (int i = 0; i < guiItems.length; i++) {
      GuiItem item = guiItems[i];
      String translatedLabel = labels[i];
      String label = labels0[i];
      item.setName(translatedLabel);
      item.setNameColor(nameColor);
      if (i == 0) { //Global
        int globalTokens = (int) pStat.get(labels0[i]).get(1);
        int totalLevel = pStat.get("global").get(0).intValue();
        int totalExperience = (int) pStat.get("global").get(29);
        int totalPlayers = leaderboards.getLeaderboardSize("global");
        int rank = leaderboards.getLeaderboardPosition(p, "global");

        item.addStatistic(lang.getString("total") + " " + lang.getString("level"),
            String.valueOf(totalLevel));
        item.addStatistic(lang.getString("total") + " " + lang.getString("exp"),
            String.valueOf(totalExperience));
        item.addSpecialLoreLine(statisticNameColor + lang.getString("rank") + ": " + ChatColor.WHITE
            + ChatColor.BOLD.toString() + "" + String.format("%,d", rank) + ChatColor.RESET
            + descriptionColor + " " + lang.getString("outOf") + " " + ChatColor.RESET
            + ChatColor.WHITE.toString() + totalPlayers);
        if (globalTokens > 0) {
          item.addEnchantmentGlow();
        }

      } else if (i < 16) { //Skills
        String skillName = labels0[i];
        int passiveTokens = (int) pStat.get(labels0[i]).get(2);
        int skillTokens = (int) pStat.get(labels0[i]).get(3);
        int level = pStat.get(labels0[i]).get(0).intValue();
        int EXP = pStat.get(labels0[i]).get(1).intValue();
        int totalPlayers = leaderboards.getLeaderboardSize(labels0[i]);
        int rank = leaderboards.getLeaderboardPosition(p, labels0[i]);
        String EXPtoNextString = getExpToNextString(p, skillName);

        GuiDisplayStatistic levelStat = new GuiDisplayStatistic(lang.getString("level"), level,
            ChatColor.GRAY.toString(), ChatColor.BLUE.toString());
        GuiDisplayStatistic expStat = new GuiDisplayStatistic(lang.getString("experience"), EXP,
            ChatColor.GRAY.toString(), ChatColor.BLUE.toString());
        GuiDisplayStatistic expToLevelStat = new GuiDisplayStatistic(lang.getString("expToLevel"),
            EXPtoNextString, ChatColor.GRAY.toString(), ChatColor.GREEN.toString());

        item.addStatistic(levelStat);
        item.addStatistic(expStat);
        item.addStatistic(expToLevelStat);
        item.addSpecialLoreLine(statisticNameColor + lang.getString("rank") + ": " + ChatColor.WHITE
            + ChatColor.BOLD.toString() + "" + String.format("%,d", rank) + ChatColor.RESET
            + descriptionColor + " " + lang.getString("outOf") + " " + ChatColor.RESET
            + ChatColor.WHITE.toString() + totalPlayers);
        if (passiveTokens > 0 || skillTokens > 0) {
          item.addEnchantmentGlow();
        }

      } else if (i == 16) { //Information
        int totalSkillTokens = getTotalSkillTokens(p);
        int totalPassiveTokens = getTotalPassiveTokens(p);
        int globalTokens = pStat.get("global").get(1).intValue();
        String playTimeString = timeStats.getPlayerPlayTimeString();
        double personalMultiplier = (double) pStat.get("global").get(23);
        int souls = (int) pStat.get("global").get(20);

        item.addStatistic(lang.getString("totalPlayTime"), playTimeString);
        item.addStatistic(lang.getString("personalMultiplier"),
            String.valueOf(personalMultiplier) + "x");
        item.addStatistic(lang.getString("globalPassiveTitle0"), globalTokens);
        item.addStatistic(lang.getString("diggingPassiveTitle2"), totalSkillTokens);
        item.addStatistic(lang.getString("diggingPassiveTitle0"), totalPassiveTokens);
        item.addStatistic(UtilityMethods.capitalizeString(lang.getString("souls")), souls);
      } else if (i == 17) {//Configuration
        item.setDescription(lang.getString("clickForOptions"));
      }
      gui.addItem(item);
    }

    //Put the items in the inventory
    gui.displayGuiForPlayer();
  }

  private void messagePlayerHelpScreen(CommandSender sender, int page) {
    if (sender instanceof Player) {
      Player p = (Player) sender;
      LanguageSelector lang = new LanguageSelector(p);
      final int TOTAL_PAGES = 4;
      if (page > TOTAL_PAGES || page < 1) {
        page = 1; //Page input out of bounds
      }
      p.sendMessage(
          ChatColor.RED + "------| " + ChatColor.GREEN + ChatColor.BOLD.toString() + " Help" +
              ChatColor.RESET + ChatColor.GREEN.toString() + " " + lang.getString("page") + " ["
              + Integer.toString(page) + "/" + Integer.toString(TOTAL_PAGES) + "]" +
              ChatColor.RED.toString() + " |-----");
      switch (page) {
        case 1:
          p.sendMessage(
              ChatColor.GOLD + "/frpg" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                  ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc0"));
          p.sendMessage(ChatColor.GOLD + "/frpg skillTreeGUI [" + lang.getString("skillName") + "]"
              + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
              ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc1"));
          p.sendMessage(ChatColor.GOLD + "/frpg configurationGUI" + ChatColor.RESET
              + ChatColor.GRAY.toString() + " - " +
              ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc2"));
          p.sendMessage(ChatColor.GOLD + "/frpg giveEXP [" + lang.getString("playerName") + "] ["
              + lang.getString("skillName") + "] [amount]" + ChatColor.RESET
              + ChatColor.GRAY.toString() + " - " +
              ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc3"));
          p.sendMessage(ChatColor.GOLD + "/frpg setLevel [" + lang.getString("playerName") + "] ["
              + lang.getString("skillName") + "] [" + lang.getString("level") + "]"
              + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
              ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc4"));
          p.sendMessage(ChatColor.GOLD + "/frpg statReset [" + lang.getString("playerName") + "] ["
              + lang.getString("skillName") + "]" + ChatColor.RESET + ChatColor.GRAY.toString()
              + " - " +
              ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc5"));
          p.sendMessage(ChatColor.GOLD + "/frpg statLeaders [" + lang.getString("skillName") + "] ["
              + lang.getString("page") + "]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
              ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc6"));
          p.sendMessage(
              ChatColor.GOLD + "/frpg info" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                  ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc7"));
          break;
        case 2:
          p.sendMessage(ChatColor.GOLD + "/frpg flintToggle [" + lang.getString("onOrOff") + "]"
              + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
              ChatColor.RESET + ChatColor.WHITE + lang.getString("manuallyToggles") + " "
              + lang.getString("diggingPerkTitle4"));
          p.sendMessage(ChatColor.GOLD + "/frpg speedToggle [" + lang.getString("onOrOff") + "]"
              + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
              ChatColor.RESET + ChatColor.WHITE + lang.getString("manuallyToggles") + " "
              + lang.getString("agilityPerkTitle2"));
          p.sendMessage(ChatColor.GOLD + "/frpg potionToggle [" + lang.getString("onOrOff") + "]"
              + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
              ChatColor.RESET + ChatColor.WHITE + lang.getString("manuallyToggles") + " "
              + lang.getString("alchemyPerkTitle2"));
          p.sendMessage(ChatColor.GOLD + "/frpg flamePickToggle [" + lang.getString("onOrOff") + "]"
              + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
              ChatColor.RESET + ChatColor.WHITE + lang.getString("manuallyToggles") + " "
              + lang.getString("smeltingPerkTitle2"));
          p.sendMessage(ChatColor.GOLD + "/frpg grappleToggle [" + lang.getString("onOrOff") + "]"
              + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
              ChatColor.RESET + ChatColor.WHITE + lang.getString("manuallyToggles") + " "
              + lang.getString("fishingPerkTitle4"));
          p.sendMessage(ChatColor.GOLD + "/frpg hotRodToggle [" + lang.getString("onOrOff") + "]"
              + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
              ChatColor.RESET + ChatColor.WHITE + lang.getString("manuallyToggles") + " "
              + lang.getString("fishingPerkTitle5"));
          p.sendMessage(ChatColor.GOLD + "/frpg veinMinerToggle [" + lang.getString("onOrOff") + "]"
              + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
              ChatColor.RESET + ChatColor.WHITE + lang.getString("manuallyToggles") + " "
              + lang.getString("miningPerkTitle4"));
          p.sendMessage(ChatColor.GOLD + "/frpg megaDigToggle [" + lang.getString("onOrOff") + "]"
              + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
              ChatColor.RESET + ChatColor.WHITE + lang.getString("manuallyToggles") + " "
              + lang.getString("diggingPerkTitle6"));
          break;
        case 3:
          p.sendMessage(
              ChatColor.GOLD + "/frpg leafBlowerToggle [" + lang.getString("onOrOff") + "]"
                  + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                  ChatColor.RESET + ChatColor.WHITE + lang.getString("manuallyToggles") + " "
                  + lang.getString("woodcuttingPerkTitle5"));
          p.sendMessage(ChatColor.GOLD + "/frpg holyAxeToggle [" + lang.getString("onOrOff") + "]"
              + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
              ChatColor.RESET + ChatColor.WHITE + lang.getString("manuallyToggles") + " "
              + lang.getString("axeMasteryPerkTitle1"));
          p.sendMessage(ChatColor.GOLD + "/frpg enchantItem [" + lang.getString("level") + "]"
              + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
              ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc8"));
          p.sendMessage(
              ChatColor.GOLD + "/frpg setSouls" + " [" + lang.getString("playerName") + "]" + " ["
                  + lang.getString("amount") + "]" + ChatColor.RESET + ChatColor.GRAY.toString()
                  + " - " +
                  ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc9"));
          p.sendMessage(
              ChatColor.GOLD + "/frpg setTokens" + " [" + lang.getString("playerName") + "]" + " ["
                  + lang.getString("skillName") + "]" + " [skill/passive]" + " [" + lang.getString(
                  "amount") + "]" + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
                  ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc10"));
          p.sendMessage(
              ChatColor.GOLD + "/frpg setTokens" + " [" + lang.getString("playerName") + "]"
                  + " global" + " [" + lang.getString("amount") + "]" + ChatColor.RESET
                  + ChatColor.GRAY.toString() + " - " +
                  ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc11"));
          p.sendMessage(ChatColor.GOLD + "/frpg saveStats [" + lang.getString("playerName") + "]"
              + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
              ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc12"));
          p.sendMessage(
              ChatColor.GOLD + "/frpg setMultiplier [" + lang.getString("playerName") + "] " + "["
                  + lang.getString("amount") + "]" + ChatColor.RESET + ChatColor.GRAY.toString()
                  + " - " +
                  ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc13"));
          break;
        case 4:
          p.sendMessage(
              ChatColor.GOLD + "/frpg resetCooldown [" + lang.getString("playerName") + "] ["
                  + lang.getString("skillName") + "]" + ChatColor.RESET + ChatColor.GRAY.toString()
                  + " - " +
                  ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc14"));
          p.sendMessage(ChatColor.GOLD + "/frpg statLookup [" + lang.getString("playerName") + "]"
              + ChatColor.RESET + ChatColor.GRAY.toString() + " - " +
              ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc15"));
          p.sendMessage(
              ChatColor.GOLD + "/frpg changeMultiplier [" + lang.getString("playerName") + "] "
                  + "[" + lang.getString("expIncrease") + "]" + ChatColor.RESET
                  + ChatColor.GRAY.toString() + " - " +
                  ChatColor.RESET + ChatColor.WHITE + lang.getString("commandDesc16"));
        default:
          break;
      }
    }
  }

  private void createLeaderboard(CommandSender sender, int page, String skillName) {
    String[] labels_0 = {"digging", "woodcutting", "mining", "farming", "fishing", "archery",
        "beastMastery", "swordsmanship", "defense", "axeMastery", "repair", "agility", "alchemy",
        "smelting", "enchanting", "global", "playTime"};
    List<String> labels_arr = Arrays.asList(labels_0);

    Leaderboards getStats = new Leaderboards();
    if (!getStats.isLeaderboardsLoaded()) {
      if (sender instanceof Player) {
        Player p = (Player) sender;
        p.sendMessage(ChatColor.RED + "Leaderboard is not yet loaded, try again soon");
      } else {
        sender.sendMessage("Leaderboard is not yet loaded, try again soon");
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
      LanguageSelector lang = new LanguageSelector(p);
      String[] titles_0 = {lang.getString("digging"), lang.getString("woodcutting"),
          lang.getString("mining"), lang.getString("farming"),
          lang.getString("fishing"), lang.getString("archery"), lang.getString("beastMastery"),
          lang.getString("swordsmanship"),
          lang.getString("defense"), lang.getString("axeMastery"), lang.getString("repair"),
          lang.getString("agility"), lang.getString("alchemy"),
          lang.getString("smelting"), lang.getString("enchanting"), lang.getString("global"),
          lang.getString("totalPlayTime")};
      String skillTitle = titles_0[labels_arr.indexOf(skillName)];
      p.sendMessage(
          ChatColor.AQUA + "------| " + ChatColor.WHITE + ChatColor.BOLD.toString() + skillTitle
              + " " + lang.getString("leaderboard") + "" +
              ChatColor.RESET + ChatColor.WHITE.toString() + " " + lang.getString("page") + " ["
              + Integer.toString(page) + "/" + Integer.toString(totalPages) + "]" +
              ChatColor.AQUA.toString() + " |-----");
      for (int i = 10 * (page - 1); i < (int) Math.min(10 * page, totalPlayers); i++) {
        PlayerLeaderboardStat info = sortedStats.get(i);
        if (skillName.equalsIgnoreCase("global")) {
          p.sendMessage(
              ChatColor.WHITE + Integer.toString(i + 1) + ". " + ChatColor.AQUA + info.get_pName()
                  + ChatColor.WHITE.toString() + " - " + ChatColor.WHITE + String.format("%,d",
                  info.get_sortedStat()) + ChatColor.WHITE + " (" + ChatColor.GRAY + String.format(
                  "%,d", info.get_stat2()) + ChatColor.WHITE + ")");
        } else if (skillName.equalsIgnoreCase("playTime")) {
          p.sendMessage(
              ChatColor.WHITE + Integer.toString(i + 1) + ". " + ChatColor.AQUA + info.get_pName()
                  + ChatColor.WHITE.toString() + " - " + ChatColor.WHITE
                  + info.get_playTimeString());
        } else {
          p.sendMessage(
              ChatColor.WHITE + Integer.toString(i + 1) + ". " + ChatColor.AQUA + info.get_pName()
                  + ChatColor.WHITE.toString() + " - " + ChatColor.WHITE + String.format("%,d",
                  info.get_stat2()) + ChatColor.WHITE + " (" + ChatColor.GRAY + String.format("%,d",
                  info.get_sortedStat()) + ChatColor.WHITE + ")");
        }
      }
    } else {
      String[] titles_0 = {"Digging", "Woodcutting", "Mining", "Farming", "Fishing", "Archery",
          "Beast Mastery", "Swordsmanship", "Defense", "Axe Mastery", "Repair", "Agility",
          "Alchemy", "Smelting", "Enchanting", "Global"};
      String skillTitle = titles_0[labels_arr.indexOf(skillName)];
      sender.sendMessage(
          "------| " + skillTitle + " Leaderboard" + " Page [" + Integer.toString(page) + "/"
              + Integer.toString(totalPages) + "]" + " |-----");
      for (int i = 10 * (page - 1); i < (int) Math.min(10 * page, totalPlayers); i++) {
        PlayerLeaderboardStat info = sortedStats.get(i);
        if (skillName.equalsIgnoreCase("global")) {
          sender.sendMessage(
              Integer.toString(i + 1) + ". " + info.get_pName() + " - " + Integer.toString(
                  (int) info.get_sortedStat()) + " (" + info.get_stat2() + ")");
        } else if (skillName.equalsIgnoreCase("playTime")) {
          sender.sendMessage(Integer.toString(i + 1) + ". " + info.get_pName() + " - "
              + info.get_playTimeString());
        } else {
          sender.sendMessage(
              Integer.toString(i + 1) + ". " + info.get_pName() + " - " + Integer.toString(
                  (int) info.get_stat2()) + " (" + info.get_sortedStat() + ")");
        }

      }
    }
  }

  private void createStatLookup(CommandSender sender, String playerName) {
    Leaderboards leaderboards = new Leaderboards();
    if (sender instanceof Player) {
      Player p = (Player) sender;
      LanguageSelector lang = new LanguageSelector(p);
      List<String> leaderboardNames = leaderboards.getLeaderboardNames();
      p.sendMessage(
          ChatColor.AQUA + "--------| " + ChatColor.WHITE + ChatColor.BOLD.toString() + playerName
              + ChatColor.RESET + ChatColor.WHITE.toString() + " " + lang.getString("stats") +
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
        p.sendMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + displayTitle + ChatColor.RESET
            + ChatColor.GRAY.toString() + " - " +
            ChatColor.WHITE + displayedStat + " (" + rankString + ChatColor.WHITE + ")");
      }

    } else {
      List<String> leaderboardNames = leaderboards.getLeaderboardNames();
      sender.sendMessage("--------| " + playerName + " stats |-------");
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
        sender.sendMessage(displayTitle + " - " + displayedStat + " (" + rankString + ")");
      }
    }
  }

  private void addToggleButton(Player p, GuiWrapper gui, String parseableName,
      Material iconItemType, int iconPosition, int toggleIndex) {
    LanguageSelector lang = new LanguageSelector(p);
    PlayerStats pStatClass = new PlayerStats(p);
    Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();

    GuiItem guiItem = new GuiItem(iconItemType, iconPosition, gui);
    guiItem.setName(lang.translateMessage(parseableName));
    gui.addItem(guiItem);

    GuiItem guiItemToggle = new GuiItem(Material.LIME_DYE, iconPosition + 9, gui);
    if ((int) pStat.get("global").get(toggleIndex) > 0) {
      guiItemToggle.setNameColor(ChatColor.BOLD + ChatColor.GREEN.toString());
      guiItemToggle.setName(lang.getString("on0"));
    } else {
      guiItemToggle.setItemType(Material.GRAY_DYE);
      guiItemToggle.setNameColor(ChatColor.BOLD + ChatColor.RED.toString());
      guiItemToggle.setName(lang.getString("off0"));
    }
    gui.addItem(guiItemToggle);
  }

  private void addToggleButton(Player p, GuiWrapper gui, String parseableName,
      Material iconItemType, int iconPosition, boolean toggleCondition) {
    LanguageSelector lang = new LanguageSelector(p);
    PlayerStats pStatClass = new PlayerStats(p);

    GuiItem guiItem = new GuiItem(iconItemType, iconPosition, gui);
    guiItem.setName(lang.translateMessage(parseableName));
    gui.addItem(guiItem);

    GuiItem guiItemToggle = new GuiItem(Material.LIME_DYE, iconPosition + 9, gui);
    if (toggleCondition) {
      guiItemToggle.setNameColor(ChatColor.BOLD + ChatColor.GREEN.toString());
      guiItemToggle.setName(lang.getString("on0"));
    } else {
      guiItemToggle.setItemType(Material.GRAY_DYE);
      guiItemToggle.setNameColor(ChatColor.BOLD + ChatColor.RED.toString());
      guiItemToggle.setName(lang.getString("off0"));
    }
    gui.addItem(guiItemToggle);
  }

  private void setTranslatedSkillTreeInformation(Player p, String skillName,
      Map<String, String[]> perksMap, Map<String, String[]> descriptionsMap,
      Map<String, String[]> passivePerksMap, Map<String, String[]> passiveDescriptionsMap) {
    LanguageSelector lang = new LanguageSelector(p);
    String[] newTitles = perksMap.get(skillName);
    String[] newDescs = descriptionsMap.get(skillName);
    String[] newPassiveTitles = passivePerksMap.get(skillName);
    String[] newPassiveDescs = passiveDescriptionsMap.get(skillName);
    int i = 0;
    for (String title : perksMap.get(skillName)) {
      String id = skillName + "PerkTitle" + i;
      newTitles[i] = lang.getString(id);
      i += 1;
    }
    i = 0;
    for (String desc : descriptionsMap.get(skillName)) {
      String id = skillName + "PerkDesc" + i;
      newDescs[i] = lang.getString(id);
      i += 1;
    }
    i = 0;
    for (String passiveTitle : passivePerksMap.get(skillName)) {
      String id = skillName + "PassiveTitle" + i;
      newPassiveTitles[i] = lang.getString(id);
      i += 1;
    }
    i = 0;
    for (String passiveDesc : passiveDescriptionsMap.get(skillName)) {
      String id = skillName + "PassiveDesc" + i;
      newPassiveDescs[i] = lang.getString(id);
      i += 1;
    }
    perksMap.put(skillName, newTitles);
    descriptionsMap.put(skillName, newDescs);
    passivePerksMap.put(skillName, newPassiveTitles);
    passiveDescriptionsMap.put(skillName, newPassiveDescs);
  }

  private void setSkillTreeProgressMainSkill(Player p, String skillName, GuiWrapper gui) {
    PlayerStats pStatClass = new PlayerStats(p);
    Map<String, ArrayList<Number>> pStatAll = pStatClass.getPlayerData();
    ArrayList<Number> pStats = pStatAll.get(skillName);
    Integer[] indices = {11, 29, 13, 31, 7, 43, 26};
    int skill_1a_level = (Integer) pStats.get(7);
    int skill_1b_level = (Integer) pStats.get(8);
    int skill_2a_level = (Integer) pStats.get(9);
    int skill_2b_level = (Integer) pStats.get(10);
    int skill_3a_level = (Integer) pStats.get(11);
    int skill_3b_level = (Integer) pStats.get(12);
    int skill_M_level = (Integer) pStats.get(13);

    GuiItem skill_1a = new GuiItem(Material.PINK_TERRACOTTA, 11, gui);
    GuiItem skill_2a = new GuiItem(Material.RED_TERRACOTTA, 13, gui);
    GuiItem skill_3a = new GuiItem(Material.RED_TERRACOTTA, 7, gui);
    GuiItem skill_1b = new GuiItem(Material.PINK_TERRACOTTA, 29, gui);
    GuiItem skill_2b = new GuiItem(Material.RED_TERRACOTTA, 31, gui);
    GuiItem skill_3b = new GuiItem(Material.RED_TERRACOTTA, 43, gui);
    GuiItem skill_M = new GuiItem(Material.RED_TERRACOTTA, 26, gui);

    if (skill_1a_level == 0) {
      skill_1a.setItemType(Material.PINK_TERRACOTTA);
    } else if (skill_1a_level > 0 && skill_1a_level < 5) {
      skill_1a.setItemType(Material.YELLOW_TERRACOTTA);
    } else {
      skill_1a.setItemType(Material.GREEN_TERRACOTTA);
    }

    if (skill_1b_level == 0) {
      skill_1b.setItemType(Material.PINK_TERRACOTTA);
    } else if (skill_1b_level > 0 && skill_1b_level < 5) {
      skill_1b.setItemType(Material.YELLOW_TERRACOTTA);
    } else {
      skill_1b.setItemType(Material.GREEN_TERRACOTTA);
    }

    if (skill_2a_level == 0) {
      if (skill_1a_level >= 2) {
        skill_2a.setItemType(Material.PINK_TERRACOTTA);
      }
    } else if (skill_2a_level > 0 && skill_2a_level < 5) {
      skill_2a.setItemType(Material.YELLOW_TERRACOTTA);
    } else {
      skill_2a.setItemType(Material.GREEN_TERRACOTTA);
    }

    if (skill_2b_level == 0) {
      if (skill_1b_level >= 2) {
        skill_2b.setItemType(Material.PINK_TERRACOTTA);
      }
    } else if (skill_2b_level > 0 && skill_2b_level < 5) {
      skill_2b.setItemType(Material.YELLOW_TERRACOTTA);
    } else {
      skill_2b.setItemType(Material.GREEN_TERRACOTTA);
    }

    if (skill_3a_level == 0) {
      if (skill_2a_level >= 2) {
        skill_3a.setItemType(Material.PINK_TERRACOTTA);
      }
    } else {
      skill_3a.setItemType(Material.GREEN_TERRACOTTA);
    }

    if (skill_3b_level == 0) {
      if (skill_2b_level >= 2) {
        skill_3b.setItemType(Material.PINK_TERRACOTTA);
      }
    } else {
      skill_3b.setItemType(Material.GREEN_TERRACOTTA);
    }

    if (skill_M_level == 0) {
      if (skill_1a_level + skill_1b_level + skill_2a_level + skill_2b_level + skill_3a_level
          + skill_3b_level >= 10) {
        skill_M.setItemType(Material.PINK_TERRACOTTA);
      }
    } else {
      skill_M.setItemType(Material.GREEN_TERRACOTTA);
    }
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);

    // /frpg aka MainGUI
    if (args.length == 0) {
      CommandHelper commandHelper = new CommandHelper(sender, args, 0, "");
      commandHelper.setCheckInBed(true);
      commandHelper.setPlayerOnlyCommand(true);
      commandHelper.setPermissionName("mainGUI");
      if (!commandHelper.isProperCommand()) {
        return true; //Command Restricted or Improper
      }
      Player p = (Player) sender;
      generateMainMenu(p); //Generates the Main GUI
      return true;
    }

    //Help
    else if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
      final String IMPROPER_ARGUMENTS_MESSSAGE = " /frpg help [::page::]";
      CommandHelper commandHelper = new CommandHelper(sender, args, 1, 2,
          IMPROPER_ARGUMENTS_MESSSAGE);
      commandHelper.setPlayerOnlyCommand(true);
      commandHelper.setPermissionName("help");
      if (!commandHelper.isProperCommand()) {
        return true; //Command Restricted or Improper
      }
      int page = 1;
      if (args.length == 2) {
        page = getArgumentAsInteger(args[1]);
        if (page == Integer.MAX_VALUE) {
          commandHelper.sendImproperArgumentsMessage();
          return true; //Improper input
        }
      }
      messagePlayerHelpScreen(sender, page); //Sends player the corresponding help screen
    }

    //saveStats
    else if (args[0].equalsIgnoreCase("saveStats") || args[0].equalsIgnoreCase("statSave")) {
      final String IMPROPER_ARGUMENTS_MESSSAGE = " /frpg saveStats";
      CommandHelper commandHelper = new CommandHelper(sender, args, 1, 2,
          IMPROPER_ARGUMENTS_MESSSAGE);
      commandHelper.setPermissionName("saveStats");
      if (!commandHelper.isProperCommand()) {
        return true; //Command Restricted or Improper
      }
      if (args.length == 1) {
        PeriodicSaving saveAll = new PeriodicSaving();
        saveAll.saveAllStats(false);
      } else if (args.length == 2) {
        String playerName = args[1];
        Player target = plugin.getServer().getPlayer(playerName);
        if (!isTargetOnline(target, sender)) {
          return true; //Player not online
        }
        PlayerStatsLoadIn saveStats = new PlayerStatsLoadIn(target);
        try {
          saveStats.setPlayerStatsMap();
        } catch (IOException e) {
          FreeRPG.log(Level.ERROR, e.getMessage());
        }
      }
    }

    // Tutorial Document (Info)
    else if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("use")) {
      final String IMPROPER_ARGUMENTS_MESSSAGE = " /frpg info";
      CommandHelper commandHelper = new CommandHelper(sender, args, 0, IMPROPER_ARGUMENTS_MESSSAGE);
      commandHelper.setPlayerOnlyCommand(true);
      commandHelper.setPermissionName("info");
      if (!commandHelper.isProperCommand()) {
        return true; //Command Restricted or Improper
      }
      Player p = (Player) sender;
      LanguageSelector lang = new LanguageSelector(p);
      p.sendMessage(
          lang.getString("informationURL") + ": " + ChatColor.AQUA + ChatColor.UNDERLINE.toString()
              + "shorturl.at/ptCDX" +
              ChatColor.RESET + ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "<-- "
              + lang.getString("click"));
    }

    //Enchant Item Command
    else if (args[0].equalsIgnoreCase("enchantItem") || args[0].equalsIgnoreCase("enchant")) {
      final String IMPROPER_ARGUMENTS_MESSSAGE = " /frpg enchantItem [::level::]";
      CommandHelper commandHelper = new CommandHelper(sender, args, 2, IMPROPER_ARGUMENTS_MESSSAGE);
      commandHelper.setPlayerOnlyCommand(true);
      commandHelper.setPermissionName("enchantItem");
      if (!commandHelper.isProperCommand()) {
        return true; //Command Restricted or Improper
      }
      Player p = (Player) sender;
      int level = getArgumentAsInteger(args[1]);
      if (level == Integer.MAX_VALUE) {
        commandHelper.sendImproperArgumentsMessage();
        return true; //Improper input
      }
      if (level > 40) {
        LanguageSelector lang = new LanguageSelector(p);
        p.sendMessage(ChatColor.RED + lang.getString("levelArgument"));
        return true; //Level input too high
      }
      ItemStack item = p.getInventory().getItemInMainHand();
      PsuedoEnchanting enchant = new PsuedoEnchanting();
      item = enchant.enchantItem(item, level, false);
      p.getInventory().setItemInMainHand(item);
    }

    //Leader Board
    else if (args[0].equalsIgnoreCase("leaderboard") || args[0].equalsIgnoreCase("statLeaders")
        || args[0].equalsIgnoreCase("top")) {
      final String IMPROPER_ARGUMENTS_MESSSAGE = " /frpg top [::skillName::] [::page::]";
      CommandHelper commandHelper = new CommandHelper(sender, args, 2, 3,
          IMPROPER_ARGUMENTS_MESSSAGE);
      commandHelper.setPermissionName("leaderboard");
      if (!commandHelper.isProperCommand()) {
        return true; //Command Restricted or Improper
      }
      int page = 1;
      if (args.length == 3) {
        page = getArgumentAsInteger(args[2]);
        if (page == Integer.MAX_VALUE) {
          commandHelper.sendImproperArgumentsMessage();
          return true; //Improper input
        }
      }
      String[] labels_0 = {"digging", "woodcutting", "mining", "farming", "fishing", "archery",
          "beastMastery", "swordsmanship", "defense", "axeMastery", "repair", "agility", "alchemy",
          "smelting", "enchanting", "global", "playTime"};
      List<String> labels_arr = Arrays.asList(labels_0);
      String skillName = UtilityMethods.convertStringToListCasing(labels_arr, args[1]);
      if (!labels_arr.contains(skillName)) {
        commandHelper.sendImproperArgumentsMessage();
        return true; //Improper input
      }
      createLeaderboard(sender, page, skillName);
    }

    //Stat Lookup
    else if (args[0].equalsIgnoreCase("statLookup") || args[0].equalsIgnoreCase("lookupStats")
        || args[0].equalsIgnoreCase("stats")) {
      final String IMPROPER_ARGUMENTS_MESSSAGE = " /frpg stats [::playerName::]";
      CommandHelper commandHelper = new CommandHelper(sender, args, 1, 2,
          IMPROPER_ARGUMENTS_MESSSAGE);
      commandHelper.setPermissionName("statLookup");
      if (!commandHelper.isProperCommand()) {
        return true; //Command Restricted or Improper
      }
      if (args.length == 1) {
        if (sender instanceof Player) {
          Player p = (Player) sender;
          p.performCommand("frpg statLookup " + p.getName());
        } else {
          commandHelper.sendImproperArgumentsMessage();
          return true; //Improper Arguments
        }
      } else if (args.length == 2) {
        String playerName = args[1];
        Leaderboards leaderboards = new Leaderboards();
        if (!leaderboards.isPlayerOnLeaderboards(playerName)) {
          if (sender instanceof Player) {
            Player p = (Player) sender;
            LanguageSelector lang = new LanguageSelector(p);
            p.sendMessage(ChatColor.RED + lang.getString("playerNotInLeaderboard"));
          } else {
            sender.sendMessage("That player is not on any leaderboards");
          }
          return true; //Player not on leaderboards
        }
        createStatLookup(sender, playerName);
      }
    }

    //CooldownReset
    else if (args[0].equalsIgnoreCase("resetCooldown") || args[0].equalsIgnoreCase(
        "cooldownReset")) {
      final String IMPROPER_ARGUMENTS_MESSSAGE = " /frpg resetCooldown [::playerName::] [::skillName::]";
      CommandHelper commandHelper = new CommandHelper(sender, args, 2, 3,
          IMPROPER_ARGUMENTS_MESSSAGE);
      commandHelper.setPermissionName("resetCooldown");
      if (!commandHelper.isProperCommand()) {
        return true; //Command Restricted or Improper
      }
      //Arguments --> Variables
      Player target;
      String skillNameInput;
      if (args.length == 2) {
        if (sender instanceof Player) {
          target = (Player) sender;
          skillNameInput = args[1];
        } else {
          commandHelper.sendImproperArgumentsMessage();
          return true; //Improper Arguments
        }
      } else {
        String playerName = args[1];
        skillNameInput = args[2];

        //Target Online Check
        target = plugin.getServer().getPlayer(playerName);
        boolean targetOnline = isTargetOnline(target, sender);
        if (!targetOnline) {
          return true; //Target not online
        }
      }

      //Skill name match check
      String[] labels_0 = {"digging", "woodcutting", "mining", "farming", "fishing", "archery",
          "beastMastery", "swordsmanship", "defense", "axeMastery"};
      List<String> labels = Arrays.asList(labels_0);
      String skillName = UtilityMethods.convertStringToListCasing(labels, skillNameInput);
      if (!labels.contains(skillName)) {
        commandHelper.sendImproperArgumentsMessage();
        return true;
      }

      AbilityTimers abilityTimers = new AbilityTimers(target);
      abilityTimers.setPlayerCooldownTime(skillName, 0);

    }

    //createFakePlayers
    else if (args[0].equalsIgnoreCase("createFakePlayers") || args[0].equalsIgnoreCase(
        "createFakeProfiles")) {
      final String IMPROPER_ARGUMENTS_MESSSAGE = " /frpg createFakePlayers [#]";
      CommandHelper commandHelper = new CommandHelper(sender, args, 2, IMPROPER_ARGUMENTS_MESSSAGE);
      commandHelper.setPermissionName("createFakePlayers");
      if (!commandHelper.isProperCommand()) {
        return true; //Command Restricted or Improper
      }

      int numPlayers = getArgumentAsInteger(args[1]);
      if (numPlayers == Integer.MAX_VALUE) {
        commandHelper.sendImproperArgumentsMessage();
        return true; //Improper input
      }

      PlayerStatsFilePreparation playerStatsFilePreparation = new PlayerStatsFilePreparation();
      Random rand = new Random();
      for (int i = 0; i < numPlayers; i++) {
        String fakeName = "FakePlayer" + rand.nextInt(100000);
        UUID fakeUUID = UUID.fromString("badf" + UUID.randomUUID().toString()
            .substring(4)); //"badf" hexadecimal identifier for "bad files"
        playerStatsFilePreparation.preparePlayerFile(fakeName, fakeUUID, false);
      }

    }

    //removeFakePlayers
    else if (args[0].equalsIgnoreCase("deleteFakePlayers") || args[0].equalsIgnoreCase(
        "deleteFakeProfiles") || args[0].equalsIgnoreCase("removeFakePlayers")
        || args[0].equalsIgnoreCase("removeFakeProfiles")) {
      final String IMPROPER_ARGUMENTS_MESSSAGE = " /frpg deleteFakePlayers";
      CommandHelper commandHelper = new CommandHelper(sender, args, 0, IMPROPER_ARGUMENTS_MESSSAGE);
      commandHelper.setPermissionName("createFakePlayers");
      if (!commandHelper.isProperCommand()) {
        return true; //Command Restricted or Improper
      }

      File userdata = new File(
          Bukkit.getServer().getPluginManager().getPlugin("FreeRPG").getDataFolder(),
          File.separator + "PlayerDatabase");
      File[] allUsers = userdata.listFiles();
      for (File f : allUsers) {
        if (f.getName().substring(0, 4).equalsIgnoreCase("badf")) {
          f.delete();
        }
      }

    }

    //Save FRPG Data
    else if (args[0].equalsIgnoreCase("thoroughSave")) {
      final String IMPROPER_ARGUMENTS_MESSSAGE = " /frpg save";
      CommandHelper commandHelper = new CommandHelper(sender, args, 0, IMPROPER_ARGUMENTS_MESSSAGE);
      commandHelper.setPermissionName("saveStats");
      if (!commandHelper.isProperCommand()) {
        return true; //Command Restricted or Improper
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
    else if (args[0].equalsIgnoreCase("loadInAllPlayerFiles") || args[0].equalsIgnoreCase(
        "loadInPlayerFiles")) {
      final String IMPROPER_ARGUMENTS_MESSSAGE = " /frpg save";
      CommandHelper commandHelper = new CommandHelper(sender, args, 0, IMPROPER_ARGUMENTS_MESSSAGE);
      commandHelper.setPermissionName("saveStats");
      if (!commandHelper.isProperCommand()) {
        return true; //Command Restricted or Improper
      }
      OfflinePlayerStatLoadIn offlinePlayerStatLoadIn = new OfflinePlayerStatLoadIn();
      offlinePlayerStatLoadIn.loadInAllOfflinePlayers();

    }

    //GiveEXP
    else if (args[0].equalsIgnoreCase("giveEXP") || args[0].equalsIgnoreCase("expGive")) {
      final String IMPROPER_ARGUMENTS_MESSSAGE = " /frpg giveEXP [::playerName::] [::skillName::] [::exp::]";
      CommandHelper commandHelper = new CommandHelper(sender, args, 3, 4,
          IMPROPER_ARGUMENTS_MESSSAGE);
      commandHelper.setPermissionName("giveEXP");
      if (!commandHelper.isProperCommand()) {
        return true; //Command Restricted or Improper
      }
      Player target;
      String skillNameInput;
      String expInput;
      if (args.length == 3) {
        if (sender instanceof Player) {
          target = (Player) sender;
          skillNameInput = args[1];
          expInput = args[2];
        } else {
          commandHelper.sendImproperArgumentsMessage();
          return true; //Improper Arguments
        }
      } else {
        String playerName = args[1];
        skillNameInput = args[2];
        expInput = args[3];

        //Target Online Check
        target = plugin.getServer().getPlayer(playerName);
        boolean targetOnline = isTargetOnline(target, sender);
        if (!targetOnline) {
          return true; //Target not online
        }
      }

      int exp = getArgumentAsInteger(expInput);
      if (exp == Integer.MAX_VALUE) {
        commandHelper.sendImproperArgumentsMessage();
        return true; //Improper argument (exp not integer)
      }

      String[] labels_0 = {"digging", "woodcutting", "mining", "farming", "fishing", "archery",
          "beastMastery", "swordsmanship", "defense", "axeMastery", "repair", "agility", "alchemy",
          "smelting", "enchanting"};
      List<String> labels_arr = Arrays.asList(labels_0);
      String skillName = UtilityMethods.convertStringToListCasing(labels_arr, skillNameInput);
      if (!labels_arr.contains(skillName)) {
        commandHelper.sendImproperArgumentsMessage();
        return true; //Improper argument (skillName not valid)
      }

      if (exp < 0) {
        if (sender instanceof Player) {
          Player p = (Player) sender;
          LanguageSelector lang = new LanguageSelector(p);
          p.sendMessage(ChatColor.RED + lang.getString("onlyIncrease"));
        } else {
          sender.sendMessage(
              "Please only increase exp with this command, otherwise, use /frpg statReset then /frpg giveEXP");
        }
        return true; //Improper argument (exp change not positive)
      }

      ChangeStats increaseStats = new ChangeStats(target);
      increaseStats.set_isCommand(true);
      increaseStats.changeEXP(skillName, exp);
      increaseStats.setTotalLevel();
      increaseStats.setTotalExperience();
    }

    //setLevel
    else if (args[0].equalsIgnoreCase("setLevel") || args[0].equalsIgnoreCase("levelSet")) {
      final String IMPROPER_ARGUMENTS_MESSSAGE = " /frpg setLevel [::playerName::] [::skillName::] [::level::]";
      CommandHelper commandHelper = new CommandHelper(sender, args, 3, 4,
          IMPROPER_ARGUMENTS_MESSSAGE);
      commandHelper.setPermissionName("setLevel");
      if (!commandHelper.isProperCommand()) {
        return true; //Command Restricted or Improper
      }
      Player target;
      String skillNameInput;
      String levelInput;
      if (args.length == 3) {
        if (sender instanceof Player) {
          target = (Player) sender;
          skillNameInput = args[1];
          levelInput = args[2];
        } else {
          commandHelper.sendImproperArgumentsMessage();
          return true; //Improper Arguments
        }
      } else {
        String playerName = args[1];
        skillNameInput = args[2];
        levelInput = args[3];

        //Target Online Check
        target = plugin.getServer().getPlayer(playerName);
        boolean targetOnline = isTargetOnline(target, sender);
        if (!targetOnline) {
          return true; //Target not online
        }
      }

      int level = getArgumentAsInteger(levelInput);
      if (level == Integer.MAX_VALUE) {
        commandHelper.sendImproperArgumentsMessage();
        return true; //Improper argument (exp not integer)
      }

      String[] labels_0 = {"digging", "woodcutting", "mining", "farming", "fishing", "archery",
          "beastMastery", "swordsmanship", "defense", "axeMastery", "repair", "agility", "alchemy",
          "smelting", "enchanting"};
      List<String> labels_arr = Arrays.asList(labels_0);
      String skillName = UtilityMethods.convertStringToListCasing(labels_arr, skillNameInput);
      if (!labels_arr.contains(skillName)) {
        commandHelper.sendImproperArgumentsMessage();
        return true; //Improper argument (skillName not valid)
      }

      ChangeStats increaseStats = new ChangeStats(target);
      int exp = increaseStats.getEXPfromLevel(level);
      PlayerStats pStatClass = new PlayerStats(target);
      Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
      int currentExp = (int) pStat.get(skillName).get(1);

      if (exp <= currentExp) {
        if (sender instanceof Player) {
          Player p = (Player) sender;
          LanguageSelector lang = new LanguageSelector(p);
          p.sendMessage(ChatColor.RED + lang.getString("onlyIncrease"));
        } else {
          sender.sendMessage(
              "Please only increase levels with this command, otherwise, use /frpg statReset then /frpg setLevel");
        }
        return true; //Improper argument (exp change not positive)
      }

      increaseStats.set_isCommand(true);
      increaseStats.changeEXP(skillName, exp - currentExp + 1);
      increaseStats.setTotalLevel();
      increaseStats.setTotalExperience();
    }

    //statReset
    else if (args[0].equalsIgnoreCase("statReset") || args[0].equalsIgnoreCase("resetStat")) {
      final String IMPROPER_ARGUMENTS_MESSSAGE = " /frpg statReset [::playerName::] [::skillName::]";
      CommandHelper commandHelper = new CommandHelper(sender, args, 2, 3,
          IMPROPER_ARGUMENTS_MESSSAGE);
      commandHelper.setPermissionName("statReset");
      if (!commandHelper.isProperCommand()) {
        return true; //Command Restricted or Improper
      }
      Player target;
      String skillNameInput;
      if (args.length == 2) {
        if (sender instanceof Player) {
          target = (Player) sender;
          skillNameInput = args[1];
        } else {
          commandHelper.sendImproperArgumentsMessage();
          return true; //Improper Arguments
        }
      } else {
        String playerName = args[1];
        skillNameInput = args[2];

        //Target Online Check
        target = plugin.getServer().getPlayer(playerName);
        boolean targetOnline = isTargetOnline(target, sender);
        if (!targetOnline) {
          return true; //Target not online
        }
      }

      String[] labels_0 = {"digging", "woodcutting", "mining", "farming", "fishing", "archery",
          "beastMastery", "swordsmanship", "defense", "axeMastery",
          "repair", "agility", "alchemy", "smelting", "enchanting",
          "global", "all"};
      List<String> labels_arr = Arrays.asList(labels_0);
      String skillName = UtilityMethods.convertStringToListCasing(labels_arr, skillNameInput);
      if (!labels_arr.contains(skillName)) {
        commandHelper.sendImproperArgumentsMessage();
        return true; //Improper argument (skillName not valid)
      }
      ChangeStats changeStats = new ChangeStats(target);
      if (!skillName.equalsIgnoreCase("all")) {
        changeStats.resetStat(skillName);
      } else {
        for (String skillNameLabel : labels_arr) {
          if (!skillNameLabel.equalsIgnoreCase("all")) {
            changeStats.resetStat(skillNameLabel);
          }
        }
      }

    }

    //setSouls
    else if (args[0].equalsIgnoreCase("setSouls") || args[0].equalsIgnoreCase("soulsSet")) {
      final String IMPROPER_ARGUMENTS_MESSSAGE = " /frpg setSouls [::playerName::] [::amount::]";
      CommandHelper commandHelper = new CommandHelper(sender, args, 2, 3,
          IMPROPER_ARGUMENTS_MESSSAGE);
      commandHelper.setPermissionName("setSouls");
      if (!commandHelper.isProperCommand()) {
        return true; //Command Restricted or Improper
      }
      Player target;
      String soulsInput;
      if (args.length == 2) {
        if (sender instanceof Player) {
          target = (Player) sender;
          soulsInput = args[1];
        } else {
          commandHelper.sendImproperArgumentsMessage();
          return true; //Improper Arguments
        }
      } else {
        String playerName = args[1];
        soulsInput = args[2];

        //Target Online Check
        target = plugin.getServer().getPlayer(playerName);
        boolean targetOnline = isTargetOnline(target, sender);
        if (!targetOnline) {
          return true; //Target not online
        }
      }

      int souls = getArgumentAsInteger(soulsInput);
      if (souls == Integer.MAX_VALUE) {
        commandHelper.sendImproperArgumentsMessage();
        return true; //Improper argument (souls not integer)
      }

      ChangeStats increaseStats = new ChangeStats(target);
      increaseStats.setStat("global", 20, souls);
    }

    //setTokens
    else if (args[0].equalsIgnoreCase("setTokens") || args[0].equalsIgnoreCase("tokensSet")) {
      final String IMPROPER_ARGUMENTS_MESSSAGE = " /frpg setTokens [::playerName::] global [::amount::] or /frpg setTokens [::playerName::] [::skillName::] skill/passive [::amount::]";
      CommandHelper commandHelper = new CommandHelper(sender, args, 3, 5,
          IMPROPER_ARGUMENTS_MESSSAGE);
      commandHelper.setPermissionName("setTokens");
      if (!commandHelper.isProperCommand()) {
        return true; //Command Restricted or Improper
      }
      Player target;
      String skillNameInput;
      String tokenType;
      String amountInput;
      if (args.length == 3) { // Two inputs: tokenType and amount
        if (sender instanceof Player) {
          target = (Player) sender;
          tokenType = args[1];
          amountInput = args[2];
          skillNameInput = "global";
        } else {
          commandHelper.sendImproperArgumentsMessage();
          return true; //Improper Arguments
        }
      } else if (args.length == 4) {
        if (args[2].equalsIgnoreCase("global")) { //Three inputs: PlayerName, token type, amount
          String playerName = args[1];
          tokenType = args[2];
          skillNameInput = "global";
          amountInput = args[3];

          //Target Online Check
          target = plugin.getServer().getPlayer(playerName);
          boolean targetOnline = isTargetOnline(target, sender);
          if (!targetOnline) {
            return true; //Target not online
          }
        } else { //Three inputs: Skillname, tokenType, amount
          target = (Player) sender;
          skillNameInput = args[1];
          tokenType = args[2];
          amountInput = args[3];
        }
      } else { //Four inputs: Playername, skillName, tokenType, amount
        String playerName = args[1];
        skillNameInput = args[2];
        tokenType = args[3];
        amountInput = args[4];

        //Target Online Check
        target = plugin.getServer().getPlayer(playerName);
        boolean targetOnline = isTargetOnline(target, sender);
        if (!targetOnline) {
          return true; //Target not online
        }
      }

      String[] labels_0 = {"digging", "woodcutting", "mining", "farming", "fishing", "archery",
          "beastMastery", "swordsmanship", "defense", "axeMastery", "repair", "agility", "alchemy",
          "smelting", "enchanting", "global"};
      List<String> labels_arr = Arrays.asList(labels_0);
      String skillName = UtilityMethods.convertStringToListCasing(labels_arr, skillNameInput);
      if (!labels_arr.contains(skillName)) {
        commandHelper.sendImproperArgumentsMessage();
        return true; //Improper argument (skillName not valid)
      }

      int amount = getArgumentAsInteger(amountInput);
      if (amount == Integer.MAX_VALUE) {
        commandHelper.sendImproperArgumentsMessage();
        return true; //Improper argument (amount not integer)
      }

      if (!(tokenType.equalsIgnoreCase("global") || tokenType.equalsIgnoreCase("skill")
          || tokenType.equalsIgnoreCase("passive"))) {
        commandHelper.sendImproperArgumentsMessage();
        return true; //Improper arguemnt (token type not global, skill, or passive)
      }

      ChangeStats increaseStats = new ChangeStats(target);
      if (tokenType.equalsIgnoreCase("global")) {
        increaseStats.setStat("global", 1, amount);
      } else if (tokenType.equalsIgnoreCase("passive")) {
        increaseStats.setStat(skillName, 2, amount);
      } else if (tokenType.equalsIgnoreCase("skill")) {
        increaseStats.setStat(skillName, 3, amount);
      }

    }

    //setMultiplier
    else if (args[0].equalsIgnoreCase("setMultiplier") || args[0].equalsIgnoreCase(
        "multiplierSet")) {
      final String IMPROPER_ARGUMENTS_MESSSAGE = " /frpg setMultiplier [::playerName::] [::expIncrease::]";
      CommandHelper commandHelper = new CommandHelper(sender, args, 2, 3,
          IMPROPER_ARGUMENTS_MESSSAGE);
      commandHelper.setPermissionName("setMultiplier");
      if (!commandHelper.isProperCommand()) {
        return true; //Command Restricted or Improper
      }
      Player target;
      String multiplierInput;
      if (args.length == 2) {
        if (sender instanceof Player) {
          target = (Player) sender;
          multiplierInput = args[1];
        } else {
          commandHelper.sendImproperArgumentsMessage();
          return true; //Improper Arguments
        }
      } else {
        String playerName = args[1];
        multiplierInput = args[2];

        //Target Online Check
        target = plugin.getServer().getPlayer(playerName);
        boolean targetOnline = isTargetOnline(target, sender);
        if (!targetOnline) {
          return true; //Target not online
        }
      }

      double multiplier = getArgumentAsDouble(multiplierInput);
      if (multiplier == Double.MAX_VALUE) {
        commandHelper.sendImproperArgumentsMessage();
        return true; //Improper argument (amount not integer)
      }

      ChangeStats setMultiplier = new ChangeStats(target);
      setMultiplier.setStat("global", 23, multiplier);

    }

    //addMultiplier
    else if (args[0].equalsIgnoreCase("addMultiplier") || args[0].equalsIgnoreCase(
        "changeMultiplier")) {
      final String IMPROPER_ARGUMENTS_MESSSAGE = " /frpg addMultiplier [::playerName::] [::expIncrease::]";
      CommandHelper commandHelper = new CommandHelper(sender, args, 2, 3,
          IMPROPER_ARGUMENTS_MESSSAGE);
      commandHelper.setPermissionName("setMultiplier");
      if (!commandHelper.isProperCommand()) {
        return true; //Command Restricted or Improper
      }
      Player target;
      String multiplierInput;
      if (args.length == 2) {
        if (sender instanceof Player) {
          target = (Player) sender;
          multiplierInput = args[1];
        } else {
          commandHelper.sendImproperArgumentsMessage();
          return true; //Improper Arguments
        }
      } else {
        String playerName = args[1];
        multiplierInput = args[2];

        //Target Online Check
        target = plugin.getServer().getPlayer(playerName);
        boolean targetOnline = isTargetOnline(target, sender);
        if (!targetOnline) {
          return true; //Target not online
        }
      }

      double multiplier = getArgumentAsDouble(multiplierInput);
      if (multiplier == Double.MAX_VALUE) {
        commandHelper.sendImproperArgumentsMessage();
        return true; //Improper argument (amount not integer)
      }

      ChangeStats setMultiplier = new ChangeStats(target);
      setMultiplier.changeStat("global", 23, multiplier);

    }

    //flamePickToggle
    else if (args[0].equalsIgnoreCase("toggleFlamePick") || args[0].equalsIgnoreCase(
        "flamePickToggle")) {
      return togglePerk("flamePickToggle", sender, args);
    }

    //flintToggle
    else if (args[0].equalsIgnoreCase("toggleFlint") || args[0].equalsIgnoreCase("flintToggle")) {
      return togglePerk("flintToggle", sender, args);
    }

    //grappleToggle
    else if (args[0].equalsIgnoreCase("toggleGrapple") || args[0].equalsIgnoreCase(
        "grappleToggle")) {
      return togglePerk("grappleToggle", sender, args);
    }

    //HotRodToggle
    else if (args[0].equalsIgnoreCase("toggleHotRod") || args[0].equalsIgnoreCase("hotRodToggle")) {
      return togglePerk("hotRodToggle", sender, args);
    }

    //MegaDigToggle
    else if (args[0].equalsIgnoreCase("toggleMegaDig") || args[0].equalsIgnoreCase(
        "megaDigToggle")) {
      return togglePerk("megaDigToggle", sender, args);
    }

    //PotionToggle
    else if (args[0].equalsIgnoreCase("togglePotion") || args[0].equalsIgnoreCase("potionToggle")) {
      return togglePerk("potionToggle", sender, args);
    }

    //SpeedToggle
    else if (args[0].equalsIgnoreCase("toggleSpeed") || args[0].equalsIgnoreCase("speedToggle")) {
      return togglePerk("speedToggle", sender, args);
    }

    //VeinMinerToggle
    else if (args[0].equalsIgnoreCase("toggleVeinMiner") || args[0].equalsIgnoreCase(
        "veinMinerToggle")) {
      return togglePerk("veinMinerToggle", sender, args);
    }

    //LeafBlowerToggle
    else if (args[0].equalsIgnoreCase("toggleLeafBlower") || args[0].equalsIgnoreCase(
        "leafBlowerToggle")) {
      return togglePerk("leafBlowerToggle", sender, args);
    }

    //holyAxeToggle
    else if (args[0].equalsIgnoreCase("toggleHolyAxe") || args[0].equalsIgnoreCase(
        "holyAxeToggle")) {
      return togglePerk("holyAxeToggle", sender, args);
    }

    //ConfigGUI
    else if (args[0].equalsIgnoreCase("configGUI") || args[0].equalsIgnoreCase(
        "configurationGUI")) {
      final String IMPROPER_ARGUMENTS_MESSSAGE = " /frpg configGUI";
      CommandHelper commandHelper = new CommandHelper(sender, args, 1, IMPROPER_ARGUMENTS_MESSSAGE);
      commandHelper.setPlayerOnlyCommand(true);
      commandHelper.setCheckInBed(true);
      commandHelper.setPermissionName("configGUI");
      if (!commandHelper.isProperCommand()) {
        return true; //Command Restricted or Improper
      }
      Player p = (Player) sender;
      LanguageSelector lang = new LanguageSelector(p);
      PlayerStats pStatClass = new PlayerStats(p);
      Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();

      //GuiWrapper
      GuiWrapper gui = new GuiWrapper(p, "Configuration Window", 54);

      //Back button
      GuiItem backButton = new GuiItem(Material.ARROW, 45, gui);
      backButton.setName(lang.getString("diggingPassiveTitle1"));
      backButton.setDescription(lang.getString("diggingPassiveDesc1"));
      gui.addItem(backButton);

      //Simple Toggle Buttons
      addToggleButton(p, gui, "::levelUpNotif::", Material.OAK_SIGN, 1,
          21); //Level up Notifications
      addToggleButton(p, gui, "::abilityPreparationNotif::", Material.OAK_SIGN, 2,
          22); //Ability Prep Notifications
      addToggleButton(p, gui, "::triggerAbilities::", Material.WOODEN_PICKAXE, 3,
          24); //Ability Trigger
      addToggleButton(p, gui, "::showEXPBar::", Material.EXPERIENCE_BOTTLE, 4, 25); //EXP bar show

      //Ability Duration Bar
      GuiItem durationBar = new GuiItem(Material.CLOCK, 5, gui);
      durationBar.setName(lang.getString("numberOfAbilityTimersDisplayed"));
      gui.addItem(durationBar);

      GuiItem durationBarToggle = new GuiItem(Material.LIME_DYE, 14, gui);
      int numberOfBars = (int) pStat.get("global").get(28);
      if (numberOfBars > 0) {
        durationBarToggle.setNameColor(ChatColor.BOLD + ChatColor.GREEN.toString());
        durationBarToggle.setName(String.valueOf(numberOfBars));
      } else {
        durationBarToggle.setNameColor(ChatColor.BOLD + ChatColor.RED.toString());
        durationBarToggle.setName(String.valueOf(0));
        durationBarToggle.setItemType(Material.GRAY_DYE);
      }
      durationBarToggle.setItemAmount(Math.max(1, numberOfBars));
      gui.addItem(durationBarToggle);

      //Sets all the items into the gui (This is redundant, but makes the code a bit clearer)
      gui.setGui();

      //LANGUAGES (sets all the language items into the gui)
      StringsAndOtherData stringsAndOtherData = new StringsAndOtherData();
      stringsAndOtherData.setLanguageItems(p, gui.getGui());

      //Put the items in the inventory
      gui.displayGuiForPlayer();
    }

    //SkillConfigGUI
    else if (args[0].equalsIgnoreCase("skillConfigGUI") || args[0].equalsIgnoreCase(
        "skillConfigurationGUI") || args[0].equalsIgnoreCase("skillConfig")) {
      final String IMPROPER_ARGUMENTS_MESSSAGE = " /frpg skillConfigGUI [::skillName::]";
      CommandHelper commandHelper = new CommandHelper(sender, args, 2, IMPROPER_ARGUMENTS_MESSSAGE);
      commandHelper.setPlayerOnlyCommand(true);
      commandHelper.setCheckInBed(true);
      commandHelper.setPermissionName("skillConfigGUI");
      if (!commandHelper.isProperCommand()) {
        return true; //Command Restricted or Improper
      }
      Player p = (Player) sender;
      LanguageSelector lang = new LanguageSelector(p);
      PlayerStats pStatClass = new PlayerStats(p);
      ConfigLoad configLoad = new ConfigLoad();

      String[] titles_0 = {"Digging", "Woodcutting", "Mining", "Farming", "Fishing", "Archery",
          "Beast Mastery", "Swordsmanship", "Defense", "Axe Mastery", "Repair", "Agility",
          "Alchemy", "Smelting", "Enchanting"};
      String[] labels_0 = {"digging", "woodcutting", "mining", "farming", "fishing", "archery",
          "beastMastery", "swordsmanship", "defense", "axeMastery", "repair", "agility", "alchemy",
          "smelting", "enchanting"};
      String[] passiveLabels0 = {"repair", "agility", "alchemy", "smelting", "enchanting"};
      List<String> labels = Arrays.asList(labels_0);
      List<String> passiveLabels = Arrays.asList(passiveLabels0);
      String skillName = UtilityMethods.convertStringToListCasing(labels, args[1]);
      if (!labels.contains(skillName)) {
        commandHelper.sendImproperArgumentsMessage();
        return true;
      }

      String skillTitle = titles_0[labels.indexOf(skillName)];
      GuiWrapper gui = new GuiWrapper(p,
          Bukkit.createInventory(p, 54, skillTitle + " Configuration"));

      addToggleButton(p, gui, "::showEXPBar::", Material.EXPERIENCE_BOTTLE, 10,
          pStatClass.isPlayerSkillExpBarOn(skillName)); //EXP bar show
      if (!passiveLabels.contains(skillName)) {
        addToggleButton(p, gui, "::triggerAbilities::", Material.WOODEN_PICKAXE, 11,
            pStatClass.isPlayerSkillAbilityOn(skillName)); //Trigger Abilities
      }

      if (!configLoad.getAllowedSkillsMap()
          .get(skillName)) { //Special condition to return (skill disabled)
        p.sendMessage(ChatColor.RED + lang.getString("disabledSkill"));
        return true;
      }

      //Back button
      GuiItem backButton = new GuiItem(Material.ARROW, 45, gui);
      backButton.setName(lang.getString("diggingPassiveTitle1"));
      backButton.setDescription(lang.getString("diggingPassiveDesc1"));
      gui.addItem(backButton);

      GuiItem skillIcon = new GuiItem(Material.WOODEN_PICKAXE, 4, gui);
      skillIcon.setNameColor(ChatColor.AQUA.toString() + ChatColor.BOLD.toString());
      skillIcon.setName(lang.getString(skillName));

      switch (skillName) {
        case "digging":
          skillIcon.setItemType(Material.IRON_SHOVEL);
          //flintFinder
          togglePerkSetGuiItem("flintToggle", p, gui);

          //Mega Dig
          togglePerkSetGuiItem("megaDigToggle", p, gui);
          break;
        case "woodcutting":
          skillIcon.setItemType(Material.IRON_AXE);

          //leafBlower
          togglePerkSetGuiItem("leafBlowerToggle", p, gui);
          break;
        case "mining":
          skillIcon.setItemType(Material.IRON_PICKAXE);

          //veinMiner
          togglePerkSetGuiItem("veinMinerToggle", p, gui);

          break;
        case "farming":
          skillIcon.setItemType(Material.IRON_HOE);
          break;
        case "fishing":
          skillIcon.setItemType(Material.FISHING_ROD);

          //grappling Hook
          togglePerkSetGuiItem("grappleToggle", p, gui);

          //hot Rod
          togglePerkSetGuiItem("hotRodToggle", p, gui);
          break;
        case "archery":
          skillIcon.setItemType(Material.BOW);
          break;
        case "beastMastery":
          skillIcon.setItemType(Material.BONE);
          break;
        case "swordsmanship":
          skillIcon.setItemType(Material.IRON_SWORD);
          break;
        case "defense":
          skillIcon.setItemType(Material.IRON_CHESTPLATE);
          break;
        case "axeMastery":
          skillIcon.setItemType(Material.GOLDEN_AXE);

          //Holy Axe
          togglePerkSetGuiItem("holyAxeToggle", p, gui);
          break;
        case "repair":
          skillIcon.setItemType(Material.ANVIL);
          break;
        case "agility":
          skillIcon.setItemType(Material.LEATHER_LEGGINGS);

          //gracefulFeet
          togglePerkSetGuiItem("speedToggle", p, gui);

          break;
        case "alchemy":
          skillIcon.setItemType(Material.POTION);

          //potionMaster
          togglePerkSetGuiItem("potionToggle", p, gui);
          break;
        case "smelting":
          skillIcon.setItemType(Material.COAL);

          //flamePick
          togglePerkSetGuiItem("flamePickToggle", p, gui);
          break;
        case "enchanting":
          skillIcon.setItemType(Material.ENCHANTING_TABLE);
          break;
        default:
          break;

      }
      gui.addItem(skillIcon);

      //Sets all the items into the gui (This is redundant, but makes the code a bit clearer)
      gui.setGui();

      //Put the items in the inventory
      gui.displayGuiForPlayer();
    }

    //ConfirmationGUI
    else if (args[0].equalsIgnoreCase("confirmGUI") || args[0].equalsIgnoreCase(
        "confirmationGUI")) {
      final String IMPROPER_ARGUMENTS_MESSSAGE = " /frpg confirmationGUI [::skillName::]";
      CommandHelper commandHelper = new CommandHelper(sender, args, 2, IMPROPER_ARGUMENTS_MESSSAGE);
      commandHelper.setPlayerOnlyCommand(true);
      commandHelper.setCheckInBed(true);
      commandHelper.setPermissionName("confirmGUI");
      if (!commandHelper.isProperCommand()) {
        return true; //Command Restricted or Improper
      }
      Player p = (Player) sender;
      LanguageSelector lang = new LanguageSelector(p);
      String[] labels_0 = {"digging", "woodcutting", "mining", "farming", "fishing", "archery",
          "beastMastery", "swordsmanship", "defense", "axeMastery", "repair", "agility", "alchemy",
          "smelting", "enchanting", "global"};
      String[] titles_0 = {lang.getString("digging"), lang.getString("woodcutting"),
          lang.getString("mining"), lang.getString("farming"), lang.getString("fishing"),
          lang.getString("archery"), lang.getString("beastMastery"),
          lang.getString("swordsmanship"), lang.getString("defense"), lang.getString("axeMastery"),
          lang.getString("repair"), lang.getString("agility"), lang.getString("alchemy"),
          lang.getString("smelting"), lang.getString("enchanting"), lang.getString("global")};
      List<String> labels = Arrays.asList(labels_0);
      String skillName = UtilityMethods.convertStringToListCasing(labels, args[1]);
      if (!labels.contains(skillName)) {
        commandHelper.sendImproperArgumentsMessage();
        return true;
      }
      String skillTitle = titles_0[labels.indexOf(skillName)];
      GuiWrapper gui = new GuiWrapper(p, Bukkit.createInventory(p, 54, "Confirmation Window"));

      //Load souls data
      ConfigLoad loadConfig = new ConfigLoad();
      ArrayList<Integer> soulsInfo = loadConfig.getSoulsInfo();
      String refundCost = Integer.toString(soulsInfo.get(1));

      //Information
      GuiItem info = new GuiItem(Material.PAPER, 22, gui);
      info.setNameColor(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString());
      info.setName(lang.getString("warning"));
      //The following is pretty messy, but it's all just to format a long strong
      String partOne =
          lang.getString("refundSkillTree0") + " " + refundCost + " " + lang.getString("souls")
              + " " + lang.getString("refundSkillTree1");
      String partTwo = lang.getString("refundSkillTree2") + " " + ChatColor.WHITE.toString() +
          skillTitle + ChatColor.RESET.toString() + ChatColor.ITALIC.toString()
          + ChatColor.GRAY.toString() + " " + lang.getString("skill") + "?";
      String refundWarningMessage = partOne + " " + partTwo;
      StringsAndOtherData stringsAndOtherData = new StringsAndOtherData();
      for (String line : stringsAndOtherData.getStringLines(refundWarningMessage)) {
        info.addSpecialLoreLine(ChatColor.ITALIC + ChatColor.GRAY.toString() + line);
      }
      gui.addItem(info);

      //Yes Button
      GuiItem yes = new GuiItem(Material.LIME_TERRACOTTA, 38, gui);
      yes.setNameColor(ChatColor.GREEN.toString() + ChatColor.BOLD.toString());
      yes.setName(lang.getString("yes0"));
      gui.addItem(yes);

      //No Button
      GuiItem no = new GuiItem(Material.RED_TERRACOTTA, 42, gui);
      no.setNameColor(ChatColor.RED.toString() + ChatColor.BOLD.toString());
      no.setName(lang.getString("no0"));
      gui.addItem(no);

      //Skill Item (Indicator)
      GuiItem skillIcon = new GuiItem(Material.IRON_SHOVEL, 4, gui);
      switch (skillName) {
        case "digging":
          skillIcon.setItemType(Material.IRON_SHOVEL);
          break;
        case "woodcutting":
          skillIcon.setItemType(Material.IRON_AXE);
          break;
        case "mining":
          skillIcon.setItemType(Material.IRON_PICKAXE);
          break;
        case "farming":
          skillIcon.setItemType(Material.IRON_HOE);
          break;
        case "fishing":
          skillIcon.setItemType(Material.FISHING_ROD);
          break;
        case "archery":
          skillIcon.setItemType(Material.BOW);
          break;
        case "beastMastery":
          skillIcon.setItemType(Material.BONE);
          break;
        case "swordsmanship":
          skillIcon.setItemType(Material.IRON_SWORD);
          break;
        case "defense":
          skillIcon.setItemType(Material.IRON_CHESTPLATE);
          break;
        case "axeMastery":
          skillIcon.setItemType(Material.GOLDEN_AXE);
          break;
        case "repair":
          skillIcon.setItemType(Material.ANVIL);
          break;
        case "agility":
          skillIcon.setItemType(Material.LEATHER_LEGGINGS);
          break;
        case "alchemy":
          skillIcon.setItemType(Material.POTION);
          break;
        case "smelting":
          skillIcon.setItemType(Material.COAL);
          break;
        case "enchanting":
          skillIcon.setItemType(Material.ENCHANTING_TABLE);
          break;
        default:
          break;
      }
      skillIcon.setNameColor(ChatColor.AQUA.toString() + ChatColor.BOLD.toString());
      skillIcon.setName(skillTitle);
      gui.addItem(skillIcon);

      //Sets all the items into the gui (This is redundant, but makes the code a bit clearer)
      gui.setGui();

      //Put the items in the inventory
      gui.displayGuiForPlayer();
    }

    //CraftingGUI
    else if (args[0].equalsIgnoreCase("craftingGUI") || args[0].equalsIgnoreCase("recipeGUI")) {
      final String IMPROPER_ARGUMENTS_MESSSAGE = " /frpg craftingRecipe [::skillName::[#]]";
      CommandHelper commandHelper = new CommandHelper(sender, args, 2, IMPROPER_ARGUMENTS_MESSSAGE);
      commandHelper.setPlayerOnlyCommand(true);
      commandHelper.setCheckInBed(true);
      commandHelper.setPermissionName("craftGUI");
      if (!commandHelper.isProperCommand()) {
        return true; //Command Restricted or Improper
      }
      Player p = (Player) sender;
      LanguageSelector lang = new LanguageSelector(p);
      String[] labels_0 = {"archery1", "farming1", "farming2", "farming3", "farming4", "farming5",
          "enchanting1", "enchanting2", "enchanting3", "enchanting4", "enchanting5",
          "enchanting6", "enchanting7", "enchanting8", "enchanting9", "enchanting10",
          "alchemy1", "alchemy2", "alchemy3", "alchemy4", "alchemy5"};
      List<String> labels = Arrays.asList(labels_0);
      String identifier = UtilityMethods.convertStringToListCasing(labels, args[1]);
      if (!labels.contains(identifier)) {
        commandHelper.sendImproperArgumentsMessage();
        return true;
      }

      ConfigLoad configLoad = new ConfigLoad();
      Map<String, OldCustomRecipe> craftingRecipes = configLoad.getCraftingRecipes();
      ArrayList<Material> recipe;
      ItemStack output;
      if (identifier.equalsIgnoreCase("archery1")) {
        CraftingRecipes craftingRecipesClass = new CraftingRecipes();
        recipe = craftingRecipesClass.getTippedArrowRecipe();
        output = new ItemStack(Material.TIPPED_ARROW, 8);
      } else {
        recipe = craftingRecipes.get(identifier).getRecipe();
        output = craftingRecipes.get(identifier).getItemStack();
      }

      GuiWrapper gui = new GuiWrapper(p, Bukkit.createInventory(p, 54, "Crafting Recipe"));

      //Back button
      GuiItem backButton = new GuiItem(Material.ARROW, 45, gui);
      backButton.setName(lang.getString("diggingPassiveTitle1"));
      backButton.setDescription(lang.getString("diggingPassiveDesc1"));
      gui.addItem(backButton);

      //Crafting Tables
      Integer[] indicies = {1, 2, 3, 4, 5, 10, 14, 19, 23, 28, 32, 37, 38, 39, 40, 41};

      for (int i : indicies) {
        GuiItem craftingTable = new GuiItem(Material.CRAFTING_TABLE, i, gui);
        craftingTable.setName(ChatColor.WHITE.toString());
        gui.addItem(craftingTable);
      }

      //Connectors
      GuiItem connector = new GuiItem(Material.GLASS_PANE, 24, gui);
      connector.setName(ChatColor.WHITE.toString());
      gui.addItem(connector);

      //Crafting Inputs and Output
      Integer[] recipeIndices = {11, 12, 13, 20, 21, 22, 29, 30, 31};
      for (int i = 0; i < 9; i++) {
        if (recipe.get(i) != null && recipe.get(i) != Material.AIR) {
          GuiItem recipePart = new GuiItem(recipe.get(i), recipeIndices[i], gui);
          gui.addItem(recipePart);
        }
      }
      GuiItem outputItem = new GuiItem(output, 25, gui);
      gui.addItem(outputItem);

      //Sets all the items into the gui (This is redundant, but makes the code a bit clearer)
      gui.setGui();

      //Put the items in the inventory
      gui.displayGuiForPlayer();
    }

    //MainGUI
    else if (args[0].equalsIgnoreCase("mainGUI") || args[0].equalsIgnoreCase("skills")) {
      if (sender instanceof Player) {
        Player p = (Player) sender;
        p.performCommand("frpg");
      } else {
        FreeRPG.log(Level.WARN, "You must be a player to perform this command");
      }
    }



        /*
        This next conditional is the biggest by far
        It handles all the skill tree GUIs, which are the most adaptable with the most unique buttons
        */

    //SkillTreeGUI
    else if (args[0].equalsIgnoreCase("skillTree") || args[0].equalsIgnoreCase("skillTreeGUI")) {
      final String IMPROPER_ARGUMENTS_MESSSAGE = " /frpg skillTree [::skillName::]";
      CommandHelper commandHelper = new CommandHelper(sender, args, 2, IMPROPER_ARGUMENTS_MESSSAGE);
      commandHelper.setPlayerOnlyCommand(true);
      commandHelper.setCheckInBed(true);
      commandHelper.setPermissionName("skillsGUI");
      if (!commandHelper.isProperCommand()) {
        return true; //Command Restricted or Improper
      }

      Player p = (Player) sender;
      LanguageSelector lang = new LanguageSelector(p);
      PlayerStats pStatClass = new PlayerStats(p);
      Map<String, ArrayList<Number>> pStatAll = pStatClass.getPlayerData();
      ConfigLoad configLoad = new ConfigLoad();
      StringsAndOtherData stringsAndOtherData = new StringsAndOtherData();

      String[] titles_0 = {"Digging", "Woodcutting", "Mining", "Farming", "Fishing", "Archery",
          "Beast Mastery", "Swordsmanship", "Defense", "Axe Mastery", "Repair", "Agility",
          "Alchemy", "Smelting", "Enchanting", "Global"};
      String[] labels_0 = {"digging", "woodcutting", "mining", "farming", "fishing", "archery",
          "beastMastery", "swordsmanship", "defense", "axeMastery", "repair", "agility", "alchemy",
          "smelting", "enchanting", "global"};
      List<String> labels_arr = Arrays.asList(labels_0);
      String skillName = UtilityMethods.convertStringToListCasing(labels_arr, args[1]);

      if (!labels_arr.contains(skillName)) {
        commandHelper.sendImproperArgumentsMessage();
        return true;
      }
      String skillTitle = titles_0[labels_arr.indexOf(skillName)];

      if (!configLoad.getAllowedSkillsMap()
          .get(skillName)) { //Special condition to return (skill disabled)
        p.sendMessage(ChatColor.RED + lang.getString("disabledSkill"));
        return true;
      }

      ArrayList<Integer> soulsInfo = configLoad.getSoulsInfo();
      String refundCost = Integer.toString(soulsInfo.get(1));

      Map<String, String[]> perksMap = stringsAndOtherData.getPerksMap();
      Map<String, String[]> descriptionsMap = stringsAndOtherData.getDescriptionsMap();
      Map<String, String[]> passivePerksMap = stringsAndOtherData.getPassivePerksMap();
      Map<String, String[]> passiveDescriptionsMap = stringsAndOtherData.getPassiveDescriptionsMap();
      setTranslatedSkillTreeInformation(p, skillName, perksMap, descriptionsMap, passivePerksMap,
          passiveDescriptionsMap);

      //Create Gui
      //GuiWrapper gui = new GuiWrapper(p,Bukkit.createInventory(p, 54,skillTitle));
      Inventory gui = Bukkit.createInventory(p, 54, skillTitle);

      if (labels_arr.indexOf(skillName) < 10) { //One of the 10 "main" skills

        //Sets colors of the skill icons to indicate whether the perk is locked/unlocked/in progress/completed
        //setSkillTreeProgressMainSkill(p,skillName,gui);

        ArrayList<Number> pStats = pStatAll.get(skillName);
        int skill_1a_level = (Integer) pStats.get(7);
        int skill_1b_level = (Integer) pStats.get(8);
        int skill_2a_level = (Integer) pStats.get(9);
        int skill_2b_level = (Integer) pStats.get(10);
        int skill_3a_level = (Integer) pStats.get(11);
        int skill_3b_level = (Integer) pStats.get(12);
        int skill_M_level = (Integer) pStats.get(13);

        int tokens_P = (Integer) pStats.get(2);
        int tokens_S = (Integer) pStats.get(3);
        Number passive1 = pStats.get(4);
        Number passive2 = pStats.get(5);
        Number passive3 = pStats.get(6);

        ItemStack skill_1a = new ItemStack(Material.PINK_TERRACOTTA);
        ItemStack skill_2a = new ItemStack(Material.RED_TERRACOTTA);
        ItemStack skill_3a = new ItemStack(Material.RED_TERRACOTTA);
        ItemStack skill_1b = new ItemStack(Material.PINK_TERRACOTTA);
        ItemStack skill_2b = new ItemStack(Material.RED_TERRACOTTA);
        ItemStack skill_3b = new ItemStack(Material.RED_TERRACOTTA);
        ItemStack skill_M = new ItemStack(Material.RED_TERRACOTTA);

        if (skill_1a_level == 0) {
          skill_1a.setType(Material.PINK_TERRACOTTA);
        } else if (skill_1a_level > 0 && skill_1a_level < 5) {
          skill_1a.setType(Material.YELLOW_TERRACOTTA);
        } else {
          skill_1a.setType(Material.GREEN_TERRACOTTA);
        }

        if (skill_1b_level == 0) {
          skill_1b.setType(Material.PINK_TERRACOTTA);
        } else if (skill_1b_level > 0 && skill_1b_level < 5) {
          skill_1b.setType(Material.YELLOW_TERRACOTTA);
        } else {
          skill_1b.setType(Material.GREEN_TERRACOTTA);
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

        if (skill_2b_level == 0) {
          if (skill_1b_level >= 2) {
            skill_2b.setType(Material.PINK_TERRACOTTA);
          }
        } else if (skill_2b_level > 0 && skill_2b_level < 5) {
          skill_2b.setType(Material.YELLOW_TERRACOTTA);
        } else {
          skill_2b.setType(Material.GREEN_TERRACOTTA);
        }

        if (skill_3a_level == 0) {
          if (skill_2a_level >= 2) {
            skill_3a.setType(Material.PINK_TERRACOTTA);
          }
        } else {
          skill_3a.setType(Material.GREEN_TERRACOTTA);
        }

        if (skill_3b_level == 0) {
          if (skill_2b_level >= 2) {
            skill_3b.setType(Material.PINK_TERRACOTTA);
          }
        } else {
          skill_3b.setType(Material.GREEN_TERRACOTTA);
        }

        if (skill_M_level == 0) {
          if (skill_1a_level + skill_1b_level + skill_2a_level + skill_2b_level + skill_3a_level
              + skill_3b_level >= 10) {
            skill_M.setType(Material.PINK_TERRACOTTA);
          }
        } else {
          skill_M.setType(Material.GREEN_TERRACOTTA);
        }

        //Beginning state of the menu
        ItemStack[] menu_items = {skill_1a, skill_1b, skill_2a, skill_2b, skill_3a, skill_3b,
            skill_M};
        String[] labels = perksMap.get(skillName);
        String[] lores_line2 = descriptionsMap.get(skillName);

        //Initialize some varibales for use
        String desc = "";
        Map<String, OldCustomRecipe> customRecipeMap = configLoad.getCraftingRecipes();

        //Make changes to some skills independent of level
        if (skillName.equalsIgnoreCase("woodcutting")) {
          ArrayList<Integer> timberBreakLimits = configLoad.getTimberBreakLimits();
          String timberBreakLimitInitial = String.valueOf(timberBreakLimits.get(0));
          String timberBreakLimitUpgraded = String.valueOf(timberBreakLimits.get(1));
          String newLore0 = stringsAndOtherData.replaceIfPresent(lores_line2[4], "64",
              timberBreakLimitInitial);
          String newLore1 = stringsAndOtherData.replaceIfPresent(newLore0, "128",
              timberBreakLimitUpgraded);
          lores_line2[4] = newLore1;
        }

        int special_index = 0;
        switch (skillName) {
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
            ArrayList<Object> woodcuttingInfo = configLoad.getWoodcuttingInfo();
            switch (skill_2b_level) {
              case 0:
                Material mat0 = (Material) woodcuttingInfo.get(0);
                String itemName0 = "";
                if (mat0 != null) {
                  itemName0 = mat0.toString().replaceAll("_", " ").toLowerCase();
                } else {
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
                } else {
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
                } else {
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
                } else {
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
                } else {
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
                desc += "I (" + lang.getString("common") + ")";
                lores_line2[special_index] = desc;
                break;
              case 1:
                desc += "II (" + lang.getString("uncommon") + ")";
                lores_line2[special_index] = desc;
                break;
              case 2:
                desc += "III (" + lang.getString("rare") + ")";
                lores_line2[special_index] = desc;
                break;
              case 3:
                desc += "IV (" + lang.getString("veryRare") + ")";
                lores_line2[special_index] = desc;
                break;
              case 4:
                desc += "V (" + lang.getString("legendary") + ")";
                lores_line2[special_index] = desc;
                break;
              default:
                break;
            }
            break;
          case "farming":
            special_index = 1;
            desc = lang.getString("farmingPerkDesc1_1") + " ";
            switch (skill_1b_level) {
              case 0:
                Material output1 = customRecipeMap.get("farming" + 1).getOutput();
                if (output1.equals(Material.COW_SPAWN_EGG)) {
                  desc += lang.getString("cowSpawnEgg");
                } else {
                  desc += stringsAndOtherData.cleanUpTitleString(output1.toString());
                }
                lores_line2[special_index] = desc;
                break;
              case 1:
                Material output2 = customRecipeMap.get("farming" + 2).getOutput();
                if (output2.equals(Material.BEE_SPAWN_EGG)) {
                  desc += lang.getString("beeSpawnEgg");
                } else {
                  desc += stringsAndOtherData.cleanUpTitleString(output2.toString());
                }
                lores_line2[special_index] = desc;
                break;
              case 2:
                Material output3 = customRecipeMap.get("farming" + 3).getOutput();
                if (output3.equals(Material.MOOSHROOM_SPAWN_EGG)) {
                  desc += lang.getString("mooshroomSpawnEgg");
                } else {
                  desc += stringsAndOtherData.cleanUpTitleString(output3.toString());
                }
                lores_line2[special_index] = desc;
                break;
              case 3:
                Material output4 = customRecipeMap.get("farming" + 4).getOutput();
                if (output4.equals(Material.HORSE_SPAWN_EGG)) {
                  desc += lang.getString("horseSpawnEgg");
                } else {
                  desc += stringsAndOtherData.cleanUpTitleString(output4.toString());
                }
                lores_line2[special_index] = desc;
                break;
              case 4:
                Material output5 = customRecipeMap.get("farming" + 5).getOutput();
                if (output5.equals(Material.SLIME_SPAWN_EGG)) {
                  desc += lang.getString("slimeSpawnEgg");
                } else {
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
            ArrayList<Object> diggingInfo = configLoad.getDiggingInfo();
            switch (skill_1a_level) {
              case 0:
                desc = lang.getString("diggingPerkDesc0_1") + " ";
                Material mat0 = (Material) diggingInfo.get(10);
                String itemName0 = "";
                if (mat0 != null) {
                  itemName0 = mat0.toString().replaceAll("_", " ").toLowerCase();
                } else {
                  itemName0 = "gold ingot";
                }
                desc += itemName0;
                lores_line2[special_index] = desc;
                break;
              case 1:
                desc = lang.getString("diggingPerkDesc0_1") + " ";
                Material mat1 = (Material) diggingInfo.get(12);
                String itemName1 = "";
                if (mat1 != null) {
                  itemName1 = mat1.toString().replaceAll("_", " ").toLowerCase();
                } else {
                  itemName1 = "name tag";
                }
                desc += itemName1;
                lores_line2[special_index] = desc;
                break;
              case 2:
                desc = lang.getString("diggingPerkDesc0_1") + " ";
                Material mat2 = (Material) diggingInfo.get(14);
                String itemName2 = "";
                if (mat2 != null) {
                  itemName2 = mat2.toString().replaceAll("_", " ").toLowerCase();
                } else {
                  itemName2 = "music discs";
                }
                desc += itemName2;
                lores_line2[special_index] = desc;
                break;
              case 3:
                desc = lang.getString("diggingPerkDesc0_1") + " ";
                Material mat3 = (Material) diggingInfo.get(16);
                String itemName3 = "";
                if (mat3 != null) {
                  itemName3 = mat3.toString().replaceAll("_", " ").toLowerCase();
                } else {
                  itemName3 = "horse armor";
                }
                desc += itemName3;
                lores_line2[special_index] = desc;
                break;
              case 4:
                desc = lang.getString("diggingPerkDesc0_1") + " ";
                Material mat4 = (Material) diggingInfo.get(18);
                String itemName4 = "";
                if (mat4 != null) {
                  itemName4 = mat4.toString().replaceAll("_", " ").toLowerCase();
                } else {
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
                desc = lang.getString("diggingPerkDesc0_1") + " ";
                Material mat5 = (Material) diggingInfo.get(21);
                String itemName5 = "";
                if (mat5 != null) {
                  itemName5 = mat5.toString().replaceAll("_", " ").toLowerCase();
                } else {
                  itemName5 = "emerald";
                }
                desc += itemName5;
                lores_line2[2] = desc;
                break;
              case 1:
                desc = lang.getString("diggingPerkDesc0_1") + " ";
                Material mat6 = (Material) diggingInfo.get(24);
                String itemName6 = "";
                if (mat6 != null) {
                  itemName6 = mat6.toString().replaceAll("_", " ").toLowerCase();
                } else {
                  itemName6 = "enchanted book";
                }
                desc += itemName6;
                lores_line2[2] = desc;
                break;
              case 2:
                desc = lang.getString("diggingPerkDesc0_1") + " ";
                Material mat7 = (Material) diggingInfo.get(27);
                String itemName7 = "";
                if (mat7 != null) {
                  itemName7 = mat7.toString().replaceAll("_", " ").toLowerCase();
                } else {
                  itemName7 = "dragon breath";
                }
                desc += itemName7;
                lores_line2[2] = desc;
                break;
              case 3:
                desc = lang.getString("diggingPerkDesc0_1") + " ";
                Material mat8 = (Material) diggingInfo.get(30);
                String itemName8 = "";
                if (mat8 != null) {
                  itemName8 = mat8.toString().replaceAll("_", " ").toLowerCase();
                } else {
                  itemName8 = "totem of undying";
                }
                desc += itemName8;
                lores_line2[2] = desc;
                break;
              case 4:
                desc = lang.getString("diggingPerkDesc0_1") + " ";
                Material mat9 = (Material) diggingInfo.get(30);
                String itemName9 = "";
                if (mat9 != null) {
                  itemName9 = mat9.toString().replaceAll("_", " ").toLowerCase();
                } else {
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

        String[] lores_line1 = {"Level: 0/5", "Level: 0/5", "Level: 0/5", "Level: 0/5",
            "Level: 0/1", "Level: 0/1", "Level: 0/1"}; //Data lines 7-13
        for (int i = 0; i < 4; i++) {
          String level = pStats.get(7 + i).toString();
          lores_line1[i] =
              ChatColor.GRAY + lang.getString("level") + " " + ChatColor.GREEN + level + "/5";
        }
        for (int i = 0; i < 3; i++) {
          String level = pStats.get(11 + i).toString();
          if (i != 2) {
            lores_line1[i + 4] =
                ChatColor.GRAY + lang.getString("level") + " " + ChatColor.BLUE + level + "/1";
          } else {
            lores_line1[i + 4] =
                ChatColor.GRAY + lang.getString("level") + " " + ChatColor.DARK_PURPLE + level
                    + "/1";
          }
        }

        Integer[] indices = {11, 29, 13, 31, 7, 43, 26};
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
        ItemStack passive_1 = new ItemStack(Material.RED_DYE, 1);
        ItemStack passive_2 = new ItemStack(Material.GREEN_DYE, 2);
        ItemStack passive_3 = new ItemStack(Material.BLUE_DYE, 3);
        ItemStack back_button = new ItemStack(Material.ARROW);

        ItemStack[] menu_items_2 = {passive_token, back_button, skill_token, passive_1, passive_2,
            passive_3};
        String[] labels_2 = passivePerksMap.get(skillName);
        String[] lores_line1_2 = {"Total: 0", "", "Total: 0", "Duration: 1 s", "Percentage: 0%",
            "Percentage: 0%"};
        String[] lores_line2_2 = passiveDescriptionsMap.get(skillName);

        //Total Passive Tokens
        lores_line1_2[0] =
            ChatColor.BLUE + lang.getString("total") + ": " + ChatColor.GOLD + String.valueOf(
                tokens_P);
        if (tokens_P > 1 && tokens_P < 64) {
          passive_token.setAmount(tokens_P);
        } else if (tokens_P >= 64) {
          passive_token.setAmount(64);
        }

        //Total skill tokens
        lores_line1_2[2] =
            ChatColor.BLUE + lang.getString("total") + ": " + ChatColor.GOLD + String.valueOf(
                tokens_S);
        if (tokens_S > 1 && tokens_S < 64) {
          skill_token.setAmount(tokens_S);
        } else if (tokens_S >= 64) {
          skill_token.setAmount(64);
        }

        double duration = passive1.doubleValue();
        double chance1 = passive2.doubleValue();
        double chance2 = passive3.doubleValue();
        //Passive 1
        duration = duration * 0.02 + 2;
        duration = Math.round(duration * 1000) / 1000.0d;
        lores_line1_2[3] =
            ChatColor.GRAY + lang.getString("duration") + ": " + ChatColor.AQUA + String.valueOf(
                duration) + " s";
        switch (skillName) {
          case "digging":
            //Passive2
            chance1 = 1 + chance1 * 0.01;
            chance1 = Math.round(chance1 * 1000) / 1000.0d;
            lores_line1_2[4] = ChatColor.GRAY + lang.getString("likelihood") + ": " + ChatColor.AQUA
                + String.valueOf(chance1) + "%";

            //Passive 3
            break;
          case "woodcutting":
          case "archery":
            //Passive 2
            chance1 = chance1 * 0.05;
            chance1 = Math.round(chance1 * 1000) / 1000.0d;
            lores_line1_2[4] = ChatColor.GRAY + lang.getString("likelihood") + ": " + ChatColor.AQUA
                + String.valueOf(chance1) + "%";

            //Passive 3
            break;

          //Passive 3
          case "mining":
            //Passive 2
            chance1 = chance1 * 0.05;
            chance1 = Math.round(chance1 * 1000) / 1000.0d;
            lores_line1_2[4] = ChatColor.GRAY + lang.getString("likelihood") + ": " + ChatColor.AQUA
                + String.valueOf(chance1) + "%";

            //Passive 3
            chance2 = chance2 * 0.01;
            chance2 = Math.round(chance2 * 1000) / 1000.0d;
            lores_line1_2[5] = ChatColor.GRAY + lang.getString("likelihood") + ": " + ChatColor.AQUA
                + String.valueOf(chance2) + "%";
            break;
          case "farming":
            //Passive 2
            chance1 = chance1 * 0.05;
            chance1 = Math.round(chance1 * 1000) / 1000.0d;
            lores_line1_2[4] = ChatColor.GRAY + lang.getString("likelihood") + ": " + ChatColor.AQUA
                + String.valueOf(chance1) + "%";

            //Passive 3
            chance2 = chance2 * 0.05;
            chance2 = Math.round(chance2 * 1000) / 1000.0d;
            lores_line1_2[5] = ChatColor.GRAY + lang.getString("likelihood") + ": " + ChatColor.AQUA
                + String.valueOf(chance2) + "%";
            break;
          case "fishing":
            //Passive 1
            duration = duration / 2.0;
            duration = Math.round(duration * 1000) / 1000.0d;
            lores_line1_2[3] = ChatColor.GRAY + lang.getString("duration") + ": " + ChatColor.AQUA
                + String.valueOf(duration) + " s";

            //Passive 2
            chance1 = chance1 * 0.05;
            chance1 = Math.round(chance1 * 1000) / 1000.0d;
            lores_line1_2[4] = ChatColor.GRAY + lang.getString("likelihood") + ": " + ChatColor.AQUA
                + String.valueOf(chance1) + "%";

            //Passive 3
            chance2 = 10 - chance2 * 0.005;
            chance2 = Math.round(chance2 * 1000) / 1000.0d;
            lores_line1_2[5] = ChatColor.GRAY + lang.getString("junkChance") + ": " + ChatColor.AQUA
                + String.valueOf(chance2) + "%";
            break;

          //Passive 3
          case "beastMastery":
            //Passive 2
            chance1 = chance1 * 0.025;
            chance1 = Math.round(chance1 * 1000) / 1000.0d;
            lores_line1_2[4] = ChatColor.GRAY + lang.getString("likelihood") + ": " + ChatColor.AQUA
                + String.valueOf(chance1) + "%";

            //Passive 3
            break;
          case "swordsmanship":
            //Passive 2
            chance1 = chance1 * 0.02;
            chance1 = Math.round(chance1 * 1000) / 1000.0d;
            lores_line1_2[4] = ChatColor.GRAY + lang.getString("likelihood") + ": " + ChatColor.AQUA
                + String.valueOf(chance1) + "%";

            //Passive 3
            break;
          case "defense":
            //Passive 2
            chance1 = 1 + chance1 * 0.01;
            chance1 = Math.round(chance1 * 1000) / 1000.0d;
            lores_line1_2[4] = ChatColor.GRAY + lang.getString("likelihood") + ": " + ChatColor.AQUA
                + String.valueOf(chance1) + "%";

            //Passive 3
            chance2 = chance2 * 0.05;
            chance2 = Math.round(chance2 * 1000) / 1000.0d;
            lores_line1_2[5] = ChatColor.GRAY + lang.getString("likelihood") + ": " + ChatColor.AQUA
                + String.valueOf(chance2) + "%";

            break;

          case "axeMastery":
            //Passive 2
            chance1 = chance1 * 0.01;
            chance1 = Math.round(chance1 * 1000) / 1000.0d;
            lores_line1_2[4] = ChatColor.GRAY + lang.getString("likelihood") + ": " + ChatColor.AQUA
                + String.valueOf(chance1) + "%";

            //Passive 3
            break;

          //Passive 3
          default:
            break;
        }

        Integer[] indices_2 = {0, 45, 9, 18, 27, 36};
        for (int i = 0; i < labels_2.length; i++) {
          ItemMeta meta = menu_items_2[i].getItemMeta();
          meta.setDisplayName(ChatColor.BOLD + labels_2[i]);
          ArrayList<String> lore = new ArrayList<>();
          String longString = lores_line2_2[i];
          ArrayList<String> splitDescs = stringsAndOtherData.getStringLines(longString);

          if (i == 3) {
            splitDescs.add("");
            splitDescs.add(ChatColor.UNDERLINE + lang.getString("abilityDescription"));
            ArrayList<String> appendingDesc = stringsAndOtherData.getStringLines(
                lang.getString("abilityDescription_" + skillName));
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
          Integer[] indices_crafting = {48, 49, 50, 51, 52};
          ItemStack[] crafting = {new ItemStack(Material.CRAFTING_TABLE),
              new ItemStack(Material.CRAFTING_TABLE),
              new ItemStack(Material.CRAFTING_TABLE), new ItemStack(Material.CRAFTING_TABLE),
              new ItemStack(Material.CRAFTING_TABLE)};
          String[] craftingNames = {lang.getString("cowEgg"), lang.getString("beeEgg"),
              lang.getString("mooshroomEgg"), lang.getString("horseEgg"),
              lang.getString("slimeEgg")};
          Material[] defaultMaterials = {Material.COW_SPAWN_EGG, Material.BEE_SPAWN_EGG,
              Material.MOOSHROOM_SPAWN_EGG, Material.HORSE_SPAWN_EGG, Material.SLIME_SPAWN_EGG};
          for (int i = 0; i < craftingNames.length; i++) {
            int stringIndex = i + 1;
            Material output = customRecipeMap.get("farming" + stringIndex).getOutput();
            if (!output.equals(defaultMaterials[i])) {
              craftingNames[i] = stringsAndOtherData.cleanUpTitleString(output.toString());
            }
          }
          int animalFarmLevel = (int) pStats.get(8);
          for (int i = 0; i < craftingNames.length; i++) {
            ArrayList<String> lore = new ArrayList<>();
            ItemMeta craftingMeta = crafting[i].getItemMeta();
            if (animalFarmLevel >= i + 1) {
              lore.add(ChatColor.GREEN + ChatColor.ITALIC.toString() + lang.getString("unlocked"));
            } else {
              lore.add(ChatColor.RED + ChatColor.ITALIC.toString() + lang.getString("locked"));
            }
            craftingMeta.setDisplayName(ChatColor.BOLD + craftingNames[i]);
            craftingMeta.setLore(lore);
            crafting[i].setItemMeta(craftingMeta);
            gui.setItem(indices_crafting[i], crafting[i]);
          }
        } else if (skillName.equalsIgnoreCase("archery")) {
          Integer[] indices_crafting = {48, 49, 50, 51, 52};
          ItemStack[] crafting = {new ItemStack(Material.CRAFTING_TABLE)};
          String[] craftingNames = {lang.getString("tippedArrows")};
          int dragonlessArrowsLevel = (int) pStats.get(11);
          for (int i = 0; i < craftingNames.length; i++) {
            ArrayList<String> lore = new ArrayList<>();
            ItemMeta craftingMeta = crafting[i].getItemMeta();
            if (dragonlessArrowsLevel >= 1) {
              lore.add(ChatColor.GREEN + ChatColor.ITALIC.toString() + lang.getString("unlocked"));
            } else {
              lore.add(ChatColor.RED + ChatColor.ITALIC.toString() + lang.getString("locked"));
            }
            craftingMeta.setDisplayName(ChatColor.BOLD + craftingNames[i]);
            craftingMeta.setLore(lore);
            crafting[i].setItemMeta(craftingMeta);
            gui.setItem(indices_crafting[i], crafting[i]);
          }
        }

        //Soul Bucket (Refunding)
        ItemStack soul = new ItemStack(Material.COMPOSTER);
        ItemMeta soulMeta = soul.getItemMeta();
        ArrayList<String> soulLore = new ArrayList<>();
        soulMeta.setDisplayName(ChatColor.BOLD + lang.getString("refundSkillTitle"));
        if ((int) pStatAll.get("global").get(9) < 1) {
          soulLore.add(ChatColor.RED + ChatColor.ITALIC.toString() + lang.getString("locked"));
        } else {
          int souls = (int) pStatAll.get("global").get(20);
          String soulsString = lang.getString("souls");
          String soulsCapitilized = UtilityMethods.capitalizeString(soulsString);
          soulLore.add(
              soulsCapitilized + ": " + ChatColor.AQUA + ChatColor.ITALIC.toString() + souls + "/"
                  + refundCost);
          soulLore.add(
              ChatColor.GRAY + ChatColor.ITALIC.toString() + lang.getString("refundSkillTreeDesc"));
          soulLore.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + "(-" + refundCost + " "
              + lang.getString("souls") + ")");
        }
        soulMeta.setLore(soulLore);
        soul.setItemMeta(soulMeta);
        gui.setItem(47, soul);

        //Configuration Menu:
        ItemStack configItem = new ItemStack(Material.REDSTONE);
        ItemMeta configItemMeta = configItem.getItemMeta();
        ArrayList<String> configItemLore = new ArrayList<>();
        configItemMeta.setDisplayName(ChatColor.BOLD + lang.getString("configuration"));
        configItemLore.addAll(
            stringsAndOtherData.getStringLines(lang.getString("skillConfigDesc")));
        configItemMeta.setLore(configItemLore);
        configItem.setItemMeta(configItemMeta);
        gui.setItem(53, configItem);

        //Connectors
        ItemStack connector = new ItemStack(Material.GLASS_PANE);
        ItemMeta connectorMeta = connector.getItemMeta();
        connectorMeta.setDisplayName(ChatColor.WHITE.toString());
        connector.setItemMeta(connectorMeta);
        Integer[] indices_3 = {6, 12, 14, 24, 25, 30, 32, 42};
        for (int i = 0; i < indices_3.length; i++) {
          gui.setItem(indices_3[i], connector);
        }
        //Put the items in the inventory
        p.openInventory(gui);
      } else if (sender instanceof Player && labels_arr.indexOf(args[1]) >= 10
          && labels_arr.indexOf(args[1]) < 15) {

        //Skills
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
                desc = lang.getString("alchemyPerkDesc1_0") + " ";
                desc += stringsAndOtherData.getPotionEffectTypeString(1, p);
                lores_line2[special_index] = desc;
                break;
              case 1:
                desc = lang.getString("alchemyPerkDesc1_0") + " ";
                desc += stringsAndOtherData.getPotionEffectTypeString(2, p);
                lores_line2[special_index] = desc;
                break;
              case 2:
                desc = lang.getString("alchemyPerkDesc1_0") + " ";
                desc += stringsAndOtherData.getPotionEffectTypeString(3, p);
                lores_line2[special_index] = desc;
                break;
              case 3:
                desc = lang.getString("alchemyPerkDesc1_0") + " ";
                desc += stringsAndOtherData.getPotionEffectTypeString(4, p);
                lores_line2[special_index] = desc;
                break;
              case 4:
                desc = lang.getString("alchemyPerkDesc1_0") + " ";
                desc += stringsAndOtherData.getPotionEffectTypeString(5, p);
                lores_line2[special_index] = desc;
                break;
              default:
                break;
            }
            switch (skill_1a_level) {
              case 0:
                desc = lang.getString("alchemyPerkDesc0_0") + " ";
                desc += stringsAndOtherData.getPotionTypeString(1, p);
                lores_line2[0] = desc;
                break;
              case 1:
                desc = lang.getString("alchemyPerkDesc0_0") + " ";
                desc += stringsAndOtherData.getPotionTypeString(2, p);
                lores_line2[0] = desc;
                break;
              case 2:
                desc = lang.getString("alchemyPerkDesc0_0") + " ";
                desc += stringsAndOtherData.getPotionTypeString(3, p);
                lores_line2[0] = desc;
                break;
              case 3:
                desc = lang.getString("alchemyPerkDesc0_0") + " ";
                desc += stringsAndOtherData.getPotionTypeString(4, p);
                lores_line2[0] = desc;
                break;
              case 4:
                desc = lang.getString("alchemyPerkDesc0_0") + " ";
                desc += stringsAndOtherData.getPotionTypeString(5, p);
                lores_line2[0] = desc;
                break;
              default:
                break;
            }
            break;
          case "enchanting":
            special_index = 1;
            desc = lang.getString("enchantingPerkDesc1_0") + " ";
            StringsAndOtherData stringsAndOtherData1 = new StringsAndOtherData();
            switch (skill_2a_level) {
              case 0:
                desc += stringsAndOtherData1.getEnchantmentPerkDescString(1, p);
                lores_line2[special_index] = desc;
                break;
              case 1:
                desc += stringsAndOtherData1.getEnchantmentPerkDescString(2, p);
                lores_line2[special_index] = desc;
                break;
              case 2:
                desc += stringsAndOtherData1.getEnchantmentPerkDescString(3, p);
                lores_line2[special_index] = desc;
                break;
              case 3:
                desc += stringsAndOtherData1.getEnchantmentPerkDescString(4, p);
                lores_line2[special_index] = desc;
                break;
              case 4:
                desc += stringsAndOtherData1.getEnchantmentPerkDescString(5, p);
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
        String[] lores_line1 = {"Level: 0/5", "Level: 0/5", "Level: 0/1"};
        String level = pStats.get(7).toString();
        lores_line1[0] =
            ChatColor.GRAY + lang.getString("level") + " " + ChatColor.GREEN + level + "/5";
        level = pStats.get(9).toString();
        lores_line1[1] =
            ChatColor.GRAY + lang.getString("level") + " " + ChatColor.GREEN + level + "/5";
        level = pStats.get(13).toString();
        lores_line1[2] =
            ChatColor.GRAY + lang.getString("level") + " " + ChatColor.DARK_PURPLE + level + "/1";

        Integer[] indices = {20, 23, 26};
        //Set skills
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
        ItemStack skill_token = new ItemStack(Material.GOLD_NUGGET);
        ItemStack passive_1 = new ItemStack(Material.RED_DYE, 1);
        ItemStack back_button = new ItemStack(Material.ARROW);

        ItemStack[] menu_items_2 = {back_button, skill_token, passive_1};
        String[] labels_2 = passivePerksMap.get(skillName);
        String[] lores_line1_2 = {"", "Total: 0", "Chance: 0 %"};
        String[] lores_line2_2 = passiveDescriptionsMap.get(skillName);

        //Total skill tokens
        lores_line1_2[1] =
            ChatColor.BLUE + lang.getString("total") + ": " + ChatColor.GOLD + String.valueOf(
                tokens_S);
        if (tokens_S > 1 && tokens_S < 64) {
          skill_token.setAmount(tokens_S);
        } else if (tokens_S >= 64) {
          skill_token.setAmount(64);
        }

        double passive = passive1.doubleValue();
        switch (skillName) {
          case "repair":
            passive = passive;
            //passive = Math.round(passive*1000)/1000.0d;
            lores_line1_2[2] =
                ChatColor.GRAY + lang.getString("level") + ": " + ChatColor.AQUA + String.valueOf(
                    passive);
            break;
          case "agility":
            passive = passive * 0.05;
            passive = Math.round(passive * 1000) / 1000.0d;
            lores_line1_2[2] = ChatColor.GRAY + lang.getString("likelihood") + ": " + ChatColor.AQUA
                + String.valueOf(passive) + "%";
            break;
          case "enchanting":
            passive = passive * 0.2;
            passive = Math.round(passive * 1000) / 1000.0d;
            lores_line1_2[2] =
                ChatColor.GRAY + lang.getString("xpBoost") + ": " + ChatColor.AQUA + "+"
                    + String.valueOf(passive) + "%";
            break;
          case "smelting":
            passive = passive * 0.2;
            passive = Math.round(passive * 1000) / 1000.0d;
            lores_line1_2[2] =
                ChatColor.GRAY + lang.getString("speedBoost") + ": " + ChatColor.AQUA + "+"
                    + String.valueOf(passive) + "%";
            break;
          case "alchemy":
            passive = passive * 0.1;
            passive = Math.round(passive * 1000) / 1000.0d;
            lores_line1_2[2] =
                ChatColor.GRAY + lang.getString("timeExtension") + ": " + ChatColor.AQUA + "+"
                    + String.valueOf(passive) + "%";
            break;
          default:
            break;
        }

        Integer[] indices_2 = {45, 0, 18};
        for (int i = 0; i < labels_2.length; i++) {
          ItemMeta meta = menu_items_2[i].getItemMeta();
          meta.setDisplayName(ChatColor.BOLD + labels_2[i]);
          ArrayList<String> lore = new ArrayList<>();
          lore.add(lores_line1_2[i]);
          String longString = lores_line2_2[i];
          ArrayList<String> splitDescs = stringsAndOtherData.getStringLines(longString);
          for (int j = 0; j < splitDescs.size(); j++) {
            lore.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + splitDescs.get(j));
          }
          ;
          meta.setLore(lore);
          menu_items_2[i].setItemMeta(meta);
          gui.setItem(indices_2[i], menu_items_2[i]);
        }

        //Crafting
        if (skillName.equalsIgnoreCase("enchanting")) {
          Integer[] indices_crafting = {39, 40, 41, 42, 43, 48, 49, 50, 51, 52};
          ItemStack[] crafting = {new ItemStack(Material.CRAFTING_TABLE),
              new ItemStack(Material.CRAFTING_TABLE),
              new ItemStack(Material.CRAFTING_TABLE), new ItemStack(Material.CRAFTING_TABLE),
              new ItemStack(Material.CRAFTING_TABLE), new ItemStack(Material.CRAFTING_TABLE),
              new ItemStack(Material.CRAFTING_TABLE), new ItemStack(Material.CRAFTING_TABLE),
              new ItemStack(Material.CRAFTING_TABLE), new ItemStack(Material.CRAFTING_TABLE)};
          String[] craftingNames = stringsAndOtherData.getEnchantingCraftingNames(p);
          int bookSmartLevel = (int) pStats.get(9);
          for (int i = 0; i < craftingNames.length; i++) {
            ArrayList<String> lore = new ArrayList<>();
            ItemMeta craftingMeta = crafting[i].getItemMeta();
            if (bookSmartLevel >= Math.ceil((double) (i + 1) / 2.0)) {
              lore.add(ChatColor.GREEN + ChatColor.ITALIC.toString() + lang.getString("unlocked"));
            } else {
              lore.add(ChatColor.RED + ChatColor.ITALIC.toString() + lang.getString("locked"));
            }
            craftingMeta.setDisplayName(ChatColor.BOLD + craftingNames[i]);
            craftingMeta.setLore(lore);
            crafting[i].setItemMeta(craftingMeta);
            gui.setItem(indices_crafting[i], crafting[i]);
          }
        } else if (skillName.equalsIgnoreCase("alchemy")) {
          Integer[] indices_crafting = {48, 49, 50, 51, 52};
          ItemStack[] crafting = {new ItemStack(Material.CRAFTING_TABLE),
              new ItemStack(Material.CRAFTING_TABLE),
              new ItemStack(Material.CRAFTING_TABLE), new ItemStack(Material.CRAFTING_TABLE),
              new ItemStack(Material.CRAFTING_TABLE)};
          String[] craftingNames = {stringsAndOtherData.getPotionTypeString(1, p),
              stringsAndOtherData.getPotionTypeString(2, p),
              stringsAndOtherData.getPotionTypeString(3, p),
              stringsAndOtherData.getPotionTypeString(4, p),
              stringsAndOtherData.getPotionTypeString(5, p)};
          int alchemicalSummoningLevel = (int) pStats.get(7);
          for (int i = 0; i < craftingNames.length; i++) {
            ArrayList<String> lore = new ArrayList<>();
            ItemMeta craftingMeta = crafting[i].getItemMeta();
            if (alchemicalSummoningLevel > i) {
              lore.add(ChatColor.GREEN + ChatColor.ITALIC.toString() + lang.getString("unlocked"));
            } else {
              lore.add(ChatColor.RED + ChatColor.ITALIC.toString() + lang.getString("locked"));
            }
            craftingMeta.setDisplayName(ChatColor.BOLD + craftingNames[i]);
            craftingMeta.setLore(lore);
            crafting[i].setItemMeta(craftingMeta);
            gui.setItem(indices_crafting[i], crafting[i]);
          }
          Integer[] indices_brewing = {39, 40, 41, 42, 43};
          ItemStack[] brewing = {new ItemStack(Material.IRON_BARS),
              new ItemStack(Material.IRON_BARS),
              new ItemStack(Material.IRON_BARS), new ItemStack(Material.IRON_BARS),
              new ItemStack(Material.IRON_BARS)};
          ItemGroups itemGroups = new ItemGroups();
          List<Material> brewingUnlocked = itemGroups.getNewIngredients();
          int ancientKnowledgeLevel = (int) pStats.get(9);
          for (int i = 0; i < brewing.length; i++) {
            String lvl = String.valueOf(i + 1);
            ArrayList<String> lore = new ArrayList<>();
            ItemStack brewingItem = brewing[i];
            ItemMeta brewingMeta = brewingItem.getItemMeta();
            brewingMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            brewingMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            brewingMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            String brewingName =
                lang.getString("alchemyPerkTitle1") + " " + lang.getString("lvl") + " " + lvl + " "
                    + lang.getString("ingredient");
            if (ancientKnowledgeLevel > i) {
              brewingItem.setType(brewingUnlocked.get(i));
              lore.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + lang.getString("usedToBrew"));
              lore.add(ChatColor.GRAY + stringsAndOtherData.getPotionEffectTypeString(i + 1, p));
            } else {
              lore.add(ChatColor.RED + ChatColor.ITALIC.toString() + lang.getString("locked"));
            }
            brewingMeta.setDisplayName(ChatColor.WHITE + ChatColor.BOLD.toString() + brewingName);
            brewingMeta.setLore(lore);
            brewingItem.setItemMeta(brewingMeta);
            gui.setItem(indices_brewing[i], brewingItem);

          }
        }

        //Souls (refunding)
        ItemStack soul = new ItemStack(Material.COMPOSTER);
        ItemMeta soulMeta = soul.getItemMeta();
        ArrayList<String> soulLore = new ArrayList<>();
        soulMeta.setDisplayName(ChatColor.BOLD + lang.getString("refundSkillTitle"));
        if ((int) pStatAll.get("global").get(9) < 1) {
          soulLore.add(ChatColor.RED + ChatColor.ITALIC.toString() + lang.getString("locked"));
        } else {
          int souls = (int) pStatAll.get("global").get(20);
          String soulsString = lang.getString("souls");
          String soulsCapitilized = UtilityMethods.capitalizeString(soulsString);
          soulLore.add(
              soulsCapitilized + ": " + ChatColor.AQUA + ChatColor.ITALIC.toString() + souls + "/"
                  + refundCost);
          soulLore.add(
              ChatColor.GRAY + ChatColor.ITALIC.toString() + lang.getString("refundSkillTreeDesc"));
          soulLore.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + "(-" + refundCost + " "
              + lang.getString("souls") + ")");
        }
        soulMeta.setLore(soulLore);
        soul.setItemMeta(soulMeta);
        gui.setItem(47, soul);

        ItemStack configItem = new ItemStack(Material.REDSTONE);
        ItemMeta configItemMeta = configItem.getItemMeta();
        ArrayList<String> configItemLore = new ArrayList<>();
        configItemMeta.setDisplayName(ChatColor.BOLD + lang.getString("configuration"));
        configItemLore.addAll(
            stringsAndOtherData.getStringLines(lang.getString("skillConfigDesc")));
        configItemMeta.setLore(configItemLore);
        configItem.setItemMeta(configItemMeta);
        gui.setItem(53, configItem);

        //Connectors
        ItemStack connector = new ItemStack(Material.GLASS_PANE);
        ItemMeta connectorMeta = connector.getItemMeta();
        connectorMeta.setDisplayName(ChatColor.WHITE.toString());
        connector.setItemMeta(connectorMeta);
        Integer[] indices_3 = {21, 22, 24, 25};
        for (int i = 0; i < indices_3.length; i++) {
          gui.setItem(indices_3[i], connector);
        }
        //Put the items in the inventory
        p.openInventory(gui);

      } else if (sender instanceof Player && labels_arr.indexOf(args[1]) > 10 && labels_arr.indexOf(
          args[1]) == 15) {
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
        int skill_M_level = (Integer) pStats.get(11);

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
        } else {
          skill_1a.setType(Material.GREEN_TERRACOTTA);
        }

        if (skill_1b_level == 0) {
          skill_1b.setType(Material.PINK_TERRACOTTA);
        } else {
          skill_1b.setType(Material.GREEN_TERRACOTTA);
        }

        if (skill_1c_level == 0) {
          skill_1c.setType(Material.PINK_TERRACOTTA);
        } else {
          skill_1c.setType(Material.GREEN_TERRACOTTA);
        }

        if (skill_2a_level == 0) {
          if (skill_1a_level == 1) {
            skill_2a.setType(Material.PINK_TERRACOTTA);
          } else {
            skill_2a.setType(Material.RED_TERRACOTTA);
          }
        } else {
          skill_2a.setType(Material.GREEN_TERRACOTTA);
        }

        if (skill_2b_level == 0) {
          if (skill_1b_level == 1) {
            skill_2b.setType(Material.PINK_TERRACOTTA);
          } else {
            skill_2b.setType(Material.RED_TERRACOTTA);
          }
        } else {
          skill_2b.setType(Material.GREEN_TERRACOTTA);
        }

        if (skill_2c_level == 0) {
          if (skill_1c_level == 1) {
            skill_2c.setType(Material.PINK_TERRACOTTA);
          } else {
            skill_2c.setType(Material.RED_TERRACOTTA);
          }
        } else {
          skill_2c.setType(Material.GREEN_TERRACOTTA);
        }

        if (skill_3a_level == 0) {
          if (skill_3b_level == 1) {
            skill_3a.setType(Material.PINK_TERRACOTTA);
          } else {
            skill_3a.setType(Material.RED_TERRACOTTA);
          }
        } else {
          skill_3a.setType(Material.GREEN_TERRACOTTA);
        }

        if (skill_3c_level == 0) {
          if (skill_3b_level == 1) {
            skill_3c.setType(Material.PINK_TERRACOTTA);
          } else {
            skill_3c.setType(Material.RED_TERRACOTTA);
          }
        } else {
          skill_3c.setType(Material.GREEN_TERRACOTTA);
        }

        if (skill_3b_level == 0) {
          if (skill_2a_level == 1 && skill_2b_level == 1 && skill_2c_level == 1) {
            skill_3b.setType(Material.PINK_TERRACOTTA);
          } else {
            skill_3b.setType(Material.RED_TERRACOTTA);
          }
        } else {
          skill_3b.setType(Material.GREEN_TERRACOTTA);
        }

        if (skill_M_level == 0) {
          if (skill_3a_level == 1 && skill_3b_level == 1 && skill_3c_level == 1) {
            skill_M.setType(Material.PINK_TERRACOTTA);
          } else {
            skill_M.setType(Material.RED_TERRACOTTA);
          }
        } else {
          skill_M.setType(Material.GREEN_TERRACOTTA);
        }

        ItemStack[] menu_items = {skill_1a, skill_1b, skill_1c, skill_2a, skill_2b, skill_2c,
            skill_3a, skill_3b, skill_3c, skill_M};
        String[] labels = perksMap.get(skillName);
        String[] lores_line2 = descriptionsMap.get(skillName);
        String[] lores_line1 = {"1", "2", "3", "4", "5", "6", "7", "8", "9",
            "10"}; //Data lines 7-13
        for (int i = 0; i < labels.length; i++) {
          String level = pStats.get(2 + i).toString();
          lores_line1[i] =
              ChatColor.GRAY + lang.getString("level") + " " + ChatColor.BLUE + level + "/1";
          if (i == 9) {
            lores_line1[i] =
                ChatColor.GRAY + lang.getString("level") + " " + ChatColor.DARK_PURPLE + level
                    + "/1";
          }
        }

        Integer[] indices = {1, 19, 37, 3, 21, 39, 6, 24, 42, 26};
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
        ItemStack g_token = new ItemStack(Material.DIAMOND);
        ItemStack back_button = new ItemStack(Material.ARROW);
        ItemStack[] menu_items_2 = {g_token, back_button};
        String[] labels_2 = passivePerksMap.get(skillName);
        String[] lores_line1_2 = {"Total: 0", ""};
        String[] lores_line2_2 = passiveDescriptionsMap.get(skillName);

        //Total global Tokens
        lores_line1_2[0] =
            ChatColor.BLUE + lang.getString("total") + ": " + ChatColor.GOLD + String.valueOf(
                tokens_G);
        if (tokens_G > 1 && tokens_G < 64) {
          g_token.setAmount(tokens_G);
        } else if (tokens_G >= 64) {
          g_token.setAmount(64);
        }
        Integer[] indices_2 = {0, 45};
        for (int i = 0; i < labels_2.length; i++) {
          ItemMeta meta = menu_items_2[i].getItemMeta();
          meta.setDisplayName(ChatColor.BOLD + labels_2[i]);
          ArrayList<String> lore = new ArrayList<>();
          lore.add(lores_line1_2[i]);
          String longString = lores_line2_2[i];
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
        Integer[] indices_3 = {2, 4, 7, 8, 13, 15, 17, 20, 22, 23, 25, 31, 33, 35, 38, 40, 43, 44};
        for (int i = 0; i < indices_3.length; i++) {
          gui.setItem(indices_3[i], connector);
        }
        //Put the items in the inventory
        p.openInventory(gui);


      } else {
        System.out.println("You need to be a player to cast this command");
      }
    } else {
      if (sender instanceof Player) {
        Player p = (Player) sender;
        LanguageSelector lang = new LanguageSelector(p);
        p.sendMessage(ChatColor.RED + lang.getString("unknownCommand"));
      } else {
        sender.sendMessage("Unknown command");
      }
    }

    return true;
  }
}
