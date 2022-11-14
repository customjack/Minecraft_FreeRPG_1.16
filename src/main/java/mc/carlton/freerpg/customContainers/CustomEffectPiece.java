package mc.carlton.freerpg.customContainers;

import java.util.Map;
import mc.carlton.freerpg.customContainers.collections.CustomEffect;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CustomEffectPiece extends CustomContainer {

  private PotionEffectType effectType;
  private int level = 1;
  private double duration = 0.0;
  private double delay = 0.0;

  private CustomEffectPiece relativeEffect;
  private double relativeDurationMultiplier = 1.0;
  private double relativeDurationAdded = 0.0;

  private double probability = 1.0;

  public CustomEffectPiece(PotionEffectType effectType) {
    this(effectType, null);
  }

  public CustomEffectPiece(PotionEffectType effectType, Map<String, Object> containerInformation) {
    super(containerInformation);
    this.effectType = effectType;
  }

  /**
   * Sets the delay
   *
   * @param delayTicks delay in ticks
   */
  public void setDelayTicks(int delayTicks) {
    this.delay = delayTicks / 20.0;
  }

  /**
   * Sets the duration
   *
   * @param durationTicks duration in ticks
   */
  public void setDurationTicks(int durationTicks) {
    this.duration = durationTicks / 20.0;
  }

  /**
   * Sets additional time added to the duration
   *
   * @param relativeDurationAdded time added in seconds
   */
  public void setRelativeDurationAdded(double relativeDurationAdded) {
    this.relativeDurationAdded = relativeDurationAdded;
  }

  /**
   * Sets additional time added to the duration
   *
   * @param relativeDurationAddedTicks time added in ticks
   */
  public void setRelativeDurationAddedTicks(int relativeDurationAddedTicks) {
    this.relativeDurationAdded = relativeDurationAddedTicks / 20.0;
  }

  /**
   * Sets the relative duration multiplier
   *
   * @param relativeDurationMultiplier multiplier for the duration of a relative effect, determines
   *                                   the duration in conjunction with relativeDurationAdded
   */
  public void setRelativeDurationMultiplier(double relativeDurationMultiplier) {
    this.relativeDurationMultiplier = relativeDurationMultiplier;
  }

  /**
   * Getter for the probability this effect is activated
   *
   * @return the probability that this effect piece activates
   */
  public double getProbability() {
    return probability;
  }

  /**
   * Sets the probability
   *
   * @param probability probability the effect is added
   */
  public void setProbability(double probability) {
    this.probability = probability;
  }

  /**
   * Getter for relativeEffect
   *
   * @return the CustomEffectPiece that determines the duration of this effect
   */
  public CustomEffectPiece getRelativeEffect() {
    return relativeEffect;
  }

  /**
   * Sets the relative effect
   *
   * @param relativeEffect a CustomEffectPiece acting as the relative effect
   */
  public void setRelativeEffect(CustomEffectPiece relativeEffect) {
    this.relativeEffect = relativeEffect;
  }

  /**
   * Sets the relative effect as the CustomEffectPiece with the longest duration in the CustomEffect
   * relativeEffect
   *
   * @param relativeEffect a CustomEffect (list of CustomEffectPiece classes)
   */
  public void setRelativeEffect(CustomEffect relativeEffect) {
    int longestDurationOfRelativeEffectPieceSet = 0;
    for (CustomEffectPiece relativeEffectPiece : relativeEffect.getCustomEffectPieces()) {
      if (longestDurationOfRelativeEffectPieceSet < relativeEffectPiece.getDuration()) {
        this.relativeEffect = relativeEffectPiece;
        longestDurationOfRelativeEffectPieceSet = relativeEffectPiece.getDuration();
      }
    }
  }

  /**
   * Getter for delay
   *
   * @return the delay in ticks
   */
  public int getDelay() {
    return (int) Math.round(20 * delay);
  }

  /**
   * Sets the delay
   *
   * @param delay delay in seconds
   */
  public void setDelay(double delay) {
    this.delay = delay;
  }

  /**
   * Getter for duration
   *
   * @return the duration in ticks
   */
  public int getDuration() {
    double trueDuration = 0.0;
    if (relativeEffect != null) {
      trueDuration = relativeDurationMultiplier * relativeEffect.getDuration();
    } else {
      trueDuration = duration;
    }
    trueDuration += relativeDurationAdded;
    return (int) Math.round(20 * trueDuration);
  }

  /**
   * Sets the duration
   *
   * @param duration duration in seconds
   */
  public void setDuration(double duration) {
    this.duration = duration;
  }

  /**
   * Getter for level
   *
   * @return the level of the potion effect
   */
  public int getLevel() {
    return level;
  }

  /**
   * Sets the effect level
   *
   * @param level potion level of the effect
   */
  public void setLevel(int level) {
    this.level = level;
  }

  /**
   * Returns the amplifier of the effect, which is level - 1
   *
   * @return the amplifier of the effect, which is level -1
   */
  public int getAmplifier() {
    return level - 1;
  }

  /**
   * Getter for the effectType
   *
   * @return PotionEffectType
   */
  public PotionEffectType getEffectType() {
    return effectType;
  }

  /**
   * Sets the effect type
   *
   * @param effectType a PotionEffectType
   */
  public void setEffectType(PotionEffectType effectType) {
    this.effectType = effectType;
  }

  /**
   * Constructs and returns a potionEffect
   *
   * @return PotionEffect that embodies some information in this CustomEffectPiece
   */
  public PotionEffect getPotionEffect() {
    return new PotionEffect(getEffectType(), getDuration(), getLevel());
  }

  @Override
  public String toString() {
    String stringValue = "";
    stringValue += "[";
    stringValue += "Effect: " + this.effectType.getName() + ", ";
    stringValue += "Duration: " + this.duration + ", ";
    stringValue += "Delay: " + this.delay + ", ";
    stringValue += "Probability: " + this.probability + ", ";
    stringValue += "Relative Duration Multiplier: " + this.relativeDurationMultiplier + ", ";
    stringValue += "Relative Duration Added: " + this.relativeDurationAdded;
    stringValue += "]";

    return stringValue;
  }
}
