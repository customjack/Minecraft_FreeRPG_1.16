package mc.carlton.freerpg.gameTools;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class ActionBarMessages {
    private Player p;

    public ActionBarMessages(Player p) {
        this.p = p;
    }

    public void sendMessage(String message) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }
}
