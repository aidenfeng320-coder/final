package javaapplication6;

import processing.core.PApplet;

public class Player {
    public float x;
    public float y;
    public float w = 26;
    public float h = 26;
    private float speed = 2.2f;
    private int hp = 100;
    private int attackCooldown = 0;
    private int dodgeTimer = 0;
    private boolean invulnerable;
    private float dirX = 1;
    private float dirY = 0;

    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;

    public Player(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setInput(boolean up, boolean down, boolean left, boolean right) {
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
    }

    public void update(Area area) {
        float nx = 0;
        float ny = 0;
        if (up) ny -= speed;
        if (down) ny += speed;
        if (left) nx -= speed;
        if (right) nx += speed;

        if (nx != 0 || ny != 0) {
            dirX = nx == 0 ? dirX : Math.signum(nx);
            dirY = ny == 0 ? dirY : Math.signum(ny);
        }

        move(area, nx, ny);

        if (attackCooldown > 0) {
            attackCooldown--;
        }
        if (dodgeTimer > 0) {
            dodgeTimer--;
            invulnerable = true;
        } else {
            invulnerable = false;
        }
    }

    private void move(Area area, float nx, float ny) {
        float nextX = x + nx;
        if (!area.collides(nextX, y, w, h)) {
            x = nextX;
        }
        float nextY = y + ny;
        if (!area.collides(x, nextY, w, h)) {
            y = nextY;
        }
    }

    public void tryAttack(Area area) {
        if (attackCooldown > 0) {
            return;
        }
        attackCooldown = 30;
        float hitX = x + dirX * 28;
        float hitY = y + dirY * 28;
        float hitW = 26;
        float hitH = 26;

        for (Enemy enemy : area.getEnemies()) {
            if (!enemy.isDead() && rectOverlap(hitX, hitY, hitW, hitH, enemy)) {
                enemy.takeDamage(15, dirX * 4, dirY * 4);
            }
        }
        BossYinCha boss = area.getBoss();
        if (boss != null && !boss.isDead() && rectOverlap(hitX, hitY, hitW, hitH, boss)) {
            boss.takeDamage(12, dirX * 4, dirY * 4);
        }
    }

    public void dodge(Area area) {
        if (dodgeTimer > 0) {
            return;
        }
        dodgeTimer = 20;
        float dashX = dirX * 40;
        float dashY = dirY * 40;
        move(area, dashX, dashY);
    }

    public void takeDamage(int amount) {
        if (invulnerable) {
            return;
        }
        hp -= amount;
        if (hp < 0) {
            hp = 0;
        }
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public int getHp() {
        return hp;
    }

    public void resetTo(float nx, float ny) {
        x = nx;
        y = ny;
        hp = 100;
    }

    public float centerX() {
        return x + w / 2f;
    }

    public float centerY() {
        return y + h / 2f;
    }

    public void draw(PApplet app) {
        app.pushStyle();
        app.fill(70, 160, 200);
        app.stroke(30, 50, 80);
        app.rect(x, y, w, h, 4);
        app.popStyle();
    }

    private boolean rectOverlap(float x, float y, float w, float h, EntityLike other) {
        return x < other.getX() + other.getW()
            && x + w > other.getX()
            && y < other.getY() + other.getH()
            && y + h > other.getY();
    }
}

interface EntityLike {
    float getX();
    float getY();
    float getW();
    float getH();
}
