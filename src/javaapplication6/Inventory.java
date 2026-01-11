package javaapplication6;

public class Inventory {
    public boolean hasSandalwood;
    public boolean hasLetter;
    public boolean hasScissors;

    public int shanXiaoPowder;
    public int zhangQiCore;
    public int kuGuDust;

    public boolean hasAllKeyItems() {
        return hasSandalwood && hasLetter && hasScissors;
    }

    public boolean hasFarmDrops() {
        return shanXiaoPowder >= 2 && zhangQiCore >= 1;
    }
}
