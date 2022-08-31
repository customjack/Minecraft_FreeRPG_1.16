package mc.carlton.freerpg.utils.gui;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/*
This class acts as a Wrapper for an inventory acting as a gui
It adds additional methods to the inventory and reduces lines of code when implementing a GUI
The GuiWrapper object is mutable
 */
public class GuiWrapper {

  private Map<Integer, GuiItem> items = new HashMap<>();
  private Player p;
  private Inventory gui;
  private GuiIconColors theme;

  /**
   * Constructor for gui
   *
   * @param p   Player who will see the gui
   * @param gui The inventory acting as the gui
   */
  public GuiWrapper(Player p, Inventory gui) {
    this.gui = gui;
    this.p = p;
  }

  /**
   * Constrcutor for gui
   *
   * @param p       Player who will see the GUI
   * @param guiName GUI display name
   * @param guiSize number of item slots in GUI
   */
  public GuiWrapper(Player p, String guiName, int guiSize) {
    this(p, Bukkit.createInventory(p, guiSize, guiName));
  }

  /**
   * Adds item to the GUI,
   *
   * @param guiItem a GuiItem to add
   */
  public void addItem(GuiItem guiItem) {
    items.put(guiItem.getIndex(), guiItem);
    setGuiItem(guiItem.getIndex(), guiItem);
  }

  /**
   * Adds items to the GUI,
   *
   * @param guiItems a GuiItem[] array to add
   */
  public void addItems(GuiItem[] guiItems) {
    for (GuiItem guiItem : guiItems) {
      addItem(guiItem);
    }
  }


  /**
   * Getter for gui
   *
   * @return Inventory acting as the gui
   */
  public Inventory getGui() {
    return gui;
  }

  /**
   * Returns a pointer to the GuiItem at a given index
   *
   * @param index positional index of where the gui item is
   * @return a pointer to the guiItem
   */
  public GuiItem getItem(int index) {
    if (items.containsKey(index)) {
      return items.get(index);
    } else {
      return null;
    }
  }

  /**
   * Sets all itemStachs in items to be displayed in the gui
   */
  public void setGui() {
    for (Integer index : items.keySet()) {
      setGuiItem(index, items.get(index));
    }
  }

  /**
   * Sets a GuiItem to be displayed in the gui at a given positonal index
   *
   * @param index   postional index of where the guiItem will be
   * @param guiItem a GuiItem
   */
  public void setGuiItem(int index, GuiItem guiItem) {
    setGuiItemColorsToTheme(guiItem);
    gui.setItem(index, guiItem.getItem());
  }

  /**
   * setter for theme
   *
   * @param theme a String of ChatColors ex (ChatColor.BOLD + ChatColor.WHITE.toString())
   */
  public void setTheme(GuiIconColors theme) {
    this.theme = theme;
  }

  /**
   * Opens the gui for the player
   */
  public void displayGuiForPlayer() {
    p.openInventory(gui);
  }

  private void setGuiItemColorsToTheme(GuiItem guiItem) {
    if (theme == null || guiItem.isThemeExemption()) {
      return;
    }
    if (theme.getNameColor() != null) {
      guiItem.setNameColor(theme.getNameColor());
    }
    if (theme.getDescriptionColor() != null) {
      guiItem.setDescriptionColor(theme.getDescriptionColor());
    }
    if (theme.getStatisticNamesColor() != null) {
      guiItem.setStatisticNameColor(theme.getStatisticNamesColor());
    }
    if (theme.getStatisticsColor() != null) {
      guiItem.setStatisticColor(theme.getStatisticsColor());
    }
    guiItem.refreshLore(true);
  }


}
