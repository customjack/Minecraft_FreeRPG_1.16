package mc.carlton.freerpg.customConfigContainers;

public class CustomContainer {
    String configPath;
    protected static final String NO_PATH = "Unknown Config Path";

    public CustomContainer(){
        this(NO_PATH);
    }
    public CustomContainer(String configPath){
        this.configPath = configPath;
    }

}
