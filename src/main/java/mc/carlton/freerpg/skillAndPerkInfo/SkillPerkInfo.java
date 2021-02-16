package mc.carlton.freerpg.skillAndPerkInfo;

import java.util.HashMap;
import java.util.Map;

public class SkillPerkInfo extends PerkInfo {
    private Map<Integer,SkillPerkLevel> skillPerkInfo = new HashMap<>();

    public SkillPerkInfo(Map<Integer, SkillPerkLevel> skillPerkInfo) {
        this.skillPerkInfo = skillPerkInfo;
    }

    public SkillPerkInfo() {
        this(new HashMap<>());
    }

    public Map<Integer, SkillPerkLevel> getSkillPerkInfo() {
        return skillPerkInfo;
    }

    public void setSkillPerkInfo(Map<Integer, SkillPerkLevel> skillPerkInfo) {
        this.skillPerkInfo = skillPerkInfo;
    }

    public void addSkillPerkInfo(int level, SkillPerkLevel skillPerkLevel) {
        this.skillPerkInfo.put(level,skillPerkLevel);
    }

    public void addSkillPerkInfo(int level, Map<String, Object> skillPerkLevelInfo) {
        this.skillPerkInfo.put(level,new SkillPerkLevel(level,skillPerkLevelInfo));
    }
}
