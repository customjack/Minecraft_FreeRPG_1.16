package mc.carlton.freerpg.leaveAndJoin;


import mc.carlton.freerpg.FreeRPG;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerJoin implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    void onPlayerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        LoginProcedure login = new LoginProcedure(p);
        login.playerLogin();
    }

    // TODO remove dead code!
    /* Some code testing a custom enchantment I was making
        ItemStack choppingAxe = new ItemStack(Material.DIAMOND_AXE,1);
        choppingAxe.addUnsafeEnchantment(Enchantment.getByKey(new NamespacedKey(FreeRPG.getPlugin(FreeRPG.class),"chopping")),1);

        ItemStack choppingBook = new ItemStack(Material.ENCHANTED_BOOK,1);
        EnchantmentStorageMeta enchantedBookMeta = (EnchantmentStorageMeta) choppingBook.getItemMeta();
        enchantedBookMeta.addStoredEnchant(Enchantment.getByKey(new NamespacedKey(FreeRPG.getPlugin(FreeRPG.class),"chopping")),1,false);
        choppingBook.setItemMeta(enchantedBookMeta);

        p.getInventory().addItem(choppingAxe);
        p.getInventory().addItem(choppingBook);

         */
}
