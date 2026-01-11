package javaapplication6;

import processing.core.PApplet;
import processing.core.PImage;

public abstract class Actor {

    protected PApplet app;
    public static final int UP = 0;
    public static final int LEFT = 1;
    public static final int DOWN = 2;
    public static final int RIGHT = 3;

    protected int hp;
    protected int attack;
    protected int defense;

    protected int x, y;
    protected int w, h;

    protected int dir = UP;
    protected boolean moving = false;

    protected PImage walk[][];
    protected PImage stand[][];
    protected int frameIndex = 0;
    protected int frameCounter = 0;
    protected int frameDelay = 6;

    public Actor(PApplet app, int hp, int attack, int defense, int x, int y, PImage walk[][], PImage stand[][]) {
        this.app = app;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.x = x;
        this.y = y;
        this.walk = walk;
        this.stand = stand;
    }

    protected void beginFrame() {
        moving = false;
    }

    protected void endFrame() {
        frameCounter++;
        if (frameCounter < frameDelay) {
            return;
        }

        frameCounter = 0;

        if (moving && walk != null && walk[dir] != null) {
            frameIndex = (frameIndex + 1) % walk[dir].length;
        } else if (stand != null && stand[dir] != null) {
            frameIndex = (frameIndex + 1) % stand[dir].length;
        }
    }

    protected PImage currentImage() {
        if (stand == null || stand[dir] == null) {
            return null;
        }

        if (moving && walk != null && walk[dir] != null && walk[dir].length > 0) {
            int idx = frameIndex % walk[dir].length;
            return walk[dir][idx];
        }

        int idx = frameIndex % stand[dir].length;
        return stand[dir][idx];
    }

    protected void move(int nx, int ny) {
        if (nx == 0 && ny == 0) {
            return;
        }

        moving = true;

        if (nx < 0) {
            dir = LEFT;
        } else if (nx > 0) {
            dir = RIGHT;
        } else if (ny < 0) {
            dir = UP;
        } else if (ny > 0) {
            dir = DOWN;
        }

        x += nx;
        y += ny;
    }

    public boolean intersects(Actor other) {
        return x < other.x + other.w && x + w > other.x && y < other.y + other.h && y + h > other.y;
    }

    public abstract void update();
}
