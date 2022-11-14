package mc.carlton.freerpg.events.newEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

/*
 * A more specific case of the CraftItemEvent that only fires if the item is actually crafted
 * (Spigot's version will fire event if the item is not actually crafted)
 */
public class FrpgPlayerCraftItemEvent extends Event implements Cancellable {

  private static final HandlerList handlers = new HandlerList();
  private boolean cancelled;
  private CraftItemEvent craftItemEvent;
  private Player player;
  private ItemStack result;

  /**
   * Constructor for the event
   *
   * @param event The wrapped craftItemEvent
   * @param p     Player who crafted the recipe
   */
  public FrpgPlayerCraftItemEvent(CraftItemEvent event, Player p) {
    this.craftItemEvent = event;
    this.player = p;
    this.result = event.getRecipe().getResult();
    this.cancelled = event.isCancelled();
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }

  /**
   * getter for result
   *
   * @return The resulting itemstack crafted
   */
  public ItemStack getResult() {
    return result;
  }

  /**
   * Getter for the craft item event
   *
   * @return the CraftItemEvent associated with this event
   */
  public CraftItemEvent getCraftItemEvent() {
    return craftItemEvent;
  }

  /**
   * Getter for the player
   *
   * @return Player who crafted the item
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Getter for cancelled
   *
   * @return whether or not the event has been cancelled
   */
  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  /**
   * Setter for cancelled, cancels the assocaited craftItemEvent
   *
   * @param cancel true if event is to be cancelled, false otherwise
   */
  @Override
  public void setCancelled(boolean cancel) {
    this.craftItemEvent.setCancelled(true); //Cancels the wrapped event
    cancelled = cancel;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }
}
