package javaapplication6;

import processing.core.PApplet;

public class NPC {
    private final float x;
    private final float y;
    private final float radius;
    private final String line;

    public NPC(float x, float y, float radius, String line) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.line = line;
    }

    public boolean near(Player player) {
        float dx = player.centerX() - x;
        float dy = player.centerY() - y;
        return dx * dx + dy * dy <= radius * radius;
    }

    public void interact(DialogueManager dialogue) {
        dialogue.push(line);
    }

    public void draw(PApplet app) {
        app.pushStyle();
        app.fill(220, 200, 170);
        app.rect(x - 10, y - 14, 20, 28, 4);
        app.popStyle();
    }
}
