package mc.carlton.freerpg.serverFileManagement;

import mc.carlton.freerpg.FreeRPG;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.FileUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class ServerDataFolderPreparation {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);

    public void initializeServerDataFolder(){
        File serverData = new File(plugin.getDataFolder(), File.separator + "ServerData");
        if (!serverData.exists()) {
            serverData.mkdir();
        }
        moveFile("blockLocations.dat",serverData);
    }

    public void moveFile(String fName,File serverData) {
        File f = new File(plugin.getDataFolder(), fName);
        if (Arrays.asList(plugin.getDataFolder().listFiles()).contains(f)) {
            File newF = new File(serverData, fName);
            FileUtil.copy(f,newF);
            f.delete();
        }
    }
}
