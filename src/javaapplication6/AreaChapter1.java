package javaapplication6;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import processing.core.PApplet;

public class AreaChapter1 extends Area {
    private final float worldW = 1600;
    private final float worldH = 1120;
    private final float[] villageRect = { 200, 160, 500, 360 };
    private final float[] outsideRect = { 700, 200, 760, 760 };
    private final float[] villageCenter = { 420, 320, 80, 80 };
    private final List<float[]> obstacles = new ArrayList<>();
    private final List<Interactable> interactables = new ArrayList<>();
    private final List<Enemy> enemies = new ArrayList<>();
    private final Random rng = new Random();
    private BossYinCha boss;
    private boolean bossPrompt;
    private int spawnTimer;

    public AreaChapter1() {
        obstacles.add(new float[] { 320, 260, 60, 80 });
        obstacles.add(new float[] { 260, 420, 80, 40 });
        obstacles.add(new float[] { 520, 220, 60, 60 });
        obstacles.add(new float[] { 980, 360, 120, 60 });
        obstacles.add(new float[] { 1200, 520, 100, 80 });

        interactables.add(new Interactable("陈年檀香", 260, 220, 30));
        interactables.add(new Interactable("破碎的信札", 540, 420, 30));
        interactables.add(new Interactable("生锈的铁剪", 420, 240, 30));
    }

    @Override
    public void update(PApplet app, Player player, Inventory inv, DialogueManager dialogue, GameState state) {
        if (state == GameState.FARM) {
            spawnTimer--;
            if (spawnTimer <= 0) {
                spawnTimer = 120 + rng.nextInt(120);
                spawnEnemy();
            }
        }

        for (Enemy enemy : enemies) {
            enemy.update(player, this);
        }

        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy enemy = it.next();
            if (enemy.isDead()) {
                enemy.onDeath(inv, dialogue);
                it.remove();
            }
        }

        if (boss != null && !boss.isDead()) {
            boss.update(player, this, enemies);
            if (boss.shouldPrompt(inv)) {
                bossPrompt = true;
            }
        }
    }

    @Override
    public void render(PApplet app) {
        app.pushStyle();
        app.background(140, 150, 160);
        app.fill(180, 170, 150);
        app.rect(villageRect[0], villageRect[1], villageRect[2], villageRect[3]);
        app.fill(120, 150, 120);
        app.rect(outsideRect[0], outsideRect[1], outsideRect[2], outsideRect[3]);
        app.fill(90, 90, 90);
        for (float[] ob : obstacles) {
            app.rect(ob[0], ob[1], ob[2], ob[3]);
        }
        app.noFill();
        app.stroke(200, 160, 120);
        app.rect(villageCenter[0], villageCenter[1], villageCenter[2], villageCenter[3]);
        app.popStyle();

        app.pushStyle();
        app.fill(200, 180, 120);
        for (Interactable item : interactables) {
            if (!item.isUsed()) {
                app.ellipse(item.x, item.y, 18, 18);
            }
        }
        app.popStyle();
    }

    @Override
    public void renderEntities(PApplet app, Player player) {
        for (Enemy enemy : enemies) {
            enemy.draw(app);
        }
        if (boss != null && !boss.isDead()) {
            boss.draw(app);
        }
    }

    @Override
    public void tryInteract(Player player, Inventory inv, DialogueManager dialogue) {
        for (Interactable item : interactables) {
            if (!item.isUsed() && item.near(player)) {
                item.interact(inv, dialogue);
                return;
            }
        }
    }

    public boolean hasInteractHint(Player player) {
        for (Interactable item : interactables) {
            if (!item.isUsed() && item.near(player)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Enemy> getEnemies() {
        return enemies;
    }

    @Override
    public BossYinCha getBoss() {
        return boss;
    }

    @Override
    public void resetBoss(Player player) {
        if (boss != null) {
            boss.reset();
        }
        enemies.clear();
        bossPrompt = false;
        player.resetTo(villageCenter[0] + 10, villageCenter[1] + 10);
    }

    @Override
    public boolean isBossPromptActive() {
        return bossPrompt;
    }

    @Override
    public void handleBossPrompt(boolean accept, Inventory inv, DialogueManager dialogue) {
        if (boss == null) {
            return;
        }
        if (accept) {
            boss.finishByLetter(dialogue);
            bossPrompt = false;
        } else {
            boss.enrage();
            bossPrompt = false;
        }
    }

    @Override
    public boolean inVillageCenter(Player player) {
        return rectOverlap(player.x, player.y, player.w, player.h, villageCenter);
    }

    public void spawnBoss() {
        if (boss == null) {
            boss = new BossYinCha(villageCenter[0] + 20, villageCenter[1] + 20);
        }
    }

    private void spawnEnemy() {
        float sx = outsideRect[0] + 40 + rng.nextInt((int) outsideRect[2] - 80);
        float sy = outsideRect[1] + 40 + rng.nextInt((int) outsideRect[3] - 80);
        int roll = rng.nextInt(3);
        if (roll == 0) {
            enemies.add(new ShanXiao(sx, sy));
        } else if (roll == 1) {
            enemies.add(new ZhangQi(sx, sy));
        } else {
            enemies.add(new KuGu(sx, sy));
        }
    }

    @Override
    public boolean collides(float x, float y, float w, float h) {
        if (x < 0 || y < 0 || x + w > worldW || y + h > worldH) {
            return true;
        }
        for (float[] ob : obstacles) {
            if (rectOverlap(x, y, w, h, ob)) {
                return true;
            }
        }
        return false;
    }

    private boolean rectOverlap(float x, float y, float w, float h, float[] rect) {
        return x < rect[0] + rect[2]
            && x + w > rect[0]
            && y < rect[1] + rect[3]
            && y + h > rect[1];
    }
}
