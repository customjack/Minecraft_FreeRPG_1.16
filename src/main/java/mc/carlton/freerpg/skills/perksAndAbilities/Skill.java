package mc.carlton.freerpg.skills.perksAndAbilities;

import java.util.Map;
import java.util.Random;
import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.core.info.player.AbilityTimers;
import mc.carlton.freerpg.core.info.player.AbilityTracker;
import mc.carlton.freerpg.core.info.player.ChangeStats;
import mc.carlton.freerpg.core.info.player.PlayerStats;
import mc.carlton.freerpg.utils.game.ActionBarMessages;
import mc.carlton.freerpg.utils.game.LanguageSelector;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;


public class Skill {

  protected Player p;
  protected String pName;
  protected ItemStack itemInHand;
  Plugin plugin;
  Map<String, Integer> expMap;
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
    this.pStatClass = new PlayerStats(p);
    this.actionMessage = new ActionBarMessages(p);
    this.lang = new LanguageSelector(p);
    this.plugin = FreeRPG.getPlugin(FreeRPG.class);
  }

  public void dropItemNaturally(Location location,
      ItemStack item) { //Won't try to drop air, like world.dropItemNaturally does
    if (item != null) {
      if (!item.getType().equals(Material.AIR) && !item.getType().equals(Material.CAVE_AIR)
          && !item.getType().equals(Material.VOID_AIR)) {
        World world = location.getWorld();
        world.dropItemNaturally(location, item);
      }
    }
  }

  public void damageTool() { //Overspecified because I'm lazy
    damageTool(1, 1.0);
  }

  public void damageTool(double modifier) {
    damageTool(1, modifier);
  }

  public void damageTool(int damage, double modifier) {
    if (itemInHand.getItemMeta().isUnbreakable()) {
      return;
    }
    ItemMeta toolMeta = itemInHand.getItemMeta();

    //Unnbreaking checks
    int unbreakingLevel = 0;
    if (toolMeta.hasEnchant(Enchantment.DURABILITY)) {
      unbreakingLevel = toolMeta.getEnchantLevel(Enchantment.DURABILITY);
    }
    if (unbreakingLevel > 0) {
      double chanceToSaveDurability = 1.0 - (1.0 / (unbreakingLevel + 1));
      for (int i = 0; i < damage; i++) { //Roll an unbreaking check for each damage point dealt
        Random rand = new Random();
        if (rand.nextDouble() < chanceToSaveDurability) { //Reduce damage dealt
          damage = damage - 1;
        }
      }
    }

    //Damage Modifier
    if (modifier
        != 1.0) { //Randomly sets damage so the expected value of the durability taken = damage*modifier
      double damageDouble = damage * modifier;
      damage = (int) Math.floor(damageDouble); //Round damage down
      double excessDamage = damageDouble - damage;
      Random rand = new Random();
      if (rand.nextDouble()
          < excessDamage) { //Round up if a random number is less than the excessDamage
        damage += 1;
      }
    }

    if (toolMeta instanceof Damageable) {
      ((Damageable) toolMeta).setDamage(((Damageable) toolMeta).getDamage() + damage);
      itemInHand.setItemMeta(toolMeta);
      if (((Damageable) toolMeta).getDamage() > itemInHand.getType().getMaxDurability()) {
        itemInHand.setAmount(0);
        p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, 1);
      }
    }
  }
}
