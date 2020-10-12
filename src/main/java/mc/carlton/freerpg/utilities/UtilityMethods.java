package mc.carlton.freerpg.utilities;

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
    public static boolean containsIgnoreCase(List<String> list, String inputString) {
        for (String string : list) {
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
}
