package javaapplication6;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Fixed-camera mini game inspired by the Skywoman story, mixed with Chinese legends.
 * Move, talk to NPCs, and collect relics to complete the quest.
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
    private Collectible[] relics;
    private Gate spiritGate;
    private boolean gateHintUnlocked;

    // 2D array: map of relic positions (x, y)
    private final int[][] relicMap = {
        {140, 140},
        {480, 220},
        {800, 560}
    };

    private final String[] storyLines = {
        "A star maiden fell from the sky like a drifting seed.",
        "She says the village lanterns are fading, just like in the old legends.",
        "Collect three Celestial Relics to rebuild the bridge of light."
    };

    private final String[] hintLines = {
        "The Weaver Girl left a silver shuttle by the river.",
        "The White Snake guards the ancient well—offer her a calm heart.",
        "Press E to talk. Walk over glowing relics to collect them."
    };

    private final String[] guideLines = {
        "From the Classic of Mountains and Seas: the Kunpeng swims as a fish,",
        "then rises as a bird. Follow its lesson: be patient, then be swift.",
        "Use WASD to move. Find all relics to complete the quest."
    };

    private final String[] gateLines = {
        "When the relics glow together, the Spirit Gate awakens.",
        "Stand near the gate and press E to open it.",
        "Beyond it is the blessing of Nuwa's sky-mending legend."
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
        npcStory = new NPC(this, 200, 240, spritesNpc1, storyLines);
        npcHint = new NPC(this, 720, 420, spritesNpc2, hintLines);
        npcGuide = new NPC(this, 520, 120, spritesNpc3, guideLines);

        task = new Task(
            "Quest: Celestial Relics",
            "Gather three relics to restore the sky bridge.",
            relicMap.length
        );
        // build gameplay items and attempt to load a saved progress file
        buildRelics();
        loadProgress();

        actors = new Actor[] { player, npcStory, npcHint, npcGuide };
        spiritGate = new Gate(880, 120, 120, 160);
    }

    @Override
    public void draw() {
        if (npcStory.isTalking() || npcHint.isTalking() || npcGuide.isTalking()) {
            player.setInput(false, false, false, false);
        } else {
            player.setInput(up, down, left, right);
        }

        // update all actors every frame
        updateActors();
        collectRelics();

        if (bg != null) {
            image(bg, 0, 0, width, height);
        } else {
            background(32, 40, 60);
        }

        drawRelics();
        spiritGate.draw(this);
        drawActors();

        task.draw(this);

        if (npcStory.isTalking()) {
            drawDialogBox(npcStory.currentLine());
        } else if (npcHint.isTalking()) {
            drawDialogBox(npcHint.currentLine());
        } else if (npcGuide.isTalking()) {
            drawDialogBox(npcGuide.currentLine());
        } else if (spiritGate.isOpened()) {
            drawDialogBox("The gate is open. The village is safe once more.");
        } else if (task.isComplete()) {
            drawDialogBox("The sky bridge shines again. Find the Spirit Gate.");
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
            } else if (task.isComplete() && spiritGate.intersects(player)) {
                spiritGate.open();
                task.setDescription("The gate is open. Press R to restart.");
            }
        }
        if (key == 'r' || key == 'R') {
            // restart the relic hunt
            resetRelics();
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

    /**
     * Build all relics from the 2D map.
     */
    private void buildRelics() {
        relics = new Collectible[relicMap.length];
        for (int i = 0; i < relicMap.length; i++) {
            relics[i] = new Collectible(relicMap[i][0], relicMap[i][1], 26);
        }
    }

    /**
     * Check if an actor is close to a point.
     */
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

    private void drawRelics() {
        for (Collectible relic : relics) {
            if (!relic.isCollected() && isNear(player, relic.centerX(), relic.centerY(), 80)) {
                pushStyle();
                noFill();
                stroke(255, 240, 150);
                strokeWeight(2);
                ellipse(relic.centerX(), relic.centerY(), relic.getSize() + 16, relic.getSize() + 16);
                popStyle();
            }
            relic.draw(this);
        }
    }

    private void collectRelics() {
        for (Collectible relic : relics) {
            if (relic.intersects(player) && !relic.isCollected()) {
                relic.collect();
                task.addFound();
                saveProgress();
            }
        }
        if (task.isComplete()) {
            task.setDescription("Find the Spirit Gate and press E to open it.");
            if (!gateHintUnlocked) {
                npcGuide.setText(gateLines);
                gateHintUnlocked = true;
            }
        }
    }

    private void resetRelics() {
        buildRelics();
        task = new Task(
            "Quest: Celestial Relics",
            "Gather three relics to restore the sky bridge.",
            relicMap.length
        );
        spiritGate = new Gate(880, 120, 120, 160);
        gateHintUnlocked = false;
        saveProgress();
    }

    /**
     * Load progress from a flat text file. If it does not exist, start fresh.
     */
    private void loadProgress() {
        String[] data = loadStrings("progress.txt");
        if (data == null || data.length == 0) {
            return;
        }
        try {
            int found = Integer.parseInt(data[0].trim());
            for (int i = 0; i < relics.length; i++) {
                if (i < found) {
                    relics[i].collect();
                    task.addFound();
                }
            }
            if (task.isComplete()) {
                task.setDescription("Find the Spirit Gate and press E to open it.");
                npcGuide.setText(gateLines);
                gateHintUnlocked = true;
            }
        } catch (NumberFormatException ignored) {
        }
    }

    /**
     * Save progress to a flat text file.
     */
    private void saveProgress() {
        saveStrings("progress.txt", new String[] { String.valueOf(task.getFound()) });
    }

    public static void main(String[] args) {
        PApplet.main(MySketch.class.getName());
    }
}
