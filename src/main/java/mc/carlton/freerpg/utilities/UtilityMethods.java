package mc.carlton.freerpg.utilities;

import org.bukkit.Color;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionType;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class UtilityMethods {
    public static String capitalizeString(String string) {
        if (string.length() == 0) {
            return string;
        }
        else if (string.length() == 1) {
            return string.toUpperCase();
        }
        else {
            return string.substring(0,1).toUpperCase() + string.substring(1);
        }
    }
    public static boolean stringCollectionContainsIgnoreCase(Collection<String> colection, String inputString) {
        for (String string : colection) {
            if (inputString.equalsIgnoreCase(string)) {
                return true;
            }
        }
        return false;
    }
    public static String convertStringToListCasing(List<String> list,String inputString) {
        for (String string : list) {
            if (inputString.equalsIgnoreCase(string)) {
                return string;
            }
        }
        return inputString;
    }
    public static String intToRankingString(int rank) {
        String suffix = "th";
        int lastTwoDigits = rank % 100;
        int lastDigit = rank % 10;
        if (!(lastTwoDigits >= 10 && lastTwoDigits <= 19) ) {
            if (lastDigit == 1) {
                suffix = "st";
            } else if (lastDigit == 2) {
                suffix = "nd";
            } else if (lastDigit == 3) {
                suffix = "rd";
            }
        }
        return (String.valueOf(rank) + suffix);
    }
    public static <E> boolean collectionOnlyContainsOneClass(Collection<? extends E> collection,Class<?> tClass ) {
        for (E item : collection) {
            if (!tClass.isInstance(item)) {
                return false;
            }
        }
        return true;
    }
    public static Color getColorFromString(String colorString) {
        colorString = colorString.substring(1,colorString.length()-1);
        List<String> RGB = Arrays.asList(colorString.trim().split(","));
        int red = 0;
        int green = 0;
        int blue = 0;
        if (RGB.size() == 3) {
            red = Integer.parseInt(RGB.get(0));
            green = Integer.parseInt(RGB.get(1));
            blue = Integer.parseInt(RGB.get(2));
        }
        return Color.fromRGB(red,green,blue);
    }
    public static String camelCaseToSpacedString(String camelCaseString) {
        return camelCaseString.replaceAll("([^_])([A-Z])", "$1 $2");
    }
    public static EntityType matchEntityType(String entityTypeString) {
        String convertedString = entityTypeString.replace(" ", "_").toUpperCase();
        for (EntityType entityType : EntityType.values()) {
            if (entityType.toString().equalsIgnoreCase(convertedString)) {
                return entityType;
            }
        }
        return null;
    }
    public static PotionType matchPotionType(String potionTypeString) {
        String convertedString = potionTypeString.replace(" ", "_").toUpperCase();
        for (PotionType potionType : PotionType.values()) {
            if (potionType.toString().equalsIgnoreCase(convertedString)) {
                return potionType;
            }
        }
        return null;
    }
    public static EntityDamageEvent.DamageCause matchDamageCause(String damageCauseString) {
        String convertedString = damageCauseString.replace(" ", "_").toUpperCase();
        for (EntityDamageEvent.DamageCause damageCause : EntityDamageEvent.DamageCause.values()) {
            if (damageCause.toString().equalsIgnoreCase(convertedString)) {
                return damageCause;
            }
        }
        return null;
    }
    public static boolean stringContainsIgnoreCase(String string, String containedString) {
        if (string.toLowerCase().contains(containedString.toLowerCase())) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean stringContainsIgnoreCase(String string, Collection<String> containedStringOptions) {
        for (String containedString : containedStringOptions) {
            if (stringContainsIgnoreCase(string,containedString)){
                return true;
            }
        }
        return false;
    }
    public static String toAlphabetic(int i) {
        if( i<0 ) {
            return "-"+toAlphabetic(-i-1);
        }

        int quot = i/26;
        int rem = i%26;
        char letter = (char)((int)'A' + rem);
        if( quot == 0 ) {
            return ""+letter;
        } else {
            return toAlphabetic(quot-1) + letter;
        }
    }
}
