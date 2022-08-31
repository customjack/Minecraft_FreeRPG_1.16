package mc.carlton.freerpg.guiTools;

public class GuiDisplayStatistic {

  private String statisticName;
  private Object statistic;
  private String statisticNameColor;
  private String statisticColor;

  /**
   * Constructor
   *
   * @param statisticName      name of the statistic
   * @param statistic          value of the statistic
   * @param statisticNameColor color of the statistic name (ex. ChatColor.BOLD +
   *                           ChatColor.WHITE.toString())
   * @param statisticColor     color of the statistic (ex. ChatColor.ITALIC + ChatColor.GRAY.toString())
   */
  public GuiDisplayStatistic(String statisticName, Object statistic, String statisticNameColor,
      String statisticColor) {
    this.statisticName = statisticName;
    this.statistic = statistic;
    this.statisticNameColor = statisticNameColor;
    this.statisticColor = statisticColor;
  }

  /**
   * Constructor
   *
   * @param statisticName name of the statistic
   * @param statistic     value of the statistic
   */
  public GuiDisplayStatistic(String statisticName, Object statistic) {
    this(statisticName, statistic, null, null);
  }

  public String getStatisticNameColor() {
    return statisticNameColor;
  }

  public void setStatisticNameColor(String statisticNameColor) {
    this.statisticNameColor = statisticNameColor;
  }

  public String getStatisticColor() {
    return statisticColor;
  }

  public void setStatisticColor(String statisticColor) {
    this.statisticColor = statisticColor;
  }

  public Object getStatistic() {
    return statistic;
  }

  public void setStatistic(Object statistic) {
    this.statistic = statistic;
  }

  public String getStatisticName() {
    return statisticName;
  }

  public void setStatisticName(String statisticName) {
    this.statisticName = statisticName;
  }
}
