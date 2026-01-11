package javaapplication6;

import processing.core.PApplet;

/**
 * ShenTan (altar) used for the sealing ritual.
 */
public class ShenTan {
    private final int x;
    private final int y;
    private final int w;
    private final int h;

    public ShenTan(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public boolean intersects(Actor actor) {
        return actor.x < x + w
            && actor.x + actor.w > x
            && actor.y < y + h
            && actor.y + actor.h > y;
    }

    public void draw(PApplet app) {
        app.pushStyle();
        app.fill(120, 90, 70, 200);
        app.stroke(255, 220, 180);
        app.rect(x, y, w, h, 12);
        app.fill(255, 240, 200);
        app.textSize(12);
        app.textAlign(PApplet.CENTER, PApplet.CENTER);
        app.text("ALTAR", x + w / 2f, y + h / 2f);
        app.popStyle();
    }
}
