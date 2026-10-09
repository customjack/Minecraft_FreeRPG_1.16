package mc.carlton.freerpg.guiTools;

import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.gameTools.LanguageSelector;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builds the icons for the nodes of the skill trees.
 *
 * What the player sees:
 *  - the item itself says WHAT the perk does (one material per perk, see ICONS)
 *  - the name color and the star line say HOW HARD it is to get (Tier: common -> legendary)
 *  - the stack size, progress bar and glint say HOW FAR the player is (State / level)
 *
 * The click handlers used to tell nodes apart by their material (red/pink/yellow/green terracotta).
 * Now that the material is the perk icon, the state is stored on the item and
 * {@link #clickType(ItemStack)} translates it back to the material the handlers expect.
 */
public final class SkillNodeIcon {

    public enum State {
        LOCKED(Material.RED_TERRACOTTA),
        AVAILABLE(Material.PINK_TERRACOTTA),
        IN_PROGRESS(Material.YELLOW_TERRACOTTA),
        MAXED(Material.GREEN_TERRACOTTA);

        private final Material clickMaterial;

        State(Material clickMaterial) {
            this.clickMaterial = clickMaterial;
        }
    }

    public enum Tier {
        COMMON("common", "common", ChatColor.WHITE, 1),
        UNCOMMON("uncommon", "uncommon", ChatColor.GREEN, 2),
        RARE("rare", "rare", ChatColor.AQUA, 3),
        LEGENDARY("legendary", "legendary", ChatColor.GOLD, 4);

        private final String langId;
        private final String fallback;
        private final ChatColor color;
        private final int stars;

        Tier(String langId, String fallback, ChatColor color, int stars) {
            this.langId = langId;
            this.fallback = fallback;
            this.color = color;
            this.stars = stars;
        }
    }

    private static final String STATE_KEY = "skill_node_state";

    //Tier of each node, in the same order as the menu items of each tree layout (see ICONS)
    private static final Tier[] MAIN_TIERS = {Tier.COMMON, Tier.COMMON, Tier.UNCOMMON, Tier.UNCOMMON, Tier.RARE, Tier.RARE, Tier.LEGENDARY};
    private static final Tier[] SECONDARY_TIERS = {Tier.COMMON, Tier.UNCOMMON, Tier.LEGENDARY};
    private static final Tier[] GLOBAL_TIERS = {Tier.COMMON, Tier.COMMON, Tier.COMMON, Tier.UNCOMMON, Tier.UNCOMMON, Tier.UNCOMMON, Tier.RARE, Tier.RARE, Tier.RARE, Tier.LEGENDARY};

    //Icons per skill. Same order as the perk titles in languages.yml:
    //main skills: 1a, 1b, 2a, 2b, 3a, 3b, Master | secondary skills: 1a, 2a, Master | global: 1a, 1b, 1c, 2a, 2b, 2c, 3a, 3b, 3c, Master
    private static final Map<String, Material[]> ICONS = new HashMap<>();
    static {
        ICONS.put("digging", new Material[]{Material.DROPPER, Material.CHEST, Material.EMERALD, Material.SOUL_SAND, Material.FLINT, Material.GOLDEN_SHOVEL, Material.NETHERITE_SHOVEL});
        ICONS.put("woodcutting", new Material[]{Material.EXPERIENCE_BOTTLE, Material.GOLDEN_AXE, Material.ENCHANTED_BOOK, Material.OAK_LEAVES, Material.DARK_OAK_LOG, Material.WIND_CHARGE, Material.DIAMOND_AXE});
        ICONS.put("mining", new Material[]{Material.GOLDEN_PICKAXE, Material.TNT, Material.DIAMOND_ORE, Material.TNT_MINECART, Material.IRON_ORE, Material.CREEPER_HEAD, Material.NETHERITE_PICKAXE});
        ICONS.put("farming", new Material[]{Material.BONE_MEAL, Material.COW_SPAWN_EGG, Material.BREAD, Material.COOKED_BEEF, Material.WHEAT, Material.SUGAR, Material.GRASS_BLOCK});
        ICONS.put("fishing", new Material[]{Material.LEAD, Material.NAUTILUS_SHELL, Material.COOKED_SALMON, Material.HEART_OF_THE_SEA, Material.FISHING_ROD, Material.BLAZE_ROD, Material.TROPICAL_FISH});
        ICONS.put("archery", new Material[]{Material.ARROW, Material.SPYGLASS, Material.SPECTRAL_ARROW, Material.FIRE_CHARGE, Material.DRAGON_BREATH, Material.CROSSBOW, Material.FIREWORK_ROCKET});
        ICONS.put("beastMastery", new Material[]{Material.LEATHER, Material.BONE, Material.COOKED_MUTTON, Material.PISTON, Material.SLIME_BALL, Material.COMPASS, Material.SADDLE});
        ICONS.put("swordsmanship", new Material[]{Material.SUGAR, Material.IRON_SWORD, Material.RABBIT_FOOT, Material.BLAZE_POWDER, Material.REDSTONE, Material.QUARTZ, Material.NETHERITE_SWORD});
        ICONS.put("defense", new Material[]{Material.GLISTERING_MELON_SLICE, Material.IRON_CHESTPLATE, Material.IRON_HELMET, Material.DIAMOND_CHESTPLATE, Material.GOLDEN_APPLE, Material.IRON_LEGGINGS, Material.TOTEM_OF_UNDYING});
        ICONS.put("axeMastery", new Material[]{Material.IRON_AXE, Material.LIGHTNING_ROD, Material.ENCHANTED_GOLDEN_APPLE, Material.NETHER_WART, Material.MACE, Material.GOLDEN_AXE, Material.NETHERITE_AXE});
        ICONS.put("repair", new Material[]{Material.GRINDSTONE, Material.IRON_INGOT, Material.ANVIL});
        ICONS.put("agility", new Material[]{Material.LEATHER_BOOTS, Material.IRON_BOOTS, Material.FEATHER});
        ICONS.put("alchemy", new Material[]{Material.CAULDRON, Material.WRITABLE_BOOK, Material.POTION});
        ICONS.put("smelting", new Material[]{Material.COAL, Material.GOLD_INGOT, Material.LAVA_BUCKET});
        ICONS.put("enchanting", new Material[]{Material.LAPIS_LAZULI, Material.ENCHANTED_BOOK, Material.EXPERIENCE_BOTTLE});
        ICONS.put("global", new Material[]{Material.IRON_PICKAXE, Material.BOOKSHELF, Material.IRON_SWORD, Material.DIAMOND_PICKAXE, Material.ENCHANTING_TABLE, Material.TARGET, Material.TOTEM_OF_UNDYING, Material.SOUL_LANTERN, Material.BEACON, Material.NETHER_STAR});
    }

    private SkillNodeIcon() {
    }

    /**
     * Works out the state of a node
     * @param level current level of the perk
     * @param maxLevel maximum level of the perk
     * @param prerequisitesMet whether the perks that gate this one are satisfied
     */
    public static State stateOf(int level, int maxLevel, boolean prerequisitesMet) {
        if (level >= maxLevel) {
            return State.MAXED;
        }
        if (level > 0) {
            return State.IN_PROGRESS;
        }
        return prerequisitesMet ? State.AVAILABLE : State.LOCKED;
    }

    /**
     * Reads a state back from the terracotta the tree builders use while working out progress
     * (red = locked, pink = available, yellow = in progress, green = maxed)
     */
    public static State stateOf(Material progressMarker) {
        for (State state : State.values()) {
            if (state.clickMaterial == progressMarker) {
                return state;
            }
        }
        return State.LOCKED;
    }

    /**
     * Creates the icon of one node of a skill tree
     * @param skillName skill the tree belongs to (ex. "mining", "global")
     * @param nodeIndex position of the node in the tree's menu item order
     * @param state state of the node
     * @param level current level of the perk
     * @param maxLevel maximum level of the perk
     * @param title translated perk name
     * @param levelLine already formatted "Level x/y" line
     * @param descriptionLines description of the perk, one entry per lore line
     * @param lang language of the player the menu is shown to
     */
    public static ItemStack create(String skillName, int nodeIndex, State state, int level, int maxLevel,
                                   String title, String levelLine, List<String> descriptionLines, LanguageSelector lang) {
        Material[] icons = ICONS.get(skillName);
        Material material = (icons != null && nodeIndex < icons.length) ? icons[nodeIndex] : state.clickMaterial;
        Tier tier = tierOf(skillName, nodeIndex);

        ItemStack item = new ItemStack(material);
        if (state == State.IN_PROGRESS || state == State.MAXED) {
            item.setAmount(Math.max(1, Math.min(level, 64)));
        }

        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_ENCHANTS);
        meta.setEnchantmentGlintOverride(state == State.MAXED);

        ChatColor nameColor = (state == State.LOCKED) ? ChatColor.DARK_GRAY : tier.color;
        meta.setDisplayName(nameColor.toString() + ChatColor.BOLD + title);

        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + translate(lang, "nodeRarity", "Rarity") + ": " + tier.color + "★".repeat(tier.stars) + " " + translate(lang, tier.langId, tier.fallback));
        String progress = (maxLevel > 1) ? " " + progressBar(level, maxLevel, tier.color) : "";
        lore.add(levelLine + progress);
        switch (state) {
            case LOCKED:
                lore.add(ChatColor.RED + "✖ " + translate(lang, "nodeLocked", "Locked"));
                break;
            case AVAILABLE:
                lore.add(ChatColor.YELLOW + "▶ " + translate(lang, "nodeAvailable", "Click to unlock"));
                break;
            case IN_PROGRESS:
                lore.add(ChatColor.YELLOW + "▶ " + translate(lang, "nodeInProgress", "Click to upgrade"));
                break;
            case MAXED:
                lore.add(ChatColor.GREEN + "✔ " + translate(lang, "nodeMaxed", "Maxed out"));
                break;
        }
        lore.add("");
        for (String line : descriptionLines) {
            lore.add(ChatColor.GRAY.toString() + ChatColor.ITALIC + line);
        }
        meta.setLore(lore);

        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(stateKey(), PersistentDataType.STRING, state.name());
        item.setItemMeta(meta);
        return item;
    }

    /**
     * The material the click handlers switch on. Skill tree nodes report the terracotta that matches their state
     * (locked = red, available = pink, in progress = yellow, maxed = green); every other item reports its own type.
     */
    public static Material clickType(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return item == null ? Material.AIR : item.getType();
        }
        String stored = item.getItemMeta().getPersistentDataContainer().get(stateKey(), PersistentDataType.STRING);
        if (stored != null) {
            try {
                return State.valueOf(stored).clickMaterial;
            } catch (IllegalArgumentException ignored) {
                //Unknown state, treat as a normal item
            }
        }
        return item.getType();
    }

    private static Tier tierOf(String skillName, int nodeIndex) {
        Tier[] tiers;
        if (skillName.equals("global")) {
            tiers = GLOBAL_TIERS;
        } else if (ICONS.containsKey(skillName) && ICONS.get(skillName).length == SECONDARY_TIERS.length) {
            tiers = SECONDARY_TIERS;
        } else {
            tiers = MAIN_TIERS;
        }
        return tiers[Math.min(nodeIndex, tiers.length - 1)];
    }

    private static String progressBar(int level, int maxLevel, ChatColor filledColor) {
        int filled = Math.max(0, Math.min(level, maxLevel));
        return filledColor + "■".repeat(filled) + ChatColor.DARK_GRAY + "□".repeat(maxLevel - filled);
    }

    //Languages that predate the node icons don't have these ids, so fall back to English instead of showing the raw id
    private static String translate(LanguageSelector lang, String id, String fallback) {
        String text = lang.getString(id);
        if (text == null || text.equals(id)) {
            text = LanguageSelector.getString(id, "enUs");
            if (text.equals(id)) {
                text = fallback;
            }
        }
        return text;
    }

    private static NamespacedKey stateKey() {
        return new NamespacedKey(FreeRPG.getPlugin(FreeRPG.class), STATE_KEY);
    }
}
