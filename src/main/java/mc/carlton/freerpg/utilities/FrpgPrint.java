package mc.carlton.freerpg.utilities;

import mc.carlton.freerpg.FreeRPG;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

//Logs through the plugin's logger (prefixed with [FreeRPG]) instead of System.out, which Paper warns about
public class FrpgPrint {
    private static Logger logger() {
        return JavaPlugin.getPlugin(FreeRPG.class).getLogger();
    }

    public static void print(String message) {
        logger().info(message);
    }

    public static void warning(String message) {
        logger().warning(message);
    }

    public static void error(String message, Throwable throwable) {
        logger().log(Level.SEVERE, message, throwable);
    }
}
