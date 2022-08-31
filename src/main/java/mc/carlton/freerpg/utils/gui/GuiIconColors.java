package mc.carlton.freerpg.utils.gui;

import org.bukkit.ChatColor;

public class GuiIconColors {

  private String nameColor;
  private String descriptionColor;
  private String statisticNamesColor;
  private String statisticsColor;

  /**
   * Constructor for ThemeColors
   *
   * @param nameColor          color of gui icon names
   * @param descriptionColor   color of gui icon descriptions
   * @param statisticNameColor color of statistic names on the gui icon
   * @param statisticColor     color of statistics on the gui icon
   */
  public GuiIconColors(String nameColor, String descriptionColor, String statisticNameColor,
      String statisticColor) {
    this.nameColor = nameColor;
    this.descriptionColor = descriptionColor;
    this.statisticNamesColor = statisticNameColor;
    this.statisticsColor = statisticColor;
  }

  /**
   * Default constructor for ThemeColors Sets: nameColor <- Bold and white descriptionColor <- Gray
   * and italic statisticNameColor <- Gray StatisticColor <- Gold
   */
  public GuiIconColors() {
    this(ChatColor.WHITE.toString() + ChatColor.BOLD.toString(),
        ChatColor.GRAY.toString() + ChatColor.ITALIC.toString(), ChatColor.GRAY.toString(),
        ChatColor.GOLD.toString());
  }

  public String getDescriptionColor() {
    return descriptionColor;
  }

  public void setDescriptionColor(String descriptionColor) {
    this.descriptionColor = descriptionColor;
  }

  public void setDescriptionColor(ChatColor descriptionColor) {
    this.descriptionColor = descriptionColor.toString();
  }

  public String getNameColor() {
    return nameColor;
  }

  public void setNameColor(String nameColor) {
    this.nameColor = nameColor;
  }

  public void setNameColor(ChatColor nameColor) {
    this.nameColor = nameColor.toString();
  }

  public String getStatisticsColor() {
    return statisticsColor;
  }

  public void setStatisticsColor(String statisticsColor) {
    this.statisticsColor = statisticsColor;
  }

  public String getStatisticNamesColor() {
    return statisticNamesColor;
  }

  public void setStatisticNamesColor(String statisticNamesColor) {
    this.statisticNamesColor = statisticNamesColor;
  }

  public void setStatisticColor(ChatColor statisticColor) {
    this.statisticsColor = statisticColor.toString();
  }

  public void setStatisticNameColor(ChatColor statisticNameColor) {
    this.statisticNamesColor = statisticNameColor.toString();
  }
}
