package javaapplication6;

import processing.core.PApplet;

public class LightSource {
    private final float x;
    private final float y;
    private final float radius;

    public LightSource(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getRadius() {
        return radius;
    }

    public boolean contains(float px, float py) {
        float dx = px - x;
        float dy = py - y;
        return dx * dx + dy * dy <= radius * radius;
    }

    public void draw(PApplet app) {
        app.pushStyle();
        app.noStroke();
        app.fill(255, 180, 80, 180);
        app.ellipse(x, y, 16, 16);
        app.popStyle();
    }
}
