package javaapplication6;

public class KuGu extends Enemy {
    public KuGu(float x, float y) {
        super(x, y);
        name = "枯骨游魂";
        hp = 35;
        speed = 1.1f;
        dropType = 3;
    }
}
