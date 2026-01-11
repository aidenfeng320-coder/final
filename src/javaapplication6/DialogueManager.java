package javaapplication6;

import java.util.ArrayDeque;
import java.util.Queue;

import processing.core.PApplet;

public class DialogueManager {
    private final Queue<String> queue = new ArrayDeque<>();
    private String current;
    private int timer;

    public void push(String msg) {
        if (msg == null || msg.isEmpty()) {
            return;
        }
        queue.add(msg);
        if (current == null) {
            next();
        }
    }

    private void next() {
        current = queue.poll();
        timer = 180;
    }

    public void update() {
        if (current == null) {
            return;
        }
        timer--;
        if (timer <= 0) {
            current = null;
            if (!queue.isEmpty()) {
                next();
            }
        }
    }

    public void draw(PApplet app) {
        if (current == null) {
            return;
        }
        app.pushStyle();
        app.fill(0, 180);
        app.rect(40, app.height - 120, app.width - 80, 70, 10);
        app.fill(255);
        app.textSize(16);
        app.textAlign(PApplet.LEFT, PApplet.TOP);
        app.text(current, 55, app.height - 110, app.width - 110, 60);
        app.popStyle();
    }
}
