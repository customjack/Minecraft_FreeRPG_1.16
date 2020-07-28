package mc.carlton.freerpg.perksAndAbilities;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.playerAndServerInfo.ChangeStats;
import mc.carlton.freerpg.playerAndServerInfo.PlayerStats;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Global {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    private Player p;
    private String pName;
    private ItemStack itemInHand;
    private static Map<Player, ArrayList<ItemStack>> playerSavedDrops = new HashMap<>();

    ChangeStats increaseStats; //Changing Stats

    PlayerStats pStatClass;
    //GET PLAYER STATS LIKE THIS:        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData(p);

    Random rand = new Random(); //Random class Import
    Material[] valuableItems0 = {Material.IRON_ORE,Material.GOLD_ORE,Material.DIAMOND_ORE,Material.EMERALD_ORE,Material.REDSTONE_ORE,Material.LAPIS_ORE,
                                  Material.IRON_BLOCK,Material.GOLD_BLOCK,Material.DIAMOND_BLOCK,Material.EMERALD_BLOCK,Material.REDSTONE_BLOCK,Material.LAPIS_BLOCK,
                                  Material.SLIME_BLOCK,Material.SPONGE,Material.NETHER_QUARTZ_ORE,Material.NETHER_WART_BLOCK,Material.DRAGON_EGG,Material.SHULKER_BOX,
                                  Material.ENCHANTING_TABLE,Material.ANVIL,Material.BEACON,Material.BREWING_STAND,Material.CAKE,Material.JUKEBOX,
                                  Material.TNT,Material.CREEPER_HEAD,Material.DRAGON_HEAD,Material.PLAYER_HEAD,Material.ZOMBIE_HEAD,Material.SKELETON_SKULL,Material.WITHER_SKELETON_SKULL,
                                  Material.ENDER_EYE,Material.ENDER_PEARL,Material.FIREWORK_ROCKET,Material.FIRE_CHARGE,Material.POTION,Material.SPLASH_POTION,Material.LINGERING_POTION,
                                  Material.NETHER_WART,Material.REDSTONE,Material.TRIDENT,Material.DIAMOND_AXE,Material.DIAMOND_BOOTS,Material.DIAMOND_CHESTPLATE,
                                  Material.DIAMOND_HELMET,Material.DIAMOND_HOE,Material.DIAMOND_LEGGINGS,Material.DIAMOND_PICKAXE,Material.DIAMOND_SHOVEL,Material.DIAMOND_SWORD,
                                  Material.ELYTRA,Material.ENCHANTED_BOOK,Material.ENCHANTED_GOLDEN_APPLE,Material.GOLDEN_APPLE,Material.IRON_INGOT,
                                  Material.MUSIC_DISC_11, Material.MUSIC_DISC_13, Material.MUSIC_DISC_BLOCKS, Material.MUSIC_DISC_CAT,
                                  Material.MUSIC_DISC_CHIRP, Material.MUSIC_DISC_FAR, Material.MUSIC_DISC_MALL, Material.MUSIC_DISC_MELLOHI,
                                  Material.MUSIC_DISC_STAL, Material.MUSIC_DISC_STRAD, Material.MUSIC_DISC_WAIT, Material.MUSIC_DISC_WARD,
                                  Material.NAME_TAG,Material.TIPPED_ARROW,Material.TOTEM_OF_UNDYING,Material.SPECTRAL_ARROW,Material.DIAMOND,Material.GOLD_INGOT,
                                  Material.HEART_OF_THE_SEA,Material.DRAGON_BREATH,Material.EMERALD,Material.NAUTILUS_SHELL,Material.NETHER_STAR,Material.SLIME_BALL,
                                  Material.RABBIT_FOOT,
                                  Material.NETHERITE_SWORD,Material.NETHERITE_HOE,Material.NETHERITE_SHOVEL,Material.NETHERITE_AXE,Material.NETHERITE_PICKAXE,Material.NETHER_GOLD_ORE,
                                  Material.NETHERITE_BLOCK,Material.NETHERITE_BOOTS,Material.NETHERITE_CHESTPLATE,Material.NETHERITE_HELMET,Material.NETHERITE_INGOT,Material.NETHERITE_LEGGINGS,
                                  Material.NETHERITE_SCRAP,Material.ANCIENT_DEBRIS};
    List<Material> valuableItems = Arrays.asList(valuableItems0);



    public Global(Player p) {
        this.p = p;
        this.pName = p.getDisplayName();
        this.itemInHand = p.getInventory().getItemInMainHand();
        this.increaseStats = new ChangeStats(p);
        this.pStatClass = new PlayerStats(p);
    }

    public double expBoost(String skillName) {
        double boost = 1.0;
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        String[] gatherer0 = {"digging","woodcutting","mining","farming","fishing"};
        String[] scholar0 =  {"repair","agility","alchemy","smelting","enchanting"};
        String[] fighter0 = {"archery","beastMastery","swordsmanship","defense","axeMastery"};
        List<String> gatherer = Arrays.asList(gatherer0);
        List<String> scholar = Arrays.asList(scholar0);
        List<String> fighter = Arrays.asList(fighter0);
        if (gatherer.contains(skillName) && (int) pStat.get("global").get(2) > 0 ) {
            boost = 1.2;
        }
        else if (scholar.contains(skillName) && (int) pStat.get("global").get(3) > 0 ) {
            boost = 1.2;
        }
        else if (fighter.contains(skillName) && (int) pStat.get("global").get(4) > 0 ) {
            boost = 1.2;
        }

        return boost;
    }

    public void skillTokenBoost(int type) {
        String[] gatherer0 = {"digging","woodcutting","mining","farming","fishing"};
        String[] scholar0 =  {"repair","agility","alchemy","smelting","enchanting"};
        String[] fighter0 = {"archery","beastMastery","swordsmanship","defense","axeMastery"};
        List<String> gatherer = Arrays.asList(gatherer0);
        List<String> scholar = Arrays.asList(scholar0);
        List<String> fighter = Arrays.asList(fighter0);
        switch (type) {
            case 5:
                for (String skillName : gatherer) {
                    increaseStats.increaseTokens(skillName,"skill",1);
                }
                break;
            case 6:
                for (String skillName : scholar) {
                    increaseStats.increaseTokens(skillName,"skill",1);
                }
                break;
            case 7:
                for (String skillName : fighter) {
                    increaseStats.increaseTokens(skillName,"skill",1);
                }
                break;
            default:
                break;
        }
    }

    public void passiveTokenBoost() { //Old skill_3b perk
        String[] abilities0 = {"digging", "woodcutting", "mining", "farming", "fishing", "archery", "beastMastery", "swordsmanship", "defense", "axeMastery"};
        String[] nonAbilities0 = {"repair", "agility", "alchemy", "smelting", "enchanting"};
        List<String> abilities = Arrays.asList(abilities0);
        List<String> nonAbilities = Arrays.asList(nonAbilities0);
        for (String skillName : abilities) {
            increaseStats.increaseTokens(skillName, "passive", 50);
        }
        for (String skillName : nonAbilities) {
            increaseStats.changeStat(skillName, 4, 50);
        }
    }

    public void gainSoul(Entity entity) {
        EntityType[] hostileMobs0 = {EntityType.SPIDER,EntityType.CAVE_SPIDER,EntityType.ENDERMAN,EntityType.ZOMBIFIED_PIGLIN,
                EntityType.BLAZE,EntityType.CREEPER,EntityType.DROWNED,EntityType.ELDER_GUARDIAN,
                EntityType.ENDERMITE,EntityType.EVOKER,EntityType.GHAST,EntityType.GUARDIAN,
                EntityType.HUSK,EntityType.MAGMA_CUBE,EntityType.PHANTOM,EntityType.PILLAGER,
                EntityType.RAVAGER,EntityType.SHULKER,EntityType.SKELETON,EntityType.SLIME,
                EntityType.STRAY,EntityType.VEX,EntityType.VINDICATOR,EntityType.WITCH,
                EntityType.WITHER_SKELETON,EntityType.ZOMBIE,EntityType.ZOMBIE_VILLAGER,EntityType.WITHER,EntityType.ENDER_DRAGON};
        List<EntityType> hostileMobs = Arrays.asList(hostileMobs0);
        EntityType entityType = entity.getType();
        if (hostileMobs.contains(entityType)) {
            int amountGained = 1;
            switch (entityType) {
                case WITHER:
                    amountGained = 100;
                    break;
                case ENDER_DRAGON:
                    amountGained = 200;
                    break;
                default:
                    break;
            }
            UUID uuid = p.getUniqueId();
            PlayerStats pStatClass = new PlayerStats(p);
            Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
            Map<String, ArrayList<Number>> pStatAll = statAll.get(uuid);
            int soulHarvestLevel = (int) pStatAll.get("global").get(9);
            if (soulHarvestLevel > 0) {
                int souls = (int) pStatAll.get("global").get(20);
                pStatAll.get("global").set(20, souls + amountGained);
                statAll.put(uuid, pStatAll);
                pStatClass.setData(statAll);
            }
        }
    }

    public void loseSouls(int amountLost) {
        UUID uuid = p.getUniqueId();
        PlayerStats pStatClass = new PlayerStats(p);
        Map<UUID, Map<String, ArrayList<Number>>> statAll = pStatClass.getData();
        Map<String, ArrayList<Number>> pStatAll = statAll.get(uuid);
        int souls =  (int) pStatAll.get("global").get(20);
        pStatAll.get("global").set(20,souls-amountLost);
        statAll.put(uuid, pStatAll);
        pStatClass.setData(statAll);
    }

    public void betterResurrectionDeath(List<ItemStack> drops) {
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        if ((int) pStat.get("global").get(8) > 0) {
            ArrayList<ItemStack> savedDrops = new ArrayList<>();
            for (ItemStack drop : drops) {
                if (drop.getEnchantments().size() != 0 || valuableItems.contains(drop.getType())) {
                    double randomNum = rand.nextDouble();
                    int initialAmount = drop.getAmount();
                    if (0.5 < randomNum) {
                        if (initialAmount == 1) {
                            ItemStack dropCopy = drop.clone();
                            savedDrops.add(dropCopy);
                            drop.setAmount(0);
                        }
                        else if (initialAmount > 1) {
                            int savedAmount = (int) Math.round(initialAmount*randomNum);
                            ItemStack dropCopy = drop.clone();
                            drop.setAmount(initialAmount-savedAmount);
                            dropCopy.setAmount(savedAmount);
                            savedDrops.add(dropCopy);
                        }
                    }
                }
            }
            playerSavedDrops.put(p,savedDrops);
        }
    }

    public void betterResurrectionRespawn() {
        Map<String, ArrayList<Number>> pStat = pStatClass.getPlayerData();
        if ((int) pStat.get("global").get(8) > 0 && playerSavedDrops.containsKey(p)) {
            ArrayList<ItemStack> savedDrops = playerSavedDrops.get(p);
            for (ItemStack savedDrop : savedDrops) {
                p.getInventory().addItem(savedDrop);
            }
            playerSavedDrops.remove(p);
        }
    }

    public void avatar(){
        PotionEffectType[] positiveEffects0  = {PotionEffectType.DOLPHINS_GRACE,PotionEffectType.LUCK,PotionEffectType.INVISIBILITY,PotionEffectType.NIGHT_VISION,
                                               PotionEffectType.FIRE_RESISTANCE,PotionEffectType.WATER_BREATHING,PotionEffectType.SPEED,PotionEffectType.JUMP,
                                               PotionEffectType.ABSORPTION,PotionEffectType.CONDUIT_POWER,PotionEffectType.DAMAGE_RESISTANCE,PotionEffectType.FAST_DIGGING,
                                               PotionEffectType.HEAL,PotionEffectType.HEALTH_BOOST,PotionEffectType.INCREASE_DAMAGE,PotionEffectType.REGENERATION,
                                               PotionEffectType.SATURATION};
        List<PotionEffectType> positiveEffects = Arrays.asList(positiveEffects0);
        for (PotionEffectType effect : positiveEffects) {
            p.addPotionEffect(new PotionEffect(effect,10*20,0));
        }
    }




}
