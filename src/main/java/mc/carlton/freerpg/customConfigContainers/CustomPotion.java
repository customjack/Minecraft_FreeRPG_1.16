package mc.carlton.freerpg.customConfigContainers;

import org.bukkit.Material;

import java.util.List;

public class CustomPotion extends CustomItem {
    public CustomPotion(List configItem, String configPath) {
        super(configItem, configPath);
    }
    public CustomPotion(List configItem) {
        super(configItem);
    }
    public CustomPotion(Material material) {
        super(material);
    }
}
