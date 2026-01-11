package javaapplication6;

import processing.core.PApplet;
import processing.core.PImage;

public class MySketch extends PApplet {
    private PImage bg;
    private Player player;
    private NPC npcStory;
    private NPC npcHint;
    private SpriteSet spritesPlayer;
    private SpriteSet spritesNpc1;
    private SpriteSet spritesNpc2;
    private boolean up, down, left, right;
    private Task task;

    private final String[] storyLines = {
        "天女云裳自月宫而来，她的羽衣被妖雾染黑。",
        "她说：凡间的魂灯正在熄灭，需要你找回三枚‘星砂’。",
        "请拜访村中的织女后裔与白蛇守护者，寻得线索。"
    };

    private final String[] hintLines = {
        "我在河畔听见织女低语，她提到‘鹊桥下的银梭’。",
        "传说白蛇守在古井，她最怕的是天女的铃音。",
        "按 E 与我对话，找到三枚星砂后按 P 更新任务。"
    };

    @Override
    public void settings() {
        size(1060, 740);
    }

    @Override
    public void setup() {
        frameRate(60);
        imageMode(CORNER);
        bg = loadImage("images/bg.png");

        spritesPlayer = new SpriteSet();
        spritesNpc1 = new SpriteSet();
        spritesNpc2 = new SpriteSet();

        spritesNpc1.load(this, 64, 64, "images/NPC1_Idle_full.png");
        spritesNpc2.load(this, 64, 64, "images/NPC1_Idle_full.png");
        spritesPlayer.load(this, 40, 48, "images/Character_Walk.png", "images/Character_Idle.png");

        player = new Player(this, width / 2, height / 2, spritesPlayer);
        npcStory = new NPC(this, 200, 240, spritesNpc1, storyLines);
        npcHint = new NPC(this, 720, 420, spritesNpc2, hintLines);

        task = new Task(
            "Quest: 天女的星砂",
            "寻找三枚星砂，为天女净化羽衣。",
            3
        );
    }

    @Override
    public void draw() {
        if (npcStory.isTalking() || npcHint.isTalking()) {
            player.setInput(false, false, false, false);
        } else {
            player.setInput(up, down, left, right);
        }

        player.update(height, width);
        npcStory.update();
        npcHint.update();

        if (bg != null) {
            image(bg, 0, 0, width, height);
        } else {
            background(32, 40, 60);
        }

        npcStory.draw();
        npcHint.draw();
        player.draw();

        task.draw(this);

        if (npcStory.isTalking()) {
            drawDialogBox(npcStory.currentLine());
        } else if (npcHint.isTalking()) {
            drawDialogBox(npcHint.currentLine());
        }
    }

    @Override
    public void keyPressed() {
        if (key == 'w' || key == 'W') {
            up = true;
        }
        if (key == 's' || key == 'S') {
            down = true;
        }
        if (key == 'a' || key == 'A') {
            left = true;
        }
        if (key == 'd' || key == 'D') {
            right = true;
        }
        if (key == 'e' || key == 'E') {
            if (player.intersects(npcStory)) {
                if (!npcStory.isTalking()) {
                    npcStory.startTalking();
                } else {
                    npcStory.nextTalk();
                }
            } else if (player.intersects(npcHint)) {
                if (!npcHint.isTalking()) {
                    npcHint.startTalking();
                } else {
                    npcHint.nextTalk();
                }
            }
        }
        if (key == 'p' || key == 'P') {
            task.addFound();
        }
    }

    @Override
    public void keyReleased() {
        if (key == 'w' || key == 'W') {
            up = false;
        }
        if (key == 's' || key == 'S') {
            down = false;
        }
        if (key == 'a' || key == 'A') {
            left = false;
        }
        if (key == 'd' || key == 'D') {
            right = false;
        }
    }

    private void drawDialogBox(String msg) {
        if (msg == null || msg.isEmpty()) {
            return;
        }

        int margin = 20;
        int boxH = 120;
        int boxY = height - boxH - margin;

        pushStyle();

        noStroke();
        fill(0, 180);
        rect(margin, boxY, width - margin * 2, boxH, 12);

        fill(255);
        textSize(18);
        textAlign(LEFT, TOP);

        text(msg, margin + 16, boxY + 16, width - margin * 2 - 32, boxH - 32);

        popStyle();
    }

    public static void main(String[] args) {
        PApplet.main(MySketch.class.getName());
    }
}
