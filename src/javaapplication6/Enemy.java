package javaapplication6;

import processing.core.PApplet;

public class Enemy implements EntityLike {
    protected float x;
    protected float y;
    protected float w = 24;
    protected float h = 24;
    protected int hp = 40;
    protected float speed = 1.2f;
    protected String name = "敌人";
    protected int dropType = 0;
    protected float dirX = 0.5f;
    protected float dirY = 0.2f;
    protected int wanderTimer = 0;

    public Enemy(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void update(Player player, Area area) {
        float dx = player.centerX() - (x + w / 2f);
        float dy = player.centerY() - (y + h / 2f);
        float dist = dx * dx + dy * dy;
        if (dist < 180 * 180) {
            float len = (float) Math.sqrt(dist);
            if (len == 0) {
                len = 1;
            }
            move(area, dx / len * speed, dy / len * speed);
        } else {
            wander(area);
        }
    }

    protected void wander(Area area) {
        wanderTimer--;
        if (wanderTimer <= 0) {
            dirX = (float) (Math.random() * 2 - 1);
            dirY = (float) (Math.random() * 2 - 1);
            wanderTimer = 60 + (int) (Math.random() * 120);
        }
        move(area, dirX * speed, dirY * speed);
    }

    protected void move(Area area, float nx, float ny) {
        float nextX = x + nx;
        if (!area.collides(nextX, y, w, h)) {
            x = nextX;
        }
        float nextY = y + ny;
        if (!area.collides(x, nextY, w, h)) {
            y = nextY;
        }
    }

    public void takeDamage(int amount, float knockX, float knockY) {
        hp -= amount;
        x += knockX;
        y += knockY;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public void onDeath(Inventory inv, DialogueManager dialogue) {
        if (dropType == 1) {
            inv.shanXiaoPowder += 1;
            dialogue.push("获得：山魈精粉 +1");
        } else if (dropType == 2) {
            inv.zhangQiCore += 1;
            dialogue.push("获得：瘴气核心 +1");
        } else if (dropType == 3) {
            inv.kuGuDust += 1;
            dialogue.push("获得：枯骨之尘 +1");
        }
    }

    public void draw(PApplet app) {
        app.pushStyle();
        app.fill(150, 120, 90);
        app.rect(x, y, w, h, 4);
        app.fill(40);
        app.textSize(10);
        app.text(name, x - 2, y - 4);
        app.popStyle();
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public float getW() {
        return w;
    }

    @Override
    public float getH() {
        return h;
    }
}
