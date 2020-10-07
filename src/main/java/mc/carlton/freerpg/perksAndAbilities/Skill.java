package mc.carlton.freerpg.perksAndAbilities;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.ActionBarMessages;
import mc.carlton.freerpg.gameTools.LanguageSelector;
import mc.carlton.freerpg.playerInfo.AbilityTimers;
import mc.carlton.freerpg.playerInfo.AbilityTracker;
import mc.carlton.freerpg.playerInfo.ChangeStats;
import mc.carlton.freerpg.playerInfo.PlayerStats;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.Random;


public class Skill {
    Plugin plugin;
    protected Player p;
    protected String pName;
    protected ItemStack itemInHand;
    Map<String,Integer> expMap;
    ChangeStats increaseStats; //Changing Stats

    AbilityTracker abilities; //Abilities class
    // GET ABILITY STATUSES LIKE THIS:   Integer[] pAbilities = abilities.getPlayerAbilities(p);

    AbilityTimers timers; //Ability Timers class
    //GET TIMERS LIKE THIS:              Integer[] pTimers = timers.getPlayerTimers(p);

    PlayerStats pStatClass;
    //GET PLAYER STATS LIKE THIS:        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData(p);

    ActionBarMessages actionMessage;
    LanguageSelector lang;


    public Skill(Player p) {
        this.p = p;
        this.pName = p.getDisplayName();
        this.itemInHand = p.getInventory().getItemInMainHand();
        this.increaseStats = new ChangeStats(p);
        this.abilities = new AbilityTracker(p);
        this.timers = new AbilityTimers(p);
        this.pStatClass=  new PlayerStats(p);
        this.actionMessage = new ActionBarMessages(p);
        this.lang = new LanguageSelector(p);
        this.plugin = FreeRPG.getPlugin(FreeRPG.class);
    }

    public void dropItemNaturally(Location location, ItemStack item) { //Won't try to drop air, like world.dropItemNaturally does
        if (item != null){
            if (!item.getType().equals(Material.AIR) && !item.getType().equals(Material.CAVE_AIR) && !item.getType().equals(Material.VOID_AIR)) {
                World world = location.getWorld();
                world.dropItemNaturally(location,item);
            }
        }
    }

    public void damageTool() { //Overspecified because I'm lazy
        damageTool(1);
    }

    public void damageTool(int damage) {
        if (itemInHand.getItemMeta().isUnbreakable()) {
            return;
        }
        ItemMeta toolMeta = itemInHand.getItemMeta();
        if (toolMeta instanceof Damageable) {
            ((Damageable) toolMeta).setDamage(((Damageable) toolMeta).getDamage()+damage);
            itemInHand.setItemMeta(toolMeta);
            if (((Damageable) toolMeta).getDamage() > itemInHand.getType().getMaxDurability()) {
                itemInHand.setAmount(0);
                p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, 1);
            }
        }
    }
}
