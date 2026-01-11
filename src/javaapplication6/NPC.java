package javaapplication6;

import processing.core.PApplet;
import processing.core.PImage;

public class NPC extends Actor {

    private String[] text;
    private boolean talking = false;
    private int lineIndex;

    public NPC(PApplet app, int x, int y, SpriteSet sprites, String[] text) {
        super(app, 1, 0, 0, x, y, sprites.walk, sprites.stand);
        this.text = text;
        this.w = 64;
        this.h = 64;
    }

    public void startTalking() {
        talking = true;
        lineIndex = 0;
    }

    public boolean isTalking() {
        return talking;
    }

    public void nextTalk() {
        if (!talking) {
            return;
        }

        lineIndex++;
        if (lineIndex >= text.length) {
            lineIndex = 0;
            talking = false;
        }
    }

    public String currentLine() {
        if (!talking || text == null || text.length == 0) {
            return "";
        }
        return text[lineIndex];
    }

    public void setText(String[] text) {
        this.text = text;
        this.lineIndex = 0;
    }

    @Override
    public void update() {
        beginFrame();
        endFrame();
    }

    public void draw() {
        if (app == null) {
            return;
        }
        PImage img = currentImage();
        if (img != null) {
            app.image(img, x, y, w, h);
            return;
        }
        app.pushStyle();
        app.fill(255, 230, 200);
        app.stroke(90, 60, 40);
        app.rect(x, y, w, h, 6);
        app.popStyle();
    }
}
