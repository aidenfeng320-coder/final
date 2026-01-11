package javaapplication6;

import processing.core.PApplet;

public class Task {
    private final String title;
    private final String description;
    private final int total;
    private int found;

    public Task(String title, String description, int total) {
        this.title = title;
        this.description = description;
        this.total = total;
    }

    public void addFound() {
        if (found < total) {
            found++;
        }
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
        app.popStyle();
    }
}
