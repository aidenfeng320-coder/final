package javaapplication6;

import processing.core.PApplet;

public class Player extends Entity {
    private float speed = 2.5f;
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;

    private boolean shadowVision;
    private int visionTimer;
    private int cooldownTimer;

    public Player(float x, float y) {
        super(x, y, 24, 24);
    }

    public void setInput(boolean up, boolean down, boolean left, boolean right) {
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
    }

    public void toggleShadowVision() {
        if (shadowVision) {
            shadowVision = false;
            cooldownTimer = 360;
        } else if (cooldownTimer <= 0) {
            shadowVision = true;
            visionTimer = 240;
        }
    }

    public void update(TileMap map) {
        float nx = 0;
        float ny = 0;
        if (up) ny -= speed;
        if (down) ny += speed;
        if (left) nx -= speed;
        if (right) nx += speed;

        move(map, nx, ny);

        if (shadowVision) {
            visionTimer--;
            if (visionTimer <= 0) {
                shadowVision = false;
                cooldownTimer = 360;
            }
        } else if (cooldownTimer > 0) {
            cooldownTimer--;
        }
    }

    private void move(TileMap map, float nx, float ny) {
        float nextX = x + nx;
        if (!map.collides(nextX, y, w, h)) {
            x = nextX;
        }
        float nextY = y + ny;
        if (!map.collides(x, nextY, w, h)) {
            y = nextY;
        }
    }

    public boolean isShadowVision() {
        return shadowVision;
    }

    public int getCooldownSeconds() {
        return cooldownTimer / 60;
    }

    public int getVisionSeconds() {
        return visionTimer / 60;
    }

    public void draw(PApplet app) {
        app.pushStyle();
        app.fill(80, 180, 220);
        app.stroke(20, 40, 60);
        app.rect(x, y, w, h, 6);
        app.popStyle();
    }
}
