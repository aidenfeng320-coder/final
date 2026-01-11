package javaapplication6;

public class Interactable {
    public final String name;
    public final float x;
    public final float y;
    public final float radius;
    private boolean used;

    public Interactable(String name, float x, float y, float radius) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public boolean isUsed() {
        return used;
    }

    public void interact(Inventory inv, DialogueManager dialogue) {
        if (used) {
            return;
        }
        used = true;
        if ("陈年檀香".equals(name)) {
            inv.hasSandalwood = true;
        } else if ("破碎的信札".equals(name)) {
            inv.hasLetter = true;
        } else if ("生锈的铁剪".equals(name)) {
            inv.hasScissors = true;
        }
        dialogue.push("获得：" + name);
    }

    public boolean near(Player player) {
        float dx = player.centerX() - x;
        float dy = player.centerY() - y;
        return dx * dx + dy * dy <= radius * radius;
    }
}
