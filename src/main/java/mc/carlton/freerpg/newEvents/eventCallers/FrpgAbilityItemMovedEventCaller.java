package mc.carlton.freerpg.newEvents.eventCallers;

import mc.carlton.freerpg.gameTools.TrackItem;
import mc.carlton.freerpg.newEvents.FrpgAbilityItemMovedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

public class FrpgAbilityItemMovedEventCaller implements Listener {

  @EventHandler
  public void inventoryClickAbilityItem(InventoryClickEvent e) {
    Player p;

    Inventory clickedInventory;
    InventoryType clickedInventoryType;
    try {
      p = (Player) e.getWhoClicked();
      clickedInventory = e.getClickedInventory();
      clickedInventoryType = clickedInventory.getType();
    } catch (Exception except) {
      return;
    }
    if (e.getClick().equals(ClickType.NUMBER_KEY)) {
      if (!clickedInventoryType.equals(InventoryType.PLAYER)) {
        ItemStack itemToBeMoved = p.getInventory().getItem(e.getHotbarButton());
        callEventIfItemIsTracked(itemToBeMoved, p, e); //Calls event
      }
    } else if (e.getClick().equals(ClickType.SWAP_OFFHAND)) {
      ItemStack itemInOffhand = p.getInventory().getItemInOffHand();
      if (itemInOffhand != null) {
        callEventIfItemIsTracked(itemInOffhand, p, e); //Calls event
      }
    } else if (e.isShiftClick()) {
      ItemStack clickedItem = e.getCurrentItem();
      callEventIfItemIsTracked(clickedItem, p, e); //Calls event
    } else if (e.getCursor() != null) {
      if (!e.getCursor().getType().equals(Material.AIR)) {
        if (!clickedInventoryType.equals(InventoryType.PLAYER)) {
          ItemStack clickedItem = e.getCursor();
          callEventIfItemIsTracked(clickedItem, p, e); //Calls event
        }
      }
    }
  }

  @EventHandler
  public void inventoryDragAbilityItem(InventoryDragEvent e) {
    Player p;

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
        callEventIfItemIsTracked(clickedItem, p, e); //Calls event
      }
    }
  }

  @EventHandler
  public void itemFrameRightClickAbilityItem(PlayerInteractEntityEvent e) {
    if (!(e.getRightClicked() instanceof ItemFrame)) {
      return;
    }
    ItemStack clickedItem;
    Player p = e.getPlayer();
    if (e.getHand().equals(EquipmentSlot.HAND)) {
      clickedItem = p.getInventory().getItemInMainHand();
    } else {
      clickedItem = p.getInventory().getItemInOffHand();
    }
    callEventIfItemIsTracked(clickedItem, p, e); //Calls event
  }

  private void createAndCallAbilityItemMovedEvent(ItemStack itemStack, Event event) {
    FrpgAbilityItemMovedEvent abilityItemMovedEvent = null;
    if (event instanceof PlayerInteractEntityEvent) {
      abilityItemMovedEvent = new FrpgAbilityItemMovedEvent((PlayerInteractEntityEvent) event,
          itemStack);
    } else if (event instanceof InventoryDragEvent) {
      abilityItemMovedEvent = new FrpgAbilityItemMovedEvent((InventoryDragEvent) event, itemStack);
    } else if (event instanceof InventoryClickEvent) {
      abilityItemMovedEvent = new FrpgAbilityItemMovedEvent((InventoryClickEvent) event, itemStack);
    }
    if (abilityItemMovedEvent != null) {
      PluginManager pluginManager = Bukkit.getServer().getPluginManager();
      pluginManager.callEvent(abilityItemMovedEvent); //Call event
    }
  }

  private void callEventIfItemIsTracked(ItemStack itemStack, Player p, Event event) {
    TrackItem trackItem = new TrackItem();
    NamespacedKey key = trackItem.getFreeRPGItemKey(itemStack, p);
    if (key != null) {
      createAndCallAbilityItemMovedEvent(itemStack, event);
    }

        /*
        Below is a more specific version of the same method
         */
        /*
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
                    FrpgAbilityItemMovedEvent abilityItemMovedEvent = new FrpgAbilityItemMovedEvent(e,clickedItem);
                    pluginManager.callEvent(abilityItemMovedEvent); //Call event
                }
            }
         */
  }
}
