package mc.carlton.freerpg.skillAndPerkInfo;

import java.util.HashMap;
import java.util.Map;

public class SkillPerkLevel {
    private Map<String, Object> skillPerkLevelInfo = new HashMap<>();
    private int level = 0;

    public SkillPerkLevel(int level, Map<String, Object> skillPerkLevelInfo) {
        this.level = level;
        this.skillPerkLevelInfo =skillPerkLevelInfo;
    }

    public SkillPerkLevel(int level) {
        this(level,new HashMap<>());
    }

    public Map<String, Object> getSkillPerkLevelInfo() {
        return skillPerkLevelInfo;
    }

    public void setSkillPerkLevelInfo(Map<String, Object> skillPerkLevelInfo) {
        this.skillPerkLevelInfo = skillPerkLevelInfo;
    }

    public void addSkillPerkLevelInfo(String identifier, Object information) {
        skillPerkLevelInfo.put(identifier,information);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
