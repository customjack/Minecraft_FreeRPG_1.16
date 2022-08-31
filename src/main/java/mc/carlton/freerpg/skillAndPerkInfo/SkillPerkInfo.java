package mc.carlton.freerpg.skillAndPerkInfo;

import java.util.HashMap;
import java.util.Map;

public class SkillPerkInfo extends PerkInfo {

  private Map<Integer, SkillPerkLevelInfo> skillPerkInfo = new HashMap<>();

  public SkillPerkInfo(Map<Integer, SkillPerkLevelInfo> skillPerkInfo) {
    this.skillPerkInfo = skillPerkInfo;
  }

  public SkillPerkInfo() {
    this(new HashMap<>());
  }

  public Map<Integer, SkillPerkLevelInfo> getSkillPerkInfo() {
    return skillPerkInfo;
  }

  public void setSkillPerkInfo(Map<Integer, SkillPerkLevelInfo> skillPerkInfo) {
    this.skillPerkInfo = skillPerkInfo;
  }

  public void addSkillPerkInfo(int level, SkillPerkLevelInfo skillPerkLevelInfo) {
    this.skillPerkInfo.put(level, skillPerkLevelInfo);
  }

  public void addSkillPerkInfo(int level, Map<String, Object> skillPerkLevelInfo) {
    this.skillPerkInfo.put(level, new SkillPerkLevelInfo(level, skillPerkLevelInfo));
  }
}
