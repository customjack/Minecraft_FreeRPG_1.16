package mc.carlton.freerpg.combatEvents;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.FireworkShotByPlayerTracker;
import mc.carlton.freerpg.globalVariables.ItemGroups;
import mc.carlton.freerpg.perksAndAbilities.*;
import mc.carlton.freerpg.playerInfo.*;
import mc.carlton.freerpg.serverConfig.ConfigLoad;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class EntityHitEntity implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)

    void onEntityHit(EntityDamageByEntityEvent e) {
        Random rand = new Random();
        Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);

        if (e.isCancelled()) {
            return;
        }

        if (e.getEntity().isInvulnerable()) { //Godmode check
            return;
        }

        //removes PvP effects if PvP is disabled
        if (e.getEntity() instanceof Player) {
            ConfigLoad loadConfig = new ConfigLoad();
            if (e.getDamager() instanceof Player) {
                if (!loadConfig.isAllowPvP()) {
                    return;
                }
            }
            else if (e.getDamager() instanceof Projectile) {
                ProjectileSource shooter = ((Projectile) e.getDamager()).getShooter();
                if (shooter instanceof Player) {
                    if (!loadConfig.isAllowPvP()) {
                        return;
                    } else if (shooter.equals(e.getEntity())) {
                        return; //Shooter shot himself
                    }
                }
            }
        }
        //removes hurting animals effects if hurting animals is disabled
        else if (e.getEntity() instanceof Animals) {
            ConfigLoad loadConfig = new ConfigLoad();
            if (e.getDamager() instanceof Player) {
                if (!loadConfig.isAllowHurtAnimals()) {
                    return;
                }
            }
            else if (e.getDamager() instanceof Projectile) {
                if (((Projectile) e.getDamager()).getShooter() instanceof Player) {
                    if (!loadConfig.isAllowHurtAnimals()) {
                        return;
                    }
                }
            }
        }

        if (e.getDamager() instanceof Player) {
            ItemGroups itemGroups = new ItemGroups();
            List<Material> shovels = itemGroups.getShovels();
            List<Material> swords = itemGroups.getSwords();
            List<Material> axes = itemGroups.getAxes();


            Player p = (Player) e.getDamager();
            if (p.getGameMode() == GameMode.CREATIVE) {
                return;
            }
            PlayerStats pStatClass = new PlayerStats(p);
            Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
            AbilityTracker abilities = new AbilityTracker(p);
            Integer[] pAbilities = abilities.getPlayerAbilities();



            //Digging
            if (shovels.contains(p.getInventory().getItemInMainHand().getType())) {
                ConfigLoad configLoad = new ConfigLoad();
                if (!configLoad.getAllowedSkillsMap().get("digging")) {
                    return;
                }
                int shovelKnightLevel = (int) pStat.get("digging").get(12);
                double multiplier = Math.min(2.0,1.0+shovelKnightLevel);
                e.setDamage(e.getDamage() * multiplier);
            }

            //swordsmanship
            else if (swords.contains(p.getInventory().getItemInMainHand().getType())) {
                ConfigLoad configLoad = new ConfigLoad();
                if (!configLoad.getAllowedSkillsMap().get("swordsmanship")) {
                    return;
                }
                Swordsmanship swordsmanshipClass = new Swordsmanship(p);
                if (pAbilities[7] > -1) {
                    swordsmanshipClass.enableAbility();
                }
                double damage = e.getDamage();
                if ((int)pStat.get("swordsmanship").get(13) > 0) {
                    damage += 2;
                    e.setDamage(damage);
                }
                Entity damagedEntity = e.getEntity();
                swordsmanshipClass.doubleHit(damagedEntity,damage);
                swordsmanshipClass.giveHitEXP(e.getFinalDamage(),damagedEntity);
            }

            //Defense
            else if (p.getInventory().getItemInMainHand().getType() == Material.AIR) {
                Defense defenseClass = new Defense(p);
                if (pAbilities[8] > -1) {
                    defenseClass.enableAbility();
                }
            }

            else if (axes.contains(p.getInventory().getItemInMainHand().getType())) {
                ConfigLoad configLoad = new ConfigLoad();
                if (!configLoad.getAllowedSkillsMap().get("axeMastery")) {
                    return;
                }
                AxeMastery axeMasteryClass = new AxeMastery(p);
                double damage = e.getDamage();
                if ((int)pStat.get("axeMastery").get(13) > 0) {
                    damage += 2;
                    e.setDamage(damage);
                }
                if (pAbilities[9] > -1) {
                    axeMasteryClass.enableAbility();
                    axeMasteryClass.greaterAxe(e.getEntity(),p.getWorld(),e.getFinalDamage());
                }
                else if (pAbilities[9] == -2) {
                    axeMasteryClass.greaterAxe(e.getEntity(),p.getWorld(),e.getFinalDamage());
                }
                double multiplier = axeMasteryClass.divineCritical();
                e.setDamage(e.getDamage()*multiplier);
                axeMasteryClass.holyAxe(e.getEntity(),p.getWorld(),e.getFinalDamage());
                axeMasteryClass.giveHitEXP(e.getFinalDamage(),e.getEntity());

            }

        }
        //Arrow of Light
        else if (e.getDamager() instanceof Arrow || e.getDamager() instanceof  SpectralArrow) {
            ProjectileSource shooter;
            if (e.getDamager() instanceof SpectralArrow) {
                shooter = ((SpectralArrow) e.getDamager()).getShooter();
            } else {
                shooter = ((Arrow) e.getDamager()).getShooter();
            }
            if (shooter instanceof Player) {
                ConfigLoad configLoad = new ConfigLoad();
                if (!configLoad.getAllowedSkillsMap().get("archery")) {
                    return;
                }
                Player p = (Player) shooter;
                PlayerStats pStatClass = new PlayerStats(p);
                Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
                Archery archeryClass = new Archery(p);
                archeryClass.explosiveArrows(e.getDamager(),e.getEntity().getLocation());
                Material arrowType = archeryClass.getArrowType();
                if (arrowType == Material.SPECTRAL_ARROW) {
                    int arrowOfLightLevel = (int) pStat.get("archery").get(10);
                    double multiplier = Math.min(arrowOfLightLevel * 0.05 + 1.0, 2.0);
                    e.setDamage(Math.min(e.getDamage() * multiplier, 32));
                }
                archeryClass.giveHitEXP(e.getFinalDamage(),e.getEntity());
                if (e.getEntity() instanceof LivingEntity) {
                    if (e.getFinalDamage() > ((LivingEntity) e.getEntity()).getHealth()) {
                        archeryClass.giveKillEXP(e.getEntity());
                    }
                }
            }
        }
        //Crossbow Strike
        else if (e.getDamager() instanceof Firework) {
            ConfigLoad configLoad = new ConfigLoad();
            if (!configLoad.getAllowedSkillsMap().get("archery")) {
                return;
            }
            Entity firework = e.getDamager();
            FireworkShotByPlayerTracker fireworkTracker = new FireworkShotByPlayerTracker();
            Player p = fireworkTracker.getPlayer(firework);
            fireworkTracker.removeFireWork(firework);
            if (p != null) {
                Archery archeryClass = new Archery(p);
                PlayerStats pStatClass = new PlayerStats(p);
                Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
                if ((int) pStat.get("archery").get(13) > 0) {
                    e.setDamage(Math.min(e.getDamage() * 2, 32));
                    archeryClass.giveHitEXP(e.getFinalDamage(),e.getEntity());
                }
                if (e.getEntity() instanceof LivingEntity) {
                    if (e.getFinalDamage() > ((LivingEntity) e.getEntity()).getHealth()) {
                        archeryClass.giveKillEXP(e.getEntity());
                    }
                }
            }
        }
        //Sharp Teeth and Keep Away
        else if (e.getDamager() instanceof Entity) {
            ConfigLoad configLoad = new ConfigLoad();
            if (!configLoad.getAllowedSkillsMap().get("beastMastery")) {
                return;
            }
            Entity wolf = e.getDamager();
            if (wolf.getType() == EntityType.WOLF) {
                Tameable dog = (Tameable) wolf;
                if (dog.isTamed()) {
                    if (!(dog.getOwner() instanceof Player)) { //Player is offline or something like that.
                        return;
                    }
                    Player p = (Player) dog.getOwner();
                    BeastMastery beastMastery = new BeastMastery(p);
                    PlayerStats pStatClass = new PlayerStats(p);
                    Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();

                    double crit = 1.0;
                    int critLevel = (int) pStat.get("beastMastery").get(5);
                    double critChance = critLevel * 0.00025;
                    if (critChance > rand.nextDouble()) {
                        crit = 1.5;
                    }

                    int sharpTeethLevel = (int) pStat.get("beastMastery").get(8);
                    double damageMultiplier = sharpTeethLevel * 0.1 + 1;
                    e.setDamage(e.getDamage() * crit * damageMultiplier);

                    Entity enemy = e.getEntity();
                    int keepAwayLevel = (int) pStat.get("beastMastery").get(10);
                    double knockBackChance = keepAwayLevel * 0.05;
                    if (knockBackChance > rand.nextDouble()) {
                        Vector knockback = enemy.getVelocity();
                        double multiplier;
                        if (knockback.length() > 0.1) {
                            double newKnockback = Math.min(knockback.length() * 5, 100.0);
                            multiplier = newKnockback / knockback.length();
                        }
                        else {
                            multiplier = 5.0;
                        }
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                enemy.setVelocity(knockback.multiply(multiplier).setY(0.4));
                            }
                        }.runTaskLater(plugin, 1);
                    }
                    LivingEntity livingEnemy = (LivingEntity) enemy;
                    if (e.getFinalDamage() > livingEnemy.getHealth()) {
                        double heartsHealed = (int) pStat.get("beastMastery").get(9);
                        LivingEntity livingDog = (LivingEntity) dog;
                        double maxHealth = ((Attributable) dog).getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                        livingDog.setHealth(Math.min(livingDog.getHealth() + heartsHealed, maxHealth));
                        beastMastery.dogKillEntity(enemy);
                    }
                    beastMastery.giveHitEXP(e.getFinalDamage(),enemy);

                }
            }
        }
        //Wolf take damage EXP
        else if (e.getEntity() instanceof Entity) {
            ConfigLoad configLoad = new ConfigLoad();
            if (!configLoad.getAllowedSkillsMap().get("beastMastery")) {
                return;
            }
            if (!(e.getDamager() instanceof  Player)) {
                Entity wolf = e.getEntity();
                if (wolf.getType() == EntityType.WOLF) {
                    Tameable dog = (Tameable) wolf;
                    if (dog.isTamed()) {
                        if (!(dog.getOwner() instanceof Player)) { //Player is offline or something like that.
                            return;
                        }
                        Player p = (Player) dog.getOwner();
                        ChangeStats increaseStats = new ChangeStats(p);
                        increaseStats.changeEXP("beastMastery", (int) Math.round(e.getFinalDamage() * 3) * 10);
                    }
                }
            }
        }

        //Getting hit (defense)
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (e.getDamage() != 0 && !e.getCause().equals(EntityDamageEvent.DamageCause.HOT_FLOOR) &&
                    !e.getCause().equals(EntityDamageEvent.DamageCause.STARVATION) &&
                    !e.getCause().equals(EntityDamageEvent.DamageCause.VOID) &&
                    !e.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION) &&
                    !e.getCause().equals(EntityDamageEvent.DamageCause.CUSTOM) &&
                    !e.getCause().equals(EntityDamageEvent.DamageCause.CONTACT)
                ) {
                Agility agilityClass = new Agility(p);
                boolean dodge = agilityClass.dodge(e.getFinalDamage());
                if (dodge) {
                    e.setCancelled(true);
                }

                Defense defenseClass = new Defense(p);
                double multiplier = defenseClass.hardBody();
                e.setDamage(e.getDamage() * multiplier);
                defenseClass.reactions(e.getFinalDamage());
                defenseClass.giveHitEXP(e.getDamage(),e.getDamager());
            }
        }

    }
}
