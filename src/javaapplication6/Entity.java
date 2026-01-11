package javaapplication6;

public class Entity {
    protected float x;
    protected float y;
    protected float w;
    protected float h;

    public Entity(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public float centerX() {
        return x + w / 2f;
    }

    public float centerY() {
        return y + h / 2f;
    }

    public boolean intersects(Entity other) {
        return x < other.x + other.w && x + w > other.x && y < other.y + other.h && y + h > other.y;
    }
}
