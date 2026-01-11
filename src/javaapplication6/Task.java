package javaapplication6;

import processing.core.PApplet;

/**
 * Simple quest tracker shown on the HUD.
 */
public class Task {
    // static variable: total relics found across all tasks (demo requirement)
    public static int totalFoundAcrossRuns = 0;
    private final String title;
    private final String description;
    private final int total;
    private int found;

    // Overloaded constructor (no total provided)
    public Task(String title, String description) {
        this(title, description, 1);
    }

    public Task(String title, String description, int total) {
        this.title = title;
        this.description = description;
        this.total = total;
        this.found = 0;
    }

    public void addFound() {
        if (found < total) {
            found++;
            totalFoundAcrossRuns++;
        }
    }

    public boolean isComplete() {
        return found >= total;
    }

    public int getFound() {
        return found;
    }

    public void draw(PApplet app) {
        app.pushStyle();
        app.fill(0, 150);
        app.noStroke();
        app.rect(12, 12, 360, 88, 10);
        app.fill(255);
        app.textSize(14);
        app.textAlign(PApplet.LEFT, PApplet.TOP);
        app.text(title, 24, 22);
        app.text(description, 24, 42, 340, 50);
        app.text("Progress: " + found + " / " + total, 24, 78);
        app.text("Total Relics: " + totalFoundAcrossRuns, 200, 78);
        app.popStyle();
    }
}
