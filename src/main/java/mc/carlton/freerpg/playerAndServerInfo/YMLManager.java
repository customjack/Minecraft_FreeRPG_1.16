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
import java.util.ArrayList;

public class YMLManager {

    public void updateCheckYML(String fileName) {
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        File f0 = new File(plugin.getDataFolder(),"config.yml");
        f0.setReadable(true,false);
        f0.setWritable(true,false);
        FileConfiguration config = YamlConfiguration.loadConfiguration(f0);
        if (fileName.equalsIgnoreCase("config.yml")) {
            if (config.contains("general.autoUpdateConfig")) {
                if (!config.getBoolean("general.autoUpdateConfig")) {
                    return;
                }
            }
        }
        else if(fileName.equalsIgnoreCase("languages.yml")) {
            if (config.contains("general.autoUpdateLanguages")) {
                if (!config.getBoolean("general.autoUpdateLanguages")) {
                    return;
                }
            }
        }
        File f = new File(plugin.getDataFolder(),fileName);
        f.setReadable(true,false);
        f.setWritable(true,false);
        FileConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f);
        File f1 = inputStreamToFile(plugin.getResource(fileName),"TEMP_"+fileName);
        f1.setReadable(true,false);
        f1.setWritable(true,false);
        FileConfiguration yamlConfigurationTrue = YamlConfiguration.loadConfiguration(f1);
        if (!yamlConfiguration.getKeys(true).equals(yamlConfigurationTrue.getKeys(true))) {
            updateYML(fileName);
        }
        f1.delete();
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
    public ArrayList<String> getAllLastLevelKeys(FileConfiguration configuration) {
        ArrayList<String> lastLevelKeys = new ArrayList<>();
        for (String key : configuration.getKeys(true)) {
            if (configuration.getConfigurationSection(key) == null) {
                lastLevelKeys.add(key);
            }
        }
        return lastLevelKeys;
    }

    public void updateYML(String fileName) {
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        System.out.println("[FreeRPG] "+fileName+" keys mismatch the current version's keys. The file may be updated... ");
        storeOldFile(fileName);
        System.out.println("[FreeRPG] Old "+fileName+" stored in /.../FreeRPG/OutdatedYMLFiles");

        plugin.saveResource(fileName,true); //Saves default file (with comments)

        //Loads the new files
        File f = new File(plugin.getDataFolder(),fileName);
        f.setReadable(true,false);
        f.setWritable(true,false);
        File outdatedYAML = new File(plugin.getDataFolder(),File.separator + "OutdatedYMLFiles");
        File f2 = new File(outdatedYAML,"OUTDATED_"+fileName);
        f2.setReadable(true,false);
        f2.setWritable(true,false);
        FileConfiguration oldYAML = YamlConfiguration.loadConfiguration(f2);
        File f3 = new File(plugin.getDataFolder(),"TEMP_"+fileName);
        f3.setReadable(true,false);
        f3.setWritable(true,false);
        FileConfiguration newYAML = YamlConfiguration.loadConfiguration(f3);
        boolean changeMade = false;
        ArrayList<String> lastLevelKeys = getAllLastLevelKeys(newYAML);
        for (String key : lastLevelKeys) {
            if (oldYAML.contains(key) && newYAML.contains(key)) {
                if (!oldYAML.get(key).equals(newYAML.get(key))) {
                    newYAML.set(key, oldYAML.get(key)); //Sets the new config to whatever data was in the old config
                    changeMade = true;
                }
            }
        }


        int keyAdded = 0; //Keys might have been added
        if (fileName.equalsIgnoreCase("languages.yml")) {
            for (String key : oldYAML.getConfigurationSection("lang").getKeys(false)) { //Checks for new languages
                if (!newYAML.contains("lang."+key)) {
                    if (keyAdded == 0) {
                        keyAdded = -1; //Keys probably haven't been added
                    }
                    changeMade = true;
                    newYAML.createSection("lang." + key); //Creates new language section
                    for (String lastLevelKey : newYAML.getConfigurationSection("lang.enUs").getKeys(false)) {
                        String customKey = "lang." + key + "." + lastLevelKey;
                        String enUsKey = "lang.enUs." + lastLevelKey;
                        if (oldYAML.contains(customKey)) {
                            newYAML.set(customKey,oldYAML.get(customKey));
                        }
                        else {
                            newYAML.set(customKey,newYAML.get(enUsKey));
                            keyAdded = 1; //Keys were definetly added
                        }
                    }
                }
            }
        }

        if (changeMade) {
            try {
                newYAML.save(f); //Changes file (comments are lost)
                if (keyAdded == 1) {
                    System.out.println("[FreeRPG] " + fileName + " updated to include new keys.");
                }
                else if (keyAdded == 0) {
                    System.out.println("[FreeRPG] " + fileName + " updated. New keys may have been added.");
                }
                else {
                    System.out.println("[FreeRPG] No change were made to " + fileName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("[FreeRPG] "+fileName+" updated to default version successfully! (If you previously made changes to " + fileName + " this is an error)");
        }
    }
}
