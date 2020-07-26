package mc.carlton.freerpg.leaveAndJoin;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    void onPlayerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        LoginProcedure login = new LoginProcedure(p);
        login.playerLogin();
    }
}
