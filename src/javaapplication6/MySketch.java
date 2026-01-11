package javaapplication6;

import processing.core.PApplet;
import processing.core.PImage;

public class MySketch extends PApplet {
    private PImage bg;
    private int worldW = 2560;
    private int worldH = 1920;
    private float camX = 0;
    private float camY = 0;
    private Player player;
    private SpriteSet spritesPlayer;
    private boolean up, down, left, right;

    @Override
    public void settings() {
        size(1060, 740);
    }

    @Override
    public void setup() {
        frameRate(60);
        imageMode(CORNER);
        bg = loadImage("images/bg.png");
        spritesPlayer = new SpriteSet();
        spritesPlayer.load(this, 40, 48, "images/Character_Walk.png", "images/Character_Idle.png");
        player = new Player(this, width / 2, height / 2, spritesPlayer);
    }

    @Override
    public void draw() {
        player.setInput(up, down, left, right);
        player.update(worldH, worldW);
        camX = player.x + player.w / 2f - width / 2f;
        camY = player.y + player.h / 2f - height / 2f;
        if (camX < 0) camX = 0;
        if (camX > worldW - width) camX = worldW - width;
        if (camY < 0) camY = 0;
        if (camY > worldH - height) camY = worldH - height;

        pushMatrix();
        translate(-camX, -camY);
        if (bg != null) {
            image(bg, 0, 0);
        } else {
            background(40, 60, 80);
        }
        player.draw();
        popMatrix();
    }

    @Override
    public void keyPressed() {
        if (key == 'w' || key == 'W') up = true;
        if (key == 's' || key == 'S') down = true;
        if (key == 'a' || key == 'A') left = true;
        if (key == 'd' || key == 'D') right = true;
    }

    @Override
    public void keyReleased() {
        if (key == 'w' || key == 'W') up = false;
        if (key == 's' || key == 'S') down = false;
        if (key == 'a' || key == 'A') left = false;
        if (key == 'd' || key == 'D') right = false;
    }

    private void drawDialogBox(String msg) {
        if (msg == null || msg.isEmpty()) {
            return;
        }

        int margin = 20;
        int boxH = 120;
        int boxY = height - boxH - margin;

        pushStyle();
        noStroke();
        fill(0, 180);
        rect(margin, boxY, width - margin * 2, boxH, 12);
        fill(255);
        textSize(18);
        textAlign(LEFT, TOP);
        text(msg, margin + 16, boxY + 16, width - margin * 2 - 32, boxH - 32);
        popStyle();
    }

    public static void main(String[] args) {
        PApplet.main(MySketch.class.getName());
    }
}
