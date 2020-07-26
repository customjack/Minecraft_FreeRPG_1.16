package mc.carlton.freerpg.clickEvents;

import mc.carlton.freerpg.perksAndAbilities.*;
import mc.carlton.freerpg.playerAndServerInfo.AbilityTracker;
import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PlayerLeftClick implements Listener {

    @EventHandler
    void onLeftClick(PlayerInteractEvent e) {
        Action a = e.getAction();

        if ((a.equals(Action.LEFT_CLICK_AIR)) || (a.equals(Action.LEFT_CLICK_BLOCK))) {
            Player p = e.getPlayer();
            if (p.getGameMode() == GameMode.CREATIVE) {
                return;
            }
            World world = p.getWorld();

            PlayerStats pStatClass = new PlayerStats(p);
            Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
            AbilityTracker abilities = new AbilityTracker(p);
            Integer[] pAbilities = abilities.getPlayerAbilities();

            Material[] leftClickItems0 = {Material.IRON_SWORD,Material.DIAMOND_SWORD,Material.GOLDEN_SWORD,Material.STONE_SWORD,Material.WOODEN_SWORD,
                                          Material.IRON_PICKAXE,Material.DIAMOND_PICKAXE,Material.STONE_PICKAXE,Material.GOLDEN_PICKAXE,Material.WOODEN_PICKAXE,
                                          Material.IRON_SHOVEL,Material.DIAMOND_SHOVEL,Material.GOLDEN_SHOVEL,Material.STONE_SHOVEL,Material.WOODEN_SHOVEL,
                                          Material.IRON_AXE,Material.DIAMOND_AXE,Material.GOLDEN_AXE,Material.STONE_AXE,Material.WOODEN_AXE,
                                          Material.BOW,Material.CROSSBOW,Material.NETHERITE_SWORD,Material.NETHERITE_SHOVEL,Material.NETHERITE_AXE,Material.NETHERITE_PICKAXE};
            List<Material> leftClickItems = Arrays.asList(leftClickItems0);

            if (p.getInventory().getItemInMainHand().getType() == Material.FISHING_ROD && a.equals(Action.LEFT_CLICK_BLOCK)) {
                Fishing fishingClass = new Fishing(p);
                fishingClass.initiateAbility();
            }

            else if (p.getInventory().getItemInMainHand().getType() == Material.BOW) {
                Archery archeryClass = new Archery(p);
                archeryClass.initiateAbility();
            }
            else if (p.getInventory().getItemInMainHand().getType() == Material.CROSSBOW) {
                Archery archeryClass = new Archery(p);
                if ((int) pStat.get("archery").get(12) > 0) {
                    archeryClass.initiateAbility();
                }
            }
            else if (p.getVehicle() != null) {
                if (!leftClickItems.contains(p.getInventory().getItemInMainHand().getType()) && p.getVehicle().getType() == EntityType.HORSE) {
                    if (pAbilities[6] > -1) {
                        BeastMastery beastMasteryClass = new BeastMastery(p);
                        beastMasteryClass.enableAbility();
                    }
                }
            }


            if (a.equals(Action.LEFT_CLICK_BLOCK)) {
                Block clickedBlock = e.getClickedBlock();
                Woodcutting woodcuttingClass = new Woodcutting(p);
                woodcuttingClass.leafBlower(clickedBlock,world);

                BlockFace blockface = e.getBlockFace();
                Digging diggingClass = new Digging(p);
                diggingClass.storeBlockFace(blockface);
            }
        }
    }
}
