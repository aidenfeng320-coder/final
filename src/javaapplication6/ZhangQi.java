package javaapplication6;

public class ZhangQi extends Enemy {
    public ZhangQi(float x, float y) {
        super(x, y);
        name = "瘴气";
        hp = 30;
        speed = 1.0f;
        dropType = 2;
    }
}
