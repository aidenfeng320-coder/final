package javaapplication6;

import processing.core.PApplet;
import processing.core.PImage;

public class NPC extends Actor {

    private final String[] text;
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

    @Override
    public void update() {
        beginFrame();
        endFrame();
    }

    public void draw() {
        PImage img = currentImage();
        if (img == null) {
            return;
        }
        app.image(img, x, y, w, h);
    }
}
