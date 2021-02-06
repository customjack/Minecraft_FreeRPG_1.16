package mc.carlton.freerpg.customConfigContainers;

import java.util.Map;

public class CustomContainer {
    String configPath;
    private Map<String,Object> containerInformation; //We only use this in the general case

    protected static final String NO_PATH = "Unknown Config Path";

    public CustomContainer(){
        this(NO_PATH);
    }
    public CustomContainer(String configPath) {
        this(configPath,null);
    }
    public CustomContainer(String configPath, Map<String,Object> containerInformation){
        this.configPath = configPath;
        this.containerInformation = containerInformation;
    }

    public void setContainerInformation(Map<String, Object> containerInformation) {
        this.containerInformation = containerInformation;
    }

    public Map<String, Object> getContainerInformation() {
        return containerInformation;
    }
}
