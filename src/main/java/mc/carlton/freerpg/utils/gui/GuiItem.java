package mc.carlton.freerpg.utils.gui;

import java.util.ArrayList;
import mc.carlton.freerpg.utils.globalVariables.StringsAndOtherData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/*
 * This class acts as a wrapper class for an ItemStack to be used in a gui. Appeands additionally information about the item
 * And uses tools to make text output neater and easier to implement.
 * The GuiItem object is mutable
 */
public class GuiItem {

  private int index;
  private ItemStack item;
  private Inventory gui;
  private String name;
  private String description;
  private GuiIconColors textColors;
  private ArrayList<GuiDisplayStatistic> statitistics;
  private ArrayList<String> specialLoreLines;
  private boolean themeExemption;

  /**
   * Constructor for GuiItem
   *
   * @param item  ItemStack to be placed in the GUI
   * @param index index at which the ItemStack will be placed in the GUI
   * @param gui   Inventory that acts as the GUI
   */
  public GuiItem(ItemStack item, int index, Inventory gui) {
    this(item, index, gui, new GuiIconColors());
  }

  /**
   * Constructor for GuiItem
   *
   * @param itemType Material of ItemStack to be placed in GUI
   * @param index    index at which the ItemStack will be placed in the GUI
   * @param gui      Inventory that acts as the GUI
   */
  public GuiItem(Material itemType, int index, Inventory gui) {
    this(new ItemStack(itemType), index, gui);
  }

  /**
   * Constructor for GuiItem
   *
   * @param itemType Material of ItemStack to be placed in GUI
   * @param index    index at which the ItemStack will be placed in the GUI
   * @param gui      GuiWrapper that acts as the GUI
   */
  public GuiItem(Material itemType, int index, GuiWrapper gui) {
    this(new ItemStack(itemType), index, gui.getGui());
  }

  /**
   * Constructor for GuiItem
   *
   * @param item  ItemStack to be placed in the GUI
   * @param index index at which the itemstack will be placed in the GUI
   * @param gui   GuiWrapper that acts as the GUI
   */
  public GuiItem(ItemStack item, int index, GuiWrapper gui) {
    this(item, index, gui.getGui());
  }


  /**
   * Constructor for GuiItem
   *
   * @param item       ItemStack to be placed in the GUI
   * @param index      index at which the itemstack will be placed in the GUI
   * @param textColors ThemeColor object to determine display colors
   * @param gui        GuiWrapper that acts as the GUI
   */
  public GuiItem(ItemStack item, int index, GuiWrapper gui, GuiIconColors textColors) {
    this(item, index, gui.getGui(), textColors);
  }

  /**
   * Constructor for GuiItem
   *
   * @param item       ItemStack to be placed in the GUI
   * @param index      index at which the itemstack will be placed in the GUI
   * @param textColors ThemeColor object to determine display colors
   * @param gui        Inventory that acts as the GUI
   */
  public GuiItem(ItemStack item, int index, Inventory gui, GuiIconColors textColors) {
    this.item = item;
    hideItemInformation(); //Hides things like enchantments, potion info, and attributes
    this.gui = gui;
    this.textColors = textColors;
    this.statitistics = new ArrayList<>();
    this.specialLoreLines = new ArrayList<>();
    if (!isIndexInsideGUI(index)) {
      throw (new IllegalArgumentException("Index outside of Inventory size"));
    }
    this.index = index;
  }

  /**
   * Setter for item description color, also changes the item's description to match new color
   *
   * @param ColorString string of chat colors (ex. ChatColor.BOLD + ChatColor.WHITE.toString() )
   */
  public void setDescriptionColor(String ColorString) {
    textColors.setDescriptionColor(ColorString);
    setDescription(this.description);
  }

  /**
   * Setter for item description color, also changes the item's description to match new color
   *
   * @param color a chat color (ex. ChatColor.WHITE)
   */
  public void setDescriptionColor(ChatColor color) {
    textColors.setDescriptionColor(color);
    setDescription(this.description);
  }

  /**
   * Setter for item name color, also changes the item's name to match new color
   *
   * @param ColorString string of chat colors (ex. ChatColor.BOLD + ChatColor.WHITE.toString() )
   */
  public void setNameColor(String ColorString) {
    textColors.setNameColor(ColorString);
    setName(this.name);
  }

  /**
   * Setter for item name color, also changes the item's name to match new color
   *
   * @param color a chat color (ex. ChatColor.WHITE)
   */
  public void setNameColor(ChatColor color) {
    textColors.setNameColor(color);
    setName(this.name);
  }

  /**
   * Setter for Statistic name color, changes item's lore to account for color changs
   *
   * @param ColorString
   */
  public void setStatisticNameColor(String ColorString) {
    textColors.setStatisticNamesColor(ColorString);
    setStatitistics(this.statitistics);
  }

  /**
   * Setter for Statistic name color, changes item's lore to account for color changs
   *
   * @param color
   */
  public void setStatisticNameColor(ChatColor color) {
    textColors.setStatisticNameColor(color);
    setStatitistics(this.statitistics);
  }

  /**
   * Setter for Statistic color, changes item's lore to account for color changs
   *
   * @param ColorString
   */
  public void setStatisticColor(String ColorString) {
    textColors.setStatisticsColor(ColorString);
    setStatitistics(this.statitistics);
  }

  /**
   * Setter for Statistic color, changes item's lore to account for color changs
   *
   * @param color
   */
  public void setStatisticColor(ChatColor color) {
    textColors.setStatisticColor(color);
    setStatitistics(this.statitistics);
  }

  /**
   * Sets the name of the item
   *
   * @param name String acting as name
   */
  public void setName(String name) {
    if (name == null || name.isEmpty()) {
      return; //Do nothing for empty name
    }
    this.name = name;
    ItemMeta itemMeta = item.getItemMeta();
    itemMeta.setDisplayName(textColors.getNameColor() + name);
    item.setItemMeta(itemMeta);
  }

  /**
   * Sets the description (description) of the item and updates the item lore
   *
   * @param description String acting as description, will be automatically divided into multiple
   *                    lines if too long.
   */
  public void setDescription(String description) {
    if (description == null || description.isEmpty()) {
      return; //Do nothing for empty description
    }
    this.description = description;
    setLore();
  }

  /**
   * Adds a statistic to the item's lore and updates the lore
   *
   * @param guiDisplayStatistic
   */
  public void addStatistic(GuiDisplayStatistic guiDisplayStatistic) {
    this.statitistics.add(guiDisplayStatistic);
    setLore();
  }

  /**
   * Adds a statistic to the item's lore and updates the lore
   *
   * @param statisticName display name of the statistic
   * @param statistic     value of the statistic to add, can be any object with a toString method.
   */
  public void addStatistic(String statisticName, Object statistic) {
    this.statitistics.add(new GuiDisplayStatistic(statisticName, statistic));
    setLore();
  }

  /**
   * Setter for statistics, adds statistics to item's lore
   *
   * @param statitistics
   */
  public void setStatitistics(ArrayList<GuiDisplayStatistic> statitistics) {
    this.statitistics = statitistics;
    setLore();
  }

  /**
   * Adds a special lore line between statistics and the description, updates the lore
   *
   * @param loreLine
   */
  public void addSpecialLoreLine(String loreLine) {
    this.specialLoreLines.add(loreLine);
    setLore();
  }

  /**
   * Adds special lore lines between statistics and the description, updates the lore
   *
   * @param specialLoreLines
   */
  public void setSpecialLoreLines(ArrayList<String> specialLoreLines) {
    this.specialLoreLines = specialLoreLines;
    setLore();
  }

  /**
   * Changes the Material type of the instance variable item
   *
   * @param itemType Material that item will be changed to
   */
  public void setItemType(Material itemType) {
    item.setType(itemType);
  }

  /**
   * Changes the item amount of the instance variable item
   *
   * @param amount amount of item to be displayed
   */
  public void setItemAmount(int amount) {
    item.setAmount(amount);
  }

  /**
   * Getter for theme exemption
   *
   * @return true if this item is exempt from the GuiWrapper color theme, false otherwise
   */
  public boolean isThemeExemption() {
    return themeExemption;
  }

  /**
   * Setter for themeExemption
   *
   * @param themeExemption set to true to exempt the item from the color theme of GuiWrapper, false
   *                       otherwise
   */
  public void setThemeExemption(boolean themeExemption) {
    this.themeExemption = themeExemption;
  }

  /**
   * Getter for item
   *
   * @return the instance variable item
   */
  public ItemStack getItem() {
    return item;
  }

  /**
   * Setter for item
   *
   * @param item ItemStack to be placed in the gui
   */
  public void setItem(ItemStack item) {
    this.item = item;
  }

  /**
   * Getter for index
   *
   * @return the instance variable index
   */
  public int getIndex() {
    return index;
  }

  /**
   * Setter for index
   *
   * @param index index at which item is placed
   */
  public void setIndex(int index) {
    if (!isIndexInsideGUI(index)) {
      throw (new IllegalArgumentException("Index outside of Inventory size"));
    }
    this.index = index;
  }

  /**
   * Getter for gui
   *
   * @return the instance variable gui
   */
  public Inventory getGui() {
    return gui;
  }

  /**
   * Settter for GUI
   *
   * @param gui Inventory that will act as the gui
   */
  public void setGui(Inventory gui) {
    this.gui = gui;
  }

  /**
   * Adds enchantment glow to the item
   */
  public void addEnchantmentGlow() {
    item.addUnsafeEnchantment(Enchantment.LOYALTY, 1);
  }

  /**
   * Removes an item flag from the GuiItem
   *
   * @param itemFlag item flag to remove (ex. ItemFlag.HIDE_ENCHANTS)
   */
  public void showItemFlag(ItemFlag itemFlag) {
    ItemMeta itemMeta = item.getItemMeta();
    if (itemMeta.hasItemFlag(itemFlag)) {
      itemMeta.removeItemFlags(itemFlag);
    }
  }

  private boolean isIndexInsideGUI(int index) {
    if (index < gui.getSize()) {
      return true;
    } else {
      return false;
    }
  }

  private void hideItemInformation() {
    ItemMeta itemMeta = item.getItemMeta();
    itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    item.setItemMeta(itemMeta);
  }

  private void setLore() {
    ItemMeta itemMeta = item.getItemMeta();
    ArrayList<String> loreLines = new ArrayList<>();
    StringsAndOtherData stringsAndOtherData = new StringsAndOtherData();
    ArrayList<String> splitDescriptionLines = stringsAndOtherData.getStringLines(description);
    for (GuiDisplayStatistic guiDisplayStatistic : statitistics) {
      loreLines.add(getLoreLineStringFromStatistic(guiDisplayStatistic, false));
    }
    for (String specialLoreLine : specialLoreLines) {
      loreLines.add(specialLoreLine);
    }
    for (String descriptionLine : splitDescriptionLines) {
      loreLines.add(textColors.getDescriptionColor() + descriptionLine);
    }
    itemMeta.setLore(loreLines);
    item.setItemMeta(itemMeta);
  }

  public void refreshLore(boolean forceTheme) {
    ItemMeta itemMeta = item.getItemMeta();
    ArrayList<String> loreLines = new ArrayList<>();
    StringsAndOtherData stringsAndOtherData = new StringsAndOtherData();
    ArrayList<String> splitDescriptionLines = stringsAndOtherData.getStringLines(description);
    for (GuiDisplayStatistic guiDisplayStatistic : statitistics) {
      loreLines.add(getLoreLineStringFromStatistic(guiDisplayStatistic, forceTheme));
    }
    for (String specialLoreLine : specialLoreLines) {
      loreLines.add(specialLoreLine);
    }
    for (String descriptionLine : splitDescriptionLines) {
      loreLines.add(textColors.getDescriptionColor() + descriptionLine);
    }
    itemMeta.setLore(loreLines);
    item.setItemMeta(itemMeta);
  }

  private String getLoreLineStringFromStatistic(GuiDisplayStatistic guiDisplayStatistic,
      boolean forceTheme) {
    String statisticName = guiDisplayStatistic.getStatisticName();
    String statistic = guiDisplayStatistic.getStatistic().toString();
    String loreLine = "";
    if (guiDisplayStatistic.getStatisticNameColor() == null || forceTheme) {
      loreLine += textColors.getStatisticNamesColor() + statisticName + ": ";
    } else {
      loreLine += guiDisplayStatistic.getStatisticNameColor() + statisticName + ": ";
    }
    if (guiDisplayStatistic.getStatisticColor() == null || forceTheme) {
      loreLine += ChatColor.RESET + textColors.getStatisticsColor() + statistic;
    } else {
      loreLine += ChatColor.RESET + guiDisplayStatistic.getStatisticColor() + statistic;
    }
    return loreLine;
  }
}
