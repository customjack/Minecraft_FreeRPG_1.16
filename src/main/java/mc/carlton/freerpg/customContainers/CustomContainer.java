package mc.carlton.freerpg.customContainers;

import java.util.Map;

public class CustomContainer {
    public Map<String,Object> containerInformation; //We only use this in the general case

    public CustomContainer( Map<String,Object> containerInformation){
        this.containerInformation = containerInformation;
    }

    public void setContainerInformation(Map<String, Object> containerInformation) {
        this.containerInformation = containerInformation;
    }

    public Map<String, Object> getContainerInformation() {
        return containerInformation;
    }
}
