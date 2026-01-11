package javaapplication6;

import processing.core.PApplet;
import processing.core.PImage;

public class SpriteSet {
    public PImage[][] walk;
    public PImage[][] stand;

    public void load(PApplet app, int frameW, int frameH, String walkSheetPath, String standSheetPath) {
        walk = splitSheet(app.loadImage(walkSheetPath), frameW, frameH);
        stand = splitSheet(app.loadImage(standSheetPath), frameW, frameH);
    }

    public void load(PApplet app, int frameW, int frameH, String standSheetPath) {
        stand = splitSheet(app.loadImage(standSheetPath), frameW, frameH);
        walk = null;
    }

    private PImage[][] splitSheet(PImage sheet, int frameW, int frameH) {
        if (sheet == null) {
            return null;
        }
        int cols = sheet.width / frameW;
        int rows = sheet.height / frameH;
        PImage[][] frames = new PImage[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                frames[r][c] = sheet.get(c * frameW, r * frameH, frameW, frameH);
            }
        }
        return frames;
    }
}
