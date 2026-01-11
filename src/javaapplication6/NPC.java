package javaapplication6;

import processing.core.PApplet;

public class NPC extends Entity {
    public static final int STATE_NORMAL = 0;
    public static final int STATE_DISGUISED = 1;
    public static final int STATE_EXPOSED = 2;
    public static final int STATE_PURIFIED = 3;

    private int state = STATE_NORMAL;
    private float speed = 1.2f;
    private float dirX = 0.6f;
    private float dirY = 0.2f;
    private int wanderTimer = 0;
    private int lightTimer = 0;
    private boolean counted;

    public NPC(float x, float y) {
        super(x, y, 22, 22);
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        if (state == STATE_DISGUISED) {
            lightTimer = 0;
        }
    }

    public boolean isPurified() {
        return state == STATE_PURIFIED;
    }

    public boolean isCounted() {
        return counted;
    }

    public void setCounted(boolean counted) {
        this.counted = counted;
    }

    public void update(TileMap map, Player player) {
        if (state == STATE_PURIFIED) {
            return;
        }

        if (state == STATE_EXPOSED) {
            fleeFrom(player, map);
        } else {
            wander(map);
        }
    }

    private void wander(TileMap map) {
        wanderTimer--;
        if (wanderTimer <= 0) {
            dirX = (float) (Math.random() * 2 - 1);
            dirY = (float) (Math.random() * 2 - 1);
            wanderTimer = 60 + (int) (Math.random() * 120);
        }

        float nx = dirX * speed;
        float ny = dirY * speed;
        move(map, nx, ny);
    }

    private void fleeFrom(Player player, TileMap map) {
        float dx = centerX() - player.centerX();
        float dy = centerY() - player.centerY();
        float len = (float) Math.sqrt(dx * dx + dy * dy);
        if (len == 0) {
            len = 1;
        }
        float nx = (dx / len) * (speed + 0.8f);
        float ny = (dy / len) * (speed + 0.8f);
        move(map, nx, ny);
    }

    private void move(TileMap map, float nx, float ny) {
        float nextX = x + nx;
        if (!map.collides(nextX, y, w, h)) {
            x = nextX;
        } else {
            dirX = -dirX;
        }
        float nextY = y + ny;
        if (!map.collides(x, nextY, w, h)) {
            y = nextY;
        } else {
            dirY = -dirY;
        }
    }

    public void applyLightDamage(boolean night, LightSource[] lights) {
        if (!night || state == STATE_NORMAL || state == STATE_PURIFIED) {
            return;
        }
        boolean inLight = false;
        for (LightSource light : lights) {
            if (light.contains(centerX(), centerY())) {
                inLight = true;
                break;
            }
        }
        if (inLight) {
            lightTimer++;
        } else if (lightTimer > 0) {
            lightTimer--;
        }
        if (state == STATE_EXPOSED && lightTimer >= 120) {
            state = STATE_PURIFIED;
        }
    }

    public void maybeExpose(Player player, boolean shadowVision) {
        if (!shadowVision || state != STATE_DISGUISED) {
            return;
        }
        float dx = centerX() - player.centerX();
        float dy = centerY() - player.centerY();
        if (dx * dx + dy * dy <= 120 * 120) {
            state = STATE_EXPOSED;
        }
    }

    public void draw(PApplet app, boolean shadowVision) {
        if (state == STATE_PURIFIED) {
            return;
        }
        app.pushStyle();
        app.stroke(30, 30, 30);
        if (state == STATE_EXPOSED) {
            app.fill(120, 40, 120);
        } else {
            app.fill(200, 180, 160);
        }
        app.rect(x, y, w, h, 4);

        if (shadowVision && state == STATE_DISGUISED) {
            app.fill(40, 0, 60, 180);
            app.rect(x + 4, y + 6, w + 4, h + 6, 4);
            app.fill(200);
            app.textSize(10);
            app.text("...", x - 2, y - 4);
        }
        app.popStyle();
    }
}
