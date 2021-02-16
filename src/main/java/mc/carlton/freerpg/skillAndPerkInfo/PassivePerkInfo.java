package mc.carlton.freerpg.skillAndPerkInfo;

import java.util.HashMap;
import java.util.Map;

public class PassivePerkInfo extends PerkInfo {
    private Map<String, Object> passivePerkInfo = new HashMap<>();

    public PassivePerkInfo(Map<String, Object> passivePerkInfo) {
        this.passivePerkInfo =passivePerkInfo;
    }

    public PassivePerkInfo() {
        this(new HashMap<>());
    }

    public Map<String, Object> getPassivePerkInfo() {
        return passivePerkInfo;
    }

    public void setPassivePerkInfo(Map<String, Object> passivePerkInfo) {
        this.passivePerkInfo = passivePerkInfo;
    }

    public void addPassivePerkInfo(String identifier, Object information) {
        passivePerkInfo.put(identifier,information);
    }
}
