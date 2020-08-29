package mc.carlton.freerpg.miscEvents;

import mc.carlton.freerpg.gameTools.ArrowTypes;
import mc.carlton.freerpg.playerAndServerInfo.ConfigLoad;
import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;

import java.util.ArrayList;
import java.util.Map;

public class PlayerPrepareCrafting implements Listener {

    @EventHandler
    public void craftEvent(PrepareItemCraftEvent event) {
        ItemStack[] contents = event.getInventory().getContents();
        ItemStack firstInContents = contents[0];
        Player p = (Player) event.getView().getPlayer();
        PlayerStats pStatClass = new PlayerStats(p);
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();

        if((firstInContents.getType()== Material.TNT) && (firstInContents.getAmount() == 1)) {
            ConfigLoad configLoad = new ConfigLoad();
            if (!configLoad.getAllowedSkillsMap().get("mining")) {
                return;
            }
            int moreBombsLevel = (int)pStat.get("mining").get(8);
            if (moreBombsLevel > 0) {
                firstInContents.setAmount(Math.min(6,moreBombsLevel+1));
            }
        }
        else if((firstInContents.getType()== Material.ARROW) && (firstInContents.getAmount() < 64)) {
            ConfigLoad configLoad = new ConfigLoad();
            if (!configLoad.getAllowedSkillsMap().get("archery")) {
                return;
            }
            if ((int)pStat.get("archery").get(7) > 0) {
                firstInContents.setAmount(4+(int)pStat.get("archery").get(7));
            }
        }
        else if(firstInContents.getType() == Material.TIPPED_ARROW && contents[5].getType() == Material.POTION) {
            ConfigLoad configLoad = new ConfigLoad();
            if (!configLoad.getAllowedSkillsMap().get("archery")) {
                return;
            }
            PotionMeta potionMeta = (PotionMeta) contents[5].getItemMeta();
            ArrowTypes effectArrows = new ArrowTypes();
            ItemStack arrow = new ItemStack(Material.ARROW,1);
            PotionData pData = potionMeta.getBasePotionData();
            switch (pData.getType()) {
                case LUCK:
                    arrow = effectArrows.getArrow("luck");
                    break;
                case JUMP:
                    arrow = effectArrows.getArrow("leaping");
                    if (pData.isExtended()) {
                        arrow = effectArrows.getArrow("long_leaping");
                    }
                    if (pData.isUpgraded()) {
                        arrow = effectArrows.getArrow("strong_leaping");
                    }
                    break;
                case REGEN:
                    arrow = effectArrows.getArrow("regeneration");
                    if (pData.isExtended()) {
                        arrow = effectArrows.getArrow("long_regeneration");
                    }
                    if (pData.isUpgraded()) {
                        arrow = effectArrows.getArrow("strong_regeneration");
                    }
                    break;
                case SPEED:
                    arrow = effectArrows.getArrow("swiftness");
                    if (pData.isExtended()) {
                        arrow = effectArrows.getArrow("long_swiftness");
                    }
                    if (pData.isUpgraded()) {
                        arrow = effectArrows.getArrow("strong_swiftness");
                    }
                    break;
                case POISON:
                    arrow = effectArrows.getArrow("poison");
                    if (pData.isExtended()) {
                        arrow = effectArrows.getArrow("long_poison");
                    }
                    if (pData.isUpgraded()) {
                        arrow = effectArrows.getArrow("strong_poison");
                    }
                    break;
                case SLOWNESS:
                    arrow = effectArrows.getArrow("slowness");
                    if (pData.isExtended()) {
                        arrow = effectArrows.getArrow("long_slowness");
                    }
                    if (pData.isUpgraded()) {
                        arrow = effectArrows.getArrow("strong_slowness");
                    }
                    break;
                case STRENGTH:
                    arrow = effectArrows.getArrow("strength");
                    if (pData.isExtended()) {
                        arrow = effectArrows.getArrow("long_strength");
                    }
                    if (pData.isUpgraded()) {
                        arrow = effectArrows.getArrow("strong_strength");
                    }
                    break;
                case WEAKNESS:
                    arrow = effectArrows.getArrow("weakness");
                    if (pData.isExtended()) {
                        arrow = effectArrows.getArrow("long_weakness");
                    }
                    if (pData.isUpgraded()) {
                        arrow = effectArrows.getArrow("strong_weakness");
                    }
                    break;
                case INSTANT_HEAL:
                    arrow = effectArrows.getArrow("healing");
                    if (pData.isExtended()) {
                        arrow = effectArrows.getArrow("long_healing");
                    }
                    if (pData.isUpgraded()) {
                        arrow = effectArrows.getArrow("strong_healing");
                    }
                    break;
                case INVISIBILITY:
                    arrow = effectArrows.getArrow("invisibility");
                    if (pData.isExtended()) {
                        arrow = effectArrows.getArrow("long_invisibility");
                    }
                    if (pData.isUpgraded()) {
                        arrow = effectArrows.getArrow("strong_invisibility");
                    }
                    break;
                case NIGHT_VISION:
                    arrow = effectArrows.getArrow("night_vision");
                    if (pData.isExtended()) {
                        arrow = effectArrows.getArrow("long_night_vision");
                    }
                    if (pData.isUpgraded()) {
                        arrow = effectArrows.getArrow("strong_night_vision");
                    }
                    break;
                case SLOW_FALLING:
                    arrow = effectArrows.getArrow("slow_falling");
                    if (pData.isExtended()) {
                        arrow = effectArrows.getArrow("long_slow_falling");
                    }
                    if (pData.isUpgraded()) {
                        arrow = effectArrows.getArrow("strong_slow_falling");
                    }
                    break;
                case TURTLE_MASTER:
                    arrow = effectArrows.getArrow("turtle_master");
                    if (pData.isExtended()) {
                        arrow = effectArrows.getArrow("long_turtle_master");
                    }
                    if (pData.isUpgraded()) {
                        arrow = effectArrows.getArrow("strong_turtle_master");
                    }
                    break;
                case INSTANT_DAMAGE:
                    arrow = effectArrows.getArrow("harming");
                    if (pData.isExtended()) {
                        arrow = effectArrows.getArrow("long_harming");
                    }
                    if (pData.isUpgraded()) {
                        arrow = effectArrows.getArrow("strong_harming");
                    }
                    break;
                case FIRE_RESISTANCE:
                    arrow = effectArrows.getArrow("fire_resistance");
                    if (pData.isExtended()) {
                        arrow = effectArrows.getArrow("long_fire_resistance");
                    }
                    if (pData.isUpgraded()) {
                        arrow = effectArrows.getArrow("strong_fire_resistance");
                    }
                    break;
                case WATER_BREATHING:
                    arrow = effectArrows.getArrow("water_breathing");
                    if (pData.isExtended()) {
                        arrow = effectArrows.getArrow("long_water_breathing");
                    }
                    if (pData.isUpgraded()) {
                        arrow = effectArrows.getArrow("strong_water_breathing");
                    }
                    break;
                default:
                    break;
            }
            ItemMeta itemMeta = arrow.getItemMeta();
            firstInContents.setItemMeta(itemMeta);
        }


    }
}
