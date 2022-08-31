package mc.carlton.freerpg.newEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;


/*
 * An event that wraps PlayerInteractEvent, but only fires if the interaction type is a right click
 */
public class FrpgPlayerRightClickEvent extends Event implements Cancellable {

  private static final HandlerList handlers = new HandlerList();
  private boolean cancelled;
  private PlayerInteractEvent playerInteractEvent;
  private boolean isClickAir;
  private boolean isOffHand;

  public FrpgPlayerRightClickEvent(PlayerInteractEvent event) {
    this.playerInteractEvent = event;
    this.isClickAir = event.getAction().equals(Action.RIGHT_CLICK_AIR);
    this.isOffHand = event.getHand().equals(EquipmentSlot.OFF_HAND);
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }

  /**
   * Getter for the playerInteractEvent
   *
   * @return the associated PlayerInteractEvent
   */
  public PlayerInteractEvent getPlayerInteractEvent() {
    return playerInteractEvent;
  }

  /**
   * Getter for the player
   *
   * @return the player associated PlayerInteractEvent (player who right clicked)
   */
  public Player getPlayer() {
    return playerInteractEvent.getPlayer();
  }

  /**
   * Getter for isClickAir
   *
   * @return true if the right click was on an air block, false otherwise
   */
  public boolean isClickAir() {
    return isClickAir;
  }

  /**
   * Getter for isOffHand
   *
   * @return true if the right click was made using an offhand item, false otherwise
   */
  public boolean isOffHand() {
    return isOffHand;
  }

  /**
   * Getter for cancelled
   *
   * @return false (always because the associated PlayerInteractEvent has two cancelled states)
   */
  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  /**
   * Setter for cancelled
   *
   * @param cancel true if the associated PlayerInteractEvent is to be cancelled
   */
  @Override
  public void setCancelled(boolean cancel) {
    playerInteractEvent.setCancelled(true);
    this.cancelled = cancel;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return handlers;
  }
}
