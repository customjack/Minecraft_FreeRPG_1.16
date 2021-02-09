package mc.carlton.freerpg.customContainers.collections;

import mc.carlton.freerpg.customContainers.CustomEffectPiece;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

public class CustomEffect {
    private HashSet<CustomEffectPiece> customEffectPieces = new HashSet<>();
    private double probability = 1.0;

    /**
     * Constuctor; initializes the custom effect as and empty set of effects
     */
    public CustomEffect(){
        this(new HashSet<>());
    }

    /**
     * Constructor, initializes the custom effect as a set of effects
     * @param effects a collection of CustomEFfectPiece classes
     */
    public CustomEffect(Collection<CustomEffectPiece> effects){
        addEffects(effects);
    }

    /**
     * Adds an effect to the CustomEffect
     * @param effect a CustomEffectPiece
     */
    public void addEffect(CustomEffectPiece effect) {
        customEffectPieces.add(effect);
    }

    /**
     * Adds effect(s) to the CustomEffect
     * @param effects a collection of CustomEffectPiece classes
     */
    public void addEffects(Collection<CustomEffectPiece> effects) {
        for (CustomEffectPiece effect : effects) {
            addEffect(effect);
        }
    }

    /**
     * Getter for set of customEffectPieces
     * @return A set of all the customEffectPieces that make up the CustomEffect
     */
    public HashSet<CustomEffectPiece> getCustomEffectPieces() {
        return customEffectPieces;
    }

    /**
     * Getter for probability
     * @return the probability that this effect activates (as a whole)
     */
    public double getProbability() {
        return probability;
    }

    /**
     * Setter for probability
     * @param probability the probability that this effect activates (as a whole)
     */
    public void setProbability(double probability) {
        this.probability = probability;
    }

    /**
     * Gives an entity the potion effect(s) using an initial seed for the random number generator
     * @param entity a LivingEntity that will receive the CustomEffect
     * @param seed a seed for the random number generator
     */
    public void giveEntityEffect(LivingEntity entity, long seed) {
        Random random = new Random(seed);
        HashSet<PotionEffect> effectsToGive = new HashSet<>();
        for (CustomEffectPiece customEffectPiece : customEffectPieces) {
            if (random.nextDouble() < customEffectPiece.getProbability()) {
                effectsToGive.add(customEffectPiece.getPotionEffect());
            }
        }
        entity.addPotionEffects(effectsToGive);
    }

    /**
     * Gives an entity the potion effect(s)
     * @param entity a LivingEntity that will receive the CustomEffect
     */
    public void giveEntityEffect(LivingEntity entity) {
        giveEntityEffect(entity,new Random().nextLong());
    }

    /*
     This method may not be necessary
     IT IS ALSO INCOMPLETE AS A RESULT
     */
    private boolean shouldAddEffect(LivingEntity entity, CustomEffectPiece customEffectPiece) {
        if (!entity.hasPotionEffect(customEffectPiece.getEffectType())) {
            return true;
        }
        PotionEffect currentEffect = entity.getPotionEffect(customEffectPiece.getEffectType());
        int futureCurrentEffectLength = currentEffect.getDuration() - customEffectPiece.getDelay(); //The length of the effect when the effect may be added, in ticks
        if (futureCurrentEffectLength > customEffectPiece.getDuration()) {
            return false;
        }
        return true;
    }
}
