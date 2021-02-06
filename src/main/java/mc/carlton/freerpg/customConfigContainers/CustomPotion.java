package mc.carlton.freerpg.customConfigContainers;

import mc.carlton.freerpg.utilities.UtilityMethods;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomPotion extends CustomItem {
    private PotionType potionType;
    private boolean isUpgraded = false;
    private boolean isExtended = false;
    private List<PotionEffect> potionEffects = new ArrayList<>();
    private Color color = Color.fromRGB(255,255,255);
    private static Map<PotionEffectType, Color> potionEffectColors;

    public CustomPotion() {
        super(Material.POTION);
    }

    public void setPotion(PotionType potionType, boolean isUpgraded, boolean isExtended) {
        this.potionType = potionType;
        this.isUpgraded = isUpgraded;
        this.isExtended = isExtended;
    }

    public void setPotion(List<PotionEffect> potionEffects) {
        this.potionEffects = potionEffects;
    }

    public void addPotionEffect(PotionEffect potionEffect) {
        this.potionEffects.add(potionEffect);

    }

    public void addPotionEffect(PotionEffectType potionEffectType,double duration, int level) {
        PotionEffect potionEffect = new PotionEffect(potionEffectType,(int) Math.round(duration*20),level-1);
        potionEffects.add(potionEffect);
    }

    public void setPotionColor(Color color) {
        this.color = color;
    }

    public void setPotionColor(String colorString) {
        this.color = UtilityMethods.getColorFromString(colorString);
    }

    private static void setPotionEffectColors() {
        if (potionEffectColors.isEmpty()) {
            PotionMeta potionMeta = ((PotionMeta) new ItemStack(Material.POTION).getItemMeta());

            //Effects with predetermined color
            potionMeta.setBasePotionData(new PotionData(PotionType.FIRE_RESISTANCE));
            potionEffectColors.put(PotionEffectType.FIRE_RESISTANCE,potionMeta.getColor());
            potionMeta.setBasePotionData(new PotionData(PotionType.INSTANT_DAMAGE));
            potionEffectColors.put(PotionEffectType.HARM,potionMeta.getColor());
            potionMeta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL));
            potionEffectColors.put(PotionEffectType.HEAL,potionMeta.getColor());
            potionMeta.setBasePotionData(new PotionData(PotionType.STRENGTH));
            potionEffectColors.put(PotionEffectType.INCREASE_DAMAGE,potionMeta.getColor());
            potionMeta.setBasePotionData(new PotionData(PotionType.INVISIBILITY));
            potionEffectColors.put(PotionEffectType.INVISIBILITY,potionMeta.getColor());
            potionMeta.setBasePotionData(new PotionData(PotionType.JUMP));
            potionEffectColors.put(PotionEffectType.JUMP,potionMeta.getColor());
            potionMeta.setBasePotionData(new PotionData(PotionType.SLOW_FALLING));
            potionEffectColors.put(PotionEffectType.LEVITATION,potionMeta.getColor());
            potionMeta.setBasePotionData(new PotionData(PotionType.NIGHT_VISION));
            potionEffectColors.put(PotionEffectType.NIGHT_VISION,potionMeta.getColor());
            potionMeta.setBasePotionData(new PotionData(PotionType.POISON));
            potionEffectColors.put(PotionEffectType.POISON,potionMeta.getColor());
            potionMeta.setBasePotionData(new PotionData(PotionType.REGEN));
            potionEffectColors.put(PotionEffectType.REGENERATION,potionMeta.getColor());
            potionMeta.setBasePotionData(new PotionData(PotionType.SLOWNESS));
            potionEffectColors.put(PotionEffectType.SLOW,potionMeta.getColor());
            potionMeta.setBasePotionData(new PotionData(PotionType.SPEED));
            potionEffectColors.put(PotionEffectType.SPEED,potionMeta.getColor());
            potionMeta.setBasePotionData(new PotionData(PotionType.WATER_BREATHING));
            potionEffectColors.put(PotionEffectType.WATER_BREATHING,potionMeta.getColor());
            potionMeta.setBasePotionData(new PotionData(PotionType.WEAKNESS));
            potionEffectColors.put(PotionEffectType.WEAKNESS,potionMeta.getColor());

            // Effects with no predetermined color
            potionEffectColors.put(PotionEffectType.ABSORPTION,Color.fromRGB(196, 201, 34));
            potionEffectColors.put(PotionEffectType.BAD_OMEN,Color.fromRGB(150, 147, 147));
            potionEffectColors.put(PotionEffectType.CONDUIT_POWER,Color.fromRGB(119, 230, 252));
            potionEffectColors.put(PotionEffectType.CONFUSION,Color.fromRGB(116, 153, 130));
            potionEffectColors.put(PotionEffectType.DAMAGE_RESISTANCE,Color.fromRGB(46, 67, 143));
            potionEffectColors.put(PotionEffectType.DOLPHINS_GRACE,Color.fromRGB(0,0,0));
            potionEffectColors.put(PotionEffectType.FAST_DIGGING,Color.fromRGB(176, 189, 36));
            potionEffectColors.put(PotionEffectType.GLOWING,Color.fromRGB(239, 252, 50));
            potionEffectColors.put(PotionEffectType.HEALTH_BOOST,Color.fromRGB(252, 10, 192));
            potionEffectColors.put(PotionEffectType.HERO_OF_THE_VILLAGE,Color.fromRGB(33, 138, 10));
            potionEffectColors.put(PotionEffectType.HUNGER,Color.fromRGB(110, 76, 49));
            potionEffectColors.put(PotionEffectType.LUCK,Color.fromRGB(130, 219, 83));
            potionEffectColors.put(PotionEffectType.SATURATION,Color.fromRGB(219, 166, 86));
            potionEffectColors.put(PotionEffectType.UNLUCK,Color.fromRGB(50, 77, 61));
            potionEffectColors.put(PotionEffectType.WITHER,Color.fromRGB(59, 59, 59));
        }
    }

    @Override
    public String toString() {
        String stringValue = "";
        stringValue += "[";
        stringValue += "Material: " + this.material.toString() + ", ";
        stringValue += "Amount: " + this.amount + ", ";
        stringValue += "Durability: (" + this.minDurabilityPortion + ", " + this.maxDurabilityPortion + "), ";
        stringValue += "Random Enchantment Range: (" + this.minEnchantmentLevel + ", " + this.maxEnchantmentLevel + ", treasure=" + isTreasure + "), ";
        stringValue += "Static Enchantments: {";
        int counter = 0;
        for (Enchantment enchantment : enchantments.keySet()) {
            stringValue += enchantment.getKey().getKey() + "-" + enchantments.get(enchantment).toString();
            counter += 1;
            if (counter < enchantments.size()) {
                stringValue += ", ";
            }
        }
        stringValue += "}, ";
        if (probability != -1) {
            stringValue += "Probability: " + probability + ", ";
        } else {
            stringValue += "Weight: " + weight + ", ";
        }
        stringValue += "Exp: " + experienceDrop + ", ";
        if (potionType != null) {
            stringValue += "Potion: " + potionType.toString() + ", level: " + (isUpgraded ? 1 : 2) + ", duration: " + (isExtended ? "normal" : "extended") + ", ";
        }
        if (!potionEffects.isEmpty()) {
            stringValue += "Potion Effects: {";
            int counter2 = 0;
            for (PotionEffect potionEffect : potionEffects) {
                stringValue += potionEffect.toString();
                counter2 +=1;
                if (counter2 < potionEffects.size()) {
                    stringValue += ", ";
                }
            }
            stringValue += "}";
        }
        stringValue += "]";
        return stringValue;
    }
}
