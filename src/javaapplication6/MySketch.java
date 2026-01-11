package javaapplication6;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Fixed-camera mini game inspired by the Skywoman story, mixed with Chinese legends.
 * Intro story -> gameplay -> ending.
 */
public class MySketch extends PApplet {
    private PImage bg;
    private Player player;
    private NPC npcStory;
    private NPC npcHint;
    private NPC npcGuide;
    private SpriteSet spritesPlayer;
    private SpriteSet spritesNpc1;
    private SpriteSet spritesNpc2;
    private SpriteSet spritesNpc3;
    private boolean up, down, left, right;
    private Task task;
    private Actor[] actors;
    private Collectible[] seals;
    private ShenTan altar;

    private static final int STATE_INTRO = 0;
    private static final int STATE_PLAY = 1;
    private static final int STATE_END = 2;
    private int gameState = STATE_INTRO;
    private int introIndex = 0;
    private long startMillis;
    private int timeLimitSeconds = 90;
    private boolean win;

    // 2D array: map of seal positions (x, y)
    private final int[][] sealMap = {
        {140, 160},
        {520, 200},
        {820, 560}
    };

    private final String[] introLines = {
        "A sky-born girl drifted down like a seed over the sea.",
        "She carried a bundle of stars and a promise to bring light.",
        "In this village, the ancient seals are fading.",
        "Find the three paper seals and return to the altar.",
        "Press SPACE to continue."
    };

    private final String[] storyLines = {
        "I saw the sky maiden fall near the river stones.",
        "She whispered about a broken sky bridge.",
        "Find the three paper seals to calm the storm."
    };

    private final String[] hintLines = {
        "The Weaver Girl hid a seal by the old loom.",
        "The White Snake sleeps near the well. Stay gentle.",
        "Use WASD to move. Walk over seals to collect them."
    };

    private final String[] guideLines = {
        "The Kunpeng changes from fish to bird in the mountains and seas.",
        "Be slow when searching, be swift when returning.",
        "Bring the seals back to the altar to finish the ritual."
    };

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
        spritesNpc1 = new SpriteSet();
        spritesNpc2 = new SpriteSet();
        spritesNpc3 = new SpriteSet();

        spritesNpc1.load(this, 64, 64, "images/NPC1_Idle_full.png");
        spritesNpc2.load(this, 64, 64, "images/NPC1_Idle_full.png");
        spritesNpc3.load(this, 64, 64, "images/NPC1_Idle_full.png");
        spritesPlayer.load(this, 40, 48, "images/Character_Walk.png", "images/Character_Idle.png");

        player = new Player(this, width / 2, height / 2, spritesPlayer);
        npcStory = new NPC(this, 200, 260, spritesNpc1, storyLines);
        npcHint = new NPC(this, 760, 420, spritesNpc2, hintLines);
        npcGuide = new NPC(this, 520, 120, spritesNpc3, guideLines);

        task = new Task(
            "Quest: Paper Seals",
            "Collect three seals, then return to the altar.",
            sealMap.length
        );

        buildSeals();
        altar = new ShenTan(880, 120, 120, 140);

        actors = new Actor[] { player, npcStory, npcHint, npcGuide };
        startMillis = millis();
    }

    @Override
    public void draw() {
        if (bg != null) {
            image(bg, 0, 0, width, height);
        } else {
            background(32, 40, 60);
        }

        if (gameState == STATE_INTRO) {
            drawIntro();
            return;
        }

        if (npcStory.isTalking() || npcHint.isTalking() || npcGuide.isTalking()) {
            player.setInput(false, false, false, false);
        } else {
            player.setInput(up, down, left, right);
        }

        updateActors();
        collectSeals();

        drawSeals();
        altar.draw(this);
        drawActors();

        task.draw(this);
        drawTimer();

        if (npcStory.isTalking()) {
            drawDialogBox(npcStory.currentLine());
        } else if (npcHint.isTalking()) {
            drawDialogBox(npcHint.currentLine());
        } else if (npcGuide.isTalking()) {
            drawDialogBox(npcGuide.currentLine());
        }

        if (gameState == STATE_END) {
            if (win) {
                drawDialogBox("The ritual holds. The sky bridge is calm again.");
            } else {
                drawDialogBox("The seals failed. The storm returns. Press R to retry.");
            }
        }
    }

    @Override
    public void keyPressed() {
        if (gameState == STATE_INTRO) {
            if (key == ' ') {
                introIndex++;
                if (introIndex >= introLines.length) {
                    gameState = STATE_PLAY;
                    startMillis = millis();
                }
            }
            return;
        }

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
            if (player.intersects(npcStory)) {
                if (!npcStory.isTalking()) {
                    npcStory.startTalking();
                } else {
                    npcStory.nextTalk();
                }
            } else if (player.intersects(npcHint)) {
                if (!npcHint.isTalking()) {
                    npcHint.startTalking();
                } else {
                    npcHint.nextTalk();
                }
            } else if (player.intersects(npcGuide)) {
                if (!npcGuide.isTalking()) {
                    npcGuide.startTalking();
                } else {
                    npcGuide.nextTalk();
                }
            } else if (task.isComplete() && altar.intersects(player)) {
                win = true;
                gameState = STATE_END;
            }
        }
        if (key == 'r' || key == 'R') {
            resetGame();
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

    private void drawIntro() {
        pushStyle();
        fill(0, 180);
        rect(60, 120, width - 120, height - 240, 16);
        fill(255);
        textSize(20);
        textAlign(LEFT, TOP);
        String line = introLines[Math.min(introIndex, introLines.length - 1)];
        text(line, 90, 150, width - 180, height - 300);
        popStyle();
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

    private void buildSeals() {
        seals = new Collectible[sealMap.length];
        for (int i = 0; i < sealMap.length; i++) {
            seals[i] = new Collectible(sealMap[i][0], sealMap[i][1], 26);
        }
    }

    private boolean isNear(Actor actor, float rx, float ry, float range) {
        float dx = (actor.x + actor.w / 2f) - rx;
        float dy = (actor.y + actor.h / 2f) - ry;
        return dx * dx + dy * dy <= range * range;
    }

    private void updateActors() {
        for (Actor actor : actors) {
            if (actor == player) {
                player.update(height, width);
            } else {
                actor.update();
            }
        }
    }

    private void drawActors() {
        for (Actor actor : actors) {
            if (actor instanceof Player) {
                ((Player) actor).draw();
            } else if (actor instanceof NPC) {
                ((NPC) actor).draw();
            }
        }
    }

    private void drawSeals() {
        for (Collectible seal : seals) {
            if (!seal.isCollected() && isNear(player, seal.centerX(), seal.centerY(), 80)) {
                pushStyle();
                noFill();
                stroke(255, 240, 150);
                strokeWeight(2);
                ellipse(seal.centerX(), seal.centerY(), seal.getSize() + 16, seal.getSize() + 16);
                popStyle();
            }
            seal.draw(this);
        }
    }

    private void collectSeals() {
        for (Collectible seal : seals) {
            if (seal.intersects(player) && !seal.isCollected()) {
                seal.collect();
                task.addFound();
            }
        }
        if (task.isComplete()) {
            task.setDescription("Return to the altar and press E to finish the ritual.");
        }
    }

    private void drawTimer() {
        int elapsed = (int) ((millis() - startMillis) / 1000);
        int remaining = timeLimitSeconds - elapsed;
        if (remaining <= 0 && gameState == STATE_PLAY) {
            win = false;
            gameState = STATE_END;
        }
        pushStyle();
        fill(0, 150);
        rect(width - 170, 12, 150, 40, 10);
        fill(255);
        textSize(14);
        textAlign(LEFT, TOP);
        text("Time: " + max(0, remaining), width - 158, 22);
        popStyle();
    }

    private void resetGame() {
        buildSeals();
        task = new Task(
            "Quest: Paper Seals",
            "Collect three seals, then return to the altar.",
            sealMap.length
        );
        altar = new ShenTan(880, 120, 120, 140);
        gameState = STATE_INTRO;
        introIndex = 0;
        win = false;
        startMillis = millis();
    }

    public static void main(String[] args) {
        PApplet.main(MySketch.class.getName());
    }
}
