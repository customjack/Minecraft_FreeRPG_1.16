package mc.carlton.freerpg.globalVariables;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.LanguageSelector;
import mc.carlton.freerpg.playerAndServerInfo.ConfigLoad;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StringsAndOtherData {
    static Map<String, String[]> perksMap = new HashMap<>();
    static Map<String, String[]> descriptionsMap = new HashMap<>();
    static Map<String, String[]> passivePerksMap = new HashMap<>();
    static Map<String, String[]> passiveDescriptionsMap = new HashMap<>();
    static public String version;
    static Map<String,String> idToStringMap = new HashMap<>();

    ArrayList<Double> levelingInfo;

    public StringsAndOtherData(){
        ConfigLoad configLoad = new ConfigLoad();
        levelingInfo = configLoad.getLevelingInfo();
    }

    public void initializeData() {
        initializeSkillDescriptions();
        initializeVersion();
        initializeLanguagesData();

    }

    public void initializeLanguagesData() {
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        File languagesYML = new File(plugin.getDataFolder()+"/languages.yml");
        languagesYML.setReadable(true,false);
        languagesYML.setWritable(true,false);
        FileConfiguration languages = YamlConfiguration.loadConfiguration(languagesYML);
        for (String language : languages.getConfigurationSection("lang").getKeys(false)) {
            for (String id : languages.getConfigurationSection("lang." + language).getKeys(false)) {
                String fullId = language +"." + id;
                idToStringMap.put(fullId,languages.getString("lang." + language + "." + id));
            }
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
                    if (iter > 6) {
                        break;
                    }
                }
                counter = counter - 1;
            }
        }
        return splitDescs;
    }

    public String getPotionEffectTypeString(int level, Player p) {
        String effectType = "";
        LanguageSelector lang = new LanguageSelector(p);
        ConfigLoad configLoad = new ConfigLoad();
        ArrayList<Object> alchemyInfo = configLoad.getAlchemyInfo();
        String name = getPotionNameFromEffect((PotionEffectType) alchemyInfo.get(4*(level-1)));
        if (!name.equalsIgnoreCase("Potion")) {
            String id = "potion" + name.substring(10).replace(" ","");
            effectType = lang.getString(id);
        }
        return effectType;
    }

    public String getPotionTypeString(int level,Player p) {
        String effectType = "";
        LanguageSelector lang = new LanguageSelector(p);
        ConfigLoad configLoad = new ConfigLoad();
        ArrayList<Object> alchemyInfo = configLoad.getAlchemyInfo();
        String id = getPotionNameIDFromPotionType((PotionType) alchemyInfo.get(20 + 2*(level-1)));
        effectType = lang.getString(id);
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
}
