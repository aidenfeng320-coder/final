package javaapplication6;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;

public class MySketch extends PApplet {
    private static final int WIDTH = 960;
    private static final int HEIGHT = 640;

    private GameController controller;
    private float camX;
    private float camY;
    private int introTimer = 240;
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;

    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    @Override
    public void setup() {
        frameRate(60);
        TileMap map = new TileMap(defaultMap());
        Player player = new Player(100, 100);

        List<NPC> npcs = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            npcs.add(new NPC(200 + i * 60, 260 + (i % 3) * 50));
        }

        LightSource[] lights = new LightSource[] {
            new LightSource(200, 200, 120),
            new LightSource(720, 240, 120),
            new LightSource(1200, 780, 140)
        };

        controller = new GameController(map, player, npcs, lights);
    }

    @Override
    public void draw() {
        controller.getPlayer().setInput(up, down, left, right);
        controller.update();

        updateCamera();

        pushMatrix();
        translate(-camX, -camY);
        controller.drawWorld(this);
        popMatrix();

        drawLighting();
        drawUI();

        if (introTimer > 0) {
            drawIntro();
            introTimer--;
        }
    }

    @Override
    public void keyPressed() {
        if (key == 'w' || key == 'W') {
            up = true;
        }
        if (key == 's' || key == 'S') {
            down = true;
        }
        if (key == 'a' || key == 'A') {
            left = true;
        }
        if (key == 'd' || key == 'D') {
            right = true;
        }
        if (key == 'e' || key == 'E') {
            controller.getPlayer().toggleShadowVision();
        }
    }

    @Override
    public void keyReleased() {
        if (key == 'w' || key == 'W') {
            up = false;
        }
        if (key == 's' || key == 'S') {
            down = false;
        }
        if (key == 'a' || key == 'A') {
            left = false;
        }
        if (key == 'd' || key == 'D') {
            right = false;
        }
    }

    private void updateCamera() {
        Player player = controller.getPlayer();
        float targetX = player.centerX() - WIDTH / 2f;
        float targetY = player.centerY() - HEIGHT / 2f;
        camX = constrain(targetX, 0, controller.getMap().getWidth() - WIDTH);
        camY = constrain(targetY, 0, controller.getMap().getHeight() - HEIGHT);
    }

    private void drawLighting() {
        if (!controller.isNight() && !controller.getPlayer().isShadowVision()) {
            return;
        }
        pushStyle();
        noStroke();
        fill(0, controller.getPlayer().isShadowVision() ? 200 : 160);
        rect(0, 0, WIDTH, HEIGHT);

        try {
            blendMode(REMOVE);
            for (LightSource light : controller.getLights()) {
                float lx = light.getX() - camX;
                float ly = light.getY() - camY;
                fill(0, 0, 0, 200);
                ellipse(lx, ly, light.getRadius() * 2, light.getRadius() * 2);
            }
            blendMode(BLEND);
        } catch (Exception ignored) {
            for (LightSource light : controller.getLights()) {
                float lx = light.getX() - camX;
                float ly = light.getY() - camY;
                fill(80, 80, 80, 120);
                ellipse(lx, ly, light.getRadius() * 2, light.getRadius() * 2);
            }
        }
        popStyle();
    }

    private void drawUI() {
        pushStyle();
        fill(0, 160);
        rect(12, 12, 260, 70, 10);
        fill(255);
        textSize(14);
        textAlign(LEFT, TOP);
        text("Time: " + controller.getDayTimerSeconds() + "s", 22, 20);
        text("Phase: " + (controller.isNight() ? "Night" : "Day"), 22, 38);
        text("Purified: " + controller.getPurifiedCount() + " / 2", 22, 56);

        Player player = controller.getPlayer();
        String vision = player.isShadowVision() ? "ON" : "OFF";
        int cd = player.isShadowVision() ? player.getVisionSeconds() : player.getCooldownSeconds();
        text("Shadow Vision: " + vision + " (" + cd + "s)", 140, 20);

        if (controller.isWin()) {
            fill(255, 230, 120);
            textSize(24);
            textAlign(CENTER, CENTER);
            text("Balance Restored", WIDTH / 2f, 60);
        }
        popStyle();
    }

    private void drawIntro() {
        pushStyle();
        fill(0, 180);
        rect(80, 220, WIDTH - 160, 80, 12);
        fill(255);
        textSize(16);
        textAlign(CENTER, CENTER);
        text("When the world learns to disguise, only shadows remember truth.", WIDTH / 2f, 260);
        popStyle();
    }

    private int[][] defaultMap() {
        int rows = 35;
        int cols = 50;
        int[][] tiles = new int[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (r == 0 || c == 0 || r == rows - 1 || c == cols - 1) {
                    tiles[r][c] = 1;
                } else if (r % 7 == 0 && c % 5 == 0) {
                    tiles[r][c] = 1;
                } else {
                    tiles[r][c] = 0;
                }
            }
        }
        return tiles;
    }

    public static void main(String[] args) {
        PApplet.main(MySketch.class.getName());
    }
}
