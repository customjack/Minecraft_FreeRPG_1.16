package mc.carlton.freerpg.gameTools;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import mc.carlton.freerpg.FreeRPG;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class BossBarStorage {

  static Map<Player, BossBar> playerExpBarMap = new ConcurrentHashMap<>();
  static Map<Player, BossBar> playerAbilityDurationMap_Slot1 = new ConcurrentHashMap<>();
  static Map<Player, BossBar> playerAbilityDurationMap_Slot2 = new ConcurrentHashMap<>();
  static Map<Player, BossBar> playerAbilityDurationMap_Slot3 = new ConcurrentHashMap<>();

  public void initializeNewPlayer(Player p) {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    BossBar expBar = Bukkit.createBossBar(new NamespacedKey(plugin, p.getUniqueId().toString()),
        p.getDisplayName() + "'s EXP Bar", BarColor.GREEN, BarStyle.SOLID);
    expBar.setVisible(false);
    expBar.addPlayer(p);
    BossBar abilityDurationBar1 = Bukkit.createBossBar(
        new NamespacedKey(plugin, p.getUniqueId().toString()),
        p.getDisplayName() + "'s Ability Duration Bar 1", BarColor.YELLOW, BarStyle.SOLID);
    abilityDurationBar1.setVisible(false);
    abilityDurationBar1.addPlayer(p);
    BossBar abilityDurationBar2 = Bukkit.createBossBar(
        new NamespacedKey(plugin, p.getUniqueId().toString()),
        p.getDisplayName() + "'s Ability Duration Bar 1", BarColor.YELLOW, BarStyle.SOLID);
    abilityDurationBar2.setVisible(false);
    abilityDurationBar2.addPlayer(p);
    BossBar abilityDurationBar3 = Bukkit.createBossBar(
        new NamespacedKey(plugin, p.getUniqueId().toString()),
        p.getDisplayName() + "'s Ability Duration Bar 1", BarColor.YELLOW, BarStyle.SOLID);
    abilityDurationBar3.setVisible(false);
    abilityDurationBar3.addPlayer(p);
    playerExpBarMap.putIfAbsent(p, expBar);
    playerAbilityDurationMap_Slot1.putIfAbsent(p, abilityDurationBar1);
    playerAbilityDurationMap_Slot2.putIfAbsent(p, abilityDurationBar2);
    playerAbilityDurationMap_Slot3.putIfAbsent(p, abilityDurationBar3);
  }

  public BossBar getPlayerExpBar(Player p) {
    if (!playerExpBarMap.containsKey(p)) {
      initializeNewPlayer(p);
    }
    return playerExpBarMap.get(p);
  }

  public BossBar getPlayerDurationBar1(Player p) {
    if (!playerAbilityDurationMap_Slot1.containsKey(p)) {
      initializeNewPlayer(p);
    }
    return playerAbilityDurationMap_Slot1.get(p);
  }

  public BossBar getPlayerDurationBar2(Player p) {
    if (!playerAbilityDurationMap_Slot2.containsKey(p)) {
      initializeNewPlayer(p);
    }
    return playerAbilityDurationMap_Slot2.get(p);
  }

  public BossBar getPlayerDurationBar3(Player p) {
    if (!playerAbilityDurationMap_Slot3.containsKey(p)) {
      initializeNewPlayer(p);
    }
    return playerAbilityDurationMap_Slot3.get(p);
  }

  public void removePlayer(Player p) {
    if (playerExpBarMap.containsKey(p)) {
      playerExpBarMap.get(p).removePlayer(p);
      playerExpBarMap.remove(p);
    }
    if (playerAbilityDurationMap_Slot1.containsKey(p)) {
      playerAbilityDurationMap_Slot1.get(p).removePlayer(p);
      playerAbilityDurationMap_Slot1.remove(p);
    }
    if (playerAbilityDurationMap_Slot2.containsKey(p)) {
      playerAbilityDurationMap_Slot2.get(p).removePlayer(p);
      playerAbilityDurationMap_Slot2.remove(p);
    }
    if (playerAbilityDurationMap_Slot3.containsKey(p)) {
      playerAbilityDurationMap_Slot3.get(p).removePlayer(p);
      playerAbilityDurationMap_Slot3.remove(p);
    }
  }

  public boolean isDurationBarActive(Player p, int number) {
    BossBar bossBar;
    if (number == 1) {
      bossBar = getPlayerDurationBar1(p);
    } else if (number == 2) {
      bossBar = getPlayerDurationBar2(p);
    } else {
      bossBar = getPlayerDurationBar3(p);
    }
    if (bossBar.isVisible()) {
      return true;
    } else {
      return false;
    }
  }

  public int numberOfActiveDurationBars(Player p) {
    int numberOfBarsActive = 0;
    for (int i = 1; i < 4; i++) {
      if (isDurationBarActive(p, i)) {
        numberOfBarsActive += 1;
      }
    }
    return numberOfBarsActive;
  }

  public BossBar getLowestUnoccupiedBar(Player p) {
    if (!isDurationBarActive(p, 1)) {
      return getPlayerDurationBar1(p);
    } else if (!isDurationBarActive(p, 2)) {
      return getPlayerDurationBar2(p);
    } else if (!isDurationBarActive(p, 3)) {
      return getPlayerDurationBar3(p);
    }
    return null;

  }


}
