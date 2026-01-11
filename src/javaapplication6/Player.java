package javaapplication6;

import processing.core.PApplet;

public class Player extends Actor {
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;
    private int worldW;
    private int worldH;
    private int speed = 3;

    public Player(PApplet app, int x, int y, SpriteSet sprites) {
        super(app, 120, 15, 5, x, y, sprites.walk, sprites.stand);
        this.w = 40;
        this.h = 40;
    }

    public void setInput(boolean up, boolean down, boolean left, boolean right) {
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
    }

    @Override
    public void update() {
        update(worldH, worldW);
    }

    public void update(int worldH, int worldW) {
        this.worldH = worldH;
        this.worldW = worldW;
        beginFrame();

        int nx = 0;
        int ny = 0;
        if (up) {
            ny -= speed;
        }
        if (down) {
            ny += speed;
        }
        if (left) {
            nx -= speed;
        }
        if (right) {
            nx += speed;
        }

        move(nx, ny);
        endFrame();

        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        if (x + w > worldW) {
            x = worldW - w;
        }
        if (y + h > worldH) {
            y = worldH - h;
        }
    }

    public void draw() {
        app.image(currentImage(), x, y, w, h);
    }
}
