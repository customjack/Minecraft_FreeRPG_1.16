package mc.carlton.freerpg.customConfigContainers;

import mc.carlton.freerpg.utilities.FrpgPrint;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
This Class' purpose is to import information from a config node into an actually a container class (CustomItem, CustomPotion, etc...)
 */
public class CustomContainerImporter {
    CustomContainer container;
    String configPath;
    private static final String IMPROPER_CONFIG = "WARNING: Improper Config at ";
    private static final String MATERIAL_NOT_FOUND = "Material not found: ";
    private static final String IMPROPER_AMOUNT =  "Amount cannot be less than 1 ";
    private static final String DURABILITY_PARAMETERS_INVALID =  "Durability Parameters Invalid";
    private static final String INVALID_DURABILITY_BOUND = "Minimum durability cannot be greater than maximum durability";
    private static final String RANDOM_ENCHANTMENT_PARAMETERS_INVALID = "Random Enchantment Parameters Invalid";
    private static final String INVALID_ENCHANTMENT_BOUNDS = "Minimum Enchantment level cannot be greater than Maximum enchantment level";
    private static final String STATIC_ENCHANTMENT_PARAMETERS_INVALID = "Static Enchantment Parameters Invalid";
    private static final String NEGATIVE_WEIGHT = "Weight Value cannot be negative";
    private static final String INVALID_PROBABILITY ="Probability value must be between 0 and 1";
    private static final String IMPROPER_DURABILITY_LIMITS = "Durability Limits cannot be less than 0";
    private static final String IMPROPER_ENCHANTMENT_LIMITS = "Enchantment Limits cannot be less than 0";
    private static final String INVALID_ENCHANTMENT = "Invalid Enchantment: ";
    private static final String INVALID_ENCHANTMENT_LEVEL = "Invalid Enchantment Level: ";
    private static final String EXPECTED_LIST = "Expected List at config node: ";
    private static final String UNKNOWN_CONFIG_LOCATION = "Unknown Config Location";

    public static List<Map<String,Object>> getConfigTableInformation(YamlConfiguration config, String configPath) {
        List configTable = config.getList(configPath);
        if (configTable == null) {
            FrpgPrint.print(EXPECTED_LIST + configPath);
            return null;
        }
        return getConfigTableInformation(configTable);
    }

    public static List<Map<String,Object>> getConfigTableInformation(List configTable,String configPath) {
        ArrayList<Map<String,Object>> tableInformation = new ArrayList<>();
        for (Object tableRow : configTable) {
            System.out.println(tableRow.getClass());
            System.out.println(tableRow);
            if (!(tableRow instanceof List)) {
                FrpgPrint.print(EXPECTED_LIST+ configPath);
                return null;
            }
            tableInformation.add(convertListedTableRowToMap((List) tableRow,configPath));
        }
        return tableInformation;
    }

    public static List<Map<String,Object>> getConfigTableInformation(List configTable) {
        return getConfigTableInformation(configTable,UNKNOWN_CONFIG_LOCATION);
    }

    public static Map<String,Object> convertListedTableRowToMap(List configTableRow, String configPath) {
        Map<String,Object> tableRow = new HashMap<>();
        for (Object tableElementObject : configTableRow) {
            if (!(tableElementObject instanceof Map)) {
                FrpgPrint.print(EXPECTED_LIST+ configPath);
                return null;
            }
            Map tableElement = (Map) tableElementObject;
            for (Object key : tableElement.keySet()) {
                Object value = tableElement.get(key);
                tableRow.put(key.toString(),value);
            }
        }
        return tableRow;
    }

    public CustomContainerImporter(CustomContainer customContainer) {
        this.container = customContainer;
    }

    public void setFromConfigEntry(Object configItem) {
        try {
            List<Map> configItemList = getListOfMapsFromValueObject(configItem);
            if (configItemList == null) {
                return;
            }
            for (Map map : configItemList) {
                setValueFromConfigNode(map);
            }
        } catch (Exception e) {
            printReadInError();
        }
    }

    private void setValueFromConfigNode(Map map) {
        Object key = getOnlyKey(map); //Gets "first" key from the map (throws error if more than one key)
        Object value = map.get(key); //Gets value of the key
        String mapKey = key.toString(); //Gets string name of the key
        setValueFromConfigNodeForCustomItem(value,mapKey);
    }

    private void setValueFromConfigNodeForCustomItem(Object value, String mapKey) {
        if (mapKey.equalsIgnoreCase("item") || mapKey.equalsIgnoreCase("drop")) {
            decodeMaterialValue(value);
        } else if (mapKey.equalsIgnoreCase("amount")) {
            decodeAmountValue(value);
        } else if (mapKey.equalsIgnoreCase("durability") || mapKey.equalsIgnoreCase("durabilityModifier")) {
            decodeDurabilityInformation(value);
        } else if (mapKey.equalsIgnoreCase("randomEnchantment") || mapKey.equalsIgnoreCase("enchantmentBounds")) {
            decodeEnchantmentInformation(value);
        } else if (mapKey.equalsIgnoreCase("staticEnchantments") || mapKey.equalsIgnoreCase("enchantments")) {
            decodeStaticEnchantmentInformation(value);
        } else if (mapKey.equalsIgnoreCase("weight")) {
            decodeWeightValue(value);
        } else if (mapKey.equalsIgnoreCase("prob") || mapKey.equalsIgnoreCase("probability")) {
            decodeProbValue(value);
        } else if (mapKey.equalsIgnoreCase("experience") || mapKey.equalsIgnoreCase("exp")) {
            decodeExperienceValue(value);
        }
    }

    private void decodeMaterialValue(Object value) {
        CustomItem customItem = (CustomItem) container;
        customItem.material = Material.getMaterial(value.toString());
        if (customItem.material == null) {
            printReadInError(MATERIAL_NOT_FOUND + value.toString());
        }
    }

    private void decodeAmountValue(Object value) {
        CustomItem customItem = (CustomItem) container;
        customItem.amount = Integer.valueOf(value.toString());
        if (customItem.amount < 1) {
            printReadInError(IMPROPER_AMOUNT);
            customItem.amount = 1;
        }
    }

    private void decodeDurabilityInformation(Object value) {
        CustomItem customItem = (CustomItem) container;
        List<Map> valueList = getListOfMapsFromValueObject(value, DURABILITY_PARAMETERS_INVALID);
        if (valueList == null) {
            return;
        }
        for (Map map : valueList) {
            decodeDurabilityKey(map);
        }
        if (customItem.minDurabilityPortion > customItem.maxDurabilityPortion) {
            printReadInError(INVALID_DURABILITY_BOUND);
            customItem.maxDurabilityPortion = customItem.minDurabilityPortion;
        }
    }

    private void decodeEnchantmentInformation(Object value) {
        CustomItem customItem = (CustomItem) container;
        List<Map> valueList = getListOfMapsFromValueObject(value, RANDOM_ENCHANTMENT_PARAMETERS_INVALID);
        if (valueList == null) {
            return;
        }
        for (Map map : valueList) {
            decodeEnchantmentKey(map);
        }
        if (customItem.minEnchantmentLevel > customItem.maxEnchantmentLevel) {
            printReadInError(INVALID_ENCHANTMENT_BOUNDS);
            customItem.maxEnchantmentLevel = customItem.minEnchantmentLevel;
        }
    }

    private void decodeStaticEnchantmentInformation(Object value) {
        List<List<Map>> valueListofLists = getListOfListOfMapsFromValueObject(value, STATIC_ENCHANTMENT_PARAMETERS_INVALID);
        if (valueListofLists == null) {
            return;
        }
        for (List<Map> listOfMaps : valueListofLists) {
            decodeStaticEnchantments(listOfMaps);
        }
    }

    private void decodeWeightValue(Object value) {
        CustomItem customItem = (CustomItem) container;
        customItem.weight = Double.valueOf(value.toString());
        if (customItem.weight < 0) {
            printReadInError(NEGATIVE_WEIGHT);
            customItem.weight = 1;
        }
    }

    private void decodeProbValue(Object value) {
        CustomItem customItem = (CustomItem) container;
        customItem.probability = Double.valueOf(value.toString());
        if (customItem.probability < 0 || customItem.probability > 1) {
            printReadInError(INVALID_PROBABILITY);
            customItem.probability = 1;
        }
    }

    private void decodeExperienceValue(Object value) {
        CustomItem customItem = (CustomItem) container;
        customItem.experienceDrop = Integer.valueOf(value.toString()); //I actually don't care if they use negative experience
    }

    private void decodeStaticEnchantments(List<Map> staticEnchantmentsInfo) {
        CustomItem customItem = (CustomItem) container;
        int level = 0;
        Enchantment enchantment = null;
        for (Map staticEnchantmentInfo : staticEnchantmentsInfo) {
            Object key = getOnlyKey(staticEnchantmentInfo); //Should only be one key per map
            String stringValue = staticEnchantmentInfo.get(key).toString();
            if (key.toString().equalsIgnoreCase("enchant")) {
                enchantment = EnchantmentWrapper.getByKey(NamespacedKey.minecraft(stringValue.toLowerCase()));
                if (enchantment == null) {
                    printReadInError(INVALID_ENCHANTMENT + stringValue);
                }
            } else if (key.toString().equalsIgnoreCase("level")) {
                level = Integer.valueOf(stringValue);
                if (level < 1) {
                    printReadInError(INVALID_ENCHANTMENT_LEVEL + stringValue);
                }
            } else {
                printReadInError(STATIC_ENCHANTMENT_PARAMETERS_INVALID);
            }
        }
        if (enchantment != null && level > 0) {
            customItem.addEnchantment(enchantment, level);
        }
    }

    private void decodeEnchantmentKey(Map map) {
        CustomItem customItem = (CustomItem) container;
        Object key = getOnlyKey(map, RANDOM_ENCHANTMENT_PARAMETERS_INVALID); //Gets "first" key from the map (throws error if more than one key)
        Object value = map.get(key); //Gets value of the key
        String mapKey = key.toString(); //Gets string name of the key
        if (mapKey.equalsIgnoreCase("upper")) {
            customItem.maxEnchantmentLevel = Integer.valueOf(value.toString());
            if (customItem.maxEnchantmentLevel < 0) {
                printReadInError(IMPROPER_ENCHANTMENT_LIMITS);
                customItem.maxEnchantmentLevel = 0;
            }
        } else if (mapKey.equalsIgnoreCase("lower")) {
            customItem.minEnchantmentLevel = Integer.valueOf(value.toString());
            if (customItem.maxEnchantmentLevel < 0) {
                printReadInError(IMPROPER_ENCHANTMENT_LIMITS);
                customItem.minEnchantmentLevel = 0;
            }
        } else if (mapKey.equalsIgnoreCase("isTreasure")) {
            customItem.isTreasure = Boolean.valueOf(value.toString());
        } else {
            printReadInError(RANDOM_ENCHANTMENT_PARAMETERS_INVALID);
        }
    }

    private void decodeDurabilityKey(Map map) {
        CustomItem customItem = (CustomItem) container;
        Object key = getOnlyKey(map, DURABILITY_PARAMETERS_INVALID); //Gets "first" key from the map (throws error if more than one key)
        Object value = map.get(key); //Gets value of the key
        String mapKey = key.toString(); //Gets string name of the key
        if (mapKey.equalsIgnoreCase("upper")) {
            customItem.maxDurabilityPortion = Double.valueOf(value.toString());
            if (customItem.maxDurabilityPortion < 0) {
                printReadInError(IMPROPER_DURABILITY_LIMITS);
                customItem.maxDurabilityPortion = 0.0;
            }
        } else if (mapKey.equalsIgnoreCase("lower")) {
            customItem.minDurabilityPortion = Double.valueOf(value.toString());
            if (customItem.minDurabilityPortion < 0) {
                printReadInError(IMPROPER_DURABILITY_LIMITS);
                customItem.minDurabilityPortion = 0.0;
            }
        } else {
            printReadInError(DURABILITY_PARAMETERS_INVALID);
        }
    }

    private void printReadInError() {
        FrpgPrint.print(IMPROPER_CONFIG + configPath);
    }

    private void printReadInError(String extraMessage) {
        FrpgPrint.print(IMPROPER_CONFIG + configPath + " (" + extraMessage + ")");
    }

    private Object getOnlyKey(Map map) {
        Object mapKey = null;
        if (map.keySet().size() > 1) {
            printReadInError();
        }
        for (Object key : map.keySet()) { //We assume the keyset is size one
            mapKey = key;
        }
        return mapKey;
    }

    private Object getOnlyKey(Map map, String extraErrorMessage) {
        Object mapKey = null;
        if (map.keySet().size() > 1) {
            printReadInError(extraErrorMessage);
        }
        for (Object key : map.keySet()) { //We assume the keyset is size one
            mapKey = key;
        }
        return mapKey;
    }

    private List<Map> getListOfMapsFromValueObject(Object value, String extraErrorMessage) {
        if ((value instanceof List)) {
            List valueList = (List) value;
            if (valueList.isEmpty()) {
                printReadInError(extraErrorMessage);
                return null;
            }
            if (!(valueList.get(0) instanceof Map)) {
                printReadInError(extraErrorMessage);
                return null;
            }
            return (List<Map>) value;
        } else {
            printReadInError(extraErrorMessage);
            return null;
        }
    }

    private List<Map> getListOfMapsFromValueObject(Object value) {
        if ((value instanceof List)) {
            List valueList = (List) value;
            if (valueList.isEmpty()) {
                printReadInError();
                return null;
            }
            for (Object mapObjet : valueList) {
                if (!(mapObjet instanceof Map)) { //One of the list objects is not a mapping
                    printReadInError();
                    return null;
                }
            }
            return (List<Map>) value;
        } else {
            printReadInError();
            return null;
        }
    }

    private List<List<Map>> getListOfListOfMapsFromValueObject(Object value,String extraErrorMessage) {
        if ((value instanceof List)) {
            List valueList = (List) value; //Object is  alist
            if (valueList.isEmpty()) { //Object is an empty list
                printReadInError(extraErrorMessage);
                return null;
            }
            for (Object listObject : valueList) { //For all (supposedly lists) in the list
                if (!(listObject instanceof List)) { //If the list object is not a list
                    printReadInError(extraErrorMessage);
                    return null;
                }
                //It's okay for this list to be empty
                for (Object mapObject : (List) listObject) {
                    if (!(mapObject instanceof Map)) { //One of the objects is not a mapping
                        printReadInError(extraErrorMessage);
                        return null;
                    }
                }
            }
            return (List<List<Map>>) value;
        } else {
            printReadInError(extraErrorMessage);
            return null;
        }
    }
}
