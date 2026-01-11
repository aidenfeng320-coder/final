package javaapplication6;

import processing.core.PApplet;

public class MySketch extends PApplet {
    private Player player;
    private AreaChapter1 area;
    private DialogueManager dialogue;
    private QuestManager quest;
    private Inventory inventory;
    private GameState state;
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;
    private boolean bossPrompt;

    @Override
    public void settings() {
        size(960, 640);
    }

    @Override
    public void setup() {
        frameRate(60);
        player = new Player(420, 320);
        area = new AreaChapter1();
        dialogue = new DialogueManager();
        quest = new QuestManager();
        inventory = new Inventory();
        state = GameState.COLLECT;
        dialogue.push("当世界学会伪装，唯有影子记得真相。");
    }

    @Override
    public void draw() {
        player.setInput(up, down, left, right);
        area.update(this, player, inventory, dialogue, state);
        player.update(area);
        dialogue.update();

        if (player.isDead()) {
            area.resetBoss(player);
            dialogue.push("你倒下了，被送回村口。");
        }

        updateState();

        area.render(this);
        area.renderEntities(this, player);
        player.draw(this);

        drawUI();
        dialogue.draw(this);
        if (bossPrompt) {
            drawBossPrompt();
        }
    }

    private void updateState() {
        if (state == GameState.COLLECT && inventory.hasAllKeyItems()) {
            state = GameState.FARM;
            dialogue.push("物品齐全，去村外收集精粉与核心。");
        } else if (state == GameState.FARM && inventory.hasFarmDrops()) {
            state = GameState.BOSS_INTRO;
            dialogue.push("村外材料足够，回村中心。");
        } else if (state == GameState.BOSS_INTRO && area.inVillageCenter(player)) {
            area.spawnBoss();
            state = GameState.BOSS_FIGHT;
            dialogue.push("索命阴差现身！");
        }

        BossYinCha boss = area.getBoss();
        if (state == GameState.BOSS_FIGHT && boss != null && boss.isFinished()) {
            state = GameState.END;
        }
        bossPrompt = state == GameState.BOSS_FIGHT && area.isBossPromptActive();
    }

    private void drawUI() {
        fill(0, 150);
        rect(10, 10, 320, 110, 10);
        fill(255);
        textSize(14);
        textAlign(LEFT, TOP);
        text("目标：" + quest.getObjective(state, inventory), 20, 18);
        text("陈年檀香: " + (inventory.hasSandalwood ? "√" : "×"), 20, 38);
        text("破碎的信札: " + (inventory.hasLetter ? "√" : "×"), 20, 56);
        text("生锈的铁剪: " + (inventory.hasScissors ? "√" : "×"), 20, 74);
        text("山魈精粉: " + inventory.shanXiaoPowder + " / 2", 180, 38);
        text("瘴气核心: " + inventory.zhangQiCore + " / 1", 180, 56);
        text("枯骨之尘: " + inventory.kuGuDust, 180, 74);
        text("生命: " + player.getHp(), 20, 94);

        if (area.hasInteractHint(player)) {
            text("按E互动", 260, 94);
        }

        if (state == GameState.END) {
            textSize(24);
            textAlign(CENTER, CENTER);
            fill(255, 220, 160);
            text("第一章完成", width / 2f, 80);
        }
    }

    private void drawBossPrompt() {
        fill(0, 200);
        rect(width / 2f - 200, height / 2f - 60, 400, 120, 12);
        fill(255);
        textSize(16);
        textAlign(CENTER, CENTER);
        text("是否出示【破碎的信札】？ Y=是 N=否", width / 2f, height / 2f);
    }

    @Override
    public void keyPressed() {
        if (key == 'w' || key == 'W') up = true;
        if (key == 's' || key == 'S') down = true;
        if (key == 'a' || key == 'A') left = true;
        if (key == 'd' || key == 'D') right = true;
        if (key == 'j' || key == 'J') player.tryAttack(area);
        if (key == 'k' || key == 'K') player.dodge(area);
        if (key == 'e' || key == 'E') area.tryInteract(player, inventory, dialogue);

        if (bossPrompt) {
            if (key == 'y' || key == 'Y') {
                area.handleBossPrompt(true, inventory, dialogue);
            } else if (key == 'n' || key == 'N') {
                area.handleBossPrompt(false, inventory, dialogue);
            }
        }
    }

    @Override
    public void keyReleased() {
        if (key == 'w' || key == 'W') up = false;
        if (key == 's' || key == 'S') down = false;
        if (key == 'a' || key == 'A') left = false;
        if (key == 'd' || key == 'D') right = false;
    }

    public static void main(String[] args) {
        PApplet.main(MySketch.class.getName());
    }
}
