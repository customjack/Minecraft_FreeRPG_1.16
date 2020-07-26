package mc.carlton.freerpg.guiEvents;

public class MaxPassiveLevels {

    public int findMaxLevel(String skillName,int index) {
        int maxLevel = 1000;
        switch (skillName) {
            case "digging":
                switch (index) {
                    case 2:
                        maxLevel = 2400;
                        break;
                    default:
                        break;
                }
                break;
            case "archery":
            case "woodcutting":
                switch (index) {
                    case 2:
                        maxLevel = 2000;
                        break;
                    default:
                        break;
                }
                break;
            case "mining":
                switch (index) {
                    case 2:
                        maxLevel = 2000;
                        break;
                    case 3:
                        maxLevel = 10000;
                        break;
                    default:
                        break;
                }
                break;
            case "farming":
            case "fishing":
                switch (index) {
                    case 2:
                    case 3:
                        maxLevel = 2000;
                        break;
                    default:
                        break;
                }
                break;
            case "beastMastery":
                switch (index) {
                    case 2:
                        maxLevel = 4000;
                        break;
                    default:
                        break;
                }
                break;
            case "swordsmanship":
                switch (index) {
                    case 2:
                        maxLevel = 5000;
                        break;
                    default:
                        break;
                }
                break;
            case "defense":
                switch (index) {
                    case 2:
                        maxLevel = 9990;
                        break;
                    case 3:
                        maxLevel = 2000;
                        break;
                    default:
                        break;
                }
                break;
            case "axeMastery":
                switch (index) {
                    case 2:
                        maxLevel = 10000;
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }

        return maxLevel;
    }
}
