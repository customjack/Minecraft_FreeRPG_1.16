package mc.carlton.freerpg.skillAndPerkInfo;

import java.util.HashMap;
import java.util.Map;

public class PassivePerkInfo extends PerkInfo implements LowestLevelInfo {
    private Map<String, Object> passivePerkInfo = new HashMap<>();

    public PassivePerkInfo(Map<String, Object> passivePerkInfo) {
        this.passivePerkInfo =passivePerkInfo;
    }

    public PassivePerkInfo() {
        this(new HashMap<>());
    }

    public Map<String, Object> getAllInfo() {
        return passivePerkInfo;
    }

    public Object getInfo(String identifier) {
        if (passivePerkInfo.containsKey(identifier)) {
            return passivePerkInfo;
        }
        return null;
    }

    public void setInfo(Map<String, Object> skillPerkLevelInfo) {
        this.passivePerkInfo = skillPerkLevelInfo;
    }

    public void addInfo(String identifier, Object information) {
        passivePerkInfo.put(identifier,information);
    }
}
