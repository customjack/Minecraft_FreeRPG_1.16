package mc.carlton.freerpg.newEvents.eventCallers;

import mc.carlton.freerpg.gameTools.TrackItem;
import mc.carlton.freerpg.newEvents.FrpgAbilityItemMovedEvent;
import mc.carlton.freerpg.newEvents.FrpgPlayerCraftItemEvent;
import mc.carlton.freerpg.newEvents.FrpgPlayerRightClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.PluginManager;

public class FrpgAbilityItemMovedEventCaller implements Listener {

    @EventHandler
    public void inventoryClickAbilityItem(InventoryClickEvent e) {
        Player p;
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();

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
                    FrpgAbilityItemMovedEvent abilityItemMovedEvent = new FrpgAbilityItemMovedEvent(e,clickedItem);
                    pluginManager.callEvent(abilityItemMovedEvent); //Call event

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
                        FrpgAbilityItemMovedEvent abilityItemMovedEvent = new FrpgAbilityItemMovedEvent(e,clickedItem);
                        pluginManager.callEvent(abilityItemMovedEvent); //Call event
                    }
                }
            }
        }
    }

    @EventHandler
    public void inventoryDragAbilityItem(InventoryDragEvent e) {
        Player p;
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();

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
                        FrpgAbilityItemMovedEvent abilityItemMovedEvent = new FrpgAbilityItemMovedEvent(e,clickedItem);
                        pluginManager.callEvent(abilityItemMovedEvent); //Call event
                    }
                }
            }
        }
    }

    @EventHandler
    public void itemFrameRightClickAbilityItem(PlayerInteractEntityEvent e){
        if (!(e.getRightClicked() instanceof ItemFrame)) {
            return;
        }
        ItemStack clickedItem;
        Player p = e.getPlayer();
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        if (e.getHand().equals(EquipmentSlot.HAND)) {
            clickedItem = p.getInventory().getItemInMainHand();
        } else {
            clickedItem = p.getInventory().getItemInOffHand();
        }
        TrackItem trackItem = new TrackItem();
        NamespacedKey key = trackItem.getFreeRPGItemKey(clickedItem,p);
        if (key != null) {
            FrpgAbilityItemMovedEvent abilityItemMovedEvent = new FrpgAbilityItemMovedEvent(e,clickedItem);
            pluginManager.callEvent(abilityItemMovedEvent); //Call event
        }
    }
}
