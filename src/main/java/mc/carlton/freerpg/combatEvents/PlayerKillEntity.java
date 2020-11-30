package mc.carlton.freerpg.combatEvents;

import mc.carlton.freerpg.gameTools.EntityPickedUpItemStorage;
import mc.carlton.freerpg.globalVariables.ItemGroups;
import mc.carlton.freerpg.perksAndAbilities.*;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlayerKillEntity implements Listener {
    @EventHandler
    void onEntityDie(EntityDeathEvent e){
        LivingEntity entity = e.getEntity();
        Player p = e.getEntity().getKiller();

        if (p != null) {
            if (p.getGameMode() == GameMode.CREATIVE) {
                return;
            }
            ItemGroups itemGroups = new ItemGroups();
            World world = p.getWorld();
            List<ItemStack> drops = e.getDrops();

            //Farming
            Farming farmingClass = new Farming(p);
            farmingClass.animalDoubleDrops(entity,world,drops);
            farmingClass.killFarmAnimalEXP(entity);

            //Swordsmanship
            if (itemGroups.getSwords().contains(p.getInventory().getItemInMainHand().getType())) {
                Swordsmanship swordsmanshipClass = new Swordsmanship(p);
                swordsmanshipClass.killBuffs(e.getEntity());
                swordsmanshipClass.thirstForBlood(e.getEntity());
                swordsmanshipClass.giveKillEXP(entity);
            }

            //Defense
            Defense defenseClass = new Defense(p);
            defenseClass.doubleDrops(entity,drops,world);
            defenseClass.healer();
            defenseClass.giveKillEXP(entity);

            //Axe Mastery
            if (itemGroups.getAxes().contains(p.getInventory().getItemInMainHand().getType())) {
                AxeMastery axeMasteryClass = new AxeMastery(p);
                axeMasteryClass.revitalized();
                axeMasteryClass.warriorBlood();
                axeMasteryClass.giveKillEXP(entity);
            }

            //Fishing
            Fishing fishingClass = new Fishing(p);
            fishingClass.killFishEXP(entity);

            //Global (Souls)
            Global globalClass = new Global(p);
            globalClass.gainSoul(entity);

        }

        EntityPickedUpItemStorage entityPickedUpItemStorage = new EntityPickedUpItemStorage();
        entityPickedUpItemStorage.removeEntity(entity);



    }
}
