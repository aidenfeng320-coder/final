package javaapplication6;

public class ShanXiao extends Enemy {
    public ShanXiao(float x, float y) {
        super(x, y);
        name = "山魈";
        hp = 45;
        speed = 1.4f;
        dropType = 1;
    }
}
