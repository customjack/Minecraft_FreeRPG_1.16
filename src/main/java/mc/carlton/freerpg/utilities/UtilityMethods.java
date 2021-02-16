package mc.carlton.freerpg.utilities;

import org.bukkit.Color;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
    public static boolean containsIgnoreCase(Collection<String> colection, String inputString) {
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
}
