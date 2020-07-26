package mc.carlton.freerpg.playerAndServerInfo;

public class HeldStats {
    private String pName;
    private int level;
    private int experience;

    public HeldStats(String pName, int level, int experience) {
        super();
        this.pName = pName;
        this.level = level;
        this.experience = experience;
    }
    public void set_pName(String pName) {
        this.pName = pName;
    }
    public void set_level(int level) {
        this.level = level;
    }
    public void set_experience(int experience) {
        this.experience = experience;
    }
    public String get_pName() {
        return this.pName;
    }
    public int get_level() {
        return this.level;
    }
    public int get_experience() {
        return this.experience;
    }

    @Override
    public String toString() {
        return "heldStat [username="+pName+", level="+level+", experience="+experience+"]";
    }



}
