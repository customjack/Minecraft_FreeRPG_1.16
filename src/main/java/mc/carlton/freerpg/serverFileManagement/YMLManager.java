package mc.carlton.freerpg.serverFileManagement;

import mc.carlton.freerpg.FreeRPG;
import org.apache.logging.log4j.Level;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.FileUtil;

import java.io.File;
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
        if (fileName.equalsIgnoreCase("config.yml") || fileName.equalsIgnoreCase("advancedConfig.yml") || fileName.equalsIgnoreCase("perkConfig.yml") ) {
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
            if (fileName.equalsIgnoreCase("languages.yml")) {
                updateLanguagesYML(fileName);
            }
            else {
                updateYML(fileName);
            }
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
            FreeRPG.log(Level.ERROR, e.getMessage());
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

    public void updateLanguagesYML(String fileName) {
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        FreeRPG.log(Level.INFO, "[FreeRPG] "+fileName+" keys mismatch the current version's keys. The file may be updated... ");
        storeOldFile(fileName);
        FreeRPG.log(Level.INFO, "[FreeRPG] Old "+fileName+" stored in /.../FreeRPG/OutdatedYMLFiles");

        plugin.saveResource(fileName,true); //Saves default file (with comments)

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
        boolean needToSave = false;
        boolean addedLines = false;
        boolean overWroteData = false;
        for (String key : oldYAML.getConfigurationSection("lang").getKeys(false)) { //Checks all languages in the old file
            if (!newYAML.contains("lang." + key) || key.equalsIgnoreCase("custom")) { //Checks for new languages in the old file
                if (!key.equalsIgnoreCase("custom")) { //If there is a new language, add it to the new file
                    newYAML.createSection("lang." + key); //Creates new language section
                    needToSave = true;
                }
                for (String lastLevelKey : newYAML.getConfigurationSection("lang.enUs").getKeys(false)) { //For every key...
                    String customKey = "lang." + key + "." + lastLevelKey;
                    String enUsKey = "lang.enUs." + lastLevelKey;
                    if (oldYAML.contains(customKey)) {
                        newYAML.set(customKey, oldYAML.get(customKey)); //Add the custom language's key to the new file
                    } else {
                        newYAML.set(customKey, newYAML.get(enUsKey)); //Set the custom language's missing key to the new file
                        addedLines = true;
                        needToSave = true;
                    }
                }
            } else { //Checks other languages to see if they were changed, overwrites any changes
                for (String lastLevelKey : newYAML.getConfigurationSection("lang."+key).getKeys(false)) { //for all language keys
                    String fullKey = "lang." + key + "." + lastLevelKey;
                    if (oldYAML.contains(fullKey)) { //if the old file has this key
                        if (!(oldYAML.get(fullKey).equals(newYAML.get(fullKey))) ) { //and that key is different from the resource file's key
                            overWroteData = true; //This line tells the plugin what to output
                            needToSave = true; //This line tells the plugin it must be saved over
                        }
                    } else { //If oldYAML is completely missing the key
                        overWroteData = true; //This line tells the plugin what to output
                        needToSave = true; //This line tells the plugin it must be saved over
                        addedLines = true; //Keys were added
                    }
                }
            }
        }


        if (needToSave) {
            try {
                newYAML.save(f); //Changes file (comments are lost)
                if (addedLines) {
                    FreeRPG.log(Level.INFO, "[FreeRPG] " + fileName + " updated to include new keys.");
                }
                else {
                    FreeRPG.log(Level.INFO, "[FreeRPG] " + fileName + " no new keys were added.");
                }
                if (overWroteData) {
                    FreeRPG.log(Level.INFO, "[FreeRPG] Overwrote some data in " + fileName +
                            " (You may not edit previously defined languages).");
                }
                else {
                    FreeRPG.log(Level.INFO, "[FreeRPG] No data was overwritten in " + fileName);
                }
            } catch (IOException e) {
                FreeRPG.log(Level.ERROR, e.getMessage());
            }
        }
    }

    public void updateYML(String fileName) {
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        FreeRPG.log(Level.INFO, "[FreeRPG] "+fileName+" keys mismatch the current version's keys. " +
                "The file may be updated... ");
        storeOldFile(fileName);
        FreeRPG.log(Level.INFO, "[FreeRPG] Old "+fileName+" stored in /.../FreeRPG/OutdatedYMLFiles");

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


        if (fileName.equalsIgnoreCase("languages.yml")) {

        }

        if (changeMade) {
            try {
                newYAML.save(f); //Changes file (comments are lost)
                FreeRPG.log(Level.INFO, "[FreeRPG] " + fileName + " updated to include new keys.");
            } catch (IOException e) {
                FreeRPG.log(Level.ERROR, e.getMessage());
            }
        }
        else {
            FreeRPG.log(Level.INFO, "[FreeRPG] "+fileName+" updated to default version successfully! " +
                    "(If you previously made changes to " + fileName + " this is an error)");
        }
    }
}
