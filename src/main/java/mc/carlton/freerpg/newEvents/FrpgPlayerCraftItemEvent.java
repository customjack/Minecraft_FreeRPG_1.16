package mc.carlton.freerpg.newEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class FrpgPlayerCraftItemEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private CraftItemEvent craftItemEvent;
    private Player player;
    private ItemStack result;

    public FrpgPlayerCraftItemEvent(CraftItemEvent event, Player p) {
        this.craftItemEvent = event;
        this.player = p;
        this.result = event.getRecipe().getResult();
    }

    public ItemStack getResult() {
        return result;
    }

    public CraftItemEvent getCraftItemEvent() {
        return craftItemEvent;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {

        cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
