package mc.carlton.freerpg.core.serverFileManagement;

import java.io.File;
import java.util.Arrays;
import mc.carlton.freerpg.FreeRPG;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.FileUtil;

public class ServerDataFolderPreparation {

  Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);

  public void initializeServerDataFolder() {
    File serverData = new File(plugin.getDataFolder(), File.separator + "ServerData");
    if (!serverData.exists()) {
      serverData.mkdir();
    }
    moveFile("blockLocations.dat", serverData);
  }

  public void moveFile(String fName, File serverData) {
    File f = new File(plugin.getDataFolder(), fName);
    if (Arrays.asList(plugin.getDataFolder().listFiles()).contains(f)) {
      File newF = new File(serverData, fName);
      FileUtil.copy(f, newF);
      f.delete();
    }
  }
}
