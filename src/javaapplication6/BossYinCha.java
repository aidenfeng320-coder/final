package javaapplication6;

import java.util.List;
import java.util.Random;

import processing.core.PApplet;

public class BossYinCha extends Enemy {
    private int blinkTimer = 180;
    private int summonTimer = 240;
    private boolean enraged;
    private int enrageTimer;
    private int promptCooldown;
    private final int maxHp = 200;
    private final Random rng = new Random();
    private boolean finished;

    public BossYinCha(float x, float y) {
        super(x, y);
        name = "索命阴差";
        hp = maxHp;
        speed = 1.0f;
        w = 40;
        h = 40;
    }

    public void update(Player player, Area area, List<Enemy> enemies) {
        if (finished) {
            return;
        }
        blinkTimer--;
        summonTimer--;
        if (blinkTimer <= 0) {
            blinkTimer = enraged ? 120 : 180;
            blinkNear(player, area);
        }
        if (summonTimer <= 0) {
            summonTimer = enraged ? 140 : 220;
            spawnMinion(enemies);
        }
        if (enrageTimer > 0) {
            enrageTimer--;
            if (enrageTimer == 0) {
                enraged = false;
            }
        }
        if (promptCooldown > 0) {
            promptCooldown--;
        }
        super.update(player, area);
    }

    private void blinkNear(Player player, Area area) {
        float nx = player.centerX() + (rng.nextFloat() * 160 - 80);
        float ny = player.centerY() + (rng.nextFloat() * 160 - 80);
        x = clamp(nx, 40, 1560);
        y = clamp(ny, 40, 1080);
        if (area.collides(x, y, w, h)) {
            x = player.centerX() + 40;
            y = player.centerY() + 40;
        }
    }

    private void spawnMinion(List<Enemy> enemies) {
        if (rng.nextBoolean()) {
            enemies.add(new ShanXiao(x + 40, y + 20));
        } else {
            enemies.add(new ZhangQi(x - 40, y + 20));
        }
    }

    public boolean shouldPrompt(Inventory inv) {
        return hp <= maxHp * 0.2f && inv.hasLetter && !finished && promptCooldown <= 0;
    }

    public void enrage() {
        enraged = true;
        enrageTimer = 600;
        speed = 1.6f;
        promptCooldown = 600;
    }

    public void finishByLetter(DialogueManager dialogue) {
        finished = true;
        dialogue.push("执念已了，阴差亦得安魂。");
    }

    public void reset() {
        hp = maxHp;
        finished = false;
        enraged = false;
        enrageTimer = 0;
        blinkTimer = 180;
        summonTimer = 240;
        promptCooldown = 0;
    }

    public boolean isFinished() {
        return finished;
    }

    @Override
    public void draw(PApplet app) {
        if (finished) {
            return;
        }
        app.pushStyle();
        app.fill(80, 20, 100);
        app.rect(x, y, w, h, 6);
        app.fill(240);
        app.textSize(12);
        app.text(name, x - 4, y - 6);
        app.popStyle();
    }

    private float clamp(float v, float min, float max) {
        return Math.max(min, Math.min(max, v));
    }
}
