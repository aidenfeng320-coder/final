package javaapplication6;

import processing.core.PApplet;

/**
 * Spirit gate that opens after the relic quest is complete.
 */
public class Gate {
    private final int x;
    private final int y;
    private final int w;
    private final int h;
    private boolean opened;

    public Gate(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public boolean isOpened() {
        return opened;
    }

    public void open() {
        opened = true;
    }

    public boolean intersects(Actor actor) {
        return actor.x < x + w
            && actor.x + actor.w > x
            && actor.y < y + h
            && actor.y + actor.h > y;
    }

    public void draw(PApplet app) {
        app.pushStyle();
        if (opened) {
            app.fill(120, 200, 160, 140);
        } else {
            app.fill(60, 90, 140, 180);
        }
        app.stroke(220, 220, 255);
        app.rect(x, y, w, h, 12);
        app.popStyle();
    }
}
