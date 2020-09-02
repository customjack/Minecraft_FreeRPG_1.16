package mc.carlton.freerpg.playerAndServerInfo;

import mc.carlton.freerpg.FreeRPG;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class LanguagesYMLManager {

    public void checkLanguagesYML() {
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        File f = new File(plugin.getDataFolder(),"languages.yml");
        f.setReadable(true,false);
        f.setWritable(true,false);
        FileConfiguration languages = YamlConfiguration.loadConfiguration(f);
        File f1 = inputStreamToFile(plugin.getResource("languages.yml"),"TEMP_languages.yml");
        f1.setReadable(true,false);
        f1.setWritable(true,false);
        FileConfiguration languagesTrue = YamlConfiguration.loadConfiguration(f1);
        if (!languages.getKeys(true).equals(languagesTrue.getKeys(true))) {
            updateLanguagesYML();
        }
        f1.delete();
    }

    public void updateLanguagesYML() {
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        System.out.println("[FreeRPG] languages.yml is not up to date or is missing a key");
        storeOldFile("languages.yml");
        System.out.println("[FreeRPG] Old languages.yml stored in /.../FreeRPG/OutdatedYMLFiles");
        plugin.saveResource("languages.yml",true);
        System.out.println("[FreeRPG] languages.yml updated succesfully");
    }

    public void storeOldFile(String fileName) {
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        File f = new File(plugin.getDataFolder(),fileName);
        f.setReadable(true,false);
        f.setWritable(true,false);
        File oldLanguagesYMLFolder = new File(plugin.getDataFolder(), File.separator + "OutdatedYMLFiles");
        if(!oldLanguagesYMLFolder.exists()){
            oldLanguagesYMLFolder.mkdir();
        }
        File newF = new File(oldLanguagesYMLFolder,"OUTDATED_"+fileName);
        FileUtil.copy(f,newF);
    }

    public File inputStreamToFile(InputStream inputStream,String fileName) {
        try {
            Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
            Path outputPath = Paths.get(plugin.getDataFolder().getPath() + "/" + fileName);
            Files.copy(inputStream, outputPath, StandardCopyOption.REPLACE_EXISTING);
            return new File(plugin.getDataFolder().getPath(),fileName);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
