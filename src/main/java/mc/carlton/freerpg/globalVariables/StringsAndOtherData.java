package mc.carlton.freerpg.globalVariables;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.CustomPotion;
import mc.carlton.freerpg.gameTools.CustomRecipe;
import mc.carlton.freerpg.gameTools.LanguageSelector;
import mc.carlton.freerpg.serverInfo.ConfigLoad;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.io.File;
import java.util.*;

public class StringsAndOtherData {
    static ArrayList<String> skillNames = new ArrayList<>();
    static ArrayList<String> skillNamesWithGlobal = new ArrayList<>();
    static Map<String, String[]> perksMap = new HashMap<>();
    static Map<String, String[]> descriptionsMap = new HashMap<>();
    static Map<String, String[]> passivePerksMap = new HashMap<>();
    static Map<String, String[]> passiveDescriptionsMap = new HashMap<>();
    static public String version;
    static Map<String,String> idToStringMap = new HashMap<>();
    static Map<String,Double> languageProgress = new HashMap<>();
    static ArrayList<String> languageCodes = new ArrayList<>();
    static Map<Integer,String> bookIndexes = new HashMap<>();
    static Map<Integer,String> dyeIndexes = new HashMap<>();

    ArrayList<Double> levelingInfo;

    public StringsAndOtherData(){
        ConfigLoad configLoad = new ConfigLoad();
        levelingInfo = configLoad.getLevelingInfo();
    }

    public void initializeData() {
        initializeSkillNames();
        initializeSkillDescriptions();
        initializeVersion();
        initializeLanguagesData();
        initializeConfigGUIIndexInfo();
        initializeLanguageCompletions();
    }

    public void initializeSkillNames() {
        String[] labels_0 = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting","global"};
        List<String> labels_arr = Arrays.asList(labels_0);
        skillNamesWithGlobal = new ArrayList<>(labels_arr);
        String[] labels_1 = {"digging","woodcutting","mining","farming","fishing","archery","beastMastery","swordsmanship","defense","axeMastery","repair","agility","alchemy","smelting","enchanting"};
        List<String> labels_arr1 = Arrays.asList(labels_1);
        skillNames = new ArrayList<>(labels_arr1);
    }


    public void initializeLanguagesData() {
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        File languagesYML = new File(plugin.getDataFolder()+"/languages.yml");
        languagesYML.setReadable(true,false);
        languagesYML.setWritable(true,false);
        FileConfiguration languages = YamlConfiguration.loadConfiguration(languagesYML);
        for (String language : languages.getConfigurationSection("lang").getKeys(false)) {
            languageCodes.add(language);
            for (String id : languages.getConfigurationSection("lang." + language).getKeys(false)) {
                String fullId = language +"." + id;
                idToStringMap.put(fullId,languages.getString("lang." + language + "." + id));
            }
        }
        if (languageCodes.size() > 14) {
            System.out.println("[FreeRPG] WARNING: Player configuration currently only supports the first 12 language options!");
        }
    }

    public void initializeConfigGUIIndexInfo() {
        int totalIndexes = Math.min(languageCodes.size(),12);
        int[] possibleBookIndexes = {19,20,21,22,23,24,25,37,38,49,40,41,42,43};
        int[] possibleDyeIndexes = {28,29,30,31,32,33,34,46,47,48,49,50,51,52};
        for (int i = 0; i < totalIndexes; i++) {
            bookIndexes.put(possibleBookIndexes[i],languageCodes.get(i));
            dyeIndexes.put(possibleDyeIndexes[i],languageCodes.get(i));
        }
    }

    public void initializeLanguageCompletions() {
        if (!languageCodes.contains("enUs")) {
            System.out.println("[FreeRPG] Languages.yml is missing enUs! Some features may be broken");
            for (String language : languageCodes) {
                languageProgress.put(language,1.0);
            }
            return;
        }
        languageProgress.put("enUs",1.0);
        Map<String, Integer> totalLanguageString = new HashMap<>();
        Map<String, Integer> totalIncompleteLanguageString = new HashMap<>();
        for (String language : languageCodes) {
            totalLanguageString.put(language,0);
            totalIncompleteLanguageString.put(language,0);
        }
        for (String partialYamlKey : idToStringMap.keySet()) {
            languagesLoop:
            for (String language : languageCodes) {
                if (language.equalsIgnoreCase("enUs")) {
                    continue;
                }
                if (partialYamlKey.contains(language)) {
                    int indexOfDot = partialYamlKey.indexOf(".");
                    String englishKey = "enUs" + partialYamlKey.substring(indexOfDot);
                    totalLanguageString.put(language,totalLanguageString.get(language)+1);
                    if (idToStringMap.get(partialYamlKey).equalsIgnoreCase(idToStringMap.get(englishKey))) {
                        totalIncompleteLanguageString.put(language,totalIncompleteLanguageString.get(language)+1);
                    }
                    break languagesLoop;
                }
            }
        }
        for (String language : languageCodes) {
            if (language.equalsIgnoreCase("enUs")) {
                continue;
            }
            double totalStrings = totalLanguageString.get(language);
            double totalIncomplete = totalIncompleteLanguageString.get(language);
            languageProgress.put(language,(totalStrings - totalIncomplete)/totalStrings);
        }
    }

    public void initializeSkillDescriptions() {
        String s_Name = "";
        String[] perks = {};
        String[] descriptions = {};
        String[] passivePerks = {};
        String[] passiveDescriptions = {};

        //digging
        s_Name = "digging";
        perks = new String[]{"Mo' drops", "Double Treasure", "Rarer Drops", "Soul Stealer", "Flint Finder", "Shovel Knight", "Mega Dig"};
        descriptions = new String[]{"Expands treasure drop table by 1 item per level","+5% chance of receiving double treasure drop per level (when treasure is rolled)","Further expands drop table by item per level","Soul sand is +5% more likely to drop treasure per level","Gravel has 100% flint drop rate (toggleable  by /flintToggle)","Shovels do double damage","When using ability, you now break a 3x3 block section (20% of normal treasure rate when active)"};
        passivePerks = new String[]{"Passive Tokens","Back","Skill Tokens","Big Dig Duration","Treasure Chance"};
        passiveDescriptions = new String[]{"Tokens to invest in passive skills (dyes)","Takes you back to the main skills menu","Tokens to invest in skill tree","Increases duration of Big Dig by 0.02 s","Increases chance of digging up treasure by 0.005%"};
        perksMap.put(s_Name,perks);
        descriptionsMap.put(s_Name,descriptions);
        passivePerksMap.put(s_Name,passivePerks);
        passiveDescriptionsMap.put(s_Name,passiveDescriptions);

        //woodcutting
        s_Name = "woodcutting";
        perks = new String[]{"Zealous Roots", "Fresh Arms", "Hidden Knowledge", "Leaf Scavenger", "Timber+", "Leaf Blower", "Able Axe"};
        descriptions = new String[]{"+20% chance for logs to drop 1 XP per level","+12 s per level of Haste I after first log broken in 5 minutes","Logs have a +0.2% chance per level to drop an enchanted book","Leaves have a 1% chance to drop +1 treasure item per level","Timber break limit increased from 64 to 128","Instantly break leaves by holding left click with an axe","Double drops, Zealous Roots, and Hidden Knowledge all now apply to timber (at half effectiveness)"};
        passivePerks = new String[]{"Passive Tokens","Back","Skill Tokens","Timber Duration","Double Drops"};
        passiveDescriptions = new String[]{"Tokens to invest in passive skills (dyes)","Takes you back to the main skills menu","Tokens to invest in skill tree","Increases duration of Timber by 0.02 s","Increases chance to receive a double drop by 0.05%"};
        perksMap.put(s_Name,perks);
        descriptionsMap.put(s_Name,descriptions);
        passivePerksMap.put(s_Name,passivePerks);
        passiveDescriptionsMap.put(s_Name,passiveDescriptions);

        //mining
        s_Name = "mining";
        perks = new String[]{"Wasteless Haste", "More Bombs", "Treasure Seeker", "Bomb-boyage", "Vein Miner", "Demolition Man", "Triple Trouble"};
        descriptions = new String[]{"Gain haste after mining ores for each level","The crafting recipe for TNT produces +1 TNT block per level","When using ability on stones, +1% per level chance for an ore to drop (extra exp is earned from dropped ores)","Increases TNT blast radius (when lit by flint and steel) each level","Ore veins are instantly mined upon breaking one block (toggle-able)","No damage is taken from TNT explosions","Double drops are now triple drops"};
        passivePerks = new String[]{"Passive Tokens","Back","Skill Tokens","Berserk Pick Duration","Double Drops","Blast Mining"};
        passiveDescriptions = new String[]{"Tokens to invest in passive skills (dyes)","Takes you back to the main skills menu","Tokens to invest in skill tree","Increases duration of Berserk Pick by 0.02s","Increases chance to receive a double drop from ores by 0.05%","Increases chances for ore to be created from TNT explosions by 0.01% (rolled 10 times per explosion)"};
        perksMap.put(s_Name,perks);
        descriptionsMap.put(s_Name,descriptions);
        passivePerksMap.put(s_Name,passivePerks);
        passiveDescriptionsMap.put(s_Name,passiveDescriptions);

        //farming
        s_Name = "farming";
        perks = new String[]{"Better Fertilizer", "Animal Farm", "Farmer's Diet", "Carnivore", "Green Thumb", "Growth Hormones", "One with Nature"};
        descriptions = new String[]{"+10% chance to not consume bonemeal on use","Can craft an additional spawn egg per level","Farm food is +20% more effective at restoring hunger and saturation per level","Meat is +20% more effective at restoring hunger and saturation per level","Ability may replant crops fully grown with higher chance of replanting in later growth stages; ability now effects Melons and Pumpkins","Sugar can be used on baby animals to make them grow instantly","Gain Regeneration I when standing still on grass"};
        passivePerks = new String[]{"Passive Tokens","Back","Skill Tokens","Natural Regeneration Duration","Double Drops (Crops)","Double Drops (Animals)"};
        passiveDescriptions = new String[]{"Tokens to invest in passive skills (dyes)","Takes you back to the main skills menu","Tokens to invest in skill tree","Increases duration of Natural Regeneration by 0.02s","Increases chance to receive a double drop from crops by 0.05%","Increases chance to receive a double drop from most passive animals by 0.05%"};
        perksMap.put(s_Name,perks);
        descriptionsMap.put(s_Name,descriptions);
        passivePerksMap.put(s_Name,passivePerks);
        passiveDescriptionsMap.put(s_Name,passiveDescriptions);

        //fishing
        s_Name = "fishing";
        perks = new String[]{"Rob", "Scavenger", "Fisherman's Diet", "Filtration", "Grappling Hook", "Hot Rod", "Fish Person"};
        descriptions = new String[]{"+15% chance to pull item off a mob per level","Unlocks new tier of fishing treasure","Fish restore +20% hunger per level","Higher tier (II-V) loot becomes more common, lower tier (I) becomes less common","Fishing rod now acts as a grappling hook (toggleable with /grappleToggle )","Fish are now cooked when caught, some fishing treasures are changed (toggleable with /hotRodToggle)","Infinite night vision when underwater, infinite dolphin's grace when in the water"};
        passivePerks = new String[]{"Passive Tokens","Back","Skill Tokens","Super Bait Duration","Double catches","Treasure Finder"};
        passiveDescriptions = new String[]{"Tokens to invest in passive skills (dyes)","Takes you back to the main skills menu","Tokens to invest in skill tree","Increases duration of Super Bait by 0.01s","Increases chance to receive a double drop by 0.05%","Decreases chance of finding junk by 0.005%, increases chance of finding treasure by 0.005%"};
        perksMap.put(s_Name,perks);
        descriptionsMap.put(s_Name,descriptions);
        passivePerksMap.put(s_Name,passivePerks);
        passiveDescriptionsMap.put(s_Name,passiveDescriptions);

        //archery
        s_Name = "archery";
        perks = new String[]{"Extra Arrows", "Sniper", "Arrow of Light", "Explosive Arrows", "Dragon-less Arrows", "Crossbow Rapid Load", "Deadly Strike"};
        descriptions = new String[]{"+1 arrow gained from crafting per level","Arrow speed increases by +2% per level (~4% damage increase/level)","Spectral arrows get a +5% damage boost per level","Arrows have a +1% of creating an explosion on hit","Allows crafting all tipped arrows with regular potions instead of lingering potions","Ability can now be used with crossbows, making all shots load instantly","Fireworks shot from crossbows do double damage (up to 16 hearts of damage)"};
        passivePerks = new String[]{"Passive Tokens","Back","Skill Tokens","Rapid Fire Duration","Retrieval"};
        passiveDescriptions = new String[]{"Tokens to invest in passive skills (dyes)","Takes you back to the main skills menu","Tokens to invest in skill tree","Increases duration of Rapid Fire by 0.02s","Increases chance for arrow shot to not consume arrow by 0.05% per level"};
        perksMap.put(s_Name,perks);
        descriptionsMap.put(s_Name,descriptions);
        passivePerksMap.put(s_Name,passivePerks);
        passiveDescriptionsMap.put(s_Name,passiveDescriptions);

        //beastMastery
        s_Name = "beastMastery";
        perks = new String[]{"Thick Fur","Sharp Teeth","Healthy Bites","Keep Away","Acro-Dog","Identify","Adrenaline Boost"};
        descriptions = new String[]{"Dogs take -10% damage per level","Dogs do +10% more damage per level","Dogs heal +1/2 heart per level from killing","Dogs have gain +5% chance of knocking back foes","Dogs do not take fall damage","Using a compass on a horse or wolf now shows their stats","Spur kick buff is now speed III"};
        passivePerks = new String[]{"Passive Tokens","Back","Skill Tokens","Spur Kick Duration","Critical Bite"};
        passiveDescriptions = new String[]{"Tokens to invest in passive skills (dyes)","Takes you back to the main skills menu","Tokens to invest in skill tree","Increases duration of Spur Kick by 0.02s","Increases chance for a dog to have a critical hit by 0.025%"};
        perksMap.put(s_Name,perks);
        descriptionsMap.put(s_Name,descriptions);
        passivePerksMap.put(s_Name,passivePerks);
        passiveDescriptionsMap.put(s_Name,passiveDescriptions);

        //swordsmanship
        s_Name = "swordsmanship";
        perks = new String[]{"Adrenaline","Killing Dpree","Adrenaline+","Killing Frenzy","Thirst for Blood","Sharper!","Sword Mastery"};
        descriptions = new String[]{"Killing hostile mobs with a sword provides +2 s of speed per level","Killing hostile mobs with a sword provides +2 s of strength per level","+20% of speed I buff from Adrenaline is now speed II","+20% of strength I buff from Killing Spree is now strength II","Killing certain aggressive mobs with a sword restores hunger","Swift strikes now adds a level of sharpness to your sword","Swords permanently do +1 heart of damage"};
        passivePerks = new String[]{"Passive Tokens","Back","Skill Tokens","Swift Strikes Duration","Double Hit"};
        passiveDescriptions = new String[]{"Tokens to invest in passive skills (dyes)","Takes you back to the main skills menu","Tokens to invest in skill tree","Increases duration of Swift Strikes by 0.02s","Increases chance to hit mob twice (second hit does 50% damage) by 0.02%"};
        perksMap.put(s_Name,perks);
        descriptionsMap.put(s_Name,descriptions);
        passivePerksMap.put(s_Name,passivePerks);
        passiveDescriptionsMap.put(s_Name,passiveDescriptions);

        //defense
        s_Name = "defense";
        perks = new String[]{"Healer","Stiffen","Hard Headed","Stiffen+","Gift From Above","Stronger Legs","Hearty"};
        descriptions = new String[]{"Gain +3s of regen per level on kill","+2% chance to gain resistance I for 5s when hit","Hard Body decreases damage by an additional 6.6% per level","+2% chance to gain resistance II for 5s when hit","Stone Solid now grants 4 absorption hearts for ability length +1 minute","Stone Solid now gives slowness I instead of slowness IV","+2 hearts permanently"};
        passivePerks = new String[]{"Passive Tokens","Back","Skill Tokens","Stone Solid Duration","Hard Body","Double Drops (Hostile Mobs)"};
        passiveDescriptions = new String[]{"Tokens to invest in passive skills (dyes)","Takes you back to the main skills menu","Tokens to invest in skill tree","Increases duration of Stone Solid by 0.02s","Increases chance to take reduced (base -33%) damage by 0.01% per level","Increases chance to receive double drops from aggressive mobs by 0.05%"};
        perksMap.put(s_Name,perks);
        descriptionsMap.put(s_Name,descriptions);
        passivePerksMap.put(s_Name,passivePerks);
        passiveDescriptionsMap.put(s_Name,passiveDescriptions);

        //axeMastery
        s_Name = "axeMastery";
        perks = new String[]{"Greater Axe","Holy Axe","Revitalized","Warrior Blood","Earthquake","Better Crits","Axe Man"};
        descriptions = new String[]{"Great Axe damage radius increases by 1 block per level","+2% chance for lighting to strike mobs on axe hit","+1% chance for full heal on kill per level","+3 s per level of Strength I on kills with an axe","Ability's AOE damage is doubled (25% -> 50% of damage)","Divine Crits now have 1.6x multiplier instead of 1.25x","Axes permanently do +1 heart of damage"};
        passivePerks = new String[]{"Passive Tokens","Back","Skill Tokens","Great Axe Duration","Divine Crits"};
        passiveDescriptions = new String[]{"Tokens to invest in passive skills (dyes)","Takes you back to the main skills menu","Tokens to invest in skill tree","Increases duration of Great Axe by 0.02s","Increases random crit chance (base 1.25x damage) by 0.01%"};
        perksMap.put(s_Name,perks);
        descriptionsMap.put(s_Name,descriptions);
        passivePerksMap.put(s_Name,passivePerks);
        passiveDescriptionsMap.put(s_Name,passiveDescriptions);

        //Repair
        s_Name = "repair";
        perks = new String[]{"Salvaging","Resourceful","Magic Repair Mastery"};
        descriptions = new String[]{"Get more materials on average from salvaging","+10% chance of keeping material used when repairing","Guarenteed to keep enchants on repair"};
        passivePerks = new String[]{"Back","Skill Tokens","Proficiency"};
        passiveDescriptions = new String[]{"Takes you back to the main skills menu","Tokens to invest in skill tree","Materials restore more durability on repair"};
        perksMap.put(s_Name,perks);
        descriptionsMap.put(s_Name,descriptions);
        passivePerksMap.put(s_Name,passivePerks);
        passiveDescriptionsMap.put(s_Name,passiveDescriptions);

        //Agility
        s_Name = "agility";
        perks = new String[]{"Dodge","Steel Bones","Graceful Feet"};
        descriptions = new String[]{"+4% chance to dodge attacks per level","-10% fall damage per level","Permanent speed I buff (toggleable by /speedToggle)"};
        passivePerks = new String[]{"Back","Skill Tokens","Roll"};
        passiveDescriptions = new String[]{"Takes you back to the main skills menu","Tokens to invest in skill tree","Chance to roll and take reduced fall damage"};
        perksMap.put(s_Name,perks);
        descriptionsMap.put(s_Name,descriptions);
        passivePerksMap.put(s_Name,passivePerks);
        passiveDescriptionsMap.put(s_Name,passiveDescriptions);

        //Alchemy
        s_Name = "alchemy";
        perks = new String[]{"Alchemical Summoning","Ancient Knowledge","Potion Master"};
        descriptions = new String[]{"Allows crafting of some potions without a brewing stand","Unlocks ability to brew new potions","All used potions are increased in level by 1 (toggleable with /togglePotion)"};
        passivePerks = new String[]{"Back","Skill Tokens","Half-life+"};
        passiveDescriptions = new String[]{"Takes you back to the main skills menu","Tokens to invest in skill tree","Increase in duration of potions when used"};
        perksMap.put(s_Name,perks);
        descriptionsMap.put(s_Name,descriptions);
        passivePerksMap.put(s_Name,passivePerks);
        passiveDescriptionsMap.put(s_Name,passiveDescriptions);

        //Smelting
        s_Name = "smelting";
        perks = new String[]{"Fuel Efficiency","Double Smelt","Flame Pickaxe"};
        descriptions = new String[]{"Fuel last +20% longer per level","+5% chance for smelted ore to be doubled per level","Mined ores are instantly smelted (toggleable with /toggleFlamePick)"};
        passivePerks = new String[]{"Back","Skill Tokens","Fuel Speed"};
        passiveDescriptions = new String[]{"Takes you back to the main skills menu","Tokens to invest in skill tree","Increasing cooking speed"};
        perksMap.put(s_Name,perks);
        descriptionsMap.put(s_Name,descriptions);
        passivePerksMap.put(s_Name,passivePerks);
        passiveDescriptionsMap.put(s_Name,passiveDescriptions);

        //Enchanting
        s_Name = "enchanting";
        perks = new String[]{"Efficient Enchanting","Booksmart","Immortal Experience"};
        descriptions = new String[]{"Levels needed to enchant -1 per level, anvil repair costs -1 (minimum of 2) XP levels per level","Unlocks crafting recipes for some enchanted books","Keep xp on death"};
        passivePerks = new String[]{"Back","Skill Tokens","Quicker Development"};
        passiveDescriptions = new String[]{"Takes you back to the main skills menu","Tokens to invest in skill tree","All xp received increased"};
        perksMap.put(s_Name,perks);
        descriptionsMap.put(s_Name,descriptions);
        passivePerksMap.put(s_Name,passivePerks);
        passiveDescriptionsMap.put(s_Name,passiveDescriptions);

        //Enchanting
        s_Name = "global";
        perks = new String[]{"Gatherer","Scholar","Fighter","Hard Work","Research","Training","Reincarnation+","Soul Harvesting","Avatar","Master of the Arts"};
        descriptions = new String[]{"+20% exp gained in Digging, Woodcutting, Mining, Farming, and Fishing","+20% exp gained in Repair, Agility, Brewing, Smelting, and Enchanting","+20% exp gained in Archery, Beast Mastery, Swordsmanship, Defense, and Axe Mastery","+1 skill token in all Gatherer skills","+1 skill token in all Scholar skills","+1 skill token in all Fighter skills","On death, 50% chance to keep some of each valuable item in your inventory","You now harvest souls from killing aggressive mobs, which can be used to refund skill trees","10% chance to take no damage and gain all in-game buffs for 10s on a hit that would normallu kill you","Ability cooldowns decreased by 33%"};
        passivePerks = new String[]{"Global Tokens","Back"};
        passiveDescriptions = new String[]{"Tokens to invest in skill tree","Takes you back to the main skills menu"};
        perksMap.put(s_Name,perks);
        descriptionsMap.put(s_Name,descriptions);
        passivePerksMap.put(s_Name,passivePerks);
        passiveDescriptionsMap.put(s_Name,passiveDescriptions);
    }

    public void initializeVersion() {
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        version = plugin.getDescription().getVersion();
    }

    public String replaceIfPresent(String initialString,String searchFor,String replaceWith) {
        if (initialString.contains(searchFor)) {
            return initialString.replace(searchFor,replaceWith);
        }
        else {
            return initialString;
        }
    }

    public ArrayList<String> getStringLines(String string) {
        ArrayList<String> splitDescs = new ArrayList<>();
        splitDescs.add(string);
        int iter = 0;
        while (splitDescs.get(splitDescs.size()-1).length() > 30) {
            int lastIndex = splitDescs.size()-1;
            boolean foundSpace = false;
            int counter = 30;
            while (foundSpace == false && counter > 0){
                if (splitDescs.get(lastIndex).charAt(counter) == ' ') {
                    splitDescs.add(splitDescs.get(lastIndex).substring(0, counter));
                    splitDescs.add(splitDescs.get(lastIndex).substring(counter+1));
                    splitDescs.remove(iter);
                    iter += 1;
                    foundSpace = true;
                }
                else if (counter == 1) {
                    splitDescs.add(splitDescs.get(lastIndex).substring(0, 30)+"-");
                    splitDescs.add(splitDescs.get(lastIndex).substring(30+1));
                    splitDescs.remove(iter);
                    iter += 1;
                    foundSpace = true;
                }
                counter = counter - 1;
            }
            if (iter > 20) {
                break;
            }
        }
        return splitDescs;
    }

    public String camelCaseToTitle(String string) {
        String newString = "";
        ArrayList<Integer> splitIndexes = new ArrayList<>();
        splitIndexes.add(0);
        for (int i = 0; i<string.length();i++){
            char c = string.charAt(i);
            if (Character.isUpperCase(c)){
                splitIndexes.add(i);
            }
        }
        splitIndexes.add(string.length()-1);
        if (splitIndexes.size() == 2) {
            return string.substring(0,1).toUpperCase() + string.substring(1);
        }
        for (int i = 0; i < splitIndexes.size()-1; i++) {
            int begin = splitIndexes.get(i);
            int end = splitIndexes.get(i+1);
            newString += string.substring(begin,begin+1).toUpperCase() + string.substring(begin+1,end) + " ";
        }
        return newString;
    }

    public String rankToString(int rank) {
        String suffix = "th";
        int lastTwoDigits = rank % 100;
        int lastDigit = rank % 10;
        if (!(lastTwoDigits >= 10 && lastTwoDigits <= 19) ) {
            if (lastDigit == 1) {
                suffix = "st";
            } else if (lastDigit == 2) {
                suffix = "nd";
            } else if (lastDigit == 3) {
                suffix = "rd";
            }
        }
        return (String.valueOf(rank) + suffix);
    }

    public String cleanUpTitleString(String string) {
        String cleanString = string.toLowerCase();
        cleanString = cleanString.replaceAll("\\[","");
        cleanString = cleanString.replaceAll("]","");
        cleanString = cleanString.replaceAll("_"," ");
        String[] cleanStringPieces = cleanString.split(" ");
        cleanString = "";
        for (int i = 0; i < cleanStringPieces.length; i++) {
            String piece = cleanStringPieces[i];
            String capitalizedPiece = piece.substring(0, 1).toUpperCase() + piece.substring(1);
            if ((i+1) == cleanStringPieces.length) {
                cleanString += capitalizedPiece;
            }
            else {
                cleanString += capitalizedPiece + " ";
            }
        }
        cleanString = replaceNumberInStringWithRomanNumeralUptoTen(cleanString); //Roman Numeral Business
        return cleanString;
    }

    public String replaceNumberInStringWithRomanNumeralUptoTen(String string) {
        string = string.replaceAll("10","X");
        string = string.replaceAll("9","IX");
        string = string.replaceAll("8","VIII");
        string = string.replaceAll("7","VII");
        string = string.replaceAll("6","VI");
        string = string.replaceAll("5","V");
        string = string.replaceAll("4","IV");
        string = string.replaceAll("3","III");
        string = string.replaceAll("2","II");
        string = string.replaceAll("1","I");
        return string;
    }
    public String[] getEnchantingCraftingNames(Player p){
        LanguageSelector lang = new LanguageSelector(p);
        String[] craftingNames = {lang.getString("enchantingCraft0"),lang.getString("enchantingCraft1"),
                lang.getString("enchantingCraft2"),lang.getString("enchantingCraft3"),lang.getString("enchantingCraft4"),
                lang.getString("enchantingCraft5"),lang.getString("enchantingCraft6"),lang.getString("enchantingCraft7"),
                lang.getString("enchantingCraft8"),lang.getString("enchantingCraft9")};
        ConfigLoad configLoad = new ConfigLoad();
        Map<String,CustomRecipe> customRecipeMap = configLoad.getCraftingRecipes();
        Enchantment[] defaultEnchants = {Enchantment.ARROW_DAMAGE,Enchantment.DIG_SPEED,Enchantment.DAMAGE_ALL,Enchantment.PROTECTION_ENVIRONMENTAL,Enchantment.LUCK,
                Enchantment.LURE,Enchantment.FROST_WALKER,Enchantment.DEPTH_STRIDER,Enchantment.MENDING,Enchantment.LOOT_BONUS_BLOCKS};
        for (int i = 0; i < craftingNames.length; i++) {
            int stringIndex = i+1;
            CustomRecipe customRecipe = customRecipeMap.get("enchanting"+stringIndex);
            Material output = customRecipe.getOutput();
            if (customRecipe.outputIsEnchanted()) {
                Enchantment enchantment = customRecipe.getEnchantment();
                if (!enchantment.equals(defaultEnchants[i]) || !output.equals(Material.ENCHANTED_BOOK)) {
                    craftingNames[i] = cleanUpTitleString(getBetterEnchantmentString(enchantment.toString())+ " " + customRecipe.getEnchantmentLevel() + " " + output.toString());
                }
            }
            else {
                craftingNames[i] = cleanUpTitleString(output.toString());
            }
        }
        return craftingNames;
    }

    public String getBetterEnchantmentString(String enchantmentString) {
        int i = enchantmentString.indexOf(":");
        int j = enchantmentString.indexOf(",");
        if (j == -1) {
            j = enchantmentString.length();
        }
        return  enchantmentString.substring(i+1,j);
    }
    public String getEnchantmentPerkDescString(int level,Player p) {
        ConfigLoad configLoad = new ConfigLoad();
        Map<String,CustomRecipe> customRecipeMap = configLoad.getCraftingRecipes();
        String[] craftingNames = getEnchantingCraftingNames(p);
        LanguageSelector lang = new LanguageSelector(p);
        int index1 = 2*(level-1);
        int index2 = 2*(level-1)+1;
        String name1 = craftingNames[index1];
        String name2 = craftingNames[index2];
        if ( name1.equalsIgnoreCase(lang.getString("enchantingCraft"+index1)) && name2.equalsIgnoreCase(lang.getString("enchantingCraft"+index2)) ) {
            return lang.getString("enchantingPerkDesc1_"+level);
        }
        else {
            index1 += 1;
            index2 += 1;
            CustomRecipe customRecipe1 = customRecipeMap.get("enchanting"+index1);
            CustomRecipe customRecipe2 = customRecipeMap.get("enchanting"+index2);
            String newString = "";
            int cost1 = customRecipe1.getXPcraftCost();
            int cost2 = customRecipe2.getXPcraftCost();
            String id1 = "xpLevel";
            String id2 = "xpLevel";
            if (cost1 != 1) {
                id1+="s";
            }
            if (cost2 != 1) {
                id2+="s";
            }

            newString = name1 + " (" + lang.getString("costs") + " "+cost1+" " + lang.getString(id1) + ")" + " & " + name2 + " (" + lang.getString("costs") + " "+cost2+" " + lang.getString(id2) + ")";
            return newString;
        }
    }

    public String getPotionEffectTypeString(int level, Player p) {
        ConfigLoad configLoad = new ConfigLoad();
        Map<String, CustomPotion> alchemyInfo = configLoad.getAlchemyInfo();
        String name = getPotionNameFromEffect(alchemyInfo.get("customPotion"+level).getPotionEffectType());
        String effectType = "";
        if (!name.equalsIgnoreCase("Potion")) {
            LanguageSelector lang = new LanguageSelector(p);
            String id = "potion" + name.substring(10).replace(" ","");
            effectType = lang.getString(id);
        }
        return effectType;
    }

    public void setLanguageItems(Player p,Inventory gui) {
        int[] indexes = {19,20,21,22,23,24,25,37,38,39,40,41,42,43};
        int maxIndex = Math.min(indexes.length,languageCodes.size());
        for (int i = 0; i<maxIndex;  i++) {
            String languageCode = languageCodes.get(i);
            String nativeNameKey = languageCode + "." + "languageName";
            String englishNameKey = languageCode + "." + "englishLanguageName";
            String nativeName = "";
            String englishName = "";
            if (idToStringMap.containsKey(nativeNameKey)) {
                nativeName = idToStringMap.get(nativeNameKey);
            }
            if (idToStringMap.containsKey(englishNameKey)) {
                englishName = idToStringMap.get(englishNameKey);
            }
            double progress = languageProgress.get(languageCode);
            String progressString = (int) Math.floor(progress * 100) + "%"; //Turn progress double into string
            ChatColor color = ChatColor.GREEN;
            if (0.7 <= progress && progress < 0.9) {
                color = ChatColor.YELLOW;
            }
            if (0.5 <= progress && progress < 0.7) {
                color = ChatColor.RED;
            }
            if (0.2 <= progress && progress < 0.5) {
                color = ChatColor.DARK_RED;
            }
            if (progress < 0.2) {
                color = ChatColor.DARK_GRAY;
            }
            LanguageSelector lang = new LanguageSelector(p);
            String language = lang.getLanguage();
            ItemStack langItem = new ItemStack(Material.BOOK);
            ItemMeta langItemMeta = langItem.getItemMeta();
            langItemMeta.setDisplayName(ChatColor.WHITE + ChatColor.BOLD.toString() + nativeName);
            ArrayList<String> langItemLore = new ArrayList<>();
            langItemLore.add(ChatColor.ITALIC + ChatColor.GRAY.toString() + "(" + englishName + ")");
            langItemLore.add(ChatColor.ITALIC + ChatColor.GRAY.toString() + lang.getString("status") + ": " +
                    ChatColor.RESET + color + progressString + " " + lang.getString("complete"));
            langItemMeta.setLore(langItemLore);
            langItem.setItemMeta(langItemMeta);
            gui.setItem(indexes[i], langItem);

            ItemStack langItemToggle = new ItemStack(Material.LIME_DYE);
            ItemMeta langItemToggleMeta = langItemToggle.getItemMeta();
            if (language.equalsIgnoreCase(languageCode)) {
                langItemToggleMeta.setDisplayName(ChatColor.BOLD + ChatColor.GREEN.toString() + lang.getString("on0"));
            } else {
                langItemToggle.setType(Material.GRAY_DYE);
                langItemToggleMeta.setDisplayName(ChatColor.BOLD + ChatColor.RED.toString() + lang.getString("off0"));
            }
            langItemToggle.setItemMeta(langItemToggleMeta);
            gui.setItem(indexes[i] + 9, langItemToggle);
        }
    }

    public String getPotionTypeString(int level,Player p) {
        String effectType = "";
        LanguageSelector lang = new LanguageSelector(p);
        ConfigLoad configLoad = new ConfigLoad();
        Map<String, CustomRecipe> customRecipeMap = configLoad.getCraftingRecipes();
        CustomRecipe customRecipe = customRecipeMap.get("alchemy"+level);
        if (customRecipe.outputIsPotion()) {
        String id = getPotionNameIDFromPotionType(customRecipe.getPotionType());
        effectType = lang.getString(id);
        }
        else {
            effectType = cleanUpTitleString(customRecipe.getOutput().toString());
        }
        return effectType;
    }

    public String getPotionNameIDFromPotionType(PotionType potionType) {
        switch (potionType) {
            case AWKWARD:
                return "potionAwkward";
            case UNCRAFTABLE:
                return "potionUncraftable";
            case MUNDANE:
                return "potionMundane";
            case TURTLE_MASTER:
                return "potionTurtleMaster";
            case THICK:
                return "potionThick";
            case WATER:
                return "potionWater";
            default:
                String tempName = getPotionNameFromEffect(potionType.getEffectType());
                String id = "potion" + tempName.substring(10).replace(" ","");
                return id;
        }

    }

    public String getPotionNameFromEffect(PotionEffectType potionEffectType) {
        String name = "Potion";
        switch (potionEffectType.getName()) {
            case "ABSORPTION":
                name = "Potion of Absorption";
                break;
            case "BAD_OMEN":
                name = "Potion of Bad Omen";
                break;
            case "BLINDNESS":
                name = "Potion of Blindness";
                break;
            case "CONDUIT_POWER":
                name = "Potion of Conduit Power";
                break;
            case "CONFUSION":
                name = "Potion of Confusion";
                break;
            case "DAMAGE_RESISTANCE":
                name = "Potion of Resistance";
                break;
            case "DOLPHINS_GRACE":
                name = "Potion of Dolphin's Grace";
                break;
            case "FAST_DIGGING":
                name = "Potion of Haste";
                break;
            case "FIRE_RESISTANCE":
                name = "Potion of Fire Resistance";
                break;
            case "GLOWING":
                name = "Potion of Glowing";
                break;
            case "HARM":
                name = "Potion of Harm";
                break;
            case "HEAL":
                name = "Potion of Healing";
                break;
            case "HEALTH_BOOST":
                name = "Potion of Health Boost";
                break;
            case "HERO_OF_THE_VILLAGE":
                name = "Potion of the Hero";
                break;
            case "HUNGER":
                name = "Potion of Hunger";
                break;
            case "INCREASE_DAMAGE":
                name = "Potion of Strength";
                break;
            case "INVISIBILITY":
                name = "Potion of Invisibility";
                break;
            case "JUMP":
                name = "Potion of Jump";
                break;
            case "LEVITATION":
                name = "Potion of Levitation";
                break;
            case "LUCK":
                name = "Potion of Luck";
                break;
            case "NIGHT_VISION":
                name = "Potion of Night Vision";
                break;
            case "POISON":
                name = "Potion of Night Vision";
                break;
            case "REGENERATION":
                name = "Potion of Regeneration";
                break;
            case "SATURATION":
                name = "Potion of Saturation";
                break;
            case "SLOW":
                name = "Potion of Slowness";
                break;
            case "SLOW_DIGGING":
                name = "Potion of Fatigue";
                break;
            case "SLOW_FALLING":
                name = "Potion of Slow Falling";
                break;
            case "SPEED":
                name = "Potion of Speed";
                break;
            case "UNLUCK":
                name = "Potion of Bad Luck";
                break;
            case "WATER_BREATHING":
                name = "Potion of Water Breathing";
                break;
            case "WEAKNESS":
                name = "Potion of Weakness";
                break;
            case "WITHER":
                name = "Potion of Decay";
                break;
            default:
                break;

        }
        return name;
    }

    public Map<String, String[]> getDescriptionsMap() {
        return descriptionsMap;
    }

    public Map<String, String[]> getPassiveDescriptionsMap() {
        return passiveDescriptionsMap;
    }

    public Map<String, String[]> getPassivePerksMap() {
        return passivePerksMap;
    }

    public Map<String, String[]> getPerksMap() {
        return perksMap;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, String> getIdToStringMap() {
        return idToStringMap;
    }

    public Map<Integer, String> getBookIndexes() {
        return bookIndexes;
    }

    public Map<Integer, String> getDyeIndexes() {
        return dyeIndexes;
    }

    public ArrayList<String> getSkillNames() {
        return skillNames;
    }

    public ArrayList<String> getSkillNamesWithGlobal() {
        return skillNamesWithGlobal;
    }
}
