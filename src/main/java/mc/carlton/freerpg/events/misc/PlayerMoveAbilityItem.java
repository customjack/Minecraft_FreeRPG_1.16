package mc.carlton.freerpg.events.misc;

import mc.carlton.freerpg.events.newEvents.FrpgAbilityItemMovedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerMoveAbilityItem implements Listener {

  @EventHandler
  public void moveAbilityItem(FrpgAbilityItemMovedEvent e) {
    e.setCancelled(true); //We simply want to cancel the ability item being moved in all cases
  }
  // TODO remove dead code!
    /*
    @EventHandler(priority =  EventPriority.HIGH)
    public void clickEvent(InventoryClickEvent e) {
        InventoryType topInventoryType;
        Player p;
        Inventory topInventory;
        Inventory clickedInventory;
        InventoryType clickedInventoryType;
        try {
            p = (Player) e.getWhoClicked();
            clickedInventory = e.getClickedInventory();
            clickedInventoryType = clickedInventory.getType();
        } catch (Exception except) {
            return;
        }
        if (e.isShiftClick()) {
            ItemStack clickedItem = e.getCurrentItem();
            TrackItem trackItem = new TrackItem();
            NamespacedKey key = trackItem.getFreeRPGItemKey(clickedItem,p);
            if (key != null) {
                ItemMeta itemMeta = clickedItem.getItemMeta();
                PersistentDataContainer container = itemMeta.getPersistentDataContainer();
                String specialItemType = container.get(key, PersistentDataType.STRING);
                if (specialItemType == null) {
                    return;
                }
                if (specialItemType.equalsIgnoreCase("frpg-digging") || specialItemType.equalsIgnoreCase("frpg-mining")
                        || specialItemType.equalsIgnoreCase("frpg-swordsmanship")) {
                    e.setCancelled(true);
                }
            }
        }
        else if (e.getCursor() != null) {
            if (!e.getCursor().getType().equals(Material.AIR)) {
                ItemStack clickedItem = e.getCursor();
                TrackItem trackItem = new TrackItem();
                NamespacedKey key = trackItem.getFreeRPGItemKey(clickedItem,p);
                if (key != null) {
                    if (!clickedInventoryType.equals(InventoryType.PLAYER)) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority =  EventPriority.HIGH)
    public void clickEvent(InventoryDragEvent e) {
        InventoryType topInventoryType;
        Player p;
        Inventory topInventory;
        Inventory clickedInventory;
        InventoryType clickedInventoryType;
        try {
            p = (Player) e.getWhoClicked();
            clickedInventory = e.getInventory();
            clickedInventoryType = clickedInventory.getType();
        } catch (Exception except) {
            return;
        }
        if (e.getOldCursor() != null) {
            if (!e.getOldCursor().getType().equals(Material.AIR)) {
                ItemStack clickedItem = e.getOldCursor();
                TrackItem trackItem = new TrackItem();
                NamespacedKey key = trackItem.getFreeRPGItemKey(clickedItem,p);
                if (key != null) {
                    if (!clickedInventoryType.equals(InventoryType.PLAYER)) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

     */
}
