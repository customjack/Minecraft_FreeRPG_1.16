package mc.carlton.freerpg.skillAndPerkInfo;

import java.util.HashMap;
import java.util.Map;
import mc.carlton.freerpg.skills.SkillName;

public class SkillTreeInfo {

  SkillName skillName;
  private Map<String, PerkInfo> skillTreeInfo;

  public SkillTreeInfo(SkillName skillName) {
    this.skillName = skillName;
    skillTreeInfo = new HashMap<>();
  }

  public void addPerkInfo(String perkId, PerkInfo perkInfo) {
    skillTreeInfo.put(perkId, perkInfo);
  }

  public void removePerkInfo(String perkId) {
    if (skillTreeInfo.containsKey(perkId)) {
      skillTreeInfo.remove(perkId);
    }
  }

  public PerkInfo getPerkInfo(String perkId) {
    if (skillTreeInfo.containsKey(perkId)) {
      return skillTreeInfo.get(perkId);
    }
    return null;
  }

  public SkillName getSkillName() {
    return skillName;
  }

  public void setSkillName(SkillName skillName) {
    this.skillName = skillName;
  }
}
