package javaapplication6;

import java.util.List;

import processing.core.PApplet;

public abstract class Area {
    public abstract void update(PApplet app, Player player, Inventory inv, DialogueManager dialogue, GameState state);

    public abstract void render(PApplet app);

    public abstract void renderEntities(PApplet app, Player player);

    public abstract void tryInteract(Player player, Inventory inv, DialogueManager dialogue);

    public abstract List<Enemy> getEnemies();

    public abstract BossYinCha getBoss();

    public abstract void resetBoss(Player player);

    public abstract boolean isBossPromptActive();

    public abstract void handleBossPrompt(boolean accept, Inventory inv, DialogueManager dialogue);

    public abstract boolean inVillageCenter(Player player);

    public abstract boolean collides(float x, float y, float w, float h);
}
