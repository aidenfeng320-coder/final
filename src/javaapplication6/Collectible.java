package javaapplication6;

import processing.core.PApplet;

/**
 * Simple collectible relic drawn as a glowing circle.
 */
public class Collectible {
    private int x;
    private int y;
    private int size;
    private boolean collected;

    public Collectible(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public boolean isCollected() {
        return collected;
    }

    public float centerX() {
        return x + size / 2f;
    }

    public float centerY() {
        return y + size / 2f;
    }

    public int getSize() {
        return size;
    }

    public void collect() {
        collected = true;
    }

    public boolean intersects(Actor actor) {
        return !collected
            && actor.x < x + size
            && actor.x + actor.w > x
            && actor.y < y + size
            && actor.y + actor.h > y;
    }

    /**
     * Draw a relic using basic shapes (works without sprite assets).
     */
    public void draw(PApplet app) {
        if (collected) {
            return;
        }
        app.pushStyle();
        app.noStroke();
        app.fill(255, 230, 120);
        app.ellipse(x + size / 2f, y + size / 2f, size, size);
        app.popStyle();
    }
}
