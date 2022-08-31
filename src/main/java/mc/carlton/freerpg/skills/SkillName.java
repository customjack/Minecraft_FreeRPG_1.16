package mc.carlton.freerpg.skills;

import java.util.ArrayList;
import java.util.Arrays;
import mc.carlton.freerpg.utils.UtilityMethods;

public enum SkillName {
  DIGGING,
  WOODCUTTING,
  MINING,
  FARMING,
  FISHING,
  ARCHERY,
  BEAST_MASTERY,
  SWORDSMANSHIP,
  DEFENSE,
  AXE_MASTERY,
  REPAIR,
  AGILITY,
  ALCHEMY,
  SMELTING,
  ENCHANTING,
  GLOBAL;

  /**
   * Attempts to match a string to a SkillName enum
   *
   * @param skillName string representation of skillName
   * @return Enum if matched, null otherwise
   */
  public static SkillName matchSkillName(String skillName) {
    String convertedSkillName = UtilityMethods.camelCaseToSpacedString(skillName).replace(" ", "_")
        .toUpperCase();
    try {
      return SkillName.valueOf(convertedSkillName);
    } catch (NullPointerException exception) {
      return null; //We don't want an exception if user input is messed up, and if we really did we can just throw one.
    }
  }

  /**
   * Gets values in ArrayList representation
   *
   * @return ArrayList representation of all enum
   */
  public static ArrayList<SkillName> getValues() {
    return new ArrayList<>(Arrays.asList(SkillName.values()));
  }

  /**
   * Gets Main skill enums in ArrayList representation
   *
   * @return ArrayList representation of all main skill enum
   */
  public static ArrayList<SkillName> getMainSkillValues() {
    return new ArrayList<>(Arrays.asList(SkillName.mainSkillValues()));
  }

  /**
   * Gets Passive skill enums in ArrayList representation
   *
   * @return ArrayList representation of all passive skill enum
   */
  public static ArrayList<SkillName> getPassiveSkillValues() {
    return new ArrayList<>(Arrays.asList(SkillName.passiveSkillValues()));
  }

  /**
   * Gets all enums (except global) in ArrayList representation
   *
   * @return ArrayList representation of all enum (except GLOBAL)
   */
  public static ArrayList<SkillName> getValuesWithoutGlobal() {
    return new ArrayList<>(Arrays.asList(SkillName.valuesWithoutGlobal()));
  }

  /**
   * Gets main skill enum
   *
   * @return array of main skill enum
   */
  public static SkillName[] mainSkillValues() {
    return new SkillName[]{
        DIGGING,
        WOODCUTTING,
        MINING,
        FARMING,
        FISHING,
        ARCHERY,
        BEAST_MASTERY,
        SWORDSMANSHIP,
        DEFENSE,
        AXE_MASTERY};
  }

  /**
   * Gets passive skill enum
   *
   * @return array of passive skill enum
   */
  public static SkillName[] passiveSkillValues() {
    return new SkillName[]{
        REPAIR,
        AGILITY,
        ALCHEMY,
        SMELTING,
        ENCHANTING,};
  }

  /**
   * Gets all enum except GLOBAL
   *
   * @return array of all enum except GLOBAL
   */
  public static SkillName[] valuesWithoutGlobal() {
    return new SkillName[]{
        DIGGING,
        WOODCUTTING,
        MINING,
        FARMING,
        FISHING,
        ARCHERY,
        BEAST_MASTERY,
        SWORDSMANSHIP,
        DEFENSE,
        AXE_MASTERY,
        REPAIR,
        AGILITY,
        ALCHEMY,
        SMELTING,
        ENCHANTING,
    };
  }

}
