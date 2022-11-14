package mc.carlton.freerpg.skills.skillAndPerkInfo;

import java.util.Map;

public interface LowestLevelInfo {

  Map<String, Object> getAllInfo();

  Object getInfo(String identifier);

  void setInfo(Map<String, Object> skillPerkLevelInfo);

  void addInfo(String identifier, Object information);
}
