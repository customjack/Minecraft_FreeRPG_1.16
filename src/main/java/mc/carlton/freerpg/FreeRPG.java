package mc.carlton.freerpg;

import mc.carlton.freerpg.brewingEvents.BrewingInventoryClick;
import mc.carlton.freerpg.brewingEvents.FinishedBrewing;
import mc.carlton.freerpg.clickEvents.PlayerLeftClick;
import mc.carlton.freerpg.clickEvents.PlayerLeftClickDeveloper;
import mc.carlton.freerpg.clickEvents.PlayerRightClick;
import mc.carlton.freerpg.clickEvents.PlayerRightClickEntity;
import mc.carlton.freerpg.combatEvents.*;
import mc.carlton.freerpg.commands.*;
import mc.carlton.freerpg.customConfigContainers.CustomContainerImporter;
import mc.carlton.freerpg.customConfigContainers.CustomItem;
import mc.carlton.freerpg.enchantingEvents.*;
import mc.carlton.freerpg.furnaceEvents.FurnaceBurn;
import mc.carlton.freerpg.furnaceEvents.FurnaceInventoryClick;
import mc.carlton.freerpg.furnaceEvents.FurnaceSmelt;
import mc.carlton.freerpg.gameTools.PsuedoEnchanting;
import mc.carlton.freerpg.globalVariables.*;
import mc.carlton.freerpg.guiEvents.*;
import mc.carlton.freerpg.leaveAndJoin.LoginProcedure;
import mc.carlton.freerpg.leaveAndJoin.LogoutProcedure;
import mc.carlton.freerpg.miscEvents.*;
import mc.carlton.freerpg.leaveAndJoin.PlayerJoin;
import mc.carlton.freerpg.leaveAndJoin.PlayerLeave;
import mc.carlton.freerpg.newEvents.FrpgPlayerCraftItemEventCaller;
import mc.carlton.freerpg.pistonEvents.PistonEvents;
import mc.carlton.freerpg.playerInfo.*;
import mc.carlton.freerpg.serverConfig.ConfigLoad;
import mc.carlton.freerpg.serverFileManagement.*;
import mc.carlton.freerpg.serverInfo.*;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

public final class FreeRPG extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        //Plugin startup logic
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);

        //Saves Resources if they aren't there
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        saveResource("languages.yml",false);
        saveResource("advancedConfig.yml",false);


        //Checks config.yml and languages.yml for updates, and update them if needed (while trying to keep any edits)
        YMLManager ymlManager = new YMLManager();
        ymlManager.updateCheckYML("config.yml");
        ymlManager.updateCheckYML("languages.yml");
        ymlManager.updateCheckYML("advancedConfig.yml");



        //Loads config to into memory
        ConfigLoad loadConfig = new ConfigLoad();
        loadConfig.initializeConfig();

        //Makes SeverData Folder
        ServerDataFolderPreparation serverDataFolderPreparation = new ServerDataFolderPreparation();
        serverDataFolderPreparation.initializeServerDataFolder();

        //Makes PlayerData Folder
        PlayerStatsFilePreparation playerStatsFilePreparation = new PlayerStatsFilePreparation();
        playerStatsFilePreparation.initializePlayerDataBase();

        //Initialize Placed Blocks Map
        PlacedBlockFileManager placedBlockFileManager = new PlacedBlockFileManager();
        placedBlockFileManager.initializePlacedBlocksFile(); //Creates blockLocations.dat if not already made
        placedBlockFileManager.initializePlacedBlocks(); //Imports data from blockLocations.dat into a hashamp

        //Initializes RecentLogouts List
        RecentPlayersFileManager recentPlayersFileManager = new RecentPlayersFileManager();
        recentPlayersFileManager.initializeRecentPlayersFile(); //Creates recentPlayers.dat if not already made
        recentPlayersFileManager.initializeRecentPlayers(); //Imports data from recentPlayters.dat to an arrayList in RecentLogouts


        //Check if the server uses world guard
        WorldGuardChecks CheckWorldGuardExistence = new WorldGuardChecks();
        CheckWorldGuardExistence.initializeWorldGuardPresent();

        //Checks if the server has PlaceHolderAPI
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI").isEnabled()) {
                new FreeRPGPlaceHolders(this).register();
            }
        }

        //Initializes all "global" variables
        MinecraftVersion minecraftVersion = new MinecraftVersion();
        minecraftVersion.initializeVersion();
        EntityGroups entityGroups = new EntityGroups();
        entityGroups.initializeAllEntityGroups();
        ExpMaps expMaps = new ExpMaps();
        expMaps.initializeAllExpMaps();
        ItemGroups itemGroups =  new ItemGroups();
        itemGroups.initializeItemGroups();
        CraftingRecipes craftingRecipes = new CraftingRecipes();
        craftingRecipes.initializeAllCraftingRecipes();
        StringsAndOtherData stringsAndOtherData = new StringsAndOtherData();
        stringsAndOtherData.initializeData();

        //Initializes Player Leaderboard (and may create a file)
        Leaderboards playerLeaderboard = new Leaderboards();
        LeaderBoardFilesManager leaderBoardFilesManager = new LeaderBoardFilesManager();
        playerLeaderboard.initializeLeaderboards();
        boolean didCreateFile = leaderBoardFilesManager.createLeaderBoardFile(false);
        if (!didCreateFile) {
            leaderBoardFilesManager.readInLeaderBoardFile();
        }

        //Initiliazes periodically saving stats
        PeriodicSaving saveStats = new PeriodicSaving();
        saveStats.periodicallySaveStats();

        //Events
        ConfigLoad configLoad = new ConfigLoad();
        if (!configLoad.isSaveRunTimeData()) {
            getServer().getPluginManager().registerEvents(new PlayerLeftClick(), this);
            getServer().getPluginManager().registerEvents(new PlayerBlockBreak(), this);
        }
        else{
            getServer().getPluginManager().registerEvents(new PlayerLeftClickDeveloper(), this);
            getServer().getPluginManager().registerEvents(new PlayerBlockBreakDeveloper(), this);
        }
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeave(), this);
        getServer().getPluginManager().registerEvents(new MainGUIclick(), this);
        getServer().getPluginManager().registerEvents(new SkillsGUIclick(), this);
        getServer().getPluginManager().registerEvents(new CraftingGUIclick(), this);
        getServer().getPluginManager().registerEvents(new ConfirmationGUIClick(), this);
        getServer().getPluginManager().registerEvents(new ConfigurationGUIClick(), this);
        getServer().getPluginManager().registerEvents(new PlayerBlockPlace(), this);
        getServer().getPluginManager().registerEvents(new PlayerRightClick(), this);
        getServer().getPluginManager().registerEvents(new PlayerTakeDamage(), this);
        getServer().getPluginManager().registerEvents(new PlayerCraft(), this);
        getServer().getPluginManager().registerEvents(new PlayerPrepareCrafting(), this);
        getServer().getPluginManager().registerEvents(new EntityHitEntity(), this);
        getServer().getPluginManager().registerEvents(new PlayerKillEntity(), this);
        getServer().getPluginManager().registerEvents(new PlayerConsumeItem(), this);
        getServer().getPluginManager().registerEvents(new PlayerRightClickEntity(), this);
        getServer().getPluginManager().registerEvents(new PlayerShear(), this);
        getServer().getPluginManager().registerEvents(new PlayerFish(), this);
        getServer().getPluginManager().registerEvents(new PlayerShootBow(), this);
        getServer().getPluginManager().registerEvents(new ArrowLand(), this);
        getServer().getPluginManager().registerEvents(new EntityGetDamaged(), this);
        getServer().getPluginManager().registerEvents(new TameEntityEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerMount(), this);
        getServer().getPluginManager().registerEvents(new PlayerDismount(), this);
        getServer().getPluginManager().registerEvents(new PlayerBreedEntity(), this);
        getServer().getPluginManager().registerEvents(new PlayerToggleSprint(), this);
        getServer().getPluginManager().registerEvents(new BrewingInventoryClick(), this);
        getServer().getPluginManager().registerEvents(new PotionSplash(), this);
        getServer().getPluginManager().registerEvents(new LingeringPotionSplash(), this);
        getServer().getPluginManager().registerEvents(new FinishedBrewing(), this);
        getServer().getPluginManager().registerEvents(new PlayerGetExperience(), this);
        getServer().getPluginManager().registerEvents(new PrepareEnchanting(), this);
        getServer().getPluginManager().registerEvents(new PrepareRepair(), this);
        getServer().getPluginManager().registerEvents(new AnvilClick(), this);
        getServer().getPluginManager().registerEvents(new PlayerEnchant(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new FurnaceSmelt(), this);
        getServer().getPluginManager().registerEvents(new FurnaceInventoryClick(), this);
        getServer().getPluginManager().registerEvents(new FurnaceBurn(), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawn(), this);
        getServer().getPluginManager().registerEvents(new PlayerDropItem(), this);
        getServer().getPluginManager().registerEvents(new ExperienceBottleBreak(), this);
        getServer().getPluginManager().registerEvents(new SkillsConfigGUIClick(), this);
        getServer().getPluginManager().registerEvents(new EntityPickUpItem(), this);
        getServer().getPluginManager().registerEvents(new CreatureSpawn(), this);
        getServer().getPluginManager().registerEvents(new DispenserDispenseItem(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveAbilityItem(), this);
        getServer().getPluginManager().registerEvents(new PlayerEnterVehicle(), this);
        getServer().getPluginManager().registerEvents(new PistonEvents(), this);
        getServer().getPluginManager().registerEvents(new FrpgPlayerCraftItemEventCaller(), this);

        //Registers commands
        getCommand("frpg").setExecutor(new FrpgCommands());
        getCommand("spite").setExecutor(new SpiteQuote());


        //If the plugin starts with players online
        for (Player p : Bukkit.getOnlinePlayers()) {
            LoginProcedure login = new LoginProcedure(p);
            login.playerLogin();
        }

        //Begin Asynchronous loading of offline player stats
        OfflinePlayerStatLoadIn offlinePlayerStatLoadIn = new OfflinePlayerStatLoadIn();
        offlinePlayerStatLoadIn.loadInOfflinePlayers();

        //Check extra stuff on load-up
        //saveResource("perkConfig.yml",false);
        //ymlManager.updateCheckYML("perkConfig.yml");
        //test();
    }

    public void test() { //The purpose of this is to just place test code to run when the plugin is enabled or disabled
        new PsuedoEnchanting().printInfo();
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
        File f = new File(plugin.getDataFolder(),"perkConfig.yml");
        f.setReadable(true,false);
        f.setWritable(true,false);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
        List test = ( (List) (config.getList("fishing.skill_1B.level5.dropTable").get(7)));
        System.out.println(test);
        System.out.println(new CustomItem(test,"fishing.skill_1B.level5.dropTable"));
        System.out.println(CustomContainerImporter.getConfigTableInformation(config,"fishing.skill_1B.level5.dropTable"));

    }

    public void onDisable() {

        //Does everything that would normally be done if a player were to log out
        for (Player p : Bukkit.getOnlinePlayers()) {
            LogoutProcedure logout = new LogoutProcedure(p);
            try {
                logout.playerLogout(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        PlacedBlockFileManager saveBlocks = new PlacedBlockFileManager();
        saveBlocks.writePlacedBlocks();
        RecentPlayersFileManager recentPlayersFileManager = new RecentPlayersFileManager();
        recentPlayersFileManager.writeRecentPlayers();
        LeaderBoardFilesManager leaderBoardFilesManager = new LeaderBoardFilesManager();
        leaderBoardFilesManager.writeOutPlayerLeaderBoardFile();

        ConfigLoad configLoad = new ConfigLoad();
        if (configLoad.isSaveRunTimeData()) {
            RunTimeData runTimeData = new RunTimeData();
            runTimeData.logRunTimeData();
        }
    }

    //Load custom enchantments
    public void registerEnchantment(Enchantment enchantment) {
        boolean registered = true;
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        } catch (Exception e) {
            registered = false;
            e.printStackTrace();
        }
        if(registered){
            // It's been registered!
        }
    }

    public void unregisterEnchantments(Enchantment enchantment) {
        try {
            Field keyField = Enchantment.class.getDeclaredField("byKey");

            keyField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<NamespacedKey, Enchantment> byKey = (HashMap<NamespacedKey, Enchantment>) keyField.get(null);

            if(byKey.containsKey(enchantment.getKey())) {
                byKey.remove(enchantment.getKey());
            }

            Field nameField = Enchantment.class.getDeclaredField("byName");

            nameField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<String, Enchantment> byName = (HashMap<String, Enchantment>) nameField.get(null);

            if(byName.containsKey(enchantment.getName())) {
                byName.remove(enchantment.getName());
            }

        } catch (Exception ignored) { }

    }

}
