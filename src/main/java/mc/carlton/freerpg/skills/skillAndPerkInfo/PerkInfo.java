package mc.carlton.freerpg.skills.skillAndPerkInfo;

public class PerkInfo {

  private boolean enabled = true;
  private int minLevel = -1;
  private int maxLevel = -1;
  private String perkId;
  private String descriptionTemplateId;

  public int getMaxLevel() {
    return maxLevel;
  }

  public void setMaxLevel(int maxLevel) {
    this.maxLevel = maxLevel;
  }

  public int getMinLevel() {
    return minLevel;
  }

  public void setMinLevel(int minLevel) {
    this.minLevel = minLevel;
  }

  public String getDescriptionTemplateId() {
    return descriptionTemplateId;
  }

  public void setDescriptionTemplateId(String descriptionTemplateId) {
    this.descriptionTemplateId = descriptionTemplateId;
  }

  public String getPerkId() {
    return perkId;
  }

  public void setPerkId(String perkId) {
    this.perkId = perkId;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}
