package mc.carlton.freerpg.skills.skillAndPerkInfo;

import java.util.HashMap;
import java.util.Map;

public class SkillPerkLevelInfo implements LowestLevelInfo {

  private Map<String, Object> skillPerkLevelInfo = new HashMap<>();
  private int level = 0;

  public SkillPerkLevelInfo(int level, Map<String, Object> skillPerkLevelInfo) {
    this.level = level;
    this.skillPerkLevelInfo = skillPerkLevelInfo;
  }

  public SkillPerkLevelInfo(int level) {
    this(level, new HashMap<>());
  }


  public Map<String, Object> getAllInfo() {
    return skillPerkLevelInfo;
  }

  public Object getInfo(String identifier) {
    if (skillPerkLevelInfo.containsKey(identifier)) {
      return skillPerkLevelInfo;
    }
    return null;
  }

  public void setInfo(Map<String, Object> skillPerkLevelInfo) {
    this.skillPerkLevelInfo = skillPerkLevelInfo;
  }

  public void addInfo(String identifier, Object information) {
    skillPerkLevelInfo.put(identifier, information);
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }
}
